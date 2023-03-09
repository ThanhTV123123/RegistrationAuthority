/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author THANH-PC
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ATTRIBUTE_DATA {
    private MENULINK_TOKEN menulink;
    private PUSH_TOKEN sticker;
    private CERTIFICATION_DATA_ATTR certificationData;
    private UNLOCK_TOKEN RequestUnlockObject;

    @JsonProperty("menulink")
    public MENULINK_TOKEN getMenulink() {
        return menulink;
    }

    public void setMenulink(MENULINK_TOKEN menulink) {
        this.menulink = menulink;
    }

    @JsonProperty("sticker")
    public PUSH_TOKEN getSticker() {
        return sticker;
    }

    public void setSticker(PUSH_TOKEN sticker) {
        this.sticker = sticker;
    }

    @JsonProperty("certificationData")
    public CERTIFICATION_DATA_ATTR getCertificationData() {
        return certificationData;
    }

    public void setCertificationData(CERTIFICATION_DATA_ATTR certificationData) {
        this.certificationData = certificationData;
    }

    public UNLOCK_TOKEN getRequestUnlockObject() {
        return RequestUnlockObject;
    }

    public void setRequestUnlockObject(UNLOCK_TOKEN RequestUnlockObject) {
        this.RequestUnlockObject = RequestUnlockObject;
    }
}
