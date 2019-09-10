<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><!-- jstl-1.2.jar -->
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
<link rel="stylesheet" type="text/css" href="css/infomana_css/deletesubject.css">
<title>Information management</title>
</head>
<body>
	<!-- 页眉 +导航-->
	<jsp:include page="infomana.jsp" flush="true"></jsp:include>
	<!-- 页眉+导航结束 -->

    <!-- 删除词条 -->
    <div id="mainContent">
    
	<div>
	<form name="selectedSubjectId">
		<table class="showAllSubject">
		    <tr>
		    <th class="subjectTableColumn">选择</th>
		    <!-- 测试专用：核对选择框选中id
		    <th>编号</th>
		    -->
		    <th class="subjectTableColumn">词语</th>
		    <th class="subjectTableColumn">提示1</th>
		    <th>提示2</th>
		    </tr>
		    <c:forEach items="${subjectList}" var="gameSub">
			    <tr>
			    <td><input type="checkbox" name="selectedId" value="${gameSub.subjectId}"></td>
			    <!-- 测试专用：核对选择框选中id
			    <td>${gameSub.subjectId}</td>
			    -->
			    <td>${gameSub.wordInfo}</td>
			    <td>${gameSub.additionInfo}</td>
			    <td>${gameSub.otherAdditionInfo}</td>
			    </tr>
		    </c:forEach>  
	    </table>
	    
	    <span id="promptMessage"></span>
	    
	    <div id="pageDiv">
	    <span>第${page.currentPage}/${page.totalPage}页  共${page.totalCount}条记录</span>
	    <a class="page" href="game/loadingdedeleteSubPage/1">首页</a>
	    <c:choose>
	    	<c:when test="${page.currentPage-1>0}">
	    		<a class="page" href="game/loadingdedeleteSubPage/${page.currentPage-1}">上一页</a>
	    	</c:when>
	    	<c:when test="${page.currentPage-1<=0}">
	    		<a class="page" href="game/loadingdedeleteSubPage/1">上一页</a>
	    	</c:when>
	    </c:choose>
	    <c:choose>
	    	<c:when test="${page.currentPage+1<page.totalPage}">
	    		<a class="page" href="game/loadingdedeleteSubPage/${page.currentPage+1}">下一页</a>
	    	</c:when>
	    	<c:when test="${page.currentPage+1>=page.totalPage}">
	    		<a class="page" href="game/loadingdedeleteSubPage/${page.totalPage}">下一页</a>
	    	</c:when>
	    </c:choose>
	    <a class="page" href="game/loadingdedeleteSubPage/${page.totalPage}">尾页</a>
	    </div>
	    
	    <div><!-- <input>的位置受<span>/<p>内容添加的影响 -->
	    <input type="button" class="button" onclick="checkSelectedId()" value="删除">
   		</div>
    </form>
	</div>
	
	</div>
	<!-- 删除词条结束 -->
</body>
<script type="text/javascript" src="js/jquery-1.6.4.js"></script>
<script type="text/javascript">	
	//确保各项数值合法性
	function checkSelectedId(){
		/* 自动将form中checkbox（实质为input）的值传入，后端方法参数声明中用Integer[] selectedId接收即可
		 * document.selectedSubjectId.action="game/deleteGameSubject";
		 * document.selectedSubjectId.submit();
		 */
		var subList=document.getElementsByName("selectedId");
		var subList_str="";
		for(var i=0;i<subList.length;i++){
			if(subList[i].checked){
				//alert("选中："+subList[i].value);//subList[i]为HTMLinputelement
				subList_str+=subList[i].value+",";
			}
		}
		//alert("subList_str:"+subList_str);
		if(subList_str.length == 0){
			document.getElementById("promptMessage").innerHTML="未选中任何一项";
		}else{
			deleteGameSubject(subList_str);
		}
	}
	//向server端发送待删除数据id
	function deleteGameSubject(subList_str){
		$.ajax({
			type:"POST",
			url:"game/deleteGameSubject",
			data:subList_str,
			contentType:"application/json;charset=utf-8",
			dataType:"text",
			success:function(result){
				if(result=="success"){//typeof result:String
					$("#promptMessage").text("删除成功");
					 setTimeout("location.reload()",1000);
				}
				else{
					$("#promptMessage").text("删除操作失败，请重新尝试");
				}
			},
			error:function(){
				$("#promptMessage").text("删除操作失败，请重新尝试");
			}
		});
	}
</script>
</html>