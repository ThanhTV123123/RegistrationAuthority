
package vn.ra.raconnector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for certification complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="certification">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="certSn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certification" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="emailContract" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="phoneContract" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="publicKeyHash" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "certification", propOrder = {
    "certSn",
    "certification",
    "emailContract",
    "id",
    "phoneContract",
    "publicKeyHash"
})
public class Certification {

    protected String certSn;
    protected String certification;
    protected String emailContract;
    protected Integer id;
    protected String phoneContract;
    protected String publicKeyHash;

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
     * Gets the value of the certification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertification() {
        return certification;
    }

    /**
     * Sets the value of the certification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertification(String value) {
        this.certification = value;
    }

    /**
     * Gets the value of the emailContract property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailContract() {
        return emailContract;
    }

    /**
     * Sets the value of the emailContract property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailContract(String value) {
        this.emailContract = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Gets the value of the phoneContract property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhoneContract() {
        return phoneContract;
    }

    /**
     * Sets the value of the phoneContract property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhoneContract(String value) {
        this.phoneContract = value;
    }

    /**
     * Gets the value of the publicKeyHash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublicKeyHash() {
        return publicKeyHash;
    }

    /**
     * Sets the value of the publicKeyHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublicKeyHash(String value) {
        this.publicKeyHash = value;
    }

}
