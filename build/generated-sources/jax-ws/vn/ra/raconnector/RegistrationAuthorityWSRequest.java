
package vn.ra.raconnector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for registrationAuthorityWSRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="registrationAuthorityWSRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="base64Cert" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="caName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certSn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificationAttrId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="certificationId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="compromiseDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enterpriseInfo" type="{http://raservice.mobileid.vn/}enterpriseInfo" minOccurs="0"/>
 *         &lt;element name="fileManagerId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="keystorePassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="keystoreType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mailComponent" type="{http://raservice.mobileid.vn/}mailComponent" minOccurs="0"/>
 *         &lt;element name="phone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="raPortalUser" type="{http://raservice.mobileid.vn/}raPortalUser" minOccurs="0"/>
 *         &lt;element name="revokeReason" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="sendEmailEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="serialNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sopin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="thumbprint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tokenSn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registrationAuthorityWSRequest", propOrder = {
    "base64Cert",
    "caName",
    "certSn",
    "certificationAttrId",
    "certificationId",
    "compromiseDate",
    "email",
    "enterpriseInfo",
    "fileManagerId",
    "keystorePassword",
    "keystoreType",
    "mailComponent",
    "phone",
    "raPortalUser",
    "revokeReason",
    "sendEmailEnabled",
    "serialNumber",
    "sopin",
    "thumbprint",
    "tokenSn"
})
public class RegistrationAuthorityWSRequest {

    protected String base64Cert;
    protected String caName;
    protected String certSn;
    protected Integer certificationAttrId;
    protected Integer certificationId;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar compromiseDate;
    protected String email;
    protected EnterpriseInfo enterpriseInfo;
    protected Integer fileManagerId;
    protected String keystorePassword;
    protected String keystoreType;
    protected MailComponent mailComponent;
    protected String phone;
    protected RaPortalUser raPortalUser;
    protected int revokeReason;
    protected boolean sendEmailEnabled;
    protected String serialNumber;
    protected String sopin;
    protected String thumbprint;
    protected String tokenSn;

    /**
     * Gets the value of the base64Cert property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBase64Cert() {
        return base64Cert;
    }

    /**
     * Sets the value of the base64Cert property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBase64Cert(String value) {
        this.base64Cert = value;
    }

    /**
     * Gets the value of the caName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaName() {
        return caName;
    }

    /**
     * Sets the value of the caName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaName(String value) {
        this.caName = value;
    }

    /**
     * Gets the value of the certSn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertSn() {
        return certSn;
    }

    /**
     * Sets the value of the certSn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertSn(String value) {
        this.certSn = value;
    }

    /**
     * Gets the value of the certificationAttrId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCertificationAttrId() {
        return certificationAttrId;
    }

    /**
     * Sets the value of the certificationAttrId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCertificationAttrId(Integer value) {
        this.certificationAttrId = value;
    }

    /**
     * Gets the value of the certificationId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCertificationId() {
        return certificationId;
    }

    /**
     * Sets the value of the certificationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCertificationId(Integer value) {
        this.certificationId = value;
    }

    /**
     * Gets the value of the compromiseDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCompromiseDate() {
        return compromiseDate;
    }

    /**
     * Sets the value of the compromiseDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCompromiseDate(XMLGregorianCalendar value) {
        this.compromiseDate = value;
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
     * Gets the value of the enterpriseInfo property.
     * 
     * @return
     *     possible object is
     *     {@link EnterpriseInfo }
     *     
     */
    public EnterpriseInfo getEnterpriseInfo() {
        return enterpriseInfo;
    }

    /**
     * Sets the value of the enterpriseInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnterpriseInfo }
     *     
     */
    public void setEnterpriseInfo(EnterpriseInfo value) {
        this.enterpriseInfo = value;
    }

    /**
     * Gets the value of the fileManagerId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFileManagerId() {
        return fileManagerId;
    }

    /**
     * Sets the value of the fileManagerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFileManagerId(Integer value) {
        this.fileManagerId = value;
    }

    /**
     * Gets the value of the keystorePassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeystorePassword() {
        return keystorePassword;
    }

    /**
     * Sets the value of the keystorePassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeystorePassword(String value) {
        this.keystorePassword = value;
    }

    /**
     * Gets the value of the keystoreType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeystoreType() {
        return keystoreType;
    }

    /**
     * Sets the value of the keystoreType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeystoreType(String value) {
        this.keystoreType = value;
    }

    /**
     * Gets the value of the mailComponent property.
     * 
     * @return
     *     possible object is
     *     {@link MailComponent }
     *     
     */
    public MailComponent getMailComponent() {
        return mailComponent;
    }

    /**
     * Sets the value of the mailComponent property.
     * 
     * @param value
     *     allowed object is
     *     {@link MailComponent }
     *     
     */
    public void setMailComponent(MailComponent value) {
        this.mailComponent = value;
    }

    /**
     * Gets the value of the phone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the value of the phone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhone(String value) {
        this.phone = value;
    }

    /**
     * Gets the value of the raPortalUser property.
     * 
     * @return
     *     possible object is
     *     {@link RaPortalUser }
     *     
     */
    public RaPortalUser getRaPortalUser() {
        return raPortalUser;
    }

    /**
     * Sets the value of the raPortalUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link RaPortalUser }
     *     
     */
    public void setRaPortalUser(RaPortalUser value) {
        this.raPortalUser = value;
    }

    /**
     * Gets the value of the revokeReason property.
     * 
     */
    public int getRevokeReason() {
        return revokeReason;
    }

    /**
     * Sets the value of the revokeReason property.
     * 
     */
    public void setRevokeReason(int value) {
        this.revokeReason = value;
    }

    /**
     * Gets the value of the sendEmailEnabled property.
     * 
     */
    public boolean isSendEmailEnabled() {
        return sendEmailEnabled;
    }

    /**
     * Sets the value of the sendEmailEnabled property.
     * 
     */
    public void setSendEmailEnabled(boolean value) {
        this.sendEmailEnabled = value;
    }

    /**
     * Gets the value of the serialNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets the value of the serialNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerialNumber(String value) {
        this.serialNumber = value;
    }

    /**
     * Gets the value of the sopin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSopin() {
        return sopin;
    }

    /**
     * Sets the value of the sopin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSopin(String value) {
        this.sopin = value;
    }

    /**
     * Gets the value of the thumbprint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThumbprint() {
        return thumbprint;
    }

    /**
     * Sets the value of the thumbprint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThumbprint(String value) {
        this.thumbprint = value;
    }

    /**
     * Gets the value of the tokenSn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTokenSn() {
        return tokenSn;
    }

    /**
     * Sets the value of the tokenSn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTokenSn(String value) {
        this.tokenSn = value;
    }

}
