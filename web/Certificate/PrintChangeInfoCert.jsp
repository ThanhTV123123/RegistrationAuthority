<%-- 
    Document   : PrintChangeInfoCert
    Created on : May 27, 2020, 11:05:56 AM
    Author     : USER
--%>
<%@page import="vn.ra.process.CommonReferServlet"%>
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
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <script src="../js/jquery.PrintArea.js"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            $(document).ready(function () {
                $('#INFO_DATE_GRANT').daterangepicker({
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
            function popupPrint(id, idCSRF)
            {
//                if (JSCheckEmptyField($("#PRINT_EMAIL").val()))
//                {
//                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#PRINT_EMAIL").val()))
//                    {
//                        $("#PRINT_EMAIL").focus();
//                        funErrorAlert(global_req_mail_format);
//                        return false;
//                    }
//                }
//                if (JSCheckEmptyField($("#PRINT_PHONE").val()))
//                {
//                    if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#PRINT_PHONE")))
//                    {
//                        $("#PRINT_PHONE").focus();
//                        funErrorAlert(global_req_phone_format);
//                        return false;
//                    }
//                }
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
                        idParam: 'printchangeinfo',
                        id: id,
                        sDate: global_fm_report_date,
                        PRINT_FULLNAME: $("#PRINT_FULLNAME").val(),
                        PRINT_TAXCODE: $("#PRINT_TAXCODE").val(),
                        PRINT_CMND: $("#PRINT_PHONE").val(),
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
            String strORGANI = "";
            String strMSTORMNS = "";
            String strADDRESS = "";
            String strEMAIL = "";
            String strPHONE = "";
            String strCMND = "";
            String strPHONE_CONTACT = "";
            try {
                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                if (EscapeUtils.IsInteger(ids) == true) {
                    db.S_BO_CERTIFICATION_GET_INFO(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                    if (rs[0].length > 0) {
                        PREFIX_UUID[][] sessPrefix;
                        strCOMPANY = EscapeUtils.CheckTextNull(rs[0][0].COMPANY_NAME);
                        sessPrefix = (PREFIX_UUID[][]) session.getAttribute("sessPrefixUIDEnterprise");
                        strMSTORMNS = rs[0][0].ENTERPRISE_ID;// CommonReferServlet.filterPrefixUIDAuto(rs[0][0].ENTERPRISE_ID, sessPrefix);//EscapeUtils.CheckTextNull((rs[0][0].TAX_CODE));
                        sessPrefix = (PREFIX_UUID[][]) session.getAttribute("sessPrefixUIDPersonal");
                        strCMND = rs[0][0].PERSONAL_ID;// CommonReferServlet.filterPrefixUIDAuto(rs[0][0].PERSONAL_ID, sessPrefix);// EscapeUtils.CheckTextNull((rs[0][0].P_ID));
                        strPHONE_CONTACT = EscapeUtils.CheckTextNull(rs[0][0].PHONE_CONTRACT);
                        String sSubjectDN = EscapeUtils.CheckTextNull(rs[0][0].SUBJECT);
                        strEMAIL = CommonFunction.getEmailInDN(sSubjectDN);
                        String sLocation = EscapeUtils.CheckTextNull(CommonFunction.getLocationInDN(sSubjectDN));
                        strADDRESS = CommonFunction.getStateOrProvinceInDN(sSubjectDN);
                        if(!"".equals(sLocation))
                        {
                            strADDRESS = sLocation + ", " + strADDRESS;
                        }
                        strPHONE = CommonFunction.getPhoneInDN(sSubjectDN);
                        strORGANI = CommonFunction.getORGANIZATIONInDN(sSubjectDN.replace("\"", "&quot;"));
                        boolean isAccessAgencyPage = true;
                        if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            BRANCH[][] branchAccess = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                            isAccessAgencyPage = CommonFunction.checkBranchTreeInvalidCert(rs[0][0].BRANCH_ID, branchAccess);
                        }
                        if (isAccessAgencyPage == true) {
        %>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitlePrintEdits"></span></h2>
            <script>$("#idLblTitlePrintEdits").text(certlist_title_print_changeinfo);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <input id="btnPrintCert" class="btn btn-info" type="button" onclick="popupPrint('<%=ids%>', '<%= anticsrf%>');" />
                </li>
                <script>
                    document.getElementById("btnPrintCert").value = global_fm_button_print;
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
                            $("#idLblTitleContactBusi").text(certlist_group_add_buss_pers_info);
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
                            $("#idLblTitlePrintMST").text(global_fm_enterprise_id);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <label id="idLblTitleEmail" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PRINT_CMND" id="PRINT_CMND" value="<%=strCMND%>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleEmail").text(global_fm_personal_id);</script>
                    </div>
                </fieldset>
                <fieldset class="scheduler-border">
                    <legend class="scheduler-border" id="idLblTitleContactReturn"></legend>
                    <div class="col-sm-6" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePrintReturnFullname"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PRINT_RETURN_FULLNAME" id="PRINT_RETURN_FULLNAME" class="form-control123">
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
                                <input type="text" name="PRINT_RETURN_ADDRESS" id="PRINT_RETURN_ADDRESS" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitlePrintReturnAddress").text(global_fm_address);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <label id="idLblTitleEmailReturn" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PRINT_RETURN_EMAIL" id="PRINT_RETURN_EMAIL" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleEmailReturn").text(global_fm_email_receive);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitlePhoneReturn" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PRINT_RETURN_PHONE" id="PRINT_RETURN_PHONE" class="form-control123"
                                    onblur="autoTrimTextField('PRINT_RETURN_PHONE', this.value);"/>
                            </div>
                        </div>
                        <script>$("#idLblTitlePhoneReturn").text(global_fm_phone);</script>
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
