package main.java.com.helloWorld.dao;

import  main.java.com.helloWorld.dao.Depense;
import  main.java.com.helloWorld.dao.GenericDaoSql;
import main.java.com.helloWorld.connection.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class DepenseService {
    private final GenericDaoSql<Depense> depenseDao;
    
    public DepenseService() {
        this.depenseDao = new GenericDaoSql<>(Depense.class);
    }
    
    public void creerDepense(int idPrevision, String libelle, double montant) throws SQLException {
        Depense depense = new Depense(idPrevision, libelle, montant, new Date());
        depenseDao.save(depense);
    }
    
    public Depense[] toutesLesDepenses() throws SQLException {
        return depenseDao.findAll();
    }
    
    public Depense[] depensesParPrevision(int idPrevision) throws SQLException {
        String query = "SELECT * FROM depense WHERE id_prevision = " + idPrevision;
        return depenseDao.createInstanceFromQuery(query);
    }
    
    public Depense getDepense(int id) throws SQLException {
        Depense[] result = depenseDao.findById(id);
        if (result.length > 0) {
            return result[0];
        }
        return null;
    }
    
    public void modifierDepense(int id, int idPrevision, String libelle, double montant, Date dateEnregistrement) throws SQLException {
        Depense depense = getDepense(id);
        if (depense != null) {
            depense.setId_prevision(idPrevision);
            depense.setLibelle(libelle);
            depense.setMontant(montant);
            depense.setDate_enregistrement(dateEnregistrement);
            depenseDao.update(depense);
        }
    }
    
    public void supprimerDepense(int id) throws SQLException {
        depenseDao.delete(id);
    }
    
    // Transaction support
    public void creerDepenseDansTransaction(Connection conn, int idPrevision, String libelle, double montant) throws SQLException {
        Depense depense = new Depense(idPrevision, libelle, montant, new Date());
        depenseDao.save(conn, depense);
    }
}