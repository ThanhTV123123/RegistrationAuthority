<%-- 
    Document   : CertificateShareReissue
    Created on : Dec 12, 2019, 5:47:06 PM
    Author     : USER
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="vn.ra.process.SessionUploadFileCert"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="vn.ra.utility.PropertiesContent"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<!DOCTYPE html>
<%
    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
    String sRepresentEnabled = LoadParamSystem.getParamStart(Definitions.CONFIG_REGISTER_REPRESENT_FORM_ENABLED);
    String sUpHighlighLabel = LoadParamSystem.getParamStart(Definitions.CONFIG_NEW_FILE_UPLOAD_HIGHLIGHTS_LABEL);
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
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            $(document).ready(function () {
                $('.loading-gif').hide();
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
            function UpdateForm(idCSRF)
            {
                var vEditAmount = "0";
                var vSTR_COMPONENT_DN_VALUE_COMMONNAME = "";
                var vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = "";
                var vSTR_COMPONENT_DN_VALUE_MST = "";
                var vSTR_COMPONENT_DN_VALUE_MNS = "";
                var vSTR_COMPONENT_DN_VALUE_CMND = "";
                var vSTR_COMPONENT_DN_VALUE_HC = "";
                var vSTR_COMPONENT_DN_VALUE_QD = "";
                var vSTR_COMPONENT_DN_VALUE_CCCD = "";
                var vSTR_COMPONENT_DN_VALUE_DEVICE = "";
                var vSTR_COMPONENT_DN_VALUE_DOMAIN_NAME = "";
                var vSTR_COMPONENT_DN_VALUE_PROVINCE_ID = "";
                var vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC = "";
                var vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT = "";
                var vSTR_COMPONENT_DN_VALUE_EMAIL_SAN = "";
                var vSTR_COMPONENT_DN_ID_EMAIL_SAN = "";
                var vSTR_COMPONENT_SAN = "";
                var vSTR_COMPONENT_DN_VALUE_PERSONAL_ID = "";
                var vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID = "";
                var vDNResult = "";
                if (!JSCheckEmptyField($("#CERTIFICATION_AUTHORITY").val()))
                {
                    $("#CERTIFICATION_AUTHORITY").focus();
                    funErrorAlert(policy_req_empty_choose + global_fm_ca);
                    return false;
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
                var caLoadEnabled = '<%=isCALoad%>';
                if(caLoadEnabled === JS_IS_WHICH_ABOUT_CA_ICA) {
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
                }
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
                    // CHECK REQUIRED FIELDS
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
                    // CHECK OUT REQUIRED FOR FORMAT PHONE, EMAIL
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
                    // CHECK OUT REQUIRED FOR FORMAT PHONE, EMAIL => SAN
                    if(localStorage.getItem("localStoreSanInputPersonal") !== "") {
                        var sListSanInputOutRequired = localStorage.getItem("localStoreSanInputPersonal").split(',');
                        for (var i = 0; i < sListSanInputOutRequired.length; i++) {
                            var idCheckOutRequired = sListSanInputOutRequired[i].split('###')[0];
                            if(idCheckOutRequired.indexOf(JS_STR_COMPONENT_DN_VALUE_E_SAN) !== -1)
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
                    
                    // OUTPUT STRING DN
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
                            // ADD_PLUS_CHARACTER
                            if (itemValue.indexOf("+") !== -1) {
                                itemValue = itemValue.replace('+' , '\\+');
                            }
                        }
                        if(itemValue !== "") {
                            vDNResult += sListInput[i].split('###')[1] + "=" + sListInput[i].split('###')[2] +
                                sSpace(itemValue) + ", ";
                        }
                    }
                    // GET UID VALUE
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
                    // OUTPUT STRING SAN
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
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../CertificateShareCommon",
                    data: {
                        idParam: 'reissuecert',
                        sID: $("#sID").val(),
                        CheckCHANGE_KEY: sCheckCHANGE_KEY_ENABLED,
                        CheckPRIVATE_KEY: sCheckPRIVATE_KEY_ENABLED,
                        CheckREVOKE_ENABLED: sCheckREVOKE_ENABLED,
                        CertProfileID: $("#idHiddenCerDurationOrProfileID").val(),
                        COMPONENT_SAN: vSTR_COMPONENT_SAN,
                        pPERSONAL_NAME: vSTR_COMPONENT_DN_VALUE_COMMONNAME,
                        pCOMPANY_NAME: vSTR_COMPONENT_DN_VALUE_COMPANY_NAME,
                        pDOMAIN_NAME: vSTR_COMPONENT_DN_VALUE_DOMAIN_NAME,
                        pTAX_CODE: vSTR_COMPONENT_DN_VALUE_MST,
                        pDECISION: vSTR_COMPONENT_DN_VALUE_QD,
                        pBUDGET_CODE: vSTR_COMPONENT_DN_VALUE_MNS,
                        pP_ID: vSTR_COMPONENT_DN_VALUE_CMND,
                        pCCCD: vSTR_COMPONENT_DN_VALUE_CCCD,
                        pPASSPORT: vSTR_COMPONENT_DN_VALUE_HC,
                        pDEVICE: vSTR_COMPONENT_DN_VALUE_DEVICE,
                        pPROVINCE_ID: vSTR_COMPONENT_DN_VALUE_PROVINCE_ID,
                        pPROVINCE_DESC: vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC,
                        pNEW_TOKEN_SN: "",
                        DN: vDNResult,
                        pENTERPRISE_ID: vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID,
                        pPERSONAL_ID: vSTR_COMPONENT_DN_VALUE_PERSONAL_ID,
                        PHONE_CONTRACT: $("#PHONE_CONTRACT").val(),
                        EMAIL_CONTRACT: $("#EMAIL_CONTRACT").val(),
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            localStorage.setItem("EDIT_CERTSHARE_REISSUE", $("#sID").val());
                            if (arr[2] === "1"){
                                pushNotificationApprove($("#sID").val(), $("#NEW_TOKEN_SN").val());
                            }
                            funSuccAlert(certlist_succ_reissue, "CertificateShareList.jsp");
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
                            funErrorAlert(global_error_wrong_agency);
                        }
                        else if (arr[0] === JS_EX_NOEXISTS_TOKEN)
                        {
                            funErrorAlert(global_error_noexists_token);
                        }
                        else if (arr[0] === JS_EX_STATUS_TOKEN)
                        {
                            funErrorAlert(global_error_token_status);
                        }
                        else if (arr[0] === "1")
                        {
                            funErrorAlert(global_error_request_exists);
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
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../UserCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'RefreshCertShareSess'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "CertificateShareList.jsp";
                        }
                        else
                        {
                            window.location = "CertificateShareList.jsp";
                        }
                    }
                });
                return false;
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
            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
            .table > thead > tr > th{border-bottom: none;}
            .btn{margin-bottom: 0px;}
        </style>
    </head>
    <body>
        <%            
        if (session.getAttribute("sUserID") != null) {
            ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Cert");
            String strCERTIFICATION_PROFILE_ID = "";
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
            String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
            CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessPolicyCert_Data");
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <%                                        CERTIFICATION[][] rs = new CERTIFICATION[1][];
            String strCertificate = "";
            String strIsCert = "0";
            try {
                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                if (EscapeUtils.IsInteger(ids) == true) {
                    db.S_BO_CERTIFICATION_DETAIL(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                    if (rs[0].length > 0) {
                        boolean isAccessAgencyPage = true;
                        if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            if(rs[0][0].SHARED_MODE == false) {
                                BRANCH[][] branchAccess = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                                isAccessAgencyPage = CommonFunction.checkBranchTreeInvalidCert(rs[0][0].BRANCH_ID, branchAccess);
//                                if (!String.valueOf(rs[0][0].BRANCH_ID).equals(SessUserAgentID)) {
//                                    isAccessAgencyPage = false;
//                                }
                            }
                        }
                        if (isAccessAgencyPage == true) {
                            int intPKI_FORMFACTOR_ID = rs[0][0].PKI_FORMFACTOR_ID;
                            String sTOKEN_SN = EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN);
                            String sCERTIFICATION_SN = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_SN);
                            String strEMAIL_CONTRACT = EscapeUtils.CheckTextNull(rs[0][0].EMAIL_CONTRACT);
                            String strPHONE_CONTRACT = EscapeUtils.CheckTextNull(rs[0][0].PHONE_CONTRACT);
                            String strCSR = EscapeUtils.CheckTextNull(rs[0][0].CSR);
                            strCertificate = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION);
                            if(!"".equals(strCertificate))
                            {
                                strIsCert = "1";
                            }
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
                            String strCREATED_DT = EscapeUtils.CheckTextNull(rs[0][0].CREATED_DT);
                            String strCREATED_BY = EscapeUtils.CheckTextNull(rs[0][0].CREATED_BY);
                            String strMODIFIED_DT = EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_DT);
                            String strMODIFIED_BY = EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_BY);
                            strCERTIFICATION_PROFILE_ID = String.valueOf(rs[0][0].CERTIFICATION_PROFILE_ID);
                            int strCITY_PROVINCE_ID = rs[0][0].CITY_PROVINCE_ID;
                            boolean isP12 = false;
                            if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                            {
                                if(rs[0][0].PRIVATE_KEY_ENABLED == true)
                                {
                                    isP12 = true;
                                    strIsCert = "0";
                                }
                            }
                            String sRegexPolicy = "";
                            String sArrayFileExten = "";
                            GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                            if (sessGeneralPolicy1[0].length > 0) {
                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
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
                            if(!"".equals(sRegexPolicy))
                            {
                                sREGEX_PHONE = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_PHONE);
                                sREGEX_EMAIL = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_EMAIL);
                            }
                            int intDuration = 0;
                            CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                            db.S_BO_CERTIFICATION_PROFILE_DETAIL(String.valueOf(rs[0][0].CERTIFICATION_PROFILE_ID), rsProfile);
                            if(rsProfile[0].length > 0) {
                                intDuration = rsProfile[0][0].DURATION;
                            }
                            String sPROPERTIES = EscapeUtils.CheckTextNull(rs[0][0].PROPERTIES);
                            String sSanDataDB = "";
                            if(!"".equals(sPROPERTIES)) {
                                ObjectMapper objectMapper = new ObjectMapper();
                                CERTIFICATION_PROPERTIES_JSON itemParsePush = objectMapper.readValue(sPROPERTIES, CERTIFICATION_PROPERTIES_JSON.class);
                                if(itemParsePush.getAttributes().size() > 0) {
                                    for (int i = 0; i < itemParsePush.getAttributes().size(); i++) {
                                        sSanDataDB = sSanDataDB + EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getKey()) + "###" + EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getValue()) + "@@@";
                                    }
                                }
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
            <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEdits"></span></h2>
            <script>$("#idLblTitleEdits").text(certlist_title_reissue);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <%
                        if(CommonFunction.checkHardTokenIDEnabled(rs[0][0].PKI_FORMFACTOR_ID) == true)
                        {
                            if(rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED
                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVISED
                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REISSUED
                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVOKED
                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED_EXPIRED
                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_STOPPED_OPERATION
                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_AUTO_REVOKED
                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVISED_KEEP_SN
                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED_KEEP_SN
                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_EXPIRED)
                            {
                    %>
                    <%
                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_REISSUE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                    %>
                    <input type="button" id="btnApprove" class="btn btn-info" onclick="UpdateForm('<%=anticsrf%>');" />
                    <script>document.getElementById("btnApprove").value = global_fm_button_add;</script>
                    <%
                                }
                            }
                        }
                    %>
                    <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                    <script>document.getElementById("btnClose").value = global_fm_button_close;</script>
                </li>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <form name="myname" method="post" class="form-horizontal">
                <input type="hidden" id="sID" name="sID" hidden="true" readonly="true" value="<%= rs[0][0].ID%>">
                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                <input type="hidden" name="idHiddenDN" id="idHiddenDN" value="<%=EscapeUtils.escapeHtmlDN(strDN)%>"/>                
                <%
                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                    String boCheckBackUpKey = "0";
                    String boCheckChangeKey = "0";
                    String boCheckRevoked = "0";
                    if (sessGeneralPolicy[0].length > 0) {
                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                        {
                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DEFAULT_PRIVATE_KEY_ENABLED))
                            {
                                boCheckBackUpKey = rsPolicy1.VALUE;
                            }
                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_GENERATE_NEW_KEY))
                            {
                                boCheckChangeKey = rsPolicy1.VALUE;
                            }
//                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_REVOKE_CERTIFICATE_AFTER_REISSUE_CHANGINFO))
                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_REVOKE_CERTIFICATE_AFTER_REISSUE))
                            {
                                boCheckRevoked = rsPolicy1.VALUE;
                            }
                        }
                    }
                %>
                <script>
                    $(document).ready(function () {
                        localStorage.setItem("loadChangeKeyOption", "1");
                        if('<%= boCheckChangeKey%>' === "0")
                        {
                            $("#idDivBackUpKey").css("display", "none");
                            localStorage.setItem("loadChangeKeyOption", "0");
                        }
                    });
                    function onChangeKey()
                    {
                        if ($("#idCheckChangeKey").is(':checked'))
                        {
                            $("#idCheckBackUpKey").attr("disabled", false);
                            $("#idCheckBackUpKeyClass").removeClass("disabled");
                            $('#idCheckRevoked').prop('checked', true);
                            $("#idCheckRevoked").attr("disabled", false);
                            $("#idCheckRevokedClass").removeClass("disabled");
                            localStorage.setItem("loadChangeKeyOption", "1");
                            LoadFormDNForPersonalCommon($("#CERTIFICATION_DURATION").val(), "");
                        } else {
                            $('#idCheckBackUpKey').prop('checked', true);
                            $('#idCheckRevoked').prop('checked', false);
                            $("#idCheckRevoked").attr("disabled", true);
                            $("#idCheckRevokedClass").addClass("disabled");
                            localStorage.setItem("loadChangeKeyOption", "0");
                            LoadFormDNForPersonalCommon($("#CERTIFICATION_DURATION").val(), "");
                        }
                    }
                </script>
                <%
                    boolean actionChangeKeyEnabled = true;
                    boolean actionBackUpKeyEnabled = true;
                    boolean actionRevokeOldCertEnabled = true;
                    if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                        actionChangeKeyEnabled = CommonFunction.checkActionMajorForBranch(Definitions.CONFIG_MAJOR_TYPE_CHANGE_KEY_ACTION, sessPolicyCert_Data);
                        actionBackUpKeyEnabled = CommonFunction.checkActionMajorForBranch(Definitions.CONFIG_MAJOR_TYPE_BACKUP_KEY_ACTION, sessPolicyCert_Data);
                        actionRevokeOldCertEnabled = CommonFunction.checkActionMajorForBranch(Definitions.CONFIG_MAJOR_TYPE_REVOKE_OLD_CERT_ACTION, sessPolicyCert_Data);
                    }
                    String idChangeKeyDisabled = "";
                    String idChangeKeyClass = "";
                    if(rs[0][0].PRIVATE_KEY_ENABLED == false) {
                        idChangeKeyDisabled = "disabled";
                        boCheckChangeKey = "1";
                        idChangeKeyClass = " disabled";
                    }
                    if(actionChangeKeyEnabled == false)
                    {
                        idChangeKeyDisabled = "disabled";
                        idChangeKeyClass = " disabled";
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleChangeKey" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="idCheckChangeKey">
                                <input type="checkbox" name="idCheckChangeKey" id="idCheckChangeKey" <%= idChangeKeyDisabled%> onchange="onChangeKey();"
                                    <%= "1".equals(boCheckChangeKey) ? "checked" : "" %>/>
                                 <div class="slider round<%=idChangeKeyClass%>"></div>
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
                                    <%= "1".equals(boCheckBackUpKey) ? "checked" : "" %> <%= actionBackUpKeyEnabled ? "" : "disabled" %>/>
                                <div id="idCheckBackUpKeyClass" class="slider round <%= actionBackUpKeyEnabled ? "" : "disabled" %>"></div>
                            </label>
                        </div>
                    </div>
                    <script>$("#idLblTitleBackUpKey").text(regiscert_fm_check_backup_key);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleRevoked" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="idCheckRevoked">
                                <input type="checkbox" name="idCheckRevoked" id="idCheckRevoked"
                                    <%= "1".equals(boCheckRevoked) ? "checked" : "" %> <%= actionRevokeOldCertEnabled ? "" : "disabled" %>/>
                                <div id="idCheckRevokedClass" class="slider round <%= actionRevokeOldCertEnabled ? "" : "disabled" %>"></div>
                            </label>
                        </div>
                    </div>
                    <script>$("#idLblTitleRevoked").text(regiscert_fm_check_revoke_reissue);</script>
                </div>
                <!-- FILE MANAGER -->
                <%
                    String sOWNER_ID = String.valueOf(rs[0][0].CERTIFICATION_OWNER_ID);
                    String sMaxLengthFile = cogCommon.GetPropertybyCode(Definitions.CONFIG_JACK_RABBIT_MAX_LENGTH_FILE).trim();
                    session.setAttribute("sessUploadFileCert", null);
                    boolean isHasFileOfUser = true;
                    FILE_MANAGER[][] rsFileMana = new FILE_MANAGER[1][];
                    db.S_BO_FILE_MANAGER_GET_BY_CERTIFICATION_AND_OWNER(EscapeUtils.escapeHtml(ids), sOWNER_ID, sessLanguageGlobal, rsFileMana);
                    if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
//                        if(rsFileMana[0].length <= 0)
//                        {
//                            isHasFileOfUser = false;
//                        } else {
                            for(FILE_MANAGER rsFile : rsFileMana[0])
                            {
                                //isHasFileOfUser = false;
                                if(!rsFile.FILE_PROFILE_NAME.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT))
                                {
                                    isHasFileOfUser = true;
                                    break;
                                }
                            }
//                        }
                    }
                    if(rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED
                        && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_EXPIRED)
                    {
//                        if(rsFileMana[0].length <= 0)
//                        {
//                            isHasFileOfUser = false;
//                        } else {
                            for(FILE_MANAGER rsFile : rsFileMana[0])
                            {
//                                isHasFileOfUser = false;
                                if(!rsFile.FILE_PROFILE_NAME.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT))
                                {
                                    isHasFileOfUser = true;
                                    break;
                                }
                            }
//                        }
                    }
                    boolean isHasFileManagerLicense = false;
                    if(rsFileMana[0].length > 0)
                    {
                        for(FILE_MANAGER rsFile : rsFileMana[0])
                        {
                            if(rsFile.FILE_PROFILE_NAME.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT))
                            {
                                if(rsFile.CERTIFICATION_ID == Integer.parseInt(EscapeUtils.escapeHtml(ids)))
                                {
                                    isHasFileManagerLicense = true;
                                    break;
                                }
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
                        SessionUploadFileCert cartIP = (SessionUploadFileCert) session.getAttribute("sessUploadFileCert");
                        cartIP = new SessionUploadFileCert();
                        if(rsFileMana[0].length > 0) {
                            if(!"0".equals(isViewActionFileEnable)) {
                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)){
                                    for(FILE_MANAGER rsFile : rsFileMana[0]) {
                                        if(!rsFile.FILE_PROFILE_NAME.equals(Definitions.CONFIG_FILE_PROFILE_CODE_AGREEMENT_PAPER)
                                                && !rsFile.FILE_PROFILE_NAME.equals(Definitions.CONFIG_FILE_PROFILE_MINUTES_OF_HANDOVER)
                                                && !rsFile.FILE_PROFILE_NAME.equals(Definitions.CONFIG_FILE_PROFILE_SERVICE_REGISTRATION_DOCUMENT)) {
                                            FILE_PROFILE_DATA item = new FILE_PROFILE_DATA();
                                            item.FILE_MANAGER_ID = rsFile.ID;
                                            item.FILE_NAME = rsFile.FILE_NAME;
                                            item.FILE_PROFILE = rsFile.FILE_PROFILE_NAME;
                                            item.FILE_SIZE = (double) rsFile.FILE_SIZE;
                                            item.FILE_MIMETYPE = rsFile.MIME_TYPE_NAME;
                                            item.FILE_STREAM = null;
                                            cartIP.AddRoleFunctionsList(item);
                                        }
                                    }
                                } else {
                                    for(FILE_MANAGER rsFile : rsFileMana[0]) {
                                        FILE_PROFILE_DATA item = new FILE_PROFILE_DATA();
                                        item.FILE_MANAGER_ID = rsFile.ID;
                                        item.FILE_NAME = rsFile.FILE_NAME;
                                        item.FILE_PROFILE = rsFile.FILE_PROFILE_NAME;
                                        item.FILE_SIZE = (double) rsFile.FILE_SIZE;
                                        item.FILE_MIMETYPE = rsFile.MIME_TYPE_NAME;
                                        item.FILE_STREAM = null;
                                        cartIP.AddRoleFunctionsList(item);
                                    }
                                }
                            }
                            session.setAttribute("sessUploadFileCert", cartIP);
                        }
                        boolean isEnableUploadFile = false;
                        if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED
                            || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_EXPIRED)
                        {
                            isEnableUploadFile = true;
                        }
                        ObjectMapper oMapperParse = new ObjectMapper();
                        FILE_PROFILE_JSON itemParsePush = oMapperParse.readValue(sJSON, FILE_PROFILE_JSON.class);
                        int jFile = 1;
                        for (FILE_PROFILE_JSON.Attribute attribute : itemParsePush.getAttributes()) {
                            String sRemark = attribute.getRemark().trim();
                            if(!"1".equals(sessLanguageGlobal)){
                                sRemark = attribute.getRemarkEn().trim();
                            }
                            String sName = attribute.getName().trim();
                            if(!sName.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT)) {
                                String sNameInID = attribute.getName().trim() + String.valueOf(jFile);
                                if(attribute.getEnabled() == true) {
                %>
                <fieldset class="scheduler-border" style="clear: both;">
                    <legend class="scheduler-border"><%= sRemark%></legend>
                    <%
                        String cssDelete ="";
                        String cssDown ="";
                        if(isEnableUploadFile == true) {
                            String sClassFileLabel = "col-sm-2";
                            String sClassFileUp = "col-sm-10";
                            if("1".equals(sRepresentEnabled)){
                                sClassFileLabel = "col-sm-3";
                                sClassFileUp = "col-sm-9";
                                cssDelete = "text-align: center;width: 80px;";
                                cssDown = "text-align: center;width: 80px;";
                            }
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
                    <%
                        }
                    %>
                    <div style="padding: 10px 0 10px 0;" id="idDiv<%= sName%>" class="table-responsive">
                        <table id="idTable<%= sName%>" class="table table-bordered table-striped projects">
                            <thead>
                                <th id="idLblTitleTableSST<%=sNameInID%>"></th>
                                <th id="idLblTitleTableFileName<%=sNameInID%>"></th>
                                <th id="idLblTitleTableSize<%=sNameInID%>"></th>
                                <th id="idLblTitleTableAction<%=sNameInID%>"></th>
                                <script>
                                    $("#idLblTitleTableSST"+'<%=sNameInID%>').text(global_fm_STT);
                                    $("#idLblTitleTableFileName"+'<%=sNameInID%>').text(global_fm_file_name);
                                    $("#idLblTitleTableSize"+'<%=sNameInID%>').text(global_fm_size);
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
                                                    $("#idLblTitleTableLinkDown<%= sNameInID_inner%>").append(global_fm_down);
                                                } else {
                                                    $("#idLblTitleTableLinkDown<%= sNameInID_inner%>").append('<i class="fa fa-pencil"></i> ' + global_fm_down);
                                                }
                                            });
                                        </script>
                                        <%
                                            if(isEnableUploadFile == true) {
                                                if(mhIP.FILE_MANAGER_ID == 0) {
                                        %>
                                        &nbsp;<a id="idLblTitleTableLinkDelete<%= sNameInID_inner%>" style="cursor: pointer;<%=cssDelete%>;<%=cssDelete%>;<%="0".equals(isViewActionFileEnable) && mhIP.FILE_MANAGER_ID != 0 ? "display: none;" : "" %>" class="btn btn-info btn-xs" onclick="DeleteTempFile('<%= mhIP.FILE_PROFILE%>', '<%= mhIP.FILE_NAME%>', '<%= mhIP.FILE_MANAGER_ID %>');">
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
                <%
                            }
                            jFile++;
                            }
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
                    function calUploadFile(input1, idType)
                    {
                        if (input1.value !== '')
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            var data1 = new FormData();
                            $.each($('#input-file' + idType)[0].files, function(k, value)
                            {
                                data1.append(k, value);
                            });
                            data1.append('pFILE_PROFILE', idType);
                            $.ajax({
                                type: 'POST',
                                url: '../FileManageUpload',
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
                                            var iconReview = '<i class="fa fa-pencil"></i> ' + global_fm_view;
                                            var iconDown = '<i class="fa fa-pencil"></i> ' + global_fm_down;
                                            var cssReview = '';
                                            var cssDelete = '';
                                            var representEnabled = '<%=sRepresentEnabled%>';
                                            var viewActionFileEnable = '<%=isViewActionFileEnable%>';
                                            var vUpHighlighLabel = "<%=sUpHighlighLabel%>";
                                            var sActionFileEnable = "";
                                            if(viewActionFileEnable === "0") {
                                                sActionFileEnable = "display: none";
                                            }
                                            if(representEnabled === "1"){
                                                iconDelete = ' ' + global_fm_button_delete;
                                                cssDelete = 'text-align: center;width: 80px;';
                                                iconReview = ' ' + global_fm_view;
                                                cssReview = 'text-align: center;width: 80px;';
                                                iconDown = ' ' + global_fm_down;
                                            }
                                            input1.value = '';
                                            $("#idTBody" + idType).empty();
                                            var content = "";
                                            var viewActionFileEnable = '<%=isViewActionFileEnable%>';
                                            var sActionFileEnable = "";
                                            if(viewActionFileEnable === "0") {
                                                sActionFileEnable = "display: none";
                                            }
                                            for (var i = 0; i < obj.length; i++) {
                                                if(obj[i].FILE_PROFILE === idType)
                                                {
                                                    var fileNameLoad = obj[i].FILE_NAME;
                                                    var sActionCRL = '<a style="cursor: pointer;'+cssDelete+'" class="btn btn-info\n\
                                                        btn-xs" onclick="DeleteTempFile(\'' + obj[i].FILE_PROFILE + '\', \'' + fileNameLoad + '\');">' + iconDelete + '</a>';
                                                    var sReviewCRL = "";
                                                    if(obj[i].FILE_PROFILE === JS_STR_FILE_PROFILE_CODE_E_CONTRACT) {
                                                        sActionCRL = "";
                                                    }
                                                    if(obj[i].FILE_MANAGER_ID !== 0 && viewActionFileEnable === "0") {
                                                        sActionCRL = "";
                                                    }
                                                    /*var fileNameExt = fileNameLoad.substring(fileNameLoad.lastIndexOf('.')+1);
                                                    if(fileNameExt.toUpperCase() === "PDF" || fileNameExt.toUpperCase() === "GIF"  || fileNameExt.toUpperCase() === "JPEG"
                                                                || fileNameExt.toUpperCase() === "JPG" || fileNameExt.toUpperCase() === "PNG"){
                                                        sReviewCRL = '<a style="cursor: pointer;'+cssReview+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                            btn-xs" onclick="ViewTempTwoParamFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + fileNameLoad + '\');">' + iconReview + '</a>';
                                                    }*/
                                                    var sActionDown = '<a style="cursor: pointer;'+cssDelete+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                        btn-xs" onclick="DownloadTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + <%= anticsrf%> + '\');">' + iconDown + '</a>';
                                                    if(obj[i].FILE_MANAGER_ID !== "0")
                                                    {
                                                        content += "<tr>" +
                                                            "<td>" + obj[i].Index + "</td>" +
                                                            "<td>" + fileNameLoad + "</td>" +
                                                            "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                            "<td>" + sActionDown +"</td>" +
                                                            "</tr>";
                                                    } else {
                                                        content += "<tr>" +
                                                            "<td>" + obj[i].Index + "</td>" +
                                                            "<td>" + vUpHighlighLabel+fileNameLoad + "</td>" +
                                                            "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                            "<td>" + sActionCRL +"</td>" +
                                                            "</tr>";
                                                    }
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
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                    }
                                }
                            });
                            return false;
                        } else
                        {
                            funErrorAlert(global_req_file);
                        }
                    }
                    function DeleteTempFile(idType, vFILE_NAME)
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
                                        idParam: 'deletetempfilecert',
                                        idType: idType,
                                        vFILE_NAME: vFILE_NAME
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
                                                var vUpHighlighLabel = "<%=sUpHighlighLabel%>";
                                                var sActionFileEnable = "";
                                                if(viewActionFileEnable === "0"){
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
                                                            btn-xs" onclick="DeleteTempFile(\'' + obj[i].FILE_PROFILE + '\', \'' + fileNameLoad + '\');">' + iconDelete + '</a>';
                                                        var sReviewCRL = "";
                                                        if(obj[i].FILE_PROFILE === JS_STR_FILE_PROFILE_CODE_E_CONTRACT) {
                                                            sActionCRL = "";
                                                        }
                                                        if(obj[i].FILE_MANAGER_ID !== 0 && viewActionFileEnable === "0") {
                                                            sActionCRL = "";
                                                        }
                                                        /*var fileNameExt = fileNameLoad.substring(fileNameLoad.lastIndexOf('.')+1);
                                                        if(fileNameExt.toUpperCase() === "PDF" || fileNameExt.toUpperCase() === "GIF"  || fileNameExt.toUpperCase() === "JPEG"
                                                            || fileNameExt.toUpperCase() === "JPG" || fileNameExt.toUpperCase() === "PNG"){
                                                            sReviewCRL = '<a style="cursor: pointer;'+cssReview+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                btn-xs" onclick="ViewTempTwoParamFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + fileNameLoad + '\');">' + iconReview + '</a>';
                                                        }*/
                                                        var sActionDown = '<a style="cursor: pointer;'+cssDelete+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                            btn-xs" onclick="DownloadTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + <%= anticsrf%> + '\');">' + iconDown + '</a>';
                                                        if(obj[i].FILE_MANAGER_ID !== "0")
                                                        {
                                                            content += "<tr>" +
                                                                "<td>" + obj[i].Index + "</td>" +
                                                                "<td>" + fileNameLoad + "</td>" +
                                                                "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                "<td>" + sActionDown +"</td>" +
                                                                "</tr>";
                                                        } else {
                                                            content += "<tr>" +
                                                                "<td>" + obj[i].Index + "</td>" +
                                                                "<td>" + vUpHighlighLabel+fileNameLoad + "</td>" +
                                                                "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                "<td>" + sActionCRL +"</td>" +
                                                                "</tr>";
                                                        }
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
                                idPurpose: idPurpose,
                                        attrTypeID: JS_STR_CERTIFICATION_ATTR_TYPE_ID_REISSUE
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
                                            if(obj[i].NAME !== JS_STR_FILE_PROFILE_CODE_AGREEMENT_PAPER){
                                                content += '<fieldset class="scheduler-border">'+
                                                    '<legend class="scheduler-border">'+obj[i].REMARK+'</legend>'+
                                                    '<div class="col-sm-13" style="padding: 0px;margin: 0;">'+
                                                        '<div class="form-group">'+
                                                            '<label class="control-label '+displayFileLabel+'" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">'+displayShowTitle+'</label>'+
                                                            '<div class="'+displayFileUp+'" style="padding-right: 0px;">'+
                                                                '<input type="file" id="input-file'+obj[i].NAME+'" style="width: 100%;"'+
                                                                    'onchange="calUploadFile(this, \'' + obj[i].NAME + '\');" class="btn btn-default btn-file select_file" multiple>'+
                                                            '</div>'+
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
                %>
                <!-- FILE MANAGER -->
                <div class="col-sm-13" style="padding-left: 0;clear: both;">
                    <div class="form-group">
                        <fieldset class="scheduler-border">
                            <legend class="scheduler-border" id="idLblTitleComponentDN"></legend>
                            <div style="padding: 0px 0px 0px 0px;margin: 0;" id="idDivViewComponentDN"></div>
                            <script>$("#idLblTitleComponentDN").text(global_fm_csr_info_cts);</script>
                        </fieldset>
                    </div>
                </div>
                <script>
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
                                        var localStoreUID_Info = "";
                                        var vDNFromDB = document.getElementById("idHiddenDN").value;
                                        var vDisabledForm = 'disabled';
                                        var caLoadEnabled = '<%=isCALoad%>';
                                        if(localStorage.getItem("loadChangeKeyOption") === "1") {
                                            vDisabledForm = "";
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
                                                break;
                                            }
                                        }
                                        if(booHasUIDCompany === true)
                                        {
                                            var vContentButton_MST_Radio = "<div class='col-sm-12' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                <div class='form-group'><div class='col-sm-3' style='padding-left: 0px;margin-left: 0px;'>";
                                            vContentButton_MST_Radio = vContentButton_MST_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID
                                                +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID+'" disabled class="form-control123" onlick="OnChangeComboboxMST();"></select>';
                                            vContentButton_MST_Radio = vContentButton_MST_Radio + "</div><div class='col-sm-9' style='padding-right: 0px;'><input class='form-control123' type='text' disabled id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID + "' />";
                                            vContentButton_MST_Radio = vContentButton_MST_Radio + "</div></div></div>";
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
                                            var vContentButton_CMND_Radio = "<div class='col-sm-12' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                <div class='form-group'><div class='col-sm-3' style='padding-left: 0px;margin-left: 0px;'>";
                                            vContentButton_CMND_Radio = vContentButton_CMND_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID
                                                +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID+'" disabled class="form-control123"></select>';
                                            vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div><div class='col-sm-9' style='padding-right: 0px;'><input class='form-control123' disabled type='text' id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID + "' />";
                                            vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div></div></div>";
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
                                                    vContent += '<div>' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                        '<input disabled class="form-control123" type="text" id="' + vInputID + '" />' +
                                                        '</div>' +
                                                        '</div>';
                                                } else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COMMONNAME
                                                        || obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_LOCALITY) {
                                                    vContent += '<div class="form-group" style="clear:both;">' +
                                                                '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                                '<div class="col-sm-10" style="padding-right: 10px;">'+
                                                                    '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                                '</div>' +
                                                            '</div>';
                                                }
                                                else
                                                {
                                                    vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                    vContent += '<div>' +
                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                        '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                        '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                        '</div>' +
                                                        '</div>';
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
                                            }
                                        }
                                        $("#idDivViewComponentDN").append(vContent);
                                        if(vSanContent !== "") {
                                            $("#idViewSanInfo").append(vSanContent);
                                        } else {
                                            $("#idViewSanComponent").css("display", "none");
                                            $("#idViewSanInfo").css("display", "none");
                                        }
                                        if(vDNFromDB !== "")
                                        {
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
                                                            var vDNFromFilterSplit = vDNFromFilter.split(',');
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
                                                            var vDNFromFilterSplit = vDNFromFilter.split(',');
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
                                                
                                            /*if(booHasUIDCompany === true) {
                                                for (var i = 0; i < obj.length; i++) {
                                                    if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY)
                                                    {
                                                        var sValuePushDB = obj[i].SubjectDNAttrCode + "=" + obj[i].SubjectDNAttrPreFix;
                                                        if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_TITLE + "=")
                                                        {
                                                            sValuePushDB = " " + sValuePushDB;
                                                        }
                                                        var indexTag = vDNFromDB.indexOf(sValuePushDB);
                                                        if(indexTag !== -1) {
                                                            var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                            var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
//                                                            $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val(sValuePushDBDataLast.replace(/###/g, ','));
                                                            $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                            $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).val(obj[i].SubjectDNAttrPreFix);
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
                                                        var indexTag = vDNFromDB.indexOf(sValuePushDB);
                                                        if(indexTag !== -1) {
                                                            var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                            var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
//                                                            $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val(sValuePushDBDataLast.replace(/###/g, ','));
                                                            $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                            $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).val(obj[i].SubjectDNAttrPreFix);
                                                        }
                                                    }
                                                }
                                            }*/
                                            
                                            for (var i = 0; i < obj.length; i++)
                                            {
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
                                                }else if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD_SAN)
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
                                            localStorage.setItem("localStoreRequiredPersonal", localStoreRequired);
                                            localStorage.setItem("localStoreInputPersonal", localStoreInput);
                                            localStorage.setItem("localStoreUID_Info", localStoreUID_Info);
                                            localStorage.setItem("localStoreSanInputPersonal", localStoreSanInput);
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
                    
                    function LoadFormDNForPersonalCommon_bk(idCertType, idCSRF)
                    {
                        $.ajax({
                            type: "post",
                            url: "../JSONCommon",
                            data: {
                                idParam: 'loadcert_profile_list',
                                vCertDurationOrProfileID: idCertType
                                //,CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html)
                            {
                                if (html.length > 0)
                                {
                                    var obj = JSON.parse(html);
                                    $("#idDivViewComponentDN").empty();
                                    if (obj[0].Code === "0")
                                    {
                                        $("#idDivViewComponentDN").css("display", "");
                                        var vContent = "";

                                        var localStoreRequired = new Array();
                                        var localStoreInput = new Array();
//                                                                        var vDNFromDB = '<= strDN%>';
                                        var vDNFromDB = document.getElementById("idHiddenDN").value;
                                        var vDisabledForm = 'disabled';
                                        for (var i = 0; i < obj.length; i++) {
                                            var vLabelRequired = "";

                                            if (obj[i].IsRequired === '1') {
                                                var vInputRequireID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID;
                                                if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_RADIO_BUTTON) {
                                                    vLabelRequired = '<label class="CssRequireField">' + global_fm_require_label + '</label>';
                                                    for (var j = 0; j < obj[i].RADIO_LIST.length; j++) {
                                                        vInputRequireID = obj[i].RADIO_LIST[j].SubjectDNAttrCode + obj[i].RADIO_LIST[j].CertTemplateID;
                                                        localStoreRequired.push(vInputRequireID + '###' + obj[i].RADIO_LIST[j].SubjectDNAttrDesc + '###' + obj[i].RADIO_LIST[j].SubjectDNAttrPreFix);
                                                    }
                                                }
                                                else {
                                                    vLabelRequired = '<label class="CssRequireField">' + global_fm_require_label + '</label>';
                                                    localStoreRequired.push(vInputRequireID + '###' + obj[i].SubjectDNAttrDesc + '###' + obj[i].SubjectDNAttrPreFix);
                                                }
                                            }
                                            if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_RADIO_BUTTON)
                                            {
                                                var vContentButton_MST_Radio = "<div class='col-sm-12' style='margin-bottom:0px;padding-left: 0;'>";
                                                var vContentButton_CMND_Radio = "<div class='col-sm-12' style='margin-bottom:0px;padding-left: 0;'>";
                                                var vContentButton_MST_Text = "";
                                                var vContentButton_CMND_Text = "";
                                                var vContentButton_MST_Check = "";
                                                var vContentButton_CMND_Check = "";
                                                for (var j = 0; j < obj[i].RADIO_LIST.length; j++) {
                                                    var vInputID = obj[i].RADIO_LIST[j].SubjectDNAttrCode + obj[i].RADIO_LIST[j].CertTemplateID;
                                                    localStoreInput.push(vInputID + "###" + obj[i].RADIO_LIST[j].SubjectDNAttrCode+ "###" + obj[i].RADIO_LIST[j].SubjectDNAttrPreFix);
                                                    if(obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS || obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                                    {
                                                        vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                        if(vContentButton_MST_Check !== "")
                                                        {
                                                            vContentButton_MST_Radio += '<label class="radio-inline"><input '+vDisabledForm+' type="radio" name="'+JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS
                                                                +'" id="'+vInputID+'" value="'+obj[i].RADIO_LIST[j].SubjectDNAttrPreFix+'">' + obj[i].RADIO_LIST[j].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label>';
                                                        }
                                                        else
                                                        {
                                                            vContentButton_MST_Check = "checked";
                                                            vContentButton_MST_Radio += '<label class="radio-inline"><input '+vDisabledForm+' type="radio" '+vContentButton_MST_Check+' name="'+JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS
                                                                +'" id="'+vInputID+'" value="'+obj[i].RADIO_LIST[j].SubjectDNAttrPreFix+'">' + obj[i].RADIO_LIST[j].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label>';
                                                        }
                                                        if(vContentButton_MST_Text === "")
                                                        {
                                                            var vInputID_Text = JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
//                                                                                        vContentButton_MST_Text = '<div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">'+
//                                                                                            '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID_Text + '" /></div>';
                                                            vContentButton_MST_Text = '<div class="col-sm-12" style="margin-bottom:5px;padding-left: 0;">'+
                                                                    '<div class="form-group">'+
                                                                    '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID_Text + '" />'+
                                                                    '</div>' +
                                                                    '</div>';
                                                        }
                                                    }
                                                    else if(obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND || obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                                                    {
                                                        vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                        if(vContentButton_CMND_Check !== "")
                                                        {// + ' (' + obj[i].SubjectDNAttrCode + '=' + obj[i].SubjectDNAttrPreFix 
                                                            vContentButton_CMND_Radio += '<label class="radio-inline"><input '+vDisabledForm+' type="radio" name="'+JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC
                                                                +'" id="'+vInputID+'" value="'+obj[i].RADIO_LIST[j].SubjectDNAttrPreFix+'">' + obj[i].RADIO_LIST[j].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label>';
                                                        }
                                                        else
                                                        {// + ' (' + obj[i].SubjectDNAttrCode + '=' + obj[i].SubjectDNAttrPreFix
                                                            vContentButton_CMND_Check = "checked";
                                                            vContentButton_CMND_Radio += '<label class="radio-inline"><input '+vDisabledForm+' type="radio" '+vContentButton_CMND_Check+' name="'+JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC
                                                                +'" id="'+vInputID+'" value="'+obj[i].RADIO_LIST[j].SubjectDNAttrPreFix+'">' + obj[i].RADIO_LIST[j].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label>';
                                                        }
                                                        if(vContentButton_CMND_Text === "")
                                                        {
                                                            var vInputID_Text = JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
//                                                                                        vContentButton_CMND_Text = '<div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">'+
//                                                                                            '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID_Text + '" /></div>';
                                                            vContentButton_CMND_Text ='<div class="col-sm-12" style="margin-bottom:10px;padding-left: 0;">'+
                                                                    '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID_Text + '" />\n\
                                                                    </div>';
                                                        }
                                                    } else {}
                                                }
                                                if(vContentButton_MST_Radio !== "")
                                                {
                                                    vContentButton_MST_Radio = vContentButton_MST_Radio + "</div>" + vContentButton_MST_Text;
                                                    vContent = vContent + vContentButton_MST_Radio;
                                                }
                                                if(vContentButton_CMND_Radio !== "")
                                                {
                                                    vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div>" + vContentButton_CMND_Text;
                                                    vContent = vContent + vContentButton_CMND_Radio;
                                                }
                                            }
                                            else
                                            {
                                                var vInputID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID;
                                                localStoreInput.push(vInputID + "###" + obj[i].SubjectDNAttrCode+ "###" + obj[i].SubjectDNAttrPreFix);
                                                if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE)
                                                {
//                                                                                vContent += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
//                                                                                        '<label class="control-label123">' + obj[i].SubjectDNAttrDesc + '</label> ' +
//                                                                                        vLabelRequired +
//                                                                                        '<select '+vDisabledForm+' class="form-control123" id="' + vInputID + '"></select>' +
//                                                                                        '</div>';
                                                    vContent += '<div class="col-sm-6" style="padding-left: 0px;">' +
                                                            '<div class="form-group">' +
                                                            '<label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc
                                                                + ' ' + vLabelRequired + '</label> ' +
                                                            '<div class="col-sm-7" style="padding-right: 0px;">'+
                                                            '<select '+vDisabledForm+' class="form-control123" id="' + vInputID + '"></select>' +
                                                            '</div>' +
                                                            '</div>' +
                                                            '</div>';
                                                    var sValuePushDB = JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE + "=" + obj[i].SubjectDNAttrPreFix;
                                                    var indexTag = vDNFromDB.indexOf(sValuePushDB);
                                                    if(indexTag !== -1)
                                                    {
                                                        var vDataInputID = '<%= strCITY_PROVINCE_ID%>';
                                                        LoadDNCity(vInputID, vDataInputID);
                                                    }
                                                }
                                                else
                                                {// + ' (' + obj[i].SubjectDNAttrCode + ')'
                                                    vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
//                                                                                vContent += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
//                                                                                    '<label class="control-label123">' + obj[i].SubjectDNAttrDesc + '</label> ' +
//                                                                                    vLabelRequired +
//                                                                                    '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID + '" />' +
//                                                                                    '</div>';
                                                    vContent += '<div class="col-sm-6" style="padding-left: 0px;">' +
                                                            '<div class="form-group">' +
                                                            '<label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc
                                                                + ' ' + vLabelRequired + '</label> ' +
                                                            '<div class="col-sm-7" style="padding-right: 0px;">'+
                                                            '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID + '" />' +
                                                            '</div>' +
                                                            '</div>' +
                                                            '</div>';
                                                }
                                            }
                                        }
                                        $("#idDivViewComponentDN").append(vContent);
                                        for (var i = 0; i < obj.length; i++) {
                                            var vLabelRequired = "";
                                            var vInputID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID;
                                            if(vDNFromDB !== "")
                                            {
//                                                                                var sValuePushDB = obj[i].SubjectDNAttrCode + "=" + obj[i].SubjectDNAttrPreFix;
//                                                                                if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_TITLE + "=")
//                                                                                {
//                                                                                    sValuePushDB = " " + sValuePushDB;
//                                                                                }
//                                                                                var indexTag = vDNFromDB.indexOf(sValuePushDB);
//                                                                                if(indexTag !== -1) {
                                                if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_RADIO_BUTTON)
                                                {
                                                    for (var j = 0; j < obj[i].RADIO_LIST.length; j++) {
                                                        var sValuePushDB = obj[i].RADIO_LIST[j].SubjectDNAttrCode + "=" + obj[i].RADIO_LIST[j].SubjectDNAttrPreFix;
                                                        if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_TITLE + "=")
                                                        {
                                                            sValuePushDB = " " + sValuePushDB;
                                                        }
                                                        var indexTag = vDNFromDB.indexOf(sValuePushDB);
                                                        if(indexTag !== -1) {
                                                            if(obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS || obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                                            {
                                                                var idValueMST_MNS = JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
                                                                var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                                var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                                $("#" + idValueMST_MNS).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                                if(sValuePushDB.indexOf(JS_STR_COMPONENT_DN_VALUE_PREFIX_MST) !== -1)
                                                                {
                                                                    $("input[name='"+JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS+"'][value='" + JS_STR_COMPONENT_DN_VALUE_PREFIX_MST + "']").attr('checked', 'checked');
                                                                } else if(sValuePushDB.indexOf(JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS) !== -1)
                                                                {
                                                                    $("input[name='"+JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS+"'][value='" + JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS + "']").attr('checked', 'checked');
                                                                }
                                                            }
                                                            else if(obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND || obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                                                            {
                                                                var idValueCMND_HC = JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
                                                                var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                                var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                                $("#" + idValueCMND_HC).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                                if(sValuePushDB.indexOf(JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND) !== -1)
                                                                {
                                                                    $("input[name='"+JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC+"'][value='" + JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND + "']").attr('checked', 'checked');
                                                                } else if(sValuePushDB.indexOf(JS_STR_COMPONENT_DN_VALUE_PREFIX_HC) !== -1)
                                                                {
                                                                    $("input[name='"+JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC+"'][value='" + JS_STR_COMPONENT_DN_VALUE_PREFIX_HC + "']").attr('checked', 'checked');
                                                                }
                                                                else{}
                                                            } else { }
                                                        }
                                                    }
                                                }
                                                else {
                                                    var sValuePushDB = obj[i].SubjectDNAttrCode + "=" + obj[i].SubjectDNAttrPreFix;
                                                    if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_TITLE + "=")
                                                    {
                                                        sValuePushDB = " " + sValuePushDB;
                                                    }
                                                    var indexTag = vDNFromDB.indexOf(sValuePushDB);
                                                    if(indexTag !== -1) {
                                                        if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE + "=")
                                                        {
                                                        } else if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_PHONE + "=") {
                                                            var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                            var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                            $("#" + vInputID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
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
                                            }
                                        }
                                        localStorage.setItem("localStoreRequiredPersonal", localStoreRequired);
                                        localStorage.setItem("localStoreInputPersonal", localStoreInput);
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
                                        funErrorAlert(global_errorsql);
                                    }
                                }
                            }
                        });
                        return false;
                    }
                </script>
                <fieldset class="scheduler-border" style="clear: both;">
                    <legend class="scheduler-border" id="idLblTitleDetailOld"></legend>
                    <script>$("#idLblTitleDetailOld").text(certlist_title_detail_old);</script>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCertSN" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="CERTIFICATION_SN" readonly value="<%= sCERTIFICATION_SN%>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleCertSN").text(global_fm_serial);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCertCA" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <select name="CERTIFICATION_AUTHORITY" id="CERTIFICATION_AUTHORITY" class="form-control123" readonly
                                    onchange="LOAD_CERTIFICATION_AUTHORITY(this.value, '<%= anticsrf%>');">
                                    <%
                                        String sFristCA = String.valueOf(rs[0][0].CERTIFICATION_AUTHORITY_ID);
                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_COMBOBOX(sessLanguageGlobal, rsCA);
                                        if (rsCA[0].length > 0) {
                                            for (int i = 0; i < rsCA[0].length; i++) {
                                    %>
                                    <option value="<%=String.valueOf(rsCA[0][i].ID)
                                        + "###" + EscapeUtils.CheckTextNull(rsCA[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT)%>" <%= rsCA[0][i].ID == rs[0][0].CERTIFICATION_AUTHORITY_ID ? "selected" : "" %>><%=rsCA[0][i].REMARK%></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <script>$("#idLblTitleCertCA").text(global_fm_ca);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCertPurpose" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <select name="CERTIFICATION_PURPOSE" id="CERTIFICATION_PURPOSE" class="form-control123" disabled>
                                    <%
                                        String sFristCerPurpose=String.valueOf(rs[0][0].CERTIFICATION_PURPOSE_ID);
                                        CERTIFICATION_PURPOSE[][] rsPurpose = new CERTIFICATION_PURPOSE[1][];
                                        db.S_BO_CERTIFICATION_PURPOSE_COMBOBOX(sessLanguageGlobal, rsPurpose);
                                        if (rsPurpose[0].length > 0) {
                                            for (int i = 0; i < rsPurpose[0].length; i++) {
                                                int intID = rsPurpose[0][i].ID;
                                    %>
                                    <option value="<%=String.valueOf(intID)%>" <%= intID == rs[0][0].CERTIFICATION_PURPOSE_ID ? "selected" : ""%>><%=rsPurpose[0][i].REMARK%></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <script>$("#idLblTitleCertPurpose").text(global_fm_ca);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCertDuration" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <select id="CERTIFICATION_DURATION" name="CERTIFICATION_DURATION" class="form-control123" disabled>
                                    <%
                                        String sFristCerDurationOrProfileID = String.valueOf(rs[0][0].CERTIFICATION_PROFILE_ID);
                                        CERTIFICATION_PROFILE[][] rsDuration = new CERTIFICATION_PROFILE[1][];
//                                        db.S_BO_CA_GET_DURATION_COMBOBOX(sFristCA, sFristCerPurpose, String.valueOf(intPKI_FORMFACTOR_ID), sessLanguageGlobal, rsDuration);
                                        db.S_BO_CA_GET_DURATION_COMBOBOX_BY_TYPE(sFristCA, sFristCerPurpose, String.valueOf(intPKI_FORMFACTOR_ID), Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE, sessLanguageGlobal, rsDuration);
                                        if (rsDuration[0].length > 0) {
                                            for (int i = 0; i < rsDuration[0].length; i++) {
//                                                sFristCerDurationOrProfileID = String.valueOf(rsDuration[0][0].ID);
                                    %>
                                    <option value="<%= String.valueOf(rsDuration[0][i].ID)%>" <%= rsDuration[0][i].ID == rs[0][0].CERTIFICATION_PROFILE_ID ? "selected" : ""%>><%= rsDuration[0][i].REMARK %></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <script>$("#idLblTitleCertDuration").text(global_fm_duration_cts);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;display: none;">
                        <div class="form-group">
                            <label id="idLblTitleFeeAmount" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="strFEE_AMOUNT" value="<%= strFEE_AMOUNT%>" readonly class="form-control123"/>
                            </div>
                        </div>
                        <script>$("#idLblTitleFeeAmount").text(global_fm_amount_fee);</script>
                    </div>
                    <%
                        if(!"".equals(strCSR)) {
                    %>
                    <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;display: none;">
                        <label class="control-label123" id="idLblTitleCSR"></label>
                        <textarea style="height: 85px;" name="CSR" id="CSR" readonly class="form-control123"><%= strCSR%></textarea>
                        <br/>
                        <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                            <a id="idLblTitleCSRCopy" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="copyToClipboard($('#CSR').val());"></a>&nbsp;
                            <a id="idLblTitleCSRDown" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="SaveCSR('<%= ids%>', '<%= anticsrf%>');"></a>
                        </div>
                        <script>
                            $("#idLblTitleCSR").text(token_fm_csr);
                            $("#idLblTitleCSRCopy").text(global_fm_copy_all);
                            $("#idLblTitleCSRDown").text(global_fm_down);
                            function SaveCSR(id, idCSRF)
                            {
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
                    <%
                        }
                    %>
                    <%
                        if (!"".equals(strCertificate)) {
                    %>
                    <div class="col-sm-13" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <label id="idLblTitleCert" class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-10" style="padding-right: 0px;padding-top: 6px;padding-left: 40px;">
                                <textarea style="height: 85px;display: none;" readonly name="CERTIFICATE" id="CERTIFICATE" class="form-control123"><%= strCertificate%></textarea>
                                <%
                                    if(isP12 == false) {
                                %>
                                <a id="idAShow" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupViewCSRS();"></a>
                                <a id="idAHide" style="cursor: pointer; color: blue; text-decoration: underline;display: none;" onclick="popupHideCTS();"></a>
                                &nbsp;
                                <script>$("#idAShow").text(global_fm_detail);$("#idAHide").text(global_fm_hide);</script>
                                <%
                                    }
                                %>
                            </div>
                        </div>
                        <script>$("#idLblTitleCert").text(global_fm_Certificate);</script>
                    </div>
                    <%
                        if(isP12 == false) {
                    %>
                    <div id="idViewCSR" class="form-group" style="display: none;padding: 10px 0px 0 0px;margin: 0; clear: both;">
                        <fieldset class="scheduler-border">
                            <legend class="scheduler-border" id="idLblTitleGroupCert"></legend>
                            <script>$("#idLblTitleGroupCert").text(global_group_cert);</script>
                            <%
                                if (!"".equals(strCertificate)) {
                                    int[] intRes = new int[1];
                                    Object[] sss = new Object[2];
                                    String[] tmp = new String[3];
                                    com.VoidCertificateComponents(strCertificate, sss, tmp, intRes);
                                    if (intRes[0] == 0 && sss.length > 0) {
                                        Object strSubjectDN = sss[0].toString().replace(", ", "\n");
                                        Object strIssuerDN = sss[1].toString().replace(", ", "\n");
                                        String strNotBefore = EscapeUtils.CheckTextNull(tmp[1]);
                                        String strNotAfter = EscapeUtils.CheckTextNull(tmp[2]);
                            %>
                            <div class="col-sm-6" style="padding-left: 0;">
                                <div class="form-group" style="vertical-align:middle;">
                                    <label id="idLblTitleCompanyPrin" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <textarea id="idCompanyPrin" class="form-control123" readonly="true" name="idCompanyPrin" style="height: 160px;"><%= strSubjectDN%></textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;">
                                <div class="form-group">
                                    <label id="idLblTitleIssuerPrin" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <textarea id="idIssuerPrin" class="form-control123" readonly="true" name="idIssuerPrin" style="height: 75px;"><%= strIssuerDN%></textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;">
                                <div class="form-group">
                                    <label id="idLblTitleCertValid" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input id="idCertValid" class="form-control123" type="text" readonly="true" value="<%= strNotBefore%>"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;">
                                <div class="form-group">
                                    <label id="idLblTitleCertExpire" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input id="idCertExpire" class="form-control123" type="text" readonly="true" value="<%= strNotAfter%>"/>
                                    </div>
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleCompanyPrin").text(global_fm_company);
                                $("#idLblTitleIssuerPrin").text(global_fm_issue);
                                $("#idLblTitleCertValid").text(global_fm_valid_cert);
                                $("#idLblTitleCertExpire").text(global_fm_Expire_cert);
                            </script>
                            <% } else {
                            %>
                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                <label class="control-label123" id="idLblTitleNoCert"></label>
                                <script>$("#idLblTitleNoCert").text(global_req_info_cert);</script>
                            </div>
                            <%
                                    }
                                }
                            %>
                        </fieldset>
                    </div>
                    <%
                        }
                    %>
                    <%
                        if(isP12 == true) {
                    %>
                    <fieldset class="scheduler-border" style="clear: both;">
                        <legend class="scheduler-border" id="idLblTitleGroupCert"></legend>
                        <script>$("#idLblTitleGroupCert").text(global_group_cert);</script>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group" style="vertical-align:middle;">
                                <label id="idLblTitleCompanyPrin" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <textarea id="idCompanyPrin" class="form-control123" readonly="true" name="idCompanyPrin" style="height: 75px;"><%= EscapeUtils.CheckTextNull(rs[0][0].SUBJECT)%></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleIssuerPrin" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <textarea id="idIssuerPrin" class="form-control123" readonly="true" style="height: 75px;" name="idIssuerPrin"><%= EscapeUtils.CheckTextNull(rs[0][0].ISSUER_SUBJECT)%></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCertValid" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input id="idEFFECTIVE_DT" class="form-control123" type="text" readonly="true" value="<%= EscapeUtils.CheckTextNull(rs[0][0].EFFECTIVE_DT) %>"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCertExpire" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input id="idEXPIRATION_DT" class="form-control123" type="text" readonly="true" value="<%= EscapeUtils.CheckTextNull(rs[0][0].EXPIRATION_DT)%>"/>
                                </div>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleCompanyPrin").text(global_fm_company);
                            $("#idLblTitleIssuerPrin").text(global_fm_issue);
                            $("#idLblTitleCertValid").text(global_fm_valid_cert);
                            $("#idLblTitleCertExpire").text(global_fm_Expire_cert);
                        </script>
                    </fieldset>
                    <%
                        }
                    %>
                    <script>
                        function popupDownloadCert(id, idCSRF)
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../DownloadFileCSR",
                                data: {
                                    idParam: 'certhasid',
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
                        function popupHideCTS()
                        {
                            document.getElementById('idViewCSR').style.display = 'none';
                            document.getElementById('idAHide').style.display = 'none';
                            document.getElementById('idAShow').style.display = '';
                        }
                        function popupViewCSRS()
                        {
                            document.getElementById('idViewCSR').style.display = '';
                            document.getElementById('idAHide').style.display = 'none';
                            document.getElementById('idAShow').style.display = 'none';
                        }
                        function popupViewCSRSSS()
                        {
                            funErrorAlert(global_error_empty_cert);
                        }
                    </script>
                    <%
                        }
                    %>
                    <script>
                        function LOAD_CERTIFICATION_PROFILE(vCertDurationOrProfileID)
                        {
                            if(vCertDurationOrProfileID !== "") {
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
                                                $("#FEE_AMOUNT").val(obj[0].AMOUNT);
                                                $("#DURATION_FREE").val(obj[0].DURATION_FREE);
                                                if(obj[0].DURATION_FREE === "0" || obj[0].DURATION_FREE === 0)
                                                {
                                                    $("#idViewDURATION_FREE").css("display", "none");
                                                } else {
                                                    $("#idViewDURATION_FREE").css("display", "");
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
                                            else {
                                                funErrorAlert(global_errorsql);
                                            }
                                        }
                                    }
                                });
                                return false;
                            } else {
                                funErrorAlert(global_fm_duration_cts + global_fm_not_blank);
                            }
                        }
                        function LOAD_CERTIFICATION_AUTHORITY(objCA, idCSRF)
                        {
                            $("#idHiddenCerCA").val(objCA.split('###')[0]);
                            $("#idHiddenCerCoreSubject").val(objCA.split('###')[1]);
                            $.ajax({
                                type: "post",
                                url: "../JSONCommon",
                                data: {
                                    idParam: 'loadcert_authority',
                                    idCA: objCA.split('###')[0],
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
                                            var idPurposeDefault = '<%=rs[0][0].CERTIFICATION_PURPOSE_ID%>';
                                            cbxCERTIFICATION_PURPOSE.value=idPurposeDefault;
                                            $("#idHiddenCerPurpose").val(idPurposeDefault);
                                            LOAD_CERTIFICATION_PURPOSE(objCA.split('###')[0], idPurposeDefault, idCSRF);
//                                            $("#idHiddenCerPurpose").val(obj[0].ID);
//                                            LOAD_CERTIFICATION_PURPOSE(objCA.split('###')[0], obj[0].ID, idCSRF);
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
                                            cbxCERTIFICATION_PURPOSE.options[cbxCERTIFICATION_PURPOSE.options.length] = new Option("---", "");
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
//                            $.ajax({
//                                type: "post",
//                                url: "../JSONCommon",
//                                data: {
//                                    idParam: 'loadcert_purpose',
//                                    idCA: objCA,
//                                    idPurpose: objPurpose,
//                                    CsrfToken: idCSRF
//                                },
//                                cache: false,
//                                success: function (html)
//                                {
//                                    if (html.length > 0)
//                                    {
//                                        var cbxCERTIFICATION_DURATION = document.getElementById("CERTIFICATION_DURATION");
//                                        removeOptions(cbxCERTIFICATION_DURATION);
//                                        var obj = JSON.parse(html);
//                                        if (obj[0].Code === "0")
//                                        {
//                                            var idProfileDefault = "";
//                                            var idDurationDefault = '<=intDuration%>';
//                                            for (var i = 0; i < obj.length; i++) {
//                                                var sItemDuration = obj[i].DURATION;
//                                                if(sItemDuration === idDurationDefault) {
//                                                    idProfileDefault = obj[i].ID;
//                                                }
//                                                cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option(obj[i].REMARK, obj[i].ID);
//                                            }
//                                            cbxCERTIFICATION_DURATION.value=idProfileDefault;
//                                            $("#idHiddenCerDurationOrProfileID").val(idProfileDefault);
////                                            $("#idHiddenCerDurationOrProfileID").val(obj[0].ID);
//                                            LOAD_CERTIFICATION_DURATION(idProfileDefault, idCSRF);
////                                            $("#idHiddenCerDurationOrProfileID").val(obj[0].ID);
////                                            LOAD_CERTIFICATION_DURATION(obj[0].ID, idCSRF);
//                                        }
//                                        else if (obj[0].Code === JS_EX_CSRF)
//                                        {
//                                            funCsrfAlert();
//                                        }
//                                        else if (obj[0].Code === JS_EX_LOGIN)
//                                        {
//                                            RedirectPageLoginNoSess(global_alert_login);
//                                        }
//                                        else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
//                                        {
//                                            RedirectPageLoginNoSess(global_alert_another_login);
//                                        }
//                                        else if (obj[0].Code === "1")
//                                        {
//                                            cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option("---", "");
//                                        }
//                                        else {
//                                            funErrorAlert(global_errorsql);
//                                        }
//                                    }
//                                }
//                            });
//                            return false;
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
                                    idAttrType: JS_STR_CERTIFICATION_ATTR_TYPE_ID_REISSUE,
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
                                            var idProfileDefault = "";
                                            var idDurationDefault = '<%=intDuration%>';
                                            for (var i = 0; i < obj.length; i++) {
                                                var sItemDuration = obj[i].DURATION;
                                                if(sItemDuration === idDurationDefault) {
                                                    idProfileDefault = obj[i].ID;
                                                }
                                                cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                            }
                                            cbxCERTIFICATION_DURATION.value=idProfileDefault;
                                            $("#idHiddenCerDurationOrProfileID").val(idProfileDefault);
                                            LOAD_CERTIFICATION_DURATION(idProfileDefault, idCSRF);
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
                            LOAD_CERTIFICATION_PROFILE(objProfile);
                        }
                        $(document).ready(function () {
                            if('<%= sFristCA%>' !== "" && '<%= sFristCerPurpose%>' !== ""
                                && '<%= sFristCerDurationOrProfileID%>' !== "" && '<%= String.valueOf(intPKI_FORMFACTOR_ID)%>' !== "")
                            {
                                LOAD_CERTIFICATION_PROFILE('<%= sFristCerDurationOrProfileID%>');
                            }
                        });
                    </script>
                    <input id="idHiddenCerCA" value="<%= sFristCA%>" style="display: none;"/>
                    <input id="idHiddenCerPurpose" value="<%= sFristCerPurpose%>" style="display: none;"/>
                    <input id="idHiddenCerFactor" value="<%= String.valueOf(intPKI_FORMFACTOR_ID)%>" style="display: none;"/>
                    <input id="idHiddenCerDurationOrProfileID" value="<%= sFristCerDurationOrProfileID%>" style="display: none;"/>
                    <!-- PROPERTIES SAN -->
                    <div class="col-sm-13" style="padding-left: 0;clear: both;" id="idViewSanComponent">
                        <div class="form-group">
                            <fieldset class="scheduler-border">
                                <legend class="scheduler-border" id="idLblTitleComponentSAN"></legend>
                                <div id="idViewSanInfo"></div>
                                <script>$("#idLblTitleComponentSAN").text(global_fm_san_info_cts);</script>
                            </fieldset>
                        </div>
                    </div>
                    <!-- PROPERTIES SAN -->
                    
                    <%
                        boolean isShowOldBackUpKey = true;
                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) && !SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            isShowOldBackUpKey = false;
                        }
                        if(rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_NEW
                            && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_DECLINED
                            && isShowOldBackUpKey == true)
                        {
                    %>
                    <div class="col-sm-6" style="padding-left: 0; display: <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) || (isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) && !SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) ? "none" : ""%>">
                        <div class="form-group">
                            <label id="idLblTitleBackUpKeyOld" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <label class="switch" for="idCheckBackUpKeyOld" style="margin-bottom: 0;">
                                    <input type="checkbox" readonly name="idCheckBackUpKeyOld" id="idCheckBackUpKeyOld" disabled
                                        <%= rs[0][0].PRIVATE_KEY_ENABLED == true ? "checked" : "" %>/>
                                    <div id="idCheckBackUpKeyClassOld" class="slider round disabled"></div>
                                </label>
                            </div>
                        </div>
                        <script>$("#idLblTitleBackUpKeyOld").text(regiscert_fm_check_backup_key);</script>
                    </div>
                    <%
                        }
                    %>
                    <%
                        if(!"".equals(sTOKEN_SN) && CommonFunction.checkViewTokenValid(sTOKEN_SN) == true)
                        {
                    %>
                    <div class="col-sm-6" style="padding-left: 0;display: <%=isShowOldBackUpKey == false ? "none" : ""%>">
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
                    <%
                        }
                    %>
                    <%
                        boolean isShowInfoShare = isShowOldBackUpKey;
                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)){
                            isShowInfoShare = false;
                            strPHONE_CONTRACT = "";
                            strEMAIL_CONTRACT = "";
                        }
                    %>
                    <div class="col-sm-6" style="padding-left: 0;display: <%=isShowOldBackUpKey == false ? "none" : ""%>">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePhoneContact"></label>
                                <label id="idLblNotePhoneContact" class="CssRequireField"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "" : "readonly"%> id="PHONE_CONTRACT" name="PHONE_CONTRACT" value="<%= strPHONE_CONTRACT%>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitlePhoneContact").text(global_fm_phone_contact);
                            $(document).ready(function () {
                                var caLoadEnabled = '<%=isCALoad%>';
                                if(caLoadEnabled === JS_IS_WHICH_ABOUT_CA_ICA) {
                                    $("#idLblNotePhoneContact").text(global_fm_require_label);
                                }
                            });
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;display: <%=isShowOldBackUpKey == false ? "none" : ""%>">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleEmailContact"></label>
                                <label id="idLblNoteEmailContact" class="CssRequireField"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "" : "readonly"%> id="EMAIL_CONTRACT" name="EMAIL_CONTRACT" value="<%= strEMAIL_CONTRACT%>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleEmailContact").text(global_fm_email_contact);
                            $(document).ready(function () {
                                var caLoadEnabled = '<%=isCALoad%>';
                                if(caLoadEnabled === JS_IS_WHICH_ABOUT_CA_ICA) {
                                    $("#idLblNoteEmailContact").text(global_fm_require_label);
                                }
                            });
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;display: <%=isShowOldBackUpKey == false ? "none" : ""%>">
                        <div class="form-group">
                            <label id="idLblTitleAgentName" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly name="BranchOfficeName" id="BranchOfficeName" value="<%= EscapeUtils.CheckTextNull(rs[0][0].BRANCH_DESC)%>" class="form-control123"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleAgentName").text(global_fm_Branch);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;display: <%=isShowOldBackUpKey == false ? "none" : ""%>">
                        <div class="form-group">
                            <label id="idLblTitleCreatedBy" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" type="text" name="CREATED_BY" readonly value="<%= strCREATED_BY%>"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleCreatedBy").text(global_fm_user_create);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;display: <%=isShowInfoShare == false ? "none" : ""%>">
                        <div class="form-group">
                            <label id="idLblTitleModifiedBy" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" type="text" name="MODIFIED_BY" readonly value="<%= strMODIFIED_BY%>"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleModifiedBy").text(global_fm_user_endupdate);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;display: <%=isShowOldBackUpKey == false ? "none" : ""%>">
                        <div class="form-group">
                            <label id="idLblTitleCreateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" type="text" name="REQUEST_TIME" readonly value="<%= strCREATED_DT%>"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleCreateDate").text(global_fm_date_create);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;display: <%=isShowInfoShare == false ? "none" : ""%>">
                        <div class="form-group">
                            <label id="idLblTitleModifiedDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" type="text" name="MODIFIED_DT" readonly value="<%= strMODIFIED_DT%>"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleModifiedDate").text(global_fm_date_endupdate);
                        </script>
                    </div>
                </fieldset>
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
        <script>
            $(document).ready(function () {
                var vsCertTypeFrist = '<%= strCERTIFICATION_PROFILE_ID%>';
                if(vsCertTypeFrist !== '')
                {
                    LoadFormDNForPersonalCommon(vsCertTypeFrist, '<%= anticsrf%>');
                }
                if('<%= strIsCert%>' === "1")
                {
                    popupViewCSRS();
                }
            });
        </script>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
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
