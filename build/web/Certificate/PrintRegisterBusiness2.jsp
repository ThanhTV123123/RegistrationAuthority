<%-- 
    Document   : PrintRegisterBusiness2
    Created on : Jan 13, 2020, 2:43:25 PM
    Author     : USER
--%>

<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="vn.ra.utility.PropertiesContent"%>
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
        <script src="../js/jquery.PrintArea.js"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            $(document).ready(function () {
                $('#REGISTER_DATE').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
                $('.loading-gif').hide();
                localStorage.setItem("PrintRegisterPersonal", null);
                localStorage.setItem("PrintRegisterBusiness", null);
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
            function popupPrint(id, idCSRF, isCA)
            {
                if (!JSCheckEmptyField($("#PRINT_EMAIL").val()))
                {
//                    if(isCA === JS_IS_WHICH_ABOUT_CA_NC) {
//                        $("#PRINT_EMAIL").focus();
//                        funErrorAlert(policy_req_empty + global_fm_email_receive);
//                        return false;
//                    }
                } else {
                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#PRINT_EMAIL").val()))
                    {
                        $("#PRINT_EMAIL").focus();
                        funErrorAlert(global_req_mail_format);
                        return false;
                    }
                }
                if (!JSCheckEmptyField($("#PRINT_PHONE").val()))
                {
//                    if(isCA === JS_IS_WHICH_ABOUT_CA_NC) {
//                        $("#PRINT_PHONE").focus();
//                        funErrorAlert(policy_req_empty + global_fm_phone);
//                        return false;
//                    }
                } else {
                    if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#PRINT_PHONE")))
                    {
                        $("#PRINT_PHONE").focus();
                        funErrorAlert(global_req_phone_format);
                        return false;
                    }
                }
                if (!JSCheckEmptyField($("#PRINT_RETURN_EMAIL").val()))
                {
//                    if(isCA === JS_IS_WHICH_ABOUT_CA_NC) {
//                        $("#PRINT_RETURN_EMAIL").focus();
//                        funErrorAlert(policy_req_empty + global_fm_email_receive);
//                        return false;
//                    }
                } else {
                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#PRINT_RETURN_EMAIL").val()))
                    {
                        $("#PRINT_RETURN_EMAIL").focus();
                        funErrorAlert(global_req_mail_format);
                        return false;
                    }
                }
                if (!JSCheckEmptyField($("#PRINT_RETURN_PHONE").val()))
                {
//                    if(isCA === JS_IS_WHICH_ABOUT_CA_NC) {
//                        $("#PRINT_RETURN_PHONE").focus();
//                        funErrorAlert(policy_req_empty + global_fm_phone);
//                        return false;
//                    }
                } else {
                    if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#PRINT_RETURN_PHONE")))
                    {
                        $("#PRINT_RETURN_PHONE").focus();
                        funErrorAlert(global_req_phone_format);
                        return false;
                    }
                }
                var isCheckRegisterInfo = "0";
                if ($("#idCheckRegisterInfo").is(':checked')) {
                    isCheckRegisterInfo = "1";
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../PrintFormCommon",
                    data: {
                        idParam: 'printregisterbusiness2',
                        id: id,
                        sDate: global_fm_report_print_date,
                        PRINT_FULLNAME: $("#PRINT_FULLNAME").val(),
                        PRINT_ADDRESS_BILLING: $("#PRINT_ADDRESS_BILLING").val(),
                        PRINT_TAXCODE: $("#PRINT_TAXCODE").val(),
                        PRINT_PHONE: $("#PRINT_PHONE").val(),
                        PRINT_EMAIL: $("#PRINT_EMAIL").val(),
                        PRINT_REPRESENTATIVE: $("#PRINT_REPRESENTATIVE").val(),
                        PRINT_ROLE: $("#PRINT_ROLE").val(),
                        PRINT_RETURN_FULLNAME: $("#PRINT_RETURN_FULLNAME").val(),
                        PRINT_RETURN_ADDRESS: $("#PRINT_RETURN_ADDRESS").val(),
                        PRINT_RETURN_PHONE: $("#PRINT_RETURN_PHONE").val(),
                        PRINT_RETURN_EMAIL: $("#PRINT_RETURN_EMAIL").val(),
                        REGISTER_DATE: $("#REGISTER_DATE").val(),
                        isCheckRegisterInfo: isCheckRegisterInfo,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('###');
                        if (myStrings[0] === "0")
                        {
                            $(".loading-gif").hide();
                            $('#over').remove();
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
                        else if (myStrings[0] === JS_EX_NO_DATA)
                        {
                            funErrorAlert(global_no_print_data);
                        }
                        else {
                            funErrorAlert(global_errorsql);
                        }
                        $(".loading-gif").hide();
                        $('#over').remove();
                    }
                });
                return false;
            }
            function popupZipWord(id, idCSRF)
            {
                if (JSCheckEmptyField($("#PRINT_EMAIL").val()))
                {
                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#PRINT_EMAIL").val()))
                    {
                        $("#PRINT_EMAIL").focus();
                        funErrorAlert(global_req_mail_format);
                        return false;
                    }
                }
                if (JSCheckEmptyField($("#PRINT_PHONE").val()))
                {
                    if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#PRINT_PHONE")))
                    {
                        $("#PRINT_PHONE").focus();
                        funErrorAlert(global_req_phone_format);
                        return false;
                    }
                }
                if (JSCheckEmptyField($("#PRINT_RETURN_EMAIL").val()))
                {
                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#PRINT_RETURN_EMAIL").val()))
                    {
                        $("#PRINT_RETURN_EMAIL").focus();
                        funErrorAlert(global_req_mail_format);
                        return false;
                    }
                }
                if (JSCheckEmptyField($("#PRINT_RETURN_PHONE").val()))
                {
                    if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#PRINT_RETURN_PHONE")))
                    {
                        $("#PRINT_RETURN_PHONE").focus();
                        funErrorAlert(global_req_phone_format);
                        return false;
                    }
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../PrintFormCommon",
                    data: {
                        idParam: 'printregisterbusinessrar2',
                        id: id,
                        sDate: global_fm_report_date,
                        PRINT_FULLNAME: $("#PRINT_FULLNAME").val(),
                        PRINT_ADDRESS_BILLING: $("#PRINT_ADDRESS_BILLING").val(),
                        PRINT_TAXCODE: $("#PRINT_TAXCODE").val(),
                        PRINT_PHONE: $("#PRINT_PHONE").val(),
                        PRINT_EMAIL: $("#PRINT_EMAIL").val(),
                        PRINT_REPRESENTATIVE: $("#PRINT_REPRESENTATIVE").val(),
                        PRINT_ROLE: $("#PRINT_ROLE").val(),
                        PRINT_RETURN_FULLNAME: $("#PRINT_RETURN_FULLNAME").val(),
                        PRINT_RETURN_ADDRESS: $("#PRINT_RETURN_ADDRESS").val(),
                        PRINT_RETURN_PHONE: $("#PRINT_RETURN_PHONE").val(),
                        PRINT_RETURN_EMAIL: $("#PRINT_RETURN_EMAIL").val(),
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('###');
                        if (myStrings[0] === "0")
                        {
                            var f = document.myname;
                            f.method = "post";
                            f.action = '../DowloadFile?filename=' + myStrings[2];
                            f.submit();
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
                        else if (myStrings[0] === JS_EX_NO_DATA)
                        {
                            funErrorAlert(global_no_print_data);
                        }
                        else {
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
                if(localStorage.getItem("sessCertToPrint") === "1")
                {
                    localStorage.setItem("sessCertToPrint", null);
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
                else
                {
                    $.ajax({
                        type: "post",
                        url: "../UserCommon",
                        data: {
                            idParam: 'backformpage',
                            idSession: 'RefreshRegisTokenSess'
                        },
                        cache: false,
                        success: function (html) {
                            var arr = sSpace(html);
                            if (arr === "0")
                            {
                                window.location = "RegisterCertList.jsp";
                            }
                            else
                            {
                                window.location = "RegisterCertList.jsp";
                            }
                        }
                    });
                    return false;
                }
            }
        </script>
        <style>
            @media (min-width: 768px){.modal-dialog{width: 900px;}}
            .modal-header{
                padding: 10px 10px 0px 10px;border-bottom:0px;
            }
        </style>
    </head>
    <body>
        <%         
        if (session.getAttribute("sUserID") != null) {
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
            String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
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
            String sREGEX_PHONE = Definitions.CONFIG_DEFAULT_VALUE_REGEX_PHONE;
            String sREGEX_EMAIL = Definitions.CONFIG_DEFAULT_VALUE_REGEX_EMAIL;
            if(!"".equals(sRegexPolicy))
            {
                sREGEX_PHONE = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_PHONE);
                sREGEX_EMAIL = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_EMAIL);
            }
        %>
        <script>
            $(document).ready(function () {
                localStorage.setItem("sessLocal_REGEX_PHONE", '<%=sREGEX_PHONE%>');
                localStorage.setItem("sessLocal_REGEX_EMAIL", '<%=sREGEX_EMAIL%>');
            });
        </script>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <div class="x_panel">
        <%                                        CERTIFICATION[][] rs = new CERTIFICATION[1][];
            String strCOMPANY = "";
            String strMSTORMNS = "";
            String strADDRESS = "";
            String strEMAIL_CONTACT = "";
            String strPHONE_CONTACT = "";
            try {
                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                if (EscapeUtils.IsInteger(ids) == true) {
                    db.S_BO_CERTIFICATION_GET_INFO(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                    if (rs[0].length > 0) {
                        strCOMPANY = EscapeUtils.CheckTextNull((rs[0][0].COMPANY_NAME));
                        PREFIX_UUID[][] sessPrefix;
                        sessPrefix = (PREFIX_UUID[][]) session.getAttribute("sessPrefixUIDEnterprise");
                        strMSTORMNS = rs[0][0].ENTERPRISE_ID;// CommonReferServlet.filterPrefixUIDAuto(rs[0][0].ENTERPRISE_ID, sessPrefix);
//                        strMSTORMNS = EscapeUtils.CheckTextNull((rs[0][0].TAX_CODE));
//                        if("".equals(strMSTORMNS)) {
//                            strMSTORMNS = EscapeUtils.CheckTextNull((rs[0][0].BUDGET_CODE));
//                        }
//                        if("".equals(strMSTORMNS)) {
//                            strMSTORMNS = EscapeUtils.CheckTextNull((rs[0][0].DECISION));
//                        }
                        strEMAIL_CONTACT = EscapeUtils.CheckTextNull((rs[0][0].EMAIL_CONTRACT));
                        strPHONE_CONTACT = EscapeUtils.CheckTextNull((rs[0][0].PHONE_CONTRACT));
//                        String sSubjectDN = EscapeUtils.CheckTextNull((rs[0][0].SUBJECT));
//                        strEMAIL = CommonFunction.getEmailInDN(sSubjectDN);
//                        String sLocation = EscapeUtils.CheckTextNull(CommonFunction.getLocationInDN(sSubjectDN));
//                        strADDRESS = CommonFunction.getStateOrProvinceInDN(sSubjectDN);
//                        if(!"".equals(sLocation))
//                        {
//                            strADDRESS = sLocation + ", " + strADDRESS;
//                        }
//                        strPHONE = CommonFunction.getPhoneInDN(sSubjectDN);
//                        strORGANI = CommonFunction.getORGANIZATIONInDN(sSubjectDN.replace("\"", "&quot;"));
                        boolean isAccessAgencyPage = true;
                        if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            BRANCH[][] branchAccess = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                            isAccessAgencyPage = CommonFunction.checkBranchTreeInvalidCert(rs[0][0].BRANCH_ID, branchAccess);
                        }
                        if (isAccessAgencyPage == true) {
                            String sContactRepName = "";
                            String sPrfileContact = EscapeUtils.CheckTextNull(rs[0][0].PROFILE_CONTACT_INFO);
                            if(!"".equals(sPrfileContact)) {
                                ObjectMapper oMapperParse = new ObjectMapper();
                                ProfileContactInfoJson profileContact = oMapperParse.readValue(sPrfileContact, ProfileContactInfoJson.class);
                                if(profileContact != null) {
                                    sContactRepName = CommonFunction.replaceCharaterSpecialJson(profileContact.RepresentativeName, false);
                                    strADDRESS = CommonFunction.replaceCharaterSpecialJson(profileContact.Address, false);
                                }
                            }
                            String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)){
                                strEMAIL_CONTACT = "";
                                strPHONE_CONTACT = "";
                            }
        %>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitlePrintEdits"></span></h2>
            <script>$("#idLblTitlePrintEdits").text(certlist_title_print_register);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <input id="btnPrintCert" class="btn btn-info" type="button" onclick="popupPrint('<%=ids%>', '<%= anticsrf%>', '<%=isCALoad%>');" />
                    <input id="btnZipWord" style="display: none;" class="btn btn-info" type="button" onclick="popupZipWord('<%=ids%>', '<%= anticsrf%>');" />
                </li>
                <script>
                    document.getElementById("btnPrintCert").value = global_fm_button_print;
                    document.getElementById("btnZipWord").value = global_fm_button_export_zip_pdf;
                </script>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <form name="myname" method="post" class="form-horizontal">
                <input type="hidden" id="sID" name="sID" hidden="true" readonly="true" value="<%= rs[0][0].ID%>">
                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                <fieldset class="scheduler-border">
                    <legend class="scheduler-border" id="idLblTitleContactBusi"></legend>
                    <div class="col-sm-6" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePrintFullname"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PRINT_FULLNAME" id="PRINT_FULLNAME" value="<%=EscapeUtils.escapeHtmlDN(strCOMPANY)%>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleContactBusi").text(certlist_group_add_bussiness_info);
                            $("#idLblTitlePrintFullname").text(global_print2_fullname_business);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePrintMST"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PRINT_TAXCODE" id="PRINT_TAXCODE" value="<%=strMSTORMNS%>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitlePrintMST").text(global_fm_MST);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <!--<label id="idLblTitleEmail" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>-->
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleEmail"></label>
                                <!--<label id="idLblNoteEmail" class="CssRequireField"></label>-->
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PRINT_EMAIL" id="PRINT_EMAIL" value="<%=strEMAIL_CONTACT%>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleEmail").text(global_fm_email_receive);
//                            $(document).ready(function () {
//                                if(IsWhichCA === JS_IS_WHICH_ABOUT_CA_NC){
//                                    $("#idLblTitleEmail").html(global_fm_email_receive + '<span style="color: red; font-weight: 100;">' + global_fm_require_label + '</span>');
//                                }
//                            });
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <!--<label id="idLblTitlePhone" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>-->
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePhone"></label>
                                <label id="idLblNotePhone" class="CssRequireField"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PRINT_PHONE" id="PRINT_PHONE" value="<%=strPHONE_CONTACT%>"
                                    class="form-control123" onblur="autoTrimTextField('PRINT_PHONE', this.value);"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitlePhone").text(global_fm_phone);
//                            $(document).ready(function () {
//                                if(IsWhichCA === JS_IS_WHICH_ABOUT_CA_NC) {
//                                    $("#idLblNotePhone").text(global_fm_require_label);
//                                }
//                            });
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <label id="idLblTitleRepresentative" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PRINT_REPRESENTATIVE" id="PRINT_REPRESENTATIVE" value="<%=sContactRepName%>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleRepresentative").text(branch_fm_representative);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleRole" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PRINT_ROLE" id="PRINT_ROLE"  class="form-control123"/>
                            </div>
                        </div>
                        <script>$("#idLblTitleRole").text(global_fm_role);</script>
                    </div>
                    <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;clear: both;">
                        <label class="control-label123" id="idLblTitlePrintAddressBilling"></label>
                        <input type="text" name="PRINT_ADDRESS_BILLING" id="PRINT_ADDRESS_BILLING" value="<%=strADDRESS%>" class="form-control123">
                        <script>
                            $("#idLblTitlePrintAddressBilling").text(token_fm_address_billing);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <div class="col-sm-5" style="padding-left: 0">
                                <label id="idLblTitleInfoRegisterDate" class="control-label" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                </label>&nbsp;&nbsp;<input type="checkbox" name="idCheck" id="idCheckS"/>
                            </div>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <style>
                                    .form-control123[readonly]{background-color:#ffffff;}
                                    .form-control123[disabled]{background-color:#eee;opacity:1}
                                </style>
                                <input type="text" name="REGISTER_DATE" readonly id="REGISTER_DATE" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleInfoRegisterDate").text(global_fm_register_date);
                            $(document).ready(function () {
                                document.getElementById("REGISTER_DATE").disabled = true;
                                $('#idCheckS').click(function() {
                                    if (!$(this).is(':checked')) {
                                       document.getElementById("REGISTER_DATE").value = "";
                                        document.getElementById("REGISTER_DATE").disabled = true;
                                    } else {
                                        document.getElementById("REGISTER_DATE").value = "";
                                        document.getElementById("REGISTER_DATE").disabled = false;
                                    }
                                });
                            });
                        </script>
                    </div>
                </fieldset>
                <fieldset class="scheduler-border">
                    <legend class="scheduler-border" id="idLblTitleContactReturn"></legend>
                    <div class="col-sm-6" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <label id="idLblTitleRegisterInfo" class="control-label" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            </label>&nbsp;&nbsp;<input type="checkbox" checked name="idCheckRegisterInfo" onchange="onChangeRegisInfo();" id="idCheckRegisterInfo"/>
                            <script>
                                function onChangeRegisInfo(){
                                    if ($("#idCheckRegisterInfo").is(':checked')) {
                                        document.getElementById("PRINT_RETURN_FULLNAME").disabled = false;
                                        document.getElementById("PRINT_RETURN_ADDRESS").disabled = false;
                                        document.getElementById("PRINT_RETURN_EMAIL").disabled = false;
                                        document.getElementById("PRINT_RETURN_PHONE").disabled = false;
                                    } else {
                                        document.getElementById("PRINT_RETURN_FULLNAME").disabled = true;
                                        document.getElementById("PRINT_RETURN_ADDRESS").disabled = true;
                                        document.getElementById("PRINT_RETURN_EMAIL").disabled = true;
                                        document.getElementById("PRINT_RETURN_PHONE").disabled = true;
                                    }
                                }
                            </script>
                        </div>
                        <script>
                            $("#idLblTitleRegisterInfo").text(global_fm_register_info);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePrintReturnFullname"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PRINT_RETURN_FULLNAME" id="PRINT_RETURN_FULLNAME" value="<%=sContactRepName%>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleContactReturn").text(certlist_group_return_contact_info);
                            $("#idLblTitlePrintReturnFullname").text(global_fm_fullname);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePrintReturnAddress"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PRINT_RETURN_ADDRESS" id="PRINT_RETURN_ADDRESS" value="<%= strADDRESS%>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitlePrintReturnAddress").text(global_fm_address);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <!--<label id="idLblTitleEmailReturn" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>-->
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleEmailReturn"></label>
                                <!--<label id="idLblNoteEmailReturn" class="CssRequireField"></label>-->
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PRINT_RETURN_EMAIL" id="PRINT_RETURN_EMAIL" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleEmailReturn").text(global_fm_email_receive);
//                            $(document).ready(function () {
//                                if(IsWhichCA === JS_IS_WHICH_ABOUT_CA_NC) {
//                                    $("#idLblTitleEmailReturn").html(global_fm_email_receive + '<span style="color: red; font-weight: 100;">' + global_fm_require_label + '</span>');
//                                }
//                            });
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <!--<label id="idLblTitlePhoneReturn" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>-->
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePhoneReturn"></label>
                                <!--<label id="idLblNotePhoneReturn" class="CssRequireField"></label>-->
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PRINT_RETURN_PHONE" id="PRINT_RETURN_PHONE" class="form-control123"
                                    onblur="autoTrimTextField('PRINT_RETURN_PHONE', this.value);"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitlePhoneReturn").text(global_fm_phone);
//                            $(document).ready(function () {
//                                if(IsWhichCA === JS_IS_WHICH_ABOUT_CA_NC) {
//                                    $("#idLblNotePhoneReturn").text(global_fm_require_label);
//                                }
//                            });
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
        </div>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../js/moment.min.js"></script>
        <script src="../js/daterangepicker.js"></script>
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