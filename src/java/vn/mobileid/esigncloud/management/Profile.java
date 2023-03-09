
package vn.mobileid.esigncloud.management;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for profile.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="profile">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="TERM_AND_CONDITION"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "profile")
@XmlEnum
public enum Profile {

    TERM_AND_CONDITION;

    public String value() {
        return name();
    }

    public static Profile fromValue(String v) {
        return valueOf(v);
    }

}
