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
public class CERTIFICATION_OWNER_COMMENT {

    @JsonProperty("ownerDeclineReason")
    public String ownerDeclineReason;
    @JsonProperty("ownerDisposeReason")
    public String ownerDisposeReason;
    @JsonProperty("ownerApproveRemark")
    public String ownerApproveRemark;
}
