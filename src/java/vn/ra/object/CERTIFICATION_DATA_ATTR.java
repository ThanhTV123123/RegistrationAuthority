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
 * @author THANH-PC
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CERTIFICATION_DATA_ATTR {

    @JsonProperty("pushNoticeEnable")
    public int pushNoticeEnable;
    @JsonProperty("duration")
    public int duration;
    @JsonProperty("feeAmount")
    public int feeAmount;
    @JsonProperty("tokenAmount")
    public int tokenAmount;
    @JsonProperty("personalName")
    public String personalName;
    @JsonProperty("companyName")
    public String companyName;
    @JsonProperty("enterpriseID")
    public String enterpriseID;
    @JsonProperty("taxCode")
    public String taxCode;
    @JsonProperty("decision")
    public String decision;
    @JsonProperty("budgetCode")
    public String budgetCode;
    @JsonProperty("personalID")
    public String personalID;
    @JsonProperty("personalCode")
    public String personalCode;
    @JsonProperty("passportCode")
    public String passportCode;
    @JsonProperty("citizenCode")
    public String citizenCode;
    @JsonProperty("deviceUUID")
    public String deviceUUID;
    @JsonProperty("domainName")
    public String domainName;
    @JsonProperty("subjectDn")
    public String subjectDn;
    @JsonProperty("issuerSubject")
    public String issuerSubject;
    @JsonProperty("phoneContract")
    public String phoneContract;
    @JsonProperty("emailContract")
    public String emailContract;
    @JsonProperty("provinceName")
    public String provinceName;
    @JsonProperty("oldSerialNumber")
    public String oldSerialNumber;
    @JsonProperty("typeName")
    public String typeName;
    @JsonProperty("tokenSn")
    public String tokenSn;
    @JsonProperty("pkiFromFactorId")
    public int pkiFromFactorId;
    @JsonProperty("pkiFromFactorCode")
    public String pkiFromFactorCode;
}
