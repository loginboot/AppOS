<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="../common.jsp" %>
<title><spring:message code="MENU.002000"/></title>
</head>
<body>

<div class="aps-search">
	<form></form>
	<strong></strong>
</div>

<!-- LIST GRID -->
<div id="userGrid" class="aps-list"></div>

<!-- ACTION BAR -->
<div class="aps-barTools">

</div>

<script type="text/javascript">
var GRID_ID = "#userGrid";

var cols=[{
	field:"userId",
	template:"#=userId#",
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
               pageSizes: Lyods.pageList
           },
	columns : cols});
</script>
</body>
</html>