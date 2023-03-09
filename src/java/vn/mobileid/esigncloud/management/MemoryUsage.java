
package vn.mobileid.esigncloud.management;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for memoryUsage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="memoryUsage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="free" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memoryType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="total" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="used" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "memoryUsage", propOrder = {
    "free",
    "memoryType",
    "total",
    "used"
})
public class MemoryUsage {

    protected String free;
    protected String memoryType;
    protected String total;
    protected String used;

    /**
     * Gets the value of the free property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFree() {
        return free;
    }

    /**
     * Sets the value of the free property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFree(String value) {
        this.free = value;
    }

    /**
     * Gets the value of the memoryType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemoryType() {
        return memoryType;
    }

    /**
     * Sets the value of the memoryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemoryType(String value) {
        this.memoryType = value;
    }

    /**
     * Gets the value of the total property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotal() {
        return total;
    }

    /**
     * Sets the value of the total property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotal(String value) {
        this.total = value;
    }

    /**
     * Gets the value of the used property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsed() {
        return used;
    }

    /**
     * Sets the value of the used property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsed(String value) {
        this.used = value;
    }

}
