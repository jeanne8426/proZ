package com.kh.spring.project.controller;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.dto.Board;
import com.kh.spring.board.model.dto.Post;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.common.code.ErrorCode;
import com.kh.spring.common.code.WorkspaceType;
import com.kh.spring.common.exception.HandlableException;
import com.kh.spring.common.util.file.FileDTO;
import com.kh.spring.common.util.file.FileUtil;
import com.kh.spring.common.util.json.JsonMaker;
import com.kh.spring.common.util.map.CamelMap;
import com.kh.spring.common.validator.ValidateResult;
import com.kh.spring.member.model.dto.Member;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.validator.MypageForm;
import com.kh.spring.memo.model.dto.Memo;
import com.kh.spring.memo.model.service.MemoService;
import com.kh.spring.project.model.dto.Project;
import com.kh.spring.project.model.dto.ProjectMember;
import com.kh.spring.project.model.dto.ProjectRole;
import com.kh.spring.project.model.dto.Workspace;
import com.kh.spring.project.model.service.ProjectService;

@Controller
@RequestMapping("project")
public class ProjectController {

   @Autowired
   ProjectService projectService;

   @Autowired
   MemberService memberSerivce;
   
   @Autowired
   BoardService boardService;

   //????????????
   @Autowired
   MemoService memoService;
   //???????????? ???
   Logger logger = LoggerFactory.getLogger(this.getClass());

   @GetMapping("welcome")
   public void welcome() {
   };

   // =======?????? ??????start 11??? 15???=====================

   @GetMapping("setting/member-management/{projectIdx}")
   public String memberManagement(Model model, @PathVariable String projectIdx) {

      // ????????????Idx??? ???????????? ????????????

      Project project = projectService.selectProjectByIdx(projectIdx);
      // ?????? ??????????????? ??????????
      if (project == null) {

         throw new HandlableException(ErrorCode.PROJECT_URL_ERROR);

      }

      // ?????? ?????? ????????????
      List<ProjectRole> projectRoleList = projectService.selectProjectRoleByIdx(projectIdx);
      // ?????? ????????? ????????????
      List<Map<String, Object>> projectMemberList = projectService.selectProjectMemberRoleByProjectIdx(projectIdx);

      model.addAttribute("projectRole", projectRoleList);
      model.addAttribute("projectMember", CamelMap.changeListMap(projectMemberList));

      return "project/setting/member-management";

   }

   // ???????????? ????????? ??????
   @GetMapping("invite/{inviteCode}")
   public String memberinviteByCode(@PathVariable String inviteCode,
         @SessionAttribute(required = false, name = "authentication") Member member) {

      if (member == null) {
         throw new HandlableException(ErrorCode.NEED_LOGIN);

      }

      Project project = projectService.selectProjectByInviteCode(inviteCode);

      projectService.inviteMemberByCode(inviteCode, member);

      if (project == null) {

         throw new HandlableException(ErrorCode.PROJECT_URL_ERROR);

      }

      return "redirect:/";
   }

   @PostMapping("reset-invite-link")
   @ResponseBody
   public String resetInviteLink(@RequestBody Project project) {

      logger.debug("????????????:" + project.toString());
      String newUuid = projectService.updateProjectInviteCode(project.getProjectIdx());
      System.out.println(newUuid);

      return newUuid;

   }

   @PostMapping(value = "invite-member", produces = "application/text; charset=UTF-8")
   @ResponseBody
   public String inviteMember(@RequestBody Map<String, String> map, HttpSession session) {
      logger.debug(map.toString());

      List<Map<String, Object>> memberList = projectService.selectUserAndMemberByEmail(map.get("email"));
      System.out.println("???????????????: " + memberList);

      // ?????? ????????? ??????
      if (memberList.size() == 0) {
         return "no-member";
      }

      // ????????? ??????????????? ?????? ?????? ??? ??????
      for (Map<String, Object> member : memberList) {
         if (map.get("projectIdx").equals(member.get("projectIdx"))) {
            return "member-exists";
         }
      }

      Member newMember = memberSerivce.selectMemberByEmail(map.get("email"));
      Project project = projectService.selectProjectByIdx(map.get("projectIdx"));

      projectService.inviteMemberByEmail(newMember, project);

      // projectService.insertProjectMember(newMember , project);

      logger.debug(memberList.toString());

      return JsonMaker.json(newMember);
   }

   @GetMapping("join-by-email/{userIdx}/{projectIdx}")
   public String JoinByEmail(@PathVariable String projectIdx, @PathVariable String userIdx) {

      int res = projectService.inviteMemberByEmailImpl(projectIdx, userIdx);

      if (res == 0) {
         throw new HandlableException(ErrorCode.PROJECT_INVITATION_REJECTED);
      }

      return "redirect:/";
   }

   @GetMapping("fcm-test")
   public void fcmText() {

   }

   // ?????? ??????
   @PostMapping("exile")
   @ResponseBody
   public String memberExile(@RequestBody Map<String, String> map) {

      projectService.deleteProjectMember(map);

      return "complete";

   }

   // ?????? ?????? ??????
   @PostMapping("invite-cancel")
   @ResponseBody
   public String inviteCancel(@RequestBody Map<String, String> map) {

      projectService.deleteProjectMember(map);

      return "complete";
   };

   // ?????? ?????? ??????2
   @PostMapping("invite-cancel-by-userIdx")
   @ResponseBody
   public String inviteCancelByUserIdx(@RequestBody Map<String, String> map) {

      projectService.deleteProjectMember(map);

      return "complete";
   };

   // ????????? ??????????????? ????????? ???
   @PostMapping("change-member-auth")
   @ResponseBody
   public String changeMemberAuth(@RequestBody Map<String, String> map) {

      projectService.updateProjectMemberAuth(map);

      return "complete";
   };

   // =

   // ==========================================?????? ?????? end=====================

   // ============================================?????? ??????
   // start=================================================================

   @GetMapping("setting/role-management/{projectIdx}")
   public String settingRoleManagement(Model model, @PathVariable String projectIdx,
         @SessionAttribute(value = "authentication") Member member) {

      List<ProjectRole> roleList = projectService.selectProjectRoleByIdx(projectIdx);
      model.addAttribute("roleList", roleList);
      model.addAttribute("roleCnt", roleList.size());

      return "project/setting/role-management";

   };

   @PostMapping("setting/role-management/{projectIdx}")
   public String RoleManagementSettingSave(Model model, @PathVariable String projectIdx,
         @RequestBody List<Map<String, String>> roleList) {

      System.out.println(roleList.toString());

      for (Map<String, String> map : roleList) {
         ProjectRole role = new ProjectRole();
         role.setProjectIdx(projectIdx);
         role.setAuthName(map.get("authName"));
         role.setProjectAuth(Integer.parseInt(map.get("projectAuth")));
         role.setCreateAuth(Integer.parseInt(map.get("createAuth")));
         role.setMemberAuth(Integer.parseInt(map.get("memberAuth")));
         role.setAuthIdx(map.get("authIdx"));
         System.out.println(map.get("prevAuthName"));
         System.out.println(role.toString());

         if (map.get("state").equals("none")) {
            System.out.println("?????????");

         } else if (map.get("state").equals("update")) {
            System.out.println("??????");
            projectService.updateRoleByPrevAuthName(map);

         } else if (map.get("state").equals("new")) {
            System.out.println("???????????????");
            projectService.insertNewRole(role);

         } else if (map.get("state").equals("delete")) {
            System.out.println("?????????");

            if (role.getAuthIdx().equals("")) {
               projectService.deleteRoleByProjectIdxAndAuthName(role);
            } else {
               projectService.deleteRoleByAuthIdx(role.getAuthIdx());
            }

         }

      }

      return "?????? ???????";

   }

   // 11/17 ????????????

   @GetMapping("setting/project-setting/{projectIdx}")
   public String settingProject(Model model, @PathVariable String projectIdx,
         @SessionAttribute(value = "authentication") Member member) {

        Project project =projectService.selectProjectExist(projectIdx);
         System.out.println("controller : " + projectIdx);
        

        model.addAttribute("project", project);

      return "project/setting/setting-project";
   }

   @PostMapping("setting/project-setting/{projectIdx}")
   public String projectSettingSave(Model model, @PathVariable String projectIdx,
         @RequestBody Map<String, String> project) {
      System.out.println(project);
      String res = projectService.updateProjectByProjectIdx(project);

      System.out.println(res);

      return "????????????";
   }
   
   //11-18??????
   @PostMapping("setting/project-delete/{projectIdx}")
   public void projectDelete(Model model, @PathVariable String projectIdx) {
      System.out.println("delete projectIdx : " + projectIdx);
      System.out.println("is del : " + projectService.projectIsDel(projectIdx));
      projectService.updateIsDelProjectByProjectIdx(projectIdx);
      
   }

   // ============================================?????? ??????
   // end=================================================================

   // ========================================?????? ??????=========================

   @GetMapping("project-list")
   public String enterProjectList(@SessionAttribute(required = false, name = "authentication") Member member,
         Model model, HttpSession session) {

      if (member == null) {
         return "redirect:/"; // ?????? ?????? ??????
      }

      // member??? ?????? ???????????? list ????????? ?????????
      String userIdx = member.getUserIdx();

      // member??? ?????? ???????????? list (11???16???)
      List<Project> projectList = new ArrayList<Project>();
      projectList = projectService.selectProjectByUserIdx(userIdx);

      model.addAttribute(projectList);

      return "project/project-list";
   }

   @PostMapping("project-list")
   @ResponseBody
   public Project createProject(@RequestBody Project project, @SessionAttribute("authentication") Member member,
         HttpSession session) {
      System.out.println(member.toString());
      System.out.println("project-list ?????? ??????????");

      logger.debug(project.getProName());
      logger.debug(project.getProDescription());

      String proName = project.getProName();
      String proDescription = project.getProDescription();
      String userIdx = member.getUserIdx();

      System.out.println("userIdx : " + userIdx); 

      // ????????? ??????????????? ???????????? ??????
      String inviteCode = UUID.randomUUID().toString();

      // ????????? ???????????? ??????
      int res = projectService.insertProject(proName, proDescription, inviteCode, userIdx);

      // ????????? ???????????? column
      // ??????????????? ????????? ..???! ????????? ?????? ?????????.
      // jsp?????? return??? ????????? ??????.
      // ?????? ???????????? ?????? ???????????? ????????? ??????
      return null;
   };

   @PostMapping("{projectIdx}") // ????????? ????????? get, ????????? post
   public String enterProjectMain(@PathVariable String projectIdx, HttpServletResponse response) throws Exception {

      return "project/project-main";
   };

   // ???????????? ????????? ?????? ?????????????
   @GetMapping("{projectIdx}")
   public String enterProjectMain(@PathVariable String projectIdx, @SessionAttribute("authentication") Member member,
         HttpSession session, Model model) {      
      
      //??????????????? ?????? ??????????????????
      List<Map<String,Object>> workspace = new ArrayList<Map<String,Object>>();
      workspace = CamelMap.changeListMap(projectService.selectWorkspaceListByProjectIdx(projectIdx));
      
      model.addAttribute("workspace", workspace);
      
   
      ////////////????????? ????????? ??????(main?????? ????????? ???)/////////
      	System.out.println("workspace : " + workspace);
      	
      	List<Memo> mainMemoList = memoService.selectMemoByTop(projectIdx);
      
		
        System.out.println("mainMemoList ??? ????????????? :"+mainMemoList);
        model.addAttribute("mainMemoList", mainMemoList);
        
           //////////////////?????? ?????? ?????? 12??? 1???////////////////////////////////
        List<Post> postList = boardService.selectBoardByTop(projectIdx);
    	 System.out.println("boardList ??? ????????????? :"+postList);
    	 model.addAttribute("postList", postList);
    	
        
         ///?????? ??????
         String userIdx = member.getUserIdx();
         System.out.println(userIdx);
         System.out.println(projectIdx);
         List<Map<String,String>> projectMemberList = new ArrayList<Map<String,String>>();
         projectMemberList = projectService.selectProjectNickname(projectIdx,userIdx);
         System.out.println("projectMemberList :"+projectMemberList.toString());      
         
         String nickname = projectMemberList.get(0).get("NICKNAME");
         System.out.println(nickname);
         String authname = projectMemberList.get(0).get("AUTH_NAME");
         System.out.println(authname);
         
         model.addAttribute("nickname",nickname);
         model.addAttribute("authname",authname);
         
      
      return "project/project-main"; 
   }
   
   ///////?????? 11??? 19??? ?????????????????? ??????
   
   @GetMapping("setting/workspace-management/{projectIdx}")
   public String settingWorkspace(Model model, @PathVariable String projectIdx,
         @SessionAttribute(value = "authentication") Member member) {

      System.out.println("???????????? ?????? ?????????????" + projectIdx);
      
      List<Workspace> workspaceList = projectService.selectWorkspaceByProjectIdx(projectIdx);
      
      System.out.println(workspaceList);
      model.addAttribute(workspaceList);
      
      return "project/setting/workspace-management";
   }
   
   @PostMapping("setting/workspace-management/{projectIdx}")
   @ResponseBody
   public void updateWorkspace(@RequestBody List<Map<String, String>> workspaceList,
                         @PathVariable String projectIdx) {
      
      projectService.settingWorkspace(workspaceList, projectIdx);
      
   }

   // ========================================?????? ?????? ???=========================
  
   
   //????????????
   @GetMapping("project-profile/{projectIdx}") 
	public String mypage( HttpSession session,
			Model model, @PathVariable String projectIdx
			,@SessionAttribute(value = "authentication") Member member
			) {
	   String userIdx = member.getUserIdx();
	   member = projectService.selectProjectMemberByUserIdx(projectIdx,userIdx);
		logger.debug(member.toString());
		

		//????????? ?????? ??????
		FileDTO fileDTO = projectService.selectProfileImgFilebyMemberIdx(member);
		System.out.println("userIdx :"+userIdx);
		System.out.println("projectIdx :"+projectIdx);
		
		
		if(fileDTO == null) {
			System.out.println("1. fileDTO ?????? ????????????");
			session.setAttribute("profileImg", "person.png");
		} else {
			session.setAttribute("profileImg", fileDTO.getSavePath()+fileDTO.getRenameFileName());
		}
		model.addAttribute("member",member);
		// Model??? error ?????? ?????? 
		model.addAttribute(new MypageForm()).addAttribute("error",new ValidateResult().getError());
		return "project/project-profile"; 
	}
   
   @PostMapping("profile/update/Color/{projectIdx}")
	@ResponseBody 
	public String changeProfileColor(@Validated MypageForm mypageForm
			,Errors errors 
			,HttpSession session, @PathVariable String projectIdx) {
		
		try {
			//1) ??? Validate ??????
			if(errors.hasErrors()) {
				return "failed"; 
			}
			//2) ??? ??????, SESSION_Member??? ?????? DB??????, ?????? SESSION update 
			String profileColor = "#" + mypageForm.getProfileColor();
			Member tempMember = new Member();
			Member sessionMember = (Member) session.getAttribute("authentication");
			
			tempMember.setProfileColor(profileColor); 
			tempMember.setUserIdx(sessionMember.getUserIdx());
			
			int res = projectService.updateMemberByProfileColor(tempMember,projectIdx);
			
			
		} catch(Exception e) {
			e.printStackTrace();
			return "failed";
		}
			//3) color??? ?????? 
			return "#" + mypageForm.getProfileColor(); 
	}

   @PostMapping("profile/update/Img/{projectIdx}")
	@ResponseBody
	public String changeProfileImg(@RequestParam List<MultipartFile> files
			,HttpSession session,  @PathVariable String projectIdx) {
		
		//1) ?????? ?????? ??? DB?????? 
		FileUtil fileUtil = new FileUtil(); 
		FileDTO fileUploaded = fileUtil.fileUpload(files.get(0));
	
		try {
			Member member = (Member) session.getAttribute("authentication");
			System.out.println(member.getUserIdx());
			int res = projectService.insertProfileImg(fileUploaded, member.getUserIdx(),projectIdx); 
		} catch(Exception e) {
			e.printStackTrace();
			
		}
		//2) profileImg??? join??? ?????? ??????, session ???????????? ?????????
		logger.debug(fileUploaded.getSavePath());
		logger.debug(fileUploaded.getRenameFileName());
		return fileUploaded.getSavePath() +fileUploaded.getRenameFileName(); 
	}
   
  
	
	@PostMapping(value= "profile/update/Nickname/{projectIdx}", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String changeProfileNickname(@Validated MypageForm mypageForm
			,Errors errors
			,HttpSession session, @PathVariable String projectIdx) {
		
		try {
			//1) ??? validate ?????? 
			if(errors.hasErrors()) {
				return "failed";
			}
			//2) ????????? ??????, SESSION_Member??? ?????? DB??????, ?????? SESSION update
			String nickname = mypageForm.getNickname();
			Member member = new Member();
			Member sessionMember =(Member) session.getAttribute("authentication"); 
			member.setNickname(nickname);
			member.setUserIdx(sessionMember.getUserIdx());
			
		
			int res = projectService.updateMemberByNickname(member,projectIdx);

		       
		} catch(Exception e) {
			e.printStackTrace(); 
			return "failed";
		}
			//3) nickname ?????? 
			return mypageForm.getNickname(); 
	}
	
	@PostMapping("profile/update/isLeave/{projectIdx}")
	@ResponseBody
	public String changeMemberIsleave(HttpSession session,@PathVariable String projectIdx) {
			
		//1. session?????? member??????, isLeave ?????? 
		try {
			Member member = new Member();
			Member sessionMember =(Member) session.getAttribute("authentication");  
			member.setUserIdx(sessionMember.getUserIdx());
			int res = projectService.updateProjectIsLeave(member,projectIdx); 
			
			
		} catch(Exception e) {
			e.printStackTrace();
			return "failed"; 
		}
		//2. return "success"
		return "success"; 
	}
}