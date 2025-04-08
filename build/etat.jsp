<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>État du Budget</title>
</head>
<body>
    <h1>État du Budget</h1>
    
    <table border="1">
        <tr>
            <th>ID Prévision</th>
            <th>Libellé</th>
            <th>Montant Prévu</th>
            <th>Montant Dépensé</th>
            <th>Montant Restant</th>
            <th>Dernière Mise à Jour</th>
        </tr>
        <c:forEach items="${etats}" var="etat">
            <tr>
                <td>${etat.idPrevision}</td>
                <td>${etat.libelle}</td>
                <td>${etat.montantPrevu}</td>
                <td>${etat.montantDepense}</td>
                <td>${etat.montantRestant}</td>
                <td>${etat.dateMiseAJour}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>