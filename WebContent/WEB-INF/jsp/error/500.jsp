<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.apache.log4j.Logger" %>
<%
	Throwable ex = null;
	if (exception != null)
		ex = exception;
	if (request.getAttribute("javax.servlet.error.exception") != null)
		ex = (Throwable) request.getAttribute("javax.servlet.error.exception");

	//记录日志
	Logger logger = Logger.getLogger("500.jsp");
	logger.error(ex.getMessage(), ex);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="${pageContext.request.contextPath}/statics/styles/lyods.css" type="text/css" rel="stylesheet" />
	<title>500</title>
</head>
<body>
<div class="ly-500-bg">
	<div class="ly-500-content">
		<a class="ly-500-btn" href="${pageContext.request.contextPath}">Return</a>
	</div>
</div>
</body>
</html>