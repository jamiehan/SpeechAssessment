<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.examstack.common.domain.cms.CmsNote" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String servletPath = (String)request.getAttribute("javax.servlet.forward.servlet_path");
	String[] list = servletPath.split("\\/");
	request.setAttribute("role",list[1]);
	request.setAttribute("topMenuId",list[2]);
	request.setAttribute("leftMenuId",list[3]);
	CmsNote note = (CmsNote)request.getAttribute("cmsNote");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	String createDate = simpleDateFormat.format(note.getCreateTime());
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


</head>
<body>
<header>
	<span style="display:none;" id="rule-role-val"><%=list[1]%></span>
	<div class="container">
		<div class="row">
			<c:import url="./common/title.jsp"></c:import>

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
<br/>
<div align="center">
	<p><h2><%= note.getNoteName()%><h2/></p>
	<p><h6><%= createDate %><h6/></p>
</div>
<br/>
<div align="center">
	<%= note.getNoteContent()%>
</div>

<footer>
	<div class="container">
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



</body>
</html>