
package vn.mobileid.esigncloud.management;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for diskUsage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="diskUsage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="available" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fileSystem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mountOn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="size" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="used" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usedPercent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "diskUsage", propOrder = {
    "available",
    "fileSystem",
    "mountOn",
    "size",
    "used",
    "usedPercent"
})
public class DiskUsage {

    protected String available;
    protected String fileSystem;
    protected String mountOn;
    protected String size;
    protected String used;
    protected String usedPercent;

    /**
     * Gets the value of the available property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvailable() {
        return available;
    }

    /**
     * Sets the value of the available property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvailable(String value) {
        this.available = value;
    }

    /**
     * Gets the value of the fileSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileSystem() {
        return fileSystem;
    }

    /**
     * Sets the value of the fileSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileSystem(String value) {
        this.fileSystem = value;
    }

    /**
     * Gets the value of the mountOn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMountOn() {
        return mountOn;
    }

    /**
     * Sets the value of the mountOn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMountOn(String value) {
        this.mountOn = value;
    }

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSize(String value) {
        this.size = value;
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

    /**
     * Gets the value of the usedPercent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsedPercent() {
        return usedPercent;
    }

    /**
     * Sets the value of the usedPercent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsedPercent(String value) {
        this.usedPercent = value;
    }

}
