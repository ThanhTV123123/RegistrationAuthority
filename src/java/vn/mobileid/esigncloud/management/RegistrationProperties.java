
package vn.mobileid.esigncloud.management;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for registrationProperties complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="registrationProperties">
 *   &lt;complexContent>
 *     &lt;extension base="{http://management.esigncloud.mobileid.vn/}attributes">
 *       &lt;sequence>
 *         &lt;element name="certificateProfiles" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registrationProperties", propOrder = {
    "certificateProfiles"
})
public class RegistrationProperties
    extends Attributes
{

    @XmlElement(nillable = true)
    protected List<String> certificateProfiles;

    /**
     * Gets the value of the certificateProfiles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the certificateProfiles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCertificateProfiles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCertificateProfiles() {
        if (certificateProfiles == null) {
            certificateProfiles = new ArrayList<String>();
        }
        return this.certificateProfiles;
    }

}
