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
public class CERTIFICATION_OWNER_DATA_ATTR {

    @JsonProperty("ownerUUID")
    public String ownerUUID;
    @JsonProperty("queueUUID")
    public String queueUUID;
    @JsonProperty("taxCode")
    public String taxCode;
    @JsonProperty("decision")
    public String decision;
    @JsonProperty("budgetCode")
    public String budgetCode;
    @JsonProperty("personalName")
    public String personalName;
    @JsonProperty("companyName")
    public String companyName;
    @JsonProperty("personalCode")
    public String personalCode;
    @JsonProperty("passportCode")
    public String passportCode;
    @JsonProperty("enterpriseID")
    public String enterpriseID;
    @JsonProperty("personalID")
    public String personalID;
    @JsonProperty("citizenID")
    public String citizenID;
    @JsonProperty("emailContract")
    public String emailContract;
    @JsonProperty("phoneContract")
    public String phoneContract;
    @JsonProperty("address")
    public String address;
    @JsonProperty("representative")
    public String representative;
    @JsonProperty("representativePosition")
    public String representativePosition;
    @JsonProperty("typeName")
    public String typeName;
    @JsonProperty("requestState")
    public String requestState;
    @JsonProperty("createUser")
    public String createUser;
    @JsonProperty("createDt")
    public Date createDt;
    @JsonProperty("approveUser")
    public String approveUser;
    @JsonProperty("approveCAUser")
    public String approveCAUser;
    @JsonProperty("approveDt")
    public Date approveDt;
    @JsonProperty("approveCADt")
    public Date approveCADt;
    @JsonProperty("ownerDisposeReason")
    public String ownerDisposeReason;
//    @JsonProperty("remarkDecline")
//    public String remarkDecline;
//    @JsonProperty("remarkApprove")
//    public String remarkApprove;
}
