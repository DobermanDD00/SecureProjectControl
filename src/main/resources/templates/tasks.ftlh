<!DOCTYPE html>
<head>
    <title>Task Manager</title>
</head>
<body>
<h1><a href="/">Task Manager</a></h1>
<hr>
<h3>Имя пользователя: <i>${user.name}</i></h3>
<form action="/logout" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <input type="submit" value="Выйти"/>
</form>
<#if user.isAdmin()>
    <a href="/admin">Панель администратора</a>
</#if>

<hr>
<h4>Все задачи:</h4>
<form action="/" method="get">
    Поиск по названию объявления: <input type="text" name="title">
    <input type="submit" value="Поиск"/>
</form>
<#list listTasksToUser as task>
    <div>
        <p><b>${task.title}</b> | <b>${task.description}</b><a href="/task/${task.id}">Подробнее...</a></p>
    </div>
<#else>
    <h3>Задач нет</h3>
</#list>
<#if user.email??>
    <hr>
    <h4><a href="/task/create">Создать новую задачу</a></h4>

</#if>
</body>
</html>