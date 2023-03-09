<%-- 
    Document   : RegisterCertSoft
    Created on : Nov 12, 2018, 11:39:50 AM
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
//                var sTempIS_MENU_LINK_SET_NO = $("#IsCheckCSR").bootstrapSwitch("state");
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
                                                } else {
                                            %>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleAgentName" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="AGENT_DESC" disabled value="<%= EscapeUtils.CheckTextNull(session.getAttribute("SessAgentName").toString())%>" class="form-control123">
                                                        <input type="text" name="AGENT_NAME" id="AGENT_NAME" disabled style="display: none;" value="<%= EscapeUtils.CheckTextNull(SessUserAgentID) %>">
                                                    </div>
                                                </div>
                                                <script>$("#idLblTitleAgentName").text(global_fm_Branch);</script>
                                            </div>
                                            <%
                                                if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                            %>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="USER" id="USER" class="form-control123">
                                                            <%
                                                                BACKOFFICE_USER[][] rssUser = new BACKOFFICE_USER[1][];
                                                                db.S_BO_GET_USER_BRANCH_ALL(EscapeUtils.CheckTextNull(SessUserAgentID), rssUser);
                                                                if (rssUser[0].length > 0) {
                                                                    for (int i = 0; i < rssUser[0].length; i++) {
                                                            %>
                                                            <option value="<%=String.valueOf(rssUser[0][i].ID)%>" <%= String.valueOf(rssUser[0][i].ID).equals(loginUID) ? "selected" : "" %>><%=rssUser[0][i].FULL_NAME + " (" + rssUser[0][i].USERNAME + ")" %></option>
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
                                            <input type="text" name="USER" id="USER" readonly style="display: none;" value="<%= loginUID%>" class="form-control123">
                                            <%
                                                    }
                                                }
                                            %>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitlePhoneContact"></label>
                                                        <label id="idLblNotePhoneContact" class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" id="PHONE_CONTRACT" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_PHONE %>" class="form-control123"
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
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleEmailContact"></label>
                                                        <label id="idLblNoteEmailContact" class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" id="EMAIL_CONTRACT" class="form-control123" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_EMAIL%>">
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleEmailContact").text(global_fm_email_contact);
                                                    $("#idLblNoteEmailContact").text(global_fm_require_label);
                                                </script>
                                            </div>
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
                                                                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                                                    {
                                                                        for (int i = 0; i < rssProfile[0].length; i++) {
                                                                            if(rssProfile[0][i].NAME.equals(strCADefault))
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
                                                                + "###" + EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT)%>" <%= rssProfile[0][i].NAME.equals(strCADefault) ? "selected" : "" %>><%=rssProfile[0][i].REMARK%></option>
                                                            <%
                                                                        }
                                                                    } else {
                                                                        boolean accessProfileAll = CommonFunction.checkAccessProfileAll(sessPolicyCert_Data);
                                                                        if(accessProfileAll == true)
                                                                        {
                                                                            for (int i = 0; i < rssProfile[0].length; i++) {
                                                                                if(rssProfile[0][i].NAME.equals(strCADefault))
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
                                                                + "###" + EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT)%>" <%= rssProfile[0][i].NAME.equals(strCADefault) ? "selected" : "" %>><%=rssProfile[0][i].REMARK%></option>
                                                            <%
                                                                            }
                                                                        } else {
                                                                            for (int i = 0; i < rssProfile[0].length; i++) {
                                                                                if(CommonFunction.checkAccessCAForBranch(rssProfile[0][i].NAME, sessPolicyCert_Data) == true)
                                                                                {
                                                                                    if(rssProfile[0][i].NAME.equals(strCADefault))
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
                                                                + "###" + EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT)%>" <%= rssProfile[0][i].NAME.equals(strCADefault) ? "selected" : "" %>><%=rssProfile[0][i].REMARK%></option>
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
                                            <input id="idSessProfileID" style="display: none;"/>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleCertPurpose" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select id="CERTIFICATION_PURPOSE" name="CERTIFICATION_PURPOSE" class="form-control123"
                                                            onchange="LOAD_CERTIFICATION_PURPOSE($('#idHiddenCerCA').val().split('###')[0], this.value, '<%= anticsrf%>');">
                                                            <%
                                                                String sFristCerPurpose="";
                                                                CERTIFICATION_PURPOSE[][] rsCertPro = new CERTIFICATION_PURPOSE[1][];
                                                                db.S_BO_CA_GET_CERTIFICATION_PURPOSE_COMBOBOX_BY_PKI_FORMFACTOR(sFristCA, pPKI_FORMFACTOR_ID, sessLanguageGlobal, rsCertPro);
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
                                                            %>
                                                        </select>
                                                    </div>
                                                </div>
                                                <script>$("#idLblTitleCertPurpose").text(global_fm_certpurpose);</script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleCertDuration" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select id="CERTIFICATION_DURATION" name="CERTIFICATION_DURATION" class="form-control123"
                                                            onchange="LOAD_CERTIFICATION_DURATION(this.value, '<%= anticsrf%>');">
                                                            <%
                                                                String sFristCerDurationOrProfileID = "";
                                                                CERTIFICATION_PROFILE[][] rsDuration = new CERTIFICATION_PROFILE[1][];
                                                                db.S_BO_CA_GET_DURATION_COMBOBOX(sFristCA, sFristCerPurpose, sessLanguageGlobal, rsDuration);
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
                                                            %>
                                                        </select>
                                                    </div>
                                                </div>
                                                <script>$("#idLblTitleCertDuration").text(global_fm_duration_cts);</script>
                                            </div>
                                            <input id="idHiddenCerCA" value="<%= sFristCA%>" style="display: none;"/>
                                            <input id="idHiddenCerCoreSubject" value="<%= sCACoreSubject%>" style="display: none;"/>
                                            <input id="idHiddenCerPurpose" value="<%= sFristCerPurpose%>" style="display: none;"/>
                                            <input id="idHiddenCerDurationOrProfileID" value="<%= sFristCerDurationOrProfileID%>" style="display: none;"/>
                                            <script>
                                                $(document).ready(function () {
                                                    if('<%= sFristCA%>' !== "" && '<%= sFristCerPurpose%>' !== "" && '<%= sFristCerDurationOrProfileID%>' !== "")
                                                    {
                                                        LOAD_CERTIFICATION_PROFILE('<%= sFristCerDurationOrProfileID%>');
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
                                                <div class="form-group">
                                                    <label class="radio-inline">
                                                        <input type="radio" name="nameCheck" id="nameCheck1" checked>
                                                        <script>document.write(global_fm_choose_genkey_server);</script>
                                                    </label>
                                                    <label class="radio-inline">
                                                        <input type="radio" name="nameCheck" id="nameCheck2">
                                                        <script>document.write(global_fm_choose_genkey_client);</script>
                                                    </label>
                                                    <input type="text" style="display: none;" id="idSessIsChoise" name="idSessIsChoise"/>
<!--                                                    <label id="idLblTitleChooseCSR" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input TYPE="checkbox" id="IsCheckCSR" name="IsCheckCSR" onchange="ClickCheckCSR();"/>
                                                    </div>-->
                                                </div>
                                                <script>
                                                    $(document).ready(function () {
                                                        $("#idLblTitleChooseCSR").text(global_fm_choose_csr);
                                                        $("#idSessIsChoise").val('1');
                                                        $("#input-file-csr").val("");
                                                        $("#input-file-csr").attr("disabled", true);
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
                                            
                                            
                                            
                                            
                                            
                                            
                                            
                                            
                                            
                                            <style>
                                                .form-control123[readonly]{background-color:#ffffff;opacity:1}
                                                .form-control123[disabled]{background-color:#eee;opacity:1}
                                                .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                                .btn{margin-bottom: 0px;}
                                            </style>
                                            <div style="height: 10px;"></div>
                                            <div id="idDivShowFileMana" style="clear: both;">
                                                <%
                                                    String sJSON = "";
                                                    CERTIFICATION_PURPOSE[][] rsPURPOSE = new CERTIFICATION_PURPOSE[1][];
                                                    db.S_BO_CERTIFICATION_PURPOSE_DETAIL_BY_CERTIFICATION_ATTR_TYPE(sFristCerPurpose, "", rsPURPOSE);
                                                    if(rsPURPOSE[0].length > 0)
                                                    {
                                                        sJSON = EscapeUtils.CheckTextNull(rsPURPOSE[0][0].FILE_PROPERTIES);
                                                    }
                                                    if(!"".equals(sJSON)) {
                                                        ObjectMapper oMapperParse = new ObjectMapper();
                                                        FILE_PROFILE_JSON itemParsePush = oMapperParse.readValue(sJSON, FILE_PROFILE_JSON.class);
                                                        for (FILE_PROFILE_JSON.Attribute attribute : itemParsePush.getAttributes()) {
                                                            String sRemark = attribute.getRemark().trim();
                                                            String sName = attribute.getName().trim();
                                                            if(attribute.getEnabled() == true) {
                                                %>
                                                <fieldset class="scheduler-border">
                                                    <legend class="scheduler-border"><%= sRemark%></legend>
                                                    <div class="col-sm-13" style="padding-left: 0;">
                                                        <div class="form-group">
                                                            <label id="idLblTitleUploadManager" class="control-label col-sm-1" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                            <div class="col-sm-11" style="padding-right: 0px;">
                                                                <input type="file" id="input-file<%=sName%>" style="width: 100%;"
                                                                    onchange="calUploadFile(this, '<%= sName%>');" class="btn btn-default btn-file select_file" multiple>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="control-label123" style="color:red;font-weight: 200;" id="idLblTitleNoteManager"></label>
                                                        </div>
                                                        <script>
                                                            $("#idLblTitleUploadManager").text(global_fm_browse_file);
                                                            $("#idLblTitleNoteManager").text(global_fm_browse_cert_note + '<%= Integer.parseInt(sMaxLengthFile) / 1024 %>' + 'MB');
                                                        </script>
                                                    </div>
                                                    <div style="padding: 10px 0 10px 0;display: none;" id="idDiv<%= sName%>">
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
                                                            enctype: "multipart/form-data",
                                                            success: function (html) {
                                                                if (html.length > 0)
                                                                {
                                                                    var obj = JSON.parse(html);
                                                                    if (obj[0].Code === "0")
                                                                    {
                                                                        input1.value = '';
                                                                        $("#idTBody" + idType).empty();
                                                                        var content = "";
                                                                        for (var i = 0; i < obj.length; i++) {
                                                                            if(obj[i].FILE_PROFILE === idType)
                                                                            {
                                                                                var sActionCRL = '<a style="cursor: pointer;" class="btn btn-info\n\
                                                                                    btn-xs" onclick="DeleteTempFile(\'' + obj[i].FILE_PROFILE + '\', \'' + obj[i].FILE_NAME + '\');">\n\
                                                                                    <i class="fa fa-pencil"></i> ' + global_fm_button_delete + '</a>';
                                                                                content += "<tr>" +
                                                                                    "<td>" + obj[i].Index + "</td>" +
                                                                                    "<td>" + obj[i].FILE_NAME + "</td>" +
                                                                                    "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                                    "<td>" + sActionCRL +"</td>" +
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
//                                                                        console.log(html);
                                                                        var obj = JSON.parse(html);
                                                                        if (obj[0].Code === "0")
                                                                        {
                                                                            $("#idTBody" + idType).empty();
                                                                            var content = "";
                                                                            for (var i = 0; i < obj.length; i++) {
                                                                                if(obj[i].FILE_PROFILE === idType)
                                                                                {
                                                                                    var sActionCRL = '<a style="cursor: pointer;" class="btn btn-info\n\
                                                                                        btn-xs" onclick="DeleteTempFile(\'' + obj[i].FILE_PROFILE + '\', \'' + obj[i].FILE_NAME + '\');">\n\
                                                                                        <i class="fa fa-pencil"></i> ' + global_fm_button_delete + '</a>';
                                                                                    content += "<tr>" +
                                                                                        "<td>" + obj[i].Index + "</td>" +
                                                                                        "<td>" + obj[i].FILE_NAME + "</td>" +
                                                                                        "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                                        "<td>" + sActionCRL +"</td>" +
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
//                                                                console.log(html);
                                                                var obj = JSON.parse(html);
                                                                if (obj[0].Code === "0")
                                                                {
                                                                    $("#idDivShowFileMana").empty();
                                                                    var content = "";
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        content += '<fieldset class="scheduler-border">'+
                                                                            '<legend class="scheduler-border">'+obj[i].REMARK+'</legend>'+
                                                                            '<div class="form-group" style="padding: 0px;margin: 0;">'+
                                                                                '<label class="control-label123">'+global_fm_browse_file+'</label>'+
                                                                                '<input type="file" id="input-file'+obj[i].NAME+'" style="width: 100%;"'+
                                                                                    'onchange="calUploadFile(this, \'' + obj[i].NAME + '\');" class="btn btn-default btn-file select_file" multiple>'+
                                                                                '<div style="height:10px;"></div><label class="control-label123" style="color:red;font-weight: 200;">' + global_fm_browse_cert_note + '<%= Integer.parseInt(sMaxLengthFile) / 1024 %>' + ' MB</label>'+
                                                                            '</div>'+
                                                                            '<div style="padding: 10px 0 10px 0;display:none;" id="idDiv'+obj[i].NAME+'">'+
                                                                                 '<table id="idTable'+obj[i].NAME+'" class="table table-striped projects">'+
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
                                            <div class="col-sm-13" style="padding-left: 0;clear: both;">
                                                <div class="form-group">
                                                    <fieldset class="scheduler-border">
                                                        <legend class="scheduler-border" id="idLblTitleComponentDN"></legend>
                                                        <div id="idViewCertInfo"></div>
                                                        <script>$("#idLblTitleComponentDN").text(global_fm_csr_info_cts);</script>
                                                    </fieldset>
                                                </div>
                                            </div>
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
                                                            idParam: 'loadcert_profile_list',
                                                            vCertDurationOrProfileID: vCertDurationOrProfileID
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            if (html.length > 0)
                                                            {
                                                                var obj = JSON.parse(html);
                                                                $("#idViewCertInfo").empty();
                                                                if (obj[0].Code === "0")
                                                                {
                                                                    $("#idViewCertInfo").css("display", "");
                                                                    localStorage.setItem("localStoreCompSTID", null);
                                                                    localStorage.setItem("localStoreCompSTValue", null);
                                                                    var vContent = "";
                                                                    var localStoreRequired = new Array();
                                                                    var localStoreInput = new Array();
                                                                    var localStoreInputID_OnInput = "";
                                                                    var localStoreInputID_Info = "";
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
                                                                            } else {
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
                                                                                localStoreInput.push(vInputID + "###" + obj[i].RADIO_LIST[j].SubjectDNAttrCode + "###" + obj[i].RADIO_LIST[j].SubjectDNAttrPreFix + "###" + obj[i].RADIO_LIST[j].SubjectDNAttrCNType + "###" + obj[i].RADIO_LIST[j].SubjectDNAttrDesc);
                                                                                if(obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS || obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                                                                {
                                                                                    vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                                                    if(vContentButton_MST_Check !== "")
                                                                                    {
                                                                                        vContentButton_MST_Radio += '<label class="radio-inline"><input type="radio" name="'+JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS
                                                                                            +'" id="'+vInputID+'" onclick="OnChangeRadioMST();" value="'+obj[i].RADIO_LIST[j].SubjectDNAttrPreFix+'">' + obj[i].RADIO_LIST[j].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label>';
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        vContentButton_MST_Check = "checked";
                                                                                        vContentButton_MST_Radio += '<label class="radio-inline"><input type="radio" '+vContentButton_MST_Check+' name="'+JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS
                                                                                            +'" id="'+vInputID+'" onclick="OnChangeRadioMST();" value="'+obj[i].RADIO_LIST[j].SubjectDNAttrPreFix+'">' + obj[i].RADIO_LIST[j].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label>';
                                                                                    }
                                                                                    if(vContentButton_MST_Text === "")
                                                                                    {
                                                                                        if(obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                                                                                        {
                                                                                            var vInputID_Text = JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
//                                                                                            vContentButton_MST_Text = '<div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">'+
//                                                                                                '<div style="width: 100%;padding-top: 5px;clear: both;">'+
//                                                                                                '<div style="float: left;width: 86%;">' +
//                                                                                                '<input class="form-control123" type="text" id="' + vInputID_Text + '" oninput="GetAlrmCertMSTMNS(this.value);" /></div>'+
//                                                                                                '</div>' +
//                                                                                                '<div style="float: right;text-align: right;">'+
//                                                                                                '<input class="btn btn-info" style="width: 120px;" disabled id="btnGetInfoMST" value="'+global_fm_button_get_info+'" type="button"/>'+
//                                                                                                '</div>' +
//                                                                                                '</div>'+
//                                                                                                '<div style="width: 100%;padding-top: 5px;">'+
//                                                                                                '<label style="color: red;" id="idHintMSTMNS">aa</label>'+
//                                                                                                '</div>'+
//                                                                                                '</div>';
                                                                                            vContentButton_MST_Text = '<div class="col-sm-12" style="margin-bottom:5px;padding-left:0;">'+
                                                                                                '<div style="width: 100%;padding-top: 5px;clear: both;">'+
                                                                                                '<div style="float: left;width: 86%;">' +
                                                                                                '<input class="form-control123" type="text" id="' + vInputID_Text + '" oninput="GetAlrmCertMSTMNS(this.value);" /></div>'+
                                                                                                '</div>' +
                                                                                                '<div style="float: right;text-align: right;">'+
                                                                                                '<input class="btn btn-info" style="width: 120px;" disabled id="btnGetInfoMST" value="'+global_fm_button_get_info+'" type="button"/>'+
                                                                                                '</div>' +
                                                                                                '</div>'+
                                                                                                '<div style="width: 100%;padding-top: 5px;">'+
                                                                                                '<label style="color: red;" id="idHintMSTMNS">aa</label>'+
                                                                                                '</div>'+
                                                                                                '</div>';
                                                                                        }
                                                                                        if(obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                                                                        {
                                                                                            var vInputID_Text = JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
//                                                                                            vContentButton_MST_Text = '<div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">'+
//                                                                                                '<div style="width: 100%;padding-top: 5px;clear: both;">'+
//                                                                                                '<div style="float: left;width: 86%;">' +
//                                                                                                '<input class="form-control123" type="text" id="' + vInputID_Text + '" oninput="GetAlrmCertMSTMNS(this.value);" /></div>'+
//                                                                                                '</div>' +
//                                                                                                '<div style="float: right;text-align: right;">'+
//                                                                                                '<input class="btn btn-info" style="width: 120px;" id="btnGetInfoMST" value="'+global_fm_button_get_info+'" type="button" onclick="GetInfoMST();"/>'+
//                                                                                                '</div>' +
//                                                                                                '</div>'+
//                                                                                                '<div style="width: 100%;padding-top: 5px;">'+
//                                                                                                '<label style="color: red;" id="idHintMSTMNS"></label>'+
//                                                                                                '</div>'+
//                                                                                                '</div>';
                                                                                            vContentButton_MST_Text = '<div class="col-sm-12" style="margin-bottom:5px;padding-left:0;">'+
                                                                                                '<div style="width: 100%;padding-top: 5px;clear: both;">'+
                                                                                                '<div style="float: left;width: 86%;">' +
                                                                                                '<input class="form-control123" type="text" id="' + vInputID_Text + '" oninput="GetAlrmCertMSTMNS(this.value);" /></div>'+
                                                                                                '</div>' +
                                                                                                '<div style="float: right;text-align: right;">'+
                                                                                                '<input class="btn btn-info" style="width: 120px;" id="btnGetInfoMST" value="'+global_fm_button_get_info+'" type="button" onclick="GetInfoMST();"/>'+
                                                                                                '</div>' +
                                                                                                '</div>'+
                                                                                                '<div style="width: 100%;padding-top: 5px;">'+
                                                                                                '<label style="color: red;" id="idHintMSTMNS"></label>'+
                                                                                                '</div>'+
                                                                                                '</div>';
                                                                                        }
                                                                                    }
                                                                                }
                                                                                else if(obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND || obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                                                                                {
                                                                                    vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                                                    if(vContentButton_CMND_Check !== "")
                                                                                    {
                                                                                        vContentButton_CMND_Radio += '<label class="radio-inline"><input type="radio" name="'+JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC
                                                                                            +'" id="'+vInputID+'" value="'+obj[i].RADIO_LIST[j].SubjectDNAttrPreFix+'">' + obj[i].RADIO_LIST[j].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label>';
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        vContentButton_CMND_Check = "checked";
                                                                                        vContentButton_CMND_Radio += '<label class="radio-inline"><input type="radio" '+vContentButton_CMND_Check+' name="'+JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC
                                                                                            +'" id="'+vInputID+'" value="'+obj[i].RADIO_LIST[j].SubjectDNAttrPreFix+'">' + obj[i].RADIO_LIST[j].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label>';
                                                                                    }
                                                                                    if(vContentButton_CMND_Text === "")
                                                                                    {
                                                                                        var vInputID_Text = JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
//                                                                                        vContentButton_CMND_Text = '<div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">'+
//                                                                                            '<input class="form-control123" type="text" id="' + vInputID_Text + '" oninput="GetAlrmCertCMNDHC(this.value);" />'+
//                                                                                            '<label style="color: red;" id="idHintCMNDHC"></label>'+
//                                                                                            '</div>';
                                                                                        vContentButton_CMND_Text ='<div class="col-sm-12" style="margin-bottom:10px;padding-left:0;">'+
                                                                                            '<input class="form-control123" type="text" id="' + vInputID_Text + '" oninput="GetAlrmCertCMNDHC(this.value);" />'+
                                                                                            '<label style="color: red;" id="idHintCMNDHC"></label>'+
                                                                                            '</div>';
                                                                                    }
                                                                                } else{}
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
                                                                            localStoreInput.push(vInputID + "###" + obj[i].SubjectDNAttrCode + "###" + obj[i].SubjectDNAttrPreFix + "###" + obj[i].SubjectDNAttrCNType + "###" + obj[i].SubjectDNAttrDesc);
                                                                            if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE)
                                                                            {
//                                                                                vContent += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
//                                                                                    '<label class="control-label123">' + obj[i].SubjectDNAttrDesc + '</label> ' +
//                                                                                    vLabelRequired +
//                                                                                    '<select class="form-control123" id="' + vInputID + '"></select>' +
//                                                                                    '</div>';
                                                                                vContent += '<div class="col-sm-6" style="padding-left: 0px;">' +
                                                                                    '<div class="form-group">' +
                                                                                    '<label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                                                    '<div class="col-sm-7" style="padding-right: 0px;">'+
                                                                                    '<select class="form-control123" id="' + vInputID + '"></select>' +
                                                                                    '</div>' +
                                                                                    '</div>' +
                                                                                    '</div>';
                                                                                LoadDNCity(vInputID);
                                                                            }
                                                                            else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_PHONE)
                                                                            {
                                                                                vContent += '<div class="col-sm-6" style="padding-left: 0px;">' +
                                                                                    '<div class="form-group">' +
                                                                                    '<label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                                                    '<div class="col-sm-7" style="padding-right: 0px;">'+
                                                                                    '<input class="form-control123" type="text" id="' + vInputID + '" onblur="autoTrimTextField("' + vInputID + '", this.value);" />' +
                                                                                    '</div>' +
                                                                                    '</div>' +
                                                                                    '</div>';
                                                                            }
                                                                            else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COUNTRY)
                                                                            {
                                                                                var valueCountry = '<%= sDN_Country%>';
//                                                                                vContent += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
//                                                                                    '<label class="control-label123">' + obj[i].SubjectDNAttrDesc + '</label> ' +
//                                                                                    vLabelRequired +
//                                                                                    '<input disabled class="form-control123" type="text" id="' + vInputID + '" value="'+valueCountry+'" />' +
//                                                                                    '</div>';
                                                                                vContent += '<div class="col-sm-6" style="padding-left: 0px;">' +
                                                                                    '<div class="form-group">' +
                                                                                    '<label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                                                    '<div class="col-sm-7" style="padding-right: 0px;">'+
                                                                                    '<input disabled class="form-control123" type="text" id="' + vInputID + '" value="'+valueCountry+'" />' +
                                                                                    '</div>' +
                                                                                    '</div>' +
                                                                                    '</div>';
                                                                            }
                                                                            else
                                                                            {
                                                                                vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
//                                                                                vContent += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
//                                                                                    '<label class="control-label123">' + obj[i].SubjectDNAttrDesc + '</label> ' +
//                                                                                    vLabelRequired +
//                                                                                    '<input class="form-control123" type="text" id="' + vInputID + '" />' +
//                                                                                    '</div>';
                                                                                vContent += '<div class="col-sm-6" style="padding-left: 0px;">' +
                                                                                    '<div class="form-group">' +
                                                                                    '<label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                                                    '<div class="col-sm-7" style="padding-right: 0px;">'+
                                                                                    '<input class="form-control123" type="text" id="' + vInputID + '" />' +
                                                                                    '</div>' +
                                                                                    '</div>' +
                                                                                    '</div>';
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
                                                                                    || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_SIGNSERVER_STAFF
                                                                                    || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE
                                                                                    || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_SIGNSERVER_ENTERPRISE)
                                                                                {
                                                                                    if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                                    {
                                                                                        localStoreInputID_Info = localStoreInputID_Info + JS_STR_COMPONENT_DN_VALUE_ORGANI + "###" + vInputID + ", ";
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    
                                                                    $("#idViewCertInfo").append(vContent);
                                                                    localStorage.setItem("localStoreRequiredPersonal", localStoreRequired);
                                                                    localStorage.setItem("localStoreInputPersonal", localStoreInput);
                                                                    localStorage.setItem("localStoreInputID_Info", localStoreInputID_Info);
                                                                    if(localStoreInputID_OnInput !== null && localStoreInputID_OnInput !== "null" && localStoreInputID_OnInput !== "")
                                                                    {
                                                                        if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE
                                                                            || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_SIGNSERVER_ENTERPRISE)
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
                                                                                document.getElementById(idOTemp).readOnly = true;
                                                                                document.getElementById(idCNTemp).oninput = function() { OnBlurCompany(idOTemp, this.value);};
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
                                                                    $("#idViewCertInfo").css("display", "none");
                                                                    funErrorAlert(global_errorsql);
                                                                }
                                                            }
                                                        }
                                                    });
                                                    return false;
                                                }
                                                function GetAlrmCertMSTMNS()
                                                {
                                                    var IsMST = "1";
                                                    var IsCall = "0";
                                                    $("#idHintMSTMNS").css("display", "none");
                                                    $("#idHintMSTMNS").text('');
                                                    var vInputID_Text = JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
                                                    var vMST = $("#"+vInputID_Text).val();
                                                    var sSelectedMST_MNS = $("input[name='"+JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS+"']:checked").val();
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
                                                    var vInputID_Text = JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
                                                    var vCMND= $("#" + vInputID_Text).val();
                                                    var IsCMND = "1";
                                                    var IsCall = "0";
                                                    $("#idHintCMNDHC").text('');
                                                    $("#idHintCMNDHC").css("display", "none");
                                                    var sSelectedMST_MNS = $("input[name='"+JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC+"']:checked").val();
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
                                                    var vMST = $("#MST_MNS_TEXT").val();
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
                                                                            if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_STAFF
                                                                                || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_SIGNSERVER_STAFF)
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
                                                                                    || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_SIGNSERVER_ENTERPRISE)
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
//                                                                                $("#" +sListInputCheckID_Info[i].split('###')[1]+ " option[value='" + obj[0].PROVINCE + "']").attr("selected", "selected");
//                                                                                $("#ST7").val("Hà Nội");
//                                                                                $("#" +sListInputCheckID_Info[i].split('###')[1]+ " option[value='" + obj[0].PROVINCE + "']").attr("selected", "selected");
//                                                                                $("#" + sSpace(sListInputCheckID_Info[i].split('###')[1])).val(obj[0].PROVINCE);
                                                                            }
                                                                            if(idCheckEmptyID_Info === JS_STR_COMPONENT_DN_VALUE_LOCALITY)
                                                                            {
                                                                                $("#" + sSpace(sListInputCheckID_Info[i].split('###')[1])).val(obj[0].LOCALTION);
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
                                                    if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                                    {
                                                        document.getElementById("btnGetInfoMST").disabled = false;
                                                    }
                                                }
                                                function LOAD_CERTIFICATION_PROFILE(vCertDurationOrProfileID)
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
                                                function LOAD_CERTIFICATION_AUTHORITY(objCA, idCSRF)
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
//                                                            LOAD_CERTIFICATION_FEE_INDEX();
                                                        }
                                                    });
                                                    return false;
                                                }
                                                function LOAD_CERTIFICATION_PURPOSE(objCA, objPurpose, idCSRF)
                                                {
                                                    $("#idHiddenCerPurpose").val(objPurpose);
                                                    LoadFileManage(objPurpose);
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loadcert_purpose',
                                                            idCA: objCA,
                                                            idPurpose: objPurpose,
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
//                                                                    cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option(global_fm_combox_choose, "");
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                                                    }
                                                                    LOAD_CERTIFICATION_DURATION(obj[0].ID, idCSRF);
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
//                                                            LOAD_CERTIFICATION_FEE_INDEX();
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
                                            </script>
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
                    <script>
                        $(document).ready(function () {
                            $("select#CERTIFICATION_AUTHORITY").prop('selectedIndex', 0);
                            LOAD_CERTIFICATION_AUTHORITY($("#CERTIFICATION_AUTHORITY").val(), '<%= anticsrf%>');
                        });
                    </script>
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
        <script src="../js/active/main.js"></script>
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
            function ShowDialogPrint(id, isEnterprise)
            {
                $('#myModalRegisterPrint').modal('show');
                $('#contentRegisterPrint').empty();
                localStorage.setItem("sessCertToPrint", "1");
                if(isEnterprise === "1")
                {
                    localStorage.setItem("PrintRegisterPersonal", null);
                    localStorage.setItem("PrintRegisterBusiness", id);
                    $('#contentRegisterPrint').load('PrintRegisterBusiness.jsp', {id:id}, function () {
                    });
                }
                else {
                    localStorage.setItem("PrintRegisterPersonal", id);
                    localStorage.setItem("PrintRegisterBusiness", null);
                    $('#contentRegisterPrint').load('PrintRegisterPersonal.jsp', {id:id}, function () {
                    });
                }
                $(".loading-gifRegisterPrint").hide();
                $(".loading-gif").hide();
                $('#over').remove();
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