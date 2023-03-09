/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.SSLUtilities;
import vn.mobileid.esigncloud.management.AgreementDetails;
import vn.mobileid.esigncloud.management.DatabaseClusterInfo;
import vn.mobileid.esigncloud.management.Management;
import vn.mobileid.esigncloud.management.ManagementReq;
import vn.mobileid.esigncloud.management.ManagementResp;
import vn.mobileid.esigncloud.management.Management_Service;
import vn.mobileid.esigncloud.management.MemoryUsage;
import vn.mobileid.esigncloud.management.NetworkInterface;
import vn.mobileid.esigncloud.management.OwnerInfo;
import vn.ra.object.CredentialDataAuthen;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author USER
 */
public class RSSPProcessCommon {

//    final public static String URL = "https://192.168.2.249:10443/eSignCloud/Management?wsdl";
//    final private static String entityName = "CORE_CLOUD_ENTITY";
//    final private static String uuid = "FBA58084-FAC6-4007-BE8B-557A08B064B4";
//    final private static String relyingParty = "FEC";
//    final private static String agreementUUID = "202002271625";
    /*
        Personal information
     */
//    final private static String personalID = "12345678";
//    final private static String personalName = "Dương Phương Vũ";
//    final private static String location = "Quận 1";
//    final private static String stateProvince = "TP Hồ Chí Minh";
//    final private static String country = "VN";
//
//    final private static String certificateProfile = "T2PSB23Y";
//
//    final private static String authorizationEmail = "vudp.90@gmail.com";
//    final private static String authorizationMobileNo = "0378932887";
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RSSPProcessCommon.class.getName());
    public static String URL;
    private static String entityName;
    private static String uuid;
    private static String relyingPartyOwner;
    private static int userRSSPID;
    
    public RSSPProcessCommon() {
        /*Config conf = new Config();
        URL = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_WS_URL);
        entityName = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_CORE_CLOUD_ENTITY);
        uuid = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_UUID);
        relyingPartyOwner = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_FEC);
        userRSSPID = Integer.parseInt(conf.GetPropertybyCode(Definitions.CONFIG_RSSP_USERID));*/
    }

    public void prepareCertificateForSignCloud(String agreementUUID, AgreementDetails agreementDetails,
        String certificateProfile, String phoneContract, String emailContract, String beneficiaryBranch,
        String beneficiaryUser, String ownerUUID, String ownerUserName, String ownerPassword, String ownerEmail,
        String ownerMobileNo, String authMode, String relyingParty, String signingProfile, String[] sResult, CredentialDataAuthen credentialAuthen)
        throws Exception {
        URL = credentialAuthen.wsUrl;
        entityName = credentialAuthen.entityName;
        uuid = credentialAuthen.uuid;
        userRSSPID = credentialAuthen.userId;
        relyingPartyOwner = credentialAuthen.relyingPartyOwner;
        CommonFunction.LogDebugString(log, "prepareCertificateForSignCloud", "URL: " + URL + "; entityName: " + entityName
            + "; uuid: " + uuid + "; relyingParty: " + relyingParty + "; userRSSPID: " + userRSSPID + "; agreementUUID: " + agreementUUID
            + "; certificateProfile: " + certificateProfile + "; ownerMobileNo: " + phoneContract + "; ownerEmail: " + emailContract
            + "; beneficiaryBranch: " + beneficiaryBranch + "; beneficiaryUser: " + beneficiaryUser + "; ownerUUID: " + ownerUUID
            + "; ownerUserName: " + ownerUserName + "; ownerPassword: " + ownerPassword + "; signingProfile: " + signingProfile
            + "; sinatureEmail: " + ownerEmail + "; sinatureMobileNo: " + ownerMobileNo);
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
        Management_Service service = new Management_Service(new URL(URL), new QName("http://management.esigncloud.mobileid.vn/", "Management"));
        Management management = service.getManagementPort();
        ManagementReq managementReq = new ManagementReq();
        managementReq.setEntityName(entityName);
        managementReq.setUuid(uuid);
        managementReq.setRelyingParty(relyingParty);
        managementReq.setAgreementUUID(agreementUUID);

        managementReq.setCertificateProfile(certificateProfile);
        managementReq.setOwnerEmail(emailContract);
        managementReq.setOwnerMobileNo(phoneContract);
        managementReq.setMobileNo(ownerMobileNo);
        managementReq.setEmail(ownerEmail);
        managementReq.setBeneficiaryBranch(beneficiaryBranch);
        managementReq.setBeneficiaryUser(beneficiaryUser);
        managementReq.setOwnerUUID(ownerUUID);
        managementReq.setSetOldCertificateToOperated(true);
        if(!"".equals(ownerUserName)) {
            managementReq.setOwnerUsername(ownerUserName);
        }
        if(!"".equals(ownerPassword)) {
            managementReq.setOwnerPassword(ownerPassword);
        }
        managementReq.setWaitForApproval(true);
        managementReq.setUserID(userRSSPID); // config
        managementReq.setAuthMode(authMode);
        managementReq.setSigningProfile(signingProfile);

//        AgreementDetails agreementDetails = new AgreementDetails();
//        agreementDetails.setPersonalName(personalName);
//        agreementDetails.setPersonalID(personalID);
//        agreementDetails.setLocation(location);
//        agreementDetails.setStateOrProvince(stateProvince);
//        agreementDetails.setCountry(country);
//
        managementReq.setAgreementDetails(agreementDetails);
        ManagementResp managementResp = management.prepareCertificateForSignCloud(managementReq);
        sResult[0] = String.valueOf(managementResp.getResponseCode());
        sResult[1] = managementResp.getResponseMessage().trim();
        sResult[2] = String.valueOf(managementResp.getRaCertificationID()).trim();
    }

    public void approveCertificateForSignCloud(String agreementUUID, String certificateID, String revokeOldCertificateEnabled,
        String relyingParty, String[] sResult, String pCERTIFICATION_SN, String sChangeKeyApprove,
        String sRequestType, String signingProfile, CredentialDataAuthen credentialAuthen, boolean revokeSetOldStatus)
        throws Exception {
        URL = credentialAuthen.wsUrl;
        entityName = credentialAuthen.entityName;
        uuid = credentialAuthen.uuid;
        userRSSPID = credentialAuthen.userId;
        relyingPartyOwner = credentialAuthen.relyingPartyOwner;
        CommonFunction.LogDebugString(log, "approveCertificateForSignCloud", "agreementUUID: " + agreementUUID
            + "; certificateID: " + certificateID + "; revokeOldCertificateEnabled: " + revokeOldCertificateEnabled
            + "; relyingParty: " + relyingParty + "; pCERTIFICATION_SN: " + pCERTIFICATION_SN
            + "; ChangeKeyApprove: " + sChangeKeyApprove + "; RequestType: " + sRequestType
            + "; RequestType: " + signingProfile+ "; revokeSetOldStatus: " + revokeSetOldStatus);
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
        Management_Service service = new Management_Service(new URL(URL), new QName("http://management.esigncloud.mobileid.vn/", "Management"));
        Management management = service.getManagementPort();
        ManagementReq managementReq = new ManagementReq();
        managementReq.setEntityName(entityName);
        managementReq.setUuid(uuid);

        if(!"".equals(pCERTIFICATION_SN)) {
            managementReq.setCertificateSerialNumber(pCERTIFICATION_SN);
        }
        managementReq.setRequestType(sRequestType);
        managementReq.setRelyingParty(relyingParty);
        managementReq.setAgreementUUID(agreementUUID);
        if(!"".equals(signingProfile)) {
            managementReq.setSigningProfile(signingProfile);
        }
        if(!"".equals(revokeOldCertificateEnabled)) {
            boolean boRevokeOldCertificate = "1".equals(revokeOldCertificateEnabled);
            managementReq.setRevokeOldCertificateEnabled(boRevokeOldCertificate);
        }
        managementReq.setWaitForApproval(true);
        if("1".equals(sChangeKeyApprove)) {
            managementReq.setKeepOldKeysEnabled(false);
        } else if("0".equals(sChangeKeyApprove)) {
            managementReq.setKeepOldKeysEnabled(true);
        }
        managementReq.setRaCertificationID(Integer.parseInt(certificateID));
        managementReq.setUserID(userRSSPID);
        if(sRequestType.equals(Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_REVOKE_CERT)) {
            managementReq.setSetOldCertificateToOperated(revokeSetOldStatus);
        }

        ManagementResp managementResp = management.approveCertificateForSignCloud(managementReq);
        sResult[0] = String.valueOf(managementResp.getResponseCode());
        sResult[1] = managementResp.getResponseMessage().trim();
    }
    
    public void declineCertificateForSignCloud(String agreementUUID, String certificateID, String sReason,
        String relyingParty, String[] sResult, String pCERTIFICATION_SN, String sType, CredentialDataAuthen credentialAuthen)
        throws Exception {
        URL = credentialAuthen.wsUrl;
        entityName = credentialAuthen.entityName;
        uuid = credentialAuthen.uuid;
        userRSSPID = credentialAuthen.userId;
        relyingPartyOwner = credentialAuthen.relyingPartyOwner;
        CommonFunction.LogDebugString(log, "declineCertificateForSignCloud", "agreementUUID: " + agreementUUID + "; certificateID: " + certificateID
            + "; sReason: " + sReason+ "; sRequestType: " + sType+ "; relyingParty: " + relyingParty + "; pCERTIFICATION_SN: " + pCERTIFICATION_SN);
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
        Management_Service service = new Management_Service(new URL(URL), new QName("http://management.esigncloud.mobileid.vn/", "Management"));
        Management management = service.getManagementPort();
        ManagementReq managementReq = new ManagementReq();
        managementReq.setEntityName(entityName);
        managementReq.setUuid(uuid);

        managementReq.setRelyingParty(relyingParty);
        managementReq.setRequestType(sType);
        managementReq.setAgreementUUID(agreementUUID);
        managementReq.setDeclineReason(sReason);
        if(!"".equals(pCERTIFICATION_SN)) {
            managementReq.setCertificateSerialNumber(pCERTIFICATION_SN);
        }

        managementReq.setWaitForApproval(true);
        managementReq.setRaCertificationID(Integer.parseInt(certificateID));
        managementReq.setUserID(userRSSPID);

        ManagementResp managementResp = management.declineCertificateForSignCloud(managementReq);
        sResult[0] = String.valueOf(managementResp.getResponseCode());
        sResult[1] = managementResp.getResponseMessage().trim();
    }
    
    public void prepareRenewCertificateForSignCloud(String agreementUUID, AgreementDetails agreementDetails,
        String certificateProfileCode, String sBeneficiaryUser, String sCertSN, String relyingParty, boolean isKeepCertSN, String[] sResult,
        CredentialDataAuthen credentialAuthen) throws Exception {
        URL = credentialAuthen.wsUrl;
        entityName = credentialAuthen.entityName;
        uuid = credentialAuthen.uuid;
        userRSSPID = credentialAuthen.userId;
        relyingPartyOwner = credentialAuthen.relyingPartyOwner;
        CommonFunction.LogDebugString(log, "prepareRenewCertificateForSignCloud", "agreementUUID: " + agreementUUID
            + "; certificateProfileCode: " + certificateProfileCode + "; CertSN: " + sCertSN + "; relyingParty: " + relyingParty);
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
        Management_Service service = new Management_Service(new URL(URL), new QName("http://management.esigncloud.mobileid.vn/", "Management"));
        Management management = service.getManagementPort();
        ManagementReq managementReq = new ManagementReq();
        managementReq.setEntityName(entityName);
        managementReq.setUuid(uuid);
        managementReq.setRelyingParty(relyingParty);
        managementReq.setUserID(userRSSPID);
        if(!"".equals(sCertSN)) {
            managementReq.setCertificateSerialNumber(sCertSN);
        }
        managementReq.setAgreementUUID(agreementUUID);
        managementReq.setCertificateProfile(certificateProfileCode);
        managementReq.setAgreementDetails(agreementDetails);
        managementReq.setBeneficiaryUser(sBeneficiaryUser);
        managementReq.setKeepCertificateSerialNumberEnabled(isKeepCertSN);
        managementReq.setWaitForApproval(true);
        managementReq.setCsrRequired(false);
        managementReq.setKeepOldKeysEnabled(false);

        ManagementResp managementResp = management.prepareRenewCertificateForSignCloud(managementReq);
        sResult[0] = String.valueOf(managementResp.getResponseCode());
        sResult[1] = managementResp.getResponseMessage().trim();
        sResult[2] = String.valueOf(managementResp.getRaCertificationID()).trim();
    }
    
    public void prepareChangeCertificateForSignCloud(String agreementUUID, boolean revokeOldCertificateEnabled,
        AgreementDetails agreementDetails, String sCertSN, String relyingParty, boolean isKeepCertSN, String[] sResult, CredentialDataAuthen credentialAuthen)
        throws Exception {
        URL = credentialAuthen.wsUrl;
        entityName = credentialAuthen.entityName;
        uuid = credentialAuthen.uuid;
        userRSSPID = credentialAuthen.userId;
        relyingPartyOwner = credentialAuthen.relyingPartyOwner;
        CommonFunction.LogDebugString(log, "prepareChangeCertificateForSignCloud", "agreementUUID: " + agreementUUID
            + "; revokeOldCertificateEnabled: " + revokeOldCertificateEnabled+ "; CertSN: " + sCertSN + "; relyingParty: " + relyingParty);
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
        Management_Service service = new Management_Service(new URL(URL), new QName("http://management.esigncloud.mobileid.vn/", "Management"));
        Management management = service.getManagementPort();
        ManagementReq managementReq = new ManagementReq();
        managementReq.setEntityName(entityName);
        managementReq.setUuid(uuid);
        managementReq.setRelyingParty(relyingParty);
        managementReq.setUserID(userRSSPID);
        if(!"".equals(sCertSN)) {
            managementReq.setCertificateSerialNumber(sCertSN);
        }
        managementReq.setAgreementUUID(agreementUUID);
        managementReq.setRevokeOldCertificateEnabled(revokeOldCertificateEnabled);
        managementReq.setKeepCertificateSerialNumberEnabled(isKeepCertSN);
        managementReq.setAgreementDetails(agreementDetails);
        managementReq.setCsrRequired(false);
        managementReq.setKeepOldKeysEnabled(false);
        managementReq.setWaitForApproval(true);

        ManagementResp managementResp = management.prepareChangeCertificateForSignCloud(managementReq);
        sResult[0] = String.valueOf(managementResp.getResponseCode());
        sResult[1] = managementResp.getResponseMessage().trim();
        sResult[2] = String.valueOf(managementResp.getRaCertificationID()).trim();
    }
    
    public void prepareRevokeCertificateForSignCloud(String agreementUUID, String pCERTIFICATION_SN, String sRevokeReason,
        String sCompromiseDate, String relyingParty, String[] sResult, CredentialDataAuthen credentialAuthen, boolean revokeSetOldStatus)
        throws Exception {
        URL = credentialAuthen.wsUrl;
        entityName = credentialAuthen.entityName;
        uuid = credentialAuthen.uuid;
        userRSSPID = credentialAuthen.userId;
        relyingPartyOwner = credentialAuthen.relyingPartyOwner;
        CommonFunction.LogDebugString(log, "prepareRevokeCertificateForSignCloud", "agreementUUID: " + agreementUUID
            + "; pCERTIFICATION_SN: " + pCERTIFICATION_SN + "; sRevokeReason: " + sRevokeReason
            + "; sCompromiseDate: " + sCompromiseDate + "; relyingParty: " + relyingParty
            + "; revokeSetOldStatus: " + revokeSetOldStatus);
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
        Management_Service service = new Management_Service(new URL(URL), new QName("http://management.esigncloud.mobileid.vn/", "Management"));
        Management management = service.getManagementPort();
        ManagementReq managementReq = new ManagementReq();
        managementReq.setEntityName(entityName);
        managementReq.setUuid(uuid);
        managementReq.setUserID(userRSSPID);
        managementReq.setRelyingParty(relyingParty);
        managementReq.setAgreementUUID(agreementUUID);
        managementReq.setRevokeReason(sRevokeReason);
        managementReq.setCompromiseDate(sCompromiseDate);
        managementReq.setCertificateSerialNumber(pCERTIFICATION_SN);
        managementReq.setSetOldCertificateToOperated(revokeSetOldStatus);

        managementReq.setWaitForApproval(true);
        managementReq.setCsrRequired(false);
        managementReq.setKeepOldKeysEnabled(false);

        ManagementResp managementResp = management.prepareRevokeCertificateForSignCloud(managementReq);
        sResult[0] = String.valueOf(managementResp.getResponseCode());
        sResult[1] = managementResp.getResponseMessage().trim();
        sResult[2] = String.valueOf(managementResp.getRaCertificationID()).trim();
    }
    
    public List<OwnerInfo> getOwnerInfoForSignCloud(String ownerUsername, String email, String mobileNo,
        String taxID, String budgetID, String pID, String passportID, String[] sResult, String citizenID, CredentialDataAuthen credentialAuthen)
        throws Exception {
        URL = credentialAuthen.wsUrl;
        entityName = credentialAuthen.entityName;
        uuid = credentialAuthen.uuid;
        userRSSPID = credentialAuthen.userId;
        relyingPartyOwner = credentialAuthen.relyingPartyOwner;
        CommonFunction.LogDebugString(log, "getOwnerInfoForSignCloud", "ownerUsername: " + ownerUsername
            + "; email: " + email + "; mobileNo: " + mobileNo + "; taxID: " + taxID + "; budgetID: " + budgetID
            + "; pID: " + pID + "; citizenID: " + citizenID + "; passportID: " + passportID + "; relyingPartyOwner: " + relyingPartyOwner);
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
        List<OwnerInfo> listOwner;
        Management_Service service = new Management_Service(new URL(URL), new QName("http://management.esigncloud.mobileid.vn/", "Management"));
        Management management = service.getManagementPort();
        ManagementReq managementReq = new ManagementReq();
        managementReq.setEntityName(entityName);
        managementReq.setUuid(uuid);
//        managementReq.setUserID(userRSSPID);
//        managementReq.setRelyingParty(relyingPartyOwner);
        if(!"".equals(ownerUsername)) {
            managementReq.setOwnerUsername(ownerUsername);
        }
        if(!"".equals(email)) {
            managementReq.setOwnerEmail(email);
        }
        if(!"".equals(mobileNo)) {
            managementReq.setOwnerMobileNo(mobileNo);
        }
        if(!"".equals(taxID)) {
            managementReq.setTaxID(taxID);
        }
        if(!"".equals(budgetID)) {
            managementReq.setBudgetID(budgetID);
        }
        if(!"".equals(pID)) {
            managementReq.setPersonalID(pID);
        }
        if(!"".equals(citizenID)) {
            managementReq.setCitizenID(citizenID);
        }
        if(!"".equals(passportID)) {
            managementReq.setPassportID(passportID);
        }

        ManagementResp managementResp = management.getOwnerInfo(managementReq);
        listOwner = managementResp.getOwnerInfo();
        sResult[0] = String.valueOf(managementResp.getResponseCode());
        sResult[1] = String.valueOf(managementResp.getResponseMessage());
        return listOwner;
    }
    
    public List<String> getAuthModesRSSP(CredentialDataAuthen credentialAuthen)
    {
        URL = credentialAuthen.wsUrl;
        entityName = credentialAuthen.entityName;
        uuid = credentialAuthen.uuid;
        userRSSPID = credentialAuthen.userId;
        relyingPartyOwner = credentialAuthen.relyingPartyOwner;
        List<String> authModes = null;
        try
        {
            SSLUtilities.trustAllHostnames();
            SSLUtilities.trustAllHttpsCertificates();
            Management_Service service = new Management_Service(new URL(URL), new QName("http://management.esigncloud.mobileid.vn/", "Management"));
            Management management = service.getManagementPort();
            ManagementReq managementReq = new ManagementReq();
            managementReq.setEntityName(entityName);
            managementReq.setUuid(uuid);
            ManagementResp managementResp = management.getAuthModes(managementReq);
            if (managementResp.getResponseCode() == 0) {
                authModes = managementResp.getAuthModes();
            } else {
                CommonFunction.LogErrorServlet(log, "getAuthModesRSSP Error: " + managementResp.getResponseCode() + " - " + managementResp.getResponseMessage());
            }
        } catch(Exception ex)
        {
            CommonFunction.LogExceptionServlet(log, "getAuthModesRSSP: " + ex.getMessage(), ex);
        } finally {
            
        }
        return authModes;
    }
    
    public List<String> getRelyingPartiesRSSP(CredentialDataAuthen credentialAuthen)
    {
        URL = credentialAuthen.wsUrl;
        entityName = credentialAuthen.entityName;
        uuid = credentialAuthen.uuid;
        userRSSPID = credentialAuthen.userId;
        relyingPartyOwner = credentialAuthen.relyingPartyOwner;
        List<String> relyingParties = null;
        try {
            SSLUtilities.trustAllHostnames();
            SSLUtilities.trustAllHttpsCertificates();
            Management_Service service
                    = new Management_Service(new URL(URL), new QName("http://management.esigncloud.mobileid.vn/", "Management"));
            Management management = service.getManagementPort();
            ManagementReq managementReq = new ManagementReq();
            managementReq.setEntityName(entityName);
            managementReq.setUuid(uuid);
            ManagementResp managementResp = management.getRelyingParties(managementReq);
            if(managementResp.getResponseCode() == 0) {
                relyingParties = managementResp.getRelyingParties();
            } else {
                CommonFunction.LogErrorServlet(log, "getRelyingPartiesRSSP Error: " + managementResp.getResponseCode() + " - " + managementResp.getResponseMessage());
            }
        } catch(Exception ex)
        {
            CommonFunction.LogExceptionServlet(log, "getRelyingPartiesRSSP: " + ex.getMessage(), ex);
        } finally {
            
        }
        return relyingParties;
    }
    
    public void getCertificateDetailForSignCloud(CredentialDataAuthen credentialAuthen,
        String relyingPartyName, String agreementUUID, String certSN, String[] sParam, int[] sCode)
    {
        URL = credentialAuthen.wsUrl;
        entityName = credentialAuthen.entityName;
        uuid = credentialAuthen.uuid;
        userRSSPID = credentialAuthen.userId;
        relyingPartyOwner = credentialAuthen.relyingPartyOwner;
        try {
            SSLUtilities.trustAllHostnames();
            SSLUtilities.trustAllHttpsCertificates();
            Management_Service service
                    = new Management_Service(new URL(URL), new QName("http://management.esigncloud.mobileid.vn/", "Management"));
            Management management = service.getManagementPort();
            ManagementReq managementReq = new ManagementReq();
            managementReq.setRelyingParty(relyingPartyName);
            managementReq.setAgreementUUID(agreementUUID);
            managementReq.setCertificateSerialNumber(certSN);
            managementReq.setEntityName(entityName);
            managementReq.setUuid(uuid);
            managementReq.setUserID(userRSSPID);
            CommonFunction.LogDebugString(log, "getCertificateDetailForSignCloud-REQ", "URL: " + URL + "; entityName: " + entityName
            + "; uuid: " + uuid + "; relyingParty: " + relyingPartyName + "; userRSSPID: " + userRSSPID
                    + "; agreementUUID: " + agreementUUID + "; certSN: " + certSN);
            ManagementResp managementResp = management.getCertificateDetailForSignCloud(managementReq);
            CommonFunction.LogDebugString(log, "getCertificateDetailForSignCloud-RES", "CODE: " + managementResp.getResponseCode());
            if(managementResp.getResponseCode() == 0) {
                sCode[0] = 0; sParam[0] = "Success";
                sParam[1] = String.valueOf(managementResp.getRemainingSigningCounter());
                sParam[2] = String.valueOf(managementResp.getCertificateStateID());
                sParam[3] = EscapeUtils.CheckTextNull(managementResp.getSigningProfiles().get(0));
                sParam[4] = managementResp.getOwnerInfo().get(0).getUsername();
                sParam[5] = String.valueOf(managementResp.getSigningCounter());
                System.out.println("managementResp.getSigningProfiles().get(0): " + EscapeUtils.CheckTextNull(managementResp.getSigningProfiles().get(0)));
                CommonFunction.LogDebugString(log, "getCertificateDetailForSignCloud-RES", "getRemainingSigningCounter: " + managementResp.getRemainingSigningCounter()
                    + "; getCertificateStateID: " + managementResp.getCertificateStateID());
            } else {
                sCode[0] = managementResp.getResponseCode();
                sParam[0] = managementResp.getResponseMessage();
                CommonFunction.LogErrorServlet(log, "getCertificateDetailForSignCloud Error: " + managementResp.getResponseCode() + " - " + managementResp.getResponseMessage());
            }
        } catch(Exception ex)
        {//TEST20220722
            CommonFunction.LogExceptionServlet(log, "getCertificateDetailForSignCloud: " + ex.getMessage(), ex);
            sCode[0] = 1;
            sParam[0] = "ERROR: " + ex.getMessage();
        } finally {
            
        }
    }
    
    public void blockSigningCertificate(CredentialDataAuthen credentialAuthen,
        String relyingPartyName, String agreementUUID, String certSN, String[] sParam, int[] sCode)
    {
        URL = credentialAuthen.wsUrl;
        entityName = credentialAuthen.entityName;
        uuid = credentialAuthen.uuid;
        userRSSPID = credentialAuthen.userId;
        relyingPartyOwner = credentialAuthen.relyingPartyOwner;
        try {
            SSLUtilities.trustAllHostnames();
            SSLUtilities.trustAllHttpsCertificates();
            Management_Service service
                    = new Management_Service(new URL(URL), new QName("http://management.esigncloud.mobileid.vn/", "Management"));
            Management management = service.getManagementPort();
            ManagementReq managementReq = new ManagementReq();
            managementReq.setRelyingParty(relyingPartyName);
            managementReq.setAgreementUUID(agreementUUID);
            managementReq.setCertificateSerialNumber(certSN);
            managementReq.setEntityName(entityName);
            managementReq.setUuid(uuid);
            managementReq.setUserID(userRSSPID);
            CommonFunction.LogDebugString(log, "blockSigningCertificate-REQ", "URL: " + URL + "; entityName: " + entityName
            + "; uuid: " + uuid + "; relyingParty: " + relyingPartyName + "; userRSSPID: " + userRSSPID + "; agreementUUID: " + agreementUUID + "; certSN: " + certSN);
            ManagementResp managementResp = management.blockSigningCertificate(managementReq);
            CommonFunction.LogDebugString(log, "blockSigningCertificate-RES", "CODE: " + managementResp.getResponseCode());
            if(managementResp.getResponseCode() == 0) {
                sCode[0] = 0;
                sParam[0] = "Success";
                sParam[1] = String.valueOf(managementResp.getRemainingSigningCounter());
                sParam[2] = String.valueOf(managementResp.getCertificateStateID());
            } else {
                sCode[0] = managementResp.getResponseCode();
                sParam[0] = "ERROR: " + managementResp.getResponseCode()+ " - " + managementResp.getResponseMessage();
                CommonFunction.LogErrorServlet(log, "blockSigningCertificate Error: " + managementResp.getResponseCode() + " - " + managementResp.getResponseMessage());
            }
        } catch(Exception ex)
        {
            CommonFunction.LogExceptionServlet(log, "blockSigningCertificate: " + ex.getMessage(), ex);
            sCode[0] = 1;
            sParam[0] = "ERROR: " + ex.getMessage();
        } finally {
            
        }
    }
    
    public void unblockSigningCertificate(CredentialDataAuthen credentialAuthen,
        String relyingPartyName, String agreementUUID, String certSN, String[] sParam, int[] sCode)
    {
        URL = credentialAuthen.wsUrl;
        entityName = credentialAuthen.entityName;
        uuid = credentialAuthen.uuid;
        userRSSPID = credentialAuthen.userId;
        relyingPartyOwner = credentialAuthen.relyingPartyOwner;
        try {
            SSLUtilities.trustAllHostnames();
            SSLUtilities.trustAllHttpsCertificates();
            Management_Service service
                    = new Management_Service(new URL(URL), new QName("http://management.esigncloud.mobileid.vn/", "Management"));
            Management management = service.getManagementPort();
            ManagementReq managementReq = new ManagementReq();
            managementReq.setRelyingParty(relyingPartyName);
            managementReq.setAgreementUUID(agreementUUID);
            managementReq.setCertificateSerialNumber(certSN);
            managementReq.setEntityName(entityName);
            managementReq.setUuid(uuid);
            managementReq.setUserID(userRSSPID);
            CommonFunction.LogDebugString(log, "unblockSigningCertificate-REQ", "URL: " + URL + "; entityName: " + entityName
            + "; uuid: " + uuid + "; relyingParty: " + relyingPartyName + "; userRSSPID: " + userRSSPID + "; agreementUUID: " + agreementUUID + "; certSN: " + certSN);
            ManagementResp managementResp = management.unblockSigningCertificate(managementReq);
            CommonFunction.LogDebugString(log, "unblockSigningCertificate-RES", "CODE: " + managementResp.getResponseCode());
            if(managementResp.getResponseCode() == 0) {
                sCode[0] = 0;
                sParam[0] = "Success";
                sParam[1] = String.valueOf(managementResp.getRemainingSigningCounter());
                sParam[2] = String.valueOf(managementResp.getCertificateStateID());
            } else {
                sCode[0] = managementResp.getResponseCode();
                sParam[0] = "ERROR: " + managementResp.getResponseCode()+ " - " + managementResp.getResponseMessage();
                CommonFunction.LogErrorServlet(log, "unblockSigningCertificate Error: " + managementResp.getResponseCode() + " - " + managementResp.getResponseMessage());
            }
        } catch(Exception ex)
        {
            CommonFunction.LogExceptionServlet(log, "unblockSigningCertificate: " + ex.getMessage(), ex);
            sCode[0] = 1;
            sParam[0] = "ERROR: " + ex.getMessage();
        } finally {
            
        }
    }
    
    public void forgetPasscodeForSignCloud(CredentialDataAuthen credentialAuthen,
        String relyingPartyName, String agreementUUID, String certSN, String[] sParam, int[] sCode)
    {
        URL = credentialAuthen.wsUrl;
        entityName = credentialAuthen.entityName;
        uuid = credentialAuthen.uuid;
        userRSSPID = credentialAuthen.userId;
        relyingPartyOwner = credentialAuthen.relyingPartyOwner;
        try {
            SSLUtilities.trustAllHostnames();
            SSLUtilities.trustAllHttpsCertificates();
            Management_Service service
                    = new Management_Service(new URL(URL), new QName("http://management.esigncloud.mobileid.vn/", "Management"));
            Management management = service.getManagementPort();
            ManagementReq managementReq = new ManagementReq();
            managementReq.setRelyingParty(relyingPartyName);
            managementReq.setAgreementUUID(agreementUUID);
            managementReq.setCertificateSerialNumber(certSN);
            managementReq.setEntityName(entityName);
            managementReq.setUuid(uuid);
            managementReq.setUserID(userRSSPID);
            CommonFunction.LogDebugString(log, "forgetPasscodeForSignCloud-REQ", "URL: " + URL + "; entityName: " + entityName
            + "; uuid: " + uuid + "; relyingParty: " + relyingPartyName + "; userRSSPID: " + userRSSPID + "; agreementUUID: " + agreementUUID + "; certSN: " + certSN);
            ManagementResp managementResp = management.forgetPasscodeForSignCloud(managementReq);
            CommonFunction.LogDebugString(log, "forgetPasscodeForSignCloud-RES", "CODE: " + managementResp.getResponseCode());
            if(managementResp.getResponseCode() == 0) {
                sCode[0] = 0;
                sParam[0] = "Success";
                sParam[1] = String.valueOf(managementResp.getRemainingSigningCounter());
                sParam[2] = String.valueOf(managementResp.getCertificateStateID());
            } else {
                sCode[0] = managementResp.getResponseCode();
                sParam[0] = "ERROR: " + managementResp.getResponseCode()+ " - " + managementResp.getResponseMessage();
                CommonFunction.LogErrorServlet(log, "forgetPasscodeForSignCloud Error: " + managementResp.getResponseCode() + " - " + managementResp.getResponseMessage());
            }
        } catch(Exception ex)
        {
            CommonFunction.LogExceptionServlet(log, "forgetPasscodeForSignCloud: " + ex.getMessage(), ex);
            sCode[0] = 1;
            sParam[0] = "ERROR: " + ex.getMessage();
        } finally {
            
        }
    }
    
    public List<String> getSigningProfilesRSSP(CredentialDataAuthen credentialAuthen) {
        URL = credentialAuthen.wsUrl;
        entityName = credentialAuthen.entityName;
        uuid = credentialAuthen.uuid;
        userRSSPID = credentialAuthen.userId;
        relyingPartyOwner = credentialAuthen.relyingPartyOwner;
        List<String> signingProfile = null;
        try {
            SSLUtilities.trustAllHostnames();
            SSLUtilities.trustAllHttpsCertificates();
            Management_Service service
                    = new Management_Service(new URL(URL), new QName("http://management.esigncloud.mobileid.vn/", "Management"));
            Management management = service.getManagementPort();
            ManagementReq managementReq = new ManagementReq();
            managementReq.setEntityName(entityName);
            managementReq.setUuid(uuid);
            ManagementResp managementResp = management.getSigningProfiles(managementReq);
            if(managementResp.getResponseCode() == 0) {
                signingProfile = managementResp.getSigningProfiles();
            } else {
                CommonFunction.LogErrorServlet(log, "getSigningProfilesRSSP Error: " + managementResp.getResponseCode() + " - " + managementResp.getResponseMessage());
            }
        } catch(Exception ex)
        {
            CommonFunction.LogExceptionServlet(log, "getSigningProfilesRSSP: " + ex.getMessage(), ex);
        } finally {
            
        }
        return signingProfile;
    }
    
    public void changeSigningProfile(CredentialDataAuthen credentialAuthen, String relyingPartyName,
            String agreementUUID, String certSN, String SIGNING_PROFILES, String[] sParam, int[] sCode) {
        URL = credentialAuthen.wsUrl;
        entityName = credentialAuthen.entityName;
        uuid = credentialAuthen.uuid;
        userRSSPID = credentialAuthen.userId;
        relyingPartyOwner = credentialAuthen.relyingPartyOwner;
        try {
            SSLUtilities.trustAllHostnames();
            SSLUtilities.trustAllHttpsCertificates();
            Management_Service service
                    = new Management_Service(new URL(URL), new QName("http://management.esigncloud.mobileid.vn/", "Management"));
            Management management = service.getManagementPort();
            ManagementReq managementReq = new ManagementReq();
            managementReq.setEntityName(entityName);
            managementReq.setUuid(uuid);
            managementReq.setRelyingParty(relyingPartyName);
            managementReq.setAgreementUUID(agreementUUID);
            managementReq.setCertificateSerialNumber(certSN);
            managementReq.setSigningProfile(SIGNING_PROFILES);
            ManagementResp managementResp = management.changeSigningProfile(managementReq);
            CommonFunction.LogDebugString(log, "changeSigningProfile-RES", "CODE: " + managementResp.getResponseCode());
            if(managementResp.getResponseCode() == 0) {
                sCode[0] = 0;
                sParam[0] = "Success";
            } else {
                sCode[0] = managementResp.getResponseCode();
                sParam[0] = "ERROR: " + managementResp.getResponseCode()+ " - " + managementResp.getResponseMessage();
                CommonFunction.LogErrorServlet(log, "changeSigningProfile Error: " + managementResp.getResponseCode() + " - " + managementResp.getResponseMessage());
            }
        } catch(Exception ex)
        {
            CommonFunction.LogExceptionServlet(log, "changeSigningProfile: " + ex.getMessage(), ex);
        } finally {
            
        }
    }
}
