<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="../common.jsp"%>
<title><spring:message code="MENU.001010" /></title>
</head>
<body>

<div class="aps-content">
	<div class="aps-window">
		<h3 class="aps-form-title">
			<spring:message code="MENU.001010" /> - <spring:message code="PUB.${action}" />
		</h3>
		<form id="menuForm" action="${ctx }/system/menu/${action}.do" method="post">
			<div class="aps-rows">
				<label class="aps-label"><spring:message code="PUB.app" /></label>
				<select id="appId" name="appId" class="aps-textbox aps-larger">
					<c:forEach items="${applst }" var="app">
						<option value="${app.appId }">${app.chiName }</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="aps-rows">
				<label class="aps-label"><spring:message code="PUB.menu" /></label>
				<div id="menuShow" class="aps-roleMenu">
				
				</div>
			</div>

			<!-- 表单操作 -->
			<div class="aps-action">
				<c:if test="${action=='upd' }">
					<button type="submit" class="aps-button">
						<spring:message code="PUB.ok" />
					</button>
				</c:if>
				<button type="reset" class="aps-button">
					<spring:message code="PUB.reset" />
				</button>
			</div>
		</form>
	</div>
</div>


<!-- 菜单显示模板  -->	
<script type="text/tmpl" id="menuTmpl">
	<c:forEach items="${mctxMap}" var="mctx">
		<c:if test="${fn:length(mctx.value)>0}">
		<p><spring:message code="${mctx.key }"/></p>
		<table class="aps-roleMenu-table">
			<thead>
				<tr>
					<th width="20%"><spring:message code="PUB.func"/></th>
					<th><spring:message code="PUB.action"/></th>
				</tr>
			</thead>
			<c:forEach items="${mctx.value }" var="menuPar"> <!-- 最外层 -->
			<tbody>
				<tr class="aps-roleMenu-trBottom">
					<td>
					<spring:message code="${menuPar.menu.name }"/>
					<input type="checkbox" class="aps-modul-menu" name="mid" value="${menuPar.menu.mid }" 
					{{= checked[${menuPar.menu.mid }]}} {{=disabled }} />
					</td>
					<td></td>
				</tr>
				<c:forEach items="${menuPar.children }" var="menuChild"> <!-- 中间层 -->
				<tr>
					<td>
						<spring:message code="${menuChild.menu.name}"/>
						<input type="checkbox" class="aps-func-menu" name="mid" value="${menuChild.menu.mid }"
						{{= checked[${menuChild.menu.mid }]}} {{= disabled}} />
					</td>
					<td>
						<c:forEach items="${menuChild.children }" var="menuSub"><!-- 最后一层 -->
							<input type="checkbox" class="aps-action-menu" name="mid" value="${menuSub.menu.mid }"
							{{= checked[${menuSub.menu.mid }]}} {{= disabled}} />
							<label class="aps-roleMenu-label"><spring:message code="${menuSub.menu.name }"/></label>
						</c:forEach>
					</td>
				</tr>
				</c:forEach>
			</tbody>
			</c:forEach>
		</table>
		</c:if>
	</c:forEach>
</script>

<!-- 菜单Script -->
<script type="text/javascript">
//初始化jquery.tmpl模板
$.template("MENU_TMPL",$.trim($("#menuTmpl").html()));

//ajax 加载应用可选菜单
function ajaxLoadMenu() {
	var id = this.value;
	var menuShow = $("#menuShow");
	$.ajax({
		type : 'POST',
		data:{appId:id},
		url : "${ctx}/system/menu/getMenuForApp.do",
		dataType : 'json',
		success : function(data) {
			var isOk = appAjaxCheck(data);
			menuShow.html($.tmpl("MENU_TMPL",{disabled:data.disabled,checked:data.checked}));
		},
		error : appAjaxError
	});	
}

$("#appId").on("change",ajaxLoadMenu);

$("#appId").change();

//ajax表单提交及valid校验
$("#menuForm").validate({
	submitHandler: function(form){
		jQuery(form).ajaxSubmit({
			type:"post",
			dataType : 'json',
			success : function(data){
				var isOk = appAjaxCheck(data);
				if(isOk){
					alert(data.retmsg.MSG);
				}				
			},
			error : appAjaxError
		});
	}
});

//菜单联动选择
function checkMenuBox(e){
	if(!e)e = window.event;
	var curr = e.target||e.srcElement;
	if(curr.tagName=="INPUT" && curr.type=="checkbox"){
		if(curr.checked){
			if($(curr).hasClass("aps-action-menu")){
				$(curr).parent().parent().find(".aps-func-menu").prop("checked",true);
				$(curr).parent().parent().parent().find(".aps-modul-menu").prop("checked",true);
			}else if($(curr).hasClass("aps-func-menu")){
				$(curr).parent().parent().find(".aps-action-menu").prop("checked",true);
				$(curr).parent().parent().parent().find(".aps-modul-menu").prop("checked",true);
			}else if($(curr).hasClass("aps-modul-menu")){
				$(curr).parent().parent().parent().find(".aps-action-menu,.aps-func-menu").prop("checked",true);
			}
		}else{
			if($(curr).hasClass("aps-func-menu")){
				$(curr).parent().parent().find(".aps-action-menu").prop("checked",false);
			}else if($(curr).hasClass("aps-modul-menu")){
				$(curr).parent().parent().parent().find(".aps-action-menu,.aps-func-menu").prop("checked",false);
			}
		}
	}
}

// 绑定菜单事件
$(".aps-roleMenu").on("click",checkMenuBox);

</script>

</body>
</html>