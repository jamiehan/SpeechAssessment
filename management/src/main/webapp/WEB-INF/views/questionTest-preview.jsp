<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%-- <%@taglib uri="spring.tld" prefix="spring"%> --%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String servletPath = (String) request
			.getAttribute("javax.servlet.forward.servlet_path");
	String[] list = servletPath.split("\\/");
	request.setAttribute("role", list[1]);
	request.setAttribute("topMenuId", list[2]);
	request.setAttribute("leftMenuId", list[3]);
%>

<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>试题预览</title>
<meta name="keywords" content="">
<link rel="shortcut icon"
	href="<%=basePath%>resources/images/favicon.ico" />
<link href="resources/bootstrap/css/bootstrap-huan.css" rel="stylesheet">
<link href="resources/font-awesome/css/font-awesome.min.css"
	rel="stylesheet">
<link href="resources/css/style.css" rel="stylesheet">
<link href="resources/css/exam.css" rel="stylesheet" type="text/css">

</head>
<body>
	<header>
		<span style="display:none;" id="rule-role-val"><%=list[1]%></span>
		<div class="container">
			<div class="row">
			<c:import url="./common/title.jsp"></c:import>
				<!-- <div class="col-xs-5">
					<div class="logo">
						<h1>
							<a href="#">网站管理系统</a>
						</h1>
						<div class="hmeta">专注互联网在线考试解决方案</div>
					</div>
				</div> -->
				<div class="col-xs-7" id="login-info">
					<c:choose>
						<c:when
							test="${not empty sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}">
							<div id="login-info-user">

								<a
									href="user-detail/${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}"
									id="system-info-account" target="_blank">${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}</a>
								<span>|</span> <a href="j_spring_security_logout"><i
									class="fa fa-sign-out"></i> 退出</a>
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
			<nav class="collapse navbar-collapse bs-navbar-collapse"
				role="navigation">
				<c:import
					url="/common-page/top-menu?topMenuId=${topMenuId}&leftMenuId=${leftMenuId}"
					charEncoding="UTF-8" />
			</nav>
		</div>
	</div>

	<!-- Navigation bar ends -->

	<!-- Slider starts -->

	<div>
		<!-- Slider (Flex Slider) -->

		<div class="container" style="min-height: 800px;">

			<div class="row">
				<div class="col-xs-2" id="left-menu">
					<c:import
						url="/common-page/left-menu?topMenuId=${topMenuId}&leftMenuId=${leftMenuId}"
						charEncoding="UTF-8" />
				</div>
				<div class="col-xs-10" id="right-content">
					<div class="page-header">
						<h1>
							<i class="fa fa-file-text"></i> 试题预览
						</h1>
					</div>
					<div class="page-content">
						<div class="def-bk" id="bk-exampaper">

							<div class="expand-bk-content" id="bk-conent-exampaper">
								<ul id="exampaper-body" style="padding: 0px;">
                                    <table class="table-striped table">
                                        <thead>
                                            <tr>
                                                <td width="10%">
                                                    ${questionTest.id }
                                                </td>
                                                <td colspan="3">
                                                    ${questionTest.name }
                                                </td>
                                            </tr>
											<tr>
												<td width="10%">

												</td>
												<td  colspan="3">
														<span style="white-space: pre" class="memo-label"></span>
													<input id="memo" type="text" value="${questionTest.memo }" hidden>
												</td>
											</tr>
                                            <tr>
                                                <td colspan="4">
                                                    <form id="from-question-answer-import" action="secure/question-answer-import/">
                                                        <div class="form-line template-download">
                                                            <span class="form-label">下载模板：</span>
                                                            <a href="resources/template/question.xlsx" style="color:rgb(22,22,22);text-decoration: underline;">点击下载</a>
                                                        </div>
                                                        <div class="form-line control-group">
                                                            <span class="form-label"><span class="warning-label">*</span>上传答案：</span>
                                                            <div class="controls file-form-line">
                                                                <div>
                                                                    <div id="div-file-list"></div>
                                                                    <!-- 用来作为文件队列区域 -->
                                                                    <div id="fileQueue"></div>
                                                                    <div id="uploadify"></div>
                                                                </div>
                                                                <span class="help-inline form-message"></span>
                                                            </div>
                                                        </div>
                                                        <div  class="form-line" >
                                                            <span class="form-label">试卷ID：</span>
                                                            <input id="test_id" type="text" value=" ${questionTest.id }">
                                                        </div>
                                                        <div class="form-line">
                                                            <input value="提交" type="submit" class="df-submit btn btn-info">
                                                        </div>
                                                    </form>

                                                </td>


                                            </tr>
                                        </thead>

                                        <tbody>
										<tr id="question-item-0">
											<td colspan="4"></td></tr>
									<c:forEach items="${questionList}" var="items">
										<tr id="question-item-${items.test_num }">
											<td width="5%">
                                                    ${items.id }
											</td>
											<td width="10%">${items.id }-${items.test_num }-${items.test_id }</td>
											<td width="40%">
												<a href="<%=list[1]%>/question/question-preview/${items.id }" target="_blank" title="预览">${items.name }</a>
											</td>

                                            <td style="white-space: pre-wrap" width="40%">答案: ${items.test_num }、${items.answer}&nbsp;&nbsp;</td>

											<%--<td>${items.questionTypeName }</td><td>${items.fieldName }</td><td>${items.pointName }</td>--%>
											<%--<td>${items.creator }</td>--%>
											<%--<td>--%>
												<%--<c:choose>--%>
													<%--<c:when test="${items.creator == sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}">--%>
														<%--<a class="change-property btn-sm btn-info"><i class="ace-icon fa fa-pencil bigger-120"></i></a>--%>
														<%--<a class="delete-question-btn btn-sm btn-danger"><i class="ace-icon fa fa-trash-o bigger-120"></i></a>--%>

														<%--<!--  <i class="fa fa-pencil change-property"></i>--%>
														<%--<i class="fa fa-trash-o delete-question-btn"></i> -->--%>
													<%--</c:when>--%>
													<%--<c:otherwise>--%>

													<%--</c:otherwise>--%>
												<%--</c:choose>--%>

											<%--</td>--%>
										</tr>
									</c:forEach>



                                </tbody>
                                    </table>


								</ul>
								<div id="exampaper-footer"
									style="height: 187px; text-align: center; margin-top: 40px;">
									<c:choose>
										<c:when
											test="${question.creator == sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}">
											<button class="btn btn-danger" id="delete-question-btn">
												<i class="fa fa-trash-o"></i> 删除该题
											</button>
											<button class="btn btn-info"
												onclick="javascript:window.close();">
												<i class="fa fa-times"></i> 关闭页面
											</button>
										</c:when>
										<c:otherwise>
											<button class="btn btn-danger" id="delete-question-btn"
												disabled="disabled" title="您只能删除你自己添加的题">
												<i class="fa fa-trash-o"></i> 删除该题
											</button>
											<button class="btn btn-info"
												onclick="javascript:window.close();">
												<i class="fa fa-times"></i> 关闭页面
											</button>
											<p>您只能删除你自己添加的题</p>
										</c:otherwise>
									</c:choose>

								</div>
							</div>

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
	<script type="text/javascript"
		src="resources/js/jquery/jquery-1.9.0.min.js"></script>
	<script type="text/javascript" src="resources/js/all.js"></script>
	<script type="text/javascript" src="resources/js/jquery-ui-1.9.2.custom.min.js"></script>
	<script type="text/javascript" src="resources/js/uploadify/jquery.uploadify3.1Fixed.js"></script>
	<script type="text/javascript" src="resources/js/question-upload-img.js"></script>
    <script type="text/javascript" src="resources/js/question-import.js"></script>
	<script type="text/javascript" src="resources/js/field-2-point.js"></script>
	<script type="text/javascript" src="resources/js/question-add.js"></script>

	<!-- Bootstrap JS -->
	<script type="text/javascript"
		src="resources/bootstrap/js/bootstrap.min.js"></script>
	<script>
        $(function() {
            var memo = $("#memo").val();
            var strs = new  Array();
            strs = memo.split("|");
            var memostr="";
            var map={};
            var mapstr = {};
            for (var i=0;i<strs.length;i++){
                // document.write(strs[i]+"<br/>");
                memostr = memostr + strs[i]+"\r\n";
                map[i]=strs[i].split('-').pop();
                mapstr[strs[i].split('-').pop()]=strs[i];
            }
            $(".memo-label").text(memostr);
            for (var j=0;j<strs.length;j++){
                // alert(map[j]+"---"+ mapstr[map[j]]);
                var maptitle = mapstr[map[j]].split('-');
                maptitle.pop();
                // alert(maptitle);
                $("#question-item-"+map[j]).after("<tr><td align=\"left\" style='font-size: 20px;' colspan=\"4\">"+maptitle+"</td></tr>");
                $("#question-item-"+map[j]).after("<tr><td align=\"left\" colspan=\"4\"></td></tr>");
            }
        });

		$(function() {

			$("#delete-question-btn").click(
					function() {
						var result = confirm("确定删除吗？删除后将不可恢复");
						if (result == true) {
							jQuery.ajax({
								headers : {
									'Accept' : 'application/json',
									'Content-Type' : 'application/json'
								},
								type : "GET",
								url : 'secure/question/delete-question/'
										+ $(".question-id").text(),
								success : function(message, tst, jqXHR) {
									if (!util.checkSessionOut(jqXHR))
										return false;
									if (message.result == "success") {
										util.success("删除成功！", function() {
											window.opener.location
													.reload(false);
											window.close();
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
                $("#from-question-answer-import").submit(function(){
                    var question_entity = new Object();
                    var testId = $("#test_id").val();
                    //检查文件上传
                    if (null == $("#div-file-list").find("input").val() || "undefined" == $("#div-file-list").find("input").val()){
                        $(".form-message").text("");
                        $(".file-form-line .form-message").addClass("has-error");
                        $(".file-form-line .form-message").text("请上传有效文件");
                        alert("上传文件无效:"+$("#div-file-list").find("input").val());
                        return false;
                    }

                    var filePath = $("#div-file-list").find("input").val().split('\\').pop();
                    var fileName = $("#div-file-list").find(".file-name").text();
                    var fileName_ext = fileName.split('.').pop();
                    // alert("ext="+fileName_ext);
                    // alert(filePath);
                    // alert($("#from-question-answer-import").attr("action") + "/" +testId);


                    question_entity.filePath = filePath;
                    question_entity.name = fileName;
                    question_entity.fieldName = testId;
                    $.ajax({
                        headers : {
                            'Accept' : 'application/json',
                            'Content-Type' : 'application/json'
                        },
                        type : "POST",
                        url : $("#from-question-answer-import").attr("action") + "/" + testId,
                        // data : filePath,
                        data : JSON.stringify(question_entity),
                        success : function(message, tst, jqXHR) {
                            if (!util.checkSessionOut(jqXHR))
                                return false;
                            if (message.result == "success") {
                                util.success("导入成功", function() {
                                    $("#submit-div .form-message").text(message.messageInfo);
                                    document.location.href = document.getElementsByTagName('base')[0].href + util.getCurrentRole() + '/question/questionTest-preview/'+ testId;
                                    //document.location.href = document.getElementsByTagName('base')[0].href + 'admin/course-list';
                                });
                            } else {
                                util.error("操作失败请稍后尝试:" + message.result);
                                $("#submit-div .form-message").text(message.messageInfo);
                                $("#btn-add-submit").removeAttr("disabled");
                            }
                        },
                        error : function(jqXHR, textStatus) {
                            util.error("操作失败请稍后尝试");
                            $("#btn-add-submit").removeAttr("disabled");
                        }
                    });
                    return false;
                });

		});
	</script>
</body>
</html>