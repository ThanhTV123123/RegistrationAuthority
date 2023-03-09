/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.object;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
/**
 *
 * @author THANH-PC
 */
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ATTRIBUTE_VALUES {
    private String tokenSn;
    private boolean changeKeyEnabled;
    private boolean keepCertificateSNEnabled;
    private boolean revokeOldCertificateEnabled;
    private String certtificateDeclineReason;
    private String certtificateRevokeReason;
    private String compromiseDate;
    private String certtificateRevokeEJBCAReason;
    private boolean deleteOldCertificateEnabled;
    private boolean certRevokeDeleteInTokenEnabled;
    private boolean revokeSetOldStatusEnabled;
    private String certtificateSuspendReason;
    private String promotionDuration;
    private java.sql.Timestamp suspendedTime;
    private String typeName; // request Type
    private String createUser;
    private Date createDt;
    private String approveUser;
    private Date approveDt;
    private String approveCAUser;
    private Date approveCADt;
    private String actionReason;
    private String requestState; // attribute state
    private String tokenDeclineReason;
    private String tokenApproveRemark;
    private String rsspAgreementUUID;
    private String rsspRelyingParty;
    private String rsspCertificateUUID;
    private String rsspConnectWSMode;
    private String certificationAttrId;
    private boolean otpAuthenticationEnabled;
    public String otp;
    public Date otpExpiration;
    private ATTRIBUTE_DATA attributeData;
    // 301120
    private String tokenIdOfBundleList;

    @JsonProperty("tokenSn")
    public String getTokenSn() {
        return tokenSn;
    }

    public void setTokenSn(String tokenSn) {
        this.tokenSn = tokenSn;
    }
    
    @JsonProperty("changeKeyEnabled")
    public boolean getChangeKeyEnabled() {
        return changeKeyEnabled;
    }

    public void setChangeKeyEnabled(boolean changeKeyEnabled) {
        this.changeKeyEnabled = changeKeyEnabled;
    }
    
    @JsonProperty("keepCertificateSNEnabled")
    public boolean getKeepCertificateSNEnabled() {
        return keepCertificateSNEnabled;
    }

    public void setKeepCertificateSNEnabled(boolean keepCertificateSNEnabled) {
        this.keepCertificateSNEnabled = keepCertificateSNEnabled;
    }
    
    @JsonProperty("revokeOldCertificateEnabled")
    public boolean getRevokeOldCertificateEnabled() {
        return revokeOldCertificateEnabled;
    }

    public void setRevokeOldCertificateEnabled(boolean revokeOldCertificateEnabled) {
        this.revokeOldCertificateEnabled = revokeOldCertificateEnabled;
    }
    
    @JsonProperty("suspendedTime")
    public java.sql.Timestamp getSuspendedTime() {
        return suspendedTime;
    }

    public void setSuspendedTime(java.sql.Timestamp suspendedTime) {
        this.suspendedTime = suspendedTime;
    }
    
    @JsonProperty("typeName")
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    @JsonProperty("certtificateDeclineReason")
    public String getCerttificateDeclineReason() {
        return certtificateDeclineReason;
    }

    public void setCerttificateDeclineReason(String certtificateDeclineReason) {
        this.certtificateDeclineReason = certtificateDeclineReason;
    }
    
    @JsonProperty("certtificateRevokeReason")
    public String getCerttificateRevokeReason() {
        return certtificateRevokeReason;
    }

    public void setCerttificateRevokeReason(String certtificateRevokeReason) {
        this.certtificateRevokeReason = certtificateRevokeReason;
    }
    
    @JsonProperty("compromiseDate")
    public String getCompromiseDate() {
        return compromiseDate;
    }

    public void setCompromiseDate(String compromiseDate) {
        this.compromiseDate = compromiseDate;
    }
    
    @JsonProperty("certtificateRevokeEJBCAReason")
    public String getCerttificateRevokeEJBCAReason() {
        return certtificateRevokeEJBCAReason;
    }

    public void setCerttificateRevokeEJBCAReason(String certtificateRevokeEJBCAReason) {
        this.certtificateRevokeEJBCAReason = certtificateRevokeEJBCAReason;
    }
    
    @JsonProperty("certRevokeDeleteInTokenEnabled")
    public boolean getCertRevokeDeleteInTokenEnabled() {
        return certRevokeDeleteInTokenEnabled;
    }

    public void setCertRevokeDeleteInTokenEnabled(boolean certRevokeDeleteInTokenEnabled) {
        this.certRevokeDeleteInTokenEnabled = certRevokeDeleteInTokenEnabled;
    }
    
    @JsonProperty("revokeSetOldStatusEnabled")
    public boolean getRevokeSetOldStatusEnabled() {
        return revokeSetOldStatusEnabled;
    }

    public void setRevokeSetOldStatusEnabled(boolean revokeSetOldStatusEnabled) {
        this.revokeSetOldStatusEnabled = revokeSetOldStatusEnabled;
    }
    
    @JsonProperty("deleteOldCertificateEnabled")
    public boolean getDeleteOldCertificateEnabled() {
        return deleteOldCertificateEnabled;
    }

    public void setDeleteOldCertificateEnabled(boolean deleteOldCertificateEnabled) {
        this.deleteOldCertificateEnabled = deleteOldCertificateEnabled;
    }

    @JsonProperty("certtificateSuspendReason")
    public String getCerttificateSuspendReason() {
        return certtificateSuspendReason;
    }

    public void setCerttificateSuspendReason(String certtificateSuspendReason) {
        this.certtificateSuspendReason = certtificateSuspendReason;
    }
    
    @JsonProperty("promotionDuration")
    public String getPromotionDuration() {
        return promotionDuration;
    }

    public void setPromotionDuration(String promotionDuration) {
        this.promotionDuration = promotionDuration;
    }

    @JsonProperty("createUser")
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @JsonProperty("createDt")
    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    @JsonProperty("approveUser")
    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }
    @JsonProperty("approveCAUser")
    public String getApproveCAUser() {
        return approveCAUser;
    }

    public void setApproveCAUser(String approveCAUser) {
        this.approveCAUser = approveCAUser;
    }

    @JsonProperty("approveDt")
    public Date getApproveDt() {
        return approveDt;
    }

    public void setApproveDt(Date approveDt) {
        this.approveDt = approveDt;
    }
    
    @JsonProperty("approveCADt")
    public Date getApproveCADt() {
        return approveCADt;
    }

    public void setApproveCADt(Date approveCADt) {
        this.approveCADt = approveCADt;
    }

    @JsonProperty("requestState")
    public String getRequestState() {
        return requestState;
    }

    public void setRequestState(String requestState) {
        this.requestState = requestState;
    }
    
    @JsonProperty("tokenDeclineReason")
    public String getTokenDeclineReason() {
        return tokenDeclineReason;
    }

    public void setTokenDeclineReason(String tokenDeclineReason) {
        this.tokenDeclineReason = tokenDeclineReason;
    }
    
    @JsonProperty("tokenIdOfBundleList")
    public String getTokenIdOfBundleList() {
        return tokenIdOfBundleList;
    }
    
    public void setTokenIdOfBundleList(String tokenIdOfBundleList) {
        this.tokenIdOfBundleList = tokenIdOfBundleList;
    }
    
    @JsonProperty("tokenApproveRemark")
    public String getTokenApproveRemark() {
        return tokenApproveRemark;
    }

    public void setTokenApproveRemark(String tokenApproveRemark) {
        this.tokenApproveRemark = tokenApproveRemark;
    }
    
    @JsonProperty("rsspAgreementUUID")
    public String getRsspAgreementUUID() {
        return rsspAgreementUUID;
    }

    public void setRsspAgreementUUID(String rsspAgreementUUID) {
        this.rsspAgreementUUID = rsspAgreementUUID;
    }
    
    @JsonProperty("rsspRelyingParty")
    public String getRsspRelyingParty() {
        return rsspRelyingParty;
    }

    public void setRsspRelyingParty(String rsspRelyingParty) {
        this.rsspRelyingParty = rsspRelyingParty;
    }
    
    @JsonProperty("rsspCertificateUUID")
    public String getRsspCertificateUUID() {
        return rsspCertificateUUID;
    }

    public void setRsspCertificateUUID(String rsspCertificateUUID) {
        this.rsspCertificateUUID = rsspCertificateUUID;
    }
    
    @JsonProperty("rsspConnectWSMode")
    public String getRsspConnectWSMode() {
        return rsspConnectWSMode;
    }

    public void setRsspConnectWSMode(String rsspConnectWSMode) {
        this.rsspConnectWSMode = rsspConnectWSMode;
    }
    
    @JsonProperty("certificationAttrId")
    public String getCertificationAttrId() {
        return certificationAttrId;
    }

    public void setCertificationAttrId(String certificationAttrId) {
        this.certificationAttrId = certificationAttrId;
    }
    
    @JsonProperty("actionReason")
    public String getActionReason() {
        return actionReason;
    }

    public void setActionReason(String actionReason) {
        this.actionReason = actionReason;
    }
    // 200507cmc
    @JsonProperty("otpAuthenticationEnabled")
    public boolean getOtpAuthenticationEnabled() {
        return otpAuthenticationEnabled;
    }

    public void setOtpAuthenticationEnabled(boolean otpAuthenticationEnabled) {
        this.otpAuthenticationEnabled = otpAuthenticationEnabled;
    }
    
    @JsonProperty("otp")
    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
    
    @JsonProperty("otpExpiration")
    public Date getOtpExpiration() {
        return otpExpiration;
    }

    public void setOtpExpiration(Date otpExpiration) {
        this.otpExpiration = otpExpiration;
    }

    @JsonProperty("attributeData")
    public ATTRIBUTE_DATA getAttributeData() {
        return attributeData;
    }

    public void setAttributeData(ATTRIBUTE_DATA attributeData) {
        this.attributeData = attributeData;
    }
}
