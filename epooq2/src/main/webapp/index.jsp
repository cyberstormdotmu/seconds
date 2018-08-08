<html>
<head>
<title>
Epooq
</title>
<style type="text/css">
body {
	width: 100%;
	height: 100%;
	background-color: #ffffff;
}

#player {
	position: absolute;
	width: 100%;
	height: 100%;
	overflow: hidden;
	margin: 0px;
	padding: 0px;
	left: 0px;
	top: 0px;
}
</style>
<script type="text/javascript" src="resources/js/jquery-1.9.1.js"></script>
<script type="text/javascript">
	$(document).ready(function() {

		$("#player").click(function(e) {
			if (document.getElementById("player").paused) {
				document.getElementById("player").play();
			} else {
				window.location.href = 'home.html';
			}
		});
	});
</script>
</head>
<body>
<video id="player" onended="window.location.href = 'home.html';">
		<source src="resources/intro_epooq.mp4" type="video/mp4" />
	</video>
</body>
</html>
