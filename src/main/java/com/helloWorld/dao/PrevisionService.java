package main.java.com.helloWorld.dao;

import main.java.com.helloWorld.connection.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrevisionService {
    private final GenericDaoSql<Prevision> previsionDao;

    public PrevisionService() {
        this.previsionDao = new GenericDaoSql<>(Prevision.class);
    }

    public PrevisionService(Connection conn) {
        this.previsionDao = new GenericDaoSql<>(Prevision.class);
    }

    public List<Prevision> getAllPrevisions() throws SQLException {
        List<Prevision> previsions = new ArrayList<>();
        String query = "SELECT * FROM previsions";
        try (Connection conn = Dbconnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Prevision prevision = new Prevision();
                prevision.setId(rs.getInt("id_prevision"));
                prevision.setLibelle(rs.getString("libelle"));
                prevision.setMontant(rs.getDouble("montant"));
                previsions.add(prevision);
            }
        }
        return previsions;
    }

    public void addPrevision(String libelle, double montant) throws SQLException {
        Prevision prevision = new Prevision();
        prevision.setLibelle(libelle);
        prevision.setMontant(montant);
        previsionDao.save(prevision);
    }

    // Nouvelle méthode pour créer une prévision dans une transaction
    public void creerPrevisionDansTransaction(Connection conn, String libelle, double montant) throws SQLException {
        Prevision prevision = new Prevision();
        prevision.setLibelle(libelle);
        prevision.setMontant(montant);
        previsionDao.save(conn, prevision); // Suppose que GenericDaoSql supporte une version avec Connection
    }
}