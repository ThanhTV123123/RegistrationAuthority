/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.object;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 *
 * @author USER
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerConfirmation {
    private Date timestamp;
    private String ipAddress;
    private String certificationInfo;
    private boolean confirmed;
    
    @JsonProperty("timestamp")
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("ipAddress")
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @JsonProperty("certificationInfo")
    public String getCertificationInfo() {
        return certificationInfo;
    }

    public void setCertificationInfo(String certificationInfo) {
        this.certificationInfo = certificationInfo;
    }

    @JsonProperty("confirmed")
    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
