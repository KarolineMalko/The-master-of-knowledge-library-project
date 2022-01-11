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
    private Connection getConnection() throws Exception {
        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/masterOfKnowledgeLibraryDB", "postgres", "postgres");
            connection.setAutoCommit(true);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Could not connect to the Database.");
        }

        return connection;
    }

    // Establish the connection to the database and prepare the tools needed to executed queries.

    /**
     * establish a connection with the database
     * @throws java.lang.Exception
     */
    public void establishConnection() throws Exception {
        try {
            connection = getConnection();
            statment = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Could not communicate with the Database.");
        }
    }
    
    /**
     *  
     * @param username the entered username by visitor
     * @param password the entered password by visitor
     * @return visitorID as a string 
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
            return "";
        }
    }
    
    /**
     *
     * @param username the entered username by admin
     * @param password the entered password by admin
     * @return adminID as a string
     */
    public String importAdminID(String username, String password) {

        String query =  "SELECT adminstrator_id\n" +
                        "FROM 	public.admin_logininfo\n" +
                        "WHERE	admin_email =\'" + username + "\' and " +
                        "       admin_password = md5(\'" + password + "\')";
        
        try {
 
           ArrayList<String> resultList = getResultsFromQuery(query, 1);

           return arrayListToString(resultList);
           
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     *
     * @return ArrayList of StockBooks and their info
     */
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
            return new ArrayList<>();
        }
    }
    
    /**
     *  to check the available copies with the specified are more than 0 
     * @param bookIsbn the bookIsbn the visitor wants to rent
     * @return the available copies of the specified book
     */
    public int checkAvailableCopies(int bookIsbn) {
        String query = "SELECT 	available_copies\n" +
                        "FROM 	public.stock\n" +
                        "WHERE	book_isbn = " + bookIsbn + " and\n" +
                        "       available_copies >  0";
        try {
            ArrayList<String> requestedBookInfo = getResultsFromQuery(query, 1);
            return Integer.parseInt(requestedBookInfo.get(0));
        }catch(Exception e) {
            return -1;
        }
    }
    
    /**
     * check if the visitor is eligible for rental or not
     * @param visitorID loggedIn visitor ID
     * @return  return the visitor's current rentals books amount
     */
    public int checkVisitorRentalBooksLimit(int visitorID) {
        String query = "SELECT 	rented_books_amount\n" +
                        "FROM 	public.visitor\n" +
                        "WHERE	id = " + visitorID +" and\n" +
                        "	rented_books_amount  < 2";
        
        try {
            ArrayList<String> requestedBookInfo = getResultsFromQuery(query, 1);
            return Integer.parseInt(requestedBookInfo.get(0));
        }catch(Exception e) {
            return -1;
        }
    }
    
    /**
     *  decrease the available copies of that book in the stock
     * @param bookIsbn  the selected bookIsbn
     * @return if the book copies has been updated or not
     */
    public boolean decreaseBookAvailableCopies(int bookIsbn) {
        
        String selectQuery =    "SELECT available_copies\n" +
                                "FROM 	public.stock\n" +
                                "WHERE	book_isbn = " + bookIsbn;
        
        String updateQuery =  "UPDATE public.stock\n" +
                        "	SET available_copies= available_copies - 1\n" +
                        "	WHERE book_isbn = " + bookIsbn;
        
        try{
            
           int bookCopiesBeforeUpdate = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
           updateInsertQuery(updateQuery);
           int bookCopiesAfterUpdate = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
           return bookCopiesAfterUpdate < bookCopiesBeforeUpdate;
        }catch(Exception e) {
            return false;
        }
    }
    
    /**
     *  increase visitor's current rentals
     * @param visitorID the loggedIn user
     * @return if the visitor's current rentals has been updated of not.
     */
    public boolean increaseVisitorRentedBooks(int visitorID) {
        String selectQuery =    "SELECT rented_books_amount\n" +
                                "FROM	public.visitor\n" +
                                "WHERE	id = " + visitorID;
        
        String updateQuery =    "UPDATE public.visitor\n" +
                                "SET 	rented_books_amount = rented_books_amount + 1\n" +
                                "WHERE 	id = " + visitorID;
        
        try{
            
           int visitorRentalsBeforeUpdate = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
           updateInsertQuery(updateQuery);
           int visitorRentalsAfterUpdate = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
           return visitorRentalsBeforeUpdate < visitorRentalsAfterUpdate;
        }catch(Exception e) {
            return false;
        }
    }
    
    /**
     *  creates a new rental instance with userID and bookIsbn
     * @param visitorID loggedIn visitor's ID
     * @param bookIsbn selected BookIsbn
     * @return check if the instance has been created in the db or not 
     */
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
            return false;
        }catch(Exception e) {
            return false;
        }
    }
    
    /**
     *  import visitor's rentals from db
     * @param visitorID loggedIn visitorId
     * @return return an arrayList of VisitorCurrentRental filled with info
     */
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
            return new ArrayList<>();
        }
    }

    /**
     *  update the user's bookIsbn current rental 
     * @param bookIsbn the selected bookIsbn
     * @param visitorID the loggedIn visitorId
     * @return if the rentalInstance termination status has been updated or not
     */
    public boolean updateRentalTerminationStatus(int bookIsbn, int visitorID) {
    
        String updateQuery =    "UPDATE public.rental\n" +
                                "SET 	terminated = 1\n" +
                                "WHERE 	visitor_id = "+ visitorID+" and\n" +
                                "	book_isbn = " + bookIsbn;
        
        try {
            updateInsertQuery(updateQuery);
            return true;

        }catch (Exception e) {
            return false;
        }
    }
    
    /**
     *  decrease the visitor's rented books
     * @param visitorID loggedIn visitorID
     * @return return if the instance termination status has been updated or not
     * @throws Exception
     */
    public boolean decreaseVisitorRentedBooks(int visitorID) throws Exception {
    
        String selectQuery =    "SELECT rented_books_amount\n" +
                                "FROM 	public.visitor\n" +
                                "WHERE	id = " + visitorID ;
        
        String updateQuery =    "UPDATE public.visitor\n" +
                                "SET 	rented_books_amount = rented_books_amount - 1\n" +
                                "WHERE 	id = "+ visitorID;
        
        try {
            int selectedrentalsAmountBeforeReturn = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
            updateInsertQuery(updateQuery);
            int selectedrentalsAmountAfterReturn = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
            if(selectedrentalsAmountAfterReturn < selectedrentalsAmountBeforeReturn) {
                 return true;
            }
            return false;
        }catch (Exception e) {
            return false;
        }
    }
    
    /**
     *
     * @param bookIsbn the selected bookIsbn
     * @return return if the selected book available copies has been increased or not
     * @throws Exception
     */
    public boolean updateStockBookAvailableCopies(int bookIsbn) throws Exception {
    
        String selectQuery =    "SELECT available_copies\n" +
                                "FROM 	public.stock\n" +
                                "WHERE	book_isbn = " + bookIsbn ;
        
        String updateQuery =    "UPDATE public.stock\n" +
                                "SET 	available_copies= available_copies + 1\n" +
                                "WHERE 	book_isbn ="+ bookIsbn;
        
        try {
            int updateStockAvailableCopiesBeforeUpdate = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
            updateInsertQuery(updateQuery);
            int updateStockAvailableCopiesAfterUpdate = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
            if(updateStockAvailableCopiesBeforeUpdate < updateStockAvailableCopiesAfterUpdate) {
                return true;
            }
            return false;
        }catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * @return an arrayList of OngoingLectureInfo if there is any ongoing lectures.
     * @throws Exception
     */
    public ArrayList<OngoingLectureInfo> importOngoingLectures() throws Exception{
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
            return new ArrayList<>();
        }
        
    }

    /**
     *  fill an arrayList of OngoingLectureInfo to show for administrator
     * @param lecturesNames the ongoing lecture's Name by administrator
     * @param lectursdate the ongoing lecture's start date by administrator
     * @param roomsId the ongoing lecture's roomId by administrator, where lecture will have a place.
     * @param enrolledStudentNums the ongoing lecture's Name by administrator
     * @return return the arrayList of OngoingLectureInfo list
     */
    public ArrayList<OngoingLectureInfo> fillCurrentLecList(ArrayList<String> lecturesNames, ArrayList<String> lectursdate, ArrayList<String> roomsId, ArrayList<String> enrolledStudentNums) {
        ArrayList<OngoingLectureInfo> lecturesInfo = new  ArrayList<>();
        try {
            
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
        }catch(NumberFormatException e) {
            return lecturesInfo;
        }
            
            
    }
    
    /**
     * create a new lecture instance by Admin
     * @param lectureName the entered Lecture's Name by administrator
     * @param lectureDate the entered Lecture's start date by administrator
     * @param roomId the entered Lecture's roomId by administrator, where lecture will have a place.
     * @param lecturerPerNum the entered Lecture's Name by administrator
     * @return return a new instance lectureInfo that has been created by Admin
     * @throws Exception
     */
    public LectureInfo createLectureInfoInstance(String lectureName, String lectureDate, int roomId, String lecturerPerNum) throws Exception {
            try {
                LectureInfo lectureInfo = new LectureInfo();
                lectureInfo.setLectureSubject(lectureName);
                lectureInfo.setLectureDate(lectureDate);
                lectureInfo.setRoomId(roomId);
                lectureInfo.setLecturerPersonNum(lecturerPerNum);
                return lectureInfo;
            }catch(Exception e) {
                throw new Exception();
            }
    }

    /**
     * insert the new lecture instance to the db
     * @param lectureInfo lectureInfo instance
     * @return return if the new lecture has been inserted or not 
     * @throws Exception
     */
    public boolean newLectureInsert(LectureInfo lectureInfo) throws Exception {
        
        String insertQuery = "INSERT INTO public.lecture(\n" +
                       "    subject, date, room_id, lecturer_id, enrolled_visitors)\n" +
                       "   VALUES (\'" + lectureInfo.getLectureSubject() +"\', \n" +
                       "			\'" + lectureInfo.getLectureDate()+"\', \n" +
                       "			" + lectureInfo.getRoomId() +",  \n" +
                       "			(SELECT 	id\n" +
                       "			FROM 	public.lecturer\n" +
                       "			WHERE	personal_number = \'" + lectureInfo.getLecturerPersonNum() +"\'), \n" +
                       "			0)";
        

        String selectQuery =   "SELECT COUNT (id)\n" +
                                    "FROM public.lecture\n";
        
        
        try {
            int lectureSizeBeforeAdding = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
            updateInsertQuery(insertQuery);
            int lectureSizeAfterAdding = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));

                return lectureSizeAfterAdding - lectureSizeBeforeAdding == 1;
        }catch(Exception e) {
            return false;
        }
    }

    /**
     *  display the week's incoming lectures to visitor
     * @return  return an arralyList of IncomingLcetures to display later for the 
     * @throws Exception
     */
    public ArrayList<IncomingLecture> displayIncomingLec() throws Exception{
        String query = "SELECT 	lecture.id, date, lecture.subject, lecturer.first_name, lecturer.last_name, room_id\n" +
                       "   FROM 	public.lecture, public.lecturer\n" +
                       "   WHERE	lecturer_id = lecturer.id and\n" +
                       "   date BETWEEN CURRENT_DATE and CURRENT_DATE+7";
        
        try {
            ArrayList<String> lectureIds = getResultsFromQuery(query, 1);
            ArrayList<String> lectureDates = getResultsFromQuery(query, 2);
            ArrayList<String> lectureSubjects = getResultsFromQuery(query, 3);
            ArrayList<String> lecturerFirstNames = getResultsFromQuery(query, 4);
            ArrayList<String> lecturerLastNames = getResultsFromQuery(query, 5);
            ArrayList<String> roomIds = getResultsFromQuery(query, 6);

            return fillIncomingLecture(lectureIds, lectureDates, lectureSubjects, lecturerFirstNames, lecturerLastNames, roomIds);
        }catch(Exception e) {
            throw new Exception();
        }
        
    }  
    
    /**
     * create an instance in the table enrolled_lecture_visitors which stores visitorID and lectureID
     * @param lectureId the selected lecture's ID 
     * @param visitorId the loggedIn visitorID
     * @return a number for if the insert has failed, if the instacne has been inserted successfully or if the visitor has already booked a place in that lecture.
     */
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
            if(!getResultsFromQuery(selectQuery, 1).isEmpty()) {
                return 2;
            }
            getResultsFromQuery(insertQuery, 1);
            if(getResultsFromQuery(selectQuery, 1).size() == 1){
                return 1;
            }
            return 0;
        }catch(Exception e){
            return 0;
        }    
    }
    
    /**
     * update lecture's enrolled visitor to make sure to not exceeded the limit.
     * @param lectureId the selected lectureID
     * @return check if the enrolled visitors has increased or not
     * @throws Exception
     */
    public boolean increseEnrolledVisitors(int lectureId) throws Exception{
        String selectQuery = "SELECT    enrolled_visitors FROM public.lecture WHERE id = " + lectureId;
        String updateQuery = "UPDATE 	public.lecture\n" +
                       "SET 	enrolled_visitors = enrolled_visitors + 1\n" +
                       "WHERE id = " + lectureId;
        
        try {
            int enrolledVistorsBeforeUpdate = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
            updateInsertQuery(updateQuery);
            int enrolledVistorsAfterUpdate = Integer.parseInt(getResultsFromQuery(selectQuery, 1).get(0));
            if(enrolledVistorsAfterUpdate > enrolledVistorsBeforeUpdate) {
                return true;
            }
            return false;
        }catch(Exception e) {
            return false;
        }
    }
    
    /**
     *  check the room capacity for the specified lectureId
     * @param lectureId the selected LectureID
     * @return return if the roomId could be converted into integer successfully, if not it returns false,
     */
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
            return false;
        }
    }



    //fill stock books instances  according to passed isbns, titles, authors, languages, publishDates, availableCopies arrasLists, and reutrn arrayList of the stockbooks
    private ArrayList<StockBook> fillStockBooksInstance(ArrayList<String> isbns, ArrayList<String> titles, ArrayList<String> authors, ArrayList<String> languages, ArrayList<String> publishDates, ArrayList<String> availableCopies) {
        ArrayList<StockBook> stockBooks = new ArrayList<>();
        try {
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
        }catch(Exception e) {
            return stockBooks;
        }
        
        
    }

    //fill visitor's current Rentals isbns, titles, authors, languages, languages, rentalDates, returnDates and return ArrayList of visitorCurrentRentals
    private ArrayList<VisitorCurrentRental> fillVisitorRentals(ArrayList<String> isbns, ArrayList<String> titles, ArrayList<String> authors, ArrayList<String> languages, ArrayList<String> rentalDates, ArrayList<String> returnDates) throws Exception {
        
        ArrayList<VisitorCurrentRental> visitorRentals = new ArrayList<>();
        try {
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
        }catch(Exception e) {
            throw new Exception();
        }
        
        
    }

    // fill the arrayList of IncomingLectures according to lectureIds, lectureDates, lectureSubjects, lecturerFirstNames, lecturerLastNames, roomIds and return it.
    private ArrayList<IncomingLecture> fillIncomingLecture(ArrayList<String> lectureIds, ArrayList<String> lectureDates, ArrayList<String> lectureSubjects, ArrayList<String> lecturerFirstNames, ArrayList<String> lecturerLastNames, ArrayList<String> roomIds) throws Exception {
        ArrayList<IncomingLecture> incomingLectures = new  ArrayList<>();
        try {
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
        }catch(Exception e) {
            return incomingLectures;
        }
        
    }

    
    /**
     * convert arrayList into String
     * @param result the converted results arrayList<String>
     * @return  the result as String
     */
    public String arrayListToString(ArrayList<String> result) {
        try {
            StringBuilder sb = new StringBuilder();
            for(String s : result){
                sb.append(s);

            }
            String str = sb.toString();
            return str;
        }catch(Exception e) {
            return "";
        }
        
    }
    
    
    /**
     *  execute the "query" in the database and get its result as a ArrayList<String>
     * @param query the passed query which will be executed in the db
     * @param columNum  the column number of the returned data of execution
     * @return return the column of the executed db
     * @throws java.lang.Exception
     */
    public ArrayList<String> getResultsFromQuery(String query, int columNum) throws Exception{
        ArrayList<String> resultsArray = new ArrayList<>();
        try {
            ResultSet executionResult = this.statment.executeQuery(query);
 
                while(executionResult.next() == true) {
                    resultsArray.add( executionResult.getString(columNum));
                }
                return resultsArray;
           
        }catch (Exception e) {
           return resultsArray;
        }
        
    }
    
    /**
     * the function is used only in update, insert queries where there is no returned data from the db
     * @param query the update, insert passed query
     * @throws Exception 
     */
    public void updateInsertQuery(String query) throws Exception{
        try {
            this.statment.executeUpdate(query);
            
        }catch (Exception e) {
           
        }
    }

}
