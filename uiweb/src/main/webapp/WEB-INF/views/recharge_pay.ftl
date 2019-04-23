<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>蓝源Eloan-P2P平台</title>

	<#include "common/links-tpl.ftl"/>

         <script type="text/javascript">

             //打开新窗口
             function openNewWindow() {
                 //var a = $('a')[0];
                 var a = $("<a href='${ctx}/recharge/alipay.do?payToken=${payToken}' target='_blank'>Alipay</a>").get(0);
                 var e = document.createEvent('MouseEvents');
                 e.initEvent( 'click', true, true );
                 a.dispatchEvent(e);
             }


                $(function() {
                    $("#retroclockbox").flipcountdown({
                        size: 'md',
                        showHour:false,
                        showMinute:true,
                        showSecond:true,
                        beforeDateTime:"${disableDate?string('MM/dd/yyyy HH:mm:ss')}"
                    });
					//将小时去掉
                    $(".xdsoft_digit:lt(6)").remove();


                    $("#confirmPayBtn").click(function(){
                        $("#alertBox").modal('show');
                        openNewWindow();
                    });
                });
		</script>
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
            <h3>线上充值</h3>
            <p>1. 线上充值手续费全免，充值前请确认您银行卡限额，以免造成不便。</p>
            <p>2. 充值成功后没有进行任何理财行为，10个工作日后受理客户的申请提现。</p>
            <p>3. 严禁信用卡充值、套现等行为，一经发现将予以处罚，包括但不限于：限制收款、冻结账户、永久停止服务。</p>
        </div>
        <div id="retroclockbox"></div>
        <!--选择银行 end-->
        <div class="area_box mb25">
            <div class="content">
                <div class="option_bank">
                    <h3>选择支付通道</h3>
                    <div class="option_bank_con clearfix">

                        <div class="radio">
                            <label>
                                <input type="radio" name="optionsRadios" id="optionsRadios1" value="0" checked>
                                <img src="${ctx}/images/pay/alipay.gif" alt="支付宝支付">
                            </label>
                        </div>
                        <div class="radio">
                            <label>
                                <input type="radio" name="optionsRadios" id="optionsRadios2" value="1">
                                <img src="${ctx}/images/pay/yeePay.gif" alt="易宝支付">
                            </label>
                        </div>
                        <div class="radio disabled">
                            <label>
                                <input type="radio" name="optionsRadios" id="optionsRadios3" value="2">
                                <img src="${ctx}/images/pay/chinabankPay.gif" alt="网银在线">
                            </label>
                        </div>

                        <input id="confirmPayBtn" type="button" name="button" value="确认支付" class="btn btn-primary">
                    </div>

                </div>
            </div>
        </div>
	</div>


    <!--mainarea end-->
    <!---弹窗区域--->
    <div class="modal fade" id="alertBox" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" style="width:520px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                    <h3 id="">提示</h3>
                </div>
                <div class="modal-body">
                    <div class="my-modal">
                        <div class="dialog-tips">
                            <p>请在新打开的页面上完成支付，支付完成前请不要关闭窗口</p>
                            <p>正在跳转请稍后......</p>
                        </div>
                        <div class="my-modal-footer">
                            <a ui-sref="helper.pay" class="btn btn-gray">支付遇到问题</a>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <button type="" class="btn" >支付完成</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 网页版权 -->
	<#include "common/footer-tpl.ftl"/>
</body>
</html>