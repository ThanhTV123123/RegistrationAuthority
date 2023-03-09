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
public class CERTIFICATE_ATTRIBUTES {

    private List<Attribute> attributes;

    @JsonProperty("attributes")
    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
    
    private List<AttributeSan> attributeSans;

    @JsonProperty("attributeSans")
    public List<AttributeSan> getAttributeSans() {
        return attributeSans;
    }

    public void setAttributeSans(List<AttributeSan> attributeSans) {
        this.attributeSans = attributeSans;
    }
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Attribute {
//        public static final String ATTRIBUTE_TYPE_TEXTFIELD = "TEXT_FIELD";
//        public static final String ATTRIBUTE_TYPE_RADIOBUTTON = "RADIO_BUTTON";
        
        private String name;
        private String remarkEn;
        private String remark;
        private String prefix;
        private boolean require;
        private String attributeType;
        private String commomNameType;
        private List<Attribute> attributes;

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        @JsonProperty("prefix")
        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        @JsonProperty("require")
        public boolean isRequire() {
            return require;
        }

        public void setRequire(boolean require) {
            this.require = require;
        }
        
        @JsonProperty("attributeType")
        public String getAttributeType() {
            return attributeType;
        }

        public void setAttributeType(String attributeType) {
            this.attributeType = attributeType;
        }
        
        @JsonProperty("commomNameType")
        public String getCommomNameType() {
            return commomNameType;
        }

        public void setCommomNameType(String commomNameType) {
            this.commomNameType = commomNameType;
        }

        @JsonProperty("attributes")
        public List<Attribute> getAttributes() {
            return attributes;
        }

        public void setAttributes(List<Attribute> attributes) {
            this.attributes = attributes;
        }
        
    }
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AttributeSan
    {
        private String name;
        private String remarkEn;
        private String remark;
        private String prefix;
        private boolean require;
        private String attributeType;
        private String commomNameType;

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        @JsonProperty("prefix")
        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        @JsonProperty("require")
        public boolean isRequire() {
            return require;
        }

        public void setRequire(boolean require) {
            this.require = require;
        }
        
        @JsonProperty("attributeType")
        public String getAttributeType() {
            return attributeType;
        }

        public void setAttributeType(String attributeType) {
            this.attributeType = attributeType;
        }
        
        @JsonProperty("commomNameType")
        public String getCommomNameType() {
            return commomNameType;
        }

        public void setCommomNameType(String commomNameType) {
            this.commomNameType = commomNameType;
        }
        
    }
}
