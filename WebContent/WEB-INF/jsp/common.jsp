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
<script type="text/javascript" src="${ctx }/statics/jquery/jquery-1.9.1.min.js"></script>
<!-- KENOD UI -->
<script type="text/javascript" src="${ctx }/statics/kendo/kendo.web.min.js"></script>
<!-- JQUERY UI -->
<script type="text/javascript" src="${ctx }/statics/jquery/jqueryui/jquery-ui.min.js"></script>
<!-- jquery tmpl  -->
<script src="${ctx}/statics/jquery/jquery.tmpl.min.js" type="text/javascript"></script>
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

<script src="${ctx}/statics/jquery/validation/messages_<%=locale%>.js" type="text/javascript"></script>

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

/**
 * Ajax check
 * @param data
 * @reture false.failed true.success
 */
function appAjaxCheck(data,form,mainForm){
	// 返回值是否为JSON对象
	if(!appCheckJsonData(data)){
		return false;
	}
	
	//uuid 较验码重置
  	if(data.retmsg._UUID_TOKEN!=null){
  		$("#_UUID_TOKEN").val(data.retmsg._UUID_TOKEN);
  		$("input[name='_UUID_TOKEN']").val(data.retmsg._UUID_TOKEN);
  	}
	
	if(data.retmsg.CODE.substring(0,7)=="ERRCODE"){
		// 不存在全局错误时弹出错误提醒
		if(!data.retmsg.GLOBAL){
			alert(data.retmsg.MSG);
		}
		
		// 全局错误处理
	  	if(data.retmsg.GLOBAL!=null){
	  		var global = "";
	  		for(var i=0;i<data.retmsg.GLOBAL.length;i++){
	  		   if(global!=""){
	  			   global+="</br>";
	  		   }
	  		   global+=data.retmsg.GLOBAL[i];
	  		}
	  		if(global!=""){
	  			var ges = $("#globalErrSpan");
	  			if(form && ges.length==0){
	  				ges = $(".globalErrSpan",form);
	  			}
	  		   	ges.html('<div class="aps-alert aps-alert-error">'
	  				+'<span class="aps-alert-close"></span><spring:message code="PUB.validErr"/>:<br/>'+global+'</div>');
	  		}
	  	}
		
		// 表单错误显示
	  	if(form || mainForm){
	  		if(form){
	  			appErrorData(data,form);
	  		}
	  		if(mainForm){
	  			appErrorData(data,mainForm);
	  		}
	  	}else{
	  		appErrorData(data,$(document.body));
	  	}
		
		return false;
	} // end if error
	return true;
}

//处理from数据信息
function appErrorData(data,form){
	//Fields Error
  	if(data.retmsg.FIELDERRS!=null){
  		//remote error span first
  		for(var i=0;i<data.retmsg.FIELDERRS.length;i++){
  			var fieldObj = $("input[name='"+data.retmsg.FIELDERRS[i].field+"']",form);
  			if(fieldObj.length==0){
  				fieldObj = $("textarea[name='"+data.retmsg.FIELDERRS[i].field+"']",form);
  				if(fieldObj.length==0){
  					fieldObj = $("select[name='"+data.retmsg.FIELDERRS[i].field+"']",form);
  				}
  			}
  			// 是否存在错误的对象
  			if(fieldObj.length!=0){
  				if(i==0){
  				    fieldObj.focus();
  				}
  				var nextSpan = fieldObj.next();
  				if(nextSpan.length!=0){	
  					if(nextSpan[0].tagName=="SPAN" && nextSpan.hasClass("error")){
  						nextSpan.html("<i></i>"+data.retmsg.FIELDERRS[i].defaultMessage);
  						nextSpan.show();
  					}
  				}else{
  					fieldObj.after("<span class='error'><i></i>"+data.retmsg.FIELDERRS[i].defaultMessage+"</span>");
  				} // end if span	  				
  			} // end if field
  		} // end for
  	} // end if valid
}

//公用多语言变量
var APS_LANG = {
		ok : "<spring:message code='PUB.ok'/>",
		cancel : "<spring:message code='PUB.cancel'/>",
		warn : "<spring:message code='PUB.warn'/>",
		message : "<spring:message code='PUB.message'/>",
		success : "<spring:message code='PUB.successful'/>",
		error : "<spring:message code='PUB.error'/>" 
};


/******JQuery Vaild For lyodssoft.com********/
//Valid 日期与时间的校验 
jQuery.validator.addMethod(
  "datetime",
   function(value, element,param) {
       if (this.optional(element)) // return true on optional element 
           return true;
       if(value.length != param) return false;
       valid = true;
       var _date = substr(value,10);
       valid = valid && jQuery.validator.methods.date.call(this, $.trim(_date), element);
        if(valid && param>10){
      	var _times = value.substring(11,value.length).split(":");
      	for(var i=0;i<_times.length;i++){
      		valid = /^\d+$/.test($.trim(_times[i]));
      		if(valid){
      			if(i==0){
	        			if(_times[i]>23){
	        				valid=false;
	        				break;
	        			}
	        		}else{
	        			if(_times[i]>59){
	        				valid=false;
	        				break;
	        			}
	        		}
      		}else{
      			break;
      		}
      	}
       } 
       return valid;
   },
  jQuery.validator.messages.date
); 
//Vaild多个邮箱检验
jQuery.validator.addMethod(
  "multiemail",
   function(value, element) {
       if (this.optional(element)) // return true on optional element 
           return true;
       var emails = value.split(/[;,]+/); // split element by , and ;
       valid = true;
       for (var i in emails) {
           value = emails[i];
           valid = valid && jQuery.validator.methods.email.call(this, $.trim(value), element);
       }
       return valid;
   },
  jQuery.validator.messages.email
);   
//Valid正则校验
jQuery.validator.addMethod(
  "pattern",
   function(value,element,jsReg) {
		if($.trim(value)!=""){
			jsReg = eval(jsReg);
			if(jsReg.test(value)){
				return true;
			}else{
				return false;
			}
		}
		return true;
   },
   jQuery.validator.messages.pattern
); 

// 加载DOM节点完成之后的事件处理
jQuery(function($){
	// global error console for close on click
	$("#globalErrSpan,.globalErrSpan").click(function(e){
		var e = e||window.event,
		curr = e.target||e.srcElement;
		if(curr.tagName=="DIV" && $(curr).hasClass("aps-alert")){
			$(curr).slideUp("slow");
		}else if(curr.tagName="SPAN" && curr.className=="aps-alert-close"){
			$(curr).parent().slideUp("slow");
		}
	});
	
	//form error close on click
	$("form").click(function(e){
		var e = e||window.event,
			curr = e.target||e.srcElement;
		if(curr.tagName=='SPAN' && $(curr).hasClass("error")){
			$(curr).slideToggle("fast");	
		}
	});
});

</script>