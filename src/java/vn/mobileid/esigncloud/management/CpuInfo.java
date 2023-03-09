
package vn.mobileid.esigncloud.management;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cpuInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cpuInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bogomips" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cache" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="model" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numOfCore" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="processListInDetail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="speed" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cpuInfo", propOrder = {
    "bogomips",
    "cache",
    "model",
    "numOfCore",
    "processListInDetail",
    "speed"
})
public class CpuInfo {

    protected String bogomips;
    protected String cache;
    protected String model;
    protected int numOfCore;
    protected String processListInDetail;
    protected String speed;

    /**
     * Gets the value of the bogomips property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBogomips() {
        return bogomips;
    }

    /**
     * Sets the value of the bogomips property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBogomips(String value) {
        this.bogomips = value;
    }

    /**
     * Gets the value of the cache property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCache() {
        return cache;
    }

    /**
     * Sets the value of the cache property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCache(String value) {
        this.cache = value;
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModel(String value) {
        this.model = value;
    }

    /**
     * Gets the value of the numOfCore property.
     * 
     */
    public int getNumOfCore() {
        return numOfCore;
    }

    /**
     * Sets the value of the numOfCore property.
     * 
     */
    public void setNumOfCore(int value) {
        this.numOfCore = value;
    }

    /**
     * Gets the value of the processListInDetail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessListInDetail() {
        return processListInDetail;
    }

    /**
     * Sets the value of the processListInDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessListInDetail(String value) {
        this.processListInDetail = value;
    }

    /**
     * Gets the value of the speed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * Sets the value of the speed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpeed(String value) {
        this.speed = value;
    }

}
