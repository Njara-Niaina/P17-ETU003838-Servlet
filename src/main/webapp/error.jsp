<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Erreur</title>
</head>
<body>
    <h1>Une erreur s'est produite</h1>
    <p>Message: <%= exception.getMessage() %></p>
    <a href="index.jsp">Retour à l'accueil</a>
</body>
</html>