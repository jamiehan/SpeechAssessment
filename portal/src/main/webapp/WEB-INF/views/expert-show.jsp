<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.examstack.common.domain.expert.CmsExpert" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	CmsExpert expert = (CmsExpert)request.getAttribute("expert");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	String createDate = simpleDateFormat.format(expert.getCreateTime());
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame
		Remove this if you use the .htaccess -->
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>育涵科技</title>
		<meta name="viewport"
		content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="keywords" content="">
		<!--<link rel="shortcut icon" href="http://localhost:8080/Portal/../resources/images/favicon.ico" />-->
		<link href="resources/bootstrap/css/bootstrap-huan.css"
		rel="stylesheet">
		<link href="resources/font-awesome/css/font-awesome.min.css"
		rel="stylesheet">
		<link href="resources/css/style.css" rel="stylesheet">

		<style>
			.table > thead > tr > th, .table > tbody > tr > th, .table > tfoot > tr > th, .table > thead > tr > td, .table > tbody > tr > td, .table > tfoot > tr > td {
				padding: 8px 0;
				line-height: 1.42857143;
				vertical-align: middle;
				border-top: 1px solid #ddd;
			}

			a.join-practice-btn {
				margin-top: 0;
			}
		</style>

	</head>

	<body>
		<header>
			<div class="container">
				<div class="row">
					<c:import url="./common/title.jsp"></c:import>
					<!-- <div class="col-xs-5">
						<div class="logo">
							<h1><a href="#"><img alt="" src="resources/images/logo.png"></a></h1>
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
					<ul class="nav navbar-nav">
						<li>
							<a href="home"><i class="fa fa-home"></i>主页</a>
						</li>
						<li>
							<a href="student/practice-list"><i class="fa fa-edit"></i>试题练习</a>
						</li>
						<li>
							<a href="exam-list"><i class="fa  fa-paper-plane-o"></i>在线考试</a>
						</li>
						<%--<li>--%>
							<%--<a href="training-list"><i class="fa fa-book"></i>培训资料</a>--%>
						<%--</li>--%>
						<li>
							<a href="rules-list"><i class="fa fa-file-text-o"></i>资料库</a>
						</li>
						<li class="active">
							<a href="expert-list"><i class="fa fa-cloud"></i>专家库</a>
						</li>
						<li>
							<a href="student/usercenter"><i class="fa fa-dashboard"></i>会员中心</a>
						</li>
						<li>
							<a href="student/setting"><i class="fa fa-cogs"></i>个人设置</a>
						</li>
					</ul>
				</nav>
			</div>
		</div>

		<!-- Navigation bar ends -->

		<!-- Slider starts -->
		<div class="content" style="margin-bottom: 100px;">
			<div class="container" style="margin-top: 40px;">
				<%--<ul class="nav nav-pills " style="margin: 20px 0;">--%>
					<%--<c:forEach items="${fieldList }" var="item">--%>
						<%--<li role="presentation" <c:if test="${item.fieldId == fieldId }"> class="active"</c:if>><a href="student/practice-list?fieldId=${item.fieldId }">${item.fieldName }</a></li>--%>
					<%--</c:forEach>--%>
				<%--</ul>--%>
				<ul class="nav nav-pills " style="margin: 20px 0;">
					<c:forEach items="${catList }" var="item">
						<li role="presentation" <c:if test="${item.id == catId }"> class="active"</c:if>><a href="rules-list?catId=${item.id}">${item.catName}</a></li>
					</c:forEach>
				</ul>
					<div class="container" style="min-height:500px;">
						<div class="row">
							<div class="col-xs-10" id="right-content">
								<div class="page-header">
									<h1><i class="fa fa-bar-chart-o"></i> 显示专家库 </h1>
								</div>

								<!-- Slider (Flex Slider) -->
								<div class="form-line add-update-examname  page-content">
									<!-- Slider starts -->
									<br/>
									<div align="center">
										<p><h2><%= expert.getTitle()%><h2/></p>
										<p><h6><%= createDate %><h6/></p>
									</div>
									<br/>
									<div align="center" >
										<%= expert.getContent()%>
									</div>
								</div>
							</div>
						</div>
					</div>
			</div>
			

		</div>

		<!-- footer.jsp  -->
		<c:import url="./common/footer.jsp"></c:import>

		<!-- Slider Ends -->

		<!-- Javascript files -->
		<!-- jQuery -->
		<script type="text/javascript"
		src="resources/js/jquery/jquery-1.9.0.min.js"></script>
		<!-- Bootstrap JS -->
		<script type="text/javascript"
		src="resources/bootstrap/js/bootstrap.min.js"></script>
		
	</body>
</html>
