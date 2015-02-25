<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="../common.jsp" %>
<title><spring:message code="MENU.001020"/></title>
</head>
<body>

<div class="aps-content">
	<div class="aps-window">
		<h3 class="aps-form-title"><spring:message code="MENU.001020"/> - <spring:message code="PUB.${action }"/></h3>
		<form id="roleForm" action="${ctx }/system/role/${action}.do" method="post">
			<input type="hidden" name="rid" value="${role.rid }" />
			<input type="hidden" name="lastModifyDate" value="${role.lastModifyDate }" />
			<input type="hidden" name="appList.appId" value="${role.appList.appId }" />
			<div class="aps-rows">
				<label class="aps-label" for="name"><spring:message code="role.name"/></label>
				<input type="text" id="name" name="name" value="${fn:escapeXml(role.name) }" class="aps-textbox aps-largerx"
				required="true" ${disabled } />
			</div>
			<div class="aps-rows">
				<label class="aps-label" for="description"><spring:message code="role.desc"/></label>
				<input type="text" id="description" name="description" value="${fn:escapeXml(role.description) }" class="aps-textbox aps-largerx" 
				${disabled } />
			</div>
			
			<div class="aps-rows">
				<label class="aps-label" for="description"><spring:message code="PUB.menu"/></label>
				<div class="aps-roleMenu">
					<p>菜单列表</p>
					<table class="aps-roleMenu-table">
						<thead>
							<tr>
								<th width="20%">功能</th>
								<th>操作</th>
							</tr>
						</thead>
					<c:forEach items="${mctx }" var="menuPar"> <!-- 最外层 -->
						<tbody>
						<tr class="aps-roleMenu-trBottom">
							<td>
							<spring:message code="${menuPar.menu.name }" />
							<input type="checkbox" class="aps-modul-menu" name="mid" value="${menuPar.menu.mid }" 
							${checked[menuPar.menu.mid] } ${disabled } />
							</td>
							<td></td>
						</tr>
						<c:forEach items="${menuPar.children }" var="menuChild"> <!-- 中间层 -->
						<tr>
							<td>
							<spring:message code="${menuChild.menu.name }" />
							<input type="checkbox" class="aps-func-menu" name="mid" value="${menuChild.menu.mid }"
							${checked[menuChild.menu.mid] } ${disabled } />
							</td>
							<td>
							<c:forEach items="${menuChild.children }" var="menuSub"><!-- 最后一层 -->
							<input type="checkbox" class="aps-action-menu" name="mid" value="${menuSub.menu.mid }"
							${checked[menuSub.menu.mid] } ${disabled } />
							<label class="aps-roleMenu-label"><spring:message code="${menuSub.menu.name }" /></label>
							</c:forEach>
							</td>
						</tr>
						</c:forEach>
						</tbody>
					</c:forEach>
					</table>
				</div>
			</div>
			
			<div class="aps-action">
				<c:if test="${action!='view' }">
				<button type="submit" class="aps-button"><spring:message code="PUB.ok"/></button>
				</c:if>
				<button type="button" class="aps-button" onclick="AppOS.get('${ctx}/system/role.do');">
				<spring:message code="PUB.return"/>
				</button>
			</div>
		</form>
	</div>
</div>

<script type="text/javascript">
//聚焦第一个输入框
$("input[type='text']:visible:enabled:first").focus();
//ajax表单提交及valid校验
$("#roleForm").validate({
	rules:{	
		name:{
			pattern : /^\w+$/,			
			remote:"${ctx}/system/role/checkRoleName.do?&rid=${role.rid}&rowNum="+Math.random()
		}
	},
	messages:{
		name:{
			remote:"<spring:message code='ERRCODE.1023'/>",
			pattern : "<spring:message code='PUB.engOrDigitalOrUnline'/>"
		}
	},
	submitHandler: function(form){
		jQuery(form).ajaxSubmit({
			type:"post",
			dataType : 'json',
			success : function(data){
				var isOk = appAjaxCheck(data);
				if(isOk){
					AppOS.get("${ctx}/system/role.do");
				}				
			},
			error : appAjaxError
		});
	}
});

// 菜单联动选择
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