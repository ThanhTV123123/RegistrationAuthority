/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author USER
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceRequestExpand {

    @JsonProperty("rsspAgreementUUID")
    public String rsspAgreementUUID;
    @JsonProperty("rsspRelyingParty")
    public String rsspRelyingParty;
    @JsonProperty("rsspCertificateUUID")
    public String rsspCertificateUUID;
    @JsonProperty("rsspConnectWSMode")
    public String rsspConnectWSMode;
    @JsonProperty("representative")
    public String representative;
    @JsonProperty("representativePosition")
    public String representativePosition;
    
    @JsonProperty("businessLicenseAddress")
    public String businessLicenseAddress;
    @JsonProperty("permanentResidence")
    public String permanentResidence;
    @JsonProperty("personalID")
    public String personalID;
    @JsonProperty("dateIssuance")
    public String dateIssuance;
    @JsonProperty("placeIssuance")
    public String placeIssuance;
    @JsonProperty("setOldCertificateToOperated")
    public boolean setOldCertificateToOperated;
}
