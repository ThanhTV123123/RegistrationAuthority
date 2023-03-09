<%-- 
    Document   : CertView
    Created on : Jul 27, 2018, 11:42:16 AM
    Author     : THANH-PC
--%>

<%@page import="vn.ra.process.RSSPProcessCommon"%>
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
        <script type="text/javascript" src="../Css/GlobalAlert.js?t=<%=sNewRefreshJS%>"></script>
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <title></title>
        <script type="text/javascript">
            $(document).ready(function () {
                $('.loading-gif').hide();
                localStorage.setItem("TransferPage", null);
            });
            function printex() {
                $('#contentPrintCert1').printArea({
                    popWd: 750,
                    popHt: 900,
                    mode: "popup",
                    popClose: false
                });
            }
            function PrintPreview(vText) {
                var popupWin = window.open('', '_blank', 'width=850,height=900,location=no,left=200px');
                popupWin.document.open();
                popupWin.document.write('<html><title></title><link rel="stylesheet" type="text/css" href="Print.css" media="screen"/></head><body onload="window.print()">');
                popupWin.document.write(vText);
                popupWin.document.write('</html>');
                popupWin.document.close();
            }
            function popupPrintCert(id, idCSRF)
            {
                $.ajax({
                    type: "post",
                    url: "../PrintFormCommon",
                    data: {
                        idParam: 'printcert',
                        id: id,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('###');
                        if (myStrings[0] === "0")
                        {
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
                            if (myStrings[1] === "1") {
                                funErrorAlert(certprofile_exists_code);
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
            function popupPrintHandover(id)
            {
                localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", id);
                localStorage.setItem("TransferPage", "detail");
                window.location = "PrintHandover.jsp?id="+ id;
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
            function popupListCertDecline(attrId, branchId, userId)
            {
                $('#myModalListCertDecline').modal('show');
                $('#contentCertDecline').empty();
                $('#contentCertDecline').load('IncludeCertDecline.jsp', {attrId: attrId, branchId: branchId, userId: userId}, function () {
                });
                $(".loading-gifCertDecline").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
//            function ViewTempFile(vFILE_MANAGER_ID) {
//                var popupWin = window.open('CertificateFileView.jsp?idFile='+vFILE_MANAGER_ID, '_blank', 'width=900,height=750,location=no,left=200px');
//                popupWin.focus();
////                window.open('CertificateFileView.jsp?idFile='+vFILE_MANAGER_ID, '_blank');
//            }
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
            @media (min-width: 768px){.modal-dialog{width: 900px;}}
            .modal-header{
                padding-right: 10px; padding-top: 10px;
            }
        </style>
    </head>
    <body>
        <%         
            if (session.getAttribute("sUserID") != null) {
            String strCERTIFICATION_PROFILE_ID = "";
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
            String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
            String SessRoleID = session.getAttribute("RoleID_ID").toString().trim();
            String allowTwoOU = cogCommon.GetPropertybyCode(Definitions.CONFIG_ALLOW_OU_COMPONENT_TWO_TOUP);
            String agentEditContact = cogCommon.GetPropertybyCode(Definitions.CONFIG_AGENCY_EDIT_CONTACT_ENABLED);
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <%                                        CERTIFICATION[][] rs = new CERTIFICATION[1][];
            String strCertificate = "";
            String strIsCert = "0";
            String sMaxLengthFile = cogCommon.GetPropertybyCode(Definitions.CONFIG_JACK_RABBIT_MAX_LENGTH_FILE).trim();
            session.setAttribute("sessUploadFileCert", null);
            try {
                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                if (EscapeUtils.IsInteger(ids) == true) {
                    db.S_BO_CERTIFICATION_DETAIL(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                    if (rs[0].length > 0) {
                        String sOWNER_ID = String.valueOf(rs[0][0].CERTIFICATION_OWNER_ID);
                        boolean isAccessAgencyPage = true;
                        if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            if(rs[0][0].SHARED_MODE == false) {
                                BRANCH[][] branchAccess = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                                isAccessAgencyPage = CommonFunction.checkBranchTreeInvalidCert(rs[0][0].BRANCH_ID, branchAccess);
                            }
                        }
                        if (isAccessAgencyPage == true) {
                            int intPKI_FORMFACTOR_ID = rs[0][0].PKI_FORMFACTOR_ID;
                            String sTOKEN_SN = EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN);
                            String sCERTIFICATION_SN = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_SN);
                            String strEMAIL_CONTRACT = EscapeUtils.CheckTextNull(rs[0][0].EMAIL_CONTRACT);
                            String strPHONE_CONTRACT = EscapeUtils.CheckTextNull(rs[0][0].PHONE_CONTRACT);
                            String strEMAIL_CONTRACTReal = EscapeUtils.CheckTextNull(rs[0][0].EMAIL_CONTRACT_REAL);
                            String strPHONE_CONTRACTReal = EscapeUtils.CheckTextNull(rs[0][0].PHONE_CONTRACT_REAL);
                            String strCERTIFICATION_STATE_DESC = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_STATE_DESC);
                            String strCERTIFICATION_ATTR_STATE_DESC = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_ATTR_STATE_DESC);
                            strCertificate = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION);
                            int intCERTIFICATION_PURPOSE_ID = rs[0][0].CERTIFICATION_PURPOSE_ID;
                            if(!"".equals(strCertificate))
                            {
                                strIsCert = "1";
                            }
                            boolean isP12 = false;
                            if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                            {
                                    if(rs[0][0].PRIVATE_KEY_ENABLED == true)
                                    {
                                        isP12 = true;
                                        strIsCert = "0";
                                    }
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
                            String sACTIVATION_CODE =EscapeUtils.CheckTextNull(rs[0][0].ACTIVATION_CODE);
                            String sACTIVATION_EXPIRATION_DT =EscapeUtils.CheckTextNull(rs[0][0].ACTIVATION_EXPIRATION_DT);
                            SessionDNSName cartToken = null;
                            String sDiscountRateOption = "0";
                            String sRegexPolicy = "";
                            String sArrayFileExten = "";
                            GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                            if (sessGeneralPolicy[0].length > 0) {
                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                {
                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION))
                                    {
                                        sDiscountRateOption = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
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
                            String sPROPERTIES = EscapeUtils.CheckTextNull(rs[0][0].PROPERTIES);
                            String sSanDataDB = "";
                            if(!"".equals(sPROPERTIES))
                            {
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
                            String isViewDeleteFileEnable = "1";
                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_EFY)) {
                                if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                    if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED
                                        || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_NEW) {
                                        isViewDeleteFileEnable = "0";
                                    }
                                }
                            }
                            String isRSSPAccess = "0";
                            if((intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD
                                || intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_REMOTE_SIGNING)
                                && (isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_FPT) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_MID)
                                    || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_CMC))) {
                                isRSSPAccess = "1";
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
            <script>$("#idLblTitleEdits").text(certlist_title_detail);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <%  
                        boolean isEditContact = false;
                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                 || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                            {
                                if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_NEW
                                    || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED)
                                {
                                    isEditContact = true;
                                }
                            }
                        } else {
                            if("1".equals(agentEditContact)) {
                                if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                {
                                    if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_NEW
                                        || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED)
                                    {
                                        isEditContact = true;
                                    }
                                }
                            }
                        }
                        if(isEditContact == true) {
                    %>
                    <input id="btnContractUpdate" class="btn btn-info" type="button" onclick="popupContractUpdate('<%=ids%>', '<%= anticsrf%>');" />
                    <script>
                        document.getElementById("btnContractUpdate").value = global_fm_button_edit;
                        function popupContractUpdate(id, idCSRF) {
                            if (!JSCheckEmptyField($("#PHONE_CONTRACT").val())) {
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
                                funErrorAlert(policy_req_empty + global_fm_email_contact);
                                return false;
                            } else {
                                if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#EMAIL_CONTRACT").val()))
                                {
                                    $("#EMAIL_CONTRACT").focus();
                                    funErrorAlert(global_req_mail_format);
                                    return false;
                                }
                            }
                            var sPhoneContactReal = "";
                            var sEmailContactReal = "";
                            if ('<%=SessAgentID%>' === JS_STR_AGENT_ROOT && '<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_ICA) {
                                sPhoneContactReal = $("#PHONE_CONTRACT_REAL").val();
                                if (JSCheckEmptyField(sPhoneContactReal)) {
                                    if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#PHONE_CONTRACT_REAL")))
                                    {
                                        $("#PHONE_CONTRACT_REAL").focus();
                                        funErrorAlert(global_req_phone_format);
                                        return false;
                                    }
                                }
                                sEmailContactReal = $("#EMAIL_CONTRACT_REAL").val();
                                if (JSCheckEmptyField(sEmailContactReal)) {
                                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#EMAIL_CONTRACT_REAL").val()))
                                    {
                                        $("#EMAIL_CONTRACT_REAL").focus();
                                        funErrorAlert(global_req_mail_format);
                                        return false;
                                    }
                                }
                            }
                            var idSIGNING_PROFILES = "";
                            var idRSSPAccess = '<%=isRSSPAccess%>';
                            if(idRSSPAccess === "1") {
                                idSIGNING_PROFILES = $("#SIGNING_PROFILES").val();
                            }
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../ReqApproveDeclineCommon",
                                data: {
                                    idParam: 'editcontractcert',
                                    id: id,
                                    idPHONE_CONTRACT: $("#PHONE_CONTRACT").val(),
                                    idEMAIL_CONTRACT: $("#EMAIL_CONTRACT").val(),
                                    SIGNING_PROFILES: idSIGNING_PROFILES,
                                    sPhoneContactReal: sPhoneContactReal,
                                    sEmailContactReal: sEmailContactReal,
                                    CsrfToken: idCSRF
                                },
                                cache: false,
                                success: function (html)
                                {
                                    var myStrings = sSpace(html).split('#');
                                    if (myStrings[0] === "0") {
                                        funSuccLocalAlert(inputcertlist_succ_edit);
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
                    <%   
                        }
                    %>
                    <%
                        if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_DECLINED
                            && rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION)
                        {
                    %>
                    <input id="btnReRegister" class="btn btn-info" type="button" onclick="popupReRegister('<%=ids%>', '<%=anticsrf%>');" />
                    <script>
                        document.getElementById("btnReRegister").value = global_fm_button_re_regis;
                        function popupReRegister(id, idCSRF)
                        {
                            $.ajax({
                                type: "post",
                                url: "../TokenCommon",
                                data: {
                                    idParam: 'checkreregistercert',
                                    id: id,
                                    CsrfToken: idCSRF
                                },
                                cache: false,
                                success: function (html)
                                {
                                    var myStrings = sSpace(html).split('#');
                                    if (myStrings[0] === "0") {
                                        window.location = "RegisterCertificate.jsp?type=re-register";
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
                    
                    <%
                        String SessLevelBranch = session.getAttribute("sessLevelBranch").toString().trim();
                        ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Cert");
                        String sessTreeArrayBranchID = session.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                        String sCERTIFICATION_ATTR_ID = String.valueOf(rs[0][0].CERTIFICATION_ATTR_ID);
                        if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                            || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT)
                        {
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
                    <input id="btnDeclineDetail" class="btn btn-info" type="button" onclick="popupListCertDecline('<%=sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(rs[0][0].BRANCH_ID)%>', '<%= String.valueOf(rs[0][0].CREATED_BY_ID)%>');" />
                    <script>document.getElementById("btnDeclineDetail").value = global_fm_button_decline;</script>
                    <%
                        }
                    %>
                    <%
                        } else if(rs[0][0].CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_COMMITED
                            && rs[0][0].CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_DECLINED
                            && rs[0][0].CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED) {
                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED
                                    && rs[0][0].CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                                    && rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVOKED)
                                { } else {
                    %>
                    <%
                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                        {
                    %>
                    <input id="btnDeclineDetail" class="btn btn-info" type="button" onclick="popupListCertDecline('<%=sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(rs[0][0].BRANCH_ID)%>', '<%= String.valueOf(rs[0][0].CREATED_BY_ID)%>');" />
                    <script>document.getElementById("btnDeclineDetail").value = global_fm_button_decline;</script>
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
                    <input id="btnDeclineDetail" class="btn btn-info" type="button" onclick="popupListCertDecline('<%=sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(rs[0][0].BRANCH_ID)%>', '<%= String.valueOf(rs[0][0].CREATED_BY_ID)%>');" />
                    <!--<span><a style="cursor: pointer;" id="btnDeclineDetail" onclick="" class="btn btn-info btn-xs"><i class="fa fa-remove"></i> </a></span>-->
                    <script>document.getElementById("btnDeclineDetail").value = global_fm_button_decline;</script>
                    <%
                                }
                            }
                        }
                    %>
                    <%
                        String remainingSigningCounter = "";
                        String userNameRSSP = "";
                        String signingProfileRSSP = "";
                        String agreementUUID = "";
                        String relyingPartyName = "";
                        String sRSSP_ACCESS_ENABLED = cogCommon.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                        if(rs[0][0].PKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD
                            && (isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_FPT) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_MID)
                            || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_CMC))) {
                            PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                            db.S_BO_PKI_FORMFACTOR_DETAIL(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD), rsFormfactorPro);
                            String sFormFactorPro = "";
                            if(rsFormfactorPro[0].length > 0) {
                                String sVALUE = EscapeUtils.CheckTextNull(rs[0][0].VALUE);
                                if(!"".equals(sVALUE)) {
                                    sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                    CredentialDataAuthen credentialAuthen = CommonFunction.loadCredentialDataAuthen(sFormFactorPro);
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    ATTRIBUTE_VALUES valueRssp = objectMapper.readValue(sVALUE, ATTRIBUTE_VALUES.class);
                                    agreementUUID = EscapeUtils.CheckTextNull(valueRssp.getRsspAgreementUUID());
                                    relyingPartyName = EscapeUtils.CheckTextNull(valueRssp.getRsspRelyingParty());
                                    String[] sParam = new String[6]; int[] sCode = new int[1];
                                    RSSPProcessCommon rsspClass = new RSSPProcessCommon();
                                    rsspClass.getCertificateDetailForSignCloud(credentialAuthen, relyingPartyName, agreementUUID, sCERTIFICATION_SN, sParam, sCode);
                                    if(sCode[0] == 0) {
                                        signingProfileRSSP = sParam[3];
                                        if(!"".equals(EscapeUtils.CheckTextNull(sParam[1]))){
                                            remainingSigningCounter = sParam[1];
                                            /*if(!"".equals(EscapeUtils.CheckTextNull(sParam[4]))){
                                                remainingSigningCounter = remainingSigningCounter + "/" + sParam[4];
                                            }*/
                                        }
                                        if(remainingSigningCounter.equals("-1")) {
                                            remainingSigningCounter = "UNLIMITED";
                                        }
                                        userNameRSSP = sParam[4];
                                        if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED
                                            || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWED
                                            || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVISED)
                                        {
                                            if(sParam[2].equals(Definitions.CONFIG_RSSP_AGREEMENT_STATE_ID_OPERATED)
                                                || sParam[2].equals(Definitions.CONFIG_RSSP_AGREEMENT_STATE_ID_RENEWED)
                                                || sParam[2].equals(Definitions.CONFIG_RSSP_AGREEMENT_STATE_ID_REVISED)
                                                || sParam[2].equals(Definitions.CONFIG_RSSP_AGREEMENT_STATE_ID_PRE_OPERATED)) {
                    %>
                    <input id="btnRSSPLock" class="btn btn-info" type="button" onclick="popupRSSPLock('<%=relyingPartyName%>', '<%=agreementUUID%>', '<%=sCERTIFICATION_SN%>', '<%=anticsrf%>');" />
                    <script>
                        document.getElementById("btnRSSPLock").value = global_fm_lock;
                        function popupRSSPLock(relyingPartyName, agreementUUID, certSN, idCSRF)
                        {
                            swal({
                                title: "",
                                text: token_confirm_lock_temp ,
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
                                $.ajax({
                                    type: "post",
                                    url: "../CertProfileCommon",
                                    data: {
                                        idParam: 'rsspcertificatelock',
                                        relyingPartyName: relyingPartyName,
                                        agreementUUID: agreementUUID,
                                        certSN: certSN,
                                        CsrfToken: idCSRF
                                    },
                                    cache: false,
                                    success: function (html)
                                    {
                                        var myStrings = sSpace(html).split('#');
                                        if (myStrings[0] === "0") {
                                            funSuccLocalAlert(cert_succ_edit);
                                        } else {
                                            funErrorAlert(myStrings[1]);
                                        }
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                    }
                                });
                                return false;
                            });
                        }
                    </script>
                    <%
                                } else if(sParam[2].equals(Definitions.CONFIG_RSSP_AGREEMENT_STATE_ID_BLOCKED)){
                    %>
                    <input id="btnRSSPUnLock" class="btn btn-info" type="button" onclick="popupRSSPUnLock('<%=relyingPartyName%>', '<%=agreementUUID%>', '<%=sCERTIFICATION_SN%>', '<%=anticsrf%>');" />
                    <script>
                        document.getElementById("btnRSSPUnLock").value = global_button_grid_unlock;
                        function popupRSSPUnLock(relyingPartyName, agreementUUID, certSN, idCSRF)
                        {
                            swal({
                                title: "",
                                text: token_confirm_unlock_temp ,
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
                                $.ajax({
                                    type: "post",
                                    url: "../CertProfileCommon",
                                    data: {
                                        idParam: 'rsspcertificateunlock',
                                        relyingPartyName: relyingPartyName,
                                        agreementUUID: agreementUUID,
                                        certSN: certSN,
                                        CsrfToken: idCSRF
                                    },
                                    cache: false,
                                    success: function (html)
                                    {
                                        var myStrings = sSpace(html).split('#');
                                        if (myStrings[0] === "0") {
                                            funSuccLocalAlert(cert_succ_edit);
                                        } else {
                                            funErrorAlert(myStrings[1]);
                                        }
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                    }
                                });
                                return false;
                            });
                        }
                    </script>
                    <%
                                            }
                                            if(sParam[2].equals(Definitions.CONFIG_RSSP_AGREEMENT_STATE_ID_OPERATED)
                                                || sParam[2].equals(Definitions.CONFIG_RSSP_AGREEMENT_STATE_ID_RENEWED)
                                                || sParam[2].equals(Definitions.CONFIG_RSSP_AGREEMENT_STATE_ID_REVISED)
                                                || sParam[2].equals(Definitions.CONFIG_RSSP_AGREEMENT_STATE_ID_PRE_OPERATED)) {
                    %>
                    <input id="btnRSSPResetPasscode" class="btn btn-info" type="button" onclick="popupRSSPResetPasscode('<%=relyingPartyName%>', '<%=agreementUUID%>', '<%=sCERTIFICATION_SN%>', '<%=anticsrf%>');" />
                    <script>
                        document.getElementById("btnRSSPResetPasscode").value = global_button_grid_resetpasscode;
                        function popupRSSPResetPasscode(relyingPartyName, agreementUUID, certSN, idCSRF)
                        {
                            swal({
                                title: "",
                                text: token_confirm_resetpasscode_temp ,
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
                                $.ajax({
                                    type: "post",
                                    url: "../CertProfileCommon",
                                    data: {
                                        idParam: 'rsspcertificatereserpasscode',
                                        relyingPartyName: relyingPartyName,
                                        agreementUUID: agreementUUID,
                                        certSN: certSN,
                                        CsrfToken: idCSRF
                                    },
                                    cache: false,
                                    success: function (html)
                                    {
                                        var myStrings = sSpace(html).split('#');
                                        if (myStrings[0] === "0") {
                                            funSuccLocalAlert(cert_succ_edit);
                                        } else {
                                            funErrorAlert(myStrings[1]);
                                        }
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                    }
                                });
                                return false;
                            });
                        }
                    </script>
                    <%
                                            }
                                        }
                                    }
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
                        if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVOKED){
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
                    %>
                    <%
                        if(!"".equals(sCERTIFICATION_SN))
                        {
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCertSN" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="CERTIFICATION_SN" readonly value="<%= sCERTIFICATION_SN%>" class="form-control123">
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
                                <input type="text" readonly name="CERTIFICATION_STATE_DESC" value="<%= strCERTIFICATION_STATE_DESC%>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleCertState").text(global_fm_Status_cert);</script>
                    </div>
                    <%
                        if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_PERMANENT_DISABLE
                            || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                            || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_PERMANENT_DISABLE
                            || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE)
                        {
                            String sReasonSuspendValue = "";
                            String sCOMMENT = EscapeUtils.CheckTextNull(rs[0][0].COMMENT);
                            if(!"".equals(sCOMMENT)) {
                                ObjectMapper oMapperParse = new ObjectMapper();
                                CERTIFICATION_COMMENT itemParsePush = oMapperParse.readValue(sCOMMENT, CERTIFICATION_COMMENT.class);
                                sReasonSuspendValue = itemParsePush.certificateSuspendReason;
                            }
                            if(!"".equals(sReasonSuspendValue)) {
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleSuspendReason" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" id="SuspendReason" name="SuspendReason" value="<%= sReasonSuspendValue%>" class="form-control123" disabled />
                            </div>
                        </div>
                        <script>$("#idLblTitleSuspendReason").text(global_fm_suspend_desc);</script>
                    </div>
                    <%
                        }
                        if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                            || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE)
                        {
                            String sRELEASE_DT = EscapeUtils.CheckTextNull(rs[0][0].RELEASE_DT);
                            if(!"".equals(sRELEASE_DT)){
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleSuspendTime" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="SuspendTime" id="SuspendTime" value="<%=sRELEASE_DT%>" readonly class="form-control123"/>
                            </div>
                        </div>
                        <script>$("#idLblTitleSuspendTime").text(global_fm_times_recovery);</script>
                    </div>
                    <%
                                }
                            }
                        }
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCertAttrState" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly name="CERTIFICATION_ATTR_STATE_DESC" value="<%= strCERTIFICATION_ATTR_STATE_DESC%>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleCertAttrState").text(global_fm_Status_request);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCertAttrType" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="CERTIFICATION_ATTR_TYPE" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_ATTR_TYPE_DESC) %>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleCertAttrType").text(global_fm_requesttype);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCA" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly id="CERTIFICATION_AUTHORITY" value="<%= EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_AUTHORITY_DESC) %>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleCA").text(global_fm_ca);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCertPurpose" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly id="CERTIFICATION_PURPOSE" value="<%= EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_PURPOSE_DESC) %>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleCertPurpose").text(global_fm_certpurpose);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleFormFactor" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly id="PKI_FORMFACTOR_DETAIL" value="<%= EscapeUtils.CheckTextNull(rs[0][0].PKI_FORMFACTOR_DESC) %>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleFormFactor").text(global_fm_Method);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCertDuration" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly id="DURATION" value="<%= EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_PROFILE_DESC) %>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleCertDuration").text(global_fm_duration_cts);</script>
                    </div>
                    <%
                        if("1".equals(sDiscountRateOption))
                        {
                            String sDisplayNoneDiscountRate = "none";
                            /*if (SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                            {
                                if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                    || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                    || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                                {
                                    sDisplayNoneDiscountRate = "";
                                }
                            }*/
                    %>
                    <div class="col-sm-6" style="padding-left: 0; display: <%= sDisplayNoneDiscountRate%>">
                        <div class="form-group">
                            <label id="idLblTitleCertDiscountRate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly id="DiscountRate" value="<%= String.valueOf(rs[0][0].DISCOUNT_RATE) %>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleCertDiscountRate").text(global_fm_percent_cts);</script>
                    </div>
                    <%
                        }
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleFeeAmount" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="strFEE_AMOUNT" value="<%= strFEE_AMOUNT%>" readonly class="form-control123"/>
                            </div>
                        </div>
                        <script>$("#idLblTitleFeeAmount").text(global_fm_amount_fee);</script>
                    </div>
                    <%
                        if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD
                            || intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_REMOTE_SIGNING) {
                            String sVALUE = rs[0][0].VALUE;
                            if(!"".equals(sVALUE))
                            {
                                ObjectMapper objectMapper = new ObjectMapper();
                                ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE, ATTRIBUTE_VALUES.class);
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleAgreementUUID" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="strAgreementUUID" value="<%= EscapeUtils.CheckTextNull(valueATTR_Frist.getRsspAgreementUUID())%>" readonly class="form-control123"/>
                            </div>
                        </div>
                        <script>$("#idLblTitleAgreementUUID").text(global_fm_uuid_agreement);</script>
                    </div>
                    <%
                            }
                        }
                    %>
                    <%
                        if("1".equals(isRSSPAccess)) {
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleremainingSigning" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="strRemainingSigning" value="<%= remainingSigningCounter%>" readonly class="form-control123"/>
                            </div>
                        </div>
                        <script>$("#idLblTitleremainingSigning").text(global_fm_remainingSigning_agreement);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleUsernameRSSP" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="strUsernameRSSP" value="<%= userNameRSSP%>" readonly class="form-control123"/>
                            </div>
                        </div>
                        <script>$("#idLblTitleUsernameRSSP").text(global_fm_Username_esigncloud);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleSigningProfile" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <select id="SIGNING_PROFILES" name="SIGNING_PROFILES" class="form-control123">
                                    <option value="">---</option>
                                </select>
                            </div>
                        </div>
                        <script>$("#idLblTitleSigningProfile").text(global_fm_rssp_signning_profiles);</script>
                    </div>
                    <script>
                        function LOAD_RSSP_SIGNINGPROFILES(rsspEnabled, defaultSigningProfile) {
                            if(rsspEnabled === "1") {
                                $.ajax({
                                    type: "post",
                                    url: "../JSONCommon",
                                    data: {
                                        idParam: 'loadrsspsigningprofileslist'
                                    },
                                    cache: false,
                                    success: function (html)
                                    {
                                        if (html.length > 0) {
                                            var cbxUSER = document.getElementById("SIGNING_PROFILES");
                                            console.log("defaultSigningProfile: " +defaultSigningProfile);
                                            removeOptions(cbxUSER);
                                            var obj = JSON.parse(html);
                                            if (obj[0].Code === "0") {
                                                for (var i = 0; i < obj.length; i++) {
                                                    cbxUSER.options[cbxUSER.options.length] = new Option(obj[i].NAME, obj[i].NAME);
                                                    if(defaultSigningProfile === obj[i].NAME) {
                                                        $("#SIGNING_PROFILES option[value='" + defaultSigningProfile + "']").attr("selected", "selected");
                                                    }
                                                }
                                            }
                                            else if (obj[0].Code === JS_EX_CSRF) {
                                                funCsrfAlert();
                                            }
                                            else if (obj[0].Code === JS_EX_LOGIN) {
                                                RedirectPageLoginNoSess(global_alert_login);
                                            }
                                            else if (obj[0].Code === JS_EX_ANOTHERLOGIN) {
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
                        $(document).ready(function () {
                            if('<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_CMC){
                                $('#SIGNING_PROFILES').attr('disabled', true);
                            }
                            LOAD_RSSP_SIGNINGPROFILES('<%= sRSSP_ACCESS_ENABLED%>', '<%= signingProfileRSSP%>');
                        });
                    </script>
                    <%
                        }
                    %>
                    <%
                        if(!"".equals(EscapeUtils.CheckTextNull(rs[0][0].COMMENT)) && isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                            ObjectMapper oMapperParse = new ObjectMapper();
                            CERTIFICATION_COMMENT itemParsePush = oMapperParse.readValue(EscapeUtils.CheckTextNull(rs[0][0].COMMENT), CERTIFICATION_COMMENT.class);
                            String sDeclineReason = itemParsePush.certificateDeclineReason;
                            if(!"".equals(sDeclineReason)){
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <%
                                if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED) {
                            %>
                            <label id="idLblTitleDesc_Decline" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;color: red;"></label>
                            <script>$("#idLblTitleDesc_Decline").text(global_fm_Note);</script>
                            <%
                                } else {
                            %>
                            <label id="idLblTitleDesc_Decline" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;color: red;"></label>
                            <script>$("#idLblTitleDesc_Decline").text(global_fm_decline_desc);</script>
                            <%
                                }
                            %>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="DESC_DECLINE_VIEW" id="DESC_DECLINE_VIEW" readonly 
                                    value="<%= sDeclineReason%>" class="form-control123"/>
                            </div>
                        </div>
                    </div>
                    <%
                            }
                        }
                    %>
                    <%
                        boolean isHasFileOfUser = true;
                        FILE_MANAGER[][] rsFileMana = new FILE_MANAGER[1][];
                        db.S_BO_FILE_MANAGER_GET_BY_CERTIFICATION_AND_OWNER(EscapeUtils.escapeHtml(ids), sOWNER_ID, sessLanguageGlobal, rsFileMana);
                        if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            if(rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_NEW) {
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
                        if(rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED)
                        {
                            if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_NEW) {
                                
                                } else {
                                    if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
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
                            } else {
                                if(rsFileMana[0].length <= 0)
                                {
                                    //isHasFileOfUser = false;
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
                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_EFY)) {
                            if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                isHasFileOfUser = true;
                            }
                        }
                    %>
                    <%
                        if(isHasFileOfUser == true) {
                    %>
                    <%
                        CERTIFICATION_POLICY_DATA[][] resCollectData =null;
                        if(intPKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD
                            && intPKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_REMOTE_SIGNING)
                        {
                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO)) {
                                CERTIFICATION[][] rsBrief = new CERTIFICATION[1][];
                                db.S_BO_CERTIFICATION_BRIEF_DETAIL(String.valueOf(rs[0][0].ID), rsBrief);
                                String sBRIEF_PROPERTIES = "";
                                if(rsBrief[0].length > 0)
                                {
                                    if(!"".equals(sBRIEF_PROPERTIES))
                                    {
                                        CERTIFICATION_POLICY_DATA[][] resIPData = new CERTIFICATION_POLICY_DATA[1][];
                                        CommonFunction.getCollectedBriefProperties(sBRIEF_PROPERTIES, resIPData);
                                        if(resIPData[0].length > 0)
                                        {
                                            request.getSession(false).setAttribute("SessCollectedBriefPro", resIPData);
                                        }
                                    }
                                }
                                resCollectData = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessCollectedBriefPro");
                            }
                        }
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
                            if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED
                                || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWED
                                || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_STOPPED_OPERATION
                                || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWED_EXPIRED)
                            {
                                if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                {
                                    isEnableUploadFile = true;
                                }
                            } else {
                                if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_NEW) {
                                    isEnableUploadFile = true;
                                }
                            }
                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_EFY)) {
                                if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                    if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED
                                        || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_NEW) {
                                        isEnableUploadFile = true;
                                    }
                                }
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
                                        boolean booCheckCollect = CommonFunction.checkBriefFileType(sName, resCollectData);
                    %>
                    <fieldset class="scheduler-border" style="clear: both;">
                        <legend class="scheduler-border"><%= sRemark%></legend>
                        <%
                            String cssDelete = "";
                            String cssDown = "";
                            String sClassFileLabel = "col-sm-2";
                            String sClassFileUp = "col-sm-10";
                            if("1".equals(sRepresentEnabled)){
                                sClassFileLabel = "col-sm-3";
                                sClassFileUp = "col-sm-9";
                                cssDelete = "text-align: center;width: 80px;";
                                cssDown = "text-align: center;width: 80px;";
                            }
                            if(isEnableUploadFile == true) {
                                String displayAttachFile = "";
                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                    if(booCheckCollect == true){
                                        displayAttachFile = "display:none;";
                                    }
                                }
                        %>
                        <div class="col-sm-13" style="padding-left: 0;<%=displayAttachFile%>">
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
                        <div style="padding: 10px 0 0px 0;" id="idDiv<%= sName%>" class="table-responsive">
                            <table id="idTable<%= sName%>" class="table table-striped projects" style="margin-bottom: 10px;">
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
                                            <a id="idLblTitleTableLinkDown<%= sNameInID_inner%>" class="btn btn-info btn-xs" style="cursor: pointer;<%=cssDown%>;<%="0".equals(isViewActionFileEnable) ? "display: none;" : "" %>" onclick="DownloadTempFile('<%= mhIP.FILE_MANAGER_ID%>', '<%= anticsrf%>');"></a>
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
                                                if(isEnableUploadFile == true && "1".equals(isViewDeleteFileEnable)) {
                                            %>
                                            <a id="idLblTitleTableLinkDelete<%= sNameInID_inner%>" class="btn btn-info btn-xs" style="cursor: pointer;<%=cssDelete%>;<%="0".equals(isViewActionFileEnable) && mhIP.FILE_MANAGER_ID != 0 ? "display: none;" : "" %>" onclick="DeleteTempFile('<%= mhIP.FILE_PROFILE%>', '<%= mhIP.FILE_NAME%>', '<%= mhIP.FILE_MANAGER_ID %>');">
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
                                                    || sFileExtend.equals("JPG") || sFileExtend.equals("JPEG")) {
                                            %>
                                            &nbsp;<a id="idLblTitleTableLinkView<%= sNameInID_inner%>" style="cursor: pointer;<%=cssDelete%>;<%="0".equals(isViewActionFileEnable) ? "display: none;" : "" %>" target="_blank" class="btn btn-info btn-xs" onclick="ViewTempFile('<%= mhIP.FILE_MANAGER_ID%>');"></a>
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
                                                        var iconDown = '<i class="fa fa-pencil"></i> ' + global_fm_down;
                                                        var iconReview = '<i class="fa fa-pencil"></i> ' + global_fm_view;
                                                        var cssDelete = '';
                                                        var cssReview = '';
                                                        var representEnabled = '<%=sRepresentEnabled%>';
                                                        var viewActionFileEnable = '<%=isViewActionFileEnable%>';
                                                        var viewDeleteFileEnable = '<%=isViewDeleteFileEnable%>';
                                                        var sActionFileEnable = "";
                                                        if(viewActionFileEnable === "0"){
                                                            sActionFileEnable = "display: none";
                                                        }
                                                        if(representEnabled === "1"){
                                                            iconDelete = ' ' + global_fm_button_delete;
                                                            iconDown = ' ' + global_fm_down;
                                                            cssDelete = 'text-align: center;width: 80px;';
                                                            iconReview = ' ' + global_fm_view;
                                                            cssReview = 'text-align: center;width: 80px;';
                                                        }
                                                        input1.value = '';
                                                        $("#idTBody" + idType).empty();
                                                        var content = "";
                                                        for (var i = 0; i < obj.length; i++) {
                                                            if(obj[i].FILE_PROFILE === idType)
                                                            {
                                                                var fileNameLoad = obj[i].FILE_NAME;
                                                                var sActionDelete = '<a class="btn btn-info\n\
                                                                    btn-xs" style="cursor: pointer;'+cssDelete+'" onclick="DeleteTempFile(\'' + obj[i].FILE_PROFILE + '\', \'' + fileNameLoad + '\', \'' + obj[i].FILE_MANAGER_ID + '\');">' + iconDelete + '</a>';
                                                                var sReviewCRL = "";
                                                                if(obj[i].FILE_PROFILE === JS_STR_FILE_PROFILE_CODE_E_CONTRACT) {
                                                                    sActionDelete = "";
                                                                }
                                                                if(obj[i].FILE_MANAGER_ID !== 0 && viewActionFileEnable === "0") {
                                                                    sActionDelete = "";
                                                                }
                                                                if(viewDeleteFileEnable === "0"){
                                                                    sActionDelete = "";
                                                                }
                                                                var fileNameExt = fileNameLoad.substring(fileNameLoad.lastIndexOf('.')+1);
                                                                if(fileNameExt.toUpperCase() === "PDF" || fileNameExt.toUpperCase() === "GIF"  || fileNameExt.toUpperCase() === "JPEG"
                                                                            || fileNameExt.toUpperCase() === "JPG" || fileNameExt.toUpperCase() === "PNG"){
                                                                    sReviewCRL = '<a class="btn btn-info\n\
                                                                        btn-xs" style="cursor: pointer;'+cssReview+';'+sActionFileEnable+'" onclick="ViewTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + fileNameLoad + '\');">' + iconReview + '</a>';
                                                                }
                                                                var sActionDown = '<a class="btn btn-info\n\
                                                                    btn-xs" style="cursor: pointer;'+cssDelete+';'+sActionFileEnable+'" onclick="DownloadTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + <%= anticsrf%> + '\');">' + iconDown + '</a>';
                                                                content += "<tr>" +
                                                                    "<td>" + obj[i].Index + "</td>" +
                                                                    "<td>" + fileNameLoad + "</td>" +
                                                                    "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                    "<td>" + sActionDown + ' ' + sActionDelete + ' ' + sReviewCRL +"</td>" +
                                                                    "</tr>";
                                                           }
                                                        }
                                                        $("#idTBody" + idType).append(content);
                                                        $("#idDiv" + idType).css("display", "");
                                                    }
                                                    else if (obj[0].Code === JS_EX_CSRF) {
                                                        funCsrfAlert();
                                                    }
                                                    else if (obj[0].Code === JS_EX_SPECIAL) {
                                                        funErrorAlert(global_error_file_special);
                                                    }
                                                    else if (obj[0].Code === JS_EX_FILE_EXTENTION) {
                                                        funErrorAlert(global_req_format_url + '<%= sArrayFileExten.replace(";", ",") %>');
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
                                                    var viewDeleteFileEnable = '<%=isViewDeleteFileEnable%>';
                                                    var sActionFileEnable = "";
                                                    if(viewActionFileEnable === "0"){
                                                        sActionFileEnable = "display: none";
                                                    }
                                                    if(representEnabled === "1") {
                                                        iconDown = ' ' + global_fm_down;
                                                        iconDelete = ' ' + global_fm_button_delete;
                                                        cssDelete = 'text-align: center;width: 80px;';
                                                        iconReview = ' ' + global_fm_view;
                                                        cssReview = 'text-align: center;width: 80px;';
                                                    }
                                                    for (var i = 0; i < obj.length; i++) {
                                                        if(obj[i].FILE_PROFILE === idType)
                                                        {
                                                            var fileNameLoad = obj[i].FILE_NAME;
                                                            var sActionCRL = '<a class="btn btn-info\n\
                                                                btn-xs" style="cursor: pointer;'+cssDelete+'" onclick="DeleteTempFile(\'' + obj[i].FILE_PROFILE + '\', \'' + fileNameLoad + '\', \'' + obj[i].FILE_MANAGER_ID + '\');">' + iconDelete + '</a>';
                                                            var sReviewCRL = "";
                                                            if(obj[i].FILE_PROFILE === JS_STR_FILE_PROFILE_CODE_E_CONTRACT) {
                                                                sActionCRL = "";
                                                            }
                                                            if(obj[i].FILE_MANAGER_ID !== 0 && viewActionFileEnable === "0") {
                                                                sActionCRL = "";
                                                            }
                                                            if(viewDeleteFileEnable === "0") {
                                                                sActionCRL = "";
                                                            }
                                                            var fileNameExt = fileNameLoad.substring(fileNameLoad.lastIndexOf('.')+1);
                                                            if(fileNameExt.toUpperCase() === "PDF" || fileNameExt.toUpperCase() === "GIF"  || fileNameExt.toUpperCase() === "JPEG"
                                                                        || fileNameExt.toUpperCase() === "JPG" || fileNameExt.toUpperCase() === "PNG"){
                                                                sReviewCRL = '<a class="btn btn-info\n\
                                                                    btn-xs" style="cursor: pointer;'+cssReview+';'+sActionFileEnable+'" onclick="ViewTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + fileNameLoad + '\');">' + iconReview + '</a>';
                                                            }
                                                            var sActionDown = '<a class="btn btn-info\n\
                                                                    btn-xs" style="cursor: pointer;'+cssDelete+';'+sActionFileEnable+'" onclick="DownloadTempFile(\'' + obj[i].FILE_MANAGER_ID + '\', \'' + <%= anticsrf%> + '\');">' + iconDown + '</a>';
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
                                                        '<div style="height:5px;'+displayShowNote+'"></div><label class="control-label123" style="color:red;font-weight: 200;'+displayShowNote+'">' + global_fm_browse_cert_note + '<%= Integer.parseInt(sMaxLengthFile) / 1024 %>' + ' MB. ' + global_fm_fileattach_support + '<%= sArrayFileExten.replace(";", ",") %>' +'</label>'+
                                                    '</div>'+
                                                    '<div style="padding: 10px 0 0px 0;display:none;" id="idDiv'+obj[i].NAME+'">'+
                                                         '<table id="idTable'+obj[i].NAME+'" class="table table-bordered table-striped projects" style="margin-bottom: 10px;">'+
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
                    %>
                    <%
                        if(!"".equals(EscapeUtils.CheckTextNull(rs[0][0].COMMENT)) && !isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                            ObjectMapper oMapperParse = new ObjectMapper();
                            CERTIFICATION_COMMENT itemParsePush = oMapperParse.readValue(EscapeUtils.CheckTextNull(rs[0][0].COMMENT), CERTIFICATION_COMMENT.class);
                            String sDeclineReason = itemParsePush.certificateDeclineReason;
                            if(!"".equals(sDeclineReason)){
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <%
                                if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED) {
                            %>
                            <label id="idLblTitleDesc_Decline" class="control-label col-sm-5" style="color: <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO) ? "red" : "#000000" %>; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <script>$("#idLblTitleDesc_Decline").text(global_fm_Note);</script>
                            <%
                                } else {
                            %>
                            <label id="idLblTitleDesc_Decline" class="control-label col-sm-5" style="color: <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO) ? "red" : "#000000" %>; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <script>$("#idLblTitleDesc_Decline").text(global_fm_decline_desc);</script>
                            <%
                                }
                            %>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="DESC_DECLINE_VIEW" id="DESC_DECLINE_VIEW" readonly 
                                       value="<%= sDeclineReason%>" class="form-control123" <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO) ? "style='color: red;'" : "" %> />
                            </div>
                        </div>
                    </div>
                    <%
                            }
                        }
                    %>
<!--                    P12 password cancel
                    <
                        if(isP12 == true) {
                            if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                            {
                                String sPROPERTIES = EscapeUtils.CheckTextNull(rs[0][0].PROPERTIES);
                                if(!"".equals(sPROPERTIES)) {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    String strPassword = "";
                                    CERTIFICATION_PROPERTIES_JSON itemParsePush = objectMapper.readValue(sPROPERTIES, CERTIFICATION_PROPERTIES_JSON.class);
                                    for (int i = 0; i < itemParsePush.getAttributes().size(); i++) {
                                        if(itemParsePush.getAttributes().get(i).getKey().equals(CERTIFICATION_PROPERTIES_JSON.Attribute.KEY_KEYSTORE_PASSWORD))
                                        {
                                            EncodeSOPIN encript = new EncodeSOPIN();
                                            strPassword = encript.decode(itemParsePush.getAttributes().get(i).getValue());
                                            break;
                                        }
                                    }
                                    if(rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_NEW
                                        && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_DECLINED
                                        && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVOKED
                                        && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_EXPIRED
                                        && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED_EXPIRED
                                        && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_PERMANENT_DISABLE
                                        && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                                        && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_PERMANENT_DISABLE
                                        && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_STOPPED_OPERATION
                                        && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_AUTO_REVOKED
                                        && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE) {
                                        
                                    }
                    %>
                    <div class="col-sm-13" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePassP12"></label>
                            </label>
                            <div class="col-sm-10" style="padding-right: 0px;padding-left: 50px;">
                                <div style="width: 100%;padding-top: 0px;clear: both;">
                                    <div style="float: left;width: 50%;">
                                        <input id="idPW" class="form-control123" readonly type="password" value="<= strPassword%>"/>
                                    </div>
                                    <div style="float: right;text-align: left;width: 50%;padding-top: 8px;">
                                        &nbsp;<span toggle="#idPW" class="fa fa-fw fa-eye field_icon toggle-password"></span>
                                        <
                                            if(rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_NEW
                                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_DECLINED
                                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVOKED
                                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_EXPIRED
                                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED_EXPIRED
                                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_STOPPED_OPERATION
                                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_AUTO_REVOKED
                                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_PERMANENT_DISABLE
                                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_PERMANENT_DISABLE
                                                && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE)
                                            {
                                        %>
                                        &nbsp;<a id="btnChangePass" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="onChangePassP12('<%=anticsrf%>');"></a>
                                        <script>$("#btnChangePass").text(global_fm_Password_change);</script>
                                        <
                                            } else {
                                        %>
                                        &nbsp;<a id="btnChangePass" style="color: blue; text-decoration: underline;"></a>
                                        <script>$("#btnChangePass").text(global_fm_Password_change);</script>
                                        <
                                            }
                                        %>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <script>$("#idLblTitlePassP12").text(global_fm_pass_p12);</script>
                    </div>
                    <script>
                        $(document).ready(function () {
                            $(".toggle-password").click(function() {
                                $(this).toggleClass("fa-eye fa-eye-slash");
                                var input = $($(this).attr("toggle"));
                                if (input.attr("type") === "password") {
                                  input.attr("type", "text");
                                } else {
                                  input.attr("type", "password");
                                }
                            });
                        });
                        function onChangePassP12(idCSRF)
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            var vCERT_ID = $("#sID").val();
                            $.ajax({
                                type: "post",
                                url: "../ReqApproveDeclineCommon",
                                data: {
                                    idParam: 'changepassp12',
                                    CsrfToken:idCSRF,
                                    sID: vCERT_ID
                                },
                                cache: false,
                                success: function (html) {
                                    var arr = sSpace(html).split('#');
                                    if (arr[0] === "0")
                                    {
                                        localStorage.setItem("EDIT_RENEWCERT_DETAIL", vCERT_ID);
                                        funSuccAlert(certlist_succ_changepass_p12, "RenewCertList.jsp");
                                    }
                                    else if (arr[0] === "1") {
                                        funErrorAlert(global_errorsql);
                                    }
                                    else if (arr[0] === "2") {
                                        funErrorAlert(certlist_error_changepass_p12);
                                    }
                                    else if (arr[0] === "3") {
                                        funErrorAlert(global_error_empty_cert);
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
                    <
                                }
                            }
                        }
                    %>
                    P12 password cancel-->
                    <%
                        if(!"".equals(sACTIVATION_CODE) && !"".equals(sACTIVATION_EXPIRATION_DT)) {
                            if(rs[0][0].CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                                && rs[0][0].CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING)
                            {
                                if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED)
                                {
                                    if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleActiCode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="sACTIVATION_CODE" value="<%= sACTIVATION_CODE %>" readonly class="form-control123"/>
                            </div>
                        </div>
                        <script>$("#idLblTitleActiCode").text(global_fm_activation_code);</script>
                    </div>
                    <%
                            }
                        } else {
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleActiCode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                 <div style="float: left;width: 60%;">
                                    <input type="text" name="sACTIVATION_CODE" value="<%= sACTIVATION_CODE %>" readonly class="form-control123"/>
                                </div>
                                <div style="float: right;text-align: right;">
                                    <%
                                        String sViewResend = "";
                                        if(rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_NEW)
                                        {
                                            sViewResend = "disabled";
                                        }
                                    %>
                                    <input type="button" class="btn btn-info" style="margin-right: 0px;width: 80px;" onclick="ReSendOTP('<%= String.valueOf(rs[0][0].ID)%>', '<%= anticsrf%>');"
                                        id="idReSendOTP" <%= sViewResend%> />
                                    <script>
                                        document.getElementById("idReSendOTP").value = global_button_grid_sendmail;
                                    </script>
                                </div>
                            </div>
                        </div>
                        <script>$("#idLblTitleActiCode").text(global_fm_activation_code);</script>
                    </div>
                    <script>
                        function ReSendOTP(vCERTIFICATION_ID, idCSRF)
                        {
                            swal({
                                title: "",
                                text: cert_confirm_otp_sendmail,
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
                                            idParam: 'resendotp',
                                            sCERTIFICATION_ID: vCERTIFICATION_ID,
                                            CsrfToken: idCSRF
                                        },
                                        cache: false,
                                        success: function (html) {
                                            var arr = sSpace(html).split('#');
                                            if (arr[0] === "0")
                                            {
//                                                                            funSuccAlert(cert_succ_otp_resend, "CertView.jsp?id=" + vCERTIFICATION_ID);
                                                funSuccLocalAlert(cert_succ_otp_resend);
                                            }
                                            else if (arr[0] === JS_EX_CSRF)
                                            {
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
                    </script>
                    <%
                        }
                        String isLoadTimeICA = "";
                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                            if(rs[0][0].CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED) {
                                isLoadTimeICA = "display: none;";
                            }
                        }
                    %>
                    <div class="col-sm-6" style="padding-left: 0; <%=isLoadTimeICA%>">
                        <div class="form-group">
                            <label id="idLblTitleActiExpire" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="sACTIVATION_EXPIRATION_DT" value="<%= sACTIVATION_EXPIRATION_DT %>" readonly class="form-control123"/>
                            </div>
                        </div>
                        <script>$("#idLblTitleActiExpire").text(global_fm_activation_date);</script>
                    </div>
                    <%
                            }
                        }
                    %>
                    <%
                        if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_DECLINED)
                        {
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
                                            var localStoreInputID_OnInput = "";
                                            var localStoreUID_Info = "";
                                            var vDNFromDB = document.getElementById("idHiddenDN").value;
                                            var vDisabledForm = 'disabled';
                                            var caLoadEnabled = '<%=isCALoad%>';
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
                                                var vDisabledForm_Info = vDisabledForm;
                                                var vContentButton_MST_Radio = "<div class='col-sm-13' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                    <div class='form-group'><div class='col-sm-3' style='padding-left: 0px;margin-left: 0px;'>";
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID
                                                    +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID+'" '+vDisabledForm_Info+' class="form-control123" onlick="OnChangeComboboxMST();"></select>';
                                                vContentButton_MST_Radio = vContentButton_MST_Radio + "</div><div class='col-sm-9'><input class='form-control123' type='text' "+vDisabledForm_Info+" id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_INPUT_ID + "' />";
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
                                                var vDisabledForm_Info = vDisabledForm;
                                                var vContentButton_CMND_Radio = "<div class='col-sm-13' style='margin-bottom:0px;padding-left: 0px;'>\n\
                                                    <div class='form-group'><div class='col-sm-3' style='padding-left: 0px;margin-left: 0px;'>";
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + '<select name="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID
                                                    +'" id="'+JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_CBX_ID+'" '+vDisabledForm_Info+' class="form-control123"></select>';
                                                vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div><div class='col-sm-9'><input class='form-control123' "+vDisabledForm_Info+" type='text' id='" + JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL_INPUT_ID + "' />";
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
                                                        vContent += '<div class="form-group">' +
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
                                            $("#idDivViewComponentDN").append(vContent);
                                            if(vSanContent !== "") {
                                                $("#idViewSanInfo").append(vSanContent);
                                            } else {
                                                $("#idViewSanComponent").css("display", "none");
                                                $("#idViewSanInfo").css("display", "none");
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
    //                                                            $("#" + vInputID).val(sValuePushDBDataLast.replace(/###/g, ','));
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
    //                                                            $("#" + vInputID).val(sValuePushDBDataLast.replace(/###/g, ','));
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
                                                if('<%= allowTwoOU%>' === '1')
                                                {
                                                    if(sInputOU !== "")
                                                    {
                                                        var sValueOU = "";
//                                                        for (var p = 0; p < obj.length; p++) {
//                                                            if(obj[p].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD)
//                                                            {
//                                                                var sValuePushDB = obj[p].SubjectDNAttrCode + "=" + obj[p].SubjectDNAttrPreFix;
                                                                var str_array = vDNFromDB.split(',');
                                                                for(var h = 0; h < str_array.length; h++)
                                                                {
                                                                    var a = sSpace(str_array[h]);
                                                                    if(a.split('=')[0] === 'OU')
                                                                    {
                                                                        sValueOU = sValueOU + a.split('=')[1] + "###";
                                                                    }
                                                                }
//                                                            }
//                                                        }
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
                                                    var vContentButton_MST_Radio =  "<div class='col-sm-12' style='margin-bottom:0px;padding-left: 0;'>";
                                                    var vContentButton_CMND_Radio =  "<div class='col-sm-12' style='margin-bottom:0px;padding-left: 0;'>";
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
//                                                                                            vContentButton_MST_Text = '<div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">'+
//                                                                                                '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID_Text + '" /></div>';
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
                                                            {
                                                                vContentButton_CMND_Radio += '<label class="radio-inline"><input '+vDisabledForm+' type="radio" name="'+JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC
                                                                    +'" id="'+vInputID+'" value="'+obj[i].RADIO_LIST[j].SubjectDNAttrPreFix+'">' + obj[i].RADIO_LIST[j].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label>';
                                                            }
                                                            else
                                                            {
                                                                vContentButton_CMND_Check = "checked";
                                                                vContentButton_CMND_Radio += '<label class="radio-inline"><input '+vDisabledForm+' type="radio" '+vContentButton_CMND_Check+' name="'+JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC
                                                                    +'" id="'+vInputID+'" value="'+obj[i].RADIO_LIST[j].SubjectDNAttrPreFix+'">' + obj[i].RADIO_LIST[j].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label>';
                                                            }
                                                            if(vContentButton_CMND_Text === "")
                                                            {
                                                                var vInputID_Text = JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
//                                                                                            vContentButton_CMND_Text = '<div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">'+
//                                                                                                '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID_Text + '" /></div>';
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
//                                                                                    vContent += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
//                                                                                            '<label class="control-label123">' + obj[i].SubjectDNAttrDesc + '</label> ' +
//                                                                                            vLabelRequired +
//                                                                                            '<select '+vDisabledForm+' class="form-control123" id="' + vInputID + '"></select>' +
//                                                                                            '</div>';
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
//                                                                                        var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
//                                                                                        var vDataInputID = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
                                                            var vDataInputID = '<%= strCITY_PROVINCE_ID%>';
                                                            LoadDNCity(vInputID, vDataInputID);
                                                        }
                                                    }
                                                    else
                                                    {
                                                        vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
//                                                                                    vContent += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
//                                                                                            '<label class="control-label123">' + obj[i].SubjectDNAttrDesc + '</label> ' +
//                                                                                            vLabelRequired +
//                                                                                            '<input '+vDisabledForm+' class="form-control123" type="text" id="' + vInputID + '" />' +
//                                                                                            '</div>';
                                                        //alert(obj[i].SubjectDNAttrDesc);
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
//                                                                    $("#" + idValueMST_MNS).val(sValuePushDBDataLast.replace(/###/g, ','));
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
//                                                                    $("#" + idValueCMND_HC).val(sValuePushDBDataLast.replace(/###/g, ','));
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
                                                    else
                                                    {
                                                        var sValuePushDB = obj[i].SubjectDNAttrCode + "=" + obj[i].SubjectDNAttrPreFix;
                                                        if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_TITLE + "=")
                                                        {
                                                            sValuePushDB = " " + sValuePushDB;
                                                        }
                                                        var indexTag = vDNFromDB.indexOf(sValuePushDB);
                                                        if(indexTag !== -1)
                                                        {
                                                            if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE + "=")
                                                            {
                                                            } else if(sValuePushDB === JS_STR_COMPONENT_DN_VALUE_PHONE + "=") {
                                                                var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                                var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
//                                                                $("#" + vInputID).val(sValuePushDBDataLast.replace(/###/g, ','));
                                                                $("#" + vInputID).val(replaceCharacterDN(sValuePushDBDataLast, "1"));
                                                            } else {
                                                                if(vInputID.indexOf(JS_STR_COMPONENT_DN_VALUE_UID) !== -1)
                                                                {
                                                                    vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                                }
                                                                var sValuePushDBDataFrist = vDNFromDB.substring(indexTag, vDNFromDB.length);
                                                                var sValuePushDBDataLast = sValuePushDBDataFrist.split(',')[0].replace(sValuePushDB, "");
//                                                                $("#" + vInputID).val(sValuePushDBDataLast.replace(/###/g, ','));
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
                        $(document).ready(function () {
                            var vsCertTypeFrist = '<%= strCERTIFICATION_PROFILE_ID%>';
                            if(vsCertTypeFrist !== '')
                            {
                                LoadFormDNForPersonalCommon(vsCertTypeFrist, '<%= anticsrf%>');
                            }
                        });
                    </script>
                    <%
                        }
                    %>
                    <%
                        if (!"".equals(strCertificate)) {
                            boolean isDownCertAgent = true;
                            if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVOKED
                                || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_AUTO_REVOKED) {
                                if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                    isDownCertAgent = false;
                                }
                            }
                    %>
                    <div class="col-sm-13" style="clear: both;">
                        <div class="form-group">
                            <label id="idLblTitleCert" class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-10" style="padding-right: 0px;padding-top: 6px;padding-left: 50px;">
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
                                <%
                                    if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                    {
                                        if(isP12 == true) {
                                            if (SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                            {
                                                if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                                {
                                %>
                                <a id="idLblTitlep12Down" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupDownloadP12Soft();"></a>
                                &nbsp;
                                <script>$("#idLblTitlep12Down").text(global_fm_p12_down);</script>
                                <%
                                                }
                                %>
                                <a id="idLblTitleCertDown" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupDownloadP12Cert('<%= ids%>', '<%= anticsrf%>');"></a>
                                &nbsp;
                                <script>$("#idLblTitleCertDown").text(global_fm_down);</script>
                                <a id="idLblTitleCertP7PDown" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupDownloadP7P('<%= ids%>', '<%= anticsrf%>');"></a>
                                &nbsp;
                                <script>$("#idLblTitleCertP7PDown").text(global_fm_p7p_down);</script>
                                <a id="idLblTitleP12SendMail" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupSendMailP12Soft('<%= ids%>', '<%= anticsrf%>');"></a>
                                &nbsp;
                                <script>$("#idLblTitleP12SendMail").text(global_button_p12_sendmail);</script>
                                <%
                                            }
                                        } else {
                                            if(isDownCertAgent == true) {
                                %>
                                <a id="idLblTitleCertDown" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupDownloadCert('<%= ids%>', '<%= anticsrf%>');"></a>
                                &nbsp;
                                <script>$("#idLblTitleCertDown").text(global_fm_down);</script>
                                <a id="idLblTitleCertP7PDown" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupDownloadP7P('<%= ids%>', '<%= anticsrf%>');"></a>
                                &nbsp;
                                <script>$("#idLblTitleCertP7PDown").text(global_fm_p7p_down);</script>
                                <%
                                            }
                                        }
                                    } else {
                                        if(isDownCertAgent == true) {
                                %>
                                <a id="idLblTitleCertDown" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupDownloadCert('<%= ids%>', '<%= anticsrf%>');"></a>
                                &nbsp;
                                <script>$("#idLblTitleCertDown").text(global_fm_down);</script>
                                <a id="idLblTitleCertP7PDown" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupDownloadP7P('<%= ids%>', '<%= anticsrf%>');"></a>
                                &nbsp;
                                <script>$("#idLblTitleCertP7PDown").text(global_fm_p7p_down);</script>
                                <%
                                        }
                                    }
                                %>
                                <%
                                    if(isP12 == false)
                                    {
                                        if(isDownCertAgent == true) {
                                %>
                                <a id="idLblTitleCertDownPem" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupDownloadPemCert('<%= ids%>', '<%= anticsrf%>');"></a>
                                &nbsp;
                                <script>$("#idLblTitleCertDownPem").text(global_fm_down_pem);</script>
                                <%
                                        }
                                    }
                                %>
                                <%
                                    if(isHasFileManagerLicense == true)
                                    {
                                        if(isDownCertAgent == true) {
                                %>
                                <a id="idLblTitleLicenseDown" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupDownloadLicense('<%= ids%>', '<%= anticsrf%>');"></a>
                                <script>$("#idLblTitleLicenseDown").text(global_fm_license_down);</script>
                                <%
                                        }
                                    } else {
                                        if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED) {
                                            if(isDownCertAgent == true) {
                                %>
                                <a id="idLblTitleLicenseCreate" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupReCreateLicense('<%= ids%>', '<%= anticsrf%>');"></a>
                                <script>$("#idLblTitleLicenseCreate").text(global_fm_license_create);</script>
                                <%
                                            }
                                        }
                                    }
                                %>
                                <%
                                    if(CommonFunction.checkHardTokenIDEnabled(intPKI_FORMFACTOR_ID) == true
                                        && rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED) {
                                        String reSignConfirm = rs[0][0].RESIGNING_CONFIRMATION_PAPER_ENABLED;
                                        if("1".equals(reSignConfirm)) {
                                %>&nbsp;&nbsp;
                                <a id="idLblTitleConfirmReSign" title="Click to cancel Sign Confirmation request" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupCancelSignConfirm('<%= ids%>', '<%= anticsrf%>');"></a>
                                <script>
                                    $("#idLblTitleConfirmReSign").text(global_fm_wait_sign_confirmation);
                                    function popupCancelSignConfirm(id, idCSRF) {
                                        $('body').append('<div id="over"></div>');
                                        $(".loading-gif").show();
                                        $.ajax({
                                            type: "post",
                                            url: "../ReqApproveDeclineCommon",
                                            data: {
                                                idParam: 'cancelresignconfirm',
                                                sID: id,
                                                CsrfToken: idCSRF
                                            },
                                            cache: false,
                                            success: function (html)
                                            {
                                                var myStrings = sSpace(html).split('#');
                                                if (myStrings[0] === "0")
                                                {
                                                    funSuccLocalAlert(profileaccss_succ_edit);
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
                                <%
                                        } else {
                                %>&nbsp;&nbsp;
                                <a id="idLblTitleConfirmReSign" title="Click to re-sign the confirmation page" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupReSignConfirm('<%= ids%>', '<%= anticsrf%>');"></a>
                                <script>
                                    $("#idLblTitleConfirmReSign").text(global_fm_sign_confirmation);
                                    function popupReSignConfirm(id, idCSRF) {
                                        $('body').append('<div id="over"></div>');
                                        $(".loading-gif").show();
                                        $.ajax({
                                            type: "post",
                                            url: "../ReqApproveDeclineCommon",
                                            data: {
                                                idParam: 'resignconfirm',
                                                sID: id,
                                                CsrfToken: idCSRF
                                            },
                                            cache: false,
                                            success: function (html)
                                            {
                                                var myStrings = sSpace(html).split('#');
                                                if (myStrings[0] === "0")
                                                {
                                                    funSuccLocalAlert(profileaccss_succ_edit);
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
                                <%
                                        }
                                    }
                                %>
                            </div>
                        </div>
                        <script>$("#idLblTitleCert").text(global_fm_Certificate);</script>
                    </div>
                    <%
                        if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                        {
                            if(isP12 == true) {
                                if (SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                {
                                    if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                    {
                    %>
                    <div class="col-sm-13" style="clear: both;display: none;" id="idP12PassView">
                        <div class="form-group">
                            <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleP12Password"></label>
                                <label id="idLblNoteP12Password" class="CssRequireField"></label>
                            </label>
                            <div class="col-sm-4" style="padding-right: 0px;">
                                <input type="text" name="P12Password" id="P12Password" maxlength="50" class="form-control123" />
                            </div>
                            <div class="col-sm-2" style="padding-right: 0px;">
                                <input type="button" id="btnOkPassP12" class="btn btn-info" onclick="approveDownloadP12Soft();"/>
                                <input id="btnColsePassP12" class="btn btn-info" type="button" onclick="cancelDownloadP12Soft();" />
                                <script>
                                    $("#idLblTitleP12Password").text(global_fm_pass_p12);
                                    $("#idLblNoteP12Password").text(global_fm_require_label);
                                    document.getElementById("btnOkPassP12").value = login_fm_buton_OK;
                                    document.getElementById("btnColsePassP12").value = login_fm_buton_cancel;
                                </script>
                            </div>
                            <div class="col-sm-4" style="padding-right: 0px;">
                            </div>
                        </div>
                    </div>
                    <%
                                        }
                                    }
                                }
                            }
                        }
                    %>  
                    <%
                        if(isP12 == false) {
                    %>
                    <div id="idViewCSR" class="form-group" style="padding: 10px 0px 0 0px;margin: 0;clear: both;">
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
                                        <textarea id="idCompanyPrin" class="form-control123" readonly="true" name="idCompanyPrin" style="height: 200px;"><%= strSubjectDN%></textarea>
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
                            <div class="col-sm-6" style="padding-left: 0;">
                                <div class="form-group">
                                    <label id="idLblTitleExpireContact" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input id="strExpireContact" class="form-control123" type="text" readonly="true" value="<%= EscapeUtils.CheckTextNull(rs[0][0].EXPIRATION_CONTRACT_DT)%>"/>
                                    </div>
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleCompanyPrin").text(global_fm_company);
                                $("#idLblTitleIssuerPrin").text(global_fm_issue);
                                $("#idLblTitleCertValid").text(global_fm_valid_cert);
                                $("#idLblTitleCertExpire").text(global_fm_Expire_cert);
                                $("#idLblTitleExpireContact").text(token_fm_TimeOffset);
                            </script>
                            <% } else {
                            %>
                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                <label class="control-label123" id="idLblTitleNoCert"></label>
                                <script>$("#idLblTitleNoCert").text(global_req_info_cert);</script>
                            </div>
                            <%
                                    }
                                } else {
                            %>
                            <div class="col-sm-6" style="padding-left: 0;">
                                <div class="form-group" style="vertical-align:middle;">
                                    <label id="idLblTitleCompanyPrin" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <textarea id="idCompanyPrin" class="form-control123" readonly="true" name="idCompanyPrin" style="height: 200px;"><%= EscapeUtils.CheckTextNull(rs[0][0].SUBJECT)%></textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;">
                                <div class="form-group">
                                    <label id="idLblTitleIssuerPrin" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <textarea id="idIssuerPrin" class="form-control123" style="height: 75px;" readonly="true" name="idIssuerPrin"><%= EscapeUtils.CheckTextNull(rs[0][0].ISSUER_SUBJECT)%></textarea>
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
                            <div class="col-sm-6" style="padding-left: 0;">
                                <div class="form-group">
                                    <label id="idLblTitleExpireContact" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input id="strExpireContact" class="form-control123" type="text" readonly="true" value="<%= EscapeUtils.CheckTextNull(rs[0][0].EXPIRATION_CONTRACT_DT)%>"/>
                                    </div>
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleCompanyPrin").text(global_fm_company);
                                $("#idLblTitleIssuerPrin").text(global_fm_issue);
                                $("#idLblTitleCertValid").text(global_fm_valid_cert);
                                $("#idLblTitleCertExpire").text(global_fm_Expire_cert);
                                $("#idLblTitleExpireContact").text(token_fm_TimeOffset);
                            </script>
                            <%
                                }
                            %>
                        </fieldset>
                    </div>
                    <%
                        }
                    %>
                    <script>
                        $(document).ready(function () {
                            $('#P12Password').keydown(function (event) {
                                if (event.keyCode === 13) {
                                    if (JSCheckEmptyField($('#P12Password').val()))
                                    {
                                        approveDownloadP12Soft();
                                        return false;
                                    }
                                    else
                                    {
                                        $("#P12Password").focus();
                                        funErrorAlert(policy_req_empty + global_fm_pass_p12);
                                        return false;
                                    }
                                }
                            });
                        });
                        
                        function popupSendMailP12Soft(id, idCSRF)
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../DownloadFileCSR",
                                data: {
                                    idParam: 'sendmailp12soft',
                                    id: id,
                                    CsrfToken: idCSRF
                                },
                                cache: false,
                                success: function (html)
                                {
                                    var myStrings = sSpace(html).split('#');
                                    if (myStrings[0] === "0")
                                    {
                                        funSuccNoLoad(sendmail_success);
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
                        
                        function popupDownloadP12Soft()
                        {
                            $("#idP12PassView").css("display", "");
                        }
                        function cancelDownloadP12Soft()
                        {
                            $("#idP12PassView").css("display", "none");
                            $("#P12Password").val('');
                        }
                        function approveDownloadP12Soft()
                        {
                            if (!JSCheckEmptyField($("#P12Password").val()))
                            {
                                $("#P12Password").focus();
                                funErrorAlert(policy_req_empty + global_fm_pass_p12);
                                return false;
                            }
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../DownloadFileCSR",
                                data: {
                                    idParam: 'downloadp12soft',
                                    id: $("#sID").val(),
                                    sP12Password: $("#P12Password").val()
                                    //, CsrfToken: idCSRF
                                },
                                cache: false,
                                success: function (html)
                                {
                                    var myStrings = sSpace(html).split('#');
                                    if (myStrings[0] === "0")
                                    {
                                        cancelDownloadP12Soft();
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
                        
                        function popupDownloadP12Cert(id, idCSRF)
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../DownloadFileCSR",
                                data: {
                                    idParam: 'p12certhasid',
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
                        
                        function popupDownloadP7P(id, idCSRF)
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../DownloadFileCSR",
                                data: {
                                    idParam: 'downloadcertp7p',
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
                        
                        function popupDownloadPemCert(id, idCSRF)
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../DownloadFileCSR",
                                data: {
                                    idParam: 'pemcerthasid',
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
                        function popupDownloadLicense(id, idCSRF)
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../DownloadFileCSR",
                                data: {
                                    idParam: 'downloadlicenseconvertjrb',
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
                                        f.action = '../DownFromSaveFile?idParam=downfilepdfcert&filename=' + myStrings[1];
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
                        function popupReCreateLicense(id, idCSRF)
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../ReqApproveDeclineCommon",
                                data: {
                                    idParam: 'recreatelicense',
                                    sID: id,
                                    CsrfToken: idCSRF
                                },
                                cache: false,
                                success: function (html)
                                {
                                    var myStrings = sSpace(html).split('#');
                                    if (myStrings[0] === "0")
                                    {
                                        funSuccLocalAlert(global_succ_license_create);
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
                        if(isP12 == true) {
                    %>
                    <fieldset class="scheduler-border" style="clear: both;">
                        <legend class="scheduler-border" id="idLblTitleGroupCert"></legend>
                        <script>$("#idLblTitleGroupCert").text(global_group_cert);</script>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group" style="vertical-align:middle;">
                                <label id="idLblTitleCompanyPrin" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <textarea id="idCompanyPrin" class="form-control123" readonly="true" name="idCompanyPrin" style="height: 200px;"><%= EscapeUtils.CheckTextNull(rs[0][0].SUBJECT)%></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleIssuerPrin" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <textarea id="idIssuerPrin" class="form-control123" style="height: 75px;" readonly="true" name="idIssuerPrin"><%= EscapeUtils.CheckTextNull(rs[0][0].ISSUER_SUBJECT)%></textarea>
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
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleExpireContact" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input id="strExpireContact" class="form-control123" type="text" readonly="true" value="<%= EscapeUtils.CheckTextNull(rs[0][0].EXPIRATION_CONTRACT_DT)%>"/>
                                </div>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleCompanyPrin").text(global_fm_company);
                            $("#idLblTitleIssuerPrin").text(global_fm_issue);
                            $("#idLblTitleCertValid").text(global_fm_valid_cert);
                            $("#idLblTitleCertExpire").text(global_fm_Expire_cert);
                            $("#idLblTitleExpireContact").text(token_fm_TimeOffset);
                        </script>
                    </fieldset>
                    <%
                        }
                    %>
                    
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
                    <script>
                        $(document).ready(function () {
                            var vsCertTypeFrist = '<%= strCERTIFICATION_PROFILE_ID%>';
                            if(vsCertTypeFrist !== '')
                            {
                                LoadFormSan(vsCertTypeFrist);
                            }
                        });
                        function LoadFormSan(idCertType)
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
                                        $("#idViewSanInfo").empty();
                                        if (obj[0].Code === "0")
                                        {
                                            $("#idViewSanComponent").css("display", "");
                                            $("#idViewSanInfo").css("display", "");
                                            var vSanContent = "";
                                            var localStoreRequired = new Array();
                                            var localStoreSanInput = new Array();
                                            for (var i = 0; i < obj.length; i++) {
                                                if (obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD_SAN)
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
                                                    vSanContent += '<div class="col-sm-6">' +
                                                    '<div class="form-group">' +
                                                    '<label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                    '<div class="col-sm-7" style="padding-right: 0px;padding-left:0;">'+
                                                    '<input class="form-control123" disabled type="text" id="' + vInputID + '" />' +
                                                    '</div>' +
                                                    '</div>' +
                                                    '</div>';
                                                }
                                            }
                                            if(vSanContent !== "") {
                                                $("#idViewSanInfo").append(vSanContent);
                                            } else {
                                                $("#idViewSanComponent").css("display", "none");
                                                $("#idViewSanInfo").css("display", "none");
                                            }
                                            var vSanDataDB = '<%= sSanDataDB%>';
                                            if(vSanDataDB !== "") {
                                                for (var i = 0; i < obj.length; i++) {
                                                    var vInputID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID;
                                                    var vInputSanOperation = obj[i].SubjectDNAttrCode;
                                                    if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_TEXT_FIELD_SAN)
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
                                            $("#idViewSanComponent").css("display", "none");
                                            $("#idViewSanInfo").css("display", "none");
                                            funErrorAlert(global_errorsql);
                                        }
                                    }
                                }
                            });
                            return false;
                        }
                    </script>
                    <!-- PROPERTIES SAN -->
                    <!-- Confirm Customer -->
                    <%
                        if(CommonFunction.checkHardTokenEnabled(rs[0][0].PKI_FORMFACTOR_NAME) == true) {
                    %>
                    <%
                        String sCUSTOMER_CONFIRMATION = rs[0][0].CUSTOMER_CONFIRMATION;
                        if(!"".equals(sCUSTOMER_CONFIRMATION))
                        {
                            ObjectMapper objectMapper = new ObjectMapper();
                            CustomerConfirmation valueCustomer = objectMapper.readValue(sCUSTOMER_CONFIRMATION, CustomerConfirmation.class);
                            String sCusIP = EscapeUtils.CheckTextNull(valueCustomer.getIpAddress());
                            String sCusTime = CommonFunction.dateConvertString(valueCustomer.getTimestamp());
                            boolean sCusActive = valueCustomer.isConfirmed();
                            String sCusContent = EscapeUtils.CheckTextNull(valueCustomer.getCertificationInfo());
                    %>
                    <div class="col-sm-13" style="padding-left: 0;clear: both;" id="idViewConfirmCusInfo">
                        <div class="form-group">
                            <fieldset class="scheduler-border">
                                <legend class="scheduler-border" id="idLblTitleConfirmCusInfo"></legend>
                                <script>$("#idLblTitleConfirmCusInfo").text(global_fm_confirm_customer);</script>
                                <div class="col-sm-6" style="padding-left: 0;">
                                    <div class="form-group" style="vertical-align:middle;">
                                        <label id="idLblTitleDetailContent" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                        <div class="col-sm-7" style="padding-right: 0px;">
                                            <textarea id="CUSTOMER_CONTENT" class="form-control123" readonly="true" name="CUSTOMER_CONTENT" style="height: 125px;"><%= sCusContent%></textarea>
                                        </div>
                                    </div>
                                    <script>
                                        $("#idLblTitleDetailContent").text(global_fm_confirm_content);
                                    </script>
                                </div>
                                <div class="col-sm-6" style="padding-left: 0;">
                                    <div class="form-group">
                                        <label id="idLblTitleDetailActive" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                        <div class="col-sm-7" style="padding-right: 0px;">
                                            <label class="switch" for="CUSTOMER_ACTIVE">
                                                <input TYPE="checkbox" disabled id="CUSTOMER_ACTIVE" <%=sCusActive ? "checked='checked'" : ""%> />
                                                <div class="slider round"></div>
                                            </label>
                                        </div>
                                    </div>
                                    <script>
                                        $("#idLblTitleDetailActive").text(global_fm_confirm);
                                    </script>
                                </div>
                                <div class="col-sm-6" style="padding-left: 0;">
                                    <div class="form-group">
                                        <label id="idLblTitleDetailTime" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                        <div class="col-sm-7" style="padding-right: 0px;">
                                            <input type="text" name="CUSTOMER_TIME" readonly value="<%= sCusTime%>" class="form-control123">
                                        </div>
                                    </div>
                                    <script>
                                        $("#idLblTitleDetailTime").text(global_fm_confirm_time);
                                    </script>
                                </div>
                                <div class="col-sm-6" style="padding-left: 0;">
                                    <div class="form-group">
                                        <label id="idLblTitleDetailIP" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                        <div class="col-sm-7" style="padding-right: 0px;">
                                            <input type="text" name="CUSTOMER_IP" readonly value="<%= sCusIP%>" class="form-control123">
                                        </div>
                                    </div>
                                    <script>
                                        $("#idLblTitleDetailIP").text(global_fm_confirm_ip);
                                    </script>
                                </div>
                            </fieldset>
                        </div>
                    </div>
                    <%
                        } else {
                    %>
                    <div class="col-sm-13" style="padding-left: 0;clear: both;" id="idViewConfirmCusInfo">
                        <div class="form-group">
                            <fieldset class="scheduler-border">
                                <legend class="scheduler-border" id="idLblTitleConfirmCusInfo"></legend>
                                <script>$("#idLblTitleConfirmCusInfo").text(global_fm_confirm_customer);</script>
                                <div class="col-sm-6" style="padding-left: 0;">
                                    <div class="form-group">
                                        <label id="idLblTitleDetailActive" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                        <div class="col-sm-7" style="padding-right: 0px;">
                                            <label class="switch" for="CUSTOMER_ACTIVE">
                                                <input TYPE="checkbox" disabled id="CUSTOMER_ACTIVE"/>
                                                <div class="slider round"></div>
                                            </label>
                                        </div>
                                    </div>
                                    <script>
                                        $("#idLblTitleDetailActive").text(global_fm_confirm);
                                    </script>
                                </div>
                            </fieldset>
                        </div>
                    </div>
                    <%
                        }
                    %>
                    <%
                        }
                    %>
                    <!-- DNS List for SSL -->
<!--                    <script>
                        $(document).ready(function () {
                            if('<= intCERTIFICATION_PURPOSE_ID%>' === JS_STR_CERTIFICATION_PURPOSE_ID_SSL)
                            {
                                $("#idViewDNSInfo").css("display","");
                            } else {
                                $("#idViewDNSInfo").css("display","none");
                            }
                        });
                    </script>-->
                    <div class="col-sm-13" style="padding-left: 0;clear: both;display: none;" id="idViewDNSInfo">
                        <div class="form-group">
                            <fieldset class="scheduler-border">
                                <legend class="scheduler-border" id="idLblTitleChangeDNSList"></legend>
                                <script>$("#idLblTitleChangeDNSList").text(global_fm_dns_list);</script>
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
                                       <%
                                           if (cartToken != null) {
                                               int j = 1;
                                               ArrayList<DNS_NAME_DATA> dsDNS = cartToken.getGH();
                                               for (DNS_NAME_DATA mhDNS : dsDNS) {
                                       %>
                                        <tr>
                                           <td><%= com.convertMoney(j)%></td>
                                           <td><%= mhDNS.DNS_NAME%></td>
                                           <td>
                                               <a disabled id="idLinkDNSDelete<%=j%>" class="btn btn-info btn-xs"></a>
                                               <script>$("#idLinkDNSDelete<%= j%>").append('<i class="fa fa-pencil"></i> ' + global_fm_button_delete);</script>
                                           </td>
                                        </tr>
                                        <%
                                                    j++;
                                                }
                                            } else {
                                        %>
                                        <tr><td colspan="4" id="idTableDNSNotFound"></td></tr>
                                        <script>
                                           $("#idTableDNSNotFound").text(global_no_data);
                                        </script>
                                        <%
                                            }
                                        %>
                                   </tbody>
                                </table>
                                </div>
                            </fieldset>
                        </div>
                    </div>
                    
                    <%
                        if(rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_NEW
                            && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_DECLINED)
                        {
                    %>
                    <div class="col-sm-6" style="padding-left: 0; display: <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) || (isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) && !SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) ? "none" : ""%>">
                        <div class="form-group">
                            <label id="idLblTitleBackUpKey" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <label class="switch" for="idCheckBackUpKey" style="margin-bottom: 0;">
                                    <input type="checkbox" readonly name="idCheckBackUpKey" id="idCheckBackUpKey" disabled
                                        <%= rs[0][0].PRIVATE_KEY_ENABLED == true ? "checked" : "" %>/>
                                    <div id="idCheckBackUpKeyClass" class="slider round disabled"></div>
                                </label>
                            </div>
                        </div>
                        <script>$("#idLblTitleBackUpKey").text(regiscert_fm_check_backup_key);</script>
                    </div>
                    <%
                        }
                    %>
                    <%
                        if(rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_NEW
                            && rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_DECLINED)
                        {
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleShareCert" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <label class="switch" for="shareModeEnabled" style="margin-bottom: 0;">
                                    <input type="checkbox" readonly name="shareModeEnabled" id="shareModeEnabled" disabled
                                        <%= rs[0][0].SHARED_MODE == true ? "checked" : "" %>/>
                                    <div id="shareModeEnabledClass" class="slider round disabled"></div>
                                </label>
                            </div>
                        </div>
                        <script>$("#idLblTitleShareCert").text(global_fm_share_mode_cert);</script>
                    </div>
                    <%
                        }
                    %>
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
                    <%
                        }
                    %>
                    <%
                        String sReadOnlyContract = "readonly";
                        if(isEditContact == true){
                            sReadOnlyContract = "";
                        }
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePhoneContact"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" <%= sReadOnlyContract%> name="PHONE_CONTRACT" id="PHONE_CONTRACT" value="<%= strPHONE_CONTRACT%>" class="form-control123">
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
                                <input type="text" <%= sReadOnlyContract%> name="EMAIL_CONTRACT" id="EMAIL_CONTRACT" value="<%= strEMAIL_CONTRACT%>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleEmailContact").text(global_fm_email_contact);</script>
                    </div>
                    <%
                        if (SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT) && isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePhoneContactReal"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PHONE_CONTRACT_REAL" id="PHONE_CONTRACT_REAL" value="<%= strPHONE_CONTRACTReal%>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitlePhoneContactReal").text(global_fm_phone_contact_real);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleEmailContactReal"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="EMAIL_CONTRACT_REAL" id="EMAIL_CONTRACT_REAL" value="<%= strEMAIL_CONTRACTReal%>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleEmailContactReal").text(global_fm_email_contact_real);</script>
                    </div>
                    <%}%>
                    <div class="col-sm-6" style="padding-left: 0;">
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
                    <div class="col-sm-6" style="padding-left: 0;">
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
                    <div class="col-sm-6" style="padding-left: 0;">
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
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCreatedDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" type="text" name="REQUEST_TIME" readonly value="<%= strCREATED_DT%>"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleCreatedDate").text(global_fm_date_create);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
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
                if('<%= strIsCert%>' === "1")
                {
                    popupViewCSRS();
                }
            });
        </script>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <!--<script src="../style/custom.min.js"></script>-->
        <!-- Modal Cert Decline -->
        <div id="myModalListCertDecline" class="modal fade" role="dialog">
            <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                 left: 0; height: 100%;" class="loading-gifCertDecline">
                <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
            </div>
            <div class="modal-dialog modal-800" id="myDialogListCertDecline">
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
        <!-- Modal Registration Print -->
<!--        <div id="myModalRegisterPrintView" class="modal fade" role="dialog">
            <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                 left: 0; height: 100%;" class="loading-gifRegisterPrintView">
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
                        <div id="contentRegisterPrintView"></div>
                    </div>
                </div>
            </div>
        </div>
        <script>
            function popupDialogReRegister(id)
            {
                $('#myModalRegisterPrintView').modal('show');
                $('#contentRegisterPrintView').empty();
                $('#contentRegisterPrintView').load('ReRegisterCert.jsp', {id:id}, function () {
                });
                $(".loading-gifRegisterPrintView").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
            function popupDialogReRegisterSoft(id)
            {
                $('#myModalRegisterPrintView').modal('show');
                $('#contentRegisterPrintView').empty();
                $('#contentRegisterPrintView').load('ReRegisterCertSoft.jsp', {id:id}, function () {
                });
                $(".loading-gifRegisterPrintView").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
            function popupPrintHandover(id)
            {
                $('#myModalRegisterPrintView').modal('show');
                $('#contentRegisterPrintView').empty();
                $('#contentRegisterPrintView').load('PrintHandover.jsp', {id:id}, function () {
                });
                $(".loading-gifRegisterPrintView").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
            function CloseDialog()
            {
                $('#myModalRegisterPrint').modal('hide');
                $(".loading-gifRegisterPrint").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
        </script>-->
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
