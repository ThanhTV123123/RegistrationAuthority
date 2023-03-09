/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import com.google.gson.Gson;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import vn.ra.object.CERTIFICATION;
import vn.ra.synch.neac.ListPdfFileBase64;
import vn.ra.synch.neac.RequestDataNEAC;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.HttpResponse;
import vn.ra.utility.HttpUtils;
import vn.ra.utility.LoadParamSystem;

/**
 *
 * @author USER
 */
public class SynchNEACFunction {
    
    private static final Logger log = Logger.getLogger(SynchNEACFunction.class);
    
    public static void synchNEACCertificate(String sUsername, int neacLogID, int certID, int requestTypeID, int pRemainingReal,
        int pRemainingSystem,int[] intRes, String[] strRes)
        throws Exception {
        String sSourceNEAC = LoadParamSystem.getParamStart(Definitions.CONFIG_SYNCH_NEAC_WS_SOURCE);
        String sUrlNEAC = LoadParamSystem.getParamStart(Definitions.CONFIG_SYNCH_NEAC_WS_URL);
        String sUserIDNEAC = LoadParamSystem.getParamStart(Definitions.CONFIG_SYNCH_NEAC_WS_USERID);
        String sUserKeyNEAC = LoadParamSystem.getParamStart(Definitions.CONFIG_SYNCH_NEAC_WS_USERKEY);
        String sFunctionName = "";
        String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
        ConnectDatabase db = new ConnectDatabase();
        CERTIFICATION[][] itemCert = new CERTIFICATION[1][];
        db.S_BO_CERTIFICATION_DETAIL(String.valueOf(certID), "1", itemCert);
        if(itemCert[0].length > 0) {
            boolean isSunchEnabled = true;
            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                if(itemCert[0][0].PKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN
                    || itemCert[0][0].PKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                    if(itemCert[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF) {
                        isSunchEnabled = false;
                    }
                }
            }
            if(isSunchEnabled == true) {
                int sTYPE = 4;
                int sCERT_SUBJECT = 3;
                Config conf = new Config();
                if("1".equals(conf.GetPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_HARDTOKEN_TYPE_HARD_ENABLED))) {
                    sTYPE = 1;
                } else {
                    if(CommonFunction.checkHardTokenIDEnabled(itemCert[0][0].PKI_FORMFACTOR_ID) == true
                        || itemCert[0][0].PKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN
                        || itemCert[0][0].PKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                        sTYPE = 1;
                    } else if(itemCert[0][0].PKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_REMOTE_SIGNING) {
                        sTYPE = 2;
                    } else if(itemCert[0][0].PKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM) {
                        sTYPE = 3;
                    }
                }
                if(itemCert[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL) {
                    sCERT_SUBJECT = 1;
                } else if(itemCert[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE) {
                    sCERT_SUBJECT = 2;
                }
                String sRequestData = "";
                if(requestTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                    if(sSourceNEAC.equals(Definitions.CONFIG_SYNCH_NEAC_SOURCE_EFY)) {
                        sFunctionName = "certificates";
                    } else {
                        sFunctionName = "add";
                    }
                    sRequestData = CommonFunction.genRequestRegisterNEAC(sSourceNEAC, sUserIDNEAC, sUserKeyNEAC,
                        EscapeUtils.CheckTextNull(itemCert[0][0].CERTIFICATION), EscapeUtils.CheckTextNull(itemCert[0][0].EFFECTIVE_DT),
                        EscapeUtils.CheckTextNull(itemCert[0][0].EFFECTIVE_DT), EscapeUtils.CheckTextNull(itemCert[0][0].EXPIRATION_DT),
                        EscapeUtils.CheckTextNull(itemCert[0][0].CERTIFICATION_SN), EscapeUtils.CheckTextNull(itemCert[0][0].SUBJECT),
                        EscapeUtils.CheckTextNull(itemCert[0][0].ISSUER_SUBJECT), sTYPE, sCERT_SUBJECT);
                    CommonFunction.LogDebugString(null, "NEAC_REGISTER_REQUEST", sRequestData);
                    if(!"".equals(sRequestData)) {
                        if(!"".equals(sFunctionName)) {
                            HttpResponse response = HttpUtils.postNEACRequestAdd(sRequestData, sSourceNEAC, sUrlNEAC + sFunctionName, 1000 * 50);
                            if(response.getCode() == 1) {
                                pRemainingReal = -1;
                                db.S_BO_NEAC_LOG_UPDATE(neacLogID, Definitions.CONFIG_SYNCH_NEAC_STATE_SUCCESS, sRequestData, response.getMsg(), pRemainingReal, sUsername);
                                intRes[0] = 0;
                                CommonFunction.LogErrorServlet(log, "SUCCESS. CertID: " + String.valueOf(certID));
                            } else if(response.getCode() == 6) {
                                db.S_BO_NEAC_LOG_UPDATE(neacLogID, Definitions.CONFIG_SYNCH_NEAC_STATE_ERROR_RESYNCHRONIZE, sRequestData, response.getMsg(), pRemainingReal, sUsername);
                            } else {
                                intRes[0] = response.getCode();
                                strRes[0] = response.getDescription();
                                pRemainingReal = pRemainingReal + 1;
                                if(pRemainingReal > pRemainingSystem) {
                                    db.S_BO_NEAC_LOG_UPDATE(neacLogID, Definitions.CONFIG_SYNCH_NEAC_STATE_ERROR_RESYNCHRONIZE, sRequestData, response.getMsg(), pRemainingReal, sUsername);
                                } else {
                                    db.S_BO_NEAC_LOG_UPDATE(neacLogID, Definitions.CONFIG_SYNCH_NEAC_STATE_ERROR_ASYNCHRONOUS, sRequestData, response.getMsg(), pRemainingReal, sUsername);
                                }
                            }
                        } else {
                            intRes[0] = 100;
                            strRes[0] = "FUNCTION API Is Invalid";
                            CommonFunction.LogErrorServlet(log, "FUNCTION API Is Invalid");
                        }
                    } else {
                        intRes[0] = 101;
                        strRes[0] = "Request Data Is Invalid";
                        CommonFunction.LogErrorServlet(log, "Request Data Is Invalid");
                    }
                } else if(requestTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                    String[] sFile = new String[2];
                    sFile[0] = "";
                    sFile[1] = "";
                    if(sSourceNEAC.equals(Definitions.CONFIG_SYNCH_NEAC_SOURCE_EFY)) {
                        sFunctionName = "certificates/revoke";
                        sRequestData = CommonFunction.genRequestRevokeNEAC(sSourceNEAC, sUserIDNEAC, sUserKeyNEAC,
                            EscapeUtils.CheckTextNull(itemCert[0][0].CERTIFICATION), EscapeUtils.CheckTextNull(itemCert[0][0].EFFECTIVE_DT),
                            EscapeUtils.CheckTextNull(itemCert[0][0].EFFECTIVE_DT), EscapeUtils.CheckTextNull(itemCert[0][0].EXPIRATION_DT),
                            EscapeUtils.CheckTextNull(itemCert[0][0].CERTIFICATION_SN), EscapeUtils.CheckTextNull(itemCert[0][0].SUBJECT),
                            EscapeUtils.CheckTextNull(itemCert[0][0].ISSUER_SUBJECT), sTYPE, sCERT_SUBJECT, null);
                    } else {
                        sFunctionName = "revoke";
                        CommonFunction.createFileCert(EscapeUtils.CheckTextNull(itemCert[0][0].CERTIFICATION_SN), EscapeUtils.CheckTextNull(itemCert[0][0].CERTIFICATION), sFile);
                        String sFileContent1 = "JVBERi0xLjQKJeLjz9MKMiAwIG9iago8PC9GaWx0ZXIvRmxhdGVEZWNvZGUvTGVuZ3RoIDU1Pj5zdHJlYW0KeJwr5HIK4TI2U7AwMFMISeEyUNA1BDP03QwVDI0UQtK4NBKTkjVDsoBSBiAJ1xCuQC4AO5YLlwplbmRzdHJlYW0KZW5kb2JqCjQgMCBvYmoKPDwvQ29udGVudHMgMiAwIFIvVHlwZS9QYWdlL1Jlc291cmNlczw8L1Byb2NTZXQgWy9QREYgL1RleHQgL0ltYWdlQiAvSW1hZ2VDIC9JbWFnZUldL0ZvbnQ8PC9GMSAxIDAgUj4+Pj4vUGFyZW50IDMgMCBSL01lZGlhQm94WzAgMCA1OTUgODQyXT4+CmVuZG9iagoxIDAgb2JqCjw8L1N1YnR5cGUvVHlwZTEvVHlwZS9Gb250L0Jhc2VGb250L0hlbHZldGljYS1PYmxpcXVlL0VuY29kaW5nL1dpbkFuc2lFbmNvZGluZz4+CmVuZG9iagozIDAgb2JqCjw8L0tpZHNbNCAwIFJdL1R5cGUvUGFnZXMvQ291bnQgMS9JVFhUKDUuMS4wKT4+CmVuZG9iago1IDAgb2JqCjw8L1R5cGUvQ2F0YWxvZy9QYWdlcyAzIDAgUj4+CmVuZG9iago2IDAgb2JqCjw8L01vZERhdGUoRDoyMDE5MTAxNjEwNTAwOSswNycwMCcpL0NyZWF0aW9uRGF0ZShEOjIwMTkxMDE2MTA1MDA5KzA3JzAwJykvUHJvZHVjZXIoaVRleHSuIDUuMS4wIKkyMDAwLTIwMTEgMVQzWFQgQlZCQSkvQXV0aG9yKE1pbGluZCkvVGl0bGUoTXkgQ29udmVydGVkIFBERik+PgplbmRvYmoKeHJlZgowIDcKMDAwMDAwMDAwMCA2NTUzNSBmIAowMDAwMDAwMjkzIDAwMDAwIG4gCjAwMDAwMDAwMTUgMDAwMDAgbiAKMDAwMDAwMDM4OSAwMDAwMCBuIAowMDAwMDAwMTM2IDAwMDAwIG4gCjAwMDAwMDA0NTIgMDAwMDAgbiAKMDAwMDAwMDQ5NyAwMDAwMCBuIAp0cmFpbGVyCjw8L0luZm8gNiAwIFIvSUQgWzw0NGI4MmQwMDhlOGFlYTVlMWM0ZWQzYWI5MTg2MzI2OT48MGQ2MWQ1MTFkYzZmNmFjNzQwZmRhNTE4YTk3OWQyYzk+XS9Sb290IDUgMCBSL1NpemUgNz4+CnN0YXJ0eHJlZgo2NzIKJSVFT0YK";
                        String sFileName1 = "convert_word_pdf2.pdf";
                        String sCert = EscapeUtils.CheckTextNull(itemCert[0][0].CERTIFICATION);
                        String sMD5File = CommonFunction.getMD5(sFileName1 + sFileContent1);
                        String sMD5Cert = CommonFunction.getMD5(sFile[0] + sCert);
                        String signature = CommonFunction.getSignatureFileNEAC(sFile[1], sMD5File, sMD5Cert, sUserIDNEAC, sUserKeyNEAC);
                        RequestDataNEAC res = new RequestDataNEAC();
                        res.signature = signature;
                        res.userID = sUserIDNEAC;
                        res.serialNumber = EscapeUtils.CheckTextNull(itemCert[0][0].CERTIFICATION_SN);
                        res.certFileName = sFile[0];
                        res.certFileBase64Content = sCert;
                        ListPdfFileBase64[] arrayList;
                        ArrayList<ListPdfFileBase64> tempList = new ArrayList<>();
                        ListPdfFileBase64 item =new ListPdfFileBase64();
                        item.fileName = sFileName1;
                        item.fileBase64Content = sFileContent1;
                        tempList.add(item);
                        arrayList = new ListPdfFileBase64[tempList.size()];
                        arrayList = tempList.toArray(arrayList);
                        res.listPdfFileBase64 = arrayList;
                        sRequestData = new Gson().toJson(res);
                        CommonFunction.LogDebugString(null, "NEAC_REVOKE_REQUEST", sRequestData);
                    }
                    if(!"".equals(sRequestData)) {
                        if(!"".equals(sFunctionName)) {
                            HttpResponse response = HttpUtils.postNEACRequestRevoke(sRequestData, sSourceNEAC, sUrlNEAC + sFunctionName, 1000 * 50, sFile[0], sFile[1]);
                            if(response.getCode() == 1) {
                                pRemainingReal = -1;
                                db.S_BO_NEAC_LOG_UPDATE(neacLogID, Definitions.CONFIG_SYNCH_NEAC_STATE_SUCCESS, sRequestData, response.getMsg(), pRemainingReal, sUsername);
                                intRes[0] = 0;
                                CommonFunction.LogErrorServlet(log, "SUCCESS. CertID: " + String.valueOf(certID));
                            } else if(response.getCode() == 6) {
                                db.S_BO_NEAC_LOG_UPDATE(neacLogID, Definitions.CONFIG_SYNCH_NEAC_STATE_ERROR_RESYNCHRONIZE, sRequestData, response.getMsg(), pRemainingReal, sUsername);
                            } else {
                                intRes[0] = response.getCode();
                                strRes[0] = response.getDescription();
                                pRemainingReal = pRemainingReal + 1;
                                if(pRemainingReal > pRemainingSystem) {
                                    db.S_BO_NEAC_LOG_UPDATE(neacLogID, Definitions.CONFIG_SYNCH_NEAC_STATE_ERROR_RESYNCHRONIZE, sRequestData, response.getMsg(), pRemainingReal, sUsername);
                                } else {
                                    db.S_BO_NEAC_LOG_UPDATE(neacLogID, Definitions.CONFIG_SYNCH_NEAC_STATE_ERROR_ASYNCHRONOUS, sRequestData, response.getMsg(), pRemainingReal, sUsername);
                                }
                            }
                        } else {
                            intRes[0] = 100;
                            strRes[0] = "FUNCTION API Is Invalid";
                            CommonFunction.LogErrorServlet(log, "FUNCTION API Is Invalid. CertID: " + String.valueOf(certID));
                        }
                    } else {
                        intRes[0] = 101;
                        strRes[0] = "Request Data Is Invalid";
                        CommonFunction.LogErrorServlet(log, "Request Data Is Invalid. CertID: " + String.valueOf(certID));
                    }
                } else {
                    intRes[0] = 102;
                    strRes[0] = "Synchronous Request Type Is Invalid";
                    CommonFunction.LogErrorServlet(log, "Synchronous Request Type Is Invalid. CertID: " + String.valueOf(certID));
                }
            }
        } else {
            CommonFunction.LogErrorServlet(log, "Certificate not found. CertID: " + String.valueOf(certID));
        }
    }
}
