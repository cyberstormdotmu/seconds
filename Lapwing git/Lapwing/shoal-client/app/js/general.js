$(document).ready(function() {
    // On scroll header small
    $(window).scroll(function(e) {
        if($(window).scrollTop() > 200)
            $(".wrapper").addClass('small-header');
        else
            $(".wrapper").removeClass('small-header');
    });

    //Prevent Page Reload on all # links
    $("a[href='#']").click(function(e) {
        e.preventDefault();
    });

    //placeholder
    $("[placeholder]").each(function () {
        $(this).attr("data-placeholder", this.placeholder);

        $(this).bind("focus", function () {
            this.placeholder = '';
        });
        $(this).bind("blur", function () {
            this.placeholder = $(this).attr("data-placeholder");
        });
    });

    // Tooltip
    $('[data-toggle="tooltip"]').tooltip();

    // $('.dropdown').hover(function() {
    //     $(this).find('.dropdown-menu').show();
    // }, function() {
    //     $(this).find('.dropdown-menu').hide();
    // });
});