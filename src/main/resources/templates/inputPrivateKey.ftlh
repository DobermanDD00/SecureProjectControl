<!DOCTYPE html>
<head>
    <title>Private Key</title>
</head>
<body>
<h1><a href="/">Private Key</a></h1>
<hr>
<h3>Имя пользователя: <i>${user.name}</i></h3>
<h4>Введите приватный ключ</h4>
<form action="/key" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <textarea id="privkey" name="privkey" rows="10" cols="50"></textarea>


    <input type="submit" value="Upload"/>
    <br>
    <span>${message}</span>

</form>
<br>
<br>


<form action="/uploadFileKey" method="post" enctype="multipart/form-data">
    Файл с приватным ключом пользователя: <input type="file" name="file"/><br><br>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <input type="submit" value="Загрузить файл"/>
</form>
</body>
</html>