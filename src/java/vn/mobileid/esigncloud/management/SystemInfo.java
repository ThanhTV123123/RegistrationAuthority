
package vn.mobileid.esigncloud.management;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for systemInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="systemInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hostname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="kernelVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastboot" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="os" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serverDateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uptime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "systemInfo", propOrder = {
    "hostname",
    "kernelVersion",
    "lastboot",
    "os",
    "serverDateTime",
    "uptime"
})
public class SystemInfo {

    protected String hostname;
    protected String kernelVersion;
    protected String lastboot;
    protected String os;
    protected String serverDateTime;
    protected String uptime;

    /**
     * Gets the value of the hostname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Sets the value of the hostname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostname(String value) {
        this.hostname = value;
    }

    /**
     * Gets the value of the kernelVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKernelVersion() {
        return kernelVersion;
    }

    /**
     * Sets the value of the kernelVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKernelVersion(String value) {
        this.kernelVersion = value;
    }

    /**
     * Gets the value of the lastboot property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastboot() {
        return lastboot;
    }

    /**
     * Sets the value of the lastboot property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastboot(String value) {
        this.lastboot = value;
    }

    /**
     * Gets the value of the os property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOs() {
        return os;
    }

    /**
     * Sets the value of the os property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOs(String value) {
        this.os = value;
    }

    /**
     * Gets the value of the serverDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerDateTime() {
        return serverDateTime;
    }

    /**
     * Sets the value of the serverDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerDateTime(String value) {
        this.serverDateTime = value;
    }

    /**
     * Gets the value of the uptime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUptime() {
        return uptime;
    }

    /**
     * Sets the value of the uptime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUptime(String value) {
        this.uptime = value;
    }

}
