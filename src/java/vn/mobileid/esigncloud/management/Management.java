
package vn.mobileid.esigncloud.management;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebService(name = "Management", targetNamespace = "http://management.esigncloud.mobileid.vn/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface Management {


    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "resynchFailedFileUpload", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.ResynchFailedFileUpload")
    @ResponseWrapper(localName = "resynchFailedFileUploadResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.ResynchFailedFileUploadResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/resynchFailedFileUploadRequest", output = "http://management.esigncloud.mobileid.vn/Management/resynchFailedFileUploadResponse")
    public ManagementResp resynchFailedFileUpload(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param paramID
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "reloadParams", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.ReloadParams")
    @ResponseWrapper(localName = "reloadParamsResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.ReloadParamsResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/reloadParamsRequest", output = "http://management.esigncloud.mobileid.vn/Management/reloadParamsResponse")
    public ManagementResp reloadParams(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq,
        @WebParam(name = "paramID", targetNamespace = "")
        int paramID);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getServerLogTypes", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetServerLogTypes")
    @ResponseWrapper(localName = "getServerLogTypesResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetServerLogTypesResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/getServerLogTypesRequest", output = "http://management.esigncloud.mobileid.vn/Management/getServerLogTypesResponse")
    public ManagementResp getServerLogTypes(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "monitorServer", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.MonitorServer")
    @ResponseWrapper(localName = "monitorServerResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.MonitorServerResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/monitorServerRequest", output = "http://management.esigncloud.mobileid.vn/Management/monitorServerResponse")
    public ManagementResp monitorServer(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getServerLog", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetServerLog")
    @ResponseWrapper(localName = "getServerLogResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetServerLogResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/getServerLogRequest", output = "http://management.esigncloud.mobileid.vn/Management/getServerLogResponse")
    public ManagementResp getServerLog(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getCertificateDetailForSignCloud", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetCertificateDetailForSignCloud")
    @ResponseWrapper(localName = "getCertificateDetailForSignCloudResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetCertificateDetailForSignCloudResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/getCertificateDetailForSignCloudRequest", output = "http://management.esigncloud.mobileid.vn/Management/getCertificateDetailForSignCloudResponse")
    public ManagementResp getCertificateDetailForSignCloud(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "prepareCertificateForSignCloud", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.PrepareCertificateForSignCloud")
    @ResponseWrapper(localName = "prepareCertificateForSignCloudResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.PrepareCertificateForSignCloudResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/prepareCertificateForSignCloudRequest", output = "http://management.esigncloud.mobileid.vn/Management/prepareCertificateForSignCloudResponse")
    public ManagementResp prepareCertificateForSignCloud(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "prepareRenewCertificateForSignCloud", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.PrepareRenewCertificateForSignCloud")
    @ResponseWrapper(localName = "prepareRenewCertificateForSignCloudResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.PrepareRenewCertificateForSignCloudResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/prepareRenewCertificateForSignCloudRequest", output = "http://management.esigncloud.mobileid.vn/Management/prepareRenewCertificateForSignCloudResponse")
    public ManagementResp prepareRenewCertificateForSignCloud(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "prepareChangeCertificateForSignCloud", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.PrepareChangeCertificateForSignCloud")
    @ResponseWrapper(localName = "prepareChangeCertificateForSignCloudResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.PrepareChangeCertificateForSignCloudResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/prepareChangeCertificateForSignCloudRequest", output = "http://management.esigncloud.mobileid.vn/Management/prepareChangeCertificateForSignCloudResponse")
    public ManagementResp prepareChangeCertificateForSignCloud(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "approveCertificateForSignCloud", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.ApproveCertificateForSignCloud")
    @ResponseWrapper(localName = "approveCertificateForSignCloudResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.ApproveCertificateForSignCloudResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/approveCertificateForSignCloudRequest", output = "http://management.esigncloud.mobileid.vn/Management/approveCertificateForSignCloudResponse")
    public ManagementResp approveCertificateForSignCloud(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "changePasscodeForSignCloud", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.ChangePasscodeForSignCloud")
    @ResponseWrapper(localName = "changePasscodeForSignCloudResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.ChangePasscodeForSignCloudResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/changePasscodeForSignCloudRequest", output = "http://management.esigncloud.mobileid.vn/Management/changePasscodeForSignCloudResponse")
    public ManagementResp changePasscodeForSignCloud(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "forgetPasscodeForSignCloud", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.ForgetPasscodeForSignCloud")
    @ResponseWrapper(localName = "forgetPasscodeForSignCloudResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.ForgetPasscodeForSignCloudResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/forgetPasscodeForSignCloudRequest", output = "http://management.esigncloud.mobileid.vn/Management/forgetPasscodeForSignCloudResponse")
    public ManagementResp forgetPasscodeForSignCloud(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "declineCertificateForSignCloud", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.DeclineCertificateForSignCloud")
    @ResponseWrapper(localName = "declineCertificateForSignCloudResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.DeclineCertificateForSignCloudResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/declineCertificateForSignCloudRequest", output = "http://management.esigncloud.mobileid.vn/Management/declineCertificateForSignCloudResponse")
    public ManagementResp declineCertificateForSignCloud(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "disableCertificateForSignCloud", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.DisableCertificateForSignCloud")
    @ResponseWrapper(localName = "disableCertificateForSignCloudResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.DisableCertificateForSignCloudResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/disableCertificateForSignCloudRequest", output = "http://management.esigncloud.mobileid.vn/Management/disableCertificateForSignCloudResponse")
    public ManagementResp disableCertificateForSignCloud(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "enableCertificateForSignCloud", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.EnableCertificateForSignCloud")
    @ResponseWrapper(localName = "enableCertificateForSignCloudResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.EnableCertificateForSignCloudResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/enableCertificateForSignCloudRequest", output = "http://management.esigncloud.mobileid.vn/Management/enableCertificateForSignCloudResponse")
    public ManagementResp enableCertificateForSignCloud(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "prepareRevokeCertificateForSignCloud", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.PrepareRevokeCertificateForSignCloud")
    @ResponseWrapper(localName = "prepareRevokeCertificateForSignCloudResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.PrepareRevokeCertificateForSignCloudResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/prepareRevokeCertificateForSignCloudRequest", output = "http://management.esigncloud.mobileid.vn/Management/prepareRevokeCertificateForSignCloudResponse")
    public ManagementResp prepareRevokeCertificateForSignCloud(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getOwnerInfo", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetOwnerInfo")
    @ResponseWrapper(localName = "getOwnerInfoResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetOwnerInfoResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/getOwnerInfoRequest", output = "http://management.esigncloud.mobileid.vn/Management/getOwnerInfoResponse")
    public ManagementResp getOwnerInfo(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "createOwnerInfo", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.CreateOwnerInfo")
    @ResponseWrapper(localName = "createOwnerInfoResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.CreateOwnerInfoResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/createOwnerInfoRequest", output = "http://management.esigncloud.mobileid.vn/Management/createOwnerInfoResponse")
    public ManagementResp createOwnerInfo(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getOwnerCertificate", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetOwnerCertificate")
    @ResponseWrapper(localName = "getOwnerCertificateResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetOwnerCertificateResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/getOwnerCertificateRequest", output = "http://management.esigncloud.mobileid.vn/Management/getOwnerCertificateResponse")
    public ManagementResp getOwnerCertificate(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "changeSigningProfile", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.ChangeSigningProfile")
    @ResponseWrapper(localName = "changeSigningProfileResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.ChangeSigningProfileResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/changeSigningProfileRequest", output = "http://management.esigncloud.mobileid.vn/Management/changeSigningProfileResponse")
    public ManagementResp changeSigningProfile(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAuthModes", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetAuthModes")
    @ResponseWrapper(localName = "getAuthModesResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetAuthModesResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/getAuthModesRequest", output = "http://management.esigncloud.mobileid.vn/Management/getAuthModesResponse")
    public ManagementResp getAuthModes(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getRelyingParties", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetRelyingParties")
    @ResponseWrapper(localName = "getRelyingPartiesResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetRelyingPartiesResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/getRelyingPartiesRequest", output = "http://management.esigncloud.mobileid.vn/Management/getRelyingPartiesResponse")
    public ManagementResp getRelyingParties(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getSigningProfiles", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetSigningProfiles")
    @ResponseWrapper(localName = "getSigningProfilesResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetSigningProfilesResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/getSigningProfilesRequest", output = "http://management.esigncloud.mobileid.vn/Management/getSigningProfilesResponse")
    public ManagementResp getSigningProfiles(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "downloadFileForSignCloud", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.DownloadFileForSignCloud")
    @ResponseWrapper(localName = "downloadFileForSignCloudResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.DownloadFileForSignCloudResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/downloadFileForSignCloudRequest", output = "http://management.esigncloud.mobileid.vn/Management/downloadFileForSignCloudResponse")
    public ManagementResp downloadFileForSignCloud(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getNTPConfig", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetNTPConfig")
    @ResponseWrapper(localName = "getNTPConfigResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetNTPConfigResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/getNTPConfigRequest", output = "http://management.esigncloud.mobileid.vn/Management/getNTPConfigResponse")
    public ManagementResp getNTPConfig(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "setNTPConfig", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.SetNTPConfig")
    @ResponseWrapper(localName = "setNTPConfigResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.SetNTPConfigResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/setNTPConfigRequest", output = "http://management.esigncloud.mobileid.vn/Management/setNTPConfigResponse")
    public ManagementResp setNTPConfig(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getNTPStatus", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetNTPStatus")
    @ResponseWrapper(localName = "getNTPStatusResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetNTPStatusResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/getNTPStatusRequest", output = "http://management.esigncloud.mobileid.vn/Management/getNTPStatusResponse")
    public ManagementResp getNTPStatus(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "restartNTPStatus", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.RestartNTPStatus")
    @ResponseWrapper(localName = "restartNTPStatusResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.RestartNTPStatusResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/restartNTPStatusRequest", output = "http://management.esigncloud.mobileid.vn/Management/restartNTPStatusResponse")
    public ManagementResp restartNTPStatus(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "deleteSigningKey", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.DeleteSigningKey")
    @ResponseWrapper(localName = "deleteSigningKeyResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.DeleteSigningKeyResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/deleteSigningKeyRequest", output = "http://management.esigncloud.mobileid.vn/Management/deleteSigningKeyResponse")
    public ManagementResp deleteSigningKey(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "blockSigningCertificate", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.BlockSigningCertificate")
    @ResponseWrapper(localName = "blockSigningCertificateResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.BlockSigningCertificateResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/blockSigningCertificateRequest", output = "http://management.esigncloud.mobileid.vn/Management/blockSigningCertificateResponse")
    public ManagementResp blockSigningCertificate(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "unblockSigningCertificate", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.UnblockSigningCertificate")
    @ResponseWrapper(localName = "unblockSigningCertificateResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.UnblockSigningCertificateResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/unblockSigningCertificateRequest", output = "http://management.esigncloud.mobileid.vn/Management/unblockSigningCertificateResponse")
    public ManagementResp unblockSigningCertificate(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "changeAuthMode", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.ChangeAuthMode")
    @ResponseWrapper(localName = "changeAuthModeResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.ChangeAuthModeResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/changeAuthModeRequest", output = "http://management.esigncloud.mobileid.vn/Management/changeAuthModeResponse")
    public ManagementResp changeAuthMode(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "updateAuthorizationInfo", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.UpdateAuthorizationInfo")
    @ResponseWrapper(localName = "updateAuthorizationInfoResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.UpdateAuthorizationInfoResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/updateAuthorizationInfoRequest", output = "http://management.esigncloud.mobileid.vn/Management/updateAuthorizationInfoResponse")
    public ManagementResp updateAuthorizationInfo(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "resetBlockingFlagAndRemainingCounter", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.ResetBlockingFlagAndRemainingCounter")
    @ResponseWrapper(localName = "resetBlockingFlagAndRemainingCounterResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.ResetBlockingFlagAndRemainingCounterResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/resetBlockingFlagAndRemainingCounterRequest", output = "http://management.esigncloud.mobileid.vn/Management/resetBlockingFlagAndRemainingCounterResponse")
    public ManagementResp resetBlockingFlagAndRemainingCounter(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "addRegistrationParty", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.AddRegistrationParty")
    @ResponseWrapper(localName = "addRegistrationPartyResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.AddRegistrationPartyResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/addRegistrationPartyRequest", output = "http://management.esigncloud.mobileid.vn/Management/addRegistrationPartyResponse")
    public ManagementResp addRegistrationParty(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getNetworkInterfaces", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetNetworkInterfaces")
    @ResponseWrapper(localName = "getNetworkInterfacesResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.GetNetworkInterfacesResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/getNetworkInterfacesRequest", output = "http://management.esigncloud.mobileid.vn/Management/getNetworkInterfacesResponse")
    public ManagementResp getNetworkInterfaces(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

    /**
     * 
     * @param managementReq
     * @return
     *     returns vn.mobileid.esigncloud.management.ManagementResp
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "setNetworkInterface", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.SetNetworkInterface")
    @ResponseWrapper(localName = "setNetworkInterfaceResponse", targetNamespace = "http://management.esigncloud.mobileid.vn/", className = "vn.mobileid.esigncloud.management.SetNetworkInterfaceResponse")
    @Action(input = "http://management.esigncloud.mobileid.vn/Management/setNetworkInterfaceRequest", output = "http://management.esigncloud.mobileid.vn/Management/setNetworkInterfaceResponse")
    public ManagementResp setNetworkInterface(
        @WebParam(name = "managementReq", targetNamespace = "")
        ManagementReq managementReq);

}
