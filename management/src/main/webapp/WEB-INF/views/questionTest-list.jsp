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
			.change-questionTest {
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
							<h1><i class="fa fa-list-ul"></i> 试题管理 </h1>
						</div>
						<div class="page-content">

							<div id="question-filter">

								<div class="page-link-content" style="float:right;margin-left:30px;">
									<ul class="pagination pagination-sm">
										${pageStr}
									</ul>
								</div>
								<div class="input-group search-form" style="float: left;width: 300px;padding: 10px 0px;">
									<input type="text" class="form-control" placeholder="搜索试题" value="${searchParam }" id="txt-search">
									<span class="input-group-btn">
										<button class="btn btn-sm btn-default" type="button" id="btn-search" data-id="0">
											<i class="fa fa-search"></i>搜索
										</button> </span>
								</div>
							</div>
							<div id="question-list">
								<input id="field-id-hidden" value="${fieldId }" type="hidden">
								<input id="knowledge-hidden" value="${knowledge }" type="hidden">
								<input id="question-type-hidden" value="${questionType }" type="hidden">
								<input id="search-param-hidden" value="${searchParam }" type="hidden">
								<input id="page-switch-hidden" value="questionTest_list" type="hidden">
								<table class="table-striped table">
									<thead>
										<tr>
											<!-- <td></td> --><td>ID</td><td class="question-name-td" style="width:240px">试题名称</td><td style="width:60px">时间</td><td>一级标题</td><td>创建人</td><td style="width:90px;">操作</td>
										</tr>
									</thead>
									<tbody>

										<c:forEach items="${questionTestList }" var="items">
											<tr>
												<td style="display:none;">
												<input type="checkbox" value="${items.id }">
												</td><td>${items.id }</td>
												<td>
													<a href="<%=list[1]%>/question/questionTest-preview/${items.id }" target="_blank" title="预览">${items.name }</a>

														<%--<div class="question-tags">--%>
															<%--&lt;%&ndash;<span>${items.tags }</span>&ndash;%&gt;--%>
															<%--<!-- <span>易错题</span>--%>
															<%--<span>送分题</span> -->--%>
														<%--</div>--%>
													
													</td>

												<td><fmt:formatDate value="${items.createTime }" pattern="yyyy-MM-dd HH:mm"/></td>
												<td>${items.memo }</td>
												<td>${items.creatorName }</td>
												<td>
													<c:choose>
														<c:when test="${items.creatorName == sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}">
															<a class="change-questionTest btn-sm btn-info"><i class="ace-icon fa fa-pencil bigger-120"></i></a>
															<a class="delete-questionTest-btn btn-sm btn-danger"><i class="ace-icon fa fa-trash-o bigger-120"></i></a>

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

							<!-- 修改试题modal -->
							<div class="modal fade" id="change-property-modal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
												&times;
											</button>
											<h6 class="modal-title" id="myModalLabel">修改试卷</h6>
										</div>
										<div class="modal-body">
											<form id="question-edit-form">
												<span id="add-update-testid" style="display:none;"></span>
												<span id="question_type_id" style="display:none;"></span>

												<div class="form-line form-question-id" >
													<span class="form-label"><span class="warning-label"></span>试卷ID：</span>
													<span id="test_id"></span>
													<span class="form-message"></span>
													<br>
												</div>
												<div class="form-line form-question-name" >
													<span class="form-label"><span class="warning-label"></span>试卷名称：</span>
													<textarea id="test_name" class="add-question-ta" rows="3" cols="30" style="width: 100%;">
													</textarea>
													<span class="form-message"></span>
													<br>
												</div>

												<div class="form-line form-question-name" >
													<span class="form-label"><span class="warning-label"></span>知识分类：</span>
													<input id="point_ids">
													<span class="form-message"></span>
													<br>
												</div>

												<div class="form-line form-question-analysis" style="display: block;">
													<span class="form-label"><span class="warning-label"></span>试卷标题：</span>
													<textarea id="test_memo" class="add-question-ta" rows="10" cols="30" style="width: 100%;">
													</textarea>
													<span class="form-message"></span>
													<br>
												</div>

												<div class="form-line form-question-name" >
													<span class="form-label"><span class="warning-label"></span>时间：</span>
													<span id="test_date"></span>
													<span class="form-message"></span>
													<br>
												</div>

												<div class="form-line form-question-name" >
													<span class="form-label"><span class="warning-label"></span>创建人：</span>
													<span id="test_creator"></span>
													<span class="form-message"></span>
													<br>
												</div>


											</form>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-default" data-dismiss="modal">
												关闭窗口
											</button>
											<button id="update-questionTest-btn" type="button" class="btn btn-primary">
												确定修改
											</button>
										</div>
									</div>
								</div>
							</div>
						</div>
							<%--修改内容end--%>

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

            Date.prototype.pattern=function(fmt) {
                var o = {
                    "M+" : this.getMonth()+1, //月份
                    "d+" : this.getDate(), //日
                    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时
                    "H+" : this.getHours(), //小时
                    "m+" : this.getMinutes(), //分
                    "s+" : this.getSeconds(), //秒
                    "q+" : Math.floor((this.getMonth()+3)/3), //季度
                    "S" : this.getMilliseconds() //毫秒
                };
                var week = {
                    "0" : "/u65e5",
                    "1" : "/u4e00",
                    "2" : "/u4e8c",
                    "3" : "/u4e09",
                    "4" : "/u56db",
                    "5" : "/u4e94",
                    "6" : "/u516d"
                };
                if(/(y+)/.test(fmt)){
                    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
                }
                if(/(E+)/.test(fmt)){
                    fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[this.getDay()+""]);
                }
                for(var k in o){
                    if(new RegExp("("+ k +")").test(fmt)){
                        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
                    }
                }
                return fmt;
            };




			$(function() {

                var answerShowForType =function(typeId,answer){
                    if (1 == typeId) {
                        $(".correct-answer").hide();
                        $(".form-question-opt").show();
                        $(".form-question-answer1").show();
                        copyToAnswer(typeId,answer);
                    } else if (2 == typeId) {
                        $(".correct-answer").hide();
                        $(".form-question-opt").show();
                        $(".form-question-answer-muti").show();
                        copyToAnswer(typeId,answer);
                    } else if (3 == typeId) {
                        $(".correct-answer").hide();
                        $(".form-question-opt").hide();
                        $(".form-question-answer-boolean").show();
                        copyToAnswer(typeId,answer);
                    } else {
                        $(".correct-answer").hide();
                        $(".form-question-opt").hide();
                        $(".form-question-answer-text").show();
                        copyToAnswer(typeId,answer);
                    }
				};

                var copyToAnswer = function (typeId,answer) {
                    if (1 == typeId) {
                        $("#question_right_answer").val(answer);
                    } else if (2 == typeId) {
                        var checkboxs = $(".form-question-answer-muti input");
                        for (var i = 0; i < checkboxs.length; i++) {
                            if(answer.indexOf(checkboxs[i].value) != -1){
                                checkboxs[i].checked=true;
                            }
                        }
                    }else if (3 == typeId){
                        $("#form-question-answer-boolean-select").val(answer);
					}else{

                        $(".form-question-answer-text textarea").val(answer);
					}
                };

				$(".change-questionTest").click(function() {
					$("#change-property-modal").modal({
						backdrop : true,
						keyboard : true
					});
					var test_id = $(this).parent().parent().find(":checkbox").val();
					$("#add-update-testid").text(test_id);
					$.ajax({
						headers : {
							'Accept' : 'application/json',
							'Content-Type' : 'application/json'
						},
						type : "GET",
						url : "secure/question/questionTest-detail/" + test_id,
						success : function(message, tst, jqXHR) {
							if (!util.checkSessionOut(jqXHR))
								return false;
							if (message.result == "success") {
								$("#test_name").text(message.object.name);
                                $("#point_ids").val(message.object.point_ids);
								$("#test_id").text(message.object.id);
								$("#test_memo").text(message.object.memo);
								var createTime = new Date(message.object.createTime);
								 $("#test_date").text(createTime.pattern("yyyy-MM-dd HH:mm:ss"));
								$("#test_creator").text(message.object.creatorName);
							} else {
								util.error("获取标签失败请稍后尝试:" + message.result);
							}

						},
						error : function(jqXHR, textStatus) {
							util.error("操作失败请稍后尝试");
						}
					});
				});




				$(".q-label-list").on("click", ".fa", function() {
					$(this).parent().remove();
				});

				
				$(".delete-questionTest-btn").click(function(){
					if(confirm("确定要删除吗？删除后无法恢复")){
						jQuery.ajax({
							headers : {
								'Accept' : 'application/json',
								'Content-Type' : 'application/json'
							},
							type : "GET",
							url : 'secure/question/delete-questionTest/' + $(this).parent().parent().find(":checkbox").val(),
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

                $("#update-questionTest-btn").click(function(){
                        var data = new Object();
                        data.id = $("#test_id").text();
                        data.name =  $("#test_name").text();
                        data.memo =  $("#test_memo").text();
                        data.point_ids =  $("#point_ids").val();
                        $.ajax({
                            headers : {
                                'Accept' : 'application/json',
                                'Content-Type' : 'application/json'
                            },
                            type : "POST",
                            url : "secure/question/questionTest-update",
                            data : JSON.stringify(data),
                            success : function(message, tst, jqXHR) {
                                if (!util.checkSessionOut(jqXHR))
                                    return false;
                                if (message.result == "success") {
                                    util.success("修改成功", function(){
                                        window.location.reload();
                                    });
                                } else {
                                    util.error("操作失败请稍后尝试:" + message.result);
                                }

                            },
                            error : function(jqXHR, textStatus) {
                                util.error("操作失败请稍后尝试"+textStatus);
                            }
                        });
                });


			});

		</script>
	</body>
</html>
