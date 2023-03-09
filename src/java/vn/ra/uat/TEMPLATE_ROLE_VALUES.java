/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author THANH-PC
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TEMPLATE_ROLE_VALUES {

    private String remark;
    private List<TEMPLATE_ROLE> attributeData;

    @JsonProperty("remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @JsonProperty("attributeData")
    public List<TEMPLATE_ROLE> getAttributeData() {
        return attributeData;
    }

    public void setAttributeData(List<TEMPLATE_ROLE> attributeData) {
        this.attributeData = attributeData;
    }
}
