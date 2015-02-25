<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="../common.jsp" %>
<title><spring:message code="MENU.001040"/></title>
</head>
<body>

<div class="aps-content">
	<div class="aps-window">
		<h3 class="aps-form-title"><spring:message code="MENU.001040"/> - <spring:message code="PUB.${action }"/></h3>
		<form id="paramsForm" action="${ctx }/system/params/${action }.do" method="post">
			<div class="globalErrSpan"></div>
			<c:forEach items="${appParams}" var="p">
			<div class="aps-rows">
				<label for="${p.name}" class="aps-label aps-larger"><spring:message code="${p.notes}"/></label>
				<input type="hidden" name="pid_${p.name}" value="${p.pid}" />
				<input type="<c:choose><c:when test="${p.name == 'mail.password'}">password</c:when><c:otherwise>text</c:otherwise></c:choose>"
				 id="${p.name }" name="${p.name}" value="${fn:escapeXml(p.value) }" class="aps-textbox aps-larger" ${p.pattern } ${disabled }/>
			</div>
			</c:forEach>
			
			<div class="aps-clear"></div>
			<div class="aps-action aps-pad-larger">
				<shiro:hasPermission name="system-params-upd">
					<button type="submit" class="aps-button" ><spring:message code="PUB.ok"/></button>
				</shiro:hasPermission>
				<button type="reset" class="aps-button"><spring:message code="PUB.reset"/></button>
			</div>
		</form>
	</div>
	
	<br />
	<div class="aps-window">
		<h3 class="aps-form-title"><spring:message code="param.pwdControl"/> - <spring:message code="PUB.${action }"/></h3>
		<form id="pwdControlForm" action="${ctx }/system/params/pwdControl/${action }.do" method="post">
			<div class="globalErrSpan"></div>
			<c:forEach items="${pwdControlParams}" var="p">
			<div class="aps-rows">
				<label for="${p.name}" class="aps-label aps-larger"><spring:message code="${p.notes}"/></label>
				<input type="hidden" name="pid_${p.name}" value="${p.pid}" />
				<c:choose>
					<c:when test="${p.name == 'PWD_CONTAIN_UPPER_CHAR'}">
						<input type="radio" name="${p.name }" value="true" ${disabled } <c:choose><c:when test="${p.value == 'true'}">checked</c:when></c:choose> /><spring:message code="PUB.yes"></spring:message>																							
                   	    <input type="radio" name="${p.name }" value="false" ${disabled } <c:choose><c:when test="${p.value == 'false'}">checked</c:when></c:choose> /><spring:message code="PUB.no"></spring:message>			
					</c:when>
					<c:otherwise>
						<input type="text" id="${p.name}" name="${p.name}" value="${fn:escapeXml(p.value) }" class="aps-textbox aps-larger"
						 ${p.pattern } ${disabled } />
					</c:otherwise>
				</c:choose>
			</div>
			</c:forEach>
			
			<div class="aps-clear"></div>
			<div class="aps-action aps-pad-larger">
				<shiro:hasPermission name="system-params-upd">
					<button type="submit" class="aps-button" ><spring:message code="PUB.ok"/></button>
				</shiro:hasPermission>
				<button type="reset" class="aps-button"><spring:message code="PUB.reset"/></button>
			</div>
		</form>
	</div>
</div>

<!-- 系统参数 Script -->
<script type="text/javascript">
// 选择一个可编辑INPUT
$("input[type='text']:visible:enabled:first").focus();
// 判断Mail发送是否需要认证
var needAuth = function() {
	return $("#mail\\.smtp\\.auth").val() == "true";
};
// 自定义Valid校验函数，判断Mail的具体填写是否正确
$.validator.addMethod("trueOrFalse", function(value, element) {
	if($.trim(value)==""){
		return true;
	}
	if (value != "true" && value != "false") {
		return false;
	}
	return true;
}, '<spring:message code="PUB.trueOrFalse"/>');

// 配置系统基本参数修改的数据校验
$("#paramsForm").validate({
	rules : {
		"mail.from":{
			required:needAuth
		},
		"mail.smtp.user":{
			required:needAuth
		},
		"mail.password":{
			required:needAuth
		}
	},
	submitHandler : function(form) {
		jQuery(form).ajaxSubmit({
			type : "post",
			dataType : 'json',
			success : function(data) {
				//成功也进行提示
				var isOk = appAjaxCheck(data,form);
				if (isOk) {
					AppOS.msgBox(data.retmsg.MSG,AppOS.constant["MSG_BOX_SUCCESS"],function(){
						AppOS.get("${ctx}/system/params.do");
					});
					return;
				}
			},
			error : appAjaxError
		});
	}
});

// 配置系统密码安全等级修改的数据校验
$("#pwdControlForm").validate({
	submitHandler : function(form) {
		jQuery(form).ajaxSubmit({
			type : "post",
			dataType : 'json',
			success : function(data) {
				//成功也进行提示
				var isOk = appAjaxCheck(data,form);
				if (isOk) {
					alert(data.retmsg.MSG);
					AppOS.get("${ctx}/system/param.do");
					return;
				}
			},
			error : appAjaxError
		});
	}
});

//默认选择false
var radioChecked = $("input[name='PWD_CONTAIN_UPPER_CHAR']:checked").length;
if (radioChecked == 0) {
	$("input[name='PWD_CONTAIN_UPPER_CHAR']")[1].checked = true;
}
</script>

</body>
</html>