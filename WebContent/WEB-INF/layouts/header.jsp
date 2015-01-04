<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page import="com.xsw.ctx.*"%>
<%@ page import="com.xsw.constant.*"%>
<%@ page import="java.util.List"%>
<%@ page import="org.apache.shiro.SecurityUtils"%>
<%@ page import="org.apache.shiro.subject.Subject" %>
<%@ page import="org.springframework.web.context.WebApplicationContext,org.springframework.web.servlet.support.RequestContextUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
WebApplicationContext webctx = RequestContextUtils.getWebApplicationContext(request);
//获取AppCtx
AppCtx appctx = (AppCtx)webctx.getBean("appctx");
%>

<div id="apsHeader" class="aps-header"> 
		<div class="aps-logo" style="background-image: url('${ctx}/statics/styles/img/logo_${locale}.png');"></div>
		<div class="aps-menu-div"> 
            <ul id="kendoMenu" class="aps-menu">
              <li class="aps-menu-li aps-menu-home">
              <a style="color:#fff;" href="${ctx}/"><spring:message code="PUB.home"/></a></li>
              <shiro:authenticated> 
              <!-- 菜单类型0.菜单目录  1.功能项  2.菜單&功能  -->
              <c:forEach items="${CURRENT_USER_ROOT_MENUCTX}" var="mc">
              	<c:if test="${mc.menu.type!=1 }">
				<li class="aps-menu-li">
					 <spring:message code="${mc.menu.name}"/><i class="k-icon k-i-arrow-s"></i>
					 <ul class="aps-menu-sub">
					 	<c:forEach items="${mc.children}" var="cmc">
					 		<li><a href="${ctx }/${cmc.menu.uri}"><spring:message code="${cmc.menu.name }"/></a></li>
					 	</c:forEach>
					 </ul>
				</li>
				</c:if>
			  </c:forEach>
              </shiro:authenticated>
              <li class="aps-menu-li" style="border-right: 1px solid #CCC;">
                <spring:message code="PUB.lang"/><i class="k-icon k-i-arrow-s"></i>
                <ul class="aps-menu-sub">
                  <%
                  for(String lang:(List<String>)appctx.getObjParam("lang"))
                  {
                	  String tlang[] = lang.split("\\|");
                  %>
                  <li><a href="?lang=<%=tlang[0]%>"><%=tlang[1]%></a> </li>
                  <%}
                  %>
                </ul>
              </li>
             <%--  <li class="menu-li">
                <spring:message code="PUB.theme"/>
                <ul>
                  <li><a href="?theme=default">Default</a></li>
                  <li><a href="?theme=gray">Gray</a></li>
                  <li> <a href="?theme=boot">bootstrap</a></li>
                </ul>
              </li> --%>
            <li class="aps-menu-li aps-menu-default">
            <div class="aps-navbar">
              <shiro:guest>  
              <a href="${ctx}/login.htm" class="aps-btn-login"><spring:message code="PUB.signin"/></a>
              </shiro:guest>
              <shiro:user> 
              <span style="color:#eee;">
              <spring:message code="PUB.hi"/>, <!--<shiro:principal/>-->
              <shiro:principal property="name"/>             
              </span>
              <shiro:authenticated>
              <a href="${ctx}/logout.do" class="aps-btn-login"><spring:message code="PUB.logout"/></a>
              <a href="${ctx}/system/setting.do" class="aps-btn-setting"><spring:message code="PUB.setting"/></a>
              </shiro:authenticated>
              <shiro:notAuthenticated>
              <a href="${ctx}/login.htm" class="aps-btn-login"><spring:message code="PUB.signin"/></a>
              </shiro:notAuthenticated>
              </shiro:user>  
            </div>
            </li>
            </ul>
         </div>   <!-- world -->
        <span id="showMenuBtn" class="showMenuBtn"></span>
</div>