$(document).ready(function () {	
	$('.slideshow').cycle({
		fx: 'fade',
		timeout: 6000
	});
	$('#donate').hover(function(){
		this.src='assets/img/btn_donate_rollover.jpg';
	}, function(){
		this.src='assets/img/btn_donate.jpg';
	});
});