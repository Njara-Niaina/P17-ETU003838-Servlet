package main.java.com.helloWorld.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Dbconnect {
    // Paramètres de connexion à la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/db_s2_ETU003838";
    private static final String USER = "ETU003838"; 
    private static final String PASSWORD = "YYxvTB9p"; 

    // Charger le pilote JDBC au démarrage
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Impossible de charger le pilote JDBC MySQL", e);
        }
    }

    // Méthode pour obtenir une connexion
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Méthode pour fermer une connexion (optionnelle)
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
}
