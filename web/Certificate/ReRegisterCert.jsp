<%-- 
    Document   : ReRegisterCert
    Created on : Oct 12, 2018, 3:51:13 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
%>
<!DOCTYPE html>
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
            });
            var checkForSpecialChar = function(string){
                for(i = 0; i < specialChars.length;i++){
                  if(string.indexOf(specialChars[i]) > -1){
                      return true;
                   }
                }
                return false;
            };
            function ValidateForm(idCSRF) {
                var vSTR_COMPONENT_DN_VALUE_COMMONNAME = "";
                var vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = "";
                var vSTR_COMPONENT_DN_VALUE_MST = "";
                var vSTR_COMPONENT_DN_VALUE_MNS = "";
                var vSTR_COMPONENT_DN_VALUE_CMND = "";
                var vSTR_COMPONENT_DN_VALUE_HC = "";
                var vSTR_COMPONENT_DN_VALUE_PROVINCE_ID = "";
                var vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC = "";
                if (!JSCheckEmptyField(document.myname.TOKEN_SN.value))
                {
                    funErrorAlert(policy_req_empty + token_fm_tokenid);
                    return false;
                }
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
                    funErrorAlert(policy_req_empty + global_fm_email_contact);
                    return false;
                } else {
                    if (!FormCheckEmailSearch($("#EMAIL_CONTRACT").val()))
                    {
                        $("#EMAIL_CONTRACT").focus();
                        funErrorAlert(global_req_mail_format);
                        return false;
                    }
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
                                    itemValue = itemValue.replace(/,/g , '\\,');
                                }
                            }
                            if(itemValue !== "") {
                                vDNResult += sListInput[i].split('###')[1] + "=" + sListInput[i].split('###')[2] +
                                    itemValue + ", ";
                            }
                        }
                    }
                    if(Is_Has_MST_MNS !== "")
                    {
                        var sSelectedMST_MNS = $("input[name='"+Is_Has_MST_MNS.split('@@@')[0]+"']:checked").val();
                        var sValueMST_MNS = $("#" + Is_Has_MST_MNS.split('@@@')[0] + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).val();
                        vDNResult = vDNResult + Is_Has_MST_MNS.split('@@@')[1] + "=" + sSelectedMST_MNS + sValueMST_MNS + ", ";
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
                        vDNResult = vDNResult + Is_Has_CMND_HC.split('@@@')[1] + "=" + sSelectedCMND_HC + sValueCMND_HC + ", ";
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
//                alert(vDNResult);
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../RequestCommon",
                    data: {
                        idParam: 're-registrationcert',
                        sID: document.myname.ID.value,
                        sTypeRegister: JS_STR_CERTIFICATION_PURPOSE_CODE_TOKEN,
                        CertProfileID: $("#idHiddenCerDurationOrProfileID").val(),
                        sTOKEN_ID: $("#TOKEN_ID").val(),
                        sTOKEN_SN: $("#TOKEN_SN").val(),
                        CACoreSubject: $("#idHiddenCerCoreSubject").val(),
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
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            if(arr[3] === "1")
                            {
                                pushNotificationApprove(document.myname.ID.value, $("#idHiddenCerDurationOrProfileID").val());
                            }
                            if(arr[2] === "1")
                            {
                                localStorage.setItem("PrintRegisterPersonal", arr[1]);
                                localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", arr[1]);
                                localStorage.setItem("PrintRegisterBusiness", null);
                                window.location = "PrintRegisterPersonal.jsp?id=" + arr[1];
                            }
                            if(arr[2] === "0")
                            {
                                localStorage.setItem("PrintRegisterPersonal", null);
                                localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", arr[1]);
                                localStorage.setItem("PrintRegisterBusiness", arr[1]);
                                window.location = "PrintRegisterBusiness.jsp?id=" + arr[1];
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
    </head>
    <body>
        <body class="nav-md">
        <%
            if (session.getAttribute("sUserID") != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String loginUID = session.getAttribute("UserID").toString().trim();
                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Cert");
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
                                        CERTIFICATION[][] rs = new CERTIFICATION[1][];           
                                        String sDN_Country = "";
                                        String sFristCerDurationOrProfileID = "";
                                        try {
                                            String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                            if (EscapeUtils.IsInteger(ids) == true) {
                                                db.S_BO_CERTIFICATION_DETAIL(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                                                if (rs[0].length > 0) {
                                                    boolean isAccessAgencyPage = true;
                                                    if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                        if (!String.valueOf(rs[0][0].BRANCH_ID).equals(SessUserAgentID)) {
                                                            isAccessAgencyPage = false;
                                                        }
                                                    }
                                                    if (isAccessAgencyPage == true) {
                                                        String sTOKEN_ID = String.valueOf(rs[0][0].TOKEN_ID);
                                                        String sTOKEN_SN = EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN);
                                                        TOKEN[][] rsToken = new TOKEN[1][];
                                                        db.S_BO_TOKEN_DETAIL(EscapeUtils.escapeHtml(sTOKEN_ID), rsToken);
                                                        if(rsToken[0][0].TOKEN_STATE_ID != Definitions.CONFIG_TOKEN_STATE_ID_LOCKED
                                                            && rsToken[0][0].TOKEN_STATE_ID != Definitions.CONFIG_TOKEN_STATE_ID_LOST) {
                                                            GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                                                            if (sessGeneralPolicy[0].length > 0) {
                                                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                                {
                                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_CERTIFICATE_ATTRIBUTE_COUNTRY))
                                                                    {
                                                                        sDN_Country = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                    }
                                                                }
                                                            }
                                                            String strFEE_AMOUNT = "0";
                                                            if(rs[0][0].FEE_AMOUNT != 0)
                                                            {
                                                                strFEE_AMOUNT = com.convertMoney(rs[0][0].FEE_AMOUNT);
                                                            }
                                                            String strDN = EscapeUtils.CheckTextNull(rs[0][0].SUBJECT);
                                                            if(!"".equals(strDN))
                                                            {
                                                                strDN = strDN.replace("\\,", "###");
                                                            }
                                                            int strCITY_PROVINCE_ID = rs[0][0].CITY_PROVINCE_ID;
                                    %>
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(regiscert_title_view);</script></h2>
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
                                                document.getElementById("btnClose").value = global_fm_button_back;
                                            </script>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form name="myname" method="post" class="form-horizontal">
                                            <input type="hidden" name="TOKEN_ID" id="TOKEN_ID" hidden="true" readonly="true" value="<%= sTOKEN_ID%>">
                                            <input type="hidden" name="ID" id="ID" hidden="true" readonly="true" value="<%= ids%>">
                                            <input type="hidden" name="idHiddenDN" id="idHiddenDN" value="<%=strDN%>"/>
                                            <input type="hidden" name="USER" id="USER" readonly value="<%= String.valueOf(rs[0][0].CREATED_BY_ID)%>">
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <%
                                                String sViewTOKEN_SN = "none";
                                                if(!"".equals(sTOKEN_SN) && CommonFunction.checkViewTokenValid(sTOKEN_SN) == true) {
                                                    sViewTOKEN_SN = "";
                                                }
                                            %>
                                            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;display: <%= sViewTOKEN_SN%>">
                                                <label class="control-label123"><script>document.write(token_fm_tokenid);</script></label>
                                                <input type="text" name="TOKEN_SN" id="TOKEN_SN" disabled value="<%= sTOKEN_SN%>" class="form-control123">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_Branch);</script></label>
                                                <input type="text" name="AGENT_NAME" disabled value="<%= EscapeUtils.CheckTextNull(rs[0][0].BRANCH_DESC)%>" class="form-control123">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_phone_contact);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input type="text" id="PHONE_CONTRACT" value="<%= EscapeUtils.CheckTextNull(rs[0][0].PHONE_CONTRACT)%>" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_PHONE%>" class="form-control123">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_email_contact);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input type="text" id="EMAIL_CONTRACT" value="<%= EscapeUtils.CheckTextNull(rs[0][0].EMAIL_CONTRACT)%>" class="form-control123" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_EMAIL%>">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_ca);</script></label>
                                                <select name="CERTIFICATION_AUTHORITY" id="CERTIFICATION_AUTHORITY" class="form-control123"
                                                    onchange="LOAD_CERTIFICATION_AUTHORITY(this.value, '<%= anticsrf%>');">
                                                    <%
                                                        String sFristCA = String.valueOf(rs[0][0].CERTIFICATION_AUTHORITY_ID);
                                                        String sCACoreSubject = EscapeUtils.CheckTextNull(rs[0][0].ISSUER_SUBJECT);
                                                        CERTIFICATION_AUTHORITY[][] rssProfile = new CERTIFICATION_AUTHORITY[1][];
                                                        db.S_BO_CERTIFICATION_AUTHORITY_COMBOBOX(sessLanguageGlobal, rssProfile);
                                                        if (rssProfile[0].length > 0) {
                                                            for (int i = 0; i < rssProfile[0].length; i++) {
//                                                                sFristCA = String.valueOf(rs[0][0].CERTIFICATION_AUTHORITY_ID);
//                                                                sCACoreSubject = EscapeUtils.CheckTextNull(rs[0][0].ISSUER_SUBJECT);
                                                    %>
                                                    <option value="<%=String.valueOf(rssProfile[0][i].ID) 
                                                        + "###" + EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT)%>"
                                                        <%= rssProfile[0][i].ID == rs[0][0].CERTIFICATION_AUTHORITY_ID ? "selected" : "" %>><%=rssProfile[0][i].REMARK%></option>
                                                    <%
                                                            }
                                                        }
                                                    %>
                                                </select>
                                            </div>
                                            <input id="idSessProfileID" style="display: none;"/>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_certpurpose);</script></label>
                                                <select id="CERTIFICATION_PURPOSE" name="CERTIFICATION_PURPOSE" class="form-control123"
                                                    onchange="LOAD_CERTIFICATION_PURPOSE($('#idHiddenCerCA').val().split('###')[0], this.value, '<%= anticsrf%>');">
                                                    <%
                                                        String sFristCerPurpose = String.valueOf(rs[0][0].CERTIFICATION_PURPOSE_ID);
                                                        CERTIFICATION_PURPOSE[][] rsCertPro = new CERTIFICATION_PURPOSE[1][];
                                                        db.S_BO_CA_GET_CERTIFICATION_PURPOSE_COMBOBOX(sFristCA, sessLanguageGlobal, rsCertPro);
                                                        if (rsCertPro.length > 0) {
                                                            for (int i = 0; i < rsCertPro[0].length; i++) {
//                                                                sFristCerPurpose = String.valueOf(rsCertPro[0][0].ID);
                                                    %>
                                                    <option value="<%= String.valueOf(rsCertPro[0][i].ID)%>" <%= rsCertPro[0][i].ID == rs[0][0].CERTIFICATION_PURPOSE_ID ? "selected" : "" %>><%= rsCertPro[0][i].REMARK%></option>
                                                    <%
                                                            }
                                                        }
                                                    %>
                                                </select>
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_duration_cts);</script></label>
                                                <select id="CERTIFICATION_DURATION" name="CERTIFICATION_DURATION" class="form-control123"
                                                    onchange="LOAD_CERTIFICATION_DURATION(this.value, '<%= anticsrf%>');">
                                                    <%
                                                        sFristCerDurationOrProfileID = String.valueOf(rs[0][0].CERTIFICATION_PROFILE_ID);
                                                        CERTIFICATION_PROFILE[][] rsDuration = new CERTIFICATION_PROFILE[1][];
                                                        db.S_BO_CA_GET_DURATION_COMBOBOX(sFristCA, sFristCerPurpose, sessLanguageGlobal, rsDuration);
                                                        if (rsDuration[0].length > 0) {
                                                            for (int i = 0; i < rsDuration[0].length; i++) {
//                                                                sFristCerDurationOrProfileID = String.valueOf(rsDuration[0][0].ID);
                                                    %>
                                                    <option value="<%= String.valueOf(rsDuration[0][i].ID)%>" <%= rsDuration[0][i].ID == rs[0][0].CERTIFICATION_PROFILE_ID ? "selected" : "" %>><%= rsDuration[0][i].REMARK %></option>
                                                    <%
                                                            }
                                                        }
                                                    %>
                                                </select>
                                            </div>
                                            <input id="idHiddenCerCA" value="<%= sFristCA%>" style="display: none;"/>
                                            <input id="idHiddenCerCoreSubject" value="<%= sCACoreSubject%>" style="display: none;"/>
                                            <input id="idHiddenCerPurpose" value="<%= sFristCerPurpose%>" style="display: none;"/>
                                            <input id="idHiddenCerDurationOrProfileID" value="<%= sFristCerDurationOrProfileID%>" style="display: none;"/>
                                            <script>
                                                $(document).ready(function () {
                                                    $("#idViewDURATION_FREE").css("display", "none");
                                                    if('<%= sFristCA%>' !== "" && '<%= sFristCerPurpose%>' !== "" && '<%= sFristCerDurationOrProfileID%>' !== "")
                                                    {
//                                                        LOAD_CERTIFICATION_PROFILE('<= sFristCerDurationOrProfileID%>');
                                                    }
                                                });
                                            </script>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_amount_fee);</script></label>
                                                <input type="text" value="<%= strFEE_AMOUNT %>" name="FEE_AMOUNT" disabled id="FEE_AMOUNT" class="form-control123">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;" id="idViewDURATION_FREE">
                                                <label class="control-label123"><script>document.write(global_fm_date_free);</script></label>
                                                <input type="text" name="DURATION_FREE" disabled id="DURATION_FREE" class="form-control123">
                                            </div>
                                            <style>
                                                .form-control123[readonly]{background-color:#ffffff;opacity:1}
                                                .form-control123[disabled]{background-color:#eee;opacity:1}
                                            </style>
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;"></div>
                                            <fieldset class="scheduler-border">
                                                <legend class="scheduler-border"><script>document.write(global_fm_csr_info_cts);</script></legend>
                                                <div id="idViewCertInfo"></div>
                                            </fieldset>
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
                                                                    var vDNFromDB = document.getElementById("idHiddenDN").value;
                                                                    var localStoreRequired = new Array();
                                                                    var localStoreInput = new Array();
                                                                    var localStoreInputID_Info = "";
                                                                    var localStoreInputID_OnInput = "";
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
                                                                            var vContentButton_MST_Radio = "<div class='form-group' style='margin-bottom:0px;'>";
                                                                            var vContentButton_CMND_Radio = "<div class='form-group' style='margin-bottom:0px;'>";
                                                                            var vContentButton_MST_Text = "";
                                                                            var vContentButton_CMND_Text = "";
                                                                            var vContentButton_MST_Check = "";
                                                                            var vContentButton_CMND_Check = "";
                                                                            for (var j = 0; j < obj[i].RADIO_LIST.length; j++) {
                                                                                var vInputID = obj[i].RADIO_LIST[j].SubjectDNAttrCode + obj[i].RADIO_LIST[j].CertTemplateID;
                                                                                localStoreInput.push(vInputID + "###" + obj[i].RADIO_LIST[j].SubjectDNAttrCode + "###" + obj[i].RADIO_LIST[j].SubjectDNAttrPreFix
                                                                                    + "###" + obj[i].RADIO_LIST[j].SubjectDNAttrCNType + "###" + obj[i].RADIO_LIST[j].SubjectDNAttrDesc);
                                                                                if(obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS 
                                                                                    || obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
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
                                                                                            vContentButton_MST_Text = '<div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">'+
                                                                                                '<div style="width: 100%;padding-top: 5px;clear: both;">'+
                                                                                                '<div style="float: left;width: 86%;">' +
                                                                                                '<input class="form-control123" type="text" id="' + vInputID_Text + '" oninput="GetAlrmCertMSTMNS(this.value);" /></div>'+
                                                                                                '</div>' +
                                                                                                '<div style="float: right;text-align: right;">'+
                                                                                                '<input class="btn btn-info" style="width: 120px;" disabled id="btnGetInfoMST" value="'+global_fm_button_get_info+'" type="button"/>'+
                                                                                                '</div>' +
                                                                                                '</div>'+
                                                                                                '<div style="width: 100%;padding-top: 5px;">'+
                                                                                                '<label style="color: red;display:none;" id="idHintMSTMNS"></label>'+
                                                                                                '</div>'+
                                                                                                '</div>';
                                                                                        }
                                                                                        if(obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                                                                        {
                                                                                            var vInputID_Text = JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
                                                                                            vContentButton_MST_Text = '<div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">'+
                                                                                                '<div style="width: 100%;padding-top: 5px;clear: both;">'+
                                                                                                '<div style="float: left;width: 86%;">' +
                                                                                                '<input class="form-control123" type="text" id="' + vInputID_Text + '" oninput="GetAlrmCertMSTMNS(this.value);" /></div>'+
                                                                                                '</div>' +
                                                                                                '<div style="float: right;text-align: right;">'+
                                                                                                '<input class="btn btn-info" style="width: 120px;" id="btnGetInfoMST" value="'+global_fm_button_get_info+'" type="button" onclick="GetInfoMST();"/>'+
                                                                                                '</div>' +
                                                                                                '</div>'+
                                                                                                '<div style="width: 100%;padding-top: 5px;">'+
                                                                                                '<label style="color: red;display:none;" id="idHintMSTMNS"></label>'+
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
                                                                                        vContentButton_CMND_Text ='<div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">'+
                                                                                            '<input class="form-control123" oninput="GetAlrmCertCMNDHC(this.value);" type="text" id="' + vInputID_Text + '" />'+
                                                                                            '<label style="color: red;display:none;" id="idHintCMNDHC"></label>'+
                                                                                            '</div>';
                                                                                    }
                                                                                }
                                                                                else { }
                                                                            }
                                                                            if(vContentButton_MST_Radio !== "<div class='form-group' style='margin-bottom:0px;'>")
                                                                            {
                                                                                vContentButton_MST_Radio = vContentButton_MST_Radio + "</div>" + vContentButton_MST_Text;
                                                                                vContent = vContent + vContentButton_MST_Radio;
                                                                            }
                                                                            if(vContentButton_CMND_Radio !== "<div class='form-group' style='margin-bottom:0px;'>")
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
                                                                                vContent += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
                                                                                    '<label class="control-label123">' + obj[i].SubjectDNAttrDesc + '</label> ' +
                                                                                    vLabelRequired +
                                                                                    '<select class="form-control123" id="' + vInputID + '"></select>' +
                                                                                    '</div>';
                                                                                var sValuePushDB = JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE + "=" + obj[i].SubjectDNAttrPreFix;
                                                                                var indexTag = vDNFromDB.indexOf(sValuePushDB);
                                                                                if(indexTag !== -1)
                                                                                {
                                                                                    var vDataInputID = '<%= strCITY_PROVINCE_ID%>';
                                                                                    LoadDNCity(vInputID, vDataInputID);
                                                                                }
                                                                            }
                                                                            else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COUNTRY)
                                                                            {
                                                                                var valueCountry = '<%= sDN_Country%>';
                                                                                vContent += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
                                                                                    '<label class="control-label123">' + obj[i].SubjectDNAttrDesc + '</label> ' +
                                                                                    vLabelRequired +
                                                                                    '<input disabled class="form-control123" type="text" id="' + vInputID + '" value="'+valueCountry+'" />' +
                                                                                    '</div>';
                                                                            }
                                                                            else
                                                                            {
                                                                                vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                                                vContent += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
                                                                                    '<label class="control-label123">' + obj[i].SubjectDNAttrDesc + '</label> ' +
                                                                                    vLabelRequired +
                                                                                    '<input class="form-control123" type="text" id="' + vInputID + '" />' +
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
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        var vLabelRequired = "";
                                                                        var vInputID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID;
                                                                        if(vDNFromDB !== "")
                                                                        {
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
                                                                                            $("#" + idValueMST_MNS).val(sValuePushDBDataLast.replace(/###/g, ','));
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
                                                                                            $("#" + idValueCMND_HC).val(sValuePushDBDataLast.replace(/###/g, ','));
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
                                                                            } else {
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
                                                                                        $("#" + vInputID).val(sValuePushDBDataLast.replace(/###/g, ','));
                                                                                    } else {
                                                                                        if(vInputID.indexOf(JS_STR_COMPONENT_DN_VALUE_UID) !== -1)
                                                                                        {
                                                                                            vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                                                        }
                                                                                        var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                                                        var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                                                        $("#" + vInputID).val(sValuePushDBDataLast.replace(/###/g, ','));
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
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
                                                    $("#idHintMSTMNS").text('');
                                                    $("#idHintMSTMNS").css("display", "none");
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
                                                    $("#idHintCMNDHC").css("display", "none");
                                                    $("#idHintCMNDHC").text('');
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
                                                                            }
                                                                            if(idCheckEmptyID_Info === JS_STR_COMPONENT_DN_VALUE_LOCALITY)
                                                                            {
                                                                                $("#" + sSpace(sListInputCheckID_Info[i].split('###')[1])).val(obj[0].LOCALTION);
                                                                            }
                                                                        }
                                                                    } else if (obj[0].Code === JS_EX_NO_DATA) {
                                                                        funErrorAlert(global_no_data);
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
                                                function LOAD_CERTIFICATION_PURPOSE(objCA, objPurpose, idCSRF)
                                                {
                                                    $("#idHiddenCerPurpose").val(objPurpose);
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
                                                        }
                                                    });
                                                    return false;
                                                }
                                                function LOAD_CERTIFICATION_DURATION(objProfile, idCSRF)
                                                {
                                                    LOAD_CERTIFICATION_PROFILE(objProfile);
                                                }
                                                function OnBlurCompany(objInput, objValue)
                                                {
                                                    $("#"+objInput).val(objValue);
                                                }
                                            </script>
                                        </form>            
                                    </div>
                                    <%
                                                    }
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
                                </div>
                            </div>
                        </div>
                        <script>
                            $(document).ready(function () {
                                var vsCertTypeFrist = '<%= sFristCerDurationOrProfileID%>';
                                if(vsCertTypeFrist !== '')
                                {
                                    LoadFormSubjectDN(vsCertTypeFrist);
                                }
//                                $("select#CERTIFICATION_AUTHORITY").prop('selectedIndex', 0);
//                                LOAD_CERTIFICATION_AUTHORITY($("#CERTIFICATION_AUTHORITY").val(), '<= anticsrf%>');
                            });
                        </script>
                    </div>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
        </div>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../style/custom.min.js"></script>
        <script src="../js/moment.min_limit.js"></script>
        <script src="../js/daterangepicker_limit.js"></script>
        <link href="../js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
        <script src="../js/checkphone/intlTelInput.js" type="text/javascript"></script>
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