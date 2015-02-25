<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="../common.jsp" %>
<title><spring:message code="MENU.001050"/></title>
</head>
<body>

<!-- 查询表单  -->
<div class="aps-search">
	<strong>
		<spring:message code="PUB.location" />
		<i class="aps-arrow"></i>
		<span><spring:message code="MENU.001050"/></span>
	</strong>
</div>

<!-- Grid表格 -->
<div id="appGrid" class="aps-list"></div>

<!-- BarTools 操作按钮 -->
<div class="aps-barTools"></div>

<!-- 应用 Script -->
<script type="text/javascript">


</script>

</body>
</html>