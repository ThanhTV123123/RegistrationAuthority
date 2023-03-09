/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.object.CERTIFICATE_ATTRIBUTES;
import vn.ra.object.CERTIFICATE_ATTRIBUTES_RADIO;
import vn.ra.object.CERTIFICATE_ATTRIBUTES_SESSION;
import vn.ra.object.CERTIFICATION_PURPOSE;
import vn.ra.object.CERTIFICATION_TYPE_COMPONENT;
import vn.ra.object.CredentialDataAuthen;
import vn.ra.object.PKI_FORMFACTOR;
import vn.ra.process.AddIPRelying;
import vn.ra.process.RSSPProcessCommon;

/**
 *
 * @author THANH-PC
 */
public class CertProfileCommon extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CertProfileCommon.class);
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
                if (sOutInner == 1) {
                    String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                    String idParam = request.getParameter("idParam");
                    if (null != idParam) {
                        switch (idParam) {
                            case "editcertprofile": {
                                //<editor-fold defaultstate="collapsed" desc="editcertprofile">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ids = request.getParameter("id");
                                    String CERTIFICATION_AUTHORITY = EscapeUtils.escapeHtml(request.getParameter("CERTIFICATION_AUTHORITY"));
                                    String CERTIFICATION_PURPOSE = request.getParameter("CERTIFICATION_PURPOSE");
                                    String CERTIFICATION_ALGORITHM = request.getParameter("CERTIFICATION_ALGORITHM");
                                    String Code = request.getParameter("Code");
                                    String ActiveFlag = request.getParameter("ActiveFlag");
                                    String Remark = request.getParameter("Remark");
                                    String Remark_EN = request.getParameter("Remark_EN");
//                                    String pISSUE_ENABLED = request.getParameter("pISSUE_ENABLED");
                                    String ENTITY_EJBCA = EscapeUtils.escapeHtml(request.getParameter("ENTITY_EJBCA"));
                                    String pRENEWAL_AMOUNT = request.getParameter("pRENEWAL_AMOUNT");
                                    String sCheckEnterpriseUID = EscapeUtils.escapeHtml(request.getParameter("sCheckEnterpriseUID"));
                                    String sCheckPersonalUID = EscapeUtils.escapeHtml(request.getParameter("sCheckPersonalUID"));
                                    String sCheckAutoSynch = EscapeUtils.escapeHtml(request.getParameter("sCheckAutoSynch"));
                                    if("".equals(pRENEWAL_AMOUNT))
                                    {
                                        pRENEWAL_AMOUNT = "0";
                                    }
                                    String DURATION = request.getParameter("DURATION");
                                    String DURATION_FREE = request.getParameter("DURATION_FREE");
                                    if("".equals(DURATION_FREE))
                                    {
                                        DURATION_FREE = "0";
                                    }
                                    String AMOUNT = request.getParameter("AMOUNT");
                                    String pCHANGE_AMOUNT = request.getParameter("pCHANGE_AMOUNT");
                                    String pREISSUE_AMOUNT = request.getParameter("pREISSUE_AMOUNT");
                                    String pGOVERNMENT_AMOUNT = request.getParameter("pGOVERNMENT_AMOUNT");
                                    String pTOKEN_AMOUNT = EscapeUtils.escapeHtml(request.getParameter("pTOKEN_AMOUNT"));
                                    if("".equals(pTOKEN_AMOUNT)) {
                                        pTOKEN_AMOUNT = "0";
                                    }
//                                    String sIssueEnaled = EscapeUtils.CheckTextNull(request.getParameter("sIssueEnaled"));
                                    String pOPTION = EscapeUtils.CheckTextNull(request.getParameter("pOPTION"));
                                    if("".equals(pGOVERNMENT_AMOUNT)) {
                                        pGOVERNMENT_AMOUNT = "0";
                                    }
                                    String pPROPERTIES = "";
                                    ObjectMapper objectMapper;
                                    String sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                    
//                                    AddIPRelying cart2 = (AddIPRelying) request.getSession(false).getAttribute("SessAddIPRelying");
//                                    if (cart2 != null) {
                                       // ObjectMapper objectMapper = new ObjectMapper();
//                                        CERTIFICATE_ATTRIBUTES certificationAttributes123 = null;
//                                        ArrayList<CERTIFICATE_ATTRIBUTES_SESSION> ds = cart2.getGH();
//                                        ArrayList<CERTIFICATE_ATTRIBUTES.Attribute> tempListParse = new ArrayList<>();
//                                        for (CERTIFICATE_ATTRIBUTES_SESSION mhIP : ds) {
//                                            CERTIFICATE_ATTRIBUTES.Attribute certificationAttributes = new CERTIFICATE_ATTRIBUTES.Attribute();
//                                            certificationAttributes.setName(mhIP.name);
//                                            certificationAttributes.setPrefix(mhIP.prefix);
//                                            certificationAttributes.setRemark(mhIP.remark);
//                                            tempListParse.add(certificationAttributes);
//                                        }
//                                        certificationAttributes123 = new CERTIFICATE_ATTRIBUTES();
//                                        certificationAttributes123.setAttributes(tempListParse);
//                                        ObjectMapper objectMapper = new ObjectMapper();
//                                        String strRes = objectMapper.writeValueAsString(certificationAttributes123);

//                                        CERTIFICATE_ATTRIBUTES certificationAttributes123 = null;
//                                        ArrayList<CERTIFICATE_ATTRIBUTES_SESSION> ds = cart2.getGH();
//                                        ArrayList<CERTIFICATE_ATTRIBUTES.Attribute> tempListParse = new ArrayList<>();
//                                        for (CERTIFICATE_ATTRIBUTES_SESSION mhIP : ds) {
//                                            CERTIFICATE_ATTRIBUTES.Attribute certificationAttributes = new CERTIFICATE_ATTRIBUTES.Attribute();
//                                            if(mhIP.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_RADIOBUTTON))
//                                            {
//                                                certificationAttributes.setAttributeType(mhIP.attributeType);
//                                                certificationAttributes.setRequire(mhIP.require);
//                                                ArrayList<CERTIFICATE_ATTRIBUTES.Attribute> tempListChil = new ArrayList<>();
//                                                for (CERTIFICATE_ATTRIBUTES_RADIO radio : mhIP.radio) {
//                                                    CERTIFICATE_ATTRIBUTES.Attribute certificationAttributesChil = new CERTIFICATE_ATTRIBUTES.Attribute();
//                                                    certificationAttributesChil.setName(radio.name);
//                                                    certificationAttributesChil.setRemark(radio.remark);
//                                                    certificationAttributesChil.setRemarkEn(radio.remarkEn);
//                                                    certificationAttributesChil.setPrefix(radio.prefix);
//                                                    certificationAttributesChil.setRequire(radio.require);
//                                                    certificationAttributesChil.setAttributeType(radio.attributeType);
//                                                    tempListChil.add(certificationAttributesChil);
//                                                }
//                                                certificationAttributes.setAttributes(tempListChil);
//                                                tempListParse.add(certificationAttributes);
//                                            }
//                                            else
//                                            {
//                                                certificationAttributes.setName(mhIP.name);
//                                                if(!"".equals(mhIP.prefix))
//                                                {
//                                                    certificationAttributes.setPrefix(mhIP.prefix);
//                                                }
//                                                certificationAttributes.setRequire(mhIP.require);
//                                                certificationAttributes.setRemark(mhIP.remark);
//                                                certificationAttributes.setRemarkEn(mhIP.remarkEn);
//                                                certificationAttributes.setAttributeType(mhIP.attributeType);
//                                                if(!"".equals(mhIP.commomNameType))
//                                                {
//                                                    certificationAttributes.setCommomNameType(mhIP.commomNameType);
//                                                }
//                                                tempListParse.add(certificationAttributes);
//                                            }
//                                        }
//                                        certificationAttributes123 = new CERTIFICATE_ATTRIBUTES();
//                                        certificationAttributes123.setAttributes(tempListParse);
//                                        pPROPERTIES = objectMapper.writeValueAsString(certificationAttributes123);
//                                    }
                                    
                                    CERTIFICATION_TYPE_COMPONENT[][] resProfileData = (CERTIFICATION_TYPE_COMPONENT[][]) request.getSession(false).getAttribute("SessComponentCertTypeAdd");
                                    if(resProfileData != null)
                                    {
                                        objectMapper = new ObjectMapper();
                                        CERTIFICATE_ATTRIBUTES certificationAttributes123 = null;
                                        ArrayList<CERTIFICATE_ATTRIBUTES.Attribute> tempListParse = new ArrayList<>();
                                        ArrayList<CERTIFICATE_ATTRIBUTES.AttributeSan> tempListParseSan = new ArrayList<>();
                                        CERTIFICATE_ATTRIBUTES.AttributeSan attrOutSan;
                                        CERTIFICATE_ATTRIBUTES.Attribute attrOut;
                                        attrOut = new CERTIFICATE_ATTRIBUTES.Attribute();
                                        boolean isHasUID_Company = false;
                                        for (CERTIFICATION_TYPE_COMPONENT mhIP : resProfileData[0]) {
                                            if(mhIP.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY))
                                            {
                                                isHasUID_Company = true;
                                                break;
                                            }
                                        }
                                        boolean isHasUID_Personal = false;
                                        for (CERTIFICATION_TYPE_COMPONENT mhIP : resProfileData[0]) {
                                            if(mhIP.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL))
                                            {
                                                isHasUID_Personal = true;
                                                break;
                                            }
                                        }
                                        if(isHasUID_Company == true)
                                        {
                                            boolean requireEnterpriseUID = false;
                                            if("1".equals(sCheckEnterpriseUID)){requireEnterpriseUID=true;}
                                            ArrayList<CERTIFICATE_ATTRIBUTES.Attribute> tempListProfile = new ArrayList<>();
                                            attrOut.setAttributeType(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY);
                                            attrOut.setRequire(requireEnterpriseUID);
                                            for (CERTIFICATION_TYPE_COMPONENT mhIP : resProfileData[0]) {
                                                if(mhIP.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY))
                                                {
                                                    CERTIFICATE_ATTRIBUTES.Attribute certificationAttributesChil = new CERTIFICATE_ATTRIBUTES.Attribute();
                                                    certificationAttributesChil.setName(mhIP.name);
                                                    certificationAttributesChil.setPrefix(mhIP.prefix);
                                                    certificationAttributesChil.setRemark(mhIP.remark);
                                                    certificationAttributesChil.setRemarkEn(mhIP.remarkEn);
                                                    certificationAttributesChil.setRequire(requireEnterpriseUID);
                                                    certificationAttributesChil.setAttributeType(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD);
                                                    tempListProfile.add(certificationAttributesChil);
                                                }
                                            }
                                            attrOut.setAttributes(tempListProfile);
                                            tempListParse.add(attrOut);
                                        }

                                        if(isHasUID_Personal == true)
                                        {
                                            boolean requirePersonalUID = false;
                                            if("1".equals(sCheckPersonalUID)){requirePersonalUID=true;}
                                            ArrayList<CERTIFICATE_ATTRIBUTES.Attribute> tempListProfile = new ArrayList<>();
                                            attrOut = new CERTIFICATE_ATTRIBUTES.Attribute();
                                            attrOut.setAttributeType(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL);
                                            attrOut.setRequire(requirePersonalUID);
                                            for (CERTIFICATION_TYPE_COMPONENT mhIP : resProfileData[0]) {
                                                if(mhIP.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL))
                                                {
                                                    CERTIFICATE_ATTRIBUTES.Attribute certificationAttributesChil = new CERTIFICATE_ATTRIBUTES.Attribute();
                                                    certificationAttributesChil.setName(mhIP.name);
                                                    certificationAttributesChil.setPrefix(mhIP.prefix);
                                                    certificationAttributesChil.setRemark(mhIP.remark);
                                                    certificationAttributesChil.setRemarkEn(mhIP.remarkEn);
                                                    certificationAttributesChil.setRequire(requirePersonalUID);
                                                    certificationAttributesChil.setAttributeType(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD);
                                                    tempListProfile.add(certificationAttributesChil);
                                                }
                                            }
                                            attrOut.setAttributes(tempListProfile);
                                            tempListParse.add(attrOut);
                                        }

                                        for (CERTIFICATION_TYPE_COMPONENT mhIP : resProfileData[0]) {
                                            if(mhIP.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD))
                                            {
                                                attrOut = new CERTIFICATE_ATTRIBUTES.Attribute();
                                                attrOut.setName(mhIP.name);
                                                attrOut.setRemark(mhIP.remark);
                                                attrOut.setRemarkEn(mhIP.remarkEn);
                                                attrOut.setRequire(mhIP.require);
                                                attrOut.setAttributeType(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD);
                                                if(!"".equals(mhIP.commomNameType))
                                                {
                                                    attrOut.setCommomNameType(mhIP.commomNameType);
                                                }
                                                tempListParse.add(attrOut);
                                            }
                                        }
                                        for (CERTIFICATION_TYPE_COMPONENT mhIP : resProfileData[0]) {
                                            if(mhIP.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD_SAN))
                                            {
                                                attrOutSan = new CERTIFICATE_ATTRIBUTES.AttributeSan();
                                                attrOutSan.setName(mhIP.name);
                                                attrOutSan.setRemark(mhIP.remark);
                                                attrOutSan.setRemarkEn(mhIP.remarkEn);
                                                attrOutSan.setRequire(mhIP.require);
                                                attrOutSan.setAttributeType(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD_SAN);
                                                tempListParseSan.add(attrOutSan);
                                            }
                                        }

                                        certificationAttributes123 = new CERTIFICATE_ATTRIBUTES();
                                        certificationAttributes123.setAttributes(tempListParse);
                                        certificationAttributes123.setAttributeSans(tempListParseSan);
                                        pPROPERTIES = objectMapper.writeValueAsString(certificationAttributes123);
                                    }
                                    if("".equals(pPROPERTIES))
                                    {
                                        sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_COMPONENT_INVALID;
                                    } else {
                                        String param1 = com.S_BO_CERTIFICATION_PROFILE_UPDATE(Integer.parseInt(ids), ActiveFlag,
                                            EscapeUtils.escapeHtml(Code), EscapeUtils.escapeHtml(CERTIFICATION_PURPOSE),
                                            EscapeUtils.escapeHtml(CERTIFICATION_ALGORITHM), EscapeUtils.escapeHtml(CERTIFICATION_AUTHORITY),
                                            AMOUNT.replace(",", ""), pRENEWAL_AMOUNT.replace(",", ""), pCHANGE_AMOUNT.replace(",", ""),
                                            pREISSUE_AMOUNT.replace(",", ""), pGOVERNMENT_AMOUNT.replace(",", ""),
                                            DURATION.replace(",", ""), DURATION_FREE.replace(",", ""), pPROPERTIES, EscapeUtils.escapeHtml(Remark_EN),
                                            EscapeUtils.escapeHtml(Remark), EscapeUtils.escapeHtml(loginUID), ENTITY_EJBCA, pTOKEN_AMOUNT.replace(",", ""));
                                        if ("0".equals(param1)) {
                                            strView = "0#0";
                                            request.getSession(false).setAttribute("RefreshCertProfileSess", "1");
                                            request.getSession(false).setAttribute("SessComponentCertTypeAdd", null);
                                            com.S_BO_CERTIFICATION_PROFILE_ATTR_UPDATE_OPTION_ONLY(Integer.parseInt(ids), Integer.parseInt(pOPTION), EscapeUtils.escapeHtml(loginUID));
//                                            String pPROFILE_ATTR_TYPE_ONLY_ISSUE = "30";
//                                            com.S_BO_CERTIFICATION_PROFILE_ATTR_UPDATE(ids, pPROFILE_ATTR_TYPE_ONLY_ISSUE,
//                                                sIssueEnaled, "", EscapeUtils.escapeHtml(loginUID));
                                            com.S_BO_CERTIFICATION_PROFILE_UPDATE_AUTO_SYNC(ids, sCheckAutoSynch, EscapeUtils.escapeHtml(loginUID));
                                        } else {
                                            strView = param1 + "#" + param1;
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "addcertprofile": {
                                //<editor-fold defaultstate="collapsed" desc="addcertprofile">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String Remark = request.getParameter("Remark");
                                    String Remark_EN = request.getParameter("Remark_EN");
                                    String ENTITY_EJBCA = EscapeUtils.escapeHtml(request.getParameter("ENTITY_EJBCA"));
                                    String pRENEWAL_AMOUNT = EscapeUtils.CheckTextNull(request.getParameter("pRENEWAL_AMOUNT"));
                                    String PKI_FORMFACTOR = EscapeUtils.CheckTextNull(request.getParameter("PKI_FORMFACTOR"));
                                    String sIssueEnaled = EscapeUtils.CheckTextNull(request.getParameter("sIssueEnaled"));
                                    String sCheckAutoSynch = EscapeUtils.escapeHtml(request.getParameter("sCheckAutoSynch"));
                                    if("".equals(pRENEWAL_AMOUNT))
                                    {
                                        pRENEWAL_AMOUNT = "0";
                                    }
                                    String DURATION = EscapeUtils.CheckTextNull(request.getParameter("DURATION"));
                                    if("".equals(DURATION))
                                    {
                                        DURATION = "0";
                                    }
                                    String DURATION_FREE = EscapeUtils.CheckTextNull(request.getParameter("DURATION_FREE"));
                                    if("".equals(DURATION_FREE))
                                    {
                                        DURATION_FREE = "0";
                                    }
                                    String AMOUNT = EscapeUtils.CheckTextNull(request.getParameter("AMOUNT"));
                                    if("".equals(AMOUNT))
                                    {
                                        AMOUNT = "0";
                                    }
                                    String pCHANGE_AMOUNT = EscapeUtils.CheckTextNull(request.getParameter("pCHANGE_AMOUNT"));
                                    if("".equals(pCHANGE_AMOUNT))
                                    {
                                        pCHANGE_AMOUNT = "0";
                                    }
                                    String pREISSUE_AMOUNT = EscapeUtils.CheckTextNull(request.getParameter("pREISSUE_AMOUNT"));
                                    if("".equals(pREISSUE_AMOUNT))
                                    {
                                        pREISSUE_AMOUNT = "0";
                                    }
                                    String pGOVERNMENT_AMOUNT = EscapeUtils.CheckTextNull(request.getParameter("pGOVERNMENT_AMOUNT"));
                                    if("".equals(pGOVERNMENT_AMOUNT))
                                    {
                                        pGOVERNMENT_AMOUNT = "0";
                                    }
                                    String pTOKEN_AMOUNT = EscapeUtils.escapeHtml(request.getParameter("pTOKEN_AMOUNT"));
                                    if("".equals(pTOKEN_AMOUNT)) {
                                        pTOKEN_AMOUNT = "0";
                                    }
                                    String CERTIFICATION_AUTHORITY = EscapeUtils.escapeHtml(request.getParameter("CERTIFICATION_AUTHORITY"));
                                    String CERTIFICATION_PURPOSE = request.getParameter("CERTIFICATION_PURPOSE");
                                    String CERTIFICATION_ALGORITHM = request.getParameter("CERTIFICATION_ALGORITHM");                                    
                                    String pCOMPONENT_PROPERTIES = "";
                                    ObjectMapper objectMapper;
                                    String sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                    CERTIFICATION_TYPE_COMPONENT[][] resProfileData = (CERTIFICATION_TYPE_COMPONENT[][]) request.getSession(false).getAttribute("SessComponentCertTypeAdd");
                                    if(resProfileData != null)
                                    {
                                        objectMapper = new ObjectMapper();
                                        CERTIFICATE_ATTRIBUTES certificationAttributes123 = null;
                                        ArrayList<CERTIFICATE_ATTRIBUTES.Attribute> tempListParse = new ArrayList<>();
                                        CERTIFICATE_ATTRIBUTES.Attribute attrOut;
                                        attrOut = new CERTIFICATE_ATTRIBUTES.Attribute();
                                        boolean isHasUID_Company = false;
                                        boolean isRequireCompany = true;
                                        for (CERTIFICATION_TYPE_COMPONENT mhIP : resProfileData[0]) {
                                            if(mhIP.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY))
                                            {
                                                isRequireCompany = mhIP.require;
                                                isHasUID_Company = true;
                                                break;
                                            }
                                        }
                                        boolean isHasUID_Personal = false;
                                        boolean isRequirePersonal = true;
                                        for (CERTIFICATION_TYPE_COMPONENT mhIP : resProfileData[0]) {
                                            if(mhIP.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL))
                                            {
                                                isRequirePersonal = mhIP.require;
                                                isHasUID_Personal = true;
                                                break;
                                            }
                                        }
                                        if(isHasUID_Company == true)
                                        {
                                            ArrayList<CERTIFICATE_ATTRIBUTES.Attribute> tempListProfile = new ArrayList<>();
                                            attrOut.setAttributeType(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY);
                                            attrOut.setRequire(isRequireCompany);
                                            for (CERTIFICATION_TYPE_COMPONENT mhIP : resProfileData[0]) {
                                                if(mhIP.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY))
                                                {
                                                    CERTIFICATE_ATTRIBUTES.Attribute certificationAttributesChil = new CERTIFICATE_ATTRIBUTES.Attribute();
                                                    certificationAttributesChil.setName(mhIP.name);
                                                    certificationAttributesChil.setPrefix(mhIP.prefix);
                                                    certificationAttributesChil.setRemark(mhIP.remark);
                                                    certificationAttributesChil.setRemarkEn(mhIP.remarkEn);
                                                    certificationAttributesChil.setRequire(mhIP.require);
                                                    certificationAttributesChil.setAttributeType(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD);
                                                    tempListProfile.add(certificationAttributesChil);
                                                }
                                            }
                                            attrOut.setAttributes(tempListProfile);
                                            tempListParse.add(attrOut);
                                        }

                                        if(isHasUID_Personal == true)
                                        {
                                            ArrayList<CERTIFICATE_ATTRIBUTES.Attribute> tempListProfile = new ArrayList<>();
                                            attrOut = new CERTIFICATE_ATTRIBUTES.Attribute();
                                            attrOut.setAttributeType(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL);
                                            attrOut.setRequire(isRequirePersonal);
                                            for (CERTIFICATION_TYPE_COMPONENT mhIP : resProfileData[0]) {
                                                if(mhIP.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL))
                                                {
                                                    CERTIFICATE_ATTRIBUTES.Attribute certificationAttributesChil = new CERTIFICATE_ATTRIBUTES.Attribute();
                                                    certificationAttributesChil.setName(mhIP.name);
                                                    certificationAttributesChil.setPrefix(mhIP.prefix);
                                                    certificationAttributesChil.setRemark(mhIP.remark);
                                                    certificationAttributesChil.setRemarkEn(mhIP.remarkEn);
                                                    certificationAttributesChil.setRequire(mhIP.require);
                                                    certificationAttributesChil.setAttributeType(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD);
                                                    tempListProfile.add(certificationAttributesChil);
                                                }
                                            }
                                            attrOut.setAttributes(tempListProfile);
                                            tempListParse.add(attrOut);
                                        }

                                        for (CERTIFICATION_TYPE_COMPONENT mhIP : resProfileData[0]) {
                                            if(mhIP.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD))
                                            {
                                                attrOut = new CERTIFICATE_ATTRIBUTES.Attribute();
                                                attrOut.setName(mhIP.name);
                                                attrOut.setRemark(mhIP.remark);
                                                attrOut.setRemarkEn(mhIP.remarkEn);
                                                attrOut.setRequire(mhIP.require);
                                                attrOut.setAttributeType(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD);
                                                if(!"".equals(mhIP.commomNameType))
                                                {
                                                    attrOut.setCommomNameType(mhIP.commomNameType);
                                                }
                                                tempListParse.add(attrOut);
                                            }
                                        }

                                        certificationAttributes123 = new CERTIFICATE_ATTRIBUTES();
                                        certificationAttributes123.setAttributes(tempListParse);
                                        pCOMPONENT_PROPERTIES = objectMapper.writeValueAsString(certificationAttributes123);
                                        CommonFunction.LogDebugString(log, "pCOMPONENT-CERT-TYPE", pCOMPONENT_PROPERTIES);
                                    }
                                    
                                    String Code = request.getParameter("Code");
                                    if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                    {
                                        int[] pPROFILE_ID = new int[1];
                                        String param1 = com.S_BO_CERTIFICATION_PROFILE_INSERT(EscapeUtils.escapeHtml(Code),
                                            CERTIFICATION_PURPOSE, CERTIFICATION_ALGORITHM, CERTIFICATION_AUTHORITY,
                                            AMOUNT.replace(",", ""), pRENEWAL_AMOUNT.replace(",", ""), pCHANGE_AMOUNT.replace(",", ""),
                                            pREISSUE_AMOUNT.replace(",", ""), pGOVERNMENT_AMOUNT.replace(",", ""), DURATION.replace(",", ""),
                                            DURATION_FREE.replace(",", ""), pCOMPONENT_PROPERTIES, EscapeUtils.escapeHtml(Remark_EN),
                                            EscapeUtils.escapeHtml(Remark), EscapeUtils.escapeHtml(loginUID), ENTITY_EJBCA, pPROFILE_ID, pTOKEN_AMOUNT.replace(",", ""));
                                        if ("0".equals(param1)) {
                                            String pPROFILE_ATTR_TYPE_ONLY_ISSUE = "30";
                                            String pFORMFACTOR_ATTR_TYPE_PROFILE = "1";
                                            com.S_BO_CERTIFICATION_PROFILE_ATTR_UPDATE(String.valueOf(pPROFILE_ID[0]), pPROFILE_ATTR_TYPE_ONLY_ISSUE,
                                                sIssueEnaled, "", EscapeUtils.escapeHtml(loginUID));
                                            com.S_BO_PKI_FORMFACTOR_ATTR_INSERT(PKI_FORMFACTOR, pFORMFACTOR_ATTR_TYPE_PROFILE,
                                                String.valueOf(pPROFILE_ID[0]),EscapeUtils.escapeHtml(loginUID));
                                            com.S_BO_CERTIFICATION_PROFILE_UPDATE_AUTO_SYNC(String.valueOf(pPROFILE_ID[0]), sCheckAutoSynch, EscapeUtils.escapeHtml(loginUID));
                                            request.getSession(false).setAttribute("RefreshCertProfileSess", "1");
                                            strView = "0#0";
                                        } else {
                                            strView = param1 + "#" + param1;
                                        }
                                    } else {
                                        strView = sVALID_CODE+"#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "deleteproperties": {
                                //<editor-fold defaultstate="collapsed" desc="deleteproperties">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String Name = request.getParameter("Name");
                                    String Prefix = request.getParameter("Prefix");
                                    String isCheck = request.getParameter("isCheck");
                                    AddIPRelying cartIP = (AddIPRelying) request.getSession(false).getAttribute("SessAddIPRelying");
                                    if (cartIP != null) {
                                        boolean booCheck = false;
                                        if("1".equals(isCheck))
                                        {
                                            booCheck = true;
                                        }
                                        String param1 = cartIP.DeleteIPList(Name, Prefix, booCheck);
                                        if ("0".equals(param1)) {
                                            request.getSession(false).setAttribute("SessAddIPRelying", cartIP);
                                            strView = "0#0";
                                        } else {
                                            strView = param1 + "#" + param1;
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "rsspcertificatelock": {
                                //<editor-fold defaultstate="collapsed" desc="rsspcertificatelock">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String relyingPartyName = request.getParameter("relyingPartyName");
                                    String agreementUUID = request.getParameter("agreementUUID");
                                    String certSN = request.getParameter("certSN");
                                    PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                    com.S_BO_PKI_FORMFACTOR_DETAIL(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD), rsFormfactorPro);
                                    String sFormFactorPro = "";
                                    if(rsFormfactorPro[0].length > 0) {
                                        sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                        CredentialDataAuthen credentialAuthen = CommonFunction.loadCredentialDataAuthen(sFormFactorPro);
                                        String[] sParam = new String[3]; int[] sCode = new int[1];
                                        RSSPProcessCommon rsspClass = new RSSPProcessCommon();
                                        rsspClass.blockSigningCertificate(credentialAuthen, relyingPartyName, agreementUUID, certSN, sParam, sCode);
                                        if(sCode[0] == 0) {
                                            strView = "0#0";
                                        } else {
                                            strView = "1#" + sParam[0];
                                        }
                                    } else {strView = "1#Method is invalid";}
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "rsspcertificateunlock": {
                                //<editor-fold defaultstate="collapsed" desc="rsspcertificateunlock">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String relyingPartyName = request.getParameter("relyingPartyName");
                                    String agreementUUID = request.getParameter("agreementUUID");
                                    String certSN = request.getParameter("certSN");
                                    PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                    com.S_BO_PKI_FORMFACTOR_DETAIL(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD), rsFormfactorPro);
                                    String sFormFactorPro = "";
                                    if(rsFormfactorPro[0].length > 0) {
                                        sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                        CredentialDataAuthen credentialAuthen = CommonFunction.loadCredentialDataAuthen(sFormFactorPro);
                                        String[] sParam = new String[3]; int[] sCode = new int[1];
                                        RSSPProcessCommon rsspClass = new RSSPProcessCommon();
                                        rsspClass.unblockSigningCertificate(credentialAuthen, relyingPartyName, agreementUUID, certSN, sParam, sCode);
                                        if(sCode[0] == 0) {
                                            strView = "0#0";
                                        } else {
                                            strView = "1#" + sParam[0];
                                        }
                                    } else {strView = "1#Method is invalid";}
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "rsspcertificatereserpasscode": {
                                //<editor-fold defaultstate="collapsed" desc="rsspcertificatereserpasscode">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String relyingPartyName = request.getParameter("relyingPartyName");
                                    String agreementUUID = request.getParameter("agreementUUID");
                                    String certSN = request.getParameter("certSN");
                                    PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                    com.S_BO_PKI_FORMFACTOR_DETAIL(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD), rsFormfactorPro);
                                    String sFormFactorPro = "";
                                    if(rsFormfactorPro[0].length > 0) {
                                        sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                        CredentialDataAuthen credentialAuthen = CommonFunction.loadCredentialDataAuthen(sFormFactorPro);
                                        String[] sParam = new String[3]; int[] sCode = new int[1];
                                        RSSPProcessCommon rsspClass = new RSSPProcessCommon();
                                        rsspClass.forgetPasscodeForSignCloud(credentialAuthen, relyingPartyName, agreementUUID, certSN, sParam, sCode);
                                        if(sCode[0] == 0) {
                                            strView = "0#0";
                                        } else {
                                            strView = "1#" + sParam[0];
                                        }
                                    } else {strView = "1#Method is invalid";}
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                        }
                    }
                } else if (sOutInner == 2) {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_LOGIN + "#0";
                } else {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN + "#0";
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
