/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author USER
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormFactorJsonProperties {

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

        private String mode;
        private String protocol;
        private String remark;
        private String remarkEn;
        private String attributeType;
        private boolean enabled;
        private CredentialDataAuthen credentialDataAuthen;

        @JsonProperty("mode")
        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        @JsonProperty("protocol")
        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        @JsonProperty("remark")
        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        @JsonProperty("remarkEn")
        public String getRemarkEn() {
            return remarkEn;
        }

        public void setRemarkEn(String remarkEn) {
            this.remarkEn = remarkEn;
        }

        @JsonProperty("attributeType")
        public String getAttributeType() {
            return attributeType;
        }

        public void setAttributeType(String attributeType) {
            this.attributeType = attributeType;
        }
        
        @JsonProperty("enabled")
        public boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @JsonProperty("credentialDataAuthen")
        public CredentialDataAuthen getCredentialDataAuthen() {
            return credentialDataAuthen;
        }

        public void setCredentialDataAuthen(CredentialDataAuthen credentialDataAuthen) {
            this.credentialDataAuthen = credentialDataAuthen;
        }
    }
}
