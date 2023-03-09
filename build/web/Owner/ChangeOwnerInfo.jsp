<%-- 
    Document   : ChangeOwnerInfo
    Created on : Nov 1, 2019, 5:10:48 PM
    Author     : USER
--%>

<%@page import="vn.ra.utility.PropertiesContent"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
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
//            document.title = cert_title_edit;
            function reload_js(src) {
                $('script[src="' + src + '"]').remove();
                $('<script>').attr('src', src).appendTo('head');
            }
            $(document).ready(function () {
                reload_js("../style/bootstrap.min.js");
            });
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            var checkForSpecialChar = function(string) {
             for(i = 0; i < specialChars.length;i++) {
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
                        idSession: 'RefreshOwnerListSess'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "OwnerList.jsp";
                        }
                        else
                        {
                            window.location = "OwnerList.jsp";
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
            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
            .table > thead > tr > th{border-bottom: none;}
            .btn{margin-bottom: 0px;}
        </style>
    </head>
    <body>
        <%
            if (session.getAttribute("sUserID") != null) {
            String strCERTIFICATION_PROFILE_ID = "";
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <%                                        CERTIFICATION_OWNER[][] rs = new CERTIFICATION_OWNER[1][];
            String strIsCert = "0";
            session.setAttribute("sessUploadFileCert", null);
            try {
                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                if (EscapeUtils.IsInteger(ids) == true) {
                    db.S_BO_CERTIFICATION_OWNER_DETAIL(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                    if (rs[0].length > 0) {
                        String sRegexPolicy = "";
                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                        if (sessGeneralPolicy[0].length > 0) {
                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                            {
                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_REGEX_FOR_PHONE_EMAIL))
                                {
                                    sRegexPolicy = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    break;
                                }
                            }
                        }
                        // GET REGEX POLICY PHONE - EMAIL
                        String sREGEX_PHONE = Definitions.CONFIG_DEFAULT_VALUE_REGEX_PHONE;
                        String sREGEX_EMAIL = Definitions.CONFIG_DEFAULT_VALUE_REGEX_EMAIL;
                        if(!"".equals(sRegexPolicy))
                        {
                            sREGEX_PHONE = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_PHONE);
                            sREGEX_EMAIL = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_EMAIL);
                        }
                        String sCOMPANY_NAME = rs[0][0].COMPANY_NAME;
                        String sPERSONAL_NAME = rs[0][0].PERSONAL_NAME;
                        String sPHONE_CONTRACT = rs[0][0].PHONE_CONTRACT;
                        String sEMAIL_CONTRACT = rs[0][0].EMAIL_CONTRACT;
                        String sTAX_CODE = EscapeUtils.CheckTextNull(rs[0][0].TAX_CODE);
                        String sBUDGET_CODE = EscapeUtils.CheckTextNull(rs[0][0].BUDGET_CODE);
                        String sDECISION = EscapeUtils.CheckTextNull(rs[0][0].DECISION);
                        String sP_ID = EscapeUtils.CheckTextNull(rs[0][0].P_ID);
                        String sPASSPORT = EscapeUtils.CheckTextNull(rs[0][0].PASSPORT);
                        String sCITIZEN_ID = EscapeUtils.CheckTextNull(rs[0][0].CITIZEN_ID);
                        String sREPRESENTATIVE = rs[0][0].REPRESENTATIVE;
                        String sREPRESENTATIVE_POSITION = rs[0][0].REPRESENTATIVE_POSITION;
                        String sADDRESS = rs[0][0].ADDRESS;
                        String sDisabledEdit = "disabled";
                        String sDisabledEdit_Company = "disabled";
                        String sDisabledEdit_Personal = "disabled";
                        String sViewEdit_Company = "none";
                        String sViewEdit_Personal = "none";
                        int pCERTIFICATION_OWNER_TYPE_ID = rs[0][0].CERTIFICATION_OWNER_TYPE_ID;
                        if(pCERTIFICATION_OWNER_TYPE_ID == Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_ENTERPRISE)
                        {
                            sViewEdit_Company = "";
                            sViewEdit_Personal = "none";
                        }
                        if(pCERTIFICATION_OWNER_TYPE_ID == Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_PERSONAL)
                        {
                            sViewEdit_Company = "none";
                            sViewEdit_Personal = "";
                        }
        %>
        <script>
            $(document).ready(function () {
                localStorage.setItem("sessLocal_REGEX_PHONE", '<%=sREGEX_PHONE%>');
                localStorage.setItem("sessLocal_REGEX_EMAIL", '<%=sREGEX_EMAIL%>');
            });
        </script>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEdits"></span> </h2>
            <script>$("#idLblTitleEdits").text(owner_title_change);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <%
                        if(rs[0][0].CERTIFICATION_OWNER_STATE_ID == Definitions.CONFIG_CERTIFICATION_OWNER_STATE_ID_OPERATED)
                        {
                    %>
                    <input type="button" id="btnApprove" class="btn btn-info" onclick="formChangeInfo('<%= rs[0][0].ID%>', '<%=anticsrf%>');" />
                    <script>document.getElementById("btnApprove").value = global_fm_button_add;</script>
                    <%
                        }
                    %>
                    <script>
                        function formChangeInfo(id, idCSRF)
                        {
                            if(document.myname.sOWNER_TYPE_ID.value !== JS_STR_OWNER_TYPE_ID_ENTERPRISE_GOV
                                && document.myname.sOWNER_TYPE_ID.value !== JS_STR_OWNER_TYPE_ID_PERSONAL_GOV)
                            {
                                if (!JSCheckEmptyField(document.myname.INPUT_MST_MNS.value) && !JSCheckEmptyField(document.myname.INPUT_CMND_HC.value))
                                {
                                    funErrorAlert(policy_req_empty + owner_fm_private_uid);
                                    return false;
                                }
                                if (!JSCheckEmptyField(document.myname.PERSONAL_NAME.value) && !JSCheckEmptyField(document.myname.COMPANY_NAME.value))
                                {
                                    funErrorAlert(policy_req_empty + global_fm_grid_company + '/' + global_fm_grid_personal);
                                    return false;
                                }
                            }
                            if (!JSCheckEmptyField($("#PHONE_CONTRACT").val()))
                            {
                                $("#PHONE_CONTRACT").focus();
                                funErrorAlert(policy_req_empty + global_fm_phone);
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
                                funErrorAlert(policy_req_empty + global_fm_email);
                                return false;
                            } else {
                                if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#EMAIL_CONTRACT").val()))
                                {
                                    $("#EMAIL_CONTRACT").focus();
                                    funErrorAlert(global_req_mail_format);
                                    return false;
                                }
                            }
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../OwnerCommon",
                                data: {
                                    idParam: 'changeownerinfo',
                                    sID: id,
                                    pPERSONAL_NAME: document.myname.PERSONAL_NAME.value,
                                    pCOMPANY_NAME: document.myname.COMPANY_NAME.value,
                                    pCBX_MST_MNS: document.myname.CBX_MST_MNS.value,
                                    pINPUT_MST_MNS: document.myname.INPUT_MST_MNS.value,
                                    pCBX_CMND_HC: document.myname.CBX_CMND_HC.value,
                                    pINPUT_CMND_HC: document.myname.INPUT_CMND_HC.value,
                                    pPHONE_CONTRACT: document.myname.PHONE_CONTRACT.value,
                                    pEMAIL_CONTRACT: document.myname.EMAIL_CONTRACT.value,
                                    pADDRESS: document.myname.ADDRESS.value,
                                    pREPRESENTATIVE: document.myname.REPRESENTATIVE.value,
                                    pREPRESENTATIVE_POSITION: document.myname.REPRESENTATIVE_POSITION.value,
                                    CsrfToken: idCSRF
                                },
                                cache: false,
                                success: function (html)
                                {
                                    var myStrings = sSpace(html).split('#');
                                    if (myStrings[0] === "0") {
                                        funSuccAlert(owner_succ_dispose, "OwnerList.jsp");
                                    }
                                    else if (myStrings[0] === JS_EX_ERROR_DB) {
                                        funErrorAlert(myStrings[1]);
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
                                    else if (myStrings[0] === JS_EX_ERROR_DB)
                                    {
                                        funErrorAlert(myStrings[1]);
                                    }
                                    else
                                    {
                                        if (myStrings[1] === JS_STR_ERROR_CODE_99) {
                                            funErrorAlert(global_error_login_info);
                                        } else {
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
                    <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                </li>
                <script>document.getElementById("btnClose").value = global_fm_button_close;</script>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <form name="myname" method="post" class="form-horizontal">
                <input type="hidden" id="sID" name="sID" hidden="true" readonly="true" value="<%= rs[0][0].ID%>">
                <input type="hidden" id="sOWNER_TYPE_ID" name="sOWNER_TYPE_ID" hidden="true" readonly="true" value="<%= rs[0][0].CERTIFICATION_OWNER_TYPE_ID%>">
                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleOWNER_TYPE" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_OWNER_TYPE_DESC)%>" id="OWNER_TYPE" name="OWNER_TYPE">
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleOWNER_TYPE").text(owner_fm_type);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleOWNER_STATE" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_OWNER_STATE_DESC)%>" id="OWNER_STATE" name="OWNER_STATE">
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleOWNER_STATE").text(global_fm_Status);
                    </script>
                </div>
                <%
                    String prefixDBDN = "";
                    String uidDBDN = "";
                    if(!"".equals(rs[0][0].ENTERPRISE_ID)) {
                        prefixDBDN = rs[0][0].ENTERPRISE_ID.split(":")[0] + ":";
                        uidDBDN = rs[0][0].ENTERPRISE_ID.split(":")[1];
                        sDisabledEdit_Company = "";
                    }
                    String prefixDBCN = "";
                    String uidDBCN = "";
                    if(!"".equals(rs[0][0].PERSONAL_ID)){
                        prefixDBCN = rs[0][0].PERSONAL_ID.split(":")[0] + ":";
                        uidDBCN = rs[0][0].PERSONAL_ID.split(":")[1];
                        sDisabledEdit_Personal = "";
                    }
                %>
<!--                <div class='col-sm-12' style='margin-bottom:0px;padding-left: 0px;'>
                    <div class='form-group'>
                        <div class='col-sm-2' style='padding-left: 0px;margin-left: 0px;'>
                            <select name="CBX_MST_MNS" id="CBX_MST_MNS" class="form-control123" <=sDisabledEdit%>>
                                <option id="CBX_MST_MNS_TIN" <= !"".equals(sTAX_CODE) ? "selected" : "" %>></option>
                                <option id="CBX_MST_MNS_BGC" <= !"".equals(sBUDGET_CODE) ? "selected" : "" %>></option>
                            </select>
                        </div>
                        <div class='col-sm-10' style='padding-right: 0px;'>
                            <input class="form-control123" value="<= sINPUT_MST_MNS%>" <=sDisabledEdit%> type="text" id="INPUT_MST_MNS" name="INPUT_MST_MNS" />
                        </div>
                    </div>
                    <script>
                        $("#CBX_MST_MNS_TIN").val(JS_STR_COMPONENT_DN_VALUE_TIN);
                        $("#CBX_MST_MNS_BGC").val(JS_STR_COMPONENT_DN_VALUE_BGC);
                        $("#CBX_MST_MNS_TIN").text(global_fm_MST);
                        $("#CBX_MST_MNS_BGC").text(global_fm_MNS);
                    </script>
                </div>-->
                <div class="col-sm-6" style="padding-left: 0;display: <%= sViewEdit_Company%>">
                    <div class="form-group">
                        <div class='col-sm-5' style='padding-left: 0px;margin-left: 0px;'>
                            <select name="CBX_MST_MNS" id="CBX_MST_MNS" class="form-control123" <%=sDisabledEdit%>>
                                <%
                                    PREFIX_UUID[][] rsPrefix;
                                    rsPrefix = new PREFIX_UUID[1][];
                                    dbTwo.S_BO_PREFIX_UUID_COMBOBOX("ENTERPRISE", sessLanguageGlobal, rsPrefix);
                                    if (rsPrefix[0].length > 0) {
                                        for (PREFIX_UUID temp1 : rsPrefix[0]) {
                                %>
                                <option value="<%=temp1.PREFIX_DB%>" <%= temp1.PREFIX_DB.equals(prefixDBDN) ? "selected" : "" %>><%=temp1.REMARK%></option>
                                <%
                                        }
                                    }
                                %>
<!--                                <option id="CBX_MST_MNS_TIN" <= !"".equals(sTAX_CODE) ? "selected" : "" %>></option>
                                <option id="CBX_MST_MNS_BGC" <= !"".equals(sBUDGET_CODE) ? "selected" : "" %>></option>
                                <option id="CBX_MST_MNS_DEC" <= !"".equals(sDECISION) ? "selected" : "" %>></option>-->
                            </select>
                        </div>
                        <div class='col-sm-7' style='padding-right: 0px;'>
                            <input class="form-control123" value="<%= uidDBDN%>" <%=sDisabledEdit%> type="text" id="INPUT_MST_MNS" name="INPUT_MST_MNS" />
                        </div>
                    </div>
<!--                    <script>
                        $("#CBX_MST_MNS_TIN").val(JS_STR_COMPONENT_DN_VALUE_TIN);
                        $("#CBX_MST_MNS_BGC").val(JS_STR_COMPONENT_DN_VALUE_BGC);
                        $("#CBX_MST_MNS_DEC").val(JS_STR_COMPONENT_DN_VALUE_DEC);
                        $("#CBX_MST_MNS_TIN").text(global_fm_MST);
                        $("#CBX_MST_MNS_BGC").text(global_fm_MNS);
                        $("#CBX_MST_MNS_DEC").text(global_fm_decision);
                    </script>-->
                </div>
                <div class="col-sm-6" style="padding-left: 0;display: <%= sViewEdit_Personal%>">
                    <div class="form-group">
                        <div class='col-sm-5' style='padding-left: 0px;margin-left: 0px;'>
                            <select name="CBX_CMND_HC" id="CBX_CMND_HC" class="form-control123" <%=sDisabledEdit%>>
                                <%
                                    rsPrefix = new PREFIX_UUID[1][];
                                    dbTwo.S_BO_PREFIX_UUID_COMBOBOX("PERSONAL", sessLanguageGlobal, rsPrefix);
                                    if (rsPrefix[0].length > 0) {
                                        for (PREFIX_UUID temp1 : rsPrefix[0]) {
                                %>
                                <option value="<%=temp1.PREFIX_DB%>" <%= temp1.PREFIX_DB.equals(prefixDBCN) ? "selected" : "" %>><%=temp1.REMARK%></option>
                                <%
                                        }
                                    }
                                %>
<!--                                <option id="CBX_CMND_HC_PID" <= !"".equals(sP_ID) ? "selected" : "" %>></option>
                                <option id="CBX_CMND_HC_PPID" <= !"".equals(sPASSPORT) ? "selected" : "" %>></option>
                                <option id="CBX_CMND_HC_PEID" <= !"".equals(sCITIZEN_ID) ? "selected" : "" %>></option>-->
                            </select>
                        </div>
                        <div class='col-sm-7' style='padding-right: 0px;'>
                            <input class="form-control123" value="<%=uidDBCN%>" <%=sDisabledEdit%> type="text" id="INPUT_CMND_HC" name="INPUT_CMND_HC" />
                        </div>
                    </div>
<!--                    <script>
                        $("#CBX_CMND_HC_PID").val(JS_STR_COMPONENT_DN_VALUE_PID);
                        $("#CBX_CMND_HC_PPID").val(JS_STR_COMPONENT_DN_VALUE_PPID);
                        $("#CBX_CMND_HC_PEID").val(JS_STR_COMPONENT_DN_VALUE_PEID);
                        $("#CBX_CMND_HC_PID").text(global_fm_CMND);
                        $("#CBX_CMND_HC_PPID").text(global_fm_HC);
                        $("#CBX_CMND_HC_PEID").text(global_fm_CitizenId);
                    </script>-->
                </div>
<!--                <div class='col-sm-12' style='margin-bottom:0px;padding-left: 0px;'>
                    <div class='form-group'>
                        <div class='col-sm-2' style='padding-left: 0px;margin-left: 0px;'>
                            <select name="CBX_CMND_HC" id="CBX_CMND_HC" class="form-control123" <=sDisabledEdit%>>
                                <option id="CBX_CMND_HC_PID" <= !"".equals(sP_ID) ? "selected" : "" %>></option>
                                <option id="CBX_CMND_HC_PPID" <= !"".equals(sPASSPORT) ? "selected" : "" %>></option>
                            </select>
                        </div>
                        <div class='col-sm-10' style='padding-right: 0px;'>
                            <input class="form-control123" value="<=sINPUT_CMND_HC%>" <=sDisabledEdit%> type="text" id="INPUT_CMND_HC" name="INPUT_CMND_HC" />
                        </div>
                    </div>
                    <script>
                        $("#CBX_CMND_HC_PID").val(JS_STR_COMPONENT_DN_VALUE_PID);
                        $("#CBX_CMND_HC_PPID").val(JS_STR_COMPONENT_DN_VALUE_PPID);
                        $("#CBX_CMND_HC_PID").text(global_fm_CMND);
                        $("#CBX_CMND_HC_PPID").text(global_fm_HC);
                    </script>
                </div>-->
                <div class="col-sm-6" style="padding-left: 0;display: <%= sViewEdit_Company%>">
                    <div class="form-group">
                        <label id="idLblTitleCOMPANY_NAME" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" value="<%= sCOMPANY_NAME%>" <%= sDisabledEdit_Company%> maxlength="150" id="COMPANY_NAME" name="COMPANY_NAME">
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleCOMPANY_NAME").text(global_fm_grid_company);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;display: <%= sViewEdit_Personal%>">
                    <div class="form-group">
                        <label id="idLblTitlePERSONAL_NAME" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" <%= sDisabledEdit_Personal%> value="<%= sPERSONAL_NAME%>" maxlength="150" id="PERSONAL_NAME" name="PERSONAL_NAME">
                        </div>
                    </div>
                    <script>
                        $("#idLblTitlePERSONAL_NAME").text(global_fm_grid_personal);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitlePHONE_CONTRACT" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" maxlength="16" value="<%= sPHONE_CONTRACT%>" id="PHONE_CONTRACT" name="PHONE_CONTRACT">
                        </div>
                    </div>
                    <script>
                        $("#idLblTitlePHONE_CONTRACT").text(global_fm_phone);
                    </script>
                </div>
                <%
                    String sEmailReadOnly = "";
                    if(rs[0][0].CERTIFICATION_OWNER_TYPE_ID == Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_ENTERPRISE_GOV
                        || rs[0][0].CERTIFICATION_OWNER_TYPE_ID == Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_PERSONAL_GOV)
                    {
                        sEmailReadOnly = "readonly";
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleEMAIL_CONTRACT" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" maxlength="150" <%= sEmailReadOnly%> id="EMAIL_CONTRACT" value="<%= sEMAIL_CONTRACT%>" name="EMAIL_CONTRACT">
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleEMAIL_CONTRACT").text(global_fm_email);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleREPRESENTATIVE" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" maxlength="150" id="REPRESENTATIVE" value="<%= sREPRESENTATIVE%>" name="REPRESENTATIVE">
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleREPRESENTATIVE").text(branch_fm_representative);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitlepREPRESENTATIVE_POSITION" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" maxlength="150" value="<%= sREPRESENTATIVE_POSITION%>" id="REPRESENTATIVE_POSITION" name="REPRESENTATIVE_POSITION">
                        </div>
                    </div>
                    <script>
                        $("#idLblTitlepREPRESENTATIVE_POSITION").text(branch_fm_representative_position);
                    </script>
                </div>
<!--                <div class="col-sm-13" style="padding-left: 0;clear: both;">
                    <div class="form-group">
                        <label id="idLblTitleADDRESS" class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-10" style="padding-right: 0px;">
                            <input class="form-control123" maxlength="500" id="ADDRESS" name="ADDRESS" value="<= sADDRESS%>">
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleADDRESS").text(global_fm_address);
                    </script>
                </div>-->
                <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                    <label id="idLblTitleADDRESS" class="control-label123"></label>
                    <input class="form-control123" maxlength="500" id="ADDRESS" name="ADDRESS" value="<%= sADDRESS%>">
                    <script>
                        $("#idLblTitleADDRESS").text(global_fm_address);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCreatedBy" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="CREATED_BY" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].CREATED_BY)%>"/>
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
                            <input class="form-control123" type="text" name="MODIFIED_BY" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_BY)%>"/>
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
                            <input class="form-control123" type="text" name="REQUEST_TIME" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].CREATED_DT)%>"/>
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
                            <input class="form-control123" type="text" name="MODIFIED_DT" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_DT)%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleModifiedDate").text(global_fm_date_endupdate);
                    </script>
                </div>
            </form>
        </div>
        <!-- Modal Cert Decline -->
<!--        <div id="myModalCertDecline" class="modal fade" role="dialog">
            <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                 left: 0; height: 100%;" class="loading-gifCertDecline">
                <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
            </div>
            <div class="modal-dialog modal-800" id="myDialogCertDecline">
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
        </div>-->
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