<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>蓝源Eloan-P2P平台</title>

	<#include "common/links-tpl.ftl"/>

	</head>
	<body>
    <!-- 网页头信息 -->
    <div class="el-header" >
        <div class="container" style="position: relative;">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="/">首页</a></li>
                <li><a href="/login.html">登录</a></li>
                <li><a href="/register.html">快速注册</a></li>
                <li><a class="el-current-user" href="/personal.do">
                    zhangsan </a></li>
                <li><a href="#"> 账户充值 </a></li>
                <li><a id="logout" href="javascript:void(0);"> 注销 </a></li>
                <li><a href="#">帮助</a></li>
            </ul>
        </div>
    </div>

    <!-- 网页导航 -->
    <div class="navbar navbar-default el-navbar">
        <div class="container">
            <div class="navbar-header">
                <a href="/">
                    <img alt="Brand" src="${ctx}/images/logo.png">
                </a>
            </div>
            <ul class="nav navbar-nav">
                <li id="index"><a href="/">首页</a></li>
                <li id="invest"><a href="/invest.do">我要投资</a></li>
                <li id="borrow" class="active"><a href="/borrow.jsp">我要借款</a></li>
                <li id="account"><a href="/personal.do">个人中心</a></li>
                <li><a href="#">新手指引</a></li>
                <li><a href="#">关于我们</a></li>
            </ul>
        </div>
    </div>

    <div class="container">
        <div class="el-tip-info">
            <h3>支付成功！</h3>
        </div>
	</div>


   
    <!-- 网页版权 -->
	<#include "common/footer-tpl.ftl"/>
</body>
</html>