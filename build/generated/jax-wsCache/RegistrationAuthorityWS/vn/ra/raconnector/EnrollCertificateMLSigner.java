
package vn.ra.raconnector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for enrollCertificateMLSigner complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="enrollCertificateMLSigner">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="registrationAuthorityWSRequest" type="{http://raservice.mobileid.vn/}registrationAuthorityWSRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enrollCertificateMLSigner", propOrder = {
    "registrationAuthorityWSRequest"
})
public class EnrollCertificateMLSigner {

    protected RegistrationAuthorityWSRequest registrationAuthorityWSRequest;

    /**
     * Gets the value of the registrationAuthorityWSRequest property.
     * 
     * @return
     *     possible object is
     *     {@link RegistrationAuthorityWSRequest }
     *     
     */
    public RegistrationAuthorityWSRequest getRegistrationAuthorityWSRequest() {
        return registrationAuthorityWSRequest;
    }

    /**
     * Sets the value of the registrationAuthorityWSRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistrationAuthorityWSRequest }
     *     
     */
    public void setRegistrationAuthorityWSRequest(RegistrationAuthorityWSRequest value) {
        this.registrationAuthorityWSRequest = value;
    }

}