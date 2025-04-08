package main.java.com.helloWorld;

import main.java.com.helloWorld.dao.DepenseService;
import main.java.com.helloWorld.dao.Depense;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/depenses")
public class DepenseServlet extends HttpServlet {
    private DepenseService depenseService;

    @Override
    public void init() throws ServletException {
        super.init();
        depenseService = new DepenseService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            if (action == null) {
                // Liste toutes les dépenses
                Depense[] depenses = depenseService.toutesLesDepenses();
                request.setAttribute("depenses", depenses);
                request.getRequestDispatcher("/WEB-INF/views/depenses.jsp").forward(request, response);
            } else if ("get".equals(action)) {
                // Récupère une dépense spécifique
                int id = Integer.parseInt(request.getParameter("id"));
                Depense depense = depenseService.getDepense(id);
                request.setAttribute("depense", depense);
                request.getRequestDispatcher("/WEB-INF/views/depenseDetail.jsp").forward(request, response);
            } else if ("byPrevision".equals(action)) {
                // Liste les dépenses par prévision
                int idPrevision = Integer.parseInt(request.getParameter("idPrevision"));
                Depense[] depenses = depenseService.depensesParPrevision(idPrevision);
                request.setAttribute("depenses", depenses);
                request.getRequestDispatcher("/WEB-INF/views/depenses.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Erreur lors de la récupération des données", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            if ("create".equals(action)) {
                int idPrevision = Integer.parseInt(request.getParameter("idPrevision"));
                String libelle = request.getParameter("libelle");
                double montant = Double.parseDouble(request.getParameter("montant"));
                depenseService.creerDepense(idPrevision, libelle, montant);
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                int idPrevision = Integer.parseInt(request.getParameter("idPrevision"));
                String libelle = request.getParameter("libelle");
                double montant = Double.parseDouble(request.getParameter("montant"));
                String dateStr = request.getParameter("dateEnregistrement");
                Date dateEnregistrement = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                depenseService.modifierDepense(id, idPrevision, libelle, montant, dateEnregistrement);
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                depenseService.supprimerDepense(id);
            }
            response.sendRedirect(request.getContextPath() + "/depenses");
        } catch (SQLException | ParseException e) {
            throw new ServletException("Erreur lors du traitement de la requête", e);
        }
    }
}