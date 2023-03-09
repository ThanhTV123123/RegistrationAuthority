
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
 * <p>Java class for managementResp complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="managementResp">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="agreementUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="authMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="authModeSupported" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="authModes" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="authorizationMethodsEnabled" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="authorizeCredential" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificateDN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificateInfo" type="{http://management.esigncloud.mobileid.vn/}certificateInfo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="certificateSerialNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificateStateID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="certificateThumbprint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificateUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="clusterInfo" type="{http://management.esigncloud.mobileid.vn/}clusterInfo" minOccurs="0"/>
 *         &lt;element name="contractExpiration" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="cpuInfo" type="{http://management.esigncloud.mobileid.vn/}cpuInfo" minOccurs="0"/>
 *         &lt;element name="csr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="databaseClusterInfo" type="{http://management.esigncloud.mobileid.vn/}databaseClusterInfo" minOccurs="0"/>
 *         &lt;element name="diskUsage" type="{http://management.esigncloud.mobileid.vn/}diskUsage" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fileInfo" type="{http://management.esigncloud.mobileid.vn/}fileInfo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="issuerDN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="logFile" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="logInstance" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="logInstance2" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="logLevels" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="logTypes" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="memoryUsages" type="{http://management.esigncloud.mobileid.vn/}memoryUsage" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="messageDetails" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mobileNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="networkInterfaces" type="{http://management.esigncloud.mobileid.vn/}networkInterface" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ntpConfig" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ntpStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ownerInfo" type="{http://management.esigncloud.mobileid.vn/}ownerInfo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ownerUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="raCertificationID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="relyingParties" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="remainingCounter" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="remainingSigningCounter" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="responseCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="responseMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signingCounter" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="signingProfiles" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="signingProfilesNameAndValue" type="{http://management.esigncloud.mobileid.vn/}nameAndValue" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="systemInfo" type="{http://management.esigncloud.mobileid.vn/}systemInfo" minOccurs="0"/>
 *         &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="validFrom" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="validTo" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "managementResp", propOrder = {
    "agreementUUID",
    "authMode",
    "authModeSupported",
    "authModes",
    "authorizationMethodsEnabled",
    "authorizeCredential",
    "billCode",
    "certificate",
    "certificateDN",
    "certificateInfo",
    "certificateSerialNumber",
    "certificateStateID",
    "certificateThumbprint",
    "certificateUUID",
    "clusterInfo",
    "contractExpiration",
    "cpuInfo",
    "csr",
    "databaseClusterInfo",
    "diskUsage",
    "email",
    "fileInfo",
    "issuerDN",
    "logFile",
    "logInstance",
    "logInstance2",
    "logLevels",
    "logTypes",
    "memoryUsages",
    "messageDetails",
    "mobileNo",
    "networkInterfaces",
    "ntpConfig",
    "ntpStatus",
    "ownerInfo",
    "ownerUUID",
    "raCertificationID",
    "relyingParties",
    "remainingCounter",
    "remainingSigningCounter",
    "responseCode",
    "responseMessage",
    "signingCounter",
    "signingProfiles",
    "signingProfilesNameAndValue",
    "systemInfo",
    "timestamp",
    "validFrom",
    "validTo"
})
public class ManagementResp {

    protected String agreementUUID;
    protected String authMode;
    @XmlElement(nillable = true)
    protected List<String> authModeSupported;
    @XmlElement(nillable = true)
    protected List<String> authModes;
    @XmlElement(nillable = true)
    protected List<Integer> authorizationMethodsEnabled;
    protected String authorizeCredential;
    protected String billCode;
    protected String certificate;
    protected String certificateDN;
    @XmlElement(nillable = true)
    protected List<CertificateInfo> certificateInfo;
    protected String certificateSerialNumber;
    protected int certificateStateID;
    protected String certificateThumbprint;
    protected String certificateUUID;
    protected ClusterInfo clusterInfo;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar contractExpiration;
    protected CpuInfo cpuInfo;
    protected String csr;
    protected DatabaseClusterInfo databaseClusterInfo;
    @XmlElement(nillable = true)
    protected List<DiskUsage> diskUsage;
    protected String email;
    @XmlElement(nillable = true)
    protected List<FileInfo> fileInfo;
    protected String issuerDN;
    protected byte[] logFile;
    protected int logInstance;
    protected long logInstance2;
    @XmlElement(nillable = true)
    protected List<String> logLevels;
    @XmlElement(nillable = true)
    protected List<String> logTypes;
    @XmlElement(nillable = true)
    protected List<MemoryUsage> memoryUsages;
    protected String messageDetails;
    protected String mobileNo;
    @XmlElement(nillable = true)
    protected List<NetworkInterface> networkInterfaces;
    protected String ntpConfig;
    protected String ntpStatus;
    @XmlElement(nillable = true)
    protected List<OwnerInfo> ownerInfo;
    protected String ownerUUID;
    protected int raCertificationID;
    @XmlElement(nillable = true)
    protected List<String> relyingParties;
    protected int remainingCounter;
    protected int remainingSigningCounter;
    protected int responseCode;
    protected String responseMessage;
    protected int signingCounter;
    @XmlElement(nillable = true)
    protected List<String> signingProfiles;
    @XmlElement(nillable = true)
    protected List<NameAndValue> signingProfilesNameAndValue;
    protected SystemInfo systemInfo;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timestamp;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar validFrom;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar validTo;

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
     * Gets the value of the authModes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the authModes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthModes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAuthModes() {
        if (authModes == null) {
            authModes = new ArrayList<String>();
        }
        return this.authModes;
    }

    /**
     * Gets the value of the authorizationMethodsEnabled property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the authorizationMethodsEnabled property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthorizationMethodsEnabled().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getAuthorizationMethodsEnabled() {
        if (authorizationMethodsEnabled == null) {
            authorizationMethodsEnabled = new ArrayList<Integer>();
        }
        return this.authorizationMethodsEnabled;
    }

    /**
     * Gets the value of the authorizeCredential property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthorizeCredential() {
        return authorizeCredential;
    }

    /**
     * Sets the value of the authorizeCredential property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthorizeCredential(String value) {
        this.authorizeCredential = value;
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
     * Gets the value of the certificateDN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificateDN() {
        return certificateDN;
    }

    /**
     * Sets the value of the certificateDN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificateDN(String value) {
        this.certificateDN = value;
    }

    /**
     * Gets the value of the certificateInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the certificateInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCertificateInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CertificateInfo }
     * 
     * 
     */
    public List<CertificateInfo> getCertificateInfo() {
        if (certificateInfo == null) {
            certificateInfo = new ArrayList<CertificateInfo>();
        }
        return this.certificateInfo;
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
     * Gets the value of the certificateStateID property.
     * 
     */
    public int getCertificateStateID() {
        return certificateStateID;
    }

    /**
     * Sets the value of the certificateStateID property.
     * 
     */
    public void setCertificateStateID(int value) {
        this.certificateStateID = value;
    }

    /**
     * Gets the value of the certificateThumbprint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificateThumbprint() {
        return certificateThumbprint;
    }

    /**
     * Sets the value of the certificateThumbprint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificateThumbprint(String value) {
        this.certificateThumbprint = value;
    }

    /**
     * Gets the value of the certificateUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificateUUID() {
        return certificateUUID;
    }

    /**
     * Sets the value of the certificateUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificateUUID(String value) {
        this.certificateUUID = value;
    }

    /**
     * Gets the value of the clusterInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ClusterInfo }
     *     
     */
    public ClusterInfo getClusterInfo() {
        return clusterInfo;
    }

    /**
     * Sets the value of the clusterInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClusterInfo }
     *     
     */
    public void setClusterInfo(ClusterInfo value) {
        this.clusterInfo = value;
    }

    /**
     * Gets the value of the contractExpiration property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getContractExpiration() {
        return contractExpiration;
    }

    /**
     * Sets the value of the contractExpiration property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setContractExpiration(XMLGregorianCalendar value) {
        this.contractExpiration = value;
    }

    /**
     * Gets the value of the cpuInfo property.
     * 
     * @return
     *     possible object is
     *     {@link CpuInfo }
     *     
     */
    public CpuInfo getCpuInfo() {
        return cpuInfo;
    }

    /**
     * Sets the value of the cpuInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link CpuInfo }
     *     
     */
    public void setCpuInfo(CpuInfo value) {
        this.cpuInfo = value;
    }

    /**
     * Gets the value of the csr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCsr() {
        return csr;
    }

    /**
     * Sets the value of the csr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCsr(String value) {
        this.csr = value;
    }

    /**
     * Gets the value of the databaseClusterInfo property.
     * 
     * @return
     *     possible object is
     *     {@link DatabaseClusterInfo }
     *     
     */
    public DatabaseClusterInfo getDatabaseClusterInfo() {
        return databaseClusterInfo;
    }

    /**
     * Sets the value of the databaseClusterInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatabaseClusterInfo }
     *     
     */
    public void setDatabaseClusterInfo(DatabaseClusterInfo value) {
        this.databaseClusterInfo = value;
    }

    /**
     * Gets the value of the diskUsage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the diskUsage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDiskUsage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DiskUsage }
     * 
     * 
     */
    public List<DiskUsage> getDiskUsage() {
        if (diskUsage == null) {
            diskUsage = new ArrayList<DiskUsage>();
        }
        return this.diskUsage;
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
     * Gets the value of the fileInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fileInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFileInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FileInfo }
     * 
     * 
     */
    public List<FileInfo> getFileInfo() {
        if (fileInfo == null) {
            fileInfo = new ArrayList<FileInfo>();
        }
        return this.fileInfo;
    }

    /**
     * Gets the value of the issuerDN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuerDN() {
        return issuerDN;
    }

    /**
     * Sets the value of the issuerDN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuerDN(String value) {
        this.issuerDN = value;
    }

    /**
     * Gets the value of the logFile property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getLogFile() {
        return logFile;
    }

    /**
     * Sets the value of the logFile property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setLogFile(byte[] value) {
        this.logFile = value;
    }

    /**
     * Gets the value of the logInstance property.
     * 
     */
    public int getLogInstance() {
        return logInstance;
    }

    /**
     * Sets the value of the logInstance property.
     * 
     */
    public void setLogInstance(int value) {
        this.logInstance = value;
    }

    /**
     * Gets the value of the logInstance2 property.
     * 
     */
    public long getLogInstance2() {
        return logInstance2;
    }

    /**
     * Sets the value of the logInstance2 property.
     * 
     */
    public void setLogInstance2(long value) {
        this.logInstance2 = value;
    }

    /**
     * Gets the value of the logLevels property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the logLevels property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLogLevels().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getLogLevels() {
        if (logLevels == null) {
            logLevels = new ArrayList<String>();
        }
        return this.logLevels;
    }

    /**
     * Gets the value of the logTypes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the logTypes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLogTypes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getLogTypes() {
        if (logTypes == null) {
            logTypes = new ArrayList<String>();
        }
        return this.logTypes;
    }

    /**
     * Gets the value of the memoryUsages property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the memoryUsages property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMemoryUsages().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MemoryUsage }
     * 
     * 
     */
    public List<MemoryUsage> getMemoryUsages() {
        if (memoryUsages == null) {
            memoryUsages = new ArrayList<MemoryUsage>();
        }
        return this.memoryUsages;
    }

    /**
     * Gets the value of the messageDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageDetails() {
        return messageDetails;
    }

    /**
     * Sets the value of the messageDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageDetails(String value) {
        this.messageDetails = value;
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
     * Gets the value of the networkInterfaces property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the networkInterfaces property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNetworkInterfaces().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NetworkInterface }
     * 
     * 
     */
    public List<NetworkInterface> getNetworkInterfaces() {
        if (networkInterfaces == null) {
            networkInterfaces = new ArrayList<NetworkInterface>();
        }
        return this.networkInterfaces;
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
     * Gets the value of the ntpStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNtpStatus() {
        return ntpStatus;
    }

    /**
     * Sets the value of the ntpStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNtpStatus(String value) {
        this.ntpStatus = value;
    }

    /**
     * Gets the value of the ownerInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ownerInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOwnerInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OwnerInfo }
     * 
     * 
     */
    public List<OwnerInfo> getOwnerInfo() {
        if (ownerInfo == null) {
            ownerInfo = new ArrayList<OwnerInfo>();
        }
        return this.ownerInfo;
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
     * Gets the value of the relyingParties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the relyingParties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelyingParties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getRelyingParties() {
        if (relyingParties == null) {
            relyingParties = new ArrayList<String>();
        }
        return this.relyingParties;
    }

    /**
     * Gets the value of the remainingCounter property.
     * 
     */
    public int getRemainingCounter() {
        return remainingCounter;
    }

    /**
     * Sets the value of the remainingCounter property.
     * 
     */
    public void setRemainingCounter(int value) {
        this.remainingCounter = value;
    }

    /**
     * Gets the value of the remainingSigningCounter property.
     * 
     */
    public int getRemainingSigningCounter() {
        return remainingSigningCounter;
    }

    /**
     * Sets the value of the remainingSigningCounter property.
     * 
     */
    public void setRemainingSigningCounter(int value) {
        this.remainingSigningCounter = value;
    }

    /**
     * Gets the value of the responseCode property.
     * 
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Sets the value of the responseCode property.
     * 
     */
    public void setResponseCode(int value) {
        this.responseCode = value;
    }

    /**
     * Gets the value of the responseMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * Sets the value of the responseMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseMessage(String value) {
        this.responseMessage = value;
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
     * Gets the value of the signingProfiles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signingProfiles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSigningProfiles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSigningProfiles() {
        if (signingProfiles == null) {
            signingProfiles = new ArrayList<String>();
        }
        return this.signingProfiles;
    }

    /**
     * Gets the value of the signingProfilesNameAndValue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signingProfilesNameAndValue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSigningProfilesNameAndValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NameAndValue }
     * 
     * 
     */
    public List<NameAndValue> getSigningProfilesNameAndValue() {
        if (signingProfilesNameAndValue == null) {
            signingProfilesNameAndValue = new ArrayList<NameAndValue>();
        }
        return this.signingProfilesNameAndValue;
    }

    /**
     * Gets the value of the systemInfo property.
     * 
     * @return
     *     possible object is
     *     {@link SystemInfo }
     *     
     */
    public SystemInfo getSystemInfo() {
        return systemInfo;
    }

    /**
     * Sets the value of the systemInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link SystemInfo }
     *     
     */
    public void setSystemInfo(SystemInfo value) {
        this.systemInfo = value;
    }

    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimestamp(XMLGregorianCalendar value) {
        this.timestamp = value;
    }

    /**
     * Gets the value of the validFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getValidFrom() {
        return validFrom;
    }

    /**
     * Sets the value of the validFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setValidFrom(XMLGregorianCalendar value) {
        this.validFrom = value;
    }

    /**
     * Gets the value of the validTo property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getValidTo() {
        return validTo;
    }

    /**
     * Sets the value of the validTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setValidTo(XMLGregorianCalendar value) {
        this.validTo = value;
    }

}
