<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/";
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

		<c:import url="./common/navigation2.jsp"></c:import>

		<!-- Navigation bar ends -->

		<!-- Slider starts -->
		<div class="content" style="margin-bottom: 100px;">
			<div class="container" style="margin-top: 40px;">

				<ul class="nav nav-pills " style="margin: 20px 0;">
					<c:forEach items="${catList }" var="item">
						<li role="presentation" <c:if test="${item.id == catId }"> class="active"</c:if>><a href="expert-list?catId=${item.id}">${item.catName}</a></li>
					</c:forEach>
				</ul>
				<div class="row">
						<div class="col-xs-12">
							<div style="border-bottom: 1px solid #ddd;">
							<h3 class="title"><i class="fa fa-book"></i>专家库</h3>
							
							</div>
							<div id="training-list">
							<table class="table-striped table">
								 <thead>
									<tr>
										<td>ID</td>
										<td>专家库名称</td>
										<%--<td>描述</td>--%>
										<td>创建人</td>
										<td>截止时间</td>
										<%--<td>操作</td>--%>
									</tr>
								</thead> 
								<tbody>
									<c:forEach items="${expertList }" var="item">
										<tr>
											<td>${item.id }</td>
											<td>
												<a href="expert-show/${item.id}" target="_blank" title="查  看">${item.title}</a>
											</td>
											<%--<td style="width:50%;">--%>
												<%--&lt;%&ndash;<div style="font-size:12px;padding:10px 0px;">&ndash;%&gt;--%>
												<%--&lt;%&ndash;&ndash;%&gt;--%>
													<%--&lt;%&ndash;${item.content}&ndash;%&gt;--%>
												<%--&lt;%&ndash;</div>&ndash;%&gt;--%>
												<%--<a href="expert-show/${item.id}" target="_blank" title="预  览">${item.content}</a>--%>
											<%--</td>--%>
											<td>
													${item.creator}
											</td>
											<td><fmt:formatDate value="${item.createTime}"
													pattern="yyyy-MM-dd" /></td>
											
											<%-- <td>${item.trainingDesc }</td> --%>
											<%-- <td><fmt:formatDate value="${item.createTime}"
													pattern="yyyy-MM-dd" /></td> --%>
										<%-- 	<td><fmt:formatDate value="${item.expTime}"
													pattern="yyyy-MM-dd" /></td> --%>
											<%--<td ><a class="btn btn-success approved-btn" data-id="${item.trainingId }" href="student/training/${item.trainingId }">参加培训</a></td>--%>

										</tr>
									</c:forEach>

								</tbody>
								<tfoot></tfoot>
							</table>

							</div>
							<div class="page-link-content">
								<ul class="pagination pagination-sm">${pageStr}</ul>
							</div>
						</div>
				</div>
			</div>
			

		</div>

		<!-- footer.jsp  -->
		<c:import url="./common/footer.jsp"></c:import>

		<!-- Slider Ends -->


		
	</body>
</html>
