package main.java.com.helloWorld.dao;

import main.java.com.helloWorld.dao.*;

import java.sql.SQLException;

public class EtatPrevisionService {
    private final GenericDaoSql<EtatPrevision> etatPrevisionDao;
    
    public EtatPrevisionService() {
        this.etatPrevisionDao = new GenericDaoSql<>(EtatPrevision.class);
    }
    
    public EtatPrevision[] tousLesEtats() throws SQLException {
        // La vue SQL "etat_previsions" correspond à "etatprevision" en Java
        String query = "SELECT * FROM etat_previsions";
        return etatPrevisionDao.createInstanceFromQuery(query);
    }
    
    public EtatPrevision getEtatPrevision(int idPrevision) throws SQLException {
        String query = "SELECT * FROM etat_previsions WHERE id_prevision = " + idPrevision;
        EtatPrevision[] result = etatPrevisionDao.createInstanceFromQuery(query);
        if (result.length > 0) {
            return result[0];
        }
        return null;
    }
}