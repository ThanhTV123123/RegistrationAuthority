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
public class PUSH_TOKEN_EDITED {

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
        private String remarkEn;
        private String remark;
        private String value;
        private String mimetype;

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

        @JsonProperty("value")
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @JsonProperty("mimetype")
        public String getMimetype() {
            return mimetype;
        }

        public void setMimetype(String mimetype) {
            this.mimetype = mimetype;
        }

    }
}
