
var GRID = {}; // 表格选择对象
var AppOS = (function(){
	var AppOS;
	
	//创建jQuery对象,给所有的jQuery方法提供统一的入口,避免繁琐难记  
    AppOS = {
    		onChange : function(item) {
    			var selectRow = this.select();
    			GRID = this.dataItem(selectRow);
    		},
    		ondatabound : function(e) {
    			GRID = {};
    			var $table = e.sender.wrapper;
    			$("thead th", $table).each(function() {
    				this.title = $(this).attr("data-title");
    			});
    			//双击查看
    			$("tbody tr",$table).dblclick(function(){
    				var id = $table.attr("id")+"View";
    				$("#"+id).click();
    			});
    		},
    		get : function(url, type) {
    			if ("top" === type) {
    				window.top.location.href = url;
    			} else if ("_bank" === type || "bank" === type) {
    				window.open(url, "_bank");
    			} else {
    				window.location.href = url;
    			}
    		},
    		showSelection : function(id, key) {
    			var tmp = key;
    			$("#" + id + " option").each(function() {
    				if ($.trim(this.value) != "" && this.value == key) {
    					tmp = this.text;
    					return false;
    				}
    			});
    			return tmp;
    		},
    		pageList : [ 5, 10,15, 20, 30, 50 ],
    		encUrl : function(url) {
    			if (url == null) {
    				return "";
    			}
    			eurl = encodeURIComponent(url);
    			eurl = eurl.replace(/\'/g, "%27");
    			return eurl;
    		},
    	    isEmpty : function(val) {
    			if (val == null) {
    				return true;
    			} else if ($.trim(val) == "") {
    				return true;
    			}
    			return false;
    		},
    		dataConvert : function(data) {
    			if (data === null)
    				return "";
    			else {
    				if (typeof data == 'string') {
    					data = data.replace(/(\n)/g, ' ');
    				}
    				return AppOS.html_encode(data);
    			}
    		},
    		html_decode : function(str) {
    			var s = "";
    			if (str == null) {
    				return "";
    			}
    			if (typeof (str) == "string") {
    				s = str.replace(/&lt;/g, "<");
    				s = s.replace(/&gt;/g, ">");
    				s = s.replace(/&nbsp;/g, " ");
    				s = s.replace(/&#39;/g, "\'");
    				s = s.replace(/&quot;/g, "\"");
    				s = s.replace(/&amp;/g, "&");
    				s = s.replace(/<br>/g, "\n");
    				return s;
    			} else {
    				return str;
    			}
    		},
    		html_encode : function(str) {
    			var s = "";
    			if (str == null) {
    				return "";
    			}
    			if (typeof (str) == "string") {
    				if (str.length == 0)
    					return "";
    				s = str.replace(/&/g, "&amp;");
    				s = s.replace(/</g, "&lt;");
    				s = s.replace(/>/g, "&gt;");
    				s = s.replace(/ /g, "&nbsp;");
    				s = s.replace(/\'/g, "&#39;");
    				s = s.replace(/\"/g, "&quot;");
    				s = s.replace(/\n/g, "<br>");
    				return s;
    			} else {
    				return str;
    			}
    		},
    		substrtip : function(str, len) {
    			if (!str || !len) {
    				return '';
    			}
    			// 预期计数：中文2字节，英文1字节
    			var a = 0;
    			// 循环计数
    			var i = 0;
    			// 临时字串
    			var temp = '';
    			for (i = 0; i < str.length; i++) {
    				if (str.charCodeAt(i) > 255) {
    					// 按照预期计数增加2
    					a += 2;
    				} else {
    					a++;
    				}
    				// 如果增加计数后长度大于限定长度，就直接返回临时字符串
    				if (a > len) {
    					return temp + "...";
    				}
    				// 将当前内容加到临时字符串
    				temp += str.charAt(i);
    			}
    			// 如果全部是单字节字符，就直接返回源字符串
    			return str;
    		},
    		substr : function(str, len) {
    			if (!str || !len) {
    				return '';
    			}
    			// 预期计数：中文2字节，英文1字节
    			var a = 0;
    			// 循环计数
    			var i = 0;
    			// 临时字串
    			var temp = '';
    			for (i = 0; i < str.length; i++) {
    				if (str.charCodeAt(i) > 255) {
    					// 按照预期计数增加2
    					a += 2;
    				} else {
    					a++;
    				}
    				// 如果增加计数后长度大于限定长度，就直接返回临时字符串
    				if (a > len) {
    					return temp;
    				}
    				// 将当前内容加到临时字符串
    				temp += str.charAt(i);
    			}
    			// 如果全部是单字节字符，就直接返回源字符串
    			return str;
    		},
    		getAlphaCharSize : function(str) {
    			// 取得字符串字母的个数
    			var size = 0;
    			for (var i = 0; i < str.length; i++) {
    				if ((str.charCodeAt(i) >= 65 && str.charCodeAt(i) <= 90)
    						|| (str.charCodeAt(i) >= 97 && str.charCodeAt(i) <= 122)) {
    					size++;
    				}
    			}
    			return size;
    		},
    		getNumberSize : function(str) {
    			// 返回字符串数字的个数
    			var size = 0;
    			for (var i = 0; i < str.length; i++) {
    				if (str.charCodeAt(i) >= 48 && str.charCodeAt(i) <= 57) {
    					size++;
    				}
    			}
    			return size;
    		},
    		getSpecialCharSize : function(str) {
    			// 返回字符串特殊字符个数
    			var size = 0;
    			for (var i = 0; i < str.length; i++) {
    				if (str.charCodeAt(i) < 48
    						|| (str.charCodeAt(i) > 57 && str.charCodeAt(i) < 65)
    						|| (str.charCodeAt(i) > 90 && str.charCodeAt(i) < 97)
    						|| str.charCodeAt(i) > 122) {
    					size++;
    				}
    			}
    			return size;
    		},
    		isContainSensitive : function(str) {
    			// 判断字符串是否包含大小写
    			var big = false;
    			var small = false;
    			for (var i = 0; i < str.length; i++) {
    				if (str.charCodeAt(i) >= 65 && str.charCodeAt(i) <= 90) {
    					big = true;
    				}
    				if (str.charCodeAt(i) >= 97 && str.charCodeAt(i) <= 122) {
    					small = true;
    				}
    				// 包含大小写直接返回 true
    				if (big && small) {
    					return true;
    				}
    			}
    			return false;
    		},
    		msgBox : function(msg, type, callback) {
    			// type 0 为确认,1 为选择窗口,
    			var _msg = $("#APS_MSG_BOX");
    			var _mode = $("#APS_MSG_MODE");
    			// 不存在就重新创建
    			if (_msg.length == 0) {
    				var _msgDiv = "<div id='APS_MSG_BOX' class='aps-msg'></div>"
    				var _msgbody = $("<div class='aps-msg-body'></div>");
    				_msg = $(_msgDiv);
    				$(document.body).append(_msg);
    				_msg.content = $("<div class='aps-msg-content'></div>");
    				_msg.action = $("<div class='aps-msg-action'></div>");
    				_msg.append(_msgbody);
    				_msgbody.append(_msg.content).append(_msg.action);
    				_msg.data("action", _msg.action).data("content", _msg.content);
    			}
    			if (_mode.length == 0) {
    				var _div = "<div id='APS_MSG_MODE' class='aps-msg-mode'></div>";
    				_mode = $(_div);
    				$(document.body).append(_mode);
    			}

    			// 内部函数
    			function _close() {
    				var _height = _msg.height();
    				_msg.animate({
    					top : -_height
    				}, 300, function() {
    					_mode.hide();
    					// 打开
    					if(AppOS.constant["MSG_BOX_CONFIRM"]!=type){
    						_cback();
    					}
    				});
    			}

    			// CONFIRM时继执行
    			function _cback() {
    				if (typeof callback == "function") {
    					callback();
    				}
    			}

    			// 打开提示框
    			function _open() {
    				_msg.data("content").html(msg);
    				_mode.show();
    				_msg.animate({
    					top : 2
    				}, 300);
    			}
    			
    			_msg.data("action").html("");
    			var _okbtn = $("<button class='aps-button'>"+APS_LANG.ok+"</button>");
    			if(AppOS.constant["MSG_BOX_CONFIRM"] == type){
    				_okbtn.on("click",_cback);
    			}else{
    				_okbtn.on("click",_close);
    			}
    			_msg.data("action").append(_okbtn);
    			// 操作类型
    			switch (type) {
    			case AppOS.constant["MSG_BOX_WARM"]:
    				msg = "<strong><i class='aps-i-warn'></i>"+APS_LANG.warn+"</strong><div class='aps-msg-hr'></div>&nbsp;&nbsp;&nbsp;&nbsp;" + msg;
    				break;
    			case AppOS.constant["MSG_BOX_ERROR"]:
    				msg = "<strong><i class='aps-i-error'></i>"+APS_LANG.error+"</strong><div class='aps-msg-hr'></div>&nbsp;&nbsp;&nbsp;&nbsp;" + msg;
    				break;
    			case AppOS.constant["MSG_BOX_SUCCESS"]:
    				msg = "<strong><i class='aps-i-success'></i>"+APS_LANG.success+"</strong><div class='aps-msg-hr'></div>&nbsp;&nbsp;&nbsp;&nbsp;" + msg;
    				break;
    			case AppOS.constant["MSG_BOX_CONFIRM"]:
    				msg = "<strong><i class='aps-i-message'></i>"+APS_LANG.message+"</strong><div class='aps-msg-hr'></div>&nbsp;&nbsp;&nbsp;&nbsp;" + msg;
    				var _callbtn = $("<button class='aps-button'>"+APS_LANG.cancel+"</button>").on("click",_close);
    				_msg.data("action").append(_callbtn);
    				break;
    			}
    			
    			// 打开
    			if(AppOS.constant["MSG_BOX_CLOSE"]==type){
    				_close();
    			}else{
    				_open();
    			}
    		},winBox : function(id,ops){
    			return $("#"+id).kendoWindow({
    				title : ops.title||"",
    				minWidth : ops.minWidth||"450px",
    				minHeight: ops.minHeight|"180px",
    				maxWidth : ops.maxWidth||"800px",
    				maxHeight : ops.maxHeight||"500px",
    				width : ops.width || "auto",
    				height : ops.height || "auto",
    				visible : ops.visible!=null?ops.visible:false,
    				modal : ops.modal!=null?ops.model:true,
    				actions : ops.actions || [ "Close" ],
    				dragstart : function(event){
    					this.wrapper.css({opacity: 0.7});
    					$(this.wrapper.context).css({visibility:"hidden"});
    				},
    				dragend : function(event){
    					this.wrapper.css({opacity: 1});
    					$(this.wrapper.context).css({visibility:"visible"});
    				}
    			}).data("kendoWindow");
    		},
    		constant : {
    			MSG_BOX_WARM : 0,
    			MSG_BOX_CONFIRM : 1,
    			MSG_BOX_ERROR : 2,
    			MSG_BOX_SUCCESS : 3,
    			MSG_BOX_CLOSE : 4
    		},
    		form_editable : function(id, type) {
    			// 表单操作的disable true or false
    			$("input[type='text'],input[type='email'],input[type='checkbox'],input[type='radio'],select,textarea",
    					$("#" + id)).attr("disabled", type);
    			type ? $("button[type='submit']", $("#" + id)).hide() : $(
    					"button[type='submit']", $("#" + id)).show();
    			$("#globalErrSpan").html("");
    			$(".globalErrSpan",$("#" + id)).html("");
    		},
    		resetForm : function(id){
    			$("#"+id).resetForm();
    			$("#"+id).validate().resetForm();
    		}
    };
   
    //Form add or clean
    $.fn.serializeJson = function() {
    	var o = {};
    	var a = this.serializeArray();
    	$.each(a, function() {
    		if (o[this.name]) {
    			if (!o[this.name].push) {
    				o[this.name] = [ o[this.name] ];
    			}
    			o[this.name].push(this.value || '');
    		} else {
    			o[this.name] = this.value || '';
    		}
    	});
    	return o;
    };
    $.fn.clearForm = function() {
    	this.find(':input').each(function() {
    		switch (this.type) {
    		case 'passsword':
    		case 'select-multiple':
    		case 'select-one':
    		case 'text':
    		case 'textarea':
    			$(this).val('');
    			break;
    		case 'checkbox':
    		case 'radio':
    			this.checked = false;
    		}
    	});
    };
   
    //
	return (window.AppOS=AppOS);
})();

