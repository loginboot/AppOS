<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="../common.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="MENU.002000"/></title>
</head>
<body>
<!-- 查询 -->
<div class="aps-search">
	<form>
		<label></label>
		<input type="text" class="aps-textbox" name="" />
		<label></label>
		<input type="text" class="aps-textbox" name="" />
		<button type="button" class="aps-button"><spring:message code="PUB.search"/></button>
		<button type="button" class="aps-button"><spring:message code="PUB.all"/></button>
	</form>
	<strong>
		
	</strong>
</div>

<!-- 表格列表 -->
<div class="aps-list" id="roleGrid"></div>

<!-- 操作按钮 -->
<div class="aps-barTools">
	
</div>


<script type="text/javascript">
	


</script>


</body>
</html>