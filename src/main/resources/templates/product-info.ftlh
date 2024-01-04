<!DOCTYPE html>
<html>
<head>
    <title>Task Manager</title>
</head>
<body>
<h1><a href="/">Task Manager</a></h1>
<h4>Подробная информация о товаре</h4>
<#list images as img>
    <img src="/images/${img.id}" height="60px"/><br><br>
</#list>
<b>Название товара: </b>${product.title}<br>
<b>Описание товара: </b>${product.description}<br>

<b>Руководитель: </b><a href="/user/${product.lead.id}">${product.lead.name}</a><br>
<b>Исполнитель: </b><a href="/user/${product.performer.id}">${product.performer.name}</a><br>
<b>Статус: </b>${product.status.title}<br>

</body>
</html>