
package vn.ra.raconnector;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for registrationAuthorityWSResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="registrationAuthorityWSResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="certifications" type="{http://raservice.mobileid.vn/}certification" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="enterpriseInfo" type="{http://raservice.mobileid.vn/}enterpriseInfo" minOccurs="0"/>
 *         &lt;element name="keystore" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="responseCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="responseMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sopin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sopinEncrypted" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tmsVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "registrationAuthorityWSResponse", propOrder = {
    "certifications",
    "enterpriseInfo",
    "keystore",
    "responseCode",
    "responseMessage",
    "sopin",
    "sopinEncrypted",
    "tmsVersion",
    "tokenSn"
})
public class RegistrationAuthorityWSResponse {

    @XmlElement(nillable = true)
    protected List<Certification> certifications;
    protected EnterpriseInfo enterpriseInfo;
    protected byte[] keystore;
    protected int responseCode;
    protected String responseMessage;
    protected String sopin;
    protected String sopinEncrypted;
    protected String tmsVersion;
    protected String tokenSn;

    /**
     * Gets the value of the certifications property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the certifications property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCertifications().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Certification }
     * 
     * 
     */
    public List<Certification> getCertifications() {
        if (certifications == null) {
            certifications = new ArrayList<Certification>();
        }
        return this.certifications;
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
     * Gets the value of the keystore property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getKeystore() {
        return keystore;
    }

    /**
     * Sets the value of the keystore property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setKeystore(byte[] value) {
        this.keystore = value;
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
     * Gets the value of the sopinEncrypted property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSopinEncrypted() {
        return sopinEncrypted;
    }

    /**
     * Sets the value of the sopinEncrypted property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSopinEncrypted(String value) {
        this.sopinEncrypted = value;
    }

    /**
     * Gets the value of the tmsVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTmsVersion() {
        return tmsVersion;
    }

    /**
     * Sets the value of the tmsVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTmsVersion(String value) {
        this.tmsVersion = value;
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
