package main.java.com.helloWorld.dao;

import main.java.com.helloWorld.connection.Dbconnect;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EtatService {
    private final GenericDaoSql<Etat> etatDao;

    public EtatService() {
        this.etatDao = new GenericDaoSql<>(Etat.class);
    }

    public EtatService(Connection conn) {
        this.etatDao = new GenericDaoSql<>(Etat.class);
    }

    public List<Etat> getAllEtats() throws SQLException {
        List<Etat> etats = new ArrayList<>();
        String query = "SELECT id_prevision, montant_prevu, montant_depense, montant_restant " +
                      "FROM etat_previsions";

        try (Connection conn = Dbconnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Etat etat = new Etat();
                etat.setId_prevision(rs.getInt("id_prevision"));
                etat.setMontant_prevu(rs.getDouble("montant_prevu"));
                etat.setMontant_depense(rs.getDouble("montant_depense"));
                etat.setMontant_restant(rs.getDouble("montant_restant"));
                etat.setDate_mise_a_jour(new Date());
                etats.add(etat);
            }
        }
        return etats;
    }

    public Etat getEtatParPrevision(int idPrevision) throws SQLException {
        String query = "SELECT id_prevision, montant_prevu, montant_depense, montant_restant " +
                      "FROM etat_previsions WHERE id_prevision = ?";

        try (Connection conn = Dbconnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idPrevision);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Etat etat = new Etat();
                    etat.setId_prevision(rs.getInt("id_prevision"));
                    etat.setMontant_prevu(rs.getDouble("montant_prevu"));
                    etat.setMontant_depense(rs.getDouble("montant_depense"));
                    etat.setMontant_restant(rs.getDouble("montant_restant"));
                    etat.setDate_mise_a_jour(new Date());
                    return etat;
                }
            }
        }
        return null;
    }

    public void mettreAJourEtat(int idPrevision) throws SQLException {
        Etat etat = getEtatParPrevision(idPrevision);
        if (etat != null) {
            // Solution temporaire : utiliser une requête directe si findByField n'existe pas
            String checkQuery = "SELECT id FROM etat WHERE id_prevision = ?";
            try (Connection conn = Dbconnect.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(checkQuery)) {
                stmt.setInt(1, idPrevision);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        etat.setId(rs.getInt("id"));
                        etatDao.update(etat);
                    } else {
                        etatDao.save(etat);
                    }
                }
            }
        }
    }

    public Etat getEtat(int id) throws SQLException {
        Etat[] result = etatDao.findById(id);
        return result.length > 0 ? result[0] : null;
    }

    public void supprimerEtat(int id) throws SQLException {
        etatDao.delete(id);
    }
}