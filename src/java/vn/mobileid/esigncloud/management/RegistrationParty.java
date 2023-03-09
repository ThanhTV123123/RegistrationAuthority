
package vn.mobileid.esigncloud.management;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for registrationParty complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="registrationParty">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="defaultBy" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="managedBy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="provinceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="registrationPartyID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="registrationProperties" type="{http://management.esigncloud.mobileid.vn/}registrationProperties" minOccurs="0"/>
 *         &lt;element name="remark" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="remarkEn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uri" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registrationParty", propOrder = {
    "defaultBy",
    "managedBy",
    "name",
    "provinceName",
    "registrationPartyID",
    "registrationProperties",
    "remark",
    "remarkEn",
    "uri"
})
public class RegistrationParty {

    protected int defaultBy;
    protected String managedBy;
    protected String name;
    protected String provinceName;
    protected int registrationPartyID;
    protected RegistrationProperties registrationProperties;
    protected String remark;
    protected String remarkEn;
    protected String uri;

    /**
     * Gets the value of the defaultBy property.
     * 
     */
    public int getDefaultBy() {
        return defaultBy;
    }

    /**
     * Sets the value of the defaultBy property.
     * 
     */
    public void setDefaultBy(int value) {
        this.defaultBy = value;
    }

    /**
     * Gets the value of the managedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManagedBy() {
        return managedBy;
    }

    /**
     * Sets the value of the managedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManagedBy(String value) {
        this.managedBy = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the provinceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvinceName() {
        return provinceName;
    }

    /**
     * Sets the value of the provinceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvinceName(String value) {
        this.provinceName = value;
    }

    /**
     * Gets the value of the registrationPartyID property.
     * 
     */
    public int getRegistrationPartyID() {
        return registrationPartyID;
    }

    /**
     * Sets the value of the registrationPartyID property.
     * 
     */
    public void setRegistrationPartyID(int value) {
        this.registrationPartyID = value;
    }

    /**
     * Gets the value of the registrationProperties property.
     * 
     * @return
     *     possible object is
     *     {@link RegistrationProperties }
     *     
     */
    public RegistrationProperties getRegistrationProperties() {
        return registrationProperties;
    }

    /**
     * Sets the value of the registrationProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistrationProperties }
     *     
     */
    public void setRegistrationProperties(RegistrationProperties value) {
        this.registrationProperties = value;
    }

    /**
     * Gets the value of the remark property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemark() {
        return remark;
    }

    /**
     * Sets the value of the remark property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemark(String value) {
        this.remark = value;
    }

    /**
     * Gets the value of the remarkEn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemarkEn() {
        return remarkEn;
    }

    /**
     * Sets the value of the remarkEn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemarkEn(String value) {
        this.remarkEn = value;
    }

    /**
     * Gets the value of the uri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUri() {
        return uri;
    }

    /**
     * Sets the value of the uri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUri(String value) {
        this.uri = value;
    }

}
