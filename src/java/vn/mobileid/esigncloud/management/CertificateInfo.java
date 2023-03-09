
package vn.mobileid.esigncloud.management;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for certificateInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="certificateInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="agreementUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificateDN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificateSerialNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificateStateID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="certificateThumbprint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cloudCertificateID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="contractExpiration" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="issuerDN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="remainingSigningCounter" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
@XmlType(name = "certificateInfo", propOrder = {
    "agreementUUID",
    "certificate",
    "certificateDN",
    "certificateSerialNumber",
    "certificateStateID",
    "certificateThumbprint",
    "cloudCertificateID",
    "contractExpiration",
    "issuerDN",
    "remainingSigningCounter",
    "validFrom",
    "validTo"
})
public class CertificateInfo {

    protected String agreementUUID;
    protected String certificate;
    protected String certificateDN;
    protected String certificateSerialNumber;
    protected int certificateStateID;
    protected String certificateThumbprint;
    protected long cloudCertificateID;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar contractExpiration;
    protected String issuerDN;
    protected int remainingSigningCounter;
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
     * Gets the value of the cloudCertificateID property.
     * 
     */
    public long getCloudCertificateID() {
        return cloudCertificateID;
    }

    /**
     * Sets the value of the cloudCertificateID property.
     * 
     */
    public void setCloudCertificateID(long value) {
        this.cloudCertificateID = value;
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
