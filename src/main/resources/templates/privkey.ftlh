<!DOCTYPE html>
<head>
    <title>Private Key</title>
</head>
<body>
<h1><a href="/">Private Key</a></h1>
<hr>
<h3>Имя пользователя: <i>${user.name}</i></h3>
<form action="/key" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <textarea id="privkey" name="privkey" rows="10" cols="50"></textarea>
    <input type="submit" value="Upload"/>
</form>
</body>
</html>