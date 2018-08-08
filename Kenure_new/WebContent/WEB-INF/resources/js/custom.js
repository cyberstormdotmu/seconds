$(document).ready(function() {

    $('.menu-toggle').on('click', function(){
      $('.menu-toggle').toggleClass('open');
	  $('#content').toggleClass('collapse1');
	  $('.leftbar').toggleClass('collapse1');
      return false;
    });
 							

	$('.user_info').on('click', function(){
		 $('.user_info').addClass('active');
      $('.user_link').addClass('open');
    });
	
	  $(document).click(function (e) {
  	  e.stopPropagation();
  	  var container = $(".user_info");

    //check if the clicked area is dropDown or not
    if (container.has(e.target).length === 0) {
    	$('.user_info').removeClass('active');
        $('.user_link').removeClass('open');
		
    }/*else{
    	$('.user_info').addClass('active');
        $('.user_link').addClass('open');
    }*/
	});
	  

	

	$(".dk-select.custom-select").mouseup(function(){
		var $this = $(this);
		if($this.hasClass('dk-select-open-down') || $this.hasClass('dk-select-open-up'))
			$(this).siblings('.tooltip-block').hide();
		else
			$(this).siblings('.tooltip-block').show();
	});
	
	// CustomSelect
	$(".custom-select").dropkick({
		mobile: true
	});

	
	$(window).load(function(){	  
	  var mh=$(window).innerHeight()-$('.header').innerHeight()-$('.footer').innerHeight();
	  $('.leftbar').css({height:mh});
	  
	  $(window).resize(function(){
		var mh=$(window).innerHeight()-$('.header').innerHeight()-$('.footer').innerHeight();
	  	$('.leftbar').css({height:mh});
	  });
	  
	});


	// ===========left menu accordian===============   
	 $(document).ready(function() {
	 	
	 	$(".menulist").find(".selected").removeClass(); 

	    // Sub nav Open
	    $(".menulist li a").click(function(){
	    	var $this = $(this).closest('li');
	    	if($this.hasClass('selected'))
	    	{
	    		$this.removeClass('selected').find('.selected').removeClass('selected');
	    		$this.find(".sub-menu-nav").stop(true,true).slideUp();
	    	}else{    		
	    		$this.siblings('li').removeClass('selected').find(".sub-menu-nav").stop(true,true).slideUp().find('li.selected').removeClass('selected');
	    		$this.addClass('selected');
	    		$this.find(">.sub-menu-nav").stop(true,true).slideDown();
	    	}
	    });

	}); 


});




//==toggle=============================

$(document).ready(function() {

    // Sub nav Open

    var link = $(this).closest('.optopn-link');


    $(".optopn-box .toggle").click(function(e){
        e.stopImmediatePropagation();
        $(this).toggleClass('active');
        $(this).next('.optopn-link').slideToggle();
    });


    $('body').click(function(){
       $(".optopn-box .toggle").removeClass('active');
       $(".optopn-link").slideUp(); 
   });

});


//==tab=============================
/*$(document).ready(function() {
        //Horizontal Tab
        $('#parentHorizontalTab').easyResponsiveTabs({
            type: 'default', //Types: default, vertical, accordion
            width: 'auto', //auto or any width like 600px
            fit: true, // 100% fit in a container
            tabidentify: 'hor_1', // The tab groups identifier
            activate: function(event) { // Callback function if tab is switched
            	var $tab = $(this);
            	var $info = $('#nested-tabInfo');
            	var $name = $('span', $info);
            	$name.text($tab.text());
            	$info.show();
            }
        });

        // Child Tab
        $('#ChildVerticalTab_1').easyResponsiveTabs({
        	type: 'default',
        	width: 'auto',
        	fit: true,
            tabidentify: 'ver_1', // The tab groups identifier
            activetab_bg: '#fff', // background color for active tabs in this group
            inactive_bg: '#F5F5F5', // background color for inactive tabs in this group
            active_border_color: '#c1c1c1', // border color for active tabs heads in this group
            active_content_border_color: '#5AB1D0' // border color for active tabs contect in this group so that it matches the tab head border
        });

        //Vertical Tab
        $('#parentVerticalTab').easyResponsiveTabs({
            type: 'vertical', //Types: default, vertical, accordion
            width: 'auto', //auto or any width like 600px
            fit: true, // 100% fit in a container
            closed: 'accordion', // Start closed if in accordion view
            tabidentify: 'hor_1', // The tab groups identifier
            activate: function(event) { // Callback function if tab is switched
            	var $tab = $(this);
            	var $info = $('#nested-tabInfo2');
            	var $name = $('span', $info);
            	$name.text($tab.text());
            	$info.show();
            }
        });
    });*/