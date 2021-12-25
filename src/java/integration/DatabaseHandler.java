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
    
    /**
     *
     * @param username
     * @param password
     * @return
     */
    public String importUserID(String username, String password) {

        String query =  "SELECT 	visitor_id\n" +
                        "FROM 	public.visitor_loginInfo\n" +
                        "WHERE	visitor_email=\'" + username + "\' and " +
                        "       visitor_password = md5(\'" + password + "\')";
        
        try {
 
           ArrayList<String> resultList = getResultsFromQuery(query, 1);

           return arrayListToString(resultList);
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    
    public String importAdminID(String username, String password) {

        String query =  "SELECT adminstrator_id\n" +
                        "FROM 	public.admin_logininfo\n" +
                        "WHERE	admin_email =\'" + username + "\' and " +
                        "       admin_password = md5(\'" + password + "\')";
        
        try {
 
           ArrayList<String> resultList = getResultsFromQuery(query, 1);

           return arrayListToString(resultList);
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    
    
    
    /**
     *
     * @param result
     * @return
     */
    public String arrayListToString(ArrayList<String> result) {
        StringBuilder sb = new StringBuilder();
        for(String s : result){
            sb.append(s);

        }
        String str = sb.toString();
        return str;
    }
    
    /**
     *
     * @param query
     * @return
     */
    public ArrayList<String> getResultRowFromQuery(String query) {
        try {
            ResultSet executionResult = this.statment.executeQuery(query);
            
                ArrayList<String> resultsArray = new ArrayList<>();
                
                while(executionResult.next() == true) {
                    
                }

                return resultsArray;
            
           
        }catch(PSQLException pslExc) {

        } 
        catch (SQLException e) {
        }
        return new ArrayList<>();
    
    }
    
    /**
     *
     * @param query
     * @param columNum
     * @return
     */
    public ArrayList<String> getResultsFromQuery(String query, int columNum){
        try {
            ResultSet executionResult = this.statment.executeQuery(query);
            
                ArrayList<String> resultsArray = new ArrayList<>();

                while(executionResult.next() == true) {
                    resultsArray.add( executionResult.getString(columNum));
                }

                return resultsArray;
            
           
        }catch(PSQLException pslExc) {

        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }
}
