<%-- 
    Document   : CertificateApprove
    Created on : Jun 27, 2018, 1:37:52 PM
    Author     : THANH-PC
--%>

<%@page import="vn.ra.process.CommonReferServlet"%>
<%@page import="vn.ra.process.SessionDNSName"%>
<%@page import="vn.ra.utility.PropertiesContent"%>
<%@page import="java.util.ArrayList"%>
<%@page import="vn.ra.process.SessionUploadFileCert"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<!DOCTYPE html>
<%
    String sRepresentEnabled = LoadParamSystem.getParamStart(Definitions.CONFIG_REGISTER_REPRESENT_FORM_ENABLED);
    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
%>
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
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js?t=<%=sNewRefreshJS%>"></script>
        <link rel="stylesheet" type="text/css" media="all" href="../js/daterangepicker.css" />
        <!--<link rel="stylesheet" type="text/css" media="all" href="../js/daterangepicker.css" />-->
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            function reload_js(src) {
                $('script[src="' + src + '"]').remove();
                $('<script>').attr('src', src).appendTo('head');
            }
            $(document).ready(function () {
                localStorage.setItem("loadChangeKeyOption", "0");
                reload_js("../style/bootstrap.min.js");
            });
            $(document).ready(function () {
                $('.loading-gif').hide();
                $("#idLblTitleEdits").text(cert_title_edit);
                $("#idLblTitleCertAttrType").text(global_fm_requesttype);
                $("#idLblTitleCertAttrState").text(global_fm_Status + " " + cert_fm_request);
                $("#idLblTitlePhoneContact").text(global_fm_phone_contact);
                $("#idLblTitleEmailContact").text(global_fm_email_contact);
                $("#idLblTitleCertCA").text(global_fm_ca);
                $("#idLblTitleCertDuration").text(global_fm_duration_cts);
                $("#idLblTitleFeeAmount").text(global_fm_amount_fee);
                $('#myModalCertDecline').modal({
                    backdrop: 'static',
                    keyboard: true,
                    show: false
                });
            });
            var checkForSpecialChar = function(string) {
                if(string !== null && string !== "") {
                    for(i = 0; i < specialChars.length;i++){
                      if(string.indexOf(specialChars[i]) > -1){
                          return true;
                       }
                    }
                    return false;
                }
            };
            function FormReIssueSoft(idATTR, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../ReqApproveDeclineCommon",
                    data: {
                        idParam: 're-issuecertsoft',
                        sID: idATTR,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            funSuccAlert(cert_succ_reissue, "CertificateList.jsp");
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        }
                        else if (arr[0] === JS_EX_LOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (arr[0] === JS_EX_ANOTHERLOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else if (arr[0] === JS_EX_WRONG_AGENCY) {
                            RedirectPageLoginNoSess(global_error_wrong_agency);
                        }
                        else if (arr[0] === JS_EX_STATUS)
                        {
                            funErrorAlert(global_error_appove_status);
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
            function FormReRevoke(idATTR, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../ReqApproveDeclineCommon",
                    data: {
                        idParam: 're-revokecert',
                        sID: idATTR,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            funSuccAlert(regisapprove_succ_approve, "CertificateList.jsp");
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        }
                        else if (arr[0] === JS_EX_LOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (arr[0] === JS_EX_ANOTHERLOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else if (arr[0] === JS_EX_WRONG_AGENCY) {
                            RedirectPageLoginNoSess(global_error_wrong_agency);
                        }
                        else if (arr[0] === JS_EX_NO_APPROVE_AGENCY)
                        {
                            funErrorAlert(global_error_appove_status);
                        }
                        else if (arr[0] === "1")
                        {
                            funErrorAlert(arr[1]);
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
            function FormReSuspend(idATTR, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../ReqApproveDeclineCommon",
                    data: {
                        idParam: 're-suspendcert',
                        sID: idATTR,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            funSuccAlert(regisapprove_succ_approve, "CertificateList.jsp");
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        }
                        else if (arr[0] === JS_EX_LOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (arr[0] === JS_EX_ANOTHERLOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else if (arr[0] === JS_EX_WRONG_AGENCY) {
                            RedirectPageLoginNoSess(global_error_wrong_agency);
                        }
                        else if (arr[0] === JS_EX_NO_APPROVE_AGENCY)
                        {
                            funErrorAlert(global_error_appove_status);
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
            function FormReRecovery(idATTR, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../ReqApproveDeclineCommon",
                    data: {
                        idParam: 're-recoverycert',
                        sID: idATTR,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            funSuccAlert(regisapprove_succ_approve, "CertificateList.jsp");
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        }
                        else if (arr[0] === JS_EX_LOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (arr[0] === JS_EX_ANOTHERLOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else if (arr[0] === JS_EX_WRONG_AGENCY) {
                            RedirectPageLoginNoSess(global_error_wrong_agency);
                        }
                        else if (arr[0] === JS_EX_NO_APPROVE_AGENCY)
                        {
                            funErrorAlert(global_error_appove_status);
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
            function ApproveFormCA(idATTR, idCSRF)
            {
                var vEditAmount = "1";
                var vSTR_COMPONENT_DN_VALUE_COMMONNAME = "";
                var vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = "";
                var vSTR_COMPONENT_DN_VALUE_DOMAIN_NAME = "";
                var vSTR_COMPONENT_DN_VALUE_MST = "";
                var vSTR_COMPONENT_DN_VALUE_QD = "";
                var vSTR_COMPONENT_DN_VALUE_MNS = "";
                var vSTR_COMPONENT_DN_VALUE_CMND = "";
                var vSTR_COMPONENT_DN_VALUE_HC = "";
                var vSTR_COMPONENT_DN_VALUE_CCCD = "";
                var vSTR_COMPONENT_DN_VALUE_DEVICE = "";
                var vSTR_COMPONENT_DN_VALUE_PROVINCE_ID = "";
                var vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC = "";
                var vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT = "";
                var vSTR_COMPONENT_DN_VALUE_EMAIL_SAN = "";
                var vSTR_COMPONENT_DN_ID_EMAIL_SAN = "";
                var vSTR_COMPONENT_DN_VALUE_PERSONAL_ID = "";
                var vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID = "";
                if (!JSCheckEmptyField($("#PHONE_CONTRACT").val()))
                {
                    funErrorAlert(policy_req_empty + global_fm_phone);
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
                    funErrorAlert(policy_req_empty + global_fm_email);
                    return false;
                } else {
                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#EMAIL_CONTRACT").val()))
                    {
                        $("#EMAIL_CONTRACT").focus();
                        funErrorAlert(global_req_mail_format);
                        return false;
                    }
                }
                if($("#checkRevokeReason").val() === JS_STR_AGENT_ROOT){
                    if (!JSCheckEmptyField($("#RevokeReason").val()))
                    {
                        $("#RevokeReason").focus();
                        funErrorAlert(policy_req_empty + global_fm_revoke_desc);
                        return false;
                    }
                }
                if (!JSCheckEmptyField($("#CERTIFICATION_PURPOSE").val()))
                {
                    $("#CERTIFICATION_PURPOSE").focus();
                    funErrorAlert(policy_req_empty_choose + global_fm_certpurpose);
                    return false;
                }
                if (!JSCheckEmptyField($("#CERTIFICATION_DURATION").val()))
                {
                    $("#CERTIFICATION_DURATION").focus();
                    funErrorAlert(policy_req_empty_choose + global_fm_duration_cts);
                    return false;
                }
                var vDNResult = "";
                var vSTR_COMPONENT_SAN = "";
                var sChoiseCert = $("#CERTIFICATION_AUTHORITY").val();
                if (sChoiseCert !== "")
                {
                    if(sSpace(localStorage.getItem("localStoreUID_Info")) !== "")
                    {
                        var sListInputCheckID_Info = localStorage.getItem("localStoreUID_Info").split(',');
                        for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                            var idLocalStoreUID_Info = sSpace(sListInputCheckID_Info[i].split('###')[0]);
                            if(idLocalStoreUID_Info === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID)
                            {
                                var idCombobox = document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID);
                                if (!JSCheckEmptyField($("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val()))
                                {
                                    $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).focus();
                                    funErrorAlert(policy_req_empty + idCombobox.options[idCombobox.selectedIndex].text);
                                    return false;
                                }
                                if(checkForSpecialChar($("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val())) {
                                    $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).focus();
                                    funErrorAlert(idCombobox.options[idCombobox.selectedIndex].text + global_req_no_special + " (" + specialChars + ")");
                                    return false;
                                }
                            }
                            if(idLocalStoreUID_Info === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID)
                            {
                                var idCombobox = document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID);
                                if (!JSCheckEmptyField($("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val()))
                                {
                                    $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).focus();
                                    funErrorAlert(policy_req_empty + idCombobox.options[idCombobox.selectedIndex].text);
                                    return false;
                                }
                                if(checkForSpecialChar($("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val())) {
                                    $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).focus();
                                    funErrorAlert(idCombobox.options[idCombobox.selectedIndex].text + global_req_no_special + " (" + specialChars + ")");
                                    return false;
                                }
                            }
                        }
                    }
                    var sListInputCheckSpecial = localStorage.getItem("localStoreInputPersonal").split(',');
                    for (var i = 0; i < sListInputCheckSpecial.length; i++) {
                        var idCheckEmptySpecial = sListInputCheckSpecial[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                        if(checkForSpecialChar($("#" + idCheckEmptySpecial).val())) {
                            $("#" + sListInputCheckSpecial[i].split('###')[0]).focus();
                            funErrorAlert(sListInputCheckSpecial[i].split('###')[4] + global_req_no_special + " (" + specialChars + ")");
                            return false;
                        }
                    }
                    var sListRequire = localStorage.getItem("localStoreRequiredPersonal").split(',');
                    for (var i = 0; i < sListRequire.length; i++) {
                        var idCheckEmpty = sListRequire[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                        if (!JSCheckEmptyField($("#" + idCheckEmpty).val()))
                        {
                            $("#" + sListRequire[i].split('###')[0]).focus();
                            funErrorAlert(policy_req_empty + sListRequire[i].split('###')[1]);
                            return false;
                        }
                        if(sListRequire[i].split('###')[0].indexOf(JS_STR_COMPONENT_DN_VALUE_EMAIL) !== -1)
                        {
                            if (JSCheckEmptyField($("#" + sListRequire[i].split('###')[0]).val()))
                            {
                                if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#" + sListRequire[i].split('###')[0]).val()))
                                {
                                    $("#" + sListRequire[i].split('###')[0]).focus();
                                    funErrorAlert(global_req_mail_format);
                                    return false;
                                }
                            }
                        }
                        if(sListRequire[i].split('###')[0].indexOf(JS_STR_COMPONENT_DN_VALUE_PHONE) !== -1)
                        {
                            if (JSCheckEmptyField($("#" + sListRequire[i].split('###')[0]).val()))
                            {
                                if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#" + sListRequire[i].split('###')[0])))
                                {
                                    $("#" + sListRequire[i].split('###')[0]).focus();
                                    funErrorAlert(global_req_phone_format);
                                    return false;
                                }
                            }
                        }
                    }
                    var sListInputOutRequired = localStorage.getItem("localStoreInputPersonal").split(',');
                    for (var i = 0; i < sListInputOutRequired.length; i++) {
                        var idCheckOutRequired = sListInputOutRequired[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                        if(idCheckOutRequired.indexOf(JS_STR_COMPONENT_DN_VALUE_EMAIL) !== -1)
                        {
                            vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT = $("#" + idCheckOutRequired).val();
                            if (JSCheckEmptyField($("#" + idCheckOutRequired).val()))
                            {
                                if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#" + idCheckOutRequired).val()))
                                {
                                    $("#" + idCheckOutRequired).focus();
                                    funErrorAlert(global_req_mail_format);
                                    return false;
                                }
                            }
                        }
                        if(idCheckOutRequired.indexOf(JS_STR_COMPONENT_DN_VALUE_PHONE) !== -1)
                        {
                            if (JSCheckEmptyField($("#" + idCheckOutRequired).val()))
                            {
                                if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#" + idCheckOutRequired)))
                                {
                                    $("#" + idCheckOutRequired).focus();
                                    funErrorAlert(global_req_phone_format);
                                    return false;
                                }
                            }
                        }
                    }
                    if(localStorage.getItem("localStoreSanInputPersonal") !== "") {
                        var sListSanInputOutRequired = localStorage.getItem("localStoreSanInputPersonal").split(',');
                        for (var i = 0; i < sListSanInputOutRequired.length; i++) {
                            var idCheckOutRequired = sListSanInputOutRequired[i].split('###')[0];
                            if(idCheckOutRequired.indexOf(JS_STR_COMPONENT_DN_VALUE_EMAIL) !== -1)
                            {
                                if (JSCheckEmptyField($("#" + idCheckOutRequired).val()))
                                {
                                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#" + idCheckOutRequired).val()))
                                    {
                                        $("#" + idCheckOutRequired).focus();
                                        funErrorAlert(global_req_mail_format);
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    var sListInput = localStorage.getItem("localStoreInputPersonal").split(',');
                    var Is_Default_CN_TYPE_PERSON = "";
                    for (var i = 0; i < sListInput.length; i++) {
                        var idItemValue = sListInput[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                        var itemValue = $("#" + idItemValue).val();
                        if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                        {
                            if(sListInput[i].split('###')[3] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_PERSON)
                            {
                                vSTR_COMPONENT_DN_VALUE_COMMONNAME = itemValue;
                            } else if(sListInput[i].split('###')[3] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_COMPANY)
                            {
                                vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = itemValue;
                            } else if(sListInput[i].split('###')[3] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_DOMAIN_NAME)
                            {
                                vSTR_COMPONENT_DN_VALUE_DOMAIN_NAME = itemValue;
                            } else {}
                            Is_Default_CN_TYPE_PERSON = sListInput[i].split('###')[3];
                        }
                        if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                        {
                            if(Is_Default_CN_TYPE_PERSON === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_PERSON)
                            {
                                vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = itemValue;
                            }
                        }
                        if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE)
                        {
                            vSTR_COMPONENT_DN_VALUE_PROVINCE_ID = itemValue;
                            itemValue = $("#" + idItemValue + " option:selected").text();
                            vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC = $("#" + idItemValue + " option:selected").text();
                        }
                        if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_LOCALITY ||
                            sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_COUNTRY ||
                            sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_TITLE ||
                            sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME ||
                            sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_ORGANUNIT ||
                            sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                        {
                            if (itemValue.indexOf(",") !== -1) {
                                itemValue = itemValue.replace(/,/g , '\\,');
                            }
                            if (itemValue.indexOf("+") !== -1) {
                                itemValue = itemValue.replace('+' , '\\+');
                            }
                        }
                        if(itemValue !== "") {
                            vDNResult += sListInput[i].split('###')[1] + "=" + sListInput[i].split('###')[2] +
                                escapeEntities(sSpace(itemValue)) + ", ";
                        }
                    }
                    var vUID_Info = sSpace(localStorage.getItem("localStoreUID_Info"));
//                    console.log("vUID_Info " + vUID_Info);
                    if(vUID_Info !== "")
                    {
                        var sListInputCheckID_Info = vUID_Info.split(',');
//                        console.log("sListInputCheckID_Info.length: " + sListInputCheckID_Info.length);
//                        var sListInputCheckID_Info = localStorage.getItem("localStoreUID_Info").split(',');
                        for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                            var idLocalStoreUID_Info = sSpace(sListInputCheckID_Info[i].split('###')[0]);
//                            console.log("idLocalStoreUID_Info " + idLocalStoreUID_Info);
                            if(idLocalStoreUID_Info === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID)
                            {
//                                console.log("i: " + i + "; " + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID + "; " + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID);
                                var sValueMST_MNS = $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val();
                                var sSelectedMST_MNS = sSpace(document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).value);
                                if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                {
                                    vSTR_COMPONENT_DN_VALUE_MST = sValueMST_MNS;
                                } else if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                                {
                                    vSTR_COMPONENT_DN_VALUE_MNS = sValueMST_MNS;
                                } else if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_QD)
                                {
                                    vSTR_COMPONENT_DN_VALUE_QD = sValueMST_MNS;
                                } else {
                                    vSTR_COMPONENT_DN_VALUE_DEVICE = sSelectedMST_MNS + sValueMST_MNS;
                                }
//                                console.log("sSelectedMST_MNS: " + sSelectedMST_MNS);
//                                console.log("sValueMST_MNS " + sValueMST_MNS);
                                vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID = getUIDCertEnterprise(sSelectedMST_MNS, sSpace(sValueMST_MNS));
                                vDNResult = vDNResult + sSpace(sListInputCheckID_Info[i].split('###')[2]) + "=" + sSpace(sSelectedMST_MNS) + sSpace(sValueMST_MNS) + ", ";
                            }
                            if(idLocalStoreUID_Info === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID)
                            {
                                var sValueCMND_HC = $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val();
                                var sSelectedCMND_HC = sSpace(document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).value);
                                if(sSelectedCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND)
                                {
                                    vSTR_COMPONENT_DN_VALUE_CMND = sValueCMND_HC;
                                }
                                if(sSelectedCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                                {
                                    vSTR_COMPONENT_DN_VALUE_HC = sValueCMND_HC;
                                }
                                if(sSelectedCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_CCCD)
                                {
                                    vSTR_COMPONENT_DN_VALUE_CCCD = sValueCMND_HC;
                                }
//                                console.log("sSelectedCMND_HC :" + sSelectedCMND_HC);
//                                console.log("sValueCMND_HC :" + sValueCMND_HC);
                                vSTR_COMPONENT_DN_VALUE_PERSONAL_ID = getUIDCertPersonal(sSelectedCMND_HC, sSpace(sValueCMND_HC));
                                vDNResult = vDNResult + sSpace(sListInputCheckID_Info[i].split('###')[2]) + "=" + sSpace(sSelectedCMND_HC) + sSpace(sValueCMND_HC) + ", ";
                            }
                        }
                    }
                    if(localStorage.getItem("localStoreSanInputPersonal") !== "") {
                        var sListSanInput = localStorage.getItem("localStoreSanInputPersonal").split(',');
                        for (var i = 0; i < sListSanInput.length; i++) {
                            var itemValue = $("#" + sListSanInput[i].split('###')[0]).val();
                            if(sSpace(itemValue) !== "") {
                                vSTR_COMPONENT_SAN = vSTR_COMPONENT_SAN + sListSanInput[i].split('###')[1] + "###" + itemValue + "@@@";
                            }
                            if(sSpace(sListSanInput[i].split('###')[1]) === JS_STR_COMPONENT_DN_VALUE_E_SAN) {
                                vSTR_COMPONENT_DN_ID_EMAIL_SAN = sListSanInput[i].split('###')[0];
                                vSTR_COMPONENT_DN_VALUE_EMAIL_SAN = sSpace(itemValue);
                            }
                        }
                    }
                    var intSub = vDNResult.lastIndexOf(',');
                    vDNResult = vDNResult.substring(0, intSub);
                }
                if(vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT !== "" && vSTR_COMPONENT_DN_VALUE_EMAIL_SAN !== "" && vSTR_COMPONENT_DN_ID_EMAIL_SAN !== "")
                {
                    if(vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT !== vSTR_COMPONENT_DN_VALUE_EMAIL_SAN)
                    {
                        $("#"+vSTR_COMPONENT_DN_ID_EMAIL_SAN).focus();
                        funErrorAlert(global_req_email_subject_san);
                        return false;
                    }
                }
                if(vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT !== "" && vSTR_COMPONENT_DN_VALUE_EMAIL_SAN === "" && vSTR_COMPONENT_DN_ID_EMAIL_SAN !== "")
                {
                    $("#"+vSTR_COMPONENT_DN_ID_EMAIL_SAN).focus();
                    funErrorAlert(global_req_email_subject_san);
                    return false;
                }
                if($("#sCERTIFICATION_ATTR_TYPE_ID").val() === JS_STR_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                    || $("#sCERTIFICATION_ATTR_TYPE_ID").val() === JS_STR_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                    || $("#sCERTIFICATION_ATTR_TYPE_ID").val() === JS_STR_CERTIFICATION_ATTR_TYPE_ID_RENEWAL)
                {
                    if (!JSCheckEmptyField($("#FEE_AMOUNT").val()))
                    {
                        $("#FEE_AMOUNT").focus();
                        funErrorAlert(policy_req_empty + global_fm_amount_fee);
                        return false;
                    } else {
                        if (!JSCheckNumericField($("#FEE_AMOUNT").val().replace(/,/g, ''))) {
                            $("#FEE_AMOUNT").focus();
                            funErrorAlert(global_fm_amount_fee + policy_req_number);
                            return false;
                        } else {
                            var HIDDEN_FEE_AMOUNT = $("#HIDDEN_FEE_AMOUNT").val().replace(/,/g, '');
                            var FEE_AMOUNT = $("#FEE_AMOUNT").val().replace(/,/g, '');
                            if(parseInt(FEE_AMOUNT) > parseInt(HIDDEN_FEE_AMOUNT))
                            {
                                funErrorAlert(global_error_amount_package_limit);
                                return false;
                            }
                        }
                    }
                    if (!JSCheckEmptyField($("#DURATION_FREE").val()))
                    {
                        $("#DURATION_FREE").focus();
                        funErrorAlert(policy_req_empty + global_fm_date_free);
                        return false;
                    } else {
                        if (!JSCheckNumericField($("#DURATION_FREE").val().replace(/,/g, ''))) {
                            $("#DURATION_FREE").focus();
                            funErrorAlert(global_fm_amount_fee + policy_req_number);
                            return false;
                        } else {
                            var HIDDEN_DURATION = $("#HIDDEN_DURATION").val().replace(/,/g, '');
                            var DURATION_FREE = $("#DURATION_FREE").val().replace(/,/g, '');
                            if(parseInt(DURATION_FREE) > parseInt(HIDDEN_DURATION))
                            {
                                funErrorAlert(global_error_promotion_package_limit);
                                return false;
                            }
                        }
                    }
                }
                var isRepresenseEnabled = '<%=sRepresentEnabled%>';
                if(isRepresenseEnabled === "1"){
                    if($("#sCERTIFICATION_ATTR_TYPE_ID").val() === JS_STR_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                        || $("#sCERTIFICATION_ATTR_TYPE_ID").val() === JS_STR_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE)
                    {
                        if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
                            if (!JSCheckEmptyField($("#registerFullname").val())) {
                                $("#registerFullname").focus();
                                funErrorAlert(policy_req_empty + global_fm_fullname);
                                return false;
                            }
                            if (!JSCheckEmptyField($("#registerIssuedAdress").val())) {
                                $("#registerIssuedAdress").focus();
                                funErrorAlert(policy_req_empty + token_fm_address_residence);
                                return false;
                            }
                        } else {
                            if (!JSCheckEmptyField($("#registerFullname").val()))
                            {
                                $("#registerFullname").focus();
                                funErrorAlert(policy_req_empty + global_fm_fullname);
                                return false;
                            }
                            if (!JSCheckEmptyField($("#registerRole").val()))
                            {
                                $("#registerRole").focus();
                                funErrorAlert(policy_req_empty + global_fm_role);
                                return false;
                            }
                        }
                        if (!JSCheckEmptyField($("#registerCMND").val()))
                        {
                            $("#registerCMND").focus();
                            funErrorAlert(policy_req_empty + global_fm_CitizenId_I + "/ "+global_fm_CMND + "/ "+global_fm_HC);
                            return false;
                        }
//                        if (!JSCheckEmptyField($("#registerIssuedDate").val()))
//                        {
//                            $("#registerIssuedDate").focus();
//                            funErrorAlert(policy_req_empty + global_fm_cmnd_date);
//                            return false;
//                        }
                    }
                }
                
                var sCheckPUSH_NOTICE = "0";
                if ($("#PUSH_NOTICE_ENABLED").is(':checked'))
                {
                    sCheckPUSH_NOTICE = "1";
                }
                var sCheckDeleteRevoke = "0";
                if ($("#FO_DELETE_CERT_WHEN_REVOKE").is(':checked'))
                {
                    sCheckDeleteRevoke = "1";
                }
                var sCheckCHANGE_KEY_ENABLED = "0";
                if ($("#idCheckChangeKey").is(':checked'))
                {
                    sCheckCHANGE_KEY_ENABLED = "1";
                }
                var sCheckPRIVATE_KEY_ENABLED = "0";
                if ($("#idCheckBackUpKey").is(':checked'))
                {
                    sCheckPRIVATE_KEY_ENABLED = "1";
                }
                var sCheckREVOKE_ENABLED = "0";
                if ($("#idCheckRevoked").is(':checked'))
                {
                    sCheckREVOKE_ENABLED = "1";
                }
                var sCheckKeepCertSN = "0";
                if ($("#idCheckKeepCertSN").is(':checked'))
                {
                    sCheckKeepCertSN = "1";
                }
                var sCheckReceivedSoftCopy = "0";
                if ($("#idReceivedSoftCopy").is(':checked')) {
                    sCheckReceivedSoftCopy = "1";
                }
                var sCheckCertConfirm = "0";
                if ($("#idCheckCertConfirm").is(':checked')) {
                    sCheckCertConfirm = "1";
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../ReqApproveDeclineCommon",
                    data: {
                        idParam: 'approvecertca',
                        sID: idATTR,
                        CertProfileID: $("#idHiddenCerDurationOrProfileID").val(),
                        vEditAmount: vEditAmount,
                        CheckPUSH_NOTICE: sCheckPUSH_NOTICE,
                        CheckCHANGE_KEY: sCheckCHANGE_KEY_ENABLED,
                        CheckPRIVATE_KEY: sCheckPRIVATE_KEY_ENABLED,
                        CheckREVOKE_ENABLED: sCheckREVOKE_ENABLED,
                        CheckDeleteRevoke: sCheckDeleteRevoke,
                        keepCertSNEnabled: sCheckKeepCertSN,
                        sFEE_AMOUNT: $("#FEE_AMOUNT").val(),
                        CSR: $("#idCSR").val(),
                        pPERSONAL_NAME: vSTR_COMPONENT_DN_VALUE_COMMONNAME,
                        pCOMPANY_NAME: vSTR_COMPONENT_DN_VALUE_COMPANY_NAME,
                        pDOMAIN_NAME: vSTR_COMPONENT_DN_VALUE_DOMAIN_NAME,
                        pTAX_CODE: vSTR_COMPONENT_DN_VALUE_MST,
                        pDECISION: vSTR_COMPONENT_DN_VALUE_QD,
                        pBUDGET_CODE: vSTR_COMPONENT_DN_VALUE_MNS,
                        pP_ID: vSTR_COMPONENT_DN_VALUE_CMND,
                        pCCCD: vSTR_COMPONENT_DN_VALUE_CCCD,
                        pPASSPORT: vSTR_COMPONENT_DN_VALUE_HC,
                        pPROVINCE_DESC: vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC,
                        pPROVINCE_ID: vSTR_COMPONENT_DN_VALUE_PROVINCE_ID,
                        pDEVICE: vSTR_COMPONENT_DN_VALUE_DEVICE,
                        pENTERPRISE_ID: vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID,
                        pPERSONAL_ID: vSTR_COMPONENT_DN_VALUE_PERSONAL_ID,
                        BRANCH_ID: $("#AGENT_NAME").val(),
                        CREATE_USER: $("#USER").val(),
                        DN: vDNResult,
                        COMPONENT_SAN: vSTR_COMPONENT_SAN,
                        REVOKE_REASON: $("#RevokeReason").val(),
                        SUSPEND_REASON: $("#SuspendReason").val(),
                        SUSPEND_TIME: $("#SuspendTime").val(),
                        CERT_REVOCATION_REASON: $("#CERT_REVOCATION_REASON").val(),
                        pDISCOUNT_RATE: $("#FEE_PERCENT").val(),
                        sCheckReceivedSoftCopy: sCheckReceivedSoftCopy,
                        idReceivedNote: $("#idReceivedNote").val(),
                        STATE_PROFILE: $("#STATE_PROFILE").val(),
                        sDURATION_FREE: $("#DURATION_FREE").val(),
                        PHONE_CONTRACT: $("#PHONE_CONTRACT").val(),
                        EMAIL_CONTRACT: $("#EMAIL_CONTRACT").val(),
                        checkCertConfirm: sCheckCertConfirm,
                        registerAddressGPKD: $("#registerAddressGPKD").val(),
                        registerFullname: $("#registerFullname").val(),
                        registerRole: $("#registerRole").val(),
                        registerCMND: $("#registerCMND").val(),
                        registerIssuedDate: $("#registerIssuedDate").val(),
                        registerIssuedPlace: $("#registerIssuedPlace").val(),
                        registerIssuedAdress: $("#registerIssuedAdress").val(),
                        registerNCRepresentative: $("#registerNCRepresentative").val(),
                        registerNCAddress: $("#registerNCAddress").val(),
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            funSuccAlert(cert_succ_approve, "CertificateList.jsp");
                        }
                        else if (arr[0] === JS_EX_CSR_NULL) {
                            $("#input-file-csr").focus();
                            funErrorAlert(global_req_file);
                        }
                        else if (arr[0] === JS_EX_CSR_KEYSIZE) {
                            $("#input-file-csr").focus();
                            funErrorAlert(global_error_keysize_csr);
                        }
                        else if (arr[0] === "1") {
                            funErrorAlert(cert_error_approve);
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        }
                        else if (arr[0] === JS_EX_LOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (arr[0] === JS_EX_ANOTHERLOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else if (arr[0] === JS_EX_WRONG_AGENCY) {
                            RedirectPageLoginNoSess(global_error_wrong_agency);
                        }
                        else if (arr[0] === JS_EX_STATUS || arr[0] === JS_EX_NO_APPROVE_AGENCY)
                        {
                            funErrorAlert(global_error_appove_status);
                        }
                        else if (arr[0] === JS_EX_INVALID_EXTERNAL_SYSTEM)
                        {
                            funErrorAlert(global_error_credential_external_invalid);
                        }
                        else if (arr[0] === JS_EX_ERROR_DB)
                        {
                            funErrorAlert(arr[1]);
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
            function ApproveFormAgency(idATTR, idCSRF)
            {
                var vSTR_COMPONENT_DN_VALUE_COMMONNAME = "";
                var vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = "";
                var vSTR_COMPONENT_DN_VALUE_DOMAIN_NAME = "";
                var vSTR_COMPONENT_DN_VALUE_MST = "";
                var vSTR_COMPONENT_DN_VALUE_MNS = "";
                var vSTR_COMPONENT_DN_VALUE_QD = "";
                var vSTR_COMPONENT_DN_VALUE_CMND = "";
                var vSTR_COMPONENT_DN_VALUE_HC = "";
                var vSTR_COMPONENT_DN_VALUE_CCCD = "";
                var vSTR_COMPONENT_DN_VALUE_DEVICE = "";
                var vSTR_COMPONENT_DN_VALUE_PROVINCE_ID = "";
                var vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC = "";
                var vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT = "";
                var vSTR_COMPONENT_DN_VALUE_EMAIL_SAN = "";
                var vSTR_COMPONENT_DN_ID_EMAIL_SAN = "";
                var vSTR_COMPONENT_DN_VALUE_PERSONAL_ID = "";
                var vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID = "";
                if (!JSCheckEmptyField($("#PHONE_CONTRACT").val()))
                {
                    funErrorAlert(policy_req_empty + global_fm_phone);
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
                    funErrorAlert(policy_req_empty + global_fm_email);
                    return false;
                } else {
                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#EMAIL_CONTRACT").val()))
                    {
                        $("#EMAIL_CONTRACT").focus();
                        funErrorAlert(global_req_mail_format);
                        return false;
                    }
                }
                
                if (!JSCheckEmptyField($("#CERTIFICATION_PURPOSE").val()))
                {
                    $("#CERTIFICATION_PURPOSE").focus();
                    funErrorAlert(policy_req_empty_choose + global_fm_certpurpose);
                    return false;
                }
                if (!JSCheckEmptyField($("#CERTIFICATION_DURATION").val()))
                {
                    $("#CERTIFICATION_DURATION").focus();
                    funErrorAlert(policy_req_empty_choose + global_fm_duration_cts);
                    return false;
                }
                var vDNResult = "";
                var vSTR_COMPONENT_SAN = "";
                var sChoiseCert = $("#CERTIFICATION_AUTHORITY").val();
                if (sChoiseCert !== "")
                {
                    if(sSpace(localStorage.getItem("localStoreUID_Info")) !== "")
                    {
                        var sListInputCheckID_Info = localStorage.getItem("localStoreUID_Info").split(',');
                        for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                            var idLocalStoreUID_Info = sSpace(sListInputCheckID_Info[i].split('###')[0]);
                            if(idLocalStoreUID_Info === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID)
                            {
                                var idCombobox = document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID);
                                if (!JSCheckEmptyField($("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val()))
                                {
                                    $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).focus();
                                    funErrorAlert(policy_req_empty + idCombobox.options[idCombobox.selectedIndex].text);
                                    return false;
                                }
                                if(checkForSpecialChar($("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val())) {
                                    $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).focus();
                                    funErrorAlert(idCombobox.options[idCombobox.selectedIndex].text + global_req_no_special + " (" + specialChars + ")");
                                    return false;
                                }
                            }
                            if(idLocalStoreUID_Info === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID)
                            {
                                var idCombobox = document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID);
                                if (!JSCheckEmptyField($("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val()))
                                {
                                    $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).focus();
                                    funErrorAlert(policy_req_empty + idCombobox.options[idCombobox.selectedIndex].text);
                                    return false;
                                }
                                if(checkForSpecialChar($("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val())) {
                                    $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).focus();
                                    funErrorAlert(idCombobox.options[idCombobox.selectedIndex].text + global_req_no_special + " (" + specialChars + ")");
                                    return false;
                                }
                            }
                        }
                    }
                    var sListInputCheckSpecial = localStorage.getItem("localStoreInputPersonal").split(',');
                    for (var i = 0; i < sListInputCheckSpecial.length; i++) {
                        var idCheckEmptySpecial = sListInputCheckSpecial[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                        if(checkForSpecialChar($("#" + idCheckEmptySpecial).val())) {
                            $("#" + sListInputCheckSpecial[i].split('###')[0]).focus();
                            funErrorAlert(sListInputCheckSpecial[i].split('###')[4] + global_req_no_special + " (" + specialChars + ")");
                            return false;
                        }
                    }
                    var sListRequire = localStorage.getItem("localStoreRequiredPersonal").split(',');
                    for (var i = 0; i < sListRequire.length; i++) {
                        var idCheckEmpty = sListRequire[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                        if (!JSCheckEmptyField($("#" + idCheckEmpty).val()))
                        {
                            $("#" + sListRequire[i].split('###')[0]).focus();
                            funErrorAlert(policy_req_empty + sListRequire[i].split('###')[1]);
                            return false;
                        }
                        if(sListRequire[i].split('###')[0].indexOf(JS_STR_COMPONENT_DN_VALUE_EMAIL) !== -1)
                        {
                            if (JSCheckEmptyField($("#" + sListRequire[i].split('###')[0]).val()))
                            {
                                if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#" + sListRequire[i].split('###')[0]).val()))
                                {
                                    $("#" + sListRequire[i].split('###')[0]).focus();
                                    funErrorAlert(global_req_mail_format);
                                    return false;
                                }
                            }
                        }
                        if(sListRequire[i].split('###')[0].indexOf(JS_STR_COMPONENT_DN_VALUE_PHONE) !== -1)
                        {
                            if (JSCheckEmptyField($("#" + sListRequire[i].split('###')[0]).val()))
                            {
                                if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#" + sListRequire[i].split('###')[0])))
                                {
                                    $("#" + sListRequire[i].split('###')[0]).focus();
                                    funErrorAlert(global_req_phone_format);
                                    return false;
                                }
                            }
                        }
                    }
                    var sListInputOutRequired = localStorage.getItem("localStoreInputPersonal").split(',');
                    for (var i = 0; i < sListInputOutRequired.length; i++) {
                        var idCheckOutRequired = sListInputOutRequired[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                        if(idCheckOutRequired.indexOf(JS_STR_COMPONENT_DN_VALUE_EMAIL) !== -1)
                        {
                            vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT = $("#" + idCheckOutRequired).val();
                            if (JSCheckEmptyField($("#" + idCheckOutRequired).val()))
                            {
                                if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#" + idCheckOutRequired).val()))
                                {
                                    $("#" + idCheckOutRequired).focus();
                                    funErrorAlert(global_req_mail_format);
                                    return false;
                                }
                            }
                        }
                        if(idCheckOutRequired.indexOf(JS_STR_COMPONENT_DN_VALUE_PHONE) !== -1)
                        {
                            if (JSCheckEmptyField($("#" + idCheckOutRequired).val()))
                            {
                                if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#" + idCheckOutRequired)))
                                {
                                    $("#" + idCheckOutRequired).focus();
                                    funErrorAlert(global_req_phone_format);
                                    return false;
                                }
                            }
                        }
                    }
                    if(localStorage.getItem("localStoreSanInputPersonal") !== "") {
                        var sListSanInputOutRequired = localStorage.getItem("localStoreSanInputPersonal").split(',');
                        for (var i = 0; i < sListSanInputOutRequired.length; i++) {
                            var idCheckOutRequired = sListSanInputOutRequired[i].split('###')[0];
                            if(idCheckOutRequired.indexOf(JS_STR_COMPONENT_DN_VALUE_EMAIL) !== -1)
                            {
                                if (JSCheckEmptyField($("#" + idCheckOutRequired).val()))
                                {
                                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#" + idCheckOutRequired).val()))
                                    {
                                        $("#" + idCheckOutRequired).focus();
                                        funErrorAlert(global_req_mail_format);
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    var sListInput = localStorage.getItem("localStoreInputPersonal").split(',');
                    var Is_Default_CN_TYPE_PERSON = "";
                    for (var i = 0; i < sListInput.length; i++) {
                        var idItemValue = sListInput[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                        var itemValue = $("#" + idItemValue).val();
                        if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                        {
                            if(sListInput[i].split('###')[3] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_PERSON)
                            {
                                vSTR_COMPONENT_DN_VALUE_COMMONNAME = itemValue;
                            } else if(sListInput[i].split('###')[3] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_COMPANY)
                            {
                                vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = itemValue;
                            } else if(sListInput[i].split('###')[3] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_DOMAIN_NAME)
                            {
                                vSTR_COMPONENT_DN_VALUE_DOMAIN_NAME = itemValue;
                            } else {}
                            Is_Default_CN_TYPE_PERSON = sListInput[i].split('###')[3];
                        }
                        if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                        {
                            if(Is_Default_CN_TYPE_PERSON === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_PERSON)
                            {
                                vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = itemValue;
                            }
                        }
                        if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE)
                        {
                            vSTR_COMPONENT_DN_VALUE_PROVINCE_ID = itemValue;
                            itemValue = $("#" + idItemValue + " option:selected").text();
                            vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC = $("#" + idItemValue + " option:selected").text();
                        }
                        if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_LOCALITY ||
                            sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_COUNTRY ||
                            sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_TITLE ||
                            sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME ||
                            sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_ORGANUNIT ||
                            sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                        {
                            if (itemValue.indexOf(",") !== -1) {
                                itemValue = itemValue.replace(/,/g , '\\,');
                            }
                            if (itemValue.indexOf("+") !== -1) {
                                itemValue = itemValue.replace('+' , '\\+');
                            }
                        }
                        if(itemValue !== "") {
                            vDNResult += sListInput[i].split('###')[1] + "=" + sListInput[i].split('###')[2] +
                                escapeEntities(sSpace(itemValue)) + ", ";
                        }
                    }
                    if(sSpace(localStorage.getItem("localStoreUID_Info")) !== "")
                    {
                        var sListInputCheckID_Info = localStorage.getItem("localStoreUID_Info").split(',');
                        for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                            var idLocalStoreUID_Info = sSpace(sListInputCheckID_Info[i].split('###')[0]);
                            if(idLocalStoreUID_Info === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID)
                            {
                                var sValueMST_MNS = $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val();
                                var sSelectedMST_MNS = sSpace(document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).value);
                                if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                {
                                    vSTR_COMPONENT_DN_VALUE_MST = sValueMST_MNS;
                                } else if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                                {
                                    vSTR_COMPONENT_DN_VALUE_MNS = sValueMST_MNS;
                                } else if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_QD)
                                {
                                    vSTR_COMPONENT_DN_VALUE_QD = sValueMST_MNS;
                                } else {
                                    vSTR_COMPONENT_DN_VALUE_DEVICE = sSelectedMST_MNS + sValueMST_MNS;
                                }
                                vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID = getUIDCertEnterprise(sSelectedMST_MNS, sSpace(sValueMST_MNS));
                                vDNResult = vDNResult + sSpace(sListInputCheckID_Info[i].split('###')[2]) + "=" + sSpace(sSelectedMST_MNS) + sSpace(sValueMST_MNS) + ", ";
                            }
                            if(idLocalStoreUID_Info === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID)
                            {
                                var sValueCMND_HC = $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val();
                                var sSelectedCMND_HC = sSpace(document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).value);
                                if(sSelectedCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND)
                                {
                                    vSTR_COMPONENT_DN_VALUE_CMND = sValueCMND_HC;
                                }
                                if(sSelectedCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                                {
                                    vSTR_COMPONENT_DN_VALUE_HC = sValueCMND_HC;
                                }
                                if(sSelectedCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_CCCD)
                                {
                                    vSTR_COMPONENT_DN_VALUE_CCCD = sValueCMND_HC;
                                }
                                vSTR_COMPONENT_DN_VALUE_PERSONAL_ID = getUIDCertPersonal(sSelectedCMND_HC, sSpace(sValueCMND_HC));
                                vDNResult = vDNResult + sSpace(sListInputCheckID_Info[i].split('###')[2]) + "=" + sSpace(sSelectedCMND_HC) + sSpace(sValueCMND_HC) + ", ";
                            }
                        }
                    }
                    if(localStorage.getItem("localStoreSanInputPersonal") !== "") {
                        var sListSanInput = localStorage.getItem("localStoreSanInputPersonal").split(',');
                        for (var i = 0; i < sListSanInput.length; i++) {
                            var itemValue = $("#" + sListSanInput[i].split('###')[0]).val();
                            if(sSpace(itemValue) !== "") {
                                vSTR_COMPONENT_SAN = vSTR_COMPONENT_SAN + sListSanInput[i].split('###')[1] + "###" + itemValue + "@@@";
                            }
                            if(sSpace(sListSanInput[i].split('###')[1]) === JS_STR_COMPONENT_DN_VALUE_E_SAN) {
                                vSTR_COMPONENT_DN_ID_EMAIL_SAN = sListSanInput[i].split('###')[0];
                                vSTR_COMPONENT_DN_VALUE_EMAIL_SAN = sSpace(itemValue);
                            }
                        }
                    }
                    var intSub = vDNResult.lastIndexOf(',');
                    vDNResult = vDNResult.substring(0, intSub);
                }
                if(vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT !== "" && vSTR_COMPONENT_DN_VALUE_EMAIL_SAN !== "" && vSTR_COMPONENT_DN_ID_EMAIL_SAN !== "")
                {
                    if(vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT !== vSTR_COMPONENT_DN_VALUE_EMAIL_SAN)
                    {
                        $("#"+vSTR_COMPONENT_DN_ID_EMAIL_SAN).focus();
                        funErrorAlert(global_req_email_subject_san);
                        return false;
                    }
                }
                if(vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT !== "" && vSTR_COMPONENT_DN_VALUE_EMAIL_SAN === "" && vSTR_COMPONENT_DN_ID_EMAIL_SAN !== "")
                {
                    $("#"+vSTR_COMPONENT_DN_ID_EMAIL_SAN).focus();
                    funErrorAlert(global_req_email_subject_san);
                    return false;
                }
                var isRepresenseEnabled = '<%=sRepresentEnabled%>';
                if(isRepresenseEnabled === "1"){
                    if($("#sCERTIFICATION_ATTR_TYPE_ID").val() === JS_STR_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                        || $("#sCERTIFICATION_ATTR_TYPE_ID").val() === JS_STR_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE)
                    {
                        if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
                            if (!JSCheckEmptyField($("#registerFullname").val())) {
                                $("#registerFullname").focus();
                                funErrorAlert(policy_req_empty + global_fm_fullname);
                                return false;
                            }
                            if (!JSCheckEmptyField($("#registerIssuedAdress").val())) {
                                $("#registerIssuedAdress").focus();
                                funErrorAlert(policy_req_empty + token_fm_address_residence);
                                return false;
                            }
                        } else {
                            if (!JSCheckEmptyField($("#registerFullname").val()))
                            {
                                $("#registerFullname").focus();
                                funErrorAlert(policy_req_empty + global_fm_fullname);
                                return false;
                            }
                            if (!JSCheckEmptyField($("#registerRole").val()))
                            {
                                $("#registerRole").focus();
                                funErrorAlert(policy_req_empty + global_fm_role);
                                return false;
                            }
                        }
                        if (!JSCheckEmptyField($("#registerCMND").val()))
                        {
                            $("#registerCMND").focus();
                            funErrorAlert(policy_req_empty + global_fm_CitizenId_I + "/ "+global_fm_CMND + "/ "+global_fm_HC);
                            return false;
                        }
//                        if (!JSCheckEmptyField($("#registerIssuedDate").val()))
//                        {
//                            $("#registerIssuedDate").focus();
//                            funErrorAlert(policy_req_empty + global_fm_cmnd_date);
//                            return false;
//                        }
                    }
                }
                var sCheckCHANGE_KEY_ENABLED = "0";
                if ($("#idCheckChangeKey").is(':checked')) {
                    sCheckCHANGE_KEY_ENABLED = "1";
                }
                var sCheckPRIVATE_KEY_ENABLED = "0";
                if ($("#idCheckBackUpKey").is(':checked')) {
                    sCheckPRIVATE_KEY_ENABLED = "1";
                }
                var sCheckREVOKE_ENABLED = "0";
                if ($("#idCheckRevoked").is(':checked')) {
                    sCheckREVOKE_ENABLED = "1";
                }
                var sCheckKeepCertSN = "0";
                if ($("#idCheckKeepCertSN").is(':checked')) {
                    sCheckKeepCertSN = "1";
                }
                var sCheckCertConfirm = "0";
                if ($("#idCheckCertConfirm").is(':checked')) {
                    sCheckCertConfirm = "1";
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../ReqApproveDeclineCommon",
                    data: {
                        idParam: 'approvecertagency',
                        sID: idATTR,
                        CertProfileID: $("#idHiddenCerDurationOrProfileID").val(),
                        CACoreSubject: $("#idHiddenCerCoreSubject").val(),
                        CSR: $("#idCSR").val(),
                        keepCertSNEnabled: sCheckKeepCertSN,
                        DN: vDNResult,
                        COMPONENT_SAN: vSTR_COMPONENT_SAN,
                        CheckCHANGE_KEY: sCheckCHANGE_KEY_ENABLED,
                        CheckPRIVATE_KEY: sCheckPRIVATE_KEY_ENABLED,
                        CheckREVOKE_ENABLED: sCheckREVOKE_ENABLED,
                        pPERSONAL_NAME: vSTR_COMPONENT_DN_VALUE_COMMONNAME,
                        pCOMPANY_NAME: vSTR_COMPONENT_DN_VALUE_COMPANY_NAME,
                        pDOMAIN_NAME: vSTR_COMPONENT_DN_VALUE_DOMAIN_NAME,
                        pDEVICE: vSTR_COMPONENT_DN_VALUE_DEVICE,
                        STATE_PROFILE: $("#STATE_PROFILE").val(),
                        pTAX_CODE: vSTR_COMPONENT_DN_VALUE_MST,
                        pDECISION: vSTR_COMPONENT_DN_VALUE_QD,
                        pBUDGET_CODE: vSTR_COMPONENT_DN_VALUE_MNS,
                        pP_ID: vSTR_COMPONENT_DN_VALUE_CMND,
                        pCCCD: vSTR_COMPONENT_DN_VALUE_CCCD,
                        pPASSPORT: vSTR_COMPONENT_DN_VALUE_HC,
                        pENTERPRISE_ID: vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID,
                        pPERSONAL_ID: vSTR_COMPONENT_DN_VALUE_PERSONAL_ID,
                        PHONE_CONTRACT: $("#PHONE_CONTRACT").val(),
                        EMAIL_CONTRACT: $("#EMAIL_CONTRACT").val(),
                        pPROVINCE_ID: vSTR_COMPONENT_DN_VALUE_PROVINCE_ID,
                        pPROVINCE_DESC: vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC,
                        BRANCH_ID: $("#AGENT_NAME").val(),
                        CREATE_USER: $("#USER").val(),
                        REVOKE_REASON: $("#RevokeReason").val(),
                        SUSPEND_REASON: $("#SuspendReason").val(),
                        SUSPEND_TIME: $("#SuspendTime").val(),
                        sFEE_AMOUNT: $("#FEE_AMOUNT").val(),
                        checkCertConfirm: sCheckCertConfirm,
                        registerAddressGPKD: $("#registerAddressGPKD").val(),
                        registerFullname: $("#registerFullname").val(),
                        registerRole: $("#registerRole").val(),
                        registerCMND: $("#registerCMND").val(),
                        registerIssuedDate: $("#registerIssuedDate").val(),
                        registerIssuedPlace: $("#registerIssuedPlace").val(),
                        registerIssuedAdress: $("#registerIssuedAdress").val(),
                        registerNCRepresentative: $("#registerNCRepresentative").val(),
                        registerNCAddress: $("#registerNCAddress").val(),
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            pushNotificationApprove(idATTR, $("#idHiddenCerDurationOrProfileID").val());
                            funSuccAlert(cert_succ_approve, "CertificateList.jsp");
                        }
                        else if (arr[0] === JS_EX_CSR_NULL) {
                            $("#input-file-csr").focus();
                            funErrorAlert(global_req_file);
                        }
                        else if (arr[0] === JS_EX_CSR_KEYSIZE) {
                            $("#input-file-csr").focus();
                            funErrorAlert(global_error_keysize_csr);
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        }
                        else if (arr[0] === JS_EX_LOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (arr[0] === JS_EX_ANOTHERLOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else if (arr[0] === JS_EX_WRONG_AGENCY) {
                            RedirectPageLoginNoSess(global_error_wrong_agency);
                        }
                        else if (arr[0] === JS_EX_DENIED_FUNCTION)
                        {
                            funErrorAlert(global_alert_another_menu);
                        }
                        else if (arr[0] === JS_EX_STATUS)
                        {
                            funErrorAlert(global_error_appove_status);
                        }
                        else if (arr[0] === JS_EX_INVALID_EXTERNAL_SYSTEM)
                        {
                            funErrorAlert(global_error_credential_external_invalid);
                        }
                        else if (arr[0] === JS_EX_ERROR_DB)
                        {
                            funErrorAlert(arr[1]);
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
            function ApproveFormChildrenAgency(idATTR, idCSRF)
            {
                var vSTR_COMPONENT_DN_VALUE_COMMONNAME = "";
                var vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = "";
                var vSTR_COMPONENT_DN_VALUE_DOMAIN_NAME = "";
                var vSTR_COMPONENT_DN_VALUE_MST = "";
                var vSTR_COMPONENT_DN_VALUE_MNS = "";
                var vSTR_COMPONENT_DN_VALUE_QD = "";
                var vSTR_COMPONENT_DN_VALUE_CMND = "";
                var vSTR_COMPONENT_DN_VALUE_HC = "";
                var vSTR_COMPONENT_DN_VALUE_CCCD = "";
                var vSTR_COMPONENT_DN_VALUE_DEVICE = "";
                var vSTR_COMPONENT_DN_VALUE_PROVINCE_ID = "";
                var vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC = "";
                var vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT = "";
                var vSTR_COMPONENT_DN_VALUE_EMAIL_SAN = "";
                var vSTR_COMPONENT_DN_ID_EMAIL_SAN = "";
                var vSTR_COMPONENT_DN_VALUE_PERSONAL_ID = "";
                var vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID = "";
                if (!JSCheckEmptyField($("#PHONE_CONTRACT").val()))
                {
                    funErrorAlert(policy_req_empty + global_fm_phone);
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
                    funErrorAlert(policy_req_empty + global_fm_email);
                    return false;
                } else {
                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#EMAIL_CONTRACT").val()))
                    {
                        $("#EMAIL_CONTRACT").focus();
                        funErrorAlert(global_req_mail_format);
                        return false;
                    }
                }
                if (!JSCheckEmptyField($("#CERTIFICATION_PURPOSE").val()))
                {
                    $("#CERTIFICATION_PURPOSE").focus();
                    funErrorAlert(policy_req_empty_choose + global_fm_certpurpose);
                    return false;
                }
                if (!JSCheckEmptyField($("#CERTIFICATION_DURATION").val()))
                {
                    $("#CERTIFICATION_DURATION").focus();
                    funErrorAlert(policy_req_empty_choose + global_fm_duration_cts);
                    return false;
                }
                var vDNResult = "";
                var vSTR_COMPONENT_SAN = "";
                var sChoiseCert = $("#CERTIFICATION_AUTHORITY").val();
                if (sChoiseCert !== "")
                {
                    if(sSpace(localStorage.getItem("localStoreUID_Info")) !== "")
                    {
                        var sListInputCheckID_Info = localStorage.getItem("localStoreUID_Info").split(',');
                        for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                            var idLocalStoreUID_Info = sSpace(sListInputCheckID_Info[i].split('###')[0]);
                            if(idLocalStoreUID_Info === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID)
                            {
                                var idCombobox = document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID);
                                if (!JSCheckEmptyField($("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val()))
                                {
                                    $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).focus();
                                    funErrorAlert(policy_req_empty + idCombobox.options[idCombobox.selectedIndex].text);
                                    return false;
                                }
                                if(checkForSpecialChar($("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val())) {
                                    $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).focus();
                                    funErrorAlert(idCombobox.options[idCombobox.selectedIndex].text + global_req_no_special + " (" + specialChars + ")");
                                    return false;
                                }
                            }
                            if(idLocalStoreUID_Info === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID)
                            {
                                var idCombobox = document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID);
                                if (!JSCheckEmptyField($("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val()))
                                {
                                    $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).focus();
                                    funErrorAlert(policy_req_empty + idCombobox.options[idCombobox.selectedIndex].text);
                                    return false;
                                }
                                if(checkForSpecialChar($("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val())) {
                                    $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).focus();
                                    funErrorAlert(idCombobox.options[idCombobox.selectedIndex].text + global_req_no_special + " (" + specialChars + ")");
                                    return false;
                                }
                            }
                        }
                    }
                    var sListInputCheckSpecial = localStorage.getItem("localStoreInputPersonal").split(',');
                    for (var i = 0; i < sListInputCheckSpecial.length; i++) {
                        var idCheckEmptySpecial = sListInputCheckSpecial[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                        if(checkForSpecialChar($("#" + idCheckEmptySpecial).val())) {
                            $("#" + sListInputCheckSpecial[i].split('###')[0]).focus();
                            funErrorAlert(sListInputCheckSpecial[i].split('###')[4] + global_req_no_special + " (" + specialChars + ")");
                            return false;
                        }
                    }
                    var sListRequire = localStorage.getItem("localStoreRequiredPersonal").split(',');
                    for (var i = 0; i < sListRequire.length; i++) {
                        var idCheckEmpty = sListRequire[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                        if (!JSCheckEmptyField($("#" + idCheckEmpty).val()))
                        {
                            $("#" + sListRequire[i].split('###')[0]).focus();
                            funErrorAlert(policy_req_empty + sListRequire[i].split('###')[1]);
                            return false;
                        }
                        if(sListRequire[i].split('###')[0].indexOf(JS_STR_COMPONENT_DN_VALUE_EMAIL) !== -1)
                        {
                            if (JSCheckEmptyField($("#" + sListRequire[i].split('###')[0]).val()))
                            {
                                if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#" + sListRequire[i].split('###')[0]).val()))
                                {
                                    $("#" + sListRequire[i].split('###')[0]).focus();
                                    funErrorAlert(global_req_mail_format);
                                    return false;
                                }
                            }
                        }
                        if(sListRequire[i].split('###')[0].indexOf(JS_STR_COMPONENT_DN_VALUE_PHONE) !== -1)
                        {
                            if (JSCheckEmptyField($("#" + sListRequire[i].split('###')[0]).val()))
                            {
                                if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#" + sListRequire[i].split('###')[0])))
                                {
                                    $("#" + sListRequire[i].split('###')[0]).focus();
                                    funErrorAlert(global_req_phone_format);
                                    return false;
                                }
                            }
                        }
                    }
                    var sListInputOutRequired = localStorage.getItem("localStoreInputPersonal").split(',');
                    for (var i = 0; i < sListInputOutRequired.length; i++) {
                        var idCheckOutRequired = sListInputOutRequired[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                        if(idCheckOutRequired.indexOf(JS_STR_COMPONENT_DN_VALUE_EMAIL) !== -1)
                        {
                            vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT = $("#" + idCheckOutRequired).val();
                            if (JSCheckEmptyField($("#" + idCheckOutRequired).val()))
                            {
                                if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#" + idCheckOutRequired).val()))
                                {
                                    $("#" + idCheckOutRequired).focus();
                                    funErrorAlert(global_req_mail_format);
                                    return false;
                                }
                            }
                        }
                        if(idCheckOutRequired.indexOf(JS_STR_COMPONENT_DN_VALUE_PHONE) !== -1)
                        {
                            if (JSCheckEmptyField($("#" + idCheckOutRequired).val()))
                            {
                                if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#" + idCheckOutRequired)))
                                {
                                    $("#" + idCheckOutRequired).focus();
                                    funErrorAlert(global_req_phone_format);
                                    return false;
                                }
                            }
                        }
                    }
                    if(localStorage.getItem("localStoreSanInputPersonal") !== "") {
                        var sListSanInputOutRequired = localStorage.getItem("localStoreSanInputPersonal").split(',');
                        for (var i = 0; i < sListSanInputOutRequired.length; i++) {
                            var idCheckOutRequired = sListSanInputOutRequired[i].split('###')[0];
                            if(idCheckOutRequired.indexOf(JS_STR_COMPONENT_DN_VALUE_EMAIL) !== -1)
                            {
                                if (JSCheckEmptyField($("#" + idCheckOutRequired).val()))
                                {
                                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#" + idCheckOutRequired).val()))
                                    {
                                        $("#" + idCheckOutRequired).focus();
                                        funErrorAlert(global_req_mail_format);
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    var sListInput = localStorage.getItem("localStoreInputPersonal").split(',');
                    var Is_Default_CN_TYPE_PERSON = "";
                    for (var i = 0; i < sListInput.length; i++) {
                        var idItemValue = sListInput[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                        var itemValue = $("#" + idItemValue).val();
                        if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                        {
                            if(sListInput[i].split('###')[3] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_PERSON)
                            {
                                vSTR_COMPONENT_DN_VALUE_COMMONNAME = itemValue;
                            } else if(sListInput[i].split('###')[3] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_COMPANY)
                            {
                                vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = itemValue;
                            } else if(sListInput[i].split('###')[3] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_DOMAIN_NAME)
                            {
                                vSTR_COMPONENT_DN_VALUE_DOMAIN_NAME = itemValue;
                            } else {}
                            Is_Default_CN_TYPE_PERSON = sListInput[i].split('###')[3];
                        }
                        if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                        {
                            if(Is_Default_CN_TYPE_PERSON === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_PERSON)
                            {
                                vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = itemValue;
                            }
                        }
                        if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE)
                        {
                            vSTR_COMPONENT_DN_VALUE_PROVINCE_ID = itemValue;
                            itemValue = $("#" + idItemValue + " option:selected").text();
                            vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC = $("#" + idItemValue + " option:selected").text();
                        }
                        if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_LOCALITY ||
                            sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_COUNTRY ||
                            sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_TITLE ||
                            sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME ||
                            sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_ORGANUNIT ||
                            sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                        {
                            if (itemValue.indexOf(",") !== -1) {
                                itemValue = itemValue.replace(/,/g , '\\,');
                            }
                            if (itemValue.indexOf("+") !== -1) {
                                itemValue = itemValue.replace('+' , '\\+');
                            }
                        }
                        if(itemValue !== "") {
                            vDNResult += sListInput[i].split('###')[1] + "=" + sListInput[i].split('###')[2] +
                                escapeEntities(sSpace(itemValue)) + ", ";
                        }
                    }
                    if(sSpace(localStorage.getItem("localStoreUID_Info")) !== "")
                    {
                        var sListInputCheckID_Info = localStorage.getItem("localStoreUID_Info").split(',');
                        for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                            var idLocalStoreUID_Info = sSpace(sListInputCheckID_Info[i].split('###')[0]);
                            if(idLocalStoreUID_Info === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID)
                            {
                                var sValueMST_MNS = $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val();
                                var sSelectedMST_MNS = sSpace(document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).value);
                                if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                {
                                    vSTR_COMPONENT_DN_VALUE_MST = sValueMST_MNS;
                                } else if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                                {
                                    vSTR_COMPONENT_DN_VALUE_MNS = sValueMST_MNS;
                                } else if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_QD)
                                {
                                    vSTR_COMPONENT_DN_VALUE_QD = sValueMST_MNS;
                                } else {
                                    vSTR_COMPONENT_DN_VALUE_DEVICE = sSelectedMST_MNS + sValueMST_MNS;
                                }
                                vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID = getUIDCertEnterprise(sSelectedMST_MNS, sSpace(sValueMST_MNS));
                                vDNResult = vDNResult + sSpace(sListInputCheckID_Info[i].split('###')[2]) + "=" + sSpace(sSelectedMST_MNS) + sSpace(sValueMST_MNS) + ", ";
                            }
                            if(idLocalStoreUID_Info === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID)
                            {
                                var sValueCMND_HC = $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val();
                                var sSelectedCMND_HC = sSpace(document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).value);
                                if(sSelectedCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND)
                                {
                                    vSTR_COMPONENT_DN_VALUE_CMND = sValueCMND_HC;
                                }
                                if(sSelectedCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                                {
                                    vSTR_COMPONENT_DN_VALUE_HC = sValueCMND_HC;
                                }
                                if(sSelectedCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_CCCD)
                                {
                                    vSTR_COMPONENT_DN_VALUE_CCCD = sValueCMND_HC;
                                }
                                vSTR_COMPONENT_DN_VALUE_PERSONAL_ID = getUIDCertPersonal(sSelectedCMND_HC, sSpace(sValueCMND_HC));
                                vDNResult = vDNResult + sSpace(sListInputCheckID_Info[i].split('###')[2]) + "=" + sSpace(sSelectedCMND_HC) + sSpace(sValueCMND_HC) + ", ";
                            }
                        }
                    }
                    if(localStorage.getItem("localStoreSanInputPersonal") !== "") {
                        var sListSanInput = localStorage.getItem("localStoreSanInputPersonal").split(',');
                        for (var i = 0; i < sListSanInput.length; i++) {
                            var itemValue = $("#" + sListSanInput[i].split('###')[0]).val();
                            if(sSpace(itemValue) !== "") {
                                vSTR_COMPONENT_SAN = vSTR_COMPONENT_SAN + sListSanInput[i].split('###')[1] + "###" + itemValue + "@@@";
                            }
                            if(sSpace(sListSanInput[i].split('###')[1]) === JS_STR_COMPONENT_DN_VALUE_E_SAN) {
                                vSTR_COMPONENT_DN_ID_EMAIL_SAN = sListSanInput[i].split('###')[0];
                                vSTR_COMPONENT_DN_VALUE_EMAIL_SAN = sSpace(itemValue);
                            }
                        }
                    }
                    var intSub = vDNResult.lastIndexOf(',');
                    vDNResult = vDNResult.substring(0, intSub);
                }
                if(vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT !== "" && vSTR_COMPONENT_DN_VALUE_EMAIL_SAN !== "" && vSTR_COMPONENT_DN_ID_EMAIL_SAN !== "")
                {
                    if(vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT !== vSTR_COMPONENT_DN_VALUE_EMAIL_SAN)
                    {
                        $("#"+vSTR_COMPONENT_DN_ID_EMAIL_SAN).focus();
                        funErrorAlert(global_req_email_subject_san);
                        return false;
                    }
                }
                if(vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT !== "" && vSTR_COMPONENT_DN_VALUE_EMAIL_SAN === "" && vSTR_COMPONENT_DN_ID_EMAIL_SAN !== "")
                {
                    $("#"+vSTR_COMPONENT_DN_ID_EMAIL_SAN).focus();
                    funErrorAlert(global_req_email_subject_san);
                    return false;
                }
                var isRepresenseEnabled = '<%=sRepresentEnabled%>';
                if(isRepresenseEnabled === "1"){
                    if($("#sCERTIFICATION_ATTR_TYPE_ID").val() === JS_STR_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                        || $("#sCERTIFICATION_ATTR_TYPE_ID").val() === JS_STR_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE)
                    {
                        if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
                            if (!JSCheckEmptyField($("#registerFullname").val())) {
                                $("#registerFullname").focus();
                                funErrorAlert(policy_req_empty + global_fm_fullname);
                                return false;
                            }
                            if (!JSCheckEmptyField($("#registerIssuedAdress").val())) {
                                $("#registerIssuedAdress").focus();
                                funErrorAlert(policy_req_empty + token_fm_address_residence);
                                return false;
                            }
                        } else {
                            if (!JSCheckEmptyField($("#registerFullname").val()))
                            {
                                $("#registerFullname").focus();
                                funErrorAlert(policy_req_empty + global_fm_fullname);
                                return false;
                            }
                            if (!JSCheckEmptyField($("#registerRole").val()))
                            {
                                $("#registerRole").focus();
                                funErrorAlert(policy_req_empty + global_fm_role);
                                return false;
                            }
                        }
                        if (!JSCheckEmptyField($("#registerCMND").val()))
                        {
                            $("#registerCMND").focus();
                            funErrorAlert(policy_req_empty + global_fm_CitizenId_I + "/ "+global_fm_CMND + "/ "+global_fm_HC);
                            return false;
                        }
//                        if (!JSCheckEmptyField($("#registerIssuedDate").val()))
//                        {
//                            $("#registerIssuedDate").focus();
//                            funErrorAlert(policy_req_empty + global_fm_cmnd_date);
//                            return false;
//                        }
                    }
                }
                
                var sCheckCHANGE_KEY_ENABLED = "0";
                if ($("#idCheckChangeKey").is(':checked'))
                {
                    sCheckCHANGE_KEY_ENABLED = "1";
                }
                var sCheckPRIVATE_KEY_ENABLED = "0";
                if ($("#idCheckBackUpKey").is(':checked'))
                {
                    sCheckPRIVATE_KEY_ENABLED = "1";
                }
                var sCheckREVOKE_ENABLED = "0";
                if ($("#idCheckRevoked").is(':checked'))
                {
                    sCheckREVOKE_ENABLED = "1";
                }
                var sCheckKeepCertSN = "0";
                if ($("#idCheckKeepCertSN").is(':checked'))
                {
                    sCheckKeepCertSN = "1";
                }
                var sCheckCertConfirm = "0";
                if ($("#idCheckCertConfirm").is(':checked')) {
                    sCheckCertConfirm = "1";
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../ReqApproveDeclineCommon",
                    data: {
                        idParam: 'approvecertchildrenagency',
                        sID: idATTR,
                        CertProfileID: $("#idHiddenCerDurationOrProfileID").val(),
                        CACoreSubject: $("#idHiddenCerCoreSubject").val(),
                        CSR: $("#idCSR").val(),
                        keepCertSNEnabled: sCheckKeepCertSN,
                        DN: vDNResult,
                        COMPONENT_SAN: vSTR_COMPONENT_SAN,
                        CheckCHANGE_KEY: sCheckCHANGE_KEY_ENABLED,
                        CheckPRIVATE_KEY: sCheckPRIVATE_KEY_ENABLED,
                        CheckREVOKE_ENABLED: sCheckREVOKE_ENABLED,
                        pPERSONAL_NAME: vSTR_COMPONENT_DN_VALUE_COMMONNAME,
                        pCOMPANY_NAME: vSTR_COMPONENT_DN_VALUE_COMPANY_NAME,
                        pDOMAIN_NAME: vSTR_COMPONENT_DN_VALUE_DOMAIN_NAME,
                        STATE_PROFILE: $("#STATE_PROFILE").val(),
                        pDEVICE: vSTR_COMPONENT_DN_VALUE_DEVICE,
                        pTAX_CODE: vSTR_COMPONENT_DN_VALUE_MST,
                        pDECISION: vSTR_COMPONENT_DN_VALUE_QD,
                        pBUDGET_CODE: vSTR_COMPONENT_DN_VALUE_MNS,
                        pP_ID: vSTR_COMPONENT_DN_VALUE_CMND,
                        pCCCD: vSTR_COMPONENT_DN_VALUE_CCCD,
                        pPASSPORT: vSTR_COMPONENT_DN_VALUE_HC,
                        pENTERPRISE_ID: vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID,
                        pPERSONAL_ID: vSTR_COMPONENT_DN_VALUE_PERSONAL_ID,
                        PHONE_CONTRACT: $("#PHONE_CONTRACT").val(),
                        EMAIL_CONTRACT: $("#EMAIL_CONTRACT").val(),
                        pPROVINCE_ID: vSTR_COMPONENT_DN_VALUE_PROVINCE_ID,
                        pPROVINCE_DESC: vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC,
                        BRANCH_ID: $("#AGENT_NAME").val(),
                        CREATE_USER: $("#USER").val(),
                        REVOKE_REASON: $("#RevokeReason").val(),
                        SUSPEND_REASON: $("#SuspendReason").val(),
                        SUSPEND_TIME: $("#SuspendTime").val(),
                        sFEE_AMOUNT: $("#FEE_AMOUNT").val(),
                        checkCertConfirm: sCheckCertConfirm,
                        registerAddressGPKD: $("#registerAddressGPKD").val(),
                        registerFullname: $("#registerFullname").val(),
                        registerRole: $("#registerRole").val(),
                        registerCMND: $("#registerCMND").val(),
                        registerIssuedDate: $("#registerIssuedDate").val(),
                        registerIssuedPlace: $("#registerIssuedPlace").val(),
                        registerIssuedAdress: $("#registerIssuedAdress").val(),
                        registerNCRepresentative: $("#registerNCRepresentative").val(),
                        registerNCAddress: $("#registerNCAddress").val(),
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            pushNotificationApprove(idATTR, $("#idHiddenCerDurationOrProfileID").val());
                            funSuccAlert(cert_succ_approve, "CertificateList.jsp");
                        }
                        else if (arr[0] === JS_EX_CSR_NULL) {
                            $("#input-file-csr").focus();
                            funErrorAlert(global_req_file);
                        }
                        else if (arr[0] === JS_EX_CSR_KEYSIZE) {
                            $("#input-file-csr").focus();
                            funErrorAlert(global_error_keysize_csr);
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        }
                        else if (arr[0] === JS_EX_LOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (arr[0] === JS_EX_ANOTHERLOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else if (arr[0] === JS_EX_WRONG_AGENCY) {
                            RedirectPageLoginNoSess(global_error_wrong_agency);
                        }
                        else if (arr[0] === JS_EX_DENIED_FUNCTION)
                        {
                            funErrorAlert(global_alert_another_menu);
                        }
                        else if (arr[0] === JS_EX_STATUS)
                        {
                            funErrorAlert(global_error_appove_status);
                        }
                        else if (arr[0] === JS_EX_INVALID_EXTERNAL_SYSTEM)
                        {
                            funErrorAlert(global_error_credential_external_invalid);
                        }
                        else if (arr[0] === JS_EX_ERROR_DB)
                        {
                            funErrorAlert(arr[1]);
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
            function pushNotificationApprove(name, desc) {
                var xmlhttp = new XMLHttpRequest();
                xmlhttp.open("POST", "../PushNotiRequestApprove?t="+new Date(), false);
                xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                xmlhttp.send("name="+name+"&message="+desc);
            }
            function FormDecline(idATTR, idCSRF, idBranch, idUser) {
                swal({
                    title: "",
                    text: request_conform_delete,
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
                            url: "../ReqApproveDeclineCommon",
                            data: {
                                idParam: 'declinecert',
                                idATTR: idATTR,
                                DESC_DECLINE: $("#DESC_DECLINE").val(),
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var arr = sSpace(html).split('#');
                                if (arr[0] === "0")
                                {
                                    if (arr[2] === "1")
                                    {
                                        pushNotificationDecline(idBranch, idUser);
                                    }
                                    funSuccLocalAlert(request_succ_delete);
                                }
                                else if (arr[0] === JS_EX_CSRF) {
                                    funCsrfAlert();
                                }
                                else if (arr[0] === JS_EX_LOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_login);
                                }
                                else if (arr[0] === JS_EX_ANOTHERLOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_another_login);
                                }
                                else if (arr[0] === JS_EX_WRONG_AGENCY) {
                                    RedirectPageLoginNoSess(global_error_wrong_agency);
                                }
                                else if (arr[0] === JS_EX_ERROR_DB) {
                                    funErrorAlert(arr[1]);
                                }
                                else if (arr[0] === JS_EX_INVALID_EXTERNAL_SYSTEM)
                                {
                                    funErrorAlert(global_error_credential_external_invalid);
                                }
                                else if (arr[0] === JS_EX_STATUS)
                                {
                                    funErrorAlert(global_error_appove_status);
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
            }
            function pushNotificationDecline(idBRANCH, user) {
                var xmlhttp = new XMLHttpRequest();
                xmlhttp.open("POST", "../PushNotiRequestDecline?t="+new Date(), false);
                xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                xmlhttp.send("name="+idBRANCH+"&user="+user);
            }
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../UserCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'RefreshApproveReqSess'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "CertificateList.jsp";
                        }
                        else
                        {
                            window.location = "CertificateList.jsp";
                        }
                    }
                });
                return false;
            }
            function popupDialogCertDecline(attrId, branchId, userId)
            {
                $('#myModalCertDecline').modal('show');
                $('#contentCertDecline').empty();
                $('#contentCertDecline').load('IncludeCertDecline.jsp', {attrId: attrId, branchId: branchId, userId: userId}, function () {
                });
                $(".loading-gifCertDecline").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
            function closeDialogCertDecline()
            {
                $('#myModalCertDecline').modal('hide');
                $(".loading-gifCertDecline").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
        </script>
        <style>
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.2em 10px 1.2em !important;
                margin: 0 0 12px 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
            legend.scheduler-border {
                color: <%="1".equals(sRepresentEnabled) ? "#C43131" : ""%>;
                <%="1".equals(sRepresentEnabled) ? "padding:0 10px 0 0;" : ""%>;
            }
            .btn-info{
                <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "background-color:#31B910;border-color:#31B910" : ""%>
            }
            .btn-info:hover,.btn-info:focus,.btn-info:active,.btn-info.active {
                <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "background-color:#31B910;border-color:#31B910" : ""%>
            }
            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
            .table > thead > tr > th{border-bottom: none;}
            .btn{margin-bottom: 0px;}
        </style>
    </head>
    <body>
        <%
            if (session.getAttribute("sUserID") != null) {
                request.getSession(false).setAttribute("SessCollectedBriefPro", null);
                String strCERTIFICATION_PROFILE_ID = "";
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                String SessLevelBranch = session.getAttribute("sessLevelBranch").toString().trim();
                String SessRoleID = session.getAttribute("RoleID_ID").toString().trim();
                String sessUserID = session.getAttribute("UserID").toString().trim();
                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Cert");
                String sessTreeArrayBranchID = session.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                String allowTwoOU = cogCommon.GetPropertybyCode(Definitions.CONFIG_ALLOW_OU_COMPONENT_TWO_TOUP);
                String profileManagerCAOption = LoadParamSystem.getParamStart(Definitions.CONFIG_PROFILE_MANAGER_LEVEL_APPROVE_OFCA);
                session.setAttribute("sessProfileStateDK01", null);
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <%                                        CERTIFICATION[][] rs = new CERTIFICATION[1][];
            session.setAttribute("sessUploadFileCert", null);
            String sMaxLengthFile = cogCommon.GetPropertybyCode(Definitions.CONFIG_JACK_RABBIT_MAX_LENGTH_FILE).trim();
            try {
                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                if (EscapeUtils.IsInteger(ids) == true) {
                    db.S_BO_CERTIFICATION_APPROVED_DETAIL(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                    if (rs[0].length > 0) {
                        String sRevokeReason = rs[0][0].REVOCATION_REASON;
                        String sOWNER_ID = String.valueOf(rs[0][0].CERTIFICATION_OWNER_ID);
                        String sRequestAgentID = String.valueOf(rs[0][0].BRANCH_ID);
                        boolean isAccessAgencyPage = true;
                        if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            BRANCH[][] branchAccess = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                            isAccessAgencyPage = CommonFunction.checkBranchTreeInvalidCert(rs[0][0].BRANCH_ID, branchAccess);
                        }
                        if (isAccessAgencyPage == true) {
                            int intCERTIFICATION_ATTR_TYPE_ID = rs[0][0].CERTIFICATION_ATTR_TYPE_ID;
                            int intCERT_ATTR_STATE_ID = rs[0][0].CERTIFICATION_ATTR_STATE_ID;
                            int intPKI_FORMFACTOR_ID = rs[0][0].PKI_FORMFACTOR_ID;
                            String sTOKEN_SN = EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN);
                            String strEMAIL_CONTRACT = EscapeUtils.CheckTextNull(rs[0][0].EMAIL_CONTRACT);
                            String strFEE_AMOUNT = com.convertMoneyAnotherZero(rs[0][0].FEE_AMOUNT);
                            String strPROFILE_PROMOTION = com.convertMoneyAnotherZero(rs[0][0].CERT_PROFILE_PROMOTION);
                            String strPROFILE_DURATION = com.convertMoneyAnotherZero(rs[0][0].CERT_PROFILE_DURATION);
                            String strPUSH_NOTICE_ENABLED = "1";
                            if(rs[0][0].PUSH_NOTICE_ENABLED == false) {
                                strPUSH_NOTICE_ENABLED = "0";
                            }
                            String strPHONE_CONTRACT = EscapeUtils.CheckTextNull(rs[0][0].PHONE_CONTRACT);
                            String strCERTIFICATION_ATTR_STATE_DESC = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_ATTR_STATE_DESC);
                            String strPAST_SUBJECT = EscapeUtils.CheckTextNull(rs[0][0].PAST_SUBJECT);
                            if(!"".equals(strPAST_SUBJECT)){
                                strPAST_SUBJECT = strPAST_SUBJECT.replace("\\,", "###");
                            }
                            String strPAST_PROPERTIES = EscapeUtils.CheckTextNull(rs[0][0].PAST_PROPERTIES);
                            String strDN = EscapeUtils.CheckTextNull(rs[0][0].SUBJECT);
                            if(!"".equals(strDN)) {
                                strDN = strDN.replace("\\,", "###");
                            }
                            strCERTIFICATION_PROFILE_ID = String.valueOf(rs[0][0].CERTIFICATION_PROFILE_ID);
                            String sCERTIFICATION_ATTR_ID = String.valueOf(rs[0][0].CERTIFICATION_ATTR_ID);
                            String sDisabledAgency = "disabled";
                            String sDisabledAgency_NoInfo = "disabled";
                            String sDisabledAgency_NoInfo_SSL = "disabled";
                            String sDisabledAgency_ChangeCA = "disabled";
                            String sDisabledCA = "disabled";
                            String sDisabledFeeAmount = "disabled";
                            String sDisabledStateProfile = "disabled";
                            String sDisabledEmailInfo_Reissue = "disabled";
                            String sDisabledBackUpKey = "disabled";
                            String sDisabledChangeKey = "disabled";
                            String sDisabledKeepCertSN = "disabled";
                            boolean isAgencyApproved = false;
                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                            {
                                if(intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED)
                                {
                                    sDisabledAgency = "";
                                    sDisabledAgency_NoInfo = "";
                                    sDisabledAgency_NoInfo_SSL = "";
                                    sDisabledAgency_ChangeCA = "";
                                    sDisabledCA = "";
                                    sDisabledEmailInfo_Reissue = "";
                                }
                                if(intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED
                                    || intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                    || intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT)
                                {
                                    if(intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_COMPENSATION
                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE
                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED)
                                    {
                                        sDisabledStateProfile = "";
                                    }
                                }
                                if(intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED
                                    || intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                    || intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT)
                                {
                                    if(intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_COMPENSATION
                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE
                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE
                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED)
                                    {
                                        sDisabledFeeAmount = "";
                                    }
                                }
                            } else {
                                if(intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                    || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT)
                                {
                                    sDisabledAgency = "";
                                    sDisabledAgency_NoInfo = "";
                                    sDisabledAgency_NoInfo_SSL = "";
                                    sDisabledAgency_ChangeCA = "";
                                    sDisabledCA = "disabled";
                                    sDisabledEmailInfo_Reissue = "disabled";
                                    if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                        if(!SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                            int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(sCERTIFICATION_ATTR_ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                            if(intApprove == 2) {
                                                isAgencyApproved = true;
                                            }
                                        }
                                    } else {
                                        if(!SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                            int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(sCERTIFICATION_ATTR_ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                            if(intApprove == 2) {
                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                    isAgencyApproved = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_COMPENSATION
                                || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                                || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE
                                || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED)
                            {
                                sDisabledAgency = "disabled";
                                sDisabledAgency_NoInfo = "disabled";
                                sDisabledAgency_NoInfo_SSL = "disabled";
                                sDisabledAgency_ChangeCA = "disabled";
                                sDisabledCA = "disabled";
                                sDisabledEmailInfo_Reissue = "disabled";
                            }
                            if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL){
                                sDisabledAgency_NoInfo = "disabled";
                                sDisabledAgency_NoInfo_SSL = "disabled";
                                sDisabledAgency_ChangeCA = "";
                                sDisabledCA = "disabled";
                                sDisabledEmailInfo_Reissue = "disabled";
                            }
                            if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE)
                            {
                                sDisabledAgency_NoInfo = "";
                                sDisabledAgency_NoInfo_SSL = "";
                                sDisabledAgency_ChangeCA = "";
                                if(intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED)
                                {
                                    sDisabledAgency_NoInfo = "disabled";
                                    sDisabledAgency_NoInfo_SSL = "disabled";
                                    sDisabledAgency_ChangeCA = "disabled";
                                }
                                if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                {
                                    sDisabledAgency_NoInfo = "";
                                    sDisabledAgency_NoInfo_SSL = "";
                                    sDisabledAgency_ChangeCA = "";
                                    sDisabledAgency = "";
                                }
                            }
                            if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                            {
                                sDisabledAgency_NoInfo = "disabled";
                                sDisabledAgency_NoInfo_SSL = "disabled";
                                sDisabledAgency_ChangeCA = "";
                                sDisabledCA = "disabled";
                                sDisabledEmailInfo_Reissue = "disabled";
                            }
                            if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL)
                            {
                                if(intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED)
                                {
                                    sDisabledCA = "";
                                    sDisabledEmailInfo_Reissue = "";
                                }
                                if(intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                    || intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT)
                                {
                                    sDisabledCA = "disabled";
                                    sDisabledEmailInfo_Reissue = "disabled";
                                }
                            }
                            if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                            {
                                if(intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED)
                                {
                                    sDisabledEmailInfo_Reissue = "";
                                }
                            }
                            if(intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_COMMITED
                                || intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_GENERATED
                                || intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ERROR_ISSUED
                                || intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED
                                || intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_DECLINED
                                || intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED)
                            {
                                sDisabledCA = "disabled";
                                sDisabledEmailInfo_Reissue = "disabled";
                                sDisabledAgency_NoInfo = "disabled";
                                sDisabledAgency_NoInfo_SSL = "disabled";
                                sDisabledAgency_ChangeCA = "disabled";
                                sDisabledAgency = "disabled";
                            }
                            int strCITY_PROVINCE_ID = rs[0][0].CITY_PROVINCE_ID;
                            int pastCITY_PROVINCE_ID = rs[0][0].PAST_CITY_PROVINCE_ID;
                            String strViewAmountFrist = "0";
                            String pFO_DELETE_CERT_WHEN_REVOKE = "";
                            String strAlam = "";
                            String sRegexPolicy = "";
                            String sDiscountRateOption = "";
                            String sArrayFileExten = "";
                            String boViewKeepCertSN = "0";
                            String isActiveSignServer = "0";
                            GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                            if (sessGeneralPolicy != null && sessGeneralPolicy[0].length > 0) {
                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                {
                                    if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                    {
                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_DELETE_CERT_WHEN_REVOKE))
                                        {
                                            pFO_DELETE_CERT_WHEN_REVOKE = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                        }
                                    } else {
                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DELETE_OLD_CERTIFICATE))
                                        {
                                            pFO_DELETE_CERT_WHEN_REVOKE = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                        }
                                    }
                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ALERT_CHECK_CERT_FOR_CA))
                                    {
                                        strAlam = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_REGEX_FOR_PHONE_EMAIL))
                                    {
                                        sRegexPolicy = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION))
                                    {
                                        sDiscountRateOption = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_INVISIBLE_OPTION_KEEPING_SN_FOR_REQUEST))
                                    {
                                        boViewKeepCertSN = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_ALLOWED_FILE_EXTENSION_LIST)) {
                                        sArrayFileExten = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                        isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                }
                            }
                            String sUserCreate = "";
                            String sDateCreate = "";
                            String sUserApprove = "";
                            String sDateApprove = "";
                            String sUserCAApprove = "";
                            String sDateCAApprove = "";
                            String sReasonSuspendValue = "";
                            boolean sDeleteOldCertChangeValue = false;
                            boolean sDeleteOldCertRevokeValue = false;
                            String sReasonDeclineValue = "";
                            String sTimeSuspend = "";
                            boolean booChangeKeyEnabled = false;
                            boolean booKeepCertSNEnabled = false;
                            boolean booRevokedEnabled = false;
                            String sVALUE = EscapeUtils.CheckTextNull(rs[0][0].VALUE);
                            String sValueReasonRevoCoreCA = "";
                            if(!"".equals(sVALUE)) {
                                try {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE, ATTRIBUTE_VALUES.class);
                                    if(valueATTR_Frist != null) {
                                        sUserCreate = EscapeUtils.CheckTextNull(valueATTR_Frist.getCreateUser());
                                        sDateCreate = CommonFunction.dateConvertString(valueATTR_Frist.getCreateDt());
                                        sUserApprove = EscapeUtils.CheckTextNull(valueATTR_Frist.getApproveUser());
                                        sDateApprove = CommonFunction.dateConvertString(valueATTR_Frist.getApproveDt());
                                        sUserCAApprove = EscapeUtils.CheckTextNull(valueATTR_Frist.getApproveCAUser());
                                        sDateCAApprove = CommonFunction.dateConvertString(valueATTR_Frist.getApproveCADt());
                                        booChangeKeyEnabled = valueATTR_Frist.getChangeKeyEnabled();
                                        if(sVALUE.contains(Definitions.CONFIG_POLICY_VALUE_CONTAIN_CERT_ATTR_KEEP_SN)) {
                                            booKeepCertSNEnabled = valueATTR_Frist.getKeepCertificateSNEnabled();
                                        }
                                        booRevokedEnabled = valueATTR_Frist.getRevokeOldCertificateEnabled();
                                        sDeleteOldCertChangeValue = valueATTR_Frist.getDeleteOldCertificateEnabled();
                                        sDeleteOldCertRevokeValue = valueATTR_Frist.getCertRevokeDeleteInTokenEnabled();
                                        sReasonSuspendValue = EscapeUtils.CheckTextNull(valueATTR_Frist.getCerttificateSuspendReason());
                                        sReasonDeclineValue = EscapeUtils.CheckTextNull(valueATTR_Frist.getCerttificateDeclineReason());
                                        sValueReasonRevoCoreCA = EscapeUtils.CheckTextNull(valueATTR_Frist.getCerttificateRevokeEJBCAReason());
                                        try {
                                            sTimeSuspend = CommonFunction.ConvertTimeStampToString(valueATTR_Frist.getSuspendedTime());
                                        } catch(Exception e){CommonFunction.LogExceptionServlet(null, "sTimeSuspend: " + e.getMessage(), e);}
                                    }
                                } catch(Exception e){CommonFunction.LogExceptionServlet(null, "ATTRIBUTE_VALUES: " + e.getMessage(), e);}
                            }
                            String sREGEX_PHONE = Definitions.CONFIG_DEFAULT_VALUE_REGEX_PHONE;
                            String sREGEX_EMAIL = Definitions.CONFIG_DEFAULT_VALUE_REGEX_EMAIL;
                            if(!"".equals(sRegexPolicy))
                            {
                                sREGEX_PHONE = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_PHONE);
                                sREGEX_EMAIL = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_EMAIL);
                            }
                            session.setAttribute("sessDNSNameForSSL_Approve", null);
                            String sPROPERTIES = EscapeUtils.CheckTextNull(rs[0][0].PROPERTIES);
                            String sSanDataDB = "";
                            if(!"".equals(sPROPERTIES))
                            {
                                try {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    CERTIFICATION_PROPERTIES_JSON itemParsePush = objectMapper.readValue(sPROPERTIES, CERTIFICATION_PROPERTIES_JSON.class);
                                    if(itemParsePush != null) {
                                        if(itemParsePush.getAttributes().size() > 0) {
                                            for (int i = 0; i < itemParsePush.getAttributes().size(); i++) {
                                                sSanDataDB = sSanDataDB + EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getKey()) + "###" + EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getValue()) + "@@@";
                                            }
                                        }
                                    }
                                } catch(Exception e){CommonFunction.LogExceptionServlet(null, "sSanDataDB: " + e.getMessage(), e);}
                            }
                            String sOldSanDataDB = "";
                            if(!"".equals(strPAST_PROPERTIES)) {
                                try {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    CERTIFICATION_PROPERTIES_JSON itemParsePush = objectMapper.readValue(strPAST_PROPERTIES, CERTIFICATION_PROPERTIES_JSON.class);
                                    if(itemParsePush != null) {
                                        if(itemParsePush.getAttributes().size() > 0) {
                                            for (int i = 0; i < itemParsePush.getAttributes().size(); i++) {
                                                sOldSanDataDB = sOldSanDataDB + EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getKey()) + "###" + EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getValue()) + "@@@";
                                            }
                                        }
                                    }
                                } catch(Exception e){CommonFunction.LogExceptionServlet(null, "sOldSanDataDB: " + e.getMessage(), e);}
                            }
                            String sDiscountRate = "0";
                            if("1".equals(sDiscountRateOption)) {
                                if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                    || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                                    || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED)
                                {
                                    if(rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                        || rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                        || rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                        try {
                                            DISCOUNT_RATE_PROFILE[][] rsDisCount = new DISCOUNT_RATE_PROFILE[1][];
                                            db.S_BO_BRANCH_GET_DISCOUNT_RATE_PROFILE(sRequestAgentID, rsDisCount);
                                            if(rsDisCount[0].length > 0) {
                                                if(!"".equals(rsDisCount[0][0].PROPERTIES)) {
                                                    PROFILE_DISCOUNT_RATE_DATA[][] resIPData = new PROFILE_DISCOUNT_RATE_DATA[1][];
                                                    CommonFunction.getAllProfileDiscountRate(rsDisCount[0][0].PROPERTIES, resIPData);
                                                    if(resIPData[0] != null && resIPData[0].length > 0) {
                                                        String sProfileCode = "";
                                                        CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(String.valueOf(rs[0][0].CERTIFICATION_PROFILE_ID), rsProfile);
                                                        if(rsProfile[0].length > 0) {
                                                            sProfileCode = EscapeUtils.CheckTextNull(rsProfile[0][0].NAME);
                                                        }
                                                        for(PROFILE_DISCOUNT_RATE_DATA resIPData1 : resIPData[0]) {
                                                            if(EscapeUtils.CheckTextNull(resIPData1.profileName).equals(sProfileCode)) {
                                                                sDiscountRate = resIPData1.rosePercent;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } catch(Exception e){CommonFunction.LogExceptionServlet(null, "DISCOUNT_RATE_PROFILE: " + e.getMessage(), e);}
                                    } else {
                                        sDiscountRate = String.valueOf(rs[0][0].DISCOUNT_RATE);
                                    }
                                } else {
                                    sDiscountRate = String.valueOf(rs[0][0].DISCOUNT_RATE);
                                }
                            }
                            if(isAgencyApproved == true){
                                sDisabledAgency_NoInfo = "disabled";
                                sDisabledAgency_NoInfo_SSL = "disabled";
                                sDisabledAgency_ChangeCA = "disabled";
                                sDisabledAgency = "disabled";
                            }
                            String isViewActionFileEnable = "1";
                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) && !SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                isViewActionFileEnable = "0";
                            }
        %>
        <script>
            $(document).ready(function () {
                localStorage.setItem("sessLocal_REGEX_PHONE", '<%=sREGEX_PHONE%>');
                localStorage.setItem("sessLocal_REGEX_EMAIL", '<%=sREGEX_EMAIL%>');
            });
        </script>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEdits"></span> </h2>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <%
                        if (rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT) {
                            if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                    %>
                    <%
                        if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ACCOUNTANT)) {
                            if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                {
                    %>
                    <input type="button" id="btnApprove" class="btn btn-info" onclick="ApproveFormAgency('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnApprove").value = global_fm_approve;</script>
                    <%
                            }
                        } else {
                            int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(sCERTIFICATION_ATTR_ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                            if(intApprove == 1) {
                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                {
                    %>
                                <input type="button" id="btnApprove" class="btn btn-info" onclick="ApproveFormChildrenAgency('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                                <script>document.getElementById("btnApprove").value = global_fm_approve;</script>
                    <%
                                    }
                                }
                            }
                        } else {
                            if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                    %>
                    <input type="button" id="btnApprove" class="btn btn-info" onclick="ApproveFormAgency('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnApprove").value = global_fm_approve;</script>
                    <%
                            } else {
                                int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(sCERTIFICATION_ATTR_ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                if(intApprove == 1) {
                    %>
                                    <input type="button" id="btnApprove" class="btn btn-info" onclick="ApproveFormChildrenAgency('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                                    <script>document.getElementById("btnApprove").value = global_fm_approve;</script>
                    <%
                                }
                            }
                        }
                    %>
                    <%
                        } else {
                            if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                            {
                    %>
                    <input type="button" id="btnApprove" class="btn btn-info" onclick="ApproveFormCA('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnApprove").value = global_fm_approve;</script>
                    <%
                        } else {
                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                    %>
                    <input type="button" id="btnApprove" class="btn btn-info" onclick="ApproveFormCA('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnApprove").value = global_fm_approve;</script>
                    <%
                                    }
                                }
                            }
                        } else if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED) {
                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                    %>
                    <%
                        if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_USER) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ACCOUNTANT)) {
                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                    %>
                    <input type="button" id="btnApprove" class="btn btn-info" onclick="ApproveFormCA('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnApprove").value = global_fm_approve;</script>
                    <%
                            }
                        } else {
                    %>
                    <input type="button" id="btnApprove" class="btn btn-info" onclick="ApproveFormCA('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnApprove").value = global_fm_approve;</script>
                    <%
                        }
                    %>
                    <%
                            }
                        } else {strViewAmountFrist = "1";}
                    %>
                    <%
                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            boolean isAllowFunctionApprove = false;
                            if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED) {
                                if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                    || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                    || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                                {
                                    isAllowFunctionApprove = true;
                                } else {
                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                    {
                                        isAllowFunctionApprove = true;
                                    }
                                }
                                if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                {
                                    if(intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE
                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED)
                                    {
                                        if(isAllowFunctionApprove == true) {
                                            String sViewFormReIssueSoft = "";
                                            if("1".equals(isActiveSignServer) && intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN){
                                                if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                    || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                                                    || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED
                                                    || (rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED
                                                    && rs[0][0].ACTIVATED_ENABLED == 0)) {
                                                    sViewFormReIssueSoft = "display:none;";
                                                }
                                            }
                    %>
                    <input type="button" id="btnIssue" style="<%=sViewFormReIssueSoft%>" class="btn btn-info" onclick="FormReIssueSoft('<%= ids%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnIssue").value = global_fm_button_issue;</script>
                    <%
                                        }
                                    }
                                }
                                if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                                    && (rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVOKED
                                    && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_AUTO_REVOKED)) {
                                    if(isAllowFunctionApprove == true)
                                    {
                    %>
                    <input type="button" id="btnReRevoke" class="btn btn-info" onclick="FormReRevoke('<%= ids%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnReRevoke").value = global_fm_approve;</script>
                    <input type="button" id="btnDecline" class="btn btn-info" onclick="popupDialogCertDecline('<%= sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(rs[0][0].BRANCH_ID)%>', '<%= String.valueOf(rs[0][0].CREATED_BY_ID)%>');" />
                    <script>document.getElementById("btnDecline").value = global_fm_button_decline;</script>
                    <%
                                    }
                                }
                                if((intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                    || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                    && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_PERMANENT_DISABLE
                                    && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                                    && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_PERMANENT_DISABLE
                                    && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE) {
                                    if(isAllowFunctionApprove == true)
                                    {
                    %>
                    <input type="button" id="btnReSuspend" class="btn btn-info" onclick="FormReSuspend('<%= ids%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnReSuspend").value = global_fm_approve;</script>
                    <%
                                    }
                                }
                                if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED
                                    && (rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_PERMANENT_DISABLE
                                    || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                                    || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_PERMANENT_DISABLE
                                    || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE))
                                {
                                    if(isAllowFunctionApprove == true)
                                    {
                    %>
                    <input type="button" id="btnReRecovery" class="btn btn-info" onclick="FormReRecovery('<%= ids%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnReRecovery").value = global_fm_approve;</script>
                    <%
                                    }
                                }
                            }
                        }
                    %>
                    <%
                        if (rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT) {
                            boolean isDeclineAgent = false;
                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                isDeclineAgent = true;
                            } else {
                                if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                    if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                        isDeclineAgent = true;
                                    } else {
                                        int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(sCERTIFICATION_ATTR_ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                        if(intApprove == 1) {
                                            isDeclineAgent = true;
                                        }
                                    }
                                } else {
                                    if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                            isDeclineAgent = true;
                                        }
                                    } else {
                                        int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(sCERTIFICATION_ATTR_ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                        if(intApprove == 1) {
                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                isDeclineAgent = true;
                                            }
                                        }
                                    }
                                }
                            }
                    %>
                    <%
                        if(isDeclineAgent == true) {
                    %>
                    <input type="button" id="btnDecline" class="btn btn-info" onclick="popupDialogCertDecline('<%= sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(rs[0][0].BRANCH_ID)%>', '<%= String.valueOf(rs[0][0].CREATED_BY_ID)%>');" />
                    <script>document.getElementById("btnDecline").value = global_fm_button_decline;</script>
                    <%
                        }
                    %>
                    <%
                        } else if(rs[0][0].CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_COMMITED
                            && rs[0][0].CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_DECLINED
                            && rs[0][0].CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED)
                        {
                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED
                                    && intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                { } else {
                    %>
                    <%
                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                        {
                    %>
                    <input type="button" id="btnDecline" class="btn btn-info" onclick="popupDialogCertDecline('<%= sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(rs[0][0].BRANCH_ID)%>', '<%= String.valueOf(rs[0][0].CREATED_BY_ID)%>');" />
                    <script>document.getElementById("btnDecline").value = global_fm_button_decline;</script>
                    <%
                        }
                    %>
                    <%
                                }
                            }
                        } else if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED)
                        {
                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                {
                    %>
                    <input type="button" id="btnDecline" class="btn btn-info" onclick="popupDialogCertDecline('<%= sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(rs[0][0].BRANCH_ID)%>', '<%= String.valueOf(rs[0][0].CREATED_BY_ID)%>');" />
                    <script>document.getElementById("btnDecline").value = global_fm_button_decline;</script>
                    <%
                                }
                            }
                        }
                        if (rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT) {
                            sDisabledBackUpKey = "";
                            sDisabledChangeKey = "";
                            sDisabledKeepCertSN = "";
                        } else if(rs[0][0].CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_COMMITED
                            && rs[0][0].CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_DECLINED
                            && rs[0][0].CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED)
                        {
                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED
                                    && intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                { } else {
                                    if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED)
                                    {
                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            sDisabledBackUpKey = "";
                                            sDisabledChangeKey = "";
                                            sDisabledKeepCertSN = "";
                                        }
                                    }
                                }
                            }
                        } else if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED)
                        {
                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            }
                        }
                        if(isAgencyApproved == true) {
                            sDisabledBackUpKey = "disabled";
                        }
                    %>
                    <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                </li>
                <script>document.getElementById("btnClose").value = global_fm_button_close;</script>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <form name="myname" method="post" class="form-horizontal">
                <input type="hidden" id="sID" name="sID" hidden="true" readonly="true" value="<%= rs[0][0].ID%>">
                <input type="hidden" id="sCERTIFICATION_ATTR_TYPE_ID" name="sCERTIFICATION_ATTR_TYPE_ID" hidden="true" readonly="true" value="<%= intCERTIFICATION_ATTR_TYPE_ID%>">
                <input type="hidden" name="CsrfTokenDetail" id="CsrfTokenDetail" value="<%=anticsrf%>"/>
                <input type="hidden" name="idHiddenDN" id="idHiddenDN" value="<%=EscapeUtils.escapeHtmlDN(strDN)%>"/>
                <%
                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                        if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                            || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                        {
                            if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                                || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED) {
                                try {
                                    String[] sUIDResult = new String[2];
                                    CommonReferServlet.collectFieldToUID(EscapeUtils.CheckTextNull(rs[0][0].TAX_CODE), EscapeUtils.CheckTextNull(rs[0][0].BUDGET_CODE),
                                        EscapeUtils.CheckTextNull(rs[0][0].DECISION), EscapeUtils.CheckTextNull(rs[0][0].P_ID),
                                        EscapeUtils.CheckTextNull(rs[0][0].PASSPORT), EscapeUtils.CheckTextNull(rs[0][0].P_EID), sUIDResult);
                                    String sEnterpriseCert = sUIDResult[0];
                                    String sPersonalCert = sUIDResult[1];
                                    String sAlam = db.S_BO_CHECK_ARLAM_CERTIFICATION(rs[0][0].CERTIFICATION_PURPOSE_ID, EscapeUtils.CheckTextNull(rs[0][0].TAX_CODE),
                                        EscapeUtils.CheckTextNull(rs[0][0].P_ID), EscapeUtils.CheckTextNull(rs[0][0].BUDGET_CODE),
                                        EscapeUtils.CheckTextNull(rs[0][0].DECISION), EscapeUtils.CheckTextNull(rs[0][0].PASSPORT),
                                        EscapeUtils.CheckTextNull(rs[0][0].P_EID), String.valueOf(intPKI_FORMFACTOR_ID), sEnterpriseCert, sPersonalCert);
                                    if(!"0".equals(sAlam) && !"2".equals(sAlam)) {
                %>
                <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                    <label class="control-label123" style="color: red;"><%= strAlam%></label>
                </div>
                <%
                                    }
                                } catch(Exception e){CommonFunction.LogExceptionServlet(null, "S_BO_CHECK_ARLAM_CERTIFICATION: " + e.getMessage(), e);}
                            }
                        }
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCertAttrType" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="CERTIFICATION_ATTR_TYPE" id="CERTIFICATION_ATTR_TYPE" class="form-control123" disabled>
                                <%
                                    CERTIFICATION_ATTR_TYPE[][] rsType = new CERTIFICATION_ATTR_TYPE[1][];
                                    db.S_BO_CERTIFICATION_ATTR_TYPE_COMBOBOX(sessLanguageGlobal, rsType);
                                    if (rsType[0].length > 0) {
                                        for (CERTIFICATION_ATTR_TYPE temp1 : rsType[0]) {
                                %>
                                <option value="<%=String.valueOf(temp1.ID)%>" <%= temp1.ID == intCERTIFICATION_ATTR_TYPE_ID ? "selected" : "" %>><%=temp1.REMARK%></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                </div>
                <%
                    if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                        String sDisabledReasonRevoke = "disabled";
                        if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED)
                        {
                            if(rs[0][0].PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD
                                && rs[0][0].PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_REMOTE_SIGNING) {
                                sDisabledReasonRevoke = "";
                            }
                        }
                        if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED) {
                            if(!"".equals(sVALUE)) {
                                try {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE, ATTRIBUTE_VALUES.class);
                                    sRevokeReason = EscapeUtils.CheckTextNull(valueATTR_Frist.getCerttificateRevokeReason());
                                } catch(Exception e){CommonFunction.LogExceptionServlet(null, "ATTRIBUTE_VALUES sRevokeReason: " + e.getMessage(), e);}
                            }
                        } else { }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;text-align: left;">
                            <label id="idLblTitleDeclineReason"></label>
                            <%
                                if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            %>
                            <label class="CssRequireField" id="idLblNoteRevokeReason"></label>
                            <script>$("#idLblNoteRevokeReason").text(global_fm_require_label);</script>
                            <%
                                }
                            %>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" id="RevokeReason" name="RevokeReason" value="<%= EscapeUtils.escapeHtmlDN(sRevokeReason)%>" class="form-control123" <%= sDisabledReasonRevoke%> />
                            <input type="hidden" id="checkRevokeReason" name="checkRevokeReason" value="<%= SessAgentID%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleDeclineReason").text(global_fm_revoke_desc);
                    </script>
                </div>
                <%
                    boolean isViewReasonRevoCoreA = false;
                    if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                        || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                        || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                    {
                        isViewReasonRevoCoreA = true;
                    } else {
                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                        {
                            isViewReasonRevoCoreA = true;
                        }
                    }
                    if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                        || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED)
                    {
                        if(isViewReasonRevoCoreA == true)
                        {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleRevokeResonCore" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="CERT_REVOCATION_REASON" id="CERT_REVOCATION_REASON" <%= sDisabledReasonRevoke%> class="form-control123" onchange="onChangeReasonCore(this.value);">
                                <%
                                    CERTIFICATION_REVOCATION_REASON[][] rsReasonCore = new CERTIFICATION_REVOCATION_REASON[1][];
                                    db.S_BO_CERTIFICATION_REVOCATION_REASON_COMBOBOX(sessLanguageGlobal, rsReasonCore);
                                    if (rsReasonCore[0].length > 0) {
                                        for (CERTIFICATION_REVOCATION_REASON temp1 : rsReasonCore[0]) {
                                            if(temp1.ID != Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_CERTIFICATEHOLD_ID
                                                && temp1.ID != Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_REMOVEFROMCRL_ID)
                                            {
                                %>
                                <option value="<%=String.valueOf(temp1.ID)%>"><%=temp1.REMARK%></option>
                                <%
                                            }
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleRevokeResonCore").text(global_fm_revoke_reason_core);
                        function onChangeReasonCore(obj)
                        {
                            if(obj === JS_STR_REASON_CORECA_UNSPECIFIED) {
                                document.getElementById("RevokeReason").readOnly = false;
                                document.getElementById("RevokeReason").value = '<%= sRevokeReason%>';
                            } else {
                                document.getElementById("RevokeReason").readOnly = true;
                                document.getElementById("RevokeReason").value = $("#CERT_REVOCATION_REASON option:selected").text();
                            }
                        }
                        $(document).ready(function () {
                            onChangeReasonCore($("#CERT_REVOCATION_REASON").val());
                        });
                    </script>
                </div>
                <%
                    } else {
                %>
                <input type="text" id="CERT_REVOCATION_REASON" name="CERT_REVOCATION_REASON" readonly value="" style="display: none;" />
                <%
                        }
                    } else {
                        if(isViewReasonRevoCoreA == true && !"".equals(sValueReasonRevoCoreCA)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleRevokeResonCore" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="CERT_REVOCATION_REASON" id="CERT_REVOCATION_REASON" disabled class="form-control123">
                                <%
                                    CERTIFICATION_REVOCATION_REASON[][] rsReasonCore = new CERTIFICATION_REVOCATION_REASON[1][];
                                    db.S_BO_CERTIFICATION_REVOCATION_REASON_COMBOBOX(sessLanguageGlobal, rsReasonCore);
                                    if (rsReasonCore[0].length > 0) {
                                        for (CERTIFICATION_REVOCATION_REASON temp1 : rsReasonCore[0]) {
                                            if(temp1.ID != Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_CERTIFICATEHOLD_ID
                                                && temp1.ID != Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_REMOVEFROMCRL_ID)
                                            {
                                %>
                                <option value="<%=String.valueOf(temp1.ID)%>" <%=String.valueOf(temp1.ID).equals(sValueReasonRevoCoreCA) ? "selected" : "" %>><%=temp1.REMARK%></option>
                                <%
                                            }
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <script>$("#idLblTitleRevokeResonCore").text(global_fm_revoke_reason_core);</script>
                </div>
                <%
                        }
                    }
                %>
                <%
                    } else {
                %>
                <input type="text" id="RevokeReason" name="RevokeReason" value="" style="display: none;" />
                <input type="hidden" id="checkRevokeReason" name="checkRevokeReason" value="0"/>
                <%
                    }
                %>
                <%
                    if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                        || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                    {
                        String sDisabledReasonRevoke = "disabled";
                        if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED)
                        {
                            sDisabledReasonRevoke = "";
                        }
                        if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_COMMITED)
                        {
                            try {
                                ObjectMapper oMapperParse = new ObjectMapper();
                                CERTIFICATION_COMMENT itemParsePush = oMapperParse.readValue(EscapeUtils.CheckTextNull(rs[0][0].COMMENT), CERTIFICATION_COMMENT.class);
                                sReasonSuspendValue = itemParsePush.certificateSuspendReason;
                            } catch(Exception e){CommonFunction.LogExceptionServlet(null, "CERTIFICATION_COMMENT sDisabledReasonRevoke: " + e.getMessage(), e);}
                        }
                        if(!"".equals(sReasonSuspendValue) && !"disabled".equals(sDisabledReasonRevoke))
                        {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleSuspendReason" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" id="SuspendReason" name="SuspendReason" value="<%= EscapeUtils.escapeHtmlDN(sReasonSuspendValue)%>" class="form-control123" <%= sDisabledReasonRevoke%> />
                        </div>
                    </div>
                    <script>$("#idLblTitleSuspendReason").text(global_fm_suspend_desc);</script>
                </div>
                <%
                    }
                %>
                <%
                    if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE
                        && (rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                        || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                        || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED))
                    {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleSuspendTime" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" class="form-control123" name="SuspendTime" id="SuspendTime"
                                value="<%=sTimeSuspend%>" readonly style="background-color: #ffffff;"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleSuspendTime").text(global_fm_times_recovery);</script>
                </div>
                <script>
                    $(document).ready(function () {
                        $('#SuspendTime').daterangepicker({
                            "singleDatePicker": true,
                            "timePicker": true,
                            "timePicker24Hour": true,
                            "timePickerSeconds": true,
                            showDropdowns: true,
                            minDate: moment(),
                            "locale": {
                                "direction": "rtl",
                                "format": "DD/MM/YYYY HH:mm:ss",
                                "separator": " - ",
                                "applyLabel": "Apply",
                                "cancelLabel": "Cancel",
                                "fromLabel": "From",
                                "toLabel": "To",
                                "customRangeLabel": "Custom",
                                "daysOfWeek": [
                                    "Su",
                                    "Mo",
                                    "Tu",
                                    "We",
                                    "Th",
                                    "Fr",
                                    "Sa"
                                ],
                                "monthNames": [
                                    "January",
                                    "February",
                                    "March",
                                    "April",
                                    "May",
                                    "June",
                                    "July",
                                    "August",
                                    "September",
                                    "October",
                                    "November",
                                    "December"
                                ],
                                "firstDay": 1
                            },
                            "showCustomRangeLabel": false,
                            "alwaysShowCalendars": true
                        }, function (start, end, label) {
                            console.log(start.toISOString(), end.toISOString(), label);
                        });
                    });
                </script>
                <%
                    } else {
                        if(!"".equals(sTimeSuspend))
                        {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleSuspendTime" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="SuspendTime" id="SuspendTime" value="<%=sTimeSuspend%>" readonly class="form-control123"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleSuspendTime").text(global_fm_times_recovery);</script>
                </div>
                <%
                        } else {
                %>
                <input type="text" name="SuspendTime" style="display: none;" id="SuspendTime" value="" readonly class="form-control123"/>
                <%
                        }
                    }
                %>
                <%
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCertAttrState" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="CERTIFICATION_ATTR_STATE_DESC" value="<%= strCERTIFICATION_ATTR_STATE_DESC%>" class="form-control123">
                        </div>
                    </div>
                </div>
                <%
                    int intApproveView = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(sCERTIFICATION_ATTR_ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                    if(intApproveView == 2) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleApproveAgent" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="ApproveAgent">
                                <input TYPE="checkbox" id="ApproveAgent" name="ApproveAgent" checked disabled />
                                <div class="slider round disabled"></div>
                            </label>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleApproveAgent").text(cert_fm_request_agent);
                    </script>
                </div>
                <%
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitlePhoneContact"></label>
                            <%
                                if(sDisabledAgency_NoInfo.equals("")) {
                            %>
                            <label class="CssRequireField" id="idLblNotePhoneContact"></label>
                            <script>$("#idLblNotePhoneContact").text(global_fm_require_label);</script>
                            <%
                                }
                            %>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="PHONE_CONTRACT" id="PHONE_CONTRACT" value="<%= strPHONE_CONTRACT%>"
                                class="form-control123" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_PHONE%>"
                                onblur="autoTrimTextField('PHONE_CONTRACT', this.value);" <%= sDisabledAgency_NoInfo%>>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleEmailContact"></label>
                            <%
                                if(sDisabledAgency_NoInfo.equals("")) {
                            %>
                            <label class="CssRequireField" id="idLblNoteEmailContact"></label>
                            <script>$("#idLblNoteEmailContact").text(global_fm_require_label);</script>
                            <%
                                }
                            %>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="EMAIL_CONTRACT" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_EMAIL%>"
                                id="EMAIL_CONTRACT" value="<%= strEMAIL_CONTRACT%>" class="form-control123" <%= sDisabledAgency_NoInfo%>>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCertCA" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="CERTIFICATION_AUTHORITY" id="CERTIFICATION_AUTHORITY" class="form-control123"
                                onchange="LOAD_CERTIFICATION_AUTHORITY(this.value, '<%= String.valueOf(rs[0][0].CERTIFICATION_PURPOSE_ID)%>', '<%= anticsrf%>');" <%= sDisabledAgency_ChangeCA%>>
                                <%
                                    CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                    db.S_BO_CERTIFICATION_AUTHORITY_COMBOBOX(sessLanguageGlobal, rsCA);
                                    String sFristCA = "";
                                    String sCACoreSubject = "";
                                    if (rsCA[0].length > 0) {
                                        sFristCA = String.valueOf(rs[0][0].CERTIFICATION_AUTHORITY_ID);
                                        for (CERTIFICATION_AUTHORITY temp1 : rsCA[0]) {
                                            if(temp1.ID == rs[0][0].CERTIFICATION_AUTHORITY_ID)
                                            {
                                                sCACoreSubject = EscapeUtils.CheckTextNull(temp1.CERTIFICATION_AUTHORITY_CORECA_SUBJECT);
                                            }
                                %>
                                <option value="<%=String.valueOf(temp1.ID)
                                    + "###" + EscapeUtils.CheckTextNull(temp1.CERTIFICATION_AUTHORITY_CORECA_SUBJECT)%>" <%= temp1.ID == rs[0][0].CERTIFICATION_AUTHORITY_ID ? "selected" : ""%>><%=temp1.REMARK%></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCertPurpose" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="CERTIFICATION_PURPOSE" id="CERTIFICATION_PURPOSE" class="form-control123"
                                onchange="LOAD_CERTIFICATION_PURPOSE($('#idHiddenCerCA').val().split('###')[0], this.value, '<%= anticsrf%>');" <%= sDisabledAgency_NoInfo_SSL%>>
                                <%
                                    CERTIFICATION_PURPOSE[][] rsPurpose = new CERTIFICATION_PURPOSE[1][];
                                    String sFristCerPurpose="";
                                    db.S_BO_CA_GET_CERTIFICATION_PURPOSE_COMBOBOX(sFristCA, sessLanguageGlobal, rsPurpose);
                                    if (rsPurpose[0].length > 0) {
                                        sFristCerPurpose = String.valueOf(rs[0][0].CERTIFICATION_PURPOSE_ID);
                                        for (CERTIFICATION_PURPOSE temp1 : rsPurpose[0]) {
                                %>
                                <option value="<%=String.valueOf(temp1.ID)%>" <%= temp1.ID == rs[0][0].CERTIFICATION_PURPOSE_ID ? "selected" : ""%>><%=temp1.REMARK%></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                        <script>$("#idLblTitleCertPurpose").text(global_fm_certpurpose);</script>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleFormFactor" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="PKI_FORMFACTOR_DETAIL" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].PKI_FORMFACTOR_DESC) %>"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleFormFactor").text(global_fm_Method);</script>
                </div>
                <%
                    String sFristCerDurationOrProfileID = "";
                    String sViewDurationFee = "";
                    String sViewDurationSum = "";
                    if(intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED
                        || intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING) {
                        if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                            || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE
                            || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                            || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_COMPENSATION
                            || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                            || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE
                            || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED) {
                            sViewDurationFee = "none;";
                        }
                    } else {
                        sViewDurationFee = "none;";
                    }
                    if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                        || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE
                        || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                        || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_COMPENSATION
                        || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                        || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE
                        || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED) {
                        sViewDurationSum = "none;";
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCertDuration" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <%
                                boolean showCertDurationText = false;
                                if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE
                                    || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                    || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED) {
                                    showCertDurationText = true;
                                } else {
                                    if("disabled".equals(sDisabledAgency_ChangeCA)) {
                                        showCertDurationText = true;
                                    }
                                }
                            %>
                            <%
                                if(showCertDurationText == true) {
                                    CERTIFICATION_PROFILE[][] rsDurationText = new CERTIFICATION_PROFILE[1][];
                                    db.S_BO_CERTIFICATION_PROFILE_DETAIL(String.valueOf(rs[0][0].CERTIFICATION_PROFILE_ID), rsDurationText);
                                    String nameDurationText = "";
                                    sFristCerDurationOrProfileID = String.valueOf(rs[0][0].CERTIFICATION_PROFILE_ID);
                                    if(rsDurationText != null && rsDurationText[0].length > 0){
                                        nameDurationText = "[" + EscapeUtils.CheckTextNull(rsDurationText[0][0].NAME) + "] " + EscapeUtils.CheckTextNull(rsDurationText[0][0].REMARK); //EscapeUtils.CheckTextNull(rsDurationText[0][0].NAME);
                                    }
                            %>
                            <input type="text" disabled name="CERTIFICATION_DURATION" value="<%= nameDurationText%>" id="CERTIFICATION_DURATION" class="form-control123">
                            <%
                                } else {
                            %>
                            <select id="CERTIFICATION_DURATION" name="CERTIFICATION_DURATION" class="form-control123"
                                onchange="LOAD_CERTIFICATION_DURATION(this.value, '<%= anticsrf%>');" <%= intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE ? "disabled" : sDisabledAgency_ChangeCA%>>
                                <%
                                    CERTIFICATION_PROFILE[][] rsDuration = new CERTIFICATION_PROFILE[1][];
                                    db.S_BO_CA_GET_DURATION_COMBOBOX_BY_TYPE(sFristCA, sFristCerPurpose, String.valueOf(intPKI_FORMFACTOR_ID), intCERTIFICATION_ATTR_TYPE_ID, sessLanguageGlobal, rsDuration);
                                    if (rsDuration[0].length > 0) {
                                        sFristCerDurationOrProfileID = String.valueOf(rs[0][0].CERTIFICATION_PROFILE_ID);
                                        for (int i = 0; i < rsDuration[0].length; i++) {
                                %>
                                <option value="<%= String.valueOf(rsDuration[0][i].ID)%>" <%= rsDuration[0][i].ID == rs[0][0].CERTIFICATION_PROFILE_ID ? "selected" : ""%>><%= "[" + rsDuration[0][i].NAME + "] " + rsDuration[0][i].REMARK %></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                            <%
                                }
                            %>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0; display: <%= sViewDurationFee%>">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleFeeAmount"></label>
                            <%
                                if(sDisabledFeeAmount.equals("")) {
                            %>
                            <label class="CssRequireField" id="idLblNoteFeeAmount"></label>
                            <script>$("#idLblNoteFeeAmount").text(global_fm_require_label);</script>
                            <%
                                }
                            %>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="FEE_AMOUNT" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_AMOUNT%>"
                                value="<%= strFEE_AMOUNT%>" id="FEE_AMOUNT" class="form-control123" oninput="autoConvertMoney(this.value, $('#FEE_AMOUNT'))" <%= sDisabledFeeAmount%>>
                            <input type="hidden" readonly value="<%= strFEE_AMOUNT%>" id="HIDDEN_FEE_AMOUNT">
                        </div>
                    </div>
                </div>
                <%
                    String sDisabledDiscountRate = "none";
                %>
                <div class="col-sm-6" style="padding-left: 0; display: <%= sDisabledDiscountRate%>">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblNoteFeePercent"></label>
                            <script>$("#idLblNoteFeePercent").text(global_fm_percent_cts);</script>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="FEE_PERCENT" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_PERCENT%>"
                                value="<%= sDiscountRate%>" <%= sDisabledCA %> id="FEE_PERCENT" class="form-control123">
                        </div>
                    </div>
                </div>
                <%
                    if(!"".equals(rs[0][0].CERTIFICATION_SN)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblNoteCertSNDetail"></label>
                            <script>$("#idLblNoteCertSNDetail").text(global_fm_serial);</script>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" disabled name="CERTIFICATION_SN_DETAIL" value="<%= rs[0][0].CERTIFICATION_SN%>"
                                id="CERTIFICATION_SN_DETAIL" class="form-control123">
                        </div>
                    </div>
                </div>
                <%
                    }
                %>
                <%
                    if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_DECLINED)
                    {
                        String sDeclineReason = "";
                        if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                            || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                            || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE
                            || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED)
                        {
                            sDeclineReason = sReasonDeclineValue;
                        } else {
                            if(!"".equals(EscapeUtils.CheckTextNull(rs[0][0].COMMENT)))
                            {
                                try {
                                    ObjectMapper oMapperParse = new ObjectMapper();
                                    CERTIFICATION_COMMENT itemParsePush = oMapperParse.readValue(EscapeUtils.CheckTextNull(rs[0][0].COMMENT), CERTIFICATION_COMMENT.class);
                                    sDeclineReason = itemParsePush.certificateDeclineReason;
                                } catch(Exception e){CommonFunction.LogExceptionServlet(null, "CERTIFICATION_COMMENT sDeclineReason: " + e.getMessage(), e);}
                            }
                        }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleDesDecliveView" class="control-label col-sm-5" style="color: <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO) ? "red" : "#000000" %>; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="DESC_DECLINE_VIEW" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_COMPANYNAME%>"
                                id="DESC_DECLINE_VIEW" readonly value="<%= EscapeUtils.escapeHtmlDN(sDeclineReason)%>" class="form-control123"
                                <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO) ? "style='color: red;'" : "" %>/>
                        </div>
                    </div>
                    <script>$("#idLblTitleDesDecliveView").text(global_fm_decline_desc);</script>
                </div>
                <%
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;display: <%= sViewDurationFee%>" id="idViewDURATION_FREE">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleDurationFree"></label>
                            <%
                                if(sDisabledFeeAmount.equals("")) {
                            %>
                            <label class="CssRequireField" id="idLblNoteDurationFree"></label>
                            <script>$("#idLblNoteDurationFree").text(global_fm_require_label);</script>
                            <%
                                }
                            %>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="DURATION_FREE" value="<%= strPROFILE_PROMOTION%>" id="DURATION_FREE"
                                class="form-control123" <%= sDisabledFeeAmount%>>
                            <input type="hidden" readonly value="<%= strPROFILE_DURATION%>" id="HIDDEN_DURATION">
                        </div>
                    </div>
                    <script>$("#idLblTitleDurationFree").text(global_fm_date_free);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;display: <%= sViewDurationSum%>">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleDurationSum"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="DURATION_SUM" value="<%= com.convertMoneyAnotherZero(rs[0][0].DURATION)%>" id="DURATION_SUM"
                                class="form-control123" disabled>
                        </div>
                    </div>
                    <script>$("#idLblTitleDurationSum").text(global_fm_duration_promotion);</script>
                </div>
                <%
                    if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                        || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                        || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                %>
                <%
                    String sDisableAgentUser = "disabled";
                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                        if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED) {
                            sDisableAgentUser = "";
                        }
                        if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                            if(rs[0][0].PKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD
                                || rs[0][0].PKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_REMOTE_SIGNING)
                            {
                                sDisableAgentUser = "disabled";
                            }
                        }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleBranchName" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="AGENT_NAME" id="AGENT_NAME" class="form-control123"
                                onchange="LOAD_BACKOFFICE_USER(this.value, '<%= anticsrf%>');" <%= sDisableAgentUser%>>
                                <%
                                    String sBranchFirst = String.valueOf(rs[0][0].BRANCH_ID);
                                    try {
                                        BRANCH[][] rst = (BRANCH[][]) session.getAttribute("sessTreeBranchSystemAgency");
                                        if (rst[0].length > 0) {
                                            for (BRANCH temp1 : rst[0]) {
                                                if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                                %>
                                <option value="<%=String.valueOf(temp1.ID)%>" <%= String.valueOf(temp1.ID).equals(sBranchFirst) ? "selected" : "" %>><%=temp1.NAME + " - " + temp1.REMARK%></option>
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
                            </select>
                        </div>
                    </div>
                    <script>$("#idLblTitleBranchName").text(global_fm_Branch + ' (' + ca_fm_Cert_01 + ')');</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="USER" id="USER" class="form-control123" <%= sDisableAgentUser%>>
                                <%
                                    BACKOFFICE_USER[][] rssUser;
                                    if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                        rssUser = new BACKOFFICE_USER[1][];
                                        db.S_BO_GET_USER_BRANCH_ALL(sBranchFirst, rssUser);
                                        if (rssUser[0].length > 0) {
                                            for (int i = 0; i < rssUser[0].length; i++) {
                                %>
                                <option value="<%=String.valueOf(rssUser[0][i].ID)%>" <%= rssUser[0][i].ID == rs[0][0].CREATED_BY_ID ? "selected" : "" %>><%=rssUser[0][i].FULL_NAME + " (" + rssUser[0][i].USERNAME + ")" %></option>
                                <%
                                            }
                                        }
                                    } else {
                                        if(SessUserAgentID.equals(sBranchFirst)) {
                                            rssUser = new BACKOFFICE_USER[1][];
                                            db.S_BO_USER_DETAIL(sessUserID, sessLanguageGlobal, rssUser);
                                            if(rssUser[0].length > 0) {
                                %>
                                <option value="<%=String.valueOf(rssUser[0][0].ID)%>" <%= rssUser[0][0].ID == rs[0][0].CREATED_BY_ID ? "selected" : "" %>><%=rssUser[0][0].FULL_NAME + " (" + rssUser[0][0].USERNAME + ")" %></option>
                                <%
                                            }
                                        } else {
                                            rssUser = new BACKOFFICE_USER[1][];
                                            db.S_BO_GET_USER_BRANCH_ALL(sBranchFirst, rssUser);
                                            if (rssUser[0].length > 0) {
                                                for (int i = 0; i < rssUser[0].length; i++) {
                                %>
                                <option value="<%=String.valueOf(rssUser[0][i].ID)%>" <%= rssUser[0][i].ID == rs[0][0].CREATED_BY_ID ? "selected" : "" %>><%=rssUser[0][i].FULL_NAME + " (" + rssUser[0][i].USERNAME + ")" %></option>
                                <%
                                                }
                                            }
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <script>$("#idLblTitleUser").text(global_fm_user_create);</script>
                </div>
                <script>
                    function LOAD_BACKOFFICE_USER(objAgency, idCSRF)
                    {
                        $.ajax({
                            type: "post",
                            url: "../JSONCommon",
                            data: {
                                idParam: 'loadadminuser_ofagency',
                                BRANCH_ID: objAgency,
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html)
                            {
                                if (html.length > 0)
                                {
                                    var cbxUSER = document.getElementById("USER");
                                    removeOptions(cbxUSER);
                                    var obj = JSON.parse(html);
                                    if (obj[0].Code === "0")
                                    {
                                        for (var i = 0; i < obj.length; i++) {
                                            cbxUSER.options[cbxUSER.options.length] = new Option(obj[i].FULL_NAME + " (" + obj[i].USERNAME + ")", obj[i].ID);
                                        }
                                    }
                                    else if (obj[0].Code === JS_EX_CSRF)
                                    {
                                        funCsrfAlert();
                                    }
                                    else if (obj[0].Code === JS_EX_LOGIN)
                                    {
                                        RedirectPageLoginNoSess(global_alert_login);
                                    }
                                    else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                    {
                                        RedirectPageLoginNoSess(global_alert_another_login);
                                    }
                                    else if (obj[0].Code === "1")
                                    {
                                        cbxUSER.options[cbxUSER.options.length] = new Option("---", "");
                                    }
                                    else {
                                        funErrorAlert(global_errorsql);
                                    }
                                }
                            }
                        });
                        return false;
                    }
                </script>
                <%
                    } else {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleAgentName" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="BranchOfficeName" id="BranchOfficeName" value="<%= EscapeUtils.CheckTextNull(rs[0][0].BRANCH_DESC)%>" class="form-control123"/>
                            <input type="text" name="AGENT_NAME" id="AGENT_NAME" disabled style="display: none;" value="<%= String.valueOf(rs[0][0].BRANCH_ID)%>">
                        </div>
                    </div>
                    <script>$("#idLblTitleAgentName").text(global_fm_Branch + ' (' + ca_fm_Cert_01 + ')');</script>
                </div>
                <%
                    if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                        if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT) {
                            sDisableAgentUser = "";
                        }
                        if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                            if(rs[0][0].PKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD
                                || rs[0][0].PKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_REMOTE_SIGNING)
                            {
                                sDisableAgentUser = "disabled";
                            }
                        }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="USER" id="USER" class="form-control123" <%= sDisableAgentUser%>>
                                <%
                                    BACKOFFICE_USER[][] rssUser = new BACKOFFICE_USER[1][];
                                    db.S_BO_GET_USER_BRANCH_ALL(EscapeUtils.CheckTextNull(String.valueOf(rs[0][0].BRANCH_ID)), rssUser);
                                    if (rssUser[0].length > 0) {
                                        for (int i = 0; i < rssUser[0].length; i++) {
                                %>
                                <option value="<%=String.valueOf(rssUser[0][i].ID)%>" <%= rssUser[0][i].ID == rs[0][0].CREATED_BY_ID ? "selected" : "" %>><%=rssUser[0][i].FULL_NAME + " (" + rssUser[0][i].USERNAME + ")" %></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <script>$("#idLblTitleUser").text(global_fm_user_create);</script>
                </div>
                <%
                    } else {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="CREATED_BY" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].CREATED_BY) %>"/>
                            <input class="form-control123" type="text" name="USER" id="USER" disabled style="display: none;" value="<%= String.valueOf(rs[0][0].CREATED_BY_ID)%>"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleUser").text(global_fm_user_create);</script>
                </div>
                <%
                    }
                %>
                <%
                    }
                %>
                <%
                    } else {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleAgentName" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="BranchOfficeName" id="BranchOfficeName" value="<%= EscapeUtils.CheckTextNull(rs[0][0].BRANCH_DESC)%>" class="form-control123"/>
                            <input type="text" name="AGENT_NAME" id="AGENT_NAME" disabled style="display: none;" value="<%= String.valueOf(rs[0][0].BRANCH_ID) %>">
                        </div>
                    </div>
                    <script>$("#idLblTitleAgentName").text(global_fm_Branch + ' (' + ca_fm_Cert_01 + ')');</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="CREATED_BY" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].CREATED_BY) %>"/>
                            <input class="form-control123" type="text" name="USER" id="USER" disabled style="display: none;" value="<%= String.valueOf(rs[0][0].CREATED_BY_ID)%>"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleUser").text(global_fm_user_create);</script>
                </div>
                <%
                    }
                %>
                <%
                    String sViewOtherRSSP = "";
                    if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD
                        || intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_REMOTE_SIGNING)
                    {
                        sViewOtherRSSP = "none";
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;clear: both;display: <%= sViewOtherRSSP%>">
                    <div class="form-group">
                        <label id="idLblTitlePushNotice" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="PUSH_NOTICE_ENABLED">
                                <input TYPE="checkbox" <%=rs[0][0].PUSH_NOTICE_ENABLED ? "checked" : ""%> id="PUSH_NOTICE_ENABLED" name="PUSH_NOTICE_ENABLED" <%= sDisabledEmailInfo_Reissue%> />
                                <div class="slider round <%= sDisabledEmailInfo_Reissue%>"></div>
                            </label>
                            <input type="hidden" readonly value="<%= strPUSH_NOTICE_ENABLED%>" id="HIDDEN_PUSH_NOTICE_ENABLED">
                        </div>
                    </div>
                    <script>$("#idLblTitlePushNotice").text(cert_fm_push_notice);</script>
                </div>
                <%
                    CERTIFICATION_POLICY_DATA[][] resCollectData =null;
                    boolean booCheckSoftCopy = false;
                    String sReceivedNote = "";
                    String idViewSoftCopy = "none";
                    int iSTATE_PROFILE = 0;
                    if("2".equals(profileManagerCAOption) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                        idViewSoftCopy = "";
                        try {
                            CERTIFICATION[][] rsBrief = new CERTIFICATION[1][];
                            db.S_BO_CERTIFICATION_BRIEF_DETAIL(String.valueOf(rs[0][0].ID), rsBrief);
                            String sBRIEF_PROPERTIES = "";
                            if(rsBrief[0].length > 0) {
                                iSTATE_PROFILE = rsBrief[0][0].FILE_MANAGER_STATE_ID;
                                sBRIEF_PROPERTIES = EscapeUtils.CheckTextNull(rsBrief[0][0].BRIEF_PROPERTIES);
                                booCheckSoftCopy = rsBrief[0][0].COLLECT_SOFTCOPY;
                                if(!"".equals(sBRIEF_PROPERTIES)) {
                                    CERTIFICATION_POLICY_DATA[][] resIPData = new CERTIFICATION_POLICY_DATA[1][];
                                    CommonFunction.getCollectedBriefProperties(sBRIEF_PROPERTIES, resIPData);
                                    if(resIPData[0].length > 0) {
                                        request.getSession(false).setAttribute("SessCollectedBriefPro", resIPData);
                                        for(CERTIFICATION_POLICY_DATA resIPData1 : resIPData[0]) {
                                            if(resIPData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_COLLECT_COMMENT)) {
                                                sReceivedNote = EscapeUtils.CheckTextNull(resIPData1.name);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            resCollectData = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessCollectedBriefPro");
                        } catch(Exception e){CommonFunction.LogExceptionServlet(null, "CERTIFICATION_POLICY_DATA: " + e.getMessage(), e);}
                    }
                %>
                <%
                    String sViewKeepCertSN = "none";
                    if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                        || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO)
                    {
                        sViewKeepCertSN = "";
                    }
                    if("none".equals(sViewOtherRSSP)) {
                        sViewKeepCertSN = sViewOtherRSSP;
                    }
                    if("0".equals(boViewKeepCertSN)) {
                        sViewKeepCertSN = "none";
                    }
                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)){
                        sViewKeepCertSN = "none";
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0; display: <%= sViewKeepCertSN%>">
                    <div class="form-group">
                        <label id="idLblTitleKeepCertSN" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="idCheckKeepCertSN">
                                <input type="checkbox" name="idCheckKeepCertSN" id="idCheckKeepCertSN"
                                    <%= booKeepCertSNEnabled == true ? "checked" : "" %> <%= sDisabledKeepCertSN%>/>
                                <div id="idCheckKeepCertSNlass" class="slider round <%= sDisabledKeepCertSN%>"></div>
                            </label>
                        </div>
                    </div>
                    <script>$("#idLblTitleKeepCertSN").text(regiscert_fm_keep_certsn);</script>
                </div>
                <%
                    if(intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE
                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED
                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVISED_KEEP_SN
                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED_KEEP_SN)
                    {
                        String strViewCertConfirm = "display:none";
                        if("1".equals(isActiveSignServer)) {
                            if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                    if (rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                        || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT) {
                                        if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                            || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                            || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)) {
                                            strViewCertConfirm = "";
                                        } else {
                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                strViewCertConfirm = "";
                                            }
                                        }
                                    } else if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED) {
                                        if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_USER) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ACCOUNTANT)) {
                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                strViewCertConfirm = "";
                                            }
                                        } else {
                                            strViewCertConfirm = "";
                                        }
                                    }
                                }
                            }
                        }
                        if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                            || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                            || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                        {
                            String sShowOnChangeKey = "0";
                            boolean isPRIVATE_KEY_ENABLED_CertOld= false;
                            int intPAST_CERTIFICATION_ID = rs[0][0].PAST_CERTIFICATION_ID;
                            CERTIFICATION[][] rsCERT = new CERTIFICATION[1][];
                            db.S_BO_CERTIFICATION_DETAIL(String.valueOf(intPAST_CERTIFICATION_ID), sessLanguageGlobal, rsCERT);
                            if(rsCERT[0].length > 0)
                            {
                                isPRIVATE_KEY_ENABLED_CertOld = rsCERT[0][0].PRIVATE_KEY_ENABLED;
                            }
                            String sPRIVATE_KEY_ENABLED_CertOld = isPRIVATE_KEY_ENABLED_CertOld == true ? "1" : "0";

                            if(CommonFunction.checkHardTokenIDEnabled(intPKI_FORMFACTOR_ID) == true) {
                                if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                    || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                    sShowOnChangeKey = "1";
                                }
                                if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                                    sShowOnChangeKey = "2";
                                }
                                String sChangeKeyEnabled = booChangeKeyEnabled == true ? "1" : "0";
                %>
                <script>
                    var isPRIVATE_KEY_ENABLED_Old = '<%= sPRIVATE_KEY_ENABLED_CertOld%>';
                    $(document).ready(function () {
                        if('<%= sChangeKeyEnabled%>' === "0")
                        {
                            if(isPRIVATE_KEY_ENABLED_Old === "0")
                            {
                                $("#idCheckBackUpKey").attr("disabled", true);
                                $("#idCheckBackUpKeyClass").addClass("disabled");
                                $('#idCheckBackUpKey').prop('checked', false);
                            }
                            localStorage.setItem("sessDeleteWhenRevokeHardToken", "1");
                        }
                        if('<%=sShowOnChangeKey%>' === "2")
                        {
                            if(isPRIVATE_KEY_ENABLED_Old === "0") {
                                $("#idCheckChangeKey").attr("disabled", true);
                                $("#idCheckChangeKeylass").addClass("disabled");
                                $('#idCheckRevoked').prop('checked', true);
                                $("#idCheckRevoked").attr("disabled", true);
                                $("#idCheckRevokedClass").addClass("disabled");
                                localStorage.setItem("loadChangeKeyOption", "0");
                            } else {
                                if('<%= sChangeKeyEnabled%>' === "0") {
                                    localStorage.setItem("loadChangeKeyOption", "0");
                                } else {
                                    localStorage.setItem("loadChangeKeyOption", "1");
                                }
                            }
                        }
                    });
                    function onChangeKey()
                    {
                        if ($("#idCheckChangeKey").is(':checked'))
                        {
                            $("#idCheckBackUpKey").attr("disabled", false);
                            $("#idCheckBackUpKeyClass").removeClass("disabled");
                            if('<%= SessAgentID%>' === JS_STR_AGENCY_ROOT_CA)
                            {
                                $("#idDeleteWhenRevokeHardToken").css("display", "");
                                $('#FO_DELETE_CERT_WHEN_REVOKE').prop('checked', true);
                            }
                            if('<%=sShowOnChangeKey%>' === "2"){
                                $('#idCheckRevoked').prop('checked', true);
                                $("#idCheckRevoked").attr("disabled", false);
                                $("#idCheckRevokedClass").removeClass("disabled");
                                localStorage.setItem("loadChangeKeyOption", "1");
                                console.log("onChangeKey ProfileID:" + $("#idHiddenCerDurationOrProfileID").val());
                                LoadFormDNForPersonalCommon($("#idHiddenCerDurationOrProfileID").val(), "");
                            }
                        } else {
                            $("#idCheckBackUpKey").attr("disabled", true);
                            $("#idCheckBackUpKeyClass").addClass("disabled");
                            if('<%=sShowOnChangeKey%>' === "1")
                            {
                                if(isPRIVATE_KEY_ENABLED_Old === "0") {
                                    $("#idCheckBackUpKey").attr("disabled", true);
                                    $("#idCheckBackUpKeyClass").addClass("disabled");
                                    $('#idCheckBackUpKey').prop('checked', false);
                                } else if(isPRIVATE_KEY_ENABLED_Old === "1") {
                                    $("#idCheckBackUpKey").attr("disabled", false);
                                    $("#idCheckBackUpKeyClass").removeClass("disabled");
                                    $('#idCheckBackUpKey').prop('checked', true);
                                }
                            } else if('<%=sShowOnChangeKey%>' === "2")
                            {
                                if(isPRIVATE_KEY_ENABLED_Old === "0") {
                                    $("#idCheckBackUpKey").attr("disabled", true);
                                    $("#idCheckBackUpKeyClass").addClass("disabled");
                                    $('#idCheckBackUpKey').prop('checked', true);
                                } else if(isPRIVATE_KEY_ENABLED_Old === "1") {
                                    $("#idCheckBackUpKey").attr("disabled", false);
                                    $("#idCheckBackUpKeyClass").removeClass("disabled");
                                    $('#idCheckBackUpKey').prop('checked', true);
                                }
                                $('#idCheckRevoked').prop('checked', false);
                                $("#idCheckRevoked").attr("disabled", true);
                                $("#idCheckRevokedClass").addClass("disabled");
                                localStorage.setItem("loadChangeKeyOption", "0");
                                console.log("onChangeKey ProfileID:" + $("#idHiddenCerDurationOrProfileID").val());
                                LoadFormDNForPersonalCommon($("#idHiddenCerDurationOrProfileID").val(), "");
                            }
                            $("#idDeleteWhenRevokeHardToken").css("display", "none");
                            $('#FO_DELETE_CERT_WHEN_REVOKE').prop('checked', false);
                        }
                    }
                </script>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleChangeKey" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="idCheckChangeKey">
                                <input type="checkbox" name="idCheckChangeKey" id="idCheckChangeKey" onclick="onChangeKey();"
                                    <%= booChangeKeyEnabled == true ? "checked" : "" %> <%= sDisabledChangeKey%>/>
                                <div id="idCheckChangeKeylass" class="slider round <%= sDisabledChangeKey%>"></div>
                            </label>
                        </div>
                    </div>
                    <script>$("#idLblTitleChangeKey").text(regiscert_fm_check_change_key);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0; display: <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) || (isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) && !SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) ? "none" : ""%>">
                    <div class="form-group">
                        <label id="idLblTitleBackUpKey" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="idCheckBackUpKey">
                                <input type="checkbox" name="idCheckBackUpKey" id="idCheckBackUpKey"
                                    <%= rs[0][0].PRIVATE_KEY_ENABLED ? "checked" : "" %> <%= sDisabledBackUpKey%>/>
                                <div id="idCheckBackUpKeyClass" class="slider round <%= sDisabledBackUpKey%>"></div>
                            </label>
                        </div>
                    </div>
                    <script>$("#idLblTitleBackUpKey").text(regiscert_fm_check_backup_key);</script>
                </div>
                <%
                        } else if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                            if(rs[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                || rs[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL
                                || rs[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE)
                            {
                                if(rs[0][0].PRIVATE_KEY_ENABLED == true) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleChangeKey" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="idCheckChangeKey">
                                <input type="checkbox" name="idCheckChangeKey" id="idCheckChangeKey" onclick="onChangeKey();"
                                    <%= booChangeKeyEnabled == true ? "checked" : "" %>/>
                                <div id="idCheckChangeKeylass" class="slider round"></div>
                            </label>
                        </div>
                    </div>
                    <script>$("#idLblTitleChangeKey").text(regiscert_fm_check_change_key);</script>
                </div>
                <input type="checkbox" style="display: none;" name="idCheckBackUpKey" id="idCheckBackUpKey" checked />
                <%
                    } else {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleChangeKey" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="idCheckChangeKey">
                                <input type="checkbox" name="idCheckChangeKey" id="idCheckChangeKey" onclick="onChangeKeySoftToken();"
                                    <%= booChangeKeyEnabled == true ? "checked" : "" %> />
                                <div id="idCheckChangeKeylass" class="slider round"></div>
                            </label>
                        </div>
                    </div>
                    <script>$("#idLblTitleChangeKey").text(regiscert_fm_check_change_key);</script>
                </div>
                <div class="col-sm-13" style="padding-left: 0;clear: both;display: <%= booChangeKeyEnabled == true ? "" : "none"%>" id="idDivViewCSRSoftToken">
                    <div class="form-group">
                        <textarea id="idCSR" class="form-control123" readonly="true" name="idCSR" style="height: 85px;display: none;">
                            <%= EscapeUtils.CheckTextNull(rs[0][0].CSR) %>
                        </textarea>
                        <label class="control-label123" id="idLblTitleCSR"></label>
                        <INPUT class="form-control123" id="input-file-csr" NAME="input-file-csr" accept=".csr,.txt"
                            TYPE="file" onchange="UploadCSR(this);" />
                        <script>$("#idLblTitleCSR").text(token_fm_csr);</script>
                    </div>
                </div>
                <script>
                    function onChangeKeySoftToken()
                    {
                        if ($("#idCheckChangeKey").is(':checked'))
                        {
                            $("#idDivViewCSRSoftToken").css("display", "");
                        } else {
                            $("#input-file-csr").val("");
                            $("#idDivViewCSRSoftToken").css("display", "none");
                        }
                    }
                    function UploadCSR(input1)
                    {
                        if (input1.value !== '')
                        {
                            if (JSCheckExtenBrowseCSR(input1.value))
                            {
                                $('body').append('<div id="over"></div>');
                                $(".loading-gif").show();
                                file1 = input1.files[0];
                                var data1 = new FormData();
                                data1.append('file', file1);
                                $.ajax({
                                    url: "../UploadFile",
                                    data: data1,
                                    cache: false,
                                    contentType: false,
                                    processData: false,
                                    type: 'POST',
                                    enctype: "multipart/form-data",
                                    success: function (html) {
                                        var myStrings = sSpace(html).split('###');
                                        if (myStrings[0] === "0")
                                        {
                                            $("textarea#idCSR").val(myStrings[1]);
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
                            }
                            else
                            {
                                funErrorAlert(global_req_csr_format);
                            }
                        }
                        else
                        {
                            funErrorAlert(global_req_file);
                        }
                    }
                </script>
                <input type="checkbox" style="display: none;" name="idCheckBackUpKey" id="idCheckBackUpKey" />
                <%
                                }
                            } else {
                %>
                <input type="checkbox" style="display: none;" name="idCheckChangeKey" id="idCheckChangeKey" checked />
                <input type="checkbox" style="display: none;" name="idCheckBackUpKey" id="idCheckBackUpKey" checked />
                <%
                            }
                %>
                <div class="col-sm-6" style="padding-left: 0; <%=strViewCertConfirm%>">
                    <div class="form-group">
                        <label id="idLblTitleCertConfirm" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="idCheckCertConfirm">
                                <input type="checkbox" name="idCheckCertConfirm" <%= "1".equals(isActiveSignServer) ? "checked" : "" %> id="idCheckCertConfirm" />
                                <div id="idCheckCertConfirmlass" class="slider round"></div>
                            </label>
                        </div>
                    </div>
                    <script>$("#idLblTitleCertConfirm").text(approve_fm_confirm_mail);</script>
                </div>
                <%            
                        } else if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                %>
                <input type="checkbox" style="display: none;" name="idCheckChangeKey" id="idCheckChangeKey" <%= booChangeKeyEnabled == true ? "checked" : "" %> />
                <input type="checkbox" style="display: none;" name="idCheckBackUpKey" id="idCheckBackUpKey" <%= rs[0][0].PRIVATE_KEY_ENABLED ? "checked" : "" %> />
                <%
                        } else {
                %>
                <input type="checkbox" style="display: none;" name="idCheckChangeKey" id="idCheckChangeKey" checked />
                <input type="checkbox" style="display: none;" name="idCheckBackUpKey" id="idCheckBackUpKey" checked />
                <%
                        }
                    } else if (intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                        || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE)
                    {
                        String sDivViewbackupKey = "";
                        if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                        {
                            if(rs[0][0].PRIVATE_KEY_ENABLED == false)
                            {
                                sDivViewbackupKey = "none";
                            }
                        }
                        if("none".equals(sViewOtherRSSP)){sDivViewbackupKey = sViewOtherRSSP;}
                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) || (isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) && !SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))){
                            sDivViewbackupKey = "none";
                        }
                %>
                <input type="checkbox" style="display: none;" name="idCheckChangeKey" id="idCheckChangeKey" checked />
                <div class="col-sm-6" style="padding-left: 0;display: <%= sDivViewbackupKey%>">
                    <div class="form-group">
                        <label id="idLblTitleBackUpKey" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="idCheckBackUpKey">
                                <input type="checkbox" name="idCheckBackUpKey" id="idCheckBackUpKey"
                                    <%= rs[0][0].PRIVATE_KEY_ENABLED ? "checked" : "" %> <%= sDisabledBackUpKey%>/>
                                <div id="idCheckBackUpKeyClass" class="slider round <%= sDisabledBackUpKey%>"></div>
                            </label>
                        </div>
                    </div>
                    <script>$("#idLblTitleBackUpKey").text(regiscert_fm_check_backup_key);</script>
                </div>
                <textarea id="idCSR" class="form-control123" readonly="true" name="idCSR" style="height: 85px;display: none;">
                    <%= EscapeUtils.CheckTextNull(rs[0][0].CSR) %>
                </textarea>
                <div class="col-sm-6" style="padding-left: 0; <%=strViewCertConfirm%>">
                    <div class="form-group">
                        <label id="idLblTitleCertConfirm" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="idCheckCertConfirm">
                                <input type="checkbox" name="idCheckCertConfirm" <%= "1".equals(isActiveSignServer) ? "checked" : "" %> id="idCheckCertConfirm" />
                                <div id="idCheckCertConfirmlass" class="slider round"></div>
                            </label>
                        </div>
                    </div>
                    <script>$("#idLblTitleCertConfirm").text(approve_fm_confirm_mail);</script>
                </div>
                <%
                    }
                    if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                        || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                    {
                        String sDisabledRevokeEnabled = "";
                        if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO){
                            String sPERSONAL_ID_NEW = rs[0][0].PERSONAL_ID;
                            String sENTERPRISE_ID_NEW = rs[0][0].ENTERPRISE_ID;
                            CERTIFICATION[][] rsCERTOld = new CERTIFICATION[1][];
                            db.S_BO_CERTIFICATION_DETAIL(String.valueOf(rs[0][0].PAST_CERTIFICATION_ID), sessLanguageGlobal, rsCERTOld);
                            if(rsCERTOld[0].length > 0) {
                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_CMC)) {
                                    if(!rsCERTOld[0][0].PERSONAL_ID.equals(sPERSONAL_ID_NEW) || !rsCERTOld[0][0].ENTERPRISE_ID.equals(sENTERPRISE_ID_NEW)) {
                                        sDisabledRevokeEnabled = "disabled";
                                    }
                                }
                            }
                        }
                        
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleRevoked" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="idCheckRevoked">
                                <input type="checkbox" name="idCheckRevoked" id="idCheckRevoked"
                                    <%= booRevokedEnabled == true ? "checked" : "" %> <%= sDisabledRevokeEnabled%>/>
                                <div id="idCheckRevokedClass" class="slider round <%= sDisabledRevokeEnabled%>"></div>
                            </label>
                        </div>
                    </div>
                    <script>$("#idLblTitleRevoked").text(regiscert_fm_check_revoke);</script>
                </div>
                <%
                        }
                    }
                %>
                <%
                    String sViewDeleteWhenRevoke = "none";
                    String sDisabledDeleteWhenRevoke = "disabled";
                    if(CommonFunction.checkHardTokenIDEnabled(intPKI_FORMFACTOR_ID) == true)
                    {
                        if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED
                            && (intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                            || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                            || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO))
                        {
                            sViewDeleteWhenRevoke = "";
                            sDisabledDeleteWhenRevoke = "";
                        } else if((rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_GENERATED
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ERROR_ISSUED
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_COMMITED)
                            && (intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                            || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                            || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO))
                        {
                            sViewDeleteWhenRevoke = "";
                            if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                            {
                                pFO_DELETE_CERT_WHEN_REVOKE = sDeleteOldCertRevokeValue == true ? "1" : "0";
                            } else {
                                pFO_DELETE_CERT_WHEN_REVOKE = sDeleteOldCertChangeValue == true ? "1" : "0";
                            }
                        } else {
                            
                        }
                        if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED){
                            if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO){
                                pFO_DELETE_CERT_WHEN_REVOKE = sDeleteOldCertChangeValue == true ? "1" : "0";
                            }
                        }
                    }
                %>
                <div class="col-sm-6" id="idDeleteWhenRevokeHardToken" style="padding-left: 0;display: <%= sViewDeleteWhenRevoke%>">
                    <div class="form-group">
                        <label id="idLblTitleCertDeleteRevoke" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="FO_DELETE_CERT_WHEN_REVOKE">
                                <input TYPE="checkbox" <%= "1".equals(pFO_DELETE_CERT_WHEN_REVOKE) ? "checked" : ""%> id="FO_DELETE_CERT_WHEN_REVOKE" name="FO_DELETE_CERT_WHEN_REVOKE" <%= sDisabledDeleteWhenRevoke%> />
                                <div class="slider round <%= sDisabledDeleteWhenRevoke%>"></div>
                            </label>
                        </div>
                    </div>
                    <%
                        if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                        {
                    %>
                    <script>$("#idLblTitleCertDeleteRevoke").text(cert_fm_revoke_delete);</script>
                    <%
                        } else {
                    %>
                    <script>$("#idLblTitleCertDeleteRevoke").text(cert_fm_revoke_delete_old);</script>
                    <%
                        }
                    %>
                    <script>
                        $(document).ready(function () {
                            if(localStorage.getItem("sessDeleteWhenRevokeHardToken") === "1")
                            {
                                $("#idDeleteWhenRevokeHardToken").css("display", "none");
                                localStorage.setItem("sessDeleteWhenRevokeHardToken", null);
                            }
                        });
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;display: <%=idViewSoftCopy%>">
                    <div class="form-group">
                        <label id="idLblTitleSoftCopy" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="idReceivedSoftCopy" style="margin-bottom: 0px;clear: both;">
                                <input type="checkbox" name="idReceivedSoftCopy" id="idReceivedSoftCopy"
                                    <%= booCheckSoftCopy ? "checked" : ""%> <%= sDisabledStateProfile %>/>
                                <div id="idReceivedSoftCopyClass" class="slider round <%= sDisabledStateProfile %>"></div>
                            </label>
                        </div>
                        <input type="text" name="idReceivedNote" id="idReceivedNote" value="<%= sReceivedNote%>" style="display: none;"/>
                    </div>
                    <script>$("#idLblTitleSoftCopy").text(global_fm_soft_copy);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;display: <%=idViewSoftCopy%>">
                    <div class="form-group">
                        <label id="idLblTitleStateProfile" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="STATE_PROFILE" id="STATE_PROFILE" class="form-control123" <%= sDisabledStateProfile%>>
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
                    <script>$("#idLblTitleStateProfile").text(global_fm_status_profile);</script>
                </div>
                <%
                    String sDownCSR = "display: none";
                    if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                        if(rs[0][0].PRIVATE_KEY_ENABLED == false && !"".equals(EscapeUtils.CheckTextNull(rs[0][0].CSR))) {
                            if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                            {
                                sDownCSR = "";
                            }
                        }
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;<%=sDownCSR%>">
                    <div class="form-group">
                        <label id="idLblTitleDownCSR" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <a onclick="downloadCSR('<%= String.valueOf(rs[0][0].ID)%>', '<%= anticsrf%>');" id="idLblActionDownCSR" class="btn btn-info btn-xs" style="cursor: pointer;"></a>
                        </div>
                    </div>
                    <script>
                        $("#idLblActionDownCSR").text(global_fm_down);
                        $("#idLblTitleDownCSR").text(cert_title_register_csr);
                        function downloadCSR(id, idCSRF){
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../DownloadFileCSR",
                                data: {
                                    idParam: 'csrhasid',
                                    id: id,
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
                    </script>
                </div>
                <input id="idHiddenCerCA" value="<%= sFristCA%>" style="display: none;"/>
                <input id="idHiddenCerCoreSubject" value="<%= sCACoreSubject%>" style="display: none;"/>
                <input id="idHiddenCerPurpose" value="<%= sFristCerPurpose%>" style="display: none;"/>
                <input id="idHiddenCerFactor" value="<%= String.valueOf(intPKI_FORMFACTOR_ID) %>" style="display: none;"/>
                <input id="idHiddenCerDurationOrProfileID" value="<%= sFristCerDurationOrProfileID%>" style="display: none;" />
                <script>
                    $(document).ready(function () {
                        if('<%= sFristCA%>' !== "" && '<%= sFristCerPurpose%>' !== "" && '<%= sFristCerDurationOrProfileID%>' !== ""
                             && '<%= String.valueOf(intPKI_FORMFACTOR_ID)%>' !== "")
                        {
                            LOAD_CERTIFICATION_PROFILE('<%= sFristCerDurationOrProfileID%>', "1");
                        }
                    });
                </script>
                <%
                    String agentUploadFile = cogCommon.GetPropertybyCode(Definitions.CONFIG_AGENCY_UPLOADFILE_APPROVE_ENABLED);
                    boolean isShowFileManager = true;
                    if(isShowFileManager == true)
                    {
                        boolean isHasFileOfUser = true;
                        FILE_MANAGER[][] rsFileMana = new FILE_MANAGER[1][];
                        db.S_BO_FILE_MANAGER_GET_BY_CERTIFICATION_AND_OWNER(String.valueOf(rs[0][0].ID), sOWNER_ID, sessLanguageGlobal, rsFileMana);
                        if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            boolean isCheckFileAgent = true;
                            if("1".equals(agentUploadFile)) {
                                if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                    isCheckFileAgent = false;
                                }
                            }
                            if(isCheckFileAgent == true) {
                                if(rsFileMana[0].length <= 0)
                                {
                                    isHasFileOfUser = false;
                                } else {
                                    for(FILE_MANAGER rsFile : rsFileMana[0])
                                    {
                                        isHasFileOfUser = false;
                                        if(!rsFile.FILE_PROFILE_NAME.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT))
                                        {
                                            isHasFileOfUser = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_DECLINED
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_COMMITED
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ERROR_ISSUED
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_GENERATED)
                        {
                            if(rsFileMana[0].length <= 0)
                            {
                                isHasFileOfUser = false;
                            } else {
                                for(FILE_MANAGER rsFile : rsFileMana[0])
                                {
                                    isHasFileOfUser = false;
                                    if(!rsFile.FILE_PROFILE_NAME.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT))
                                    {
                                        isHasFileOfUser = true;
                                        break;
                                    }
                                }
                            }
                        }
                %>
                <%
                    boolean attrTypeAccespFile = false;
                    if(rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                        || rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                        || rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                        || rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                        || rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                    {
                        attrTypeAccespFile = true;
                    } else {
                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_EFY) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO)
                            || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                            if(rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                                attrTypeAccespFile = true;
                            }
                        }
                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                            if(rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                || rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE) {
                                attrTypeAccespFile = true;
                            }
                        }
                    }
                    if(attrTypeAccespFile == true && isHasFileOfUser == true)
                    {
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
                        SessionUploadFileCert cartIP = (SessionUploadFileCert) session.getAttribute("sessUploadFileCert");
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
                                cartIP.AddRoleFunctionsList(item);
                            }
                            session.setAttribute("sessUploadFileCert", cartIP);
                        }
                        boolean isEnableUploadFile = false;
                        if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED)
                        {
                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                            {
                                isEnableUploadFile = true;
                            } else {
                                if("1".equals(agentUploadFile)) {
                                    if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                        isEnableUploadFile = true;
                                    }
                                }
                            }
                        }
                        ObjectMapper oMapperParse = new ObjectMapper();
                        FILE_PROFILE_JSON itemParsePush = oMapperParse.readValue(sJSON, FILE_PROFILE_JSON.class);
                        int jFile = 1;
                        if(itemParsePush != null) {
                            try {
                                for (FILE_PROFILE_JSON.Attribute attribute : itemParsePush.getAttributes()) {
                                    String sViewCheckTypeFile = "none";
                                    String sCheckDisabled = "disabled";
                                    if("2".equals(profileManagerCAOption) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO)
                                         || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                        if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                            sViewCheckTypeFile = "";
                                            if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                                                || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED) {
                                                sCheckDisabled = "";
                                            }
                                        }
                                    }
                                    String sName = attribute.getName().trim();
                                    boolean booCheckCollect = CommonFunction.checkBriefFileType(sName, resCollectData);
                                    boolean booScanCollect = CommonFunction.checkBriefFileScan(sName, resCollectData);
                                    String sRemark = attribute.getRemark().trim();
                                    if(!sName.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT)) {
                                        if(sName.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT))
                                        {
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
                            if(sName.equals(Definitions.CONFIG_FILE_PROFILE_SERVICE_REGISTRATION_DOCUMENT) && isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                CERTIFICATION[][] rsBrief = new CERTIFICATION[1][];
                                db.S_BO_CERTIFICATION_BRIEF_DETAIL(String.valueOf(rs[0][0].ID), rsBrief);
                                int iBUSINESS_LICENSE_TYPE_ID = 0;
                                if(rsBrief[0].length > 0) {
                                    iBUSINESS_LICENSE_TYPE_ID = rsBrief[0][0].BUSINESS_LICENSE_TYPE_ID;
                                }
                                session.setAttribute("sessProfileStateDK01", String.valueOf(iBUSINESS_LICENSE_TYPE_ID));
                        %>
                        <div>
                            <label id="idLblTitleStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>"></label>&nbsp;
                            <label class="switch" for="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>" style="margin-bottom: 0px;clear: both;">
                                <input type="checkbox" name="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>" id="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>"
                                    onclick="onChangeStateDK01('idCheckStateDK01'+'<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>', '<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>');"
                                    <%= iBUSINESS_LICENSE_TYPE_ID == Definitions.CONFIG_BRIEF_TYPE_ID_SCAN ? "checked" : ""%> <%=sCheckDisabled%>/>
                                <div id="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>Class" class="slider round <%=sCheckDisabled%>"></div>
                            </label>
                            <script>$("#idLblTitleStateDK01"+'<%=Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN%>').text(global_fm_profile_scan);</script>
                            
                            <label id="idLblTitleStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>"></label>&nbsp;
                            <label class="switch" for="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>" style="margin-bottom: 0px;clear: both;">
                                <input type="checkbox" name="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>" id="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>"
                                    onclick="onChangeStateDK01('idCheckStateDK01'+'<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>', '<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>');"
                                    <%= iBUSINESS_LICENSE_TYPE_ID == Definitions.CONFIG_BRIEF_TYPE_ID_DIGITAL_SIGNATURE ? "checked" : ""%> <%=sCheckDisabled%>/>
                                <div id="idCheckStateDK01<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>Class" class="slider round <%=sCheckDisabled%>"></div>
                            </label>
                            <script>$("#idLblTitleStateDK01"+'<%=Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE%>').text(global_fm_profile_signature);</script>
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
                            <script>
                                $("#idLblTitleScan"+'<%=sName%>').text(global_fm_scan_valid);
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
                        </div>
                    </legend>
                    <%
                        String cssDelete ="";
                        String cssDown ="";
                        if(isEnableUploadFile == true) {
                            String sClassFileLabel = "col-sm-1";
                            String sClassFileUp = "col-sm-11";
                            if("1".equals(sRepresentEnabled)) {
                                sClassFileLabel = "col-sm-3";
                                sClassFileUp = "col-sm-9";
                                cssDelete = "text-align: center;width: 80px;";
                                cssDown = "text-align: center;width: 80px;";
                            }
                            String displayAttachFile = "";
                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO)) {
                                if(booCheckCollect == true) {
                                    displayAttachFile = "display:none;";
                                }
                            }
                    %>
                    <div class="col-sm-13" style="padding-left: 0;<%=displayAttachFile%>">
                        <div class="form-group">
                            <label id="idLblTitleUploadManager<%= sName%>" class="control-label <%=sClassFileLabel%>" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="<%=sClassFileUp%>" style="padding-right: 0px;">
                                <input type="file" id="input-file<%=sName%>" style="width: 100%;"
                                    onchange="calUploadFile(this, '<%= sName%>', '<%= String.valueOf(rs[0][0].ID)%>');" class="btn btn-default btn-file select_file" multiple>
                            </div>
                        </div>
                        <div class="form-group" style="display: <%="1".equals(sRepresentEnabled) ? "none" : ""%>">
                            <label class="control-label123" style="color:red;font-weight: 200;" id="idLblTitleNoteManager<%= sName%>"></label>
                        </div>
                        <script>
                            $(document).ready(function () {
                                var sss = '<%=sRepresentEnabled%>';
                                if(sss === "1"){
                                    $("#idLblTitleUploadManager"+'<%= sName%>').text(global_fm_browse_file + ' (' + global_fm_browse_file_upload + '<%= Integer.parseInt(sMaxLengthFile) / 1024 %>' + 'MB)');
                                } else {
                                    $("#idLblTitleUploadManager"+'<%= sName%>').text(global_fm_browse_file);
                                }
                            });
                            $("#idLblTitleNoteManager"+'<%= sName%>').text(global_fm_browse_cert_note + '<%= Integer.parseInt(sMaxLengthFile) / 1024 %>' + 'MB' + '. ' + global_fm_fileattach_support + '<%= sArrayFileExten.replace(";", ",") %>');
                        </script>
                    </div>
                    <%
                        }
                    %>
                    <div style="padding: 10px 0 0px 0;" id="idDiv<%= sName%>" class="table-responsive">
                         <table id="idTable<%= sName%>" class="table table-striped projects" style="margin-bottom: 10px;">
                             <thead>
                                <th id="idLblTitleTableSST<%= sName%>"></th>
                                <th id="idLblTitleTableFileName<%= sName%>"></th>
                                <th id="idLblTitleTableSize<%= sName%>"></th>
                                <th id="idLblTitleTableAction<%= sName%>"></th>
                                <script>
                                    $("#idLblTitleTableSST"+'<%= sName%>').text(global_fm_STT);
                                    $("#idLblTitleTableFileName"+'<%= sName%>').text(global_fm_file_name);
                                    $("#idLblTitleTableSize"+'<%= sName%>').text(global_fm_size);
                                    $("#idLblTitleTableAction"+'<%= sName%>').text(global_fm_action);
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
                                    <td><%= String.valueOf(j)%></td>
                                    <td><%= EscapeUtils.CheckTextNull(mhIP.FILE_NAME)%></td>
                                    <td><%= com.convertMoneyFromDouble(mhIP.FILE_SIZE / 1024)%></td>
                                    <td>
                                        <a id="idLblTitleTableLinkDown<%= sNameInID_inner%>" style="cursor: pointer;<%=cssDown%>;<%="0".equals(isViewActionFileEnable) ? "display: none;" : "" %>" class="btn btn-info btn-xs" onclick="DownloadTempFile('<%= mhIP.FILE_MANAGER_ID%>', '<%= anticsrf%>');">
                                        </a>
                                        <script>
                                            $(document).ready(function () {
                                                var representEnabled = '<%=sRepresentEnabled%>';
                                                if(representEnabled === "1") {
                                                    $("#idLblTitleTableLinkDown"+'<%= sNameInID_inner%>').append(global_fm_down);
                                                } else {
                                                    $("#idLblTitleTableLinkDown"+'<%= sNameInID_inner%>').append('<i class="fa fa-pencil"></i> ' + global_fm_down);
                                                }
                                            });
                                        </script>
                                        <%
                                            if(isEnableUploadFile == true) {
                                        %>
                                        <a id="idLblTitleTableLinkDelete<%= sNameInID_inner%>" style="cursor: pointer;<%=cssDelete%>;<%="0".equals(isViewActionFileEnable) && mhIP.FILE_MANAGER_ID != 0 ? "display: none;" : "" %>" class="btn btn-info btn-xs" onclick="DeleteTempFile('<%= mhIP.FILE_PROFILE%>', '<%= mhIP.FILE_NAME%>', '<%= mhIP.FILE_MANAGER_ID %>');">
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
                                        %>
                                        <%
                                            String sFileExtend = CommonFunction.getExtendFile(EscapeUtils.CheckTextNull(mhIP.FILE_NAME)).toUpperCase();
                                            if(sFileExtend.equals("PDF") || sFileExtend.equals("PNG") || sFileExtend.equals("GIF")
                                                || sFileExtend.equals("JPG") || sFileExtend.equals("JPEG")){
                                        %>
                                        &nbsp;<a id="idLblTitleTableLinkView<%= sNameInID_inner%>" style="cursor: pointer;<%=cssDelete%>;<%="0".equals(isViewActionFileEnable) ? "display: none;" : "" %>" target="_blank" class="btn btn-info btn-xs" onclick="ViewTempTwoParamFile('<%= mhIP.FILE_MANAGER_ID%>', '');"></a>
                                        <script>
                                            $(document).ready(function () {
                                                var representEnabled = '<%=sRepresentEnabled%>';
                                                if(representEnabled === "1") {
                                                    $("#idLblTitleTableLinkView"+'<%= sNameInID_inner%>').append(global_fm_view);
                                                } else {
                                                    $("#idLblTitleTableLinkView"+'<%= sNameInID_inner%>').append('<i class="fa fa-pencil"></i> ' + global_fm_view);
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
                                <tr><td colspan="4" id="idLblTitleTableNoFile<%= sName%>"></td></tr>
                                <script>
                                    $("#idLblTitleTableNoFile"+'<%= sName%>').text(global_no_file_list);
                                </script>
                                <%
                                        }
                                    } else {
                                %>
                                <tr><td colspan="4" id="idLblTitleTableNoFile<%= sName%>"></td></tr>
                                <script>
                                    $("#idLblTitleTableNoFile"+'<%= sName%>').text(global_no_file_list);
                                </script>
                                <%
                                    }
                                %>
                            </tbody>
                         </table>
                    </div>
                </fieldset>
                <%
                                        }
                                        jFile++;
                                    }
                                }
                            } catch(Exception e){CommonFunction.LogExceptionServlet(null, "FILE_JSON: " + e.getMessage(), e);}
                        }
                    }
                %>
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
                </script>
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
                                        url: '../FileManageUploadEdit',
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
                                                    var iconDelete = '<i class="fa fa-pencil"></i> ' + global_fm_button_delete;
                                                    var cssDelete = '';
                                                    var iconReview = '<i class="fa fa-pencil"></i> ' + global_fm_view;
                                                    var iconDown = '<i class="fa fa-pencil"></i> ' + global_fm_down;
                                                    var cssReview = '';
                                                    var representEnabled = '<%=sRepresentEnabled%>';
                                                    var viewActionFileEnable = '<%=isViewActionFileEnable%>';
                                                    var sActionFileEnable = "";
                                                    if(viewActionFileEnable === "0"){
                                                        sActionFileEnable = "display: none";
                                                    }
                                                    if(representEnabled === "1") {
                                                        iconDelete = ' ' + global_fm_button_delete;
                                                        cssDelete = 'text-align: center;width: 80px;';
                                                        iconReview = ' ' + global_fm_view;
                                                        cssReview = 'text-align: center;width: 80px;';
							iconDown = ' ' + global_fm_down;
                                                    }
                                                    input1.value = '';
                                                    $("#idTBody" + idType).empty();
                                                    var content = "";
                                                    for (var i = 0; i < obj.length; i++) {
                                                        if(obj[i].FILE_PROFILE === idType)
                                                        {
                                                            var fileNameLoad = obj[i].FILE_NAME;
                                                            var sActionCRL = '<a style="cursor: pointer;'+cssDelete+'" class="btn btn-info\n\
                                                                btn-xs" onclick="DeleteTempFile(\'' + obj[i].FILE_PROFILE + '\', \'' + fileNameLoad + '\', \'' + obj[i].FILE_MANAGER_ID + '\');">' + iconDelete + '</a>';
                                                            if(obj[i].FILE_PROFILE === JS_STR_FILE_PROFILE_CODE_E_CONTRACT) {
                                                                sActionCRL = "";
                                                            }
                                                            var sReviewCRL = "";
                                                            var fileNameExt = fileNameLoad.substring(fileNameLoad.lastIndexOf('.')+1);
                                                            if(fileNameExt.toUpperCase() === "PDF" || fileNameExt.toUpperCase() === "GIF"  || fileNameExt.toUpperCase() === "JPEG"
                                                                || fileNameExt.toUpperCase() === "JPG" || fileNameExt.toUpperCase() === "PNG"){
                                                                sReviewCRL = '<a style="cursor: pointer;'+cssReview+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                    btn-xs" onclick="ViewTempTwoParamFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + fileNameLoad + '\');">' + iconReview + '</a>';
                                                            }
                                                            if(obj[i].FILE_MANAGER_ID !== 0 && viewActionFileEnable === "0") {
                                                                sActionCRL = "";
                                                            }
                                                            var sActionDown = '<a style="cursor: pointer;'+cssReview+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                    btn-xs" onclick="DownloadTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + <%= anticsrf%> + '\');">' + iconDown + '</a>';
                                                            content += "<tr>" +
                                                                "<td>" + obj[i].Index + "</td>" +
                                                                "<td>" + fileNameLoad + "</td>" +
                                                                "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                "<td>" + sActionDown  + ' ' + sActionCRL + ' ' + sReviewCRL +"</td>" +
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
                                        idParam: 'deletetempfilecertedit',
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
                                                var cssReview = '';
                                                var cssDelete = '';
                                                var representEnabled = '<%=sRepresentEnabled%>';
                                                var viewActionFileEnable = '<%=isViewActionFileEnable%>';
                                                var sActionFileEnable = "";
                                                if(viewActionFileEnable === "0") {
                                                    sActionFileEnable = "display: none";
                                                }
                                                if(representEnabled === "1"){
                                                    iconDelete = ' ' + global_fm_button_delete;
                                                    cssDelete = 'text-align: center;width: 80px;';
                                                    iconDown = ' ' + global_fm_down;
                                                    iconReview = ' ' + global_fm_view;
                                                    cssReview = 'text-align: center;width: 80px;';
                                                }
                                                for (var i = 0; i < obj.length; i++) {
                                                    if(obj[i].FILE_PROFILE === idType)
                                                    {
                                                        var fileNameLoad = obj[i].FILE_NAME;
                                                        var sActionCRL = '<a style="cursor: pointer;'+cssDelete+'" class="btn btn-info\n\
                                                            btn-xs" onclick="DeleteTempFile(\'' + obj[i].FILE_PROFILE + '\', \'' + fileNameLoad+ '\', \'' + obj[i].FILE_MANAGER_ID + '\');">' + iconDelete + '</a>';
                                                        if(obj[i].FILE_PROFILE === JS_STR_FILE_PROFILE_CODE_E_CONTRACT) {
                                                            sActionCRL = "";
                                                        }
                                                        if(obj[i].FILE_MANAGER_ID !== 0 && viewActionFileEnable === "0") {
                                                            sActionCRL = "";
                                                        }
                                                        var sReviewCRL = "";
                                                        var fileNameExt = fileNameLoad.substring(fileNameLoad.lastIndexOf('.')+1);
                                                        if(fileNameExt.toUpperCase() === "PDF" || fileNameExt.toUpperCase() === "GIF" || fileNameExt.toUpperCase() === "JPEG"
                                                                    || fileNameExt.toUpperCase() === "JPG" || fileNameExt.toUpperCase() === "PNG"){
                                                            sReviewCRL = '<a style="cursor: pointer;'+cssReview+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                btn-xs" onclick="ViewTempTwoParamFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + fileNameLoad + '\');">' + iconReview + '</a>';
                                                        }
                                                        var sActionDown = '<a style="cursor: pointer;'+cssDelete+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                    btn-xs" onclick="DownloadTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + <%= anticsrf%> + '\');">' + iconDown + '</a>';
                                                        content += "<tr>" +
                                                            "<td>" + obj[i].Index + "</td>" +
                                                            "<td>" + fileNameLoad + "</td>" +
                                                            "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                            "<td>" + sActionDown + ' ' + sActionCRL + ' ' + sReviewCRL +"</td>" +
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
                    function LoadFileManage(idPurpose)
                    {
                        $.ajax({
                            type: 'POST',
                            url: '../JSONCommon',
                            data: {
                                idParam: 'loadfilemanageofpurpose',
                                idPurpose: idPurpose
                            },
                            cache: false,
                            success: function (html) {
                                if (html.length > 0)
                                {
                                    var obj = JSON.parse(html);
                                    if (obj[0].Code === "0")
                                    {
                                        $("#idDivShowFileMana").empty();
                                        var content = "";
                                        var representEnabled = '<%=sRepresentEnabled%>';
                                        var displayShowNote = "";
                                        var displayShowTitle = global_fm_browse_file;
                                        var displayFileLabel = "col-sm-2";
                                        var displayFileUp = "col-sm-10";
                                        if(representEnabled === "1"){
                                            displayFileLabel = "col-sm-3";
                                            displayFileUp = "col-sm-9";
                                            displayShowNote = "display:none;";
                                            displayShowTitle = global_fm_browse_file + ' (' + global_fm_browse_file_upload + '<%= Integer.parseInt(sMaxLengthFile) / 1024 %>' + ' MB)';
                                        }
                                        for (var i = 0; i < obj.length; i++) {
                                            content += '<fieldset class="scheduler-border">'+
                                                '<legend class="scheduler-border">'+obj[i].REMARK+'</legend>'+
                                                '<div class="form-group">'+
                                                    '<label class="control-label '+displayFileLabel+'" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">'+displayShowTitle+'</label>'+
                                                    '<div class="'+displayFileUp+'" style="padding-right: 0px;">'+
                                                    '<input type="file" id="input-file'+obj[i].NAME+'" style="width: 100%;"'+
                                                        'onchange="calUploadFile(this, \'' + obj[i].NAME + '\');" class="btn btn-default btn-file select_file" multiple>'+
                                                    '</div>'+
                                                    '<div style="height:5px;'+displayShowNote+'"></div><label class="control-label123" style="color:red;font-weight: 200;'+displayShowNote+'">' + global_fm_browse_cert_note + '<%= Integer.parseInt(sMaxLengthFile) / 1024 %>' + ' MB. ' + global_fm_fileattach_support + '<%= sArrayFileExten.replace(";", ",") %>' + '</label>'+
                                                '</div>'+
                                                '<div style="padding: 10px 0 0px 0;display:none;" id="idDiv'+obj[i].NAME+'">'+
                                                     '<table id="idTable'+obj[i].NAME+'" class="table table-striped projects" style="margin-bottom: 10px;">'+
                                                         '<thead>'+
                                                            '<th>'+global_fm_STT+'</th>'+
                                                            '<th>'+global_fm_file_name+'</th>'+
                                                            '<th>'+global_fm_size+'</th>'+
                                                            '<th>'+global_fm_action+'</th>'+
                                                        '</thead>'+
                                                        '<tbody id="idTBody'+obj[i].NAME+'">'+
                                                        '</tbody>'+
                                                     '</table>'+
                                                '</div>'+
                                            '</fieldset>';
                                        }
                                        $("#idDivShowFileMana").append(content);
                                        $("#idDivShowFileMana").css("display", "");
                                    } else if(obj[0].Code === "1")
                                    {
                                        $("#idDivShowFileMana").css("display", "none");
                                    }
                                    else {
                                        funErrorAlert(global_errorsql);
                                    }
                                }
                            }
                        });
                        return false;
                    }
                </script>
                <%
                        }
                    }
                %>
                <div class="col-sm-13" style="padding-left: 0;clear: both;">
                    <div class="form-group">
                        <fieldset class="scheduler-border">
                            <legend class="scheduler-border" id="idLblTitleComponentDN"></legend>
                            <div style="padding: 0px 0px 0px 0px;margin: 0;" id="idDivViewComponentDN"></div>
                            <script>$("#idLblTitleComponentDN").text(global_fm_csr_info_cts);</script>
                        </fieldset>
                    </div>
                </div>
                <!-- PROPERTIES SAN -->
                <div class="col-sm-13" style="padding-left: 0;clear: both;display: none;" id="idViewSanComponent">
                    <div class="form-group">
                        <fieldset class="scheduler-border">
                            <legend class="scheduler-border" id="idLblTitleComponentSAN"></legend>
                            <div id="idViewSanInfo"></div>
                            <script>$("#idLblTitleComponentSAN").text(global_fm_san_info_cts);</script>
                        </fieldset>
                    </div>
                </div>
                <!-- REPRESENTE SAN -->
                <%
                    boolean isShowRepresend = false;
                    String sRegisterIssuedAdress ="";
                    String sRegisterIssuedPlace ="";
                    String sRegisterIssuedDate ="";
                    String sRegisterCMND ="";
                    String sRegisterRole ="";
                    String sRegisterFullname ="";
                    String sRegisterAddressGPKD = "";
                    String sRepresentReadonly = "readonly";
                    if("1".equals(sRepresentEnabled) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                        if(rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                            || rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                            || rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                            || rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL)
                        {
                            if("1".equals(sRepresentEnabled)) {
                                isShowRepresend = true;
                            }
                            try {
                                String sPrfileContact = EscapeUtils.CheckTextNull(rs[0][0].PROFILE_CONTACT_INFO);
                                if(!"".equals(sPrfileContact)) {
                                    ObjectMapper oMapperParse = new ObjectMapper();
                                    ProfileContactInfoJson profileContact = oMapperParse.readValue(sPrfileContact, ProfileContactInfoJson.class);
                                    if(profileContact != null) {
                                        sRegisterIssuedPlace = CommonFunction.replaceCharaterSpecialJson(profileContact.PIDIssuedBy, false);
                                        sRegisterIssuedDate = EscapeUtils.CheckTextNull(profileContact.PIDDate);
                                        sRegisterCMND = EscapeUtils.CheckTextNull(profileContact.PID);
                                        sRegisterFullname = CommonFunction.replaceCharaterSpecialJson(profileContact.RepresentativeName, false);
                                        sRegisterIssuedAdress = CommonFunction.replaceCharaterSpecialJson(profileContact.Address, false);
                                        sRegisterRole = CommonFunction.replaceCharaterSpecialJson(profileContact.Position, false);
                                        sRegisterAddressGPKD = CommonFunction.replaceCharaterSpecialJson(profileContact.AddressLicense, false);
                                    }
                                }
                            } catch(Exception e){CommonFunction.LogExceptionServlet(null, "ProfileContactInfoJson: " + e.getMessage(), e);}
                            if(intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED
                                || intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                || intCERT_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT) {
                                if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                    || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                    || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                    sRepresentReadonly = "";
                                }
                            }
                        }
                    }
                %>
                <div class="col-sm-13" style="padding-left: 0;clear: both;display: <%= isShowRepresend == true ? "" : "none" %>;" id="idViewRepresent">
                    <div class="form-group">
                        <fieldset class="scheduler-border">
                            <legend class="scheduler-border" id="idLblTitleRepresent"></legend>
                            <script>$("#idLblTitleRepresent").text(global_fm_representative_legal);</script>
                            <div>
                                <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                    <label id="idLblTitleFullnameDK"></label>
                                    <label id="idLblNoteFullnameDK" class="CssRequireField"></label>
                                </label>
                                <div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">
                                    <input class="form-control123" <%=sRepresentReadonly%> maxlength="256" value="<%=sRegisterFullname%>" id="registerFullname" name="registerFullname"/>
                                    <!--<input class="form-control123" readonly style="display: none;" maxlength="256" value="<=sRegisterAddressGPKD%>" id="registerAddressGPKD" name="registerAddressGPKD"/>-->
                                </div>
                                <script>
                                    $("#idLblTitleFullnameDK").text(global_fm_fullname);
                                    $("#idLblNoteFullnameDK").text(global_fm_require_label);
                                </script>
                            </div>
                            <div id="idViewDivRegisterRole">
                                <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                    <label id="idLblTitleRegisterRole"></label>
                                    <label id="idLblNoteRegisterRole" class="CssRequireField"></label>
                                </label>
                                <div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">
                                    <input class="form-control123" <%=sRepresentReadonly%> maxlength="256" value="<%=sRegisterRole%>" id="registerRole" name="registerRole"/>
                                </div>
                                <script>
                                    $("#idLblTitleRegisterRole").text(global_fm_role);
                                    $("#idLblNoteRegisterRole").text(global_fm_require_label);
                                    $(document).ready(function () {
                                        if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
                                            $("#idLblNoteRegisterRole").css("display", "none");
                                        } else {
                                            $("#idLblNoteRegisterRole").css("display", "");
                                        }
                                        $('#registerIssuedDate').daterangepicker({
                                            singleDatePicker: true,
                                            showDropdowns: true
                                        }, function (start, end, label) {
                                            console.log(start.toISOString(), end.toISOString(), label);
                                        });
                                    });
                                    
                                </script>
                            </div>
                            <div>
                                <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                    <label id="idLblTitleRegisterCMND"></label>
                                    <label id="idLblNoteRegisterCMND" class="CssRequireField"></label>
                                </label>
                                <div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">
                                    <input class="form-control123" <%=sRepresentReadonly%> maxlength="256" value="<%=sRegisterCMND%>" id="registerCMND" name="registerCMND"/>
                                </div>
                                <script>
                                    $("#idLblTitleRegisterCMND").text(global_fm_CitizenId_I + "/ "+global_fm_CMND + "/ "+global_fm_HC);
                                    $("#idLblNoteFullnameDK").text(global_fm_require_label);
                                </script>
                            </div>
                            <div>
                                <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                    <label id="idLblTitleRegisterIssuedDate"></label>
                                    <!--<label id="idLblNoteRegisterIssuedDate" class="CssRequireField"></label>-->
                                </label>
                                <div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">
                                    <input type="text" class="form-control123" <%=sRepresentReadonly%> value="<%=sRegisterIssuedDate%>" id="registerIssuedDate" name="registerIssuedDate"/>
                                </div>
                                <script>
                                    $("#idLblTitleRegisterIssuedDate").text(global_fm_cmnd_date);
//                                    $("#idLblNoteRegisterIssuedDate").text(global_fm_require_label);
                                </script>
                            </div>
                            <div style="clear: both;">
                                    <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                        <label id="idLblTitleRegisterIssuedPlace"></label>
                                    </label>
                                    <div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">
                                        <input class="form-control123" <%=sRepresentReadonly%> maxlength="256" value="<%=sRegisterIssuedPlace%>" id="registerIssuedPlace" name="registerIssuedPlace"/>
                                    </div>
                                <script>
                                    $("#idLblTitleRegisterIssuedPlace").text(global_fm_place);
                                </script>
                            </div>
                            <div>
                                <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                    <label id="idLblTitleRegisterIssuedAdress"></label>
                                </label>
                                <div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">
                                    <input class="form-control123" <%=sRepresentReadonly%> maxlength="256" value="<%=sRegisterIssuedAdress%>" id="registerIssuedAdress" name="registerIssuedAdress"/>
                                </div>
                                <script>
                                    $("#idLblTitleRegisterIssuedAdress").text(token_fm_address_residence);
                                </script>
                            </div>
                        </fieldset>
                    </div>
                    <!-- REPRESENTE SAN -->
                </div>
                <div class="col-sm-13" style="padding-left: 0;clear: both;display: <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) ? "" : "none" %>;" id="idViewRepresent">
                    <div class="form-group">
                        <fieldset class="scheduler-border">
                            <legend class="scheduler-border" id="idLblTitleNCRepresent"></legend>
                            <script>$("#idLblTitleNCRepresent").text(certlist_group_add_info);</script>
                            <div class="col-sm-6" style="padding-left: 0;">
                                <div class="form-group">
                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                        <label id="idLblTitleNCRepresentative"></label>
                                        <label id="idLblNoteNCRepresentative" class="CssRequireField"></label>
                                    </label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input class="form-control123" <%=sRepresentReadonly%> maxlength="256" value="<%=sRegisterFullname%>" id="registerNCRepresentative" name="registerNCRepresentative"/>
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleNCRepresentative").text(branch_fm_representative);
                                    $("#idLblNoteNCRepresentative").text(global_fm_require_label);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;">
                                <div class="form-group">
                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                        <label id="idLblTitleNCAddress"></label>
                                        <label id="idLblNoteNCAddress" class="CssRequireField"></label>
                                    </label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input class="form-control123" maxlength="256" <%=sRepresentReadonly%> id="registerNCAddress" value="<%=sRegisterIssuedAdress%>" name="registerNCAddress"/>
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleNCAddress").text(global_fm_address_GPKD);
                                    $("#idLblNoteNCAddress").text(global_fm_require_label);
                                </script>
                            </div>
                        </fieldset>
                    </div>
                </div>
                <%
                    String isViewSubjectDNOld = "none";
                    if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                        || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                        || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                        isViewSubjectDNOld = "";
                    }
                    if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_CMC)) {
                        isViewSubjectDNOld = "none";
                    }
                %>
                <div class="col-sm-13" style="padding-left: 0;clear: both;display: <%=isViewSubjectDNOld%>">
                    <div class="form-group">
                        <fieldset class="scheduler-border">
                            <legend class="scheduler-border" id="idLblTitleOldSubjectDN"></legend>
                            <div style="padding: 0px 0px 0px 0px;margin: 0;" id="idDivViewOldSubjectDN"></div>
                            <script>$("#idLblTitleOldSubjectDN").text(certlist_title_detail_current);</script>
                        </fieldset>
                        <input type="hidden" name="idHiddenOldDN" id="idHiddenOldDN" value="<%=EscapeUtils.escapeHtmlDN(strPAST_SUBJECT)%>"/>
                    </div>
                </div>
                <div class="col-sm-13" style="padding-left: 0;clear: both;display: none;display: <%=isViewSubjectDNOld%>" id="idViewOldSanComponent">
                    <div class="form-group">
                        <fieldset class="scheduler-border">
                            <legend class="scheduler-border" id="idLblTitleOldComponentSAN"></legend>
                            <div id="idViewOldSanInfo"></div>
                            <script>$("#idLblTitleOldComponentSAN").text(global_fm_san_info_cts);</script>
                        </fieldset>
                    </div>
                </div>
                <%
                    String sHasPAST_SUBJECT = "0";
                    if(!"".equals(strPAST_SUBJECT)) {
                        sHasPAST_SUBJECT = "1";
                    }
                %>
                <script>
                    $(document).ready(function () {
                        if('<%=isViewSubjectDNOld%>' === '' && '<%=sHasPAST_SUBJECT%>' === '1') {
                            var vOldDNFromDB = document.getElementById("idHiddenOldDN").value;
                            LoadFormOldSubjectDN('<%=strCERTIFICATION_PROFILE_ID%>', vOldDNFromDB, '');
                        }
                    });
                    function LoadFormOldSubjectDN(idCertType, vDNFromDB, vSanDataDB)
                    {
                        $.ajax({
                            type: "post",
                            url: "../JSONCommon",
                            data: {
                                idParam: 'loadcert_profile_list_new',
                                vCertDurationOrProfileID: idCertType
                            },
                            cache: false,
                            success: function (html)
                            {
                                if (html.length > 0)
                                {
                                    var obj = JSON.parse(html);
                                    $("#idDivViewOldSubjectDN").empty();
                                    $("#idViewOldSanInfo").empty();
                                    if (obj[0].Code === "0")
                                    {
                                        $("#idDivViewOldSubjectDN").css("display", "");
                                        $("#idViewOldSanComponent").css("display", "");
                                        $("#idViewOldSanInfo").css("display", "");
                                        var vContent = "";
                                        var vSanContent = "";
                                        var localStoreRequired = new Array();
                                        var localStoreInput = new Array();
                                        var localStoreSanInput = new Array();
                                        var localStoreInputID_OnInput = "";
                                        var localStoreUID_Info = "";
                                        var vDisabledForm = 'disabled';
                                        var isRepresenseEnabled = '<%=sRepresentEnabled%>';
                                        var caLoadEnabled = '<%=isCALoad%>';
                                        var vReadOnlyForm = 'readonly';
                                        var booHasUIDCompany = false;
                                        var JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ADD_ID = JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID + "1";
                                        var JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ADD_ID = JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID + "1";
                                        var JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ADD_ID = JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID + "s";
                                        var JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ADD_ID = JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID + "s";
                                        for (var i = 0; i < obj.length; i++) {
                                            if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY)
                                            {
                                                booHasUIDCompany = true;
                                                break;
                                            }
                                        }
                                        var booHasUIDPersonal = false;
                                        for (var i = 0; i < obj.length; i++) {
                                            if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL)
                                            {
                                                booHasUIDPersonal = true;
                                                break;
                                            }
                                        }
                                        if(booHasUIDCompany === true)
                                        {
                                            var vDisabledForm_Info = vDisabledForm;
                                            if('<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                || '<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                || '<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                            {
                                                vDisabledForm_Info = "disabled";
                                            }
                                            var vContentButton_MST_Radio = "";
                                            if(isRepresenseEnabled === "1") {
                                                vContentButton_MST_Radio = "<div class='col-sm-13' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                        <div class='form-group'><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'>";
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ADD_ID
                                                    +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ADD_ID+'" '+vDisabledForm_Info+' class="form-control123" onlick="OnChangeComboboxMST();"></select>';
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + "</div><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;margin-right:0px;padding-right:0;'><input class='form-control123' type='text' "+vDisabledForm_Info+" id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ADD_ID + "' />";
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + "</div></div><div class='col-sm-6'></div></div></div>";
                                            } else {
                                                vContentButton_MST_Radio = "<div class='col-sm-12' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                    <div class='form-group'><div class='col-sm-3' style='padding-left: 0px;margin-left: 0px;'>";
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ADD_ID
                                                    +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ADD_ID+'" '+vDisabledForm_Info+' class="form-control123" onlick="OnChangeComboboxMST();"></select>';
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + "</div><div class='col-sm-9' style='padding-right: 0px;'><input class='form-control123' type='text' "+vDisabledForm_Info+" id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ADD_ID + "' />";
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + "</div></div></div>";
                                            }
                                            vContent = vContent + vContentButton_MST_Radio;
                                            $("#idDivViewOldSubjectDN").append(vContent);
                                            var cbxCompany = document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ADD_ID);
                                            removeOptions(cbxCompany);
                                            var vUID_CODE = "";
                                            for (var i = 0; i < obj.length; i++) {
                                                if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY)
                                                {
                                                    if(vUID_CODE === "") {
                                                        vUID_CODE = obj[i].SubjectDNAttrCode;
                                                    }
                                                    if(caLoadEnabled === JS_IS_WHICH_ABOUT_CA_ICA) {
                                                        cbxCompany.options[cbxCompany.options.length] = new Option(obj[i].SubjectDNAttrDesc, obj[i].SubjectDNAttrPreFix);
                                                    } else{
                                                        cbxCompany.options[cbxCompany.options.length] = new Option(obj[i].SubjectDNAttrDesc + ' - ' + obj[i].SubjectDNAttrPreFix, obj[i].SubjectDNAttrPreFix);
                                                    }
                                                }
                                            }
                                            localStoreUID_Info = localStoreUID_Info + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ADD_ID + "###" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ADD_ID+ "###" + vUID_CODE + ",";
                                            vContent = "";
                                        }
                                        if(booHasUIDPersonal === true)
                                        {
                                            var vDisabledForm_Info = vDisabledForm;
                                            if('<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                || '<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                || '<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                            {
                                                vDisabledForm_Info = "disabled";
                                            }
                                            var vContentButton_CMND_Radio = "";
                                            if(isRepresenseEnabled === "1") {
                                                vContentButton_CMND_Radio = "<div class='col-sm-13' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                        <div class='form-group'><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'>";
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ADD_ID
                                                    +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ADD_ID+'" '+vDisabledForm_Info+' class="form-control123"></select>';
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;margin-right:0px;padding-right:0;'><input class='form-control123' "+vDisabledForm_Info+" type='text' id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ADD_ID + "' />";
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div></div><div class='col-sm-6'></div></div></div>";
                                            } else {
                                                vContentButton_CMND_Radio = "<div class='col-sm-12' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                    <div class='form-group'><div class='col-sm-3' style='padding-left: 0px;margin-left: 0px;'>";
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ADD_ID
                                                    +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ADD_ID+'" '+vDisabledForm_Info+' class="form-control123"></select>';
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div><div class='col-sm-9' style='padding-right: 0px;'><input class='form-control123' "+vDisabledForm_Info+" type='text' id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ADD_ID + "' />";
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div></div></div>";
                                            }
                                            vContent = vContent + vContentButton_CMND_Radio;
                                            $("#idDivViewOldSubjectDN").append(vContent);
                                            var cbxPersonal = document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ADD_ID);
                                            removeOptions(cbxPersonal);
                                            var vUID_CODE = "";
                                            for (var i = 0; i < obj.length; i++) {
                                                if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL)
                                                {
                                                    if(vUID_CODE === "") {
                                                        vUID_CODE = obj[i].SubjectDNAttrCode;
                                                    }
                                                    if(caLoadEnabled === JS_IS_WHICH_ABOUT_CA_ICA) {
                                                        cbxPersonal.options[cbxPersonal.options.length] = new Option(obj[i].SubjectDNAttrDesc, obj[i].SubjectDNAttrPreFix);
                                                    } else{
                                                        cbxPersonal.options[cbxPersonal.options.length] = new Option(obj[i].SubjectDNAttrDesc + ' - ' + obj[i].SubjectDNAttrPreFix, obj[i].SubjectDNAttrPreFix);
                                                    }
                                                }
                                            }
                                            localStoreUID_Info = localStoreUID_Info + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ADD_ID + "###" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ADD_ID + "###" + vUID_CODE + ",";
                                            vContent = "";
                                        }
                                        for (var i = 0; i < obj.length; i++) {
                                            if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD)
                                            {
                                                var vLabelRequired = "";
                                                if (obj[i].IsRequired === '1')
                                                {
                                                    var vInputRequireID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID;
                                                    if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD) {
                                                        vLabelRequired = '<label class="CssRequireField">' + global_fm_require_label + '</label>';
                                                        localStoreRequired.push(vInputRequireID + '###' + obj[i].SubjectDNAttrDesc + '###' + obj[i].SubjectDNAttrPreFix);
                                                    }
                                                }
                                                var vInputID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID + '1';
                                                localStoreInput.push(vInputID + "###" + obj[i].SubjectDNAttrCode + "###" + obj[i].SubjectDNAttrPreFix + "###" + obj[i].SubjectDNAttrCNType + "###" + obj[i].SubjectDNAttrDesc);
                                                if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE)
                                                {
                                                    vContent += '<div>' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                        '<select '+vDisabledForm+' class="form-control123" id="' + vInputID + '"></select>' +
                                                        '</div>' +
                                                        '</div>';
                                                    var sValuePushDB = JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE + "=" + obj[i].SubjectDNAttrPreFix;
                                                    var indexTag = vDNFromDB.indexOf(sValuePushDB);
                                                    if(indexTag !== -1)
                                                    {
                                                        var vDataInputID = '<%= pastCITY_PROVINCE_ID%>';
                                                        LoadDNCity(vInputID, vDataInputID);
                                                    } else {
                                                        LoadDNCity(vInputID, "");
                                                    }
                                                } else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_PHONE)
                                                {
                                                    vContent += '<div>' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                        '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID + '" onblur="autoTrimTextField("' + vInputID + '", this.value);" />' +
                                                        '</div>' +
                                                        '</div>';
                                                }
                                                else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COUNTRY)
                                                {
                                                    var displayCountry = "";
                                                    if(isRepresenseEnabled === "1"){displayCountry = "display:none;";}
                                                    vContent += '<div style="'+displayCountry+'">' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                        '<input disabled class="form-control123" type="text" id="' + vInputID + '" />' +
                                                        '</div>' +
                                                        '</div>';
                                                } else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                                                {
                                                    if(isRepresenseEnabled === "1") {
                                                        vContent += '<div>' +
                                                            '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                            '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                            '<input '+ vReadOnlyForm +' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                            '</div>' +
                                                            '</div>';
                                                    } else {
                                                        vContent += '<div class="form-group" style="clear:both;">' +
                                                            '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                            '<div class="col-sm-10" style="padding-right: 10px;">'+
                                                                '<input '+vReadOnlyForm+' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                            '</div>' +
                                                        '</div>';
                                                    }
                                                } else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_LOCALITY)
                                                {
                                                    vContent += '<div class="form-group" style="clear:both;">' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-10" style="padding-right: 10px;">'+
                                                            '<input '+vReadOnlyForm+' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                        '</div>' +
                                                    '</div>';
                                                } else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                {
                                                    vContent += '<div class="form-group" style="clear:both;">' +
                                                            '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                            '<div class="col-sm-10" style="padding-right: 10px;">'+
                                                            '<input '+ vReadOnlyForm +' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                            '</div>' +
                                                            '</div>';
                                                } else
                                                {
                                                    vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                    vContent += '<div>' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                        '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                        '</div>' +
                                                        '</div>';
                                                }
                                                if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_EMAIL)
                                                {
                                                    localStoreInputID_OnInput = localStoreInputID_OnInput + JS_STR_COMPONENT_DN_VALUE_EMAIL + "###" + vInputID + ", ";
                                                }
                                            } else if (obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD_SAN)
                                            {
                                                var vLabelRequired = "";
                                                if (obj[i].IsRequired === '1') {
                                                    var vInputRequireID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID;
                                                    if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD_SAN) {
                                                        vLabelRequired = '<label class="CssRequireField">' + global_fm_require_label + '</label>';
                                                        localStoreRequired.push(vInputRequireID + '###' + obj[i].SubjectDNAttrDesc + '###' + obj[i].SubjectDNAttrPreFix);
                                                    }
                                                }
                                                var vInputID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID + '1';
                                                localStoreSanInput.push(vInputID + "###" + obj[i].SubjectDNAttrCode + "###" + obj[i].SubjectDNAttrPreFix + "###" + obj[i].SubjectDNAttrCNType + "###" + obj[i].SubjectDNAttrDesc);
                                                vSanContent += '<div>' +
                                                    '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                    '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                    '<input class="form-control123" '+vDisabledForm+' type="text" id="' + vInputID + '" />' +
                                                    '</div>' +
                                                    '</div>';
                                                if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_E_SAN)
                                                {
                                                    localStoreInputID_OnInput = localStoreInputID_OnInput + JS_STR_COMPONENT_DN_VALUE_E_SAN + "###" + vInputID + ", ";
                                                }
                                            }
                                        }
                                        $("#idDivViewOldSubjectDN").append(vContent);
                                        if(vSanContent !== "") {
                                            $("#idViewOldSanInfo").append(vSanContent);
                                        } else {
                                            $("#idViewOldSanComponent").css("display", "none");
                                            $("#idViewOldSanInfo").css("display", "none");
                                        }
                                        if(vDNFromDB !== "") {
                                            var vDNFromFilter = "";
                                            if(booHasUIDCompany === true || booHasUIDPersonal === true) {
                                                var vDNFromDBSplit = vDNFromDB.split(',');
                                                for (var iDN = 0; iDN < vDNFromDBSplit.length; iDN++) {
                                                    if(sSpace(vDNFromDBSplit[iDN]).indexOf(JS_STR_COMPONENT_DN_VALUE_UID) !== -1) {
                                                        vDNFromFilter = vDNFromFilter + vDNFromDBSplit[iDN] + ",";
                                                    }
                                                }
                                            }
                                            var intDNFilterSub = vDNFromFilter.lastIndexOf(',');
                                            vDNFromFilter = vDNFromFilter.substring(0, intDNFilterSub);
                                            if(booHasUIDCompany === true) {
                                                for (var i = 0; i < obj.length; i++) {
                                                    if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY)
                                                    {
                                                        var sValuePushDB = obj[i].SubjectDNAttrCode + "=" + obj[i].SubjectDNAttrPreFix;
                                                        if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_TITLE + "=")
                                                        {
                                                            sValuePushDB = " " + sValuePushDB;
                                                        }
                                                        var indexTag = vDNFromFilter.indexOf(sValuePushDB);
                                                        if(indexTag !== -1) {
                                                            var sValuePushDBDataFrist = vDNFromFilter.substring(indexTag, vDNFromFilter.length);
                                                            var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                            $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ADD_ID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                            $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ADD_ID).val(obj[i].SubjectDNAttrPreFix);
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            if(booHasUIDPersonal === true) {
                                                for (var i = 0; i < obj.length; i++) {
                                                    if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL)
                                                    {
                                                        var sValuePushDB = obj[i].SubjectDNAttrCode + "=" + obj[i].SubjectDNAttrPreFix;
                                                        if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_TITLE + "=")
                                                        {
                                                            sValuePushDB = " " + sValuePushDB;
                                                        }
                                                        var indexTag = vDNFromFilter.indexOf(sValuePushDB);
                                                        if(indexTag !== -1) {
                                                            var sValuePushDBDataFrist = vDNFromFilter.substring(indexTag, vDNFromFilter.length);
                                                            var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                            $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ADD_ID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                            $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ADD_ID).val(obj[i].SubjectDNAttrPreFix);
                                                        }
                                                    }
                                                }
                                            }
                                            var sInputOU = "";
                                            for (var i = 0; i < obj.length; i++) {
                                                var vInputID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID + '1';
                                                var vInputSanOperation = obj[i].SubjectDNAttrCode;
                                                if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD)
                                                {
                                                    var sValuePushDB = obj[i].SubjectDNAttrCode + "=" + obj[i].SubjectDNAttrPreFix;
                                                    if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_TITLE + "=")
                                                    {
                                                        sValuePushDB = " " + sValuePushDB;
                                                    }
                                                    var indexTag = vDNFromDB.indexOf(sValuePushDB);
                                                    if(indexTag !== -1) {
                                                        if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE + "=") {
                                                        } else if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_PHONE + "=") {
                                                            var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                            var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                            $("#" + vInputID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                        } else if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_ORGANUNIT + "=") {
                                                            if('<%= allowTwoOU%>' === '1')
                                                            {
                                                                sInputOU = sInputOU + vInputID + "###";
                                                            } else {
                                                                var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                                var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                                $("#" + vInputID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                            }
                                                        } else {
                                                            if(vInputID.indexOf(JS_STR_COMPONENT_DN_VALUE_UID) !== -1)
                                                            {
                                                                vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                            }
                                                            var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                            var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                            $("#" + vInputID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                        }
                                                    }
                                                }
                                                else if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD_SAN)
                                                {
                                                    if(vSanDataDB !== "")
                                                    {
                                                        var sSanDataSplit1 = vSanDataDB.split('@@@');
                                                        for (var iSan = 0; iSan < sSanDataSplit1.length; iSan++) {
                                                            var sSanDataSplit2 = sSpace(sSanDataSplit1[iSan].split('###')[0]);
                                                            if(sSanDataSplit2 !== "")
                                                            {
                                                                if(sSanDataSplit2 === vInputSanOperation)
                                                                {
                                                                    $("#" + vInputID).val(sSpace(sSanDataSplit1[iSan].split('###')[1]));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if('<%= allowTwoOU%>' === '1')
                                            {
                                                if(sInputOU !== "")
                                                {
                                                    var sValueOU = "";
//                                                    for (var p = 0; p < obj.length; p++) {
//                                                        if(obj[p].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD) {
//                                                            var sValuePushDB = obj[p].SubjectDNAttrCode + "=" + obj[p].SubjectDNAttrPreFix;
                                                            var str_array = vDNFromDB.split(',');
                                                            for(var h = 0; h < str_array.length; h++)
                                                            {
                                                                var a = sSpace(str_array[h]);
                                                                if(a.split('=')[0] === 'OU')
                                                                {
                                                                    sValueOU = sValueOU + a.split('=')[1] + "###";
                                                                }
                                                            }
//                                                        }
//                                                    }
                                                    var intSub = sInputOU.lastIndexOf('###');
                                                    sInputOU = sInputOU.substring(0, intSub);
                                                    var sInputOUArray = sInputOU.split('###');
                                                    if(sInputOUArray.length > 0) {
                                                        if(sInputOUArray.length === 1){
                                                            $("#"+sInputOU.split('###')[0]).val(sValueOU.split('###')[0]);
                                                        }
                                                        if(sInputOUArray.length === 2){
                                                            $("#"+sInputOU.split('###')[0]).val(sValueOU.split('###')[0]);
                                                            $("#"+sInputOU.split('###')[1]).val(sValueOU.split('###')[1]);
                                                        }
                                                        if(sInputOUArray.length === 3){
                                                            $("#"+sInputOU.split('###')[0]).val(sValueOU.split('###')[0]);
                                                            $("#"+sInputOU.split('###')[1]).val(sValueOU.split('###')[1]);
                                                            $("#"+sInputOU.split('###')[2]).val(sValueOU.split('###')[2]);
                                                        }
                                                        if(sInputOUArray.length === 4){
                                                            $("#"+sInputOU.split('###')[0]).val(sValueOU.split('###')[0]);
                                                            $("#"+sInputOU.split('###')[1]).val(sValueOU.split('###')[1]);
                                                            $("#"+sInputOU.split('###')[2]).val(sValueOU.split('###')[2]);
                                                            $("#"+sInputOU.split('###')[3]).val(sValueOU.split('###')[3]);
                                                        }
                                                    }
                                                    /*if(sInputOU.split('###')[0] !== "")
                                                    {
                                                        $("#"+sInputOU.split('###')[0]).val(sValueOU.split('###')[0]);
                                                    }
                                                    if(sInputOU.split('###')[1] !== "")
                                                    {
                                                        $("#"+sInputOU.split('###')[1]).val(sValueOU.split('###')[1]);
                                                    }*/
                                                }
                                            }
                                            if(localStoreInputID_OnInput !== null && localStoreInputID_OnInput !== "null" && localStoreInputID_OnInput !== "")
                                            {
                                                if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE
                                                    || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_CODE_SIGNING)
                                                {
                                                    var idCNTemp="";
                                                    var idOTemp = "";
                                                    var sListInputCheckID_Info = localStoreInputID_OnInput.split(',');
                                                    for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                                                        if(sSpace(sListInputCheckID_Info[i].split('###')[0]) === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                        {
                                                            idOTemp = sSpace(sListInputCheckID_Info[i].split('###')[1]);
                                                        }
                                                        if(sSpace(sListInputCheckID_Info[i].split('###')[0]) === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                                                        {
                                                            idCNTemp = sSpace(sListInputCheckID_Info[i].split('###')[1]);
                                                        }
                                                    }
                                                    if(idCNTemp !== "" && idOTemp !== "")
                                                    {
                                                        $("#"+idOTemp).prop("readonly", true);
                                                        $("#"+idCNTemp).on('input',function(e){
                                                            OnBlurCompany(idOTemp, this.value);
                                                        });
                                                    }
                                                }
                                                var idE_SUBJECTTemp="";
                                                var idE_SANTemp = "";
                                                var sListInputCheckID_Info = localStoreInputID_OnInput.split(',');
                                                for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                                                    if(sSpace(sListInputCheckID_Info[i].split('###')[0]) === JS_STR_COMPONENT_DN_VALUE_E_SAN)
                                                    {
                                                        idE_SANTemp = sSpace(sListInputCheckID_Info[i].split('###')[1]);
                                                    }
                                                    if(sSpace(sListInputCheckID_Info[i].split('###')[0]) === JS_STR_COMPONENT_DN_VALUE_EMAIL)
                                                    {
                                                        idE_SUBJECTTemp = sSpace(sListInputCheckID_Info[i].split('###')[1]);
                                                    }
                                                }
                                                if(idE_SUBJECTTemp !== "" && idE_SANTemp !== "")
                                                {
                                                    document.getElementById(idE_SUBJECTTemp).oninput = function() { OnBlurCompany(idE_SANTemp, this.value);};
                                                }
                                            }
                                        }
                                    }
                                    else if (obj[0].Code === JS_EX_CSRF)
                                    {
                                        funCsrfAlert();
                                    } else if (obj[0].Code === JS_EX_LOGIN)
                                    {
                                        RedirectPageLoginNoSess(global_alert_login);
                                    }
                                    else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                    {
                                        RedirectPageLoginNoSess(global_alert_another_login);
                                    }
                                    else {
                                        $("#idDivViewOldSubjectDN").css("display", "none");
                                        $("#idViewOldSanComponent").css("display", "none");
                                        $("#idViewOldSanInfo").css("display", "none");
                                        funErrorAlert(global_errorsql);
                                    }
                                }
                            }
                        });
                        return false;
                    }
                    function LoadFormOldSubjectDN_BK(idCertType, vDNFromDB, vSanDataDB)
                    {
                        $.ajax({
                            type: "post",
                            url: "../JSONCommon",
                            data: {
                                idParam: 'loadcert_profile_list_new',
                                vCertDurationOrProfileID: idCertType
                            },
                            cache: false,
                            success: function (html)
                            {
                                if (html.length > 0)
                                {
                                    var obj = JSON.parse(html);
                                    $("#idDivViewOldSubjectDN").empty();
                                    $("#idViewOldSanInfo").empty();
                                    if (obj[0].Code === "0")
                                    {
                                        $("#idDivViewOldSubjectDN").css("display", "");
                                        $("#idViewOldSanComponent").css("display", "");
                                        $("#idViewOldSanInfo").css("display", "");
                                        var vContent = "";
                                        var vSanContent = "";
                                        var localStoreRequired = new Array();
                                        var localStoreInput = new Array();
                                        var localStoreSanInput = new Array();
                                        var localStoreInputID_OnInput = "";
                                        var localStoreUID_Info = "";
                                        var vDisabledForm = 'disabled';
                                        var isRepresenseEnabled = '<%=sRepresentEnabled%>';
                                        var caLoadEnabled = '<%=isCALoad%>';
                                        var vReadOnlyForm = 'readonly';
                                        var booHasUIDCompany = false;
                                        JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID = JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID + "1";
                                        JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID = JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID + "1";
                                        JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID = JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID + "s";
                                        JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID = JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID + "s";
                                        for (var i = 0; i < obj.length; i++) {
                                            if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY)
                                            {
                                                booHasUIDCompany = true;
                                                break;
                                            }
                                        }
                                        var booHasUIDPersonal = false;
                                        for (var i = 0; i < obj.length; i++) {
                                            if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL)
                                            {
                                                booHasUIDPersonal = true;
                                                break;
                                            }
                                        }
                                        if(booHasUIDCompany === true)
                                        {
                                            var vDisabledForm_Info = vDisabledForm;
                                            if('<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                || '<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                || '<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                            {
                                                vDisabledForm_Info = "disabled";
                                            }
                                            var vContentButton_MST_Radio = "";
                                            if(isRepresenseEnabled === "1") {
                                                vContentButton_MST_Radio = "<div class='col-sm-13' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                        <div class='form-group'><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'>";
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID
                                                    +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID+'" '+vDisabledForm_Info+' class="form-control123" onlick="OnChangeComboboxMST();"></select>';
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + "</div><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;margin-right:0px;padding-right:0;'><input class='form-control123' type='text' "+vDisabledForm_Info+" id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID + "' />";
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + "</div></div><div class='col-sm-6'></div></div></div>";
                                            } else {
                                                vContentButton_MST_Radio = "<div class='col-sm-12' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                    <div class='form-group'><div class='col-sm-3' style='padding-left: 0px;margin-left: 0px;'>";
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID
                                                    +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID+'" '+vDisabledForm_Info+' class="form-control123" onlick="OnChangeComboboxMST();"></select>';
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + "</div><div class='col-sm-9' style='padding-right: 0px;'><input class='form-control123' type='text' "+vDisabledForm_Info+" id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID + "' />";
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + "</div></div></div>";
                                            }
                                            vContent = vContent + vContentButton_MST_Radio;
                                            $("#idDivViewOldSubjectDN").append(vContent);
                                            var cbxCompany = document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID);
                                            removeOptions(cbxCompany);
                                            var vUID_CODE = "";
                                            for (var i = 0; i < obj.length; i++) {
                                                if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY)
                                                {
                                                    if(vUID_CODE === "") {
                                                        vUID_CODE = obj[i].SubjectDNAttrCode;
                                                    }
                                                    if(caLoadEnabled === JS_IS_WHICH_ABOUT_CA_ICA) {
                                                        cbxCompany.options[cbxCompany.options.length] = new Option(obj[i].SubjectDNAttrDesc, obj[i].SubjectDNAttrPreFix);
                                                    } else{
                                                        cbxCompany.options[cbxCompany.options.length] = new Option(obj[i].SubjectDNAttrDesc + ' - ' + obj[i].SubjectDNAttrPreFix, obj[i].SubjectDNAttrPreFix);
                                                    }
                                                }
                                            }
                                            localStoreUID_Info = localStoreUID_Info + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID + "###" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID+ "###" + vUID_CODE + ",";
                                            vContent = "";
                                        }
                                        if(booHasUIDPersonal === true)
                                        {
                                            var vDisabledForm_Info = vDisabledForm;
                                            if('<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                || '<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                || '<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                            {
                                                vDisabledForm_Info = "disabled";
                                            }
                                            var vContentButton_CMND_Radio = "";
                                            if(isRepresenseEnabled === "1") {
                                                vContentButton_CMND_Radio = "<div class='col-sm-13' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                        <div class='form-group'><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'>";
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID
                                                    +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID+'" '+vDisabledForm_Info+' class="form-control123"></select>';
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;margin-right:0px;padding-right:0;'><input class='form-control123' "+vDisabledForm_Info+" type='text' id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID + "' />";
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div></div><div class='col-sm-6'></div></div></div>";
                                            } else {
                                                vContentButton_CMND_Radio = "<div class='col-sm-12' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                    <div class='form-group'><div class='col-sm-3' style='padding-left: 0px;margin-left: 0px;'>";
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID
                                                    +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID+'" '+vDisabledForm_Info+' class="form-control123"></select>';
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div><div class='col-sm-9' style='padding-right: 0px;'><input class='form-control123' "+vDisabledForm_Info+" type='text' id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID + "' />";
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div></div></div>";
                                            }
                                            vContent = vContent + vContentButton_CMND_Radio;
                                            $("#idDivViewOldSubjectDN").append(vContent);
                                            var cbxPersonal = document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID);
                                            removeOptions(cbxPersonal);
                                            var vUID_CODE = "";
                                            for (var i = 0; i < obj.length; i++) {
                                                if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL)
                                                {
                                                    if(vUID_CODE === "") {
                                                        vUID_CODE = obj[i].SubjectDNAttrCode;
                                                    }
                                                    if(caLoadEnabled === JS_IS_WHICH_ABOUT_CA_ICA) {
                                                        cbxPersonal.options[cbxPersonal.options.length] = new Option(obj[i].SubjectDNAttrDesc, obj[i].SubjectDNAttrPreFix);
                                                    } else{
                                                        cbxPersonal.options[cbxPersonal.options.length] = new Option(obj[i].SubjectDNAttrDesc + ' - ' + obj[i].SubjectDNAttrPreFix, obj[i].SubjectDNAttrPreFix);
                                                    }
                                                }
                                            }
                                            localStoreUID_Info = localStoreUID_Info + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID + "###" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID + "###" + vUID_CODE + ",";
                                            vContent = "";
                                        }
                                        for (var i = 0; i < obj.length; i++) {
                                            if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD)
                                            {
                                                var vLabelRequired = "";
                                                if (obj[i].IsRequired === '1')
                                                {
                                                    var vInputRequireID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID;
                                                    if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD) {
                                                        vLabelRequired = '<label class="CssRequireField">' + global_fm_require_label + '</label>';
                                                        localStoreRequired.push(vInputRequireID + '###' + obj[i].SubjectDNAttrDesc + '###' + obj[i].SubjectDNAttrPreFix);
                                                    }
                                                }
                                                var vInputID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID + '1';
                                                localStoreInput.push(vInputID + "###" + obj[i].SubjectDNAttrCode + "###" + obj[i].SubjectDNAttrPreFix + "###" + obj[i].SubjectDNAttrCNType + "###" + obj[i].SubjectDNAttrDesc);
                                                if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE)
                                                {
                                                    vContent += '<div>' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                        '<select '+vDisabledForm+' class="form-control123" id="' + vInputID + '"></select>' +
                                                        '</div>' +
                                                        '</div>';
                                                    var sValuePushDB = JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE + "=" + obj[i].SubjectDNAttrPreFix;
                                                    var indexTag = vDNFromDB.indexOf(sValuePushDB);
                                                    if(indexTag !== -1)
                                                    {
                                                        var vDataInputID = '<%= pastCITY_PROVINCE_ID%>';
                                                        LoadDNCity(vInputID, vDataInputID);
                                                    } else {
                                                        LoadDNCity(vInputID, "");
                                                    }
                                                } else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_PHONE)
                                                {
                                                    vContent += '<div>' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                        '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID + '" onblur="autoTrimTextField("' + vInputID + '", this.value);" />' +
                                                        '</div>' +
                                                        '</div>';
                                                }
                                                else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COUNTRY)
                                                {
                                                    var displayCountry = "";
                                                    if(isRepresenseEnabled === "1"){displayCountry = "display:none;";}
                                                    vContent += '<div style="'+displayCountry+'">' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                        '<input disabled class="form-control123" type="text" id="' + vInputID + '" />' +
                                                        '</div>' +
                                                        '</div>';
                                                } else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                                                {
                                                    if(isRepresenseEnabled === "1") {
                                                        vContent += '<div>' +
                                                            '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                            '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                            '<input '+ vReadOnlyForm +' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                            '</div>' +
                                                            '</div>';
                                                    } else {
                                                        vContent += '<div class="form-group" style="clear:both;">' +
                                                            '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                            '<div class="col-sm-10" style="padding-right: 10px;">'+
                                                                '<input '+vReadOnlyForm+' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                            '</div>' +
                                                        '</div>';
                                                    }
                                                } else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_LOCALITY)
                                                {
                                                    vContent += '<div class="form-group" style="clear:both;">' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-10" style="padding-right: 10px;">'+
                                                            '<input '+vReadOnlyForm+' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                        '</div>' +
                                                    '</div>';
                                                } else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                {
                                                    vContent += '<div class="form-group" style="clear:both;">' +
                                                            '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                            '<div class="col-sm-10" style="padding-right: 10px;">'+
                                                            '<input '+ vReadOnlyForm +' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                            '</div>' +
                                                            '</div>';
                                                } else
                                                {
                                                    vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                    vContent += '<div>' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                        '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                        '</div>' +
                                                        '</div>';
                                                }
                                                if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_EMAIL)
                                                {
                                                    localStoreInputID_OnInput = localStoreInputID_OnInput + JS_STR_COMPONENT_DN_VALUE_EMAIL + "###" + vInputID + ", ";
                                                }
                                            } else if (obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD_SAN)
                                            {
                                                var vLabelRequired = "";
                                                if (obj[i].IsRequired === '1') {
                                                    var vInputRequireID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID;
                                                    if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD_SAN) {
                                                        vLabelRequired = '<label class="CssRequireField">' + global_fm_require_label + '</label>';
                                                        localStoreRequired.push(vInputRequireID + '###' + obj[i].SubjectDNAttrDesc + '###' + obj[i].SubjectDNAttrPreFix);
                                                    }
                                                }
                                                var vInputID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID + '1';
                                                localStoreSanInput.push(vInputID + "###" + obj[i].SubjectDNAttrCode + "###" + obj[i].SubjectDNAttrPreFix + "###" + obj[i].SubjectDNAttrCNType + "###" + obj[i].SubjectDNAttrDesc);
                                                vSanContent += '<div>' +
                                                    '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                    '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                    '<input class="form-control123" '+vDisabledForm+' type="text" id="' + vInputID + '" />' +
                                                    '</div>' +
                                                    '</div>';
                                                if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_E_SAN)
                                                {
                                                    localStoreInputID_OnInput = localStoreInputID_OnInput + JS_STR_COMPONENT_DN_VALUE_E_SAN + "###" + vInputID + ", ";
                                                }
                                            }
                                        }
                                        $("#idDivViewOldSubjectDN").append(vContent);
                                        if(vSanContent !== "") {
                                            $("#idViewOldSanInfo").append(vSanContent);
                                        } else {
                                            $("#idViewOldSanComponent").css("display", "none");
                                            $("#idViewOldSanInfo").css("display", "none");
                                        }
                                        if(vDNFromDB !== "") {
                                            var vDNFromFilter = "";
                                            if(booHasUIDCompany === true || booHasUIDPersonal === true) {
                                                var vDNFromDBSplit = vDNFromDB.split(',');
                                                for (var iDN = 0; iDN < vDNFromDBSplit.length; iDN++) {
                                                    if(sSpace(vDNFromDBSplit[iDN]).indexOf(JS_STR_COMPONENT_DN_VALUE_UID) !== -1) {
                                                        vDNFromFilter = vDNFromFilter + vDNFromDBSplit[iDN] + ",";
                                                    }
                                                }
                                            }
                                            var intDNFilterSub = vDNFromFilter.lastIndexOf(',');
                                            vDNFromFilter = vDNFromFilter.substring(0, intDNFilterSub);
                                            if(booHasUIDCompany === true) {
                                                for (var i = 0; i < obj.length; i++) {
                                                    if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY)
                                                    {
                                                        var sValuePushDB = obj[i].SubjectDNAttrCode + "=" + obj[i].SubjectDNAttrPreFix;
                                                        if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_TITLE + "=")
                                                        {
                                                            sValuePushDB = " " + sValuePushDB;
                                                        }
                                                        var indexTag = vDNFromFilter.indexOf(sValuePushDB);
                                                        if(indexTag !== -1) {
                                                            var sValuePushDBDataFrist = vDNFromFilter.substring(indexTag, vDNFromFilter.length);
                                                            var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                            $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                            $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).val(obj[i].SubjectDNAttrPreFix);
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            if(booHasUIDPersonal === true) {
                                                for (var i = 0; i < obj.length; i++) {
                                                    if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL)
                                                    {
                                                        var sValuePushDB = obj[i].SubjectDNAttrCode + "=" + obj[i].SubjectDNAttrPreFix;
                                                        if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_TITLE + "=")
                                                        {
                                                            sValuePushDB = " " + sValuePushDB;
                                                        }
                                                        var indexTag = vDNFromFilter.indexOf(sValuePushDB);
                                                        if(indexTag !== -1) {
                                                            var sValuePushDBDataFrist = vDNFromFilter.substring(indexTag, vDNFromFilter.length);
                                                            var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                            $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                            $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).val(obj[i].SubjectDNAttrPreFix);
                                                        }
                                                    }
                                                }
                                            }
                                            var sInputOU = "";
                                            for (var i = 0; i < obj.length; i++) {
                                                var vInputID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID + '1';
                                                var vInputSanOperation = obj[i].SubjectDNAttrCode;
                                                if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD)
                                                {
                                                    var sValuePushDB = obj[i].SubjectDNAttrCode + "=" + obj[i].SubjectDNAttrPreFix;
                                                    if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_TITLE + "=")
                                                    {
                                                        sValuePushDB = " " + sValuePushDB;
                                                    }
                                                    var indexTag = vDNFromDB.indexOf(sValuePushDB);
                                                    if(indexTag !== -1) {
                                                        if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE + "=") {
                                                        } else if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_PHONE + "=") {
                                                            var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                            var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                            $("#" + vInputID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                        } else if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_ORGANUNIT + "=") {
                                                            if('<%= allowTwoOU%>' === '1')
                                                            {
                                                                sInputOU = sInputOU + vInputID + "###";
                                                            } else {
                                                                var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                                var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                                $("#" + vInputID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                            }
                                                        } else {
                                                            if(vInputID.indexOf(JS_STR_COMPONENT_DN_VALUE_UID) !== -1)
                                                            {
                                                                vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                            }
                                                            var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                            var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                            $("#" + vInputID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                        }
                                                    }
                                                }
                                                else if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD_SAN)
                                                {
                                                    if(vSanDataDB !== "")
                                                    {
                                                        var sSanDataSplit1 = vSanDataDB.split('@@@');
                                                        for (var iSan = 0; iSan < sSanDataSplit1.length; iSan++) {
                                                            var sSanDataSplit2 = sSpace(sSanDataSplit1[iSan].split('###')[0]);
                                                            if(sSanDataSplit2 !== "")
                                                            {
                                                                if(sSanDataSplit2 === vInputSanOperation)
                                                                {
                                                                    $("#" + vInputID).val(sSpace(sSanDataSplit1[iSan].split('###')[1]));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if('<%= allowTwoOU%>' === '1')
                                            {
                                                if(sInputOU !== "")
                                                {
                                                    var sValueOU = "";
//                                                    for (var p = 0; p < obj.length; p++) {
//                                                        if(obj[p].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD)
//                                                        {
//                                                            var sValuePushDB = obj[p].SubjectDNAttrCode + "=" + obj[p].SubjectDNAttrPreFix;
                                                            var str_array = vDNFromDB.split(',');
                                                            for(var h = 0; h < str_array.length; h++)
                                                            {
                                                                var a = sSpace(str_array[h]);
                                                                if(a.split('=')[0] === 'OU')
                                                                {
                                                                    sValueOU = sValueOU + a.split('=')[1] + "###";
                                                                }
                                                            }
//                                                        }
//                                                    }
                                                    var intSub = sInputOU.lastIndexOf('###');
                                                    sInputOU = sInputOU.substring(0, intSub);
                                                    var sInputOUArray = sInputOU.split('###');
                                                    if(sInputOUArray.length > 0) {
                                                        if(sInputOUArray.length === 1){
                                                            $("#"+sInputOU.split('###')[0]).val(sValueOU.split('###')[0]);
                                                        }
                                                        if(sInputOUArray.length === 2){
                                                            $("#"+sInputOU.split('###')[0]).val(sValueOU.split('###')[0]);
                                                            $("#"+sInputOU.split('###')[1]).val(sValueOU.split('###')[1]);
                                                        }
                                                        if(sInputOUArray.length === 3){
                                                            $("#"+sInputOU.split('###')[0]).val(sValueOU.split('###')[0]);
                                                            $("#"+sInputOU.split('###')[1]).val(sValueOU.split('###')[1]);
                                                            $("#"+sInputOU.split('###')[2]).val(sValueOU.split('###')[2]);
                                                        }
                                                        if(sInputOUArray.length === 4){
                                                            $("#"+sInputOU.split('###')[0]).val(sValueOU.split('###')[0]);
                                                            $("#"+sInputOU.split('###')[1]).val(sValueOU.split('###')[1]);
                                                            $("#"+sInputOU.split('###')[2]).val(sValueOU.split('###')[2]);
                                                            $("#"+sInputOU.split('###')[3]).val(sValueOU.split('###')[3]);
                                                        }
                                                    }
                                                    /*if(sInputOU.split('###')[0] !== "")
                                                    {
                                                        $("#"+sInputOU.split('###')[0]).val(sValueOU.split('###')[0]);
                                                    }
                                                    if(sInputOU.split('###')[1] !== "")
                                                    {
                                                        $("#"+sInputOU.split('###')[1]).val(sValueOU.split('###')[1]);
                                                    }*/
                                                }
                                            }
                                            if(localStoreInputID_OnInput !== null && localStoreInputID_OnInput !== "null" && localStoreInputID_OnInput !== "")
                                            {
                                                if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE
                                                    || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_CODE_SIGNING)
                                                {
                                                    var idCNTemp="";
                                                    var idOTemp = "";
                                                    var sListInputCheckID_Info = localStoreInputID_OnInput.split(',');
                                                    for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                                                        if(sSpace(sListInputCheckID_Info[i].split('###')[0]) === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                        {
                                                            idOTemp = sSpace(sListInputCheckID_Info[i].split('###')[1]);
                                                        }
                                                        if(sSpace(sListInputCheckID_Info[i].split('###')[0]) === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                                                        {
                                                            idCNTemp = sSpace(sListInputCheckID_Info[i].split('###')[1]);
                                                        }
                                                    }
                                                    if(idCNTemp !== "" && idOTemp !== "")
                                                    {
                                                        $("#"+idOTemp).prop("readonly", true);
                                                        $("#"+idCNTemp).on('input',function(e){
                                                            OnBlurCompany(idOTemp, this.value);
                                                        });
                                                    }
                                                }
                                                var idE_SUBJECTTemp="";
                                                var idE_SANTemp = "";
                                                var sListInputCheckID_Info = localStoreInputID_OnInput.split(',');
                                                for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                                                    if(sSpace(sListInputCheckID_Info[i].split('###')[0]) === JS_STR_COMPONENT_DN_VALUE_E_SAN)
                                                    {
                                                        idE_SANTemp = sSpace(sListInputCheckID_Info[i].split('###')[1]);
                                                    }
                                                    if(sSpace(sListInputCheckID_Info[i].split('###')[0]) === JS_STR_COMPONENT_DN_VALUE_EMAIL)
                                                    {
                                                        idE_SUBJECTTemp = sSpace(sListInputCheckID_Info[i].split('###')[1]);
                                                    }
                                                }
                                                if(idE_SUBJECTTemp !== "" && idE_SANTemp !== "")
                                                {
                                                    document.getElementById(idE_SUBJECTTemp).oninput = function() { OnBlurCompany(idE_SANTemp, this.value);};
                                                }
                                            }
                                        }
                                    }
                                    else if (obj[0].Code === JS_EX_CSRF)
                                    {
                                        funCsrfAlert();
                                    } else if (obj[0].Code === JS_EX_LOGIN)
                                    {
                                        RedirectPageLoginNoSess(global_alert_login);
                                    }
                                    else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                    {
                                        RedirectPageLoginNoSess(global_alert_another_login);
                                    }
                                    else {
                                        $("#idDivViewOldSubjectDN").css("display", "none");
                                        $("#idViewOldSanComponent").css("display", "none");
                                        $("#idViewOldSanInfo").css("display", "none");
                                        funErrorAlert(global_errorsql);
                                    }
                                }
                            }
                        });
                        return false;
                    }
                </script>
                <script>
                    function LOAD_CERTIFICATION_PROFILE(vCertDurationOrProfileID, isFrist)
                    {
                        $("#idHiddenCerDurationOrProfileID").val(vCertDurationOrProfileID);
                        $.ajax({
                            type: "post",
                            url: "../JSONCommon",
                            data: {
                                idParam: 'loadcert_profile_frist',
                                vCertDurationOrProfileID: vCertDurationOrProfileID
                            },
                            cache: false,
                            success: function (html)
                            {
                                if (html.length > 0)
                                {
                                    var obj = JSON.parse(html);
                                    if (obj[0].Code === "0")
                                    {
                                        if('<%= strViewAmountFrist%>' === "0")
                                        {
                                            if(isFrist !== "1") {
                                                if('<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_RENEWAL)
                                                {
                                                    $("#FEE_AMOUNT").val(obj[0].RENEWAL_AMOUNT);
                                                    $("#HIDDEN_FEE_AMOUNT").val(obj[0].RENEWAL_AMOUNT);
                                                } else if('<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE)
                                                {
                                                    $("#FEE_AMOUNT").val(obj[0].AMOUNT);
                                                    $("#HIDDEN_FEE_AMOUNT").val(obj[0].AMOUNT);
                                                } else if('<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION)
                                                {
                                                    $("#FEE_AMOUNT").val(obj[0].AMOUNT);
                                                    $("#HIDDEN_FEE_AMOUNT").val(obj[0].AMOUNT);
                                                }
                                            }
                                        }
                                        $("#DURATION_FREE").val(obj[0].DURATION_FREE);
                                        LoadFormDNForPersonalCommon(vCertDurationOrProfileID);
                                    }
                                    else if (obj[0].Code === JS_EX_CSRF)
                                    {
                                        funCsrfAlert();
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
                            }
                        });
                        return false;
                    }
                    function LOAD_CERTIFICATION_AUTHORITY(objCA, idCertPurpose, idCSRF)
                    {
                        $("#idHiddenCerCA").val(objCA.split('###')[0]);
                        $("#idHiddenCerCoreSubject").val(objCA.split('###')[1]);
                        $.ajax({
                            type: "post",
                            url: "../JSONCommon",
                            data: {
                                idParam: 'loadcert_authority_ofapprove',
                                idCA: objCA.split('###')[0],
                                idCertPurpose: idCertPurpose,
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html)
                            {
                                if (html.length > 0)
                                {
                                    var cbxCERTIFICATION_PURPOSE = document.getElementById("CERTIFICATION_PURPOSE");
                                    removeOptions(cbxCERTIFICATION_PURPOSE);
                                    var obj = JSON.parse(html);
                                    if (obj[0].Code === "0")
                                    {
                                        for (var i = 0; i < obj.length; i++) {
                                            cbxCERTIFICATION_PURPOSE.options[cbxCERTIFICATION_PURPOSE.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                        }
                                        var idPurposeFrist = '<%=rs[0][0].CERTIFICATION_PURPOSE_ID%>';
                                        var varCertAttrID = '<%=intCERTIFICATION_ATTR_TYPE_ID%>';
                                        if(varCertAttrID === JS_STR_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                            || varCertAttrID === JS_STR_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                            || varCertAttrID === JS_STR_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                        {
                                            $("#idHiddenCerPurpose").val(idPurposeFrist);
                                            cbxCERTIFICATION_PURPOSE.value=idPurposeFrist;
                                            LOAD_CERTIFICATION_PURPOSE(objCA.split('###')[0], idPurposeFrist, idCSRF);
                                        } else {
                                            $("#idHiddenCerPurpose").val(obj[0].ID);
                                            LOAD_CERTIFICATION_PURPOSE(objCA.split('###')[0], obj[0].ID, idCSRF);
                                        }
                                        
                                    }
                                    else if (obj[0].Code === JS_EX_CSRF)
                                    {
                                        funCsrfAlert();
                                    }
                                    else if (obj[0].Code === JS_EX_LOGIN)
                                    {
                                        RedirectPageLoginNoSess(global_alert_login);
                                    }
                                    else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                    {
                                        RedirectPageLoginNoSess(global_alert_another_login);
                                    }
                                    else if (obj[0].Code === "1")
                                    {
                                        var cbxCERTIFICATION_DURATION = document.getElementById("CERTIFICATION_DURATION");
                                        removeOptions(cbxCERTIFICATION_DURATION);
                                        cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option("---", "");
                                    }
                                    else {
                                        funErrorAlert(global_errorsql);
                                    }
                                }
                            }
                        });
                        return false;
                    }
                    function LOAD_CERTIFICATION_PURPOSE(objCA, objPurpose, idCSRF)
                    {
                        var vFristPKIFormFactor = '<%= String.valueOf(intPKI_FORMFACTOR_ID)%>';
                        $("#idHiddenCerPurpose").val(objPurpose);
                        LOAD_PROFILE_BY_FORMFACTOR(objCA, objPurpose, vFristPKIFormFactor, idCSRF);
                    }
                    function LOAD_PROFILE_BY_FORMFACTOR(objCA, objPurpose, objFactor, idCSRF)
                    {
                        $.ajax({
                            type: "post",
                            url: "../JSONCommon",
                            data: {
                                idParam: 'loadcert_purpose',
                                idCA: objCA,
                                idPurpose: objPurpose,
                                idFactor: objFactor,
                                idAttrType: '<%= intCERTIFICATION_ATTR_TYPE_ID%>',
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html)
                            {
                                if (html.length > 0)
                                {
                                    var cbxCERTIFICATION_DURATION = document.getElementById("CERTIFICATION_DURATION");
                                    removeOptions(cbxCERTIFICATION_DURATION);
                                    var obj = JSON.parse(html);
                                    if (obj[0].Code === "0")
                                    {
                                        $("#idHiddenCerDurationOrProfileID").val(obj[0].ID);
                                        var vProfileIDTransfer = obj[0].ID;
                                        for (var i = 0; i < obj.length; i++) {
                                            cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                        }
                                        LOAD_CERTIFICATION_DURATION(vProfileIDTransfer, idCSRF);
                                    }
                                    else if (obj[0].Code === JS_EX_CSRF)
                                    {
                                        funCsrfAlert();
                                    }
                                    else if (obj[0].Code === JS_EX_LOGIN)
                                    {
                                        RedirectPageLoginNoSess(global_alert_login);
                                    }
                                    else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                    {
                                        RedirectPageLoginNoSess(global_alert_another_login);
                                    }
                                    else if (obj[0].Code === "1")
                                    {
                                        cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option("---", "");
                                    }
                                    else {
                                        funErrorAlert(global_errorsql);
                                    }
                                }
                            }
                        });
                        return false;
                    }
                    function LOAD_CERTIFICATION_DURATION(objProfile, idCSRF)
                    {
                        LOAD_CERTIFICATION_PROFILE(objProfile, "0");
                    }
                    function LoadDNCity(cbxCity, vValueST)
                    {
                        $.ajax({
                            type: "post",
                            url: "../JSONCommon",
                            data: {
                                idParam: 'listcitycombobox',
                                CsrfToken: '<%= anticsrf%>'
                            },
                            cache: false,
                            success: function (html)
                            {
                                var cbxTemplateCity = document.getElementById(cbxCity);
                                removeOptions(cbxTemplateCity);
                                if (html.length > 0)
                                {
                                    var obj = JSON.parse(html);
                                    if (obj[0].Code === "0")
                                    {
                                        if(vValueST === "") {
                                            cbxTemplateCity.options[cbxTemplateCity.options.length] = new Option("", "");
                                        }
                                        for (var i = 0; i < obj.length; i++) {
                                            cbxTemplateCity.options[cbxTemplateCity.options.length] = new Option(obj[i].cityprovincedesc, obj[i].cityprovinceId);
                                            if(vValueST === obj[i].cityprovinceId)
                                            {
                                                $("#" +cbxCity+ " option[value='" + vValueST + "']").attr("selected", "selected");
                                            }
                                        }
                                    }
                                    else if (obj[0].Code === JS_EX_CSRF)
                                    {
                                        funCsrfAlert();
                                    } else if (obj[0].Code === JS_EX_LOGIN)
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
                            }
                        });
                        return false;
                    }
                    function LoadFormDNForPersonalCommon(idCertType, idCSRF)
                    {
                        $.ajax({
                            type: "post",
                            url: "../JSONCommon",
                            data: {
                                idParam: 'loadcert_profile_list_new',
                                vCertDurationOrProfileID: idCertType
                            },
                            cache: false,
                            success: function (html)
                            {
                                if (html.length > 0)
                                {
                                    var obj = JSON.parse(html);
                                    $("#idDivViewComponentDN").empty();
                                    $("#idViewSanInfo").empty();
                                    if (obj[0].Code === "0")
                                    {
                                        $("#idDivViewComponentDN").css("display", "");
                                        $("#idViewSanComponent").css("display", "");
                                        $("#idViewSanInfo").css("display", "");
                                        var vContent = "";
                                        var vSanContent = "";
                                        var localStoreRequired = new Array();
                                        var localStoreInput = new Array();
                                        var localStoreSanInput = new Array();
                                        var localStoreInputID_OnInput = "";
                                        var localStoreUID_Info = "";
                                        var vDNFromDB = document.getElementById("idHiddenDN").value;
                                        var vDisabledForm = '<%= sDisabledAgency%>';
                                        var isRepresenseEnabled = '<%=sRepresentEnabled%>';
                                        var caLoadEnabled = '<%=isCALoad%>';
                                        var vReadOnlyForm = '';
                                        if(vDisabledForm === "disabled"){vReadOnlyForm = "readonly";}
                                        JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID = JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID.replace("1","");
                                        JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID = JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID.replace("1","");
                                        JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID = JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID.replace("s","");
                                        JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID = JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID.replace("s","");
                                        if('<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                                            if(localStorage.getItem("loadChangeKeyOption") === "1") {
                                                vDisabledForm = ""; vReadOnlyForm="";
                                            } else {
                                                vDisabledForm = 'disabled';vReadOnlyForm = "readonly";
                                            }
                                        }
                                        var booHasUIDCompany = false;
                                        for (var i = 0; i < obj.length; i++) {
                                            if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY)
                                            {
                                                booHasUIDCompany = true;
                                                break;
                                            }
                                        }
                                        var booHasUIDPersonal = false;
                                        for (var i = 0; i < obj.length; i++) {
                                            if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL)
                                            {
                                                booHasUIDPersonal = true;
                                                console.log("booHasUIDCompany-CN: true");
                                                break;
                                            }
                                        }
                                        if(booHasUIDCompany === true)
                                        {
                                            var vDisabledForm_Info = vDisabledForm;
                                            if('<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                || '<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                || '<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                            {
                                                vDisabledForm_Info = "disabled";
                                            }
                                            var vContentButton_MST_Radio = "";
                                            if(isRepresenseEnabled === "1") {
                                                vContentButton_MST_Radio = "<div class='col-sm-13' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                        <div class='form-group'><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'>";
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID
                                                    +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID+'" '+vDisabledForm_Info+' class="form-control123" onlick="OnChangeComboboxMST();"></select>';
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + "</div><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;margin-right:0px;padding-right:0;'><input class='form-control123' type='text' "+vDisabledForm_Info+" id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID + "' />";
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + "</div></div><div class='col-sm-6'></div></div></div>";
                                            } else {
                                                vContentButton_MST_Radio = "<div class='col-sm-12' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                    <div class='form-group'><div class='col-sm-3' style='padding-left: 0px;margin-left: 0px;'>";
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID
                                                    +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID+'" '+vDisabledForm_Info+' class="form-control123" onlick="OnChangeComboboxMST();"></select>';
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + "</div><div class='col-sm-9' style='padding-right: 0px;'><input class='form-control123' type='text' "+vDisabledForm_Info+" id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID + "' />";
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + "</div></div></div>";
                                            }
                                            vContent = vContent + vContentButton_MST_Radio;
                                            $("#idDivViewComponentDN").append(vContent);
                                            var cbxCompany = document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID);
                                            removeOptions(cbxCompany);
                                            var vUID_CODE = "";
                                            for (var i = 0; i < obj.length; i++) {
                                                if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY)
                                                {
                                                    if(vUID_CODE === "") {
                                                        vUID_CODE = obj[i].SubjectDNAttrCode;
                                                    }
                                                    if(caLoadEnabled === JS_IS_WHICH_ABOUT_CA_ICA) {
                                                        cbxCompany.options[cbxCompany.options.length] = new Option(obj[i].SubjectDNAttrDesc, obj[i].SubjectDNAttrPreFix);
                                                    } else{
                                                        cbxCompany.options[cbxCompany.options.length] = new Option(obj[i].SubjectDNAttrDesc + ' - ' + obj[i].SubjectDNAttrPreFix, obj[i].SubjectDNAttrPreFix);
                                                    }
                                                }
                                            }
                                            localStoreUID_Info = localStoreUID_Info + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID + "###" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID+ "###" + vUID_CODE + ",";
                                            vContent = "";
                                        }
                                        if(booHasUIDPersonal === true)
                                        {
                                            var vDisabledForm_Info = vDisabledForm;
                                            if('<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                || '<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                || '<%= intCERTIFICATION_ATTR_TYPE_ID%>' === JS_STR_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                            {
                                                vDisabledForm_Info = "disabled";
                                            }
                                            var vContentButton_CMND_Radio = "";
                                            if(isRepresenseEnabled === "1") {
                                                vContentButton_CMND_Radio = "<div class='col-sm-13' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                        <div class='form-group'><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'>";
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID
                                                    +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID+'" '+vDisabledForm_Info+' class="form-control123"></select>';
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;margin-right:0px;padding-right:0;'><input class='form-control123' "+vDisabledForm_Info+" type='text' id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID + "' />";
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div></div><div class='col-sm-6'></div></div></div>";
                                            } else {
                                                vContentButton_CMND_Radio = "<div class='col-sm-12' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                    <div class='form-group'><div class='col-sm-3' style='padding-left: 0px;margin-left: 0px;'>";
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID
                                                    +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID+'" '+vDisabledForm_Info+' class="form-control123"></select>';
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div><div class='col-sm-9' style='padding-right: 0px;'><input class='form-control123' "+vDisabledForm_Info+" type='text' id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID + "' />";
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div></div></div>";
                                            }
                                            vContent = vContent + vContentButton_CMND_Radio;
                                            $("#idDivViewComponentDN").append(vContent);
                                            var cbxPersonal = document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID);
                                            removeOptions(cbxPersonal);
                                            var vUID_CODE = "";
                                            for (var i = 0; i < obj.length; i++) {
                                                if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL)
                                                {
                                                    if(vUID_CODE === "") {
                                                        vUID_CODE = obj[i].SubjectDNAttrCode;
                                                    }
                                                    if(caLoadEnabled === JS_IS_WHICH_ABOUT_CA_ICA) {
                                                        cbxPersonal.options[cbxPersonal.options.length] = new Option(obj[i].SubjectDNAttrDesc, obj[i].SubjectDNAttrPreFix);
                                                    } else{
                                                        cbxPersonal.options[cbxPersonal.options.length] = new Option(obj[i].SubjectDNAttrDesc + ' - ' + obj[i].SubjectDNAttrPreFix, obj[i].SubjectDNAttrPreFix);
                                                    }
                                                }
                                            }
                                            localStoreUID_Info = localStoreUID_Info + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID + "###" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID + "###" + vUID_CODE + ",";
                                            vContent = "";
                                        }
                                        for (var i = 0; i < obj.length; i++) {
                                            if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD)
                                            {
                                                var vLabelRequired = "";
                                                if (obj[i].IsRequired === '1')
                                                {
                                                    var vInputRequireID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID;
                                                    if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD) {
                                                        vLabelRequired = '<label class="CssRequireField">' + global_fm_require_label + '</label>';
                                                        localStoreRequired.push(vInputRequireID + '###' + obj[i].SubjectDNAttrDesc + '###' + obj[i].SubjectDNAttrPreFix);
                                                    }
                                                }
                                                var vInputID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID;
                                                localStoreInput.push(vInputID + "###" + obj[i].SubjectDNAttrCode + "###" + obj[i].SubjectDNAttrPreFix + "###" + obj[i].SubjectDNAttrCNType + "###" + obj[i].SubjectDNAttrDesc);
                                                if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE)
                                                {
                                                    vContent += '<div>' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                        '<select '+vDisabledForm+' class="form-control123" id="' + vInputID + '"></select>' +
                                                        '</div>' +
                                                        '</div>';
                                                    var sValuePushDB = JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE + "=" + obj[i].SubjectDNAttrPreFix;
                                                    var indexTag = vDNFromDB.indexOf(sValuePushDB);
                                                    if(indexTag !== -1)
                                                    {
                                                        var vDataInputID = '<%= strCITY_PROVINCE_ID%>';
                                                        LoadDNCity(vInputID, vDataInputID);
                                                    } else {
                                                        LoadDNCity(vInputID, "");
                                                    }
                                                } else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_PHONE)
                                                {
                                                    vContent += '<div >' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                        '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID + '" onblur="autoTrimTextField("' + vInputID + '", this.value);" />' +
                                                        '</div>' +
                                                        '</div>';
                                                }
                                                else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COUNTRY)
                                                {
                                                    var displayCountry = "";
                                                    if(isRepresenseEnabled === "1"){displayCountry = "display:none;";}
                                                    vContent += '<div style="'+displayCountry+'">' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                        '<input disabled class="form-control123" type="text" id="' + vInputID + '" />' +
                                                        '</div>' +
                                                        '</div>';
                                                } else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                                                {
                                                    if(isRepresenseEnabled === "1") {
                                                        vContent += '<div>' +
                                                            '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                            '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                            '<input '+ vReadOnlyForm +' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                            '</div>' +
                                                            '</div>';
                                                    } else {
                                                        vContent += '<div class="form-group" style="clear:both;">' +
                                                            '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                            '<div class="col-sm-10" style="padding-right: 10px;">'+
                                                            '<input '+ vReadOnlyForm +' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                            '</div>' +
                                                            '</div>';
                                                    }
                                                } else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_LOCALITY)
                                                {
                                                    vContent += '<div class="form-group" style="clear:both;">' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-10" style="padding-right: 10px;">'+
                                                        '<input '+ vReadOnlyForm +' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                        '</div>' +
                                                        '</div>';
                                                } else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                {
                                                    vContent += '<div>' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                        '<input '+vReadOnlyForm+' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                        '</div>' +
                                                        '</div>';
                                                } else
                                                {
                                                    vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                    vContent += '<div>' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                        '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                        '</div>' +
                                                        '</div>';
                                                }
                                                if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_EMAIL)
                                                {
                                                    localStoreInputID_OnInput = localStoreInputID_OnInput + JS_STR_COMPONENT_DN_VALUE_EMAIL + "###" + vInputID + ", ";
                                                }
                                            } else if (obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD_SAN)
                                            {
                                                var vLabelRequired = "";
                                                if (obj[i].IsRequired === '1')
                                                {
                                                    var vInputRequireID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID;
                                                    if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD_SAN) {
                                                        vLabelRequired = '<label class="CssRequireField">' + global_fm_require_label + '</label>';
                                                        localStoreRequired.push(vInputRequireID + '###' + obj[i].SubjectDNAttrDesc + '###' + obj[i].SubjectDNAttrPreFix);
                                                    }
                                                }
                                                var vInputID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID;
                                                localStoreSanInput.push(vInputID + "###" + obj[i].SubjectDNAttrCode + "###" + obj[i].SubjectDNAttrPreFix + "###" + obj[i].SubjectDNAttrCNType + "###" + obj[i].SubjectDNAttrDesc);
                                                vSanContent += '<div>' +
                                                    '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                    '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                    '<input class="form-control123" '+vDisabledForm+' type="text" id="' + vInputID + '" />' +
                                                    '</div>' +
                                                    '</div>';
                                                if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_E_SAN)
                                                {
                                                    localStoreInputID_OnInput = localStoreInputID_OnInput + JS_STR_COMPONENT_DN_VALUE_E_SAN + "###" + vInputID + ", ";
                                                }
                                            }
                                        }
                                        if(isRepresenseEnabled === "1") {
                                            vContent += '<div id="idViewDivRegisterGPKD">' +
                                                '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + global_fm_address_GPKD + ' <label class="CssRequireField">' + global_fm_require_label + '</label></label> ' +
                                                '<div class="col-sm-4" style="padding-right: 10px;">'+
                                                '<input class="form-control123" "<%=sRepresentReadonly%>" type="text" id="registerAddressGPKD" value="<%=sRegisterAddressGPKD%>" name="registerAddressGPKD" />' +
                                                '</div>' +
                                                '</div>';
                                        } else {
                                            vContent += '<div class="col-sm-6" style="padding-left: 0px;display:none;" id="idViewDivRegisterGPKD">' +
                                                '<div class="form-group">' +
                                                '<label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + global_fm_address_GPKD + ' <label class="CssRequireField">' + global_fm_require_label + '</label></label> ' +
                                                '<div class="col-sm-7" style="padding-right: 0px;">'+
                                                '<input class="form-control123" "<%=sRepresentReadonly%>" type="text" id="registerAddressGPKD" name="registerAddressGPKD" />' +
                                                '</div>' +
                                                '</div>' +
                                                '</div>';
                                        }
                                        $("#idDivViewComponentDN").append(vContent);
                                        if(vSanContent !== "") {
                                            $("#idViewSanInfo").append(vSanContent);
                                        } else {
                                            $("#idViewSanComponent").css("display", "none");
                                            $("#idViewSanInfo").css("display", "none");
                                        }
                                        var certType = '<%=rs[0][0].CERTIFICATION_PURPOSE_ID%>';
                                        if(certType === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
                                            $("#idViewDivRegisterGPKD").css("display", "none");
                                        } else {
                                            if(isRepresenseEnabled === "1") {
                                                $("#idViewDivRegisterGPKD").css("display", "");
                                            }
                                        }
                                        if(vDNFromDB !== "") {
                                            var vDNFromFilter = "";
                                            var countUID = 0;
                                            if(booHasUIDCompany === true || booHasUIDPersonal === true) {
                                                var vDNFromDBSplit = vDNFromDB.split(',');
                                                for (var iDN = 0; iDN < vDNFromDBSplit.length; iDN++) {
                                                    if(sSpace(vDNFromDBSplit[iDN]).indexOf(JS_STR_COMPONENT_DN_VALUE_UID) !== -1) {
                                                        vDNFromFilter = vDNFromFilter + vDNFromDBSplit[iDN] + ",";
                                                        countUID = countUID + 1;
                                                    }
                                                }
                                            }
                                            var intDNFilterSub = vDNFromFilter.lastIndexOf(',');
                                            vDNFromFilter = vDNFromFilter.substring(0, intDNFilterSub);
                                            if(booHasUIDCompany === true) {
                                                if(countUID === 1) {
                                                    for (var i = 0; i < obj.length; i++) {
                                                        if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY)
                                                        {
                                                            var sValuePushDB = obj[i].SubjectDNAttrCode + "=" + obj[i].SubjectDNAttrPreFix;
                                                            if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_TITLE + "=")
                                                            {
                                                                sValuePushDB = " " + sValuePushDB;
                                                            }
                                                            var indexTag = vDNFromFilter.indexOf(sValuePushDB);
                                                            if(indexTag !== -1) {
                                                                var sValuePushDBDataFrist = vDNFromFilter.substring(indexTag, vDNFromFilter.length);
                                                                var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                                $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                                $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).val(obj[i].SubjectDNAttrPreFix);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                if(countUID === 2) {
                                                    for (var i = 0; i < obj.length; i++) {
                                                        if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY)
                                                        {
                                                            var sValuePushDB = obj[i].SubjectDNAttrCode + "=" + obj[i].SubjectDNAttrPreFix;
                                                            if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_TITLE + "=")
                                                            {
                                                                sValuePushDB = " " + sValuePushDB;
                                                            }
                                                            var indexTag = vDNFromFilter.indexOf(sValuePushDB);
                                                            if(indexTag !== -1) {
                                                                var sValuePushDBDataFrist = vDNFromFilter.substring(indexTag, vDNFromFilter.length);
                                                                var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                                $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                                $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).val(obj[i].SubjectDNAttrPreFix);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if(booHasUIDPersonal === true) {
                                                if(countUID === 1) {
                                                    for (var i = 0; i < obj.length; i++) {
                                                        if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL) {
                                                            var sValuePushDB = obj[i].SubjectDNAttrCode + "=" + obj[i].SubjectDNAttrPreFix;
                                                            if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_TITLE + "=") {
                                                                sValuePushDB = " " + sValuePushDB;
                                                            }
                                                            var indexTag = vDNFromFilter.indexOf(sValuePushDB);
                                                            if(indexTag !== -1) {
                                                                var sValuePushDBDataFrist = vDNFromFilter.substring(indexTag, vDNFromFilter.length);
                                                                var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                                $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                                $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).val(obj[i].SubjectDNAttrPreFix);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                if(countUID === 2) {
                                                    for (var i = 0; i < obj.length; i++) {
                                                        if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL) {
                                                            var sValuePushDB = obj[i].SubjectDNAttrCode + "=" + obj[i].SubjectDNAttrPreFix;
                                                            if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_TITLE + "=") {
                                                                sValuePushDB = " " + sValuePushDB;
                                                            }
                                                            var indexTag = vDNFromFilter.indexOf(sValuePushDB);
                                                            if(indexTag !== -1) {
                                                                var sValuePushDBDataFrist = vDNFromFilter.substring(indexTag, vDNFromFilter.length);
                                                                var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                                $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                                $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).val(obj[i].SubjectDNAttrPreFix);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            var sInputOU = "";
                                            for (var i = 0; i < obj.length; i++) {
                                                var vInputID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID;
                                                var vInputSanOperation = obj[i].SubjectDNAttrCode;
                                                if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD)
                                                {
                                                    var sValuePushDB = obj[i].SubjectDNAttrCode + "=" + obj[i].SubjectDNAttrPreFix;
                                                    if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_TITLE + "=")
                                                    {
                                                        sValuePushDB = " " + sValuePushDB;
                                                    }
                                                    var indexTag = vDNFromDB.indexOf(sValuePushDB);
                                                    if(indexTag !== -1) {
                                                        if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE + "=") {
                                                        } else if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_PHONE + "=") {
                                                            var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                            var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                            $("#" + vInputID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                        } else if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_ORGANUNIT + "=") {
                                                            if('<%= allowTwoOU%>' === '1')
                                                            {
                                                                sInputOU = sInputOU + vInputID + "###";
                                                            } else {
                                                                var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                                var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                                $("#" + vInputID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                            }
                                                        } else {
                                                            if(vInputID.indexOf(JS_STR_COMPONENT_DN_VALUE_UID) !== -1)
                                                            {
                                                                vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                            }
                                                            var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                            var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                            $("#" + vInputID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                        }
                                                    }
                                                }
                                                else if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD_SAN)
                                                {
                                                    var vSanDataDB = '<%= sSanDataDB%>';
                                                    if(vSanDataDB !== "")
                                                    {
                                                        var sSanDataSplit1 = vSanDataDB.split('@@@');
                                                        for (var iSan = 0; iSan < sSanDataSplit1.length; iSan++) {
                                                            var sSanDataSplit2 = sSpace(sSanDataSplit1[iSan].split('###')[0]);
                                                            if(sSanDataSplit2 !== "")
                                                            {
                                                                if(sSanDataSplit2 === vInputSanOperation)
                                                                {
                                                                    $("#" + vInputID).val(sSpace(sSanDataSplit1[iSan].split('###')[1]));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if('<%= allowTwoOU%>' === '1')
                                            {
                                                if(sInputOU !== "")
                                                {
                                                    var sValueOU = "";
//                                                    for (var p = 0; p < obj.length; p++) {
//                                                        if(obj[p].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD) {
                                                            //var sValuePushDB = obj[p].SubjectDNAttrCode + "=" + obj[p].SubjectDNAttrPreFix;
                                                            var str_array = vDNFromDB.split(',');
                                                            for(var h = 0; h < str_array.length; h++)
                                                            {
                                                                var a = sSpace(str_array[h]);
                                                                if(a.split('=')[0] === 'OU')
                                                                {
                                                                    sValueOU = sValueOU + a.split('=')[1] + "###";
                                                                }
                                                            }
//                                                        }
//                                                    }
                                                    var intSub = sInputOU.lastIndexOf('###');
                                                    sInputOU = sInputOU.substring(0, intSub);
                                                    var sInputOUArray = sInputOU.split('###');
                                                    if(sInputOUArray.length > 0) {
                                                        if(sInputOUArray.length === 1) {
                                                            $("#"+sInputOU.split('###')[0]).val(sValueOU.split('###')[0]);
                                                        }
                                                        if(sInputOUArray.length === 2){
                                                            $("#"+sInputOU.split('###')[0]).val(sValueOU.split('###')[0]);
                                                            $("#"+sInputOU.split('###')[1]).val(sValueOU.split('###')[1]);
                                                        }
                                                        if(sInputOUArray.length === 3){
                                                            $("#"+sInputOU.split('###')[0]).val(sValueOU.split('###')[0]);
                                                            $("#"+sInputOU.split('###')[1]).val(sValueOU.split('###')[1]);
                                                            $("#"+sInputOU.split('###')[2]).val(sValueOU.split('###')[2]);
                                                        }
                                                        if(sInputOUArray.length === 4){
                                                            $("#"+sInputOU.split('###')[0]).val(sValueOU.split('###')[0]);
                                                            $("#"+sInputOU.split('###')[1]).val(sValueOU.split('###')[1]);
                                                            $("#"+sInputOU.split('###')[2]).val(sValueOU.split('###')[2]);
                                                            $("#"+sInputOU.split('###')[3]).val(sValueOU.split('###')[3]);
                                                        }
                                                    }
                                                    /*if(sInputOU.split('###')[0] !== "")
                                                    {
                                                        $("#"+sInputOU.split('###')[0]).val(sValueOU.split('###')[0]);
                                                    }
                                                    if(sInputOU.split('###')[1] !== "")
                                                    {
                                                        $("#"+sInputOU.split('###')[1]).val(sValueOU.split('###')[1]);
                                                    }*/
                                                }
                                            }
                                            localStorage.setItem("localStoreRequiredPersonal", localStoreRequired);
                                            localStorage.setItem("localStoreInputPersonal", localStoreInput);
                                            localStorage.setItem("localStoreSanInputPersonal", localStoreSanInput);
                                            localStorage.setItem("localStoreUID_Info", localStoreUID_Info);
                                            if(localStoreInputID_OnInput !== null && localStoreInputID_OnInput !== "null" && localStoreInputID_OnInput !== "")
                                            {
                                                if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE
                                                    || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_CODE_SIGNING)
                                                {
                                                    var idCNTemp="";
                                                    var idOTemp = "";
                                                    var sListInputCheckID_Info = localStoreInputID_OnInput.split(',');
                                                    for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                                                        if(sSpace(sListInputCheckID_Info[i].split('###')[0]) === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                        {
                                                            idOTemp = sSpace(sListInputCheckID_Info[i].split('###')[1]);
                                                        }
                                                        if(sSpace(sListInputCheckID_Info[i].split('###')[0]) === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                                                        {
                                                            idCNTemp = sSpace(sListInputCheckID_Info[i].split('###')[1]);
                                                        }
                                                    }
                                                    if(idCNTemp !== "" && idOTemp !== "")
                                                    {
                                                        $("#"+idOTemp).prop("readonly", true);
                                                        $("#"+idCNTemp).on('input',function(e){
                                                            OnBlurCompany(idOTemp, this.value);
                                                        });
                                                    }
                                                }
                                                var idE_SUBJECTTemp="";
                                                var idE_SANTemp = "";
                                                var sListInputCheckID_Info = localStoreInputID_OnInput.split(',');
                                                for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                                                    if(sSpace(sListInputCheckID_Info[i].split('###')[0]) === JS_STR_COMPONENT_DN_VALUE_E_SAN)
                                                    {
                                                        idE_SANTemp = sSpace(sListInputCheckID_Info[i].split('###')[1]);
                                                    }
                                                    if(sSpace(sListInputCheckID_Info[i].split('###')[0]) === JS_STR_COMPONENT_DN_VALUE_EMAIL)
                                                    {
                                                        idE_SUBJECTTemp = sSpace(sListInputCheckID_Info[i].split('###')[1]);
                                                    }
                                                }
                                                if(idE_SUBJECTTemp !== "" && idE_SANTemp !== "")
                                                {
                                                    document.getElementById(idE_SUBJECTTemp).oninput = function() { OnBlurCompany(idE_SANTemp, this.value);};
                                                }
                                            }
                                            if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
                                                $("#idLblTitleRepresent").text(cert_title_register_owner);
                                            } else {
                                                $("#idLblTitleRepresent").text(global_fm_representative_legal);
                                            }
                                        }
                                    }
                                    else if (obj[0].Code === JS_EX_CSRF)
                                    {
                                        funCsrfAlert();
                                    } else if (obj[0].Code === JS_EX_LOGIN)
                                    {
                                        RedirectPageLoginNoSess(global_alert_login);
                                    }
                                    else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                    {
                                        RedirectPageLoginNoSess(global_alert_another_login);
                                    }
                                    else {
                                        $("#idDivViewComponentDN").css("display", "none");
                                        $("#idViewSanComponent").css("display", "none");
                                        $("#idViewSanInfo").css("display", "none");
                                        funErrorAlert(global_errorsql);
                                    }
                                }
                            }
                        });
                        return false;
                    }
                    
                    function OnBlurCompany(objInput, objValue)
                    {
                        $("#"+objInput).val(objValue);
                    }
                </script>
                
                <%
                    if("1".equals(isActiveSignServer) && intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN
                        && rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED) {
                %>
                <fieldset class="scheduler-border" style="clear: both;">
                    <legend class="scheduler-border" id="idLblTitleConfirmCusInfo"></legend>
                    <script>$("#idLblTitleConfirmCusInfo").text(global_fm_confirm_customer);</script>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleHSMConfirmActive" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <select name="HSM_CONFIRM_DETAIL" id="HSM_CONFIRM_DETAIL" class="form-control123" disabled>
                                    <option value="0" <%= rs[0][0].ACTIVATED_ENABLED == 0 ? "selected" : ""%> id="idLblTitleHSMConfirmActive0"></option>
                                    <option value="1" <%= rs[0][0].ACTIVATED_ENABLED == 1 ? "selected" : ""%> id="idLblTitleHSMConfirmActive1"></option>
                                    <option value="2" <%= rs[0][0].ACTIVATED_ENABLED == 2 ? "selected" : ""%> id="idLblTitleHSMConfirmActive2"></option>
                                </select>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleHSMConfirmActive").text(global_fm_confirm);
                            $("#idLblTitleHSMConfirmActive0").text(hsm_confirm_not_confirm);
                            $("#idLblTitleHSMConfirmActive1").text(hsm_confirm_has_confirm);
                            $("#idLblTitleHSMConfirmActive2").text(global_fm_button_decline);
                        </script>
                    </div>
                    <%
                        String sCONFIRMATION_HSM = rs[0][0].CONFIRMATION_PROPERTIES;
                        if(!"".equals(sCONFIRMATION_HSM)) {
                            boolean isConfirmEnabled = false;
                            if(rs[0][0].ACTIVATED_ENABLED == 2) {
                                isConfirmEnabled = true;
                            }
                            String sDeclineReason = "";
                            try {
                                ObjectMapper objectMapper = new ObjectMapper();
                                CERTIFICATION_PROPERTIES_JSON itemParsePush = objectMapper.readValue(sCONFIRMATION_HSM, CERTIFICATION_PROPERTIES_JSON.class);
                                if(itemParsePush.getAttributes().size() > 0) {
                                    for (int i = 0; i < itemParsePush.getAttributes().size(); i++) {
                                        if(EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getKey()).equals(Definitions.CONFIG_VALUE_HSM_CONFIRM_DECLINE_REASON)){
                                            sDeclineReason = EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getValue());
                                            break;
                                        }
                                    }
                                }
                            } catch(Exception e){CommonFunction.LogExceptionServlet(null, "sCONFIRMATION_HSM: " + e.getMessage(), e);}
                            if(isConfirmEnabled == true) {
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleHSMConfirmReason" class="control-label col-sm-5" style="color: <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO) ? "red" : "#000000" %>; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="CONFIRM_REASON" value="<%= sDeclineReason%>" style="width: 65%;" class="form-control1234" readonly <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO) ? "style='color: red;font-weight: bold;'" : "" %>/>
                                <input id="btnReSendMailHSM" class="btn btn-info" type="button" onclick="reSendMailHSM('<%=String.valueOf(rs[0][0].ID)%>', '<%= anticsrf%>');" />
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleHSMConfirmReason").text(global_fm_decline_desc);
                            document.getElementById("btnReSendMailHSM").value = global_button_reconfirm;
                            function reSendMailHSM(id, idCSRF)
                            {
                                $('body').append('<div id="over"></div>');
                                $(".loading-gif").show();
                                $.ajax({
                                    type: "post",
                                    url: "../ReqApproveDeclineCommon",
                                    data: {
                                        idParam: 'resendmailconfirmhsm',
                                        id: id,
                                        idPHONE_CONTRACT: $("#PHONE_CONTRACT").val(),
                                        idEMAIL_CONTRACT: $("#EMAIL_CONTRACT").val(),
                                        CsrfToken: idCSRF
                                    },
                                    cache: false,
                                    success: function (html)
                                    {
                                        var myStrings = sSpace(html).split('#');
                                        if (myStrings[0] === "0") {
                                            funSuccLocalAlert(sendmail_success);
                                        } else if (myStrings[0] === JS_EX_CSRF) {
                                            funCsrfAlert();
                                        } else if (myStrings[0] === JS_EX_LOGIN)
                                        {
                                            RedirectPageLoginNoSess(global_alert_login);
                                        }
                                        else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
                                        {
                                            RedirectPageLoginNoSess(global_alert_another_login);
                                        } else if (myStrings[1] === JS_STR_ERROR_CODE_99) {
                                            funErrorAlert(global_error_login_info);
                                        } else {
                                            funErrorAlert(global_errorsql);
                                        }
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                    }
                                });
                                return false;
                            }
                        </script>
                    </div>
                    <%
                            }
                        }
                    %>
                </fieldset>
                <%
                    }
                %>
                <%
                    CERTIFICATION[][] rsCertCicle = new CERTIFICATION[1][];
                    db.S_BO_CERTIFICATION_APPROVED_DETAIL_CYCLIFE_CERTIFICATE(String.valueOf(rs[0][0].ID), sessLanguageGlobal, rsCertCicle);
                    if(rsCertCicle != null && rsCertCicle[0].length > 0) {
                        int p=0;
                %>
                <fieldset class="scheduler-border">
                    <legend class="scheduler-border" id="idLblTitleGroupCertHis"></legend>
                    <script>$("#idLblTitleGroupCertHis").text(global_fm_cert_circlelife);</script>
                    <div class="table-responsive">
                    <table class="table table-bordered table-striped projects" id="mtTokenHisCert">
                        <thead>
                        <th id="idLblTitleTableHisSTT"></th>
                        <th id="idLblTitleTableHisSevice"></th>
                        <th id="idLblTitleTableHisState"></th>
                        <th id="idLblTitleTableHisBranch"></th>
                        <th id="idLblTitleTableHisUser"></th>
                        <th id="idLblTitleTableHisCreateDT"></th>
                        <th id="idLblTitleTableHisOperatedDT"></th>
                        <th id="idLblTitleTableHisRevokeDT"></th>
                        <script>
                            $("#idLblTitleTableHisSTT").text(global_fm_STT);
                            $("#idLblTitleTableHisSevice").text(cert_fm_type_request);
                            $("#idLblTitleTableHisState").text(global_fm_Status);
                            $("#idLblTitleTableHisBranch").text(global_fm_Branch);
                            $("#idLblTitleTableHisUser").text(global_fm_user_create);
                            $("#idLblTitleTableHisCreateDT").text(global_fm_date_create);
                            $("#idLblTitleTableHisOperatedDT").text(global_fm_valid_cert);
                            $("#idLblTitleTableHisRevokeDT").text(global_fm_date_revoke);
                        </script>
                        </thead>
                        <tbody>
                            <%
                                for(CERTIFICATION rsCert1 : rsCertCicle[0]) {
                                    String sMBRANCH_DESCST = rsCert1.BRANCH_DESC;
                                    p=p+1;
                            %>
                            <tr>
                                <td><%= p%></td>
                                <td><%= rsCert1.SERVICE_TYPE_DESC %></td>
                                <td><%= rsCert1.CERTIFICATION_STATE_DESC %></td>
                                <td><%= sMBRANCH_DESCST%></td>
                                <td><%= rsCert1.CREATED_BY %></td>
                                <td><%= rsCert1.CREATED_DT %></td>
                                <td><%= rsCert1.OPERATED_DT %></td>
                                <td><%= rsCert1.REVOKED_DT %></td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                    <div id="greenIP" style="margin: 5px 0 5px 0;"> </div>
                    </div>
                    <script>
                        if (parseInt('<%=p%>') > 0)
                        {
                            $('#greenIP').smartpaginator({totalrecords: parseInt('<%=p%>'), recordsperpage: 10, datacontainer: 'mtTokenHisCert', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
                        }
                    </script>
                </fieldset>
                <%}%>
                <%
                    if(!"".equals(sTOKEN_SN) && CommonFunction.checkViewTokenValid(sTOKEN_SN) == true)
                    {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleTokenSN" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="TOKEN_SN" readonly value="<%= sTOKEN_SN%>" class="form-control123">
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleTokenSN").text(token_fm_tokenid);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleBranchToken" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="BRANCH_TOKEN_DESC" readonly value="<%= rs[0][0].BRANCH_TOKEN_DESC%>" class="form-control123">
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleBranchToken").text(global_fm_Branch + ' (' + login_fm_token_ssl + ')');
                    </script>
                </div>
                <%
                    }
                %>
                <%
                    if(!"".equals(sVALUE))
                    {
                %>
                <%
                    if(!"".equals(sUserCreate)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCreateBy" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="CREATED_BY" readonly value="<%= sUserCreate%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleCreateBy").text(global_fm_user_request);
                    </script>
                </div>
                <%
                    }
                %>
                <%
                    if(!"".equals(sDateCreate)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCreateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="REQUEST_TIME" readonly value="<%= sDateCreate%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleCreateDate").text(global_fm_date_create);
                    </script>
                </div>
                <%
                    }
                %>
                <%
                    if(!"".equals(sUserApprove)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleModifiedBy" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="MODIFIED_BY" readonly value="<%= sUserApprove%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleModifiedBy").text(global_fm_user_approve_agency);
                    </script>
                </div>
                <%
                    }
                %>
                <%
                    if(!"".equals(sDateApprove)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleModifiedDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="MODIFIED_DT" readonly value="<%= sDateApprove%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleModifiedDate").text(global_fm_date_approve_agency);
                    </script>
                </div>
                <%
                    }
                %>
                <%
                    if(!"".equals(sUserCAApprove)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleModifiedByCA" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="MODIFIED_BY_CA" readonly value="<%= sUserCAApprove%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleModifiedByCA").text(global_fm_user_approve_ca);
                    </script>
                </div>
                <%
                    }
                %>
                <%
                    if(!"".equals(sDateCAApprove)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleModifiedDateCA" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="MODIFIED_DT_CA" readonly value="<%= sDateCAApprove%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleModifiedDateCA").text(global_fm_date_approve_ca);
                    </script>
                </div>
                <%
                    }
                    } else { }
                %>
            </form>
        </div>
        <div class="x_title" style="display: <%=!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "none" : ""%>">
            <h2></h2>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <%
                        if (rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT) {
                            if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                    %>
                    <%
                        if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ACCOUNTANT)) {
                            if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                {
                    %>
                    <input type="button" id="btnApproveFooter" class="btn btn-info" onclick="ApproveFormAgency('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnApproveFooter").value = global_fm_approve;</script>
                    <%
                            }
                        } else {
                            int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(sCERTIFICATION_ATTR_ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                            if(intApprove == 1) {
                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                {
                    %>
                                <input type="button" id="btnApproveFooter" class="btn btn-info" onclick="ApproveFormChildrenAgency('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                                <script>document.getElementById("btnApproveFooter").value = global_fm_approve;</script>
                    <%
                                    }
                                }
                            }
                        } else {
                            if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                    %>
                    <input type="button" id="btnApproveFooter" class="btn btn-info" onclick="ApproveFormAgency('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnApproveFooter").value = global_fm_approve;</script>
                    <%
                            } else {
                                int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(sCERTIFICATION_ATTR_ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                if(intApprove == 1) {
                    %>
                                    <input type="button" id="btnApproveFooter" class="btn btn-info" onclick="ApproveFormChildrenAgency('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                                    <script>document.getElementById("btnApproveFooter").value = global_fm_approve;</script>
                    <%
                                }
                            }
                        }
                    %>
                    <%
                        } else {
                            if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                            {
                    %>
                    <input type="button" id="btnApproveFooter" class="btn btn-info" onclick="ApproveFormCA('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnApproveFooter").value = global_fm_approve;</script>
                    <%
                        } else {
                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                    %>
                    <input type="button" id="btnApproveFooter" class="btn btn-info" onclick="ApproveFormCA('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnApproveFooter").value = global_fm_approve;</script>
                    <%
                                    }
                                }
                            }
                        } else if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED) {
                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                    %>
                    <%
                        if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_USER) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ACCOUNTANT)) {
                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                    %>
                    <input type="button" id="btnApproveFooter" class="btn btn-info" onclick="ApproveFormCA('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnApproveFooter").value = global_fm_approve;</script>
                    <%
                            }
                        } else {
                    %>
                    <input type="button" id="btnApproveFooter" class="btn btn-info" onclick="ApproveFormCA('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnApproveFooter").value = global_fm_approve;</script>
                    <%
                        }
                    %>
                    <%
                            }
                        } else {strViewAmountFrist = "1";}
                    %>
                    <%
                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            boolean isAllowFunctionApprove = false;
                            if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED) {
                                if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                    || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                    || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                                {
                                    isAllowFunctionApprove = true;
                                } else {
                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                    {
                                        isAllowFunctionApprove = true;
                                    }
                                }
                                if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                {
                                    if(intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE
                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED)
                                    {
                                        if(isAllowFunctionApprove == true)
                                        {
                                            String sViewFormReIssueSoft = "";
                                            if("1".equals(isActiveSignServer) && intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN){
                                                if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                    || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                                                    || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED
                                                    || (rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED
                                                    && rs[0][0].ACTIVATED_ENABLED == 0)) {
                                                    sViewFormReIssueSoft = "display:none;";
                                                }
                                            }
                    %>
                    <input type="button" id="btnIssueFooter" class="btn btn-info" style="<%=sViewFormReIssueSoft%>" onclick="FormReIssueSoft('<%= ids%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnIssueFooter").value = global_fm_button_issue;</script>
                    <%
                                        }
                                    }
                                }
                                if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                                    && (rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVOKED
                                    && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_AUTO_REVOKED)) {
                                    if(isAllowFunctionApprove == true)
                                    {
                    %>
                    <input type="button" id="btnReRevokeFooter" class="btn btn-info" onclick="FormReRevoke('<%= ids%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnReRevokeFooter").value = global_fm_approve;</script>
                    <input type="button" id="btnDeclineFooter" class="btn btn-info" onclick="popupDialogCertDecline('<%= sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(rs[0][0].BRANCH_ID)%>', '<%= String.valueOf(rs[0][0].CREATED_BY_ID)%>');" />
                    <script>document.getElementById("btnDeclineFooter").value = global_fm_button_decline;</script>
                    <%
                                    }
                                }
                                if((intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                    || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                    && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_PERMANENT_DISABLE
                                    && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                                    && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_PERMANENT_DISABLE
                                    && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE) {
                                    if(isAllowFunctionApprove == true)
                                    {
                    %>
                    <input type="button" id="btnReSuspendFooter" class="btn btn-info" onclick="FormReSuspend('<%= ids%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnReSuspendFooter").value = global_fm_approve;</script>
                    <%
                                    }
                                }
                                if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED
                                    && (rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_PERMANENT_DISABLE
                                    || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                                    || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_PERMANENT_DISABLE
                                    || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE))
                                {
                                    if(isAllowFunctionApprove == true)
                                    {
                    %>
                    <input type="button" id="btnReRecoveryFooter" class="btn btn-info" onclick="FormReRecovery('<%= ids%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnReRecoveryFooter").value = global_fm_approve;</script>
                    <%
                                    }
                                }
                            }
                        }
                    %>
                    <%
                        if (rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT) {
                            boolean isDeclineAgent = false;
                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                isDeclineAgent = true;
                            } else {
                                if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                    if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                        isDeclineAgent = true;
                                    } else {
                                        int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(sCERTIFICATION_ATTR_ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                        if(intApprove == 1) {
                                            isDeclineAgent = true;
                                        }
                                    }
                                } else {
                                    if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                            isDeclineAgent = true;
                                        }
                                    } else {
                                        int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(sCERTIFICATION_ATTR_ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                        if(intApprove == 1) {
                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                isDeclineAgent = true;
                                            }
                                        }
                                    }
                                }
                            }
                    %>
                    <%
                        if(isDeclineAgent == true) {
                    %>
                    <input type="button" id="btnDeclineFooter" class="btn btn-info" onclick="popupDialogCertDecline('<%= sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(rs[0][0].BRANCH_ID)%>', '<%= String.valueOf(rs[0][0].CREATED_BY_ID)%>');" />
                    <script>document.getElementById("btnDeclineFooter").value = global_fm_button_decline;</script>
                    <%
                        }
                    %>
                    <%
                        } else if(rs[0][0].CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_COMMITED
                            && rs[0][0].CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_DECLINED
                            && rs[0][0].CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED)
                        {
                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED
                                    && intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                { } else {
                    %>
                    <%
                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                        {
                    %>
                    <input type="button" id="btnDeclineFooter" class="btn btn-info" onclick="popupDialogCertDecline('<%= sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(rs[0][0].BRANCH_ID)%>', '<%= String.valueOf(rs[0][0].CREATED_BY_ID)%>');" />
                    <script>document.getElementById("btnDeclineFooter").value = global_fm_button_decline;</script>
                    <%
                        }
                    %>
                    <%
                                }
                            }
                        } else if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED)
                        {
                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                {
                    %>
                    <input type="button" id="btnDeclineFooter" class="btn btn-info" onclick="popupDialogCertDecline('<%= sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(rs[0][0].BRANCH_ID)%>', '<%= String.valueOf(rs[0][0].CREATED_BY_ID)%>');" />
                    <script>document.getElementById("btnDeclineFooter").value = global_fm_button_decline;</script>
                    <%
                                }
                            }
                        }
                    %>
                    <input id="btnCloseFooter" class="btn btn-info" type="button" onclick="closeForm();" />
                </li>
                <script>document.getElementById("btnCloseFooter").value = global_fm_button_close;</script>
            </ul>
            <div class="clearfix"></div>
        </div>
        <!-- Modal Cert Decline -->
        <div id="myModalCertDecline" class="modal fade" role="dialog">
            <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                 left: 0; height: 100%;" class="loading-gifCertDecline">
                <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
            </div>
            <div class="modal-dialog modal-800" id="myDialogCertDecline">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div id="contentCertDecline"></div>
                    </div>
                </div>
            </div>
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
        <script>
            $(document).ready(function () {
                var vsCertTypeFrist = '<%= strCERTIFICATION_PROFILE_ID%>';
                if(vsCertTypeFrist !== '')
                {
                    LoadFormDNForPersonalCommon(vsCertTypeFrist, '<%= anticsrf%>');
                }
            });
        </script>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
<!--        <script src="../js/moment.min_limit.js"></script>
        <script src="../js/daterangepicker_limit.js"></script>-->
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
