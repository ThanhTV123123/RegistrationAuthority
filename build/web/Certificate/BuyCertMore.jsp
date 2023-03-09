<%-- 
    Document   : BuyCertMore
    Created on : Nov 24, 2019, 9:23:04 PM
    Author     : USER
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="vn.ra.process.SessionUploadFileCert"%>
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
    String sUpHighlighLabel = LoadParamSystem.getParamStart(Definitions.CONFIG_NEW_FILE_UPLOAD_HIGHLIGHTS_LABEL);
%>
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
            String sNewRefreshJS = conf.GetPropertybyCode(Definitions.CONFIG_JS_REFRESH_STRING_RANDOM);
        %>
        <script src="../js/Language.js?t=<%=sNewRefreshJS%>"></script>
        <script src="../js/process_javajs.js?t=<%=sNewRefreshJS%>"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js?t=<%=sNewRefreshJS%>"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            $(document).ready(function () {
                $('.loading-gif').hide();
                $('#registerIssuedDate').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
                localStorage.setItem("localStoreRequiredPersonal", null);
                localStorage.setItem("localStoreInputPersonal", null);
                localStorage.setItem("localStoreInputID_Info", null);
                $('#myModalRegisterPrint').modal({
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
                    closeForm();
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
            
            function ValidateForm(id, idCSRF, isCA)
            {
                var vSTR_COMPONENT_DN_VALUE_COMMONNAME = "";
                var vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = "";
                var vSTR_COMPONENT_DN_VALUE_DOMAIN_NAME = "";
                var vSTR_COMPONENT_DN_VALUE_MST = "";
                var vSTR_COMPONENT_DN_VALUE_QD = "";
                var vSTR_COMPONENT_DN_VALUE_MNS = "";
                var vSTR_COMPONENT_DN_VALUE_CMND = "";
                var vSTR_COMPONENT_DN_VALUE_CCCD = "";
                var vSTR_COMPONENT_DN_VALUE_DEVICE = "";
                var vSTR_COMPONENT_DN_VALUE_HC = "";
                var vSTR_COMPONENT_DN_VALUE_PROVINCE_ID = "";
                var vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC = "";
                var vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT = "";
                var vSTR_COMPONENT_DN_VALUE_EMAIL_SAN = "";
                var vSTR_COMPONENT_DN_ID_EMAIL_SAN = "";
                var vSTR_COMPONENT_DN_VALUE_PERSONAL_ID = "";
                var vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID = "";
                if (!JSCheckEmptyField($("#USER_BUYMORE").val()))
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
                var isRepresenseEnabled = '<%=sRepresentEnabled%>';
                if(isRepresenseEnabled === "1"){
                    if (!JSCheckEmptyField($("#registerFullname").val()))
                    {
                        $("#registerFullname").focus();
                        funErrorAlert(policy_req_empty + global_fm_fullname);
                        return false;
                    }
                    if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
//                        if (!JSCheckEmptyField($("#registerIssuedAdress").val()))
//                        {
//                            $("#registerIssuedAdress").focus();
//                            funErrorAlert(policy_req_empty + token_fm_address_residence);
//                            return false;
//                        }
                    } else {
                        if (!JSCheckEmptyField($("#registerAddressGPKD").val()))
                        {
                            $("#registerAddressGPKD").focus();
                            funErrorAlert(policy_req_empty + global_fm_address_GPKD);
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
                    if (!JSCheckEmptyField($("#registerIssuedDate").val()))
                    {
                        $("#registerIssuedDate").focus();
                        funErrorAlert(policy_req_empty + global_fm_cmnd_date);
                        return false;
                    }
                }
                var vDNResult = "";
                var vSTR_COMPONENT_SAN = "";
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
                                if(sSpace(sValueMST_MNS) !== "")
                                {
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
                                if(sSpace(sValueCMND_HC) !== "")
                                {
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
//                        goToByScroll("idViewSanComponent");
                    }
                }
                if(vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT !== "" && vSTR_COMPONENT_DN_VALUE_EMAIL_SAN === "" && vSTR_COMPONENT_DN_ID_EMAIL_SAN !== "")
                {
                    $("#"+vSTR_COMPONENT_DN_ID_EMAIL_SAN).focus();
                    funErrorAlert(global_req_email_subject_san);
                    return false;
//                    $("#"+vSTR_COMPONENT_DN_ID_EMAIL_SAN).val(vSTR_COMPONENT_DN_VALUE_EMAIL_SUBJECT);
                }
                if($("#PKI_FORMFACTOR").val() === JS_STR_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                    var sTempIS_MENU_LINK_SET_NO = $("#idSessIsChoise").val();
                    if(sTempIS_MENU_LINK_SET_NO === "0")
                    {
                        if (!JSCheckEmptyField($("#idCSR").val()))
                        {
                            $("#input-file-csr").focus();
                            funErrorAlert(global_req_file);
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
                    url: "../RequestCommon",
                    data: {
                        idParam: 'buycertificatemore',
                        id: id,
                        BRANCH_ID: $("#AGENT_NAME").val(),
                        CertProfileID: $("#idHiddenCerDurationOrProfileID").val(),
                        CACoreSubject: $("#idHiddenCerCoreSubject").val(),
                        pCERTIFICATION_PURPOSE: $("#CERTIFICATION_PURPOSE").val(),
                        pPKI_FORMFACTOR: $("#PKI_FORMFACTOR").val(),
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
                        pCCCD: vSTR_COMPONENT_DN_VALUE_CCCD,
                        pPASSPORT: vSTR_COMPONENT_DN_VALUE_HC,
                        pDEVICE: vSTR_COMPONENT_DN_VALUE_DEVICE,
                        PHONE_CONTRACT: $("#PHONE_CONTRACT").val(),
                        EMAIL_CONTRACT: $("#EMAIL_CONTRACT").val(),
                        CREATE_USER: $("#USER_BUYMORE").val(),
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
                        pENTERPRISE_ID: vSTR_COMPONENT_DN_VALUE_ENTERPRISE_ID,
                        pPERSONAL_ID: vSTR_COMPONENT_DN_VALUE_PERSONAL_ID,
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
                                    funSuccAlert(owner_succ_dispose, 'RenewCertList.jsp');
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
                                            window.location = 'RenewCertList.jsp';
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
                                    funSuccAlert(owner_succ_dispose, 'RenewCertList.jsp');
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
                                            window.location = 'RenewCertList.jsp';
                                        }
                                    });
                                }
                            }
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        } else if (arr[0] === JS_EX_LOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_login);
                        } else if (arr[0] === JS_EX_ERROR_CORECA_CALL) {
                            funErrorAlert(global_error_coreca_call_approve);
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
                        else if (arr[0] === JS_EX_CSR_KEYSIZE)
                        {
                            funErrorAlert(global_error_keysize_csr);
                        } else if (arr[0] === JS_EX_CSR_EXISTS) {
                            funErrorAlert(global_error_exist_csr);
                        }
                        else if (arr[0] === JS_EX_DNS_SSL_NULL) {
                            $("#idDNSName").focus();
                            funErrorAlert(policy_req_empty + global_fm_dns_name);
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
                $(".loading-gif").hide();
                $('#over').remove();
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
                        idSession: 'RefreshRenewCertSess'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "RenewCertList.jsp";
                        }
                        else
                        {
                            window.location = "RenewCertList.jsp";
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
    <body>
        <%
            if (session.getAttribute("sUserID") != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String loginUID = session.getAttribute("UserID").toString().trim();
                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                String SessRoleID_ID = session.getAttribute("RoleID_ID").toString().trim();
                String sessUserID = request.getSession(false).getAttribute("UserID").toString().trim();
                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Cert");
                CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessPolicyCert_Data");
                CERTIFICATION_POLICY_DATA[][] sessPolicyFormFactor_Data = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessPolicyFormFactor_Data");
                session.setAttribute("sessUploadFileCert", null);
                session.setAttribute("sessDNSNameForSSL", null);
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
            <%
                String sDN_Country = "";
                String sMaxLengthFile = cogCommon.GetPropertybyCode(Definitions.CONFIG_JACK_RABBIT_MAX_LENGTH_FILE).trim();
                try {
                    String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                    CERTIFICATION[][] rs = new CERTIFICATION[1][];
                    if (EscapeUtils.IsInteger(ids) == true) {
                        db.S_BO_CERTIFICATION_DETAIL(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                        if (rs[0].length > 0) {
                            boolean isAccessAgencyPage = true;
                            if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                BRANCH[][] branchAccess = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                                isAccessAgencyPage = CommonFunction.checkBranchTreeInvalidCert(rs[0][0].BRANCH_ID, branchAccess);
                            }
                            if (isAccessAgencyPage == true) {
                                int pCERTIFICATION_STATE_ID = rs[0][0].CERTIFICATION_STATE_ID;
                                String pPHONE_CONTRACT = EscapeUtils.CheckTextNull(rs[0][0].PHONE_CONTRACT);
                                String pEMAIL_CONTRACT = EscapeUtils.CheckTextNull(rs[0][0].EMAIL_CONTRACT);
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
                                String isViewFileOld = "0";
                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                    isViewFileOld = "1";
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
                <script>$("#idLblTitleEdits").text(buymorecert_title_view);</script>
                <ul class="nav navbar-right panel_toolbox">
                    <li>
                        <%
                            if(rs[0][0].PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                if(pCERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_DECLINED) {
                                    if(CommonFunction.CheckRoleFuncValidOrNot(Definitions.CONFIG_ROLE_PROPERTIES_CERT_BUY_MORE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                        %>
                        <input type="button" id="btnSave" class="btn btn-info" onclick="ValidateForm('<%=ids%>', '<%=anticsrf%>', '<%=isCALoad%>');" />
                        <script>document.getElementById("btnSave").value = global_fm_button_add;</script>
                        <%
                                    }
                                }
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
                    <input type="hidden" name="hdfOwnerID" id="hdfOwnerID" value="<%= rs[0][0].CERTIFICATION_OWNER_ID%>"/>
                    <fieldset class="scheduler-border" style="clear: both;">
                        <legend class="scheduler-border" id="idLblTitleRegisterOwner"></legend>
                        <script>$("#idLblTitleRegisterOwner").text(cert_title_register_owner);</script>
                        <div class="col-sm-13" style="padding-left: 0;">
                            <div class="col-sm-6" style="padding-left: 0;">
                                <div class="form-group">
                                    <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                        <label id="idLblTitlePhoneContact"></label>
                                        <label id="idLblNotePhoneContact" class="CssRequireField"></label>
                                    </label>
                                    <div class="col-sm-6" style="padding-right: 0px;">
                                        <input type="text" id="PHONE_CONTRACT" value="<%= pPHONE_CONTRACT%>" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_PHONE %>" class="form-control123"
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
                                        <input type="text" value="<%= pEMAIL_CONTRACT%>" id="EMAIL_CONTRACT" class="form-control123" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_EMAIL%>">
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleEmailContact").text(global_fm_email_contact);
                                    $("#idLblNoteEmailContact").text(global_fm_require_label);
                                </script>
                            </div>
                        </div>
                    </fieldset>

                    <fieldset class="scheduler-border" style="clear: both;">
                        <legend class="scheduler-border" id="idLblTitleRegisterCert"></legend>
                        <script>$("#idLblTitleRegisterCert").text(cert_title_register_cert);</script>
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
                                                        if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT))
                                                        {
                                                            if("".equals(sBranchFirst)) {
                                                                sBranchFirst = String.valueOf(temp1.ID);
                                                            }
                                        %>
                                        <option value="<%=String.valueOf(temp1.ID)%>"><%=temp1.NAME + " - " + temp1.REMARK%></option>
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
                                    <select name="USER_BUYMORE" id="USER_BUYMORE" class="form-control123">
                                        <%
                                            BACKOFFICE_USER[][] rssUser;
                                            if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                rssUser = new BACKOFFICE_USER[1][];
                                                db.S_BO_GET_USER_BRANCH_ALL(sBranchFirst, rssUser);
                                                if (rssUser[0].length > 0) {
                                                    for (int i = 0; i < rssUser[0].length; i++) {
                                        %>
                                        <option value="<%=String.valueOf(rssUser[0][i].ID)%>"><%=rssUser[0][i].FULL_NAME + " (" + rssUser[0][i].USERNAME + ")" %></option>
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
                                        <option value="<%=String.valueOf(rssUser[0][i].ID)%>"><%=rssUser[0][i].FULL_NAME + " (" + rssUser[0][i].USERNAME + ")" %></option>
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
                                            var cbxUSER = document.getElementById("USER_BUYMORE");
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
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCA" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <select name="CERTIFICATION_AUTHORITY" id="CERTIFICATION_AUTHORITY" class="form-control123"
                                        onchange="LOAD_CERTIFICATION_AUTHORITY(this.value, '<%= anticsrf%>');">
                                        <%
                                            String sFristCA = "";
                                            String sFristCodeCA = "";
                                            String sCACoreSubject = "";
                                            CERTIFICATION_AUTHORITY[][] rssProfile = new CERTIFICATION_AUTHORITY[1][];
                                            db.S_BO_CERTIFICATION_AUTHORITY_COMBOBOX(sessLanguageGlobal, rssProfile);
                                            if (rssProfile[0].length > 0) {
                                                int intCAIDDefault = CommonFunction.getCAIDDefault(strCADefault, rssProfile[0]);
                                                if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                    for (int i = 0; i < rssProfile[0].length; i++) {
//                                                        if(rssProfile[0][i].ID == intCAIDDefault) {
                                                        if("".equals(sFristCA)) {
                                                            sFristCA = String.valueOf(rssProfile[0][i].ID);
                                                        }
                                                        if("".equals(sFristCodeCA)) {
                                                            sFristCodeCA = EscapeUtils.CheckTextNull(rssProfile[0][i].NAME);
                                                        }
                                                        if("".equals(sCACoreSubject)) {
                                                            sCACoreSubject = EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT);
                                                        }
//                                                        }
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
                                            String sFristCerPurpose="";
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
                                            String sFirstPKI_FormFactor = "";
                                            if(!"".equals(sFristCerPurpose)) {
                                                PKI_FORMFACTOR[][] rsPKIFormFactor = new PKI_FORMFACTOR[1][];
                                                db.S_BO_CA_GET_PKI_FORMFACTOR_COMBOBOX_FOR_CERTIFICATION_PURPOSE(Integer.parseInt(sFristCerPurpose),
                                                    sessLanguageGlobal, rsPKIFormFactor);
                                                if (rsPKIFormFactor.length > 0) {
                                                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                                    {
                                                        for (int i = 0; i < rsPKIFormFactor[0].length; i++) {
                                                            if(rsPKIFormFactor[0][i].ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM) {
                                                                sFirstPKI_FormFactor = String.valueOf(rsPKIFormFactor[0][0].ID);
                                        %>
                                        <option value="<%= String.valueOf(rsPKIFormFactor[0][i].ID)%>"><%= rsPKIFormFactor[0][i].REMARK%></option>
                                        <%
                                                        }
                                                    }
                                                } else {
                                                    for (int i = 0; i < rsPKIFormFactor[0].length; i++) {
                                                        if(rsPKIFormFactor[0][i].ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM) {
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
                                            }
                                        %>
                                    </select>
                                </div>
                            </div>
                            <script>$("#idLblTitlePKIFormfactor").text(global_fm_Method);</script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCertDuration" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <select id="CERTIFICATION_DURATION" name="CERTIFICATION_DURATION" class="form-control123"
                                        onchange="LOAD_CERTIFICATION_DURATION(this.value, '<%= anticsrf%>');">
                                        <%
                                            String sFristCerDurationOrProfileID = "";
                                            if(!"".equals(sFristCA) && !"".equals(sFristCerPurpose) && !"".equals(sFirstPKI_FormFactor)) {
                                                CERTIFICATION_PROFILE[][] rsDuration = new CERTIFICATION_PROFILE[1][];
//                                                db.S_BO_CA_GET_DURATION_COMBOBOX(sFristCA, sFristCerPurpose, sFirstPKI_FormFactor, sessLanguageGlobal, rsDuration);
                                                db.S_BO_CA_GET_DURATION_COMBOBOX_BY_TYPE(sFristCA, sFristCerPurpose, sFirstPKI_FormFactor, Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE, sessLanguageGlobal, rsDuration);
                                                if (rsDuration[0].length > 0) {
                                                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                                    {
                                                        for (int i = 0; i < rsDuration[0].length; i++) {
                                                            sFristCerDurationOrProfileID = String.valueOf(rsDuration[0][0].ID);
                                        %>
                                        <option value="<%= String.valueOf(rsDuration[0][i].ID)%>"><%= rsDuration[0][i].REMARK %></option>
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
                                        %>
                                        <option value="<%= String.valueOf(rsDuration[0][i].ID)%>"><%= rsDuration[0][i].REMARK %></option>
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
                                        %>
                                        <option value="<%= String.valueOf(rsDuration[0][i].ID)%>"><%= rsDuration[0][i].REMARK %></option>
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
                            <script>$("#idLblTitleCertDuration").text(global_fm_duration_cts);</script>
                        </div>
                        <input id="idHiddenCerCA" value="<%= sFristCA%>" style="display: none;"/>
                        <input id="idHiddenCerCoreSubject" value="<%= sCACoreSubject%>" style="display: none;"/>
                        <input id="idHiddenCerPurpose" value="<%= sFristCerPurpose%>" style="display: none;"/>
                        <input id="idHiddenCerFactor" value="<%= sFirstPKI_FormFactor%>" style="display: none;"/>
                        <input id="idHiddenCerDurationOrProfileID" value="<%= sFristCerDurationOrProfileID%>" style="display: none;"/>
                        <script>
                            $(document).ready(function () {
                                if('<%= sFristCA%>' !== "" && '<%= sFristCerPurpose%>' !== ""
                                    && '<%= sFristCerDurationOrProfileID%>' !== "" && '<%= sFirstPKI_FormFactor%>' !== "")
                                {
                                    LOAD_CERTIFICATION_PROFILE('<%= sFristCerDurationOrProfileID%>');
                                    //dialogChooseCertOld($("#hdfOwnerID").val(), '');
                                    dialogChooseCertOld('<%=ids%>', '');
                                }
                            });
                        </script>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleFeeAmount" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" name="FEE_AMOUNT" disabled id="FEE_AMOUNT" class="form-control123">
                                </div>
                            </div>
                            <script>$("#idLblTitleFeeAmount").text(global_fm_amount_fee);</script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;" id="idViewDURATION_FREE">
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
                            });
                            function LOAD_PKI_FORMFACTOR(vValue)
                            {
                                if(vValue === JS_STR_PKI_FORMFACTOR_ID_HARD_TOKEN
                                    || vValue === JS_STR_PKI_FORMFACTOR_ID_HARD_TOKEN_SAFENET
                                    || vValue === JS_STR_PKI_FORMFACTOR_ID_HARD_TOKEN_SMARTCARD
                                    || vValue === JS_STR_PKI_FORMFACTOR_ID_HARD_TOKEN_EPASS3003)
                                {
                                    $("#idDivFfactorHardToken").css("display","");
                                    $("#idDivFfactorSoftToken").css("display","none");
                                } else if(vValue === JS_STR_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                {
                                    $("#idDivFfactorHardToken").css("display","none");
                                    $("#idDivFfactorSoftToken").css("display","");
                                } else{
                                    $("#idDivFfactorHardToken").css("display","none");
                                    $("#idDivFfactorSoftToken").css("display","none");
                                }
                                if('<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_ICA || ('<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_NC && '<%=SessAgentID%>' !== JS_STR_AGENCY_ROOT_CA)){
                                    $("#idDivFfactorHardToken").css("display","none");
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
                            <div class="col-sm-13" style="padding-left: 0;clear: both;">
                                <%
                                    String sP12CreateOnlyCA = LoadParamSystem.getParamStart(Definitions.CONFIG_CREATE_P12_USER_AGENCY_ENABLED);
                                %>
                                <div class="form-group">
                                    <label class="radio-inline" id="idRadioNameCheck1">
                                        <input type="radio" name="nameCheck" id="nameCheck1" checked>
                                        <span id="idLblTitleChooseGenKeyServer"></span>
                                        <script>$("#idLblTitleChooseGenKeyServer").text(global_fm_choose_genkey_server);</script>
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="nameCheck" id="nameCheck2">
                                        <span id="idLblTitleChooseGenKeyClient"></span>
                                        <script>$("#idLblTitleChooseGenKeyClient").text(global_fm_choose_genkey_client);</script>
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
                                        funErrorAlert(global_req_file);
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
                                    FILE_MANAGER[][] rsFileMana = new FILE_MANAGER[1][];
                                    db.S_BO_FILE_MANAGER_GET_BY_CERTIFICATION_AND_OWNER(ids, String.valueOf(rs[0][0].CERTIFICATION_OWNER_ID), sessLanguageGlobal, rsFileMana);
                                    SessionUploadFileCert cartIP = (SessionUploadFileCert) session.getAttribute("sessUploadFileCert");
                                    cartIP = new SessionUploadFileCert();
                                    if(rsFileMana[0].length > 0) {
                                        if("1".equals(isViewFileOld)) {
                                            for(FILE_MANAGER rsFile : rsFileMana[0]) {
                                                if(rsFile.FILE_PROFILE_NAME.equals(Definitions.CONFIG_FILE_PROFILE_PHOTO_ACTIVITY_DECLARATION)
                                                    || rsFile.FILE_PROFILE_NAME.equals(Definitions.CONFIG_FILE_PROFILE_PHOTO_ID_CARD)) {
                                                    FILE_PROFILE_DATA item = new FILE_PROFILE_DATA();
                                                    item.FILE_MANAGER_ID = rsFile.ID;
                                                    item.FILE_NAME = rsFile.FILE_NAME;
                                                    item.FILE_PROFILE = rsFile.FILE_PROFILE_NAME;
                                                    item.FILE_SIZE = (double) rsFile.FILE_SIZE;
                                                    item.FILE_MIMETYPE = rsFile.MIME_TYPE_NAME;
                                                    item.FILE_STREAM = null;
                                                    cartIP.AddRoleFunctionsList(item);
                                                    System.out.println("rsFile.FILE_PROFILE_NAME: " + rsFile.FILE_PROFILE_NAME);
                                                }
                                            }
                                        }
                                        session.setAttribute("sessUploadFileCert", cartIP);
                                    }
                                    
                                    String sJSON = "";
                                    CERTIFICATION_PURPOSE[][] rsPURPOSE = new CERTIFICATION_PURPOSE[1][];
                                    db.S_BO_CERTIFICATION_PURPOSE_DETAIL_BY_CERTIFICATION_ATTR_TYPE(sFristCerPurpose, Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_BUY_MORE, rsPURPOSE);
                                    if(rsPURPOSE[0].length > 0){
                                        sJSON = EscapeUtils.CheckTextNull(rsPURPOSE[0][0].FILE_PROPERTIES);
                                    }
                                    if(!"".equals(sJSON)) {
                                        ObjectMapper oMapperParse = new ObjectMapper();
                                        FILE_PROFILE_JSON itemParsePush = oMapperParse.readValue(sJSON, FILE_PROFILE_JSON.class);
                                        int jFile = 1;
                                        boolean isEnableUploadFile = true;
                                        for (FILE_PROFILE_JSON.Attribute attribute : itemParsePush.getAttributes()) {
                                            String sRemark = attribute.getRemark().trim();
                                            String sName = attribute.getName().trim();
                                            if(!sName.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT) && !sName.equals(Definitions.CONFIG_FILE_PROFILE_CODE_AGREEMENT_PAPER)) {
                                                String sNameInID = attribute.getName().trim() + String.valueOf(jFile);
                                                if(attribute.getEnabled() == true) {
                            %>
                            <fieldset class="scheduler-border">
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
                                        <label id="idLblTitleUploadManager" class="control-label <%=sClassFileLabel%>" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                        <div class="<%=sClassFileUp%>" style="padding-right: 0px;">
                                            <input type="file" id="input-file<%=sName%>" style="width: 100%;"
                                                onchange="calUploadFile(this, '<%= sName%>');" class="btn btn-default btn-file select_file" multiple>
                                        </div>
                                    </div>
                                    <div class="form-group" style="display: <%="1".equals(sRepresentEnabled) ? "none" : ""%>">
                                        <label class="control-label123" style="color:red;font-weight: 200;" id="idLblTitleNoteManager"></label>
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
                                <%
                                    }
                                %>
                                <div style="padding: 10px 0 10px 0;" id="idDiv<%= sName%>" class="table-responsive">
                                     <table id="idTable<%= sName%>" class="table table-striped projects">
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
                                                <td><%=mhIP.FILE_MANAGER_ID!=0 ? "" : sUpHighlighLabel%><%= EscapeUtils.CheckTextNull(mhIP.FILE_NAME)%></td>
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
                                                            if(mhIP.FILE_MANAGER_ID == 0){
                                                    %>
                                                    <a id="idLblTitleTableLinkDelete<%= sNameInID_inner%>" style="cursor: pointer;<%=cssDelete%>;<%=cssDelete%>;<%="0".equals(isViewActionFileEnable) && mhIP.FILE_MANAGER_ID != 0 ? "display: none;" : "" %>" class="btn btn-info btn-xs" onclick="DeleteTempFile('<%= mhIP.FILE_PROFILE%>', '<%= mhIP.FILE_NAME%>', '<%= mhIP.FILE_MANAGER_ID %>');">
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
                                                    var cssReview = '';
                                                    var cssDelete = '';
                                                    var viewActionFileEnable = '<%=isViewActionFileEnable%>';
                                                    var vUpHighlighLabel = "<%=sUpHighlighLabel%>";
                                                    var sActionFileEnable = "";
                                                    if(viewActionFileEnable === "0"){
                                                        sActionFileEnable = "display: none";
                                                    }
                                                    var representEnabled = '<%=sRepresentEnabled%>';
                                                    if(representEnabled === "1"){
                                                        iconDelete = ' ' + global_fm_button_delete;
                                                        cssDelete = 'text-align: center;width: 60px;';
                                                        iconReview = ' ' + global_fm_view;
                                                        cssReview = 'text-align: center;width: 60px;';
                                                    }
                                                    input1.value = '';
                                                    $("#idTBody" + idType).empty();
                                                    var content = "";
                                                    for (var i = 0; i < obj.length; i++) {
                                                        if(obj[i].FILE_PROFILE === idType)
                                                        {
                                                            /*var fileNameLoad = obj[i].FILE_NAME;
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
                                                                "</tr>";*/
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
                                                            var fileNameExt = fileNameLoad.substring(fileNameLoad.lastIndexOf('.')+1);
                                                            if(fileNameExt.toUpperCase() === "PDF" || fileNameExt.toUpperCase() === "GIF"  || fileNameExt.toUpperCase() === "JPEG"
                                                                        || fileNameExt.toUpperCase() === "JPG" || fileNameExt.toUpperCase() === "PNG"){
                                                                sReviewCRL = '<a style="cursor: pointer;'+cssReview+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                    btn-xs" onclick="ViewTempTwoParamFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + fileNameLoad + '\');">' + iconReview + '</a>';
                                                            }
                                                            var sActionDown = '<a style="cursor: pointer;'+sActionFileEnable+'" class="btn btn-info\n\
                                                                btn-xs" onclick="DownloadTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + <%= anticsrf%> + '\');">\n\
                                                                <i class="fa fa-pencil"></i> ' + global_fm_down + '</a>';
                                                            if(obj[i].FILE_MANAGER_ID !== "0")
                                                            {
                                                                content += "<tr>" +
                                                                    "<td>" + obj[i].Index + "</td>" +
                                                                    "<td>" + fileNameLoad + "</td>" +
                                                                    "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                    "<td>" + sActionDown + ' ' + sReviewCRL +"</td>" +
                                                                    "</tr>";
                                                            } else {
                                                                content += "<tr>" +
                                                                    "<td>" + obj[i].Index + "</td>" +
                                                                    "<td>" + vUpHighlighLabel + fileNameLoad + "</td>" +
                                                                    "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                    "<td>" + sActionCRL + ' ' + sReviewCRL +"</td>" +
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
                                                                /*var fileNameLoad = obj[i].FILE_NAME;
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
                                                                    "</tr>";*/
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
                                                                var fileNameExt = fileNameLoad.substring(fileNameLoad.lastIndexOf('.')+1);
                                                                if(fileNameExt.toUpperCase() === "PDF" || fileNameExt.toUpperCase() === "GIF"  || fileNameExt.toUpperCase() === "JPEG"
                                                                            || fileNameExt.toUpperCase() === "JPG" || fileNameExt.toUpperCase() === "PNG"){
                                                                    sReviewCRL = '<a style="cursor: pointer;'+cssReview+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                        btn-xs" onclick="ViewTempTwoParamFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + fileNameLoad + '\');">' + iconReview + '</a>';
                                                                }
                                                                var sActionDown = '<a style="cursor: pointer;'+cssDelete+';'+sActionFileEnable+'" class="btn btn-info\n\
                                                                    btn-xs" onclick="DownloadTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + <%= anticsrf%> + '\');">' + iconDown + '</a>';
                                                                if(obj[i].FILE_MANAGER_ID !== "0")
                                                                {
                                                                    content += "<tr>" +
                                                                        "<td>" + obj[i].Index + "</td>" +
                                                                        "<td>" + obj[i].FILE_NAME + "</td>" +
                                                                        "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                        "<td>" + sActionDown + ' ' + sReviewCRL +"</td>" +
                                                                        "</tr>";
                                                                } else {
                                                                    content += "<tr>" +
                                                                        "<td>" + obj[i].Index + "</td>" +
                                                                        "<td>" + vUpHighlighLabel + obj[i].FILE_NAME + "</td>" +
                                                                        "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                        "<td>" + sActionCRL + ' ' + sReviewCRL +"</td>" +
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
                                                                '<div style="height:5px;'+displayShowNote+'""></div><label class="control-label123" style="color:red;font-weight: 200;'+displayShowNote+'"">' + global_fm_browse_cert_note + '<%= Integer.parseInt(sMaxLengthFile) / 1024 %>' + ' MB. ' + global_fm_fileattach_support + '<%= sArrayFileExten.replace(";", ",") %>' + '</label>'+
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
                        <div class="col-sm-13" style="padding-left: 0;clear: both;">
                            <div class="form-group">
                                <fieldset class="scheduler-border">
                                    <legend class="scheduler-border" id="idLblTitleComponentDN"></legend>
                                    <div id="idViewCertInfo"></div>
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
                                    <div class="col-sm-6" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleFullname"></label>
                                                <label id="idLblNoteFullname" class="CssRequireField"></label>
                                            </label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input class="form-control123" maxlength="256" id="registerFullname" name="registerFullname"/>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleFullname").text(global_fm_fullname);
                                            $("#idLblNoteFullname").text(global_fm_require_label);
                                        </script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;" id="idViewDivRegisterRole">
                                        <div class="form-group">
                                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleRegisterRole"></label>
                                                <label id="idLblNoteRegisterRole" class="CssRequireField"></label>
                                            </label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input class="form-control123" maxlength="256" id="registerRole" name="registerRole"/>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleRegisterRole").text(global_fm_role);
                                            $("#idLblNoteRegisterRole").text(global_fm_require_label);
                                        </script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleRegisterCMND"></label>
                                                <label id="idLblNoteRegisterCMND" class="CssRequireField"></label>
                                            </label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input class="form-control123" maxlength="256" id="registerCMND" name="registerCMND"/>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleRegisterCMND").text(global_fm_CitizenId_I + "/"+global_fm_CMND + "/"+global_fm_HC);
                                            $("#idLblNoteRegisterCMND").text(global_fm_require_label);
                                        </script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleRegisterIssuedDate"></label>
                                                <label id="idLblNoteRegisterIssuedDate" class="CssRequireField"></label>
                                            </label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input class="form-control123" maxlength="25" id="registerIssuedDate" name="registerIssuedDate"/>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleRegisterIssuedDate").text(global_fm_cmnd_date);
                                            $("#idLblNoteRegisterIssuedDate").text(global_fm_require_label);
                                        </script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleRegisterIssuedPlace"></label>
                                                <!--<label id="idLblNoteRegisterIssuedPlace" class="CssRequireField"></label>-->
                                            </label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input class="form-control123" maxlength="256" id="registerIssuedPlace" name="registerIssuedPlace"/>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleRegisterIssuedPlace").text(global_fm_place);
//                                            $("#idLblNoteRegisterIssuedPlace").text(global_fm_require_label);
                                        </script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleRegisterIssuedAdress"></label>
                                                <!--<label id="idLblNoteRegisterIssuedAdress" class="CssRequireField"></label>-->
                                            </label>
                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                <input class="form-control123" maxlength="256" id="registerIssuedAdress" name="registerIssuedAdress"/>
                                            </div>
                                        </div>
                                        <script>
                                            $(document).ready(function () {
                                                $("#idLblTitleRegisterIssuedAdress").text(token_fm_address_residence);
//                                                var isRepresenseEnabled = '<=sRepresentEnabled%>';
//                                                if(isRepresenseEnabled === "1"){
//                                                    $("#idLblNoteRegisterIssuedAdress").text(global_fm_require_label);
//                                                }
                                            });
                                        </script>
                                    </div>
                                </fieldset>
                            </div>
                        </div>
                        <!-- REPRESENTE SAN -->
                        
                        <div class="col-sm-13" style="padding-left: 0;clear: both;display: none;" id="idViewDNSInfo">
                            <div class="form-group">
                                <fieldset class="scheduler-border">
                                    <legend class="scheduler-border" id="idLblTitleComponentDNSList"></legend>
                                    <script>$("#idLblTitleComponentDNSList").text(global_fm_dns_list);</script>
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
                                            cbxTemplateCity.options[cbxTemplateCity.options.length] = new Option("", "");
                                            for (var i = 0; i < obj.length; i++) {
                                                cbxTemplateCity.options[cbxTemplateCity.options.length] = new Option(obj[i].cityprovincedesc, obj[i].cityprovinceId);
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
                                            var localStoreRequired = new Array();
                                            var localStoreInput = new Array();
                                            var localStoreSanInput = new Array();
                                            var localStoreInputID_OnInput = "";
                                            var localStoreInputID_Info = "";
                                            var localStoreUID_Info = "";
                                            var localStoreComponentForOwner = "";
                                            var vPersonalCNBlur = "";
                                            var booHasUIDCompany = false;
                                            var isRepresenseEnabled = '<%=sRepresentEnabled%>';
                                            var caLoadEnabled = '<%=isCALoad%>';
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
                                                if(isRepresenseEnabled === "1"){
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
                                                        '<label style="color: red;" id="idHintMSTMNS"></label></div></div>';
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
                                                    vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div><div class='col-sm-9' style='padding-right: 0px;'><input class='form-control123' oninput='GetAlrmCertCMNDHC(this.value);' type='text' id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID + "' />";
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
                                                        console.log("IsRequired: " + obj[i].IsRequired);
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
                                                        localStoreComponentForOwner = localStoreComponentForOwner + obj[i].SubjectDNAttrCode + "@" + vInputID + "###";
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
                                                    if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_LOCALITY) {
                                                        localStoreComponentForOwner = localStoreComponentForOwner + obj[i].SubjectDNAttrCode + "@" + vInputID + "###";
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
                                            if(isRepresenseEnabled === "1"){
                                                vContent += '<div class="col-sm-6" style="padding-left: 0px;" id="idViewDivRegisterGPKD">' +
                                                    '<div class="form-group">' +
                                                    '<label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + global_fm_address_GPKD + ' <label class="CssRequireField">' + global_fm_require_label + '</label></label> ' +
                                                    '<div class="col-sm-7" style="padding-right: 0px;">'+
                                                    '<input class="form-control123" type="text" id="registerAddressGPKD" name="registerAddressGPKD" />' +
                                                    '</div>' +
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
                                            } else {
                                                $("#idViewSanComponent").css("display", "none");
                                                $("#idViewSanInfo").css("display", "none");
                                            }
                                            if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
                                                $("#idLblTitleRepresent").text(cert_title_register_owner);
                                                $("#idViewDivRegisterGPKD").css("display", "none");
                                                $("#idViewDivRegisterRole").css("display", "none");
                                                document.getElementById("registerFullname").disabled = true;
                                                document.getElementById("registerCMND").disabled = true;
                                                document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).oninput = function() { OnBlurPersonalID("registerCMND", this.value);};
                                                if(vPersonalCNBlur !== ""){
                                                    document.getElementById(vPersonalCNBlur).oninput = function() { OnBlurPersonalName("registerFullname", this.value);};
                                                }
                                            } else {
                                                $("#idLblTitleRepresent").text(global_fm_representative_legal);
                                                if(isRepresenseEnabled === "1"){
                                                    $("#idViewDivRegisterGPKD").css("display", "");
                                                }
                                                $("#idViewDivRegisterRole").css("display", "");
                                                document.getElementById("registerFullname").disabled = false;
                                                document.getElementById("registerCMND").disabled = false;
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
                                                        document.getElementById(idCNTemp).oninput = function() { OnBlurCompany(idOTemp, this.value);};
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
                                            if($("#hdfOwnerID").val() !== "")
                                            {
//                                                dialogChooseCertOld($("#hdfOwnerID").val(), $("#CERTIFICATION_PURPOSE").val());
                                                dialogChooseCertOld('<%=ids%>', $("#CERTIFICATION_PURPOSE").val());
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
                        function OnBlurPersonalID(idReceived, objValue) {
                            $("#"+idReceived).val(objValue);
                        }
                        function OnBlurPersonalName(idReceived, objValue) {
                            $("#"+idReceived).val(objValue);
                        }
                        function GetAlrmCertMSTMNS()
                        {
                            var IsMST = "1";
                            var IsCall = "0";
                            $("#idViewHintMSTMNS").css("display", "none");
                            $("#idHintMSTMNS").css("display", "none");
                            $("#idHintMSTMNS").text('');
                            var vMST = $("#"+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val();
                            var sSelectedMST_MNS = $("#"+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).val();
                            if(sSelectedMST_MNS !== "") {
                                if(vMST.length > 6) {
                                    IsCall = "1";
                                    vMST = sSelectedMST_MNS + vMST;
                                }
                            }
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
                                                        $("#" +sListInputCheckID_Info[i].split('###')[1]).val(obj[0].PROVINCE);
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
                        function OnChangeRadioMST()
                        {
                            var sSelectedMST_MNS = $("input[name='"+JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS+"']:checked").val();
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
                        function OnChangeComboboxMST()
                        {
                            var sSelectedMST_MNS = $("#"+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).val();
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
                                                for (var i = 0; i < obj.length; i++) {
                                                    cbxCERTIFICATION_PURPOSE.options[cbxCERTIFICATION_PURPOSE.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                                }
                                                $("#idHiddenCerPurpose").val(obj[0].ID);
                                                LOAD_CERTIFICATION_PURPOSE(objCA.split('###')[0], obj[0].ID, idCSRF);
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
                            //LoadFileManage(objPurpose);
                            $.ajax({
                                type: "post",
                                url: "../JSONCommon",
                                data: {
                                    idParam: 'loadpki_formfactorby_purpose',
                                    idCA: objCA,
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
                                            for (var i = 0; i < obj.length; i++) {
                                                if(vFristPKIFormFactor === "") {
                                                    vFristPKIFormFactor = sSpace(obj[i].ID);
                                                }
                                                cbxPKI_FORMFACTOR.options[cbxPKI_FORMFACTOR.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                            }
                                            $("#idHiddenCerFactor").val(vFristPKIFormFactor);
                                            LOAD_PROFILE_BY_FORMFACTOR(objCA, objPurpose, vFristPKIFormFactor, idCSRF);
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
                                    idAttrType: JS_STR_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE,
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
                                            LOAD_PKI_FORMFACTOR(objFactor);
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
                                            for (var i = 0; i < obj.length; i++) {
                                                if(vFristPKIFormFactor === "")
                                                {
                                                    vFristPKIFormFactor = sSpace(obj[i].ID);
                                                }
                                                cbxPKI_FORMFACTOR.options[cbxPKI_FORMFACTOR.options.length] = new Option(obj[i].REMARK, obj[i].ID);
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
                        function dialogChooseCertOld(idOwner, idPurpose)
                        {
                            $.ajax({
                                type: "post",
                                url: "../JSONCommon",
                                data: {
                                    idParam: 'choosecertoldforcert',
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
                                            var vOWNER_TYPE_ID = obj[0].OWNER_TYPE_ID;
                                            var vENTERPRISE_ID = obj[0].ENTERPRISE_ID;
                                            var vPERSONAL_ID = obj[0].PERSONAL_ID;
                                            var vPHONE_CONTRACT = obj[0].PHONE_NUMBER;
                                            var vEMAIL = obj[0].EMAIL;
                                            var vLOCATION = obj[0].LOCATION;
                                            var vPROVINCE_ID = obj[0].PROVINCE_ID;
                                            var vCOMPANY_NAME = obj[0].COMPANY_NAME;
                                            var vPERSONAL_NAME = obj[0].PERSONAL_NAME;
                                            if(idPurpose === '') {
                                                console.log("vOWNER_TYPE_ID: " + vOWNER_TYPE_ID);
                                                if(vOWNER_TYPE_ID === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE)
                                                {
                                                    $("#CERTIFICATION_PURPOSE").val(JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE);
                                                    LOAD_CERTIFICATION_PURPOSE($("#CERTIFICATION_AUTHORITY").val().split('###')[0], JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE, '<%= anticsrf%>');
                                                } else if(vOWNER_TYPE_ID === JS_STR_CERTIFICATION_PURPOSE_ID_STAFF)
                                                {
                                                    $("#CERTIFICATION_PURPOSE").val(JS_STR_CERTIFICATION_PURPOSE_ID_STAFF);
                                                    LOAD_CERTIFICATION_PURPOSE($("#CERTIFICATION_AUTHORITY").val().split('###')[0], JS_STR_CERTIFICATION_PURPOSE_ID_STAFF, '<%= anticsrf%>');
                                                } else if(vOWNER_TYPE_ID === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL)
                                                {
                                                    $("#CERTIFICATION_PURPOSE").val(JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL);
                                                    LOAD_CERTIFICATION_PURPOSE($("#CERTIFICATION_AUTHORITY").val().split('###')[0], JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL, '<%= anticsrf%>');
                                                } else if(vOWNER_TYPE_ID === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE_GOV)
                                                {
                                                    $("#CERTIFICATION_PURPOSE").val(JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE_GOV);
                                                    LOAD_CERTIFICATION_PURPOSE($("#CERTIFICATION_AUTHORITY").val().split('###')[0], JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE_GOV, '<%= anticsrf%>');
                                                } else if(vOWNER_TYPE_ID === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL_GOV)
                                                {
                                                    $("#CERTIFICATION_PURPOSE").val(JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL_GOV);
                                                    LOAD_CERTIFICATION_PURPOSE($("#CERTIFICATION_AUTHORITY").val().split('###')[0], JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL_GOV, '<%= anticsrf%>');
                                                }
                                            }
                                            $("#PHONE_CONTRACT").val(vPHONE_CONTRACT);
                                            $("#EMAIL_CONTRACT").val(vEMAIL);
                                            if('<%=sRepresentEnabled%>' === '1'){
                                                if($("#CERTIFICATION_PURPOSE").val() !== JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
                                                    $("#registerFullname").val(obj[0].REPRESENTATIVE_NAME);
                                                    $("#registerRole").val(obj[0].POSITION);
                                                    $("#registerAddressGPKD").val(obj[0].ADDRESS_LICENSE);
                                                }
                                                $("#registerIssuedAdress").val(obj[0].ADDRESS);
                                                $("#registerCMND").val(obj[0].PID);
                                                $("#registerIssuedDate").val(obj[0].PID_DATE);
                                                $("#registerIssuedPlace").val(obj[0].PID_ISSUEDBY);
                                            }
                                            setTimeout(function() {
                                                if(sSpace(localStorage.getItem("localStoreComponentForOwner")) !== "")
                                                {
                                                    var sListComponentForOwner = localStorage.getItem("localStoreComponentForOwner").split('###');
                                                    for (var i = 0; i < sListComponentForOwner.length; i++) {
                                                        if(checkUIDVNEnterprise(sSpace(sListComponentForOwner[i].split('@')[0]), vOWNER_TYPE_ID)) {
                                                            var prefixInput = convertPrefixEnterpriseToVN(vENTERPRISE_ID);
                                                            if(prefixInput !== ''){
                                                                document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).value=prefixInput;
                                                            }
                                                            $("#"+ JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val(vENTERPRISE_ID.split(":")[1]);
                                                            if($("#CERTIFICATION_PURPOSE").val() !== JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL_GOV
                                                                && $("#CERTIFICATION_PURPOSE").val() !== JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE_GOV)
                                                            {
                                                            }
                                                        } else if(checkUIDVNPersonal(sSpace(sListComponentForOwner[i].split('@')[0]))) {
                                                            var prefixInput = convertPrefixPersonalToVN(vPERSONAL_ID);
                                                            if(prefixInput !== '') {
                                                                document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).value=prefixInput;
                                                            }
                                                            $("#"+ JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val(vPERSONAL_ID.split(":")[1]);
                                                            if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
                                                                $("#registerCMND").val(vPERSONAL_ID.split(":")[1]);
                                                            }
                                                            if($("#CERTIFICATION_PURPOSE").val() !== JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL_GOV
                                                                && $("#CERTIFICATION_PURPOSE").val() !== JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE_GOV)
                                                            {
                                                            }
                                                        }
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
                                                                $("#registerFullname").val(vPERSONAL_NAME);
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
                                                        if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_LOCALITY)
                                                        {
                                                            var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                            $("#"+ids).val(vLOCATION);
                                                        }
                                                        if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE)
                                                        {
                                                            var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                            $("#"+ids).val(vPROVINCE_ID);
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
                        function dialogChooseOwner(idOwner, idPurpose)
                        {
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
                                            var vOWNER_TYPE_ID = obj[0].OWNER_TYPE_ID;
                                            var vENTERPRISE_ID = obj[0].ENTERPRISE_ID;
                                            var vPERSONAL_ID = obj[0].PERSONAL_ID;
                                            var vPHONE_CONTRACT = obj[0].PHONE_NUMBER;
                                            var vEMAIL = obj[0].EMAIL;
                                            var vCOMPANY_NAME = obj[0].COMPANY_NAME;
                                            var vPERSONAL_NAME = obj[0].PERSONAL_NAME;
                                            if(idPurpose === '')
                                            {
                                                console.log("vOWNER_TYPE_ID: " + vOWNER_TYPE_ID);
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
                                            //$("#idShowOwnerSearch").css("display", "");
                                            $("#PHONE_CONTRACT").val(vPHONE_CONTRACT);
                                            $("#EMAIL_CONTRACT").val(vEMAIL);
                                            //ClearFieldModalSearch();
                                            setTimeout(function() {
//                                                console.log("localStoreComponentForOwner: " + sSpace(localStorage.getItem("localStoreComponentForOwner")));
                                                if(sSpace(localStorage.getItem("localStoreComponentForOwner")) !== "")
                                                {
//                                                    console.log(localStorage.getItem("localStoreComponentForOwner"));
                                                    var sListComponentForOwner = localStorage.getItem("localStoreComponentForOwner").split('###');
                                                    for (var i = 0; i < sListComponentForOwner.length; i++) {
//                                                        if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST
//                                                            || sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS
//                                                            || sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_QD)
                                                        if(checkUIDVNEnterprise(sSpace(sListComponentForOwner[i].split('@')[0]), vOWNER_TYPE_ID)) {
                                                            //var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                            var prefixInput = convertPrefixEnterpriseToVN(vENTERPRISE_ID);
                                                            if(prefixInput !== ''){
                                                                document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).value=prefixInput;
                                                            }
//                                                            $("#"+ids).val(vENTERPRISE_ID.split(":")[1]);
                                                            $("#"+ JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID).val(vENTERPRISE_ID.split(":")[1]);
                                                            
//                                                            if(vMST !== "") {
//                                                                document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).value=JS_STR_COMPONENT_DN_VALUE_PREFIX_MST;
//                                                                $("#"+ids).val(vMST);
//                                                            } else if(vMSN !== "") {
//                                                                document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).value=JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS;
//                                                                $("#"+ids).val(vMSN);
//                                                            } else if(vDECISION !== "") {
//                                                                document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).value=JS_STR_COMPONENT_DN_VALUE_PREFIX_QD;
//                                                                $("#"+ids).val(vDECISION);
//                                                            }
                                                            if($("#CERTIFICATION_PURPOSE").val() !== JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL_GOV
                                                                && $("#CERTIFICATION_PURPOSE").val() !== JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE_GOV)
                                                            {
                                                                //document.getElementById(ids).disabled=true;
                                                                //document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID).disabled=true;
                                                            }
                                                        } else if(checkUIDVNPersonal(sSpace(sListComponentForOwner[i].split('@')[0]))) {
//                                                        if(sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND
//                                                            || sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC
//                                                            || sSpace(sListComponentForOwner[i].split('@')[0]) === JS_STR_COMPONENT_DN_VALUE_PREFIX_CCCD)
//                                                            console.log(sListComponentForOwner[i].split('@')[0]);
                                                           // var ids = sSpace(sListComponentForOwner[i].split('@')[1]);
                                                            var prefixInput = convertPrefixPersonalToVN(vPERSONAL_ID);
                                                            if(prefixInput !== ''){
                                                                document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).value=prefixInput;
                                                            }
//                                                            $("#"+ids).val(vPERSONAL_ID.split(":")[1]);
                                                            $("#"+ JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID).val(vPERSONAL_ID.split(":")[1]);
                                                            if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
                                                                $("#registerCMND").val(vPERSONAL_ID.split(":")[1]);
                                                            }
                                                                
//                                                            if(vCMND !== "") {
//                                                                document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).value=JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND;
//                                                                $("#"+ids).val(vCMND);
//                                                                if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
//                                                                    $("#registerCMND").val(vCMND);
//                                                                }
//                                                            } else if(vCCCD !== "") {
//                                                                document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).value=JS_STR_COMPONENT_DN_VALUE_PREFIX_CCCD;
//                                                                $("#"+ids).val(vCCCD);
//                                                                if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
//                                                                    $("#registerCMND").val(vCCCD);
//                                                                }
//                                                            } else if(vHC !== "") {
//                                                                document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).value=JS_STR_COMPONENT_DN_VALUE_PREFIX_HC;
//                                                                $("#"+ids).val(vHC);
//                                                                if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
//                                                                    $("#registerCMND").val(vHC);
//                                                                }
//                                                            }
                                                            if($("#CERTIFICATION_PURPOSE").val() !== JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL_GOV
                                                                && $("#CERTIFICATION_PURPOSE").val() !== JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE_GOV)
                                                            {
                                                                //document.getElementById(ids).disabled=true;
                                                                //document.getElementById(JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID).disabled=true;
                                                            }
                                                        }
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
                                                                $("#registerFullname").val(vPERSONAL_NAME);
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
                </form>
            </div>
                                                    
            <div class="x_title" style="display: <%=!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "none" : ""%>">
                <h2></h2>
                <ul class="nav navbar-right panel_toolbox">
                    <li>
                        <%
                            if(pCERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_DECLINED
                                    && pCERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_NEW)
                            {
                                if(CommonFunction.CheckRoleFuncValidOrNot(Definitions.CONFIG_ROLE_PROPERTIES_CERT_BUY_MORE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                        %>
                        <input type="button" id="btnSaveFooter" class="btn btn-info" onclick="ValidateForm('<%=ids%>', '<%=anticsrf%>', '<%=isCALoad%>');" />
                        <script>document.getElementById("btnSaveFooter").value = global_fm_button_add;</script>
                        <%
                                }
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
                                                        
            <%          }
                    }
                }
            %>
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
            <script>
                $(document).ready(function () {
                    //$("select#CERTIFICATION_AUTHORITY").prop('selectedIndex', 0);
                    //LOAD_CERTIFICATION_AUTHORITY($("#CERTIFICATION_AUTHORITY").val(), '<= anticsrf%>');
                });
            </script>
        
<!--        <script src="../style/bootstrap.min.js"></script>
        <script src="../style/custom.min.js"></script>
        <link href="../js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
        <script src="../js/checkphone/intlTelInput.js" type="text/javascript"></script>
        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/main.js"></script>-->
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
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