jQuery( document ).ready( function( $ ) {

	var $menu = $('#menu'),
	  $menulink = $('.menu-toggle'),
	  $menuTrigger = $('.has-submenu > a');
	  $FlymenuTrigger = $('.has-submenu a.fly');

	$menulink.click(function(e) {
		e.preventDefault();
		$menulink.toggleClass('open');
		$menu.toggleClass('active');
	});

	$menuTrigger.click(function(e) {
		e.preventDefault();
		var $this = $(this);		
		//$(".sub-menu").removeClass("active");
		//$(this).addClass('active').next('ul').addClass('active');
		//$(this).toggleClass('active').next('ul').toggleClass('active');
		if($(this).parent("li").hasClass("active"))
		{
			$(".menu li").removeClass("active");
		}
		else
		{
			$(".menu li").removeClass("active");
			$(this).parent("li").toggleClass("active")
		}
		//$this.toggleClass('active').next('ul').toggleClass('active');
	});
	
	$FlymenuTrigger.click(function(e) {
		e.preventDefault();
		var $this = $(this);		
		//$(".sub-menu").removeClass("active");
		//$(this).addClass('active').next('ul').addClass('active');
		//$(this).toggleClass('active').next('ul').toggleClass('active');
		if($(this).parent("li").hasClass("flyactive"))
		{
			$(".menu li").removeClass("flyactive");
		}
		else
		{
			$(".menu li").removeClass("flyactive");
			$(this).parent("li").toggleClass("flyactive")
		}
		//$this.toggleClass('active').next('ul').toggleClass('active');
	});

});