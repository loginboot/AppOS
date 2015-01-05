<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@page language="java"
	import="org.springframework.web.util.HtmlUtils,java.net.*,java.util.*,com.xsw.utils.Util,com.xsw.ctx.*,com.xsw.model.*,com.xsw.service.*,org.springframework.web.context.WebApplicationContext,org.springframework.web.servlet.support.RequestContextUtils"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ page import="org.apache.shiro.SecurityUtils"%>
<%@ page import="org.apache.shiro.subject.Subject"%>
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />

<link type="image/x-icon" href="${ctx}/statics/styles/img/icon.ico" rel="Shortcut Icon">
<!-- kendo themes -->
<link href="${ctx}/statics/<spring:theme code="kendo.common"/>" type="text/css" rel="stylesheet" />
<link href="${ctx}/statics/<spring:theme code="kendo.themes"/>" type="text/css" rel="stylesheet" />
<!-- jquey ui -->
<link href="${ctx}/statics/<spring:theme code='jqueryui'/>" type="text/css" rel="stylesheet" />
<!-- lyods ui -->
<link href="${ctx}/statics/<spring:theme code="mainstyle.css"/>" type="text/css" rel="stylesheet" />

<!-- JQUERY -->
<script type="text/javascript" src="${ctx }/statics/jquery/jquery-1.11.0.min.js"></script>
<!-- KENOD UI -->
<script type="text/javascript" src="${ctx }/statics/kendo/kendo.web.min.js"></script>
<!-- JQUERY UI -->
<script type="text/javascript" src="${ctx }/statics/jquery/jqueryui/jquery-ui.min.js"></script>
<!-- jquery tmpl  -->
<script src="${ctx}/statics/jquery/jquery.tmpl.js" type="text/javascript"></script>
<!-- date time pick -->
<script src="${ctx}/statics/jquery/jqueryui/jquery-ui-timepicker-addon.js" type="text/javascript"></script>

<script src="${ctx}/statics/jquery/jquery.cookie.js" type="text/javascript"></script>
<script src="${ctx}/statics/jquery/jquery.form.js" type="text/javascript"></script>
<script src="${ctx}/statics/jquery/validation/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctx}/statics/js/AppOS.js" type="text/javascript"></script>
<script src="${ctx}/statics/js/json2.js" type="text/javascript"></script>

<%
    String locale = "en_US";
    if (request.getCookies() != null) {
        for (Cookie ck : request.getCookies()) {
    if ("org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE".equals(ck.getName())) {
        locale = ck.getValue();
    }
        }
    }

    request.setAttribute("locale", locale);
    AppCtx appctx = null;
    WebApplicationContext webctx = null;

    webctx = RequestContextUtils.getWebApplicationContext(request);
    //获取AppCtx
    appctx = (AppCtx) webctx.getBean("appctx");
%>

<script type="text/javascript">
/**
 * Ajaxa Error Function
 * @param jqXHR
 * @param textStatus
 * @param errorThrown
 */
function appAjaxError(jqXHR, textStatus, errorThrown){			
	//No response
	if(jqXHR.readyState<4){
		alert('<spring:message code="ERRCODE.9999"/>,error:<spring:message code="ERRCODE.9990"/>,status:'+textStatus);
		//has error
	}else{
		alert('<spring:message code="PUB.error"/>,error:'+jqXHR.responseText);
	}
}
/**
 * check if return json format data
 */
function appCheckJsonData(data){
	if(JSON.stringify(data).indexOf("\"retmsg\":")<0 && JSON.stringify(data).indexOf("\\\"retmsg\\\":")<0){
		alert('<spring:message code="PUB.error"/>:'+JSON.stringify(data));
		return false;
	}
	return true;
}
</script>