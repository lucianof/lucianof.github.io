$(document).ready(function () {	
	$('.slideshow').cycle({
		fx: 'fade',
		timeout: 6000
	});
	$('#donate').hover(function(){
		this.src='/assets/img/btn_donate_rollover.jpg';
	}, function(){
		this.src='/assets/img/btn_donate.jpg';
	});
	if($(window).width() > 767){
		equalHeight($('#content > div'));
	}
});

function equalHeight(group) {
	var tallest = 0;
	group.each(function() {
		var thisHeight = $(this).outerHeight();
		if(thisHeight > tallest) {
			tallest = thisHeight;
		}
	});
	group.each(function() {
		var thisOuterHeight = $(this).outerHeight();
		var thisHeight = $(this).height();
		$(this).height(thisHeight + tallest - thisOuterHeight);
	});
}