

var AppOS = (function(){
	var AppOS = "";
	
	//创建jQuery对象,给所有的jQuery方法提供统一的入口,避免繁琐难记  
    var AppOS = function( selector, context ) {  
        //jQuery的构造对象,调用了jQuery.fn.init方法  
        //最后返回jQuery.fn.init的对象  
        return new jQuery.fn.init( selector, context, rootjQuery );  
    };
    
    //定义jQuery的原型,jQuery.fn指向jQuery.prototype对象  
    AppOS.fn = AppOS.prototype = {  
	    //重新指定构造函数属性,因为默认指向jQuery.fn.init  
	    constructor: AppOS,  
	    init: function( selector, context, rootjQuery ) {
	    	
	    }
    };
	
   
    //
	return (window.AppOS=AppOS);
})();

