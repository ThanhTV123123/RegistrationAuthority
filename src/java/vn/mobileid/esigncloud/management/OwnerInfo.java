
package vn.mobileid.esigncloud.management;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ownerInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ownerInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="budgetID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="citizenID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="companyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oath2Credentials" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="passportID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="personalID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="personalName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="phone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="taxID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uuid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ownerInfo", propOrder = {
    "budgetID",
    "citizenID",
    "companyName",
    "email",
    "oath2Credentials",
    "passportID",
    "personalID",
    "personalName",
    "phone",
    "taxID",
    "type",
    "username",
    "uuid"
})
public class OwnerInfo {

    protected String budgetID;
    protected String citizenID;
    protected String companyName;
    protected String email;
    protected String oath2Credentials;
    protected String passportID;
    protected String personalID;
    protected String personalName;
    protected String phone;
    protected String taxID;
    protected String type;
    protected String username;
    protected String uuid;

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
     * Gets the value of the companyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the value of the companyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyName(String value) {
        this.companyName = value;
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
     * Gets the value of the oath2Credentials property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOath2Credentials() {
        return oath2Credentials;
    }

    /**
     * Sets the value of the oath2Credentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOath2Credentials(String value) {
        this.oath2Credentials = value;
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
     * Gets the value of the personalName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonalName() {
        return personalName;
    }

    /**
     * Sets the value of the personalName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonalName(String value) {
        this.personalName = value;
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
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
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

}
