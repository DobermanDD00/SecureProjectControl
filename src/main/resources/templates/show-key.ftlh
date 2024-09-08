<!DOCTYPE HTML>
<html lang="en" xmlns:th="http://thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <title>Spring Boot Thymeleaf Application - Form Samples - Inputs</title>
</head>
<body>

<h3>Создан пользователь</h3>

<form th:method="post" th:action="@{/downloadPrivateKey2}" th:object="${userDto.getUser()}">
    <fieldset>
        <div>
            <label>Имя пользователя: </label>
            <span th:text="*{name}"></span>
        </div>
        <div>
            <label>Публичный ключ: </label>
            <span th:text="${PublicKey}"></span>
        </div>
        <div>
            <label>Приватный ключ: </label>
            <span th:text="${PrivateKey}"></span>
        </div>
        <input type="reset" id="resetButton2" name="reset" th:value="Reset"/>
        <input type="submit" id="submitButton2" th:value="Download private key">
    </fieldset>
    <span th:text="*{info}" style="white-space: pre-line"></span>
</form>
</body>
</html>