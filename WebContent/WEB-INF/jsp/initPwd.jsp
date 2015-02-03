<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- 密码初始化表单 -->
<div id="initWin" style="display: none;">
	<form id="initPwdForm" action="${ctx }/initPwd.do" method="post" class="aps-form">
		<input id="userId" type="hidden" name="userId"/>
		<fieldset class="aps-fieldset">
			<legend><small><spring:message code="PUB.sercurity"/></small></legend>		
			<div class="aps-rows">
				<label for="oldpassword" class="aps-label"><spring:message code="PUB.oldpwd"/></label>
				<input type="password" id="oldpassword" name="oldpassword" class="aps-textbox" required maxlength="50"/>			   
			</div>
	        <div class="aps-rows">
				<label for="password" class="aps-label"><spring:message code="PUB.newpwd"/></label>
				<input type="password" id="password" name="password" class="aps-textbox" validPwd="true" beDifferentToOld="true" required maxlength="50"/>			   
			</div>
			<div class="aps-rows">
				<label for="ConfirmPassword" class="aps-label"><spring:message code="user.confPwd"/></label>
				<input type="password" id="ConfirmPassword" name="ConfirmPassword" equalTo="#password" class="aps-textbox" required maxlength="50"/>					
			</div>
	        <div class="aps-action">
				<button type="submit" class="aps-button"><spring:message code="PUB.ok"/></button>
				<button type="reset" class="aps-button"><spring:message code="PUB.reset"/></button>
			</div>
		</fieldset>
	</form>
</div>

<!-- 密码初始化Script -->
<script type="text/javascript">
var initPwd = "<shiro:principal property='initPwd'/>";
//密码是否初始化
if("1"===initPwd){
	$("#userId").val("<shiro:principal property='userId'/>");
	var errorReason = "<spring:message code='<%=ExpStatusEnum.lockReasonMap.get(Constant.USER_PWD_HAS_EXPIRED)%>' />";
	var titleStr = '<spring:message code="PUB.sercurity"/>';
	
	//init kendo searchForm and windows
	var init_win = $("#initWin").kendoWindow({
		draggable : false,	
		minWidth : "500px",
		minHeight: "150px",
		visible : false,
		modal : true,
		actions : [ "Maximize" ]
	}).data("kendoWindow");
	
	init_win.title(titleStr);
	init_win.center();
	init_win.open();
	
	// 密码级别控制校验
	var PWD_MIN_LENGTH = parseInt("${PWD_MIN_LENGTH}"); //密码最小长度
	var PWD_MIN_ALPHA_CHAR = parseInt("${PWD_MIN_ALPHA_CHAR}"); //最少字母个数
	var PWD_MIN_NUM_CHAR = parseInt("${PWD_MIN_NUM_CHAR}"); //最少数字个数
	var PWD_MIN_SPECIAL_CHAR = parseInt("${PWD_MIN_SPECIAL_CHAR}"); //最少特殊字符个数
	var PWD_CONTAIN_UPPER_CHAR = "${PWD_CONTAIN_UPPER_CHAR}"; //必须包括大小写
	// 密码校验的错误提示
	function getErrorStr() {
		var str = "<spring:message code='PUB.pwdValidError' /> ";
		var hasCheck = false;
		if (PWD_MIN_LENGTH > 0) {
			str += PWD_MIN_LENGTH + " <spring:message code='PUB.length' />";
			hasCheck = true;
		}
		if (PWD_MIN_ALPHA_CHAR > 0) {
			if (hasCheck) {
				str += ", ";
			}
			str += PWD_MIN_ALPHA_CHAR
					+ " <spring:message code='PUB.letters' />";
			hasCheck = true;
		}
		if (PWD_MIN_NUM_CHAR > 0) {
			if (hasCheck) {
				str += ", ";
			}
			str += PWD_MIN_NUM_CHAR + " <spring:message code='PUB.degits' /> ";
			hasCheck = true;
		}
		if (PWD_MIN_SPECIAL_CHAR > 0) {
			if (hasCheck) {
				str += ", ";
			}
			str += PWD_MIN_SPECIAL_CHAR
					+ " <spring:message code='PUB.specialCharracters' />";
			hasCheck = true;
		}
		if (PWD_CONTAIN_UPPER_CHAR == "true") {
			if (hasCheck) {
				str += ", ";
			}
			str += "<spring:message code='PUB.containSensitive' />";
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
	}, "<spring:message code='PUB.beDifferentToOld' />");
	
	//Valid校验
	$("#initPwdForm").validate({
		submitHandler : function(form) {
			jQuery(form).ajaxSubmit({
				type : "post",
				dataType : 'json',
				success : function(data) {
					var isOk = appAjaxCheck(data);
					if (isOk == true) {
						init_win.close();
						window.location.reload();
						return;
					}
				},
				error : appAjaxError
			});
		}
	});
}
</script>