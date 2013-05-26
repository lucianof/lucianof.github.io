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
	equalHeight($('#content > div'), 768);
});

function equalHeight(group, minWindowWidth) {
	var timer = null;
	var setEqualHeightTimeout = function() {
      clearTimeout(timer);
      return timer = setTimeout(makeEqualHeight, 20);
    };
	var makeEqualHeight = function(){
		$(group).height("auto");
		if($(window).width() >= minWindowWidth){
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
	}
	makeEqualHeight();
	$(window).resize(setEqualHeightTimeout);
}