<%-- 
    Document   : CertProfileNew
    Created on : Jul 26, 2018, 2:57:50 PM
    Author     : THANH-PC
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
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
        <link href="../Css/active/bootstrap-switch.css" rel="stylesheet">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            document.title = certprofile_title_add;
            changeFavicon("../");
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.citycode.value))
                {
                    document.myname.citycode.focus();
                    funErrorAlert(policy_req_empty + certprofile_fm_code);
                    return false;
                } else {
                    if (JSCheckSpaceField(document.myname.citycode.value))
                    {
                        document.myname.citycode.focus();
                        funErrorAlert(certprofile_fm_code + global_req_no_space);
                        return false;
                    }
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
                if (!JSCheckEmptyField(document.myname.PKI_FORMFACTOR.value))
                {
                    document.myname.PKI_FORMFACTOR.focus();
                    funErrorAlert(policy_req_empty + global_fm_Method);
                    return false;
                }
                var sCheckIssue = "1";
                if ($("#ISSUE_ENABLED").is(':checked'))
                {
                    sCheckIssue = "0";
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
                        idParam: 'addcertprofile',
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
                        PKI_FORMFACTOR: document.myname.PKI_FORMFACTOR.value,
                        sIssueEnaled: sCheckIssue,
                        sCheckAutoSynch: sCheckAutoSynch,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccAlert(certprofile_succ_add, "CertProfileList.jsp");
                        }
                        else if (myStrings[0] === JS_EX_CSRF)
                        {
                            funCsrfAlert();
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
            function closeForm()
            {
                 $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'SessRefreshCertProfile'
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
        <style>
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.2em 5px 1.2em !important;
                margin: 0 0 1.1em 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
        </style>
    </head>
    <body class="nav-md">
        <%
            if (session.getAttribute("sUserID") != null) {
                String anticsrf = "";
                anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                String strPurposeFrist = "";
                session.setAttribute("SessComponentCertTypeAdd", null);
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
                        document.getElementById("idNameURL").innerHTML = certprofile_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleEdits" style="color: #36526D;"></span></h2>
                                        <script>$("#idLblTitleEdits").text(certprofile_title_add);</script>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="padding-right: 10px;">
                                                <input id="btnSave" type="button" class="btn btn-info" onclick="return ValidateForm('<%=anticsrf%>');"/>
                                                <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                                                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                                <script>
                                                    document.getElementById("btnSave").value = global_fm_button_add;
                                                    document.getElementById("btnClose").value = global_fm_button_back;
                                                </script>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form name="myname" method="post" class="form-horizontal">
                                            <%
                                                try {
                                            %>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleCode"></label>
                                                        <label id="idLblNoteCode"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" oninput="onBlurToUppercase(this);" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_NAME %>"
                                                            id="citycode" name="citycode"/>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleCode").text(certprofile_fm_code);
                                                    $("#idLblNoteCode").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleRemark"></label>
                                                        <label id="idLblNoteRemark"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK %>" id="Remark" name="Remark"/>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleRemark").text(global_fm_remark_vn);
                                                    $("#idLblNoteRemark").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleRemark_EN"></label>
                                                        <label id="idLblNoteRemark_EN"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK %>" id="Remark_EN" name="Remark_EN"/>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleRemark_EN").text(global_fm_remark_en);
                                                    $("#idLblNoteRemark_EN").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleAmount"></label>
                                                        <label id="idLblNoteAmount"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" id="AMOUNT" name="AMOUNT" oninput="autoConvertMoney(this.value, $('#AMOUNT'))"/>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleAmount").text(global_fm_amount_fee);
                                                    $("#idLblNoteAmount").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleAmountRenew"></label>
                                                        <label class="CssRequireField" id="idLblNoteAmountRenew"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" id="RENEWAL_AMOUNT" name="RENEWAL_AMOUNT" oninput="autoConvertMoney(this.value, $('#RENEWAL_AMOUNT'))"/>
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
                                                        <input class="form-control123" id="CHANGE_AMOUNT" name="CHANGE_AMOUNT" oninput="autoConvertMoney(this.value, $('#CHANGE_AMOUNT'))"/>
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
                                                        <input class="form-control123" id="REISSUE_AMOUNT" name="REISSUE_AMOUNT" oninput="autoConvertMoney(this.value, $('#REISSUE_AMOUNT'))"/>
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
                                                        <input class="form-control123" id="GOVERNMENT_AMOUNT" name="GOVERNMENT_AMOUNT" oninput="autoConvertMoney(this.value, $('#GOVERNMENT_AMOUNT'))"/>
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
                                                        <input class="form-control123" id="TOKEN_AMOUNT" name="TOKEN_AMOUNT" oninput="autoConvertMoney(this.value, $('#TOKEN_AMOUNT'))"/>
                                                    </div>
                                                </div>
                                                <script>$("#idLblTitleAmountToken").text(global_fm_amount_token);</script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleDuration"></label>
                                                        <label id="idLblNoteDuration"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" id="DURATION" name="DURATION" oninput="autoConvertMoney(this.value, $('#DURATION'))"/>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleDuration").text(global_fm_duration);
                                                    $("#idLblNoteDuration").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleDurationFree"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" id="DURATION_FREE" value="0" name="DURATION_FREE" oninput="autoConvertMoney(this.value, $('#DURATION_FREE'))"/>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleDurationFree").text(global_fm_date_free);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleEJBCA"></label>
                                                        <label id="idLblNoteEJBCA"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <textarea class="form-control123" id="ENTITY_EJBCA" style="height: 60px;" name="ENTITY_EJBCA"></textarea>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleEJBCA").text(global_fm_entity_ejbca);
                                                    $("#idLblNoteEJBCA").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleCA" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="CERTIFICATION_AUTHORITY" id="CERTIFICATION_AUTHORITY" class="form-control123">
                                                            <%
                                                                CERTIFICATION_AUTHORITY[][] rsNoNull = new CERTIFICATION_AUTHORITY[1][];
                                                                try {
                                                                    db.S_BO_CERTIFICATION_AUTHORITY_COMBOBOX(sessLanguageGlobal, rsNoNull);
                                                                    if (rsNoNull[0].length > 0) {
                                                                        for (int i = 0; i < rsNoNull[0].length; i++) {
                                                            %>
                                                            <option value="<%=String.valueOf(rsNoNull[0][i].ID)%>">
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
                                                <script>
                                                    $("#idLblTitleCA").text(global_fm_ca);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleCertPurpose" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="CERTIFICATION_PURPOSE" id="CERTIFICATION_PURPOSE" class="form-control123"
                                                            onchange="LOAD_USER_APPROVE(this.value, '<%= anticsrf %>');">
                                                            <%
                                                                CERTIFICATION_PURPOSE[][] rsCertPuspose = new CERTIFICATION_PURPOSE[1][];
                                                                try {
                                                                    db.S_BO_CERTIFICATION_PURPOSE_COMBOBOX(sessLanguageGlobal, rsCertPuspose);
                                                                    if (rsCertPuspose[0].length > 0) {
                                                                        for (int i = 0; i < rsCertPuspose[0].length; i++) {
                                                                            strPurposeFrist = String.valueOf(rsCertPuspose[0][0].ID);
                                                            %>
                                                            <option value="<%=String.valueOf(rsCertPuspose[0][i].ID)%>">
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
                                                <script>
                                                    $("#idLblTitleCertPurpose").text(global_fm_certpurpose);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitlePKIFormfactor" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="PKI_FORMFACTOR" id="PKI_FORMFACTOR" class="form-control123">
                                                            <%
                                                                PKI_FORMFACTOR[][] rsPKIFormFactor = new PKI_FORMFACTOR[1][];
                                                                try {
                                                                    db.S_BO_CA_GET_PKI_FORMFACTOR_COMBOBOX_FOR_CERTIFICATION_PURPOSE(Integer.parseInt(strPurposeFrist),
                                                                            sessLanguageGlobal, rsPKIFormFactor);
                                                                    if (rsPKIFormFactor[0].length > 0) {
                                                                        for (int i = 0; i < rsPKIFormFactor[0].length; i++) {
                                                            %>
                                                            <option value="<%=String.valueOf(rsPKIFormFactor[0][i].ID)%>">
                                                                <%=rsPKIFormFactor[0][i].REMARK%>
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
                                                <script>
                                                    $("#idLblTitlePKIFormfactor").text(global_fm_Method);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleCertAlgorithm" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="CERTIFICATION_ALGORITHM" id="CERTIFICATION_ALGORITHM" class="form-control123">
                                                            <%
                                                                CERTIFICATION_ALGORITHM[][] rsCertAlgorithm = new CERTIFICATION_ALGORITHM[1][];
                                                                try {
                                                                    db.S_BO_CERTIFICATION_ALGORITHM_COMBOBOX(sessLanguageGlobal, rsCertAlgorithm);
                                                                    if (rsCertAlgorithm[0].length > 0) {
                                                                        for (int i = 0; i < rsCertAlgorithm[0].length; i++) {
                                                            %>
                                                            <option value="<%=String.valueOf(rsCertAlgorithm[0][i].ID)%>">
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
                                                <script>
                                                    $("#idLblTitleCertAlgorithm").text(global_fm_certalgorithm);
                                                </script>
                                            </div>
                                            <%
                                                String sSynchNone = "";
                                                if("1".equals(sShowSynch)){
                                                    sSynchNone = "none";
                                                }
                                            %>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleActive" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <label class="switch" for="ISSUE_ENABLED">
                                                            <input TYPE='checkbox' class='js-switch' data-switchery='true' checked id="ISSUE_ENABLED"/>
                                                            <div class='slider round'></div>
                                                        </label>
                                                        <script>$("#idLblTitleActive").text(global_fm_renew_access);</script>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;display: <%=sSynchNone%>">
                                                <div class="form-group">
                                                    <label id="idLblTitleAutoSynch" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <label class="switch" for="AUTO_SYNCH_EDIT">
                                                            <input TYPE='checkbox' class='js-switch' data-switchery='true' <%="0".equals(sShowSynch) ? "" : "checked"%> id="AUTO_SYNCH_EDIT"/>
                                                            <div class='slider round'></div>
                                                        </label>
                                                        <script>$("#idLblTitleAutoSynch").text(synchneac_fm_synch_auto);</script>
                                                    </div>
                                                </div>
                                            </div>
                                            <div style="height: 10px;"></div>
                                            <fieldset class="scheduler-border" id="idViewUserApprove" style="display: none;clear: both;">
                                                <legend class="scheduler-border"><script>document.write(global_fm_csr_info_cts);</script></legend>
                                                <div id="idViewCertInfo" class="table-responsive">
                                                    <table class="table table-striped projects">
                                                        <thead>
                                                        <th><script>document.write(global_fm_STT);</script></th>
                                                        <th><script>document.write(global_fm_subjectdn);</script></th>
                                                        <th><script>document.write(global_fm_prefix);</script></th>
                                                        <th><script>document.write(global_fm_Description);</script></th>
                                                        <th><script>document.write(global_fm_required);</script></th>
                                                        <th><script>document.write(global_fm_action);</script></th>
                                                        </thead>
                                                        <tbody id="idListUserApprove"></tbody>
                                                    </table>
                                                </div>
                                            </fieldset>
                                            <script>
                                                function LOAD_USER_APPROVE(idID, idCSRF)
                                                {
                                                    LOAD_FORM_FACTOR(idID, idCSRF);
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loadproperties_purpose',
                                                            idID: idID,
                                                            CsrfToken: idCSRF
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
                                                                    var content = "";
//                                                                    var vStringViewCheckbox = "";
                                                                    for (var i = 0; i < obj.length; i++) {
//                                                                        var idCheckBox = obj[i].NAME.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE)+'_'+obj[i].Index;
//                                                                        var sRequiredHTML = "<input disabled TYPE='checkbox' checked id='"+idCheckBox+"' />";
//                                                                        if(obj[i].REQUIRED === "0")
//                                                                        {
//                                                                            sRequiredHTML = "<input TYPE='checkbox' disabled id='"+idCheckBox+"' />";
//                                                                        }
                                                                        var idCheckBox = obj[i].NAME + '' + obj[i].Index;
                                                                        var isChecked = "";
                                                                        if(obj[i].REQUIRED === "1")
                                                                        {
                                                                            isChecked = "checked";
                                                                        }
                                                                        var checkDisabled = "";
                                                                        if(obj[i].ATTRIBUTE_TYPE === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY
                                                                            || obj[i].ATTRIBUTE_TYPE === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL) {
                                                                            checkDisabled = "disabled";
                                                                        }
                                                                        var sActionCRL = '<a style="cursor: pointer;" class="btn btn-info\n\
                                                                            btn-xs" onclick="onFormFunctionDelete(\'' + obj[i].NAME + '\');">\n\
                                                                            <i class="fa fa-pencil"></i> ' + global_fm_button_delete + '</a>';
                                                                        var sRequire = '<label class="switch" for="'+idCheckBox+'"><input TYPE="checkbox" class="js-switch" data-switchery="true"\n\
                                                                            '+isChecked+' '+checkDisabled+' id="'+idCheckBox+'" onclick="onFormFunctionRequire(\''+ obj[i].NAME +'\', \''+ idCheckBox +'\');"/><div class="slider round '+checkDisabled+'"></div></label>';
                                                                        content += "<tr>" +
                                                                            "<td>" + obj[i].Index + "</td>" +
                                                                            "<td>" + obj[i].NAME + "</td>" +
                                                                            "<td>" + obj[i].PREFIX + "</td>" +
                                                                            "<td>" + obj[i].REMARK + "</td>" +
                                                                            "<td>" + sRequire + "</td>" +
                                                                            "<td>" + sActionCRL + "</td>" +
                                                                            "</tr>";
                                                                    }
                                                                    $("#idListUserApprove").append(content);
                                                                    $("#idViewUserApprove").css("display", "");
//                                                                    if(vStringViewCheckbox !== "")
//                                                                    {
//                                                                        ViewCheckBox(vStringViewCheckbox);
//                                                                    }
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
//                                                                $(".loading-gif").hide();
//                                                                $('#over').remove();
                                                            }
                                                        }
                                                    });
                                                    return false;
                                                }
                                                function LOAD_FORM_FACTOR(objPurpose, idCSRF)
                                                {
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loadpki_formfactorby_purpose_nocert',
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
                                                function onFormFunctionRequire(idFieldCode, idRequire)
                                                {
                                                    var isCheck = "0";
                                                    if ($("#" + idRequire).is(':checked'))
                                                    {
                                                        isCheck = "1";
                                                    }
                                                    $('body').append('<div id="over"></div>');
                                                    $(".loading-gif").show();
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
                                                            $(".loading-gif").hide();
                                                            $('#over').remove();
                                                        }
                                                    });
                                                    return false;
                                                }
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
                                                function LOAD_USER_APPROVE_BK(idID, idCSRF)
                                                {
//                                                    $('body').append('<div id="over"></div>');
//                                                    $(".loading-gif").show();
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loadproperties_purpose',
                                                            idID: idID,
                                                            CsrfToken: idCSRF
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
                                                                    var content = "";
                                                                    var vStringViewCheckbox = "";
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        var idCheckBox = obj[i].NAME.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE)+'_'+obj[i].Index;
                                                                        var sRequiredHTML = "<input disabled TYPE='checkbox' checked id='"+idCheckBox+"' />";
                                                                        if(obj[i].REQUIRED === "0")
                                                                        {
                                                                            sRequiredHTML = "<input TYPE='checkbox' disabled id='"+idCheckBox+"' />";
                                                                        }
                                                                        vStringViewCheckbox = vStringViewCheckbox + "@@@" + idCheckBox;
                                                                        content += "<tr>" +
                                                                            "<td>" + obj[i].Index + "</td>" +
                                                                            "<td>" + obj[i].NAME + "</td>" +
                                                                            "<td>" + obj[i].PREFIX + "</td>" +
                                                                            "<td>" + sRequiredHTML + "</td>" +
                                                                            "<td>" + obj[i].REMARK + "</td>" +
                                                                            "<td>" + obj[i].REMARK_EN + "</td>" +
                                                                            "</tr>";
                                                                    }
                                                                    $("#idListUserApprove").append(content);
                                                                    $("#idViewUserApprove").css("display", "");
                                                                    if(vStringViewCheckbox !== "")
                                                                    {
                                                                        ViewCheckBox(vStringViewCheckbox);
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
//                                                                $(".loading-gif").hide();
//                                                                $('#over').remove();
                                                            }
                                                        }
                                                    });
                                                    return false;
                                                }
                                                function ViewCheckBox(vvViewCheckbox)
                                                {
                                                    var sStringTemp = "";
                                                    var vItemViewCheckbox = vvViewCheckbox.split('@@@');
                                                    for (var n = 1; n < vItemViewCheckbox.length; n++) {
                                                        sStringTemp = sStringTemp + "setTimeout(function() {$('#"+vItemViewCheckbox[n]+"').bootstrapSwitch('setState', true);}, 0);";
                                                    }
                                                    eval(sStringTemp);
                                                }
                                            </script>
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
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <script>
                        $(document).ready(function () {
                            if('<%= strPurposeFrist%>' !== "")
                            {
                                LOAD_USER_APPROVE('<%= strPurposeFrist%>', '<%= anticsrf%>');
                            }
                        });
                    </script>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
        </div>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../style/custom.min.js"></script>
        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/bootstrap-switch.js"></script>
        <script src="../js/active/main.js"></script>
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
