/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.ws;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.XmlTransient;
import vn.ra.object.BranchInfo;
import vn.ra.object.CertificateBriefInfo;
import vn.ra.object.CertificateAuthorityInfo;
import vn.ra.object.CertificateComponentInfo;
import vn.ra.object.CertificateInfo;
import vn.ra.object.CertificateNEACReportInfo;
import vn.ra.object.CertificateOwnerInfo;
import vn.ra.object.CertificateOwnerStateInfo;
import vn.ra.object.CertificateOwnerTypeInfo;
import vn.ra.object.CertificateProfileInfo;
import vn.ra.object.CertificatePurposeInfo;
import vn.ra.object.CertificateReportInfo;
import vn.ra.object.CertificateRevocationReasonInfo;
import vn.ra.object.CertificateExpireSoonInfo;
import vn.ra.object.CertificateStateInfo;
import vn.ra.object.CityProvinceInfo;
import vn.ra.object.FileManagerInfo;
import vn.ra.object.FileTypeInfo;
import vn.ra.object.FormFactorSNInfo;
import vn.ra.object.FormFactorSNError;
import vn.ra.object.FormFactorTokenInfo;
import vn.ra.object.FormFactorUnblockInfo;
import vn.ra.object.FormfactorInfo;
import vn.ra.object.PeriodicReportInfo;
import vn.ra.object.QueueStateInfo;
import vn.ra.object.QueueTypeInfo;
import vn.ra.object.ReconciliationReportInfo;
import vn.ra.object.RequestStateInfo;
import vn.ra.object.UserInfo;
import vn.ra.object.UserRoleInfo;

/**
 *
 * @author THANH-PC
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RAServiceResp {

    public int responseCode;
    public String responseMessage;
    public String billCode;
    public String certificate;
    public String certificateSN;
    public String p12Password;
    public int certificateID;
    public int certificateOwnerID;
    public double amount;
    public int durationFree;
    // token unblocik/request
    public int formFactorUnblockID;
    public int formFactorTokenID;
    public String queueStateCode;
    public String queueStateName;
    //
    public byte[] byteCertificate;
    public byte[] p12Certificate;
    public String activationCode;
    public String certificateStateCode;
    public String certificateStateName;
    public String certificateOwnerStateCode;
    public String certificateOwnerStateName;
    public String accessToken;
    public UserInfo[] userInfo;
    public UserRoleInfo[] userRoleInfo;
    public FileTypeInfo[] fileTypeInfo;
    public CityProvinceInfo[] cityProvinceInfo;
    public CertificateStateInfo[] certificateStateInfo;
    public RequestStateInfo[] requestStateInfo;
    public CertificateAuthorityInfo[] certificateAuthorityInfo;
    public CertificatePurposeInfo[] certificatePurposeInfo;
    public CertificateProfileInfo[] certificateProfileInfo;
    public FileManagerInfo[] fileManagerInfo;
    public CertificateComponentInfo[] certificateComponentInfo;
    public CertificateInfo[] certificateInfo;
    public CertificateRevocationReasonInfo[] certificateRevocationReasonInfo;
    public FormfactorInfo[] formfactorInfo;
    public PeriodicReportInfo[] periodicReportInfo;
    public ReconciliationReportInfo[] reconciliationReportInfo;
    public CertificateReportInfo[] certificateReportInfo;
    @XmlTransient
    public int countCertificateReportInfo;
    public CertificateOwnerTypeInfo[] certificateOwnerTypeInfo;
    public CertificateOwnerStateInfo[] certificateOwnerStateInfo;
    public CertificateOwnerInfo[] certificateOwnerInfo;
    public CertificateNEACReportInfo[] certificateNEACReportInfo;
    @XmlTransient
    public int countCertificateNEACReportInfo;
    public BranchInfo[] branchInfo;
    public FormFactorUnblockInfo[] formFactorUnblockInfo;
    public FormFactorTokenInfo[] formFactorTokenInfo;
    public QueueStateInfo[] queueStateInfo;
    public QueueTypeInfo[] queueTypeInfo;
    // 021220
    public CertificateBriefInfo[] certificateBriefInfo;
    public CertificateExpireSoonInfo[] certificateExpireSoonInfo;
    public FormFactorSNError[] formFactorTokenError;
    public String queueTypeCode;
    public FormFactorSNInfo[] formFactorSNInfo;
    public String pushNotification;
    public String menuLink;
    public String remark;
    public String requestExecutionProcess;
}
