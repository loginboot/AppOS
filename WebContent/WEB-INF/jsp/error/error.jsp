<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><spring:message code="PUB.error"/></title>
<%@include file="../common.jsp" %>
</head>
<body>
<div class="ly-error-bg">
	<div class="ly-error-text">
		<strong><spring:message code="PUB.error"/>:</strong>
		<p style="text-indent: 20px; font-size: 14px;">${fn:escapeXml(errorMsg)}</p>
	</div>
	<div class="ly-error-ret">
		<a class="ly-btn-signin"  href="<c:url value="/${returnUrl}"/>"><spring:message code="PUB.return"/></a>
	</div>
</div>
</body>
</html>