<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="../common.jsp"%>
<title><spring:message code="PUB.menu" /></title>
</head>
<body>

<div class="aps-content">
	<div class="aps-window">
		<h3 class="aps-form-title">
			<spring:message code="MENU.001030" /> - <spring:message code="PUB.${action }" />
		</h3>
		<form id="menuForm" action="${ctx }/system/menu/${action}.do" method="post">
			<div class="aps-rows">
				<label class="aps-label"><spring:message code="PUB.app" /></label> <select name="appId" class="aps-textbox">
					<c:forEach items="${applst }" var="app">
						<option class="${app.appId }">${app.chnName }</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="aps-rows">
				<label></label>
				<div id="menuShow">
				
				</div>
			</div>

			<!-- 表单操作 -->
			<div class="aps-actions">
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
	{{each(i,mctx) mctxMap}}
		{{if mctx.value.length>0}}
		<p>{{mctx.key}}</p>
		<table>
			<thead>
				<tr>
					<th width="20%">功能</th>
					<th>操作</th>
				</tr>
			</thead>
			{{each(j,menuPar) mctx.value}}
			<body>
				<tr>
					<td>
					{{= menuPar.menu.name}}
					<input type="checkbox" class="aps-modul-menu" name="mid" value="{{= menuPar.menu.mid}}" 
					{{= checked[menuPar.menu.mid]}} {{=disabled }} />
					</td>
					<td></td>
				</tr>
				{{each(z,menuChild) menuPar.children}}
				<tr>
					<td>
						{{= menuChild.menu.name}}
						<input type="checkbox" class="aps-func-menu" name="mid" value="${menuChild.menu.mid }"
						{{= checked[menuChild.menu.mid]}} {{= disabled}} />
					</td>
					<td>
						{{each(a,menuSub) menuChild.children}}
							<input type="checkbox" class="aps-action-menu" name="mid" value="{{= menuSub.menu.mid}}"
							{{= checked[menuSub.menu.mid]}} {{= disabled}} />
							<label class="aps-roleMenu-label">{{= menuSub.menu.name}}</label>
						{{/each}}
					</td>
				</tr>
				{{/each}}
			</body>
			{{/each}}
		</table>
		{{/if}}
	{{/each}}
</script>

<!-- 菜单Script -->
<script type="text/javascript">
//初始化jquery.tmpl模板
$.template("MENU_TMPL",$.trim($("#menuTmpl").html()));




</script>

</body>
</html>