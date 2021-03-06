package com.kh.spring.project.model.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.kh.spring.common.code.Config;
import com.kh.spring.common.code.ErrorCode;
import com.kh.spring.common.code.WorkspaceType;
import com.kh.spring.common.exception.HandlableException;
import com.kh.spring.common.mail.MailSender;
import com.kh.spring.common.util.file.FileDTO;
import com.kh.spring.common.util.map.CamelMap;
import com.kh.spring.member.model.dto.Member;
import com.kh.spring.member.model.repository.MemberRepository;
import com.kh.spring.project.model.dto.Project;
import com.kh.spring.project.model.dto.ProjectMember;
import com.kh.spring.project.model.dto.ProjectRole;
import com.kh.spring.project.model.dto.Workspace;
import com.kh.spring.project.model.repository.ProjectRepository;

@Service
public class ProjectServiceImpl implements ProjectService {

   @Autowired
   ProjectRepository projectRepository;

   @Autowired
   private MailSender mailSender;

   @Autowired
   private RestTemplate http;

   @Override
   public String updateProjectInviteCode(String projectIdx) {

      String newUuid = UUID.randomUUID().toString();
      projectRepository.updateProjectInviteCode(newUuid, projectIdx);

      return newUuid;

   }

   @Override
   public Project selectProjectByIdx(String projectIdx) {

      return projectRepository.selectProjectByIdx(projectIdx);
   }

   @Override
   public List<Map<String, Object>> selectProjectMemberByProjectIdx(String projectIdx) {

      return projectRepository.selectProjectMemberByProjectIdx(projectIdx);
   }

   @Override
   public List<Map<String, Object>> selectProjectMemberRoleByProjectIdx(String projectIdx) {
      return projectRepository.selectProjectMemberRoleByProjectIdx(projectIdx);
   }

   // ?????? ?????? ??????

   @Override
   public List<Map<String, Object>> selectUserAndMemberByEmail(String email) {

      return CamelMap.changeListMap(projectRepository.selectUserAndMemberByEmail(email));

   }

   @Override
   public void inviteMemberByEmail(Member newMember, Project project) {

      Map<String, Object> defaultRole = CamelMap
            .changeMap(projectRepository.selectDefaultOfProjectRole(project.getProjectIdx()));
      String authIdx = (String) defaultRole.get("authIdx"); // ?????? idx??? ????????????

      ProjectMember newProjectMember = new ProjectMember();
      newProjectMember.setProjectIdx(project.getProjectIdx());
      newProjectMember.setUserIdx(newMember.getUserIdx());
      newProjectMember.setAuthIdx(authIdx);
      newProjectMember.setNickname(newMember.getNickname());
      newProjectMember.setProfileColor(newMember.getProfileColor());
      newProjectMember.setIsOk(0);

      projectRepository.insertProjectMember(newProjectMember);

      MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
      body.add("mailTemplate", "project-invite-mail");
      body.add("nickname", newMember.getNickname());
      body.add("projectIdx", project.getProjectIdx());
      body.add("userIdx", newMember.getUserIdx());

      // RestTemplate??? ?????? Content-type : application/json
      RequestEntity<MultiValueMap<String, String>> request = RequestEntity.post(Config.DOMAIN.DESC + "/mail")
            .accept(MediaType.APPLICATION_FORM_URLENCODED).body(body);

      String subject = project.getProName() + " ?????????????????? ????????? ???????????????.";
      String htmlTxt = http.exchange(request, String.class).getBody();
      mailSender.send(newMember.getEmail(), subject, htmlTxt);

   }

   // ????????? ?????????
   @Override
   public int inviteMemberByEmailImpl(String projectIdx, String userIdx) {

      return projectRepository.updateProjectMemberStatus(projectIdx, userIdx);

   }

   @Override
   public Project selectProjectByInviteCode(String inviteCode) {
      return projectRepository.selectProjectByInviteCode(inviteCode);

   }

   public void inviteMemberByCode(String inviteCode, Member member) {

      Project project = projectRepository.selectProjectByInviteCode(inviteCode);
      if (project == null) {
         throw new HandlableException(ErrorCode.PROJECT_URL_ERROR);
      }

      List<Map<String, Object>> projectMemberList = projectRepository
            .selectProjectMemberByProjectIdx(project.getProjectIdx());
      System.out.println(projectMemberList);
      for (Map<String, Object> pm : projectMemberList) {

         if (pm.get("USER_IDX").equals(member.getUserIdx())) {
            System.out.println("?????? ???????????? ??????????????????.");
            return;

         }

      }

      Map<String, Object> defaultRole = CamelMap
            .changeMap(projectRepository.selectDefaultOfProjectRole(project.getProjectIdx()));
      String authIdx = (String) defaultRole.get("authIdx"); // ?????? idx??? ????????????

      ProjectMember projectMember = new ProjectMember();
      projectMember.setProjectIdx(project.getProjectIdx());
      projectMember.setUserIdx(member.getUserIdx());
      projectMember.setAuthIdx(authIdx);
      projectMember.setNickname(member.getNickname());
      projectMember.setProfileColor(member.getProfileColor());
      projectMember.setIsOk(1);

      projectRepository.insertProjectMember(projectMember);

   }

   public void deleteProjectMember(Map<String, String> map) {

      projectRepository.deleteProjectMember(map);

   };

   public void updateProjectMemberAuth(Map<String, String> map) {

      projectRepository.updateProjectMemberAuth(map);

   };

   // ?????? ???????????? ???

   // ?????? ?????? ??????
   @Override
   public List<ProjectRole> selectProjectRoleByIdx(String projectIdx) {

      return projectRepository.selectProjectRoleByIdx(projectIdx);
   }
   
   @Override
   public void updateRoleByPrevAuthName(Map<String, String> map) {
      projectRepository.updateRoleByPrevAuthName(map);
      
   }


   @Override
   public void insertNewRole(ProjectRole role) {
      projectRepository.insertNewRole(role);

   }

   @Override
   public void deleteRoleByAuthIdx(String authIdx) {
      projectRepository.deleteRoleByAuthIdx(authIdx);

   }

   @Override
   public void deleteRoleByProjectIdxAndAuthName(ProjectRole role) {
      projectRepository.deleteRoleByProjectIdxAndAuthName(role);
   }
   
   //11/17 ??????
   @Override
   public String updateProjectByProjectIdx(Map<String, String> project) {
      System.out.println("service ::" + project);
      if(project.get("nameState").equals("update") || project.get("descriptionState").equals("update") ) {
         projectRepository.updateProjectByProjectIdx(project);
         return "??????";
      }else {
         return "????????? ??????.";      
         }
   }
   //11/18 ??????
   @Override
   public void updateIsDelProjectByProjectIdx(String projectIdx) {
      projectRepository.updateIsDelProjectByProjectIdx(projectIdx);
   }
   
   @Override
   public int projectIsDel(String projectIdx) {
      
      
      return projectRepository.projectIsDel(projectIdx);
   }
   
   @Override
   public ProjectRole selectProjectRoleByProjectIdxAndUserIdx(String projectIdx, String userIdx) {
      return projectRepository.selectProjectRoleByProjectIdxAndUserIdx(projectIdx,userIdx);
   }


   // ?????? ?????? ???
////////////????????? ????????? ?????? ??????
   @Override
   public List<Project> selectProjectByUserIdx(String userIdx) {

      return projectRepository.selectProjectByUserIdx(userIdx);
   }

   @Override
   public int insertProject(String proName, String proDescription, String inviteCode, String userIdx) {
      Project project = new Project();
      project.setProName(proName);
      project.setProDescription(proDescription);
      project.setInviteCode(inviteCode);
      int res = projectRepository.insertProject(project);
      projectRepository.insertRole();
      projectRepository.insertAdmin(userIdx);
      
      
      System.out.println("????????? : ?????? ?????????" + project.getProjectIdx());
      return res;
      
      
   }

   @Override
   public List<String> selectProjectIdxByUserIdx(String userIdx) {
      return projectRepository.selectProjectIdxByUserIdx(userIdx);
   }

   @Override
   public List<Project> selectProjectByProjectIdx(String projectIdx) {
      return projectRepository.selectProjectByProjectIdx(projectIdx);
   }

   @Override
   public Project selectProjectExist(String projectIdx) {
      return projectRepository.selectProjectExist(projectIdx);
   }

   @Override
   public List<Workspace> selectWorkspaceByProjectIdx(String projectIdx) {
      return projectRepository.selectWorkspaceByProjectIdx(projectIdx);
   }


   //11??? 24??? ?????? ??????
   @Override
   public void settingWorkspace(List<Map<String, String>> workspaceList, String projectIdx) {
      int sort = 1;
      System.out.println("projectIdx" + projectIdx);
      System.out.println("workspaceList"+workspaceList);
      for (Map<String, String> map : workspaceList) {
         
         String wsIdx = map.get("workWsIdx");
         String wsType = map.get("workOption");
         String wsName = map.get("workWrite");
         String wsState = map.get("workState");
         
         System.out.println("wsIdx : " + wsIdx);
         System.out.println("wsType : " + wsType);
         System.out.println("wsName : " +wsName);
         System.out.println("wsState : " +wsState);
         switch(wsType) {
            case "??????" :
               wsType = "ME";
               break;
            case "?????????" :
               wsType = "LD";
               break;
            case "??????" :
               wsType = "CH";
               break;
            case "?????????" :
            	wsType = "CL";
            	break;
            case "?????????" :
               wsType = "BO";
         }
         
         if (wsState.equals("none")) {// ????????? ????????? ?????????, ??????. (UPDATE)
            System.out.println("???????????? ???????????? ???");
               projectRepository.updateWorkspace(wsIdx, wsName,sort);
               sort++;
            } else if (wsState.equals("hide")) {// ???????????? hide?????? ?????????, (DELETE)
               System.out.println("???????????? ???????????? ???");
               projectRepository.deleteWorkspace(wsIdx);
            } else if (wsState.equals("insert")) {// ???????????? ?????? ???????????? ????????? (INSERT)
               System.out.println("???????????? ???????????? ???");
               projectRepository.insertWorkspace(wsIdx,wsType, wsName, sort, projectIdx);
               sort++;
            }
         }
         // ?????? data ?????? ??????
         projectRepository.deleteNonWorkspace(sort);
         // ?????? wsState??? none?????? ??????
      }

      @Override
      public List<Map<String, Object>> selectWorkspaceListByProjectIdx(String projectIdx) {

         return projectRepository.selectWorkspaceListByProjectIdx(projectIdx);
      }

//????????? ????????? ?????? ???

/////??????+??????
      @Override
      public List<Map<String, String>> selectProjectNickname(String projectIdx, String userIdx) {
         return projectRepository.selectProjectNickname(projectIdx, userIdx);
      }
      
      //???????????? ??????

	
	@Override
	public int updateMemberByNickname(Member member,String projectIdx) {
		// TODO Auto-generated method stub
		return projectRepository.updateMemberByNickname(member,projectIdx);
	}

	@Override
	public FileDTO selectProfileImgFilebyMemberIdx(Member dummyMember) {
		// TODO Auto-generated method stub
		return projectRepository.selectProfileImgFilebyMemberIdx(dummyMember);
	}


	@Override
	public Member selectProjectMemberByUserIdx(String userIdx, String projectIdx) {
		// TODO Auto-generated method stub
		return projectRepository.selectProjectMemberByUserIdx(userIdx,projectIdx);
	}

	@Override
	public int insertProfileImg(FileDTO fileUploaded, String userIdx, String projectIdx) {
		// TODO Auto-generated method stub
		return projectRepository.insertProfileImg(fileUploaded,userIdx,projectIdx);
	}

	@Override
	public int updateMemberByProfileColor(Member tempMember, String projectIdx) {
		// TODO Auto-generated method stub
		return projectRepository.updateMemberByProfileColor(tempMember,projectIdx);
	}

	@Override
	public int updateProjectIsLeave(Member member, String projectIdx) {
		// TODO Auto-generated method stub
		return projectRepository.updateProjectIsLeave(member,projectIdx);
	}



	
     
	
   }