<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="../common.jsp" %>
<title><spring:message code="MENU.001090"/></title>
</head>
<body>

<!-- 查询表单  -->
<div class="aps-search">
	<form id="searchForm">
	<label><spring:message code="PUB.func" /></label>
	<select id="EQ_permission" name="search_EQ_permission" class="aps-textbox">
		<option selected="selected" value=""><spring:message code="PUB.all"/></option>
		<c:forEach items="${permissions}" var="per">
			<option value="${per.key}"><spring:message code="${fn:escapeXml(per.value) }"/></option>
		</c:forEach>
	</select>
	<label><spring:message code="PUB.action" /></label>
	<select id="EQ_type" name="search_EQ_type" class="aps-textbox input-small">
		<option selected="selected" value=""><spring:message code="PUB.all"/></option>
		<c:forEach items="${types }" var="type">
			<option value="${type.key}"><spring:message code="${fn:escapeXml(type.value) }" /></option>
		</c:forEach>
	</select>
	<label><spring:message code="PUB.userName" /></label>
	<input type="text" class="aps-textbox" name="search_RIGHTLIKE_user.name" value="${fn:escapeXml(requestScope['search_RIGHTLIKE_user.name'])}" />
	<label><spring:message code="PUB.content" /></label>
	<input type="text" class="aps-textbox" name="search_LIKE_details" value="${fn:escapeXml(seach_LIKE_details) }" />
	<button class="aps-button" type="button" onclick="search()"><spring:message code="PUB.search"/></button>
	<button class="aps-button" type="button" onclick="search('all')"><spring:message code="PUB.all"/></button>
	<button class="aps-button" type="button" onclick="AppOS.exportData('${ctx}/system/atlog/export.do','searchForm')">
	<spring:message code="PUB.excelExport"/>
	</button>
	</form>
	<strong>
		<spring:message code="PUB.location" />
		<i class="aps-arrow"></i>
		<span><spring:message code="MENU.001090"/></span>
	</strong>
</div>

<!-- Grid表格 -->
<div id="atlogGrid" class="aps-list"></div>

<!-- 应用 Script -->
<script type="text/javascript">
//表格ID
var GRID_ID = "#atlogGrid";
function search(type){
	if("all"==type){

	}
	$(GRID_ID).data("kendoGrid").pager(1);
}

//显示用户名称
function showUserName(user){
	if(user){
		return user.name;
	}
	return "";
}

//表格列信息
var cols = [{
	field:"name",
	template:"#=showUserName(user)#",
	title:'<spring:message code="PUB.userName"/>',
	width:"15%"
},{
	field:"operTime",
	template:"#=AppOS.substr(operTime,19)#",
	title:'<spring:message code="PUB.date"/>',
	width:"10%"
},{
	field:"type",
	template:"#=AppOS.showSelection('EQ_type',type)#",
	title:'<spring:message code="PUB.action"/>',
	width:"10%"
},{
	field:"permission",
	template:"#=AppOS.showSelection('EQ_permission',permission)#",
	title:'<spring:message code="PUB.func"/>',
	width:"10%"
},{
	field:"remoteHost",
	template:"#=AppOS.dataConvert(remoteHost)#",
	title:'<spring:message code="PUB.host"/>',
	width:"15%"
},{
	field:"details",
	template:"<div title='#=AppOS.dataConvert(details)#'>#=AppOS.dataConvert(details)#</div>",
	title:'<spring:message code="PUB.content"/>',
	width:"40%"
}];

//分页数据源
var dataSource =  new kendo.data.DataSource({//data source
	transport : {
		read : {
				url : "${ctx}/system/atlog.do",
				dataType : "json",
				type:"post",
				data:function() {
                    return $("#searchForm").serializeJson();
               }
		}
	},
	schema : { //对应接收参数转换为原始格式
		data : function(data) {
			return data.rows;
		},
		total : function(data) {
			return data.total;
		}
	},
	page:"${page}",
	pageSize : "${pageSize}",
	serverPaging : true
});

//init kendo grid and window
$(GRID_ID).kendoGrid({
		dataSource : dataSource,
		change : AppOS.onChange,
		dataBound : AppOS.ondatabound,
		selectable : "row",
		sortable : false,
		pageable: {
               input: true,
               numeric: false,
               refresh: true,
               pageSizes: AppOS.pageList
           },
	columns : cols});	


</script>

</body>
</html>