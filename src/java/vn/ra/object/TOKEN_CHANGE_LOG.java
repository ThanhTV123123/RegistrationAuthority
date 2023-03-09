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
public class TOKEN_CHANGE_LOG {

    private String TOKEN_SN;
    private String BRANCH_DESC;
    private String INITIALIZE;
    private String IS_LOCK;
    private String IS_LOST;
    private String IS_RE_OPERATION;
    private String IS_UNLOCK;
    private String CHANGE_SOPIN;
    private String SO_PIN;
    private String MENU_LINK_NAME;
    private String MENU_LINK_URL;
    private String PUSH_NOTICE_CONTENT;
    private String PUSH_NOTICE_URL;
    private String PUSH_NOTICE_TEXT_COLOR;
    private String PUSH_NOTICE_BGR_COLOR;
    private String ACTIVE_FLAG;
    private String PERMANENT_INITIALZED;
    private String ACTIVATION_REMAINING_COUNTER;

    @JsonProperty("TOKEN_SN")
    public String getTOKEN_SN() {
        return TOKEN_SN;
    }

    public void setTOKEN_SN(String TOKEN_SN) {
        this.TOKEN_SN = TOKEN_SN;
    }

    @JsonProperty("BRANCH")
    public String getBRANCH_DESC() {
        return BRANCH_DESC;
    }

    public void setBRANCH_DESC(String BRANCH_DESC) {
        this.BRANCH_DESC = BRANCH_DESC;
    }

    @JsonProperty("INITIALIZE_ENABLED")
    public String getINITIALIZE() {
        return INITIALIZE;
    }

    public void setINITIALIZE(String INITIALIZE) {
        this.INITIALIZE = INITIALIZE;
    }

    @JsonProperty("LOCK_ENABLED")
    public String getIS_LOCK() {
        return IS_LOCK;
    }

    public void setIS_LOCK(String IS_LOCK) {
        this.IS_LOCK = IS_LOCK;
    }

    @JsonProperty("UNLOCK_ENABLED")
    public String getIS_UNLOCK() {
        return IS_UNLOCK;
    }

    public void setIS_UNLOCK(String IS_UNLOCK) {
        this.IS_UNLOCK = IS_UNLOCK;
    }

    @JsonProperty("CHANGE_SOPIN_ENABLED")
    public String getCHANGE_SOPIN() {
        return CHANGE_SOPIN;
    }

    public void setCHANGE_SOPIN(String CHANGE_SOPIN) {
        this.CHANGE_SOPIN = CHANGE_SOPIN;
    }

    @JsonProperty("SO_PIN")
    public String getSO_PIN() {
        return SO_PIN;
    }

    public void setSO_PIN(String SO_PIN) {
        this.SO_PIN = SO_PIN;
    }

    @JsonProperty("MENU_LINK_NAME")
    public String getMENU_LINK_NAME() {
        return MENU_LINK_NAME;
    }

    public void setMENU_LINK_NAME(String MENU_LINK_NAME) {
        this.MENU_LINK_NAME = MENU_LINK_NAME;
    }

    @JsonProperty("MENU_LINK_URL")
    public String getMENU_LINK_URL() {
        return MENU_LINK_URL;
    }

    public void setMENU_LINK_URL(String MENU_LINK_URL) {
        this.MENU_LINK_URL = MENU_LINK_URL;
    }

    @JsonProperty("PUSH_NOTICE_CONTENT")
    public String getPUSH_NOTICE_CONTENT() {
        return PUSH_NOTICE_CONTENT;
    }

    public void setPUSH_NOTICE_CONTENT(String PUSH_NOTICE_CONTENT) {
        this.PUSH_NOTICE_CONTENT = PUSH_NOTICE_CONTENT;
    }

    @JsonProperty("PUSH_NOTICE_URL")
    public String getPUSH_NOTICE_URL() {
        return PUSH_NOTICE_URL;
    }

    public void setPUSH_NOTICE_URL(String PUSH_NOTICE_URL) {
        this.PUSH_NOTICE_URL = PUSH_NOTICE_URL;
    }

    @JsonProperty("PUSH_NOTICE_TEXT_COLOR")
    public String getPUSH_NOTICE_TEXT_COLOR() {
        return PUSH_NOTICE_TEXT_COLOR;
    }

    public void setPUSH_NOTICE_TEXT_COLOR(String PUSH_NOTICE_TEXT_COLOR) {
        this.PUSH_NOTICE_TEXT_COLOR = PUSH_NOTICE_TEXT_COLOR;
    }

    @JsonProperty("PUSH_NOTICE_BGR_COLOR")
    public String getPUSH_NOTICE_BGR_COLOR() {
        return PUSH_NOTICE_BGR_COLOR;
    }

    public void setPUSH_NOTICE_BGR_COLOR(String PUSH_NOTICE_BGR_COLOR) {
        this.PUSH_NOTICE_BGR_COLOR = PUSH_NOTICE_BGR_COLOR;
    }

    @JsonProperty("ACTIVE_FLAG")
    public String getACTIVE_FLAG() {
        return ACTIVE_FLAG;
    }

    public void setACTIVE_FLAG(String ACTIVE_FLAG) {
        this.ACTIVE_FLAG = ACTIVE_FLAG;
    }
    
    @JsonProperty("IS_LOST")
    public String getIS_LOST() {
        return IS_LOST;
    }

    public void setIS_LOST(String IS_LOST) {
        this.IS_LOST = IS_LOST;
    }
    
    @JsonProperty("IS_RE_OPERATION")
    public String getIS_RE_OPERATION() {
        return IS_RE_OPERATION;
    }

    public void setIS_RE_OPERATION(String IS_RE_OPERATION) {
        this.IS_RE_OPERATION = IS_RE_OPERATION;
    }
    
    @JsonProperty("PERMANENT_INITIALZED")
    public String getPERMANENT_INITIALZED() {
        return PERMANENT_INITIALZED;
    }

    public void setPERMANENT_INITIALZED(String PERMANENT_INITIALZED) {
        this.PERMANENT_INITIALZED = PERMANENT_INITIALZED;
    }
    @JsonProperty("ACTIVATION_REMAINING_COUNTER")
    public String getACTIVATION_REMAINING_COUNTER() {
        return ACTIVATION_REMAINING_COUNTER;
    }

    public void setACTIVATION_REMAINING_COUNTER(String ACTIVATION_REMAINING_COUNTER) {
        this.ACTIVATION_REMAINING_COUNTER = ACTIVATION_REMAINING_COUNTER;
    }
}
