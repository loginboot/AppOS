<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="../common.jsp" %>
<title><spring:message code="MENU.001020"/></title>
</head>
<body>

<!-- 查询表单  -->
<div class="aps-search">
	<form>
		<label><spring:message code="PUB.name" /></label>
		<input type="text" class="aps-textbox" name="search_RIGHTLIKE_name" value="${fn:escapeXml(search_RIGHTLIKE_name) }" />
		<label><spring:message code="PUB.loginName" /></label>
		<input type="text" class="aps-textbox" name="search_RIGHTLIKE_loginName" value="${fn:escapeXml(search_RIGHTLIKE_loginName) }"  />
		<button type="button" class="aps-button" onclick="search()"><spring:message code="PUB.search"/></button>
		<button type="button" class="aps-button" onclick="search('all')"><spring:message code="PUB.all"/></button>
	</form>
	<strong>
		<spring:message code="PUB.location" />
		<i class="aps-arrow"></i>
		<span><spring:message code="MENU.001020"/></span>
	</strong>
</div>

<!-- 表格列表 -->
<div id="userGrid" class="aps-list"></div>

<!-- 操作按钮 -->
<div class="aps-barTools">
	<shiro:hasPermission name="system-user-add">
	<button class="aps-button" id="userGridAdd"><spring:message code="PUB.add"/></button>
	</shiro:hasPermission>
	
	<shiro:hasPermission name="system-user-upd">
	<button class="aps-button" id="userGridUpd"><spring:message code="PUB.upd"/></button>
	</shiro:hasPermission>
	
	<shiro:hasPermission name="system-user-del">
	<button class="aps-button" id="userGridDel" ><spring:message code="PUB.del"/></button>
	</shiro:hasPermission>	
	
	<button class="aps-button" id="userGridView" ><spring:message code="PUB.view"/></button>	
</div>

<!--  -->
<script type="text/javascript">
// 表格ID
var GRID_ID = "#userGrid";

// 查询
function search(type){
	if("all"==type){

	}
	$(GRID_ID).data("kendoGrid").pager(1);
}

// 表格列模块
var cols=[{
	field:"userId",
	template:"#=userId#",
	title:'<spring:message code="PUB.id"/>',
	width:100
},{
	field:"name",
	template:"#=AppOS.dataConvert(name)#",
	title:'<spring:message code="PUB.name"/>',
	width:150
},{
	field:"loginName",
	template:"#=AppOS.dataConvert(loginName)#",
	title:'<spring:message code="PUB.loginName"/>',
},{
	field:"email",
	template:"#=AppOS.dataConvert(email)#",
	title:'<spring:message code="PUB.email"/>',
},{
	field:"phone",
	template:"#=AppOS.dataConvert(phone)#",
	title:'<spring:message code="PUB.phone"/>',
},{
	field:"mobile",
	template:"#=AppOS.dataConvert(mobile)#",
	title:'<spring:message code="PUB.mobile"/>',
},{
	field:"fax",
	template:"#=AppOS.dataConvert(fax)#",
	title:'<spring:message code="PUB.fax"/>',
},{
	field:"status",
	template:"#=AppOS.showSelection('search_EQ_status',status)#",
	title:'<spring:message code="PUB.status"/>',
},{
	field:"modifierUserName",
	template:"#=AppOS.dataConvert(modifierUserName)#",
	title:'<spring:message code="PUB.lst_mod_user"/>'
},{
	field:"lastModifyDate",
	template:"#=AppOS.substr(lastModifyDate,16)#",
	title:'<spring:message code="PUB.lastModifyDate"/>',
	width:"150px"
}];

//分页数据源
var dataSource =  new kendo.data.DataSource({//data source
	transport : {
		read : {
				url : "${ctx}/system/user.do",
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
		dataBound:AppOS.ondatabound,
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