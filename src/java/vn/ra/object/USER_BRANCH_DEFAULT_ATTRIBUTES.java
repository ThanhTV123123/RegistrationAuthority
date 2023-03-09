/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.object;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author USER
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class USER_BRANCH_DEFAULT_ATTRIBUTES {

    private List<Attribute> attributes;

    @JsonProperty("attributes")
    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Attribute {

        private String MSISDN;
        private String EMAIL;
        private String REMARK;
        private String PARENT_NAME;
        private String BRANCH_ROLE_NAME;
        private String PROVINCE_NAME;
        private String attributeType;
//        private List<Attribute> attributes;

        @JsonProperty("MSISDN")
        public String getMSISDN() {
            return MSISDN;
        }

        public void setMSISDN(String MSISDN) {
            this.MSISDN = MSISDN;
        }

        @JsonProperty("EMAIL")
        public String getEMAIL() {
            return EMAIL;
        }

        public void setEMAIL(String EMAIL) {
            this.EMAIL = EMAIL;
        }

        @JsonProperty("REMARK")
        public String getREMARK() {
            return REMARK;
        }

        public void setREMARK(String REMARK) {
            this.REMARK = REMARK;
        }

        @JsonProperty("PARENT_NAME")
        public String getPARENT_NAME() {
            return PARENT_NAME;
        }

        public void setPARENT_NAME(String PARENT_NAME) {
            this.PARENT_NAME = PARENT_NAME;
        }

        @JsonProperty("BRANCH_ROLE_NAME")
        public String getBRANCH_ROLE_NAME() {
            return BRANCH_ROLE_NAME;
        }

        public void setBRANCH_ROLE_NAME(String BRANCH_ROLE_NAME) {
            this.BRANCH_ROLE_NAME = BRANCH_ROLE_NAME;
        }

        @JsonProperty("PROVINCE_NAME")
        public String getPROVINCE_NAME() {
            return PROVINCE_NAME;
        }

        public void setPROVINCE_NAME(String PROVINCE_NAME) {
            this.PROVINCE_NAME = PROVINCE_NAME;
        }

        @JsonProperty("attributeType")
        public String getAttributeType() {
            return attributeType;
        }

        public void setAttributeType(String attributeType) {
            this.attributeType = attributeType;
        }

//        @JsonProperty("attributes")
//        public List<Attribute> getAttributes() {
//            return attributes;
//        }
//
//        public void setAttributes(List<Attribute> attributes) {
//            this.attributes = attributes;
//        }

    }
}
