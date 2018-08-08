<head>
<script type="text/javascript">
	$(document).ready(
			function() {

				$('.menu-toggle').on('click', function() {
					$('.menu-toggle').toggleClass('open');
					$('#content').toggleClass('collapse1');
					$('.leftbar').toggleClass('collapse1');
					return false;
				});

				$('.user_info').on('click', function() {
					$('.user_info').addClass('active');
					$('.user_link').addClass('open');
				});

				$(document).click(function(e) {
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

				$(".dk-select.custom-select").mouseup(
						function() {
							var $this = $(this);
							if ($this.hasClass('dk-select-open-down')
									|| $this.hasClass('dk-select-open-up'))
								$(this).siblings('.tooltip-block').hide();
							else
								$(this).siblings('.tooltip-block').show();
						});

				// CustomSelect
				$(".custom-select").dropkick({
					mobile : true
				});

				$(window).load(
						function() {
							var mh = $(window).innerHeight()
									- $('.header').innerHeight()
									- $('.footer').innerHeight();
							$('.leftbar').css({
								height : mh
							});

							$(window).resize(
									function() {
										var mh = $(window).innerHeight()
												- $('.header').innerHeight()
												- $('.footer').innerHeight();
										$('.leftbar').css({
											height : mh
										});
									});

						});
			});
</script>
</head>
<body>
	<div id="wrapper">
		<div id="header">
			<div class="logo" data-ng-show="${isAdmin}">
				<a href="adminDashboard" onclick="onClickMenu(this.id)"><img
					src="${pageContext.request.contextPath}/images/logo.jpg" alt="" /></a>
			</div>
			<div class="logo" data-ng-show="${isCustomer}">
				<a href="customerDashboard" onclick="onClickMenu(this.id)"><img
					src="${pageContext.request.contextPath}/images/logo.jpg" alt="" /></a>
			</div>
			<div class="logo" data-ng-show="${isConsumer}">
				<a href="consumerDashboard" onclick="onClickMenu(this.id)"><img
					src="${pageContext.request.contextPath}/images/logo.jpg" alt="" /></a>
			</div>
			<div class="logo" data-ng-show="${isInstaller}">
				<a href="installerDashboard" onclick="onClickMenu(this.id)"><img
					src="${pageContext.request.contextPath}/images/logo.jpg" alt="" /></a>
			</div>

			<a class="menu-toggle" href="#menu"> <i class="menu-icon"> <span
					class="line1"></span> <span class="line2"></span> <span
					class="line3"></span>
			</i>
			</a>

			<div class="hd-right">
				<div class="user_info" id="header_user_info">
					<ul>
						<li><font size="4">${userData.userName}</font></li>
						<!-- <li><a href="#" title=""><img src="${pageContext.request.contextPath}/images/user-icon.png"
								alt=""></a></li> -->

						<li data-ng-show="${isAdmin}"><a href="adminProfile"><img
								src="${pageContext.request.contextPath}/images/user-icon.png" alt=""></a></li>
						<li data-ng-show="${isCustomer}"><a href="customerProfile"><img
								src="${pageContext.request.contextPath}/images/user-icon.png" alt=""></a></li>
						<li data-ng-show="${isConsumer}"><a href="consumerProfile"><img
								src="${pageContext.request.contextPath}/images/user-icon.png" alt=""></a></li>
						<li data-ng-show="${isInstaller}"><a href="installerProfile"><img
								src="${pageContext.request.contextPath}/images/user-icon.png" alt=""></a></li>

						<li><a href="${pageContext.request.contextPath}/logout" title=""><img src="${pageContext.request.contextPath}/images/logut.png"
								alt=""></a></li>

					</ul>
				</div>
			</div>
		</div>
	</div>
</body>
