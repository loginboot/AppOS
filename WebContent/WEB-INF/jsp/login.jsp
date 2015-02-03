<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="common.jsp" %>
<title><spring:message code="PUB.login"/></title>
</head>
<body>

<div class="aps-login-body"></div>

<!-- LOGIN 登陆表单 -->
<div class="aps-login">
	<!-- 透明背景 -->
	<div class="aps-login-opa"></div>
	<form id="loginForm" class="aps-login-form" action="${ctx }/login.htm" method="post">
		<input type="hidden" name="appId" value="1" />
		<div>
			<p><spring:message code="PUB.loginName" /></p>
			<input type="text" name="username" required="true" />
		</div>
		<div>
			<p><spring:message code="PUB.password" /></p>
			<input type="password" name="password" required="true" />
		</div>
		<div>
			<p>
			<input type="checkbox" name="readMe" />
			<spring:message code="PUB.rememberMe" />
			</p>
		</div>
		<div class="aps-login-action">
			<button type="submit" class="aps-login-btn">Sumbit</button>
		</div>
	</form>
</div>
<!-- 登录Script -->
<script type="text/javascript">
//获取第一个input焦点
$("input[type='text']:visible:enabled:first").focus();
//valid加载
$("#loginForm").validate();
</script>
</body>
</html>