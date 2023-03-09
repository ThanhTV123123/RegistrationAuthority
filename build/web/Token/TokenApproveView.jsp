<%-- 
    Document   : TokenApproveView
    Created on : Jul 6, 2018, 8:54:41 AM
    Author     : THANH-PC
--%>

<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <!--<script type='text/javascript' src='../Css/jscolor.js'></script>-->
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <script type='text/javascript' src='../Css/jscolor.js'></script>
        <link rel="stylesheet" type="text/css" media="all" href="../js/daterangepicker.css" />
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            document.title = backoffice_title_view;
            $(document).ready(function () {
                $('.loading-gif').hide();
                localStorage.setItem("storeAnticsrfTokenApprove", null);
                $('[data-toggle="tooltipPrefix"]').tooltip();
            });
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../UserCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'RefreshApproveTokenSess'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "TokenApproveList.jsp";
                        }
                        else
                        {
                            window.location = "TokenApproveList.jsp";
                        }
                    }
                });
                return false;
            }
            function ValidateForm(vTOKEN_ID, vTOKEN_ATTR_ID, vTOKEN_ATTR_TYPE_ID, vLanguage, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../TokenCommon",
                    data: {
                        idParam: 'approvetoken',
                        TOKEN_ID: vTOKEN_ID,
                        vTOKEN_ATTR_ID: vTOKEN_ATTR_ID,
                        vTOKEN_ATTR_TYPE_ID: vTOKEN_ATTR_TYPE_ID,
                        LOCK_REASON: $("#idLOCK_REASON").val(),
                        vLanguage: vLanguage,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
//                            localStorage.setItem("EDIT_TOKENAPPROVE", vTOKEN_ID);
                            funSuccAlert(token_succ_edit, "TokenApproveList.jsp");
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
            function popupCancel(TokenID, idID, idCSRF)
            {
                swal({
                    title: "",
                    text: token_confirm_cancel_request,
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
                            url: "../TokenCommon",
                            data: {
                                idParam: 'cancelrequest',
                                TokenID: TokenID,
                                id: idID,
                                CsrfToken:idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var myStrings = sSpace(html);
                                var arr = myStrings.split('#');
                                if (arr[0] === "0")
                                {
                                    localStorage.setItem("EDIT_TOKENAPPROVE", idID);
                                    funSuccAlert(token_succ_cancel_request, "TokenApproveList.jsp");
//                                    funSuccLocalAlert(token_succ_cancel_request);TokenApproveList
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
        <style type="text/css">
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
            input[type=checkbox][disabled] {
            }
            input[type=checkbox][checked]{
                display: inline-block;
                opacity: 4;
            }
        </style>
    </head>
    <body class="nav-md">
        <%
            if ((session.getAttribute("sUserID")) != null) {
                ROLE_DATA[][] sessFunctionToken = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Token");
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
        %>
        <script>
            $(document).ready(function () {
                localStorage.setItem("storeAnticsrfTokenApprove", '<%= anticsrf%>');
            });
        </script>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <%                                        TOKEN[][] rs = new TOKEN[1][];
            try {
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                String SessRoleID = session.getAttribute("RoleID_ID").toString().trim();
                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
//                ids = seEncript.decrypt(ids);
                String sRole = session.getAttribute("RoleID_ID").toString().trim();
                if (EscapeUtils.IsInteger(ids) == true) {
                    db.S_BO_TOKEN_ATTR_DETAIL(ids, sessLanguageGlobal, rs);
                    if (rs[0].length > 0) {
        %>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleEdits" style="color: #36526D;"></span></h2>
            <script>$("#idLblTitleEdits").text(backoffice_title_view);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <%
                        if(rs[0][0].TOKEN_ATTR_STATE == Definitions.CONFIG_TOKEN_STATE_ID_PENDDING) {
                    %>
                    <%
                        if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_USER) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)
                            || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ACCOUNTANT) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ACCOUNTANT))
                        {
                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_APPROVED_TOKEN,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                    %>
                    <input type="button" id="btnSave" class="btn btn-info" onclick="ValidateForm('<%= rs[0][0].ID %>', '<%= rs[0][0].TOKEN_ATTR_ID %>', '<%= rs[0][0].TOKEN_ATTR_TYPE_ID %>', '<%= sessLanguageGlobal%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnSave").value = global_fm_approve;</script>
                    <%
                            }
                        } else {
                    %>
                    <input type="button" id="btnSave" class="btn btn-info" onclick="ValidateForm('<%= rs[0][0].ID %>', '<%= rs[0][0].TOKEN_ATTR_ID %>', '<%= rs[0][0].TOKEN_ATTR_TYPE_ID %>', '<%= sessLanguageGlobal%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnSave").value = global_fm_approve;</script>
                    <%
                        }
                    %>
                    <%
                        }
                    %>
                    <%
                        if(rs[0][0].TOKEN_ATTR_STATE == Definitions.CONFIG_TOKEN_STATE_ID_PENDDING) {
                    %>
                    <%
                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                    %>
                    <input type="button" id="btnDecline" class="btn btn-info" onclick="popupCancel('<%=rs[0][0].ID%>', '<%=rs[0][0].TOKEN_ATTR_ID %>');" />
                    <script>document.getElementById("btnDecline").value = global_fm_button_decline;</script>
                    <%
                        }
                    %>
                    <%
                        } else if(rs[0][0].TOKEN_ATTR_STATE == Definitions.CONFIG_TOKEN_STATE_ID_APPROVED) {
                            if(sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) 
                            {
                    %>
                    <%
                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                    %>
                    <input type="button" id="btnDecline" class="btn btn-info" onclick="popupCancel('<%=rs[0][0].ID%>', '<%=rs[0][0].TOKEN_ATTR_ID %>', '<%= anticsrf%>');" />
                    <script>document.getElementById("btnDecline").value = global_fm_button_decline;</script>
                    <%
                        }
                    %>
                    <%
                            }
                        }
                    %>
                    <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                    <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                    <script>
                        document.getElementById("btnClose").value = global_fm_button_close;
                    </script>
                </li>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <form name="myname" method="post" class="form-horizontal">
                <%
                    if(!"".equals(EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN))
                        && CommonFunction.checkViewTokenValid(EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN)) == true) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleTokenSN" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="TOKEN_SN" readonly class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN)%>" />
                        </div>
                    </div>
                    <script>$("#idLblTitleTokenSN").text(token_fm_tokenid);</script>
                </div>
                <%
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleStatus" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input id="TOKEN_ATTR_STATE" value="<%= EscapeUtils.CheckTextNull(rs[0][0].TOKEN_ATTR_STATE_DESC) %>" readonly class="form-control123"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleStatus").text(global_fm_Status);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleDetailBranch" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input name="DETAIL_BRANCH_DESC" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].BRANCH_DESC)%>" class="form-control123"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleDetailBranch").text(global_fm_Branch);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleType" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input name="USERNAME" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].TOKEN_ATTR_TYPE_DESC)%>" class="form-control123"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleType").text(global_fm_requesttype);</script>
                </div>
                <%
                    if(rs[0][0].TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_LOCK)
                    {
                        String sLockReasonReadonly = "readonly";
                        if(rs[0][0].TOKEN_ATTR_STATE == Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_PENDING)) {
                            sLockReasonReadonly = "";
                        }
                        String sLockReason = "";
                        String sVALUE_OLD = EscapeUtils.CheckTextNull(rs[0][0].VALUE);
                        if(!"".equals(sVALUE_OLD))
                        {
                            ObjectMapper objectMapper = new ObjectMapper();
                            ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                            sLockReason = valueATTR_Frist.getActionReason();
                        }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleLockReason" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input name="idLOCK_REASON" id="idLOCK_REASON" <%=sLockReasonReadonly%> value="<%= sLockReason%>" class="form-control123"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleLockReason").text(token_fm_reason_block);</script>
                </div>
                <%
                    }
                %>
                <%
                    if(rs[0][0].TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_UNLOCK) {
                %>
                <style type="text/css">
                    .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                    .table > thead > tr > th{border-bottom: none;}
                    .btn{margin-bottom: 0px;}
                    .table{font-size: 12px;}
                </style>
                <%
                    int sSumCert = 0;
                    CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                    db.S_BO_CERTIFICATION_LIST_BY_TOKEN_ID(String.valueOf(rs[0][0].ID), sessLanguageGlobal, rsCert);
                    if (rsCert != null && rsCert[0].length > 0) {
                        sSumCert = rsCert[0].length;
                %>
                <fieldset class="scheduler-border" style="clear: both;">
                    <legend class="scheduler-border" id="idLblTitleCertInfoUnlock"></legend>
                    <script>$("#idLblTitleCertInfoUnlock").text(cert_title_register_cert);</script>
                    <div class="table-responsive">
                        <table id="idDetailTableList" class="table table-bordered table-striped projects">
                            <thead>
                            <th id="idHeaderDetailSTT"></th>
                            <th id="idHeaderDetailCompany"></th>
                            <th id="idHeaderDetailMST"></th>
                            <th id="idHeaderDetailPersonal"></th>
                            <th id="idHeaderDetailCMND"></th>
                            <th id="idHeaderDetailDomain"></th>
                            <script>
                                $("#idHeaderDetailSTT").text(global_fm_STT);
                                $("#idHeaderDetailCompany").text(global_fm_grid_company);
                                $("#idHeaderDetailMST").text(global_fm_enterprise_id);
                                $("#idHeaderDetailPersonal").text(global_fm_grid_personal);
                                $("#idHeaderDetailCMND").text(global_fm_personal_id);
                                $("#idHeaderDetailDomain").text(global_fm_grid_domain);
                            </script>
                            </thead>
                            <tbody>
                                <%
                                    int j=1;
                                    for (CERTIFICATION temp1 : rsCert[0]) {
//                                        String sSMTOrMSN = EscapeUtils.CheckTextNull(temp1.TAX_CODE);
//                                        if("".equals(sSMTOrMSN)) {
//                                            sSMTOrMSN = EscapeUtils.CheckTextNull(temp1.BUDGET_CODE);
//                                        }
//                                        if("".equals(sSMTOrMSN)) {
//                                            sSMTOrMSN = EscapeUtils.CheckTextNull(temp1.DECISION);
//                                        }
//                                        String sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_ID);
//                                        if("".equals(sCMNDOrHC)) {
//                                            sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_EID);
//                                        }
//                                        if("".equals(sCMNDOrHC)) {
//                                            sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.PASSPORT);
//                                        }
                                %>
                                <tr>
                                    <td style="text-align: center;"><%= com.convertMoney(j)%></td>
                                    <td><%= EscapeUtils.CheckTextNull(temp1.COMPANY_NAME)%></td>
                                    <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= temp1.ENTERPRISE_ID_REMARK%>"><%= temp1.ENTERPRISE_ID%></a></td>
                                    <td><%= EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME)%></td>
                                    <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= temp1.PERSONAL_ID_REMARK%>"><%= temp1.PERSONAL_ID%></a></td>
                                    <td><%= EscapeUtils.CheckTextNull(temp1.DOMAIN_NAME)%></td>
                                </tr>
                                <%
                                        j++;
                                    }
                                %>
                            </tbody>
                        </table>
                        <div id="greenDetail" style="margin: 5px 0 10px 0;"> </div>
                        <script>
                            $(document).ready(function () {
                                var sNum = '<%= sSumCert%>';
                                console.log(sNum);
                                if (parseInt(sNum) > 0)
                                {
                                    $('#greenDetail').smartpaginator({totalrecords: sNum, recordsperpage: 10, datacontainer: 'idDetailTableList', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
                                }
                            });
                        </script>
                    </div>
                </fieldset>
                <%
                    }
                %>
                <%
                    String sJSONUnlock = EscapeUtils.CheckTextNull(rs[0][0].VALUE);
                    if(!"".equals(sJSONUnlock))
                    {
                        ObjectMapper objectMapper = new ObjectMapper();
                        ATTRIBUTE_VALUES jsonGroup = objectMapper.readValue(sJSONUnlock, ATTRIBUTE_VALUES.class);
                        String strPhone="";
                        String strEmail = "";
                        boolean booOTPEnabled = false;
                        if (jsonGroup.getAttributeData() != null) {
                            if (jsonGroup.getAttributeData().getRequestUnlockObject() != null) {
                                if(!"".equals(EscapeUtils.CheckTextNull(jsonGroup.getAttributeData().getRequestUnlockObject().PHONE_NUMBER)))
                                {
                                    strPhone = jsonGroup.getAttributeData().getRequestUnlockObject().PHONE_NUMBER;
                                }
                                if(!"".equals(EscapeUtils.CheckTextNull(jsonGroup.getAttributeData().getRequestUnlockObject().EMAIL)))
                                {
                                    strEmail = jsonGroup.getAttributeData().getRequestUnlockObject().EMAIL;
                                }
                                booOTPEnabled = jsonGroup.getAttributeData().getRequestUnlockObject().otpAuthenticationEnabled;
                            }
                        }
                        if(!"".equals(strPhone) || !"".equals(strEmail)) {
                %>
                <fieldset class="scheduler-border" style="clear: both;">
                    <legend class="scheduler-border" id="idLblTitleGroupUnlock"></legend>
                    <script>$("#idLblTitleGroupUnlock").text(token_group_unlock);</script>
                    <div class="form-group">
                        <div class="col-sm-6">
                            <%
                                if(booOTPEnabled == false) {
                            %>
                            <label class="control-label123" id="idLblTitlePhone"></label>
                            <textarea type="Text" id="strNAME_LINK" readonly name="strNAME_LINK" class="form-control123"><%= strPhone%></textarea>
                            <script>$("#idLblTitlePhone").text(global_fm_phone);</script>
                            <%
                                } else {
                            %>
                            <label class="control-label123" id="idLblTitleDetailOTP"></label>
                            <input type="Text" id="DETAIL_OTP" readonly name="DETAIL_OTP" class="form-control123"></input>
                            <script>
                                $("#idLblTitleDetailOTP").text(global_fm_Method);
                                $("#DETAIL_OTP").val(global_button_grid_OTP);
                            </script>
                            <%
                                }
                            %>
                        </div>
                        <div class="col-sm-6">
                            <label id="idLblTitleEmail" class="control-label123"></label>
                            <textarea type="Text" id="strLINK_VALUE" readonly name="strLINK_VALUE" class="form-control123"><%= strEmail%></textarea>
                        </div>
                        <script>$("#idLblTitleEmail").text(global_fm_email);</script>
                    </div>
                </fieldset>
                <%
                            }
                        }
                    }
                %>
                <%
                    if(rs[0][0].TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PUSH_NOTFICATION) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String sJSONPush = EscapeUtils.CheckTextNull(rs[0][0].VALUE);
                        String strCOLOR_TEXT = "";
                        String strCOLOR_BKGR = "";
                        String strLINK_NOTICE = "";
                        String strNOTICE_INFO = "";
                        if(!"".equals(sJSONPush)) {
                            ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                            ATTRIBUTE_VALUES itemParsePush = objectMapper.readValue(sJSONPush, ATTRIBUTE_VALUES.class);
                            dataATTR = itemParsePush.getAttributeData();
                            PUSH_TOKEN jsonParse = dataATTR.getSticker();
                            strCOLOR_TEXT=jsonParse.PUSH_NOTICE_TEXT_COLOR;
                            strCOLOR_BKGR=jsonParse.PUSH_NOTICE_BGR_COLOR;
                            strLINK_NOTICE=jsonParse.PUSH_NOTICE_URL;
                            strNOTICE_INFO=jsonParse.PUSH_NOTICE_CONTENT;
                        }
                %>
                <fieldset class="scheduler-border" style="clear: both;">
                    <legend class="scheduler-border" id="idLblTitleGroupNotifi"></legend>
                    <script>$("#idLblTitleGroupNotifi").text(token_group_notification);</script>
                    <div class="form-group">
                        <div class="col-sm-6">
                            <label class="control-label123" id="idLblTitleColorText"></label>
                            <input type="text" disabled id="strCOLOR_TEXT" name="strCOLOR_TEXT" disabled class="form-control123 jscolor" value="<%= strCOLOR_TEXT%>"/>
                        </div>
                        <script>$("#idLblTitleColorText").text(token_fm_colortext);</script>
                        <div class="col-sm-6">
                            <label class="control-label123" id="idLblTitleColorBkgr"></label>
                            <input type="text" disabled id="strCOLOR_BKGR" name="strCOLOR_BKGR" disabled class="form-control123 jscolor" value="<%= strCOLOR_BKGR%>"/>
                        </div>
                        <script>$("#idLblTitleColorBkgr").text(token_fm_colorgkgd);</script>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-6">
                            <label class="control-label123" id="idLblTitleLinkNote"></label>
                            <textarea type="Text" id="strLINK_NOTICE" readOnly name="strLINK_NOTICE" style="height: 50px;" class="form-control123"><%= strLINK_NOTICE%></textarea>
                        </div>
                        <script>$("#idLblTitleLinkNote").text(token_fm_noticelink);</script>
                        <div class="col-sm-6">
                            <label class="control-label123" id="idLblTitleInfoNote"></label>
                            <textarea type="Text" id="strNOTICE_INFO" readOnly name="strNOTICE_INFO" style="height: 50px;" class="form-control123"><%= strNOTICE_INFO%></textarea>
                        </div>
                        <script>$("#idLblTitleInfoNote").text(token_fm_noticeinfor);</script>
                    </div>
                </fieldset>
                <%
                    }
                %>
                <%
                    if(rs[0][0].TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_MENU_LINK) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String sJSONMenu = EscapeUtils.CheckTextNull(rs[0][0].VALUE);
                        String strNAME_LINK="";
                        String strLINK_VALUE="";
                        if(!"".equals(sJSONMenu)) {
                            ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                            ATTRIBUTE_VALUES itemParsePush = objectMapper.readValue(sJSONMenu, ATTRIBUTE_VALUES.class);
                            dataATTR = itemParsePush.getAttributeData();
                            MENULINK_TOKEN jsonParse = dataATTR.getMenulink();
                            strNAME_LINK=jsonParse.MENU_LINK_NAME;
                            strLINK_VALUE=jsonParse.MENU_LINK_URL;
                        }
                %>
                <fieldset class="scheduler-border">
                    <legend class="scheduler-border" id="idLblTitleGroupDynamic"></legend>
                    <script>$("#idLblTitleGroupDynamic").text(token_group_dynamic);</script>
                    <div class="form-group">
                        <div class="col-sm-6">
                            <label id="idLblTitleLinkName" class="control-label123"></label>
                            <textarea type="Text" disabled id="strNAME_LINK" name="strNAME_LINK" class="form-control123"><%= strNAME_LINK%></textarea>
                        </div>
                        <script>$("#idLblTitleLinkName").text(token_fm_linkname);</script>
                        <div class="col-sm-6">
                            <label id="idLblTitleLinkValue" class="control-label123"></label>
                            <textarea type="Text" disabled id="strLINK_VALUE" name="strLINK_VALUE" class="form-control123"><%= strLINK_VALUE%></textarea>
                        </div>
                        <script>$("#idLblTitleLinkValue").text(token_fm_linkvalue);</script>
                    </div>
                </fieldset>
                <%
                    }
                %>
                <%
                    String sUserCreate = "";
                    String sDateCreate = "";
                    String sUserApprove = "";
                    String sDateApprove = "";
                    String sVALUE = EscapeUtils.CheckTextNull(rs[0][0].VALUE);
                    if(!"".equals(sVALUE))
                    {
                        ObjectMapper objectMapper = new ObjectMapper();
                        ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE, ATTRIBUTE_VALUES.class);
                        sUserCreate = EscapeUtils.CheckTextNull(valueATTR_Frist.getCreateUser());
                        sDateCreate = CommonFunction.dateConvertString(valueATTR_Frist.getCreateDt());
                        sUserApprove = EscapeUtils.CheckTextNull(valueATTR_Frist.getApproveUser());
                        sDateApprove = CommonFunction.dateConvertString(valueATTR_Frist.getApproveDt());

                %>
                <%
                    if(!"".equals(sUserCreate)) {
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCreateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="CREATED_BY" readonly value="<%= sUserCreate%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleCreateDate").text(global_fm_user_create);
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
                        <label id="idLblTitleCreateBy" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="REQUEST_TIME" readonly value="<%= sDateCreate%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleCreateBy").text(global_fm_date_create);
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
                        $("#idLblTitleModifiedBy").text(global_fm_user_approve);
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
                        $("#idLblTitleModifiedDate").text(global_fm_date_approve);
                    </script>
                </div>
                <%
                    }
                %>
                <%
                    }
                %>
            </form>
        </div>
        <%
        } else {
        %>
        <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
            <label style="color: red;" id="idLblTitleNoData"></label>
            <script>
                $("#idLblTitleNoData").text(global_no_data);
            </script>
        </div>
        <%
            }
        } else {
        %>
        <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
            <label style="color: red;" id="idLblTitleNoData"></label>
            <script>
                $("#idLblTitleNoData").text(global_no_data);
            </script>
        </div>
        <%
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
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../js/moment.min.js"></script>
        <script src="../js/daterangepicker.js"></script>
        <!--<script src="../style/custom.min.js"></script>-->
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
