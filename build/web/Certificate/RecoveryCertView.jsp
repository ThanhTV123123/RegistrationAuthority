<%-- 
    Document   : RecoveryCertView
    Created on : Jul 4, 2019, 9:45:02 AM
    Author     : THANH-PC
--%>

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
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            //document.title = certlist_title_recovery;
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
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
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                String SessRoleID = session.getAttribute("RoleID_ID").toString().trim();
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <script>
            function UpdateForm(idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../ReqApproveDeclineCommon",
                    data: {
                        idParam: 'recoverycert',
                        sID: $("#sID").val(),
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            if('<%= SessAgentID%>' === JS_STR_AGENT_ROOT)
                            {
                                localStorage.setItem("EDIT_RENEWCERT_RECOVERY", $("#sID").val());
                                funSuccAlert(certlist_succ_recovery_ca, "RenewCertList.jsp");
                            }
                            else
                            {
                                localStorage.setItem("EDIT_RENEWCERT_RECOVERY", $("#sID").val());
                                funSuccAlert(certlist_succ_recovery, "RenewCertList.jsp");
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
            function pushNotificationApprove(name, desc) {
                var xmlhttp = new XMLHttpRequest();
                xmlhttp.open("POST", "../PushNotiRequestApprove?t="+new Date(), false);
                xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                xmlhttp.send("name="+name+"&message="+desc);
            }
        </script>
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
                            String strCERTIFICATION_STATE_DESC = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_STATE_DESC);
//                            String strCSR = EscapeUtils.CheckTextNull(rs[0][0].CSR);
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
                                strFEE_AMOUNT = com.convertMoney(rs[0][0].FEE_AMOUNT);
                            }
                            String strCREATED_DT = EscapeUtils.CheckTextNull(rs[0][0].CREATED_DT);
                            String strCREATED_BY = EscapeUtils.CheckTextNull(rs[0][0].CREATED_BY);
                            String strMODIFIED_DT = EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_DT);
                            String strMODIFIED_BY = EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_BY);
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
                            String strCERTIFICATION_PROFILE_ID = String.valueOf(rs[0][0].CERTIFICATION_PROFILE_ID);
        %>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEdits"></span></h2>
            <script>$("#idLblTitleEdits").text(certlist_title_recovery);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <%
                        if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_PERMANENT_DISABLE
                            || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                            || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_PERMANENT_DISABLE
                            || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE)
                        {
                    %>
                    <%
                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_RECOVERED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                    %>
                    <input type="button" id="btnApprove" class="btn btn-info" onclick="UpdateForm('<%=anticsrf%>');" />
                    <script>document.getElementById("btnApprove").value = global_fm_button_recovery;</script>
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
                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                <input type="hidden" name="idHiddenDN" id="idHiddenDN" value="<%=EscapeUtils.escapeHtmlDN(strDN)%>"/>
<!--                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleReason" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="DeclineReason" id="DeclineReason" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleReason").text(global_fm_revoke_desc);</script>
                </div>-->
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
                        <label id="idLblTitleCertState" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="CERTIFICATION_STATE_DESC" value="<%= strCERTIFICATION_STATE_DESC%>" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleCertState").text(global_fm_Status);</script>
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
                                <input type="text" id="SuspendReason" name="SuspendReason" value="<%= EscapeUtils.escapeHtmlDN(sReasonSuspendValue)%>" class="form-control123" disabled />
                            </div>
                        </div>
                        <script>$("#idLblTitleSuspendReason").text(global_fm_suspend_desc);</script>
                    </div>
                <%
                        }
                    }
                %>
                <%
                    if(rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                        || rs[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE)
                    {
                        String sRELEASE_DT = EscapeUtils.CheckTextNull(rs[0][0].RELEASE_DT);
                        if(!"".equals(sRELEASE_DT)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleSuspendTime" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="SUSPEND_TIME" id="SUSPEND_TIME" value="<%= sRELEASE_DT%>" readonly class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleSuspendTime").text(global_fm_times_recovery);</script>
                </div>
                <%
                        }
                    }
                %>
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
                        <label id="idLblTitleCertCA" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly id="CERTIFICATION_AUTHORITY" value="<%= EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_AUTHORITY_DESC) %>" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleCertCA").text(global_fm_ca);</script>
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
                        <label id="idLblTitleCertDuration" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly id="DURATION" value="<%= EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_PROFILE_DESC) %>" class="form-control123">
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
                <%
                    String sViewDeleteWhenRevoke = "none";
                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                        if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                            || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)) {
                            if(!sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN) && !sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                                && !sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN))
                            {
                                sViewDeleteWhenRevoke = "";
                            }
                        } else {
                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                sViewDeleteWhenRevoke = "";
                            }
                        }
                    }
                %>
                
                <div class="col-sm-13" style="padding-left: 0;clear: both;display: none;">
                    <div class="form-group">
                        <fieldset class="scheduler-border">
                            <legend class="scheduler-border" id="idLblTitleComponentDN"></legend>
                            <div style="padding: 0px 0px 0px 0px;margin: 0;" id="idDivViewComponentDN"></div>
                            <script>$("#idLblTitleComponentDN").text(global_fm_csr_info_cts);</script>
                        </fieldset>
                    </div>
                </div>
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
                            <a id="idAShow" style="cursor: pointer; color: blue; text-decoration: underline;display: none;" onclick="popupViewCSRS();"></a>
                            <a id="idAHide" style="cursor: pointer; color: blue; text-decoration: underline;display: none;" onclick="popupHideCTS();"></a>
                            &nbsp;
                            <script>$("#idAShow").text(global_fm_detail);$("#idAHide").text(global_fm_hide);</script>
                            <%
                                }
                            %>
<!--                            CERT DOWNLOAD
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
                <div id="idViewCSR" class="form-group" style="display: none;padding: 10px 0px 0 0px;margin: 0;clear: both;">
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
                                <label id="idLblTitleCompanyPrin" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <textarea id="idCompanyPrin" class="form-control123" readonly="true" name="idCompanyPrin" style="height: 160px;"><%= strSubjectDN%></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleIssuerPrin" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <textarea id="idIssuerPrin" class="form-control123" readonly="true" name="idIssuerPrin" style="height: 75px;"><%= strIssuerDN%></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCertValid" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input id="idCertValid" class="form-control123" type="text" readonly="true" value="<%= strNotBefore%>"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCertExpire" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
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
