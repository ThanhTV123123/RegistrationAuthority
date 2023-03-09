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
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.object.CERTIFICATE_ATTRIBUTES;
import vn.ra.object.CERTIFICATE_ATTRIBUTES_RADIO;
import vn.ra.object.CERTIFICATE_ATTRIBUTES_SESSION;
import vn.ra.object.CERTIFICATION_TYPE_COMPONENT;
import vn.ra.object.FILE_PROFILE_JSON;

/**
 *
 * @author THANH-PC
 */
public class CertificateTypeCommon extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CertificateTypeCommon.class);
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
                    String idParam = request.getParameter("idParam");
                    String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                    if (null != idParam) switch (idParam) {
                        case "editcertificatetype":{
                            //<editor-fold defaultstate="collapsed" desc="editcertificatetype">
                            String anticsrf = request.getParameter("CsrfToken");
                            if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                String ids = EscapeUtils.escapeHtml(request.getParameter("id"));
                                String Remark = request.getParameter("Remark");
                                String Remark_EN = request.getParameter("Remark_EN");
                                String ActiveFlag = request.getParameter("ActiveFlag");
                                String pCOMPONENT_PROPERTIES = "";
                                String pFILE_PROPERTIES = "";
                                ObjectMapper objectMapper;
                                String sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                CERTIFICATION_TYPE_COMPONENT[][] resProfileData = (CERTIFICATION_TYPE_COMPONENT[][]) request.getSession(false).getAttribute("SessComponentCertTypeAdd");
                                if(resProfileData != null)
                                {
                                    objectMapper = new ObjectMapper();
                                    CERTIFICATE_ATTRIBUTES certificationAttributes123 = null;
                                    ArrayList<CERTIFICATE_ATTRIBUTES.Attribute> tempListParse = new ArrayList<>();
                                    CERTIFICATE_ATTRIBUTES.Attribute attrOut;
                                    ArrayList<CERTIFICATE_ATTRIBUTES.AttributeSan> tempListParseSan = new ArrayList<>();
                                    CERTIFICATE_ATTRIBUTES.AttributeSan attrOutSan;
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
                                        ArrayList<CERTIFICATE_ATTRIBUTES.Attribute> tempListProfile = new ArrayList<>();
                                        attrOut = new CERTIFICATE_ATTRIBUTES.Attribute();
                                        attrOut.setAttributeType(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY);
                                        attrOut.setRequire(true);
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
                                        attrOut.setRequire(true);
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
                                    pCOMPONENT_PROPERTIES = objectMapper.writeValueAsString(certificationAttributes123);
                                    CommonFunction.LogDebugString(log, "pCOMPONENT-CERT-TYPE", pCOMPONENT_PROPERTIES);
                                }
                                // 
                                if("".equals(pCOMPONENT_PROPERTIES))
                                {
                                    sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_COMPONENT_INVALID;
                                } else {
                                    // file profile json
                                    FILE_PROFILE_JSON.Attribute[][] resFileData = (FILE_PROFILE_JSON.Attribute[][]) request.getSession(false).getAttribute("SessComponentFileProfileAdd");
                                    if(resFileData != null)
                                    {
                                        objectMapper = new ObjectMapper();
                                        FILE_PROFILE_JSON certificationAttributes123 = new FILE_PROFILE_JSON();
                                        ArrayList<FILE_PROFILE_JSON.Attribute> tempListParse = new ArrayList<>();
                                        for (FILE_PROFILE_JSON.Attribute mhIP : resFileData[0]) {
                                            FILE_PROFILE_JSON.Attribute certificationAttributesChil = new FILE_PROFILE_JSON.Attribute();
                                            certificationAttributesChil.setName(mhIP.getName());
                                            certificationAttributesChil.setEnabled(true);
                                            certificationAttributesChil.setRemark(mhIP.getRemark());
                                            certificationAttributesChil.setRemarkEn(mhIP.getRemarkEn());
                                            certificationAttributesChil.setIsRequire(mhIP.getIsRequire());
                                            tempListParse.add(certificationAttributesChil);
                                        }
                                        certificationAttributes123.setAttributes(tempListParse);
                                        pFILE_PROPERTIES = objectMapper.writeValueAsString(certificationAttributes123);
                                    }
                                    if(!"".equals(pFILE_PROPERTIES))
                                    {
                                        CommonFunction.LogDebugString(log, "pFILE-CERT-TYPE", pFILE_PROPERTIES);
                                    } else {
                                        sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_COMPONENT_INVALID;
                                        CommonFunction.LogDebugString(log, "pFILE-CERT-TYPE", pFILE_PROPERTIES);
                                    }
                                }
                                // 
                                if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                {
                                    String param1 = com.S_BO_CERTIFICATION_PURPOSE_UPDATE(Integer.parseInt(ids),
                                            pCOMPONENT_PROPERTIES, pFILE_PROPERTIES, EscapeUtils.escapeHtml(Remark_EN),
                                            EscapeUtils.escapeHtml(Remark), ActiveFlag, loginUID);
                                    if ("0".equals(param1)) {
                                        request.getSession(false).setAttribute("SessRefreshCertType", "1");
                                        strView = "0#0";
                                    } else {
                                        strView = param1+"#" + param1;
                                    }
                                } else {
                                    strView = sVALID_CODE+"#0";
                                }
                            } else {
                                strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "addcertificatetype":{
                            //<editor-fold defaultstate="collapsed" desc="addcertificatetype">
                            String anticsrf = request.getParameter("CsrfToken");
                            if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                String pName = request.getParameter("citycode");
                                String Remark = request.getParameter("Remark");
                                String Remark_EN = request.getParameter("Remark_EN");
                                String pCOMPONENT_PROPERTIES = "";
                                String pFILE_PROPERTIES = "";
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
                                        ArrayList<CERTIFICATE_ATTRIBUTES.Attribute> tempListProfile = new ArrayList<>();
                                        attrOut.setAttributeType(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY);
                                        attrOut.setRequire(true);
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
                                        attrOut.setAttributeType(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL);
                                        attrOut.setRequire(true);
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
                                if("".equals(pCOMPONENT_PROPERTIES))
                                {
                                    sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_COMPONENT_INVALID;
                                } else {
                                    // file profile json
                                    FILE_PROFILE_JSON.Attribute[][] resFileData = (FILE_PROFILE_JSON.Attribute[][]) request.getSession(false).getAttribute("SessComponentFileProfileAdd");
                                    if(resFileData != null)
                                    {
                                        objectMapper = new ObjectMapper();
                                        FILE_PROFILE_JSON certificationAttributes123 = new FILE_PROFILE_JSON();
                                        ArrayList<FILE_PROFILE_JSON.Attribute> tempListParse = new ArrayList<>();
                                        for (FILE_PROFILE_JSON.Attribute mhIP : resFileData[0]) {
                                            FILE_PROFILE_JSON.Attribute certificationAttributesChil = new FILE_PROFILE_JSON.Attribute();
                                            certificationAttributesChil.setName(mhIP.getName());
                                            certificationAttributesChil.setEnabled(true);
                                            certificationAttributesChil.setRemark(mhIP.getRemark());
                                            certificationAttributesChil.setRemarkEn(mhIP.getRemarkEn());
                                            certificationAttributesChil.setIsRequire(mhIP.getIsRequire());
                                            tempListParse.add(certificationAttributesChil);
                                        }
                                        certificationAttributes123.setAttributes(tempListParse);
                                        pFILE_PROPERTIES = objectMapper.writeValueAsString(certificationAttributes123);
                                    }
                                    if(!"".equals(pFILE_PROPERTIES))
                                    {
                                        CommonFunction.LogDebugString(log, "pFILE-CERT-TYPE", pFILE_PROPERTIES);
                                    } else {
                                        sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_COMPONENT_INVALID;
                                        CommonFunction.LogDebugString(log, "pFILE-CERT-TYPE", pFILE_PROPERTIES);
                                    }
                                }
                                if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                {
                                    String param1 = com.S_BO_CERTIFICATION_PURPOSE_INSERT(EscapeUtils.escapeHtml(pName),
                                        pCOMPONENT_PROPERTIES, pFILE_PROPERTIES, EscapeUtils.escapeHtml(Remark_EN), EscapeUtils.escapeHtml(Remark), loginUID);
                                    if ("0".equals(param1)) {
                                        request.getSession(false).setAttribute("SessRefreshCertType", "1");
                                        strView = "0#0";
                                    } else {
                                        strView = param1+"#" + param1;
                                    }
                                } else {
                                    strView = sVALID_CODE+"#0";
                                }
                            } else {
                                strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                            }
                            break;
                            //</editor-fold>
                        }
                        // component for cert type
                        case "addcomponentcerttypeadd": {
                            //<editor-fold defaultstate="collapsed" desc="addcomponentcerttypeadd">
                            String idCERT_FIELDCODE = EscapeUtils.CheckTextNull(request.getParameter("idCERT_FIELDCODE"));
                            String idCERT_PREFIX = EscapeUtils.CheckTextNull(request.getParameter("idCERT_PREFIX"));
                            String idCERT_DESC_VN = EscapeUtils.CheckTextNull(request.getParameter("idCERT_DESC_VN"));
                            String idCERT_DESC_EN = EscapeUtils.CheckTextNull(request.getParameter("idCERT_DESC_EN"));
                            String ididCERT_REQUIRE = EscapeUtils.CheckTextNull(request.getParameter("idCERT_REQUIRE"));
                            String idCERT_ATTRIBUTE_TYPE = EscapeUtils.CheckTextNull(request.getParameter("idCERT_ATTRIBUTE_TYPE"));
                            String idCERT_CN_TYPE = EscapeUtils.CheckTextNull(request.getParameter("idCERT_CN_TYPE"));
                            CERTIFICATION_TYPE_COMPONENT[][] resProfileData = (CERTIFICATION_TYPE_COMPONENT[][]) request.getSession(false).getAttribute("SessComponentCertTypeAdd");
                            CERTIFICATION_TYPE_COMPONENT[][] reProfileDataLast = new CERTIFICATION_TYPE_COMPONENT[1][];
                            boolean isValid = true;
                            if(resProfileData != null) {
                                if(resProfileData[0].length > 0)
                                {
                                    for(CERTIFICATION_TYPE_COMPONENT resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.name.equals(idCERT_FIELDCODE) && !resProfileData1.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)
                                            && !resProfileData1.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL))
                                        {
                                            isValid = false;
                                            break;
                                        }
                                    }
                                }
                            }
                            if(isValid == true)
                            {
                                if(idCERT_ATTRIBUTE_TYPE.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)
                                    || idCERT_ATTRIBUTE_TYPE.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL))
                                {
                                    ididCERT_REQUIRE = "1";
                                }
                                if(resProfileData != null) {
                                    if(resProfileData[0].length > 0) {
                                        ArrayList<CERTIFICATION_TYPE_COMPONENT> tempList;
                                        tempList = new ArrayList<>();
                                        tempList.addAll(Arrays.asList(resProfileData[0]));
                                        CERTIFICATION_TYPE_COMPONENT rsItem = new CERTIFICATION_TYPE_COMPONENT();
                                        rsItem.name = idCERT_FIELDCODE;
                                        rsItem.prefix = idCERT_PREFIX;
                                        rsItem.remark = idCERT_DESC_VN;
                                        rsItem.remarkEn = idCERT_DESC_EN;
                                        rsItem.require = "1".equals(ididCERT_REQUIRE);
                                        rsItem.attributeType = idCERT_ATTRIBUTE_TYPE;
                                        rsItem.commomNameType = idCERT_CN_TYPE;
                                        tempList.add(rsItem);
                                        reProfileDataLast[0] = new CERTIFICATION_TYPE_COMPONENT[tempList.size()];
                                        reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                    }
                                } else {
                                    ArrayList<CERTIFICATION_TYPE_COMPONENT> tempList = new ArrayList<>();
                                    CERTIFICATION_TYPE_COMPONENT rsItem = new CERTIFICATION_TYPE_COMPONENT();
                                    rsItem.name = idCERT_FIELDCODE;
                                    rsItem.prefix = idCERT_PREFIX;
                                    rsItem.remark = idCERT_DESC_VN;
                                    rsItem.remarkEn = idCERT_DESC_EN;
                                    rsItem.require = "1".equals(ididCERT_REQUIRE);
                                    rsItem.attributeType = idCERT_ATTRIBUTE_TYPE;
                                    rsItem.commomNameType = idCERT_CN_TYPE;
                                    tempList.add(rsItem);
                                    reProfileDataLast[0] = new CERTIFICATION_TYPE_COMPONENT[tempList.size()];
                                    reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                }
                                if(reProfileDataLast[0].length > 0)
                                {
                                    request.getSession(false).setAttribute("SessComponentCertTypeAdd", reProfileDataLast);
                                    strView = "0#0";
                                }
                            } else {
                                strView = "FIELDCODE_EXISTS#0";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "deletecomponentcerttypeadd":{
                            //<editor-fold defaultstate="collapsed" desc="deletecomponentcerttypeadd">
                            String idCERT_FIELDCODE = EscapeUtils.CheckTextNull(request.getParameter("idCERT_FIELDCODE"));
                            CERTIFICATION_TYPE_COMPONENT[][] resProfileData = (CERTIFICATION_TYPE_COMPONENT[][]) request.getSession(false).getAttribute("SessComponentCertTypeAdd");
                            CERTIFICATION_TYPE_COMPONENT[][] reProfileDataLast = new CERTIFICATION_TYPE_COMPONENT[1][];
                            boolean isValid = true;
                            if(resProfileData[0].length > 0)
                            {
                                for(CERTIFICATION_TYPE_COMPONENT resProfileData1 : resProfileData[0])
                                {
                                    if(resProfileData1.name.equals(idCERT_FIELDCODE))
                                    {
                                        isValid = false;
                                        break;
                                    }
                                }
                            }
                            if(isValid == false) {
                                if(resProfileData[0].length > 0) {
                                    int intR = 100000;
                                    ArrayList<CERTIFICATION_TYPE_COMPONENT> tempList;
                                    tempList = new ArrayList<>();
                                    tempList.addAll(Arrays.asList(resProfileData[0]));
                                    for (int i = 0; i < tempList.size(); i++) {
                                        if (EscapeUtils.CheckTextNull(tempList.get(i).name).equals(idCERT_FIELDCODE)) {
                                            intR = i;
                                        }
                                    }
                                    if (intR != 100000) {
                                        CERTIFICATION_TYPE_COMPONENT hang = tempList.get(intR);
                                        tempList.remove(hang);
                                    }
                                    reProfileDataLast[0] = new CERTIFICATION_TYPE_COMPONENT[tempList.size()];
                                    reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                }
                                if(reProfileDataLast != null) {
                                    if(reProfileDataLast[0].length > 0) {
                                        request.getSession(false).setAttribute("SessComponentCertTypeAdd", reProfileDataLast);
                                        strView = "0#0";
                                    } else {
                                        strView = "0#0";
                                    }
                                } else {
                                    strView = "0#0";
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "requirecomponentcerttypeadd":{
                            //<editor-fold defaultstate="collapsed" desc="requirecomponentcerttypeadd">
                            String idCERT_FIELDCODE = EscapeUtils.CheckTextNull(request.getParameter("idCERT_FIELDCODE"));
                            String idRequire = EscapeUtils.CheckTextNull(request.getParameter("idRequire"));
                            CERTIFICATION_TYPE_COMPONENT[][] resProfileData = (CERTIFICATION_TYPE_COMPONENT[][]) request.getSession(false).getAttribute("SessComponentCertTypeAdd");
                            CERTIFICATION_TYPE_COMPONENT[][] reProfileDataLast = new CERTIFICATION_TYPE_COMPONENT[1][];
                            boolean isValid = true;
                            boolean isRequire = false;
                            if("1".equals(idRequire))
                            {
                                isRequire = true;
                            }
                            if(resProfileData[0].length > 0)
                            {
                                for(CERTIFICATION_TYPE_COMPONENT resProfileData1 : resProfileData[0])
                                {
                                    if(resProfileData1.name.equals(idCERT_FIELDCODE))
                                    {
                                        isValid = true;
                                        break;
                                    }
                                }
                            }
                            if(isValid == true) {
                                if(resProfileData[0].length > 0) {
                                    ArrayList<CERTIFICATION_TYPE_COMPONENT> tempList;
                                    tempList = new ArrayList<>();
                                    tempList.addAll(Arrays.asList(resProfileData[0]));
                                    for (CERTIFICATION_TYPE_COMPONENT tempList1 : tempList) {
                                        if(tempList1.name.equals(idCERT_FIELDCODE))
                                        {
                                            tempList1.require = isRequire;
                                        }
                                    }
                                    reProfileDataLast[0] = new CERTIFICATION_TYPE_COMPONENT[tempList.size()];
                                    reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                }
                                if(reProfileDataLast != null) {
                                    if(reProfileDataLast[0].length > 0) {
                                        request.getSession(false).setAttribute("SessComponentCertTypeAdd", reProfileDataLast);
                                        strView = "0#0";
                                    } else {
                                        strView = "0#0";
                                    }
                                } else {
                                    strView = "0#0";
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        // file profile for cert type
                        case "addfileprofileadd":{
                            //<editor-fold defaultstate="collapsed" desc="addfileprofileadd">
                            String idFILE_PROFILE = EscapeUtils.CheckTextNull(request.getParameter("idFILE_PROFILE"));
                            String isFILE_REQUIRE = EscapeUtils.CheckTextNull(request.getParameter("isFILE_REQUIRE"));
                            String sName = "";
                            String sRemark = "";
                            String sRemarkEN = "";
                            if(!"".equals(idFILE_PROFILE))
                            {
                                String[] parts = idFILE_PROFILE.split("###");
                                sName = EscapeUtils.CheckTextNull(parts[0]);
                                sRemark = EscapeUtils.CheckTextNull(parts[1]);
                                sRemarkEN = EscapeUtils.CheckTextNull(parts[2]);
                            }
                            FILE_PROFILE_JSON.Attribute[][] resProfileData = (FILE_PROFILE_JSON.Attribute[][]) request.getSession(false).getAttribute("SessComponentFileProfileAdd");
                            FILE_PROFILE_JSON.Attribute[][] reProfileDataLast = new FILE_PROFILE_JSON.Attribute[1][];
                            boolean isValid = true;
                            if(resProfileData != null) {
                                if(resProfileData[0].length > 0)
                                {
                                    for(FILE_PROFILE_JSON.Attribute resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.getName().equals(sName))
                                        {
                                            isValid = false;
                                            break;
                                        }
                                    }
                                }
                            }
                            if(isValid == true)
                            {
                                if(resProfileData != null) {
                                    if(resProfileData[0].length > 0) {
                                        ArrayList<FILE_PROFILE_JSON.Attribute> tempList;
                                        tempList = new ArrayList<>();
                                        tempList.addAll(Arrays.asList(resProfileData[0]));
                                        FILE_PROFILE_JSON.Attribute rsItem = new FILE_PROFILE_JSON.Attribute();
                                        rsItem.setName(sName);
                                        rsItem.setRemark(sRemark);
                                        rsItem.setRemarkEn(sRemarkEN);
                                        rsItem.setIsRequire("1".equals(isFILE_REQUIRE));
                                        rsItem.setEnabled(true);
                                        tempList.add(rsItem);
                                        reProfileDataLast[0] = new FILE_PROFILE_JSON.Attribute[tempList.size()];
                                        reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                    }
                                } else {
                                    ArrayList<FILE_PROFILE_JSON.Attribute> tempList = new ArrayList<>();
                                    FILE_PROFILE_JSON.Attribute rsItem = new FILE_PROFILE_JSON.Attribute();
                                    rsItem.setName(sName);
                                    rsItem.setRemark(sRemark);
                                    rsItem.setRemarkEn(sRemarkEN);
                                    rsItem.setIsRequire("1".equals(isFILE_REQUIRE));
                                    rsItem.setEnabled(true);
                                    tempList.add(rsItem);
                                    reProfileDataLast[0] = new FILE_PROFILE_JSON.Attribute[tempList.size()];
                                    reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                }
                                if(reProfileDataLast[0].length > 0)
                                {
                                    request.getSession(false).setAttribute("SessComponentFileProfileAdd", reProfileDataLast);
                                    strView = "0#0";
                                }
                            } else {
                                strView = "FILECODE_EXISTS#0";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "requirefileprofileadd":{
                            //<editor-fold defaultstate="collapsed" desc="requirefileprofileadd">
                            String idFILE_PROFILE = EscapeUtils.CheckTextNull(request.getParameter("idFILE_PROFILE"));
                            String isFILE_REQUIRE = EscapeUtils.CheckTextNull(request.getParameter("isFILE_REQUIRE"));
                            FILE_PROFILE_JSON.Attribute[][] resProfileData = (FILE_PROFILE_JSON.Attribute[][]) request.getSession(false).getAttribute("SessComponentFileProfileAdd");
                            FILE_PROFILE_JSON.Attribute[][] reProfileDataLast = new FILE_PROFILE_JSON.Attribute[1][];
                            boolean isValid = true;
                            boolean isRequire = false;
                            if("1".equals(isFILE_REQUIRE))
                            {
                                isRequire = true;
                            }
                            if(resProfileData[0].length > 0)
                            {
                                for(FILE_PROFILE_JSON.Attribute resProfileData1 : resProfileData[0])
                                {
                                    if(resProfileData1.getName().equals(idFILE_PROFILE))
                                    {
                                        isValid = true;
                                        break;
                                    }
                                }
                            }
                            if(isValid == true) {
                                if(resProfileData[0].length > 0) {
                                    ArrayList<FILE_PROFILE_JSON.Attribute> tempList;
                                    tempList = new ArrayList<>();
                                    tempList.addAll(Arrays.asList(resProfileData[0]));
                                    for (FILE_PROFILE_JSON.Attribute tempList1 : tempList) {
                                        if(tempList1.getName().equals(idFILE_PROFILE))
                                        {
                                            tempList1.setIsRequire(isRequire);
                                        }
                                    }
                                    reProfileDataLast[0] = new FILE_PROFILE_JSON.Attribute[tempList.size()];
                                    reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                }
                                if(reProfileDataLast != null) {
                                    if(reProfileDataLast[0].length > 0) {
                                        request.getSession(false).setAttribute("SessComponentFileProfileAdd", reProfileDataLast);
                                        strView = "0#0";
                                    } else {
                                        strView = "0#0";
                                    }
                                } else {
                                    strView = "0#0";
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "deletefileprofileadd":{
                            //<editor-fold defaultstate="collapsed" desc="deletefileprofileadd">
                            String idFILE_PROFILE_CODE = EscapeUtils.CheckTextNull(request.getParameter("idFILE_PROFILE_CODE"));
                            FILE_PROFILE_JSON.Attribute[][] resProfileData = (FILE_PROFILE_JSON.Attribute[][]) request.getSession(false).getAttribute("SessComponentFileProfileAdd");
                            FILE_PROFILE_JSON.Attribute[][] reProfileDataLast = new FILE_PROFILE_JSON.Attribute[1][];
                            boolean isValid = true;
                            if(resProfileData[0].length > 0)
                            {
                                for(FILE_PROFILE_JSON.Attribute resProfileData1 : resProfileData[0])
                                {
                                    if(resProfileData1.getName().equals(idFILE_PROFILE_CODE))
                                    {
                                        isValid = false;
                                        break;
                                    }
                                }
                            }
                            if(isValid == false) {
                                if(resProfileData[0].length > 0) {
                                    int intR = 100000;
                                    ArrayList<FILE_PROFILE_JSON.Attribute> tempList;
                                    tempList = new ArrayList<>();
                                    tempList.addAll(Arrays.asList(resProfileData[0]));
                                    for (int i = 0; i < tempList.size(); i++) {
                                        if (EscapeUtils.CheckTextNull(tempList.get(i).getName()).equals(idFILE_PROFILE_CODE)) {
                                            intR = i;
                                        }
                                    }
                                    if (intR != 100000) {
                                        FILE_PROFILE_JSON.Attribute hang = tempList.get(intR);
                                        tempList.remove(hang);
                                    }
                                    reProfileDataLast[0] = new FILE_PROFILE_JSON.Attribute[tempList.size()];
                                    reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                }
                                if(reProfileDataLast != null) {
                                    if(reProfileDataLast[0].length > 0) {
                                        request.getSession(false).setAttribute("SessComponentFileProfileAdd", reProfileDataLast);
                                        strView = "0#0";
                                    } else {
                                        strView = "0#0";
                                    }
                                } else {
                                    strView = "0#0";
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                    }
                } else if (sOutInner == 2) {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_LOGIN + "#0";
                } else {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN + "#0";
                }
            } catch (NumberFormatException | SQLException | UnsupportedEncodingException e) {
                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#" + e.getMessage();
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
