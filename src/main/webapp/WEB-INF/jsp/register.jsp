<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache,must-revalidate">
    <title>注册-个人用户</title>
    <link type="text/css" rel="stylesheet" href="/css/regist.personal.css"/>
    <link type="text/css" rel="stylesheet" href="/css/passport.base.css"/>
    <script type="text/javascript" src="/js/jquery-1.6.4.js"></script>
</head>
<body>
<div class="w" id="logo">
    <div>
    	<a href="http://localhost:8082">
    		<img src="/images/youtu-logo.jpg" alt="优兔商城" width="170" height="60"/>
    	</a> <b></b>
    </div>
</div>

<div class="w" id="regist">
    <div class="mt">
        <ul class="tab">
            <li class="curr">个人用户</li>
        </ul>
        <div class="extra">
        <span>我已经注册，现在就&nbsp;
        	<a href="/page/login" class="flk13">登录</a>
        </span>
        </div>
    </div>
    <div class="mc">
        <form id="personRegForm" method="post" onsubmit="return false;">
            <div class="form" onselectstart="return false;">
                <div class="item" id="select-regName">
                    <span class="label"><b class="ftx04">*</b>用户名：</span>

                    <div class="fl item-ifo">
                        <div class="o-intelligent-regName">
                            <input type="text" id="regName" name="username" class="text" tabindex="1" autoComplete="off"
                                   onpaste="return false;"
                                   value=""
                                   onfocus="if(this.value=='') this.value='';this.style.color='#333'"
                                   onblur="if(this.value=='') {this.value='';this.style.color='#999999'}"/>
                            <i class="i-name"></i>
                            <ul id="intelligent-regName" class="hide"></ul>
                            <label id="regName_succeed" class="blank"></label>
                            <label id="regName_error" class="hide"></label>
                        </div>
                    </div>
                </div>
                <div class="item" id="demail">
						<span class="label"><b class="ftx04">*</b>昵称：</span>

						<div class="fl item-ifo">
							<input type="text" id="nickname" maxlength="11" name="nickname"
								class="text" tabindex="4"
								autocomplete="off" /> <i class="i-name"></i> <label
								id="email_succeed" class="blank"></label> <label
								id="email_error"></label>
						</div>
					</div>
                <div id="o-password">
                    <div class="item">
                        <span class="label"><b class="ftx04">*</b>请设置密码：</span>

                        <div class="fl item-ifo">
                            <input type="password" id="pwd" name="password" class="text" tabindex="2"
                                   style="ime-mode:disabled;"
                                   onpaste="return  false" autocomplete="off"/>
                            <i class="i-pass"></i>
                            <label id="pwd_succeed" class="blank"></label>
                            <label id="pwd_error"></label>
                            <span class="clr"></span>
                        </div>
                    </div>

                    <div class="item">
                        <span class="label"><b class="ftx04">*</b>请确认密码：</span>

                        <div class="fl item-ifo">
                            <input type="password" id="pwdRepeat" name="pwdRepeat" class="text" tabindex="3"
                                   onpaste="return  false" autocomplete="off"/>
                            <i class="i-pass"></i>
                            <label id="pwdRepeat_succeed" class="blank"></label>
                            <label id="pwdRepeat_error"></label>
                        </div>
                    </div>
                    
					<div class="item" id="dphone">
						<span class="label"><b class="ftx04">*</b>验证手机：</span>

						<div class="fl item-ifo">
							<input type="text" id="phone" maxlength="11" name="phone"
								class="text" tabindex="4"
								autocomplete="off" /> <i class="i-phone"></i> <label
								id="phone_succeed" class="blank"></label> <label
								id="phone_error"></label>
						</div>
					</div>
					<div class="item" id="demail">
						<span class="label"><b class="ftx04">*</b>电子邮箱：</span>

						<div class="fl item-ifo">
							<input type="text" id="email" maxlength="11" name="email"
								class="text" tabindex="4"
								autocomplete="off" /> <i class="i-name"></i> <label
								id="email_succeed" class="blank"></label> <label
								id="email_error"></label>
						</div>
					</div>
					
					</div>
                <div class="item item-new">
                    <span class="label">&nbsp;</span>

                    <div class="fl item-ifo">
                        <input type="checkbox" class="checkbox" checked="checked" id="readme"
                               onclick="agreeonProtocol();">
                        <label for="protocol">我已阅读并同意<a href="#" class="blue" id="protocol">《优兔用户注册协议》</a></label>
                        <span class="clr"></span>
                        <label id="protocol_error" class="error hide">请接受服务条款</label>
                    </div>
                </div>
                <div class="item">
                    <span class="label">&nbsp;</span>
                    <input type="button" class="btn-img btn-regist" id="registsubmit" value="立即注册" tabindex="8"
                           clstag="regist|keycount|personalreg|07"
                           onclick="REGISTER.reg();"/>
                </div>
            </div>
            <div class="phone">
                <img width="180" height="180" src="/images/phone-bg.jpg">
            </div>
            <span class="clr"></span>
        </form>
    </div>
<script type="text/javascript">
	var REGISTER={
		param:{
			//单点登录系统的url
			surl:""
		},
		inputcheck:function(){
				//不能为空检查
				if ($("#regName").val() == "") {
					alert("用户名不能为空");
					$("#regName").focus();
					return false;
				}
				if ($("#nickName").val() == "") {
					alert("昵称不能为空");
					$("#nickName").focus();
					return false;
				}
				if ($("#pwd").val() == "") {
					alert("密码不能为空");
					$("#pwd").focus();
					return false;
				}
				if ($("#phone").val() == "") {
					alert("手机号不能为空");
					$("#phone").focus();
					return false;
				}
				if ($("#email").val() == "") {
					alert("邮箱不能为空");
					$("#eamil").focus();
					return false;
				}
				
				//密码检查
				if ($("#pwd").val() != $("#pwdRepeat").val()) {
					alert("确认密码和密码不一致，请重新输入！");
					$("#pwdRepeat").select();
					$("#pwdRepeat").focus();
					return false;
				}
				return true;
		},
		/* beforeSubmit:function() {
				//检查用户是否已经被占用
				$.ajax({
	            	//get请求每次都会被浏览器缓存，如果每次请求url都一样，浏览器就会返回缓存的结果，所以加随机数
					url : REGISTER.param.surl + "/user/check/"+escape($("#regName").val())+"/1?r=" + Math.random(),
	            	success : function(data) {
	            		if (data.data) {
	            			//检查手机号是否存在
	            			$.ajax({
	            				url : REGISTER.param.surl + "/user/check/"+$("#phone").val()+"/2?r=" + Math.random(),
				            	success : function(data) {
				            		if (data.data) {
					            		REGISTER.doSubmit();
				            		} else {
				            			alert("此手机号已经被注册！");
				            			$("#phone").select();
				            		}
				            	}
	            			});
	            		} else {
	            			alert("此用户名已经被占用，请选择其他用户名");
	            			$("#regName").select();
	            		}	
	            	}
				});
	            	
		}, */
		doSubmit:function() {
			$.post("/user/register?r=" + Math.random(),$("#personRegForm").serialize(), function(data){
				//根据返回状态码，401：用户名已存在  402：手机号码已存在  403：电子邮箱已存在  404:昵称已存在
				if(data.status == 200){
					alert('用户注册成功，请登录！');
					REGISTER.login();
				}else if(data.status == 401){
					alert('此用户名已经被占用，请选择其他用户名');
					$("#regName").select();
					$("#regName").focus();
				} else if(data.status == 402){
					alert('此手机号已经被注册！');
					$("#phone").select();
					$("#phone").focus();
				} else if(data.status == 403){
					alert('此电子邮箱已经被注册！');
					$("#email").select();
					$("#email").focus();
				}else if(data.status == 404){
					alert('此昵称已经被注册！');
					$("#nickName").select();
					$("#nickName").focus();
				} else {
					alert("注册失败！");
				}
			});
		},
		login:function() {
			 location.href = "/page/login";
			 return false;
		},
		reg:function() {
			//提交注册信息之前先校验数据的不为空
			if (this.inputcheck()) {
				//执行注册操作，根据返回的状态码判断数据是否通过
				this.doSubmit();
			}
		}
	};
</script>
</body>
</html>
