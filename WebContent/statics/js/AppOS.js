
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
    				$("#view").click();
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
    				return AppOS.fn.html_encode(data);
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

