/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import vn.ra.object.CERTIFICATION;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author thanh
 */
public class GetCertificateInfo extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final Logger log = Logger.getLogger(GetCertificateInfo.class.getName());

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
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
        response.setContentType("text/html;charset=UTF-8");
        Config conf = new Config();
        System.out.println("---CORS Configuration Begin - Pass domain---");
        String sDOMAIN_CALL_CHANNE = conf.GetPropertybyCode(Definitions.CONFIG_DOMAIN_CALL_CHANNEL_GETCERT);
        CommonFunction.addCorsHeaderPassDomain(response, sDOMAIN_CALL_CHANNE);
        System.out.println("---CORS Configuration Completed - Pass domain ---");
        try (PrintWriter out = response.getWriter()) {
            ConnectDatabase db = new ConnectDatabase();
            JSONArray listJson = new JSONArray();
            try {
                String idParam = request.getParameter("REQUEST_TYPE");
                if (null != idParam) {
                    switch (idParam) {
                        case "GetInfoDateExpire": {
                            //<editor-fold defaultstate="collapsed" desc="loadjsonpushnoti">
                            String idCERT_TYPE = EscapeUtils.CheckTextNull(request.getParameter("idCERT_TYPE"));
                            String idMST = EscapeUtils.CheckTextNull(request.getParameter("idMST"));
                            String idCMND = EscapeUtils.CheckTextNull(request.getParameter("idCMND"));
                            String idMNS = EscapeUtils.CheckTextNull(request.getParameter("idBUDGET_CODE"));
                            String idPASSPORT = EscapeUtils.CheckTextNull(request.getParameter("idPASSPORT"));
                            String pKEY_AUTHEN_CLIENT = EscapeUtils.CheckTextNull(request.getParameter("KEY_AUTHEN"));
                            String pKEY_AUTHEN_SERVER = conf.GetPropertybyCode(Definitions.CONFIG_KEY_AUTHEN_CHANNEL_GETCERT);
                            if (pKEY_AUTHEN_SERVER.equals(pKEY_AUTHEN_CLIENT)) {
                                if (!"".equals(idCERT_TYPE)) {
                                    boolean isCheckCertType = false;
                                    if(Definitions.CONFIG_CHANNEL_GETCERT_TYPE_ENTERPRISE.equals(idCERT_TYPE))
                                    {
                                        if (!"".equals(idMST) || !"".equals(idMNS))
                                        {
                                            isCheckCertType = true;
                                        }
                                        idCMND = "";
                                        idPASSPORT = "";
                                    } else if (Definitions.CONFIG_CHANNEL_GETCERT_TYPE_PERSONAL.equals(idCERT_TYPE))
                                    {
                                        if (!"".equals(idCMND) || !"".equals(idPASSPORT))
                                        {
                                            isCheckCertType = true;
                                        }
                                        idMST = "";
                                        idMNS = "";
                                    }
                                    if (isCheckCertType == true) {
                                        CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                                        db.S_BO_GET_CERTIFICATION_OPERATION(idCERT_TYPE, idMST, idMNS, idCMND, idPASSPORT, rsCert);
                                        if (rsCert[0].length > 0) {
                                            int i = 1;
                                            for (CERTIFICATION temp1 : rsCert[0]) {
                                                JSONObject json = new JSONObject();
                                                json.put("CODE", "0");
                                                json.put("MESSAGE", "Success");
                                                json.put("INDEX", String.valueOf(i));
                                                json.put("CERT_TYPE", EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PURPOSE_DESC));
                                                json.put("MST", EscapeUtils.CheckTextNull(temp1.TAX_CODE));
                                                json.put("CMND", EscapeUtils.CheckTextNull(temp1.P_ID));
                                                json.put("BUDGET_CODE", EscapeUtils.CheckTextNull(temp1.BUDGET_CODE));
                                                json.put("PASSPORT", EscapeUtils.CheckTextNull(temp1.PASSPORT));
                                                json.put("DATE_VALID", EscapeUtils.CheckTextNull(temp1.EFFECTIVE_DT));
                                                json.put("DATE_EXPIRE", EscapeUtils.CheckTextNull(temp1.EXPIRATION_DT));
                                                listJson.add(json);
                                                i++;
                                            }
                                            
                                            
                                        }
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("CODE", "1");
                                    json.put("MESSAGE", "No Data Found");
                                    listJson.add(json);
                                }
                            } else {
                                JSONObject json = new JSONObject();
                                json.put("CODE", "2");
                                json.put("MESSAGE", "Invalid Authentication Code");
                                listJson.add(json);
                            }
                            break;
                            //</editor-fold>
                        }
                    }
                }
            } catch (NumberFormatException e) {
                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                JSONObject json = new JSONObject();
                json.put("CODE", "3");
                json.put("MESSAGE", e.getMessage());
                listJson.add(json);
            }
//            CommonFunction.LogDebugString(log, "JSON-GetInfoDateExpire", listJson.toString());
            out.println(listJson);
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
//            processRequest(request, response);
        } catch (Exception e) {
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
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
        } catch (Exception e) {
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
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
