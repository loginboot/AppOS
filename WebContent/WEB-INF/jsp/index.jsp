<%@ page language="java" contentType="text/html; charset=UTF-8"  import="com.xsw.constant.*"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="common.jsp" %>
<title>
<c:choose>
<c:when test="${locale=='en_US' }">${SESSION_APP_LIST.appList.engName }</c:when>
<c:otherwise>${SESSION_APP_LIST.appList.chiName }</c:otherwise>
</c:choose>
</title>
</head>
<body>
<div class="aps-index">
	<img alt="" src="${ctx }/statics/styles/img/index.png" width="990" />
</div>

<!-- 初始化密码 -->
<%@include file="initPwd.jsp" %>
</body>
</html>