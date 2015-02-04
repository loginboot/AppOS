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
// 表格ID
var GRID_ID = "#roleGrid";
function search(type){
	if("all"==type){

	}
	$(GRID_ID).data("kendoGrid").pager(1);
}

// 表格列信息
var cols=[{
	field:"rid",
	template:"#=rid#",
	title:'<spring:message code="PUB.id"/>',
	width:100
},{
	field:"loginName",
	template:"#=AppOS.dataConvert(loginName)#",
	title:'<spring:message code="PUB.loginName"/>',
	width:150
}];

//分页数据源
var dataSource =  new kendo.data.DataSource({//data source
	transport : {
		read : {
				url : "${ctx}/system/role.do",
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
               pageSizes: Lyods.pageList
           },
	columns : cols});	


</script>


</body>
</html>