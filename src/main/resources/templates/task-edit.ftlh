<!DOCTYPE html>
<html>
<head>
    <title>Task Manager</title>
</head>
<body>
<h1><a href="/">Task Manager</a></h1>
<h4>Редактирование задачи</h4>
<#--<#list images as img>-->
<#--    <img src="/images/${img.id}" height="60px"/><br><br>-->
<#--</#list>-->
<form action="/task/update/${task.id}" method="post">

    <b>Название задачи: </b><input type="text" name="title" value="${task.title}"/><br>
    <b>Описание задачи: </b><input type="text" name="description" value="${task.description}"/><br>
    Руководитель:
    <select class="task.lead" name = "lead" id = "task.lead.id">
        <option selected value="${task.lead.id}">${task.lead.name}</option>
        <#list users as user>
            <option value="${user.id}"> ${user.name}</option>
        </#list>
<#--        TODO Исправить в будущем, убрать повторения в селекте, возникающие при задании значения по умолчанию -->
    </select>
    Исполнитель:
    <select class="task.performer" name = "performer" id = "task.performer.id">
        <option selected value="${task.performer.id}">${task.performer.name}</option>
        <#list users as user>
            <option value="${user.id}"> ${user.name}</option>
        </#list>
    </select>
    Статус:
    <select class="task.status" name = "status" id = "task.status.id">
        <option selected value="${task.status.id}">${task.status.title}</option>

        <#list statuses as status>
            <option value="${status.id}"> ${status.title}</option>
        </#list>
    </select>
<#--    <b>Руководитель: </b><a href="/user/${task.lead.id}">${task.lead.name}</a><br>-->
<#--    <b>Исполнитель: </b><a href="/user/${task.performer.id}">${task.performer.name}</a><br>-->
<#--    <b>Статус: </b>${task.status.title}<br>-->
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <input type="submit" value="Подтвердить изменения"/>
</form>
</body>
</html>