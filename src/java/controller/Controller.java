package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import beans.StockBook;
import beans.VisitorCurrentRental;
import integration.DatabaseHandler;
import java.io.IOException;
import java.io.PrintWriter;
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
            returnBookActionPageHanlder(request, response, parametersMap);
            
        }else if(parametersMap.containsKey("bookLecture")){
            forwardingDispatcher(request, response, "/lecturesPage.jsp");
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
    
    
    private void returnBookActionPageHanlder(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> parametersMap) {
        
        
    }
    
    public void administratorPageHandler(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> parametersMap) throws ServletException, IOException{
        if(parametersMap.containsKey("listLectures")){
            forwardingDispatcher(request, response, "/lectureListPage.jsp");
        }
        else if(parametersMap.containsKey("newSlot")){
            forwardingDispatcher(request, response, "/newSlot.jsp");
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
            else if(parametersMap.containsKey("rentBook") || parametersMap.containsKey("returnBook") || parametersMap.containsKey("bookLecture")) {
                 visitorPageHandler(request, response, parametersMap);
            }
            else if(parametersMap.containsKey("listLectures") || parametersMap.containsKey("newSlot")) {
                
                administratorPageHandler(request, response, parametersMap);
            }else if (parametersMap.containsKey("rentBookAction")) {
                rentBookActionPageHandler(request, response, parametersMap);
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
