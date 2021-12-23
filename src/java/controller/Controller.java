package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import integration.DatabaseHandler;
import java.io.IOException;
import java.io.PrintWriter;
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
         if(parametersMap.containsKey("visitSub")){
            forwardingDispatcher(request, response, "/visitorLoginPage.jsp");
           // return 0;
        }else if(parametersMap.containsKey("adminSub"))
            forwardingDispatcher(request, response, "/adminLoginPage.jsp");
            //return 1;
        
    }
    
    public void visitorLoginPageHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String email = request.getParameter("visitEmail");
        String pass = request.getParameter("visitPassword");
        System.out.println("here");
        forwardingDispatcher(request, response, "/visitorPage.jsp");
    }
    
    public void adminLoginPageHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        forwardingDispatcher(request, response, "/administratorPage.jsp");
    }
    
    public void visitorPageHandler(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> parametersMap) throws ServletException, IOException{
        if(parametersMap.containsKey("rentBook")){
            forwardingDispatcher(request, response, "/rentalBookPage.jsp");
            
        }else if(parametersMap.containsKey("returnBook")){
            forwardingDispatcher(request, response, "/returnBookPage.jsp");
            
        }else if(parametersMap.containsKey("bookLecture")){
            forwardingDispatcher(request, response, "/lecturesPage.jsp");
        }
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
        processRequest(request, response);
        try{
            
            
            processRequest(request, response);
            Map<String, String[]> parametersMap =  request.getParameterMap();                         
            welcomePageHandler(request, response, parametersMap);
            if(parametersMap.containsKey("visitPassword")) {
                visitorLoginPageHandler(request, response);
            }else if(parametersMap.containsKey("adminEmail")) {
                adminLoginPageHandler(request, response);
            }
            
            visitorPageHandler(request, response, parametersMap);
            administratorPageHandler(request, response, parametersMap);
            
        
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
