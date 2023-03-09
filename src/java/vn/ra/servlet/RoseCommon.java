/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.object.BRANCH;
import vn.ra.object.CERTIFICATION_POLICY_DATA;
import vn.ra.object.CERTIFICATION_PROFILE;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.PKI_FORMFACTOR;
import vn.ra.object.PROFILE_DISCOUNT_RATE_ATTRIBUTE;
import vn.ra.object.PROFILE_DISCOUNT_RATE_DATA;
import vn.ra.object.SOAPSecureProperties;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author USER
 */
public class RoseCommon extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RoseCommon.class);
    private static final long serialVersionUID = 6106269076155338045L;

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
        try (PrintWriter out = response.getWriter()) {
            HttpSession sessionsa = request.getSession(false);
            ConnectDatabase com = new ConnectDatabase();
            String strView = "";
            try {
                int sOutInner;
                if (sessionsa != null) {
                    String strInnerUsername = sessionsa.getAttribute("sUserID").toString().trim();
                    String strInnerSessionKey = sessionsa.getAttribute("sesSessKey").toString().trim();
                    sOutInner = com.CheckIsLoginOnline(strInnerUsername, strInnerSessionKey);
                } else {
                    sOutInner = 2;
                }
                switch (sOutInner) {
                    case 1:
                        String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                        String idParam = request.getParameter("idParam");
                        if (null != idParam) {
                            switch (idParam) {
                                case "editrose": {
                                    //<editor-fold defaultstate="collapsed" desc="editrose">
                                    String anticsrf = request.getParameter("CsrfToken");
                                    if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                        String id = request.getParameter("id");
                                        String Remark = request.getParameter("Remark");
                                        String Remark_EN = request.getParameter("Remark_EN");
                                        String ActiveFlag = request.getParameter("ActiveFlag");
                                        String pPROPERTIES = "";
                                        ObjectMapper objectMapper = null;
                                        PROFILE_DISCOUNT_RATE_DATA[][] resProfileData = (PROFILE_DISCOUNT_RATE_DATA[][]) request.getSession(false).getAttribute("SessDiscountRateAccess");
                                        if(resProfileData != null && resProfileData[0] != null)
                                        {
                                            if(resProfileData[0].length > 0)
                                            {
                                                PROFILE_DISCOUNT_RATE_ATTRIBUTE certificationAttributes123 = null;
                                                objectMapper = new ObjectMapper();
                                                ArrayList<PROFILE_DISCOUNT_RATE_ATTRIBUTE.Attribute> tempListParse = new ArrayList<>();
                                                PROFILE_DISCOUNT_RATE_ATTRIBUTE.Attribute attrOut = new PROFILE_DISCOUNT_RATE_ATTRIBUTE.Attribute();
                                                attrOut.setAttributeType(Definitions.CONFIG_ROSE_ATTRIBUTE_TYPE_PROFILE_DISCOUNT_RATE_LIST);
                                                attrOut.setEnabled(true);
                                                ArrayList<PROFILE_DISCOUNT_RATE_ATTRIBUTE.Attribute> tempListProfile = new ArrayList<>();
                                                for (PROFILE_DISCOUNT_RATE_DATA mhIP : resProfileData[0]) {
                                                    PROFILE_DISCOUNT_RATE_ATTRIBUTE.Attribute item = new PROFILE_DISCOUNT_RATE_ATTRIBUTE.Attribute();
                                                    item.setProfileName(mhIP.profileName);
                                                    item.setProfileRemark(mhIP.profileRemark);
                                                    item.setProfileRemarkEN(mhIP.profileRemarkEN);
                                                    item.setRosePercent(mhIP.rosePercent);
                                                    item.setEnabled(mhIP.enabled);
                                                    item.setIsMoneyType(mhIP.isMoneyType);
                                                    item.setAttributeType(mhIP.attributeType);
                                                    tempListProfile.add(item);
                                                }
                                                attrOut.setAttributes(tempListProfile);
                                                tempListParse.add(attrOut);
                                                certificationAttributes123 = new PROFILE_DISCOUNT_RATE_ATTRIBUTE();
                                                certificationAttributes123.setAttributes(tempListParse);
                                                pPROPERTIES = objectMapper.writeValueAsString(certificationAttributes123);
                                                CommonFunction.LogDebugString(log, "pPROPERTIES-ROSE", pPROPERTIES);
                                            }
                                        } else {
                                            GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                            if (sessGeneralPolicy1[0].length > 0) {
                                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                {
                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DEFAULT_DISCOUNT_RATE_PROFILE))
                                                    {
                                                        pPROPERTIES = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        
                                        String param1 = com.S_BO_DISCOUNT_RATE_PROFILE_UPDATE(id, EscapeUtils.escapeHtml(Remark),
                                            EscapeUtils.escapeHtml(Remark_EN),
                                            ActiveFlag, pPROPERTIES, loginUID);
                                        if ("0".equals(param1)) {
                                            strView = "0#0";
                                            request.getSession(false).setAttribute("SessRefreshDiscountRate", "1");
                                        } else {
                                            strView = param1 + "#" + param1;
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                    }
                                    //</editor-fold>
                                    break;
                                }
                                case "addrose": {
                                    //<editor-fold defaultstate="collapsed" desc="addrose">
                                    String anticsrf = request.getParameter("CsrfToken");
                                    if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                        String GroupCode = request.getParameter("GroupCode");
                                        String Remark = request.getParameter("Remark");
                                        String Remark_EN = request.getParameter("Remark_EN");
                                        String param1 = com.S_BO_DISCOUNT_RATE_PROFILE_INSERT(EscapeUtils.escapeHtml(GroupCode),
                                                EscapeUtils.escapeHtml(Remark), EscapeUtils.escapeHtml(Remark_EN),
                                                loginUID);
                                        if ("0".equals(param1)) {
                                            strView = "0#0";
                                        } else {
                                            strView = param1 + "#" + param1;
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                    }
                                    //</editor-fold>
                                    break;
                                }
                                // Rose Access
                                case "deleteroseproperties": {
                                    //<editor-fold defaultstate="collapsed" desc="deleteroseproperties">
                                    String activeKey = EscapeUtils.CheckTextNull(request.getParameter("activeKey"));
                                    PROFILE_DISCOUNT_RATE_DATA[][] resProfileData = (PROFILE_DISCOUNT_RATE_DATA[][]) request.getSession(false).getAttribute("SessDiscountRateAccess");
                                    PROFILE_DISCOUNT_RATE_DATA[][] reProfileDataLast = new PROFILE_DISCOUNT_RATE_DATA[1][];
                                    if (resProfileData[0].length > 0) {
                                        boolean isValid = true;
                                        if (resProfileData[0].length > 0) {
                                            for (PROFILE_DISCOUNT_RATE_DATA resProfileData1 : resProfileData[0]) {
                                                if (resProfileData1.profileName.equals(activeKey)) {
                                                    isValid = false;
                                                    break;
                                                }
                                            }
                                        }
                                        if (isValid == false) {
                                            if (resProfileData[0].length > 0) {
                                                int intR = 100000;
                                                ArrayList<PROFILE_DISCOUNT_RATE_DATA> tempList;
                                                tempList = new ArrayList<>();
                                                tempList.addAll(Arrays.asList(resProfileData[0]));
                                                for (int i = 0; i < tempList.size(); i++) {
                                                    if (EscapeUtils.CheckTextNull(tempList.get(i).profileName).equals(activeKey)) {
                                                        intR = i;
                                                    }
                                                }
                                                if (intR != 100000) {
                                                    PROFILE_DISCOUNT_RATE_DATA hang = tempList.get(intR);
                                                    tempList.remove(hang);
                                                }
                                                if(tempList.size() > 0) {
                                                    reProfileDataLast[0] = new PROFILE_DISCOUNT_RATE_DATA[tempList.size()];
                                                    reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                                }
                                            }
                                        }
                                        if (reProfileDataLast != null) {
//                                            if (reProfileDataLast[0].length > 0) {
                                                request.getSession(false).setAttribute("SessDiscountRateAccess", reProfileDataLast);
                                                strView = "0#0";
//                                            } else {
//                                                strView = "0#0";
//                                            }
                                        } else {
                                            strView = "0#0";
                                        }
                                    }
                                    //</editor-fold>
                                    break;
                                }
                                case "addroseproperties": {
                                    //<editor-fold defaultstate="collapsed" desc="addroseproperties">
                                    String PERCENT_ADD = EscapeUtils.CheckTextNull(request.getParameter("PERCENT_ADD"));
                                    String idCERT_DURATION = EscapeUtils.CheckTextNull(request.getParameter("CERTIFICATION_DURATION"));
                                    String idSessIsChoise = EscapeUtils.CheckTextNull(request.getParameter("idSessIsChoise"));
                                    String CERTIFICATION_DURATION = "";
                                    String sREMARK = "";
                                    String sREMARK_EN = "";
                                    CERTIFICATION_PROFILE[][] temp = new CERTIFICATION_PROFILE[1][];
                                    com.S_BO_CERTIFICATION_PROFILE_DETAIL(idCERT_DURATION, temp);
                                    if(temp[0].length > 0) {
                                        CERTIFICATION_DURATION = EscapeUtils.CheckTextNull(temp[0][0].NAME);
                                        sREMARK = EscapeUtils.CheckTextNull(temp[0][0].REMARK);
                                        sREMARK_EN = EscapeUtils.CheckTextNull(temp[0][0].REMARK_EN);
                                    }
                                    PROFILE_DISCOUNT_RATE_DATA[][] resProfileData = (PROFILE_DISCOUNT_RATE_DATA[][]) request.getSession(false).getAttribute("SessDiscountRateAccess");
                                    PROFILE_DISCOUNT_RATE_DATA[][] reProfileDataLast = new PROFILE_DISCOUNT_RATE_DATA[1][];
                                    boolean isValid = true;
                                    if (resProfileData != null && resProfileData[0] != null) {
                                        if (resProfileData[0].length > 0) {
                                            for (PROFILE_DISCOUNT_RATE_DATA resProfileData1 : resProfileData[0]) {
                                                if (resProfileData1.profileName.equals(CERTIFICATION_DURATION)) {
                                                    isValid = false;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (isValid == true) {
                                        if (resProfileData != null && resProfileData[0] != null) {
                                            if (resProfileData[0].length > 0) {
                                                ArrayList<PROFILE_DISCOUNT_RATE_DATA> tempList;
                                                tempList = new ArrayList<>();
                                                tempList.addAll(Arrays.asList(resProfileData[0]));
                                                PROFILE_DISCOUNT_RATE_DATA rsItem = new PROFILE_DISCOUNT_RATE_DATA();
                                                rsItem.profileName = CERTIFICATION_DURATION;
                                                rsItem.profileRemark = sREMARK;
                                                rsItem.profileRemarkEN = sREMARK_EN;
                                                rsItem.rosePercent = PERCENT_ADD;
                                                rsItem.enabled = true;
                                                rsItem.isMoneyType = false;
                                                if("1".equals(idSessIsChoise)) {
                                                    rsItem.isMoneyType = true;
                                                }
                                                rsItem.attributeType = Definitions.CONFIG_ROSE_ATTRIBUTE_TYPE_PROFILE_DISCOUNT_RATE_ITEM;
                                                tempList.add(rsItem);
                                                reProfileDataLast[0] = new PROFILE_DISCOUNT_RATE_DATA[tempList.size()];
                                                reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                            }
                                        } else {
                                            ArrayList<PROFILE_DISCOUNT_RATE_DATA> tempList = new ArrayList<>();
                                            PROFILE_DISCOUNT_RATE_DATA rsItem = new PROFILE_DISCOUNT_RATE_DATA();
                                            rsItem.profileName = CERTIFICATION_DURATION;
                                            rsItem.profileRemark = sREMARK;
                                            rsItem.profileRemarkEN = sREMARK_EN;
                                            rsItem.rosePercent = PERCENT_ADD;
                                            rsItem.enabled = true;
                                            rsItem.isMoneyType = false;
                                            if("1".equals(idSessIsChoise)) {
                                                rsItem.isMoneyType = true;
                                            }
                                            rsItem.attributeType = Definitions.CONFIG_ROSE_ATTRIBUTE_TYPE_PROFILE_DISCOUNT_RATE_ITEM;
                                            tempList.add(rsItem);
                                            reProfileDataLast[0] = new PROFILE_DISCOUNT_RATE_DATA[tempList.size()];
                                            reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                        }
                                        if (reProfileDataLast[0].length > 0) {
                                            request.getSession(false).setAttribute("SessDiscountRateAccess", reProfileDataLast);
                                            strView = "0#0";
                                        }
                                    } else {
                                        strView = "ROSEPROPERTIES_CODE_EXISTS#0";
                                    }
                                    //</editor-fold>
                                    break;
                                }
                            }
                        }
                        break;
                    case 2:
                        strView = Definitions.CONFIG_EXCEPTION_STRING_LOGIN + "#0";
                        break;
                    default:
                        strView = Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN + "#0";
                        break;
                }
            } catch (NumberFormatException | SQLException e) {
                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#" + e.getMessage();
            } finally {
            }
            out.println(strView);
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
