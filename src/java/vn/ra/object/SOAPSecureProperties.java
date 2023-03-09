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
 * @author THANH-PC
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SOAPSecureProperties {

    @JsonProperty("attributeType")
    public String attributeType;
    @JsonProperty("remarkEn")
    public String remarkEn;
    @JsonProperty("remark")
    public String remark;
    @JsonProperty("password")
    public String password;
    @JsonProperty("signature")
    public String signature;
    @JsonProperty("publicKeyPem")
    public String publicKeyPem;
}
