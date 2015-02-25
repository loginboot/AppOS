<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="../common.jsp" %>
<title><spring:message code="MENU.001030"/></title>
</head>
<body>
<div class="aps-content">
	<div class="aps-window">
		<h3 class="aps-form-title"><spring:message code="MENU.001030"/> - <spring:message code="PUB.${action }"/></h3>
		<form id="userForm" action="${ctx }/system/user/${action}.do" method="post">
			<input type="hidden" name="userId" value="${user.userId }" />
			<input type="hidden" name="lastModifyDate" value="${user.lastModifyDate }" />
			<input type="hidden" name="appList.appId" value="${user.appList.appId }" />
			
			<div class="aps-rows">
				<label class="aps-label"><spring:message code="PUB.resetPwd"/></label>
				<button id="resetPwdBtn" class="aps-button" type="button"
				 onclick="resetPwd()" ><spring:message code="PUB.resetPwd"/></button>
			    <label id="newPwd"></label>
			</div>
			
			<div class="aps-rows aps-group">
				<label class="aps-label" for="name"><spring:message code="PUB.name"/></label>
				<input type="text" id="name" name="name" value="${fn:escapeXml(user.name) }" class="aps-textbox"
				required="true" ${disabled } />
			</div>
			
			<div class="aps-rows aps-group">
				<label class="aps-label" for="loginName"><spring:message code="PUB.loginName" /></label>
				<input type="text" id="loginName" name="loginName" value="${fn:escapeXml(user.loginName) }" class="aps-textbox"
				required="true" ${disabled } />
			</div>
			
			<div class="aps-rows aps-group" <c:if test="${action!='add'}">style="display:none;"</c:if>>
				<label class="aps-label" for="password"><spring:message code="PUB.password" /></label>
				<input type="password" id="password" name="password"
				value="${fn:escapeXml(user.password) }" class="aps-textbox"  ${disabled } required />
			</div>
			<div class="aps-rows aps-group" <c:if test="${action!='add'}">style="display:none;"</c:if>>
				<label for="ConfirmPassword" class="aps-label"><spring:message code="user.confPwd"/></label>
				<input type="password" id="ConfirmPassword" name="ConfirmPassword"
				value="${fn:escapeXml(user.password) }" class="aps-textbox"  ${disabled } required equalTo="#password" />
			</div>
			
			<div class="aps-rows aps-group">
				<label for="rid" class="aps-label"><spring:message code="user.userRole"/></label>
				<select class="aps-textbox" name="rid" id="rid" ${disabled }>
					<c:forEach items="${roles }" var="role">
						<option value="${role.rid }">${fn:escapeXml(role.name) }</option>					
					</c:forEach>
				</select>
			</div>
			<div class="aps-rows aps-group">
				<label class="aps-label"><spring:message code="PUB.status"/></label>
				<select  id="status" name="status" required class="aps-textbox" ${disabled }>
				   <c:forEach items="${statusMap }" var="sm">
				   	<option value="${sm.key }" <c:if test="${user.status==sm.key}">selected</c:if>><spring:message code="${sm.value }"/></option>
				   </c:forEach>
				</select>	
				<label id="lockReason" class="aps-lock-reason">${lockReason }</label>
			</div>
			
			<div class="aps-rows aps-group">
				<label for="email" class="aps-label"><spring:message code="PUB.email"/></label>
				<input type="email" id="email" 
				name="email" value="${fn:escapeXml(user.email) }" class="aps-textbox" maxlength="100" ${disabled } />
			</div>
			<div class="aps-rows aps-group">
				<label for="phone" class="aps-label"><spring:message code="PUB.phone"/></label>
				<input type="text" id="phone" 
				name="phone" value="${fn:escapeXml(user.phone) }" class="aps-textbox"  maxlength="50" ${disabled } />
			</div>	
			<div class="aps-rows aps-group">
				<label for="mobile" class="aps-label"><spring:message code="PUB.mobile"/></label>
				<input type="text" id="mobile" 
				name="mobile" value="${fn:escapeXml(user.mobile) }" class="aps-textbox" maxlength="30" ${disabled } />
			</div>
			<div class="aps-rows aps-group">
				<label for="fax" class="aps-label"><spring:message code="PUB.fax"/></label>
				<input type="text" id="fax"
				 name="fax" value="${fn:escapeXml(user.fax) }" class="aps-textbox" maxlength="50" ${disabled } />
			</div>
			<div class="aps-rows aps-group">
				<label for="address" class="aps-label aps-vtop"><spring:message code="PUB.address"/></label>
				<textarea id="address" rows="3"
				 name="address" class="aps-textbox"  maxlength="300" ${disabled } >${fn:escapeXml(user.address) }</textarea>
			</div>	
			
			<div class="aps-clear"></div>
			<div class="aps-action">
				<c:if test="${action!='view' }">
				<button type="submit" class="aps-button"><spring:message code="PUB.ok"/></button>
				</c:if>
				<button type="button" class="aps-button" onclick="AppOS.get('${ctx}/system/user.do');">
				<spring:message code="PUB.return"/>
				</button>
			</div>
		</form>
	</div>
</div>

<script type="text/javascript">
//聚焦第一个输入框
$("input[type='text']:visible:enabled:first").focus();

// 表单检验提交
$("#userForm").validate({		
	rules:{	
		loginName:{
			pattern : /^\w+$/,
			remote:"${ctx}/system/user/checkUserName.do?&userId=${user.userId}&rowNum="+Math.random()
		}
	},
	messages:{
		loginName:{
			remote:"<spring:message code='ERRCODE.3104'/>",
			pattern : "<spring:message code='PUB.engOrDigitalOrUnline'/>"
		}
	},
	submitHandler: function(form){
		$(form).ajaxSubmit({
			type:"post",
			dataType : 'json',
			success : function(data){
				//成功也进行提示
				var isOk = appAjaxCheck(data);
				if(isOk){
					AppOS.get("${ctx}/system/user.do");
					return;
				}				
			},
			error : appAjaxError
		});
	}
});

//重置密碼
function resetPwd(){
	$.ajax({
		type : 'POST',
		url : '${ctx}/system/user/resetPwd.do?userId=${user.userId}',
		dataType : 'json',
		success : function(data) {
			var isOk = appCheckJsonData(data);
			if (isOk){
				$("#newPwd").html("<spring:message code="user.resetinf"/><font color='red'>" + data.resetpwd+"</font>");
			}
		},
		error : appAjaxError
	});
}

</script>
</body>
</html>