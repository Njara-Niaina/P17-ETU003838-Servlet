package main.java.com.helloWorld;

import main.java.com.helloWorld.connection.Dbconnect;
import main.java.com.helloWorld.dao.EtatService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/etats")
public class EtatServlet extends HttpServlet {
    private EtatService etatService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = Dbconnect.getConnection(); // Gestion de l'exception
            etatService = new EtatService(conn);
        } catch (SQLException e) {
            throw new ServletException("Erreur lors de l'initialisation de la connexion", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("etats", etatService.getAllEtats()); // Gestion de l'exception
            request.getRequestDispatcher("/WEB-INF/views/etats.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erreur lors de la récupération des états", e);
        }
    }
}