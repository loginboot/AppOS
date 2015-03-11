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
<div class="aps-barTools">
	<shiro:hasPermission name="system-appList-upd">
		<button class="aps-button" id="appGridUpd"><spring:message code="PUB.action2"/></button>
	</shiro:hasPermission>
	<button class="aps-button" id="appGridView"><spring:message code="PUB.view"/></button>
	<shiro:hasPermission name="system-appList">
		<button class="aps-button" onclick="AppOS.get('${ctx}/system/appList/export.do')"><spring:message code="PUB.excelExport"/></button>
	</shiro:hasPermission>
</div>

<!-- 应用 Script -->
<script type="text/javascript">
//表格ID
var GRID_ID = "#appGrid";
function search(type){
	if("all"==type){

	}
	$(GRID_ID).data("kendoGrid").pager(1);
}

//表格列信息
var cols=[{
	field:"appId",
	template:"#=appId#",
	title:'<spring:message code="PUB.id"/>',
	width:50
},{
	field:"chiName",
	template:"#=AppOS.dataConvert(chiName)#",
	title:'<spring:message code="PUB.name"/>',
	width:150
},{
	field:"engName",
	template:"#=AppOS.dataConvert(engName)#",
	title:'<spring:message code="PUB.engName"/>',
	width:150
},{
	field:"displayName",
	template:"#=AppOS.dataConvert(displayName)#",
	title:'<spring:message code="PUB.disName"/>',
	width:"100px"
},{
	field:"indexUrl",
	template:"#=AppOS.dataConvert(indexUrl)#",
	title:'<spring:message code="client.urlName"/>',
	width:"100px"
},{
	field:"version",
	template:"#=AppOS.dataConvert(version)#",
	title:'<spring:message code="PUB.version"/>',
	width:"100px"
},{
	field:"modifierUserName",
	template:"#=AppOS.dataConvert(modifierUserName)#",
	title:'<spring:message code="PUB.lst_mod_user"/>',
	width:100
},{
	field:"lastModifyDate",
	template:"#=AppOS.substr(lastModifyDate,19)#",
	title:'<spring:message code="PUB.lastModifyDate"/>',
	width:150
}];

//分页数据源
var dataSource =  new kendo.data.DataSource({//data source
	transport : {
		read : {
				url : "${ctx}/system/appList.do",
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