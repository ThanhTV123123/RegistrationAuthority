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
public class CredentialDataAuthen {

    @JsonProperty("wsUrl")
    public String wsUrl;
    @JsonProperty("entityName")
    public String entityName;
    @JsonProperty("userName")
    public String userName;
    @JsonProperty("passWord")
    public String passWord;
    @JsonProperty("uuid")
    public String uuid;
    @JsonProperty("relyingPartyOwner")
    public String relyingPartyOwner;
    @JsonProperty("userId")
    public int userId;
    @JsonProperty("attributeType")
    public String attributeType;
}
