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
 * @author THANH-PC
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CERTIFICATION_POLICY_ATTRIBUTE {

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

        private String name;
        private String certificateAuthority;
        private String certificatePurpose;
        private String remarkEn;
        private String remark;
        private boolean enabled;
        private boolean approveCAEnabled;
        private String attributeType;
        private List<Attribute> attributes;

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @JsonProperty("certificateAuthority")
        public String getCertificateAuthority() {
            return certificateAuthority;
        }

        public void setCertificateAuthority(String certificateAuthority) {
            this.certificateAuthority = certificateAuthority;
        }

        @JsonProperty("certificatePurpose")
        public String getCertificatePurpose() {
            return certificatePurpose;
        }

        public void setCertificatePurpose(String certificatePurpose) {
            this.certificatePurpose = certificatePurpose;
        }

        @JsonProperty("remarkEn")
        public String getRemarkEn() {
            return remarkEn;
        }

        public void setRemarkEn(String remarkEn) {
            this.remarkEn = remarkEn;
        }

        @JsonProperty("remark")
        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        @JsonProperty("enabled")
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @JsonProperty("approveCAEnabled")
        public boolean isApproveCAEnabled() {
            return approveCAEnabled;
        }

        public void setApproveCAEnabled(boolean approveCAEnabled) {
            this.approveCAEnabled = approveCAEnabled;
        }

        @JsonProperty("attributeType")
        public String getAttributeType() {
            return attributeType;
        }

        public void setAttributeType(String attributeType) {
            this.attributeType = attributeType;
        }

        @JsonProperty("attributes")
        public List<Attribute> getAttributes() {
            return attributes;
        }

        public void setAttributes(List<Attribute> attributes) {
            this.attributes = attributes;
        }
    }
}
