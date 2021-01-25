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
request.setAttribute("topMenuId",list[2]);
request.setAttribute("leftMenuId",list[3]);
%>

<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">

		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>试题管理</title>
		<meta name="keywords" content="">
		<link rel="shortcut icon" href="<%=basePath%>resources/images/favicon.ico" />
		<link href="resources/bootstrap/css/bootstrap-huan.css" rel="stylesheet">
		<link href="resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="resources/css/style.css" rel="stylesheet">

		<link href="resources/css/exam.css" rel="stylesheet">
		<link href="resources/chart/morris.css" rel="stylesheet">
		<style>
			.examing-point {
				display: block;
				font-size: 10px;
				margin-top: 5px;
			}
			.question-name-td {
				width: 300px;
			}
			.change-property {
				cursor: pointer;
			}
			.add-tag-btn {
				cursor: pointer;
			}
		</style>
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

		<div>
			<!-- Slider (Flex Slider) -->

			<div class="container" style="min-height:500px;">

				<div class="row">
					<div class="col-xs-2" id="left-menu">
						<c:import url="/common-page/left-menu?topMenuId=${topMenuId}&leftMenuId=${leftMenuId}" charEncoding="UTF-8" />
					</div>
					<div class="col-xs-10" id="right-content">
						<div class="page-header">
							<h1><i class="fa fa-list-ul"></i> 通知管理 </h1>
						</div>
						<div class="page-content">

							<div id="question-filter"></div>
							<div id="cmsnote-list">

								<table class="table-striped table">
									<thead>
										<tr>
											<!-- <td></td> --><td class="question-name-td" style="width:40px">id</td><td style="width:580px">通知名称</td><td>创建人</td><td>创建时间</td><td style="width:90px;">操作</td>
										</tr>
									</thead>
									<tbody>

										<c:forEach items="${cmsNoteList }" var="items">
											<tr><td style="display:none;">
												<input type="checkbox" value="${items.id }">
											</td>
												<td>${items.id }</td>
												<td>
													<a href="<%=list[1]%>/cms/cms-note-show/${items.id }" target="_blank" title="预览">${items.noteName }</a>

													</td>

												<td>${items.creator }</td>
												<td><fmt:formatDate pattern="yyyy-MM-dd"
																	value="${items.createTime}" /></td>
												<td>
													<c:choose>
														<c:when test="${items.creator == sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}">
															<a class="change-property btn-sm btn-info" href="<%=basePath%>admin/cms/cms_update/${items.id}"><i class="ace-icon fa fa-pencil bigger-120" id="${items.id}"></i></a>
															<a class="delete-question-btn btn-sm btn-danger" id="${items.id}"><i class="ace-icon fa fa-trash-o bigger-120"></i></a>


														</c:when>
														<c:otherwise>

														</c:otherwise>
													</c:choose>

												</td>
											</tr>
										</c:forEach>

									</tbody><tfoot></tfoot>
								</table>

							</div>
							<div class="page-link-content">
								<ul class="pagination pagination-sm">
									${pageStr}
								</ul>
							</div>

						</div>
					</div>
				</div>
			</div>
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

		<!-- Slider Ends -->

		<!-- Javascript files -->
		<!-- jQuery -->
		<script type="text/javascript" src="resources/js/jquery/jquery-1.9.0.min.js"></script>
		<!-- Bootstrap JS -->
		<script type="text/javascript" src="resources/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="resources/js/all.js"></script>
		
		<script type="text/javascript" src="resources/js/question-list.js"></script>
		<script type="text/javascript" src="resources/js/field-2-point.js"></script>

		<script>
			$(function() {
				
				$(".delete-question-btn").click(function(){

					var paper_id = $(this).attr("id");
					if(confirm("确定要删除吗？删除后无法恢复")){
						jQuery.ajax({
							headers : {
								'Accept' : 'application/json',
								'Content-Type' : 'application/json'
							},
							type : "GET",
							url : '/management/admin/cms/delete-cms/' + paper_id,
							success : function(message, tst, jqXHR) {
								if (!util.checkSessionOut(jqXHR))
									return false;
								if (message.result == "success") {
									util.success("删除成功！", function() {
										
										window.location.reload();
									});
								} else {
									util.error("操作失败请稍后尝试");
								}
							},
							error : function(jqXHR, textStatus) {
								util.error("操作失败请稍后尝试");
							}
						});

					}
				
					
				});
			});

		</script>
	</body>
</html>
