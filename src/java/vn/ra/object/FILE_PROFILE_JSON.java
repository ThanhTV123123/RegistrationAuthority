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
 * @author thanh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FILE_PROFILE_JSON {

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
        private boolean enabled;
        private String remarkEn;
        private String remark;
        private boolean isrequire;

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
        @JsonProperty("enabled")
        public boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
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

        @JsonProperty("isrequire")
        public boolean getIsRequire() {
            return isrequire;
        }

        public void setIsRequire(boolean isrequire) {
            this.isrequire = isrequire;
        }
    }
}
