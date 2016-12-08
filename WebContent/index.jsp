<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>File Box- Backup with the best!</title>
<link rel="stylesheet" href="index.css">
</head>
<body>
<header>
<h1 align=center >FILE BOX</h1>
</header>



<div class="login-page">
  <div class="form">
 
    <form class="login-form" id="login_form" action="login" method=POST>
      <input type="text" id="uid" name="username" placeholder="username"/>
      <input type="password" id="pwd" name="password" placeholder="password"/>
      <button type="submit">
	  login</button>
	<div id="server_result"></div>
	<input id="validation_check" type="hidden" value="${invalidEntry}"/>
	<input id="action_user_logout" type="hidden" value="${userLogout}">
    </form>
  </div>
</div>

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