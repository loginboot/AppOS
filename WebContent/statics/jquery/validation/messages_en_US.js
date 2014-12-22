(function(factory) {
	if (typeof define === "function" && define.amd) {
		define([ "jquery", "../jquery.validate" ], factory);
	} else {
		factory(jQuery);
	}
}(function($) {

	$.extend(jQuery.validator.defaults, {
		errorElement : "span"
	});
}));