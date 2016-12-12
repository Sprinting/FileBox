<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>File Box- Backup with the best!</title>
</head>
<body>
<header>


</header>
<nav>
Navbar Placeholder
</nav>
<hr>
<article>
Intro article section
</article>
<hr>
<section style="max-width:25%" id="section_process" data-state="">
<form id="login_form" action="login" method=POST>
	<div>
	<label for="uid">Username: </label>
	<input type=text id="uid" name="username"/>
	</div>
	<div>
	<label for="pwd">Password: </label>
	<input type=text id="pwd" name="password"/>
	</div>
	<hr>
	<div id="submit_button">
		<button type="submit">
		Sign in
		</button>
	</div>
	<div id="server_result"></div>
	<input id="validation_check" type="hidden" value="${invalidEntry}"/>
	<input id="action_user_logout" type="hidden" value="${userLogout}">
</form>
</section>
<script type="text/javascript">
	if(document.getElementById('validation_check').value)
		{
			document.getElementById('server_result').innerHTML="Your input was rejected by the server<br>"
				+"Please try again!";
			document.getElementById('server_result').style.color='red';
		}
	if(document.getElementById('action_user_logout').value)
	{
		document.getElementById('server_result').innerHTML="Successfully logged out!";
		document.getElementById('server_result').style.color='red';
	}
</script>
</body>
</html>
