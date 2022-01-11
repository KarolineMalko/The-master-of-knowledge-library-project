package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import beans.StockBook;
import beans.IncomingLecture;
import beans.LectureInfo;
import beans.OngoingLectureInfo;
import beans.VisitorCurrentRental;
import integration.DatabaseHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Karoline Malko
 */
public class Controller extends HttpServlet {

    private final DatabaseHandler databaseHandler;
    
    public Controller() throws Exception {
        this.databaseHandler = new DatabaseHandler();
        this.databaseHandler.establishConnection();
    }
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs, the exception will be catched and re-thrown to a higher level
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
    }
    
    /**
     * add an attribute to the session
     * @param request the request follows any action / webpage visit in the server
     * @param name  the name which the object will be saved as
     * @param obj   any object can be stored in the session
     */
    protected void addAttributeToSession(HttpServletRequest request, String name, Object obj) {
        HttpSession session = request.getSession();
        session.setAttribute(name, obj);
    }
    
    /**
     * get an attribute from the session
     * @param request   the request follows any action / webpage visit in the server
     * @param name the name which the object will be saved as
     * @return return the object which has been saved as "name"
     */
    protected Object getAttributeFromSession(HttpServletRequest request, String name) {
        HttpSession session = request.getSession();
        return session.getAttribute(name);
    }
    
    /**
     * forward the request to another webpage
     * @param request the request follows any action / webpage visit in the server
     * @param response the response that server sends back to user
     * @param pageName the name of the page the request will be forwarded to
     * 
     * @throws ServletException if any ServletException has been thrown, the exception will be catched and re-thrown to a higher level
     * @throws IOException if any IOException has been thrown, the exception will be catched and re-thrown to a higher level
     */
    public void forwardingDispatcher(HttpServletRequest request, HttpServletResponse response, String pageName) throws ServletException, IOException {
        RequestDispatcher oldNew = request.getRequestDispatcher(pageName);
        oldNew.forward(request, response);
    }
    
    /**
     * redirect the request to other pages according to the pressed button action
     * @param request the request follows any action / webpage visit in the server
     * @param response the response that server sends back to user
     * @param parametersMap that include buttons Names, labels names, and other actions name comes with the request 
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if any IOException has been thrown, the exception will be catched and re-thrown to a higher level
     * @throws Exception a general Exception will be thrown
     */
    public void welcomePageHandler(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> parametersMap) throws ServletException, IOException, Exception{
        try {
            if(parametersMap.containsKey("visitSub")) {
            forwardingDispatcher(request, response, "/visitorLoginPage.jsp");
            
            }else if(parametersMap.containsKey("adminSub")) {
                forwardingDispatcher(request, response, "/adminLoginPage.jsp");
            }
        }catch(IOException | ServletException e) {
            throw new Exception();
        }
        
    }
    
    /**
     *  import the userID and set it as attribute in session 
     * @param request the request follows any action / webpage visit in the server
     * @param response the response that server sends back to user
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs, the exception will be catched and re-thrown to a higher level
     */
    public void visitorLoginPageHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String email = request.getParameter("visitEmail");
        String pass = request.getParameter("visitPassword");
        String userIDTxt = this.databaseHandler.importUserID(email, pass);
        addAttributeToSession(request, "visitorID", userIDTxt);
        forwardingDispatcher(request, response, "/visitorPage.jsp");
    }
    
    /**
     *  add adminID attribute to session
     * @param request the request follows any action / webpage visit in the server
     * @param response the response that server sends back to user
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if any IOException has been thrown, the exception will be catched and re-thrown to a higher level
     */
    public void adminLoginPageHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String email = request.getParameter("adminEmail");
        String pass = request.getParameter("adminPassword");
        String adminIDTxt = this.databaseHandler.importAdminID(email, pass);
        addAttributeToSession(request, "adminID", adminIDTxt);
        forwardingDispatcher(request, response, "/administratorPage.jsp");
    }
    
    /**
     *  add the stockBooks to the session
     * @param request the request follows any action / webpage visit in the server
     * @param response the response that server sends back to user
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs, the exception will be catched and re-thrown to a higher level
     */
    public void rentBookPageHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        ArrayList<StockBook> stockBooksInfo = this.databaseHandler.importStockBooksInfo();
        addAttributeToSession(request, "stockBooks", stockBooksInfo);
        forwardingDispatcher(request, response, "/availableStockBooksPage.jsp");
    }
    
    /**
     *  redirect the user to either rent a book, return a book or book a lecture.
     * @param request the request follows any action / webpage visit in the server
     * @param response the response that server sends back to user
     * @param parametersMap that include buttons Names, labels names, and other actions name comes with the request 
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException  if an I/O error occurs, the exception will be catched and re-thrown to a higher level
     */
    public void visitorPageHandler(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> parametersMap) throws ServletException, IOException, Exception{
        if(parametersMap.containsKey("rentBook")){
            
            rentBookPageHandler(request, response);
            
        }else if(parametersMap.containsKey("returnBook")){
            showCurrentRentedBooks(request, response);
            
        }else if(parametersMap.containsKey("displayLectures")){
            displayLectures(request, response);
        }
    }
    
    
    /*
        pass parameters and execute queries to apply rent book by checking if user is eligible for an extra rental, and if there is available copies in the stock, 
        then decrease the specified book copies from the stock, and check the change in the database, 
        increase the user's rentals amount and check the change in the database, and the same thing for creating a new rental instance.
    */
    private void rentBookActionPageHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookIsbn = Integer.parseInt(request.getParameter("rentBookAction"));
        String visitorIDAsText =  (String) getAttributeFromSession(request, "visitorID");
        int visitorID = Integer.parseInt(visitorIDAsText);
        int availableCopies = this.databaseHandler.checkAvailableCopies(bookIsbn);
        int rentedVisitorBooks = this.databaseHandler.checkVisitorRentalBooksLimit(visitorID);
        if(visitorID == 0 || rentedVisitorBooks == -1 || availableCopies == -1 ) {
            forwardingDispatcher(request, response, "/failedRental.jsp");
            System.out.println(visitorID);
            System.out.println(rentedVisitorBooks);
            System.out.println(availableCopies);
            
            return;
        }
        boolean decreaseSucess = this.databaseHandler.decreaseBookAvailableCopies(bookIsbn); 
        if (decreaseSucess != true) {
            System.out.println("second if: " + decreaseSucess);
            forwardingDispatcher(request, response, "/failedRental.jsp");
            return;
        }
        
        boolean visitorRentalSucess = this.databaseHandler.increaseVisitorRentedBooks(visitorID); 
        if(visitorRentalSucess != true) {
            System.out.println("third if: " + decreaseSucess);
            forwardingDispatcher(request, response, "/failedRental.jsp");
            return;
        }
        
        boolean newRentalInstance = this.databaseHandler.createNewRentalInstance(visitorID, bookIsbn);
        if(newRentalInstance != true) {
            System.out.print("fourth if: " + decreaseSucess);
            forwardingDispatcher(request, response, "/failedRental.jsp");
            return;
        }
        forwardingDispatcher(request, response, "/successfulRental.jsp");
    }
    
    
    //  Show the user's rented book to choose which book will be returned
 
    private void showCurrentRentedBooks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception {
        try {
            String visitorIDAsText =  (String) getAttributeFromSession(request, "visitorID");
            int visitorID = Integer.parseInt(visitorIDAsText);
            ArrayList<VisitorCurrentRental> visitorRentals = this.databaseHandler.importVisitorRentalsInfo(visitorID);

            addAttributeToSession(request, "visitorRentals", visitorRentals);
            forwardingDispatcher(request, response, "/showVisitorRentals.jsp");
        } catch (IOException ex) {
            throw new Exception();
        }
        
    }
    
    // this function is called when the user chooses to return a specific book of his previous rentals, the function updates the status of rental instance in the db, 
    // then execute query to decrease the visitor's rented books amount and check the change, and the same thing for updating stock book's available copies. and forward 
    // to the rental approval page.
    private void returnBookActionPageHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception {
        
        int bookIsbn = Integer.parseInt(request.getParameter("returnBookAction"));
        String visitorIDAsText =  (String) getAttributeFromSession(request, "visitorID");
        int visitorID = Integer.parseInt(visitorIDAsText);
        
        boolean rentalTerminateUpdateSucess = this.databaseHandler.updateRentalTerminationStatus(bookIsbn, visitorID); 
        if (rentalTerminateUpdateSucess != true) {
            forwardingDispatcher(request, response, "/failedRentalReturn.jsp");
            return;
        }
        
        boolean visitorRentedAmountSucess = this.databaseHandler.decreaseVisitorRentedBooks(visitorID); 
        if(visitorRentedAmountSucess != true) {
            forwardingDispatcher(request, response, "/failedRentalReturn.jsp");
            return;
        }
        boolean updateBookAvailableCopiesSuccess = this.databaseHandler.updateStockBookAvailableCopies(bookIsbn);

        if(updateBookAvailableCopiesSuccess != true) {
            forwardingDispatcher(request, response, "/failedRentalReturn.jsp");
            return;
        }
        forwardingDispatcher(request, response, "/successfulReturn.jsp");
        
    }

    /**
     *  the function execute queries to book a lecture, check the room capacity if it has exceeded, increase Enrolled visitor's number, and create enrolled Visitors instance in the table.
     * @param request the request follows any action / webpage visit in the server
     * @param response the response that server sends back to user
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs, the exception will be catched and re-thrown to a higher level
     * @throws Exception a general Exception will be thrown
     */
    public void bookLecture(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception {
        int lectureId = Integer.parseInt(request.getParameter("bookLecture"));
        int userID = Integer.parseInt((String) getAttributeFromSession(request, "visitorID"));
        if(!(this.databaseHandler.checkRoomCapacity(lectureId))){
            forwardingDispatcher(request, response, "/maximumEnrolledVisitorsReached.jsp");
            return;
        }
        if(!(this.databaseHandler.increseEnrolledVisitors(lectureId))) {
            forwardingDispatcher(request, response, "/unsuccessfulVisitorLectureRegistration.jsp");
            return;
        }
        int enrolledVisitorInstanceCase = this.databaseHandler.createEnrolledVisitorsInstanse(lectureId, userID);
        switch (enrolledVisitorInstanceCase) {
            case 0:
                forwardingDispatcher(request, response, "/unsuccessfulVisitorLectureRegistration.jsp");
                return;
            case 2:
                forwardingDispatcher(request, response, "/alreadEnrolledVisitorError.jsp");
                return;
            default:
                forwardingDispatcher(request, response, "/successfullyEnrolled.jsp");
                return;
        }
        
    }

    /**
     * display the administrator page handler 
     * @param request the request follows any action / webpage visit in the server
     * @param response the response that server sends back to user
     * @param parametersMap that include buttons Names, labels names, and other actions name comes with the request 
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs, the exception will be catched and re-thrown to a higher level
      * @throws Exception a general Exception will be thrown
     */
    public void administratorPageHandler(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> parametersMap) throws ServletException, IOException, Exception{
        if(parametersMap.containsKey("listLectures")){
            ArrayList<OngoingLectureInfo> lectures = this.databaseHandler.importOngoingLectures();
            addAttributeToSession(request, "lectures", lectures);
            forwardingDispatcher(request, response, "/lectureListPage.jsp");
        }
        else if(parametersMap.containsKey("newSlot")){
            forwardingDispatcher(request, response, "/newSlot.jsp");
        }
    }

    /**
     *  display the week's lectures to visitor and let him/her book a lecture to enroll in the library
     * @param request the request follows any action / webpage visit in the server
     * @param response the response that server sends back to user
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs, the exception will be catched and re-thrown to a higher level
     * @throws Exception a general Exception will be thrown
     */
    public void displayLectures(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception {
        ArrayList<IncomingLecture> weekIncomingLecture = this.databaseHandler.displayIncomingLec();
        addAttributeToSession(request, "weekIncomingLecture", weekIncomingLecture);
        forwardingDispatcher(request, response, "/lecturePage.jsp");
    }
    
    // Show the admin The week's incoming lectures and lsit them
    private void showAdminIncomingLectures(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception {
        ArrayList<IncomingLecture> weekIncomingLecture = this.databaseHandler.displayIncomingLec();
        addAttributeToSession(request, "weekIncomingLecture", weekIncomingLecture);
        forwardingDispatcher(request, response, "/AdminIcomigLecturePage.jsp");
    }

    /**
     *  create the new lecture instance and insert it to the db. And check if it has been added to the db or not.
     * @param request the request follows any action / webpage visit in the server
     * @param response the response that server sends back to user
     * @throws Exception a general Exception will be thrown
     */
    public void addNewLecturePageHandler(HttpServletRequest request, HttpServletResponse response) throws Exception{
        try{    
            String lectureName = request.getParameter("lectureName");
            String lectureDate = request.getParameter("lectureDate");
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            String lecturerPerNum = request.getParameter("lecturerPerNum");
            
            LectureInfo newLectureInfoInstance = this.databaseHandler.createLectureInfoInstance(lectureName, lectureDate, roomId, lecturerPerNum);
            
            boolean addedOrNotAdded = this.databaseHandler.newLectureInsert(newLectureInfoInstance);
            
            if (addedOrNotAdded == true){
                forwardingDispatcher(request, response, "/lectureIsAddedPage.jsp");
            }else{
                forwardingDispatcher(request, response, "/lectureIsNotAddedPage.jsp");
            }
        }catch(Exception e) {
            throw new Exception();
        }          
    }



    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        forwardingDispatcher(request, response, "/welcomePage.jsp");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs, the exception will be catched and re-thrown to a higher level
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        try{
            
            processRequest(request, response);
            Map<String, String[]> parametersMap =  request.getParameterMap(); 
            if (parametersMap.containsKey("visitSub") || parametersMap.containsKey("adminSub")) {
                welcomePageHandler(request, response, parametersMap);
            }
            else if(parametersMap.containsKey("visitPassword")) {
                visitorLoginPageHandler(request, response);
            }else if(parametersMap.containsKey("adminEmail")) {
                adminLoginPageHandler(request, response);
            }
            else if(parametersMap.containsKey("rentBook") || parametersMap.containsKey("returnBook") || parametersMap.containsKey("displayLectures")) {
                 visitorPageHandler(request, response, parametersMap);
            }
	    else if (parametersMap.containsKey("rentBookAction")) {
                rentBookActionPageHandler(request, response);
            }
	    else if(parametersMap.containsKey("returnBookAction")) {
                returnBookActionPageHandler(request, response);
            }
	    else if(parametersMap.containsKey("bookLecture")) {
                bookLecture(request, response);
            }
            else if(parametersMap.containsKey("listLectures") || parametersMap.containsKey("newSlot")) {
                
                administratorPageHandler(request, response, parametersMap);
            }
            else if(parametersMap.containsKey("showWeekLectures")) {
                showAdminIncomingLectures(request, response);
            }
	    else if(parametersMap.containsKey("addNewLecture")) {
                addNewLecturePageHandler(request, response);
            }
        
    }catch(Exception e) {
           System.out.print("An error has been occured");
        }
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    
    
    

    

}
