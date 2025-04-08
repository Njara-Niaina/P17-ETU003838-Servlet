<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestion des Dépenses</title>
</head>
<body>
    <h1>Gestion des Dépenses</h1>
    
    <form action="depense" method="post">
        <label>ID Prévision:</label>
        <input type="number" name="id_prevision" required><br>
        <label>Libellé:</label>
        <input type="text" name="libelle" required><br>
        <label>Montant:</label>
        <input type="number" step="0.01" name="montant" required><br>
        <input type="submit" value="Ajouter Dépense">
    </form>

    <h2>Liste des Dépenses</h2>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>ID Prévision</th>
            <th>Libellé</th>
            <th>Montant</th>
            <th>Date</th>
        </tr>
        <c:forEach items="${depenses}" var="depense">
            <tr>
                <td>${depense.idDepense}</td>
                <td>${depense.idPrevision}</td>
                <td>${depense.libelle}</td>
                <td>${depense.montant}</td>
                <td>${depense.dateEnregistrement}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>