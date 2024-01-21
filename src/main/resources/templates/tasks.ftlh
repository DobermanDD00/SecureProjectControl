<!DOCTYPE html>
<head>
    <title>Task Manager</title>
</head>
<body>
<h1><a href="/">Task Manager</a></h1>
<hr>
<#if user.email??>
    <h3>Имя пользователя: <i>${user.name}</i></h3>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <input type="submit" value="Выйти"/>
    </form>
    <#if user.isAdmin()>
        <a href="/admin">Панель администратора</a>
    </#if>
<#else>
    <a href="/login">Войти</a></h1>
</#if>
<hr>
<h4>Все задачи:</h4>
<form action="/" method="get">
    Поиск по названию объявления: <input type="text" name="title">
    <input type="submit" value="Поиск"/>
</form>
<#--<#list tasks as task>-->
<#--    <div>-->
<#--        <p><b>${task.title}</b> | <b>${task.description}</b><a href="/task/${task.id}">Подробнее...</a></p>-->
<#--    </div>-->
<#--<#else>-->
<#--    <h3>Задач нет</h3>-->
<#--</#list>-->
<#list tasksUser as task>
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