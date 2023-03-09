/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import vn.ra.object.ENTERPRISE_INFO;
import vn.ra.raconnector.EnterpriseInfo;
import vn.ra.raconnector.GenerateDigitalCertification;
import vn.ra.raconnector.MailComponent;
import vn.ra.raconnector.RaPortalUser;
import vn.ra.raconnector.RegistrationAuthorityWSRequest;
import vn.ra.raconnector.RegistrationAuthorityWSResponse;
import vn.ra.raconnector.Revoke;
import vn.ra.raconnector.RevokeResponse;
import vn.ra.raconnector.Uat;
import vn.ra.raconnector.UatResponse;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author THANH-PC
 */
public class ConnectConnector {

    public static void RevokeCertificate(String sTokenSN, String sCertificateSN, int sRevokeReason, String sCaName,
        String sCompromiseDate, int[] intRes, String[] sRes, int sCertID, int sRequestID) {
        try {
            Config conf = new Config();
            String url_WSConnector = conf.GetPropertybyCode(Definitions.CONFIG_WS_URL_RACONNECTOR);
            RegistrationAuthorityWSRequest reqRevoke = new RegistrationAuthorityWSRequest();
            reqRevoke.setTokenSn(sTokenSN);
            reqRevoke.setSerialNumber(sCertificateSN);
            reqRevoke.setCaName(sCaName);
            reqRevoke.setRevokeReason(sRevokeReason);
            reqRevoke.setCertificationId(sCertID);
            reqRevoke.setCertificationAttrId(sRequestID);
            if(!"".equals(sCompromiseDate))
            {
                sCompromiseDate = sCompromiseDate.replace("-", "/");
                XMLGregorianCalendar strCreateDate = CommonFunction.ConvertStringXMLGregorian(sCompromiseDate);
                reqRevoke.setCompromiseDate(strCreateDate);
            }
            CommonFunction.LogDebugString(null, "RevokeCertificate-REQUEST", "URL: " + url_WSConnector
                + "; TOKEN_SN: " + sTokenSN + "; CERTIFICATION_SN: " + sCertificateSN + "; CA_NAME: " + sCaName
                + "; REVOKE_REASON_EJBCA: " + sRevokeReason + "; CERT_ID: " + sCertID
                + "; CERT_ATTR_ID: " + sRequestID);
            RegistrationAuthorityWSResponse enrollResponse1 = ConnectorWS.getInstance().getWS(url_WSConnector).revoke(reqRevoke);
            CommonFunction.LogDebugString(null, "RevokeCertificate-RESPONSE", "RES_CODE: " + enrollResponse1.getResponseCode()
                    + "; RES_MESS: " + enrollResponse1.getResponseMessage());
            intRes[0] = enrollResponse1.getResponseCode();
            sRes[0] = enrollResponse1.getResponseMessage();
        } catch (Exception e) {
            intRes[0] = 1;
            sRes[0] = e.getMessage();
            CommonFunction.LogExceptionServlet(null, "RevokeCertificate: " + e.getMessage(), e);
        }

//        enrollResponse1.getReturn()
//        ArrayList<CERT_PROFILE> tempList = new ArrayList<>();
//        UserData userData1 = new UserData();
//        userData1.setEntityProfileName(sProfileList);
//        CommonFunction.LogStringDebug("WS-LoadProfileCert: " + Definitions.CONFIG_WS_FUNCTION_GET_PROFILELIST + "; EntityProfileName: " + sProfileList + "; URL: " + url_WSConnector);
//        EnrollResponse enrollResponse1 = Connector.getInstance().getWS(url_WSConnector).call(Definitions.CONFIG_WS_FUNCTION_GET_PROFILELIST, userData1);
//        List<IdAndName> entityList = enrollResponse1.getIdAndNames();
//        for (IdAndName entityList1 : entityList) {
//            CERT_PROFILE tempItem = new CERT_PROFILE();
//            tempItem.CERT_PROFILE_NAME = entityList1.getName();
//            tempList.add(tempItem);
//        }
//        response[0] = new CERT_PROFILE[tempList.size()];
//        response[0] = tempList.toArray(response[0]);
    }

    public static void ReCreateDigitalCert(String sCertID, int[] intRes, String[] sRes) {
        try {
            Config conf = new Config();
            String url_WSConnector = conf.GetPropertybyCode(Definitions.CONFIG_WS_URL_RACONNECTOR);
            RegistrationAuthorityWSRequest reqRevoke = new RegistrationAuthorityWSRequest();
            reqRevoke.setCertificationId(Integer.parseInt(sCertID));
            CommonFunction.LogDebugString(null, "ReCreateDigitalCert-REQUEST", "URL: " + url_WSConnector
                    + "; CERT_ID " + sCertID);
            RegistrationAuthorityWSResponse enrollResponse1 = ConnectorWS.getInstance().getWS(url_WSConnector).generateDigitalCertification(reqRevoke);
            CommonFunction.LogDebugString(null, "ReCreateDigitalCert-RESPONSE", "RES_CODE: " + enrollResponse1.getResponseCode()
                    + "; RES_MESS: " + enrollResponse1.getResponseMessage());
            intRes[0] = enrollResponse1.getResponseCode();
            sRes[0] = enrollResponse1.getResponseMessage();
        } catch (Exception e) {
            intRes[0] = 1;
            sRes[0] = e.getMessage();
            CommonFunction.LogExceptionServlet(null, "ReCreateDigitalCert: " + e.getMessage(), e);
        }
    }

    public static void SendMailOTP(String sCertID, int[] intRes, String[] sRes) {
        try {
            Config conf = new Config();
            String url_WSConnector = conf.GetPropertybyCode(Definitions.CONFIG_WS_URL_RACONNECTOR);
            RegistrationAuthorityWSRequest reqRevoke = new RegistrationAuthorityWSRequest();
            reqRevoke.setCertificationId(Integer.parseInt(sCertID));
            CommonFunction.LogDebugString(null, "SendMailOTP-REQUEST", "URL: " + url_WSConnector
                + "; CERT_ID " + sCertID);
            RegistrationAuthorityWSResponse enrollResponse1 = ConnectorWS.getInstance().getWS(url_WSConnector).sendAuthenticationCode(reqRevoke);
            CommonFunction.LogDebugString(null, "SendMailOTP-RESPONSE", "RES_CODE: " + enrollResponse1.getResponseCode()
                + "; RES_MESS: " + enrollResponse1.getResponseMessage());
            intRes[0] = enrollResponse1.getResponseCode();
            sRes[0] = enrollResponse1.getResponseMessage();
            if(intRes[0] != 0)
            {
                CommonFunction.LogErrorServlet(null, "SendMailOTP: " + sRes[0]);
            }
        } catch (Exception e) {
            intRes[0] = 1;
            sRes[0] = e.getMessage();
            CommonFunction.LogExceptionServlet(null, "Error SendMailOTP: " + e.getMessage(), e);
        }
    }

    public static void SendMailCreateUser(String sUserID, String sPassword, int[] intRes, String[] sRes) {
        try {
            Config conf = new Config();
            String url_WSConnector = conf.GetPropertybyCode(Definitions.CONFIG_WS_URL_RACONNECTOR);
            RaPortalUser raUser = new RaPortalUser();
            raUser.setId(Integer.parseInt(sUserID));
            raUser.setPassword(sPassword);
            RegistrationAuthorityWSRequest reqRevoke = new RegistrationAuthorityWSRequest();
            reqRevoke.setRaPortalUser(raUser);
            CommonFunction.LogDebugString(null, "SendMailCreateUser-REQUEST", "URL: " + url_WSConnector
                    + "; USER_ID " + sUserID + "; PASSWORD: " + sPassword);
            RegistrationAuthorityWSResponse enrollResponse1 = ConnectorWS.getInstance().getWS(url_WSConnector).notifyPasswordToNewUser(reqRevoke);
            CommonFunction.LogDebugString(null, "SendMailCreateUser-RESPONSE", "RES_CODE: " + enrollResponse1.getResponseCode()
                    + "; RES_MESS: " + enrollResponse1.getResponseMessage());
            intRes[0] = enrollResponse1.getResponseCode();
            sRes[0] = enrollResponse1.getResponseMessage();
        } catch (Exception e) {
            intRes[0] = 1;
            sRes[0] = e.getMessage();
            CommonFunction.LogExceptionServlet(null, "SendMailCreateUser: " + e.getMessage(), e);
        }
    }
    public static void SendMailForgotPass(String sUserID, String sPassword, int[] intRes, String[] sRes) {
        try {
            Config conf = new Config();
            String url_WSConnector = conf.GetPropertybyCode(Definitions.CONFIG_WS_URL_RACONNECTOR);
            RaPortalUser raUser = new RaPortalUser();
            raUser.setId(Integer.parseInt(sUserID));
            raUser.setPassword(sPassword);
            RegistrationAuthorityWSRequest reqRevoke = new RegistrationAuthorityWSRequest();
            reqRevoke.setRaPortalUser(raUser);
            CommonFunction.LogDebugString(null, "SendMailForgotPass-REQUEST", "URL: " + url_WSConnector
                    + "; USER_ID " + sUserID + "; PASSWORD: " + sPassword);
            RegistrationAuthorityWSResponse enrollResponse1 = ConnectorWS.getInstance().getWS(url_WSConnector).notifyForgottenPasswordToUser(reqRevoke);
            CommonFunction.LogDebugString(null, "SendMailForgotPass-RESPONSE", "RES_CODE: " + enrollResponse1.getResponseCode()
                    + "; RES_MESS: " + enrollResponse1.getResponseMessage());
            intRes[0] = enrollResponse1.getResponseCode();
            sRes[0] = enrollResponse1.getResponseMessage();
        } catch (Exception e) {
            intRes[0] = 1;
            sRes[0] = e.getMessage();
            CommonFunction.LogExceptionServlet(null, "SendMailForgotPass: " + e.getMessage(), e);
        }
    }

    public static void GetCompanyFromTaxCode(String sTaxCode, ENTERPRISE_INFO[][] response,
        int[] intRes, String[] sRes) {
        ArrayList<ENTERPRISE_INFO> tempList = new ArrayList<>();
        try {
            Config conf = new Config();
            String url_WSConnector = conf.GetPropertybyCode(Definitions.CONFIG_WS_URL_RACONNECTOR);
            EnterpriseInfo raInfo = new EnterpriseInfo();
            raInfo.setTaxCode(sTaxCode);
            RegistrationAuthorityWSRequest reqRevoke = new RegistrationAuthorityWSRequest();
            reqRevoke.setEnterpriseInfo(raInfo);
            CommonFunction.LogDebugString(null, "GetCompanyFromTaxCode-REQUEST", "URL: " + url_WSConnector
                    + "; USER_ID " + sTaxCode);
            RegistrationAuthorityWSResponse enrollResponse1 = ConnectorWS.getInstance().getWS(url_WSConnector).getEnterpriseInfo(reqRevoke);
            if (enrollResponse1.getResponseCode() == 0) {
                ENTERPRISE_INFO tempItem = new ENTERPRISE_INFO();
                tempItem.ISSUE_DATE = EscapeUtils.CheckTextNull(enrollResponse1.getEnterpriseInfo().getIssueDate());
                tempItem.PLACEOF_ISSUE = EscapeUtils.CheckTextNull(enrollResponse1.getEnterpriseInfo().getPlaceOfIssue());
                tempItem.POSITION = EscapeUtils.CheckTextNull(enrollResponse1.getEnterpriseInfo().getPosition());
                tempItem.PERMANENT_ADDRESS = EscapeUtils.CheckTextNull(enrollResponse1.getEnterpriseInfo().getPermanentAddress());
                tempItem.CCCD = EscapeUtils.CheckTextNull(enrollResponse1.getEnterpriseInfo().getLegalDocumentValue());
                
                tempItem.NAME = EscapeUtils.CheckTextNull(enrollResponse1.getEnterpriseInfo().getName());
                tempItem.LOCALTION = EscapeUtils.CheckTextNull(enrollResponse1.getEnterpriseInfo().getLocation());
                tempItem.TAXCODE = EscapeUtils.CheckTextNull(enrollResponse1.getEnterpriseInfo().getTaxCode());
                tempItem.PROVINCE = EscapeUtils.CheckTextNull(enrollResponse1.getEnterpriseInfo().getProvince());
                tempItem.ADDRESS = EscapeUtils.CheckTextNull(enrollResponse1.getEnterpriseInfo().getAddress());
                tempItem.PRESENTATIVE_NAME = EscapeUtils.CheckTextNull(enrollResponse1.getEnterpriseInfo().getPresentativeName());
                tempList.add(tempItem);
                response[0] = new ENTERPRISE_INFO[tempList.size()];
                response[0] = tempList.toArray(response[0]);
                CommonFunction.LogDebugString(null, "GetCompanyFromTaxCode-RESPONSE", "RES_CODE: " + enrollResponse1.getResponseCode()
                        + "; RES_MESS: " + enrollResponse1.getResponseMessage());
                intRes[0] = 0;
                sRes[0] = enrollResponse1.getResponseMessage();
            } else {
                intRes[0] = enrollResponse1.getResponseCode();
            }
        } catch (Exception e) {
            intRes[0] = 1;
            sRes[0] = e.getMessage();
            CommonFunction.LogExceptionServlet(null, "GetCompanyFromTaxCode: " + e.getMessage(), e);
        }
    }
    
    public static void ReloadParameters(int[] intRes, String[] sRes) {
        try {
            Config conf = new Config();
            String url_WSConnector = conf.GetPropertybyCode(Definitions.CONFIG_WS_URL_RACONNECTOR);
            CommonFunction.LogDebugString(null, "ReloadParameters-REQUEST", "URL: " + url_WSConnector);
            RegistrationAuthorityWSResponse enrollResponse1 = ConnectorWS.getInstance().getWS(url_WSConnector).reloadSystemConfiguarion();
            CommonFunction.LogDebugString(null, "ReloadParameters-RESPONSE", "RES_CODE: " + enrollResponse1.getResponseCode()
                + "; RES_MESS: " + enrollResponse1.getResponseMessage());
            intRes[0] = enrollResponse1.getResponseCode();
            sRes[0] = enrollResponse1.getResponseMessage();
        } catch (Exception e) {
            intRes[0] = 1;
            sRes[0] = e.getMessage();
            CommonFunction.LogExceptionServlet(null, "ReloadParameters: " + e.getMessage(), e);
        }
    }
    
    public static void EnrollCertificate(String sTokenSN, String sPassP12, String sCert_Attr_ID, int[] intRes, String[] sRes) {
        try {
            Config conf = new Config();
            String url_WSConnector = conf.GetPropertybyCode(Definitions.CONFIG_WS_URL_RACONNECTOR);
            RegistrationAuthorityWSRequest reqEnroll = new RegistrationAuthorityWSRequest();
            reqEnroll.setTokenSn(sTokenSN);
            reqEnroll.setKeystorePassword(sPassP12);
            reqEnroll.setCertificationAttrId(Integer.parseInt(sCert_Attr_ID));
            CommonFunction.LogDebugString(null, "EnrollCertificate-REQUEST", "URL: " + url_WSConnector
                + "; TOKEN_SN: " + sTokenSN + "; CERTIFICATION_ATTR_ID: " + sCert_Attr_ID + "; Pass: " + sPassP12);
            RegistrationAuthorityWSResponse enrollResponse1 = ConnectorWS.getInstance().getWS(url_WSConnector).enrollCertificate(reqEnroll);
            CommonFunction.LogDebugString(null, "EnrollCertificate-RESPONSE", "RES_CODE: " + enrollResponse1.getResponseCode()
                + "; RES_MESS: " + enrollResponse1.getResponseMessage());
            intRes[0] = enrollResponse1.getResponseCode();
            sRes[0] = enrollResponse1.getResponseMessage();
        } catch (NumberFormatException e) {
            intRes[0] = 1;
            sRes[0] = e.getMessage();
            CommonFunction.LogExceptionServlet(null, "EnrollCertificate: " + e.getMessage(), e);
        }
    }

    public static String TestConnectWS() {
        String sResult;
        try {
            Config conf = new Config();
            String url_WSConnector = conf.GetPropertybyCode(Definitions.CONFIG_WS_URL_RACONNECTOR);
            CommonFunction.LogDebugString(null, "TestConnectWS-REQUEST", "URL: " + url_WSConnector + "; CALL");
            sResult = ConnectorWS.getInstance().getWS(url_WSConnector).uat();
            CommonFunction.LogDebugString(null, "TestConnectWS-RESPONSE", "RES_MESS: " + sResult);
        } catch (Exception e) {
            sResult = "Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(null, "TestConnectWS: " + e.getMessage(), e);
        }
        return sResult;
    }
    
    public static byte[] generateKeystore(String p12Password, boolean sendEmailEnabled, String sCertID, int[] intRes, String[] sRes) {
        byte[] sByteFile = null;
        try {
            Config conf = new Config();
            String url_WSConnector = conf.GetPropertybyCode(Definitions.CONFIG_WS_URL_RACONNECTOR);
            RegistrationAuthorityWSRequest reqRevoke = new RegistrationAuthorityWSRequest();
            reqRevoke.setCertificationId(Integer.parseInt(sCertID));
            reqRevoke.setKeystorePassword(p12Password);
            reqRevoke.setSendEmailEnabled(sendEmailEnabled);
            CommonFunction.LogDebugString(null, "generateKeystore-REQUEST", "URL: " + url_WSConnector
                + "; CERT_ID " + sCertID);
            RegistrationAuthorityWSResponse enrollResponse1 = ConnectorWS.getInstance().getWS(url_WSConnector).generateKeystore(reqRevoke);
            CommonFunction.LogDebugString(null, "generateKeystore-RESPONSE", "RES_CODE: " + enrollResponse1.getResponseCode()
                + "; RES_MESS: " + enrollResponse1.getResponseMessage());
            intRes[0] = enrollResponse1.getResponseCode();
            sRes[0] = enrollResponse1.getResponseMessage();
            sByteFile = enrollResponse1.getKeystore();
            if(intRes[0] != 0)
            {
                CommonFunction.LogErrorServlet(null, "generateKeystore: " + sRes[0]);
            }
        } catch (Exception e) {
            intRes[0] = 1;
            sRes[0] = e.getMessage();
            CommonFunction.LogExceptionServlet(null, "Error generateKeystore: " + e.getMessage(), e);
        }
        return sByteFile;
    }

    public static void signFileProfile(String sFileID, int[] intRes, String[] sRes) {
        try {
            Config conf = new Config();
            String url_WSConnector = conf.GetPropertybyCode(Definitions.CONFIG_WS_URL_RACONNECTOR);
            RegistrationAuthorityWSRequest reqRevoke = new RegistrationAuthorityWSRequest();
            reqRevoke.setFileManagerId(Integer.parseInt(sFileID));
            CommonFunction.LogDebugString(null, "signFileProfile-REQUEST", "URL: " + url_WSConnector
                + "; FILE_ID " + sFileID);
            RegistrationAuthorityWSResponse enrollResponse1 = ConnectorWS.getInstance().getWS(url_WSConnector).signFile(reqRevoke);
            CommonFunction.LogDebugString(null, "signFileProfile-RESPONSE", "RES_CODE: " + enrollResponse1.getResponseCode()
                + "; RES_MESS: " + enrollResponse1.getResponseMessage());
            intRes[0] = enrollResponse1.getResponseCode();
            sRes[0] = enrollResponse1.getResponseMessage();
            if(intRes[0] != 0) {
                CommonFunction.LogErrorServlet(null, "signFileProfile: " + sRes[0]);
            }
        } catch (NumberFormatException e) {
            intRes[0] = 1;
            sRes[0] = e.getMessage();
            CommonFunction.LogExceptionServlet(null, "Error signFileProfile: " + e.getMessage(), e);
        }
    }
    
    public static void SendMailConfirmHSM(String sTitle, String sContent, String sMailTo, int[] intRes, String[] sRes) {
        try {
            Config conf = new Config();
            String url_WSConnector = conf.GetPropertybyCode(Definitions.CONFIG_WS_URL_RACONNECTOR);
            RegistrationAuthorityWSRequest reqRevoke = new RegistrationAuthorityWSRequest();
            MailComponent component = new MailComponent();
            component.setSubject(sTitle);
            component.setContent(sContent);
            component.setSendTo(sMailTo);
            reqRevoke.setMailComponent(component);
            CommonFunction.LogDebugString(null, "SendMailConfirmHSM-REQUEST", "URL: " + url_WSConnector
                    + "; MAILTO: " + sMailTo + "; SUBJECT: " + sTitle);
            RegistrationAuthorityWSResponse enrollResponse1 = ConnectorWS.getInstance().getWS(url_WSConnector).sendMail(reqRevoke);
            CommonFunction.LogDebugString(null, "SendMailConfirmHSM-RESPONSE", "RES_CODE: " + enrollResponse1.getResponseCode()
                    + "; RES_MESS: " + enrollResponse1.getResponseMessage());
            intRes[0] = enrollResponse1.getResponseCode();
            sRes[0] = enrollResponse1.getResponseMessage();
        } catch (Exception e) {
            intRes[0] = 1;
            sRes[0] = e.getMessage();
            CommonFunction.LogExceptionServlet(null, "SendMailConfirmHSM: " + e.getMessage(), e);
        }
    }
    
}
