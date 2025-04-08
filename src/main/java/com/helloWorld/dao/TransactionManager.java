package main.java.com.helloWorld.dao;

import main.java.com.helloWorld.connection.Dbconnect;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    private Connection connection;
    private final PrevisionService previsionService;

    public TransactionManager() throws SQLException {
        this.connection = Dbconnect.getConnection();
        this.previsionService = new PrevisionService(connection);
    }

    public Connection getConnection() {
        return connection;
    }

    public void exampleTransaction() throws SQLException {
        try {
            connection.setAutoCommit(false);
            previsionService.creerPrevisionDansTransaction(connection, "Nouvelle prévision", 1000.0);
            // Ajoutez d'autres opérations ici si nécessaire
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}