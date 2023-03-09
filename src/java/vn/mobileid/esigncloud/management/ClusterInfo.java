
package vn.mobileid.esigncloud.management;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for clusterInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="clusterInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="allHosts" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="currentHost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastChange" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastUpdated" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="offlineHosts" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="onlineHosts" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clusterInfo", propOrder = {
    "allHosts",
    "currentHost",
    "lastChange",
    "lastUpdated",
    "offlineHosts",
    "onlineHosts"
})
public class ClusterInfo {

    @XmlElement(nillable = true)
    protected List<String> allHosts;
    protected String currentHost;
    protected String lastChange;
    protected String lastUpdated;
    @XmlElement(nillable = true)
    protected List<String> offlineHosts;
    @XmlElement(nillable = true)
    protected List<String> onlineHosts;

    /**
     * Gets the value of the allHosts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the allHosts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAllHosts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAllHosts() {
        if (allHosts == null) {
            allHosts = new ArrayList<String>();
        }
        return this.allHosts;
    }

    /**
     * Gets the value of the currentHost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentHost() {
        return currentHost;
    }

    /**
     * Sets the value of the currentHost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentHost(String value) {
        this.currentHost = value;
    }

    /**
     * Gets the value of the lastChange property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastChange() {
        return lastChange;
    }

    /**
     * Sets the value of the lastChange property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastChange(String value) {
        this.lastChange = value;
    }

    /**
     * Gets the value of the lastUpdated property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Sets the value of the lastUpdated property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastUpdated(String value) {
        this.lastUpdated = value;
    }

    /**
     * Gets the value of the offlineHosts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the offlineHosts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOfflineHosts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOfflineHosts() {
        if (offlineHosts == null) {
            offlineHosts = new ArrayList<String>();
        }
        return this.offlineHosts;
    }

    /**
     * Gets the value of the onlineHosts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the onlineHosts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOnlineHosts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOnlineHosts() {
        if (onlineHosts == null) {
            onlineHosts = new ArrayList<String>();
        }
        return this.onlineHosts;
    }

}
