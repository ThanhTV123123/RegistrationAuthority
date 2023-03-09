/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.synch.neac;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author vanth
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestDataNEAC {

    public String signature;
    public String serialNumber;
    public String userID;
    public String certFileName;
    public String certFileBase64Content;
    public NEREQUEST_DATA certForCAModel;
    public ListPdfFileBase64[] listPdfFileBase64;
}
