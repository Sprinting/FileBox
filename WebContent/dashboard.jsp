<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>${sessionScope.username}-Dashboard</title>
    
    <script type="text/javascript" src="router.js"></script>
	<script type="text/javascript" src="dashboard.js"></script>

	</head>
<body>
	<aside class="left">
		Welcome,<br>${username}
	</aside>
	<nav class="navbar">
        <ul>
            <li><a href="#upload">Upload</a></li>
            <li><a href="#dashboard">Dashboard</a></li>
        </ul>
    </nav>
	<hr>
	<section class="upload">
		<div class="upload form">
			<form method="post" action="example/upload" enctype="multipart/form-data">
				<label for="upload">Select a file to upload</label>
				<input id="upload" type="file" name="filesName">
				<button type="Submit">Upload</button>
			</form>
		</div>
	</section>
	<hr>
	<section id="dashboard">
        <section id="context_links">
        </section>
        <section id="user_files">
        </section>
        <section id="share_files">
        </section>
	</section>
	<nav class="bottom">
	<a href="/FileBox/logout">Logout</a>
	</nav>
</body>
</html>
