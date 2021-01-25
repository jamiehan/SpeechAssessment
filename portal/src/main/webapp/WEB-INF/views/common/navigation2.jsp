<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020-09-28
  Time: 16:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Javascript files -->
<!-- jQuery -->
<script type="text/javascript"
        src="resources/js/jquery/jquery-1.9.0.min.js"></script>
<!-- Bootstrap JS -->
<script type="text/javascript"
        src="resources/bootstrap/js/bootstrap.min.js"></script>
<div class="navbar bs-docs-nav" role="banner">
    <div class="container">
        <nav class="collapse navbar-collapse bs-navbar-collapse" role="navigation">
            <ul class="nav navbar-nav">

                <li class="same-class">
                    <a href="home"><i class="fa fa-home"></i>主页</a>
                </li>
                <li class="same-class">
                    <a href="student/practice-list"><i class="fa fa-edit"></i>试题练习</a>
                </li>
                <li class="same-class">
                    <a href="exam-list"><i class="fa  fa-paper-plane-o"></i>在线考试</a>
                </li>
                <%--<li id="training-list">--%>
                <%--<a href="training-list"><i class="fa fa-book"></i>培训资料</a>--%>
                <%--</li>--%>
                <li class="same-class">
                    <a href="rules-list"><i class="fa fa-file-text-o"></i>资料库</a>
                </li>
                <li class="same-class active">
                    <a href="expert-list"><i class="fa fa-cloud"></i>专家库</a>
                </li>
                <li class="same-class">
                    <a href="student/usercenter"><i class="fa fa-dashboard"></i>会员中心</a>
                </li>
                <li class="same-class">
                    <a href="student/setting"><i class="fa fa-cogs"></i>个人设置</a>
                </li>

            </ul>
        </nav>
    </div>
</div>


<script language="JavaScript">
    // var $ = require("jquery");
    $("li").on("click",function() {
        $(this).addClass("active");
    });
    //     .on("mouseup",function(){
    //     $(this).removeClass("active");
    // });
</script>


<style>
    /*.div{font-size:14px}*/
    /* css注释：设置使用对象选择器名为“.div”的字体大小为14px */
    /*.span{font-size:20px}*/
</style>