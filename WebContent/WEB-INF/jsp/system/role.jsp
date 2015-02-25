<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="../common.jsp" %>
<title><spring:message code="MENU.001020"/></title>
</head>
<body>
<!-- 查询 -->
<div class="aps-search">
	<form>
		<label><spring:message code="role.name" /></label>
		<input type="text" class="aps-textbox" name="search_RIGHTLIKE_name" value="${fn:escapeXml(search_RIGHTLIKE_name) }" />
		<label><spring:message code="role.desc" /></label>
		<input type="text" class="aps-textbox" name="search_RIGHTLIKE_description" value="${fn:escapeXml(search_RIGHTLIKE_description) }"  />
		<button type="button" class="aps-button"><spring:message code="PUB.search"/></button>
		<button type="button" class="aps-button"><spring:message code="PUB.all"/></button>
	</form>
	<strong>
		<spring:message code="PUB.location" />
		<i class="aps-arrow"></i>
		<span><spring:message code="MENU.001020"/></span>
	</strong>
</div>

<!-- 表格列表 -->
<div class="aps-list" id="roleGrid"></div>

<!-- 操作按钮 -->
<div class="aps-barTools">
	<shiro:hasPermission name="system-role-add">
	<button class="aps-button" id="roleGridAdd"><spring:message code="PUB.add"/></button>
	</shiro:hasPermission>
	
	<shiro:hasPermission name="system-role-upd">
	<button class="aps-button" id="roleGridUpd"><spring:message code="PUB.upd"/></button>
	</shiro:hasPermission>
	
	<shiro:hasPermission name="system-role-del">
	<button class="aps-button" id="roleGridDel" ><spring:message code="PUB.del"/></button>
	</shiro:hasPermission>	
	
	<button class="aps-button" id="roleGridView" ><spring:message code="PUB.view"/></button>	
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
	field:"name",
	template:"#=AppOS.dataConvert(name)#",
	title:'<spring:message code="role.name"/>',
	width:150
},{
	field:"description",
	template:"#=AppOS.dataConvert(description)#",
	title:'<spring:message code="role.desc"/>',
	width:250
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

// ---------角色信息操作函数--------------------
// 角色新增
$(GRID_ID+"Add").click(function(){
	AppOS.get("${ctx}/system/role/add.do");
});

// 角色修改或查看
$(GRID_ID+"Upd,"+GRID_ID+"View").click(function(){
	if(!GRID.rid){
		alert("<spring:message code='PUB.nonselected'/>");
		return;
	}
	var path = "view";
	if(GRID_ID+"Upd"=="#"+this.id){
		path = "upd";
	}
	AppOS.get("${ctx}/system/role/"+path+"/"+GRID.rid+".do");
});

// 角色删除
$(GRID_ID+"Del").click(function(){
	if(!GRID.rid){
		alert("<spring:message code='PUB.nonselected'/>");
		return;
	}
	if(confirm("<spring:message code='PUB.confirmDelete'/>")){
		jQuery.ajax({
			type : 'POST',
			url : '${ctx}/system/role/del.do',
			dataType : 'json',
			data : {id:GRID.rid},
			success : function(data) {
				var isOk = appAjaxCheck(data);
				if(isOk==true){
					dataSource.read();
					return;
				}		
			},
			error : appAjaxError
		});
	}
});
</script>
</body>
</html>