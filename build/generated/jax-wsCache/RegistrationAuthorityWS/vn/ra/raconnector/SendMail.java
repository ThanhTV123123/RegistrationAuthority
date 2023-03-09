
package vn.ra.raconnector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sendMail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sendMail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="raRequest" type="{http://raservice.mobileid.vn/}registrationAuthorityWSRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendMail", propOrder = {
    "raRequest"
})
public class SendMail {

    protected RegistrationAuthorityWSRequest raRequest;

    /**
     * Gets the value of the raRequest property.
     * 
     * @return
     *     possible object is
     *     {@link RegistrationAuthorityWSRequest }
     *     
     */
    public RegistrationAuthorityWSRequest getRaRequest() {
        return raRequest;
    }

    /**
     * Sets the value of the raRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistrationAuthorityWSRequest }
     *     
     */
    public void setRaRequest(RegistrationAuthorityWSRequest value) {
        this.raRequest = value;
    }

}
