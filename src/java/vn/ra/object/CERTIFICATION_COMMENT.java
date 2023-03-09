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
public class CERTIFICATION_COMMENT {

    @JsonProperty("certificateDeclineReason")
    public String certificateDeclineReason;
    @JsonProperty("certificateRevokeReason")
    public String certificateRevokeReason;
    @JsonProperty("certificateSuspendReason")
    public String certificateSuspendReason;
    @JsonProperty("certificateApproveRemark")
    public String certificateApproveRemark;
    @JsonProperty("certificateAttachmentApproveRemark")
    public String certificateAttachmentApproveRemark;
}
