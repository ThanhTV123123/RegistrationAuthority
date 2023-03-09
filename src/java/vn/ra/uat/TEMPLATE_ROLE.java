/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author THANH-PC
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TEMPLATE_ROLE {

    @JsonProperty("enabled")
    public boolean enabled;
    @JsonProperty("name")
    public String name;
    @JsonProperty("remark")
    public String remark;
    @JsonProperty("remarkEn")
    public String remarkEn;
}
