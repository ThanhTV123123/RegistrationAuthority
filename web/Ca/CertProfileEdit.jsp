<%-- 
    Document   : CertProfileEdit
    Created on : Jul 25, 2018, 5:31:43 PM
    Author     : THANH-PC
--%>

<%@page import="java.util.AbstractList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="vn.ra.process.AddIPRelying"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">-->
        <link href="../style/custom.min.css" rel="stylesheet">
        <!--<link href="../Css/active/bootstrap-switch.css" rel="stylesheet">-->
        <!--<script src="../js/Language.js"></script>-->
        <script src="../js/process_javajs.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
//            document.title = certprofile_title_edit;
//            changeFavicon("../");
            $(document).ready(function () {
                $('.loading-gif').hide();
                $("#idLblTitleEdits").text(certprofile_title_edit);
                $("#idLblTitleCode").text(certprofile_fm_code);
//                $("#idLblNoteCode").text(global_fm_require_label);
                $("#idLblTitleRemark").text(global_fm_remark_vn);
                $("#idLblNoteRemark").text(global_fm_require_label);
                $("#idLblTitleRemark_En").text(global_fm_remark_en);
                $("#idLblNoteRemark_En").text(global_fm_require_label);
                $("#idLblTitleAmountFee").text(global_fm_amount_fee);
                $("#idLblNoteAmountFee").text(global_fm_require_label);
                
                
                $("#idLblTitleDuration").text(global_fm_duration);
                $("#idLblNoteDuration").text(global_fm_require_label);
                $("#idLblTitleDateFree").text(global_fm_date_free);
//                $("#idLblNoteDateFree").text(global_fm_require_label);
                $("#idLblTitleEntity").text(global_fm_entity_ejbca);
                $("#idLblNoteEntity").text(global_fm_require_label);
                $("#idLblTitleCA").text(global_fm_ca);
                $("#idLblTitleCertPurpose").text(global_fm_certpurpose);
                $("#idLblTitleCertAlgorithm").text(global_fm_certalgorithm);
                $("#idLblTitleCertInfo").text(global_fm_csr_info_cts);
                $("#idLblTitleTableSTT").text(global_fm_STT);
                
                $("#idLblTitleCreateUser").text(global_fm_user_create);
                $("#idLblTitleCreateDate").text(global_fm_date_create);
                $("#idLblTitleUpdateUser").text(global_fm_user_endupdate);
                $("#idLblTitleUpdateDate").text(global_fm_date_endupdate);
                $("#idLblTitleNoData").text(global_no_data);
            });
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.citycode.value))
                {
                    document.myname.citycode.focus();
                    funErrorAlert(policy_req_empty + certprofile_fm_code);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.Remark.value))
                {
                    document.myname.Remark.focus();
                    funErrorAlert(policy_req_empty + global_fm_remark_vn);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.Remark_EN.value))
                {
                    document.myname.Remark_EN.focus();
                    funErrorAlert(policy_req_empty + global_fm_remark_en);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.AMOUNT.value.replace(/,/g, '')))
                {
                    document.myname.AMOUNT.focus();
                    funErrorAlert(policy_req_empty + global_fm_amount_fee);
                    return false;
                } else {
                    if (!JSCheckNumericField(document.myname.AMOUNT.value.replace(/,/g, '')))
                    {
                        document.myname.AMOUNT.focus();
                        funErrorAlert(global_fm_amount_fee + policy_req_number);
                        return false;
                    }
                }
                if (JSCheckEmptyField(document.myname.RENEWAL_AMOUNT.value.replace(/,/g, '')))
                {
                    if (!JSCheckNumericField(document.myname.RENEWAL_AMOUNT.value.replace(/,/g, '')))
                    {
                        document.myname.RENEWAL_AMOUNT.focus();
                        funErrorAlert(global_fm_amount_renewal + policy_req_number);
                        return false;
                    }
                }
                if (JSCheckEmptyField(document.myname.CHANGE_AMOUNT.value.replace(/,/g, '')))
                {
                    if (!JSCheckNumericField(document.myname.CHANGE_AMOUNT.value.replace(/,/g, '')))
                    {
                        document.myname.CHANGE_AMOUNT.focus();
                        funErrorAlert(global_fm_amount_changeinfo + policy_req_number);
                        return false;
                    }
                }
                if (JSCheckEmptyField(document.myname.REISSUE_AMOUNT.value.replace(/,/g, '')))
                {
                    if (!JSCheckNumericField(document.myname.REISSUE_AMOUNT.value.replace(/,/g, '')))
                    {
                        document.myname.REISSUE_AMOUNT.focus();
                        funErrorAlert(global_fm_amount_reissue + policy_req_number);
                        return false;
                    }
                }
                if (JSCheckEmptyField(document.myname.GOVERNMENT_AMOUNT.value.replace(/,/g, '')))
                {
                    if (!JSCheckNumericField(document.myname.GOVERNMENT_AMOUNT.value.replace(/,/g, '')))
                    {
                        document.myname.GOVERNMENT_AMOUNT.focus();
                        funErrorAlert(global_fm_amount_goverment + policy_req_number);
                        return false;
                    }
                }
                if (JSCheckEmptyField(document.myname.TOKEN_AMOUNT.value.replace(/,/g, '')))
                {
                    if (!JSCheckNumericField(document.myname.TOKEN_AMOUNT.value.replace(/,/g, '')))
                    {
                        document.myname.TOKEN_AMOUNT.focus();
                        funErrorAlert(global_fm_amount_token + policy_req_number);
                        return false;
                    }
                }
                if (!JSCheckEmptyField(document.myname.DURATION.value.replace(/,/g, '')))
                {
                    document.myname.DURATION.focus();
                    funErrorAlert(policy_req_empty + global_fm_duration);
                    return false;
                } else {
                    if (!JSCheckNumericField(document.myname.DURATION.value.replace(/,/g, '')))
                    {
                        document.myname.DURATION.focus();
                        funErrorAlert(global_fm_duration + policy_req_number);
                        return false;
                    }
                }
                if (JSCheckEmptyField(document.myname.DURATION_FREE.value.replace(/,/g, '')))
                {
                    if (!JSCheckNumericField(document.myname.DURATION_FREE.value.replace(/,/g, '')))
                    {
                        document.myname.DURATION_FREE.focus();
                        funErrorAlert(global_fm_date_free + policy_req_number);
                        return false;
                    }
                }
                if (!JSCheckEmptyField(document.myname.ENTITY_EJBCA.value))
                {
                    document.myname.ENTITY_EJBCA.focus();
                    funErrorAlert(policy_req_empty + global_fm_entity_ejbca);
                    return false;
                }
                var sCheckActiveFlag = "0";
                if ($("#ActiveFlag").is(':checked'))
                {
                    sCheckActiveFlag = "1";
                }
                var sCheckIssue = "1";
                if ($("#ISSUE_ENABLED_EDIT").is(':checked'))
                {
                    sCheckIssue = "0";
                }
                var sCheckEnterpriseUID = "0";
                if ($("#enterpriseUID").is(':checked'))
                {
                    sCheckEnterpriseUID = "1";
                }
                var sCheckPersonalUID = "0";
                if ($("#personalUID").is(':checked'))
                {
                    sCheckPersonalUID = "1";
                }
                var sCheckAutoSynch = "0";
                if ($("#AUTO_SYNCH_EDIT").is(':checked'))
                {
                    sCheckAutoSynch = "1";
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../CertProfileCommon",
                    data: {
                        idParam: 'editcertprofile',
                        id: document.myname.ID.value,
                        Code: document.myname.citycode.value,
                        DURATION: document.myname.DURATION.value,
                        DURATION_FREE: document.myname.DURATION_FREE.value,
                        ENTITY_EJBCA: document.myname.ENTITY_EJBCA.value,
                        AMOUNT: document.myname.AMOUNT.value,
                        pCHANGE_AMOUNT: document.myname.CHANGE_AMOUNT.value,
                        pREISSUE_AMOUNT: document.myname.REISSUE_AMOUNT.value,
                        pGOVERNMENT_AMOUNT: document.myname.GOVERNMENT_AMOUNT.value,
                        pRENEWAL_AMOUNT: document.myname.RENEWAL_AMOUNT.value,
                        pTOKEN_AMOUNT: document.myname.TOKEN_AMOUNT.value,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.Remark_EN.value,
                        CERTIFICATION_AUTHORITY: document.myname.CERTIFICATION_AUTHORITY.value,
                        CERTIFICATION_PURPOSE: document.myname.CERTIFICATION_PURPOSE.value,
                        CERTIFICATION_ALGORITHM: document.myname.CERTIFICATION_ALGORITHM.value,
                        ActiveFlag: sCheckActiveFlag,
//                        sIssueEnaled: sCheckIssue,
                        pOPTION: document.myname.pOPTIONShow.value,
                        sCheckEnterpriseUID: sCheckEnterpriseUID,
                        sCheckPersonalUID: sCheckPersonalUID,
                        sCheckAutoSynch: sCheckAutoSynch,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            localStorage.setItem("EDIT_CERTPROFILE", document.myname.ID.value);
                            funSuccAlert(certprofile_succ_edit, "CertProfileList.jsp");
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
            function DeleteProperties(Name, Prefix, idCSRF)
            {
                $.ajax({
                    type: "post",
                    url: "../CertProfileCommon",
                    data: {
                        idParam: 'deleteproperties',
                        Name: Name,
                        Prefix: Prefix,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccAlert(certprofile_succ_edit, "CertProfileEdit.jsp?id=" + $("#ID").val());
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
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'RefreshCertProfileSess'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "CertProfileList.jsp";
                        }
                        else
                        {
                            window.location = "CertProfileList.jsp";
                        }
                    }
                });
                return false;
            }
        </script>
        <style type="text/css">
            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
            .table > thead > tr > th{border-bottom: none;}
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.2em 5px 1.2em !important;
                margin: 0 0 1.1em 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
        </style>
    </head>
    <body>
        <%
            String anticsrf = "";
            anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            session.setAttribute("SessAddIPRelying", null);
            session.setAttribute("SessComponentCertTypeAdd", null);
        %> 
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <div class="x_panel">
            <div class="x_title">
                <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEdits"></span></h2>
                <ul class="nav navbar-right panel_toolbox">
                    <li style="padding-right: 10px;">
                        <input id="btnSave" type="button" data-switch-get="state" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');"/>
                        <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                        <script>
                            document.getElementById("btnSave").value = global_fm_button_edit;
                            document.getElementById("btnClose").value = global_fm_button_close;
                        </script>
                    </li>
                </ul>
                <div class="clearfix"></div>
            </div>
            <div class="x_content">
                <%
                    CERTIFICATION_PROFILE[][] rs = new CERTIFICATION_PROFILE[1][];
                    try {
                        String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                        String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                        if (EscapeUtils.IsInteger(ids) == true) {
                            db.S_BO_CERTIFICATION_PROFILE_DETAIL(ids, rs);
                            if (rs[0].length > 0) {
                                String sValueSynch = rs[0][0].AUTO_ASYNC;
                                String sShowSynch = "0";
                                GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                                if (sessGeneralPolicy[0].length > 0) {
                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                    {
                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_AUTO_ASYNC_ALL_CERTIFICATE_WITH_NEAC))
                                        {
                                            sShowSynch = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                            break;
                                        }
                                    }
                                }
                                String strID =String.valueOf(rs[0][0].ID);
                                String strBranchCode = EscapeUtils.CheckTextNull(rs[0][0].NAME);
                                String strDateLimit = EscapeUtils.CheckTextNull(rs[0][0].CREATED_DT);
                                String sProperties = EscapeUtils.CheckTextNull(rs[0][0].PROPERTIES);
                                CERTIFICATION_TYPE_COMPONENT[][] resProfileData = new CERTIFICATION_TYPE_COMPONENT[1][];
                                CommonFunction.getJsonComponentForCert(sProperties, resProfileData);
                %>
                <form name="myname" method="post" class="form-horizontal">
                    <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input value="<%= strID%>" class="form-control123" readonly style="display: none;" id="ID" name="ID">
                                <input value="<%= strBranchCode%>" class="form-control123" readonly id="citycode" name="citycode" oninput="onBlurToUppercase(this);" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_NAME %>">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleRemark"></label>
                                <label class="CssRequireField" id="idLblNoteRemark"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" value="<%= rs[0][0].REMARK %>" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK %>" id="Remark" name="Remark"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleRemark_En"></label>
                                <label class="CssRequireField" id="idLblNoteRemark_En"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" value="<%= rs[0][0].REMARK_EN %>" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK %>" id="Remark_EN" name="Remark_EN"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleAmountFee"></label>
                                <label class="CssRequireField" id="idLblNoteAmountFee"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" value="<%= com.convertMoneyAnotherZero(rs[0][0].AMOUNT) %>"  oninput="autoConvertMoney(this.value, $('#AMOUNT'))" id="AMOUNT" name="AMOUNT"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleAmountRenew"></label>
                                <label class="CssRequireField" id="idLblNoteAmountRenew"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" id="RENEWAL_AMOUNT" name="RENEWAL_AMOUNT" value="<%= com.convertMoneyAnotherZero(rs[0][0].RENEWAL_AMOUNT) %>" oninput="autoConvertMoney(this.value, $('#RENEWAL_AMOUNT'))"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblNoteAmountRenew").text(global_fm_require_label);
                            $("#idLblTitleAmountRenew").text(global_fm_amount_renewal);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleAmountChangeInfo"></label>
                                <label class="CssRequireField" id="idLblNoteAmounChangeInfo"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" id="CHANGE_AMOUNT" name="CHANGE_AMOUNT" value="<%= com.convertMoneyAnotherZero(rs[0][0].CHANGE_AMOUNT) %>" oninput="autoConvertMoney(this.value, $('#CHANGE_AMOUNT'))"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblNoteAmountChangeInfo").text(global_fm_require_label);
                            $("#idLblTitleAmountChangeInfo").text(global_fm_amount_changeinfo);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleAmountReissue"></label>
                                <label class="CssRequireField" id="idLblNoteAmountReissue"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" id="REISSUE_AMOUNT" name="REISSUE_AMOUNT" value="<%= com.convertMoneyAnotherZero(rs[0][0].REISSUE_AMOUNT) %>" oninput="autoConvertMoney(this.value, $('#REISSUE_AMOUNT'))"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblNoteAmountReissue").text(global_fm_require_label);
                            $("#idLblTitleAmountReissue").text(global_fm_amount_reissue);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleAmountGoverment"></label>
                                <label class="CssRequireField" id="idLblNoteAmountGoverment"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" id="GOVERNMENT_AMOUNT" name="GOVERNMENT_AMOUNT" value="<%= com.convertMoneyAnotherZero(rs[0][0].GOVERNMENT_AMOUNT) %>" oninput="autoConvertMoney(this.value, $('#GOVERNMENT_AMOUNT'))"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblNoteAmountGoverment").text(global_fm_require_label);
                            $("#idLblTitleAmountGoverment").text(global_fm_amount_goverment);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleAmountToken"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" id="TOKEN_AMOUNT" name="TOKEN_AMOUNT" value="<%= com.convertMoneyAnotherZero(rs[0][0].TOKEN_AMOUNT) %>" oninput="autoConvertMoney(this.value, $('#TOKEN_AMOUNT'))"/>
                            </div>
                        </div>
                        <script>$("#idLblTitleAmountToken").text(global_fm_amount_token);</script>
                    </div>
                    
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleDuration"></label>
                                <label class="CssRequireField" id="idLblNoteDuration"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" id="DURATION" name="DURATION" value="<%= com.convertMoneyAnotherZero(rs[0][0].DURATION) %>" oninput="autoConvertMoney(this.value, $('#DURATION'))"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleDateFree"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" id="DURATION_FREE" name="DURATION_FREE" value="<%= com.convertMoneyAnotherZero(rs[0][0].DURATION_FREE) %>" oninput="autoConvertMoney(this.value, $('#DURATION_FREE'))"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleEntity"></label>
                                <label class="CssRequireField" id="idLblNoteEntity"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <textarea class="form-control123" id="ENTITY_EJBCA" style="height: 60px;" name="ENTITY_EJBCA"><%= EscapeUtils.CheckTextNull(rs[0][0].ENTITY_EJBCA) %></textarea>
                                <!--<input class="form-control123" id="ENTITY_EJBCA" name="ENTITY_EJBCA" value="<=  %>"/>-->
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCA" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <select name="CERTIFICATION_AUTHORITY" id="CERTIFICATION_AUTHORITY" class="form-control123" disabled>
                                    <%
                                        CERTIFICATION_AUTHORITY[][] rsNoNull = new CERTIFICATION_AUTHORITY[1][];
                                        try {
                                            db.S_BO_CERTIFICATION_AUTHORITY_COMBOBOX(sessLanguageGlobal, rsNoNull);
                                            if (rsNoNull[0].length > 0) {
                                                for (int i = 0; i < rsNoNull[0].length; i++) {
                                    %>
                                    <option value="<%=String.valueOf(rsNoNull[0][i].ID)%>" <%= rsNoNull[0][i].ID==rs[0][0].CERTIFICATION_AUTHORITY_ID ? "selected" : ""%>>
                                        <%=rsNoNull[0][i].REMARK%>
                                    </option>
                                    <%
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
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCertPurpose" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <select name="CERTIFICATION_PURPOSE" id="CERTIFICATION_PURPOSE" class="form-control123" disabled>
                                    <%
                                        CERTIFICATION_PURPOSE[][] rsCertPuspose = new CERTIFICATION_PURPOSE[1][];
                                        try {
                                            db.S_BO_CERTIFICATION_PURPOSE_COMBOBOX(sessLanguageGlobal, rsCertPuspose);
                                            if (rsCertPuspose[0].length > 0) {
                                                for (int i = 0; i < rsCertPuspose[0].length; i++) {
                                    %>
                                    <option value="<%=String.valueOf(rsCertPuspose[0][i].ID)%>" <%= rsCertPuspose[0][i].ID==rs[0][0].CERTIFICATION_PURPOSE_ID ? "selected" : ""%>>
                                        <%=rsCertPuspose[0][i].REMARK%>
                                    </option>
                                    <%
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
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCertAlgorithm" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <select name="CERTIFICATION_ALGORITHM" id="CERTIFICATION_ALGORITHM" class="form-control123" disabled>
                                    <%
                                        CERTIFICATION_ALGORITHM[][] rsCertAlgorithm = new CERTIFICATION_ALGORITHM[1][];
                                        try {
                                            db.S_BO_CERTIFICATION_ALGORITHM_COMBOBOX(sessLanguageGlobal, rsCertAlgorithm);
                                            if (rsCertAlgorithm[0].length > 0) {
                                                for (int i = 0; i < rsCertAlgorithm[0].length; i++) {
                                    %>
                                    <option value="<%=String.valueOf(rsCertAlgorithm[0][i].ID)%>" <%= rsCertAlgorithm[0][i].ID==rs[0][0].CERTIFICATION_ALGORITHM_ID ? "selected" : ""%>>
                                        <%=rsCertAlgorithm[0][i].REMARK%>
                                    </option>
                                    <%
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
                    </div>
                    <%
                        String sSynchClear = "";
                        String sSynchNone = "";
                        if("1".equals(sShowSynch)){
                            sSynchClear = "clear:both;";
                            sSynchNone = "none";
                        }
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleOPTIONShow" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <select name="pOPTIONShow" id="pOPTIONShow" class="form-control123">
                                    <option id="titleOptionAll" value="0" <%="0".equals(rs[0][0].ONLY_ISSUE) && "0".equals(rs[0][0].ONLY_RENEWAL) ? "selected" : "" %>></option>
                                    <option id="titleOptionIssue" value="1" <%="1".equals(rs[0][0].ONLY_ISSUE) && "0".equals(rs[0][0].ONLY_RENEWAL) ? "selected" : "" %>></option>
                                    <option id="titleOptionRenew" value="2" <%="0".equals(rs[0][0].ONLY_ISSUE) && "1".equals(rs[0][0].ONLY_RENEWAL) ? "selected" : "" %>></option>
                                </select>
                                <script>
                                    $("#titleOptionAll").text(global_fm_combox_all);
                                    $("#titleOptionIssue").text(certprofile_fm_service_issue);
                                    $("#titleOptionRenew").text(certprofile_fm_service_renew);
                                    $("#idLblTitleOPTIONShow").text(branch_fm_access_profile);
                                </script>
                            </div>
                        </div>
                    </div>
<!--                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleISSUE_ENABLED" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <label class="switch" for="ISSUE_ENABLED_EDIT">
                                    <input TYPE='checkbox' class='js-switch' data-switchery='true' <=rs[0][0].UNDISPLAY_RENEWAL_ENABLED ? "" : "checked"%> id="ISSUE_ENABLED_EDIT"/>
                                    <div class='slider round'></div>
                                </label>
                                <script>$("#idLblTitleISSUE_ENABLED").text(global_fm_renew_access);</script>
                            </div>
                        </div>
                    </div>-->
                    <div class="col-sm-6" style="padding-left: 0;display: <%=sSynchNone%>">
                        <div class="form-group">
                            <label id="idLblTitleAutoSynch" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <label class="switch" for="AUTO_SYNCH_EDIT">
                                    <input TYPE='checkbox' class='js-switch' data-switchery='true' <%=!"".equals(sValueSynch) && "1".equals(sValueSynch) ? "checked" : ""%> id="AUTO_SYNCH_EDIT"/>
                                    <div class='slider round'></div>
                                </label>
                                <script>$("#idLblTitleAutoSynch").text(synchneac_fm_synch_auto);</script>
                            </div>
                        </div>
                    </div>
                    <div style="height: 10px;"></div>
                    <fieldset class="scheduler-border" style="clear: both;">
                        <legend class="scheduler-border" id="idLblTitleCertInfo"></legend>
                        <div id="idViewCertInfo">
                            <script>
                                function onFormFunctionDelete(idFieldCode)
                                {
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gif").show();
                                    $.ajax({
                                        type: "post",
                                        url: "../CertificateTypeCommon",
                                        data: {
                                            idParam: 'deletecomponentcerttypeadd',
                                            idCERT_FIELDCODE: idFieldCode
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            var myStrings = sSpace(html).split('#');
                                            if (myStrings[0] === "0")
                                            {
                                                onLoadFunction();
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
                                function onLoadFunction()
                                {
                                    $.ajax({
                                        type: "post",
                                        url: "../JSONCommon",
                                        data: {
                                            idParam: 'loadcomponentcerttypeadd'
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            if (html.length > 0)
                                            {
                                                var obj = JSON.parse(html);
                                                if (obj[0].Code === "0")
                                                {
                                                    $("#idListUserApprove").empty();
                                                    var contentProps = "";
                                                    var sCount = 1;
                                                    for (var i = 0; i < obj.length; i++) {
                                                        var idCheckBox = obj[i].NAME + '' + obj[i].NO;
                                                        var isChecked = "";
                                                        if(obj[i].REQUIRE === "1")
                                                        {
                                                            isChecked = "checked";
                                                        }
                                                        var sActionCRL = '<a style="cursor: pointer;" class="btn btn-info\n\
                                                            btn-xs" onclick="onFormFunctionDelete(\'' + obj[i].NAME + '\');">\n\
                                                            <i class="fa fa-pencil"></i> ' + global_fm_button_delete + '</a>';
                                                        var sRequire = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' disabled class='js-switch' data-switchery='true' "+isChecked+" id='"+idCheckBox+"' /><div class='slider round'></div></label>";
                                                        contentProps += "<tr>" +
                                                            "<td>" + obj[i].NO + "</td>" +
                                                            "<td>" + obj[i].NAME + "</td>" +
                                                            "<td>" + obj[i].PREFIX + "</td>" +
                                                            "<td>" + obj[i].REMARK + "</td>" +
                                                            "<td>" + sRequire + "</td>" +
                                                            "<td>" + sActionCRL + "</td>" +
                                                            "</tr>";
                                                        sCount++;
                                                    }
                                                    $("#idListUserApprove").append(contentProps);
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
                                function onFormFunctionRequire(idFieldCode, idRequire)
                                {
                                    var isCheck = "0";
                                    if ($("#" + idRequire).is(':checked'))
                                    {
                                        isCheck = "1";
                                    }
                                    $.ajax({
                                        type: "post",
                                        url: "../CertificateTypeCommon",
                                        data: {
                                            idParam: 'requirecomponentcerttypeadd',
                                            idCERT_FIELDCODE: idFieldCode,
                                            idRequire: isCheck
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            var myStrings = sSpace(html).split('#');
                                            if (myStrings[0] === "0")
                                            {
                                                //onLoadFunction();
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
                                        }
                                    });
                                    return false;
                                }
                            </script>
                            <%
                                if(resProfileData != null) {
                                    session.setAttribute("SessComponentCertTypeAdd", resProfileData);
                                    if(resProfileData[0].length > 0) {
                                        boolean isHasEnterpriseUID = false;
                                        boolean requireEnterpriseUID = false;
                                        boolean isHasPersonalUID = false;
                                        boolean requirePersonalUID = false;
                                        for(CERTIFICATION_TYPE_COMPONENT resProfileItem : resProfileData[0]) {
                                            if(resProfileItem.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)){
                                                isHasEnterpriseUID = true;
                                                requireEnterpriseUID = resProfileItem.require;
                                            }
                                            if(resProfileItem.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL)){
                                                isHasPersonalUID = true;
                                                requirePersonalUID = resProfileItem.require;
                                            }
                                        }
                            %>
                            <div class="col-sm-6" style="padding-left: 0;display: <%= isHasEnterpriseUID == false ? "none" : "" %>">
                                <div class="form-group">
                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                        <label id="idLblTitleEnterpriseUID"></label>
                                    </label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <label class="switch" for="enterpriseUID">
                                            <input TYPE='checkbox' class='js-switch' data-switchery='true' <%= requireEnterpriseUID == true ? "checked" : "" %> id="enterpriseUID" />
                                            <div class='slider round'></div>
                                        </label>
                                    </div>
                                    <script>$("#idLblTitleEnterpriseUID").text(certtype_fm_component_uuid_company_require);</script>
                                </div>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;display: <%= isHasPersonalUID == false ? "none" : "" %>">
                                <div class="form-group">
                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                        <label id="idLblTitlePersonalUID"></label>
                                    </label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <label class="switch" for="personalUID">
                                            <input TYPE='checkbox' class='js-switch' data-switchery='true' <%= requirePersonalUID == true ? "checked" : "" %> id="personalUID" />
                                            <div class='slider round'></div>
                                        </label>
                                    </div>
                                    <script>$("#idLblTitlePersonalUID").text(certtype_fm_component_uuid_personal_require);</script>
                                </div>
                            </div>
                            <div class="table-responsive" style="clear: both;">
                                <table class="table table-bordered table-striped projects">
                                    <thead>
                                    <th id="idLblTitleTableSTT"></th>
                                    <th id="idLblTitleTableDN"></th>
                                    <th id="idLblTitleTablePrefix"></th>
                                    <th id="idLblTitleTableRemark"></th>
                                    <th id="idLblTitleTableFunctionAttriType"></th>
                                    <th id="idLblTitleTableRequire"></th>
                                    <script>
                                        $("#idLblTitleTableDN").text(global_fm_subjectdn);
                                        $("#idLblTitleTablePrefix").text(global_fm_prefix);
                                        $("#idLblTitleTableRequire").text(global_fm_required);
                                        $("#idLblTitleTableRemark").text(global_fm_Description);
                                        $("#idLblTitleTableAction").text(global_fm_action);
                                        $("#idLblTitleTableFunctionAttriType").text(certtype_component_attributetype);
                                    </script>
                                    </thead>
                                    <tbody id="idListUserApprove">
                                        <%
                                            int j = 1;
                                            for(CERTIFICATION_TYPE_COMPONENT resProfileItem : resProfileData[0]) {
                                                String sRequireID = resProfileItem.name + String.valueOf(j);
                                                String readonlyUID = "";
                                                if(resProfileItem.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)
                                                    || resProfileItem.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL))
                                                {
                                                    readonlyUID = "disabled";
                                                }
                                        %>
                                        <tr>
                                            <td><%= String.valueOf(j)%></td>
                                            <td><%= EscapeUtils.CheckTextNull(resProfileItem.name)%></td>
                                            <td><%= EscapeUtils.CheckTextNull(resProfileItem.prefix)%></td>
                                            <td><%= "1".equals(sessLanguageGlobal) ? EscapeUtils.CheckTextNull(resProfileItem.remark) : EscapeUtils.CheckTextNull(resProfileItem.remarkEn)%></td>
                                            <td>
                                                <%
                                                    sRequireID = sRequireID.replace(Definitions.CONFIG_COMPONENT_DN_TAG_UID, Definitions.CONFIG_COMPONENT_DN_TAG_UID_BEFORE);
                                                %>
                                                <span id="span<%=sRequireID%>"></span>
                                                <script>
                                                    var vValueType = '<%=EscapeUtils.CheckTextNull(resProfileItem.attributeType)%>';
                                                    if(vValueType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY)
                                                    {
                                                        $("#span<%=sRequireID%>").append(certtype_fm_component_uuid_company);
                                                    } else if(vValueType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL)
                                                    {
                                                        $("#span<%=sRequireID%>").append(certtype_fm_component_uuid_personal);
                                                    } else {
                                                        $("#span<%=sRequireID%>").append(certtype_fm_component_text);
                                                    }
                                                </script>
                                            </td>
                                            <td>
                                                <label class="switch" for="<%=sRequireID%>">
                                                    <input TYPE='checkbox' <%=readonlyUID%> class='js-switch' data-switchery='true' <%= resProfileItem.require == true ? "checked" : "" %> id="<%= sRequireID%>"
                                                           onclick="onFormFunctionRequire('<%= EscapeUtils.CheckTextNull(resProfileItem.name)%>', '<%= sRequireID%>');" />
                                                    <div class='slider round <%=readonlyUID%>'></div>
                                                </label>
                                            </td>
                                        </tr>
                                        <%
                                                j++;
                                            }
                                        %>
                                    </tbody>
                                </table>
                            </div>
                            <%
                                    }
                                }
                            %>
                        </div>
                    </fieldset>
                    
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleActive" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <label class="switch" for="ActiveFlag">
                                    <input TYPE='checkbox' class='js-switch' data-switchery='true' id="ActiveFlag" <%=rs[0][0].ENABLED ? "checked" : ""%> />
                                    <div class='slider round'></div>
                                </label>
                                <script>$("#idLblTitleActive").text(global_fm_active);</script>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCreateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly name="strCreateUser" class="form-control123" value="<%= rs[0][0].CREATED_BY%>" />
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCreateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" name="Date" readonly value="<%= strDateLimit%>">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleUpdateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly name="strUpdateUser" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_BY)%>" />
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleUpdateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" name="UpdateDate" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_DT) %>">
                            </div>
                        </div>
                    </div>
                </form>
                <%
                    } else
                    {
                %>
                <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
                    <label style="color: red;" id="idLblTitleNoData"></label>
                </div>
                <%
                        }
                    } else
                        {
                %>
                <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
                    <label style="color: red;" id="idLblTitleNoData"></label>
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
            </div>
        </div>
        <script src="../style/jquery.min.js"></script>
        <!--<script src="../style/bootstrap.min.js"></script>-->
<!--        <script src="../style/custom.min.js"></script>-->
        <script src="../js/active/highlight.js"></script>
        <!--<script src="../js/active/bootstrap-switch.js"></script>-->
        <script src="../js/active/main.js"></script>
    </body>
</html>