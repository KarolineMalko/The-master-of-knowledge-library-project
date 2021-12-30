package integration;


import beans.StockBook;
import beans.VisitorCurrentRental;
import beans.IncomingLecture;
import beans.LectureInfo;
import beans.OngoingLectureInfo;
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
                        "WHERE	available_copies >= 0 and \n" +
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
    
    public ArrayList<VisitorCurrentRental> importVisitorRentalsInfo(int visitorID) {
        
        String query = "SELECT 	isbn, title, author, language, rental_date, return_date\n" +
                        "FROM 	public.rental, public.book\n" +
                        "WHERE	visitor_id = " +  visitorID+" and \n" +
                        "	rental.book_isbn = book.isbn and\n" +
                        "	terminated = 0 ";
        try{
            
            ArrayList<String> isbns = getResultsFromQuery(query, 1);
            ArrayList<String> titles = getResultsFromQuery(query, 2);
            ArrayList<String> authors = getResultsFromQuery(query, 3);
            ArrayList<String> languages = getResultsFromQuery(query, 4);
            ArrayList<String> rentalDates = getResultsFromQuery(query, 5);
            ArrayList<String> returnDates = getResultsFromQuery(query, 6);
            
            return fillVisitorRentals(isbns, titles, authors, languages, rentalDates, returnDates);
            
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    
    public boolean updateRentalTerminationStatus(int bookIsbn, int visitorID) {
    
        String selectQuery =    "SELECT terminated\n" +
                                "FROM 	public.rental\n" +
                                "WHERE	book_isbn = " + bookIsbn;
        
        String updateQuery =    "UPDATE public.rental\n" +
                                "SET 	terminated = 1\n" +
                                "WHERE 	visitor_id = "+ visitorID+" and\n" +
                                "	book_isbn = " + bookIsbn;
        
        try {
           // int selectedRentalStatusBeforeReturn = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
            getResultsFromQuery(updateQuery, 1);
//            int selectedRentalStatusAfterReturn = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
//            if(selectedRentalStatusAfterReturn == 1) {
//                return true;
//            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    public boolean decreaseVisitorRentedBooks(int visitorID) {
    
        String selectQuery =    "SELECT rented_books_amount\n" +
                                "FROM 	public.visitor\n" +
                                "WHERE	id = " + visitorID ;
        
        String updateQuery =    "UPDATE public.visitor\n" +
                                "SET 	rented_books_amount = rented_books_amount - 1\n" +
                                "WHERE 	id = "+ visitorID;
        
        try {
            int selectedrentalsAmountBeforeReturn = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
            //Thread.sleep(1000);
            getResultsFromQuery(updateQuery, 1);
            //Thread.sleep(1000);
            int selectedrentalsAmountAfterReturn = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
            //System.out.println("selectedrentalsAmountBeforeReturn:" + selectedrentalsAmountBeforeReturn);
            //System.out.println("selectedrentalsAmountAfterReturn:" + selectedrentalsAmountAfterReturn);
            if(selectedrentalsAmountAfterReturn < selectedrentalsAmountBeforeReturn) {
                 return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateStockBookAvailableCopies(int bookIsbn) {
    
        String selectQuery =    "SELECT available_copies\n" +
                                "FROM 	public.stock\n" +
                                "WHERE	book_isbn = " + bookIsbn ;
        
        String updateQuery =    "UPDATE public.stock\n" +
                                "SET 	available_copies= available_copies + 1\n" +
                                "WHERE 	book_isbn ="+ bookIsbn;
        
        try {
            int updateStockAvailableCopiesBeforeUpdate = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
            Thread.sleep(200);
            getResultsFromQuery(updateQuery, 1);
            Thread.sleep(200);
            int updateStockAvailableCopiesAfterUpdate = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
            //System.out.println("updateStockAvailableCopiesBeforeUpdate:" + updateStockAvailableCopiesBeforeUpdate);
            //System.out.println("updateStockAvailableCopiesAfterUpdate:" + updateStockAvailableCopiesAfterUpdate);
            if(updateStockAvailableCopiesBeforeUpdate < updateStockAvailableCopiesAfterUpdate) {
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    



    public ArrayList<OngoingLectureInfo> importOngoingLectures(){
        String query =  "SELECT 	*\n" +
                        "FROM 	public.lecture\n" +
                        "WHERE	date < now()::timestamp and \n" +
                        "date + '2 hours' > now()::timestamp";
        try{ 
            ArrayList<String> lecturesNames = getResultsFromQuery(query, 2);
            ArrayList<String> lectursdate = getResultsFromQuery(query, 3);
            ArrayList<String> roomsId = getResultsFromQuery(query, 4);
            ArrayList<String> enrolledStudentNums = getResultsFromQuery(query, 6);
            
            
            ArrayList<OngoingLectureInfo> currentLectures = fillCurrentLecList(lecturesNames, lectursdate, roomsId, enrolledStudentNums);
            
            return currentLectures;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<OngoingLectureInfo>();
    }



    public ArrayList<OngoingLectureInfo> fillCurrentLecList(ArrayList<String> lecturesNames, ArrayList<String> lectursdate, ArrayList<String> roomsId, ArrayList<String> enrolledStudentNums) {
        
            ArrayList<OngoingLectureInfo> lecturesInfo = new  ArrayList<>();
            int lecturesNamesSize = lecturesNames.size();
            
            for(int i = 0; i < lecturesNamesSize; i++) {
                OngoingLectureInfo ongoinfLecture = new OngoingLectureInfo();
                ongoinfLecture.setLectureSubject(lecturesNames.get(i));
                ongoinfLecture.setLectureDate(lectursdate.get(i));
                ongoinfLecture.setRoomId(Integer.parseInt(roomsId.get(i)));
                ongoinfLecture.setEnrolledVisitorsNum(Integer.parseInt(enrolledStudentNums.get(i)));
                
                lecturesInfo.add(ongoinfLecture);
            }
            return lecturesInfo;
            
    }




     public boolean newLectureInsert(LectureInfo lectureInfo) {
        
        String insertQuery = "INSERT INTO public.lecture(\n" +
                       "    subject, date, room_id, lecturer_id, enrolled_visitors)\n" +
                       "   VALUES (\'" + lectureInfo.getLectureSubject() +"\', \n" +
                       "			\'" + lectureInfo.getLectureDate()+"\', \n" +
                       "			" + lectureInfo.getRoomId() +",  \n" +
                       "			(SELECT 	id\n" +
                       "			FROM 	public.lecturer\n" +
                       "			WHERE	personal_number = \'" + lectureInfo.getLecturerPersonNum() +"\'), \n" +
                       "			0)";
        
        //System.out.println(query);
        String selectQuery =   "SELECT COUNT (id)\n" +
                                    "FROM public.lecture\n";
        
        
        
        
        int lectureSizeBeforeAdding = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
        getResultsFromQuery(insertQuery, 1);
         int lectureSizeAfterAdding = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
        
        if(lectureSizeAfterAdding - lectureSizeBeforeAdding == 1){
            return true;
        }else{
            return false;
        }
    }




    public ArrayList<IncomingLecture> displayIncomingLec(){
        String query = "SELECT 	lecture.id, date, lecture.subject, lecturer.first_name, lecturer.last_name, room_id\n" +
                       "   FROM 	public.lecture, public.lecturer\n" +
                       "   WHERE	lecturer_id = lecturer.id and\n" +
                       "   date BETWEEN CURRENT_DATE and CURRENT_DATE+7";
        
        ArrayList<String> lectureIds = getResultsFromQuery(query, 1);
        ArrayList<String> lectureDates = getResultsFromQuery(query, 2);
        ArrayList<String> lectureSubjects = getResultsFromQuery(query, 3);
        ArrayList<String> lecturerFirstNames = getResultsFromQuery(query, 4);
        ArrayList<String> lecturerLastNames = getResultsFromQuery(query, 5);
        ArrayList<String> roomIds = getResultsFromQuery(query, 6);

         //System.out.println(arrayListToString(lectureSubject));
       
        return fillIncomingLecture(lectureIds, lectureDates, lectureSubjects, lecturerFirstNames, lecturerLastNames, roomIds);
    }  
    
    public int createEnrolledVisitorsInstanse(int lectureId, int visitorId){
        //return 0 when inserting instance into DB has failed
        // return 1 when the instance successfully inserted into the DB
        // return 2 when the visitor has already booked a place in that lecture
        
        String insertQuery =  "INSERT INTO public.enrolled_lecture_visitors(\n" +
                        "lecture_id, visitor_id)\n" +
                        "VALUES	(" + lectureId +", " + visitorId +")";
        String selectQuery =   "SELECT lecture_id\n" +
                               "FROM public.enrolled_lecture_visitors\n" + 
                               "WHERE lecture_id = " + lectureId + " and visitor_id = " + visitorId;
        try{
            if(getResultsFromQuery(selectQuery, 1).size() != 0) {
                return 2;
            }
            getResultsFromQuery(insertQuery, 1);
            if(getResultsFromQuery(selectQuery, 1).size() == 1){
                return 1;
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }        
        return 0;
    }
    
    
    public boolean increseEnrolledVisitors(int lectureId){
        String selectQuery = "SELECT    enrolled_visitors FROM public.lecture WHERE id = " + lectureId;
        String updateQuery = "UPDATE 	public.lecture\n" +
                       "SET 	enrolled_visitors = enrolled_visitors + 1\n" +
                       "WHERE id = " + lectureId;
        
        try {
            int enrolledVistorsBeforeUpdate = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
            getResultsFromQuery(updateQuery, 1);
            int enrolledVistorsAfterUpdate = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
            if(enrolledVistorsAfterUpdate > enrolledVistorsBeforeUpdate) {
                return true;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
        
        //createInstanse();
    }
    
    public boolean checkRoomCapacity(int lectureId){
        String query =  "SELECT	room_id\n" +
                        "FROM	public.lecture, public.room\n" +
                        "WHERE	lecture.room_id = room.id 	and\n" +
                        "lecture.id = " + lectureId +"	and \n" +
                        "room.maximum_number_of_visitors >= lecture.enrolled_visitors + 1";
   
        try{
            ArrayList<String> roomIds = getResultsFromQuery(query, 1);

            int roomId = Integer.parseInt(roomIds.get(0));
            return true;
            
        }catch(Exception e){
            System.out.println("You can NOT book this lecture!");
        }
        return false;
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

    private ArrayList<VisitorCurrentRental> fillVisitorRentals(ArrayList<String> isbns, ArrayList<String> titles, ArrayList<String> authors, ArrayList<String> languages, ArrayList<String> rentalDates, ArrayList<String> returnDates) {
        
        ArrayList<VisitorCurrentRental> visitorRentals = new ArrayList<>();
        int numberOfRentedBooks = isbns.size();
        for(int i = 0; i < numberOfRentedBooks; i++) {
           VisitorCurrentRental vCR = new VisitorCurrentRental();
           vCR.setIsbn(new BigInteger(isbns.get(i)));
           vCR.setTitle(titles.get(i));
           vCR.setAuthor(authors.get(i));
           vCR.setLanguage(languages.get(i));
           vCR.setRental_date(rentalDates.get(i));
           vCR.setReturn_date(returnDates.get(i));
           visitorRentals.add(vCR);
        }
        return visitorRentals;
        
    }

    private ArrayList<IncomingLecture> fillIncomingLecture(ArrayList<String> lectureIds, ArrayList<String> lectureDates, ArrayList<String> lectureSubjects, ArrayList<String> lecturerFirstNames, ArrayList<String> lecturerLastNames, ArrayList<String> roomIds) {
        ArrayList<IncomingLecture> incomingLectures = new  ArrayList<>();
        int incomingLecturesSize = lectureIds.size();
            
        for(int i = 0; i < incomingLecturesSize; i++) {
            IncomingLecture incomingLecture = new IncomingLecture();
            incomingLecture.setId(Integer.parseInt(lectureIds.get(i)));
            incomingLecture.setDate(lectureDates.get(i));
            incomingLecture.setSubject(lectureSubjects.get(i));
            incomingLecture.setLecturerFirstName(lecturerFirstNames.get(i));
            incomingLecture.setLecturerLastName(lecturerLastNames.get(i));
            incomingLecture.setRoomId(Integer.parseInt(roomIds.get(i)));
                
            incomingLectures.add(incomingLecture);
        }
        return incomingLectures;
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
           // System.out.print("No Data has been returned from teh update query");
        }
        return new ArrayList<>();
    }

}
