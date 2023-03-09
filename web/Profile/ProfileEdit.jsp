<%-- 
    Document   : ProfileEdit
    Created on : Dec 10, 2019, 5:37:45 PM
    Author     : USER
--%>

<%@page import="vn.ra.utility.PropertiesContent"%>
<%@page import="vn.ra.process.SessionDNSName"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.ArrayList"%>
<%@page import="vn.ra.process.SessionUploadFileCert"%>
<%@page import="vn.ra.process.EncodeSOPIN"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <%
            String sNewRefreshJS = cogCommon.GetPropertybyCode(Definitions.CONFIG_JS_REFRESH_STRING_RANDOM);
        %>
        <script src="../js/Language.js?t=<%=sNewRefreshJS%>"></script>
        <script src="../js/process_javajs.js?t=<%=sNewRefreshJS%>"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            $(document).ready(function () {
                $('.loading-gif').hide();
                $('#idReceivedDate').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
//                $('#idCompensationDate').daterangepicker({
//                    singleDatePicker: true,
//                    showDropdowns: true
//                }, function (start, end, label) {
//                    console.log(start.toISOString(), end.toISOString(), label);
//                });
            });
            function ValidateForm(idCert, idOwner, idCSRF) {
                if (!JSCheckEmptyField($("#PHONE_CONTRACT").val()))
                {
                    $("#PHONE_CONTRACT").focus();
                    funErrorAlert(policy_req_empty + global_fm_phone_contact);
                    return false;
                } else {
                    if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#PHONE_CONTRACT")))
                    {
                        $("#PHONE_CONTRACT").focus();
                        funErrorAlert(global_req_phone_format);
                        return false;
                    }
                }
                if (!JSCheckEmptyField($("#EMAIL_CONTRACT").val()))
                {
                    $("#EMAIL_CONTRACT").focus();
                    funErrorAlert(policy_req_empty + global_fm_email_contact_signserver);
                    return false;
                } else {
                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#EMAIL_CONTRACT").val()))
                    {
                        $("#EMAIL_CONTRACT").focus();
                        funErrorAlert(global_req_mail_format);
                        return false;
                    }
                }
                if (!JSCheckEmptyField(document.myname.idReceivedDate.value))
                {
                    document.myname.idReceivedDate.focus();
                    funErrorAlert(policy_req_empty + collation_fm_date_receipt);
                    return false;
                }
                if (JSCheckEmptyField($("#idReceivedEmailManager").val()))
                {
//                    var sEmailGroup = sSpace($("#idReceivedEmailManager").val()).split(';');
//                    for (var i = 0; i < sEmailGroup.length; i++) {
//                        var itemEmail = sSpace(sEmailGroup[i]);
//                        if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), itemEmail))
//                        {
//                            $("#idReceivedEmailManager").focus();
//                            funErrorAlert(global_req_mail_format);
//                            return false;
//                        }
//                    }
                }
                if (JSCheckEmptyField($("#idReceivedPhoneManager").val()))
                {
//                    var sPhoneGroup = sSpace($("#idReceivedPhoneManager").val()).split(';');
//                    for (var i = 0; i < sPhoneGroup.length; i++) {
//                        var itemPhone = sSpace(sPhoneGroup[i]);
//                        if (!FormCheckPhoneHandProfile(localStorage.getItem("sessLocal_REGEX_PHONE"), itemPhone))
//                        {
//                            $("#idReceivedPhoneManager").focus();
//                            funErrorAlert(global_req_phone_format);
//                            return false;
//                        }
//                    }
//                    if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#idReceivedPhoneManager")))
//                    {
//                        $("#idReceivedPhoneManager").focus();
//                        funErrorAlert(global_req_phone_format);
//                        return false;
//                    }
                }
                var sCheckReceivedSoftCopy = "0";
                if ($("#idReceivedSoftCopy").is(':checked'))
                {
                    sCheckReceivedSoftCopy = "1";
                }
                var sCheckReceivedReadFile = "0";
                if ($("#idReceivedReadFile").is(':checked'))
                {
                    sCheckReceivedReadFile = "1";
                }
                if(sSpace(document.myname.idCheckEnough.value) === '1')
                {
                    swal({
                        title: "",
                        text: profile_conform_update,
                        imageUrl: "../Images/icon_warning.png",
                        imageSize: "45x45",
                        showCancelButton: true,
                        closeOnConfirm: true,
                        allowOutsideClick: false,
                        confirmButtonText: login_fm_buton_OK,
                        cancelButtonText: global_button_grid_cancel,
                        cancelButtonColor: "#199DC0"
                    },
                    function () {
                        $('body').append('<div id="over"></div>');
                        $(".loading-gif").show();
                        setTimeout(function () {
                            $.ajax({
                                type: "post",
                                url: "../ProfileCommon",
                                data: {
                                    idParam: 'editprofile',
                                    id: idCert,
                                    idOwner: idOwner,
                                    idReceivedDate: document.myname.idReceivedDate.value,
                                    idReceivedNote: document.myname.idReceivedNote.value,
                                    idCheckEnough: document.myname.idCheckEnough.value,
                                    idCheckProfileType: document.myname.idCheckProfileType.value,
                                    PHONE_CONTRACT: document.myname.PHONE_CONTRACT.value,
                                    EMAIL_CONTRACT: document.myname.EMAIL_CONTRACT.value,
                                    FINE_FOR_LACK_OF_BRIEF: document.myname.FINE_FOR_LACK_OF_BRIEF.value,
                                    FEE_AMOUNT: document.myname.FEE_AMOUNT.value,
                                    TOKEN_AMOUNT: document.myname.TOKEN_AMOUNT.value,
                                    idReceivedEmailManager: document.myname.idReceivedEmailManager.value,
                                    idReceivedPhoneManager: document.myname.idReceivedPhoneManager.value,
                                    idReceivedAddressManager: document.myname.idReceivedAddressManager.value,
                                    idReceivedPositionManager: document.myname.idReceivedPositionManager.value,
                                    idReceivedNameManager: document.myname.idReceivedNameManager.value,
                                    idReceivedNameContact: document.myname.idReceivedNameContact.value,
                                    sCheckReceivedSoftCopy: sCheckReceivedSoftCopy,
                                    STATE_PROFILE: $("#STATE_PROFILE").val(),
                                    idReceivedReadFile: sCheckReceivedReadFile,
                                    registerAddressGPKD: $("#registerAddressGPKD").val(),
                                    registerCMND: $("#registerCMND").val(),
                                    registerIssuedDate: $("#registerIssuedDate").val(),
                                    registerIssuedPlace: $("#registerIssuedPlace").val(),
                                    CsrfToken: idCSRF
                                },
                                cache: false,
                                success: function (html)
                                {
                                    var myStrings = sSpace(html).split('#');
                                    if (myStrings[0] === "0")
                                    {
                                        funSuccAlert(inputcertlist_succ_edit, "ProfileList.jsp");
                                    }
                                    else if (myStrings[0] === JS_EX_CSRF)
                                    {
                                        funCsrfAlert();
                                    } else if (myStrings[0] === JS_EX_LOGIN)
                                    {
                                        RedirectPageLoginNoSess(global_alert_login);
                                    }
                                    else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
                                    {
                                        RedirectPageLoginNoSess(global_alert_another_login);
                                    }
                                    else
                                    {
                                        funErrorAlert(global_errorsql);
                                    }
                                    $(".loading-gif").hide();
                                    $('#over').remove();
                                }
                            });
                            return false;
                        }, JS_STR_ACTION_TIMEOUT);
                    });
                } else {
                    $('body').append('<div id="over"></div>');
                    $(".loading-gif").show();
                    $.ajax({
                        type: "post",
                        url: "../ProfileCommon",
                        data: {
                            idParam: 'editprofile',
                            id: idCert,
                            idOwner: idOwner,
                            idReceivedDate: document.myname.idReceivedDate.value,
                            idReceivedNote: document.myname.idReceivedNote.value,
                            idCheckEnough: document.myname.idCheckEnough.value,
                            idCheckProfileType: document.myname.idCheckProfileType.value,
                            PHONE_CONTRACT: document.myname.PHONE_CONTRACT.value,
                            EMAIL_CONTRACT: document.myname.EMAIL_CONTRACT.value,
                            FINE_FOR_LACK_OF_BRIEF: document.myname.FINE_FOR_LACK_OF_BRIEF.value,
                            FEE_AMOUNT: document.myname.FEE_AMOUNT.value,
                            TOKEN_AMOUNT: document.myname.TOKEN_AMOUNT.value,
                            idReceivedEmailManager: document.myname.idReceivedEmailManager.value,
                            idReceivedPhoneManager: document.myname.idReceivedPhoneManager.value,
                            idReceivedAddressManager: document.myname.idReceivedAddressManager.value,
                            idReceivedPositionManager: document.myname.idReceivedPositionManager.value,
                            idReceivedNameManager: document.myname.idReceivedNameManager.value,
                            idReceivedNameContact: document.myname.idReceivedNameContact.value,
                            sCheckReceivedSoftCopy: sCheckReceivedSoftCopy,
                            STATE_PROFILE: $("#STATE_PROFILE").val(),
                            idReceivedReadFile: sCheckReceivedReadFile,
                            registerAddressGPKD: $("#registerAddressGPKD").val(),
                            registerCMND: $("#registerCMND").val(),
                            registerIssuedDate: $("#registerIssuedDate").val(),
                            registerIssuedPlace: $("#registerIssuedPlace").val(),
                            CsrfToken: idCSRF
                        },
                        cache: false,
                        success: function (html)
                        {
                            var myStrings = sSpace(html).split('#');
                            if (myStrings[0] === "0")
                            {
                                funSuccAlert(inputcertlist_succ_edit, "ProfileList.jsp");
                            }
                            else if (myStrings[0] === JS_EX_CSRF)
                            {
                                funCsrfAlert();
                            } else if (myStrings[0] === JS_EX_LOGIN)
                            {
                                RedirectPageLoginNoSess(global_alert_login);
                            }
                            else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
                            {
                                RedirectPageLoginNoSess(global_alert_another_login);
                            }
                            else
                            {
                                funErrorAlert(global_errorsql);
                            }
                            $(".loading-gif").hide();
                            $('#over').remove();
                        }
                    });
                    return false;
                }
            }
            
            function ValidateControls(idCert, idCSRF) {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../ProfileCommon",
                    data: {
                        idParam: 'editcontrolsprofile',
                        id: idCert,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccAlert(inputcertlist_succ_edit, "ProfileList.jsp");
                        }
                        else if (myStrings[0] === JS_EX_CSRF)
                        {
                            funCsrfAlert();
                        } else if (myStrings[0] === JS_EX_LOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else
                        {
                            funErrorAlert(global_errorsql);
                        }
                        $(".loading-gif").hide();
                        $('#over').remove();
                    }
                });
                return false;
            }
            
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../UserCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'RefreshProfileCertSess'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "ProfileList.jsp";
                        }
                        else
                        {
                            window.location = "ProfileList.jsp";
                        }
                    }
                });
                return false;
            }
        </script>
        <style>
            .field-icon {
                float: right;
                margin-left: -25px;
                margin-top: -25px;
                position: relative;
                z-index: 2;
            }
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.2em 10px 1.2em !important;
                margin: 0 0 12px 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
            .table > thead > tr > th{border-bottom: none;}
            .btn{margin-bottom: 0px;}
            @media (min-width: 768px){.modal-dialog{width: 900px;}}
            .modal-header{
                padding-right: 10px; padding-top: 10px;
            }
        </style>
    </head>
    <body>
        <%         
            if (session.getAttribute("sUserID") != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                session.setAttribute("sessProfileStateDK01", null);
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <%                                        CERTIFICATION[][] rs = new CERTIFICATION[1][];
            String sMaxLengthFile = cogCommon.GetPropertybyCode(Definitions.CONFIG_JACK_RABBIT_MAX_LENGTH_FILE).trim();
            session.setAttribute("sessUploadFileCertProfile", null);
            try {
                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                if (EscapeUtils.IsInteger(ids) == true) {
                    db.S_BO_CERTIFICATION_DETAIL(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                    if (rs[0].length > 0) {
                        boolean isAccessAgencyPage = true;
                        if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            BRANCH[][] branchAccess = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                            isAccessAgencyPage = CommonFunction.checkBranchTreeInvalidCert(rs[0][0].BRANCH_ID, branchAccess);
                        }
                        if (isAccessAgencyPage == true) {
                            String sOWNER_ID = String.valueOf(rs[0][0].CERTIFICATION_OWNER_ID);
                            String sCERTIFICATION_SN = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_SN);
                            String strEMAIL_CONTRACT = EscapeUtils.CheckTextNull(rs[0][0].EMAIL_CONTRACT);
                            String strPHONE_CONTRACT = EscapeUtils.CheckTextNull(rs[0][0].PHONE_CONTRACT);
                            String strCERTIFICATION_STATE_DESC = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_STATE_DESC);
                            String strCROSS_CHECKED_DT = EscapeUtils.CheckTextNull(rs[0][0].CROSS_CHECKED_DT);
                            String strISSUED_DT = EscapeUtils.CheckTextNull(rs[0][0].ISSUED_DT);
                            String strMST_MNS = rs[0][0].ENTERPRISE_ID; //EscapeUtils.CheckTextNull(rs[0][0].TAX_CODE);
                            String strCMND_HC = rs[0][0].PERSONAL_ID;// EscapeUtils.CheckTextNull(rs[0][0].P_ID);
                            String strCompany = EscapeUtils.CheckTextNull(rs[0][0].COMPANY_NAME);
                            String strPersonal = EscapeUtils.CheckTextNull(rs[0][0].PERSONAL_NAME);
                            String sCOLLECTED_NOTE = rs[0][0].PROFILE_NOTE;
                            String strDN = EscapeUtils.CheckTextNull(rs[0][0].SUBJECT);
                            if(!"".equals(strDN))
                            {
                                strDN = strDN.replace("\\,", "###");
                            }
                            String strFEE_AMOUNT = "0";
                            if(rs[0][0].FEE_AMOUNT != 0)
                            {
                                strFEE_AMOUNT = com.convertMoneyAnotherZero(rs[0][0].FEE_AMOUNT);
                            }
                            String strTOKEN_AMOUNT = "0";
                            if(rs[0][0].TOKEN_AMOUNT != 0)
                            {
                                strTOKEN_AMOUNT = com.convertMoneyAnotherZero(rs[0][0].TOKEN_AMOUNT);
                            }
                            String sRegexPolicy = "";
                            String sArrayFileExten = "";
                            GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                            if (sessGeneralPolicy[0].length > 0) {
                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                {
                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_REGEX_FOR_PHONE_EMAIL)) {
                                        sRegexPolicy = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_ALLOWED_FILE_EXTENSION_LIST)) {
                                        sArrayFileExten = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                }
                            }
                            String sREGEX_PHONE = Definitions.CONFIG_DEFAULT_VALUE_REGEX_PHONE;
                            String sREGEX_EMAIL = Definitions.CONFIG_DEFAULT_VALUE_REGEX_EMAIL;
                            if(!"".equals(sRegexPolicy)) {
                                sREGEX_PHONE = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_PHONE);
                                sREGEX_EMAIL = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_EMAIL);
                            }
                            String sRepresentEnabled = LoadParamSystem.getParamStart(Definitions.CONFIG_REGISTER_REPRESENT_FORM_ENABLED);
                            String signProfileEnabled = LoadParamSystem.getParamStart(Definitions.CONFIG_SIGN_CERT_PROFILE_ENABLED);
                            String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                            String isViewActionFileEnable = "1";
                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) && !Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                isViewActionFileEnable = "0";
                            }
        %>
        <%
            request.getSession(false).setAttribute("SessCollectedBriefPro", null);
            CERTIFICATION[][] rsBrief = new CERTIFICATION[1][];
            db.S_BO_CERTIFICATION_BRIEF_DETAIL(ids, rsBrief);
            boolean booBRIEF_TYPE = false;
            boolean booCOLLECT_ENABLED = false;
            boolean booCOMMIT_ENABLED = false;
            String sBRIEF_PROPERTIES = "";
            String sADDRESS = "";
            String sPOSITION = "";
            String sREPRESENTATIVE_EMAIL = "";
            String sREPRESENTATIVE_PHONE = "";
            String sREPRESENTATIVE_NAME = "";
            String sCONTACT_NAME = "";
            String PIDIssuedBy = "";
            String PIDDate = "";
            String PID = "";
            String AddressLicense = "";
            String pINFO_BRIEF = "";
            boolean booCheckSoftCopy = false;
            int iSTATE_PROFILE = 0;
            int iBUSINESS_LICENSE_TYPE_ID = 0;
            String sCOLLECTED_BRIEF_DT = "";
            String sFINE_FOR_LACK_OF_BRIEF="";
            ObjectMapper oMapperParse;
            if(rsBrief[0].length > 0)
            {
                sFINE_FOR_LACK_OF_BRIEF = com.convertMoneyAnotherZero(rsBrief[0][0].FINE_FOR_LACK_OF_BRIEF);
                booBRIEF_TYPE = rsBrief[0][0].BRIEF_TYPE;
                iBUSINESS_LICENSE_TYPE_ID = rsBrief[0][0].BUSINESS_LICENSE_TYPE_ID;
                pINFO_BRIEF = rsBrief[0][0].INFO_BRIEF;
                booCOMMIT_ENABLED = rsBrief[0][0].COMMIT_ENABLED;
                iSTATE_PROFILE = rsBrief[0][0].FILE_MANAGER_STATE_ID;
                sCOLLECTED_BRIEF_DT = EscapeUtils.CheckTextNull(rsBrief[0][0].RECEIVED_BRIEF_DATE);
                booCheckSoftCopy = rsBrief[0][0].COLLECT_SOFTCOPY;
                booCOLLECT_ENABLED = rsBrief[0][0].COLLECT_ENABLED;
                sBRIEF_PROPERTIES = EscapeUtils.CheckTextNull(rsBrief[0][0].BRIEF_PROPERTIES);
                String sPrfileContact = EscapeUtils.CheckTextNull(rsBrief[0][0].PROFILE_CONTACT_INFO);
                if(!"".equals(sPrfileContact)) {
                    try {
                        oMapperParse = new ObjectMapper();
                        ProfileContactInfoJson profileContact = oMapperParse.readValue(sPrfileContact, ProfileContactInfoJson.class);
                        if(profileContact != null) {
                            PIDIssuedBy = CommonFunction.replaceCharaterSpecialJson(profileContact.PIDIssuedBy, false);
                            PIDDate = EscapeUtils.CheckTextNull(profileContact.PIDDate);
                            PID = EscapeUtils.CheckTextNull(profileContact.PID);
                            AddressLicense = CommonFunction.replaceCharaterSpecialJson(profileContact.AddressLicense, false);
                            sREPRESENTATIVE_EMAIL = EscapeUtils.CheckTextNull(profileContact.RepresentativeEmail);
                            sREPRESENTATIVE_PHONE = EscapeUtils.CheckTextNull(profileContact.RepresentativePhone);
                            sREPRESENTATIVE_NAME = CommonFunction.replaceCharaterSpecialJson(profileContact.RepresentativeName, false);
                            sCONTACT_NAME = CommonFunction.replaceCharaterSpecialJson(profileContact.ContactName, false);
                            sADDRESS = CommonFunction.replaceCharaterSpecialJson(profileContact.Address, false);
                            sPOSITION = CommonFunction.replaceCharaterSpecialJson(profileContact.Position, false);
                        }
                    } catch(Exception e){CommonFunction.LogExceptionServlet(null, "ProfileContactInfoJson: " + e.getMessage(), e);}
                }
                if(!"".equals(sBRIEF_PROPERTIES)) {
                    try {
                        CERTIFICATION_POLICY_DATA[][] resIPData = new CERTIFICATION_POLICY_DATA[1][];
                        CommonFunction.getCollectedBriefProperties(sBRIEF_PROPERTIES, resIPData);
                        if(resIPData != null) {
                            if(resIPData[0].length > 0) {
                                request.getSession(false).setAttribute("SessCollectedBriefPro", resIPData);
                            }
                        }
                    } catch(Exception e){CommonFunction.LogExceptionServlet(null, "SessCollectedBriefPro load: " + e.getMessage(), e);}
                }
            }
        %>
        <script>
            $(document).ready(function () {
                localStorage.setItem("sessLocal_REGEX_PHONE", '<%=sREGEX_PHONE%>');
                localStorage.setItem("sessLocal_REGEX_EMAIL", '<%=sREGEX_EMAIL%>');
            });
        </script>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEdits"></span></h2>
            <script>$("#idLblTitleEdits").text(profile_title_detail);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <%
                        if(booCOLLECT_ENABLED == false) {
                    %>
                    <input id="btnSave" type="button" class="btn btn-info" data-switch-get="state" onclick="ValidateForm('<%= ids%>', '<%= sOWNER_ID%>', '<%=anticsrf%>');"/>
                    <script>
                        document.getElementById("btnSave").value = global_fm_button_edit;
                    </script>
                    <%
                        } else {
                    %>
                    <input id="btnSave" type="button" class="btn btn-info" data-switch-get="state" onclick="ValidateControls('<%= ids%>', '<%=anticsrf%>');"/>
                    <script>
                        document.getElementById("btnSave").value = global_fm_button_edit;
                    </script>
                    <%
                        }
                    %>
                    <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                    <script>
                        document.getElementById("btnClose").value = global_fm_button_close;
                    </script>
                </li>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <form name="myname" method="post" class="form-horizontal">
                <input type="hidden" id="sID" name="sID" hidden="true" readonly="true" value="<%= rs[0][0].ID%>">
                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                <input type="hidden" name="idHiddenDN" id="idHiddenDN" value="<%=strDN%>"/>
                <%
                    if(!"".equals(sCERTIFICATION_SN)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCertSN" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="CERTIFICATION_SN" disabled value="<%= sCERTIFICATION_SN%>" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleCertSN").text(global_fm_serial);</script>
                </div>
                <%
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCertState" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" disabled name="CERTIFICATION_STATE_DESC" value="<%= strCERTIFICATION_STATE_DESC%>" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleCertState").text(global_fm_Status_cert);</script>
                </div>
                <%
                    if(!"".equals(strMST_MNS)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleMSTMNS"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" disabled name="MST_MNS" value="<%= strMST_MNS%>" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleMSTMNS").text(global_fm_enterprise_id);</script>
                </div>
                <%
                    }
                %>
                <%
                    if(!"".equals(strCMND_HC)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleCMNDHC"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" disabled name="CMND_HC" value="<%= strCMND_HC%>" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleCMNDHC").text(global_fm_personal_id);</script>
                </div>
                <%
                    }
                %>
                <%
                    if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVOKED) {
                        if(!"".equals(rs[0][0].REVOCATION_REASON)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleDeclineReason" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="DeclineReason" readonly value="<%= rs[0][0].REVOCATION_REASON%>" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleDeclineReason").text(global_fm_revoke_desc);</script>
                </div>
                <%
                        }
                    }
                %>
                <%
                    if(!"".equals(strCompany)) {
                        String sClear = "";
                        if(!"".equals(strMST_MNS) && !"".equals(strCMND_HC)) {
                            sClear = "clear: both;";
                        }
                %>
                <div class="col-sm-6" style="padding-left: 0;<%=sClear%>">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleCompany"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" disabled name="Company" value="<%= strCompany%>" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleCompany").text(global_fm_grid_company);</script>
                </div>
                <%
                    }
                %>
                <%
                    if(!"".equals(strPersonal)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitlePersonal"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" disabled name="Personal" value="<%= strPersonal%>" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitlePersonal").text(global_fm_grid_personal);</script>
                </div>
                <%
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;display: none;">
                    <div class="form-group">
                        <label id="idLblTitleCertAttrType" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="CERTIFICATION_ATTR_TYPE" disabled value="<%= EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_ATTR_TYPE_DESC) %>" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleCertAttrType").text(global_fm_requesttype);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;display: none;">
                    <div class="form-group">
                        <label id="idLblTitleCA" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" disabled id="CERTIFICATION_AUTHORITY" value="<%= EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_AUTHORITY_DESC) %>" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleCA").text(global_fm_ca);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;display: none;">
                    <div class="form-group">
                        <label id="idLblTitleCertPurpose" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" disabled id="CERTIFICATION_PURPOSE" value="<%= EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_PURPOSE_DESC) %>" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleCertPurpose").text(global_fm_certpurpose);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleFormFactor" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" disabled id="PKI_FORMFACTOR_DETAIL" value="<%= EscapeUtils.CheckTextNull(rs[0][0].PKI_FORMFACTOR_DESC) %>" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleFormFactor").text(global_fm_Method);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCertDuration" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" disabled id="DURATION" value="<%= EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_PROFILE_DESC) %>" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleCertDuration").text(global_fm_duration_cts);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;display: none;">
                    <div class="form-group">
                        <label id="idLblTitleDetailFeeAmount" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="strFEE_AMOUNT" value="<%= strFEE_AMOUNT%>" disabled class="form-control123"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleDetailFeeAmount").text(global_fm_amount_fee);</script>
                </div>
                <%
                    if(!"".equals(strISSUED_DT)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleIssueDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="strISSUED_DT" value="<%= strISSUED_DT%>" disabled class="form-control123"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleIssueDate").text(global_fm_date_gencert);</script>
                </div>
                <%
                    }
                %>
                <%
                    boolean booCOLLECT_ENABLED_Two = booCOLLECT_ENABLED;
                    if (!Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                        booCOLLECT_ENABLED_Two = true;
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitlePhoneContact"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="PHONE_CONTRACT" <%= booCOLLECT_ENABLED_Two ? "readonly" : ""%> id="PHONE_CONTRACT" value="<%= strPHONE_CONTRACT%>" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitlePhoneContact").text(global_fm_phone_contact);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleEmailContact"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="EMAIL_CONTRACT" <%= booCOLLECT_ENABLED_Two ? "readonly" : ""%> id="EMAIL_CONTRACT" value="<%= strEMAIL_CONTRACT%>" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleEmailContact").text(global_fm_email_contact);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleFeeAmount"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="FEE_AMOUNT" id="FEE_AMOUNT" value="<%= strFEE_AMOUNT%>"
                                <%= booCOLLECT_ENABLED_Two ? "readonly" : ""%> class="form-control123" oninput="autoConvertMoney(this.value, $('#FEE_AMOUNT'))">
                        </div>
                    </div>
                    <script>$("#idLblTitleFeeAmount").text(global_fm_amount_fee);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleTokenAmount"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="TOKEN_AMOUNT" id="TOKEN_AMOUNT" value="<%= strTOKEN_AMOUNT%>"
                                <%= booCOLLECT_ENABLED_Two ? "readonly" : ""%> class="form-control123" oninput="autoConvertMoney(this.value, $('#TOKEN_AMOUNT'))">
                        </div>
                    </div>
                    <script>$("#idLblTitleTokenAmount").text(global_fm_amount_token);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleFineLack"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="FINE_FOR_LACK_OF_BRIEF" maxlength="11" id="FINE_FOR_LACK_OF_BRIEF" value="<%= sFINE_FOR_LACK_OF_BRIEF%>"
                                <%= booCOLLECT_ENABLED_Two ? "readonly" : ""%> class="form-control123" onblur="autoConvertMoney(this.value, $('#FINE_FOR_LACK_OF_BRIEF'))" oninput="autoConvertMoney(this.value, $('#FINE_FOR_LACK_OF_BRIEF'))">
                        </div>
                    </div>
                    <script>$("#idLblTitleFineLack").text(collation_fm_money_overdue);</script>
                </div>
                <%
                    if(!"".equals(strCROSS_CHECKED_DT)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCollationTime" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="strCROSS_CHECKED_DT" value="<%= strCROSS_CHECKED_DT%>" disabled class="form-control123"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleCollationTime").text(collation_fm_time);</script>
                </div>
                <%
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleDateReceipt" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="Text" readonly id="idReceivedDate" name="idReceivedDate" <%= booCOLLECT_ENABLED_Two ? "disabled" : ""%> maxlength="25" class="form-control123"
                                value="<%= "".equals(sCOLLECTED_BRIEF_DT) ? com.ConvertMonthSub(0) : sCOLLECTED_BRIEF_DT %>"
                                style="<%= booCOLLECT_ENABLED_Two ? "background-color:#eee;opacity:1" : "background-color:#fff;opacity:1" %>"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleDateReceipt").text(collation_fm_date_receipt);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleProfileType" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="idCheckProfileType" id="idCheckProfileType" class="form-control123" <%= booCOLLECT_ENABLED_Two ? "disabled" : ""%>>
                                <option value="0" id="idLblTitleTypeInMounth" <%= booBRIEF_TYPE ? "" : "selected" %>></option>
                                <option value="1" id="idLblTitleTypeCompensation" <%= booBRIEF_TYPE ? "selected" : "" %>></option>
                            </select>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleTypeInMounth").text(collation_fm_type_inmounth);
                        $("#idLblTitleTypeCompensation").text(collation_fm_profile_overdue);
                        $("#idLblTitleProfileType").text(collation_fm_type);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleEnough" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="idCheckEnough" id="idCheckEnough" class="form-control123" <%= booCOLLECT_ENABLED_Two ? "disabled" : ""%>>
                                <option value="0" id="idLblTitleUnEnoughed" <%= booCOLLECT_ENABLED ? "" : "selected" %>></option>
                                <option value="1" id="idLblTitleEnoughed" <%= booCOLLECT_ENABLED ? "selected" : "" %>></option>
                            </select>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleEnoughed").text(profile_fm_enoughed);
                        $("#idLblTitleUnEnoughed").text(profile_fm_unenoughed);
                        $("#idLblTitleEnough").text(global_fm_status_control);
                    </script>
                </div>
                <style>.form-control123[readonly]{background-color:#eee;opacity:1}</style>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleNoteReceipt" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="Text" id="idReceivedNote" name="idReceivedNote" class="form-control123"
                                value="<%= sCOLLECTED_NOTE %>" <%= booCOLLECT_ENABLED_Two ? "readonly" : ""%> />
                        </div>
                    </div>
                    <script>$("#idLblTitleNoteReceipt").text(global_fm_Note_offset);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleSoftCopy" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="idReceivedSoftCopy" style="margin-bottom: 0px;clear: both;">
                                <input type="checkbox" name="idReceivedSoftCopy" id="idReceivedSoftCopy"
                                    <%= booCheckSoftCopy ? "checked" : ""%> <%=booCOLLECT_ENABLED_Two ? "disabled" : ""%>/>
                                <div id="idReceivedSoftCopyClass" class="slider round <%=booCOLLECT_ENABLED_Two ? "disabled" : ""%>"></div>
                            </label>
                        </div>
                    </div>
                    <script>$("#idLblTitleSoftCopy").text(global_fm_soft_copy);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleStateProfileDetail" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="STATE_PROFILE" id="STATE_PROFILE" class="form-control123" <%=booCOLLECT_ENABLED_Two ? "disabled" : ""%>>
                                <%
                                    FILE_MANAGER_STATE[][] rsStatus = new FILE_MANAGER_STATE[1][];
                                    db.S_BO_FILE_MANAGER_STATE_COMBOBOX(sessLanguageGlobal, rsStatus);
                                    if (rsStatus[0].length > 0) {
                                        for (FILE_MANAGER_STATE temp1 : rsStatus[0]) {
                                %>
                                <option value="<%=String.valueOf(temp1.ID)%>" <%= temp1.ID == iSTATE_PROFILE ? "selected" : ""%>><%=temp1.REMARK%></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <script>$("#idLblTitleStateProfileDetail").text(global_fm_status_profile);</script>
                </div>
                <%
                    boolean booCOMMIT_ENABLEDView = booCOLLECT_ENABLED_Two;
                    String sViewReadFile = "";
                    if(booCOMMIT_ENABLED == true){booCOMMIT_ENABLEDView=true;}
                    String idViewContactAdded = "";
                    if (!Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                        sViewReadFile = "none";
                        idViewContactAdded = "none";
                        booCOMMIT_ENABLED = false;
                        booCOMMIT_ENABLEDView =true;
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;display: <%= sViewReadFile%>">
                    <div class="form-group">
                        <label id="idLblTitleReadFile" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="idReceivedReadFile" style="margin-bottom: 0px;clear: both;">
                                <input type="checkbox" name="idReceivedReadFile" id="idReceivedReadFile"
                                    <%= booCOMMIT_ENABLED ? "checked" : ""%> <%=booCOMMIT_ENABLEDView ? "disabled" : ""%>/>
                                <div id="idReceivedReadFileClass" class="slider round <%=booCOMMIT_ENABLEDView ? "disabled" : ""%>"></div>
                            </label>
                        </div>
                    </div>
                    <script>$("#idLblTitleReadFile").text(collation_fm_approved_profile);</script>
                </div>
                <fieldset class="scheduler-border" style="clear: both; display: <%=idViewContactAdded%>">
                    <legend class="scheduler-border" id="idLblTitleContactAdded"></legend>
                    <script>$("#idLblTitleContactAdded").text(certlist_group_add_info);</script>
                    <div class="col-sm-6" style="padding-left: 0;display: <%= "1".equals(sRepresentEnabled) ? "" : "none" %>">
                        <div class="form-group">
                            <label id="idLblTitleAddressGPKD" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="Text" id="registerAddressGPKD" name="registerAddressGPKD" class="form-control123"
                                    value="<%= AddressLicense %>" <%= booCOLLECT_ENABLED_Two ? "readonly" : ""%> />
                            </div>
                        </div>
                        <script>$("#idLblTitleAddressGPKD").text(global_fm_address + " (" + collation_fm_print_GPKD + ")");</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleNameManager" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="Text" id="idReceivedNameManager" name="idReceivedNameManager" class="form-control123"
                                    value="<%= sREPRESENTATIVE_NAME %>" <%= booCOLLECT_ENABLED_Two ? "readonly" : ""%> />
                            </div>
                        </div>
                        <script>$("#idLblTitleNameManager").text(global_fm_name_manager);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleNameContact" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="Text" id="idReceivedNameContact" name="idReceivedNameContact" class="form-control123"
                                    value="<%= sCONTACT_NAME %>" <%= booCOLLECT_ENABLED_Two ? "readonly" : ""%> />
                            </div>
                        </div>
                        <script>$("#idLblTitleNameContact").text(global_fm_name_contact);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;display: <%= "1".equals(sRepresentEnabled) ? "" : "none" %>">
                        <div class="form-group">
                            <label id="idLblTitleRegisterCMND" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="Text" id="registerCMND" name="registerCMND" class="form-control123"
                                    value="<%= PID %>" <%= booCOLLECT_ENABLED_Two ? "readonly" : ""%> />
                            </div>
                        </div>
                        <script>$("#idLblTitleRegisterCMND").text(global_fm_CMND);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;display: <%= "1".equals(sRepresentEnabled) ? "" : "none" %>">
                        <div class="form-group">
                            <label id="idLblTitleRegisterIssuedDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="Text" id="registerIssuedDate" name="registerIssuedDate" class="form-control123"
                                    value="<%= PIDDate %>" <%= booCOLLECT_ENABLED_Two ? "readonly" : ""%> />
                            </div>
                        </div>
                        <script>$("#idLblTitleRegisterIssuedDate").text(global_fm_cmnd_date);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;display: <%= "1".equals(sRepresentEnabled) ? "" : "none" %>">
                        <div class="form-group">
                            <label id="idLblTitleRegisterIssuedPlace" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="Text" id="registerIssuedPlace" name="registerIssuedPlace" class="form-control123"
                                    value="<%= PIDIssuedBy %>" <%= booCOLLECT_ENABLED_Two ? "readonly" : ""%> />
                            </div>
                        </div>
                        <script>$("#idLblTitleRegisterIssuedPlace").text(global_fm_place);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;display: <%= !"1".equals(sRepresentEnabled) ? "" : "none" %>">
                       <div class="form-group">
                           <label id="idLblTitleEmailManager" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                           <div class="col-sm-7" style="padding-right: 0px;">
                               <input type="Text" id="idReceivedEmailManager" name="idReceivedEmailManager" class="form-control123"
                                   value="<%= sREPRESENTATIVE_EMAIL %>" <%= booCOLLECT_ENABLED_Two ? "readonly" : ""%> />
                           </div>
                       </div>
                       <script>$("#idLblTitleEmailManager").text(global_fm_email_manager);</script>
                   </div>
                   <div class="col-sm-6" style="padding-left: 0;display: <%= !"1".equals(sRepresentEnabled) ? "" : "none" %>">
                       <div class="form-group">
                           <label id="idLblTitlePhoneManager" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                           <div class="col-sm-7" style="padding-right: 0px;">
                               <input type="Text" id="idReceivedPhoneManager" name="idReceivedPhoneManager" class="form-control123"
                                   value="<%= sREPRESENTATIVE_PHONE %>" <%= booCOLLECT_ENABLED_Two ? "readonly" : ""%> />
                           </div>
                       </div>
                       <script>$("#idLblTitlePhoneManager").text(global_fm_phone_manager);</script>
                   </div>
                   <div class="col-sm-6" style="padding-left: 0;">
                       <div class="form-group">
                           <label id="idLblTitlePositionManager" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                           <div class="col-sm-7" style="padding-right: 0px;">
                               <input type="Text" id="idReceivedPositionManager" name="idReceivedPositionManager" class="form-control123"
                                   value="<%= sPOSITION %>" <%= booCOLLECT_ENABLED_Two ? "readonly" : ""%> />
                           </div>
                       </div>
                       <script>$("#idLblTitlePositionManager").text(branch_fm_representative_position);</script>
                   </div>
                   <div class="col-sm-6" style="padding-left: 0;">
                       <div class="form-group">
                           <label id="idLblTitleAddressManager" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                           <div class="col-sm-7" style="padding-right: 0px;">
                               <input type="Text" id="idReceivedAddressManager" name="idReceivedAddressManager" class="form-control123"
                                   value="<%= sADDRESS %>" <%= booCOLLECT_ENABLED_Two ? "readonly" : ""%> />
                           </div>
                       </div>
                       <script>$("#idLblTitleAddressManager").text(global_fm_address);</script>
                   </div>
                </fieldset>
                <%
                    boolean isHasFileOfUser = true;
                    FILE_MANAGER[][] rsFileMana = new FILE_MANAGER[1][];
                    db.S_BO_FILE_MANAGER_GET_BY_CERTIFICATION_AND_OWNER(EscapeUtils.escapeHtml(ids), sOWNER_ID, sessLanguageGlobal, rsFileMana);
                    boolean isHasFileManagerLicense = false;
                    if(rsFileMana[0].length > 0)
                    {
                        for(FILE_MANAGER rsFile : rsFileMana[0])
                        {
                            if(rsFile.FILE_PROFILE_NAME.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT))
                            {
                                isHasFileManagerLicense = true;
                                break;
                            }
                        }
                    }
                %>
                <%
                    if(isHasFileOfUser == true) {
                %>
                <div id="idDivShowFileMana">
                <%
                    String sJSON = "";
                    CERTIFICATION_PURPOSE[][] rsPURPOSE = new CERTIFICATION_PURPOSE[1][];
                    db.S_BO_CERTIFICATION_PURPOSE_DETAIL_BY_CERTIFICATION_ATTR_TYPE(String.valueOf(rs[0][0].CERTIFICATION_PURPOSE_ID), "", rsPURPOSE);
                    if(rsPURPOSE[0].length > 0)
                    {
                        sJSON = EscapeUtils.CheckTextNull(rsPURPOSE[0][0].FILE_PROPERTIES);
                    }
                    if(!"".equals(sJSON)) {
                        SessionUploadFileCert cartIP = (SessionUploadFileCert) session.getAttribute("sessUploadFileCertProfile");
                        cartIP = new SessionUploadFileCert();
                        if(rsFileMana[0].length > 0)
                        {
                            for(FILE_MANAGER rsFile : rsFileMana[0])
                            {
                                FILE_PROFILE_DATA item = new FILE_PROFILE_DATA();
                                item.FILE_MANAGER_ID = rsFile.ID;
                                item.FILE_NAME = rsFile.FILE_NAME;
                                item.FILE_PROFILE = rsFile.FILE_PROFILE_NAME;
                                item.FILE_SIZE = (double) rsFile.FILE_SIZE;
                                item.FILE_MIMETYPE = rsFile.MIME_TYPE_NAME;
                                item.FILE_STREAM = null;
                                item.SIGNED = rsFile.SIGNED;
                                item.MODIFIED_DT = rsFile.MODIFIED_DT;
                                cartIP.AddRoleFunctionsList(item);
                            }
                            session.setAttribute("sessUploadFileCertProfile", cartIP);
                        }
                        boolean isEnableUploadFile = false;
                        if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                            if(rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_DECLINED
                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED_EXPIRED
                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_STOPPED_OPERATION)
                            {
                                isEnableUploadFile = true;
                            }
                        } else {
                            if(rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_DECLINED) {
                                isEnableUploadFile = true;
                            }
                        }
                        CERTIFICATION_POLICY_DATA[][] resCollectData = null;
                        if(session.getAttribute("SessCollectedBriefPro") != null) {
                            resCollectData = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessCollectedBriefPro");
                        }
                        oMapperParse = new ObjectMapper();
                        FILE_PROFILE_JSON itemParsePush = oMapperParse.readValue(sJSON, FILE_PROFILE_JSON.class);
                        int jFile = 1;
                        if(itemParsePush != null) {
                            try {
                                for (FILE_PROFILE_JSON.Attribute attribute : itemParsePush.getAttributes()) {
                                    String sCheckDisabled = "";
                                    if(booCOLLECT_ENABLED == true) {
                                        sCheckDisabled = "disabled";
                                    }
                                    String sViewCheckTypeFile = "";
                                    if (!Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                        sViewCheckTypeFile = "none";
                                    }
                                    String sName = attribute.getName().trim();
                                    boolean booCheckCollect = CommonFunction.checkBriefFileType(sName, resCollectData);
                                    boolean booScanCollect = CommonFunction.checkBriefFileScan(sName, resCollectData);
                                    String sRemark = attribute.getRemark().trim();
                                    if(!"1".equals(sessLanguageGlobal)){
                                        sRemark = attribute.getRemarkEn().trim();
                                    }
                                    if(sName.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT)) {
                                        isEnableUploadFile = false;
                                    }
                                    String sNameInID = attribute.getName().trim() + String.valueOf(jFile);
                                    if(attribute.getEnabled() == true) {
                                        String displayShowScan = "none";
                                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                            if(sName.equals(Definitions.CONFIG_FILE_PROFILE_PHOTO_ID_CARD)
                                                || sName.equals(Definitions.CONFIG_FILE_PROFILE_PHOTO_ACTIVITY_DECLARATION)) {
                                                displayShowScan = "";
                                            }
                                        }
                %>
                <fieldset class="scheduler-border" style="clear: both;">
                    <legend class="scheduler-border"><%= sRemark%> &nbsp;
                        <label class="switch" for="idCheck<%=sName%>" style="margin-bottom: 0px;clear: both;display: <%=sViewCheckTypeFile%>">
                            <input type="checkbox" name="idCheck<%=sName%>" id="idCheck<%=sName%>" onclick="onFileTypeCheck('idCheck'+'<%=sName%>', '<%=sName%>');"
                                <%= booCheckCollect ? "checked" : ""%> <%=sCheckDisabled%>/>
                            <div id="idCheck<%=sName%>Class" class="slider round <%=sCheckDisabled%>"></div>
                        </label>
                        <%
                            if(sName.equals(Definitions.CONFIG_FILE_PROFILE_SERVICE_REGISTRATION_DOCUMENT)
                                && isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                session.setAttribute("sessProfileStateDK01", String.valueOf(iBUSINESS_LICENSE_TYPE_ID));
                        %>
                        <div>
                            <label id="idLblTitleStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>"></label>&nbsp;
                            <label class="switch" for="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>" style="margin-bottom: 0px;clear: both;">
                                <input type="checkbox" name="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>" id="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>"
                                    onclick="onChangeStateDK01('idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>', '<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>');"
                                    <%= iBUSINESS_LICENSE_TYPE_ID == Definitions.CONFIG_BRIEF_TYPE_ID_SCAN ? "checked" : ""%> <%=sCheckDisabled%>/>
                                <div id="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>Class" class="slider round <%=sCheckDisabled%>"></div>
                            </label>
                            <script>$("#idLblTitleStateDK01"+'<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>').text(global_fm_profile_scan);</script>
                            
                            <label id="idLblTitleStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>"></label>&nbsp;
                            <label class="switch" for="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>" style="margin-bottom: 0px;clear: both;">
                                <input type="checkbox" name="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>" id="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>"
                                    onclick="onChangeStateDK01('idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>', '<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>');"
                                    <%= iBUSINESS_LICENSE_TYPE_ID == Definitions.CONFIG_BRIEF_TYPE_ID_DIGITAL_SIGNATURE ? "checked" : ""%> <%=sCheckDisabled%>/>
                                <div id="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>Class" class="slider round <%=sCheckDisabled%>"></div>
                            </label>
                            <script>$("#idLblTitleStateDK01"+'<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>').text(global_fm_profile_signature);</script>
                            
                            <label id="idLblTitleStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_PAPER%>"></label>&nbsp;
                            <label class="switch" for="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_PAPER%>" style="margin-bottom: 0px;clear: both;">
                                <input type="checkbox" name="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_PAPER%>" id="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_PAPER%>"
                                    onclick="onChangeStateDK01('idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_PAPER%>', '<%=Definitions.CONFIG_BRIEF_TYPE_CODE_PAPER%>');"
                                    <%= iBUSINESS_LICENSE_TYPE_ID == Definitions.CONFIG_BRIEF_TYPE_ID_PAPER ? "checked" : ""%> <%=sCheckDisabled%>/>
                                <div id="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_PAPER%>Class" class="slider round <%=sCheckDisabled%>"></div>
                            </label>
                            <script>$("#idLblTitleStateDK01"+'<%=Definitions.CONFIG_BRIEF_TYPE_CODE_PAPER%>').text(global_fm_profile_paper);</script>
                        </div>
                        <script>
                            function onChangeStateDK01(vIdChecked, vName)
                            {
                                var isCheck = "0";
                                if ($("#"+vIdChecked).is(':checked')) {
                                    isCheck = "1";
                                }
                                if(isCheck === "1"){
                                    if(vName === "SCAN") {
                                        $('#idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>').prop('checked', false);
                                        $('#idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_PAPER%>').prop('checked', false);
                                    } else if(vName === "DIGITAL_SIGNATURE"){
                                        $('#idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>').prop('checked', false);
                                        $('#idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_PAPER%>').prop('checked', false);
                                    } else if(vName === "PAPER"){
                                        $("#idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>").prop('checked', false);
                                        $("#idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>").prop('checked', false);
                                    }
                                }
                                
                                $.ajax({
                                    type: "post",
                                    url: "../ProfileCommon",
                                    data: {
                                        idParam: 'checkprofilestatedk01',
                                        sChecked: isCheck,
                                        sName: vName
                                    },
                                    cache: false,
                                    success: function (html)
                                    {
                                        var myStrings = sSpace(html).split('#');
                                        if (myStrings[0] === "0") { }
                                        else if (myStrings[0] === JS_EX_CSRF)
                                        {
                                            funCsrfAlert();
                                        } else if (myStrings[0] === JS_EX_LOGIN)
                                        {
                                            RedirectPageLoginNoSess(global_alert_login);
                                        }
                                        else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
                                        {
                                            RedirectPageLoginNoSess(global_alert_another_login);
                                        }
                                        else
                                        {
                                            funErrorAlert(global_errorsql);
                                        }
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                    }
                                });
                                return false;
                            }
                        </script>
                        <%
                            }
                        %>
                        
                        <div style="display: <%= displayShowScan%>">
                            <label id="idLblTitleScan<%=sName%>"></label>
                            <label class="switch" for="idCheckScan<%=sName%>" style="margin-bottom: 0px;clear: both;display: <%=sViewCheckTypeFile%>">
                                <input type="checkbox" name="idCheckScan<%=sName%>" id="idCheckScan<%=sName%>" onclick="onFileTypeScan('idCheckScan'+'<%=sName%>', '<%=sName%>');"
                                    <%= booScanCollect ? "checked" : ""%> <%=sCheckDisabled%>/>
                                <div id="idCheckS<%=sName%>Class" class="slider round <%=sCheckDisabled%>"></div>
                            </label>
                            <script>$("#idLblTitleScan"+'<%=sName%>').text(global_fm_scan_valid);</script>
                        </div>
                    </legend>
                    <%
                        if(booCheckCollect == true) {
                            sCheckDisabled = "disabled";
                        } else {
                            sCheckDisabled = "";
                        }
                        String cssDelete ="";
                        String cssDown ="";
                        String sClassFileLabel = "col-sm-1";
                        String sClassFileUp = "col-sm-11";
                        if("1".equals(sRepresentEnabled)){
                            sClassFileLabel = "col-sm-3";
                            sClassFileUp = "col-sm-9";
                            cssDelete = "text-align: center;width: 80px;";
                            cssDown = "text-align: center;width: 80px;";
                        }
                        if(isEnableUploadFile == true) {
                            if(!"disabled".equals(sCheckDisabled)){
                    %>
                    <div class="col-sm-13" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleUploadManager<%= sNameInID%>" class="control-label <%=sClassFileLabel%>" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="<%=sClassFileUp%>" style="padding-right: 0px;">
                                <input type="file" id="input-file<%=sName%>" style="width: 100%;"
                                    onchange="calUploadFile(this, '<%= sName%>', '<%= ids%>');" class="btn btn-default btn-file select_file" multiple>
                            </div>
                        </div>
                        <div class="form-group" style="display: <%="1".equals(sRepresentEnabled) ? "none" : ""%>">
                            <label class="control-label123" style="color:red;font-weight: 200;" id="idLblTitleNoteManager<%= sNameInID%>"></label>
                        </div>
                        <script>
                            $(document).ready(function () {
                                var sss = '<%=sRepresentEnabled%>';
                                if(sss === "1"){
                                    $("#idLblTitleUploadManager"+'<%= sNameInID%>').text(global_fm_browse_file + ' (' + global_fm_browse_file_upload + '<%= Integer.parseInt(sMaxLengthFile) / 1024 %>' + 'MB)');
                                } else {
                                    $("#idLblTitleUploadManager"+'<%= sNameInID%>').text(global_fm_browse_file);
                                }
                            });
                            $("#idLblTitleNoteManager"+'<%= sNameInID%>').text(global_fm_browse_cert_note + '<%= Integer.parseInt(sMaxLengthFile) / 1024 %>' + 'MB' + '. ' + global_fm_fileattach_support + '<%= sArrayFileExten.replace(";", ",") %>');
                        </script>
                    </div>
<!--                    <div class="form-group" style="padding: 0px;margin: 0;">
                        <label class="control-label123" id="idLblTitleUploadManager<= sNameInID%>"></label>
                        <input type="file" id="input-file<=sName%>" style="width: 100%;"
                            onchange="calUploadFile(this, '<= sName%>', '<= ids%>');" class="btn btn-default btn-file select_file">
                        <div style="height:10px;"></div>
                        <label class="control-label123" style="color:red;font-weight: 200;" id="idLblTitleNoteManager<= sNameInID%>"></label>
                    </div>
                    <script>
                        $("#idLblTitleUploadManager"+'<= sNameInID%>').text(global_fm_browse_file);
                        $("#idLblTitleNoteManager"+'<= sNameInID%>').text(global_fm_browse_cert_note + '<= Integer.parseInt(sMaxLengthFile) / 1024 %>' + 'MB');
                    </script>-->
                    <%
                            }
                        }
                    %>
                    <div style="padding: 0px 0 0px 0;" id="idDiv<%= sName%>" class="table-responsive">
                        <table id="idTable<%= sName%>" class="table table-striped projects" style="margin-bottom: 10px;">
                            <thead>
                                <th id="idLblTitleTableFileName<%=sNameInID%>"></th>
                                <th id="idLblTitleTableSize<%=sNameInID%>"></th>
                                <th <%= "1".equals(signProfileEnabled) ? "" : "style='display:none;'"%> id="idLblTitleTableSign<%=sNameInID%>"></th>
                                <th id="idLblTitleTableAction<%=sNameInID%>"></th>
                                <script>
                                    $("#idLblTitleTableFileName"+'<%=sNameInID%>').text(global_fm_file_name);
                                    $("#idLblTitleTableSize"+'<%=sNameInID%>').text(global_fm_size);
                                    $("#idLblTitleTableSign"+'<%=sNameInID%>').text(global_fm_Status_signed);
                                    $("#idLblTitleTableAction"+'<%=sNameInID%>').text(global_fm_action);
                                </script>
                            </thead>
                            <tbody id="idTBody<%= sName%>">
                                <%
                                    boolean isHasFile = false;
                                    if (cartIP != null) {
                                        int j = 1;
                                        ArrayList<FILE_PROFILE_DATA> ds = cartIP.getGH();
                                        for (FILE_PROFILE_DATA mhIP : ds) {
                                            String sNameInID_inner = sNameInID + String.valueOf(j);
                                            if(mhIP.FILE_PROFILE.equals(sName)) {
                                                isHasFile = true;
                                %>
                                <tr>
                                    <td><%= EscapeUtils.CheckTextNull(mhIP.FILE_NAME)%></td>
                                    <td><%= com.convertMoneyFromDouble(mhIP.FILE_SIZE / 1024)%></td>
                                    <td <%= "1".equals(signProfileEnabled) ? "" : "style='display:none;'"%>><%= mhIP.SIGNED || sName.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT) ? "<span style='color: #0000FF;'>SIGNED</span>" : "<span style='color: #FF0000;'>UNSIGNED</span>"%></td>
                                    <td>
                                        <a id="idLblTitleTableLinkDown<%= sNameInID_inner%>" style="cursor: pointer;<%="0".equals(isViewActionFileEnable) ? "display: none;" : "" %>" class="btn btn-info btn-xs" onclick="DownloadTempFile('<%= mhIP.FILE_MANAGER_ID%>', '<%= anticsrf%>');"></a>
                                        <script>
                                            $(document).ready(function () {
                                                var representEnabled = '<%=sRepresentEnabled%>';
                                                if(representEnabled === "1") {
                                                    $("#idLblTitleTableLinkDown<%= sNameInID_inner%>").append(global_fm_down);
                                                } else {
                                                    $("#idLblTitleTableLinkDown<%= sNameInID_inner%>").append('<i class="fa fa-pencil"></i> ' + global_fm_down);
                                                }
                                            });
                                        </script>
                                        <%
                                            if(isEnableUploadFile == true) {
                                                if(!"disabled".equals(sCheckDisabled)){
                                        %>
                                        <a id="idLblTitleTableLinkDelete<%= sNameInID_inner%>" style="cursor: pointer;<%="0".equals(isViewActionFileEnable) && mhIP.FILE_MANAGER_ID != 0 ? "display: none;" : "" %>" class="btn btn-info btn-xs" onclick="DeleteTempFile('<%= mhIP.FILE_PROFILE%>', '<%= mhIP.FILE_NAME%>', '<%= mhIP.FILE_MANAGER_ID %>');">
                                        </a>
                                        <script>
                                            $(document).ready(function () {
                                                var representEnabled = '<%=sRepresentEnabled%>';
                                                if(representEnabled === "1") {
                                                    $("#idLblTitleTableLinkDelete"+'<%= sNameInID_inner%>').append(global_fm_button_delete);
                                                } else {
                                                    $("#idLblTitleTableLinkDelete"+'<%= sNameInID_inner%>').append('<i class="fa fa-pencil"></i> ' + global_fm_button_delete);
                                                }
                                            });
                                        </script>
                                        <%
                                                }
                                            }
                                        %>
                                        <%
                                            String sFileExtend = CommonFunction.getExtendFile(EscapeUtils.CheckTextNull(mhIP.FILE_NAME)).toUpperCase();
                                            if(sFileExtend.equals("PDF") || sFileExtend.equals("PNG") || sFileExtend.equals("GIF")
                                                || sFileExtend.equals("JPG") || sFileExtend.equals("JPEG")) {
                                        %>
                                        <a id="idLblTitleTableLinkView<%= sNameInID_inner%>" style="cursor: pointer;<%="0".equals(isViewActionFileEnable) ? "display: none;" : "" %>" target="_blank" class="btn btn-info btn-xs" onclick="ViewTempFile('<%= mhIP.FILE_MANAGER_ID%>', '');"></a>
                                        <script>
                                            $(document).ready(function () {
                                                var representEnabled = '<%=sRepresentEnabled%>';
                                                if(representEnabled === "1") {
                                                    $("#idLblTitleTableLinkView"+'<%= sNameInID_inner%>').append(global_fm_view);
                                                } else {
                                                    $("#idLblTitleTableLinkView"+'<%= sNameInID_inner%>').append('<i class="fa fa-pencil"></i> ' + global_fm_view);
                                                }
                                            });
                                            function ViewTempFile(vFILE_MANAGER_ID, vFILE_NAME) {
//                                                window.open('../Certificate/CertificateFileView.jsp?idFile='+vFILE_MANAGER_ID, '_blank');
                                                window.open('ProfileFileView.jsp?idFile='+vFILE_MANAGER_ID + '&idName=' + vFILE_NAME, '_blank');
                                            }
                                        </script>
                                        <%
                                            }
                                        %>
                                        <%
                                            if(mhIP.SIGNED == false && SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT) && mhIP.FILE_MANAGER_ID != 0 && "1".equals(signProfileEnabled) && !sName.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT)) {
                                        %>
                                        <a id="idLblTitleTableLinkSign<%= sNameInID_inner%>" style="cursor: pointer;<%="0".equals(isViewActionFileEnable) ? "display: none;" : "" %>" target="_blank" class="btn btn-info btn-xs" onclick="SignTempFile('<%= mhIP.FILE_MANAGER_ID%>', '<%= mhIP.FILE_PROFILE%>', '<%= EscapeUtils.escapeHtml(ids)%>', '<%= sOWNER_ID%>');"></a>
                                        <script>
                                            $(document).ready(function () {
                                                var representEnabled = '<%=sRepresentEnabled%>';
                                                if(representEnabled === "1") {
                                                    $("#idLblTitleTableLinkSign"+'<%= sNameInID_inner%>').append(global_fm_sign);
                                                } else {
                                                    $("#idLblTitleTableLinkSign"+'<%= sNameInID_inner%>').append('<i class="fa fa-pencil"></i> ' + global_fm_sign);
                                                }
                                            });
                                        </script>
                                        <%
                                            }
                                        %>
                                    </td>
                                </tr>
                                <%
                                            j++;
                                        }
                                    }
                                    if(isHasFile == false)
                                    {
                                %>
                                <tr><td colspan="4" id="idLblTitleTableNoFile<%= sNameInID%>"></td></tr>
                                <script>
                                    $("#idLblTitleTableNoFile"+'<%= sNameInID%>').text(global_no_file_list);
                                </script>
                                <%
                                        }
                                    } else {
                                %>
                                <tr><td colspan="4" id="idLblTitleTableNoFile<%= sNameInID%>"></td></tr>
                                <script>
                                    $("#idLblTitleTableNoFile"+'<%= sNameInID%>').text(global_no_file_list);
                                </script>
                                <%
                                    }
                                %>
                            </tbody>
                         </table>
                    </div>
                </fieldset>
                <script>
                    function onFileTypeCheck(vIdChecked, vName)
                    {
                        var isCheck = "0";
                        if ($("#"+vIdChecked).is(':checked'))
                        {
                            isCheck = "1";
                        }
                        $.ajax({
                            type: "post",
                            url: "../ProfileCommon",
                            data: {
                                idParam: 'checkenoughtypeprofile',
                                sChecked: isCheck,
                                sName: vName
                            },
                            cache: false,
                            success: function (html)
                            {
                                var myStrings = sSpace(html).split('#');
                                if (myStrings[0] === "0")
                                {
                                    
                                }
                                else if (myStrings[0] === JS_EX_CSRF)
                                {
                                    funCsrfAlert();
                                } else if (myStrings[0] === JS_EX_LOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_login);
                                }
                                else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_another_login);
                                }
                                else
                                {
                                    funErrorAlert(global_errorsql);
                                }
                                $(".loading-gif").hide();
                                $('#over').remove();
                            }
                        });
                        return false;
                    }
                    
                    function onFileTypeScan(vIdChecked, vName)
                    {
                        var isCheck = "0";
                        if ($("#"+vIdChecked).is(':checked'))
                        {
                            isCheck = "1";
                        }
                        $.ajax({
                            type: "post",
                            url: "../ProfileCommon",
                            data: {
                                idParam: 'checkscantypeprofile',
                                sChecked: isCheck,
                                sName: vName
                            },
                            cache: false,
                            success: function (html)
                            {
                                var myStrings = sSpace(html).split('#');
                                if (myStrings[0] === "0")
                                {
                                    
                                }
                                else if (myStrings[0] === JS_EX_CSRF)
                                {
                                    funCsrfAlert();
                                } else if (myStrings[0] === JS_EX_LOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_login);
                                }
                                else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_another_login);
                                }
                                else
                                {
                                    funErrorAlert(global_errorsql);
                                }
                                $(".loading-gif").hide();
                                $('#over').remove();
                            }
                        });
                        return false;
                    }
                </script>
                <%
                                    }
                                jFile++;
                                }
                            } catch(Exception e){CommonFunction.LogExceptionServlet(null, "FILE_JSON: " + e.getMessage(), e);}
                        }
                    }
                %>
                </div>
                <script>
                    function DownloadTempFile(vFILE_MANAGER_ID, idCSRF)
                    {
                        $('body').append('<div id="over"></div>');
                        $(".loading-gif").show();
                        $.ajax({
                            type: "post",
                            url: "../DownloadFileCSR",
                            data: {
                                idParam: 'downloadfilemanager',
                                pFILE_MANAGER_ID: vFILE_MANAGER_ID,
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html)
                            {
                                var myStrings = sSpace(html).split('#');
                                if (myStrings[0] === "0")
                                {
                                    $(".loading-gif").hide();
                                    $('#over').remove();
                                    var f = document.myname;
                                    f.method = "post";
                                    f.action = '../DowloadFile?filename=' + myStrings[1];
                                    f.submit();
                                }
                                else if (myStrings[0] === JS_EX_CSRF)
                                {
                                    funCsrfAlert();
                                }
                                else if (myStrings[0] === JS_EX_NO_DATA_WRITE)
                                {
                                    funErrorAlert(global_no_data);
                                }
                                else if (myStrings[0] === JS_EX_LOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_login);
                                }
                                else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_another_login);
                                }
                                else
                                {
                                    funErrorAlert(global_errorsql);
                                }
                                $(".loading-gif").hide();
                                $('#over').remove();
                            }
                        });
                        return false;
                    }
                    function calUploadFile(input1, idType, idCert)
                    {
                        if (input1.value !== '')
                        {
                            swal({
                                title: "",
                                text: file_conform_upload,
                                imageUrl: "../Images/icon_warning.png",
                                imageSize: "45x45",
                                showCancelButton: true,
                                closeOnConfirm: true,
                                allowOutsideClick: false,
                                confirmButtonText: login_fm_buton_OK,
                                cancelButtonText: global_button_grid_cancel,
                                cancelButtonColor: "#199DC0"
                            },
                            function () {
                                $('body').append('<div id="over"></div>');
                                $(".loading-gif").show();
                                setTimeout(function () {
                                    var data1 = new FormData();
                                    $.each($('#input-file' + idType)[0].files, function(k, value)
                                    {
                                        data1.append(k, value);
                                    });
                                    data1.append('pFILE_PROFILE', idType);
                                    data1.append('pCERTIFICATION_ID', idCert);
                                    $.ajax({
                                        type: 'POST',
                                        url: '../FileManageProfileUpload',
                                        data: data1,
                                        cache: false,
                                        contentType: false,
                                        processData: false,
                                        enctype: "multipart/form-data",
                                        success: function (html) {
                                            if (html.length > 0)
                                            {
                                                var obj = JSON.parse(html);
                                                if (obj[0].Code === "0")
                                                {
                                                    input1.value = '';
                                                    $("#idTBody" + idType).empty();
                                                    var iconDelete = '<i class="fa fa-pencil"></i> ' + global_fm_button_delete;
                                                    var cssDelete = '';
                                                    var iconReview = '<i class="fa fa-pencil"></i> ' + global_fm_view;
                                                    var iconDown = '<i class="fa fa-pencil"></i> ' + global_fm_down;
                                                    var iconSign = '<i class="fa fa-pencil"></i> ' + global_fm_sign;
                                                    var cssReview = '';
                                                    var representEnabled = '<%=sRepresentEnabled%>';
                                                    var signFileEnabled = '<%=signProfileEnabled%>';
                                                    var signCertID = '<%=EscapeUtils.escapeHtml(ids)%>';
                                                    var sessAgentID = '<%=SessAgentID%>';
                                                    var signOwnnerID = '<%=sOWNER_ID%>';
                                                    var viewActionFileEnable = '<%=isViewActionFileEnable%>';
                                                    var sActionFileEnable = "";
                                                    if(viewActionFileEnable === "0"){
                                                        sActionFileEnable = "display: none";
                                                    }
                                                    var hiddenSignColumn = "";
                                                    if(signFileEnabled !== "1") {
                                                        hiddenSignColumn = "style='display:none;'";
                                                    }
                                                    if(representEnabled === "1") {
                                                        iconDelete = ' ' + global_fm_button_delete;
                                                        cssDelete = 'text-align: center;width: 80px;';
                                                        iconReview = ' ' + global_fm_view;
                                                        cssReview = 'text-align: center;width: 80px;';
                                                        iconDown = ' ' + global_fm_down;
                                                        iconSign = ' ' + global_fm_sign;
                                                    }
                                                    var content = "";
                                                    for (var i = 0; i < obj.length; i++) {
                                                        if(obj[i].FILE_PROFILE === idType) {
                                                            var fileNameLoad = obj[i].FILE_NAME;
                                                            var sActionDelete = '<a style="cursor: pointer;'+cssDelete+'" class="btn btn-info\n\
                                                                btn-xs" onclick="DeleteTempFile(\'' + obj[i].FILE_PROFILE + '\', \'' + fileNameLoad + '\', \'' + obj[i].FILE_MANAGER_ID + '\');">' + iconDelete + '</a>';
                                                            var sReviewCRL = "";
                                                            var fileNameExt = fileNameLoad.substring(fileNameLoad.lastIndexOf('.')+1);
                                                            if(fileNameExt.toUpperCase() === "PDF" || fileNameExt.toUpperCase() === "GIF"  || fileNameExt.toUpperCase() === "JPEG"
                                                                || fileNameExt.toUpperCase() === "JPG" || fileNameExt.toUpperCase() === "PNG"){
                                                                sReviewCRL = '<a style="cursor: pointer;'+cssReview+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                    btn-xs" onclick="ViewTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + fileNameLoad + '\');">' + iconReview + '</a>';
                                                            }
                                                            var sSignCRL = "";
                                                            if(obj[i].FILE_MANAGER_ID !== 0 && obj[i].SIGNED === 'OFF' && signFileEnabled === "1" && sessAgentID === JS_STR_AGENT_ROOT) {
                                                                sSignCRL = ' <a style="cursor: pointer;'+cssReview+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                    btn-xs" onclick="SignTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + idType + '\', \'' + signCertID + '\', \'' + signOwnnerID + '\');">' + iconSign + '</a>';
                                                            }
                                                            if(obj[i].FILE_PROFILE === JS_STR_FILE_PROFILE_CODE_E_CONTRACT) {
                                                                sActionDelete = "";
                                                                sSignCRL = "";
                                                            }
                                                            var sActionDown = '<a style="cursor: pointer;'+cssReview+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                btn-xs" onclick="DownloadTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + <%= anticsrf%> + '\');">' + iconDown + '</a>';
                                                            var strSigned = "<span style='color: #FF0000;'>UNSIGNED</span>";
                                                            if(obj[i].SIGNED === 'ON' || obj[i].FILE_PROFILE === JS_STR_FILE_PROFILE_CODE_E_CONTRACT){
                                                                strSigned = "<span style='color: #0000FF;'>SIGNED</span>";
                                                            }
                                                            if(obj[i].FILE_MANAGER_ID !== 0 && viewActionFileEnable === "0") {
                                                                sActionDelete = "";
                                                                sActionDown = "";
                                                                sReviewCRL = "";
                                                            }
                                                            content += "<tr>" +
                                                                "<td>" + fileNameLoad + "</td>" +
                                                                "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                "<td "+hiddenSignColumn+">" + strSigned + "</td>" +
                                                                "<td>" + sActionDown  + ' ' + sActionDelete + ' ' + sReviewCRL + sSignCRL +"</td>" +
                                                                "</tr>";
                                                       }
                                                    }
                                                    $("#idTBody" + idType).append(content);
                                                    $("#idDiv" + idType).css("display", "");
                                                }
                                                else if (obj[0].Code === JS_EX_CSRF)
                                                {
                                                    funCsrfAlert();
                                                }
                                                else if (obj[0].Code === JS_EX_SPECIAL)
                                                {
                                                    funErrorAlert(global_error_file_special);
                                                }
                                                else if (obj[0].Code === JS_EX_FILE_EXTENTION) {
                                                    funErrorAlert(global_req_format_url + '<%= sArrayFileExten.replace(";", ",")%>');
                                                }
                                                else if (obj[0].Code === JS_EX_FILE_SIZE) {
                                                    funErrorAlert(global_fm_browse_cert_note + '<%= Integer.parseInt(sMaxLengthFile) / 1024 %>' + ' MB');
                                                }
                                                else if (obj[0].Code === JS_EX_LOGIN)
                                                {
                                                    RedirectPageLoginNoSess(global_alert_login);
                                                }
                                                else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                                {
                                                    RedirectPageLoginNoSess(global_alert_another_login);
                                                }
                                                else {
                                                    funErrorAlert(global_errorsql);
                                                }
                                            }
                                            $(".loading-gif").hide();
                                            $('#over').remove();
                                        }
                                    });
                                    return false;
                                }, JS_STR_ACTION_TIMEOUT);
                            });
                        } else
                        {
                            funErrorAlert(global_req_file);
                        }
                    }
                    function DeleteTempFile(idType, vFILE_NAME, pFILE_MANAGER_ID)
                    {
                        swal({
                            title: "",
                            text: file_conform_delete,
                            imageUrl: "../Images/icon_warning.png",
                            imageSize: "45x45",
                            showCancelButton: true,
                            closeOnConfirm: true,
                            allowOutsideClick: false,
                            confirmButtonText: login_fm_buton_OK,
                            cancelButtonText: global_button_grid_cancel,
                            cancelButtonColor: "#199DC0"
                        },
                        function () {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            setTimeout(function () {
                                $.ajax({
                                    type: 'POST',
                                    url: '../FileManageCommon',
                                    data: {
                                        idParam: 'deletetempfilecertprofile',
                                        idType: idType,
                                        vFILE_NAME: vFILE_NAME,
                                        pFILE_MANAGER_ID: pFILE_MANAGER_ID
                                    },
                                    cache: false,
                                    success: function (html) {
                                        if (html.length > 0)
                                        {
                                            var obj = JSON.parse(html);
                                            if (obj[0].Code === "0")
                                            {
                                                $("#idTBody" + idType).empty();
                                                var content = "";
                                                var iconDelete = '<i class="fa fa-pencil"></i> ' + global_fm_button_delete;
                                                var iconReview = '<i class="fa fa-pencil"></i> ' + global_fm_view;
                                                var iconDown = '<i class="fa fa-pencil"></i> ' + global_fm_down;
                                                var iconSign = '<i class="fa fa-pencil"></i> ' + global_fm_sign;
                                                var cssReview = '';
                                                var cssDelete = '';
                                                var representEnabled = '<%=sRepresentEnabled%>';
                                                var signFileEnabled = '<%=signProfileEnabled%>';
                                                var signCertID = '<%=EscapeUtils.escapeHtml(ids)%>';
                                                var sessAgentID = '<%=SessAgentID%>';
                                                var signOwnnerID = '<%=sOWNER_ID%>';
                                                var viewActionFileEnable = '<%=isViewActionFileEnable%>';
                                                var sActionFileEnable = "";
                                                if(viewActionFileEnable === "0"){
                                                    sActionFileEnable = "display: none";
                                                }
                                                var hiddenSignColumn = "";
                                                if(signFileEnabled !== "1") {
                                                    hiddenSignColumn = "style='display:none;'";
                                                }
                                                if(representEnabled === "1"){
                                                    iconDelete = ' ' + global_fm_button_delete;
                                                    cssDelete = 'text-align: center;width: 80px;';
                                                    iconDown = ' ' + global_fm_down;
                                                    iconReview = ' ' + global_fm_view;
                                                    iconSign = ' ' + global_fm_sign;
                                                    cssReview = 'text-align: center;width: 80px;';
                                                }
                                                for (var i = 0; i < obj.length; i++) {
                                                    if(obj[i].FILE_PROFILE === idType)
                                                    {
                                                        var fileNameLoad = obj[i].FILE_NAME;
                                                        var sActionCRL = '<a style="cursor: pointer;'+cssDelete+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                            btn-xs" onclick="DeleteTempFile(\'' + obj[i].FILE_PROFILE + '\', \'' + fileNameLoad + '\', \'' + obj[i].FILE_MANAGER_ID + '\');">' + iconDelete + '</a>';
                                                        var sReviewCRL = "";
                                                        var fileNameExt = fileNameLoad.substring(fileNameLoad.lastIndexOf('.')+1);
                                                        if(fileNameExt.toUpperCase() === "PDF" || fileNameExt.toUpperCase() === "GIF" || fileNameExt.toUpperCase() === "JPEG"
                                                                    || fileNameExt.toUpperCase() === "JPG" || fileNameExt.toUpperCase() === "PNG"){
                                                            sReviewCRL = '<a style="cursor: pointer;'+cssReview+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                btn-xs" onclick="ViewTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + fileNameLoad + '\');">' + iconReview + '</a>';
                                                        }
                                                        var sSignCRL = "";
                                                        if(obj[i].FILE_MANAGER_ID !== 0 && obj[i].SIGNED === 'OFF' && signFileEnabled === "1" && sessAgentID === JS_STR_AGENT_ROOT) {
                                                            sSignCRL = ' <a style="cursor: pointer;'+cssReview+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                btn-xs" onclick="SignTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + idType + '\', \'' + signCertID + '\', \'' + signOwnnerID + '\');">' + iconSign + '</a>';
                                                        }
                                                        if(obj[i].FILE_PROFILE === JS_STR_FILE_PROFILE_CODE_E_CONTRACT) {
                                                            sActionCRL = "";
                                                            sSignCRL = "";
                                                        }
                                                        var sActionDown = '<a style="cursor: pointer;'+cssDelete+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                btn-xs" onclick="DownloadTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + <%= anticsrf%> + '\');">' + iconDown + '</a>';
                                                        var strSigned = "<span style='color: #FF0000;'>UNSIGNED</span>";
                                                        if(obj[i].SIGNED === 'ON' || obj[i].FILE_PROFILE === JS_STR_FILE_PROFILE_CODE_E_CONTRACT){
                                                            strSigned = "<span style='color: #0000FF;'>SIGNED</span>";
                                                        }
                                                        if(obj[i].FILE_MANAGER_ID !== 0 && viewActionFileEnable === "0") {
                                                            sActionCRL = "";
                                                            sActionDown = "";
                                                            sReviewCRL = "";
                                                        }
                                                        content += "<tr>" +
//                                                            "<td>" + obj[i].Index + "</td>" +
                                                            "<td>" + fileNameLoad + "</td>" +
                                                            "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                            "<td "+hiddenSignColumn+">" + strSigned + "</td>" +
                                                            "<td>" + sActionDown + ' ' + sActionCRL + ' ' + sReviewCRL + sSignCRL +"</td>" +
                                                            "</tr>";
                                                   }
                                                }
                                                $("#idTBody" + idType).append(content);
                                                $("#idDiv" + idType).css("display", "");
                                                funSuccNoLoad(file_succ_delete);
                                            } else if(obj[0].Code === "1")
                                            {
                                                $("#idTBody" + idType).empty();
                                                $("#idDiv" + idType).css("display", "none");
                                                funSuccNoLoad(file_succ_delete);
                                            }
                                            else {
                                                funErrorAlert(global_errorsql);
                                            }
                                        }
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                    }
                                });
                                return false;
                            }, JS_STR_ACTION_TIMEOUT);
                        });
                    }
                    function SignTempFile(pFILE_MANAGER_ID, idType, idCertID, idOwnerID)
                    {
                        swal({
                            title: "",
                            text: file_conform_signprofile,
                            imageUrl: "../Images/icon_warning.png",
                            imageSize: "45x45",
                            showCancelButton: true,
                            closeOnConfirm: true,
                            allowOutsideClick: false,
                            confirmButtonText: login_fm_buton_OK,
                            cancelButtonText: global_button_grid_cancel,
                            cancelButtonColor: "#199DC0"
                        },
                        function () {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            setTimeout(function () {
                                $.ajax({
                                    type: 'POST',
                                    url: '../FileManageCommon',
                                    data: {
                                        idParam: 'signfilecertprofile',
                                        idType: idType,
                                        idCertID: idCertID,
                                        idOwnerID: idOwnerID,
                                        pFILE_MANAGER_ID: pFILE_MANAGER_ID
                                    },
                                    cache: false,
                                    success: function (html) {
                                        if (html.length > 0)
                                        {
                                            var obj = JSON.parse(html);
                                            if (obj[0].Code === "0")
                                            {
                                                $("#idTBody" + idType).empty();
                                                var content = "";
                                                var iconDelete = '<i class="fa fa-pencil"></i> ' + global_fm_button_delete;
                                                var iconReview = '<i class="fa fa-pencil"></i> ' + global_fm_view;
                                                var iconDown = '<i class="fa fa-pencil"></i> ' + global_fm_down;
                                                var iconSign = '<i class="fa fa-pencil"></i> ' + global_fm_sign;
                                                var cssReview = '';
                                                var cssDelete = '';
                                                var representEnabled = '<%=sRepresentEnabled%>';
                                                var signFileEnabled = '<%=signProfileEnabled%>';
                                                var sessAgentID = '<%=SessAgentID%>';
                                                var signCertID = '<%=EscapeUtils.escapeHtml(ids)%>';
                                                var signOwnnerID = '<%=sOWNER_ID%>';
                                                var viewActionFileEnable = '<%=isViewActionFileEnable%>';
                                                var sActionFileEnable = "";
                                                if(viewActionFileEnable === "0"){
                                                    sActionFileEnable = "display: none";
                                                }
                                                var hiddenSignColumn = "";
                                                if(signFileEnabled !== "1") {
                                                    hiddenSignColumn = "style='display:none;'";
                                                }
                                                if(representEnabled === "1"){
                                                    iconDelete = ' ' + global_fm_button_delete;
                                                    cssDelete = 'text-align: center;width: 80px;';
                                                    iconDown = ' ' + global_fm_down;
                                                    iconReview = ' ' + global_fm_view;
                                                    iconSign = ' ' + global_fm_sign;
                                                    cssReview = 'text-align: center;width: 80px;';
                                                }
                                                for (var i = 0; i < obj.length; i++) {
                                                    if(obj[i].FILE_PROFILE === idType) {
                                                        var fileNameLoad = obj[i].FILE_NAME;
                                                        var sActionCRL = '<a style="cursor: pointer;'+cssDelete+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                            btn-xs" onclick="DeleteTempFile(\'' + obj[i].FILE_PROFILE + '\', \'' + fileNameLoad + '\', \'' + obj[i].FILE_MANAGER_ID + '\');">' + iconDelete + '</a>';
                                                        var sReviewCRL = "";
                                                        var fileNameExt = fileNameLoad.substring(fileNameLoad.lastIndexOf('.')+1);
                                                        if(fileNameExt.toUpperCase() === "PDF" || fileNameExt.toUpperCase() === "GIF" || fileNameExt.toUpperCase() === "JPEG"
                                                            || fileNameExt.toUpperCase() === "JPG" || fileNameExt.toUpperCase() === "PNG") {
                                                            sReviewCRL = '<a style="cursor: pointer;'+cssReview+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                btn-xs" onclick="ViewTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + fileNameLoad + '\');">' + iconReview + '</a>';
                                                        }
                                                        var sSignCRL = "";
                                                        if(obj[i].FILE_MANAGER_ID !== 0 && obj[i].SIGNED === 'OFF' && signFileEnabled === "1" && sessAgentID === JS_STR_AGENT_ROOT) {
                                                            sSignCRL = ' <a style="cursor: pointer;'+cssReview+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                btn-xs" onclick="SignTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + idType + '\', \'' + signCertID + '\', \'' + signOwnnerID + '\');">' + iconSign + '</a>';
                                                        }
                                                        if(obj[i].FILE_PROFILE === JS_STR_FILE_PROFILE_CODE_E_CONTRACT) {
                                                            sActionCRL = "";
                                                            sSignCRL = "";
                                                        }
                                                        var sActionDown = '<a style="cursor: pointer;'+cssDelete+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                btn-xs" onclick="DownloadTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + <%= anticsrf%> + '\');">' + iconDown + '</a>';
                                                        var strSigned = "<span style='color: #FF0000;'>UNSIGNED</span>";
                                                        if(obj[i].SIGNED === 'ON' || obj[i].FILE_PROFILE === JS_STR_FILE_PROFILE_CODE_E_CONTRACT){
                                                            strSigned = "<span style='color: #0000FF;'>SIGNED</span>";
                                                        }
                                                        content += "<tr>" +
                                                            "<td>" + fileNameLoad + "</td>" +
                                                            "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                            "<td "+hiddenSignColumn+">" + strSigned + "</td>" +
                                                            "<td>" + sActionDown + ' ' + sActionCRL + ' ' + sReviewCRL + sSignCRL +"</td>" +
                                                            "</tr>";
                                                   }
                                                }
                                                $("#idTBody" + idType).append(content);
                                                $("#idDiv" + idType).css("display", "");
                                                funSuccNoLoad(fm_succ_signprofile);
                                            }
                                            else {
                                                funErrorAlert(global_errorsql);
                                            }
                                        }
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                    }
                                });
                                return false;
                            }, JS_STR_ACTION_TIMEOUT);
                        });
                    }
                </script>
                <%
                    }
                %>
                <%
                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_CMC) && !"".equals(pINFO_BRIEF)) {
                        try {
                            oMapperParse = new ObjectMapper();
                            ProfileActionInfoJson profileAction = oMapperParse.readValue(pINFO_BRIEF, ProfileActionInfoJson.class);
                            if(profileAction != null) {
                                String sControlTime = CommonFunction.dateConvertString(profileAction.controlTime);
                                String sControlUser = EscapeUtils.CheckTextNull(profileAction.controlUser);
                %>
                <div class="col-sm-6" style="padding-left: 0; clear: both;">
                    <div class="form-group">
                        <label id="idLblTitleControlUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="idReceivedControlUser" class="form-control123"
                                value="<%= sControlUser %>" readonly />
                        </div>
                    </div>
                    <script>$("#idLblTitleControlUser").text(collation_fm_user);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleControlTime" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="idReceivedControlTime" class="form-control123"
                                value="<%= sControlTime %>" readonly />
                        </div>
                    </div>
                    <script>$("#idLblTitleControlTime").text(collation_fm_time);</script>
                </div>
                <%
                            }
                        } catch(Exception e){CommonFunction.LogExceptionServlet(null, "ProfileActionInfoJson: " + e.getMessage(), e);}
                    }
                %>
            </form>
        </div>
        <%
                    }
                }
            }
        } catch (Exception e) {
            checkExFinnaly = 1;
            CommonFunction.LogExceptionJSP(request.getRequestURI(), e.getMessage(), e);
        } finally {
            if (checkExFinnaly == 1) {
                checkExFinnaly = 0;
        %>
        <script type="text/javascript">
            window.onload = function () {
                RedirectPageError();
            }();
        </script>
        <%
                }
            }
        %>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../js/moment.min.js"></script>
        <script src="../js/daterangepicker.js"></script>
        <%
        } else {
        %>
        <script type="text/javascript">
            window.onload = function () {
                RedirectPageLoginNoSess(global_alert_login);
            }();
        </script>
        <%
            }
        %>
    </body>
</html>