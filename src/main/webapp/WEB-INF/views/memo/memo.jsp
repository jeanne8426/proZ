<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
 <link type="text/css" rel="stylesheet" href="/resources/css/bootstrap.css"> 
 <link type="text/css" rel="stylesheet" href="/resources/css/memo/memo.css">
    <script type="text/javascript" src="https://bootswatch.com/_vendor/jquery/dist/jquery.min.js"></script>
    <script type="text/javascript" src="https://bootswatch.com/_vendor/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
    
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://kit.fontawesome.com/485bb3ceac.js" crossorigin="anonymous"></script>

    <style type="text/css">
        html,
        body {

            width: 100%;
            height: 100%;
            min-width: 1000px;

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
            flex:1;
            height: 100%;
            display: flex;
            justify-content: space-between;
            overflow: auto;

        }

        nav {
            background: rgb(19, 23, 34);
            background: linear-gradient(0deg, rgba(19, 23, 34, 1) 0%, rgba(26, 17, 47, 1) 22%, rgba(26, 30, 41, 1) 83%, rgba(19, 23, 34, 1) 100%);


            width: 220px;
            height: 100%;
            flex-shrink: 0;

        }

        section {

            height: 100%;
            width: 100%;
            overflow-y: auto;

            background: RGB(245, 246, 247);



        }


        aside {

            background: rgb(19, 23, 34);
            background: linear-gradient(0deg, rgba(19, 23, 34, 1) 0%, rgba(26, 17, 47, 1) 22%, rgba(26, 30, 41, 1) 83%, rgba(19, 23, 34, 1) 100%);

            height: 100%;
            width: 200px;
            flex-shrink: 0;

        }

        #login-btn {
            width: 200px;
            height: 100px;
        

        }
     
    </style>
   
<script>
    $(document).ready(function (){
        $(".memo-btn").click(function(){
            $("#modal").css('display','flex');
        });
        $(".close").click(function(){
            $("#modal").hide();
        });
    })
    $(document).ready(function (){
        $("#memo-yellow").click(function(){
            $("#modal-yellow").css('display','flex');
        });
        $(".close").click(function(){
            $("#modal-yellow").hide();
        });
    })

    
</script>
</head>
<body>

    <div class="wrap">
        <header>
        




        </header>

        <div class="con">
        <nav></nav>
        <section>
            <!--여기서만 작업-->
            <div id="title-control">
            <div id="title">
               # <i class="fas fa-user-edit"></i> proZ 메모 
            </div>
        </div>
        <div style="display: flex;
        justify-content:center;">
        <hr width="90%" ></div>
        

        <div id="top">
            <div id="range">
                <button id="new">최신순</button>
                <button id="old">오래된순</button>
            </div>
            <div id="search-form">
                <img class="search-icon" src="/resources/img/search.png">
                <input class="form-control" id="search" type="text" placeholder="검색하기" >  
            </div>
            <button type="button"  id="memo-btn" class="memo-btn">메모 작성</button>
        </div>
    <div id="memo">        
           
           
     </div>
     <div id="modal">
        <div id = "write-memo">
            <!--메모지 모달 창 띄우면 보여지는 거-->
            <div id="close" class="close"><i class="fas fa-times "></i></div>
            <div id="text"><textarea style="resize: none; border: none;"></textarea>
            </div>
        
            <div id="editor">
                <div id="back-color"><i class="fas fa-palette "></i></i></div>
                <div id="font"><i class="fas fa-font "></i></div>
                <div id="italic"><i class="fas fa-italic "></i></div>
                <div id="under"><i class="fas fa-underline "></i></div>
                <div id="line"><i class="fas fa-strikethrough"></i></div>
                <div id="save"><i class="fas fa-save "></i></div>
            </div>
        </div>
    </div>

    <div id="modal-yellow">
        <div id = "write-memo">
            <!--메모지 모달 창 띄우면 보여지는 거-->
            <div id="close" class="close"><i class="fas fa-times "></i></div>
            <div id="text"><textarea style="resize: none; border: none;"></textarea>
            </div>
        </div>
    </div>

        </section>
        <aside></aside>
        </div>
    </div>
    
    <script type="text/javascript">
   
    </script>
</body>
</html>