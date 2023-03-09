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
public class CERTIFICATION_PROPERTIES_JSON {

    private List<CERTIFICATION_PROPERTIES_JSON.Attribute> attributes;

    @JsonProperty("attributes")
    public List<CERTIFICATION_PROPERTIES_JSON.Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<CERTIFICATION_PROPERTIES_JSON.Attribute> attributes) {
        this.attributes = attributes;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Attribute {

        public static final String KEY_KEYSTORE_PASSWORD = "KEYSTORE_PASSWORD";
        public static final String SUBJECT_ALT_NAMES = "SUBJECT_ALT_NAMES";

        private String key;
        private String value;

        @JsonProperty("key")
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @JsonProperty("value")
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
