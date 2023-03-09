/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import vn.ra.object.ROLE;
import vn.ra.process.ConnectDatabase;

/**
 *
 * @author THANH-PC
 */
public class UAT_LoadListAjax extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UAT_LoadListAjax.class.getName());

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
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("text/html");
            response.setHeader("Cache-control", "no-cache, no-store");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "-1");

            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type");
            response.setHeader("Access-Control-Max-Age", "86400");
            Gson gson = new Gson();
            JsonObject myObj = new JsonObject();
            ConnectDatabase db = new ConnectDatabase();
            String idParam = request.getParameter("idParam");
            if (null != idParam) {
                switch (idParam) {
                    case "listparams": {
                        //<editor-fold defaultstate="collapsed" desc="listparams">
                        ROLE[][] rsParams = new ROLE[1][];
                        db.S_BO_ROLE_COMBOBOX("",rsParams);
                        JsonElement countryObj = gson.toJsonTree(rsParams[0]);
                        if (rsParams[0].length > 0) {
                            myObj.addProperty("success", true);
                        } else {
                            myObj.addProperty("success", false);
                        }
                        myObj.add("ROLE", countryObj);
                        out.println(myObj.toString());
                        break;
                        //</editor-fold>
                    }
                }
            }
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            log.info(ex.getMessage(), ex);
        }
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            log.info(ex.getMessage(), ex);
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
