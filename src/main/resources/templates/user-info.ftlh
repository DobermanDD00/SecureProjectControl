<!DOCTYPE html>
<html>
<head>
    <title>Task Manager</title>
</head>
<body>
<h1><a href="/">Task Manager</a></h1>
<h3>Подробная информация о пользователе ${user.name}</h3>
<b>Номер телефона: </b> ${user.phoneNumber}<br>
<b>Email: </b> ${user.email}<br>
<b>Имя пользователя: </b>${user.name}<br>
<b>Пароль пользователя: </b>${user.password}<br>
<#--<h4>Задачи пользователя ${user.name}</h4>-->
<#--<#list products as task>-->
<#--    <div>-->
<#--        <p><b>${task.title}</b> ${task.description} руб. | <a href="/task/${task.id}">Подробнее...</a></p>-->
<#--    </div>-->
<#--    <#else>-->
<#--    <h3>Задач нет</h3>-->
<#--</#list>-->
<#if errorMessage??>
    <h2 style="color: red">${errorMessage}</h2>
</#if>
</body>
</html>