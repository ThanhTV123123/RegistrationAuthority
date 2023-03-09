<%-- 
    Document   : PrintRegisterPersonal
    Created on : Aug 5, 2018, 6:20:13 PM
    Author     : THANH-PC
--%>

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
                $('#TO_DATE').daterangepicker({
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
                if (!JSCheckEmptyField($("#TO_DATE").val()))
                {
                    $("#TO_DATE").focus();
                    funErrorAlert(policy_req_empty + global_fm_cmnd_date);
                    return false;
                }
                if (!JSCheckEmptyField($("#TO_PLACE").val()))
                {
                    $("#TO_PLACE").focus();
                    funErrorAlert(policy_req_empty + global_fm_place);
                    return false;
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../PrintFormCommon",
                    data: {
                        idParam: 'printregisterpersonal',
                        id: id,
                        sDate: global_fm_report_date,
                        TO_DATE: $("#TO_DATE").val(),
                        TO_PLACE: $("#TO_PLACE").val(),
                        TO_ADDRESS: $("#TO_ADDRESS").val(),
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
            
            function popupWordConfirm(id, idCSRF)
            {
                if (!JSCheckEmptyField($("#TO_DATE").val()))
                {
                    $("#TO_DATE").focus();
                    funErrorAlert(policy_req_empty + global_fm_cmnd_date);
                    return false;
                }
                if (!JSCheckEmptyField($("#TO_PLACE").val()))
                {
                    $("#TO_PLACE").focus();
                    funErrorAlert(policy_req_empty + global_fm_place);
                    return false;
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../PrintFormCommon",
                    data: {
                        idParam: 'wordregisterpersonal',
                        id: id,
                        sDate: global_fm_report_date,
                        TO_DATE: $("#TO_DATE").val(),
                        TO_PLACE: $("#TO_PLACE").val(),
                        TO_ADDRESS: $("#TO_ADDRESS").val(),
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
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <div class="x_panel">
        <%                                        CERTIFICATION[][] rs = new CERTIFICATION[1][];
            String strCMNDHC = "";
            try {
                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                if (EscapeUtils.IsInteger(ids) == true) {
                    db.S_BO_CERTIFICATION_GET_INFO(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                    if (rs[0].length > 0) {
                        PREFIX_UUID[][] sessPrefix;
                        sessPrefix = (PREFIX_UUID[][]) session.getAttribute("sessPrefixUIDPersonal");
                        strCMNDHC = rs[0][0].PERSONAL_ID;// CommonReferServlet.filterPrefixUIDAuto(rs[0][0].PERSONAL_ID, sessPrefix);
                        String sSubjectDN = EscapeUtils.CheckTextNull((rs[0][0].SUBJECT));
                        String strADDRESS = "";
                        String sLocation = EscapeUtils.CheckTextNull(CommonFunction.getLocationInDN(sSubjectDN));
                        strADDRESS = CommonFunction.getStateOrProvinceInDN(sSubjectDN);
                        if(!"".equals(sLocation)) {
                            strADDRESS = sLocation + ", " + strADDRESS;
                        }
                        boolean isAccessAgencyPage = true;
                        if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            BRANCH[][] branchAccess = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                            isAccessAgencyPage = CommonFunction.checkBranchTreeInvalidCert(rs[0][0].BRANCH_ID, branchAccess);
                        }
                        if (isAccessAgencyPage == true) {
        %>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEditsPrintPer"></span></h2>
            <script>$("#idLblTitleEditsPrintPer").text(certlist_title_print_register);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <input id="btnPrintCertPer" class="btn btn-info" type="button" onclick="popupPrint('<%=ids%>', '<%= anticsrf%>');" />
                </li>
                <script>
                    document.getElementById("btnPrintCertPer").value = global_fm_button_print;
                </script>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <form name="myname" method="post" class="form-horizontal">
                <input type="hidden" id="sID" name="sID" hidden="true" readonly="true" value="<%= rs[0][0].ID%>">
                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                <fieldset class="scheduler-border">
                    <legend class="scheduler-border" id="idLblTitleGroupInfo"></legend>
                    <script>$("#idLblTitleGroupInfo").text(certlist_group_add_info);</script>
                    <style>
                        .aba123[readonly]{background-color:#ffffff;opacity:1}
                    </style>
                    <div class="form-group">
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCMND" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" name="FROM_CMND" id="FROM_CMND" readonly value="<%= strCMNDHC%>" class="form-control123">
                                </div>
                            </div>
                            <script>$("#idLblTitleCMND").text(global_fm_personal_id);</script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                    <label id="idLblTitleDate"></label>
                                    <label class="CssRequireField" id="idLblNoteDate"></label>
                                </label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" name="TO_DATE" value="" readonly id="TO_DATE" class="form-control123 aba123">
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleDate").text(global_fm_cmnd_date);
                                $("#idLblNoteDate").text(global_fm_require_label);
                            </script>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                    <label id="idLblTitlePlace"></label>
                                    <label class="CssRequireField" id="idLblNotePlace"></label>
                                </label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" name="TO_PLACE" id="TO_PLACE" class="form-control123">
                                </div>
                            </div>
                            <script>
                                $("#idLblTitlePlace").text(global_fm_place);
                                $("#idLblNotePlace").text(global_fm_require_label);
                            </script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                    <label id="idLblTitleAddress"></label>
                                </label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" name="TO_ADDRESS" id="TO_ADDRESS" value="<%= strADDRESS%>" class="form-control123">
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleAddress").text(token_fm_address_permanent);
                            </script>
                        </div>
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
        <!--<script src="../style/custom.min.js"></script>-->
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
