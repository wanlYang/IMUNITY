/* Author: VIPArcher */
// Add new jQuery function
(function($){
	// browser info
	"use strict";
	$.browser = (function() {
		var
		rwebkit = /(webkit)\/([\w.]+)/,
		ropera = /(opera)(?:.*version)?[ \/]([\w.]+)/,
		rmsie = /(msie) ([\w.]+)/,
		rmozilla = /(mozilla)(?:.*? rv:([\w.]+))?/,
		browser = {},
		browserMatch = (function(ua) {
			var match = rwebkit.exec(ua) || ropera.exec(ua) || rmsie.exec(ua) || ua.indexOf("compatible") < 0 && rmozilla.exec(ua) || [];
			return {
				browser : match[1] || "",
				version : match[2] || "0"
			};
		})(window.navigator.userAgent.toLowerCase());
		if (browserMatch.browser) {
			browser[browserMatch.browser] = true;
			browser.version = browserMatch.version;
			browser.language = (window.navigator.browserLanguage || window.navigator.language).toLowerCase();
		}
		return browser;
	})();
	$.extend({
		/* getUrlParam */
		getUrlParam: function(name) {
			var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if(r != null) return decodeURI(r[2]);
			return null;
		},
		/* scrollFloor */
		scrollFloor: function(options){
			var $body = $(document.body),data = [];
			var defaults = {
				floorClass:  '.scroll-floor',
				navClass:    '.scroll-nav',
				activeClass: 'active',
				activeTop: 200,
				scrollTop: 0,
				delayTime: 500
			};
			function getItem(_list,newOptions){
				_list.each(function() {
				    var item = {};
				    item.$obj = $body.find(this);
				    item.$activeTop = $body.find(this).offset().top - newOptions.activeTop;
				    item.$scrollTop = $body.find(this).offset().top + newOptions.scrollTop;
				    data.push(item);
				});
			};
			function scrollActive(_list,newOptions){
				var nowScrollTop = $(window).scrollTop();
				$.each(data,function(i,item){
					if(nowScrollTop > item.$activeTop){
						_list.removeClass(newOptions.activeClass).eq(i).addClass(newOptions.activeClass);
					}
				});
			};
			var newOptions = $.extend({}, defaults, options);
			var floorList = $body.find(newOptions.floorClass),navList = $body.find(newOptions.navClass);
			getItem(floorList,newOptions);
			scrollActive(navList,newOptions);
			$(window).bind('scroll',function(){scrollActive(navList,newOptions);});
			navList.bind('click',function(){
				var _index = $body.find(this).index();
				$('html,body').animate({'scrollTop' : data[_index].$scrollTop},newOptions.delayTime);
			});
		}
	});
	$.fn.extend({
		/* 绑定input只能输入正则内容 默认为数字（带小数点） */
		onlyReg: function (opt) {
			var reg = opt && opt.reg ? opt.reg : /[^0-9.]/g;
			$(this).bind('input propertychange',function() {
				$(this).val($(this).val().replace(reg,''));
			});
		},
		/* textarea高度根据内容自适应 */
		txtaAutoHeight: function () {
			return this.each(function () {
				var $this = $(this);
				if (!$this.attr('initAttrH')) {
					$this.attr('initAttrH', $this.outerHeight());
				}
				setAutoHeight(this).on('input', function () {
				setAutoHeight(this);
				});
			});
			function setAutoHeight(elem) {
				var $obj = $(elem);
				return $obj.css({ height: $obj.attr('initAttrH'), 'overflow-y': 'hidden' }).height(elem.scrollHeight);
			}
		},
		smartFloat: function(top) {
			var top = Number.isInteger(top) ? top : 250;
			var position = function(element) {
				var _top = element.offset().top, pos = element.css("position");
				$(window).scroll(function() {
					var scrolls = $(this).scrollTop();
					if (scrolls + top > _top) {
						if (window.XMLHttpRequest) {
							element.css({
								position: "fixed",
								top: top
							});	
						} else {
							element.css({
								top: scrolls
							});	
						}
					}else {
						element.css({
							position: pos,
							top: _top
						});	
					}
				});
			};
			return $(this).each(function() {
				position($(this));						 
			});
		},
		/* 返回顶部 */
		toTop: function(opt){
			var i=this,e=$(window),s=$("html, body"),n=$.extend({autohide:!0,offset:450,speed:500,position:!0,right:0,bottom:52},opt);i.css({cursor:"pointer"}),n.autohide&&i.css("display","none"),n.position&&i.css({position:"fixed",right:n.right,bottom:n.bottom}),i.click(function(){s.animate({scrollTop:0},n.speed)}),e.scroll(function(){var o=e.scrollTop();n.autohide&&(o>n.offset?i.fadeIn(n.speed):i.fadeOut(n.speed))})
		}
	});
})(jQuery);
// String#format
String.prototype.format = function(args) {
	if (arguments.length > 0) {
		var result = this;
		if (arguments.length == 1 && typeof (args) == "object") {
			for (var key in args) {
				var reg=new RegExp ("({"+key+"})","g");
				result = result.replace(reg, args[key]);
			}
		} else {
			for (var i = 0; i < arguments.length; i++) {
				if (arguments[i]==undefined){
					return "";
				} else {
					var reg=new RegExp ("({["+i+"]})","g");
					result = result.replace(reg, arguments[i]);
				}
			}
		}
		return result;
	} else {
		return this;
	}
};