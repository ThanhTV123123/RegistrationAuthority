/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.ws;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.ra.object.CertificateComponentInfo;
import vn.ra.object.FileManagerInfo;
import vn.ra.object.FormFactorSNInfo;

/**
 *
 * @author THANH-PC
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RAServiceReq {

    public int language;
//    public int issueEnabled;
    public boolean approveEnabled;// "" | 0 | 1
    public boolean changeKeyEnabled;// true | false
    public boolean backupKeyEnabled;// true | false
    public int certificateID;
    public String userName;
    public String passWord;
    public String taxCode;
    public String personalTaxCode;
    public String socialInsuranceCode;
    public String personalSocialInsuranceCode;
    public String unitCode;
    public String decision;
    public String citizenId;
    public String pid;
    public String budgetCode;
    public String passport;
    public String certificateSN;
    public String certificateStateCode;
    public String requestStateCode;
    public String certificateAuthorityCode;
    public String certificatePurposeCode;
    public String certificateProfileCode;
    public String formFactorCode;
    public String dnsName;
    public boolean p12EmailEnabled;
    public String p12Password;
    public boolean activationCodeEnabled;
    public String branchCode;
    public String beneficiaryBranch;
    public boolean certificateNotificationEnabled;
    public String remark;
    public String beneficiaryUser;
    public String approveUser;
    public String phoneContact;
    public String emailContact;
    public String csr;
    public String cityProvinceCode;
    public String userRoleCode;
    public int quarter;
    public int year;
    public boolean periodicReportEnabled;
    public boolean reconciliationReportEnabled;
    public String fromCreateDate;
    public String toCreateDate;
//    public String certificateRevokeReasonUser;
    public String declineReason;
    public String compromiseDate;
    public String certificateRevocationReason;
    public boolean deleteCertificateEnabled;
    public boolean inHouseEnabled;
    public String fileTypeCode;
    public String phoneNumber;
    public String emailAddress;
    public String certificateOwnerTypeCode;
    public String certificateOwnerStateCode;
    public int certificateOwnerID;
    public int promotionDuration;
    public String personalName;
    public String companyName;
    public String representative;
    public String representativePosition;
    public String address;
    public String expandFutureParamXML;
    public CertificateComponentInfo[] certificateComponentInfo;
    public FileManagerInfo[] fileManagerInfo;
    public CredentialData credentialData;
    // token unblock
    public String formFactorSN;
    public boolean userPINEnabled;
    public int formFactorUnblockID;
    public int formFactorTokenID;
    public String queueStateCode;
    public String queueTypeCode;
    public String fullName;
    public boolean passwordEmailEnabled;
    public boolean keepCertificateSNEnabled;
    public boolean renewProfileEnabled;
    //200422
    public boolean revokeOldCertificateEnabled;
    //160720
    public boolean temporarySuspendEnabled;
    public String suspendReason;
    public String suspendTime;
    //070920
    public String effectiveTime;
    public String expirationTime;
    //301120
    public int expireDateNumber;
    public FormFactorSNInfo[] formFactorSNInfo;
    public String pushNotification;
    public String menuLink;
    public boolean asyncModeEnabled;
    public String billCode;
    //281220
    public String activationCode;
    public String iccid;
    public String certificateFormatType; // CERT/PEM
}
