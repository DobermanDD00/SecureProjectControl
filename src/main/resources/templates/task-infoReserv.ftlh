<!DOCTYPE html>
<html>
<head>
    <title>Task Manager</title>
</head>
<body>
<h1><a href="/">Task Manager</a></h1>
<h4>Подробная информация о задаче</h4>
<#--<#list images as img>-->
<#--    <img src="/images/${img.id}" height="60px"/><br><br>-->
<#--</#list>-->
<b>Название задачи: </b>${task.title}<br>
<b>Описание задачи: </b>${task.description}<br>

<b>Руководитель: </b><a href="/user/${task.lead.id}">${task.lead.name}</a><br>
<b>Исполнитель: </b><a href="/user/${task.performer.id}">${task.performer.name}</a><br>
<b>Статус: </b>${task.status.title}<br>
<b>История: </b ><span style="white-space: pre-line">${task.history}</span><br>

<form action="/task/edit/${task.id}">
    <input type="submit" value="Редактировать задачу"/>
</form>

</body>
</html>