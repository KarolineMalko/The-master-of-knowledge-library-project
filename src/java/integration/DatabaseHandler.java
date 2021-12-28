package integration;


import beans.StockBook;
import beans.VisitorCurrentRental;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import org.postgresql.util.PSQLException;

// Responsible for handling the communication to the database, 
// and executing queries to retrieve data and save data to the database.
public class DatabaseHandler {

    private Connection connection = null;
    private Statement statment = null;
    public ArrayList<StockBook> stockBooks;
    public ArrayList<VisitorCurrentRental> visitorCurrentRentals;

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
    
    public ArrayList<StockBook> importStockBooksInfo() {
        
        String query = "SELECT	isbn, title, author, language, publish_date, available_copies\n" +
                        "FROM	public.stock, public.book\n" +
                        "WHERE	available_copies > 0 and \n" +
                        "		public.book.ISBN = public.stock.book_isbn\n" +
                        "ORDER BY title, author";
        try{
            
            ArrayList<String> isbns = getResultsFromQuery(query, 1);
            ArrayList<String> titles = getResultsFromQuery(query, 2);
            ArrayList<String> authors = getResultsFromQuery(query, 3);
            ArrayList<String> languages = getResultsFromQuery(query, 4);
            ArrayList<String> publishDates = getResultsFromQuery(query, 5);
            ArrayList<String> availableCopies = getResultsFromQuery(query, 6);
            
            return fillStockBooksInstance(isbns, titles, authors, languages, publishDates, availableCopies);
            
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    
    public int checkAvailableCopies(int bookIsbn) {
        String query = "SELECT 	available_copies\n" +
                        "FROM 	public.stock\n" +
                        "WHERE	book_isbn = " + bookIsbn + " and\n" +
                        "       available_copies - 1 >= 0";
        try {
            ArrayList<String> requestedBookInfo = getResultsFromQuery(query, 1);
            return Integer.parseInt(requestedBookInfo.get(0));
        }catch(Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    
    public int checkVisitorRentalBooksLimit(int visitorID) {
        String query = "SELECT 	rented_books_amount\n" +
                        "FROM 	public.visitor\n" +
                        "WHERE	id = " + visitorID +" and\n" +
                        "	rented_books_amount + 1 <= 2";
        
        try {
            ArrayList<String> requestedBookInfo = getResultsFromQuery(query, 1);
            return Integer.parseInt(requestedBookInfo.get(0));
        }catch(Exception e) {
            e.printStackTrace();
        }
        return -1;
        
    }
    
    
    public boolean decreaseBookAvailableCopies(int bookIsbn) {
        
        String selectQuery =    "SELECT available_copies\n" +
                                "FROM 	public.stock\n" +
                                "WHERE	book_isbn = " + bookIsbn;
        
        String updateQuery =  "UPDATE public.stock\n" +
                        "	SET available_copies= available_copies - 1\n" +
                        "	WHERE book_isbn = " + bookIsbn;
        
        try{
            
           int bookCopiesBeforeUpdate = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
           getResultsFromQuery(updateQuery, 1);
           int bookCopiesAfterUpdate = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
           if(bookCopiesAfterUpdate < bookCopiesBeforeUpdate) {
               return true;
           }
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    public boolean increaseVisitorRentedBooks(int visitorID) {
        String selectQuery =    "SELECT rented_books_amount\n" +
                                "FROM	public.visitor\n" +
                                "WHERE	id = " + visitorID;
        
        String updateQuery =    "UPDATE public.visitor\n" +
                                "SET 	rented_books_amount = rented_books_amount + 1\n" +
                                "WHERE 	id = " + visitorID;
        
        try{
            
           int visitorRentalsBeforeUpdate = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
           getResultsFromQuery(updateQuery, 1);
           int visitorRentalsAfterUpdate = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
           if(visitorRentalsBeforeUpdate < visitorRentalsAfterUpdate) {
               return true;
           }
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
        
    }
    
    
    public boolean createNewRentalInstance(int visitorID, int bookIsbn) {
        
        String selectQuery =    "SELECT COUNT(*)\n" +
                                "FROM 	public.rental";
        
        String insertQuery =    "INSERT INTO public.rental(rental_date, return_date, visitor_id, book_isbn, terminated)\n" +
                                "VALUES 		(CURRENT_DATE, CURRENT_DATE + interval \'2 months\', " + visitorID + ", " + bookIsbn + ", 0)";
        
        try{
            
           int totalRentalsBeforeInsert = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
           getResultsFromQuery(insertQuery, 1);
           int totalRentalsAfterInsert = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
           if(totalRentalsBeforeInsert < totalRentalsAfterInsert) {
               return true;
           }
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
        
    }
    
    public ArrayList<StockBook> importVisitorRentalsInfo(int visitorID) {
        
        String query = "SELECT 	isbn, title, author, language, rental_date, return_date\n" +
                        "FROM 	public.rental, public.book\n" +
                        "WHERE	visitor_id = 1 and \n" +
                        "	rental.book_isbn = book.isbn and\n" +
                        "	terminated = 0 ";
        try{
            
            ArrayList<String> isbns = getResultsFromQuery(query, 1);
            ArrayList<String> titles = getResultsFromQuery(query, 2);
            ArrayList<String> authors = getResultsFromQuery(query, 3);
            ArrayList<String> languages = getResultsFromQuery(query, 4);
            ArrayList<String> publishDates = getResultsFromQuery(query, 5);
            ArrayList<String> availableCopies = getResultsFromQuery(query, 6);
            
            return fillStockBooksInstance(isbns, titles, authors, languages, publishDates, availableCopies);
            
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public void getVisitorRentals() {
        
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
           
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private ArrayList<StockBook> fillStockBooksInstance(ArrayList<String> isbns, ArrayList<String> titles, ArrayList<String> authors, ArrayList<String> languages, ArrayList<String> publishDates, ArrayList<String> availableCopies) {
        ArrayList<StockBook> stockBooks = new ArrayList<>();
        int numberOfBooks = isbns.size();
        for(int i = 0; i < numberOfBooks; i++) {
           StockBook sb = new StockBook();
           sb.setIsbn(new BigInteger(isbns.get(i)));
           sb.setTitle(titles.get(i));
           sb.setAuthor(authors.get(i));
           sb.setLanguage(languages.get(i));
           sb.setPublishDate(publishDates.get(i));
           sb.setAvailableCopies(Integer.parseInt(availableCopies.get(i)));
           stockBooks.add(sb);
        }
        return stockBooks;
        
    }

//    private ArrayList<VisitorCurrentRental> fillVisitorRentlsArrayList()
//                visitorCurrentRentals = new ArrayList<>();
//    
//   

  
}
