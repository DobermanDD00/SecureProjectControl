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
<h4>Товары со всей России</h4>
<form action="/" method="get">
    Поиск по названию объявления: <input type="text" name="title">
    <input type="submit" value="Поиск"/>
</form>
<#list products as product>
    <div>
        <p><b>${product.title}</b> | <b>${product.description}</b><a href="/product/${product.id}">Подробнее...</a></p>
    </div>
<#else>
    <h3>Товаров нет</h3>
</#list>
<#if user.email??>
    <hr>
    <h3>Создать новую задачу</h3>
    <form action="/product/create" method="post" enctype="multipart/form-data">
        Название: <input type="text" name="title"/><br><br>
        Описание: <input type="text" name="description"/><br><br>

        Руководитель:
        <select class="product.lead" name = "lead" id = "product.lead.id">
            <#list users as user>
                <option value="${user.id}"> ${user.name}</option>
            </#list>
        </select>
        Исполнитель:
        <select class="product.performer" name = "performer" id = "product.performer.id">
            <#list users as user>
                <option value="${user.id}"> ${user.name}</option>
            </#list>
        </select>
        Статус:
        <select class="product.status" name = "status" id = "product.status.id">
            <#list statuses as status>
                <option value="${status.id}"> ${status.title}</option>
            </#list>
        </select>




        <hr>
        <#--        Руководитель: <input type="text" name="description"/><br><br>-->
        <#--        Статус: <input type="text" name="description"/><br><br>-->
        <#--        Активность: <input type="text" name="description"/><br><br>-->


        Первая фотография: <input type="file" name="file1"/><br><br>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <input type="submit" value="Добавить задачу"/>
    </form>
</#if>
</body>
</html>