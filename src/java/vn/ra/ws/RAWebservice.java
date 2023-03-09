/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.log4j.Logger;
import vn.ra.object.BRANCH;
import vn.ra.object.CertificateInfo;
import vn.ra.object.CertificateStateInfo;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.RESPONSE_CODE;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.RACoreWSProcess;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author THANH-PC
 * register: 1756 -> 2860
 * renewal: 2862 -> 3478
 * Change: 3480 -> 4238
 */
@WebService(serviceName = "RAWebservice")
public class RAWebservice {

    private static final Logger log = Logger.getLogger(RAWebservice.class);

    @Resource
    WebServiceContext wsContext;

    private String getIPAddress() {
        MessageContext mc = wsContext.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        return req.getRemoteAddr();
    }
    
     /**
     * This is a sample web service operation
     *
     * @param raServiceReq
     * @return
     */
    //<editor-fold defaultstate="collapsed" desc="@@@ 1 getBranchForTMSRA">
    @WebMethod(operationName = "getBranchForTMSRA")
    public RAServiceResp getBranchForTMSRA(@WebParam(name = "getBranchForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sCA_ENABLED = "1";
            String sIP_Request = getIPAddress();
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                String pPARENT_ID;
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    if (!pPARENT_ID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                        sCA_ENABLED = "0";
                    }
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    if("0".equals(sCA_ENABLED)) {
                        raServiceReq.branchCode = raServiceReq.credentialData.username;
                    }
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getBranchProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
            // get response code return
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return raServiceResp;
    }
    //</editor-fold>

    /**
     * This is a sample web service operation
     *
     * @param raServiceReq
     * @return
     */
    //<editor-fold defaultstate="collapsed" desc="@@@ 2 getUserRoleForTMSRA">
    @WebMethod(operationName = "getUserRoleForTMSRA")
    public RAServiceResp getUserRoleForTMSRA(@WebParam(name = "getUserRoleForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sCA_ENABLED = "";
            String sIP_Request = getIPAddress();
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                String pPARENT_ID;
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    if (!pPARENT_ID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                        sCA_ENABLED = "0";
                    }
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getUserRoleProcess(sCA_ENABLED, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="@@@ 3 getUserForTMSRA">
    @WebMethod(operationName = "getUserForTMSRA")
    public RAServiceResp getUserForTMSRA(@WebParam(name = "getUserForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pBRANCH_NAME = "";
            String sIP_Request = getIPAddress();
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                String pPARENT_ID;
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    if (!pPARENT_ID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                        pBRANCH_NAME = rsBranch[0][0].NAME;
                    } else {
                        pBRANCH_NAME = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
                    }
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getUserProcess(pBRANCH_NAME, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="@@@ 4 getCityProvinceForTMSRA">
    @WebMethod(operationName = "getCityProvinceForTMSRA")
    public RAServiceResp getCityProvinceForTMSRA(@WebParam(name = "getCityProvinceForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sIP_Request = getIPAddress();
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCityProvinceProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="@@@ 5 getCertificateAuthorityForTMSRA">
    @WebMethod(operationName = "getCertificateAuthorityForTMSRA")
    public RAServiceResp getCertificateAuthorityForTMSRA(@WebParam(name = "getCertificateAuthorityForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        try {
            String sSOAP_WS = "";
            String sCERT_PROFILE_PROPERTIES = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sIP_Request = getIPAddress();
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES))
                    {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateAuthorityProcess(sCERT_PROFILE_PROPERTIES, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="@@@ 6 getCertificatePurposeForTMSRA">
    @WebMethod(operationName = "getCertificatePurposeForTMSRA")
    public RAServiceResp getCertificatePurposeForTMSRA(@WebParam(name = "getCertificatePurposeForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        try {
            String sSOAP_WS = "";
            String sCERT_PROFILE_PROPERTIES = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sIP_Request = getIPAddress();
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificatePurposeProcess(sCERT_PROFILE_PROPERTIES, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="@@@ 7 getCertificateProfileForTMSRA">
    @WebMethod(operationName = "getCertificateProfileForTMSRA")
    public RAServiceResp getCertificateProfileForTMSRA(@WebParam(name = "getCertificateProfileForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        try {
            String sSOAP_WS = "";
            String sCERT_PROFILE_PROPERTIES = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sIP_Request = getIPAddress();
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateProfileProcess(sCERT_PROFILE_PROPERTIES, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="@@@ 8 getCertificateComponentForTMSRA">
    @WebMethod(operationName = "getCertificateComponentForTMSRA")
    public RAServiceResp getCertificateComponentForTMSRA(@WebParam(name = "getCertificateComponentForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sCERT_PROFILE_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sIP_Request = getIPAddress();
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateComponentProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="@@@ 8 getCertificateComponent2ForTMSRA">
    @WebMethod(operationName = "getCertificateComponent2ForTMSRA")
    public RAServiceResp getCertificateComponent2ForTMSRA(@WebParam(name = "getCertificateComponent2ForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sCERT_PROFILE_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sIP_Request = getIPAddress();
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateComponent2Process(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="@@@ 9 getCertificateFileListForTMSRA">
    @WebMethod(operationName = "getCertificateFileListForTMSRA")
    public RAServiceResp getCertificateFileListForTMSRA(@WebParam(name = "getCertificateFileListForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sIP_Request = getIPAddress();
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateFileListProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="@@@ 10 getFormFactorForTMSRA">
    @WebMethod(operationName = "getFormFactorForTMSRA")
    public RAServiceResp getFormFactorForTMSRA(@WebParam(name = "getFormFactorForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        try {
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sIP_Request = getIPAddress();
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getFormFactorProcess(sCERT_POLICY_PROPERTIES, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="@@@ 11 getCertificateStateForTMSRA">
    @WebMethod(operationName = "getCertificateStateForTMSRA")
    public RAServiceResp getCertificateStateForTMSRA(@WebParam(name = "getCertificateStateForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sIP_Request = getIPAddress();
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateStateProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="@@@ 12 getCertificateRevocationReasonForTMSRA">
    @WebMethod(operationName = "getCertificateRevocationReasonForTMSRA")
    public RAServiceResp getCertificateRevocationReasonForTMSRA(@WebParam(name = "getCertificateRevocationReasonForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sIP_Request = getIPAddress();
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateRevocationReasonProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);                    
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="@@@ 13 getCertificateInfoForTMSRA">
    @WebMethod(operationName = "getCertificateInfoForTMSRA")
    public RAServiceResp getCertificateInfoForTMSRA(@WebParam(name = "getCertificateInfoForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        ObjectMapper objectMapper;
        String pBeneficiaryUserDefault = "";
        try {
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sIP_Request = getIPAddress();
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                        || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        if("".equals(pBeneficiaryUserDefault))
                        {
                            pBeneficiaryUserDefault = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        }
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateInfoProcess(pBeneficiaryUserDefault, System_Log_ID, System_Log_BillCode,
                        sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    RAServiceResp raServiceRespLog = new RAServiceResp();
                    raServiceRespLog.responseCode = raServiceResp.responseCode;
                    raServiceRespLog.responseMessage = raServiceResp.responseMessage;
                    CertificateInfo[][] certInfoLog = new CertificateInfo[1][];
                    CommonFunction.certListTruncatedForSystemLog(raServiceResp.certificateInfo, certInfoLog);
                    raServiceRespLog.certificateInfo = certInfoLog[0];
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceRespLog.responseCode),
                        objectMapper.writeValueAsString(raServiceRespLog), sTOKEN_SN, pBeneficiaryUserDefault);
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="@@@ 14 getFileTypeForTMSRA">
    @WebMethod(operationName = "getFileTypeForTMSRA")
    public RAServiceResp getFileTypeForTMSRA(@WebParam(name = "getFileTypeForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sIP_Request = getIPAddress();
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getFileTypeProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="@@@ 15 getCertificateAttachmentForTMSRA">
    @WebMethod(operationName = "getCertificateAttachmentForTMSRA")
    public RAServiceResp getCertificateAttachmentForTMSRA(@WebParam(name = "getCertificateAttachmentForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sIP_Request = getIPAddress();
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateAttachmentProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                    
//                    //<editor-fold defaultstate="collapsed" desc="### CHECK IP - FUNCTION ACCESS">
//                    CERTIFICATION_POLICY_DATA[][] resPolicyData;
//                    boolean checkFunctionAccessAll = CommonFunction.checkAPIAccessFunctionAll(sFUNCTIONALTITY_PROPERTIES);
//                    if (checkFunctionAccessAll == false) {
//                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_ACCESS_FUNCTION_INVALID;
//                        resPolicyData = new CERTIFICATION_POLICY_DATA[1][];
//                        CommonFunction.getFunctionAccessList(sFUNCTIONALTITY_PROPERTIES, resPolicyData);
//                        if (resPolicyData[0].length > 0) {
//                            for (CERTIFICATION_POLICY_DATA rsPolicyProperties : resPolicyData[0]) {
//                                if (sFunctionWS.equals(EscapeUtils.CheckTextNull(rsPolicyProperties.name))) {
//                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
//                        boolean checkIPAccessAll = CommonFunction.checkAPIAccessIPAll(sIP_ADDRESS_PROPERTIES);
//                        if (checkIPAccessAll == false) {
//                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_ACCESS_IP_INVALID;
//                            resPolicyData = new CERTIFICATION_POLICY_DATA[1][];
//                            CommonFunction.getIPAccessList(sIP_ADDRESS_PROPERTIES, resPolicyData);
//                            if (resPolicyData[0].length > 0) {
//                                for (CERTIFICATION_POLICY_DATA rsPolicyProperties : resPolicyData[0]) {
//                                    if (sIP_Request.equals(EscapeUtils.CheckTextNull(rsPolicyProperties.name))) {
//                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    //</editor-fold>
//                    
//                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
//                        if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.certificateSN))) {
//                            CertificateInfo[][] certInfo = new CertificateInfo[1][];
//                            int[] pRESPONSE_CODE = new int[1];
//                            db.S_BO_API_CERTIFICATION_GET_INFO("", "", "", "", EscapeUtils.CheckTextNull(raServiceReq.certificateSN),
//                                    0, "", "", raServiceReq.language, pRESPONSE_CODE, certInfo, "", "", "");
//                            if (certInfo[0].length > 0) {
//                                int pCERTIFICATION_ID = certInfo[0][0].certificateID;
//                                if (pCERTIFICATION_ID > 0) {
//                                    FILE_MANAGER[][] fileManager = new FILE_MANAGER[1][];
//                                    db.S_BO_API_FILE_MANAGER_BY_CERTIFICATION_ID(pCERTIFICATION_ID, EscapeUtils.CheckTextNull(raServiceReq.fileTypeCode),
//                                            raServiceReq.language, fileManager);
//                                    if (fileManager[0].length > 0) {
//                                        FileManagerInfo[][] response = new FileManagerInfo[1][];
//                                        ArrayList<FileManagerInfo> tempList = new ArrayList<>();
//                                        for (FILE_MANAGER fileTemp : fileManager[0]) {
//                                            String sJRBConfig = EscapeUtils.CheckTextNull(fileTemp.DMS_PROPERTIES);
//                                            String sUUID = EscapeUtils.CheckTextNull(fileTemp.UUID);
//                                            InputStream inStream = CommonFunction.getStreamFromServerFile(sUUID, sJRBConfig);
//                                            FileManagerInfo mh = new FileManagerInfo();
//                                            mh.fileTypeCode = EscapeUtils.CheckTextNull(fileTemp.FILE_PROFILE_NAME);
//                                            mh.fileTypeName = EscapeUtils.CheckTextNull(fileTemp.FILE_PROFILE_DESC);
//                                            mh.fileName = EscapeUtils.CheckTextNull(fileTemp.FILE_NAME);
//                                            mh.fileByte = IOUtils.toByteArray(inStream);
//                                            tempList.add(mh);
//                                        }
//                                        response[0] = new FileManagerInfo[tempList.size()];
//                                        response[0] = tempList.toArray(response[0]);
//                                        raServiceResp.fileManagerInfo = response[0];
//                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
//                                    } else {
//                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_NO_DATA;
//                                    }
//                                } else {
//                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_NO_DATA;
//                                }
//                            } else {
//                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_NO_DATA;
//                            }
//                        } else {
//                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_NO_DATA;
//                        }
//                    }
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                // get response code return
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="### 16 registerCertificateForTMSRA">
    /*
    * SOFT: issue cert p12/truyen csr issue cert
    * HARD: AC and inhouse, approve ca.
    * SIM: AC and inhouse, call rssp create owner, agreement
    * ESIGNCLOUD: Inhouse true -> truyen csr and issue return cert; else AC -> approve ca, send AC (call rssp create owner, agreement))
    * truyen promotionDuration, prefix Vietnamese
    */
    @WebMethod(operationName = "registerCertificateForTMSRA")
    public RAServiceResp registerCertificateForTMSRA(@WebParam(name = "registerCertificateForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            boolean autoApproveCAServer = false;
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES = "";
            String sCERT_PROFILE_PROPERTIES = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sISSUE_P12_ENABLED = "";
            String pApproveCAUser = "";
            GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
            db.S_BO_GENERAL_POLICY_LIST(String.valueOf(raServiceReq.language), rsPolicy);
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
                raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sISSUE_P12_ENABLED = rsBranch[0][0].ISSUE_P12_ENABLED;
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.registerCertificateProcess(sCERT_PROFILE_PROPERTIES, sCERT_POLICY_PROPERTIES, pApproveCAUser,
                        autoApproveCAServer, System_Log_ID, System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, sISSUE_P12_ENABLED, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_RSSP_REST_RETURN_ERROR) {
                        raServiceResp.responseMessage = rsResponseCode[0][0].REMARK + ": " + raServiceResp.responseMessage;
                    } else {
                        raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                    }
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 16-2 registerCertificate2ForTMSRA">
    /*
    * SOFT: issue cert p12/truyen csr issue cert
    * HARD: AC and inhouse, approve ca.
    * ESIGNCLOUD: truyen csr and issue return cert, no inhouse
    * SIM: no SIM
    * truyen expirationTime, prefix English
    */
    @WebMethod(operationName = "registerCertificate2ForTMSRA")
    public RAServiceResp registerCertificate2ForTMSRA(@WebParam(name = "registerCertificate2ForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            boolean autoApproveCAServer = false;
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES = "";
            String sCERT_PROFILE_PROPERTIES = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pApproveCAUser = "";
            GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
            db.S_BO_GENERAL_POLICY_LIST(String.valueOf(raServiceReq.language), rsPolicy);
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
                raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.registerCertificate2Process(sCERT_PROFILE_PROPERTIES, sCERT_POLICY_PROPERTIES, pApproveCAUser,
                        autoApproveCAServer, System_Log_ID, System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                    raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="### 17 renewCertificateForTMSRA">
    /*
    * prefix Vietnamese
    */
    @WebMethod(operationName = "renewCertificateForTMSRA")
    public RAServiceResp renewCertificateForTMSRA(@WebParam(name = "renewCertificateForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            boolean autoApproveCAServer = false;
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES = "";
            String sCERT_PROFILE_PROPERTIES = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            String pBeneficiaryUserDefault = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.renewCertificateProcess(sCERT_PROFILE_PROPERTIES, sCERT_POLICY_PROPERTIES, pPARENT_ID,
                        pBRANCH_ID, pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 17 renewCertificate2ForTMSRA">
    /*
    * prefix English
    */
    @WebMethod(operationName = "renewCertificate2ForTMSRA")
    public RAServiceResp renewCertificate2ForTMSRA(@WebParam(name = "renewCertificate2ForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            boolean autoApproveCAServer = false;
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES = "";
            String sCERT_PROFILE_PROPERTIES = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            String pBeneficiaryUserDefault = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.renewCertificate2Process(sCERT_PROFILE_PROPERTIES, sCERT_POLICY_PROPERTIES, pPARENT_ID,
                        pBRANCH_ID, pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="### 18 changeCertificateInfoForTMSRA">
    /*
    * prefix Vietnamese
    */
    @WebMethod(operationName = "changeCertificateInfoForTMSRA")
    public RAServiceResp changeCertificateInfoForTMSRA(@WebParam(name = "changeCertificateInfoForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            boolean autoApproveCAServer = false;
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES = "";
            String sCERT_PROFILE_PROPERTIES = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            String pBeneficiaryUserDefault = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.changeCertificateProcess(sCERT_PROFILE_PROPERTIES, sCERT_POLICY_PROPERTIES, pPARENT_ID,
                        pBRANCH_ID, pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 18 changeCertificateInfo2ForTMSRA">
    /*
    * prefix English
    */
    @WebMethod(operationName = "changeCertificateInfo2ForTMSRA")
    public RAServiceResp changeCertificateInfo2ForTMSRA(@WebParam(name = "changeCertificateInfo2ForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            boolean autoApproveCAServer = false;
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES = "";
            String sCERT_PROFILE_PROPERTIES = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            String pBeneficiaryUserDefault = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.changeCertificate2Process(sCERT_PROFILE_PROPERTIES, sCERT_POLICY_PROPERTIES, pPARENT_ID,
                        pBRANCH_ID, pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="### 19 revokeCertificateForTMSRA">
    @WebMethod(operationName = "revokeCertificateForTMSRA")
    public RAServiceResp revokeCertificateForTMSRA(@WebParam(name = "revokeCertificateForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            boolean autoApproveCAServer = false;
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            String pBeneficiaryUserDefault = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.revokeCertificateProcess(pPARENT_ID, pBRANCH_ID, pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 20 suspendCertificateForTMSRA">
    @WebMethod(operationName = "suspendCertificateForTMSRA")
    public RAServiceResp suspendCertificateForTMSRA(@WebParam(name = "suspendCertificateForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            boolean autoApproveCAServer = false;
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            String pBeneficiaryUserDefault = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.suspendCertificateProcess(pPARENT_ID, pBRANCH_ID, pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 21 recoveryCertificateForTMSRA">
    @WebMethod(operationName = "recoveryCertificateForTMSRA")
    public RAServiceResp recoveryCertificateForTMSRA(@WebParam(name = "recoveryCertificateForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            boolean autoApproveCAServer = false;
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            String pBeneficiaryUserDefault = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.recoveryCertificateProcess(pPARENT_ID, pBRANCH_ID, pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="### 22 declineCertificateForTMSRA">
    @WebMethod(operationName = "declineCertificateForTMSRA")
    public RAServiceResp declineCertificateForTMSRA(@WebParam(name = "declineCertificateForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser;
            String pBeneficiaryUserDefault = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.declineCertificateProcess(pPARENT_ID, pBRANCH_ID, pBeneficiaryUserDefault, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="### 23 approveCertificateForTMSRA">
    @WebMethod(operationName = "approveCertificateForTMSRA")
    public RAServiceResp approveCertificateForTMSRA(@WebParam(name = "approveCertificateForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        boolean autoApproveCAServer = false;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            String pBeneficiaryUserDefault = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.approveCertificateProcess(sCERT_POLICY_PROPERTIES, pPARENT_ID, pBRANCH_ID,
                        pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 24 activationCertificateForTMSRA">
    @WebMethod(operationName = "activationCertificateForTMSRA")
    public RAServiceResp activationCertificateForTMSRA(@WebParam(name = "activationCertificateForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        boolean autoApproveCAServer = false;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            String pBeneficiaryUserDefault = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.activationCertificateProcess(sCERT_POLICY_PROPERTIES, pPARENT_ID, pBRANCH_ID,
                        pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="### 24 reissueCertificateForTMSRA">
    @WebMethod(operationName = "reissueCertificateForTMSRA")
    public RAServiceResp reissueCertificateForTMSRA(@WebParam(name = "reissueCertificateForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            boolean autoApproveCAServer = false;
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            String pBeneficiaryUserDefault = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.reissueCertificateProcess(sCERT_POLICY_PROPERTIES, pPARENT_ID, pBRANCH_ID,
                        pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="### 25 reportCertificateNEACForTMSRA">
    @WebMethod(operationName = "reportCertificateNEACForTMSRA")
    public RAServiceResp reportCertificateNEACForTMSRA(@WebParam(name = "reportCertificateNEACForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        String pBeneficiaryUserDefault = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        ObjectMapper objectMapper;
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sCERT_POLICY_PROPERTIES;
            String sIP_Request = getIPAddress();
            String pPARENT_ID = "";
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.reportCertificateNEACProcess(pPARENT_ID, pBeneficiaryUserDefault, System_Log_ID,
                        System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="### 26 reportCertificateListNEACForTMSRA">
    @WebMethod(operationName = "reportCertificateListNEACForTMSRA")
    public RAServiceResp reportCertificateListNEACForTMSRA(@WebParam(name = "reportCertificateListNEACForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        String pBeneficiaryUserDefault = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        ObjectMapper objectMapper;
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sCERT_POLICY_PROPERTIES;
            String sIP_Request = getIPAddress();
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));		
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.reportCertificateListNEACProcess(pPARENT_ID, pBeneficiaryUserDefault, pApproveCAUser,
                        System_Log_ID, System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                if (System_Log_ID[0] != 0) {
                    RAServiceResp raServiceRespLog = new RAServiceResp();
                    raServiceRespLog.responseCode = raServiceResp.responseCode;
                    raServiceRespLog.responseMessage = raServiceResp.responseMessage;
                    raServiceRespLog.countCertificateNEACReportInfo = raServiceResp.certificateNEACReportInfo.length;
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceRespLog.responseCode),
                        objectMapper.writeValueAsString(raServiceRespLog), sTOKEN_SN, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 27 reportCertificateForTMSRA">
    @WebMethod(operationName = "reportCertificateForTMSRA")
    public RAServiceResp reportCertificateForTMSRA(@WebParam(name = "reportCertificateForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        String pBeneficiaryUserDefault = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        ObjectMapper objectMapper;
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sIP_Request = getIPAddress();
            String pPARENT_ID = "";
            String pBRANCH_ID = "";
            String sCERT_POLICY_PROPERTIES;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = String.valueOf(rsBranch[0][0].ID);
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.reportCertificateProcess(pPARENT_ID, pBRANCH_ID, pBeneficiaryUserDefault, System_Log_ID,
                        System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                // get response code return
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                if (System_Log_ID[0] != 0) {
                    RAServiceResp raServiceRespLog = new RAServiceResp();
                    raServiceRespLog.responseCode = raServiceResp.responseCode;
                    raServiceRespLog.responseMessage = raServiceResp.responseMessage;
                    raServiceRespLog.countCertificateReportInfo = raServiceResp.certificateReportInfo.length;
                    //CertificateReportInfo[][] certReportLog = new CertificateReportInfo[1][];
                    //CommonFunction.certReportTruncatedForSystemLog(raServiceResp.certificateReportInfo, certReportLog);
                    //raServiceRespLog.certificateReportInfo = certReportLog[0];
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceRespLog.responseCode),
                        objectMapper.writeValueAsString(raServiceRespLog), sTOKEN_SN, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 28 setCertificateAttachmentForTMSRA">
    @WebMethod(operationName = "setCertificateAttachmentForTMSRA")
    public RAServiceResp setCertificateAttachmentForTMSRA(@WebParam(name = "setCertificateAttachmentForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            boolean autoApproveCAServer = false;
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.setCertificateAttachmentProcess(pPARENT_ID, pBRANCH_ID,
                        pApproveCAUser, autoApproveCAServer, System_Log_ID, System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 29 approveCertificateAttachmentForTMSRA">
    @WebMethod(operationName = "approveCertificateAttachmentForTMSRA")
    public RAServiceResp approveCertificateAttachmentForTMSRA(@WebParam(name = "approveCertificateAttachmentForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        boolean autoApproveCAServer = false;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.approveCertificateAttachmentProcess(pPARENT_ID, pBRANCH_ID,
                        pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 30 registerCertificateOwnerForTMSRA">
    @WebMethod(operationName = "registerCertificateOwnerForTMSRA")
    public RAServiceResp registerCertificateOwnerForTMSRA(@WebParam(name = "registerCertificateOwnerForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        ObjectMapper objectMapper;
        String pBeneficiaryUserDefault = "";
        String sIP_Request = getIPAddress();
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_REGISTRATION_CERTIFICATION_OWNER;
        try {
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pApproveCAUser = "";
            boolean autoApproveCAServer = false;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                        || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        if("".equals(pBeneficiaryUserDefault))
                        {
                            pBeneficiaryUserDefault = pApproveCAUser;
                        }
                        raServiceReq.beneficiaryUser = pBeneficiaryUserDefault;
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.registerCertificateOwnerProcess(pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    RAServiceResp raServiceRespLog = new RAServiceResp();
                    raServiceRespLog.responseCode = raServiceResp.responseCode;
                    raServiceRespLog.responseMessage = raServiceResp.responseMessage;
                    CertificateInfo[][] certInfoLog = new CertificateInfo[1][];
                    CommonFunction.certListTruncatedForSystemLog(raServiceResp.certificateInfo, certInfoLog);
                    raServiceRespLog.certificateInfo = certInfoLog[0];
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceRespLog.responseCode),
                        objectMapper.writeValueAsString(raServiceRespLog), sTOKEN_SN, pBeneficiaryUserDefault);
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 31 changeCertificateOwnerInfoForTMSRA">
    @WebMethod(operationName = "changeCertificateOwnerInfoForTMSRA")
    public RAServiceResp changeCertificateOwnerInfoForTMSRA(@WebParam(name = "changeCertificateOwnerInfoForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        ObjectMapper objectMapper;
        String pBeneficiaryUserDefault = "";
        String sIP_Request = getIPAddress();
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_CHANGE_CERTIFICATION_OWNER_INFO;
        try {
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pApproveCAUser = "";
            boolean autoApproveCAServer = false;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                        || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        if("".equals(pBeneficiaryUserDefault))
                        {
                            pBeneficiaryUserDefault = pApproveCAUser;
                        }
                        raServiceReq.beneficiaryUser = pBeneficiaryUserDefault;
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.changeCertificateOwnerInfoProcess(pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, pBeneficiaryUserDefault);
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 32 disposeCertificateOwnerForTMSRA">
    @WebMethod(operationName = "disposeCertificateOwnerForTMSRA")
    public RAServiceResp disposeCertificateOwnerForTMSRA(@WebParam(name = "disposeCertificateOwnerForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        ObjectMapper objectMapper;
        String pBeneficiaryUserDefault = "";
        String sIP_Request = getIPAddress();
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_DISPOSE_CERTIFICATION_OWNER;
        try {
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pApproveCAUser = "";
            boolean autoApproveCAServer = false;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                        || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        if("".equals(pBeneficiaryUserDefault))
                        {
                            pBeneficiaryUserDefault = pApproveCAUser;
                        }
                        raServiceReq.beneficiaryUser = pBeneficiaryUserDefault;
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.disposeCertificateOwnerProcess(pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, pBeneficiaryUserDefault);
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 33 declineCertificateOwnerForTMSRA">
    @WebMethod(operationName = "declineCertificateOwnerForTMSRA")
    public RAServiceResp declineCertificateOwnerForTMSRA(@WebParam(name = "declineCertificateOwnerForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        ObjectMapper objectMapper;
        String sIP_Request = getIPAddress();
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_DECLINE_CERTIFICATION_OWNER;
        try {
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pApproveCAUser = "";
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                        || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.declineCertificateOwnerProcess(System_Log_ID, System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, raServiceReq.approveUser);
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 34 approveCertificateOwnerForTMSRA">
    @WebMethod(operationName = "approveCertificateOwnerForTMSRA")
    public RAServiceResp approveCertificateOwnerForTMSRA(@WebParam(name = "approveCertificateOwnerForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        ObjectMapper objectMapper;
        String sIP_Request = getIPAddress();
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_APPROVAL_CERTIFICATION_OWNER;
        try {
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pApproveCAUser = "";
            boolean autoApproveCAServer = false;
//            boolean autoApproveCAClient = raServiceReq.approveEnabled;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                        || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.approveCertificateOwnerProcess(pApproveCAUser, autoApproveCAServer, System_Log_ID, System_Log_BillCode,
                        sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                    //<editor-fold defaultstate="collapsed" desc="### CLOSE OLD PROCESS">
//                    RAServiceReq raReqTemp;
//                    raReqTemp = raServiceReq;
//                    raReqTemp.fileManagerInfo = null;
//                    raReqTemp.certificateComponentInfo = null;
//                    objectMapper = new ObjectMapper();
//                    db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_API_RA, Definitions.CONFIG_LOG_SOURCE_API_RA, "", "",
//                        sFunctionWS, objectMapper.writeValueAsString(raReqTemp),
//                        raServiceReq.approveUser, System_Log_ID, sIP_Request, System_Log_BillCode);
//                    //<editor-fold defaultstate="collapsed" desc="### CHECK IP - FUNCTION ACCESS">
//                    CERTIFICATION_POLICY_DATA[][] resPolicyData;
//                    boolean checkFunctionAccessAll = CommonFunction.checkAPIAccessFunctionAll(sFUNCTIONALTITY_PROPERTIES);
//                    if (checkFunctionAccessAll == false) {
//                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_ACCESS_FUNCTION_INVALID;
//                        resPolicyData = new CERTIFICATION_POLICY_DATA[1][];
//                        CommonFunction.getFunctionAccessList(sFUNCTIONALTITY_PROPERTIES, resPolicyData);
//                        if (resPolicyData[0].length > 0) {
//                            for (CERTIFICATION_POLICY_DATA rsPolicyProperties : resPolicyData[0]) {
//                                if (sFunctionWS.equals(EscapeUtils.CheckTextNull(rsPolicyProperties.name))) {
//                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
//                        boolean checkIPAccessAll = CommonFunction.checkAPIAccessIPAll(sIP_ADDRESS_PROPERTIES);
//                        if (checkIPAccessAll == false) {
//                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_ACCESS_IP_INVALID;
//                            resPolicyData = new CERTIFICATION_POLICY_DATA[1][];
//                            CommonFunction.getIPAccessList(sIP_ADDRESS_PROPERTIES, resPolicyData);
//                            if (resPolicyData[0].length > 0) {
//                                for (CERTIFICATION_POLICY_DATA rsPolicyProperties : resPolicyData[0]) {
//                                    if (sIP_Request.equals(EscapeUtils.CheckTextNull(rsPolicyProperties.name))) {
//                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    //</editor-fold>
//                    
//                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
//                        int pMessagingQueueID = 0;
//                        String sVALUE_OLD = "";
//                        String sCOMMENT_OLD = "";
//                        String pQueueStateCode = "";
//                        String pOwnerStateCode = "";
//                        String pQueueTypeCode = "";
//                        String pOwnerTypeCode = "";
//                        //<editor-fold defaultstate="collapsed" desc="### GET CERTIFICATE OWNER">
//                        if(raServiceReq.certificateOwnerID != 0)
//                        {
//                            CertificateOwnerInfo[][] certOwnerInfo = new CertificateOwnerInfo[1][];
//                            db.S_BO_API_CERTIFICATION_OWNER_GET_INFO(raServiceReq.certificateOwnerID, "", "", "", "", "", "", "", "", raServiceReq.language, certOwnerInfo);
//                            if (certOwnerInfo[0].length > 0) {
//                                pQueueStateCode = certOwnerInfo[0][0].messagingQueueStateCode;
//                                pQueueTypeCode = certOwnerInfo[0][0].messagingQueueTypeCode;
//                                pOwnerStateCode = certOwnerInfo[0][0].certificateOwnerStateCode;
//                                pMessagingQueueID = certOwnerInfo[0][0].messagingQueueID;
//                                pOwnerTypeCode = certOwnerInfo[0][0].certificateOwnerTypeCode;
//                                sVALUE_OLD = certOwnerInfo[0][0].value;
//                                sCOMMENT_OLD = certOwnerInfo[0][0].comment;
//                                if(pQueueTypeCode.equals(Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_CHANGE_OWNER_INFO)
//                                    || pQueueTypeCode.equals(Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_CODE_DISPOSE_OWNER))
//                                {
//                                    if(!pOwnerStateCode.equals(Definitions.CONFIG_CERTIFICATION_OWNER_STATE_CODE_OPERATED))
//                                    {
//                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_OWNER_STATE_INVALID;
//                                    }
//                                }
//                            } else {
//                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_OWNER_REQUEST_INVALID;
//                            }
//                        } else {
//                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_OWNER_REQUEST_INVALID;
//                        }
//                        //</editor-fold>
//                        
//                        if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS)
//                        {
//                            //<editor-fold defaultstate="collapsed" desc="### GET FULLNAME">
//                            String raFullname = "";
//                            UserInfo[][] userDetail = new UserInfo[1][];
//                            db.S_BO_API_USER_DETAIL(pApproveCAUser, raServiceReq.language, userDetail);
//                            if (userDetail[0].length > 0) {
//                                raFullname = EscapeUtils.CheckTextNull(userDetail[0][0].fullName);
//                            }
//                            //</editor-fold>
//
//                            //<editor-fold defaultstate="collapsed" desc="### LEVEL APPROVE">
//                            // intLevelApprove - new : 0, agency approve : 1, CA approve : 2
//                            int intLevelApprove = 0;
//                            if (autoApproveCAServer == false && autoApproveCAClient == false) {
//                                intLevelApprove = 0;
//                            } else if (autoApproveCAServer == false && autoApproveCAClient == true) {
//                                intLevelApprove = 1;
//                            } else if (autoApproveCAServer == true && autoApproveCAClient == false) {
//                                intLevelApprove = 0;
//                            } else if (autoApproveCAServer == true && autoApproveCAClient == true) {
//                                intLevelApprove = 2;
//                            }
//                            //</editor-fold>
//                            switch (intLevelApprove) {
//                                case 1:
//                                    if(!pQueueStateCode.equals(Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_INITIALIZED)
//                                            && !pQueueStateCode.equals(Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PENDING))
//                                    {
//                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_OWNER_STATE_INVALID;
//                                    }   break;
//                                case 2:
//                                    if(!pQueueStateCode.equals(Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_INITIALIZED)
//                                            && !pQueueStateCode.equals(Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PRE_APPROVED)
//                                            && !pQueueStateCode.equals(Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PENDING))
//                                    {
//                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_OWNER_STATE_INVALID;
//                                    }   break;
//                                default:
//                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_OWNER_REQUEST_INVALID;
//                                    break;
//                            }
//                            if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS)
//                            {
//                                raServiceResp.certificateOwnerID = raServiceReq.certificateOwnerID;
//                                raServiceResp.certificateOwnerStateCode = pOwnerStateCode;
//                                CERTIFICATION_OWNER_DATA_ATTR tempLogReq;
//                                if(!"".equals(sVALUE_OLD)) {
//                                    tempLogReq = objectMapper.readValue(sVALUE_OLD, CERTIFICATION_OWNER_DATA_ATTR.class);
//                                } else {
//                                    tempLogReq = new CERTIFICATION_OWNER_DATA_ATTR();
//                                }
//                                //<editor-fold defaultstate="collapsed" desc="### Approve request -> PRE_APPROVED, APPROVED">
//                                String sOwnerDisposeReasonOld = "";
//                                if(!"".equals(sCOMMENT_OLD))
//                                {
//                                    try {
//                                        CERTIFICATION_OWNER_COMMENT jsonCertCommentOld = objectMapper.readValue(sCOMMENT_OLD, CERTIFICATION_OWNER_COMMENT.class);
//                                        sOwnerDisposeReasonOld = jsonCertCommentOld.ownerDisposeReason;
//                                    } catch(IOException e) {}
//                                }
//                                CERTIFICATION_OWNER_COMMENT jsonCertComment = new CERTIFICATION_OWNER_COMMENT();
//                                jsonCertComment.ownerDisposeReason = sOwnerDisposeReasonOld;
//                                jsonCertComment.ownerApproveRemark = EscapeUtils.CheckTextNull(raServiceReq.remark);
//                                if (intLevelApprove == 1 || intLevelApprove == 2) {
//                                    tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PRE_APPROVED;
//                                    tempLogReq.approveUser = raFullname + " (" + raServiceReq.approveUser + ")";
//                                    tempLogReq.approveDt =new Date();
//                                    String sPRE_APPROVED = db.S_BO_API_CERTIFICATION_OWNER_PRE_APPROVED(pMessagingQueueID,
//                                        objectMapper.writeValueAsString(tempLogReq), raServiceReq.approveUser);
//                                    if("0".equals(sPRE_APPROVED)) {
//                                        raServiceResp.certificateStateCode = Definitions.CONFIG_CERTIFICATION_STATE_CODE_NEW;
//                                    } else {
//                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERT_ERROR_APPROVE;
//                                    }
//                                }
//                                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
//                                    if (intLevelApprove == 2) {
//                                        tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_APPROVED;
//                                        tempLogReq.approveCAUser = raFullname + " (" + raServiceReq.approveUser + ")";
//                                        tempLogReq.approveCADt = new Date();
//                                        String sApprove = db.S_BO_API_CERTIFICATION_OWNER_APPROVED(pMessagingQueueID, objectMapper.writeValueAsString(tempLogReq), raServiceReq.approveUser);
//                                        if ("0".equals(sApprove)) {
//                                            if(pQueueTypeCode.equals(Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_CODE_REGISTRATION_OWNER))
//                                            {
//                                                db.S_BO_API_CERTIFICATION_OWNER_REGISTRATION(pMessagingQueueID, objectMapper.writeValueAsString(jsonCertComment), raServiceReq.approveUser);
//                                                raServiceResp.certificateOwnerStateCode = Definitions.CONFIG_CERTIFICATION_OWNER_STATE_CODE_OPERATED;
//                                            } else if(pQueueTypeCode.equals(Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_CHANGE_OWNER_INFO))
//                                            {
//                                                String sPersonalNameChange = "";
//                                                String sCompanyNameChange = "";
//                                                String sAddressChange = "";
//                                                String sPhoneChange = "";
//                                                String sEmailChange = "";
//                                                String sRepresentativeChange = "";
//                                                String sRepresentativePositionChange = "";
//                                                try{
//                                                    sPersonalNameChange = tempLogReq.personalName;
//                                                    sCompanyNameChange = tempLogReq.companyName;
//                                                    sAddressChange = tempLogReq.address;
//                                                    sPhoneChange = tempLogReq.phoneContract;
//                                                    sEmailChange = tempLogReq.emailContract;
//                                                    if(pOwnerTypeCode.equals(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_CODE_ENTERPRISE_GOV)
//                                                        || pOwnerTypeCode.equals(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_CODE_PERSONAL_GOV))
//                                                    {
//                                                        sEmailChange = "";
//                                                    }
//                                                    sRepresentativeChange = tempLogReq.representative;
//                                                    sRepresentativePositionChange = tempLogReq.representativePosition;
//                                                } catch (Exception e) {}
//                                                db.S_BO_API_CERTIFICATION_OWNER_CHANGE_INFO(pMessagingQueueID, sAddressChange, sPersonalNameChange,
//                                                    sCompanyNameChange, sPhoneChange, sEmailChange, objectMapper.writeValueAsString(jsonCertComment),
//                                                    sRepresentativeChange, sRepresentativePositionChange, raServiceReq.approveUser);
//                                            } else if(pQueueTypeCode.equals(Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_CODE_DISPOSE_OWNER))
//                                            {
//                                                String sReasonDispose = EscapeUtils.CheckTextNull(tempLogReq.ownerDisposeReason);
//                                                jsonCertComment.ownerDeclineReason = sReasonDispose;
//                                                db.S_BO_API_CERTIFICATION_OWNER_DISPOSE(pMessagingQueueID, objectMapper.writeValueAsString(jsonCertComment), raServiceReq.approveUser);
//                                                raServiceResp.certificateOwnerStateCode = Definitions.CONFIG_CERTIFICATION_OWNER_STATE_CODE_DISPOSED;
//                                            }
//                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
//                                        } else {
//                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERT_ERROR_APPROVE;
//                                        }
//                                    }
//                                }
//                                //</editor-fold>
//                            }
//                        }
//                    }
                    //</editor-fold>
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                // get response code return
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, raServiceReq.approveUser);
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 35 setCertificateOwnerAttachmentForTMSRA">
    @WebMethod(operationName = "setCertificateOwnerAttachmentForTMSRA")
    public RAServiceResp setCertificateOwnerAttachmentForTMSRA(@WebParam(name = "setCertificateOwnerAttachmentForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
//        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_SET_CERTIFICATION_OWNER_ATTACHMENT;
        try {
            sIP_Request = getIPAddress();
            boolean autoApproveCAServer = false;
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pApproveCAUser = "";
            String pBeneficiaryUserDefault = "";
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
//                    pBRANCH_ID = rsBranch[0][0].ID;
//                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        if("".equals(pBeneficiaryUserDefault))
                        {
                            pBeneficiaryUserDefault = pApproveCAUser;
                        }
                        raServiceReq.beneficiaryUser = pBeneficiaryUserDefault;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.setCertificateOwnerAttachmentProcess(pApproveCAUser, autoApproveCAServer, log, System_Log_ID,
                        System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                    //<editor-fold defaultstate="collapsed" desc="### CLOSE OLD PROCESS">
//                    RAServiceReq raReqTemp = new RAServiceReq();
//                    raReqTemp.certificateSN = raServiceReq.certificateSN;
//                    raReqTemp.approveEnabled = raServiceReq.approveEnabled;
//                    raReqTemp.language = raServiceReq.language;
//                    raReqTemp.credentialData = raServiceReq.credentialData;
//                    objectMapper = new ObjectMapper();
//                    db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_API_RA, Definitions.CONFIG_LOG_SOURCE_API_RA, "", "",
//                        sFunctionWS, objectMapper.writeValueAsString(raReqTemp), raServiceReq.approveUser, System_Log_ID, sIP_Request, System_Log_BillCode);
//                    //<editor-fold defaultstate="collapsed" desc="### CHECK IP - FUNCTION ACCESS">
//                    CERTIFICATION_POLICY_DATA[][] resPolicyData;
//                    boolean checkFunctionAccessAll = CommonFunction.checkAPIAccessFunctionAll(sFUNCTIONALTITY_PROPERTIES);
//                    if (checkFunctionAccessAll == false) {
//                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_ACCESS_FUNCTION_INVALID;
//                        resPolicyData = new CERTIFICATION_POLICY_DATA[1][];
//                        CommonFunction.getFunctionAccessList(sFUNCTIONALTITY_PROPERTIES, resPolicyData);
//                        if (resPolicyData[0].length > 0) {
//                            for (CERTIFICATION_POLICY_DATA rsPolicyProperties : resPolicyData[0]) {
//                                if (sFunctionWS.equals(EscapeUtils.CheckTextNull(rsPolicyProperties.name))) {
//                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
//                        boolean checkIPAccessAll = CommonFunction.checkAPIAccessIPAll(sIP_ADDRESS_PROPERTIES);
//                        if (checkIPAccessAll == false) {
//                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_ACCESS_IP_INVALID;
//                            resPolicyData = new CERTIFICATION_POLICY_DATA[1][];
//                            CommonFunction.getIPAccessList(sIP_ADDRESS_PROPERTIES, resPolicyData);
//                            if (resPolicyData[0].length > 0) {
//                                for (CERTIFICATION_POLICY_DATA rsPolicyProperties : resPolicyData[0]) {
//                                    if (sIP_Request.equals(EscapeUtils.CheckTextNull(rsPolicyProperties.name))) {
//                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    //</editor-fold>
//                    
//                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
//                        //<editor-fold defaultstate="collapsed" desc="### GET CERTIFICATE OWNER">
//                        String pCERTIFICATION_ATTR_TYPE_CODE = Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_CODE_SUPPLEMENT_FILE;
//                        CertificateOwnerInfo[][] rsReq = new CertificateOwnerInfo[1][];
//                        if(raServiceReq.certificateOwnerID != 0)
//                        {
//                            db.S_BO_API_CERTIFICATION_OWNER_GET_INFO(raServiceReq.certificateOwnerID, "", "", "", "",
//                                "", "", "", "", raServiceReq.language, rsReq);
//                            if (rsReq[0].length > 0) {
//                                if(!rsReq[0][0].certificateOwnerStateCode.equals(Definitions.CONFIG_CERTIFICATION_OWNER_STATE_CODE_OPERATED)) {
//                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_OWNER_STATE_INVALID;
//                                }
//                            } else {
//                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_REQUEST_INVALID;
//                            }
//                        } else {
//                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_REQUEST_INVALID;
//                        }
//                        //</editor-fold>
//
//                        if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
//                            //<editor-fold defaultstate="collapsed" desc="### File manager Modules">
//                            if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS)
//                            {
//                                if (raServiceReq.fileManagerInfo != null) {
//                                    if (raServiceReq.fileManagerInfo.length <= 0) {
//                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CLIENT_COMPONENT_FILE_EMPTY;
//                                    }
//                                } else {
//                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CLIENT_COMPONENT_FILE_EMPTY;
//                                }
//                            }
//                            //</editor-fold>
//
//                            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
//                                //<editor-fold defaultstate="collapsed" desc="### LEVEL APPROVE">
//                                // intLevelApprove - new : 0, agency approve : 1, CA approve : 2
//                                int intLevelApprove = 0;
//                                if (autoApproveCAServer == false && autoApproveCAClient == false) {
//                                    intLevelApprove = 0;
//                                } else if (autoApproveCAServer == false && autoApproveCAClient == true) {
//                                    intLevelApprove = 1;
//                                } else if (autoApproveCAServer == true && autoApproveCAClient == false) {
//                                    intLevelApprove = 0;
//                                } else if (autoApproveCAServer == true && autoApproveCAClient == true) {
//                                    intLevelApprove = 2;
//                                }
//                                //</editor-fold>
//
//                                //<editor-fold defaultstate="collapsed" desc="### GET FULLNAME">
//                                String raFullnameCreate = "";
//                                String raFullname = "";
//                                UserInfo[][] userInfo = new UserInfo[1][];
//                                db.S_BO_API_USER_LIST(EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser), "", "", raServiceReq.language, userInfo);
//                                if (userInfo[0].length > 0) {
//                                    raFullnameCreate = EscapeUtils.CheckTextNull(userInfo[0][0].fullName);
//                                }
//                                UserInfo[][] userDetail = new UserInfo[1][];
//                                db.S_BO_API_USER_DETAIL(pApproveCAUser, raServiceReq.language, userDetail);
//                                if (userDetail[0].length > 0) {
//                                    raFullname = EscapeUtils.CheckTextNull(userDetail[0][0].fullName);
//                                }
//                                //</editor-fold>
//
//                                //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR ">
//                                CERTIFICATION_OWNER_DATA_ATTR tempLogReq = new CERTIFICATION_OWNER_DATA_ATTR();
//                                tempLogReq.typeName = pCERTIFICATION_ATTR_TYPE_CODE;
//                                tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PENDING;
//                                tempLogReq.createUser = raFullnameCreate + " (" + raServiceReq.beneficiaryUser + ")";
//                                tempLogReq.createDt = new Date();
//                                //</editor-fold>
//
//                                int pMESSAGING_QUEUE_STATE_ID = Definitions.CONFIG_MESSAGING_QUEUE_STATE_ID_PENDING;
//                                int pMESSAGING_QUEUE_FUNCTION_ID = Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_ID_SUPPLEMENT_FILE;
//                                int[] pOWNER_ATTR_ID = new int[1];
//                                String[] pRESPONSE_CODE = new String[1];
//                                db.S_BO_API_CERTIFICATION_OWNER_INSERT_MESSAGING_QUEUE(raServiceReq.certificateOwnerID, pMESSAGING_QUEUE_STATE_ID,
//                                    pMESSAGING_QUEUE_FUNCTION_ID, objectMapper.writeValueAsString(tempLogReq),"", raServiceReq.beneficiaryUser,
//                                    pRESPONSE_CODE, pOWNER_ATTR_ID);
//                                if("0".equals(pRESPONSE_CODE[0]))
//                                {
//                                    raServiceResp.certificateOwnerID = raServiceReq.certificateOwnerID;
//                                    String[] pRESPONSE_CODE_NAME = new String[1];
//                                    GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
//                                    db.S_BO_GENERAL_POLICY_LIST(String.valueOf(raServiceReq.language), rsPolicy);
//                                    //<editor-fold defaultstate="collapsed" desc="### File Attach: Check Add to JRB">
//                                    String sJRBConfig = "";
//                                    if (rsPolicy[0].length > 0) {
//                                        for (GENERAL_POLICY rsPolicy1 : rsPolicy[0]) {
//                                            if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT)) {
//                                                sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
//                                                break;
//                                            }
//                                        }
//                                    }
//                                    int[] pFILE_MANAGER_ID = new int[1];
//                                    if (!"".equals(sJRBConfig)) {
//                                        String sJRB_Source = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_SOURCE);
//                                        if (sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_EFY)) {
//                                            String sIP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_IP);
//                                            String sHTTP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PROTOCOL);
//                                            String sCONTEXT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_CONTEXT);
//                                            String sPORT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PORT);
//                                            String sDEFAULT_USER = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERNAME);
//                                            String sDEFAULT_PASS = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PASSWORD);
//                                            String sOWNERCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_OWNERCODE);
//                                            String sAPPCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_APPCODE);
//                                            String sFUNCTION_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_FUNCTION_UP);
//                                            String idUUID_Temp = Definitions.CONFIG_JACK_RABBIT_UUID_SAMPLE;
//                                            for (FileManagerInfo fileManagerInsert : raServiceReq.fileManagerInfo) {
//                                                String sFileData = new String(Base64.encodeBase64(fileManagerInsert.fileByte), "UTF-8"); //EscapeUtils.CheckTextNull(mhIP.FILE_URL);//CommonFunction.encodeFileToBase64Binary(fileUp);
//                                                CloseableHttpResponse pHttpRes = ConnectFileToPartner.upFileParner(sIP_CONNECT, sHTTP_CONNECT,
//                                                        sCONTEXT_CONNECT, Integer.parseInt(sPORT_CONNECT), sDEFAULT_USER,
//                                                        sDEFAULT_PASS, sOWNERCODE_CONNECT, sAPPCODE_CONNECT, sFUNCTION_CONNECT, idUUID_Temp,
//                                                        fileManagerInsert.fileName, sFileData);
//                                                InputStream isStr = pHttpRes.getEntity().getContent();
//                                                String resultUUID = IOUtils.toString(isStr);
//                                                CommonFunction.LogDebugString(log, sTOKEN_SN + " - FILE - " + sJRB_Source + " - sUUID", resultUUID);
//                                                String sMimeType = EscapeUtils.CheckTextNull(fileManagerInsert.mimeType);
//                                                db.S_BO_API_CERTIFICATION_OWNER_INSERT_SUPPLEMENT_FILE(raServiceReq.certificateOwnerID,
//                                                    Definitions.CONFIG_FILE_PROFILE_CERTIFICATION_OWNER_FILE,
//                                                    resultUUID, sJRBConfig, sMimeType, fileManagerInsert.fileName, fileManagerInsert.fileByte.length,
//                                                    raServiceReq.beneficiaryUser, pRESPONSE_CODE_NAME, pFILE_MANAGER_ID);
//                                                if(!"0".equals(pRESPONSE_CODE_NAME[0]))
//                                                {
//                                                    raServiceResp.responseCode = Integer.parseInt(pRESPONSE_CODE_NAME[0]);
//                                                    break;
//                                                }
//                                            }
//                                        } else if (sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_JRB)) {
//                                            String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
//                                            String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
//                                            String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
//                                            String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
//                                            String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
//                                            String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
//                                            String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
//                                            for (FileManagerInfo fileManagerInsert : raServiceReq.fileManagerInfo) {
//                                                String sMimeType = EscapeUtils.CheckTextNull(fileManagerInsert.mimeType);
//                                                JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
//                                                        Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
//                                                InputStream isFILE_STREAM = new ByteArrayInputStream(fileManagerInsert.fileByte);
//                                                JCRFile jrbFile = JackRabbitCommon.uploadFile(jcrConfig, fileManagerInsert.fileName, sMimeType, isFILE_STREAM);
//                                                CommonFunction.LogDebugString(log, sTOKEN_SN + " - FILE - " + sJRB_Source + " - sUUID", jrbFile.getUuid());
//                                                db.S_BO_API_CERTIFICATION_OWNER_INSERT_SUPPLEMENT_FILE(raServiceReq.certificateOwnerID,
//                                                    Definitions.CONFIG_FILE_PROFILE_CERTIFICATION_OWNER_FILE,
//                                                    jrbFile.getUuid(), sJRBConfig, sMimeType, fileManagerInsert.fileName, fileManagerInsert.fileByte.length,
//                                                    raServiceReq.beneficiaryUser, pRESPONSE_CODE_NAME, pFILE_MANAGER_ID);
//                                                if(!"0".equals(pRESPONSE_CODE_NAME[0]))
//                                                {
//                                                    raServiceResp.responseCode = Integer.parseInt(pRESPONSE_CODE_NAME[0]);
//                                                    break;
//                                                }
//                                            }
//                                        } else if (sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_MID)) {
//                                            String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
//                                            String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
//                                            String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
//                                            String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
//                                            String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
//                                            String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
//                                            String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
//                                            for (FileManagerInfo fileManagerInsert : raServiceReq.fileManagerInfo) {
//                                                String sMimeType = EscapeUtils.CheckTextNull(fileManagerInsert.mimeType);
//                                                InputStream isFILE_STREAM = new ByteArrayInputStream(fileManagerInsert.fileByte);
//                                                ConnectJackRabbitNew openJRB = new ConnectJackRabbitNew(sJRB_Host, sJRB_UserID, sJRB_UserPass,
//                                                    Integer.parseInt(sJRB_MaxSession), Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
//                                                String[] sReturnJRB = new String[2];
//                                                vn.mobileid.fms.client.JCRFile jrbFile = openJRB.uploadFile(EscapeUtils.CheckTextNull(fileManagerInsert.fileName),
//                                                    EscapeUtils.CheckTextNull(sMimeType), isFILE_STREAM, sReturnJRB);
//                                                CommonFunction.LogDebugString(log, sTOKEN_SN + " - FILE - " + sJRB_Source + " - sUUID", sReturnJRB[0].trim());
//                                                db.S_BO_API_CERTIFICATION_OWNER_INSERT_SUPPLEMENT_FILE(raServiceReq.certificateOwnerID,
//                                                    Definitions.CONFIG_FILE_PROFILE_CERTIFICATION_OWNER_FILE,
//                                                    sReturnJRB[0].trim(), sJRBConfig, sMimeType, sReturnJRB[1].trim(), fileManagerInsert.fileByte.length,
//                                                    raServiceReq.beneficiaryUser, pRESPONSE_CODE_NAME, pFILE_MANAGER_ID);
//                                                if(!"0".equals(pRESPONSE_CODE_NAME[0]))
//                                                {
//                                                    raServiceResp.responseCode = Integer.parseInt(pRESPONSE_CODE_NAME[0]);
//                                                    break;
//                                                }
//                                            }
//                                        } else {
//                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_FILE_NOSUPPORT;
//                                        }
//                                    } else {
//                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_FILE_NOSUPPORT;
//                                    }
//                                    //</editor-fold>
//
//                                    //<editor-fold defaultstate="collapsed" desc="### Approve request -> PRE_APPROVED, APPROVED">
//                                    if (intLevelApprove == 1 || intLevelApprove == 2) {
//                                        tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PRE_APPROVED;
//                                        tempLogReq.approveUser = raFullname + " (" + raServiceReq.approveUser + ")";
//                                        tempLogReq.approveDt = new Date();
//                                        String sPRE_APPROVED = db.S_BO_API_CERTIFICATION_OWNER_PRE_APPROVED(pOWNER_ATTR_ID[0], objectMapper.writeValueAsString(tempLogReq), EscapeUtils.CheckTextNull(raServiceReq.approveUser));
//                                        if("0".equals(sPRE_APPROVED)) {
//                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
//                                        } else if("99".equals(sPRE_APPROVED)) {
//                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
//                                        } else {
//                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERT_ERROR_APPROVE;
//                                        }
//                                    }
//                                    if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
//                                        if (intLevelApprove == 2) {
//                                            tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_APPROVED;
//                                            tempLogReq.approveCAUser = raFullname + " (" + raServiceReq.approveUser + ")";
//                                            tempLogReq.approveCADt = new Date();
//                                            String sApprove = db.S_BO_API_CERTIFICATION_OWNER_APPROVED(pOWNER_ATTR_ID[0], objectMapper.writeValueAsString(tempLogReq), EscapeUtils.CheckTextNull(raServiceReq.approveUser));
//                                            if ("0".equals(sApprove)) {
//                                                String sUpdate = db.S_BO_API_CERTIFICATION_OWNER_SUPPLEMENT_FILE(pOWNER_ATTR_ID[0],"" ,raServiceReq.approveUser);
//                                                if("0".equals(sUpdate)) {
//                                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
//                                                } else {
//                                                    raServiceResp.responseCode = Integer.parseInt(sUpdate);
//                                                }
//                                            } else {
//                                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERT_ERROR_APPROVE;
//                                            }
//                                        }
//                                    }
//                                    //</editor-fold>
//                                } else {
//                                    raServiceResp.responseCode = Integer.parseInt(pRESPONSE_CODE[0]);
//                                }
//                            }
//                        }
//                    }
                    //</editor-fold>
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                // get response code return
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 36 approveCertificateOwnerAttachmentForTMSRA">
    @WebMethod(operationName = "approveCertificateOwnerAttachmentForTMSRA")
    public RAServiceResp approveCertificateOwnerAttachmentForTMSRA(@WebParam(name = "approveCertificateOwnerAttachmentForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        boolean autoApproveCAServer = false;
        try {
            sIP_Request = getIPAddress();
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pApproveCAUser = "";
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.approveCertificateOwnerAttachmentProcess(pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="@@@ 37 getCertificateOwnerTypeForTMSRA">
    @WebMethod(operationName = "getCertificateOwnerTypeForTMSRA")
    public RAServiceResp getCertificateOwnerTypeForTMSRA(@WebParam(name = "getCertificateOwnerTypeForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_CERTIFICATION_OWNER_TYPE;
        String sIP_Request = getIPAddress();
        try {
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                        || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateOwnerTypeProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="@@@ 38 getCertificateOwnerStateForTMSRA">
    @WebMethod(operationName = "getCertificateOwnerStateForTMSRA")
    public RAServiceResp getCertificateOwnerStateForTMSRA(@WebParam(name = "getCertificateOwnerStateForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_CERTIFICATION_OWNER_STATE;
        String sIP_Request = getIPAddress();
        try {
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                        || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateOwnerStateProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="@@@ 39 getCertificateOwnerInfoForTMSRA">
    @WebMethod(operationName = "getCertificateOwnerInfoForTMSRA")
    public RAServiceResp getCertificateOwnerInfoForTMSRA(@WebParam(name = "getCertificateOwnerInfoForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_CERTICATION_OWNER_INFO;
        String sIP_Request = getIPAddress();
        try {
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                        || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateOwnerInfoProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="@@@ 40 getCertificateOwnerAttachmentForTMSRA">
    @WebMethod(operationName = "getCertificateOwnerAttachmentForTMSRA")
    public RAServiceResp getCertificateOwnerAttachmentForTMSRA(@WebParam(name = "getCertificateOwnerAttachmentForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_CERTIFICATION_OWNER_ATTACHMENT;
        String sIP_Request = getIPAddress();
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateOwnerAttachmentProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 41 setFormFactorUnblockForTMSRA">
    @WebMethod(operationName = "setFormFactorUnblockForTMSRA")
    public RAServiceResp setFormFactorUnblockForTMSRA(@WebParam(name = "setFormFactorUnblockForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.setFormFactorUnblockProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                // get response code return
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 42 approveFormFactorUnblockForTMSRA">
    @WebMethod(operationName = "approveFormFactorUnblockForTMSRA")
    public RAServiceResp approveFormFactorUnblockForTMSRA(@WebParam(name = "approveFormFactorUnblockForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.approveFormFactorUnblockProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                // get response code return
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 43 declineFormFactorUnblockForTMSRA">
    @WebMethod(operationName = "declineFormFactorUnblockForTMSRA")
    public RAServiceResp declineFormFactorUnblockForTMSRA(@WebParam(name = "declineFormFactorUnblockForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.declineFormFactorUnblockProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="@@@ 44 getFormFactorUnblockForTMSRA">
    @WebMethod(operationName = "getFormFactorUnblockForTMSRA")
    public RAServiceResp getFormFactorUnblockForTMSRA(@WebParam(name = "getFormFactorUnblockForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        String sIP_Request = getIPAddress();
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_FORMFACTOR_UNBLOCK;
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getFormFactorUnblockProcess(pPARENT_ID, pBRANCH_ID, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 45 setFormFactorTokenForTMSRA">
    @WebMethod(operationName = "setFormFactorTokenForTMSRA")
    public RAServiceResp setFormFactorTokenForTMSRA(@WebParam(name = "setFormFactorTokenForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.setFormFactorTokenProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 46 approveFormFactorTokenForTMSRA">
    @WebMethod(operationName = "approveFormFactorTokenForTMSRA")
    public RAServiceResp approveFormFactorTokenForTMSRA(@WebParam(name = "approveFormFactorTokenForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.approveFormFactorTokenProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 47 declineFormFactorTokenForTMSRA">
    @WebMethod(operationName = "declineFormFactorTokenForTMSRA")
    public RAServiceResp declineFormFactorTokenForTMSRA(@WebParam(name = "declineFormFactorTokenForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.declineFormFactorTokenProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="@@@ 48 getFormFactorTokenForTMSRA">
    @WebMethod(operationName = "getFormFactorTokenForTMSRA")
    public RAServiceResp getFormFactorTokenForTMSRA(@WebParam(name = "getFormFactorTokenForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        String sIP_Request = getIPAddress();
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_FORMFACTOR_TOKEN;
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getFormFactorTokenProcess(pPARENT_ID, pBRANCH_ID, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="@@@ 49 getQueueStateForTMSRA">
    @WebMethod(operationName = "getQueueStateForTMSRA")
    public RAServiceResp getQueueStateForTMSRA(@WebParam(name = "getQueueStateForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        String sIP_Request = getIPAddress();
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_QUEUE_STATE;
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getQueueStateProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="@@@ 50 getQueueTypeForTMSRA">
    @WebMethod(operationName = "getQueueTypeForTMSRA")
    public RAServiceResp getQueueTypeForTMSRA(@WebParam(name = "getQueueTypeForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_QUEUE_TYPE;
        String sIP_Request = getIPAddress();
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getQueueTypeProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 51 registerBeneficiaryUserForTMSRA">
    @WebMethod(operationName = "registerBeneficiaryUserForTMSRA")
    public RAServiceResp registerBeneficiaryUserForTMSRA(@WebParam(name = "registerBeneficiaryUserForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        ObjectMapper objectMapper;
        String sIP_Request = getIPAddress();
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_REGISTRATION_BENEFICIARY_USER;
        try {
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pApproveCAUser;
            String pPARENT_ID = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                        || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.registerBeneficiaryUserProcess(pPARENT_ID, pBRANCH_ID, System_Log_ID, System_Log_BillCode,
                        sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, raServiceReq.approveUser);
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="@@@ 52 getCertificateBriefInfoForTMSRA">
    @WebMethod(operationName = "getCertificateBriefInfoForTMSRA")
    public RAServiceResp getCertificateProfileAttachmentForTMSRA(@WebParam(name = "getCertificateBriefInfoForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String pBeneficiaryUserDefault = "";
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_CERTIFICATION_BRIEF_INFO;
        String sSOAP_WS = "";
        String sCERT_POLICY_PROPERTIES;
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sIP_Request = getIPAddress();
        String pPARENT_ID = "";
        int pBRANCH_ID = 0;
        try {
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                        || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        if("".equals(pBeneficiaryUserDefault))
                        {
                            pBeneficiaryUserDefault = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        }
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateBriefInfoProcess(pPARENT_ID, pBRANCH_ID, pBeneficiaryUserDefault, System_Log_ID, System_Log_BillCode,
                        sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 53 compensateCertificateForTMSRA">
    @WebMethod(operationName = "compensateCertificateForTMSRA")
    public RAServiceResp compensateCertificateForTMSRA(@WebParam(name = "compensateCertificateForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            boolean autoApproveCAServer = false;
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES = "";
            String sCERT_PROFILE_PROPERTIES = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            String pBeneficiaryUserDefault = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.compensateCertificateProcess(sCERT_PROFILE_PROPERTIES, sCERT_POLICY_PROPERTIES, pPARENT_ID,
                        pBRANCH_ID, pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="@@@ 54 getCertificateExpireSoonInfoForTMSRA">
    @WebMethod(operationName = "getCertificateExpireSoonInfoForTMSRA")
    public RAServiceResp getCertificateExpireSoonInfoForTMSRA(@WebParam(name = "getCertificateExpireSoonInfoForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        String sFunctionWS=Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_CERTIFICATION_EXPIRE_SOON_INFO;
        RAServiceResp raServiceResp = new RAServiceResp();
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String pBeneficiaryUserDefault = "";
        String sSOAP_WS = "";
        String sCERT_POLICY_PROPERTIES;
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sIP_Request = getIPAddress();
        String pPARENT_ID = "";
        int pBRANCH_ID = 0;
        try {
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                        || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                        if("".equals(pBeneficiaryUserDefault))
                        {
                            pBeneficiaryUserDefault = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        }
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateExpireSoonInfoProcess(pPARENT_ID, pBRANCH_ID, pBeneficiaryUserDefault, System_Log_ID, System_Log_BillCode,
                        sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    RAServiceResp raServiceRespLog = new RAServiceResp();
                    raServiceRespLog.responseCode = raServiceResp.responseCode;
                    raServiceRespLog.responseMessage = raServiceResp.responseMessage;
                    raServiceRespLog.billCode = raServiceResp.billCode;
                    raServiceRespLog.countCertificateReportInfo = raServiceResp.certificateExpireSoonInfo.length;
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceRespLog.responseCode),
                        objectMapper.writeValueAsString(raServiceRespLog), sTOKEN_SN, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 55 setFormFactorTokenBundleForTMSRA">
    @WebMethod(operationName = "setFormFactorTokenBundleForTMSRA")
    public RAServiceResp setFormFactorTokenBundleForTMSRA(@WebParam(name = "setFormFactorTokenBundleForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.setFormFactorTokenBundleProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0 && !"".equals(System_Log_BillCode[0])) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 56 approveFormFactorTokenBundleForTMSRA">
    @WebMethod(operationName = "approveFormFactorTokenBundleForTMSRA")
    public RAServiceResp approveFormFactorTokenBundleForTMSRA(@WebParam(name = "approveFormFactorTokenBundleForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.approveFormFactorTokenBundleProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                raServiceResp.requestExecutionProcess = Definitions.CONFIG_API_RESULT_PROCESS_COMPLETE;
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 57 declineFormFactorTokenBundleForTMSRA">
    @WebMethod(operationName = "declineFormFactorTokenBundleForTMSRA")
    public RAServiceResp declineFormFactorTokenBundleForTMSRA(@WebParam(name = "declineFormFactorTokenBundleForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            sIP_Request = getIPAddress();
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES;
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.declineFormFactorTokenBundleProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="@@@ 58 getFormFactorTokenBundleForTMSRA">
    @WebMethod(operationName = "getFormFactorTokenBundleForTMSRA")
    public RAServiceResp getFormFactorTokenBundleForTMSRA(@WebParam(name = "getFormFactorTokenBundleForTMSRA") RAServiceReq raServiceReq)
    {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        String sIP_Request = getIPAddress();
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_FORMFACTOR_TOKEN;
        try {
            String sSOAP_WS = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pPARENT_ID = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    pBRANCH_ID = rsBranch[0][0].ID;
                    pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>
            
            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getFormFactorTokenBundleProcess(pPARENT_ID, pBRANCH_ID, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
            RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
            db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
            if (rsResponseCode[0].length > 0) {
                raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
}
