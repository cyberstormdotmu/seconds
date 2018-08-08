$(document).ready(function() {
    var $nav = $(".main-navigation");
    var $speed = parseInt($nav.attr("animation-speed"));
    var $mobileMode;    

    $nav.find("ul").each(function(){
        $(this).siblings("a").closest('li').addClass('has-menu');
    });

    $nav.find("ul li").hover(function(){
        var $this = $(this).find("> ul");
        if($this.length > 0 && $(window).innerWidth() > $nav.attr("mobile-menu")){
            if($(window).innerWidth() <  parseInt($this.offset().left) + $this.innerWidth())
                $this.addClass('reverse');          
            else
                $this.removeClass('reverse');
        }
    });

    $nav.find("ul li a .arrow-icon").click(function(e){     
        e.stopImmediatePropagation()
        var $this = $(this).closest('li');      
        if($(window).innerWidth() <= $nav.attr("mobile-menu")){
            if(!$this.hasClass('open')){
                $this.siblings('.open').find("ul").stop(true, true).slideUp($speed);
                $this.siblings('.open').removeClass('open').find(".open").removeClass('open');
                $this.addClass('open').find("> ul").slideDown($speed, function(){
                    resizeScroll();
                });
            }else{
                $this.find("ul").stop(true, true).slideUp($speed, function(){
                    resizeScroll();
                });
                $this.removeClass('open').find(".open").removeClass('open');                
            }
        };
    });

    $nav.find("a").click(function(e){
        var $this = $(this);        
        if($(window).innerWidth() <= $nav.attr("mobile-menu") && $nav.attr("only-arrow-click") == "false"){         
            e.preventDefault();
            $this.find(".arrow-icon").click();
        }
    });

    $(".nav-icon, .menu-overlay").click(function(){
        $("body").toggleClass('menu-open');
    });


    // Custom Scroll
    function addScroll(){
        $nav.niceScroll($nav.find("> ul"),{
            cursorcolor:$nav.attr("scroll-color"),
            background:$nav.attr("scroll-bg"),
            cursorwidth:$nav.attr("scroll-width"),
            horizrailenabled:false,
            cursorborder:"none",
            cursorborderradius:0,
            autohidemode:false,
            bouncescroll:false
        });
    }
    function resizeScroll(){
        $nav.getNiceScroll().resize();
    }
    function removeScroll(){
        $nav.getNiceScroll().remove();
    }

    if($(window).innerWidth() <= $nav.attr("mobile-menu")){
        $mobileMode = true;
        addScroll();
    }else{
        $mobileMode = false;
    }
    $(window).resize(function(){
        var $w = $(window).innerWidth();
        if($mobileMode && $(window).innerWidth() > $nav.attr("mobile-menu")){
            $mobileMode = false;
            removeScroll();
            $nav.removeAttr('style');           
            $nav.children('ul').removeAttr('style');            
        }
        else if(!$mobileMode && $(window).innerWidth() <= $nav.attr("mobile-menu")){
            $mobileMode = true;
            addScroll();
        }
    });

});