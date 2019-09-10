<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>    

<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/infomana_css/infomana.css">
<link rel="stylesheet" type="text/css" href="css/infomana_css/addnewsubject.css">
<title>Information management</title>
</head>
<body>
	<!-- 页眉 +导航-->
	<jsp:include page="infomana.jsp" flush="true"></jsp:include>
	<!-- 页眉+导航结束 -->

    <!-- 增加新词条 -->
    <div id="mainContent">
    
	<div>
		<form id="addNewSubject">
			<div class="tableRow">
				<p>题目：</p>
				<p>
				<input id="wordInfo" class="input" type="text" name="wordInfo" placeholder="如：网球">
				</p>
			</div>
			<div class="tableRow">
				<p>提示：</p>
				<p>
				<input id="addInfo" class="input" type="text" name="additionInfo" placeholder="如：运动">  
				</p>
			</div>
			<div class="tableRow">
				<p>其它提示：</p>
				<p>
				<input id="othAddInfo" class="input" type="text" name="otherAdditionInfo" placeholder="如：2个字+球类运动">  
				</p>
			</div>
			<span id="promptMessage"></span>
			<div>
			    <input type="button" class="button" value="添加" onclick="checkNewSubject()">
		 	</div>
		</form>
	</div>
	
	</div>
	<!-- 增加新词条结束 -->
</body>
<script type="text/javascript" src="js/jquery-1.6.4.js"></script>
<script type="text/javascript">
	
	//确保各项数值合法性
	function checkNewSubject(){
		var wordInfo=document.getElementById("wordInfo").value;
		var addInfo=document.getElementById("addInfo").value;
		var othAddInfo=document.getElementById("othAddInfo").value;
		if(wordInfo.length>0&&wordInfo.length<6&&addInfo.length>0&&addInfo.length<6&&othAddInfo.length>0&&othAddInfo.length<11){
			document.getElementById("promptMessage").innerHTML="";
			var subjectInfo={
					"wordInfo":wordInfo,
					"additionInfo":addInfo,
					"otherAdditionInfo":othAddInfo
			};
			addNewSubject(JSON.stringify(subjectInfo));
		}else{
			document.getElementById("promptMessage").innerHTML="请确保各项数值合法";
		}
	}
	//向后台传递数据并获取添加成功/失败的返回信息
	function addNewSubject(dataStr){
		$.ajax({
			type:"POST",
			url:"game/addNewSubject",
			data:dataStr,
			contentType:"application/json;charset=utf-8",
			dataType:"text",
			success:function(result){
				if(result=="success"){//typeof result:String
					//document.getElementById().innerHTML=;
					$("#promptMessage").text("添加成功");
					setTimeout("location.reload()",1000);
				}
				else{
					$("#promptMessage").text("添加操作失败，请重新尝试");
				}
			},
			error:function(){
				$("#promptMessage").text("添加操作失败，请重新尝试");
			}
		});
	}
</script>
</html>