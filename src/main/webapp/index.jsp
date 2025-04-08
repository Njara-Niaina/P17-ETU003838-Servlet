<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Gestion de Budget</title>
</head>
<body>
    <h1>Bienvenue dans le Gestion de Budget</h1>
    <ul>
        <li><a href="PrevisionServlet">Gestion des Prévisions</a></li>
        <li><a href="${pageContext.request.contextPath}/depense">Gestion des Dépenses</a></li>
        <li><a href="${pageContext.request.contextPath}/etat">État du Budget</a></li>
    </ul>
</body>
</html>