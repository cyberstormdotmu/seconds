
! function(window, document, $) {
    "use strict";
    var $doc = $(document);
    $.site = $.site || {}, $.extend($.site, {
        _queue: {
            prepare: [],
            run: [],
            complete: []
        },
        run: function() {
            var self = this;
            this.dequeue("prepare", function() {
                self.trigger("before.run", self)
            }), this.dequeue("run", function() {
                self.dequeue("complete", function() {
                    self.trigger("after.run", self)
                })
            })
        },
        dequeue: function(name, done) {
            var self = this,
                queue = this.getQueue(name),
                fn = queue.shift(),
                next = function() {
                    self.dequeue(name, done)
                };
            fn ? fn.call(this, next) : $.isFunction(done) && done.call(this)
        },
        getQueue: function(name) {
            return $.isArray(this._queue[name]) || (this._queue[name] = []), this._queue[name]
        },
        extend: function(obj) {
            return $.each(this._queue, function(name, queue) {
                $.isFunction(obj[name]) && (queue.push(obj[name]), delete obj[name])
            }), $.extend(this, obj), this
        },
        trigger: function(name, data, $el) {
            "undefined" != typeof name && ("undefined" == typeof $el && ($el = $doc), $el.trigger(name + ".site", data))
        },
        throttle: function(func, wait) {
            var context, args, result, _now = Date.now || function() {
                    return (new Date).getTime()
                },
                timeout = null,
                previous = 0,
                later = function() {
                    previous = _now(), timeout = null, result = func.apply(context, args), context = args = null
                };
            return function() {
                var now = _now(),
                    remaining = wait - (now - previous);
                return context = this, args = arguments, 0 >= remaining ? (clearTimeout(timeout), timeout = null, previous = now, result = func.apply(context, args), context = args = null) : timeout || (timeout = setTimeout(later, remaining)), result
            }
        },
        resize: function() {
            if (document.createEvent) {
                var ev = document.createEvent("Event");
                ev.initEvent("resize", !0, !0), window.dispatchEvent(ev)
            } else {
                element = document.documentElement;
                var event = document.createEventObject();
                element.fireEvent("onresize", event)
            }
        }
    }), $.configs = $.configs || {}, $.extend($.configs, {
        data: {},
        get: function(name) {
            for (var callback = function(data, name) {
                    return data[name]
                }, data = this.data, i = 0; i < arguments.length; i++) name = arguments[i], data = callback(data, name);
            return data
        },
        set: function(name, value) {
            this.data[name] = value
        },
        extend: function(name, options) {
            var value = this.get(name);
            return $.extend(!0, value, options)
        }
    }), $.components = $.components || {}, $.extend($.components, {
        _components: {},
        register: function(name, obj) {
            this._components[name] = obj
        },
        init: function(name, context, args) {
            var self = this;
            if ("undefined" == typeof name) $.each(this._components, function(name) {
                self.init(name)
            });
            else {
                context = context || document, args = args || [];
                var obj = this.get(name);
                if (obj) switch (obj.mode) {
                    case "default":
                        return this._initDefault(name, context);
                    case "init":
                        return this._initComponent(name, obj, context, args);
                    case "api":
                        return this._initApi(name, obj, args);
                    default:
                        return this._initApi(name, obj, context, args), void this._initComponent(name, obj, context, args)
                }
            }
        },
        call: function(name, context) {
            var args = Array.prototype.slice.call(arguments, 2),
                obj = this.get(name);
            return context = context || document, this._initComponent(name, obj, context, args)
        },
        _initDefault: function(name, context) {
            if ($.fn[name]) {
                var defaults = this.getDefaults(name);
                $("[data-plugin=" + name + "]", context).each(function() {
                    var $this = $(this),
                        options = $.extend(!0, {}, defaults, $this.data());
                    $this[name](options)
                })
            }
        },
        _initComponent: function(name, obj, context, args) {
            $.isFunction(obj.init) && obj.init.apply(obj, [context].concat(args))
        },
        _initApi: function(name, obj, args) {
            "undefined" == typeof obj.apiCalled && $.isFunction(obj.api) && (obj.api.apply(obj, args), obj.apiCalled = !0)
        },
        getDefaults: function(name) {
            var component = this.get(name);
            return component && "undefined" != typeof component.defaults ? component.defaults : {}
        },
        get: function(name, property) {
            return "undefined" != typeof this._components[name] ? "undefined" != typeof property ? this._components[name][property] : this._components[name] : void 0
        }
    })
}(window, document, jQuery);

/********************************************************************************/

! function(window, document, $) {
    "use strict";
    var $body = $(document.body);
    $.configs.set("site", {
        
    }), window.Site = $.site.extend({
        run: function(next) {
            this.polyfillIEWidth(), "undefined" != typeof $.site.menu && $.site.menu.init(), "undefined" != typeof $.site.menubar && ($(".site-menubar").on("changing.site.menubar", function() {
              
            }), $(document).on("click", '[data-toggle="menubar"]', function() {
                return $.site.menubar.toggle(), !1
            }), $.site.menubar.init(), Breakpoints.on("change", function() {
                $.site.menubar.change()
            })), 
           
            $.components.init();
        },
        polyfillIEWidth: function() {
            if (navigator.userAgent.match(/IEMobile\/10\.0/)) {
                var msViewportStyle = document.createElement("style");
                msViewportStyle.appendChild(document.createTextNode("@-ms-viewport{width:auto!important}")), document.querySelector("head").appendChild(msViewportStyle)
            }
        },
        loadAnimate: function(callback) {
            return $.components.call("animsition", document, callback)
        },        
    })
}(window, document, jQuery);

/********************************************************************************************/

! function(window, document, $) {
    "use strict";
    var pluginName = "responsiveHorizontalTabs",
        defaults = {
            navSelector: ".nav-tabs",
            itemSelector: ">li",
            dropdownSelector: ">.dropdown",
            dropdownItemSelector: "li",
            tabSelector: ".tab-pane",
            activeClassName: "active"
        },
        Plugin = function(el, options) {
            var $tabs = this.$tabs = $(el);
            this.options = options = $.extend(!0, {}, defaults, options);
            var $nav = this.$nav = $tabs.find(this.options.navSelector),
                $dropdown = this.$dropdown = $nav.find(this.options.dropdownSelector),
                $items = this.$items = $nav.find(this.options.itemSelector).filter(function() {
                    return !$(this).is($dropdown)
                });
            this.$dropdownItems = $dropdown.find(this.options.dropdownItemSelector), this.$tabPanel = this.$tabs.find(this.options.tabSelector), this.breakpoints = [], $items.each(function() {
                $(this).data("width", $(this).width())
            }), this.init(), this.bind()
        };
    Plugin.prototype = {
        init: function() {
            if (0 !== this.$dropdown.length) {
                this.$dropdown.show(), this.breakpoints = [];
                var length = this.length = this.$items.length,
                    dropWidth = this.dropWidth = this.$dropdown.width(),
                    total = 0;
                if (this.flag = length, 1 >= length) return void this.$dropdown.hide();
                for (var i = 0; length - 2 > i; i++) 0 === i ? this.breakpoints.push(this.$items.eq(i).outerWidth() + dropWidth) : this.breakpoints.push(this.breakpoints[i - 1] + this.$items.eq(i).width());
                for (i = 0; length > i; i++) total += this.$items.eq(i).outerWidth();
                this.breakpoints.push(total), this.layout()
            }
        },
        layout: function() {
            if (!(this.breakpoints.length <= 0)) {
                for (var width = this.$nav.width(), i = 0, activeClassName = this.options.activeClassName, active = this.$tabPanel.filter("." + activeClassName).index(); i < this.breakpoints.length && !(this.breakpoints[i] > width); i++);
                if (i !== this.flag) {
                    if (this.$items.removeClass(activeClassName), this.$dropdownItems.removeClass(activeClassName), this.$dropdown.removeClass(activeClassName), i === this.breakpoints.length) this.$dropdown.hide(), this.$items.show(), this.$items.eq(active).addClass(activeClassName);
                    else {
                        this.$dropdown.show();
                        for (var j = 0; j < this.length; j++) i > j ? (this.$items.eq(j).show(), this.$dropdownItems.eq(j).hide()) : (this.$items.eq(j).hide(), this.$dropdownItems.eq(j).show());
                        i > active ? this.$items.eq(active).addClass(activeClassName) : (this.$dropdown.addClass(activeClassName), this.$dropdownItems.eq(active).addClass(activeClassName))
                    }
                    this.flag = i
                }
            }
        },
        bind: function() {
            var self = this;
            $(window).resize(function() {
                self.layout()
            })
        }
    }, $.fn[pluginName] = function(options) {
        if ("string" == typeof options) {
            var method = options,
                method_arguments = Array.prototype.slice.call(arguments, 1);
            return /^\_/.test(method) ? !1 : this.each(function() {
                var api = $.data(this, pluginName);
                api && "function" == typeof api[method] && api[method].apply(api, method_arguments)
            })
        }
        return this.each(function() {
            $.data(this, pluginName) ? $.data(this, pluginName).init() : $.data(this, pluginName, new Plugin(this, options))
        })
    }
}(window, document, jQuery);

/******************************************************************************/

$.components.register("horizontalTab", {
    mode: "init",
    init: function(context) {
        $.fn.responsiveHorizontalTabs && $(".nav-tabs-horizontal", context).responsiveHorizontalTabs()
    }
});

/********************************************************************/

var Site = window.Site;
  $(document).ready(function() {
	Site.run(); });
  