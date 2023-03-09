/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author THANH-PC
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TEMPLATE_ROLE_DATA {

    private TEMPLATE_ROLE menulink;

    @JsonProperty("menulink")
    public TEMPLATE_ROLE getMenulink() {
        return menulink;
    }

    public void setMenulink(TEMPLATE_ROLE menulink) {
        this.menulink = menulink;
    }
}
