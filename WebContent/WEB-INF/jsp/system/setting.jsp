<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="../common.jsp" %>
<title><spring:message code="PUB.setting" /></title>
</head>
<body>

<!-- 系统设置界面 -->
<div class="aps-content">
	<!-- 用户信息设置 -->
	<div class="aps-window">
		<h3 class="aps-form-title"><spring:message code="PUB.userinfo"/> - <spring:message code="PUB.upd" /></h3>
		<form id="infoForm" action="${ctx }/system/params/${action }.do" method="post">
			<div class="globalErrSpan"></div>
			<input type="hidden" name="appList.appId"  value="${user.appList.appId}"/>
			<input type="hidden" name="userId"  value="${user.userId}"/>
			<input type="hidden" name="lastModifyDate" value="${user.lastModifyDate}"/>
			<input type="hidden" name="password" value="${user.password}"/>
			<input type="hidden" id="loginName" name="loginName" value="${fn:escapeXml(user.loginName) }"/>
			<div class="aps-rows">
				<label for="name" class="aps-label"><spring:message code="PUB.name" /></label>
				<input type="text" id="name" name="name"
				value="${fn:escapeXml(user.name) }" class="aps-textbox" required />
			</div>
			<div class="aps-rows">
				<label for="loginName" class="aps-label"><spring:message code="PUB.loginName" /></label>
				<label class="aps-textbox">
				${fn:escapeXml(user.loginName) }</label>
			</div>
			<div class="aps-rows">
				<label for="email" class="aps-label"><spring:message code="PUB.email"/></label>
				<input type="email" id="email" 
				name="email" value="${fn:escapeXml(user.email) }" class="aps-textbox" />
			</div>
			<div class="aps-rows">
				<label for="phone" class="aps-label"><spring:message code="PUB.phone"/></label>
				<input type="text" id="phone" 
				name="phone" value="${fn:escapeXml(user.phone) }" class="aps-textbox"  maxlength="50"/>
			</div>	
			<div class="aps-rows">
				<label for="mobile" class="aps-label"><spring:message code="PUB.mobile"/></label>
				<input type="text" id="mobile" 
				name="mobile" value="${fn:escapeXml(user.mobile) }" class="aps-textbox" maxlength="30"/>
			</div>
			<div class="aps-rows">
				<label for="fax" class="aps-label"><spring:message code="PUB.fax"/></label>
				<input type="text" id="fax"
				 name="fax" value="${fn:escapeXml(user.fax) }" class="aps-textbox" maxlength="50"/>
			</div>
			<div class="aps-rows">
				<label for="address" class="aps-label aps-vtop"><spring:message code="PUB.address"/></label>
				<textarea id="address" rows="3"
				 name="address" class="aps-textbox"  maxlength="300" >${fn:escapeXml(user.address) }</textarea>
			</div>	
			<div class="aps-action">
				<button type="submit" class="aps-button"><spring:message code="PUB.ok"/></button>
				<button type="button" class="aps-button" onclick="history.back()"><spring:message code="PUB.return"/></button>
			</div>
		</form>
	</div>
	
	<br />
	<!-- 用户密码设置 -->
	<div class="aps-window">
		<h3 class="aps-form-title"><spring:message code="param.pwdControl"/> - <spring:message code="PUB.upd" /></h3>
		<form id="secForm" action="${ctx }/system/params/${action }.do" method="post">
			<div class="globalErrSpan"></div>
			<input type="hidden" name="userId" value="${user.userId}"/>
			<div class="aps-rows">
				<label for="oldpassword" class="aps-label"><spring:message code="PUB.oldpwd"/></label>
				<input type="password" id="oldpassword" 
				name="oldpassword" class="aps-textbox" required maxlength="50"/>			   
			</div>
	        <div class="aps-rows">
				<label for="password" class="aps-label"><spring:message code="PUB.newpwd"/></label>
				<input type="password" id="password" 
				name="password" class="aps-textbox" validPwd=true beDifferentToOld=true required maxlength="50"/>			   
			</div>
			<div class="aps-rows">
				<label for="ConfirmPassword" class="aps-label"><spring:message code="user.confPwd"/></label>
				<input type="password"
				 id="ConfirmPassword" name="ConfirmPassword" equalTo="#password" class="aps-textbox" required maxlength="50"/>					
			</div>
	        <div class="aps-action">
				<button type="submit" class="aps-button"><spring:message code="PUB.ok"/></button>
				<button type="reset" class="aps-button" onclick="AppOS.resetForm('secForm');"><spring:message code="PUB.reset"/></button>
			</div>
		</form>
	</div>
</div>

<!-- 用户信息Script -->
<script type="text/javascript">
//用户个人信息设置
$("#infoForm").validate({			
	submitHandler: function(form){
		jQuery(form).ajaxSubmit({
			type:"post",
			dataType : 'json',
			success : function(data){
				//成功也进行提示
				var isOk = appAjaxCheck(data);
				if(isOk){
					AppOS.msgBox(data.retmsg.MSG,AppOS.constant["MSG_BOX_SUCCESS"],function(){
						AppOS.get("${ctx}/system/setting.do");
					});
					return;
				}				
			},
			error : appAjaxError
		});
	}			
});

// 密码级别控制校验
var PWD_MIN_LENGTH = parseInt("${PWD_MIN_LENGTH}"); //密码最小长度
var PWD_MIN_ALPHA_CHAR = parseInt("${PWD_MIN_ALPHA_CHAR}"); //最少字母个数
var PWD_MIN_NUM_CHAR = parseInt("${PWD_MIN_NUM_CHAR}"); //最少数字个数
var PWD_MIN_SPECIAL_CHAR = parseInt("${PWD_MIN_SPECIAL_CHAR}"); //最少特殊字符个数
var PWD_CONTAIN_UPPER_CHAR = "${PWD_CONTAIN_UPPER_CHAR}"; //必须包括大小写
// 密码校验的错误提示
function getErrorStr() {
	var str = "<spring:message code='pwdValidError' /> ";
	var hasCheck = false;
	if (PWD_MIN_LENGTH > 0) {
		str += PWD_MIN_LENGTH + " <spring:message code='length' />";
		hasCheck = true;
	}
	if (PWD_MIN_ALPHA_CHAR > 0) {
		if (hasCheck) {
			str += ", ";
		}
		str += PWD_MIN_ALPHA_CHAR
				+ " <spring:message code='letters' />";
		hasCheck = true;
	}
	if (PWD_MIN_NUM_CHAR > 0) {
		if (hasCheck) {
			str += ", ";
		}
		str += PWD_MIN_NUM_CHAR + " <spring:message code='degits' /> ";
		hasCheck = true;
	}
	if (PWD_MIN_SPECIAL_CHAR > 0) {
		if (hasCheck) {
			str += ", ";
		}
		str += PWD_MIN_SPECIAL_CHAR
				+ " <spring:message code='specialCharracters' />";
		hasCheck = true;
	}
	if (PWD_CONTAIN_UPPER_CHAR == "true") {
		if (hasCheck) {
			str += ", ";
		}
		str += "<spring:message code='containSensitive' />";
		hasCheck = true;
	}
	return str;
}
// 密码的Vaild
$.validator.addMethod("validPwd", function(value, element) {
	if(value.length < PWD_MIN_LENGTH) return false;
	if(AppOS.getAlphaCharSize(value) < PWD_MIN_ALPHA_CHAR) return false;
	if(AppOS.getNumberSize(value) < PWD_MIN_NUM_CHAR) return false;
	if(AppOS.getSpecialCharSize(value) < PWD_MIN_SPECIAL_CHAR) return false;
	if(PWD_CONTAIN_UPPER_CHAR=="true"){
		return AppOS.isContainSensitive(value);
	}		
	return true;
}, getErrorStr());

$.validator.addMethod("beDifferentToOld", function(value, element) {
	if(value == $("#oldpassword").val()) return false;
	return true;
}, "<spring:message code='beDifferentToOld' />");

//用户密码设置
$("#secForm").validate({			
	submitHandler: function(form){
		jQuery(form).ajaxSubmit({
			type:"post",
			dataType : 'json',
			success : function(data){
				//成功也进行提示
				var isOk = lyodsAjaxCheck(data);
				if(isOk){
					AppOS.msgBox(data.retmsg.MSG,AppOS.constant["MSG_BOX_SUCCESS"],function(){
						AppOS.get("${ctx}/system/setting.do");
					});
					return;
				}				
			},
			error : lyodsAjaxError
		});
	}			
});

</script>

</body>
</html>