<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   
<!DOCTYPE html>
<html lang="ko">

<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
    <title>Document</title>
    <script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" ></script>
    <script type="text/javascript" src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" ></script>
    <link type="text/css" rel="stylesheet" href="/resources/js/modal/modal.js">
	
    <script src="https://cdn.jsdelivr.net/npm/treeviz@2.3.0/dist/index.min.js"></script>

    <style type="text/css">
       html,
        body {

            width: 100%;
            height: 100%;
            min-width: 1000px;
            min-height: 800px;
        }

        .wrap {
            height: 100%;
            display: flex;
            flex-direction: column;

        }

        header {
            height: 60px;
            background: rgb(143, 122, 229);
            background: radial-gradient(circle, rgba(143, 122, 229, 1) 0%, rgba(241, 112, 170, 1) 100%);

        }

        .con {

            height: 100%;
            display: flex;
            justify-content: space-between;

        }

        nav {
            background: rgb(19, 23, 34);
            background: linear-gradient(0deg, rgba(19, 23, 34, 1) 0%, rgba(26, 17, 47, 1) 22%, rgba(26, 30, 41, 1) 83%, rgba(19, 23, 34, 1) 100%);


            width: 220px;
            height: 100%;
            flex-shrink: 0;

        }

        section {

            flex: 1;
            width: 100%;
            height:100%;
            background: RGB(245, 246, 247);
            display: flex;
            justify-content: center;
            overflow-y: auto;
            min-width: 1300px;
            overflow-x: hidden;
        }


        aside {

            background: rgb(19, 23, 34);
            background: linear-gradient(0deg, rgba(19, 23, 34, 1) 0%, rgba(26, 17, 47, 1) 22%, rgba(26, 30, 41, 1) 83%, rgba(19, 23, 34, 1) 100%);

            height: 100%;
            width: 200px;
            flex-shrink: 0;

        }



        /*?????? ??????////////////////////////////////////////////////////////////*/

        .section-wrap {
            margin: 0 50px;
            border: solid red(128, 123, 123);
            height: auto;
            width:100%;
            display: flex;
            flex-direction: column;
            align-items: center;

        }

        .roadmap-tit {
            font-size: 25px;
            margin: 20px 0px;
            font-weight: 900;
            width: 1200px;

        }

        .content-wrap {
            width: 1200px;
            height:90%;
            padding-bottom: 20px;
            display: flex;
            flex-direction: column;
        }

        .btnarea{
            width:100%;
            height:8%;
            display:flex;
            justify-content: flex-end;
            align-items:center;
        }

        .address{
            display:flex;
            flex:1;
            align-items: center;
        }
        
        .addresscon{
        	width:50%;
        }

        #gitarea{
            width:70%;
            height:80%;
            display: flex;
            flex: 1;
            justify-content: space-between;
            background-color: RGB(227, 228, 231);
            padding:5px;
            border-radius: 5px;
        }

        #gitbtn{
            display:flex;
            align-items: center;
            width:13%;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        #modifinishbtn, #onedeps, #modifybtn, #allview, #resetbtn {
            width:10%;
            height:80%;
            display: flex;
            justify-content: center;
            align-items: center;
            margin-left:10px;
        }
        
        #resetbtn{
        	font-size:13px;
        }

     

        .gitinput {
            width:28%;
            margin-right: 10px;

        }

        .branchinput{
            width:12%;
        }


        #ignoreinput{
            width:42%;
            border:solid 1px #999;
            overflow-x:auto;
            overflow-y:hidden;
            display: flex;
            align-items: center;
            padding:3px;
            border-radius: 5px;
            
        }

        #ignoreinput::-webkit-scrollbar {
            width: 3px;
            height: 1px;
        }
        #ignoreinput::-webkit-scrollbar-thumb {
            background-color: #000000;
        }
        #ignoreinput::-webkit-scrollbar-track {
            background-color: grey;
        }

        .ignoreele{
            width:70px;
            height:100%;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 13px;
            margin-right:5px;
        }

        .addIgnore{
            width:30px;
            height:100%;
            margin-left:5px;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: #777;
            border: none;
            font-size:15px;
        }

        .addIgnore:hover{
            background-color: #555;
        }

        .map-view{
            width:100%;
            height:95%;
            display: flex;
            flex-direction:column;
        }

        


        #tree{
            width:100%;
            height:85%;
            
        }
        
        #commitarea{
        	width:100%;
        	height:15%;
        	padding:10px;
        	border :solid 1px #999;
        	border-radius : 5px;
        	background-color:#fff;
        }

        .map-editor{
            width:15%;
            display:none;
            padding:3px;
            margin-top:10px;
        }


        .input-area{
            width:100%;
            height:23%;
            /* border: solid 1px #ccc; */
            background-color: RGB(227, 228, 231);
            border-radius: 5px;
            padding:10px;
            display:flex;
            flex-direction: column;
        }

        input{
            width:100%;
        }



         .linebtn {
            display:flex;
            width:100%;
            padding: 10px 0px 10px 0px;
            justify-content: space-around;
            flex-direction: column;
            
        }
        


        #addline, #removeline{
        cursor: pointer;
        width:100%;
        font-size:25px;
        height:30px;
        display:flex;
        justify-content: center;
        align-items: center;
        margin-bottom:10px;
      }

      .fromto{
          display:flex;
          width:100%;
          justify-content: space-between;
      }

      .sourceclass, .targetclass{
          width:48%;
      }

      .btn-info {
        color: #fff;
        background-color: #8f7ae5;
        border-color: #8f7ae5;
    }

    .btn-info:hover {
        color: #fff;
        background-color: #6d5dac;
        border-color: #6d5dac;
    }

    #add{
        width:200px;
    }

    #remove{
        width:200px;
    }






    </style>


</head>

<body>

      <div class="wrap">
        <header>





        </header>

        <div class="con">
            <nav></nav>
            <section>
                    <div class="section-wrap">
    
    
                        <div class="roadmap-tit">
                            #RoadMap
                            <hr>
                        </div>

                    <div class="content-wrap">

                        <!-- cytoscape -->
                        <div class="btnarea">
                            <div class="address"><div class="addresscon form-control" id="disabledInput">https://github.com/sazzeo/proZ</div></div>
                            <div id="onedeps" class="btn btn-primary">????????????</div>
                            <div id="allview" class="btn btn-primary">?????? ??????</div>
                            <div id="modifybtn" class="btn btn-info">????????????</div>
                            <div id="gitarea" style="display: none;">
                                <input type="text" class= "gitinput form-control" name="element" style="display:none;" placeholder="git">
                                <input type="text" class= "branchinput form-control" name="element" style="display:none;" placeholder="branch">
                                <div id="ignoreinput">
                                    <input type="text" class="ignoreele form-control" name="element"  placeholder="ignore">
                                    <div class="addIgnore btn btn-primary">+</div>
                                </div>
                                <div id="gitbtn" class="btn btn-primary" style="display:none;">??? ????????????</div>
                            </div>
                            <div id="resetbtn" class="btn btn-primary" style="display:none;">??? ?????????<br>????????????</div>
                            <div id="modifinishbtn" class="btn btn-info" style="display:none;">????????????</div>
                        </div>
                        <div class="map-view">
                            <div id="tree" ></div>
                            <div id="commitarea" ></div>
                        
                        <!-- cytoscapt-end -->
                        </div>
                        </div>
                    </div>
            </section>
            <aside></aside>
        </div>

    </div>


<script type="text/javascript">
	
	

$("#modifybtn").click(function(){
    $("#modifinishbtn").css("display","flex");
    $("#gitbtn").css("display","flex");
    $("#resetbtn").css("display","flex");
    $("#modifybtn").hide();
    $("#allview").hide();
    $(".address").hide();
    $("#onedeps").hide();
    $("#tree").css("width","85%");
    $(".gitinput").css("display","flex");
    $(".branchinput").css("display","flex");
    $("#ignoreinput").css("display","flex");
    $("#gitarea").css("display","flex");
});

$("#modifinishbtn").click(function(){
    $("#modifybtn").css("display","flex");
    $("#allview").css("display","flex");
    $(".address").css("display","flex");
    $("#onedeps").css("display","flex");
    $("#modifinishbtn").hide();
    $("#tree").css("width","100%");
    $("#gitbtn").hide();
    $("#resetbtn").hide();
    $(".gitinput").hide();
    $(".branchinput").hide();
    $("#ignoreinput").hide();
    $("#gitarea").hide();
});

    
    $(".addIgnore").click(function(){
        $("#ignoreinput").prepend('<input type="text" class="ignoreele form-control" name="element"  placeholder="ignore">')
        document.getElementById("ignoreinput").scrollLeft += 100;
    });

  

    
    
    $("#gitbtn").on('click', function() {
    	
    	var git = $(".gitinput").val();
    	
    	fetch("/loadmap/git/upload",{
			method : "POST",
			headers :  {"Content-type" : "application/json; charset=UTF-8"},
			body : JSON.stringify({
					wsIdx : ${param.wsIdx},
					gitRepo : git	,
					branch : "main" ,
					ignore : ["css" , "resources" , "test" , "img"]
					
			
				})
			
		})
    	
    	
    	
    })


	let dataJson = JSON.parse('${loadmap.gitTree}');
    
    console.dir(dataJson);
    
    //??? ??? ????????????
    let dataArr = [{id:"root" , text_1:"root" , father: null}];
    

    let data = [{id:"root" , text_1:"root" , father: null}];

    
    dataJson.forEach(function(e) {
    	let d;
    	if(!e.prev) {
    		
    	
    	d =  { id: e.sha , text_1:e.path , father: "root" , color:"#2196F3" };

        data.push(d);   
    		
    	}else {
    		let color;
    		if(e.type="tree") {
    			color = "#B076CF";
    			
    		}else if(e.type="blob"){
    			color ="#E971AD";
    		}
    		
    	d =  { id: e.sha , text_1:e.path , father: e.prev , color:color };
    		
    	}
    	
    	dataArr.push(d);
    	
    })
    
    console.dir(dataArr);
    

</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/cytoscape/3.20.0/cytoscape.umd.js" integrity="sha512-bgoBr08oPe0Fgqfk4TY8yNOXb1g3pkWHnsiVLLqmR+71gyo1v4PRwFEYTIL1xuFG/EHZRAvn7P1aMvs/9rKoAg==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://pagecdn.io/lib/cytoscape/3.19.1/cytoscape.umd.js" crossorigin="anonymous"  ></script>
<!-- cytoscape--------------------------------------------------------------------------------- -->
 <script>
 
     
     
     //???????????? ??? id
     function add(id) {
    	 console.dir("????????????: ")
    	 console.dir(id);
    	 dataArr.forEach(function(e) {
    	
    		 if(id==e.father) {
    			 data.push(e);
    			 console.dir(e.text_1)
    			 console.dir("????????? ??????");
    		 }
    		 
    		 
    	 })
    	 
    	 
     }
     
     
     
        

    var myTree = Treeviz.create({
        htmlId: "tree",
        idKey: "id",
        hasFlatData: true,
        relationnalField: "father",
        hasPanAndZoom: true,
        nodeWidth:150,
        nodeHeight:50,
        mainAxisNodeSpacing:2, //??????
        isHorizontal:true,
        renderNode: function(node) { 
        return result = "<div class='box' style='cursor:pointer;height:"+node.settings.nodeHeight+"px; width:auto;display:flex;flex-direction:column;justify-content:center;align-items:center;background-color:"
                +node.data.color+
              ";border-radius:5px;'><div><strong>"
          +node.data.text_1+
          "</strong></div></div>";
        },
        linkWidth : (nodeData)=> 5, //?????? ??? ??????
        linkShape:"curve",
        linkColor : (nodeData) => "#B0BEC5" ,
        onNodeClick : (nodeData, node) => {
        	console.dir(node)
        	console.dir("??????id: ");
        	console.dir(node.id);
        	add(node.id);
        	myTree.refresh(data);
        	
        } //?????? ????????? ???????????? ?????????
    });
	
    
 
    
    
    
    myTree.refresh(data);

    
    var toggle=true;
    
    
    
    
    
/*     
    document.querySelector("#add").addEventListener("click", () => {toggle ? myTree.refresh(data_2) : myTree.refresh(data_3); toggle = false});
    document.querySelector("#remove").addEventListener("click", () => myTree.refresh(data));
      */
    </script>

</body>

</html>