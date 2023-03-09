<%-- 
    Document   : SuspendCertView
    Created on : Jul 2, 2019, 5:28:00 PM
    Author     : THANH-PC
--%>

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
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link rel="stylesheet" type="text/css" media="all" href="../js/daterangepicker.css" />
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
//            document.title = certlist_title_reissue;
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            var checkForSpecialChar = function(string) {
             for(i = 0; i < specialChars.length;i++){
               if(string.indexOf(specialChars[i]) > -1){
                   return true;
                }
             }
             return false;
            };
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
        %>
        <script>
            function UpdateForm(idCSRF)
            {
                if(sSpace($("#CERTIFICATION_ATTR_TYPE_SUSPEND").val()) === JS_STR_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                {
                    if (!JSCheckEmptyField($("#SUSPEND_TIME").val()))
                    {
                        $("#SUSPEND_TIME").focus();
                        funErrorAlert(policy_req_empty_choose + global_fm_times_recovery);
                        return false;
                    }
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                var vCERT_ID = $("#sID").val();
                $.ajax({
                    type: "post",
                    url: "../RequestCommon",
                    data: {
                        idParam: 'suspendcert',
                        sID: vCERT_ID,
                        sCERTIFICATION_ATTR_TYPE: $("#CERTIFICATION_ATTR_TYPE_SUSPEND").val(),
                        sSUSPEND_TIME: $("#SUSPEND_TIME").val(),
                        sSUSPEND_REASON: $("#SUSPEND_REASON").val(),
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            if('<%= SessAgentID%>' === JS_STR_AGENT_ROOT)
                            {
                                localStorage.setItem("EDIT_RENEWCERT_SUSPEND", vCERT_ID);
                                funSuccAlert(certlist_succ_suspend_ca, "RenewCertList.jsp");
                            }
                            else
                            {
                                localStorage.setItem("EDIT_RENEWCERT_SUSPEND", vCERT_ID);
                                funSuccAlert(certlist_succ_suspend, "RenewCertList.jsp");
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
                        else if (arr[0] === JS_EX_ERROR_CORECA_CALL)
                        {
                            funErrorAlert(global_error_coreca_call_approve);
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
                            BRANCH[][] branchAccess = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                            isAccessAgencyPage = CommonFunction.checkBranchTreeInvalidCert(rs[0][0].BRANCH_ID, branchAccess);
//                            if (!String.valueOf(rs[0][0].BRANCH_ID).equals(SessUserAgentID)) {
//                                isAccessAgencyPage = false;
//                            }
                        }
                        if (isAccessAgencyPage == true) {
                            int intPKI_FORMFACTOR_ID = rs[0][0].PKI_FORMFACTOR_ID;
                            String sTOKEN_SN = EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN);
                            String sCERTIFICATION_SN = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_SN);
                            String strEMAIL_CONTRACT = EscapeUtils.CheckTextNull(rs[0][0].EMAIL_CONTRACT);
                            String strPHONE_CONTRACT = EscapeUtils.CheckTextNull(rs[0][0].PHONE_CONTRACT);
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
                            boolean isP12 = false;
                            if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                            {
                                if(rs[0][0].PRIVATE_KEY_ENABLED == true)
                                {
                                    isP12 = true;
                                    strIsCert = "0";
                                }
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
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEdits"></span></h2>
            <script>$("#idLblTitleEdits").text(certlist_title_suspend);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <%
                        if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED)
                        {
                    %>
                    <%
                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PERMANENT_DISABLE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true
                            || CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_TEMPORARY_DISABLE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                        {
                    %>
                    <input type="button" id="btnApprove" class="btn btn-info" onclick="UpdateForm('<%=anticsrf%>');" />
                    <script>document.getElementById("btnApprove").value = global_fm_button_suspend;</script>
                    <%
                        }
                    %>
                    <%
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
                <input type="hidden" name="idHiddenDN" id="idHiddenDN" value="<%=EscapeUtils.escapeHtmlDN(strDN)%>"/>  
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCertType" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="CERTIFICATION_ATTR_TYPE_SUSPEND" id="CERTIFICATION_ATTR_TYPE_SUSPEND" class="form-control123" onchange="onChangeCertType();">
                                <%
                                    String sFristCERT_ATTR_TYPE = "";
                                    CERTIFICATION_ATTR_TYPE[][] rsType = new CERTIFICATION_ATTR_TYPE[1][];
                                    db.S_BO_CERTIFICATION_ATTR_TYPE_COMBOBOX(sessLanguageGlobal, rsType);
                                    if (rsType[0].length > 0) {
                                        for (CERTIFICATION_ATTR_TYPE temp1 : rsType[0]) {
                                            if(temp1.NAME.equals(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_PERMANENT_DISABLE)
                                                || temp1.NAME.equals(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_TEMPORARY_DISABLE))
                                            {
                                                if(CommonFunction.CheckRoleFuncValid(temp1.NAME,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                {
                                                    if("".equals(sFristCERT_ATTR_TYPE))
                                                    {
                                                        sFristCERT_ATTR_TYPE = String.valueOf(temp1.ID);
                                                    }
                                %>
                                <option value="<%=String.valueOf(temp1.ID)%>"><%=temp1.REMARK%></option>
                                <%
                                                }
                                            }
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <script>
                        $(document).ready(function () {
                            $("#idLblTitleCertType").text(cert_fm_type_request);
                            var vFristCERT_ATTR_TYPE = '<%= sFristCERT_ATTR_TYPE%>';
                            if (vFristCERT_ATTR_TYPE === JS_STR_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                            {
                                $("#idDivSuspend").css("display", "");
                            } else {
                                $("#SUSPEND_TIME").val("");
                                $("#idDivSuspend").css("display", "none");
                            }
                        });
                        function onChangeCertType()
                        {
                            if ($("#CERTIFICATION_ATTR_TYPE_SUSPEND").val() === JS_STR_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                            {
                                $("#idDivSuspend").css("display", "");
                            } else {
                                $("#SUSPEND_TIME").val("");
                                $("#idDivSuspend").css("display", "none");
                            }
                        }    
                    </script>
                </div>
                <!-- REVOKE REASON CLOSE BEGIN -->
                <!--<
                    if (SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleRevokeResonCore" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="CERT_REVOCATION_REASON" id="CERT_REVOCATION_REASON" class="form-control123">
                                <
                                    CERTIFICATION_REVOCATION_REASON[][] rsReasonCore = new CERTIFICATION_REVOCATION_REASON[1][];
                                    db.S_BO_CERTIFICATION_REVOCATION_REASON_COMBOBOX(sessLanguageGlobal, rsReasonCore);
                                    if (rsReasonCore[0].length > 0) {
                                        for (CERTIFICATION_REVOCATION_REASON temp1 : rsReasonCore[0]) {
                                            if(temp1.NAME.equals(Definitions.CONFIG_CERTIFICATION_REVOCATION_REASON_CODE_SUPERSEDED)
                                                || temp1.NAME.equals(Definitions.CONFIG_CERTIFICATION_REVOCATION_REASON_CODE_CERTIFICATEHOLD))
                                            {
                                %>
                                <option value="%=temp1.NAME%>"><=temp1.REMARK%></option>
                                <
                                            }
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <script>$("#idLblTitleRevokeResonCore").text(global_fm_revoke_reason_core);</script>
                </div>
                <
                    } else {
                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleRevokeResonCore" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="CERT_REVOCATION_REASON" id="CERT_REVOCATION_REASON" class="form-control123">
                                <
                                    CERTIFICATION_REVOCATION_REASON[][] rsReasonCore = new CERTIFICATION_REVOCATION_REASON[1][];
                                    db.S_BO_CERTIFICATION_REVOCATION_REASON_COMBOBOX(sessLanguageGlobal, rsReasonCore);
                                    if (rsReasonCore[0].length > 0) {
                                        for (CERTIFICATION_REVOCATION_REASON temp1 : rsReasonCore[0]) {
                                            if(temp1.NAME.equals(Definitions.CONFIG_CERTIFICATION_REVOCATION_REASON_CODE_SUPERSEDED)
                                                || temp1.NAME.equals(Definitions.CONFIG_CERTIFICATION_REVOCATION_REASON_CODE_CERTIFICATEHOLD))
                                            {
                                %>
                                <option value="<=temp1.NAME%>"><=temp1.REMARK%></option>
                                <
                                            }
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <script>$("#idLblTitleRevokeResonCore").text(global_fm_revoke_reason_core);</script>
                </div>
                <
                        } else {
                %>
                <input id="CERT_REVOCATION_REASON" name="CERT_REVOCATION_REASON" readonly value="" style="display: none;"/>
                <
                        }
                    }
                %>-->
                <!-- REVOKE REASON CLOSE END -->
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleSuspendReason" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="SUSPEND_REASON" id="SUSPEND_REASON" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleSuspendReason").text(global_fm_suspend_desc);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;" id="idDivSuspend">
                    <div class="form-group">
                        <label id="idLblTitleSuspendTime" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblRequireSuspendTime" class="CssRequireField"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="SUSPEND_TIME" id="SUSPEND_TIME" readonly class="form-control123">
                             <!--style="width: 86%;"<span class="fa fa-th"></span>-->
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleSuspendTime").text(global_fm_times_recovery);
                        $("#idLblRequireSuspendTime").text(global_fm_require_label);
                    </script>
                </div>
                <script>
                    $(document).ready(function () {
                        if ($("#CERTIFICATION_ATTR_TYPE_SUSPEND").val() === JS_STR_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                        {
                            $("#idDivSuspend").css("display", "");
                        } else {
                            $("#SUSPEND_TIME").val("");
                            $("#idDivSuspend").css("display", "none");
                        }
                        $('#SUSPEND_TIME').daterangepicker({
                            "singleDatePicker": true,
                            "timePicker": true,
                            "timePicker24Hour": true,
                            "timePickerSeconds": true,
                            showDropdowns: true,
//                            startDate:'18/07/2019 09:10:10',
//                            minDate: moment().subtract(7, "days"),
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
                <fieldset class="scheduler-border" style="clear: both;">
                    <legend class="scheduler-border" id="idLblTitleDetailOld"></legend>
                    <script>$("#idLblTitleDetailOld").text(certlist_title_detail);</script>
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
                                <select name="CERTIFICATION_AUTHORITY" id="CERTIFICATION_AUTHORITY" class="form-control123" disabled>
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
                                        String sFristCerDurationOrProfileID = "";
                                        CERTIFICATION_PROFILE[][] rsDuration = new CERTIFICATION_PROFILE[1][];
                                        db.S_BO_CA_GET_DURATION_COMBOBOX_BY_TYPE(sFristCA, sFristCerPurpose, String.valueOf(intPKI_FORMFACTOR_ID), 0, sessLanguageGlobal, rsDuration);
                                        if (rsDuration[0].length > 0) {
                                            for (int i = 0; i < rsDuration[0].length; i++) {
                                                sFristCerDurationOrProfileID = String.valueOf(rsDuration[0][0].ID);
                                    %>
                                    <option value="<%= String.valueOf(rsDuration[0][i].ID)%>"><%= rsDuration[0][i].REMARK %></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <script>$("#idLblTitleCertDuration").text(global_fm_duration_cts);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleFeeAmount" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="strFEE_AMOUNT" value="<%= strFEE_AMOUNT%>" readonly class="form-control123"/>
                            </div>
                        </div>
                        <script>$("#idLblTitleFeeAmount").text(global_fm_amount_fee);</script>
                    </div>
                    
                    <!-- BEGIN FILE MANAGER -->
                    <%
                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_EFY) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO)
                            || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                            String sArrayFileExten = "";
                            GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                            if (sessGeneralPolicy1[0].length > 0) {
                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                {
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_ALLOWED_FILE_EXTENSION_LIST)) {
                                        sArrayFileExten = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                        break;
                                    }
                                }
                            }
                            String sOWNER_ID = String.valueOf(rs[0][0].CERTIFICATION_OWNER_ID);
                            String sMaxLengthFile = cogCommon.GetPropertybyCode(Definitions.CONFIG_JACK_RABBIT_MAX_LENGTH_FILE).trim();
                            session.setAttribute("sessUploadFileCert", null);
                            boolean isHasFileOfUser = true;
                            FILE_MANAGER[][] rsFileMana = new FILE_MANAGER[1][];
                            db.S_BO_FILE_MANAGER_GET_BY_CERTIFICATION_AND_OWNER(EscapeUtils.escapeHtml(ids), sOWNER_ID, sessLanguageGlobal, rsFileMana);
                            if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                    for(FILE_MANAGER rsFile : rsFileMana[0])
                                    {
                                        if(!rsFile.FILE_PROFILE_NAME.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT))
                                        {
                                            isHasFileOfUser = true;
                                            break;
                                        }
                                    }
                            }
                            if(rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED)
                            {
                                for(FILE_MANAGER rsFile : rsFileMana[0])
                                {
                                    if(!rsFile.FILE_PROFILE_NAME.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT))
                                    {
                                        isHasFileOfUser = true;
                                        break;
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
                    %>
                    <%
                        if(isHasFileOfUser == true) {
                    %>
                    <div id="idDivShowFileMana">
                    <%
                        String sJSON = "";
                        CERTIFICATION_PURPOSE[][] rsPURPOSE = new CERTIFICATION_PURPOSE[1][];
                        db.S_BO_CERTIFICATION_PURPOSE_DETAIL_BY_CERTIFICATION_ATTR_TYPE(String.valueOf(rs[0][0].CERTIFICATION_PURPOSE_ID), "", rsPURPOSE);
                        if(rsPURPOSE[0].length > 0) {
                            sJSON = EscapeUtils.CheckTextNull(rsPURPOSE[0][0].FILE_PROPERTIES);
                        }
                        if(!"".equals(sJSON)) {
                            SessionUploadFileCert cartIP = (SessionUploadFileCert) session.getAttribute("sessUploadFileCert");
                            cartIP = new SessionUploadFileCert();
                            if(rsFileMana[0].length > 0) {
                                if(!"0".equals(isViewActionFileEnable)) {
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
                                session.setAttribute("sessUploadFileCert", cartIP);
                            }
                            boolean isEnableUploadFile = false;
                            if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED) {
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
                                    String sNameInID = attribute.getName().trim() + String.valueOf(jFile);
                                    if(attribute.getEnabled() == true) {
                    %>
                    <fieldset class="scheduler-border" style="clear: both;">
                        <legend class="scheduler-border"><%= sRemark%></legend>
                        <%
                            String cssDelete ="";
                            String cssDown ="";
                            if(isEnableUploadFile == true && !isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO)) {
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
                                jFile++;
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
                                                                "<td>" + fileNameLoad + "</td>" +
                                                                "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                "<td>" + sActionDown + ' ' + sReviewCRL +"</td>" +
    //                                                            "<td>" + sActionDown + ' ' + sActionCRL +"</td>" +
                                                                "</tr>";
                                                        } else {
                                                            content += "<tr>" +
                                                                "<td>" + obj[i].Index + "</td>" +
                                                                "<td>" + fileNameLoad + "</td>" +
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
    //                    function ViewTempFile(vFILE_MANAGER_ID, vFILE_NAME)
    //                    {
    //                        window.open('CertificateFileView.jsp?idFile='+vFILE_MANAGER_ID + '&idName=' + vFILE_NAME, '_blank');
    //                    }
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
                                                                    "<td>" + fileNameLoad + "</td>" +
                                                                    "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                    "<td>" + sActionDown + ' ' + sReviewCRL +"</td>" +
                                                                    "</tr>";
                                                            } else {
                                                                content += "<tr>" +
                                                                    "<td>" + obj[i].Index + "</td>" +
                                                                    "<td>" + fileNameLoad + "</td>" +
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
                                            attrTypeID: JS_STR_CERTIFICATION_ATTR_TYPE_ID_REVOKE
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
                        }
                    %>
                    <!-- END FILE MANAGER -->
                    
                    <!-- CSR View CLOSE BEGIN -->
                    <!--<
                        if(!"".equals(strCSR)) {
                    %>
                    <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;display: none;">
                        <label class="control-label123" id="idLblTitleCSR"></label>
                        <textarea style="height: 85px;" name="CSR" id="CSR" readonly class="form-control123"><= strCSR%></textarea>
                        <br/>
                        <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                            <a id="idLblTitleCSRCopy" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="copyToClipboard($('#CSR').val());"></a>&nbsp;
                            <a id="idLblTitleCSRDown" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="SaveCSR('<= ids%>', '<= anticsrf%>');"></a>
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
                    <
                        }
                    %>-->
                    <!-- CSR View CLOSE END -->
                    <%
                        if (!"".equals(strCertificate)) {
                    %>
                    <div class="col-sm-13" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <label id="idLblTitleCert" class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-10" style="padding-right: 0px;padding-top: 6px;text-align: left;padding-left: 50px;">
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
<!--                                CERT DOWNLOAD
                                <
                                    if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                    {
                                        if (SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                        {
                                %>
                                <a id="idLblTitleCertDown" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupDownloadCert('<= ids%>', '<= anticsrf%>');"></a>
                                &nbsp;
                                <script>$("#idLblTitleCertDown").text(global_fm_down);</script>
                                <
                                        }
                                    } else {
                                %>
                                <a id="idLblTitleCertDown" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupDownloadCert('<= ids%>', '<= anticsrf%>');"></a>
                                &nbsp;
                                <script>$("#idLblTitleCertDown").text(global_fm_down);</script>
                                <
                                    }
                                %>
                                CERT DOWNLOAD-->
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
                                    idAttrType: "",
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
                        $(document).ready(function () {
                            if('<%= sFristCA%>' !== "" && '<%= sFristCerPurpose%>' !== "" && '<%= sFristCerDurationOrProfileID%>' !== ""
                                 && '<%= String.valueOf(intPKI_FORMFACTOR_ID)%>' !== "")
                            {
                                LOAD_CERTIFICATION_PROFILE('<%= sFristCerDurationOrProfileID%>');
                            }
                        });
                    </script>
                    <input id="idHiddenCerCA" value="<%= sFristCA%>" style="display: none;"/>
                    <input id="idHiddenCerPurpose" value="<%= sFristCerPurpose%>" style="display: none;"/>
                    <input id="idHiddenCerDurationOrProfileID" value="<%= sFristCerDurationOrProfileID%>" style="display: none;"/>
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
                                                    '<div class="col-sm-7" style="padding-right: 0px;">'+
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
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePhoneContact"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly name="PHONE_CONTRACT" value="<%= strPHONE_CONTRACT%>" class="form-control123">
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
                                <input type="text" readonly name="EMAIL_CONTRACT" value="<%= strEMAIL_CONTRACT%>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleEmailContact").text(global_fm_email_contact);</script>
                    </div>
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
                            <label id="idLblTitleCreateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" type="text" name="REQUEST_TIME" readonly value="<%= strCREATED_DT%>"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleCreateDate").text(global_fm_date_create);
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
        <script src="../js/moment.min_limit.js"></script>
        <script src="../js/daterangepicker_limit.js"></script>
        <!--<script src="../style/custom.min.js"></script>
        <script src="../js/moment.min.js"></script>
        <script src="../js/daterangepicker.js"></script>-->
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