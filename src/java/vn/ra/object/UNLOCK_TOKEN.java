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
public class UNLOCK_TOKEN {

    @JsonProperty("phoneNumber")
    public String PHONE_NUMBER;
    @JsonProperty("email")
    public String EMAIL;
    @JsonProperty("organizationType")
    public boolean OrganizationType;
    @JsonProperty("customerIdentifier")
    public String CustomerIdentifier;
    @JsonProperty("otpAuthenticationEnabled")
    public boolean otpAuthenticationEnabled;
}
