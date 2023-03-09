
package vn.mobileid.esigncloud.management;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for managementReq complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="managementReq">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="agreementDetails" type="{http://management.esigncloud.mobileid.vn/}agreementDetails" minOccurs="0"/>
 *         &lt;element name="agreementUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="authMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="authModeSupported" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="authorizeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="beneficiaryBranch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="beneficiaryUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="blockReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="budgetID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificateProfile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificateSerialNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="citizenID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cloudCertificateID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="cloudFileManagerID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="compromiseDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="csrRequired" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="currentPasscode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="declineReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="downloadingFileUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entityBillCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fileProfile" type="{http://management.esigncloud.mobileid.vn/}profile" minOccurs="0"/>
 *         &lt;element name="keepCertificateSerialNumberEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="keepKeysInHSMEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="keepOldKeysEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="logDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="logLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="logType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mobileNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="multipleSignature" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="networkInterface" type="{http://management.esigncloud.mobileid.vn/}networkInterface" minOccurs="0"/>
 *         &lt;element name="newPasscode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="noSendPassCodeToEndUserEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="notificationSubject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="notificationTemplate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ntpConfig" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ownerEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ownerMobileNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ownerPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ownerUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ownerUsername" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="p2pEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="passCodeNotificationMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="passportID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="personalID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="raCertificationID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="registrationParty" type="{http://management.esigncloud.mobileid.vn/}registrationParty" minOccurs="0"/>
 *         &lt;element name="relyingParty" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="relyingPartyBillCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="revokeOldCertificateEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="revokeReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="revokeReasonDetail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="scal" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="setOldCertificateToOperated" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="sharedAgreementUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sharedMode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="sharedModeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sharedRelyingParty" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signDocument" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="signingCounter" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="signingProfile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="taxID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="templateID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="twoFactorMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unblockReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="uuid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="waitForApproval" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "managementReq", propOrder = {
    "agreementDetails",
    "agreementUUID",
    "authMode",
    "authModeSupported",
    "authorizeCode",
    "beneficiaryBranch",
    "beneficiaryUser",
    "billCode",
    "blockReason",
    "budgetID",
    "certificate",
    "certificateProfile",
    "certificateSerialNumber",
    "citizenID",
    "cloudCertificateID",
    "cloudFileManagerID",
    "compromiseDate",
    "csrRequired",
    "currentPasscode",
    "declineReason",
    "downloadingFileUUID",
    "email",
    "entityBillCode",
    "entityName",
    "fileProfile",
    "keepCertificateSerialNumberEnabled",
    "keepKeysInHSMEnabled",
    "keepOldKeysEnabled",
    "language",
    "logDate",
    "logLevel",
    "logType",
    "mobileNo",
    "multipleSignature",
    "networkInterface",
    "newPasscode",
    "noSendPassCodeToEndUserEnabled",
    "notificationSubject",
    "notificationTemplate",
    "ntpConfig",
    "ownerEmail",
    "ownerMobileNo",
    "ownerPassword",
    "ownerUUID",
    "ownerUsername",
    "p2PEnabled",
    "passCodeNotificationMethod",
    "passportID",
    "personalID",
    "raCertificationID",
    "registrationParty",
    "relyingParty",
    "relyingPartyBillCode",
    "requestType",
    "revokeOldCertificateEnabled",
    "revokeReason",
    "revokeReasonDetail",
    "scal",
    "setOldCertificateToOperated",
    "sharedAgreementUUID",
    "sharedMode",
    "sharedModeType",
    "sharedRelyingParty",
    "signDocument",
    "signingCounter",
    "signingProfile",
    "taxID",
    "templateID",
    "twoFactorMethod",
    "unblockReason",
    "userID",
    "uuid",
    "waitForApproval"
})
public class ManagementReq {

    protected AgreementDetails agreementDetails;
    protected String agreementUUID;
    protected String authMode;
    @XmlElement(nillable = true)
    protected List<String> authModeSupported;
    protected String authorizeCode;
    protected String beneficiaryBranch;
    protected String beneficiaryUser;
    protected String billCode;
    protected String blockReason;
    protected String budgetID;
    protected String certificate;
    protected String certificateProfile;
    protected String certificateSerialNumber;
    protected String citizenID;
    protected int cloudCertificateID;
    protected long cloudFileManagerID;
    protected String compromiseDate;
    protected boolean csrRequired;
    protected String currentPasscode;
    protected String declineReason;
    protected String downloadingFileUUID;
    protected String email;
    protected String entityBillCode;
    protected String entityName;
    protected Profile fileProfile;
    protected boolean keepCertificateSerialNumberEnabled;
    protected boolean keepKeysInHSMEnabled;
    protected boolean keepOldKeysEnabled;
    protected String language;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar logDate;
    protected String logLevel;
    protected String logType;
    protected String mobileNo;
    protected int multipleSignature;
    protected NetworkInterface networkInterface;
    protected String newPasscode;
    protected boolean noSendPassCodeToEndUserEnabled;
    protected String notificationSubject;
    protected String notificationTemplate;
    protected String ntpConfig;
    protected String ownerEmail;
    protected String ownerMobileNo;
    protected String ownerPassword;
    protected String ownerUUID;
    protected String ownerUsername;
    @XmlElement(name = "p2pEnabled")
    protected boolean p2PEnabled;
    protected String passCodeNotificationMethod;
    protected String passportID;
    protected String personalID;
    protected int raCertificationID;
    protected RegistrationParty registrationParty;
    protected String relyingParty;
    protected String relyingPartyBillCode;
    protected String requestType;
    protected boolean revokeOldCertificateEnabled;
    protected String revokeReason;
    protected String revokeReasonDetail;
    protected int scal;
    protected boolean setOldCertificateToOperated;
    protected String sharedAgreementUUID;
    protected int sharedMode;
    protected String sharedModeType;
    protected String sharedRelyingParty;
    protected boolean signDocument;
    protected int signingCounter;
    protected String signingProfile;
    protected String taxID;
    protected int templateID;
    protected String twoFactorMethod;
    protected String unblockReason;
    protected int userID;
    protected String uuid;
    protected boolean waitForApproval;

    /**
     * Gets the value of the agreementDetails property.
     * 
     * @return
     *     possible object is
     *     {@link AgreementDetails }
     *     
     */
    public AgreementDetails getAgreementDetails() {
        return agreementDetails;
    }

    /**
     * Sets the value of the agreementDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link AgreementDetails }
     *     
     */
    public void setAgreementDetails(AgreementDetails value) {
        this.agreementDetails = value;
    }

    /**
     * Gets the value of the agreementUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgreementUUID() {
        return agreementUUID;
    }

    /**
     * Sets the value of the agreementUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgreementUUID(String value) {
        this.agreementUUID = value;
    }

    /**
     * Gets the value of the authMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthMode() {
        return authMode;
    }

    /**
     * Sets the value of the authMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthMode(String value) {
        this.authMode = value;
    }

    /**
     * Gets the value of the authModeSupported property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the authModeSupported property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthModeSupported().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAuthModeSupported() {
        if (authModeSupported == null) {
            authModeSupported = new ArrayList<String>();
        }
        return this.authModeSupported;
    }

    /**
     * Gets the value of the authorizeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthorizeCode() {
        return authorizeCode;
    }

    /**
     * Sets the value of the authorizeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthorizeCode(String value) {
        this.authorizeCode = value;
    }

    /**
     * Gets the value of the beneficiaryBranch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryBranch() {
        return beneficiaryBranch;
    }

    /**
     * Sets the value of the beneficiaryBranch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryBranch(String value) {
        this.beneficiaryBranch = value;
    }

    /**
     * Gets the value of the beneficiaryUser property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryUser() {
        return beneficiaryUser;
    }

    /**
     * Sets the value of the beneficiaryUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryUser(String value) {
        this.beneficiaryUser = value;
    }

    /**
     * Gets the value of the billCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillCode() {
        return billCode;
    }

    /**
     * Sets the value of the billCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillCode(String value) {
        this.billCode = value;
    }

    /**
     * Gets the value of the blockReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBlockReason() {
        return blockReason;
    }

    /**
     * Sets the value of the blockReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBlockReason(String value) {
        this.blockReason = value;
    }

    /**
     * Gets the value of the budgetID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBudgetID() {
        return budgetID;
    }

    /**
     * Sets the value of the budgetID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBudgetID(String value) {
        this.budgetID = value;
    }

    /**
     * Gets the value of the certificate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificate() {
        return certificate;
    }

    /**
     * Sets the value of the certificate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificate(String value) {
        this.certificate = value;
    }

    /**
     * Gets the value of the certificateProfile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificateProfile() {
        return certificateProfile;
    }

    /**
     * Sets the value of the certificateProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificateProfile(String value) {
        this.certificateProfile = value;
    }

    /**
     * Gets the value of the certificateSerialNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificateSerialNumber() {
        return certificateSerialNumber;
    }

    /**
     * Sets the value of the certificateSerialNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificateSerialNumber(String value) {
        this.certificateSerialNumber = value;
    }

    /**
     * Gets the value of the citizenID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCitizenID() {
        return citizenID;
    }

    /**
     * Sets the value of the citizenID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCitizenID(String value) {
        this.citizenID = value;
    }

    /**
     * Gets the value of the cloudCertificateID property.
     * 
     */
    public int getCloudCertificateID() {
        return cloudCertificateID;
    }

    /**
     * Sets the value of the cloudCertificateID property.
     * 
     */
    public void setCloudCertificateID(int value) {
        this.cloudCertificateID = value;
    }

    /**
     * Gets the value of the cloudFileManagerID property.
     * 
     */
    public long getCloudFileManagerID() {
        return cloudFileManagerID;
    }

    /**
     * Sets the value of the cloudFileManagerID property.
     * 
     */
    public void setCloudFileManagerID(long value) {
        this.cloudFileManagerID = value;
    }

    /**
     * Gets the value of the compromiseDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompromiseDate() {
        return compromiseDate;
    }

    /**
     * Sets the value of the compromiseDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompromiseDate(String value) {
        this.compromiseDate = value;
    }

    /**
     * Gets the value of the csrRequired property.
     * 
     */
    public boolean isCsrRequired() {
        return csrRequired;
    }

    /**
     * Sets the value of the csrRequired property.
     * 
     */
    public void setCsrRequired(boolean value) {
        this.csrRequired = value;
    }

    /**
     * Gets the value of the currentPasscode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentPasscode() {
        return currentPasscode;
    }

    /**
     * Sets the value of the currentPasscode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentPasscode(String value) {
        this.currentPasscode = value;
    }

    /**
     * Gets the value of the declineReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeclineReason() {
        return declineReason;
    }

    /**
     * Sets the value of the declineReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeclineReason(String value) {
        this.declineReason = value;
    }

    /**
     * Gets the value of the downloadingFileUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDownloadingFileUUID() {
        return downloadingFileUUID;
    }

    /**
     * Sets the value of the downloadingFileUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDownloadingFileUUID(String value) {
        this.downloadingFileUUID = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the entityBillCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntityBillCode() {
        return entityBillCode;
    }

    /**
     * Sets the value of the entityBillCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntityBillCode(String value) {
        this.entityBillCode = value;
    }

    /**
     * Gets the value of the entityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * Sets the value of the entityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntityName(String value) {
        this.entityName = value;
    }

    /**
     * Gets the value of the fileProfile property.
     * 
     * @return
     *     possible object is
     *     {@link Profile }
     *     
     */
    public Profile getFileProfile() {
        return fileProfile;
    }

    /**
     * Sets the value of the fileProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link Profile }
     *     
     */
    public void setFileProfile(Profile value) {
        this.fileProfile = value;
    }

    /**
     * Gets the value of the keepCertificateSerialNumberEnabled property.
     * 
     */
    public boolean isKeepCertificateSerialNumberEnabled() {
        return keepCertificateSerialNumberEnabled;
    }

    /**
     * Sets the value of the keepCertificateSerialNumberEnabled property.
     * 
     */
    public void setKeepCertificateSerialNumberEnabled(boolean value) {
        this.keepCertificateSerialNumberEnabled = value;
    }

    /**
     * Gets the value of the keepKeysInHSMEnabled property.
     * 
     */
    public boolean isKeepKeysInHSMEnabled() {
        return keepKeysInHSMEnabled;
    }

    /**
     * Sets the value of the keepKeysInHSMEnabled property.
     * 
     */
    public void setKeepKeysInHSMEnabled(boolean value) {
        this.keepKeysInHSMEnabled = value;
    }

    /**
     * Gets the value of the keepOldKeysEnabled property.
     * 
     */
    public boolean isKeepOldKeysEnabled() {
        return keepOldKeysEnabled;
    }

    /**
     * Sets the value of the keepOldKeysEnabled property.
     * 
     */
    public void setKeepOldKeysEnabled(boolean value) {
        this.keepOldKeysEnabled = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguage(String value) {
        this.language = value;
    }

    /**
     * Gets the value of the logDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLogDate() {
        return logDate;
    }

    /**
     * Sets the value of the logDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLogDate(XMLGregorianCalendar value) {
        this.logDate = value;
    }

    /**
     * Gets the value of the logLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogLevel() {
        return logLevel;
    }

    /**
     * Sets the value of the logLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogLevel(String value) {
        this.logLevel = value;
    }

    /**
     * Gets the value of the logType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogType() {
        return logType;
    }

    /**
     * Sets the value of the logType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogType(String value) {
        this.logType = value;
    }

    /**
     * Gets the value of the mobileNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets the value of the mobileNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobileNo(String value) {
        this.mobileNo = value;
    }

    /**
     * Gets the value of the multipleSignature property.
     * 
     */
    public int getMultipleSignature() {
        return multipleSignature;
    }

    /**
     * Sets the value of the multipleSignature property.
     * 
     */
    public void setMultipleSignature(int value) {
        this.multipleSignature = value;
    }

    /**
     * Gets the value of the networkInterface property.
     * 
     * @return
     *     possible object is
     *     {@link NetworkInterface }
     *     
     */
    public NetworkInterface getNetworkInterface() {
        return networkInterface;
    }

    /**
     * Sets the value of the networkInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link NetworkInterface }
     *     
     */
    public void setNetworkInterface(NetworkInterface value) {
        this.networkInterface = value;
    }

    /**
     * Gets the value of the newPasscode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewPasscode() {
        return newPasscode;
    }

    /**
     * Sets the value of the newPasscode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewPasscode(String value) {
        this.newPasscode = value;
    }

    /**
     * Gets the value of the noSendPassCodeToEndUserEnabled property.
     * 
     */
    public boolean isNoSendPassCodeToEndUserEnabled() {
        return noSendPassCodeToEndUserEnabled;
    }

    /**
     * Sets the value of the noSendPassCodeToEndUserEnabled property.
     * 
     */
    public void setNoSendPassCodeToEndUserEnabled(boolean value) {
        this.noSendPassCodeToEndUserEnabled = value;
    }

    /**
     * Gets the value of the notificationSubject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotificationSubject() {
        return notificationSubject;
    }

    /**
     * Sets the value of the notificationSubject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotificationSubject(String value) {
        this.notificationSubject = value;
    }

    /**
     * Gets the value of the notificationTemplate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotificationTemplate() {
        return notificationTemplate;
    }

    /**
     * Sets the value of the notificationTemplate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotificationTemplate(String value) {
        this.notificationTemplate = value;
    }

    /**
     * Gets the value of the ntpConfig property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNtpConfig() {
        return ntpConfig;
    }

    /**
     * Sets the value of the ntpConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNtpConfig(String value) {
        this.ntpConfig = value;
    }

    /**
     * Gets the value of the ownerEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnerEmail() {
        return ownerEmail;
    }

    /**
     * Sets the value of the ownerEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnerEmail(String value) {
        this.ownerEmail = value;
    }

    /**
     * Gets the value of the ownerMobileNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnerMobileNo() {
        return ownerMobileNo;
    }

    /**
     * Sets the value of the ownerMobileNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnerMobileNo(String value) {
        this.ownerMobileNo = value;
    }

    /**
     * Gets the value of the ownerPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnerPassword() {
        return ownerPassword;
    }

    /**
     * Sets the value of the ownerPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnerPassword(String value) {
        this.ownerPassword = value;
    }

    /**
     * Gets the value of the ownerUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnerUUID() {
        return ownerUUID;
    }

    /**
     * Sets the value of the ownerUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnerUUID(String value) {
        this.ownerUUID = value;
    }

    /**
     * Gets the value of the ownerUsername property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnerUsername() {
        return ownerUsername;
    }

    /**
     * Sets the value of the ownerUsername property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnerUsername(String value) {
        this.ownerUsername = value;
    }

    /**
     * Gets the value of the p2PEnabled property.
     * 
     */
    public boolean isP2PEnabled() {
        return p2PEnabled;
    }

    /**
     * Sets the value of the p2PEnabled property.
     * 
     */
    public void setP2PEnabled(boolean value) {
        this.p2PEnabled = value;
    }

    /**
     * Gets the value of the passCodeNotificationMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassCodeNotificationMethod() {
        return passCodeNotificationMethod;
    }

    /**
     * Sets the value of the passCodeNotificationMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassCodeNotificationMethod(String value) {
        this.passCodeNotificationMethod = value;
    }

    /**
     * Gets the value of the passportID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassportID() {
        return passportID;
    }

    /**
     * Sets the value of the passportID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassportID(String value) {
        this.passportID = value;
    }

    /**
     * Gets the value of the personalID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonalID() {
        return personalID;
    }

    /**
     * Sets the value of the personalID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonalID(String value) {
        this.personalID = value;
    }

    /**
     * Gets the value of the raCertificationID property.
     * 
     */
    public int getRaCertificationID() {
        return raCertificationID;
    }

    /**
     * Sets the value of the raCertificationID property.
     * 
     */
    public void setRaCertificationID(int value) {
        this.raCertificationID = value;
    }

    /**
     * Gets the value of the registrationParty property.
     * 
     * @return
     *     possible object is
     *     {@link RegistrationParty }
     *     
     */
    public RegistrationParty getRegistrationParty() {
        return registrationParty;
    }

    /**
     * Sets the value of the registrationParty property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistrationParty }
     *     
     */
    public void setRegistrationParty(RegistrationParty value) {
        this.registrationParty = value;
    }

    /**
     * Gets the value of the relyingParty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelyingParty() {
        return relyingParty;
    }

    /**
     * Sets the value of the relyingParty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelyingParty(String value) {
        this.relyingParty = value;
    }

    /**
     * Gets the value of the relyingPartyBillCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelyingPartyBillCode() {
        return relyingPartyBillCode;
    }

    /**
     * Sets the value of the relyingPartyBillCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelyingPartyBillCode(String value) {
        this.relyingPartyBillCode = value;
    }

    /**
     * Gets the value of the requestType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Sets the value of the requestType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestType(String value) {
        this.requestType = value;
    }

    /**
     * Gets the value of the revokeOldCertificateEnabled property.
     * 
     */
    public boolean isRevokeOldCertificateEnabled() {
        return revokeOldCertificateEnabled;
    }

    /**
     * Sets the value of the revokeOldCertificateEnabled property.
     * 
     */
    public void setRevokeOldCertificateEnabled(boolean value) {
        this.revokeOldCertificateEnabled = value;
    }

    /**
     * Gets the value of the revokeReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRevokeReason() {
        return revokeReason;
    }

    /**
     * Sets the value of the revokeReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRevokeReason(String value) {
        this.revokeReason = value;
    }

    /**
     * Gets the value of the revokeReasonDetail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRevokeReasonDetail() {
        return revokeReasonDetail;
    }

    /**
     * Sets the value of the revokeReasonDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRevokeReasonDetail(String value) {
        this.revokeReasonDetail = value;
    }

    /**
     * Gets the value of the scal property.
     * 
     */
    public int getScal() {
        return scal;
    }

    /**
     * Sets the value of the scal property.
     * 
     */
    public void setScal(int value) {
        this.scal = value;
    }

    /**
     * Gets the value of the setOldCertificateToOperated property.
     * 
     */
    public boolean isSetOldCertificateToOperated() {
        return setOldCertificateToOperated;
    }

    /**
     * Sets the value of the setOldCertificateToOperated property.
     * 
     */
    public void setSetOldCertificateToOperated(boolean value) {
        this.setOldCertificateToOperated = value;
    }

    /**
     * Gets the value of the sharedAgreementUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSharedAgreementUUID() {
        return sharedAgreementUUID;
    }

    /**
     * Sets the value of the sharedAgreementUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSharedAgreementUUID(String value) {
        this.sharedAgreementUUID = value;
    }

    /**
     * Gets the value of the sharedMode property.
     * 
     */
    public int getSharedMode() {
        return sharedMode;
    }

    /**
     * Sets the value of the sharedMode property.
     * 
     */
    public void setSharedMode(int value) {
        this.sharedMode = value;
    }

    /**
     * Gets the value of the sharedModeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSharedModeType() {
        return sharedModeType;
    }

    /**
     * Sets the value of the sharedModeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSharedModeType(String value) {
        this.sharedModeType = value;
    }

    /**
     * Gets the value of the sharedRelyingParty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSharedRelyingParty() {
        return sharedRelyingParty;
    }

    /**
     * Sets the value of the sharedRelyingParty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSharedRelyingParty(String value) {
        this.sharedRelyingParty = value;
    }

    /**
     * Gets the value of the signDocument property.
     * 
     */
    public boolean isSignDocument() {
        return signDocument;
    }

    /**
     * Sets the value of the signDocument property.
     * 
     */
    public void setSignDocument(boolean value) {
        this.signDocument = value;
    }

    /**
     * Gets the value of the signingCounter property.
     * 
     */
    public int getSigningCounter() {
        return signingCounter;
    }

    /**
     * Sets the value of the signingCounter property.
     * 
     */
    public void setSigningCounter(int value) {
        this.signingCounter = value;
    }

    /**
     * Gets the value of the signingProfile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSigningProfile() {
        return signingProfile;
    }

    /**
     * Sets the value of the signingProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSigningProfile(String value) {
        this.signingProfile = value;
    }

    /**
     * Gets the value of the taxID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxID() {
        return taxID;
    }

    /**
     * Sets the value of the taxID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxID(String value) {
        this.taxID = value;
    }

    /**
     * Gets the value of the templateID property.
     * 
     */
    public int getTemplateID() {
        return templateID;
    }

    /**
     * Sets the value of the templateID property.
     * 
     */
    public void setTemplateID(int value) {
        this.templateID = value;
    }

    /**
     * Gets the value of the twoFactorMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTwoFactorMethod() {
        return twoFactorMethod;
    }

    /**
     * Sets the value of the twoFactorMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTwoFactorMethod(String value) {
        this.twoFactorMethod = value;
    }

    /**
     * Gets the value of the unblockReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnblockReason() {
        return unblockReason;
    }

    /**
     * Sets the value of the unblockReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnblockReason(String value) {
        this.unblockReason = value;
    }

    /**
     * Gets the value of the userID property.
     * 
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the value of the userID property.
     * 
     */
    public void setUserID(int value) {
        this.userID = value;
    }

    /**
     * Gets the value of the uuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the value of the uuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUuid(String value) {
        this.uuid = value;
    }

    /**
     * Gets the value of the waitForApproval property.
     * 
     */
    public boolean isWaitForApproval() {
        return waitForApproval;
    }

    /**
     * Sets the value of the waitForApproval property.
     * 
     */
    public void setWaitForApproval(boolean value) {
        this.waitForApproval = value;
    }

}
