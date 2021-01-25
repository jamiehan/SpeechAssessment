<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%-- <%@taglib uri="spring.tld" prefix="spring"%> --%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String servletPath = (String)request.getAttribute("javax.servlet.forward.servlet_path");
	String[] list = servletPath.split("\\/");
	request.setAttribute("role",list[1]);
	//	request.setAttribute("topMenuId",list[2]);
//	request.setAttribute("leftMenuId",list[3]);

	request.setAttribute("topMenuId","common");
	request.setAttribute("leftMenuId","cms-rules-list");
%>

<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="utf-8"><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>试题管理</title>
	<meta name="keywords" content="">
	<link rel="shortcut icon" href="<%=basePath%>resources/images/favicon.ico" />
	<link href="resources/bootstrap/css/bootstrap-huan.css" rel="stylesheet">
	<link href="resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">
	<link href="resources/css/style.css" rel="stylesheet">

	<link href="resources/css/question-add.css" rel="stylesheet">
	<link href="resources/chart/morris.css" rel="stylesheet">
	<link href="resources/css/jquery-ui-1.9.2.custom.min.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
		.uploadify-button-text{
			text-decoration: underline;
		}

		span.add-img{
			text-decoration: underline;
			cursor:pointer;
		}

		span.add-img:hover{
			text-decoration: underline;
		}

		.swfupload {
			z-index: 10000 !important;
		}

		.add-content-img{
			display:block;
		}

		.diaplay-img{
			margin-right:5px;
		}

		.diaplay-img:hover{
			text-decoration: underline;

		}
	</style>



	<script type="text/javascript" src="resources/js/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="resources/js/ueditor/ueditor.all.min.js"></script>
	<script type="text/javascript" src="resources/js/ueditor/ueditor.all.js"></script>
	<script type="text/javascript" src="resources/js/ueditor/lang/zh-cn/zh-cn.js"></script>
	<script type="text/javascript" src="resources/js/jquery/jquery-1.9.0.min.js"></script>

</head>
<body>
<header>
	<span style="display:none;" id="rule-role-val"><%=list[1]%></span>
	<div class="container">
		<div class="row">
			<c:import url="./common/title.jsp"></c:import>
			<!-- <div class="col-xs-5">
                <div class="logo">
                    <h1><a href="#">网站管理系统</a></h1>
                    <div class="hmeta">
                        专注互联网在线考试解决方案
                    </div>
                </div>
            </div> -->
			<div class="col-xs-7" id="login-info">
				<c:choose>
					<c:when test="${not empty sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}">
						<div id="login-info-user">

							<a href="user-detail/${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}" id="system-info-account" target="_blank">${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}</a>
							<span>|</span>
							<a href="j_spring_security_logout"><i class="fa fa-sign-out"></i> 退出</a>
						</div>
					</c:when>
					<c:otherwise>
						<a class="btn btn-primary" href="user-register">用户注册</a>
						<a class="btn btn-success" href="user-login-page">登录</a>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</header>
<!-- Navigation bar starts -->

<div class="navbar bs-docs-nav" role="banner">
	<div class="container">
		<nav class="collapse navbar-collapse bs-navbar-collapse" role="navigation">
			<c:import url="/common-page/top-menu?topMenuId=${topMenuId}&leftMenuId=${leftMenuId}" charEncoding="UTF-8" />
		</nav>
	</div>
</div>

<!-- Navigation bar ends -->

<!-- Slider starts -->

<div class="container" style="min-height:500px;">

	<div class="row">
	<div class="col-xs-2" id="left-menu">
		<c:import url="/common-page/left-menu?topMenuId=${topMenuId}&leftMenuId=${leftMenuId}" charEncoding="UTF-8" />
	</div>

		<div class="col-xs-10" id="right-content">
			<div class="page-header">
				<h1><i class="fa fa-bar-chart-o"></i> 添加资料 </h1>
			</div>

            <%--<div class="form-line add-update-examname page-content" >--%>
            <div class="form-line page-content">
                <span class="form-label"><span class="warning-label">*</span>资料类型：</span>
                <%--<select id="cat_id">--%>
                    <%--<option id="16">规章制度</option>--%>
                    <%--<option id="17">对外培训一</option>--%>
                <%--</select>--%>
                <select id="cat_id">
                    <c:forEach items="${catList }" var="item">
                    <option value="${item.id }">${item.catName }</option>
                    </c:forEach>
                </select>
            </div>
		<!-- Slider (Flex Slider) -->
	<div class="form-line add-update-examname page-content" >
		<span class="form-label"><span class="warning-label">*</span>资料名称：</span>
		<input id="note_name" type="text" class="df-input-narrow">
		<span class="form-message"></span>
	</div>
	<div class="container" style="min-height:500px;">
		<script id="txt" title="txt" label="txt" name="txt" type="text/plain" style="width:680px;"></script><script type="text/javascript">
		var editor= UE.getEditor('txt',{
			imageUrl:"<%=request.getContextPath()%>/ueditor/upload.do?Type=Image",
			fileUrl:"<%=request.getContextPath()%>/ueditor/upload.do?Type=File",
			catcherUrl:"<%=request.getContextPath()%>/ueditor/getRemoteImage.do",
			imageManagerUrl:"<%=request.getContextPath()%>/ueditor/imageManager.do?picNum=50&insite=false",
			wordImageUrl:"<%=request.getContextPath()%>/ueditor/upload.do?Type=File",
			getMovieUrl:"<%=request.getContextPath()%>/ueditor/getmovie.do",
			videoUrl:"<%=request.getContextPath()%>/ueditor/upload.do?Type=Media"
		});
		</script>
		<button id="submit">提交</button>
	</div>
	</div>
	</div>
</div>

<footer>
	<div class="container" align="center">
		<div class="row">
			<div class="col-md-12">
				<div class="copy">
					<p>
						育涵科技 Copyright © <a href="." target="_blank">主页</a>
						| <a href="#" target="_blank">关于我们</a> | <a
							href="#" target="_blank">FAQ</a> | <a
							href="#" target="_blank">联系我们</a>
					</p>
				</div>
			</div>
		</div>

	</div>

</footer>

<!-- Slider Ends -->
<script type="text/javascript">

	$("#submit").click(function(){
        var catId = $("#cat_id").val();
		var name = $("#note_name").val();
		var content = UE.getEditor('txt').getContent();

		if(!name){
			alert('请填写标题!');
		}else if (!UE.getEditor('txt').hasContents()){
			alert('请先填写内容!');
		}else {
			var jsonObj = {"catId": catId, "title": name,"content":content};
			$.ajax({
				// cache: true,
				type: "POST",
				url: "<%=request.getContextPath()%>/admin/cms/cmsRules_add_in",
				data: JSON.stringify(jsonObj),
				// dataType: "json",
				contentType: "application/json;charset=UTF-8",//指定消息请求类型
				// async: false,
				// error: function (request) {
                // 	alert("Connection error");
                // },
				success: function (result) {
                    // alert(result);
                    window.location.href = result;
					// if("success" == result.result) {
					// 	alert("提交成功");
					//
					// 	window.location.href = result;
					// }else{
					// 	alert("提交失败");
					//
					// }
				}
			});
		}
	});
</script>


</body>
</html>