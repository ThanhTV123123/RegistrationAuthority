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
public class PROFILE_DISCOUNT_RATE_ATTRIBUTE {
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

        private String profileName;
        private String profileRemark;
        private String profileRemarkEN;
        private String rosePercent;
        private boolean enabled;
        private boolean isMoneyType;
        private String attributeType;
        private List<Attribute> attributes;

        @JsonProperty("profileName")
        public String getProfileName() {
            return profileName;
        }

        public void setProfileName(String profileName) {
            this.profileName = profileName;
        }
        
        @JsonProperty("profileRemark")
        public String getProfileRemark() {
            return profileRemark;
        }

        public void setProfileRemark(String profileRemark) {
            this.profileRemark = profileRemark;
        }
        
        @JsonProperty("profileRemarkEN")
        public String getProfileRemarkEN() {
            return profileRemarkEN;
        }

        public void setProfileRemarkEN(String profileRemarkEN) {
            this.profileRemarkEN = profileRemarkEN;
        }
        
        @JsonProperty("rosePercent")
        public String getRosePercent() {
            return rosePercent;
        }

        public void setRosePercent(String rosePercent) {
            this.rosePercent = rosePercent;
        }
        
        @JsonProperty("enabled")
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        @JsonProperty("isMoneyType")
        public boolean getIsMoneyType() {
            return isMoneyType;
        }

        public void setIsMoneyType(boolean isMoneyType) {
            this.isMoneyType = isMoneyType;
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
