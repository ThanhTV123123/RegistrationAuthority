/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.object;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author THANH-PC
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PUSH_TOKEN {

    @JsonProperty("content")
    public String PUSH_NOTICE_CONTENT;
    @JsonProperty("url")
    public String PUSH_NOTICE_URL;
    @JsonProperty("textColor")
    public String PUSH_NOTICE_TEXT_COLOR;
    @JsonProperty("bgrColor")
    public String PUSH_NOTICE_BGR_COLOR;
    @JsonProperty("textPushNotice")
    public String PUSH_NOTICE_TEXT;
    @JsonProperty("linkPushNotice")
    public String PUSH_NOTICE_LINK;

}
