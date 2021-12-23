package integration;


import java.sql.*;
import java.util.ArrayList;
import org.postgresql.util.PSQLException;

// Responsible for handling the communication to the database, 
// and executing queries to retrieve data and save data to the database.
public class DatabaseHandler {

    private Connection connection = null;
    private Statement statment = null;

    // Get the connection to the database if it exists.
    private Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/masterOfKnowledgeLibraryDB", "postgres", "postgres");
            connection.setAutoCommit(true);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Could not connect to the Database.");
        }

        return connection;
    }

    // Establish the connection to the database and prepare the tools needed to executed queries.

    /**
     *
     */
    public void establishConnection() {
        try {
            connection = getConnection();
            statment = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not communicate with the Database.");
        }
    }
    
    
}
