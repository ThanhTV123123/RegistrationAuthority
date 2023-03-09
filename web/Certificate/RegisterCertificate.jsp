<%-- 
    Document   : RegisterCertificate
    Created on : Aug 5, 2019, 11:41:54 AM
    Author     : THANH-PC
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="vn.ra.process.SessionUploadFileCert"%>
<%@page import="java.util.List"%>
<%@page import="vn.ra.process.RSSPProcessCommon"%>
<%@page import="vn.ra.utility.PropertiesContent"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<%    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
    Config conf = new Config();
    String printRegisterCAOption = conf.GetPropertybyCode(Definitions.CONFIG_FORM_REGISTRATION_CERT_PRINT);
    String sRepresentEnabled = LoadParamSystem.getParamStart(Definitions.CONFIG_REGISTER_REPRESENT_FORM_ENABLED);
    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="UTF-8">
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <%
            String sNewRefreshJS = conf.GetPropertybyCode(Definitions.CONFIG_JS_REFRESH_STRING_RANDOM);
        %>
        <script src="../js/Language.js?t=<%=sNewRefreshJS%>"></script>
        <script src="../js/process_javajs.js?t=<%=sNewRefreshJS%>"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js?t=<%=sNewRefreshJS%>"></script>
        <link rel="stylesheet" type="text/css" media="all" href="../js/daterangepicker.css" />
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            document.title = regiscert_title_view;
            $(document).ready(function () {
                $('#registerIssuedDate').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
//                localStorage.setItem("isHassFile","0");
                $("#hdfOwnerID").val('');
                $("#hdfOwnerUUID").val('');
                $("#hdfOwnerTypeID").val('');
                $('.loading-gif').hide();
                localStorage.setItem("localStoreRequiredPersonal", null);
                localStorage.setItem("localStoreInputPersonal", null);
                localStorage.setItem("localStoreSanInputPersonal", null);
                localStorage.setItem("localStoreInputID_Info", null);
                localStorage.setItem("localStoreSearchForOwner", null);
                localStorage.setItem("localStoreHasSanInfo", null);
                $('#myModalRegisterPrint').modal({
                    backdrop: 'static',
                    keyboard: true,
                    show: false
                });
                $('#myModalOTPOwner').modal({
                    backdrop: 'static',
                    keyboard: true,
                    show: false
                });
                $('#myModalRegisterPrint').on('hidden.bs.modal', function () {
                    $('#myModalRegisterPrint').modal({
                        backdrop: 'static',
                        keyboard: true,
                        show: false
                    });
                    window.location = 'RegisterCertificate.jsp';
                });  
            });
            var checkForSpecialChar = function(string){
             for(i = 0; i < specialChars.length;i++){
               if(string.indexOf(specialChars[i]) > -1){
                   return true;
                }
             }
             return false;
            };
            
            function ValidateForm(idCSRF, rsspEnabled, isCA)
            {
                var vSTR_COMPONENT_DN_VALUE_COMMONNAME = "";
                var vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = "";
                var vSTR_COMPONENT_DN_VALUE_DOMAIN_NAME = "";
                var vSTR_COMPONENT_DN_VALUE_MST = "";
                var vSTR_COMPONENT_DN_VALUE_MNS = "";
                var vSTR_COMPONENT_DN_VALUE_QD = "";
                var vSTR_COMPONENT_DN_VALUE_CMND = "";
                var vSTR_COMPONENT_DN_VALUE_CCCD = "";
                var vSTR_COMPONENT_DN_VALUE_DEVICE = "";
                var vSTR_COMPONENT_DN_VALUE_HC = "";
                var vSTR_COMPONENT_DN_VALUE_PROVINCE_ID = "";
                var vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC = "";
                var vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT = "";
                var vSTR_COMPONENT_DN_VALUE_EMAIL_SAN = "";
                var vSTR_COMPONENT_DN_ID_EMAIL_SAN = "";
                var vSTR_COMPONENT_SAN = "";
                var vSTR_COMPONENT_DN_VALUE_PERSONAL_ID = "";
                var vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID = "";
                var storeOU = "";
                var caLoadEnabled = '<%=isCALoad%>';
                if (!JSCheckEmptyField($("#USER").val()))
                {
                    funErrorAlert(global_error_not_user_create);
                    return false;
                }
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
                if (!JSCheckEmptyField($("#PKI_FORMFACTOR").val()))
                {
                    $("#PKI_FORMFACTOR").focus();
                    funErrorAlert(global_fm_Method + global_fm_not_blank);
                    return false;
                } else {
                    if($("#PKI_FORMFACTOR").val() === JS_STR_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                        if(rsspEnabled === "1") {
                            if($("#CERTIFICATION_MODE").val() === JS_STR_PKI_FORMFACTOR_MODE_DIRECT) {
                                if (JSCheckEmptyField($("#RSSP_OWNER_PHONE").val()))
                                {
                                    if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#RSSP_OWNER_PHONE")))
                                    {
                                        $("#RSSP_OWNER_PHONE").focus();
                                        funErrorAlert(global_req_phone_format);
                                        return false;
                                    }
                                }
                                if (JSCheckEmptyField($("#RSSP_OWNER_EMAIL").val()))
                                {
                                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#RSSP_OWNER_EMAIL").val()))
                                    {
                                        $("#RSSP_OWNER_EMAIL").focus();
                                        funErrorAlert(global_req_mail_format);
                                        return false;
                                    }
                                }
                                if(caLoadEnabled !== JS_IS_WHICH_ABOUT_CA_NC) {
                                    if (!JSCheckEmptyField($("#OWNER_USER_RSSP").val())) {
                                        $("#OWNER_USER_RSSP").focus();
                                        funErrorAlert(policy_req_empty + global_fm_Username_esigncloud);
                                        return false;
                                    }
                                }
                                if (!JSCheckEmptyField($("#RSSP_RELYING_PARTY").val())) {
                                    $("#RSSP_RELYING_PARTY").focus();
                                    funErrorAlert(policy_req_empty_choose + global_fm_rssp_replying_party);
                                    return false;
                                }
                                if (!JSCheckEmptyField($("#RSSP_AUTHENMODES").val())) {
                                    $("#RSSP_AUTHENMODES").focus();
                                    funErrorAlert(policy_req_empty_choose + global_fm_rssp_authmodes);
                                    return false;
                                }
                                if(caLoadEnabled === JS_IS_WHICH_ABOUT_CA_FPT || caLoadEnabled === JS_IS_WHICH_ABOUT_CA_CMC || caLoadEnabled === JS_IS_WHICH_ABOUT_CA_MID) {
                                    if (!JSCheckEmptyField($("#SIGNING_PROFILES").val())) {
                                        $("#SIGNING_PROFILES").focus();
                                        funErrorAlert(policy_req_empty_choose + global_fm_rssp_signning_profiles);
                                        return false;
                                    }
                                }
                            }
                        } else {
                            $("#PKI_FORMFACTOR").focus();
                            funErrorAlert(global_req_formfactor_support);
                            return false;
                        }
                    }
                }
                var isRepresenseEnabled = '<%=sRepresentEnabled%>';
                if(isRepresenseEnabled === "1"){
                    if (!JSCheckEmptyField($("#registerFullname").val()))
                    {
                        $("#registerFullname").focus();
                        funErrorAlert(policy_req_empty + global_fm_fullname);
                        return false;
                    }
                    if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
                        if (!JSCheckEmptyField($("#registerIssuedAdress").val())) {
                            $("#registerIssuedAdress").focus();
                            funErrorAlert(policy_req_empty + token_fm_address_residence);
                            return false;
                        }
                    } else {
                        if (!JSCheckEmptyField($("#registerAddressGPKD").val()))
                        {
                            $("#registerAddressGPKD").focus();
                            funErrorAlert(policy_req_empty + global_fm_address_GPKD);
                            return false;
                        }
                        if (!JSCheckEmptyField($("#registerRole").val())) {
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
//                    if (!JSCheckEmptyField($("#registerIssuedDate").val()))
//                    {
//                        $("#registerIssuedDate").focus();
//                        funErrorAlert(policy_req_empty + global_fm_cmnd_date);
//                        return false;
//                    }
                }
                if(caLoadEnabled === JS_IS_WHICH_ABOUT_CA_NC) {
                    if (!JSCheckEmptyField($("#registerNCRepresentative").val()))
                    {
                        $("#registerNCRepresentative").focus();
                        funErrorAlert(policy_req_empty + branch_fm_representative);
                        return false;
                    }
                    if (!JSCheckEmptyField($("#registerNCAddress").val()))
                    {
                        $("#registerNCAddress").focus();
                        funErrorAlert(policy_req_empty + global_fm_address_GPKD);
                        return false;
                    }
                }
                var vDNResult = "";
                var sChoiseCert = $("#CERTIFICATION_AUTHORITY").val();
                if (sChoiseCert !== "")
                {
                    // CHECK SPECIAL FIELDS
                    // UID
                    if(sSpace(localStorage.getItem("localStoreUID_Info")) !== "")
                    {
                        var sListInputCheckID_Info = localStorage.getItem("localStoreUID_Info").split(',');
                        for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                            var idLocalStoreUID_Info = sSpace(sListInputCheckID_Info[i].split('###')[0]);
                            if(idLocalStoreUID_Info === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID)
                            {
                                var idCombobox = document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID);
                                if(sSpace(sListInputCheckID_Info[i].split('###')[3]) === "1")
                                {
                                    if (!JSCheckEmptyField($("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val()))
                                    {
                                        $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).focus();
                                        funErrorAlert(policy_req_empty + idCombobox.options[idCombobox.selectedIndex].text);
                                        return false;
                                    }
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
                                if(sSpace(sListInputCheckID_Info[i].split('###')[3]) === "1")
                                {
                                    if (!JSCheckEmptyField($("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val()))
                                    {
                                        $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).focus();
                                        funErrorAlert(policy_req_empty + idCombobox.options[idCombobox.selectedIndex].text);
                                        return false;
                                    }
                                }
                                if(checkForSpecialChar($("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val())) {
                                    $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).focus();
                                    funErrorAlert(idCombobox.options[idCombobox.selectedIndex].text + global_req_no_special + " (" + specialChars + ")");
                                    return false;
                                }
                            }
                        }
                    }
                    // TEXT_FIELD
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
                    // CHECK EXISTS CHARACTER EQUALS
                    var sListEquals = localStorage.getItem("localStoreInputPersonal").split(',');
                    for (var m = 0; m < sListEquals.length; m++) {
                        var idItemValueEquals = sListEquals[m].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                        var itemValueEquals = $("#" + idItemValueEquals).val();
                        if (!JSCheckEqualsDN(itemValueEquals))
                        {
                            $("#" + idItemValueEquals).focus();
                            funErrorAlert(global_error_exists_equals_dn);
                            return false;
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
                                escapeEntities(sSpace(itemValue)) + ", ";
                            if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_ORGANUNIT){
                                storeOU = storeOU + escapeEntities(sSpace(itemValue)) + "###";
                            }
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
                                if(sSpace(sValueMST_MNS) !== "") {
                                    vDNResult = vDNResult + sSpace(sListInputCheckID_Info[i].split('###')[2]) + "=" + sSpace(sSelectedMST_MNS) + sSpace(sValueMST_MNS) + ", ";
                                }
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
                                if(sSpace(sValueCMND_HC) !== "") {
                                    vDNResult = vDNResult + sSpace(sListInputCheckID_Info[i].split('###')[2]) + "=" + sSpace(sSelectedCMND_HC) + sSpace(sValueCMND_HC) + ", ";
                                }
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
                if($("#PKI_FORMFACTOR").val() === JS_STR_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                    var sTempIS_MENU_LINK_SET_NO = $("#idSessIsChoise").val();
                    if(sTempIS_MENU_LINK_SET_NO === "0")
                    {
                        if (!JSCheckEmptyField($("#idCSR").val()))
                        {
                            $("#input-file-csr").focus();
                            funErrorAlert(global_req_file + global_req_file_has_data);
                            return false;
                        }
                    }
                }
                var sCheckPUSH_NOTICE = "0";
                if ($("#PUSH_NOTICE_ENABLED").is(':checked'))
                {
                    sCheckPUSH_NOTICE = "1";
                }
                var isCheckBackUpKey = "0";
                if ($("#idCheckBackUpKey").is(':checked')) {
                    isCheckBackUpKey = "1";
                }
                
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../ReqApproveDeclineCommon",
                    data: {
                        idParam: 'checkcertarlamuser',
                        pCERTIFICATION_PURPOSE: $("#CERTIFICATION_PURPOSE").val(),
                        pMNS: vSTR_COMPONENT_DN_VALUE_MNS,
                        pMST: vSTR_COMPONENT_DN_VALUE_MST,
                        pDECISION: vSTR_COMPONENT_DN_VALUE_QD,
                        pCMND: vSTR_COMPONENT_DN_VALUE_CMND,
                        pHC: vSTR_COMPONENT_DN_VALUE_HC,
                        pCCCD: vSTR_COMPONENT_DN_VALUE_CCCD,
                        pENTERPRISE_ID: vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID,
                        pPERSONAL_ID: vSTR_COMPONENT_DN_VALUE_PERSONAL_ID,
                        pFormFactor: $("#PKI_FORMFACTOR").val()
                    },
                    cache: false,
                    success: function (html_first) {
                        var arr_first = sSpace(html_first).split('#');
                        if (arr_first[0] === "0")
                        {
                            swal({
                                title: "",
                                text: arr_first[2],
                                imageUrl: "../Images/icon_warning.png",
                                imageSize: "45x45",
                                showCancelButton: true,
                                closeOnConfirm: true,
                                allowOutsideClick: false,
                                confirmButtonText: login_fm_buton_continue,
                                cancelButtonText: global_button_grid_cancel,
                                cancelButtonColor: "#199DC0"
                            },
                            function () {
                                $('body').append('<div id="over"></div>');
                                $(".loading-gif").show();
                                setTimeout(function () {
                                    $.ajax({
                                        type: "post",
                                        url: "../RequestCommon",
                                        data: {
                                            idParam: 'registrationcert_factor',
                                            BRANCH_ID: $("#AGENT_NAME").val(),
                                            CertProfileID: $("#idHiddenCerDurationOrProfileID").val(),
                                            CACoreSubject: $("#idHiddenCerCoreSubject").val(),
                                            pCERTIFICATION_PURPOSE: $("#CERTIFICATION_PURPOSE").val(),
                                            pPKI_FORMFACTOR: $("#PKI_FORMFACTOR").val(),
                                            pCERTIFICATION_MODE: $("#CERTIFICATION_MODE").val(),
                                            pCSR: $("#idCSR").val(),
                                            DN: vDNResult,
                                            COMPONENT_SAN: vSTR_COMPONENT_SAN,
                                            pPERSONAL_NAME: vSTR_COMPONENT_DN_VALUE_COMMONNAME,
                                            pCOMPANY_NAME: vSTR_COMPONENT_DN_VALUE_COMPANY_NAME,
                                            pDOMAIN_NAME: vSTR_COMPONENT_DN_VALUE_DOMAIN_NAME,
                                            pTAX_CODE: vSTR_COMPONENT_DN_VALUE_MST,
                                            pDECISION: vSTR_COMPONENT_DN_VALUE_QD,
                                            pBUDGET_CODE: vSTR_COMPONENT_DN_VALUE_MNS,
                                            pP_ID: vSTR_COMPONENT_DN_VALUE_CMND,
                                            pPASSPORT: vSTR_COMPONENT_DN_VALUE_HC,
                                            pCCCD: vSTR_COMPONENT_DN_VALUE_CCCD,
                                            pDEVICE: vSTR_COMPONENT_DN_VALUE_DEVICE,
                                            PHONE_CONTRACT: $("#PHONE_CONTRACT").val(),
                                            EMAIL_CONTRACT: $("#EMAIL_CONTRACT").val(),
                                            hdfOwnerID: $("#hdfOwnerID").val(),
                                            hdfOwnerUUID: $("#hdfOwnerUUID").val(),
                                            hdfOwnerTypeID: $("#hdfOwnerTypeID").val(),
                                            pOWNER_USER_RSSP: $("#OWNER_USER_RSSP").val(),
                                            pAGREEMENT_UUID_RSSP: $("#AGREEMENT_UUID_RSSP").val(),
                                            CREATE_USER: $("#USER").val(),
                                            RSSP_OWNER_EMAIL: $("#RSSP_OWNER_EMAIL").val(),
                                            RSSP_OWNER_PHONE: $("#RSSP_OWNER_PHONE").val(),
                                            RSSP_AUTHENMODES: $("#RSSP_AUTHENMODES").val(),
                                            RSSP_RELYING_PARTY: $("#RSSP_RELYING_PARTY").val(),
                                            SIGNING_PROFILES: $("#SIGNING_PROFILES").val(),
                                            pPROVINCE_ID: vSTR_COMPONENT_DN_VALUE_PROVINCE_ID,
                                            pPROVINCE_DESC: vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC,
                                            CheckPUSH_NOTICE: sCheckPUSH_NOTICE,
                                            CheckPRIVATE_KEY: isCheckBackUpKey,
                                            registerAddressGPKD: $("#registerAddressGPKD").val(),
                                            registerFullname: $("#registerFullname").val(),
                                            registerRole: $("#registerRole").val(),
                                            registerCMND: $("#registerCMND").val(),
                                            registerIssuedDate: $("#registerIssuedDate").val(),
                                            registerIssuedPlace: $("#registerIssuedPlace").val(),
                                            registerIssuedAdress: $("#registerIssuedAdress").val(),
                                            registerNCRepresentative: $("#registerNCRepresentative").val(),
                                            registerNCAddress: $("#registerNCAddress").val(),
                                            notCancel: "1",
                                            pENTERPRISE_ID: vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID,
                                            pPERSONAL_ID: vSTR_COMPONENT_DN_VALUE_PERSONAL_ID,
                                            rsspChoiseOwner: $("#idSessIsChoiseOwner").val(),
                                            storeOU: storeOU,
                                            CsrfToken: idCSRF
                                        },
                                        cache: false,
                                        success: function (html) {
                                            var arr = sSpace(html).split('#');
                                            if (arr[0] === "0")
                                            {
                                                if (arr[3] === "1")
                                                {
                                                    pushNotificationApprove($("#AGENT_NAME").val(), $("#idHiddenCerDurationOrProfileID").val());
                                                }
                                                if(arr[2] === "1")
                                                {
                                                    localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", arr[1]);
                                                    localStorage.setItem("PrintRegisterPersonal", arr[1]);
                                                    localStorage.setItem("PrintRegisterBusiness", null);
                                                    if(isCA === JS_IS_WHICH_ABOUT_CA_ICA) {
                                                        funSuccAlert(owner_succ_dispose, 'RegisterList.jsp');
                                                    } else {
                                                        swal({
                                                            title: "",
                                                            text: regiscert_succ_add + '. \n' + global_confirm_print_register,
                                                            imageUrl: "../Images/success.png",
                                                            imageSize: "45x45",
                                                            showCancelButton: true,
                                                            closeOnConfirm: true,
                                                            allowOutsideClick: false,
                                                            confirmButtonText: login_fm_buton_OK,
                                                            cancelButtonText: global_button_grid_cancel,
                                                            cancelButtonColor: "#199DC0"
                                                        },
                                                        function (isConfirm) {
                                                            if (isConfirm) {
                                                                ShowDialogPrint(arr[1], "0", '<%= printRegisterCAOption%>', isCA);
                                                            } else {
                                                                window.location = 'RegisterCertificate.jsp';
                                                            }
                                                        });
                                                    }
                                                }
                                                if(arr[2] === "0")
                                                {
                                                    localStorage.setItem("PrintRegisterPersonal", null);
                                                    localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", arr[1]);
                                                    localStorage.setItem("PrintRegisterBusiness", arr[1]);
                                                    if(isCA === JS_IS_WHICH_ABOUT_CA_ICA) {
                                                        funSuccAlert(owner_succ_dispose, 'RegisterList.jsp');
                                                    } else {
                                                        swal({
                                                            title: "",
                                                            text: regiscert_succ_add + '. \n' + global_confirm_print_register,
                                                            imageUrl: "../Images/success.png",
                                                            imageSize: "45x45",
                                                            showCancelButton: true,
                                                            closeOnConfirm: true,
                                                            allowOutsideClick: false,
                                                            confirmButtonText: login_fm_buton_OK,
                                                            cancelButtonText: global_button_grid_cancel,
                                                            cancelButtonColor: "#199DC0"
                                                        },
                                                        function (isConfirm) {
                                                            if (isConfirm) {
                                                                ShowDialogPrint(arr[1], "1", '<%= printRegisterCAOption%>', isCA);
                                                            } else {
                                                                window.location = 'RegisterCertificate.jsp';
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                            else if (arr[0] === JS_EX_ERROR_DB) {
                                                funErrorAlert(arr[1]);
                                            }
                                            else if (arr[0] === JS_EX_ERROR_CORECA_CALL) {
                                                funErrorAlert(global_error_coreca_call_approve);
                                            }
                                            else if (arr[0] === JS_EX_TAXCODE_EXISTS_REGISTER) {
                                                funErrorAlert(global_error_exists_mst_budget_regis);
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
                                            } else if (arr[0] === JS_EX_NO_FORMFACTOR) {
                                                funErrorAlert(global_error_method);
                                            }
                                            else if (arr[0] === JS_EX_CSR_KEYSIZE)
                                            {
                                                funErrorAlert(global_error_keysize_csr);
                                            }
                                            else if (arr[0] === JS_EX_CSR_EXISTS) {
                                                funErrorAlert(global_error_exist_csr);
                                            }
                                            else if (arr[0] === JS_EX_RSSP_USERNAME_EXISTS) {
                                                funErrorAlert(global_fm_Username_esigncloud_exists);
                                            }
                                            else if (arr[0] === JS_EX_DNS_SSL_NULL) {
                                                $("#idDNSName").focus();
                                                funErrorAlert(policy_req_empty + global_fm_dns_name);
                                            }
                                            else if (arr[0] === "1")
                                            {
                                                funErrorAlert(global_error_request_exists);
                                            }
                                            else if (arr[0] === JS_EX_INVALID_EXTERNAL_SYSTEM)
                                            {
                                                funErrorAlert(global_error_credential_external_invalid);
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
                        } else if (arr_first[0] === "1")
                        {
                            $.ajax({
                                type: "post",
                                url: "../RequestCommon",
                                data: {
                                    idParam: 'registrationcert_factor',
                                    BRANCH_ID: $("#AGENT_NAME").val(),
                                    CertProfileID: $("#idHiddenCerDurationOrProfileID").val(),
                                    CACoreSubject: $("#idHiddenCerCoreSubject").val(),
                                    pCERTIFICATION_PURPOSE: $("#CERTIFICATION_PURPOSE").val(),
                                    pPKI_FORMFACTOR: $("#PKI_FORMFACTOR").val(),
                                    pCERTIFICATION_MODE: $("#CERTIFICATION_MODE").val(),
                                    pCSR: $("#idCSR").val(),
                                    DN: vDNResult,
                                    COMPONENT_SAN: vSTR_COMPONENT_SAN,
                                    pPERSONAL_NAME: vSTR_COMPONENT_DN_VALUE_COMMONNAME,
                                    pCOMPANY_NAME: vSTR_COMPONENT_DN_VALUE_COMPANY_NAME,
                                    pDOMAIN_NAME: vSTR_COMPONENT_DN_VALUE_DOMAIN_NAME,
                                    pTAX_CODE: vSTR_COMPONENT_DN_VALUE_MST,
                                    pDECISION: vSTR_COMPONENT_DN_VALUE_QD,
                                    pBUDGET_CODE: vSTR_COMPONENT_DN_VALUE_MNS,
                                    pP_ID: vSTR_COMPONENT_DN_VALUE_CMND,
                                    pPASSPORT: vSTR_COMPONENT_DN_VALUE_HC,
                                    pCCCD: vSTR_COMPONENT_DN_VALUE_CCCD,
                                    pDEVICE: vSTR_COMPONENT_DN_VALUE_DEVICE,
                                    PHONE_CONTRACT: $("#PHONE_CONTRACT").val(),
                                    EMAIL_CONTRACT: $("#EMAIL_CONTRACT").val(),
                                    hdfOwnerID: $("#hdfOwnerID").val(),
                                    hdfOwnerUUID: $("#hdfOwnerUUID").val(),
                                    hdfOwnerTypeID: $("#hdfOwnerTypeID").val(),
                                    pOWNER_USER_RSSP: $("#OWNER_USER_RSSP").val(),
                                    pAGREEMENT_UUID_RSSP: $("#AGREEMENT_UUID_RSSP").val(),
                                    CREATE_USER: $("#USER").val(),
                                    RSSP_OWNER_EMAIL: $("#RSSP_OWNER_EMAIL").val(),
                                    RSSP_OWNER_PHONE: $("#RSSP_OWNER_PHONE").val(),
                                    RSSP_AUTHENMODES: $("#RSSP_AUTHENMODES").val(),
                                    RSSP_RELYING_PARTY: $("#RSSP_RELYING_PARTY").val(),
                                    SIGNING_PROFILES: $("#SIGNING_PROFILES").val(),
                                    pPROVINCE_ID: vSTR_COMPONENT_DN_VALUE_PROVINCE_ID,
                                    pPROVINCE_DESC: vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC,
                                    CheckPUSH_NOTICE: sCheckPUSH_NOTICE,
                                    CheckPRIVATE_KEY: isCheckBackUpKey,
                                    registerAddressGPKD: $("#registerAddressGPKD").val(),
                                    registerFullname: $("#registerFullname").val(),
                                    registerRole: $("#registerRole").val(),
                                    registerCMND: $("#registerCMND").val(),
                                    registerIssuedDate: $("#registerIssuedDate").val(),
                                    registerIssuedPlace: $("#registerIssuedPlace").val(),
                                    registerIssuedAdress: $("#registerIssuedAdress").val(),
                                    registerNCRepresentative: $("#registerNCRepresentative").val(),
                                    registerNCAddress: $("#registerNCAddress").val(),
                                    pENTERPRISE_ID: vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID,
                                    pPERSONAL_ID: vSTR_COMPONENT_DN_VALUE_PERSONAL_ID,
                                    rsspChoiseOwner: $("#idSessIsChoiseOwner").val(),
                                    storeOU: storeOU,
                                    CsrfToken: idCSRF
                                },
                                cache: false,
                                success: function (html) {
                                    var arr = sSpace(html).split('#');
                                    if (arr[0] === "0")
                                    {
                                        if (arr[3] === "1")
                                        {
                                            pushNotificationApprove($("#AGENT_NAME").val(), $("#idHiddenCerDurationOrProfileID").val());
                                        }
                                        if(arr[2] === "1")
                                        {
                                            localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", arr[1]);
                                            localStorage.setItem("PrintRegisterPersonal", arr[1]);
                                            localStorage.setItem("PrintRegisterBusiness", null);
                                            if(isCA === JS_IS_WHICH_ABOUT_CA_ICA) {
                                                funSuccAlert(owner_succ_dispose, 'RegisterList.jsp');
                                            } else {
                                                swal({
                                                    title: "",
                                                    text: regiscert_succ_add + '. \n' + global_confirm_print_register,
                                                    imageUrl: "../Images/success.png",
                                                    imageSize: "45x45",
                                                    showCancelButton: true,
                                                    closeOnConfirm: true,
                                                    allowOutsideClick: false,
                                                    confirmButtonText: login_fm_buton_OK,
                                                    cancelButtonText: global_button_grid_cancel,
                                                    cancelButtonColor: "#199DC0"
                                                },
                                                function (isConfirm) {
                                                    if (isConfirm) {
                                                        ShowDialogPrint(arr[1], "0", '<%= printRegisterCAOption%>', isCA);
                                                    } else {
                                                        window.location = 'RegisterCertificate.jsp';
                                                    }
                                                });	
                                            }
                                        }
                                        if(arr[2] === "0")
                                        {
                                            localStorage.setItem("PrintRegisterPersonal", null);
                                            localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", arr[1]);
                                            localStorage.setItem("PrintRegisterBusiness", arr[1]);
                                            if(isCA === JS_IS_WHICH_ABOUT_CA_ICA) {
                                                funSuccAlert(owner_succ_dispose, 'RegisterList.jsp');
                                            } else {
                                                swal({
                                                    title: "",
                                                    text: regiscert_succ_add + '. \n' + global_confirm_print_register,
                                                    imageUrl: "../Images/success.png",
                                                    imageSize: "45x45",
                                                    showCancelButton: true,
                                                    closeOnConfirm: true,
                                                    allowOutsideClick: false,
                                                    confirmButtonText: login_fm_buton_OK,
                                                    cancelButtonText: global_button_grid_cancel,
                                                    cancelButtonColor: "#199DC0"
                                                },
                                                function (isConfirm) {
                                                    if (isConfirm) {
                                                        ShowDialogPrint(arr[1], "1", '<%= printRegisterCAOption%>', isCA);
                                                    } else {
                                                        window.location = 'RegisterCertificate.jsp';
                                                    }
                                                });
                                            }
                                        }
                                    }
                                    else if (arr[0] === JS_EX_ERROR_DB) {
                                        funErrorAlert(arr[1]);
                                    }
                                    else if (arr[0] === JS_EX_ERROR_CORECA_CALL) {
                                        funErrorAlert(global_error_coreca_call_approve);
                                    }
                                    else if (arr[0] === JS_EX_TAXCODE_EXISTS_REGISTER) {
                                        funErrorAlert(global_error_exists_mst_budget_regis);
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
                                    } else if (arr[0] === JS_EX_NO_FORMFACTOR) {
                                        funErrorAlert(global_error_method);
                                    }
                                    else if (arr[0] === JS_EX_CSR_KEYSIZE)
                                    {
                                        funErrorAlert(global_error_keysize_csr);
                                    }
                                    else if (arr[0] === JS_EX_CSR_EXISTS) {
                                        funErrorAlert(global_error_exist_csr);
                                    }
                                    else if (arr[0] === JS_EX_RSSP_USERNAME_EXISTS) {
                                        funErrorAlert(global_fm_Username_esigncloud_exists);
                                    }
                                    else if (arr[0] === JS_EX_DNS_SSL_NULL) {
                                        $("#idDNSName").focus();
                                        funErrorAlert(policy_req_empty + global_fm_dns_name);
                                    }
                                    else if (arr[0] === "1")
                                    {
                                        funErrorAlert(global_error_request_exists);
                                    }
                                    else if (arr[0] === JS_EX_INVALID_EXTERNAL_SYSTEM)
                                    {
                                        funErrorAlert(global_error_credential_external_invalid);
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
                        else if (arr_first[0] === "2") {
                            swal({
                                title: "",
                                text: global_req_warning_exists_cert,
                                imageUrl: "../Images/icon_warning.png",
                                imageSize: "45x45",
                                showCancelButton: true,
                                closeOnConfirm: true,
                                allowOutsideClick: false,
                                confirmButtonText: login_fm_buton_continue,
                                cancelButtonText: global_button_grid_cancel,
                                cancelButtonColor: "#199DC0"
                            },
                            function () {
                                $('body').append('<div id="over"></div>');
                                $(".loading-gif").show();
                                setTimeout(function () {
                                    $.ajax({
                                        type: "post",
                                        url: "../RequestCommon",
                                        data: {
                                            idParam: 'registrationcert_factor',
                                            BRANCH_ID: $("#AGENT_NAME").val(),
                                            CertProfileID: $("#idHiddenCerDurationOrProfileID").val(),
                                            CACoreSubject: $("#idHiddenCerCoreSubject").val(),
                                            pCERTIFICATION_PURPOSE: $("#CERTIFICATION_PURPOSE").val(),
                                            pPKI_FORMFACTOR: $("#PKI_FORMFACTOR").val(),
                                            pCERTIFICATION_MODE: $("#CERTIFICATION_MODE").val(),
                                            pCSR: $("#idCSR").val(),
                                            DN: vDNResult,
                                            COMPONENT_SAN: vSTR_COMPONENT_SAN,
                                            pPERSONAL_NAME: vSTR_COMPONENT_DN_VALUE_COMMONNAME,
                                            pCOMPANY_NAME: vSTR_COMPONENT_DN_VALUE_COMPANY_NAME,
                                            pDOMAIN_NAME: vSTR_COMPONENT_DN_VALUE_DOMAIN_NAME,
                                            pTAX_CODE: vSTR_COMPONENT_DN_VALUE_MST,
                                            pDECISION: vSTR_COMPONENT_DN_VALUE_QD,
                                            pBUDGET_CODE: vSTR_COMPONENT_DN_VALUE_MNS,
                                            pP_ID: vSTR_COMPONENT_DN_VALUE_CMND,
                                            pPASSPORT: vSTR_COMPONENT_DN_VALUE_HC,
                                            pCCCD: vSTR_COMPONENT_DN_VALUE_CCCD,
                                            pDEVICE: vSTR_COMPONENT_DN_VALUE_DEVICE,
                                            PHONE_CONTRACT: $("#PHONE_CONTRACT").val(),
                                            EMAIL_CONTRACT: $("#EMAIL_CONTRACT").val(),
                                            hdfOwnerID: $("#hdfOwnerID").val(),
                                            hdfOwnerUUID: $("#hdfOwnerUUID").val(),
                                            hdfOwnerTypeID: $("#hdfOwnerTypeID").val(),
                                            pOWNER_USER_RSSP: $("#OWNER_USER_RSSP").val(),
                                            pAGREEMENT_UUID_RSSP: $("#AGREEMENT_UUID_RSSP").val(),
                                            CREATE_USER: $("#USER").val(),
                                            RSSP_OWNER_EMAIL: $("#RSSP_OWNER_EMAIL").val(),
                                            RSSP_OWNER_PHONE: $("#RSSP_OWNER_PHONE").val(),
                                            RSSP_AUTHENMODES: $("#RSSP_AUTHENMODES").val(),
                                            RSSP_RELYING_PARTY: $("#RSSP_RELYING_PARTY").val(),
                                            SIGNING_PROFILES: $("#SIGNING_PROFILES").val(),
                                            pPROVINCE_ID: vSTR_COMPONENT_DN_VALUE_PROVINCE_ID,
                                            pPROVINCE_DESC: vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC,
                                            CheckPUSH_NOTICE: sCheckPUSH_NOTICE,
                                            CheckPRIVATE_KEY: isCheckBackUpKey,
                                            registerAddressGPKD: $("#registerAddressGPKD").val(),
                                            registerFullname: $("#registerFullname").val(),
                                            registerRole: $("#registerRole").val(),
                                            registerCMND: $("#registerCMND").val(),
                                            registerIssuedDate: $("#registerIssuedDate").val(),
                                            registerIssuedPlace: $("#registerIssuedPlace").val(),
                                            registerIssuedAdress: $("#registerIssuedAdress").val(),
                                            registerNCRepresentative: $("#registerNCRepresentative").val(),
                                            registerNCAddress: $("#registerNCAddress").val(),
                                            pENTERPRISE_ID: vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID,
                                            pPERSONAL_ID: vSTR_COMPONENT_DN_VALUE_PERSONAL_ID,
                                            rsspChoiseOwner: $("#idSessIsChoiseOwner").val(),
                                            storeOU: storeOU,
                                            CsrfToken: idCSRF
                                        },
                                        cache: false,
                                        success: function (html) {
                                            var arr = sSpace(html).split('#');
                                            if (arr[0] === "0")
                                            {
                                                if (arr[3] === "1")
                                                {
                                                    pushNotificationApprove($("#AGENT_NAME").val(), $("#idHiddenCerDurationOrProfileID").val());
                                                }
                                                if(arr[2] === "1")
                                                {
                                                    localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", arr[1]);
                                                    localStorage.setItem("PrintRegisterPersonal", arr[1]);
                                                    localStorage.setItem("PrintRegisterBusiness", null);
                                                    if(isCA === JS_IS_WHICH_ABOUT_CA_ICA) {
                                                        funSuccAlert(owner_succ_dispose, 'RegisterList.jsp');
                                                    } else {
                                                        swal({
                                                            title: "",
                                                            text: regiscert_succ_add + '. \n' + global_confirm_print_register,
                                                            imageUrl: "../Images/success.png",
                                                            imageSize: "45x45",
                                                            showCancelButton: true,
                                                            closeOnConfirm: true,
                                                            allowOutsideClick: false,
                                                            confirmButtonText: login_fm_buton_OK,
                                                            cancelButtonText: global_button_grid_cancel,
                                                            cancelButtonColor: "#199DC0"
                                                        },
                                                        function (isConfirm) {
                                                            if (isConfirm) {
                                                                ShowDialogPrint(arr[1], "0", '<%= printRegisterCAOption%>', isCA);
                                                            } else {
                                                                window.location = 'RegisterCertificate.jsp';
                                                            }
                                                        });		
                                                    }
                                                }
                                                if(arr[2] === "0")
                                                {
                                                    localStorage.setItem("PrintRegisterPersonal", null);
                                                    localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", arr[1]);
                                                    localStorage.setItem("PrintRegisterBusiness", arr[1]);
                                                    if(isCA === JS_IS_WHICH_ABOUT_CA_ICA) {
                                                        funSuccAlert(owner_succ_dispose, 'RegisterList.jsp');
                                                    } else {
                                                        swal({
                                                            title: "",
                                                            text: regiscert_succ_add + '. \n' + global_confirm_print_register,
                                                            imageUrl: "../Images/success.png",
                                                            imageSize: "45x45",
                                                            showCancelButton: true,
                                                            closeOnConfirm: true,
                                                            allowOutsideClick: false,
                                                            confirmButtonText: login_fm_buton_OK,
                                                            cancelButtonText: global_button_grid_cancel,
                                                            cancelButtonColor: "#199DC0"
                                                        },
                                                        function (isConfirm) {
                                                            if (isConfirm) {
                                                                ShowDialogPrint(arr[1], "1", '<%= printRegisterCAOption%>', isCA);
                                                            } else {
                                                                window.location = 'RegisterCertificate.jsp';
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                            else if (arr[0] === JS_EX_ERROR_DB) {
                                                funErrorAlert(arr[1]);
                                            }
                                            else if (arr[0] === JS_EX_ERROR_CORECA_CALL) {
                                                funErrorAlert(global_error_coreca_call_approve);
                                            }
                                            else if (arr[0] === JS_EX_TAXCODE_EXISTS_REGISTER) {
                                                funErrorAlert(global_error_exists_mst_budget_regis);
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
                                            } else if (arr[0] === JS_EX_NO_FORMFACTOR) {
                                                funErrorAlert(global_error_method);
                                            }
                                            else if (arr[0] === JS_EX_CSR_KEYSIZE)
                                            {
                                                funErrorAlert(global_error_keysize_csr);
                                            }
                                            else if (arr[0] === JS_EX_CSR_EXISTS) {
                                                funErrorAlert(global_error_exist_csr);
                                            }
                                            else if (arr[0] === JS_EX_RSSP_USERNAME_EXISTS) {
                                                funErrorAlert(global_fm_Username_esigncloud_exists);
                                            }
                                            else if (arr[0] === JS_EX_DNS_SSL_NULL) {
                                                $("#idDNSName").focus();
                                                funErrorAlert(policy_req_empty + global_fm_dns_name);
                                            }
                                            else if (arr[0] === "1")
                                            {
                                                funErrorAlert(global_error_request_exists);
                                            }
                                            else if (arr[0] === JS_EX_INVALID_EXTERNAL_SYSTEM)
                                            {
                                                funErrorAlert(global_error_credential_external_invalid);
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
                        else {
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
                window.location = "RegisterCertificate.jsp";
            }
            function GetOwnerForm()
            {
                ShowDialog();
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
            legend.scheduler-border {
                color: <%="1".equals(sRepresentEnabled) ? "#C43131" : ""%>;
                <%="1".equals(sRepresentEnabled) ? "padding:0 10px 0 0;" : ""%>;
            }
            .btn-info{
                <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "background-color:#31B910;border-color:#31B910" : ""%>
                /*color:#fff;background-color:#C43131;border-color:#C43131*/
            }
            .btn-info:hover,.btn-info:focus,.btn-info:active,.btn-info.active {
                <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "background-color:#31B910;border-color:#31B910" : ""%>
                /*color:#fff;background-color:#C43131;border-color:#C43131*/
            }
            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
            .table > thead > tr > th{border-bottom: none;}
            .btn{margin-bottom: 0px;}
            @media (min-width: 768px){.modal-dialog{width: 1200px;}}
            .modal-header{
                padding-right: 10px; padding-top: 10px;
            }
        </style>
    </head>
    <body class="nav-md">
        <%
            if (session.getAttribute("sUserID") != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String loginUID = session.getAttribute("UserID").toString().trim();
                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                String sessUserID = request.getSession(false).getAttribute("UserID").toString().trim();
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                String SessRoleID_ID = session.getAttribute("RoleID_ID").toString().trim();
                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Cert");
                CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessPolicyCert_Data");
                CERTIFICATION_POLICY_DATA[][] sessPolicyFormFactor_Data = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessPolicyFormFactor_Data");
                session.setAttribute("sessUploadFileCert", null);
                session.setAttribute("sessDNSNameForSSL", null);
                String sRSSP_ACCESS_ENABLED = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                String sAgreementUUID = CommonFunction.getUUIDV4();
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <div class="container body">
            <div class="main_container">
                <div class="col-md-3 left_col">
                    <div class="left_col scroll-view">
                        <%@ include file="../Modules/Header.jsp" %>
                        <br />
                        <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
                            <%@ include file="../Modules/MenuLeft.jsp" %>
                        </div>
                    </div>
                </div>
                <div class="top_nav">
                    <%@ include file="../Modules/Navigate.jsp" %>
                    <script>
                        document.getElementById("idNameURL").innerHTML = regiscert_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <%
                                        String sDN_Country = "";
                                        String sMaxLengthFile = cogCommon.GetPropertybyCode(Definitions.CONFIG_JACK_RABBIT_MAX_LENGTH_FILE).trim();
                                        String intCASelected = "";
                                        try {
                                            String pPUSH_NOTICE_ENABLED = "";
                                            String strCADefault="";
                                            String boCheckBackUpKey = "0";
                                            String sRegexPolicy = "";
                                            String sArrayFileExten = "";
                                            GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                                            if (sessGeneralPolicy[0].length > 0) {
                                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                {
                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_CERTIFICATE_ATTRIBUTE_COUNTRY))
                                                    {
                                                        sDN_Country = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                    }
                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DEFAULT_CERTIFICATION_PUSH_NOTICE_ENABLED))
                                                    {
                                                        pPUSH_NOTICE_ENABLED = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                    }
                                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CA_DEFAULT_FOR_EXPORT))
                                                    {
                                                        strCADefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                    }
                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DEFAULT_PRIVATE_KEY_ENABLED))
                                                    {
                                                        boCheckBackUpKey = rsPolicy1.VALUE;
                                                    }
                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_REGEX_FOR_PHONE_EMAIL))
                                                    {
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
                                            String sSessReRegisterCert = "";
                                            String sReRegisterAllow = EscapeUtils.CheckTextNull(request.getParameter("type"));
                                            if(!"".equals(sReRegisterAllow)) {
                                                if(session.getAttribute("SessReRegisterCert") != null) {
                                                    sSessReRegisterCert = EscapeUtils.CheckTextNull(session.getAttribute("SessReRegisterCert").toString());
                                                }
                                            } else {
                                                session.setAttribute("SessReRegisterCert", null);
                                            }
                                            String sReEmailContact = "";
                                            String sRePhoneContact = "";
                                            int iReBRANCH_ID = 0;
                                            int iReCREATED_BY_ID = 0;
                                            int iReCERTIFICATION_PROFILE_ID = 0;
                                            int iReCERTIFICATION_AUTHORITY_ID = 0;
                                            int iReCERTIFICATION_PURPOSE_ID = 0;
                                            int iRePKI_FORMFACTOR_ID = 0;
                                            int iReCITY_PROVINCE_ID = 0;
                                            String sReSUBJECT = "";
                                            String sSanDataDB = "";
                                            
                                            String sRegisterFullname = "";
                                            String sRegisterRole = "";
                                            String sRegisterCMND = "";
                                            String sRegisterIssuedDate = "";
                                            String sRegisterIssuedPlace = "";
                                            String sRegisterIssuedAdress = "";
                                            String sRegisterAddressGPKD = "";
//                                            FILE_MANAGER[][] rsFileMana = null;
                                            if(!"".equals(sSessReRegisterCert))
                                            {
                                                CERTIFICATION[][] rs;
                                                rs = new CERTIFICATION[1][];
                                                db.S_BO_CERTIFICATION_DETAIL(sSessReRegisterCert, sessLanguageGlobal, rs);
                                                if (rs[0].length > 0) {
                                                    sReEmailContact = EscapeUtils.CheckTextNull(rs[0][0].EMAIL_CONTRACT);
                                                    sRePhoneContact = EscapeUtils.CheckTextNull(rs[0][0].PHONE_CONTRACT);
                                                    iReBRANCH_ID = rs[0][0].BRANCH_ID;
                                                    iReCREATED_BY_ID = rs[0][0].CREATED_BY_ID;
                                                    iReCERTIFICATION_PROFILE_ID = rs[0][0].CERTIFICATION_PROFILE_ID;
                                                    iReCERTIFICATION_AUTHORITY_ID = rs[0][0].CERTIFICATION_AUTHORITY_ID;
                                                    iReCERTIFICATION_PURPOSE_ID = rs[0][0].CERTIFICATION_PURPOSE_ID;
                                                    iRePKI_FORMFACTOR_ID = rs[0][0].PKI_FORMFACTOR_ID;
                                                    iReCITY_PROVINCE_ID = rs[0][0].CITY_PROVINCE_ID;
                                                    sReSUBJECT = EscapeUtils.CheckTextNull(rs[0][0].SUBJECT);
                                                    if(!"".equals(sReSUBJECT)) {
                                                        sReSUBJECT = sReSUBJECT.replace("\\,", "###");
                                                    }
                                                    String sPROPERTIES = EscapeUtils.CheckTextNull(rs[0][0].PROPERTIES);
                                                    if(!"".equals(sPROPERTIES)) {
                                                        ObjectMapper objectMapper = new ObjectMapper();
                                                        CERTIFICATION_PROPERTIES_JSON itemParsePush = objectMapper.readValue(sPROPERTIES, CERTIFICATION_PROPERTIES_JSON.class);
                                                        if(itemParsePush.getAttributes().size() > 0) {
                                                            for (int i = 0; i < itemParsePush.getAttributes().size(); i++) {
                                                                sSanDataDB = sSanDataDB + EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getKey()) + "###" + EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getValue()) + "@@@";
                                                            }
                                                        }
                                                    }
//                                                    String sOWNER_ID = String.valueOf(rs[0][0].CERTIFICATION_OWNER_ID);
//                                                    rsFileMana = new FILE_MANAGER[1][];
//                                                    db.S_BO_FILE_MANAGER_GET_BY_CERTIFICATION_AND_OWNER(sSessReRegisterCert, sOWNER_ID, sessLanguageGlobal, rsFileMana);
                                                }
                                                if("1".equals(sRepresentEnabled)) {
                                                    rs = new CERTIFICATION[1][];
                                                    db.S_BO_CERTIFICATION_BRIEF_DETAIL(sSessReRegisterCert, rs);
                                                    if(rs[0].length > 0) {
                                                        String sPrfileContact = EscapeUtils.CheckTextNull(rs[0][0].PROFILE_CONTACT_INFO);
                                                        if(!"".equals(sPrfileContact)) {
                                                            try {
                                                                ObjectMapper oMapperParse = new ObjectMapper();
                                                                ProfileContactInfoJson profileContact = oMapperParse.readValue(sPrfileContact, ProfileContactInfoJson.class);
                                                                if(profileContact != null) {
                                                                    sRegisterIssuedPlace = CommonFunction.replaceCharaterSpecialJson(profileContact.PIDIssuedBy, false);
                                                                    sRegisterIssuedDate = EscapeUtils.CheckTextNull(profileContact.PIDDate);
                                                                    sRegisterCMND = EscapeUtils.CheckTextNull(profileContact.PID);
                                                                    sRegisterAddressGPKD = CommonFunction.replaceCharaterSpecialJson(profileContact.AddressLicense, false);
                                                                    sRegisterFullname = CommonFunction.replaceCharaterSpecialJson(profileContact.RepresentativeName, false);
                                                                    sRegisterIssuedAdress = CommonFunction.replaceCharaterSpecialJson(profileContact.Address, false);
                                                                    sRegisterRole = CommonFunction.replaceCharaterSpecialJson(profileContact.Position, false);
                                                                }
                                                            } catch(Exception e){CommonFunction.LogExceptionServlet(null, "ProfileContactInfoJson " + e.getMessage(), e);}
                                                        }
                                                    }
                                                }
                                            }
                                            String sBranchNameLogin = "";
                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) && !SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                BRANCH[][] rsBranch = new BRANCH[1][];
                                                db.S_BO_BRANCH_DETAIL(SessUserAgentID, rsBranch);
                                                if(rsBranch.length > 0) {
                                                    sBranchNameLogin = EscapeUtils.CheckTextNull(rsBranch[0][0].NAME);
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
                                        <script>$("#idLblTitleEdits").text(regiscert_title_view);</script>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <%
                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_ISSUE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                %>
                                                <input type="button" id="btnSave" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>', '<%=sRSSP_ACCESS_ENABLED%>', '<%=isCALoad%>');" />
                                                <script>document.getElementById("btnSave").value = global_fm_button_regis;</script>
                                                <%
                                                    }
                                                %>
                                                <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                                            </li>
                                            <script>
                                                document.getElementById("btnClose").value = global_fm_refresh;
                                            </script>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form name="myname" method="post" class="form-horizontal">
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <input type="hidden" name="hdfOwnerID" id="hdfOwnerID"/>
                                            <input type="hidden" name="hdfOwnerUUID" id="hdfOwnerUUID"/>
                                            <input type="hidden" name="hdfOwnerTypeID" id="hdfOwnerTypeID"/>
                                            <fieldset class="scheduler-border" style="clear: both;" id="idShowOwnerTMSInfo">
                                                <legend class="scheduler-border"><script>document.write(cert_title_register_owner);</script></legend>
                                                <div class="col-sm-13" style="padding-left: 0;">
                                                    <div class="col-sm-6" style="padding-left: 0;">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                <label id="idLblTitlePhoneContact"></label>
                                                                <label id="idLblNotePhoneContact" class="CssRequireField"></label>
                                                            </label>
                                                            <div class="col-sm-6" style="padding-right: 0px;">
                                                                <input type="text" value="<%= sRePhoneContact%>" id="PHONE_CONTRACT" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_PHONE %>" class="form-control123"
                                                                    onblur="autoTrimTextField('PHONE_CONTRACT', this.value);"/>
                                                            </div>
                                                        </div>
                                                        <script>
                                                            $("#idLblTitlePhoneContact").text(global_fm_phone_contact);
                                                            $("#idLblNotePhoneContact").text(global_fm_require_label);
                                                        </script>
                                                    </div>
                                                    <div class="col-sm-6" style="padding-left: 0;">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                <label id="idLblTitleEmailContact"></label>
                                                                <label id="idLblNoteEmailContact" class="CssRequireField"></label>
                                                            </label>
                                                            <div class="col-sm-6" style="padding-right: 0px;">
                                                                <input type="text" value="<%= sReEmailContact%>" id="EMAIL_CONTRACT" class="form-control123" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_EMAIL%>">
                                                            </div>
                                                        </div>
                                                        <script>
                                                            $("#idLblTitleEmailContact").text(global_fm_email_contact);
                                                            $("#idLblNoteEmailContact").text(global_fm_require_label);
                                                        </script>
                                                    </div>
                                                </div>
                                                <div class="col-sm-13" style="padding-left: 0;display: none;" id="idViewRSSPPhoneEnail">
                                                    <div class="col-sm-6" style="padding-left: 0;">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                <label id="idLblTitlePhoneAuthen"></label>
                                                                <!--<label id="idLblNotePhoneAuthen" class="CssRequireField"></label>-->
                                                            </label>
                                                            <div class="col-sm-6" style="padding-right: 0px;">
                                                                <input type="text" id="RSSP_OWNER_PHONE" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_PHONE %>" class="form-control123"
                                                                    onblur="autoTrimTextField('RSSP_OWNER_PHONE', this.value);"/>
                                                            </div>
                                                        </div>
                                                        <script>
                                                            $("#idLblTitlePhoneAuthen").text(global_fm_phone_authen_rssp);
//                                                            $("#idLblNotePhoneAuthen").text(global_fm_require_label);
                                                        </script>
                                                    </div>
                                                    <div class="col-sm-6" style="padding-left: 0;">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                <label id="idLblTitleEmailAuthen"></label>
                                                                <!--<label id="idLblNoteEmailAuthen" class="CssRequireField"></label>-->
                                                            </label>
                                                            <div class="col-sm-6" style="padding-right: 0px;">
                                                                <input type="text" id="RSSP_OWNER_EMAIL" class="form-control123" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_EMAIL%>">
                                                            </div>
                                                        </div>
                                                        <script>
                                                            $("#idLblTitleEmailAuthen").text(global_fm_email_authen_rssp);
//                                                            $("#idLblNoteEmailAuthen").text(global_fm_require_label);
                                                        </script>
                                                    </div>
                                                </div>
                                                <div class="col-sm-13" style="padding-left: 0;">
                                                    <div class="col-sm-6" style="padding-left: 0;">
                                                        <div class="form-group" id="idShowAgreementRSSP" style="display: none;">
                                                            <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                <label id="idLblTitleAgreementRSSP"></label>
                                                                <!--<label id="idLblNoteAgreementRSSP" class="CssRequireField"></label>-->
                                                            </label>
<!--                                                            <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                
                                                            </label>-->
                                                            <div class="col-sm-6" style="padding-right: 0px;">
                                                                <input type="text" id="AGREEMENT_UUID_RSSP" name="AGREEMENT_UUID_RSSP" value="<%= sAgreementUUID%>" class="form-control123">
                                                            </div>
                                                            <script>
                                                                $("#idLblTitleAgreementRSSP").text(global_fm_uuid_agreement);
//                                                                $("#idLblNoteAgreementRSSP").text(global_fm_require_label);
                                                            </script>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-6" style="padding-left: 0;">
                                                        <div class="form-group" id="idShowUsernameRSSP" style="display: none;">
                                                            <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                <label id="idLblTitleUsernameRSSP"></label>
                                                                <label id="idLblNoteUsernameRSSP" class="CssRequireField"></label>
                                                            </label>
<!--                                                            <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                
                                                            </label>-->
                                                            <div class="col-sm-6" style="padding-right: 0px;">
                                                                <input type="text" id="OWNER_USER_RSSP" name="OWNER_USER_RSSP" class="form-control123">
                                                            </div>
                                                            <script>
                                                                $("#idLblTitleUsernameRSSP").text(global_fm_Username_esigncloud);
                                                                if('<%= isCALoad%>' !== JS_IS_WHICH_ABOUT_CA_NC) {
                                                                    $("#idLblNoteUsernameRSSP").text(global_fm_require_label);
                                                                }
                                                            </script>
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $(document).ready(function () {
                                                            $("#idSessIsChoiseOwner").val('1');
                                                            $("input[name='radioCheckOwner']").click(function() {
                                                                if (this.value === 'checkOwnerNew')
                                                                {
                                                                    $("#idSessIsChoiseOwner").val('1');
                                                                    CancelOwnerForm();
                                                                    document.getElementById("CERTIFICATION_PURPOSE").disabled=false;
                                                                    if(localStorage.getItem("localStoreComponentForOwner") !== null) {
                                                                        if(sSpace(localStorage.getItem("localStoreComponentForOwner")) !== "")
                                                                        {
                                                                            var sListComponentForOwner = localStorage.getItem("localStoreComponentForOwner").split('###');
                                                                            for (var i = 0; i < sListComponentForOwner.length; i++) {
                                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                                                                {
                                                                                    var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                                    $("#"+ids).val('');
                                                                                    document.getElementById(ids).disabled=false;
                                                                                    document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).disabled=false;
                                                                                }
                                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                                                                                {
                                                                                    var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                                    $("#"+ids).val('');
                                                                                    document.getElementById(ids).disabled=false;
                                                                                    document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).disabled=false;
                                                                                }
                                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_QD)
                                                                                {
                                                                                    var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                                    $("#"+ids).val('');
                                                                                    document.getElementById(ids).disabled=false;
                                                                                    document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).disabled=false;
                                                                                }
                                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND)
                                                                                {
                                                                                    var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                                    $("#"+ids).val('');
                                                                                    document.getElementById(ids).disabled=false;
                                                                                    document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).disabled=false;
                                                                                }
                                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                                                                                {
                                                                                    var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                                    $("#"+ids).val('');
                                                                                    document.getElementById(ids).disabled=false;
                                                                                    document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).disabled=false;
                                                                                }

                                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                                                                                {
                                                                                    if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE)
                                                                                    {
                                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                                        $("#"+ids).val('');
                                                                                    } else if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_STAFF)
                                                                                    {
                                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                                        $("#"+ids).val('');
                                                                                    } else if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL)
                                                                                    {
                                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                                        $("#"+ids).val('');
                                                                                    }
                                                                                }
                                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                                {
                                                                                    if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE)
                                                                                    {
                                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                                        $("#"+ids).val('');
                                                                                    } else if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_STAFF)
                                                                                    {
                                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                                        $("#"+ids).val('');
                                                                                    }
                                                                                }
                                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_EMAIL)
                                                                                {
                                                                                    var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                                    $("#"+ids).val('');
                                                                                }
                                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PHONE)
                                                                                {
                                                                                    var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                                    $("#"+ids).val('');
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if (this.value === 'checkOwnerExists')
                                                                {
                                                                    $("#idSessIsChoiseOwner").val('0');
                                                                    GetOwnerForm();
                                                                }
                                                            });
                                                        });
                                                        function CancelOwnerForm()
                                                        {
                                                            $("#PHONE_CONTRACT").val('');
                                                            $("#EMAIL_CONTRACT").val('');
                                                            $("#OWNER_USER_RSSP").val('');
                                                            $("#AGREEMENT_UUID_RSSP").val('');
                                                            $("#idShowOwnerSearch").css("display", "none");
                                                            $("#idCancelOwnerForm").css("display", "none");
                                                            $("#hdfOwnerID").val('');
                                                            $("#hdfOwnerUUID").val('');
                                                            $("#hdfOwnerTypeID").val('');
                                                        }
                                                    </script>
                                                </div>
                                                <div class="col-sm-13" style="padding-left: 0;">
                                                    <div class="col-sm-6" style="padding-left: 0;">
                                                        <div class="form-group">
                                                            <label class="radio-inline owner" style="font-weight: bold;">
                                                                <input type="radio" name="radioCheckOwner" id="checkOwnerNew" value="checkOwnerNew" checked>
                                                                <script>document.write(global_fm_option_owner_new);</script>
                                                            </label>&nbsp;&nbsp;
                                                            <label class="radio-inline owner" style="font-weight: bold;">
                                                                <input type="radio" name="radioCheckOwner" id="checkOwnerExists" value="checkOwnerExists">
                                                                <script>document.write(global_fm_option_owner_search);</script>
                                                            </label>
                                                            <input type="text" style="display: none;" id="idSessIsChoiseOwner" name="idSessIsChoiseOwner"/>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-6" style="padding-left: 0;"></div>
                                                </div>
                                            </fieldset>
                                            <fieldset class="scheduler-border" style="clear: both;">
                                                <legend class="scheduler-border"><script>document.write(cert_title_register_cert);</script></legend>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleAgentName" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="AGENT_NAME" id="AGENT_NAME" class="form-control123"
                                                                onchange="LOAD_BACKOFFICE_USER(this.value, '<%= anticsrf%>');">
                                                                <%
                                                                    String sBranchFirst = "";
                                                                    try {
                                                                        BRANCH[][] rst = (BRANCH[][]) session.getAttribute("sessTreeBranchSystemAgency");
                                                                        if (rst[0].length > 0) {
                                                                            for (BRANCH temp1 : rst[0]) {
                                                                                if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                                    if("".equals(sBranchFirst)) {
                                                                                        sBranchFirst = String.valueOf(temp1.ID);
                                                                                        if(iReBRANCH_ID != 0){
                                                                                            sBranchFirst = String.valueOf(iReBRANCH_ID);
                                                                                        }
                                                                                    }
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= iReBRANCH_ID != 0 && temp1.ID == iReBRANCH_ID ? "selected" : "" %>><%=temp1.NAME + " - " + temp1.REMARK%></option>
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
                                                    <script>$("#idLblTitleAgentName").text(global_fm_Branch);</script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="USER" id="USER" class="form-control123">
                                                                <%
                                                                    BACKOFFICE_USER[][] rssUser;
                                                                    if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                                        int rCREATED_BY_ID = iReCREATED_BY_ID;
                                                                        if(rCREATED_BY_ID == 0) {
                                                                            rCREATED_BY_ID = Integer.parseInt(sessUserID);
                                                                        }
                                                                        rssUser = new BACKOFFICE_USER[1][];
                                                                        db.S_BO_GET_USER_BRANCH_ALL(sBranchFirst, rssUser);
                                                                        if (rssUser[0].length > 0) {
                                                                            for (int i = 0; i < rssUser[0].length; i++) {
                                                                %>
                                                                <option value="<%=String.valueOf(rssUser[0][i].ID)%>" <%= rCREATED_BY_ID != 0 && rssUser[0][i].ID == rCREATED_BY_ID ? "selected" : "" %>><%=rssUser[0][i].FULL_NAME + " (" + rssUser[0][i].USERNAME + ")" %></option>
                                                                <%
                                                                            }
                                                                        }
                                                                    } else {
                                                                        if(SessUserAgentID.equals(sBranchFirst)) {
                                                                            rssUser = new BACKOFFICE_USER[1][];
                                                                            db.S_BO_USER_DETAIL(sessUserID, sessLanguageGlobal, rssUser);
                                                                            if(rssUser[0].length > 0) {
                                                                %>
                                                                <option value="<%=String.valueOf(rssUser[0][0].ID)%>"><%=rssUser[0][0].FULL_NAME + " (" + rssUser[0][0].USERNAME + ")" %></option>
                                                                <%
                                                                            }
                                                                        } else {
                                                                            rssUser = new BACKOFFICE_USER[1][];
                                                                            db.S_BO_GET_USER_BRANCH_ALL(sBranchFirst, rssUser);
                                                                            if (rssUser[0].length > 0) {
                                                                                for (int i = 0; i < rssUser[0].length; i++) {
                                                                %>
                                                                <option value="<%=String.valueOf(rssUser[0][i].ID)%>" <%= iReCREATED_BY_ID != 0 && rssUser[0][i].ID == iReCREATED_BY_ID ? "selected" : "" %>><%=rssUser[0][i].FULL_NAME + " (" + rssUser[0][i].USERNAME + ")" %></option>
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
                                                <!-- PHONE OLD POSITION -->
                                                <%
                                                    String sFristCerDurationOrProfileID = "";
                                                    String sFristCA = "";
                                                    String sFristCodeCA = "";
                                                    String sCACoreSubject = "";
                                                    String sFirstPKI_FormFactor = "";
                                                    String sFristCerPurpose="";
                                                    if("".equals(sSessReRegisterCert)){
                                                %>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleCA" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="CERTIFICATION_AUTHORITY" id="CERTIFICATION_AUTHORITY" class="form-control123"
                                                                onchange="LOAD_CERTIFICATION_AUTHORITY(this.value, '<%= anticsrf%>');">
                                                                <%
                                                                    CERTIFICATION_AUTHORITY[][] rssProfile = new CERTIFICATION_AUTHORITY[1][];
                                                                    db.S_BO_CERTIFICATION_AUTHORITY_COMBOBOX(sessLanguageGlobal, rssProfile);
                                                                    if (rssProfile[0].length > 0) {
                                                                        int intCAIDDefault = CommonFunction.getCAIDDefault(strCADefault, rssProfile[0]);
                                                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                                                        {
                                                                            for (int i = 0; i < rssProfile[0].length; i++) {
                                                                                if(rssProfile[0][i].ID == intCAIDDefault)
                                                                                {
                                                                                    intCASelected = String.valueOf(rssProfile[0][i].ID) + "###" + EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT);
                                                                                    if("".equals(sFristCA)) {
                                                                                        sFristCA = String.valueOf(rssProfile[0][i].ID);
                                                                                    }
                                                                                    if("".equals(sFristCodeCA)) {
                                                                                        sFristCodeCA = EscapeUtils.CheckTextNull(rssProfile[0][i].NAME);
                                                                                    }
                                                                                    if("".equals(sCACoreSubject)) {
                                                                                        sCACoreSubject = EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT);
                                                                                    }
                                                                                }
                                                                %>
                                                                <option value="<%=String.valueOf(rssProfile[0][i].ID)
                                                                    + "###" + EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT)%>" <%= rssProfile[0][i].ID == intCAIDDefault ? "selected" : "" %>><%=rssProfile[0][i].REMARK%></option>
                                                                <%
                                                                            }
                                                                        } else {
                                                                            boolean accessProfileAll = CommonFunction.checkAccessProfileAll(sessPolicyCert_Data);
                                                                            if(accessProfileAll == true)
                                                                            {
                                                                                for (int i = 0; i < rssProfile[0].length; i++) {
                                                                                    if(rssProfile[0][i].ID == intCAIDDefault)
                                                                                    {
                                                                                        if("".equals(sFristCA)) {
                                                                                            sFristCA = String.valueOf(rssProfile[0][i].ID);
                                                                                        }
                                                                                        if("".equals(sFristCodeCA)) {
                                                                                            sFristCodeCA = EscapeUtils.CheckTextNull(rssProfile[0][i].NAME);
                                                                                        }
                                                                                        if("".equals(sCACoreSubject)) {
                                                                                            sCACoreSubject = EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT);
                                                                                        }
                                                                                    }
                                                                %>
                                                                <option value="<%=String.valueOf(rssProfile[0][i].ID)
                                                                    + "###" + EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT)%>" <%= rssProfile[0][i].ID == intCAIDDefault ? "selected" : "" %>><%=rssProfile[0][i].REMARK%></option>
                                                                <%
                                                                                }
                                                                            } else {
                                                                                for (int i = 0; i < rssProfile[0].length; i++) {
                                                                                    if(CommonFunction.checkAccessCAForBranch(rssProfile[0][i].NAME, sessPolicyCert_Data) == true)
                                                                                    {
                                                                                        if(rssProfile[0][i].ID == intCAIDDefault)
                                                                                        {
                                                                                            if("".equals(sFristCA)) {
                                                                                                sFristCA = String.valueOf(rssProfile[0][i].ID);
                                                                                            }
                                                                                            if("".equals(sFristCodeCA)) {
                                                                                                sFristCodeCA = EscapeUtils.CheckTextNull(rssProfile[0][i].NAME);
                                                                                            }
                                                                                            if("".equals(sCACoreSubject)) {
                                                                                                sCACoreSubject = EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT);
                                                                                            }
                                                                                        }
                                                                %>
                                                                <option value="<%=String.valueOf(rssProfile[0][i].ID)
                                                                    + "###" + EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT)%>" <%= rssProfile[0][i].ID == intCAIDDefault ? "selected" : "" %>><%=rssProfile[0][i].REMARK%></option>
                                                                <%
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <script>$("#idLblTitleCA").text(global_fm_ca);</script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleCertPurpose" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select id="CERTIFICATION_PURPOSE" name="CERTIFICATION_PURPOSE" class="form-control123"
                                                                onchange="LOAD_CERTIFICATION_PURPOSE($('#idHiddenCerCA').val().split('###')[0], this.value, '<%= anticsrf%>');">
                                                                <%
                                                                    if(!"".equals(sFristCA)) {
                                                                        CERTIFICATION_PURPOSE[][] rsCertPro = new CERTIFICATION_PURPOSE[1][];
                                                                        db.S_BO_CA_GET_CERTIFICATION_PURPOSE_COMBOBOX(sFristCA, sessLanguageGlobal, rsCertPro);
                                                                        if (rsCertPro.length > 0) {
                                                                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                                                            {
                                                                                for (int i = 0; i < rsCertPro[0].length; i++) {
                                                                                    sFristCerPurpose = String.valueOf(rsCertPro[0][0].ID);
                                                                %>
                                                                <option value="<%= String.valueOf(rsCertPro[0][i].ID)%>"><%= rsCertPro[0][i].REMARK%></option>
                                                                <%
                                                                            }
                                                                        } else {
                                                                            boolean accessProfileAll = CommonFunction.checkAccessProfileAll(sessPolicyCert_Data);
                                                                            if(accessProfileAll == true)
                                                                            {
                                                                                for (int i = 0; i < rsCertPro[0].length; i++) {
                                                                                    if("".equals(sFristCerPurpose))
                                                                                    {
                                                                                        sFristCerPurpose = String.valueOf(rsCertPro[0][i].ID);
                                                                                    }
                                                                %>
                                                                <option value="<%= String.valueOf(rsCertPro[0][i].ID)%>"><%= rsCertPro[0][i].REMARK%></option>
                                                                <%
                                                                                }
                                                                            } else {
                                                                                for (int i = 0; i < rsCertPro[0].length; i++) {
                                                                                    if(CommonFunction.checkAccessPurposeForBranch(sFristCodeCA, rsCertPro[0][i].NAME, sessPolicyCert_Data) == true)
                                                                                    {
                                                                                        if("".equals(sFristCerPurpose))
                                                                                        {
                                                                                            sFristCerPurpose = String.valueOf(rsCertPro[0][i].ID);
                                                                                        }
                                                                %>
                                                                <option value="<%= String.valueOf(rsCertPro[0][i].ID)%>"><%= rsCertPro[0][i].REMARK%></option>
                                                                <%
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <script>$("#idLblTitleCertPurpose").text(global_fm_certpurpose);</script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitlePKIFormfactor" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select id="PKI_FORMFACTOR" name="PKI_FORMFACTOR" class="form-control123"
                                                                onchange="LOAD_PROFILE_BY_FORMFACTOR($('#idHiddenCerCA').val().split('###')[0], $('#CERTIFICATION_PURPOSE').val(), this.value, '<%= anticsrf%>');">
                                                                <%
                                                                    if(!"".equals(sFristCerPurpose))
                                                                    {
                                                                        PKI_FORMFACTOR[][] rsPKIFormFactor = new PKI_FORMFACTOR[1][];
                                                                        db.S_BO_CA_GET_PKI_FORMFACTOR_COMBOBOX_FOR_CERTIFICATION_PURPOSE(Integer.parseInt(sFristCerPurpose),
                                                                            sessLanguageGlobal, rsPKIFormFactor);
                                                                        if (rsPKIFormFactor.length > 0) {
                                                                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                                                            {
                                                                                for (int i = 0; i < rsPKIFormFactor[0].length; i++) {
                                                                                        sFirstPKI_FormFactor = String.valueOf(rsPKIFormFactor[0][0].ID);
                                                                %>
                                                                <option value="<%= String.valueOf(rsPKIFormFactor[0][i].ID)%>"><%= rsPKIFormFactor[0][i].REMARK%></option>
                                                                <%
                                                                            }
                                                                        } else {
                                                                            for (int i = 0; i < rsPKIFormFactor[0].length; i++) {
                                                                                if(CommonFunction.checkAccessPKIFormFactorForBranch(rsPKIFormFactor[0][i].NAME, sessPolicyFormFactor_Data) == true)
                                                                                {
                                                                                    if("".equals(sFirstPKI_FormFactor))
                                                                                    {
                                                                                        sFirstPKI_FormFactor = String.valueOf(rsPKIFormFactor[0][i].ID);
                                                                                    }
                                                                %>
                                                                <option value="<%= String.valueOf(rsPKIFormFactor[0][i].ID)%>"><%= rsPKIFormFactor[0][i].REMARK%></option>
                                                                <%
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <script>$("#idLblTitlePKIFormfactor").text(global_fm_Method);</script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleCertDuration"></label>
                                                            <label id="idLblNoteCertDuration"class="CssRequireField"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select id="CERTIFICATION_DURATION" name="CERTIFICATION_DURATION" class="form-control123"
                                                                onchange="LOAD_CERTIFICATION_DURATION(this.value, '<%= anticsrf%>');">
                                                                <%
                                                                    if(!"".equals(sFristCA) && !"".equals(sFristCerPurpose) && !"".equals(sFirstPKI_FormFactor))
                                                                    {
                                                                        CERTIFICATION_PROFILE[][] rsDuration = new CERTIFICATION_PROFILE[1][];
                                                                        db.S_BO_CA_GET_DURATION_COMBOBOX_BY_TYPE(sFristCA, sFristCerPurpose, sFirstPKI_FormFactor, Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION, sessLanguageGlobal, rsDuration);
                                                                        if (rsDuration[0].length > 0) {
                                                                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                                                            {
                                                                                for (int i = 0; i < rsDuration[0].length; i++) {
                                                                                    sFristCerDurationOrProfileID = String.valueOf(rsDuration[0][0].ID);
                                                                                    String sRemarkDuration = "[" + rsDuration[0][i].NAME + "] " + rsDuration[0][i].REMARK;
                                                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                                                        sRemarkDuration = rsDuration[0][i].REMARK;
                                                                                    }
                                                                %>
                                                                <option value="<%= String.valueOf(rsDuration[0][i].ID)%>"><%= sRemarkDuration%></option>
                                                                <%
                                                                            }
                                                                        } else {
                                                                            boolean accessProfileAll = CommonFunction.checkAccessProfileAll(sessPolicyCert_Data);
                                                                            if(accessProfileAll == true)
                                                                            {
                                                                                for (int i = 0; i < rsDuration[0].length; i++) {
                                                                                    if("".equals(sFristCerDurationOrProfileID)) {
                                                                                        sFristCerDurationOrProfileID = String.valueOf(rsDuration[0][i].ID);
                                                                                    }
                                                                                    String sRemarkDuration = "[" + rsDuration[0][i].NAME + "] " + rsDuration[0][i].REMARK;
                                                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                                                        sRemarkDuration = rsDuration[0][i].REMARK;
                                                                                    }
                                                                %>
                                                                <option value="<%= String.valueOf(rsDuration[0][i].ID)%>"><%= sRemarkDuration%></option>
                                                                <%
                                                                                }
                                                                            } else {
                                                                                for (int i = 0; i < rsDuration[0].length; i++) {
                                                                                    if(CommonFunction.checkAccessProfileForBranch(rsDuration[0][i].CERTIFICATION_AUTHORITY_NAME,
                                                                                        rsDuration[0][i].NAME, sessPolicyCert_Data) == true)
                                                                                    {
                                                                                        if("".equals(sFristCerDurationOrProfileID)) {
                                                                                            sFristCerDurationOrProfileID = String.valueOf(rsDuration[0][i].ID);
                                                                                        }
                                                                                        String sRemarkDuration = "[" + rsDuration[0][i].NAME + "] " + rsDuration[0][i].REMARK;
                                                                                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                                                            sRemarkDuration = rsDuration[0][i].REMARK;
                                                                                        }
                                                                %>
                                                                <option value="<%= String.valueOf(rsDuration[0][i].ID)%>"><%= sRemarkDuration%></option>
                                                                <%
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleCertDuration").text(global_fm_duration_cts);
                                                        $("#idLblNoteCertDuration").text(global_fm_require_label);
                                                    </script>
                                                </div>
                                                <%
                                                    String sFormFactorPro = "";
                                                    String sDisplayPKIMode = "none";
                                                    FormFactorJsonProperties jsonGroup = null;
                                                    if(!"".equals(sFirstPKI_FormFactor)) {
                                                        PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                                        db.S_BO_PKI_FORMFACTOR_DETAIL(sFirstPKI_FormFactor, rsFormfactorPro);
                                                        if(rsFormfactorPro[0].length > 0) {
                                                            sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                                        }
                                                        if(!"".equals(sFormFactorPro)) {
                                                            try{
                                                                ObjectMapper objectMapper = new ObjectMapper();
                                                                jsonGroup = objectMapper.readValue(sFormFactorPro, FormFactorJsonProperties.class);
                                                                if(jsonGroup != null) {
                                                                    sDisplayPKIMode = "";
                                                                }
                                                            } catch(Exception e){
                                                                CommonFunction.LogExceptionJSP("FormFactorJsonProperties", e.getMessage(), e);
                                                            }
                                                        }
                                                    }
                                                %>
                                                <div class="col-sm-6" id="idViewPKIMode" style="padding-left: 0;display: <%=sDisplayPKIMode%>">
                                                    <div class="form-group">
                                                        <label id="idLblTitleCertMode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select id="CERTIFICATION_MODE" name="CERTIFICATION_MODE" class="form-control123"
                                                                onchange="LOAD_FORMFACTOR_MODE(this.value);">
                                                                <%
                                                                    if(jsonGroup != null) {
                                                                        try{
                                                                            for (FormFactorJsonProperties.Attribute attribute : jsonGroup.getAttributes()) {
                                                                                if (attribute.getAttributeType().equals(Definitions.CONFIG_FORMFACTOR_ATTRIBUTE_TYPE_MODE)
                                                                                    && attribute.getEnabled() == true) {
                                                                %>
                                                                <option value="<%=attribute.getMode()%>"><%= "1".equals(sessLanguageGlobal) ? attribute.getRemark() : attribute.getRemarkEn() %></option>
                                                                <%
                                                                                }
                                                                            }
                                                                        } catch(Exception e){
                                                                            CommonFunction.LogExceptionJSP("CERTIFICATION_MODE", e.getMessage(), e);
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <script>$("#idLblTitleCertMode").text(global_fm_mode);</script>
                                                </div>
                                                <%
                                                    } else {
                                                %>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleCA" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="CERTIFICATION_AUTHORITY" id="CERTIFICATION_AUTHORITY" class="form-control123"
                                                                onchange="LOAD_CERTIFICATION_AUTHORITY(this.value, '<%= anticsrf%>');">
                                                                <%
                                                                    CERTIFICATION_AUTHORITY[][] rssProfile = new CERTIFICATION_AUTHORITY[1][];
                                                                    db.S_BO_CERTIFICATION_AUTHORITY_COMBOBOX(sessLanguageGlobal, rssProfile);
                                                                    if (rssProfile[0].length > 0) {
                                                                        int intCAIDDefault = iReCERTIFICATION_AUTHORITY_ID;// CommonFunction.getCAIDDefault(strCADefault, rssProfile[0]);
                                                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                                                        {
                                                                            for (int i = 0; i < rssProfile[0].length; i++) {
                                                                                if(rssProfile[0][i].ID == intCAIDDefault)
                                                                                {
                                                                                    intCASelected = String.valueOf(rssProfile[0][i].ID) + "###" + EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT);
                                                                                    if("".equals(sFristCA)) {
                                                                                        sFristCA = String.valueOf(rssProfile[0][i].ID);
                                                                                    }
                                                                                    if("".equals(sFristCodeCA)) {
                                                                                        sFristCodeCA = EscapeUtils.CheckTextNull(rssProfile[0][i].NAME);
                                                                                    }
                                                                                    if("".equals(sCACoreSubject)) {
                                                                                        sCACoreSubject = EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT);
                                                                                    }
                                                                                }
                                                                %>
                                                                <option value="<%=String.valueOf(rssProfile[0][i].ID)
                                                                    + "###" + EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT)%>" <%= rssProfile[0][i].ID == intCAIDDefault ? "selected" : "" %>><%=rssProfile[0][i].REMARK%></option>
                                                                <%
                                                                            }
                                                                        } else {
                                                                            boolean accessProfileAll = CommonFunction.checkAccessProfileAll(sessPolicyCert_Data);
                                                                            if(accessProfileAll == true)
                                                                            {
                                                                                for (int i = 0; i < rssProfile[0].length; i++) {
                                                                                    if(rssProfile[0][i].ID == intCAIDDefault)
                                                                                    {
                                                                                        if("".equals(sFristCA)) {
                                                                                            sFristCA = String.valueOf(rssProfile[0][i].ID);
                                                                                        }
                                                                                        if("".equals(sFristCodeCA)) {
                                                                                            sFristCodeCA = EscapeUtils.CheckTextNull(rssProfile[0][i].NAME);
                                                                                        }
                                                                                        if("".equals(sCACoreSubject)) {
                                                                                            sCACoreSubject = EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT);
                                                                                        }
                                                                                    }
                                                                %>
                                                                <option value="<%=String.valueOf(rssProfile[0][i].ID)
                                                                    + "###" + EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT)%>" <%= rssProfile[0][i].ID == intCAIDDefault ? "selected" : "" %>><%=rssProfile[0][i].REMARK%></option>
                                                                <%
                                                                                }
                                                                            } else {
                                                                                for (int i = 0; i < rssProfile[0].length; i++) {
                                                                                    if(CommonFunction.checkAccessCAForBranch(rssProfile[0][i].NAME, sessPolicyCert_Data) == true)
                                                                                    {
                                                                                        if(rssProfile[0][i].ID == intCAIDDefault)
                                                                                        {
                                                                                            if("".equals(sFristCA)) {
                                                                                                sFristCA = String.valueOf(rssProfile[0][i].ID);
                                                                                            }
                                                                                            if("".equals(sFristCodeCA)) {
                                                                                                sFristCodeCA = EscapeUtils.CheckTextNull(rssProfile[0][i].NAME);
                                                                                            }
                                                                                            if("".equals(sCACoreSubject)) {
                                                                                                sCACoreSubject = EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT);
                                                                                            }
                                                                                        }
                                                                %>
                                                                <option value="<%=String.valueOf(rssProfile[0][i].ID)
                                                                    + "###" + EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT)%>" <%= rssProfile[0][i].ID == intCAIDDefault ? "selected" : "" %>><%=rssProfile[0][i].REMARK%></option>
                                                                <%
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <script>$("#idLblTitleCA").text(global_fm_ca);</script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleCertPurpose" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select id="CERTIFICATION_PURPOSE" name="CERTIFICATION_PURPOSE" class="form-control123"
                                                                onchange="LOAD_CERTIFICATION_PURPOSE($('#idHiddenCerCA').val().split('###')[0], this.value, '<%= anticsrf%>');">
                                                                <%
                                                                    if(!"".equals(sFristCA))
                                                                    {
                                                                        CERTIFICATION_PURPOSE[][] rsCertPro = new CERTIFICATION_PURPOSE[1][];
                                                                        db.S_BO_CA_GET_CERTIFICATION_PURPOSE_COMBOBOX(sFristCA, sessLanguageGlobal, rsCertPro);
                                                                        if (rsCertPro.length > 0) {
                                                                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                                                            {
                                                                                for (int i = 0; i < rsCertPro[0].length; i++) {
                                                                                    sFristCerPurpose = String.valueOf(iReCERTIFICATION_PURPOSE_ID);// String.valueOf(rsCertPro[0][0].ID);
                                                                %>
                                                                <option value="<%= String.valueOf(rsCertPro[0][i].ID)%>" <%= rsCertPro[0][i].ID == iReCERTIFICATION_PURPOSE_ID ? "selected" : "" %>><%= rsCertPro[0][i].REMARK%></option>
                                                                <%
                                                                            }
                                                                        } else {
                                                                            boolean accessProfileAll = CommonFunction.checkAccessProfileAll(sessPolicyCert_Data);
                                                                            if(accessProfileAll == true)
                                                                            {
                                                                                for (int i = 0; i < rsCertPro[0].length; i++) {
                                                                                    if("".equals(sFristCerPurpose))
                                                                                    {
                                                                                        sFristCerPurpose = String.valueOf(iReCERTIFICATION_PURPOSE_ID);// String.valueOf(rsCertPro[0][i].ID);
                                                                                    }
                                                                %>
                                                                <option value="<%= String.valueOf(rsCertPro[0][i].ID)%>" <%= rsCertPro[0][i].ID == iReCERTIFICATION_PURPOSE_ID ? "selected" : "" %>><%= rsCertPro[0][i].REMARK%></option>
                                                                <%
                                                                                }
                                                                            } else {
                                                                                for (int i = 0; i < rsCertPro[0].length; i++) {
                                                                                    if(CommonFunction.checkAccessPurposeForBranch(sFristCodeCA, rsCertPro[0][i].NAME, sessPolicyCert_Data) == true)
                                                                                    {
                                                                                        if("".equals(sFristCerPurpose))
                                                                                        {
                                                                                            sFristCerPurpose = String.valueOf(iReCERTIFICATION_PURPOSE_ID);// String.valueOf(rsCertPro[0][i].ID);
                                                                                        }
                                                                %>
                                                                <option value="<%= String.valueOf(rsCertPro[0][i].ID)%>" <%= rsCertPro[0][i].ID == iReCERTIFICATION_PURPOSE_ID ? "selected" : "" %>><%= rsCertPro[0][i].REMARK%></option>
                                                                <%
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <script>$("#idLblTitleCertPurpose").text(global_fm_certpurpose);</script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitlePKIFormfactor" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select id="PKI_FORMFACTOR" name="PKI_FORMFACTOR" class="form-control123"
                                                                onchange="LOAD_PROFILE_BY_FORMFACTOR($('#idHiddenCerCA').val().split('###')[0], $('#CERTIFICATION_PURPOSE').val(), this.value, '<%= anticsrf%>');">
                                                                <%
                                                                    if(!"".equals(sFristCerPurpose))
                                                                    {
                                                                        PKI_FORMFACTOR[][] rsPKIFormFactor = new PKI_FORMFACTOR[1][];
                                                                        db.S_BO_CA_GET_PKI_FORMFACTOR_COMBOBOX_FOR_CERTIFICATION_PURPOSE(Integer.parseInt(sFristCerPurpose),
                                                                            sessLanguageGlobal, rsPKIFormFactor);
                                                                        if (rsPKIFormFactor.length > 0) {
                                                                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                                                            {
                                                                                for (int i = 0; i < rsPKIFormFactor[0].length; i++) {
//                                                                                    if(rsPKIFormFactor[0][i].ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM) {
                                                                                        sFirstPKI_FormFactor = String.valueOf(iRePKI_FORMFACTOR_ID);// String.valueOf(rsPKIFormFactor[0][0].ID);
                                                                %>
                                                                <option value="<%= String.valueOf(rsPKIFormFactor[0][i].ID)%>" <%= rsPKIFormFactor[0][i].ID == iRePKI_FORMFACTOR_ID ? "selected" : "" %>><%= rsPKIFormFactor[0][i].REMARK%></option>
                                                                <%
//                                                                                }
                                                                            }
                                                                        } else {
                                                                            for (int i = 0; i < rsPKIFormFactor[0].length; i++) {
                                                                                    if(CommonFunction.checkAccessPKIFormFactorForBranch(rsPKIFormFactor[0][i].NAME, sessPolicyFormFactor_Data) == true)
                                                                                    {
                                                                                        if("".equals(sFirstPKI_FormFactor))
                                                                                        {
                                                                                            sFirstPKI_FormFactor = String.valueOf(iRePKI_FORMFACTOR_ID);// String.valueOf(rsPKIFormFactor[0][i].ID);
                                                                                        }
                                                                %>
                                                                <option value="<%= String.valueOf(rsPKIFormFactor[0][i].ID)%>" <%= rsPKIFormFactor[0][i].ID == iRePKI_FORMFACTOR_ID ? "selected" : "" %>><%= rsPKIFormFactor[0][i].REMARK%></option>
                                                                <%
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <script>$("#idLblTitlePKIFormfactor").text(global_fm_Method);</script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleCertDuration"></label>
                                                            <label id="idLblNoteCertDuration"class="CssRequireField"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select id="CERTIFICATION_DURATION" name="CERTIFICATION_DURATION" class="form-control123"
                                                                onchange="LOAD_CERTIFICATION_DURATION(this.value, '<%= anticsrf%>');">
                                                                <%
                                                                    if(!"".equals(sFristCA) && !"".equals(sFristCerPurpose) && !"".equals(sFirstPKI_FormFactor))
                                                                    {
                                                                        CERTIFICATION_PROFILE[][] rsDuration = new CERTIFICATION_PROFILE[1][];
                                                                        db.S_BO_CA_GET_DURATION_COMBOBOX_BY_TYPE(sFristCA, sFristCerPurpose, sFirstPKI_FormFactor, Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION, sessLanguageGlobal, rsDuration);
                                                                        if (rsDuration[0].length > 0) {
                                                                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                                for (int i = 0; i < rsDuration[0].length; i++) {
                                                                                    sFristCerDurationOrProfileID = String.valueOf(iReCERTIFICATION_PROFILE_ID);// String.valueOf(rsDuration[0][0].ID);
                                                                                    String sRemarkDuration = "[" + rsDuration[0][i].NAME + "] " + rsDuration[0][i].REMARK;
                                                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                                                        sRemarkDuration = rsDuration[0][i].REMARK;
                                                                                    }
                                                                %>
                                                                <option value="<%= String.valueOf(rsDuration[0][i].ID)%>" <%=rsDuration[0][i].ID == iReCERTIFICATION_PROFILE_ID ? "selected" : "" %>><%= sRemarkDuration%></option>
                                                                <%
                                                                            }
                                                                        } else {
                                                                            boolean accessProfileAll = CommonFunction.checkAccessProfileAll(sessPolicyCert_Data);
                                                                            if(accessProfileAll == true)
                                                                            {
                                                                                for (int i = 0; i < rsDuration[0].length; i++) {
                                                                                    if("".equals(sFristCerDurationOrProfileID)) {
                                                                                        sFristCerDurationOrProfileID = String.valueOf(iReCERTIFICATION_PROFILE_ID);// String.valueOf(rsDuration[0][i].ID);
                                                                                    }
                                                                                    String sRemarkDuration = "[" + rsDuration[0][i].NAME + "] " + rsDuration[0][i].REMARK;
                                                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                                                        sRemarkDuration = rsDuration[0][i].REMARK;
                                                                                    }
                                                                %>
                                                                <option value="<%= String.valueOf(rsDuration[0][i].ID)%>" <%=rsDuration[0][i].ID == iReCERTIFICATION_PROFILE_ID ? "selected" : "" %>><%= sRemarkDuration%></option>
                                                                <%
                                                                                }
                                                                            } else {
                                                                                for (int i = 0; i < rsDuration[0].length; i++) {
                                                                                    if(CommonFunction.checkAccessProfileForBranch(rsDuration[0][i].CERTIFICATION_AUTHORITY_NAME,
                                                                                        rsDuration[0][i].NAME, sessPolicyCert_Data) == true)
                                                                                    {
                                                                                        if("".equals(sFristCerDurationOrProfileID)) {
                                                                                            sFristCerDurationOrProfileID = String.valueOf(iReCERTIFICATION_PROFILE_ID);// String.valueOf(rsDuration[0][i].ID);
                                                                                        }
                                                                                        String sRemarkDuration = "[" + rsDuration[0][i].NAME + "] " + rsDuration[0][i].REMARK;
                                                                                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                                                            sRemarkDuration = rsDuration[0][i].REMARK;
                                                                                        }
                                                                %>
                                                                <option value="<%= String.valueOf(rsDuration[0][i].ID)%>" <%=rsDuration[0][i].ID == iReCERTIFICATION_PROFILE_ID ? "selected" : "" %>><%= sRemarkDuration%></option>
                                                                <%
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleCertDuration").text(global_fm_duration_cts);
                                                        $("#idLblNoteCertDuration").text(global_fm_require_label);
                                                    </script>
                                                </div>
                                                <%
                                                    String sFormFactorPro = "";
                                                    String sDisplayPKIMode = "none";
                                                    FormFactorJsonProperties jsonGroup = null;
                                                    if(!"".equals(sFirstPKI_FormFactor)) {
                                                        PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                                        db.S_BO_PKI_FORMFACTOR_DETAIL(sFirstPKI_FormFactor, rsFormfactorPro);
                                                        if(rsFormfactorPro[0].length > 0) {
                                                            sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                                        }
                                                        if(!"".equals(sFormFactorPro)) {
                                                            try {
                                                                ObjectMapper objectMapper = new ObjectMapper();
                                                                jsonGroup = objectMapper.readValue(sFormFactorPro, FormFactorJsonProperties.class);
                                                                if(jsonGroup != null) {
                                                                    sDisplayPKIMode = "";
                                                                }
                                                            } catch(Exception e){CommonFunction.LogExceptionServlet(null, "FormFactorJsonProperties: " + e.getMessage(), e);}
                                                        }
                                                    }
                                                %>
                                                <div class="col-sm-6" id="idViewPKIMode" style="padding-left: 0;display: <%=sDisplayPKIMode%>">
                                                    <div class="form-group">
                                                        <label id="idLblTitleCertMode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select id="CERTIFICATION_MODE" name="CERTIFICATION_MODE" class="form-control123"
                                                                onchange="LOAD_FORMFACTOR_MODE(this.value);">
                                                                <%
                                                                    if(jsonGroup != null) {
                                                                        try {
                                                                            for (FormFactorJsonProperties.Attribute attribute : jsonGroup.getAttributes()) {
                                                                                if (attribute.getAttributeType().equals(Definitions.CONFIG_FORMFACTOR_ATTRIBUTE_TYPE_MODE)
                                                                                    && attribute.getEnabled() == true) {
                                                                %>
                                                                <option value="<%=attribute.getMode()%>"><%= "1".equals(sessLanguageGlobal) ? attribute.getRemark() : attribute.getRemarkEn() %></option>
                                                                <%
                                                                                }
                                                                            }
                                                                        } catch(Exception e){CommonFunction.LogExceptionServlet(null, "FormFactorJsonProperties CERTIFICATION_MODE: " + e.getMessage(), e);}
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <script>$("#idLblTitleCertMode").text(global_fm_mode);</script>
                                                </div>
                                                <%
                                                    }
                                                %>
                                                <%
                                                    if("1".equals(sRSSP_ACCESS_ENABLED)) {
                                                %>
                                                <div class="col-sm-6" style="padding-left: 0; display: none;" id="idViewRsspRelying">
                                                    <div class="form-group">
                                                        <label id="idLblTitleCertRsspRelying" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select id="RSSP_RELYING_PARTY" name="RSSP_RELYING_PARTY" class="form-control123">
                                                                <option value="">---</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <script>$("#idLblTitleCertRsspRelying").text(global_fm_rssp_replying_party);</script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0; display: none;" id="idViewRsspAuthenModes">
                                                    <div class="form-group">
                                                        <label id="idLblTitleCertRspAuthen" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select id="RSSP_AUTHENMODES" name="RSSP_AUTHENMODES" class="form-control123">
                                                                <option value="">---</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <script>$("#idLblTitleCertRspAuthen").text(global_fm_rssp_authmodes);</script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0; display: none;" id="idViewRsspSigningProfiles">
                                                    <div class="form-group">
                                                        <label id="idLblTitleSigningProfiles" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select id="SIGNING_PROFILES" name="SIGNING_PROFILES" class="form-control123">
                                                                <option value="">---</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <script>$("#idLblTitleSigningProfiles").text(global_fm_rssp_signning_profiles);</script>
                                                </div>
                                                <%
                                                    }
                                                %>
                                                <input id="idHiddenCerCA" value="<%= sFristCA%>" style="display: none;"/>
                                                <input id="idHiddenCerCoreSubject" value="<%= sCACoreSubject%>" style="display: none;"/>
                                                <input id="idHiddenCerPurpose" value="<%= sFristCerPurpose%>" style="display: none;"/>
                                                <input id="idHiddenCerFactor" value="<%= sFirstPKI_FormFactor%>" style="display: none;"/>
                                                <input id="idHiddenCerDurationOrProfileID" value="<%= sFristCerDurationOrProfileID%>" style="display: none;"/>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleFeeAmount" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="FEE_AMOUNT" disabled id="FEE_AMOUNT" class="form-control123">
                                                        </div>
                                                    </div>
                                                    <script>$("#idLblTitleFeeAmount").text(global_fm_amount_fee);</script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;display: <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "none" : ""%>" id="idViewDURATION_FREE">
                                                    <div class="form-group">
                                                        <label id="idLblTitleDurationFree" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="DURATION_FREE" disabled id="DURATION_FREE" class="form-control123">
                                                        </div>
                                                    </div>
                                                    <script>$("#idLblTitleDurationFree").text(global_fm_date_free);</script>
                                                </div>
                                                <script>
                                                    $(document).ready(function () {
                                                        var vFirstPKI_FormFactor = '<%= sFirstPKI_FormFactor%>';
                                                        if(vFirstPKI_FormFactor === JS_STR_PKI_FORMFACTOR_ID_HARD_TOKEN
                                                            || vFirstPKI_FormFactor === JS_STR_PKI_FORMFACTOR_ID_HARD_TOKEN_SAFENET
                                                            || vFirstPKI_FormFactor === JS_STR_PKI_FORMFACTOR_ID_HARD_TOKEN_SMARTCARD
                                                            || vFirstPKI_FormFactor === JS_STR_PKI_FORMFACTOR_ID_HARD_TOKEN_EPASS3003)
                                                        {
                                                            $("#idDivFfactorHardToken").css("display","");
                                                            $("#idDivFfactorSoftToken").css("display","none");
                                                        } else if(vFirstPKI_FormFactor === JS_STR_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                            $("#idDivFfactorHardToken").css("display","none");
                                                            $("#idDivFfactorSoftToken").css("display","");
                                                        } else {
                                                            $("#idDivFfactorHardToken").css("display","none");
                                                            $("#idDivFfactorSoftToken").css("display","none");
                                                        }
                                                        if('<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_ICA || ('<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_NC && '<%=SessAgentID%>' !== JS_STR_AGENCY_ROOT_CA)){
                                                            $("#idDivFfactorHardToken").css("display","none");
                                                        }
//                                                        var branchNameLogin = '<=sBranchNameLogin%>';
//                                                        getRPRsspForAgency(branchNameLogin);
                                                    });
                                                    function onInputEmailRSSP(objInput, objValue)
                                                    {
                                                        $("#"+objInput).val(objValue);
                                                    }
                                                    function onInputPhoneRSSP(objInput1, objInput2, objValue)
                                                    {
                                                        $("#"+objInput1).val(objValue);
                                                        $("#"+objInput2).val(objValue);
                                                    }
                                                    function LOAD_PKI_FORMFACTOR(vValue)
                                                    {
                                                        if(localStorage.getItem("localStoreSearchForOwner") !== null
                                                            && localStorage.getItem("localStoreSearchForOwner") !== "")
                                                        {
                                                            if(localStorage.getItem("localStoreSearchForOwner") === "1")
                                                            {
                                                                
                                                            } else {
                                                                $("#idSessIsChoiseOwner").val('1');
                                                                $("input[name='radioCheckOwner'][value='checkOwnerNew']").prop('checked', true);
                                                                $("#hdfOwnerID").val('');
                                                                $("#hdfOwnerUUID").val('');
                                                                $("#hdfOwnerTypeID").val('');
                                                            }
                                                        }
                                                        else {
                                                            $("#idSessIsChoiseOwner").val('1');
                                                            $("input[name='radioCheckOwner'][value='checkOwnerNew']").prop('checked', true);
                                                            $("#hdfOwnerID").val('');
                                                            $("#hdfOwnerUUID").val('');
                                                            $("#hdfOwnerTypeID").val('');
                                                        }
                                                        localStorage.setItem("localStoreSearchForOwner", null);
                                                        $("#idShowOwnerSearch").css("display", "none");
                                                        $("#idCancelOwnerForm").css("display", "none");
                                                        if(vValue === JS_STR_PKI_FORMFACTOR_ID_HARD_TOKEN
                                                            || vValue === JS_STR_PKI_FORMFACTOR_ID_HARD_TOKEN_SAFENET
                                                            || vValue === JS_STR_PKI_FORMFACTOR_ID_HARD_TOKEN_SMARTCARD
                                                            || vValue === JS_STR_PKI_FORMFACTOR_ID_HARD_TOKEN_EPASS3003
                                                            || vValue === JS_STR_PKI_FORMFACTOR_ID_PKI_USIM)
                                                        {
                                                            $("#idDivShowFileMana").css("display","");
                                                            $("#idDivFfactorHardToken").css("display","");
                                                            $("#idDivFfactorSoftToken").css("display","none");
                                                            $("#idShowUsernameRSSP").css("display","none");
                                                            $("#idShowAgreementRSSP").css("display","none");
                                                            $("#idViewRsspAuthenModes").css("display","none");
                                                            $("#idViewRsspSigningProfiles").css("display","none");
                                                            $("#idViewRsspRelying").css("display","none");
                                                            $("#idViewRSSPPhoneEnail").css("display","none");
                                                            sessionStorage.setItem("sessHasRSSP", "0");
                                                        } else if(vValue === JS_STR_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                                        {
                                                            $("#idDivShowFileMana").css("display","");
                                                            $("#idDivFfactorHardToken").css("display","none");
                                                            $("#idDivFfactorSoftToken").css("display","");
                                                            $("#idShowUsernameRSSP").css("display","none");
                                                            $("#idShowAgreementRSSP").css("display","none");
                                                            $("#idViewRsspAuthenModes").css("display","none");
                                                            $("#idViewRsspSigningProfiles").css("display","none");
                                                            $("#idViewRsspRelying").css("display","none");
                                                            $("#idViewRSSPPhoneEnail").css("display","none");
                                                            sessionStorage.setItem("sessHasRSSP", "0");
                                                        } else {
                                                            $("#idDivFfactorHardToken").css("display","none");
                                                            $("#idDivFfactorSoftToken").css("display","none");
                                                            sessionStorage.setItem("sessHasRSSP", "0");
                                                            $("#idShowUsernameRSSP").css("display","none");
                                                            $("#idShowAgreementRSSP").css("display","none");
                                                            $("#idViewRsspAuthenModes").css("display","none");
                                                            $("#idViewRsspSigningProfiles").css("display","none");
                                                            $("#idViewRsspRelying").css("display","none");
                                                            $("#idViewRSSPPhoneEnail").css("display","none");
                                                            if(vValue === JS_STR_PKI_FORMFACTOR_ID_ESIGNCLOUD)
                                                            {
                                                                $("#idDivShowFileMana").css("display","");
                                                                if($("#CERTIFICATION_MODE").val() === JS_STR_PKI_FORMFACTOR_MODE_DIRECT) {
                                                                    $("#idShowUsernameRSSP").css("display","");
                                                                    $("#idShowAgreementRSSP").css("display","");
                                                                    $("#idViewRsspAuthenModes").css("display","");
                                                                    if('<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_FPT || '<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_CMC || '<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_MID){
                                                                        $("#idViewRsspSigningProfiles").css("display","");
                                                                    }
                                                                    $("#idViewRsspRelying").css("display","");
                                                                    $("#idViewRSSPPhoneEnail").css("display","");
                                                                    if('<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_CMC || '<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_MID) {
                                                                        document.getElementById("PHONE_CONTRACT").oninput = function() { onInputPhoneRSSP("RSSP_OWNER_PHONE", "OWNER_USER_RSSP", this.value);};
                                                                        document.getElementById("EMAIL_CONTRACT").oninput = function() { onInputEmailRSSP("RSSP_OWNER_EMAIL", this.value);};
                                                                    }
                                                                    sessionStorage.setItem("sessHasRSSP", "1");
                                                                    resetOwnerForm();
                                                                    LOAD_RSSP_RELYING_PARTY('<%=sRSSP_ACCESS_ENABLED%>');
                                                                    LOAD_RSSP_AUTHENMODES('<%=sRSSP_ACCESS_ENABLED%>');
                                                                    LOAD_RSSP_SIGNINGPROFILES('<%=sRSSP_ACCESS_ENABLED%>');
                                                                } else {
                                                                    $("#idDivShowFileMana").css("display","");
                                                                }
                                                                //ABC
                                                            }
                                                            else {
                                                                $("#idDivShowFileMana").css("display","");
                                                                $("#idViewRsspAuthenModes").css("display","none");
                                                                $("#idViewRsspSigningProfiles").css("display","none");
                                                                $("#idViewRSSPPhoneEnail").css("display","none");
                                                                $("#idViewRsspRelying").css("display","none");
                                                            }
                                                        }
                                                        if('<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_ICA || ('<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_NC && '<%=SessAgentID%>' !== JS_STR_AGENCY_ROOT_CA)){
                                                            $("#idDivFfactorHardToken").css("display","none");
                                                        }
                                                        if(localStorage.getItem("localStoreHasSanInfo") !== null
                                                            && localStorage.getItem("localStoreHasSanInfo") !== "null")
                                                        {
                                                            if($("#PKI_FORMFACTOR").val() === JS_STR_PKI_FORMFACTOR_ID_ESIGNCLOUD)
                                                            {
                                                                $("#idViewSanComponent").css("display","none");
                                                            } else {
                                                                $("#idViewSanComponent").css("display","");
                                                            }
                                                        }
                                                    }
                                                    function getRPRsspForAgency(branchNameLogin){
                                                        var isChannel = "";
                                                        console.log(branchNameLogin);
                                                        var mydata = JSON.parse(dataRP);
                                                        for(var i=0;i<mydata.length; i++){
                                                            console.log(mydata[i].agency);
                                                            if(sSpace(mydata[i].agency) === branchNameLogin){
                                                                isChannel = sSpace(mydata[i].channel);
                                                            }
                                                        }
                                                        console.log("isChannel result: " + isChannel);
                                                        return isChannel;
                                                    }
                                                    function LOAD_RSSP_RELYING_PARTY(rsspEnabled)
                                                    {
                                                        if(rsspEnabled === "1"){
                                                            console.log("rsspEnabled: " + rsspEnabled);
                                                            var caLoadEnabled = '<%=isCALoad%>';
                                                            //var branchNameLogin = '<=sBranchNameLogin%>';
                                                            $.ajax({
                                                                type: "post",
                                                                url: "../JSONCommon",
                                                                data: {
                                                                    idParam: 'loadrsspownerlist'
                                                                },
                                                                cache: false,
                                                                success: function (html)
                                                                {
                                                                    if (html.length > 0)
                                                                    {
                                                                        var cbxUSER = document.getElementById("RSSP_RELYING_PARTY");
                                                                        removeOptions(cbxUSER);
                                                                        var obj = JSON.parse(html);
                                                                        if (obj[0].Code === "0")
                                                                        {
                                                                            if(caLoadEnabled === JS_IS_WHICH_ABOUT_CA_NC && '<%=SessAgentID%>' !== JS_STR_AGENT_ROOT) {
                                                                                $("#idViewRsspRelying").css("display","none");
//                                                                                var isChannel = getRPRsspForAgency(branchNameLogin);
//                                                                                console.log("isChannel:" + isChannel);
//                                                                                if(isChannel !== ""){
//                                                                                    for (var i = 0; i < obj.length; i++) {
//                                                                                        var sNameWS = sSpace(obj[i].NAME);
//                                                                                        if(sNameWS === isChannel){
//                                                                                            cbxUSER.options[cbxUSER.options.length] = new Option(sNameWS, sNameWS);
//                                                                                        }
//                                                                                    }
//                                                                                }
                                                                            }
                                                                            for (var i = 0; i < obj.length; i++) {
                                                                                cbxUSER.options[cbxUSER.options.length] = new Option(obj[i].NAME, obj[i].NAME);
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
                                                    }
                                                    function LOAD_RSSP_AUTHENMODES(rsspEnabled)
                                                    {
                                                        if(rsspEnabled === "1"){
                                                            $.ajax({
                                                                type: "post",
                                                                url: "../JSONCommon",
                                                                data: {
                                                                    idParam: 'loadrsspauthenmethodlist'
                                                                },
                                                                cache: false,
                                                                success: function (html)
                                                                {
                                                                    if (html.length > 0)
                                                                    {
                                                                        var cbxUSER = document.getElementById("RSSP_AUTHENMODES");
                                                                        removeOptions(cbxUSER);
                                                                        var obj = JSON.parse(html);
                                                                        if (obj[0].Code === "0")
                                                                        {
                                                                            for (var i = 0; i < obj.length; i++) {
                                                                                cbxUSER.options[cbxUSER.options.length] = new Option(obj[i].NAME, obj[i].NAME);
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
                                                    }
                                                    function LOAD_RSSP_SIGNINGPROFILES(rsspEnabled)
                                                    {
                                                        if(rsspEnabled === "1"){
                                                            $.ajax({
                                                                type: "post",
                                                                url: "../JSONCommon",
                                                                data: {
                                                                    idParam: 'loadrsspsigningprofileslist'
                                                                },
                                                                cache: false,
                                                                success: function (html)
                                                                {
                                                                    if (html.length > 0)
                                                                    {
                                                                        var cbxUSER = document.getElementById("SIGNING_PROFILES");
                                                                        removeOptions(cbxUSER);
                                                                        var obj = JSON.parse(html);
                                                                        if (obj[0].Code === "0")
                                                                        {
                                                                            for (var i = 0; i < obj.length; i++) {
                                                                                cbxUSER.options[cbxUSER.options.length] = new Option(obj[i].NAME, obj[i].NAME);
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
                                                    }
                                                    function resetOwnerForm()
                                                    {
                                                        document.getElementById("CERTIFICATION_PURPOSE").disabled=false;
                                                        if(localStorage.getItem("localStoreComponentForOwner") !== null) {
                                                            if(sSpace(localStorage.getItem("localStoreComponentForOwner")) !== "")
                                                            {
                                                                var sListComponentForOwner = localStorage.getItem("localStoreComponentForOwner").split('###');
                                                                for (var i = 0; i < sListComponentForOwner.length; i++) {
                                                                    if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                                                    {
                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                        $("#"+ids).val('');
                                                                        document.getElementById(ids).disabled=false;
                                                                        document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).disabled=false;
                                                                    }
                                                                    if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                                                                    {
                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                        $("#"+ids).val('');
                                                                        document.getElementById(ids).disabled=false;
                                                                        document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).disabled=false;
                                                                    }
                                                                    if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_QD)
                                                                    {
                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                        $("#"+ids).val('');
                                                                        document.getElementById(ids).disabled=false;
                                                                        document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).disabled=false;
                                                                    }
                                                                    if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND)
                                                                    {
                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                        $("#"+ids).val('');
                                                                        document.getElementById(ids).disabled=false;
                                                                        document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).disabled=false;
                                                                    }
                                                                    if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_CCCD)
                                                                    {
                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                        $("#"+ids).val('');
                                                                        document.getElementById(ids).disabled=false;
                                                                        document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).disabled=false;
                                                                    }
                                                                    if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                                                                    {
                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                        $("#"+ids).val('');
                                                                        document.getElementById(ids).disabled=false;
                                                                        document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).disabled=false;
                                                                    }

                                                                    if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                                                                    {
                                                                        if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE)
                                                                        {
                                                                            var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                            $("#"+ids).val('');
                                                                        } else if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_STAFF)
                                                                        {
                                                                            var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                            $("#"+ids).val('');
                                                                        } else if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL)
                                                                        {
                                                                            var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                            $("#"+ids).val('');
                                                                        }
                                                                    }
                                                                    if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                    {
                                                                        if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE)
                                                                        {
                                                                            var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                            $("#"+ids).val('');
                                                                        } else if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_STAFF)
                                                                        {
                                                                            var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                            $("#"+ids).val('');
                                                                        }
                                                                    }

                                                                    if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_EMAIL)
                                                                    {
                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                        $("#"+ids).val('');
                                                                    }
                                                                    if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PHONE)
                                                                    {
                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                        $("#"+ids).val('');
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                </script>
                                                <%
                                                    boolean actionBackUpKeyEnabled = true;
                                                    if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                        actionBackUpKeyEnabled = CommonFunction.checkActionMajorForBranch(Definitions.CONFIG_MAJOR_TYPE_BACKUP_KEY_ACTION, sessPolicyCert_Data);
                                                    }
                                                %>
                                                <div class="col-sm-6" style="padding-left: 0; display: <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) || (isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) && !SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) ? "none" : ""%>" id="idDivFfactorHardToken">
                                                    <div class="form-group">
                                                        <label id="idLblTitleBackUpKey" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <label class="switch" for="idCheckBackUpKey">
                                                                <input type="checkbox" name="idCheckBackUpKey" id="idCheckBackUpKey"
                                                                       <%= "1".equals(boCheckBackUpKey) ? "checked" : "" %> <%= actionBackUpKeyEnabled ? "" : "disabled" %>/>
                                                                <div class="slider round <%= actionBackUpKeyEnabled ? "" : "disabled" %>"></div>
                                                            </label>
                                                        </div>
                                                    </div>
                                                    <script>$("#idLblTitleBackUpKey").text(regiscert_fm_check_backup_key);</script>
                                                </div>
                                                <div id="idDivFfactorSoftToken">
                                                    <%
                                                        String sViewPUSH_NOTICE_ENABLED = "none";
                                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                            if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                                || SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                                                            {
                                                                sViewPUSH_NOTICE_ENABLED = "";
                                                            } else {
                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                {
                                                                    sViewPUSH_NOTICE_ENABLED = "";
                                                                }
                                                            }
                                                        }
                                                    %>
                                                    <div class="col-sm-6" style="padding-left: 0;display: <%= sViewPUSH_NOTICE_ENABLED%>">
                                                        <div class="form-group">
                                                            <label id="idLblTitlePushNotive" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                                <label class="switch" for="PUSH_NOTICE_ENABLED">
                                                                    <input TYPE="checkbox" <%= "1".equals(pPUSH_NOTICE_ENABLED) ? "checked" : ""%> id="PUSH_NOTICE_ENABLED" name="PUSH_NOTICE_ENABLED" />
                                                                    <div class="slider round"></div>
                                                                </label>
                                                            </div>
                                                        </div>
                                                        <script>$("#idLblTitlePushNotive").text(cert_fm_push_notice);</script>
                                                    </div>
                                                    <%
                                                        String sP12CreateOnlyCA = LoadParamSystem.getParamStart(Definitions.CONFIG_CREATE_P12_USER_AGENCY_ENABLED);
                                                    %>
                                                    <div class="col-sm-13" style="padding-left: 0;clear: both;">
                                                        <div class="form-group">
                                                            <label class="radio-inline" id="idRadioNameCheck1">
                                                                <input type="radio" name="nameCheck" id="nameCheck1" checked>
                                                                <script>document.write(global_fm_choose_genkey_server);</script>
                                                            </label>
                                                            <label class="radio-inline">
                                                                <input type="radio" name="nameCheck" id="nameCheck2">
                                                                <script>document.write(global_fm_choose_genkey_client);</script>
                                                            </label>
                                                            <input type="text" style="display: none;" id="idSessIsChoise" name="idSessIsChoise"/>
                                                        </div>
                                                        <script>
                                                            $(document).ready(function () {
                                                                $("#idLblTitleChooseCSR").text(global_fm_choose_csr);
                                                                $("#input-file-csr").attr("disabled", true);
                                                                if('<%=SessAgentID%>' === JS_STR_AGENT_ROOT){
                                                                    $("#idSessIsChoise").val('1');
                                                                } else {
                                                                    if('<%=sP12CreateOnlyCA%>' === '0') {
                                                                        $("#idSessIsChoise").val('0');
                                                                        $("#input-file-csr").val("");
                                                                        $("#input-file-csr").attr("disabled", false);
                                                                        $("#idCSR").val("");
                                                                        $("#nameCheck2").prop("checked", true);
                                                                        $("#idRadioNameCheck1").css("display", "none");
                                                                    } else {
                                                                        $("#idSessIsChoise").val('1');
                                                                    }
                                                                }
                                                                $("#input-file-csr").val("");
                                                                $('.radio-inline').on('click', function () {
                                                                    var s = $(this).find('input').attr('id');
                                                                    if (s === 'nameCheck1')
                                                                    {
                                                                        $("#idSessIsChoise").val('1');
                                                                        $("#input-file-csr").val("");
                                                                        $("#input-file-csr").attr("disabled", true);
                                                                        $("#idCSR").val("");
                                                                    }
                                                                    if (s === 'nameCheck2')
                                                                    {
                                                                        $("#idSessIsChoise").val('0');
                                                                        $("#input-file-csr").val("");
                                                                        $("#input-file-csr").attr("disabled", false);
                                                                        $("#idCSR").val("");
                                                                    }
                                                                });
                                                            });
                                                        </script>
                                                    </div>
                                                    <div class="col-sm-13" style="padding-left: 0;clear: both;">
                                                        <div class="form-group">
                                                            <textarea id="idCSR" class="form-control123" readonly="true" name="idCSR" style="height: 85px;display: none;"></textarea>
                                                            <INPUT class="form-control123" id="input-file-csr" NAME="input-file-csr" accept=".csr,.txt"
                                                                TYPE="file" onchange="UploadCSR(this);" />
                                                        </div>
                                                    </div>
                                                    <script>
                                                        function ClickCheckCSR()
                                                        {
                                                            if ($("#IsCheckCSR").is(':checked'))
                                                            {
                                                                $("#input-file-csr").val("");
                                                                $("#input-file-csr").attr("disabled", false);
                                                                $("#idCSR").val("");
                                                            }
                                                            else
                                                            {
                                                                $("#input-file-csr").val("");
                                                                $("#input-file-csr").attr("disabled", true);
                                                                $("#idCSR").val("");
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
                                                                funErrorAlert(global_req_file + global_req_file_has_data);
                                                            }
                                                        }
                                                    </script>
                                                </div>

                                                <style>
                                                    .form-control123[readonly]{background-color:#ffffff;opacity:1}
                                                    .form-control123[disabled]{background-color:#eee;opacity:1}
                                                    .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                                    .btn{margin-bottom: 0px;}
                                                </style>
                                                <div style="height: 10px;"></div>
                                                <div id="idDivShowFileMana" style="clear: both;">
                                                    <%
                                                        if(!"".equals(sFristCerPurpose)) {
                                                            String sJSON = "";
                                                            CERTIFICATION_PURPOSE[][] rsPURPOSE = new CERTIFICATION_PURPOSE[1][];
                                                            db.S_BO_CERTIFICATION_PURPOSE_DETAIL_BY_CERTIFICATION_ATTR_TYPE(sFristCerPurpose, Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION, rsPURPOSE);
                                                            if(rsPURPOSE[0].length > 0) {
                                                                sJSON = EscapeUtils.CheckTextNull(rsPURPOSE[0][0].FILE_PROPERTIES);
                                                            }
                                                            if(!"".equals(sJSON)) {
                                                                /*SessionUploadFileCert cartIP = (SessionUploadFileCert) session.getAttribute("sessUploadFileCert");
                                                                cartIP = new SessionUploadFileCert();
                                                                if(rsFileMana != null) {
                                                                    if(rsFileMana[0].length > 0) {
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
//                                                                        }
                                                                        session.setAttribute("sessUploadFileCert", cartIP);
                                                                    }
                                                                }*/
                                                                try {
                                                                    ObjectMapper oMapperParse = new ObjectMapper();
                                                                    FILE_PROFILE_JSON itemParsePush = oMapperParse.readValue(sJSON, FILE_PROFILE_JSON.class);
                                                                    int jFile = 1;
                                                                    if(itemParsePush != null) {
                                                                        for (FILE_PROFILE_JSON.Attribute attribute : itemParsePush.getAttributes()) {
                                                                            String sRemark = attribute.getRemark().trim();
                                                                            String sName = attribute.getName().trim();
                                                                            if(!sName.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT)) {
                                                                                if(attribute.getEnabled() == true) {
        //                                                                            String sNameInID = sName + String.valueOf(jFile);
                                                    %>
                                                    <fieldset class="scheduler-border">
                                                        <legend class="scheduler-border"><%= sRemark%></legend>
                                                        <div class="col-sm-13" style="padding-left: 0;">
                                                            <div class="form-group">
                                                                <%
                                                                    String sClassFileLabel = "col-sm-2";
                                                                    String sClassFileUp = "col-sm-10";
                                                                    if("1".equals(sRepresentEnabled)){
                                                                        sClassFileLabel = "col-sm-3";
                                                                        sClassFileUp = "col-sm-9";
                                                                    }
                                                                %>
                                                                <label id="idLblTitleUploadManager" class="control-label <%=sClassFileLabel%>" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                                <div class="<%=sClassFileUp%>" style="padding-right: 0px;">
                                                                    <input type="file" id="input-file<%=sName%>" style="width: 100%;"
                                                                        onchange="calUploadFile(this, '<%= sName%>');" class="btn btn-default btn-file select_file" multiple>
                                                                </div>
                                                            </div>
                                                            <div class="form-group" style="display: <%="1".equals(sRepresentEnabled) ? "none" : ""%>">
                                                                <label class="control-label123" style="color: red;font-weight: 200;" id="idLblTitleNoteManager"></label>
                                                            </div>
                                                            <script>
                                                                $(document).ready(function () {
                                                                    var sss = '<%=sRepresentEnabled%>';
                                                                    if(sss === "1"){
                                                                        $("#idLblTitleUploadManager").text(global_fm_browse_file + ' (' + global_fm_browse_file_upload + '<%= Integer.parseInt(sMaxLengthFile) / 1024 %>' + 'MB)');
                                                                    } else {
                                                                        $("#idLblTitleUploadManager").text(global_fm_browse_file);
                                                                    }
                                                                });
                                                                $("#idLblTitleNoteManager").text(global_fm_browse_cert_note + '<%= Integer.parseInt(sMaxLengthFile) / 1024 %>' + 'MB' + '. ' + global_fm_fileattach_support + '<%= sArrayFileExten.replace(";", ",") %>');
                                                            </script>
                                                        </div>
                                                        <div style="padding: 10px 0 10px 0;display: none;" id="idDiv<%= sName%>" class="table-responsive">
                                                             <table id="idTable<%= sName%>" class="table table-striped projects">
                                                                 <thead>
                                                                    <th id="idLblTitleTableSST"></th>
                                                                    <th id="idLblTitleTableFileName"></th>
                                                                    <th id="idLblTitleTableSize"></th>
                                                                    <th id="idLblTitleTableAction"></th>
                                                                    <script>
                                                                        $("#idLblTitleTableSST").text(global_fm_STT);
                                                                        $("#idLblTitleTableFileName").text(global_fm_file_name);
                                                                        $("#idLblTitleTableSize").text(global_fm_size);
                                                                        $("#idLblTitleTableAction").text(global_fm_action);
                                                                    </script>
                                                                </thead>
                                                                <tbody id="idTBody<%= sName%>">
                                                                    <tr><td colspan="4" id="idLblTitleTableNoFile"></td></tr>
                                                                    <script>
                                                                        $("#idLblTitleTableNoFile").text(global_no_file_list);
                                                                    </script>
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
                                                                } catch(Exception e){CommonFunction.LogExceptionServlet(null, "FILE_JSON " + e.getMessage(), e);}
                                                            }
                                                        }
                                                    %>
                                                </div>
                                                <script>
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
                                                                enctype: "multipart/form-data;charset=UTF-8",
                                                                success: function (html) {
                                                                    if (html.length > 0)
                                                                    {
                                                                        var obj = JSON.parse(html);
                                                                        if (obj[0].Code === "0")
                                                                        {
//                                                                            if(localStorage.getItem("isHassFile") !== null) {
//                                                                                var s123 = localStorage.getItem("isHassFile");
//                                                                                if(parseInt(s123) > 0) {
//                                                                                    localStorage.setItem("isHassFile", parseInt(s123) + 1);
//                                                                                } else{localStorage.setItem("isHassFile", "1");}
//                                                                            } else {
//                                                                                localStorage.setItem("isHassFile", "1");
//                                                                            }
                                                                            var iconDelete = '<i class="fa fa-pencil"></i> ' + global_fm_button_delete;
                                                                            var iconReview = '<i class="fa fa-pencil"></i> ' + global_fm_view;
                                                                            var cssDelete = '';
                                                                            var cssReview = '';
                                                                            var representEnabled = '<%=sRepresentEnabled%>';
                                                                            if(representEnabled === "1"){
                                                                                iconDelete = ' ' + global_fm_button_delete;
                                                                                iconReview = ' ' + global_fm_view;
                                                                                cssDelete = 'text-align: center;width: 60px;';
                                                                                cssReview = 'text-align: center;width: 60px;';
                                                                            }
                                                                            input1.value = '';
                                                                            $("#idTBody" + idType).empty();
                                                                            var content = "";
                                                                            for (var i = 0; i < obj.length; i++) {
                                                                                if(obj[i].FILE_PROFILE === idType)
                                                                                {
                                                                                    var fileNameLoad = obj[i].FILE_NAME;
                                                                                    var sActionCRL = '<a style="cursor: pointer;'+cssDelete+'" class="btn btn-info\n\
                                                                                        btn-xs" onclick="DeleteTempFile(\'' + obj[i].FILE_PROFILE + '\', \'' + fileNameLoad + '\');">' + iconDelete + '</a>';
                                                                                    var sReviewCRL = "";
                                                                                    var fileNameExt = fileNameLoad.substring(fileNameLoad.lastIndexOf('.')+1);
                                                                                    if(fileNameExt.toUpperCase() === "PDF" || fileNameExt.toUpperCase() === "GIF"  || fileNameExt.toUpperCase() === "JPEG"
                                                                                        || fileNameExt.toUpperCase() === "JPG" || fileNameExt.toUpperCase() === "PNG"){
                                                                                        sReviewCRL = '&nbsp;<a style="cursor: pointer;'+cssReview+'" class="btn btn-info\n\
                                                                                            btn-xs" onclick="ViewTempTwoParamFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + fileNameLoad + '\');">' + iconReview + '</a>';
                                                                                    }
                                                                                    content += "<tr>" +
                                                                                        "<td>" + obj[i].Index + "</td>" +
                                                                                        "<td>" + fileNameLoad + "</td>" +
                                                                                        "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                                        "<td>" + sActionCRL + sReviewCRL +"</td>" +
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
//                                                    function ViewTempFile(vFILE_MANAGER_ID, vFILE_NAME)
//                                                    {
//                                                        window.open('CertificateFileView.jsp?idFile='+vFILE_MANAGER_ID + '&idName=' + vFILE_NAME, '_blank');
//                                                    }
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
//                                                                                if(localStorage.getItem("isHassFile") !== null) {
//                                                                                    var ss = localStorage.getItem("isHassFile");
//                                                                                    if(parseInt(ss) > 0) {
//                                                                                        localStorage.setItem("isHassFile", parseInt(ss) - 1);
//                                                                                    } else {localStorage.setItem("isHassFile", "0");}
//                                                                                }
                                                                                $("#idTBody" + idType).empty();
                                                                                var content = "";
                                                                                var iconDelete = '<i class="fa fa-pencil"></i> ' + global_fm_button_delete;
                                                                                var iconReview = '<i class="fa fa-pencil"></i> ' + global_fm_view;
                                                                                var cssDelete = '';
                                                                                var cssReview = '';
                                                                                var representEnabled = '<%=sRepresentEnabled%>';
                                                                                if(representEnabled === "1"){
                                                                                    iconDelete = ' ' + global_fm_button_delete;
                                                                                    iconReview = ' ' + global_fm_view;
                                                                                    cssDelete = 'text-align: center;width: 60px;';
                                                                                    cssReview = 'text-align: center;width: 60px;';
                                                                                }
                                                                                for (var i = 0; i < obj.length; i++) {
                                                                                    if(obj[i].FILE_PROFILE === idType)
                                                                                    {
                                                                                        var fileNameLoad = obj[i].FILE_NAME;
                                                                                        var sActionCRL = '<a style="cursor: pointer;'+cssDelete+'" class="btn btn-info\n\
                                                                                            btn-xs" onclick="DeleteTempFile(\'' + obj[i].FILE_PROFILE + '\', \'' + obj[i].FILE_NAME + '\');">' + iconDelete + '</a>';
                                                                                        var sReviewCRL = "";
                                                                                        var fileNameExt = fileNameLoad.substring(fileNameLoad.lastIndexOf('.')+1);
                                                                                        if(fileNameExt.toUpperCase() === "PDF" || fileNameExt.toUpperCase() === "GIF"  || fileNameExt.toUpperCase() === "JPEG"
                                                                                            || fileNameExt.toUpperCase() === "JPG" || fileNameExt.toUpperCase() === "PNG"){
                                                                                            sReviewCRL = '&nbsp;<a style="cursor: pointer;'+cssReview+'" class="btn btn-info\n\
                                                                                                btn-xs" onclick="ViewTempTwoParamFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + fileNameLoad + '\');">' + iconReview + '</a>';
                                                                                        }
                                                                                        content += "<tr>" +
                                                                                            "<td>" + obj[i].Index + "</td>" +
                                                                                            "<td>" + obj[i].FILE_NAME + "</td>" +
                                                                                            "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                                            "<td>" + sActionCRL + sReviewCRL +"</td>" +
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
//                                                                                if(localStorage.getItem("isHassFile") !== null) {
//                                                                                    var ss = localStorage.getItem("isHassFile");
//                                                                                    if(parseInt(ss) > 0) {
//                                                                                        localStorage.setItem("isHassFile", parseInt(ss) - 1);
//                                                                                    } else {localStorage.setItem("isHassFile", "0");}
//                                                                                }
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
                                                    function LoadTempFileExists()
                                                    {
                                                        $.ajax({
                                                            type: 'POST',
                                                            url: '../FileManageCommon',
                                                            data: {
                                                                idParam: 'loadtempfilecert'
                                                            },
                                                            cache: false,
                                                            success: function (html) {
                                                                if (html.length > 0)
                                                                {
                                                                    var obj = JSON.parse(html);
                                                                    if (obj[0].Code === "0")
                                                                    {
                                                                        var iconDelete = '<i class="fa fa-pencil"></i> ' + global_fm_button_delete;
                                                                        var iconReview = '<i class="fa fa-pencil"></i> ' + global_fm_view;
                                                                        var cssDelete = '';
                                                                        var cssReview = '';
                                                                        var representEnabled = '<%=sRepresentEnabled%>';
                                                                        if(representEnabled === "1"){
                                                                            iconDelete = ' ' + global_fm_button_delete;
                                                                            iconReview = ' ' + global_fm_view;
                                                                            cssDelete = 'text-align: center;width: 60px;';
                                                                            cssReview = 'text-align: center;width: 60px;';
                                                                        }
                                                                        var sListRequire = localStorage.getItem("localStoreFileProfileSave").split(',');
                                                                        for (var j = 0; j < sListRequire.length; j++) {
                                                                            var content = "";
                                                                            for (var i = 0; i < obj.length; i++) {
                                                                                if(obj[i].FILE_PROFILE === sListRequire[j])
                                                                                {
                                                                                    var fileNameLoad = obj[i].FILE_NAME;
                                                                                    var sActionCRL = '<a style="cursor: pointer;'+cssDelete+'" class="btn btn-info\n\
                                                                                        btn-xs" onclick="DeleteTempFile(\'' + obj[i].FILE_PROFILE + '\', \'' + obj[i].FILE_NAME + '\');">' + iconDelete + '</a>';
                                                                                    var sReviewCRL = "";
                                                                                    var fileNameExt = fileNameLoad.substring(fileNameLoad.lastIndexOf('.')+1);
                                                                                    if(fileNameExt.toUpperCase() === "PDF" || fileNameExt.toUpperCase() === "GIF"  || fileNameExt.toUpperCase() === "JPEG"
                                                                                        || fileNameExt.toUpperCase() === "JPG" || fileNameExt.toUpperCase() === "PNG"){
                                                                                        sReviewCRL = '&nbsp;<a style="cursor: pointer;'+cssReview+'" class="btn btn-info\n\
                                                                                            btn-xs" onclick="ViewTempTwoParamFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + fileNameLoad + '\');">' + iconReview + '</a>';
                                                                                    }
                                                                                    content += "<tr>" +
                                                                                        "<td>" + obj[i].Index + "</td>" +
                                                                                        "<td>" + obj[i].FILE_NAME + "</td>" +
                                                                                        "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                                        "<td>" + sActionCRL + sReviewCRL +"</td>" +
                                                                                        "</tr>";
                                                                               }
                                                                            }
                                                                            console.log("ProfileName-" + j + ": " + sListRequire[j]);
                                                                            console.log("ProfileContent-" + j + ": " + content);
                                                                            $("#idTBody" + sListRequire[j]).append(content);
                                                                            $("#idDiv" + sListRequire[j]).css("display", "");
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        });
                                                        return false;
                                                    }
                                                    function LoadFileManage(idPurpose)
                                                    {
                                                        $.ajax({
                                                            type: 'POST',
                                                            url: '../JSONCommon',
                                                            data: {
                                                                idParam: 'loadfilemanageofpurpose',
                                                                idPurpose: idPurpose,
                                                                attrTypeID: JS_STR_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
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
                                                                        var localStoreFileProfile = new Array();
                                                                        for (var i = 0; i < obj.length; i++) {
                                                                            if(obj[i].NAME !== JS_STR_FILE_PROFILE_CODE_E_CONTRACT && obj[i].NAME !== JS_STR_FILE_PROFILE_CODE_AGREEMENT_PAPER)
                                                                            {
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
                                                                                localStoreFileProfile.push(obj[i].NAME);
                                                                            }
                                                                        }
                                                                        localStorage.setItem("localStoreFileProfileSave", localStoreFileProfile);
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
                                                <div class="col-sm-13" style="padding-left: 0;clear: both;">
                                                    <div class="form-group">
                                                        <fieldset class="scheduler-border">
                                                            <legend class="scheduler-border" id="idLblTitleComponentDN"></legend>
                                                            <div id="idViewCertInfo"></div>
<!--                                                            <div class="col-sm-6" style="padding-left: 0;">
                                                                <div class="form-group">
                                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                        <label id="idLblTitleAddressGPKD"></label>
                                                                    </label>
                                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                                        <input class="form-control123" maxlength="256" id="registerAddressGPKD" name="registerAddressGPKD"/>
                                                                    </div>
                                                                </div>
                                                                <script>
                                                                    $("#idLblTitleAddressGPKD").text(global_fm_address + " (" + collation_fm_print_GPKD + ")");
                                                                </script>
                                                            </div>-->
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
                                                <!-- PROPERTIES SAN -->
                                                
                                                <!-- REPRESENTE SAN -->
                                                <div class="col-sm-13" style="padding-left: 0;clear: both;display: <%= "1".equals(sRepresentEnabled) ? "" : "none" %>;" id="idViewRepresent">
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
                                                                    <input class="form-control123" maxlength="256" value="<%=sRegisterFullname%>" id="registerFullname" name="registerFullname"/>
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
                                                                    <input class="form-control123" maxlength="256" value="<%=sRegisterRole%>" id="registerRole" name="registerRole"/>
                                                                </div>
                                                                <script>
                                                                    $("#idLblTitleRegisterRole").text(global_fm_role);
                                                                    $("#idLblNoteRegisterRole").text(global_fm_require_label);
                                                                </script>
                                                            </div>
                                                            <div id="idDivRegisterCMND">
                                                                <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                    <label id="idLblTitleRegisterCMND"></label>
                                                                    <label id="idLblNoteRegisterCMND" class="CssRequireField"></label>
                                                                </label>
                                                                <div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">
                                                                    <input class="form-control123" maxlength="256" value="<%=sRegisterCMND%>" id="registerCMND" name="registerCMND"/>
                                                                </div>
                                                                <script>
                                                                    $("#idLblTitleRegisterCMND").text(global_fm_CitizenId_I + "/"+global_fm_CMND + "/"+global_fm_HC);
                                                                    $("#idLblNoteRegisterCMND").text(global_fm_require_label);
                                                                </script>
                                                            </div>
                                                            <div>
                                                                <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                    <label id="idLblTitleRegisterIssuedDate"></label>
                                                                    <!--<label id="idLblNoteRegisterIssuedDate" class="CssRequireField"></label>-->
                                                                </label>
                                                                <div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">
                                                                    <input class="form-control123" maxlength="25" value="<%=sRegisterIssuedDate%>" id="registerIssuedDate" name="registerIssuedDate"/>
                                                                </div>
                                                                <script>
                                                                    $("#idLblTitleRegisterIssuedDate").text(global_fm_cmnd_date);
//                                                                    $("#idLblNoteRegisterIssuedDate").text(global_fm_require_label);
                                                                </script>
                                                            </div>
                                                            <div style="clear: both;">
                                                                <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                    <label id="idLblTitleRegisterIssuedPlace"></label>
                                                                    <!--<label id="idLblNoteRegisterIssuedPlace" class="CssRequireField"></label>-->
                                                                </label>
                                                                <div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">
                                                                    <input class="form-control123" maxlength="256" value="<%=sRegisterIssuedPlace%>" id="registerIssuedPlace" name="registerIssuedPlace"/>
                                                                </div>
                                                                <script>
                                                                    $("#idLblTitleRegisterIssuedPlace").text(global_fm_place);
//                                                                    $("#idLblNoteRegisterIssuedPlace").text(global_fm_require_label);
                                                                </script>
                                                            </div>
                                                            <div>
                                                                <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                    <label id="idLblTitleRegisterIssuedAdress"></label>
                                                                    <label id="idLblNoteRegisterIssuedAdress" class="CssRequireField"></label>
                                                                </label>
                                                                <div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">
                                                                    <input class="form-control123" maxlength="256" value="<%=sRegisterIssuedAdress%>" id="registerIssuedAdress" name="registerIssuedAdress"/>
                                                                </div>
                                                                <script>
                                                                    $("#idLblTitleRegisterIssuedAdress").text(token_fm_address_residence);
//                                                                    $("#idLblNoteRegisterIssuedAdress").text(global_fm_require_label);
                                                                </script>
                                                            </div>
                                                        </fieldset>
                                                    </div>
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
                                                                        <input class="form-control123" maxlength="256" id="registerNCRepresentative" name="registerNCRepresentative"/>
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
                                                                        <input class="form-control123" maxlength="256" id="registerNCAddress" name="registerNCAddress"/>
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
                                                <!-- REPRESENTE SAN -->
                                                
                                                <div class="col-sm-13" style="padding-left: 0;clear: both;display: none;" id="idViewDNSInfo">
                                                    <div class="form-group">
                                                        <fieldset class="scheduler-border">
                                                            <legend class="scheduler-border"><script>document.write(global_fm_dns_list);</script></legend>
                                                            <div class="col-sm-13" style="padding-left: 0;">
                                                                <div class="form-group">
                                                                    <label id="idLblTitleDNS" class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                                    <div class="col-sm-8" style="padding-right: 0px;">
                                                                       <input name="idDNSName" id="idDNSName" class="form-control123" />
                                                                    </div>
                                                                    <div class="col-sm-2" style="padding-right: 0px;">
                                                                        <input type="button" id="btnSaveDNS" class="btn btn-info" onclick="addDNSNew('sessDNSNameForSSL');"/>
                                                                        <script>
                                                                            $("#btnSaveDNS").val(global_fm_button_New);
                                                                        </script>
                                                                    </div>
                                                                </div>
                                                                <script>$("#idLblTitleDNS").text(global_fm_dns_name);</script>
                                                            </div>
                                                            <div class="table-responsive">
                                                            <table id="idTableDNS" class="table table-bordered table-striped projects">
                                                                <thead>
                                                                   <th id="idLblTitleTableDNSSTT"></th>
                                                                   <th id="idLblTitleTableDNSName"></th>
                                                                   <th id="idLblTitleTableDNSAction"></th>
                                                                   <script>
                                                                       $("#idLblTitleTableDNSSTT").text(global_fm_STT);
                                                                       $("#idLblTitleTableDNSName").text(global_fm_dns_name);
                                                                       $("#idLblTitleTableDNSAction").text(global_fm_action);
                                                                   </script>
                                                               </thead>
                                                               <tbody id="idTBodyDNS">
                                                                   <tr><td colspan="4" id="idTableDNSNotFound"></td></tr>
                                                                   <script>
                                                                       $("#idTableDNSNotFound").text(global_no_data);
                                                                   </script>
                                                               </tbody>
                                                            </table>
                                                            </div>
                                                        </fieldset>
                                                        <script>
                                                            function addDNSNew(idSessionDNS)
                                                            {
                                                                if (!JSCheckEmptyField($("#idDNSName").val()))
                                                                {
                                                                    $("#idDNSName").focus();
                                                                    funErrorAlert(policy_req_empty + global_fm_dns_name);
                                                                    return false;
                                                                }

                                                                $('body').append('<div id="over"></div>');
                                                                $(".loading-gif").show();
                                                                $.ajax({
                                                                    type: "post",
                                                                    url: "../FileManageCommon",
                                                                    data: {
                                                                        idParam: 'adddnsName',
                                                                        idDNS: $("#idDNSName").val(),
                                                                        idSessionDNS: idSessionDNS
                                                                    },
                                                                    cache: false,
                                                                    success: function (html)
                                                                    {
                                                                        if (html.length > 0)
                                                                        {
                                                                            var obj = JSON.parse(html);
                                                                            if (obj[0].Code === "0")
                                                                            {
                                                                                $("#idDNSName").val('');
                                                                                $("#idTBodyDNS").empty();
                                                                                var content = "";
                                                                                for (var i = 0; i < obj.length; i++) {
                                                                                    var sActionCRL = '<a style="cursor: pointer;" class="btn btn-info\n\
                                                                                        btn-xs" onclick="DeleteDNSNew(\'' + obj[i].DNS_NAME + '\', \'' + idSessionDNS + '\');">\n\
                                                                                        <i class="fa fa-pencil"></i> ' + global_fm_button_delete + '</a>';
                                                                                    content += "<tr>" +
                                                                                        "<td>" + obj[i].Index + "</td>" +
                                                                                        "<td>" + obj[i].DNS_NAME + "</td>" +
                                                                                        "<td>" + sActionCRL +"</td>" +
                                                                                        "</tr>";
                                                                                }
                                                                                $("#idTBodyDNS").append(content);
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
                                                                            $(".loading-gif").hide();
                                                                            $('#over').remove();
                                                                        }
                                                                    }
                                                                });
                                                                return false;
                                                            }
                                                            function DeleteDNSNew(vDNS_NAME, idSessionDNS)
                                                            {
                                                                $('body').append('<div id="over"></div>');
                                                                $(".loading-gif").show();
                                                                $.ajax({
                                                                    type: 'POST',
                                                                    url: '../FileManageCommon',
                                                                    data: {
                                                                        idParam: 'deletednsName',
                                                                        idDNS: vDNS_NAME,
                                                                        idSessionDNS: idSessionDNS
                                                                    },
                                                                    cache: false,
                                                                    success: function (html) {
                                                                        if (html.length > 0)
                                                                        {
                                                                            var obj = JSON.parse(html);
                                                                            if (obj[0].Code === "0")
                                                                            {
                                                                                $("#idTBodyDNS").empty();
                                                                                var content = "";
                                                                                for (var i = 0; i < obj.length; i++) {
                                                                                    var sActionCRL = '<a style="cursor: pointer;" class="btn btn-info\n\
                                                                                        btn-xs" onclick="DeleteDNSNew(\'' + obj[i].DNS_NAME + '\', \'' + idSessionDNS + '\');">\n\
                                                                                        <i class="fa fa-pencil"></i> ' + global_fm_button_delete + '</a>';
                                                                                    content += "<tr>" +
                                                                                        "<td>" + obj[i].Index + "</td>" +
                                                                                        "<td>" + obj[i].DNS_NAME + "</td>" +
                                                                                        "<td>" + sActionCRL +"</td>" +
                                                                                        "</tr>";
                                                                                }
                                                                                $("#idTBodyDNS").append(content);
                                                                                funSuccNoLoad(global_succ_delete);
                                                                            } else if(obj[0].Code === "1")
                                                                            {
                                                                                $("#idTBodyDNS").empty();
                                                                                funSuccNoLoad(global_error_delete);
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
                                                            }
                                                        </script>
                                                    </div>
                                                </div>
                                            </fieldset>
                                            
                                            <script>
                                                function LoadDNCity(cbxCity)
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
                                                                    var vValueSessCity = '<%= iReCITY_PROVINCE_ID%>';
                                                                    cbxTemplateCity.options[cbxTemplateCity.options.length] = new Option("", "");
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        cbxTemplateCity.options[cbxTemplateCity.options.length] = new Option(obj[i].cityprovincedesc, obj[i].cityprovinceId);
                                                                        if(vValueSessCity.toString() !== "0")
                                                                        {
                                                                            if(vValueSessCity.toString() === obj[i].cityprovinceId)
                                                                            {
                                                                                $("#"+ cbxCity +" option[value='" + vValueSessCity.toString() + "']").attr("selected", "selected");
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
                                                                    funErrorAlert(global_errorsql);
                                                                }
                                                            }
                                                        }
                                                    });
                                                    return false;
                                                }
                                                function LoadFormSubjectDN(vCertDurationOrProfileID)
                                                {
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loadcert_profile_list_new',
                                                            vCertDurationOrProfileID: vCertDurationOrProfileID
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            if (html.length > 0)
                                                            {
                                                                var obj = JSON.parse(html);
                                                                $("#idViewCertInfo").empty();
                                                                $("#idViewSanInfo").empty();
                                                                if (obj[0].Code === "0")
                                                                {
                                                                    $("#idViewCertInfo").css("display", "");
                                                                    $("#idViewSanComponent").css("display", "");
                                                                    $("#idViewSanInfo").css("display", "");
                                                                    localStorage.setItem("localStoreCompSTID", null);
                                                                    localStorage.setItem("localStoreCompSTValue", null);
                                                                    var vContent = "";
                                                                    var vSanContent = "";
                                                                    var vPersonalCNBlur = "";
                                                                    var localStoreRequired = new Array();
                                                                    var localStoreInput = new Array();
                                                                    var localStoreSanInput = new Array();
                                                                    var localStoreInputID_OnInput = "";
                                                                    var localStoreInputID_Info = "";
                                                                    var localStoreUID_Info = "";
                                                                    var localStoreComponentForOwner = "";
                                                                    var booHasUIDCompany = false;
                                                                    var caLoadEnabled = '<%=isCALoad%>';
                                                                    var isRepresenseEnabled = '<%=sRepresentEnabled%>';
                                                                    var isGetCompInfoDisplay = "";
                                                                    var isRegisterNoteDN = "";
                                                                    var isRegisterNoteCN = "";
                                                                    if(caLoadEnabled === JS_IS_WHICH_ABOUT_CA_NC && '<%=SessAgentID%>' !== JS_STR_AGENT_ROOT) {
                                                                        isGetCompInfoDisplay = "display:none;";
                                                                    }
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
                                                                    if(caLoadEnabled !== JS_IS_WHICH_ABOUT_CA_ICA) {
                                                                        isRegisterNoteDN = '';
                                                                        isRegisterNoteCN = '';
                                                                    } else {
                                                                        if(booHasUIDCompany === true) {
                                                                            isRegisterNoteDN = "<span style='color:red;'>" + global_fm_register_note + "</span>";
                                                                        } else {
                                                                            isRegisterNoteCN = "<span style='color:red;'>" + global_fm_register_note + "</span>";
                                                                        }
                                                                    }
                                                                    if(booHasUIDCompany === true) {
                                                                        var vContentButton_MST_Radio = "";
                                                                        if(isRepresenseEnabled === "1") {
                                                                            vContentButton_MST_Radio = "<div class='col-sm-13' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                                                <div class='form-group'><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'>";
                                                                            vContentButton_MST_Radio = vContentButton_MST_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID
                                                                                +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID+'" class="form-control123" onlick="OnChangeComboboxMST();"></select>';
                                                                            vContentButton_MST_Radio = vContentButton_MST_Radio + "</div><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;margin-right:0px;padding-right:0;'><input class='form-control123' type='text' id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID + "' oninput='GetAlrmCertMSTMNS(this.value);' />";
                                                                            vContentButton_MST_Radio = vContentButton_MST_Radio + "</div></div><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'><div class='col-sm-3' style='padding-left: 0px;margin-left: 0px;'><input class='btn btn-info' style='width: 120px;"+isGetCompInfoDisplay
                                                                                +"' id='btnGetInfoMST' onclick='GetInfoMST();' value='"+global_fm_button_get_info+"' type='button'/></div><div class='col-sm-9'>"+isRegisterNoteDN+"</div></div></div>";
                                                                            vContentButton_MST_Radio = vContentButton_MST_Radio + '<div class="form-group" id="idViewHintMSTMNS" style="width: 100%;padding-top: 5px;display:none;">'+
                                                                                '<label style="color: red;" id="idHintMSTMNS"></label></div></div>';
                                                                        } else {
                                                                            vContentButton_MST_Radio = "<div class='col-sm-12' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                                            <div class='form-group'><div class='col-sm-3' style='padding-left: 0px;margin-left: 0px;'>";
                                                                            vContentButton_MST_Radio = vContentButton_MST_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID
                                                                                +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID+'" class="form-control123" onlick="OnChangeComboboxMST();"></select>';
                                                                            vContentButton_MST_Radio = vContentButton_MST_Radio + "</div><div class='col-sm-7'><input class='form-control123' type='text' id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID + "' oninput='GetAlrmCertMSTMNS(this.value);' />";
                                                                            vContentButton_MST_Radio = vContentButton_MST_Radio + "</div><div class='col-sm-2'><input class='btn btn-info' style='width: 120px;"+isGetCompInfoDisplay+"' id='btnGetInfoMST' onclick='GetInfoMST();' value='"+global_fm_button_get_info+"' type='button'/></div></div>";
                                                                            vContentButton_MST_Radio = vContentButton_MST_Radio + '<div class="form-group" id="idViewHintMSTMNS" style="width: 100%;padding-top: 5px;display:none;">'+
                                                                                '<label style="color: red;" id="idHintMSTMNS"></label></div><div style="clear:both;"></div></div>';
                                                                        }
                                                                        vContent = vContent + vContentButton_MST_Radio;
                                                                        $("#idViewCertInfo").append(vContent);
                                                                        var cbxCompany = document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID);
                                                                        removeOptions(cbxCompany);
                                                                        var vUID_CODE = "";
                                                                        var vUID_PREFIX = "";
                                                                        var isRequireUID = "0";
                                                                        for (var i = 0; i < obj.length; i++) {
                                                                            if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY)
                                                                            {
                                                                                isRequireUID = obj[i].IsRequired;
                                                                                if(vUID_CODE === "") {
                                                                                    vUID_CODE = obj[i].SubjectDNAttrCode;
                                                                                }
                                                                                if(vUID_PREFIX === "") {
                                                                                    vUID_PREFIX = obj[i].SubjectDNAttrPreFix;
                                                                                }
                                                                                if(caLoadEnabled === JS_IS_WHICH_ABOUT_CA_ICA) {
                                                                                    cbxCompany.options[cbxCompany.options.length] = new Option(obj[i].SubjectDNAttrDesc, obj[i].SubjectDNAttrPreFix);
                                                                                } else{
                                                                                    cbxCompany.options[cbxCompany.options.length] = new Option(obj[i].SubjectDNAttrDesc + ' - ' + obj[i].SubjectDNAttrPreFix, obj[i].SubjectDNAttrPreFix);
                                                                                }
                                                                            }
                                                                        }
                                                                        localStoreUID_Info = localStoreUID_Info + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID + "###" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID+ "###" + vUID_CODE + "###" + isRequireUID + ",";
                                                                        vContent = "";
                                                                        if(cbxCompany.value !== JS_STR_COMPONENT_DN_VALUE_PREFIX_MST) {
                                                                            document.getElementById("btnGetInfoMST").disabled = true;
                                                                        }
                                                                        localStoreComponentForOwner = localStoreComponentForOwner + vUID_PREFIX + "@" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID + "###";
                                                                    }
                                                                    
                                                                    if(booHasUIDPersonal === true) {
                                                                        var vContentButton_CMND_Radio = "";
                                                                        if(isRepresenseEnabled === "1") {
                                                                            vContentButton_CMND_Radio = "<div class='col-sm-13' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                                                <div class='form-group'><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;'>";
                                                                            vContentButton_CMND_Radio = vContentButton_CMND_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID
                                                                                +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID+'" class="form-control123"></select>';
                                                                            vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div><div class='col-sm-6' style='padding-left: 0px;margin-left: 0px;margin-right:0px;padding-right:0;'><input class='form-control123' oninput='GetAlrmCertCMNDHC(this.value);' type='text' id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID + "' />";
                                                                            vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div></div><div class='col-sm-6'>"+isRegisterNoteCN+"</div></div>";
                                                                            vContentButton_CMND_Radio = vContentButton_CMND_Radio + '<div class="form-group" id="idViewHintCMNDHC" style="width: 100%;padding-top: 5px;display:none;">'+
                                                                                '<label style="color: red;" id="idHintCMNDHC"></label></div></div>';
                                                                        } else {
                                                                            vContentButton_CMND_Radio = "<div class='col-sm-12' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                                            <div class='form-group'><div class='col-sm-3' style='padding-left: 0px;margin-left: 0px;'>";
                                                                            vContentButton_CMND_Radio = vContentButton_CMND_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID
                                                                                +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID+'" class="form-control123"></select>';
                                                                            vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div><div class='col-sm-9' style='padding-right:0;'><input class='form-control123' oninput='GetAlrmCertCMNDHC(this.value);' type='text' id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID + "' />";
                                                                            vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div></div>";
                                                                            vContentButton_CMND_Radio = vContentButton_CMND_Radio + '<div class="form-group" id="idViewHintCMNDHC" style="width: 100%;padding-top: 5px;display:none;">'+
                                                                                '<label style="color: red;" id="idHintCMNDHC"></label></div></div>';
                                                                        }
                                                                        vContent = vContent + vContentButton_CMND_Radio;
                                                                        $("#idViewCertInfo").append(vContent);
                                                                        var cbxPersonal = document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID);
                                                                        removeOptions(cbxPersonal);
                                                                        var vUID_CODE = "";
                                                                        var vUID_PREFIX = "";
                                                                        var isRequireUID = "0";
                                                                        for (var i = 0; i < obj.length; i++) {
                                                                            if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL)
                                                                            {
                                                                                isRequireUID = obj[i].IsRequired;
                                                                                if(vUID_CODE === "") {
                                                                                    vUID_CODE = obj[i].SubjectDNAttrCode;
                                                                                }
                                                                                if(vUID_PREFIX === "") {
                                                                                    vUID_PREFIX = obj[i].SubjectDNAttrPreFix;
                                                                                }
                                                                                if(caLoadEnabled === JS_IS_WHICH_ABOUT_CA_ICA) {
                                                                                    cbxPersonal.options[cbxPersonal.options.length] = new Option(obj[i].SubjectDNAttrDesc, obj[i].SubjectDNAttrPreFix);
                                                                                } else{
                                                                                    cbxPersonal.options[cbxPersonal.options.length] = new Option(obj[i].SubjectDNAttrDesc + ' - ' + obj[i].SubjectDNAttrPreFix, obj[i].SubjectDNAttrPreFix);
                                                                                }
                                                                            }
                                                                        }
                                                                        localStoreUID_Info = localStoreUID_Info + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID + "###" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID + "###" + vUID_CODE + "###" + isRequireUID + ",";
                                                                        vContent = "";
                                                                        localStoreComponentForOwner = localStoreComponentForOwner + vUID_PREFIX + "@" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID + "###";
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
                                                                                    '<select class="form-control123" id="' + vInputID + '"></select>' +
                                                                                    '</div>'+
                                                                                    '</div>';
                                                                                LoadDNCity(vInputID);
                                                                            } else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_PHONE)
                                                                            {
                                                                                vContent += '<div>' +
                                                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                                                    '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                                                    '<input class="form-control123" type="text" id="' + vInputID + '" onblur="autoTrimTextField("' + vInputID + '", this.value);" />' +
                                                                                    '</div>'+
                                                                                    '</div>';
                                                                                localStoreComponentForOwner = localStoreComponentForOwner + obj[i].SubjectDNAttrCode + "@" + vInputID + "###";
                                                                            } else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_EMAIL)
                                                                            {
                                                                                vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                                                vContent += '<div>' +
                                                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                                                    '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                                                    '<input class="form-control123" type="text" id="' + vInputID + '" />' +
                                                                                    '</div>'+
                                                                                    '</div>';
                                                                                localStoreComponentForOwner = localStoreComponentForOwner + obj[i].SubjectDNAttrCode + "@" + vInputID + "###";
                                                                                localStoreInputID_OnInput = localStoreInputID_OnInput + JS_STR_COMPONENT_DN_VALUE_EMAIL + "###" + vInputID + ", ";
                                                                            }
                                                                            else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COUNTRY)
                                                                            {
                                                                                var valueCountry = '<%= sDN_Country%>';
                                                                                var displayCountry = "";
                                                                                if(isRepresenseEnabled === "1"){displayCountry = "display:none;";}
                                                                                vContent += '<div style="padding-left: 0px;'+displayCountry+'">' +
                                                                                    '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                                                    '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                                                    '<input disabled class="form-control123" type="text" id="' + vInputID + '" value="'+valueCountry+'" />' +
                                                                                    '</div>' +
                                                                                    '</div>';
                                                                            } else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COMMONNAME
                                                                                || obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_LOCALITY) {
                                                                                vContent += '<div class="form-group" style="clear:both;">' +
                                                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                                                        '<div class="col-sm-10" style="padding-right: 10px;">'+
                                                                                            '<input class="form-control123" type="text" id="' + vInputID + '" />' +
                                                                                        '</div>' +
                                                                                    '</div>';
                                                                            }
                                                                            else
                                                                            {
                                                                                vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                                                vContent += '<div>' +
                                                                                        '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                                                    '<div class="col-sm-4" style="padding-right: 10px;margin-bottom: 10px;">'+
                                                                                    '<input class="form-control123" type="text" id="' + vInputID + '" />' +
                                                                                    '</div>'+
                                                                                    '</div>';
                                                                            }
                                                                            if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                                                                            {
                                                                                localStoreComponentForOwner = localStoreComponentForOwner + obj[i].SubjectDNAttrCode + "@" + vInputID + "###";
                                                                                if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
                                                                                    vPersonalCNBlur = vInputID;
                                                                                }
                                                                            }
                                                                            if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                            {
                                                                                if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_STAFF
                                                                                    || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE
                                                                                    || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL_GOV
                                                                                    || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE_GOV)
                                                                                {
                                                                                    localStoreComponentForOwner = localStoreComponentForOwner + obj[i].SubjectDNAttrCode + "@" + vInputID + "###";
                                                                                }
                                                                            }
                                                                            if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_LOCALITY
                                                                                || obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE
                                                                                || obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COMMONNAME || obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                            {
                                                                                if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_LOCALITY)
                                                                                {
                                                                                    localStoreInputID_Info = localStoreInputID_Info + JS_STR_COMPONENT_DN_VALUE_LOCALITY + "###" + vInputID + ", ";
                                                                                }
                                                                                if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE)
                                                                                {
                                                                                    localStoreInputID_Info = localStoreInputID_Info + JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE + "###" + vInputID + ", ";
                                                                                }
                                                                                if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                                                                                {
                                                                                    localStoreInputID_Info = localStoreInputID_Info + JS_STR_COMPONENT_DN_VALUE_COMMONNAME + "###" + vInputID + ", ";
                                                                                    localStoreInputID_OnInput = localStoreInputID_OnInput + JS_STR_COMPONENT_DN_VALUE_COMMONNAME + "###" + vInputID + ", ";
                                                                                }
                                                                                if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                                {
                                                                                    localStoreInputID_OnInput = localStoreInputID_OnInput + JS_STR_COMPONENT_DN_VALUE_ORGANI + "###" + vInputID + ", ";
                                                                                }
                                                                                if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_STAFF
                                                                                    || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE
                                                                                    || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_CODE_SIGNING)
                                                                                {
                                                                                    if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                                    {
                                                                                        localStoreInputID_Info = localStoreInputID_Info + JS_STR_COMPONENT_DN_VALUE_ORGANI + "###" + vInputID + ", ";
                                                                                    }
                                                                                }
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
                                                                                '<input class="form-control123" type="text" id="' + vInputID + '" />' +
                                                                                '</div>' +
                                                                                '</div>';
                                                                            if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_E_SAN)
                                                                            {
                                                                                localStoreInputID_OnInput = localStoreInputID_OnInput + JS_STR_COMPONENT_DN_VALUE_E_SAN + "###" + vInputID + ", ";
                                                                                localStoreComponentForOwner = localStoreComponentForOwner + obj[i].SubjectDNAttrCode + "@" + vInputID + "###";
                                                                            }
                                                                        }
                                                                    }
                                                                    if(isRepresenseEnabled === "1") {
                                                                        vContent += '<div id="idViewDivRegisterGPKD">' +
                                                                            '<label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + global_fm_address_GPKD + ' <label class="CssRequireField">' + global_fm_require_label + '</label></label> ' +
                                                                            '<div class="col-sm-4" style="padding-right: 10px;">'+
                                                                            '<input class="form-control123" type="text" id="registerAddressGPKD" value="<%=sRegisterAddressGPKD%>" name="registerAddressGPKD" />' +
                                                                            '</div>' +
                                                                            '</div>';
                                                                    } else {
                                                                        vContent += '<div class="col-sm-6" style="padding-left: 0px;display:none;" id="idViewDivRegisterGPKD">' +
                                                                            '<div class="form-group">' +
                                                                            '<label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + global_fm_address_GPKD + ' <label class="CssRequireField">' + global_fm_require_label + '</label></label> ' +
                                                                            '<div class="col-sm-7" style="padding-right: 0px;">'+
                                                                            '<input class="form-control123" type="text" id="registerAddressGPKD" name="registerAddressGPKD" />' +
                                                                            '</div>' +
                                                                            '</div>' +
                                                                            '</div>';
                                                                    }
                                                                    $("#idViewCertInfo").append(vContent);
                                                                    if(vSanContent !== "") {
                                                                        $("#idViewSanInfo").append(vSanContent);
                                                                        localStorage.setItem("localStoreHasSanInfo", "1");
                                                                    } else {
                                                                        $("#idViewSanComponent").css("display", "none");
                                                                        $("#idViewSanInfo").css("display", "none");
                                                                        localStorage.setItem("localStoreHasSanInfo", null);
                                                                    }
                                                                    var vValueSessSubject = '<%= sReSUBJECT%>';
                                                                    if(vValueSessSubject.toString() !== "") {
                                                                        var vDNFromFilter = "";
                                                                        var countUID = 0;
                                                                        if(booHasUIDCompany === true || booHasUIDPersonal === true) {
                                                                            var vDNFromDBSplit = vValueSessSubject.split(',');
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
                                                                                var indexTag = vValueSessSubject.indexOf(sValuePushDB);
                                                                                if(indexTag !== -1) {
                                                                                    if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE + "=") {
                                                                                    } else if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_PHONE + "=") {
                                                                                        var sValuePushDBDataFrist = vValueSessSubject.substring(indexTag, vValueSessSubject.length);
                                                                                        var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                        //                                                                $("#" + vInputID).val(sValuePushDBDataLast.replace(/###/g, ','));
                                                                                        $("#" + vInputID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                                                    } else if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_ORGANUNIT + "=") {
                                                                                        if('<%= printRegisterCAOption%>' === '1')
                                                                                        {
                                                                                            sInputOU = sInputOU + vInputID + "###";
                                                                                        } else {
                                                                                            var sValuePushDBDataFrist = vValueSessSubject.substring(indexTag, vValueSessSubject.length);
                                                                                            var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                        //                                                                    $("#" + vInputID).val(sValuePushDBDataLast.replace(/###/g, ','));
                                                                                            $("#" + vInputID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                                                        }
                                                                                    } else {
                                                                                        if(vInputID.indexOf(JS_STR_COMPONENT_DN_VALUE_UID) !== -1)
                                                                                        {
                                                                                            vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                                                        }
                                                                                        var sValuePushDBDataFrist = vValueSessSubject.substring(indexTag, vValueSessSubject.length);
                                                                                        var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                        //                                                                $("#" + vInputID).val(sValuePushDBDataLast.replace(/###/g, ','));
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
                                                                        // OU process
                                                                        if('<%= printRegisterCAOption%>' === '1')
                                                                        {
                                                                            if(sInputOU !== "")
                                                                            {
                                                                                var sValueOU = "";
                                                                                for (var p = 0; p < obj.length; p++) {
                                                                                    if(obj[p].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD)
                                                                                    {
                                                                                        var sValuePushDB = obj[p].SubjectDNAttrCode + "=" + obj[p].SubjectDNAttrPreFix;
                                                                                        var str_array = vValueSessSubject.split(',');
                                                                                        for(var h = 0; h < str_array.length; h++)
                                                                                        {
                                                                                            var a = sSpace(str_array[h]);
                                                                                            if(a.split('=')[0] === 'OU')
                                                                                            {
                                                                                                sValueOU = sValueOU + a.split('=')[1] + "###";
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(sInputOU.split('###')[0] !== "")
                                                                                {
                                                                                    $("#"+sInputOU.split('###')[0]).val(sValueOU.split('###')[0]);
                                                                                }
                                                                                if(sInputOU.split('###')[1] !== "")
                                                                                {
                                                                                    $("#"+sInputOU.split('###')[1]).val(sValueOU.split('###')[1]);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
                                                                        $("#idLblTitleRepresent").text(cert_title_register_owner);
                                                                        $("#idViewDivRegisterGPKD").css("display", "none");
                                                                        $("#idViewDivRegisterRole").css("display", "none");
                                                                        $("#idDivRegisterCMND").addClass("form-group");
                                                                        document.getElementById("registerFullname").disabled = true;
                                                                        document.getElementById("registerCMND").disabled = true;
                                                                        document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).oninput = function() { OnBlurPersonalID("registerCMND", this.value);};
                                                                        if(vPersonalCNBlur !== ""){
                                                                            document.getElementById(vPersonalCNBlur).oninput = function() { OnBlurPersonalName("registerFullname", this.value);};
                                                                        }
                                                                        $("#idLblNoteRegisterIssuedAdress").text(global_fm_require_label);
                                                                    } else {
                                                                        $("#idLblTitleRepresent").text(global_fm_representative_legal);
                                                                        if(isRepresenseEnabled === "1"){
                                                                            $("#idViewDivRegisterGPKD").css("display", "");
                                                                        }
                                                                        $("#idViewDivRegisterRole").css("display", "");
                                                                        $("#idDivRegisterCMND").removeClass("form-group");
                                                                        document.getElementById("registerFullname").disabled = false;
                                                                        document.getElementById("registerCMND").disabled = false;
                                                                        $("#idLblNoteRegisterIssuedAdress").text('');
                                                                    }
                                                                    localStorage.setItem("localStoreRequiredPersonal", localStoreRequired);
                                                                    localStorage.setItem("localStoreInputPersonal", localStoreInput);
                                                                    localStorage.setItem("localStoreSanInputPersonal", localStoreSanInput);
                                                                    localStorage.setItem("localStoreInputID_Info", localStoreInputID_Info);
                                                                    localStorage.setItem("localStoreComponentForOwner", localStoreComponentForOwner);
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
//                                                                                document.getElementById(idOTemp).readOnly = true;
//                                                                                $("#"+idOTemp).css("background-color","#eee");
                                                                                document.getElementById(idCNTemp).oninput = function() { OnBlurCompany(idOTemp, this.value);};
                                                                            }
                                                                        }
                                                                        // E_SAN filter from E subject
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
                                                                        {//alert(idE_SUBJECTTemp + ' - ' + idE_SANTemp);
                                                                            document.getElementById(idE_SUBJECTTemp).oninput = function() { OnBlurCompany(idE_SANTemp, this.value);};
                                                                        }
                                                                    }
                                                                    if($("#hdfOwnerID").val() !== "")
                                                                    {
                                                                        dialogChooseOwner($("#hdfOwnerID").val(), $("#CERTIFICATION_PURPOSE").val());
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
                                                                    $("#idViewCertInfo").css("display", "none");
                                                                    $("#idViewSanComponent").css("display", "none");
                                                                    $("#idViewSanInfo").css("display", "none");
                                                                    funErrorAlert(global_errorsql);
                                                                }
                                                            }
                                                        }
                                                    });
                                                    return false;
                                                }
                                                function OnBlurPersonalID(idReceived, objValue)
                                                {
                                                    $("#"+idReceived).val(objValue);
                                                }
                                                function OnBlurPersonalName(idReceived, objValue)
                                                {
                                                    $("#"+idReceived).val(objValue);
                                                }
                                                function GetAlrmCertMSTMNS()
                                                {
                                                    var IsMST = "1";
                                                    var IsCall = "0";
                                                    $("#idViewHintMSTMNS").css("display", "none");
                                                    $("#idHintMSTMNS").css("display", "none");
                                                    $("#idHintMSTMNS").text('');
//                                                    var vInputID_Text = JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
                                                    var vMST = $("#"+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val();
                                                    var sSelectedMST_MNS = $("#"+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).val();
                                                    if(sSelectedMST_MNS !== "") {
                                                        if(vMST.length > 6) {
                                                            IsCall = "1";
                                                            vMST = sSelectedMST_MNS + vMST;
                                                        }
                                                    }
//                                                    if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
//                                                    {
//                                                        if(vMST.length > 8)
//                                                        {
//                                                            IsCall = "1";
//                                                            IsMST = "0";
//                                                        }
//                                                    }
//                                                    if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
//                                                    {
//                                                        if(vMST.length > 8)
//                                                        {
//                                                            IsCall = "1";
//                                                            IsMST = "1";
//                                                        }
//                                                    }
//                                                    if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_QD)
//                                                    {
//                                                        if(vMST.length > 8)
//                                                        {
//                                                            IsCall = "1";
//                                                            IsMST = "2";
//                                                        }
//                                                    }
                                                    if(IsCall === "1")
                                                    {
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../RequestCommon",
                                                            data: {
                                                                idParam: 'gethiscertmst',
                                                                IsMST: IsMST,
                                                                vMST: vMST
                                                            },
                                                            cache: false,
                                                            success: function (html)
                                                            {
                                                                var myStrings = sSpace(html).split('#');
                                                                if (myStrings[0] === "0")
                                                                {
                                                                    $("#idViewHintMSTMNS").css("display", "");
                                                                    $("#idHintMSTMNS").css("display", "");
                                                                    if(IsMST === "1")
                                                                    {
                                                                        $("#idHintMSTMNS").text(global_succ_mst_register);
                                                                    }
                                                                    if(IsMST === "0")
                                                                    {
                                                                        $("#idHintMSTMNS").text(global_succ_mns_register);
                                                                    }
                                                                }
                                                                else if (myStrings[0] === "1")
                                                                {
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
                                                            }
                                                        });
                                                        return false;
                                                    }
                                                }
                                                function GetAlrmCertCMNDHC()
                                                {
//                                                    var vInputID_Text = JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
                                                    var vCMND= $("#" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val();
                                                    var IsCMND = "1";
                                                    var IsCall = "0";
                                                    $("#idHintCMNDHC").text('');
                                                    $("#idViewHintCMNDHC").css("display", "none");
                                                    $("#idHintCMNDHC").css("display", "none");
                                                    var sSelectedMST_MNS = $("#"+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).val();
                                                    if(sSelectedMST_MNS !== "") {
                                                        if(vCMND.length > 6) {
                                                            IsCall = "1";
                                                            vCMND = sSelectedMST_MNS + vCMND;
                                                        }
                                                    }
//                                                    if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND)
//                                                    {
//                                                        if(vCMND.length > 8)
//                                                        {
//                                                            IsCall = "1";
//                                                            IsCMND = "1";
//                                                        }
//                                                    }
//                                                    if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_CCCD)
//                                                    {
//                                                        if(vCMND.length > 8)
//                                                        {
//                                                            IsCall = "1";
//                                                            IsCMND = "2";
//                                                        }
//                                                    }
//                                                    if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
//                                                    {
//                                                        if(vCMND.length > 8)
//                                                        {
//                                                            IsCall = "1";
//                                                            IsCMND = "0";
//                                                        }
//                                                    }
                                                    if(IsCall === "1")
                                                    {
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../RequestCommon",
                                                            data: {
                                                                idParam: 'gethiscertcmnd',
                                                                IsCMND: IsCMND,
                                                                vCMND: vCMND
                                                            },
                                                            cache: false,
                                                            success: function (html)
                                                            {
                                                                var myStrings = sSpace(html).split('#');
                                                                if (myStrings[0] === "0")
                                                                {
                                                                    $("#idViewHintCMNDHC").css("display", "");
                                                                    $("#idHintCMNDHC").css("display", "");
                                                                    if(IsCMND === "1")
                                                                    {
                                                                        $("#idHintCMNDHC").text(global_succ_cmnd_register);
                                                                    }
                                                                    if(IsCMND === "0")
                                                                    {
                                                                        $("#idHintCMNDHC").text(global_succ_hc_register);
                                                                    }
                                                                }
                                                                else if (myStrings[0] === "1")
                                                                {
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
                                                            }
                                                        });
                                                        return false;
                                                    }
                                                }
                                                
                                                function GetInfoMST()
                                                {
                                                    var vMST = $("#"+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val();
                                                    if(vMST !== "")
                                                    {
                                                        var vID_Info = localStorage.getItem("localStoreInputID_Info");
                                                        if(vID_Info !== "")
                                                        {
                                                            $.ajax({
                                                            type: "post",
                                                            url: "../JSONCommon",
                                                            data: {
                                                                idParam: 'getcompanyinfomst',
                                                                vMST: vMST
                                                            },
                                                            cache: false,
                                                            success: function (html)
                                                            {
                                                                if (html.length > 0)
                                                                {
                                                                    var obj = JSON.parse(html);
                                                                    if (obj[0].Code === "0")
                                                                    {
                                                                        var sListInputCheckID_Info = localStorage.getItem("localStoreInputID_Info").split(',');
                                                                        for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                                                                            var idCheckEmptyID_Info = sSpace(sListInputCheckID_Info[i].split('###')[0]);
//                                                                            if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_STAFF
//                                                                                || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_SIGNSERVER_STAFF)
                                                                            if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_STAFF)
                                                                            {
                                                                                if(idCheckEmptyID_Info === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                                {
                                                                                    $("#" + sSpace(sListInputCheckID_Info[i].split('###')[1])).val(obj[0].NAME);
                                                                                }
                                                                            }
                                                                            else 
                                                                            {
                                                                                if(idCheckEmptyID_Info === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                                                                                {
                                                                                    $("#" + sSpace(sListInputCheckID_Info[i].split('###')[1])).val(obj[0].NAME);
                                                                                }
//                                                                                if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE
//                                                                                    || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_SIGNSERVER_ENTERPRISE)
                                                                                if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE
                                                                                    || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_CODE_SIGNING)
                                                                                {
                                                                                    if(idCheckEmptyID_Info === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                                    {
                                                                                        $("#" + sSpace(sListInputCheckID_Info[i].split('###')[1])).val(obj[0].NAME);
                                                                                    }
                                                                                }
                                                                            }
                                                                            if(idCheckEmptyID_Info === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE)
                                                                            {
                                                                                if(obj[0].PROVINCE !== "") {
                                                                                    $("#" +sListInputCheckID_Info[i].split('###')[1]).val(obj[0].PROVINCE);
                                                                                }
                                                                            }
                                                                            if(idCheckEmptyID_Info === JS_STR_COMPONENT_DN_VALUE_LOCALITY)
                                                                            {
                                                                                $("#" + sSpace(sListInputCheckID_Info[i].split('###')[1])).val(obj[0].LOCALTION);
                                                                            }
                                                                            if('<%=sRepresentEnabled%>' === '1') {
                                                                                if($("#CERTIFICATION_PURPOSE").val() !== JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
                                                                                    $("#registerFullname").val(obj[0].PRESENTATIVE_NAME);
                                                                                    $("#registerRole").val(obj[0].POSITION);
                                                                                    $("#registerAddressGPKD").val(obj[0].ADDRESS);
                                                                                }
                                                                                $("#registerIssuedAdress").val(obj[0].PERMANENT_ADDRESS);
                                                                                $("#registerCMND").val(obj[0].CCCD);
                                                                                $("#registerIssuedDate").val(obj[0].ISSUE_DATE);
                                                                                $("#registerIssuedPlace").val(obj[0].PLACEOF_ISSUE);
                                                                            }
                                                                        }
                                                                        $("#registerAddressGPKD").val(obj[0].ADDRESS);
                                                                        $("#registerFullname").val(obj[0].PRESENTATIVE_NAME);
                                                                    } else if (obj[0].Code === JS_EX_NO_DATA) {
                                                                        funErrorAlert(global_no_getcompany_data);
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
                                                                else
                                                                {
                                                                    funErrorAlert(global_errorsql);
                                                                }
                                                            }
                                                        });
                                                        return false;
                                                            localStorage.setItem("localStoreInputID_Info", null);
                                                        }
                                                    } else {
                                                        funErrorAlert(policy_req_empty + global_fm_MST);
                                                    }
                                                }
                                                function OnChangeComboboxMST()
                                                {
                                                    var sSelectedMST_MNS = $("#"+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).val();
                                                    alert(sSelectedMST_MNS);
                                                    if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                                                    {
                                                        document.getElementById("btnGetInfoMST").disabled = true;
                                                    }
                                                    if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_QD)
                                                    {
                                                        document.getElementById("btnGetInfoMST").disabled = true;
                                                    }
                                                    if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                                    {
                                                        document.getElementById("btnGetInfoMST").disabled = false;
                                                    }
                                                }
                                                function LOAD_CERTIFICATION_PROFILE(vCertDurationOrProfileID)
                                                {
                                                    if(vCertDurationOrProfileID !== "")
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
                                                                        $("#FEE_AMOUNT").val(obj[0].AMOUNT);
                                                                        $("#DURATION_FREE").val(obj[0].DURATION_FREE);
                                                                        LoadFormSubjectDN(vCertDurationOrProfileID);
                                                                        if(obj[0].DURATION_FREE === "0" || obj[0].DURATION_FREE === 0)
                                                                        {
                                                                            $("#idViewDURATION_FREE").css("display", "none");
                                                                        } else {
                                                                            $("#idViewDURATION_FREE").css("display", "");
                                                                        }
                                                                        if('<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_ICA) {
                                                                            $("#idViewDURATION_FREE").css("display", "none");
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
                                                        $("#FEE_AMOUNT").val('');
                                                        $("#DURATION_FREE").val('');
                                                    }
                                                }
                                                function LOAD_CERTIFICATION_AUTHORITY(objCA, idCSRF)
                                                {
                                                    if(objCA !== null && objCA !== "")
                                                    {
                                                        $("#idHiddenCerCA").val(objCA.split('###')[0]);
                                                        $("#idHiddenCerCoreSubject").val(objCA.split('###')[1]);
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../JSONCommon",
                                                            data: {
                                                                idParam: 'loadcert_authority_ofsofttoken',
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
                                                                        var vValueSessPurpose = '<%= iReCERTIFICATION_PURPOSE_ID%>';
                                                                        var vPurposeIDTransfer = obj[0].ID;
                                                                        for (var i = 0; i < obj.length; i++) {
                                                                            cbxCERTIFICATION_PURPOSE.options[cbxCERTIFICATION_PURPOSE.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                                                            if(vValueSessPurpose.toString() !== "0")
                                                                            {
                                                                                if(vValueSessPurpose.toString() === obj[i].ID)
                                                                                {
                                                                                    vPurposeIDTransfer = obj[i].ID;
                                                                                    $("#CERTIFICATION_PURPOSE option[value='" + vValueSessPurpose.toString() + "']").attr("selected", "selected");
                                                                                }
                                                                            }
                                                                        }
                                                                        $("#idHiddenCerPurpose").val(obj[0].ID);
                                                                        LOAD_CERTIFICATION_PURPOSE(objCA.split('###')[0], vPurposeIDTransfer, idCSRF);
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
                                                }
                                                function LOAD_CERTIFICATION_PURPOSE(objCA, objPurpose, idCSRF)
                                                {
                                                    $("#idHiddenCerPurpose").val(objPurpose);
                                                    LoadFileManage(objPurpose);
                                                    LoadTempFileExists();
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loadpki_formfactorby_purpose',
                                                            idPurpose: objPurpose,
                                                            CsrfToken: idCSRF
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            if (html.length > 0)
                                                            {
                                                                var cbxPKI_FORMFACTOR = document.getElementById("PKI_FORMFACTOR");
                                                                removeOptions(cbxPKI_FORMFACTOR);
                                                                var obj = JSON.parse(html);
                                                                if (obj[0].Code === "0")
                                                                {
                                                                    var vFristPKIFormFactor = "";
                                                                    var vValueSessFormfactor = '<%= iRePKI_FORMFACTOR_ID%>';
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        if(vFristPKIFormFactor === "") {
                                                                            vFristPKIFormFactor = sSpace(obj[i].ID);
                                                                        }
                                                                        cbxPKI_FORMFACTOR.options[cbxPKI_FORMFACTOR.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                                                        if(vValueSessFormfactor.toString() !== "0")
                                                                        {
                                                                            if(vValueSessFormfactor.toString() === obj[i].ID)
                                                                            {
                                                                                vFristPKIFormFactor = obj[i].ID;
                                                                                $("#PKI_FORMFACTOR option[value='" + vValueSessFormfactor.toString() + "']").attr("selected", "selected");
                                                                            }
                                                                        }
                                                                    }
                                                                    $("#idHiddenCerFactor").val(vFristPKIFormFactor);
                                                                    LOAD_PROFILE_BY_FORMFACTOR(objCA, objPurpose, vFristPKIFormFactor, idCSRF);
//                                                                    LOAD_PKI_FORMFACTOR(vFristPKIFormFactor);
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
                                                                    cbxPKI_FORMFACTOR.options[cbxPKI_FORMFACTOR.options.length] = new Option("---", "");
                                                                }
                                                                else {
                                                                    funErrorAlert(global_errorsql);
                                                                }
                                                            }
                                                        }
                                                    });
                                                    return false;
                                                }
                                                function LOAD_DNS_SSL_FORM(sValuePurpose)
                                                {
                                                    if(sValuePurpose === JS_STR_CERTIFICATION_PURPOSE_ID_SSL)
                                                    {
                                                        $("#idViewDNSInfo").css("display","");
                                                    } else {
                                                        $("#idViewDNSInfo").css("display","none");
                                                    }
                                                }
                                                
                                                function LOAD_MODE_BY_FORMFACTOR(objFactor)
                                                {
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loadformfactor_mode',
                                                            idFactor: objFactor
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            if (html.length > 0)
                                                            {
                                                                var cbxCERTIFICATION_MODE = document.getElementById("CERTIFICATION_MODE");
                                                                removeOptions(cbxCERTIFICATION_MODE);
                                                                var obj = JSON.parse(html);
                                                                if (obj[0].Code === "0")
                                                                {
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        cbxCERTIFICATION_MODE.options[cbxCERTIFICATION_MODE.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                                                    }
                                                                    $("#idViewPKIMode").css("display","");
                                                                    LOAD_PKI_FORMFACTOR(objFactor);
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
                                                                    cbxCERTIFICATION_MODE.options[cbxCERTIFICATION_MODE.options.length] = new Option("---", "");
                                                                    $("#idViewPKIMode").css("display","none");
                                                                }
                                                                else {
                                                                    funErrorAlert(global_errorsql);
                                                                }
                                                            }
                                                        }
                                                    });
                                                    return false;
                                                }
                                                
                                                function LOAD_FORMFACTOR_MODE(value) {
                                                    if($("#PKI_FORMFACTOR").val() === JS_STR_PKI_FORMFACTOR_ID_ESIGNCLOUD)
                                                    {
                                                        $("#idDivShowFileMana").css("display","");
                                                        if(value === JS_STR_PKI_FORMFACTOR_MODE_DIRECT) {
                                                            $("#idShowUsernameRSSP").css("display","");
                                                            $("#idShowAgreementRSSP").css("display","");
                                                            $("#idViewRsspAuthenModes").css("display","");
                                                            if('<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_FPT || '<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_CMC || '<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_MID) {
                                                                $("#idViewRsspSigningProfiles").css("display","");
                                                            }
                                                            $("#idViewRsspRelying").css("display","");
                                                            $("#idViewRSSPPhoneEnail").css("display","");
                                                            if('<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_CMC || '<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_MID) {
                                                                document.getElementById("PHONE_CONTRACT").oninput = function() { onInputPhoneRSSP("RSSP_OWNER_PHONE", "OWNER_USER_RSSP", this.value);};
                                                                document.getElementById("EMAIL_CONTRACT").oninput = function() { onInputEmailRSSP("RSSP_OWNER_EMAIL", this.value);};
                                                            }
                                                            sessionStorage.setItem("sessHasRSSP", "1");
                                                            resetOwnerForm();
                                                            LOAD_RSSP_RELYING_PARTY('<%=sRSSP_ACCESS_ENABLED%>');
                                                            LOAD_RSSP_AUTHENMODES('<%=sRSSP_ACCESS_ENABLED%>');
                                                            LOAD_RSSP_SIGNINGPROFILES('<%=sRSSP_ACCESS_ENABLED%>');
                                                        } else {
                                                            sessionStorage.setItem("sessHasRSSP", "0");
                                                            $("#idShowUsernameRSSP").css("display","none");
                                                            $("#idShowAgreementRSSP").css("display","none");
                                                            $("#idViewRsspAuthenModes").css("display","none");
                                                            $("#idViewRsspSigningProfiles").css("display","none");
                                                            $("#idViewRsspRelying").css("display","none");
                                                            $("#idViewRSSPPhoneEnail").css("display","none");
                                                            $("#idDivShowFileMana").css("display","");
                                                        }
                                                    }
                                                }
                                                
                                                function LOAD_PROFILE_BY_FORMFACTOR(objCA, objPurpose, objFactor, idCSRF)
                                                {
                                                    LOAD_MODE_BY_FORMFACTOR(objFactor);
                                                    LOAD_PKI_FORMFACTOR(objFactor);
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loadcert_purpose',
                                                            idCA: objCA,
                                                            idPurpose: objPurpose,
                                                            idFactor: objFactor,
                                                            idAttrType: JS_STR_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION,
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
                                                                    var vValueSessProfile = '<%= iReCERTIFICATION_PROFILE_ID%>';
                                                                    var vValueCALoad = '<%= isCALoad%>';
                                                                    var vProfileIDTransfer = obj[0].ID;
                                                                    if(vValueCALoad === JS_IS_WHICH_ABOUT_CA_ICA && '<%=sSessReRegisterCert%>' === "") {
                                                                        cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option(global_fm_duration_cts_choose, "");
                                                                    }
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        var remarkDuration = '[' + obj[i].NAME + '] ' + obj[i].REMARK;
                                                                        if(vValueCALoad === JS_IS_WHICH_ABOUT_CA_ICA) {
                                                                            remarkDuration = obj[i].REMARK;
                                                                        }
                                                                        cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option(remarkDuration, obj[i].ID);
                                                                        if(vValueSessProfile.toString() !== "0")
                                                                        {
                                                                            if(vValueSessProfile.toString() === obj[i].ID)
                                                                            {
//                                                                                if(vValueCALoad === JS_IS_WHICH_ABOUT_CA_ICA) {
//                                                                                    vProfileIDTransfer = "";
//                                                                                } else {
                                                                                    vProfileIDTransfer = obj[i].ID;
                                                                                    $("#CERTIFICATION_DURATION option[value='" + vValueSessProfile.toString() + "']").attr("selected", "selected");
//                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    if(vValueCALoad === JS_IS_WHICH_ABOUT_CA_ICA && '<%=sSessReRegisterCert%>' === "") {
                                                                        vProfileIDTransfer = "";
                                                                    }
                                                                    console.log("Load vProfileIDTransfer: " + vProfileIDTransfer);
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
                                                function LOAD_PKI_FORMFACTOR_BY_PURPOSE(objPurpose, idCSRF) // cancel
                                                {
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loadpki_formfactorby_purpose',
                                                            idPurpose: objPurpose,
                                                            CsrfToken: idCSRF
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            if (html.length > 0)
                                                            {
                                                                var cbxPKI_FORMFACTOR = document.getElementById("PKI_FORMFACTOR");
                                                                removeOptions(cbxPKI_FORMFACTOR);
                                                                var obj = JSON.parse(html);
                                                                if (obj[0].Code === "0")
                                                                {
                                                                    var vFristPKIFormFactor = "";
                                                                    var vValueSessFormfactor = '<%= iRePKI_FORMFACTOR_ID%>';
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        if(vFristPKIFormFactor === "")
                                                                        {
                                                                            vFristPKIFormFactor = sSpace(obj[i].ID);
                                                                        }
                                                                        cbxPKI_FORMFACTOR.options[cbxPKI_FORMFACTOR.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                                                        if(vValueSessFormfactor.toString() !== "0")
                                                                        {
                                                                            if(vValueSessFormfactor.toString() === obj[i].ID)
                                                                            {
                                                                                vFristPKIFormFactor = obj[i].ID;
                                                                                $("#PKI_FORMFACTOR option[value='" + vValueSessFormfactor.toString() + "']").attr("selected", "selected");
                                                                            }
                                                                        }
                                                                    }
                                                                    LOAD_PKI_FORMFACTOR(vFristPKIFormFactor);
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
                                                                    cbxPKI_FORMFACTOR.options[cbxPKI_FORMFACTOR.options.length] = new Option("---", "");
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
                                                function LOAD_CERTIFICATION_FEE_INDEX()
                                                {
                                                    if($("#CERTIFICATION_DURATION").val() === "")
                                                    {
                                                        $("#FEE_AMOUNT").val('');
                                                        $("#DURATION_FREE").val('');
                                                    }
                                                }
                                                function OnBlurCompany(objInput, objValue)
                                                {
                                                    $("#"+objInput).val(objValue);
                                                }
                                                function OnBlurCompany_SAN(objInput, objValue)
                                                {
                                                    if(objValue !== ""){
                                                        $("#"+objInput).val(objValue);
                                                    }
                                                }
                                            </script>
                                        </form>
                                    </div>
                                    <div class="x_title">
                                        <h2></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <%
                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_ISSUE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                %>
                                                <input type="button" id="btnSaveFooter" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>', '<%=sRSSP_ACCESS_ENABLED%>', '<%=isCALoad%>');" />
                                                <script>document.getElementById("btnSaveFooter").value = global_fm_button_regis;</script>
                                                <%
                                                    }
                                                %>
                                                <input id="btnCloseFooter" class="btn btn-info" type="button" onclick="closeForm();" />
                                            </li>
                                            <script>
                                                document.getElementById("btnCloseFooter").value = global_fm_refresh;
                                            </script>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <%
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
                                </div>
                            </div>
                        </div>
                    <script>
                        $(document).ready(function () {
                            var vIntCASelected = '<%=intCASelected%>';
                            if(vIntCASelected !== '') {
                                $("select#CERTIFICATION_AUTHORITY").val(vIntCASelected);
                            } else {
                                $("select#CERTIFICATION_AUTHORITY").prop('selectedIndex', 0);
                            }
                            LOAD_CERTIFICATION_AUTHORITY($("#CERTIFICATION_AUTHORITY").val(), '<%= anticsrf%>');
                        });
                    </script>
                    </div>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
        </div>
        <!-- HTML Model Confirm Delete -->
        <div id="myModalOTPOwner" class="modal fade" role="dialog">
            <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                 left: 0; height: 100%;" class="loading-gifOwner">
                <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
            </div>
            <div class="modal-dialog modal-800" id="myDialogOTPHardware">
                <div class="modal-content">
                    <div class="modal-header">
                        <div style="width: 70%; float: left;">
                            <h3 class="modal-title" style="font-size: 18px;"> <span id="idLblTitleOwnerSearch"></span></h3>
                            <script>$("#idLblTitleOwnerSearch").text(owner_title_cert_search);</script>
                        </div>
                        <div style="width: 29%; float: right;text-align: right;">
                            <input type="button" id="btnModalSearch" data-switch-get="state" class="btn btn-info" onclick="dialogSearchOwner();" />
                            <input type="button" id="idCloseDialog" onclick="ClearFieldModalSearch();" class="btn btn-info" />
                            <script>
                                document.getElementById("btnModalSearch").value = global_fm_button_search;
                                document.getElementById("idCloseDialog").value = global_fm_button_close;
                            </script>
                        </div>
                    </div>
                    <div class="modal-body">
                        <div class="x_panel">
                            <div class="x_content" style="margin-top: 0px;">
                                <form role="formAddOTPHardware" id="formOTPHardware">
                                    <!--<div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">-->
                                    <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                        <div class="form-group">
                                            <label id="idLblTitleModalTaxCode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input type="text" name="TAXCODE_MODAL" id="TAXCODE_MODAL" class="form-control123"/>
                                            </div>
                                        </div>
                                        <script>$("#idLblTitleModalTaxCode").text(global_fm_MST);</script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                        <div class="form-group">
                                            <label id="idLblTitleModalBudget" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input type="text" name="BUDGET_CODE_MODAL" id="BUDGET_CODE_MODAL" class="form-control123"/>
                                            </div>
                                        </div>
                                        <script>$("#idLblTitleModalBudget").text(global_fm_MNS);</script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                        <div class="form-group">
                                            <label id="idLblTitleModalDicision" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input type="text" name="DECISION_MODAL" id="DECISION_MODAL" class="form-control123"/>
                                            </div>
                                        </div>
                                        <script>$("#idLblTitleModalDicision").text(global_fm_decision);</script>
                                    </div>
                                    <!--</div>-->
                                    <!--<div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;clear: both;">-->
                                    <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                        <div class="form-group">
                                            <label id="idLblTitleModalCMND" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input type="text" name="CMND_MODAL" id="CMND_MODAL" class="form-control123"/>
                                            </div>
                                        </div>
                                        <script>$("#idLblTitleModalCMND").text(global_fm_CMND);</script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                        <div class="form-group">
                                            <label id="idLblTitleModalCCCD" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input type="text" name="CCCD_MODAL" id="CCCD_MODAL" class="form-control123"/>
                                            </div>
                                        </div>
                                        <script>$("#idLblTitleModalCCCD").text(global_fm_CitizenId);</script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                        <div class="form-group">
                                            <label id="idLblTitleModalPASSPORT" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input type="text" name="PASSPORT_MODAL" id="PASSPORT_MODAL" class="form-control123"/>
                                            </div>
                                        </div>
                                        <script>$("#idLblTitleModalPASSPORT").text(global_fm_HC);</script>
                                    </div>
                                    <!--</div>-->
                                    <!--<div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;clear: both;" id="idShowSearchOwnerCompany">-->
                                    <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;" id="idShowSearchOwnerCompany">
                                        <div class="form-group">
                                            <label id="idLblTitleModalCOMPANY_NAME" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input type="text" name="COMPANY_NAME_MODAL" maxlength="150" id="COMPANY_NAME_MODAL" class="form-control123"/>
                                            </div>
                                        </div>
                                        <script>$("#idLblTitleModalCOMPANY_NAME").text(global_fm_grid_company);</script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;" id="idShowSearchOwnerPersonal">
                                        <div class="form-group">
                                            <label id="idLblTitleModalPERSONAL_NAME" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input type="text" name="PERSONAL_NAME_MODAL" maxlength="150" id="PERSONAL_NAME_MODAL" class="form-control123"/>
                                            </div>
                                        </div>
                                        <script>$("#idLblTitleModalPERSONAL_NAME").text(global_fm_grid_personal);</script>
                                    </div>
                                    <!--</div>-->
                                    <!--<div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;clear: both;">-->
                                    <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                        <div class="form-group">
                                            <label id="idLblTitleModalEMAIL" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input type="text" name="EMAIL_MODAL" maxlength="150" id="EMAIL_MODAL" class="form-control123"/>
                                            </div>
                                        </div>
                                        <script>$("#idLblTitleModalEMAIL").text(global_fm_email);</script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                        <div class="form-group">
                                            <label id="idLblTitleModalPHONE" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input type="text" name="PHONE_MODAL" maxlength="16" id="PHONE_MODAL" class="form-control123"/>
                                            </div>
                                        </div>
                                        <script>$("#idLblTitleModalPHONE").text(global_fm_phone);</script>
                                    </div>
                                    <!--</div>-->
                                    <!--<div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;clear: both;" >-->
                                    <div class="col-sm-6" style="padding-left: 0;display: none;" id="idShowSearchOwnerUser">
                                        <div class="form-group">
                                            <label id="idLblTitleModalUserRSSP" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input type="text" name="OWNER_USER_RSSP_MODAL" maxlength="150" id="OWNER_USER_RSSP_MODAL" class="form-control123"/>
                                            </div>
                                        </div>
                                        <script>$("#idLblTitleModalUserRSSP").text(global_fm_Username_esigncloud);</script>
                                    </div>
                                        <!--<div class="col-sm-6" style="padding-left: 0;"></div>-->
                                    <!--</div>-->
                                </form>
                            </div>
                            <script>
                                function dialogSearchOwner()
                                {
                                    if (!JSCheckEmptyField($("#TAXCODE_MODAL").val()) && !JSCheckEmptyField($("#BUDGET_CODE_MODAL").val())
                                        && !JSCheckEmptyField($("#DECISION_MODAL").val())
                                        && !JSCheckEmptyField($("#CMND_MODAL").val()) && !JSCheckEmptyField($("#CCCD_MODAL").val()) && !JSCheckEmptyField($("#PASSPORT_MODAL").val())
                                        && !JSCheckEmptyField($("#COMPANY_NAME_MODAL").val()) && !JSCheckEmptyField($("#PERSONAL_NAME_MODAL").val())
                                        && !JSCheckEmptyField($("#EMAIL_MODAL").val()) && !JSCheckEmptyField($("#PHONE_MODAL").val())
                                        && !JSCheckEmptyField($("#OWNER_USER_RSSP_MODAL").val()))
                                    {
                                        funErrorAlert(global_fm_check_search);
                                        return false;
                                    }
                                    if(JSCheckEmptyField($("#EMAIL_MODAL").val()))
                                    {
                                        if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#EMAIL_MODAL").val()))
                                        {
                                            $("#EMAIL_MODAL").focus();
                                            funErrorAlert(global_req_mail_format);
                                            return false;
                                        }
                                    }
                                    if (JSCheckEmptyField($("#PHONE_MODAL").val()))
                                    {
                                        if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#PHONE_MODAL")))
                                        {
                                            $("#PHONE_MODAL").focus();
                                            funErrorAlert(global_req_phone_format);
                                            return false;
                                        }
                                    }
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gifOwner").show();
                                    $.ajax({
                                        type: "post",
                                        url: "../JSONCommon",
                                        data: {
                                            idParam: 'loadownerforregistercert',
                                            idTAXCODE_MODAL: $("#TAXCODE_MODAL").val(),
                                            idDECISION_MODAL: $("#DECISION_MODAL").val(),
                                            idBUDGET_CODE_MODAL: $("#BUDGET_CODE_MODAL").val(),
                                            idCMND_MODAL: $("#CMND_MODAL").val(),
                                            idCCCD_MODAL: $("#CCCD_MODAL").val(),
                                            idPASSPORT_MODAL: $("#PASSPORT_MODAL").val(),
                                            idCOMPANY_NAME_MODAL: $("#COMPANY_NAME_MODAL").val(),
                                            idPERSONAL_NAME_MODAL: $("#PERSONAL_NAME_MODAL").val(),
                                            idEMAIL_MODAL: $("#EMAIL_MODAL").val(),
                                            idPHONE_MODAL: $("#PHONE_MODAL").val(),
                                            pOWNER_USER_RSSP: $("#OWNER_USER_RSSP_MODAL").val(),
                                            idHasRSSP: sessionStorage.getItem("sessHasRSSP")
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            if (html.length > 0)
                                            {
                                                var obj = JSON.parse(html);
                                                if (obj[0].Code === "0")
                                                {
                                                    $("#idTBodyModalOwner").empty();
                                                    var contentToken = "";
                                                    var indexToken = 1;
                                                    var idPurpose = "";
                                                    for (var i = 0; i < obj.length; i++) {
                                                        var sActiveHTML = '<a style="cursor: pointer;" onclick="dialogChooseOwner(\''+ obj[i].ID +'\', \''+ idPurpose +'\');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> '+ global_fm_choise +' </a>';
                                                        if(sessionStorage.getItem("sessHasRSSP") === "1") {
                                                            if('<%= sRSSP_ACCESS_ENABLED%>' === '1') {
                                                                sActiveHTML = '<a style="cursor: pointer;" onclick="dialogChooseRSSPOwner(\''+ obj[i].OWNER_UUID +'\', \''+ obj[i].EMAIL +'\', \''+ obj[i].PHONE_NUMBER +'\', \''+ obj[i].OWNER_USERNAME +'\');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> '+ global_fm_choise +' </a>';
                                                            }
                                                        }
                                                        var vMST_MSN = obj[i].TAX_CODE;
                                                        if(vMST_MSN === "") {
                                                            vMST_MSN = obj[i].MNS;
                                                        }
                                                        if(vMST_MSN === "") {
                                                            vMST_MSN = obj[i].DECISION;
                                                        }
                                                        var vCMND_HC = obj[i].CMND;
                                                        if(vCMND_HC === "") {
                                                            vCMND_HC = obj[i].CCCD;
                                                        }
                                                        if(vCMND_HC === "") {
                                                            vCMND_HC = obj[i].HC;
                                                        }
                                                        contentToken += "<tr>" +
                                                            "<td>" + indexToken + "</td>" +
                                                            "<td>" + obj[i].COMPANY_NAME + "</td>" +
                                                            "<td>" + vMST_MSN + "</td>" +
                                                            "<td>" + obj[i].PERSONAL_NAME + "</td>" +
                                                            "<td>" + vCMND_HC + "</td>" +
                                                            "<td>" + obj[i].EMAIL + "</td>" +
                                                            "<td>" + obj[i].PHONE_NUMBER + "</td>" +
                                                            "<td>" + sActiveHTML + "</td>" +
                                                            "</tr>";
                                                        indexToken++;
                                                    }
                                                    if(contentToken !== "")
                                                    {
                                                        $("#idTBodyModalOwner").append(contentToken);
                                                        $("#idShowResultModalSearch").css("display", "");
                                                    } else{
                                                        $("#idShowResultModalSearch").css("display", "none");
                                                    }
                                                }
                                                else if (obj[0].Code === "2")
                                                {
                                                    funErrorAlert(obj[0].Message);
                                                }
                                                else if (obj[0].Code === JS_EX_CSRF)
                                                {
                                                    funCsrfAlert();
                                                }
                                                else if (obj[0].Code === JS_EX_LOGIN)
                                                {
                                                    RedirectPageLoginNoSess(global_alert_login);
                                                }
                                                else if (obj[0].Code === "1")
                                                {
                                                    $("#idTBodyModalOwner").empty();
                                                    var contentToken = "<tr><td colspan='8' id='idLblTitleTableNoFile'>"+global_no_data+"</td></tr>";
                                                    $("#idTBodyModalOwner").append(contentToken);
                                                    $("#idShowResultModalSearch").css("display", "");
                                                }
                                                else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                                {
                                                    RedirectPageLoginNoSess(global_alert_another_login);
                                                }
                                                else {
                                                    funErrorAlert(global_errorsql);
                                                }
                                                $(".loading-gifOwner").hide();
                                                $('#over').remove();
                                            }
                                        }
                                    });
                                    return false;
                                }
                                function dialogChooseRSSPOwner(idUUID, idEMAIL, idPHONE, idUSERNAME) {
                                    $("#hdfOwnerUUID").val(idUUID);
                                    $("#idShowOwnerSearch").css("display", "");
                                    $("#OWNER_USER_RSSP").val(idUSERNAME);
                                    $("#PHONE_CONTRACT").val(idPHONE);
                                    $("#EMAIL_CONTRACT").val(idEMAIL);
                                    ClearFieldModalSearch();
                                    LOAD_RSSP_RELYING_PARTY('<%=sRSSP_ACCESS_ENABLED%>');
                                    LOAD_RSSP_AUTHENMODES('<%=sRSSP_ACCESS_ENABLED%>');
                                    LOAD_RSSP_SIGNINGPROFILES('<%=sRSSP_ACCESS_ENABLED%>');
                                    $(".loading-gifOwner").hide();
                                    $('#over').remove();
                                }
                                function dialogChooseOwner(idOwner, idPurpose)
                                {
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gifOwner").show();
                                    localStorage.setItem("localStoreSearchForOwner", null);
                                    $.ajax({
                                        type: "post",
                                        url: "../JSONCommon",
                                        data: {
                                            idParam: 'chooseownerforcert',
                                            idOwner: idOwner
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            if (html.length > 0)
                                            {
                                                var obj = JSON.parse(html);
                                                if (obj[0].Code === "0")
                                                {
                                                    localStorage.setItem("localStoreSearchForOwner", "1");
                                                    var vOWNER_TYPE_ID = obj[0].OWNER_TYPE_ID;
                                                    $("#hdfOwnerID").val(obj[0].OWNER_ID);
                                                    $("#hdfOwnerTypeID").val(vOWNER_TYPE_ID);
                                                    var vMST = obj[0].TAX_CODE;
                                                    var vMSN = obj[0].MNS;
                                                    var vDECISION = obj[0].DECISION;
                                                    var vCMND = obj[0].CMND;
                                                    var vCCCD = obj[0].CCCD;
                                                    var vHC = obj[0].HC;
                                                    var vPHONE_CONTRACT = obj[0].PHONE_NUMBER;
                                                    var vEMAIL = obj[0].EMAIL;
                                                    var vCOMPANY_NAME = obj[0].COMPANY_NAME;
                                                    var vPERSONAL_NAME = obj[0].PERSONAL_NAME;
                                                    if(idPurpose === '')
                                                    {
                                                        if(vOWNER_TYPE_ID === JS_STR_OWNER_TYPE_ID_ENTERPRISE)
                                                        {
                                                            $("#CERTIFICATION_PURPOSE").val(JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE);
                                                            LOAD_CERTIFICATION_PURPOSE($("#CERTIFICATION_AUTHORITY").val().split('###')[0], JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE, '<%= anticsrf%>');
                                                            //document.getElementById("CERTIFICATION_PURPOSE").disabled=true;
                                                        } else if(vOWNER_TYPE_ID === JS_STR_OWNER_TYPE_ID_STAFF)
                                                        {
                                                            $("#CERTIFICATION_PURPOSE").val(JS_STR_CERTIFICATION_PURPOSE_ID_STAFF);
                                                            LOAD_CERTIFICATION_PURPOSE($("#CERTIFICATION_AUTHORITY").val().split('###')[0], JS_STR_CERTIFICATION_PURPOSE_ID_STAFF, '<%= anticsrf%>');
                                                            //document.getElementById("CERTIFICATION_PURPOSE").disabled=true;
                                                        } else if(vOWNER_TYPE_ID === JS_STR_OWNER_TYPE_ID_PERSONAL)
                                                        {
                                                            $("#CERTIFICATION_PURPOSE").val(JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL);
                                                            LOAD_CERTIFICATION_PURPOSE($("#CERTIFICATION_AUTHORITY").val().split('###')[0], JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL, '<%= anticsrf%>');
                                                            //document.getElementById("CERTIFICATION_PURPOSE").disabled=true;
                                                        } else if(vOWNER_TYPE_ID === JS_STR_OWNER_TYPE_ID_ENTERPRISE_GOV)
                                                        {
                                                            $("#CERTIFICATION_PURPOSE").val(JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE_GOV);
                                                            LOAD_CERTIFICATION_PURPOSE($("#CERTIFICATION_AUTHORITY").val().split('###')[0], JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE_GOV, '<%= anticsrf%>');
                                                            //document.getElementById("CERTIFICATION_PURPOSE").disabled=true;
                                                        } else if(vOWNER_TYPE_ID === JS_STR_OWNER_TYPE_ID_PERSONAL_GOV)
                                                        {
                                                            $("#CERTIFICATION_PURPOSE").val(JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL_GOV);
                                                            LOAD_CERTIFICATION_PURPOSE($("#CERTIFICATION_AUTHORITY").val().split('###')[0], JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL_GOV, '<%= anticsrf%>');
                                                            //document.getElementById("CERTIFICATION_PURPOSE").disabled=true;
                                                        }
                                                    }
                                                    $("#idShowOwnerSearch").css("display", "");
//                                                    $("#idCancelOwnerForm").css("display", "");
                                                    $("#PHONE_CONTRACT").val(vPHONE_CONTRACT);
                                                    $("#EMAIL_CONTRACT").val(vEMAIL);
                                                    ClearFieldModalSearch();
                                                    setTimeout(function() {
                                                        if(sSpace(localStorage.getItem("localStoreComponentForOwner")) !== "")
                                                        {
                                                            var sListComponentForOwner = localStorage.getItem("localStoreComponentForOwner").split('###');
                                                            for (var i = 0; i < sListComponentForOwner.length; i++) {
                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST
                                                                    || sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS
                                                                    || sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_QD)
                                                                {
                                                                    var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                    if(vMST !== "") {
                                                                        document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).value=JS_STR_COMPONENT_DN_VALUE_PREFIX_MST;
                                                                        $("#"+ids).val(vMST);
                                                                    } else if(vMSN !== "") {
                                                                        document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).value=JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS;
                                                                        $("#"+ids).val(vMSN);
                                                                    } else if(vDECISION !== "") {
                                                                        document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).value=JS_STR_COMPONENT_DN_VALUE_PREFIX_QD;
                                                                        $("#"+ids).val(vDECISION);
                                                                    }
//                                                                    if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST) {
//                                                                        $("#"+ids).val(vMST);
//                                                                    } else if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
//                                                                    {
//                                                                        $("#"+ids).val(vMSN);
//                                                                    }
                                                                    if($("#CERTIFICATION_PURPOSE").val() !== JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL_GOV
                                                                        && $("#CERTIFICATION_PURPOSE").val() !== JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE_GOV)
                                                                    {
                                                                        //document.getElementById(ids).disabled=true;
                                                                        //document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).disabled=true;
                                                                    }
                                                                }
//                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
//                                                                {
//                                                                    var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
//                                                                    $("#"+ids).val(vMSN);
//                                                                    document.getElementById(ids).disabled=true;
//                                                                    document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).disabled=true;
//                                                                }
                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND
                                                                    || sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC
                                                                    || sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_CCCD)
                                                                {
                                                                    var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                    if(vCMND !== "") {
                                                                        document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).value=JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND;
                                                                        $("#"+ids).val(vCMND);
                                                                    } else if(vCCCD !== "") {
                                                                        document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).value=JS_STR_COMPONENT_DN_VALUE_PREFIX_CCCD;
                                                                        $("#"+ids).val(vCCCD);
                                                                    } else if(vHC !== "") {
                                                                        document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).value=JS_STR_COMPONENT_DN_VALUE_PREFIX_HC;
                                                                        $("#"+ids).val(vHC);
                                                                    }
//                                                                    if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND) {
//                                                                        $("#"+ids).val(vCMND);
//                                                                    } else if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
//                                                                    {
//                                                                        $("#"+ids).val(vHC);
//                                                                    }
                                                                    if($("#CERTIFICATION_PURPOSE").val() !== JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL_GOV
                                                                        && $("#CERTIFICATION_PURPOSE").val() !== JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE_GOV)
                                                                    {
                                                                        //document.getElementById(ids).disabled=true;
                                                                        //document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).disabled=true;
                                                                    }
                                                                }
//                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
//                                                                {
//                                                                    var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
//                                                                    $("#"+ids).val(vHC);
//                                                                    document.getElementById(ids).disabled=true;
//                                                                    document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).disabled=true;
//                                                                }
                                                                
                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                                                                {
                                                                    if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE)
                                                                    {
                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                        $("#"+ids).val(vCOMPANY_NAME);
                                                                    } else if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_STAFF)
                                                                    {
                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                        $("#"+ids).val(vPERSONAL_NAME);
                                                                    } else if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL)
                                                                    {
                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                        $("#"+ids).val(vPERSONAL_NAME);
                                                                    } else if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL_GOV)
                                                                    {
                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                        $("#"+ids).val(vPERSONAL_NAME);
                                                                    } else if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE_GOV)
                                                                    {
                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                        $("#"+ids).val(vPERSONAL_NAME);
                                                                    }
                                                                }
                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                {
                                                                    if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE)
                                                                    {
                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                        $("#"+ids).val(vCOMPANY_NAME);
                                                                    } else if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_STAFF)
                                                                    {
                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                        $("#"+ids).val(vCOMPANY_NAME);
                                                                    } else if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL_GOV
                                                                        || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE_GOV)
                                                                    {
                                                                        var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                        $("#"+ids).val(vCOMPANY_NAME);
                                                                    }
                                                                }
                                                                
                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_EMAIL)
                                                                {
                                                                    var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                    $("#"+ids).val(vEMAIL);
                                                                }
                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_E_SAN)
                                                                {
                                                                    var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                    $("#"+ids).val(vEMAIL);
                                                                }
                                                                if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PHONE)
                                                                {
                                                                    var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                                    $("#"+ids).val(vPHONE_CONTRACT);
                                                                }
                                                            }
                                                        }
                                                        
    //                                                    if(vMST_MSN === "")
    //                                                    {
    //                                                        vMST_MSN = obj[0].MNS;
    //                                                    }
    //                                                    if(vCMND_HC === "")
    //                                                    {
    //                                                        vCMND_HC = obj[0].HC;
    //                                                    }
    //                                                    $("#MST_MST_SEARCH").val(vMST_MSN);
    //                                                    $("#CMND_HC_SEARCH").val(vCMND_HC);
    //                                                    $("#OWNER_TYPE_ID").val(obj[0].OWNER_TYPE_ID);
                                                    }, 1500);
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
                                                $(".loading-gifOwner").hide();
                                                $('#over').remove();
                                            }
                                        }
                                    });
                                    return false;
                                }
                            </script>
                        </div>
                        <div class="x_panel" id="idShowResultModalSearch" style="display: none;">
                            <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                <h2><i class="fa fa-list-ul"></i> <script>document.write(cert_title_table);</script></h2>
                                <ul class="nav navbar-right panel_toolbox">
                                    <li style="color: red;font-weight: bold;"></li>
                                </ul>
                                <div class="clearfix"></div>
                            </div>
                            <div class="table-responsive">
                                <table id="idTableList" class="table table-bordered table-striped projects">
                                    <thead>
                                        <th><script>document.write(global_fm_STT);</script></th>
                                        <th><script>document.write(global_fm_grid_company);</script></th>
                                        <th><script>document.write(global_fm_enterprise_id);</script></th>
                                        <th><script>document.write(global_fm_grid_personal);</script></th>
                                        <th><script>document.write(global_fm_personal_id);</script></th>
                                        <th><script>document.write(global_fm_email);</script></th>
                                        <th><script>document.write(global_fm_phone);</script></th>
                                        <th><script>document.write(global_fm_action);</script></th>
                                    </thead>
                                    <tbody id="idTBodyModalOwner">
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script>
            function ClearFieldModalSearch()
            {
                $("#idTBodyModalOwner").empty();
                $("#idShowResultModalSearch").css("display", "none");
                $('#TAXCODE_MODAL').val('');
                $('#DECISION_MODAL').val('');
                $('#BUDGET_CODE_MODAL').val('');
                $('#CMND_MODAL').val('');
                $('#CCCD_MODAL').val('');
                $('#PASSPORT_MODAL').val('');
                $('#COMPANY_NAME_MODAL').val('');
                $('#PERSONAL_NAME_MODAL').val('');
                $('#EMAIL_MODAL').val('');
                $('#PHONE_MODAL').val('');
                CloseDialog();
            }
            function ShowDialog()
            {
                $('#myModalOTPOwner').modal('show');
                $(".loading-gifOwner").hide();
                $(".loading-gif").hide();
                $('#over').remove();
                if(sessionStorage.getItem("sessHasRSSP") !== null && sessionStorage.getItem("sessHasRSSP") === "1"
                    && '<%= sRSSP_ACCESS_ENABLED%>' === '1')
                {
                    $("#idShowSearchOwnerCompany").css("display", "none");
                    $("#idShowSearchOwnerUser").css("display", "");
                    $("#idShowSearchOwnerCompany").css("display", "none");
                    $("#idShowSearchOwnerPersonal").css("display", "none");
                } else {
                    $("#idShowSearchOwnerCompany").css("display", "");
                    $("#idShowSearchOwnerUser").css("display", "none");
                    $("#idShowSearchOwnerCompany").css("display", "");
                    $("#idShowSearchOwnerPersonal").css("display", "");
                }
            }
            function CloseDialog()
            {
                $('#myModalOTPOwner').modal('hide');
                $(".loading-gifOwner").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
        </script>
        
        <script src="../style/bootstrap.min.js"></script>
        <script src="../style/custom.min.js"></script>
        <link href="../js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
        <script src="../js/checkphone/intlTelInput.js" type="text/javascript"></script>
        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/main.js"></script>
            <script src="../js/moment.min.js"></script>
        <script src="../js/daterangepicker.js"></script>
        <!-- Modal Registration Print -->
        <div id="myModalRegisterPrint" class="modal fade" role="dialog">
            <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                 left: 0; height: 100%;" class="loading-gifRegisterPrint">
                <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
            </div>
            <div class="modal-dialog modal-800" id="myDialogRegisterPrint">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div id="contentRegisterPrint"></div>
                    </div>
                </div>
            </div>
        </div>
        <script>
            function genCSRFPrint()
            {
                var vDNResult = "";
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'refreshCSRF'
                    },
                    cache: false,
                    async: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            vDNResult = myStrings[1];
                        }
                        else
                        {
                            vDNResult = "";
                        }
                    }
                });
                return vDNResult;
            }
            function PrintPreview(vText) {
                var popupWin = window.open('', '_blank', 'width=850,height=900,location=no,left=200px');
                popupWin.document.open();
                popupWin.document.write('<html><title></title><link rel="stylesheet" type="text/css" href="Print.css" media="screen"/></head><body onload="window.print()">');
                popupWin.document.write(vText);
                popupWin.document.write('</html>');
                popupWin.document.close();
            }
            function popupPrintRegister(id, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../PrintFormCommon",
                    data: {
                        idParam: 'printregisterredrect',
                        id: id,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('###');
                        if (myStrings[0] === "0")
                        {
                            $(".loading-gif").hide();
                            $('#over').remove();
                            PrintPreview(myStrings[1]);
                        }
                        else if (myStrings[0] === JS_EX_CSRF)
                        {
                            funCsrfAlert();
//                            funErrorAlert(CSRF_Mess);
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
            function ShowDialogPrint(id, isEnterprise, printRegCAOption, isCA)
            {
                if(isCA === JS_IS_WHICH_ABOUT_CA_ICA || isCA === JS_IS_WHICH_ABOUT_CA_MATBAO) {
                    popupPrintRegister(id, genCSRFPrint());
                } else {
                    $('#myModalRegisterPrint').modal('show');
                    $('#contentRegisterPrint').empty();
                    localStorage.setItem("sessCertToPrint", "1");
                    if(isEnterprise === "1")
                    {
                        localStorage.setItem("PrintRegisterPersonal", null);
                        localStorage.setItem("PrintRegisterBusiness", id);
                        if(printRegCAOption === '1') {
                            $('#contentRegisterPrint').load('PrintRegisterBusiness.jsp', {id:id}, function () {
                            });
                        } else if (printRegCAOption === '2') {
                            $('#contentRegisterPrint').load('PrintRegisterBusiness2.jsp', {id:id}, function () {
                            });
                        }
                    }
                    else {
                        localStorage.setItem("PrintRegisterPersonal", id);
                        localStorage.setItem("PrintRegisterBusiness", null);
                        if(printRegCAOption === '1') {
                            $('#contentRegisterPrint').load('PrintRegisterPersonal.jsp', {id:id}, function () {
                            });
                        } else if (printRegCAOption === '2') {
                            $('#contentRegisterPrint').load('PrintRegisterPersonal2.jsp', {id:id}, function () {
                            });
                        }
                    }
                    $(".loading-gifRegisterPrint").hide();
                    $(".loading-gif").hide();
                    $('#over').remove();
                }
            }
            function CloseDialogPrint()
            {
                $('#myModalRegisterPrint').modal('hide');
                $(".loading-gifRegisterPrint").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
        </script>
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
