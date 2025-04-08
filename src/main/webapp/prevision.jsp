<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="main.java.com.helloword.dao.PrevisionService" %>
<%@ page import="main.java.com.helloword.dao.DepenseService" %>
<%@ page import="main.java.com.helloword.dao.EtatService" %>
<%@ page import="main.java.com.helloword.dao.Prevision" %>
<%@ page import="main.java.com.helloword.dao.Depense" %>
<%@ page import="main.java.com.helloword.dao.Etat" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestion de Budget</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        h2 { color: #555; }
        table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        form { margin-bottom: 20px; }
        input[type="text"], input[type="number"] { padding: 5px; margin-right: 10px; }
        input[type="submit"] { padding: 5px 10px; background-color: #4CAF50; color: white; border: none; cursor: pointer; }
        input[type="submit"]:hover { background-color: #45a049; }
    </style>
</head>
<body>
    <h1>Gestion de Budget</h1>

    <!-- Section Prévisions -->
    <h2>Gestion des Prévisions</h2>
    <form method="post" action="${pageContext.request.contextPath}/index.jsp">
        <input type="text" name="libelle" placeholder="Libellé" required>
        <input type="number" name="montant" placeholder="Montant" step="0.01" required>
        <input type="submit" name="addPrevision" value="Ajouter Prévision">
    </form>
    <%
        if ("POST".equalsIgnoreCase(request.getMethod()) && request.getParameter("addPrevision") != null) {
            String libelle = request.getParameter("libelle");
            double montant = Double.parseDouble(request.getParameter("montant"));
            PrevisionService previsionService = new PrevisionService();
            previsionService.addPrevision(libelle, montant);
        }
    %>
    <table>
        <tr>
            <th>ID</th>
            <th>Libellé</th>
            <th>Montant</th>
        </tr>
        <%
            PrevisionService previsionService = new PrevisionService();
            List<Prevision> previsions = previsionService.getAllPrevisions();
            request.setAttribute("previsions", previsions);
        %>
        <c:forEach var="prevision" items="${previsions}">
            <tr>
                <td>${prevision.id}</td>
                <td>${prevision.libelle}</td>
                <td>${prevision.montant}</td>
            </tr>
        </c:forEach>
    </table>

    <!-- Section Dépenses -->
    <h2>Gestion des Dépenses</h2>
    <form method="post" action="${pageContext.request.contextPath}/index.jsp">
        <select name="idPrevision" required>
            <option value="">Sélectionner une prévision</option>
            <c:forEach var="prevision" items="${previsions}">
                <option value="${prevision.id}">${prevision.libelle}</option>
            </c:forEach>
        </select>
        <input type="text" name="libelleDepense" placeholder="Libellé" required>
        <input type="number" name="montantDepense" placeholder="Montant" step="0.01" required>
        <input type="submit" name="addDepense" value="Ajouter Dépense">
    </form>
    <%
        if ("POST".equalsIgnoreCase(request.getMethod()) && request.getParameter("addDepense") != null) {
            int idPrevision = Integer.parseInt(request.getParameter("idPrevision"));
            String libelle = request.getParameter("libelleDepense");
            double montant = Double.parseDouble(request.getParameter("montantDepense"));
            DepenseService depenseService = new DepenseService();
            depenseService.ajouterDepense(idPrevision, libelle, montant, new java.util.Date());
        }
    %>
    <table>
        <tr>
            <th>ID</th>
            <th>Prévision</th>
            <th>Libellé</th>
            <th>Montant</th>
            <th>Date</th>
        </tr>
        <%
            DepenseService depenseService = new DepenseService();
            List<Depense> depenses = depenseService.getAllDepenses();
            request.setAttribute("depenses", depenses);
        %>
        <c:forEach var="depense" items="${depenses}">
            <tr>
                <td>${depense.id}</td>
                <td>${depense.idPrevision}</td>
                <td>${depense.libelle}</td>
                <td>${depense.montant}</td>
                <td>${depense.dateEnregistrement}</td>
            </tr>
        </c:forEach>
    </table>

    <!-- Section État du Budget -->
    <h2>État du Budget</h2>
    <table>
        <tr>
            <th>ID Prévision</th>
            <th>Montant Prévu</th>
            <th>Montant Dépensé</th>
            <th>Montant Restant</th>
            <th>Date Mise à Jour</th>
        </tr>
        <%
            EtatService etatService = new EtatService();
            List<Etat> etats = etatService.getAllEtats();
            request.setAttribute("etats", etats);
        %>
        <c:forEach var="etat" items="${etats}">
            <tr>
                <td>${etat.id_prevision}</td>
                <td>${etat.montant_prevu}</td>
                <td>${etat.montant_depense}</td>
                <td>${etat.montant_restant}</td>
                <td>${etat.date_mise_a_jour}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>