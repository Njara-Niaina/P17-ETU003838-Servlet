package main.java.com.helloWorld;

import main.java.com.helloWorld.connection.Dbconnect;
import main.java.com.helloWorld.dao.PrevisionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class PrevisionServlet extends HttpServlet {
    private PrevisionService previsionService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = Dbconnect.getConnection();
            previsionService = new PrevisionService(conn);
        } catch (SQLException e) {
            throw new ServletException("Erreur lors de l'initialisation de la connexion", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("previsions", previsionService.getAllPrevisions());
            request.getRequestDispatcher("/WEB-INF/views/previsions.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erreur lors de la récupération des prévisions", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String libelle = request.getParameter("libelle");
        double montant = Double.parseDouble(request.getParameter("montant"));
        try {
            previsionService.addPrevision(libelle, montant);
            response.sendRedirect(request.getContextPath() + "/prevision"); // Corrigé : "/prevision" au lieu de "/previsions"
        } catch (SQLException e) {
            throw new ServletException("Erreur lors de l'ajout de la prévision", e);
        }
    }
}