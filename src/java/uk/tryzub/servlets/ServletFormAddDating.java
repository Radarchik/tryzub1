/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.servlets;

import uk.tryzub.entity.Dating;
import uk.tryzub.controllers.DatingHelper1;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tszin
 */
public class ServletFormAddDating extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ServletFormAddDating</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ServletFormAddDating at " + request.getContextPath() + "</h1>");
            // Get the values of all request parameters
            //Enumeration en = request.getParameterNames();

            int section =  Integer.parseInt(request.getParameter("razdel"));
            Date date = new Date();
            String name = request.getParameter("imja");
            String email = request.getParameter("email");
            String city = request.getParameter("city");
            String message = request.getParameter("info");

            Dating dating = new Dating( city,  date, email,  message, name, section);
            DatingHelper1 datingHelper = new DatingHelper1();
            datingHelper.addDatings(dating);
            
            request.getSession().invalidate();
            
            out.println("<h1>Session id is " + request.getSession().getId() + "</h1>");
            
            //redirect to window after reload data
            String redirectURL = "http://localhost:35700/tryzub/dating1.jsp";
            response.sendRedirect(redirectURL);
            /* while (en.hasMoreElements()) {
                // Get the name of the request parameter
                String name = (String) en.nextElement();
                out.println(name);
                
                // Get the value of the request parameter
                String value = request.getParameter(name);
                
                // If the request parameter can appear more than once in the query string, get all values
                String[] values = request.getParameterValues(name);
                
                for (int i = 0; i < values.length; i++) {
                out.println(" " + values[i]);
                }
                
                }*/

            out.println("</body>");
            out.println("</html>");
            response.setContentType("text/plain");

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
