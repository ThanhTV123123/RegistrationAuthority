/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.object;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author USER
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileContactInfoJson {

    @JsonProperty("RepresentativeName")
    public String RepresentativeName;
    @JsonProperty("RepresentativePhone")
    public String RepresentativePhone;
    @JsonProperty("RepresentativeEmail")
    public String RepresentativeEmail;
    @JsonProperty("ContactName")
    public String ContactName;
    @JsonProperty("Position")
    public String Position;
    // HK thuong tru
    @JsonProperty("Address")
    public String Address;
    @JsonProperty("PIDIssuedBy")
    public String PIDIssuedBy;
    @JsonProperty("PIDDate")
    public String PIDDate;
    @JsonProperty("PID")
    public String PID;
    // dkkd
    @JsonProperty("AddressLicense")
    public String AddressLicense;
}
