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
 * @author USER
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PushNotificationAPI {

    @JsonProperty("notificationContent")
    public String notificationContent;
    @JsonProperty("popupUrl")
    public String popupUrl;
    @JsonProperty("textColor")
    public String textColor;
    @JsonProperty("backgroundColor")
    public String backgroundColor;
}
