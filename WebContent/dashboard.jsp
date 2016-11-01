<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>${sessionScope.username}-Dashboard</title>
    <link rel="stylesheet" href="dashboard.css">
	<script type="text/javascript" src="dashboard.js"></script>
	</head>
<body>
	<aside class="left">
		Welcome,<br>${username}
	</aside>
	<hr>
	<section class="upload">
		<div class="upload form">
			<form method="post" action="example/upload" enctype="multipart/form-data">
				<label for="upload">Select a file to upload</label>
				<input id="upload" type="file" name="filesName">
				<hr>
				<button type="Submit">Upload</button>
			</form>
		</div>
	</section>
	<hr>
	<section id="dashboard">
	</section>
	<nav class="bottom">
	<a href="/FileBox/logout">Logout</a>
	</nav>
</body>
</html>
