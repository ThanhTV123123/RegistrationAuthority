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
public class RESPONSE_LOG {

    @JsonProperty("ResponseCode")
    public String ResponseCode;
    @JsonProperty("ResponseMessage")
    public String ResponseMessage;
}
