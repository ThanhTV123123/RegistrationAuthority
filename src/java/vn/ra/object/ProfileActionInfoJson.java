/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.object;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 *
 * @author vanth
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileActionInfoJson {

    @JsonProperty("controlUser")
    public String controlUser;
    @JsonProperty("controlTime")
    public Date controlTime;
}
