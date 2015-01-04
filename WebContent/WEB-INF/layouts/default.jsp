<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<head>
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<title><sitemesh:title/></title>
<sitemesh:head/>
</head>
<body >
	<%@ include file="/WEB-INF/layouts/header.jsp"%>
	<sitemesh:body/>
	<%@ include file="/WEB-INF/layouts/footer.jsp"%>
</body>
</html>