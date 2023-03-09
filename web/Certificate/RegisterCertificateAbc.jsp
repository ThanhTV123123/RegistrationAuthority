<%-- 
    Document   : RegisterCertificateAbc
    Created on : Aug 5, 2019, 6:08:12 PM
    Author     : THANH-PC
--%>

<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<%    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
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
        <!--<link href="../Css/active/bootstrap-switch.css" rel="stylesheet">-->
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link rel="stylesheet" type="text/css" media="all" href="../js/daterangepicker.css" />
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            document.title = regiscert_title_view;
            $(document).ready(function () {
                $('.loading-gif').hide();
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
                    window.location = 'RegisterCertSoft.jsp';
                });  
//                $("#input-file-csr").val("");
//                $("#input-file-csr").attr("disabled", true);
//                $('#IsCheckCSR').prop('checked', false);
            });
            var checkForSpecialChar = function(string){
             for(i = 0; i < specialChars.length;i++){
               if(string.indexOf(specialChars[i]) > -1){
                   return true;
                }
             }
             return false;
            };
            function ValidateForm(idCSRF)
            {
                var vSTR_COMPONENT_DN_VALUE_COMMONNAME = "";
                var vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = "";
                var vSTR_COMPONENT_DN_VALUE_MST = "";
                var vSTR_COMPONENT_DN_VALUE_MNS = "";
                var vSTR_COMPONENT_DN_VALUE_CMND = "";
                var vSTR_COMPONENT_DN_VALUE_HC = "";
                var vSTR_COMPONENT_DN_VALUE_PROVINCE_ID = "";
                var vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC = "";
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
                    if (!JSCheckFormatPhoneNew_EditOne($("#PHONE_CONTRACT")))
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
                    if (!FormCheckEmailSearch($("#EMAIL_CONTRACT").val()))
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
                var vDNResult = "";
                var sChoiseCert = $("#CERTIFICATION_AUTHORITY").val();
                if (sChoiseCert !== "")
                {
                    // CHECK SPECIAL FIELDS
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
                        if(sListRequire[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST
                            || sListRequire[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                        {
                            var sSelectedRequireMST_MNS = $("input[name='"+JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS+"']:checked").val();
                            if(sListRequire[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                            {
                                if(sSelectedRequireMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                {
                                    if (!JSCheckEmptyField($("#" + JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).val()))
                                    {
                                        $("#" + JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).focus();
                                        funErrorAlert(policy_req_empty + sListRequire[i].split('###')[1]);
                                        return false;
                                    }
                                }
                            }
                            if(sListRequire[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                            {
                                if(sSelectedRequireMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                                {
                                    if (!JSCheckEmptyField($("#" + JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).val()))
                                    {
                                        $("#" + JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).focus();
                                        funErrorAlert(policy_req_empty + sListRequire[i].split('###')[1]);
                                        return false;
                                    }
                                }
                            }
                        }
                        if(sListRequire[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND
                            || sListRequire[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                        {
                            var sSelectedRequireCMND_HC = $("input[name='"+JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC+"']:checked").val();
                            if(sListRequire[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND)
                            {
                                if(sSelectedRequireCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND)
                                {
                                    if (!JSCheckEmptyField($("#" + JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).val()))
                                    {
                                        $("#" + JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).focus();
                                        funErrorAlert(policy_req_empty + sListRequire[i].split('###')[1]);
                                        return false;
                                    }
                                }
                            }
                            if(sListRequire[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                            {
                                if(sSelectedRequireCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                                {
                                    if (!JSCheckEmptyField($("#" + JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).val()))
                                    {
                                        $("#" + JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).focus();
                                        funErrorAlert(policy_req_empty + sListRequire[i].split('###')[1]);
                                        return false;
                                    }
                                }
                            }
                        }
                        if(sListRequire[i].split('###')[0].indexOf(JS_STR_COMPONENT_DN_VALUE_EMAIL) !== -1)
                        {
                            if (JSCheckEmptyField($("#" + sListRequire[i].split('###')[0]).val()))
                            {
                                if (!FormCheckEmailSearch($("#" + sListRequire[i].split('###')[0]).val()))
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
                                if (!JSCheckFormatPhoneNew_EditOne($("#" + sListRequire[i].split('###')[0])))
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
                            if (JSCheckEmptyField($("#" + idCheckOutRequired).val()))
                            {
                                if (!FormCheckEmailSearch($("#" + idCheckOutRequired).val()))
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
                                if (!JSCheckFormatPhoneNew_EditOne($("#" + idCheckOutRequired)))
                                {
                                    $("#" + idCheckOutRequired).focus();
                                    funErrorAlert(global_req_phone_format);
                                    return false;
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
                    var Is_Has_MST_MNS = "";
                    var Is_Has_CMND_HC = "";
                    var Is_Default_CN_TYPE_PERSON = "";
                    for (var i = 0; i < sListInput.length; i++) {
                        var idItemValue = sListInput[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                        var itemValue = $("#" + idItemValue).val();
                        if(sListInput[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST || sListInput[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS
                            || sListInput[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND || sListInput[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                        {
                            if(sListInput[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST || sListInput[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                            {
                                Is_Has_MST_MNS = JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + "@@@" + sListInput[i].split('###')[1];
                            }
                            if(sListInput[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND || sListInput[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                            {
                                Is_Has_CMND_HC = JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + "@@@" + sListInput[i].split('###')[1];
                            }
                        }
                        else
                        {
                            if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                            {
                                if(sListInput[i].split('###')[3] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_PERSON)
                                {
                                    vSTR_COMPONENT_DN_VALUE_COMMONNAME = itemValue;
                                } else if(sListInput[i].split('###')[3] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_COMPANY)
                                {
                                    vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = itemValue;
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
//                                   itemValue = JSEncodeHTMLEntityDash(itemValue);
                                    itemValue = itemValue.replace(/,/g , '\\,');
                                }
                            }
                            if(itemValue !== "") {
                                vDNResult += sListInput[i].split('###')[1] + "=" + sListInput[i].split('###')[2] +
                                    sSpace(itemValue) + ", ";
                            }
                        }
                    }
                    if(Is_Has_MST_MNS !== "")
                    {
                        var sSelectedMST_MNS = $("input[name='"+Is_Has_MST_MNS.split('@@@')[0]+"']:checked").val();
                        var sValueMST_MNS = $("#" + Is_Has_MST_MNS.split('@@@')[0] + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).val();
                        vDNResult = vDNResult + Is_Has_MST_MNS.split('@@@')[1] + "=" + sSpace(sSelectedMST_MNS) + sSpace(sValueMST_MNS) + ", ";
                        if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                        {
                            vSTR_COMPONENT_DN_VALUE_MNS = sValueMST_MNS;
                        }
                        if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                        {
                            vSTR_COMPONENT_DN_VALUE_MST = sValueMST_MNS;
                        }
                    }
                    if(Is_Has_CMND_HC !== "")
                    {
                        var sSelectedCMND_HC = $("input[name='"+Is_Has_CMND_HC.split('@@@')[0]+"']:checked").val();
                        var sValueCMND_HC = $("#" + Is_Has_CMND_HC.split('@@@')[0] + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).val();
                        vDNResult = vDNResult + Is_Has_CMND_HC.split('@@@')[1] + "=" + sSpace(sSelectedCMND_HC) + sSpace(sValueCMND_HC) + ", ";
                        if(sSelectedCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND)
                        {
                            vSTR_COMPONENT_DN_VALUE_CMND = sValueCMND_HC;
                        }
                        if(sSelectedCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                        {
                            vSTR_COMPONENT_DN_VALUE_HC = sValueCMND_HC;
                        }
                    }
                    var intSub = vDNResult.lastIndexOf(',');
                    vDNResult = vDNResult.substring(0, intSub);
                }
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
                var sCheckPUSH_NOTICE = "0";
                if ($("#PUSH_NOTICE_ENABLED").is(':checked'))
                {
                    sCheckPUSH_NOTICE = "1";
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../RequestCommon",
                    data: {
                        idParam: 'checkcertarlamuser',
                        pCERTIFICATION_PURPOSE: $("#CERTIFICATION_PURPOSE").val(),
                        pMNS: vSTR_COMPONENT_DN_VALUE_MNS,
                        pMST: vSTR_COMPONENT_DN_VALUE_MST,
                        pCMND: vSTR_COMPONENT_DN_VALUE_CMND,
                        pHC: vSTR_COMPONENT_DN_VALUE_HC
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
                                            idParam: 'registrationcertsoft',
                                            BRANCH_ID: $("#AGENT_NAME").val(),
                                            sTypeRegister: JS_STR_CERTIFICATION_PURPOSE_CODE_TOKEN,
                                            CertProfileID: $("#idHiddenCerDurationOrProfileID").val(),
                                            CACoreSubject: $("#idHiddenCerCoreSubject").val(),
                                            pCERTIFICATION_PURPOSE: $("#CERTIFICATION_PURPOSE").val(),
                                            pCSR: $("#idCSR").val(),
                                            DN: vDNResult,
                                            pPERSONAL_NAME: vSTR_COMPONENT_DN_VALUE_COMMONNAME,
                                            pCOMPANY_NAME: vSTR_COMPONENT_DN_VALUE_COMPANY_NAME,
                                            pDOMAIN_NAME: "",
                                            pTAX_CODE: vSTR_COMPONENT_DN_VALUE_MST,
                                            pBUDGET_CODE: vSTR_COMPONENT_DN_VALUE_MNS,
                                            pP_ID: vSTR_COMPONENT_DN_VALUE_CMND,
                                            pPASSPORT: vSTR_COMPONENT_DN_VALUE_HC,
                                            PHONE_CONTRACT: $("#PHONE_CONTRACT").val(),
                                            EMAIL_CONTRACT: $("#EMAIL_CONTRACT").val(),
                                            CREATE_USER: $("#USER").val(),
                                            pPROVINCE_ID: vSTR_COMPONENT_DN_VALUE_PROVINCE_ID,
                                            pPROVINCE_DESC: vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC,
                                            CheckPUSH_NOTICE: sCheckPUSH_NOTICE,
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
                                                            ShowDialogPrint(arr[1], "0");
                                                        } else {
                                                            window.location = 'RegisterCertSoft.jsp';
                                                        }
                                                    });		
                                                }
                                                if(arr[2] === "0")
                                                {
                                                    localStorage.setItem("PrintRegisterPersonal", null);
                                                    localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", arr[1]);
                                                    localStorage.setItem("PrintRegisterBusiness", arr[1]);
//                                                    window.location = "PrintRegisterBusiness.jsp?id=" + arr[1];
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
                                                            ShowDialogPrint(arr[1], "1");
                                                        } else {
                                                            window.location = 'RegisterCertSoft.jsp';
                                                        }
                                                    });		
                                                }
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
                                            else if (arr[0] === JS_EX_CSR_KEYSIZE)
                                            {
                                                funErrorAlert(global_error_keysize_csr);
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
                                }, JS_STR_ACTION_TIMEOUT);
                            });
                        } else if (arr_first[0] === "1")
                        {
                            $.ajax({
                                type: "post",
                                url: "../RequestCommon",
                                data: {
                                    idParam: 'registrationcertsoft',
                                    BRANCH_ID: $("#AGENT_NAME").val(),
                                    sTypeRegister: JS_STR_CERTIFICATION_PURPOSE_CODE_TOKEN,
                                    CertProfileID: $("#idHiddenCerDurationOrProfileID").val(),
                                    CACoreSubject: $("#idHiddenCerCoreSubject").val(),
                                    pCERTIFICATION_PURPOSE: $("#CERTIFICATION_PURPOSE").val(),
                                    pCSR: $("#idCSR").val(),
                                    DN: vDNResult,
                                    pPERSONAL_NAME: vSTR_COMPONENT_DN_VALUE_COMMONNAME,
                                    pCOMPANY_NAME: vSTR_COMPONENT_DN_VALUE_COMPANY_NAME,
                                    pDOMAIN_NAME: "",
                                    pTAX_CODE: vSTR_COMPONENT_DN_VALUE_MST,
                                    pBUDGET_CODE: vSTR_COMPONENT_DN_VALUE_MNS,
                                    pP_ID: vSTR_COMPONENT_DN_VALUE_CMND,
                                    pPASSPORT: vSTR_COMPONENT_DN_VALUE_HC,
                                    PHONE_CONTRACT: $("#PHONE_CONTRACT").val(),
                                    EMAIL_CONTRACT: $("#EMAIL_CONTRACT").val(),
                                    CREATE_USER: $("#USER").val(),
                                    pPROVINCE_ID: vSTR_COMPONENT_DN_VALUE_PROVINCE_ID,
                                    pPROVINCE_DESC: vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC,
                                    CheckPUSH_NOTICE: sCheckPUSH_NOTICE,
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
                                                    ShowDialogPrint(arr[1], "0");
                                                } else {
                                                    window.location = 'RegisterCertSoft.jsp';
                                                }
                                            });	
                                        }
                                        if(arr[2] === "0")
                                        {
                                            localStorage.setItem("PrintRegisterPersonal", null);
                                            localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", arr[1]);
                                            localStorage.setItem("PrintRegisterBusiness", arr[1]);
//                                            window.location = "PrintRegisterBusiness.jsp?id=" + arr[1];
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
                                                    ShowDialogPrint(arr[1], "1");
                                                } else {
                                                    window.location = 'RegisterCertSoft.jsp';
                                                }
                                            });	
                                        }
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
                                    else if (arr[0] === JS_EX_CSR_KEYSIZE)
                                    {
                                        funErrorAlert(global_error_keysize_csr);
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
                        } else {
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
                window.location = "RegisterCertSoft.jsp";
//                $.ajax({
//                    type: "post",
//                    url: "../UserCommon",
//                    data: {
//                        idParam: 'backformpage',
//                        idSession: 'RefreshRegisTokenSess'
//                    },
//                    cache: false,
//                    success: function (html) {
//                        var arr = sSpace(html);
//                        if (arr === "0")
//                        {
//                            window.location = "RegisterCertList.jsp";
//                        }
//                        else
//                        {
//                            window.location = "RegisterCertList.jsp";
//                        }
//                    }
//                });
//                return false;
            }
        </script>
    </head>
    <body class="nav-md">
        <%
            if (session.getAttribute("sUserID") != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String loginUID = session.getAttribute("UserID").toString().trim();
                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                String SessRoleID_ID = session.getAttribute("RoleID_ID").toString().trim();
                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Cert");
                CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessPolicyCert_Data");
                session.setAttribute("sessUploadFileCert", null);
                int pPKI_FORMFACTOR_ID = Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN;
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
                        document.getElementById("idNameURL").innerHTML = regiscert_soft_title_list;
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
                                        try {
                                            String pPUSH_NOTICE_ENABLED = "";
                                            String strCADefault="";
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
                                                }
                                            }
                                    %>
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEdits"></span></h2>
                                        <script>$("#idLblTitleEdits").text(regiscert_title_view);</script>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <%
                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_ISSUE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                %>
                                                <input type="button" id="btnSave" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');" />
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
                                            <%
                                                if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            %>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleAgentName" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="AGENT_NAME" id="AGENT_NAME" class="form-control123"
                                                    onchange="LOAD_BACKOFFICE_USER(this.value, '<%= anticsrf%>');">
                                                    <%
                                                        BRANCH[][] rst = new BRANCH[1][];
                                                        String sBranchFirst = "";
                                                        try {
                                                            db.S_BO_BRANCH_COMBOBOX(sessLanguageGlobal, rst);
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
                                                        <select name="USER" id="USER" class="form-control123">
                                                            <%
                                                                BACKOFFICE_USER[][] rssUser = new BACKOFFICE_USER[1][];
                                                                db.S_BO_GET_USER_BRANCH_ALL(sBranchFirst, rssUser);
                                                                if (rssUser[0].length > 0) {
                                                                    for (int i = 0; i < rssUser[0].length; i++) {
                                                            %>
                                                            <option value="<%=String.valueOf(rssUser[0][i].ID)%>"><%=rssUser[0][i].FULL_NAME + " (" + rssUser[0][i].USERNAME + ")" %></option>
                                                            <%
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
                                                }
                                            %>
                                        </form>
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
                    </div>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
        </div>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../style/custom.min.js"></script>
        <link href="../js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
        <script src="../js/checkphone/intlTelInput.js" type="text/javascript"></script>
        <script src="../js/active/highlight.js"></script>
        <!--<script src="../js/active/bootstrap-switch.js"></script>-->
        <!--<script src="../js/active/main.js"></script>-->
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