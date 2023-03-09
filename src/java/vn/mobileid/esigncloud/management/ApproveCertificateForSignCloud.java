
package vn.mobileid.esigncloud.management;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for approveCertificateForSignCloud complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="approveCertificateForSignCloud">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="managementReq" type="{http://management.esigncloud.mobileid.vn/}managementReq" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "approveCertificateForSignCloud", propOrder = {
    "managementReq"
})
public class ApproveCertificateForSignCloud {

    protected ManagementReq managementReq;

    /**
     * Gets the value of the managementReq property.
     * 
     * @return
     *     possible object is
     *     {@link ManagementReq }
     *     
     */
    public ManagementReq getManagementReq() {
        return managementReq;
    }

    /**
     * Sets the value of the managementReq property.
     * 
     * @param value
     *     allowed object is
     *     {@link ManagementReq }
     *     
     */
    public void setManagementReq(ManagementReq value) {
        this.managementReq = value;
    }

}
