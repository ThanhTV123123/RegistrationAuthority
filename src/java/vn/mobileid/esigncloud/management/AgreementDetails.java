
package vn.mobileid.esigncloud.management;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for agreementDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="agreementDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="applicationForm" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="authorizeLetter" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="budgetID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="businessLicense" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="citizenID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="country" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="decisionNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="givenName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="location" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="organization" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="organizationUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="organizationUnit2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="organizationUnit3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="organizationUnit4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="passportID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="personalID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="personalName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="photoActivityDeclaration" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="photoAuthorizeDelegate" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="photoBackSideIDCard" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="photoFrontSideIDCard" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="photoIDCard" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="requestForm" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="rfc822Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="socialInsuranceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stateOrProvince" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="taxID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telephoneNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unitCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "agreementDetails", propOrder = {
    "applicationForm",
    "authorizeLetter",
    "budgetID",
    "businessLicense",
    "citizenID",
    "country",
    "decisionNumber",
    "email",
    "givenName",
    "location",
    "organization",
    "organizationUnit",
    "organizationUnit2",
    "organizationUnit3",
    "organizationUnit4",
    "passportID",
    "personalID",
    "personalName",
    "photoActivityDeclaration",
    "photoAuthorizeDelegate",
    "photoBackSideIDCard",
    "photoFrontSideIDCard",
    "photoIDCard",
    "requestForm",
    "rfc822Name",
    "socialInsuranceNumber",
    "stateOrProvince",
    "taxID",
    "telephoneNumber",
    "title",
    "unitCode"
})
public class AgreementDetails {

    protected byte[] applicationForm;
    protected byte[] authorizeLetter;
    protected String budgetID;
    protected byte[] businessLicense;
    protected String citizenID;
    protected String country;
    protected String decisionNumber;
    protected String email;
    protected String givenName;
    protected String location;
    protected String organization;
    protected String organizationUnit;
    protected String organizationUnit2;
    protected String organizationUnit3;
    protected String organizationUnit4;
    protected String passportID;
    protected String personalID;
    protected String personalName;
    protected byte[] photoActivityDeclaration;
    protected byte[] photoAuthorizeDelegate;
    protected byte[] photoBackSideIDCard;
    protected byte[] photoFrontSideIDCard;
    protected byte[] photoIDCard;
    protected byte[] requestForm;
    protected String rfc822Name;
    protected String socialInsuranceNumber;
    protected String stateOrProvince;
    protected String taxID;
    protected String telephoneNumber;
    protected String title;
    protected String unitCode;

    /**
     * Gets the value of the applicationForm property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getApplicationForm() {
        return applicationForm;
    }

    /**
     * Sets the value of the applicationForm property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setApplicationForm(byte[] value) {
        this.applicationForm = value;
    }

    /**
     * Gets the value of the authorizeLetter property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getAuthorizeLetter() {
        return authorizeLetter;
    }

    /**
     * Sets the value of the authorizeLetter property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setAuthorizeLetter(byte[] value) {
        this.authorizeLetter = value;
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
     * Gets the value of the businessLicense property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBusinessLicense() {
        return businessLicense;
    }

    /**
     * Sets the value of the businessLicense property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBusinessLicense(byte[] value) {
        this.businessLicense = value;
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
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the decisionNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDecisionNumber() {
        return decisionNumber;
    }

    /**
     * Sets the value of the decisionNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDecisionNumber(String value) {
        this.decisionNumber = value;
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
     * Gets the value of the givenName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Sets the value of the givenName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGivenName(String value) {
        this.givenName = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * Gets the value of the organization property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Sets the value of the organization property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganization(String value) {
        this.organization = value;
    }

    /**
     * Gets the value of the organizationUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganizationUnit() {
        return organizationUnit;
    }

    /**
     * Sets the value of the organizationUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganizationUnit(String value) {
        this.organizationUnit = value;
    }

    /**
     * Gets the value of the organizationUnit2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganizationUnit2() {
        return organizationUnit2;
    }

    /**
     * Sets the value of the organizationUnit2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganizationUnit2(String value) {
        this.organizationUnit2 = value;
    }

    /**
     * Gets the value of the organizationUnit3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganizationUnit3() {
        return organizationUnit3;
    }

    /**
     * Sets the value of the organizationUnit3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganizationUnit3(String value) {
        this.organizationUnit3 = value;
    }

    /**
     * Gets the value of the organizationUnit4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganizationUnit4() {
        return organizationUnit4;
    }

    /**
     * Sets the value of the organizationUnit4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganizationUnit4(String value) {
        this.organizationUnit4 = value;
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
     * Gets the value of the photoActivityDeclaration property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPhotoActivityDeclaration() {
        return photoActivityDeclaration;
    }

    /**
     * Sets the value of the photoActivityDeclaration property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPhotoActivityDeclaration(byte[] value) {
        this.photoActivityDeclaration = value;
    }

    /**
     * Gets the value of the photoAuthorizeDelegate property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPhotoAuthorizeDelegate() {
        return photoAuthorizeDelegate;
    }

    /**
     * Sets the value of the photoAuthorizeDelegate property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPhotoAuthorizeDelegate(byte[] value) {
        this.photoAuthorizeDelegate = value;
    }

    /**
     * Gets the value of the photoBackSideIDCard property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPhotoBackSideIDCard() {
        return photoBackSideIDCard;
    }

    /**
     * Sets the value of the photoBackSideIDCard property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPhotoBackSideIDCard(byte[] value) {
        this.photoBackSideIDCard = value;
    }

    /**
     * Gets the value of the photoFrontSideIDCard property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPhotoFrontSideIDCard() {
        return photoFrontSideIDCard;
    }

    /**
     * Sets the value of the photoFrontSideIDCard property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPhotoFrontSideIDCard(byte[] value) {
        this.photoFrontSideIDCard = value;
    }

    /**
     * Gets the value of the photoIDCard property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPhotoIDCard() {
        return photoIDCard;
    }

    /**
     * Sets the value of the photoIDCard property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPhotoIDCard(byte[] value) {
        this.photoIDCard = value;
    }

    /**
     * Gets the value of the requestForm property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getRequestForm() {
        return requestForm;
    }

    /**
     * Sets the value of the requestForm property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setRequestForm(byte[] value) {
        this.requestForm = value;
    }

    /**
     * Gets the value of the rfc822Name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfc822Name() {
        return rfc822Name;
    }

    /**
     * Sets the value of the rfc822Name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfc822Name(String value) {
        this.rfc822Name = value;
    }

    /**
     * Gets the value of the socialInsuranceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSocialInsuranceNumber() {
        return socialInsuranceNumber;
    }

    /**
     * Sets the value of the socialInsuranceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSocialInsuranceNumber(String value) {
        this.socialInsuranceNumber = value;
    }

    /**
     * Gets the value of the stateOrProvince property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateOrProvince() {
        return stateOrProvince;
    }

    /**
     * Sets the value of the stateOrProvince property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateOrProvince(String value) {
        this.stateOrProvince = value;
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
     * Gets the value of the telephoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * Sets the value of the telephoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber(String value) {
        this.telephoneNumber = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the unitCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitCode() {
        return unitCode;
    }

    /**
     * Sets the value of the unitCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitCode(String value) {
        this.unitCode = value;
    }

}
