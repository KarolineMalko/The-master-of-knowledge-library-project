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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private DatabaseHandler databaseHandler;
    
    public Controller() {
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
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
    }
    
       protected void addAttributeToSession(HttpServletRequest request, String name, Object obj) {
        HttpSession session = request.getSession();
        session.setAttribute(name, obj);
    }
    
    protected Object getAttributeFromSession(HttpServletRequest request, String name) {
        HttpSession session = request.getSession();
        return session.getAttribute(name);
    }
    
    /**
     *
     * @param request
     * @param response
     * @param pageName
     * @throws ServletException
     * @throws IOException
     */
    public void forwardingDispatcher(HttpServletRequest request, HttpServletResponse response, String pageName) throws ServletException, IOException {
        RequestDispatcher oldNew = request.getRequestDispatcher(pageName);
        oldNew.forward(request, response);
    }
    
    
    public void welcomePageHandler(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> parametersMap) throws ServletException, IOException{
         if(parametersMap.containsKey("visitSub")) {
            forwardingDispatcher(request, response, "/visitorLoginPage.jsp");
            
        }else if(parametersMap.containsKey("adminSub")) {
            forwardingDispatcher(request, response, "/adminLoginPage.jsp");
        }
    }
    
    public void visitorLoginPageHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String email = request.getParameter("visitEmail");
        String pass = request.getParameter("visitPassword");
        String userIDTxt = this.databaseHandler.importUserID(email, pass);
        addAttributeToSession(request, "visitorID", userIDTxt);
        forwardingDispatcher(request, response, "/visitorPage.jsp");
    }
    
    public void adminLoginPageHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String email = request.getParameter("adminEmail");
        String pass = request.getParameter("adminPassword");
        String adminIDTxt = this.databaseHandler.importAdminID(email, pass);
        addAttributeToSession(request, "adminID", adminIDTxt);
        forwardingDispatcher(request, response, "/administratorPage.jsp");
    }
    
    public void rentBookPageHandler(HttpServletRequest request, HttpServletResponse response, String pagePathAndName) throws ServletException, IOException{
        ArrayList<StockBook> stockBooksInfo = this.databaseHandler.importStockBooksInfo();
        addAttributeToSession(request, "stockBooks", stockBooksInfo);
        forwardingDispatcher(request, response, "/availableStockBooksPage.jsp");
    }
    
    public void visitorPageHandler(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> parametersMap) throws ServletException, IOException{
        if(parametersMap.containsKey("rentBook")){
            
            rentBookPageHandler(request, response, "/rentalBookPage.jsp");
            
        }else if(parametersMap.containsKey("returnBook")){
            showCurrentRentedBooks(request, response, parametersMap);
            
        }else if(parametersMap.containsKey("displayLectures")){
            displayLectures(request, response, parametersMap);
        }
    }
    
    
    
    private void rentBookActionPageHandler(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> parametersMap) throws ServletException, IOException {
        int bookIsbn = Integer.parseInt(request.getParameter("rentBookAction"));
        String visitorIDAsText =  (String) getAttributeFromSession(request, "visitorID");
        int visitorID = Integer.parseInt(visitorIDAsText);
        int availableCopies = this.databaseHandler.checkAvailableCopies(bookIsbn);
        int rentedVisitorBooks = this.databaseHandler.checkVisitorRentalBooksLimit(visitorID);
        if(visitorID == 0 || rentedVisitorBooks == -1 || availableCopies == -1 ) {
            forwardingDispatcher(request, response, "/failedRental.jsp");
            return;
        }
        boolean decreaseSucess = this.databaseHandler.decreaseBookAvailableCopies(bookIsbn); 
        if (decreaseSucess != true) {
            forwardingDispatcher(request, response, "/failedRental.jsp");
            return;
        }
        
        boolean visitorRentalSucess = this.databaseHandler.increaseVisitorRentedBooks(visitorID); 
        if(visitorRentalSucess != true) {
            forwardingDispatcher(request, response, "/failedRental.jsp");
            return;
        }
        
        boolean newRentalInstance = this.databaseHandler.createNewRentalInstance(visitorID, bookIsbn);
        if(newRentalInstance != true) {
            forwardingDispatcher(request, response, "/failedRental.jsp");
            return;
        }
        forwardingDispatcher(request, response, "/successfulRental.jsp");
    }
    
    
    private void showCurrentRentedBooks(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> parametersMap) throws ServletException, IOException {
        try {
            String visitorIDAsText =  (String) getAttributeFromSession(request, "visitorID");
            int visitorID = Integer.parseInt(visitorIDAsText);
            ArrayList<VisitorCurrentRental> visitorRentals = this.databaseHandler.importVisitorRentalsInfo(visitorID);

            addAttributeToSession(request, "visitorRentals", visitorRentals);
            forwardingDispatcher(request, response, "/showVisitorRentals.jsp");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    
    private void returnBookActionPageHandler(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> parametersMap) throws ServletException, IOException {
        
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
        //System.out.println(updateBookAvailableCopiesSuccess);
        if(updateBookAvailableCopiesSuccess != true) {
            forwardingDispatcher(request, response, "/failedRentalReturn.jsp");
            return;
        }
        forwardingDispatcher(request, response, "/successfulReturn.jsp");
        
    }

   public void bookLecture(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> parametersMap) throws ServletException, IOException {
        int lectureId = Integer.parseInt(request.getParameter("bookLecture"));
        int userID = Integer.parseInt((String) getAttributeFromSession(request, "visitorID"));
        if(!(this.databaseHandler.checkRoomCapacity(lectureId))){
            forwardingDispatcher(request, response, "/maximumEnrolledVisitorsReached.jsp");
            return;
          //System.out.println("You can book this lecture!");
        }
        if(!(this.databaseHandler.increseEnrolledVisitors(lectureId))) {
            forwardingDispatcher(request, response, "/unsuccessfulVisitorLectureRegistration.jsp");
            return;
        }
        int enrolledVisitorInstanceCase = this.databaseHandler.createEnrolledVisitorsInstanse(lectureId, userID);
        if(enrolledVisitorInstanceCase == 0) {
            forwardingDispatcher(request, response, "/unsuccessfulVisitorLectureRegistration.jsp");
            return;
        }else if(enrolledVisitorInstanceCase == 2) {
            forwardingDispatcher(request, response, "/alreadEnrolledVisitorError.jsp");
            return;
        }else {
            forwardingDispatcher(request, response, "/successfullyEnrolled.jsp");
            return;
        }
        
    }


	
    
    
    
    
    public void administratorPageHandler(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> parametersMap) throws ServletException, IOException{
        if(parametersMap.containsKey("listLectures")){
            ArrayList<OngoingLectureInfo> lectures = this.databaseHandler.importOngoingLectures();
            addAttributeToSession(request, "lectures", lectures);
            forwardingDispatcher(request, response, "/lectureListPage.jsp");
        }
        else if(parametersMap.containsKey("newSlot")){
            forwardingDispatcher(request, response, "/newSlot.jsp");
        }
    }


    public void displayLectures(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> parametersMap) throws ServletException, IOException {
        ArrayList<IncomingLecture> weekIncomingLecture = this.databaseHandler.displayIncomingLec();
        addAttributeToSession(request, "weekIncomingLecture", weekIncomingLecture);
        forwardingDispatcher(request, response, "/lecturePage.jsp");
    }
    

    public void addNewLecturePageHandler(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> parametersMap){
        try{    
            ArrayList<String> newSlotInfo = new ArrayList<>();
            String lectureName = request.getParameter("lectureName");
            String lectureDate = request.getParameter("lectureDate");
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            String lecturerPerNum = request.getParameter("lecturerPerNum");
            
            
            //ArrayList<LectureInfo> lectureInfoList = new ArrayList<LectureInfo>();
            LectureInfo lectureInfo = new LectureInfo();
            lectureInfo.setLectureSubject(lectureName);
            lectureInfo.setLectureDate(lectureDate);
            lectureInfo.setRoomId(roomId);
            lectureInfo.setLecturerPersonNum(lecturerPerNum);
            
            boolean addedOrNotAdded = this.databaseHandler.newLectureInsert(lectureInfo);
            
            if (addedOrNotAdded == true){
                forwardingDispatcher(request, response, "/lectureIsAddedPage.jsp");
            }else{
                forwardingDispatcher(request, response, "/lectureIsNotAddedPage.jsp");
            }
            }catch(Exception e) {
            e.printStackTrace();
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
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        //processRequest(request, response);
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
                rentBookActionPageHandler(request, response, parametersMap);
            }
	    else if(parametersMap.containsKey("returnBookAction")) {
                returnBookActionPageHandler(request, response, parametersMap);
            }
	    else if(parametersMap.containsKey("bookLecture")) {
                bookLecture(request, response, parametersMap);
            }
            else if(parametersMap.containsKey("listLectures") || parametersMap.containsKey("newSlot")) {
                
                administratorPageHandler(request, response, parametersMap);
            }
	    else if(parametersMap.containsKey("addNewLecture")) {
                addNewLecturePageHandler(request, response, parametersMap);
            }
        
    }catch(Exception e) {
            e.printStackTrace();
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
