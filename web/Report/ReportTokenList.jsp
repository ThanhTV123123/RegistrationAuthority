<%-- 
    Document   : ReportTokenList
    Created on : Nov 28, 2019, 4:16:29 PM
    Author     : USER
--%>

<%@page import="java.net.URLEncoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<%  response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
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
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script language="javascript">
            changeFavicon("../");
            document.title = tokenreport_title_list;
            $(document).ready(function () {
                localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", null);
                $('#demo1').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
                $('#demo2').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
                $('.loading-gif').hide();
                localStorage.setItem("TransferPage", null);
                $('#myModalRegisterPrint').modal({
                    backdrop: 'static',
                    keyboard: true,
                    show: false
                });
                $('#idCheck').removeClass("js-switch");
                $('#idCheck').addClass("js-switch");
                
            });
            
            function popupDetailCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('CertView.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
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
            function popupPrintCertList(id, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
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
            function searchForm(id)
            {
//                var idFromCreateDate = document.form.FromCreateDate.value;
//                var idToCreateDate = document.form.ToCreateDate.value;
//                if (!JSCheckDateDDMMYYYYSlash(idFromCreateDate))
//                {
//                    funErrorAlert(global_fm_FromDate + global_error_invalid);
//                    return false;
//                }
//                if (!JSCheckDateDDMMYYYYSlash(idToCreateDate))
//                {
//                    funErrorAlert(global_fm_ToDate + global_error_invalid);
//                    return false;
//                }
//                if (parseDate(idToCreateDate).getTime() < parseDate(idFromCreateDate).getTime())
//                {
//                    funErrorAlert(global_check_datesearch);
//                    return false;
//                }
//                else
//                {
                    document.getElementById("idHiddenLoad").value = JS_STR_GRID_SEARCH_RESET;
                    document.getElementById("tempCsrfToken").value = id;
                    var f = document.form;
                    f.method = "post";
                    f.action = '';
                    f.submit();
//                }
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
        </script>
        <style>
            .projects th{font-weight: bold;}.navbar-right{margin-right: 0;padding-right:10px;}
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.2em 10px 1.2em !important;
                margin: 0 0 12px 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
            @media (min-width: 768px){.modal-dialog{width: 900px;}}
            .modal-header{
                padding: 10px 10px 10px 10px;border-bottom:0px;
            }
            .x_panel{padding: 10px 10px}
        </style>
    </head>
    <body class="nav-md">
        <%
            if (session.getAttribute("sUserID") != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String sessTreeArrayBranchID = session.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
//                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
//                String SessUserID = session.getAttribute("UserID").toString().trim();
//                String SessRoleID_ID = session.getAttribute("RoleID_ID").toString().trim();
//                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Cert");
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
                        document.getElementById("idNameURL").innerHTML = tokenreport_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%                            int status = 1000;
                                int statusLoad = 0;
                                int j = 1;
//                                iSwRws = 10;
//                                iTotSrhRcrds = 5;
                                String nameHiddenLoad = request.getParameter("nameHiddenLoad");
                                String tempIPagNo = request.getParameter("iPagNo");
                                int iTotRslts = com.Converter(request.getParameter("iTotRslts"));
                                int iTotPags = com.Converter(request.getParameter("iTotPags"));
                                int iPagNo = com.Converter(tempIPagNo);
                                int iPaNoSS = iPagNo;
                                int cPagNo = com.Converter(request.getParameter("cPagNo"));
                                if (Definitions.CONFIG_GRID_SEARCH_RESET.equals(nameHiddenLoad)) {
                                    tempIPagNo = "null";
                                    iPagNo = 0;
                                    cPagNo = 0;
                                    iPaNoSS = 0;
                                }
                                int iEnRsNo = 0;
                                if (iPagNo == 0) {
                                    iPagNo = 0;
                                } else {
                                    iPagNo = Math.abs((iPagNo - 1) * iSwRws);
                                }
                                String hasPaging = "0";
                                if (tempIPagNo != null && !"null".equals(tempIPagNo) && !"".equals(tempIPagNo)) {
                                    hasPaging = "1";
                                }
                                String strMess = "";
                                try {
                                    TOKEN[][] rsPgin = new TOKEN[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (session.getAttribute("RefreshTokenReportSess") != null && session.getAttribute("sessFromCreateDateTokenReport") != null
                                            && session.getAttribute("sessToCreateDateTokenReport") != null) {
                                        session.setAttribute("RefreshTokenReportSessPaging", "1");
                                        session.setAttribute("SearchSharePagingTokenReport", "0");
                                        statusLoad = 1;
                                        String FromCreateDate = (String) session.getAttribute("sessFromCreateDateTokenReport");
                                        String ToCreateDate = (String) session.getAttribute("sessToCreateDateTokenReport");
                                        String METHOD = (String) session.getAttribute("sessMethodTokenReport");
                                        String AGENT_ID = (String) session.getAttribute("sessAGENT_IDTokenReport");
                                        
                                        session.setAttribute("RefreshTokenReportSess", null);
                                        session.setAttribute("sessFromCreateDateTokenReport", FromCreateDate);
                                        session.setAttribute("sessToCreateDateTokenReport", ToCreateDate);
                                        session.setAttribute("sessMethodTokenReport", METHOD);
                                        session.setAttribute("sessAGENT_IDTokenReport", AGENT_ID);
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(AGENT_ID)) {
                                            AGENT_ID = "";
                                        }
                                        int ss = 0;
                                        String pREMAINING_BEGINING_MONTH="0";
                                        String pIMPORT_IN_MONTH="0";
                                        String pTOKEN_USED_IN_MONTH="0";
                                        String pREAMINING_END_MONTH="0";
                                        if("1".equals(METHOD)) {
                                            pREMAINING_BEGINING_MONTH="1";
                                        }
                                        if("2".equals(METHOD)) {
                                            pIMPORT_IN_MONTH="1";
                                        }
                                        if("3".equals(METHOD)) {
                                            pTOKEN_USED_IN_MONTH="1";
                                        }
                                        if("4".equals(METHOD)) {
                                            pREAMINING_END_MONTH="1";
                                        }
                                        if(SessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            sessTreeArrayBranchID = "";
                                        }
                                        if ((session.getAttribute("CountListTokenReport")) == null) {
                                            ss = db.S_BO_TOKEN_REPORT_TOTAL(AGENT_ID, EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), pREMAINING_BEGINING_MONTH,
                                                pIMPORT_IN_MONTH, pTOKEN_USED_IN_MONTH, pREAMINING_END_MONTH, sessTreeArrayBranchID);
                                            session.setAttribute("CountListTokenReport", String.valueOf(ss));
                                        } else {
                                            String sCount = (String) session.getAttribute("CountListTokenReport");
                                            ss = Integer.parseInt(sCount);
                                            session.setAttribute("CountListTokenReport", String.valueOf(ss));
                                        }
                                        if (session.getAttribute("SearchIPageNoPagingTokenReport") != null) {
                                            String sPage = (String) session.getAttribute("SearchIPageNoPagingTokenReport");
                                            iPagNo = Integer.parseInt(sPage);
                                        }
                                        if (session.getAttribute("SearchISwRwsPagingTokenReport") != null) {
                                            String sSumPage = (String) session.getAttribute("SearchISwRwsPagingTokenReport");
                                            iSwRws = Integer.parseInt(sSumPage);
                                        }
                                        if (session.getAttribute("RefreshTokenReportSessNumberPaging") != null) {
                                            String sNoPage = (String) session.getAttribute("RefreshTokenReportSessNumberPaging");
                                            iPaNoSS = Integer.parseInt(sNoPage);
                                        }
                                        session.setAttribute("SearchIPageNoPagingTokenReport", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingTokenReport", String.valueOf(iSwRws));
                                        if (ss > 0) {
                                            db.S_BO_TOKEN_REPORT_LIST(AGENT_ID, EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), pREMAINING_BEGINING_MONTH,
                                                pIMPORT_IN_MONTH, pTOKEN_USED_IN_MONTH, pREAMINING_END_MONTH,
                                                iPagNo, iSwRws, Integer.parseInt(sessLanguageGlobal), rsPgin, sessTreeArrayBranchID);
                                        }
                                        iTotRslts = ss;
                                        if (ss > 0) {
                                            strMess = com.convertMoney(ss);
                                        }
                                        if (iTotRslts > 0 && rsPgin[0].length > 0) {
                                            status = 1;
                                        } else {
                                            status = 1000;
                                        }
                                    } else if (request.getMethod().equals("POST") || "1".equals(hasPaging)) {
                                        session.setAttribute("RefreshTokenReportSessPaging", null);
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("pIaTokenReport", null);
                                            session.setAttribute("pIbTokenReport", null);
                                            session.setAttribute("SearchShareStoreTokenReport", null);
                                            session.setAttribute("SearchIPageNoPagingTokenReport", null);
                                            session.setAttribute("SearchISwRwsPagingTokenReport", null);
                                        }
                                        String sCsrfToken = request.getParameter("CsrfToken");
                                        String stempCsrfToken = request.getParameter("tempCsrfToken");
                                        if (!"1".equals(hasPaging)) {
                                            if (sCsrfToken != null && sCsrfToken.equals(stempCsrfToken)) {
                                            } else {
                            %>
                            <script type="text/javascript">
                                window.onload = function () {
                                    funCsrfAlert();
                                }();
                            </script>
                            <%
                                        }
                                    }
                                    statusLoad = 1;
                                    request.setCharacterEncoding("UTF-8");
                                    String FromCreateDate = request.getParameter("FromCreateDate");
                                    String ToCreateDate = request.getParameter("ToCreateDate");
                                    String METHOD = request.getParameter("METHOD");
                                    String AGENT_ID = request.getParameter("AGENT_ID");
                                    if ("1".equals(hasPaging)) {
                                        session.setAttribute("SearchSharePagingTokenReport", "0");
                                        ToCreateDate = (String) session.getAttribute("sessToCreateDateTokenReport");
                                        FromCreateDate = (String) session.getAttribute("sessFromCreateDateTokenReport");
                                        METHOD = (String) session.getAttribute("sessMethodTokenReport");
                                        AGENT_ID = (String) session.getAttribute("sessAGENT_IDTokenReport");
                                        session.setAttribute("SessParamOnPagingCertList", null);
                                    } else {
                                        session.setAttribute("SearchSharePagingTokenReport", "1");
                                        session.setAttribute("CountListTokenReport", null);
                                    }
                                    session.setAttribute("sessFromCreateDateTokenReport", FromCreateDate);
                                    session.setAttribute("sessToCreateDateTokenReport", ToCreateDate);
                                    session.setAttribute("sessMethodTokenReport", METHOD);
                                    session.setAttribute("sessAGENT_IDTokenReport", AGENT_ID);
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(AGENT_ID)) {
                                        AGENT_ID = "";
                                    }
                                    int ss = 0;
                                    String pREMAINING_BEGINING_MONTH="0";
                                    String pIMPORT_IN_MONTH="0";
                                    String pTOKEN_USED_IN_MONTH="0";
                                    String pREAMINING_END_MONTH="0";
                                    if("1".equals(METHOD)) {
                                        pREMAINING_BEGINING_MONTH="1";
                                    }
                                    if("2".equals(METHOD)) {
                                        pIMPORT_IN_MONTH="1";
                                    }
                                    if("3".equals(METHOD)) {
                                        pTOKEN_USED_IN_MONTH="1";
                                    }
                                    if("4".equals(METHOD)) {
                                        pREAMINING_END_MONTH="1";
                                    }
                                    if(SessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                        sessTreeArrayBranchID = "";
                                    }
                                    if ((session.getAttribute("CountListTokenReport")) == null) {
                                        ss = db.S_BO_TOKEN_REPORT_TOTAL(AGENT_ID, EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), pREMAINING_BEGINING_MONTH,
                                                pIMPORT_IN_MONTH, pTOKEN_USED_IN_MONTH, pREAMINING_END_MONTH, sessTreeArrayBranchID);
                                        session.setAttribute("CountListTokenReport", String.valueOf(ss));
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListTokenReport");
                                        ss = Integer.parseInt(sCount);
                                        session.setAttribute("CountListTokenReport", String.valueOf(ss));
                                    }
                                    iTotRslts = ss;
                                    if (iTotRslts > 0) {
                                        db.S_BO_TOKEN_REPORT_LIST(AGENT_ID, EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), pREMAINING_BEGINING_MONTH,
                                                pIMPORT_IN_MONTH, pTOKEN_USED_IN_MONTH, pREAMINING_END_MONTH,
                                                iPagNo, iSwRws, Integer.parseInt(sessLanguageGlobal), rsPgin, sessTreeArrayBranchID);
                                        session.setAttribute("SearchIPageNoPagingTokenReport", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingTokenReport", String.valueOf(iSwRws));
                                        strMess = com.convertMoney(ss);
                                        if (rsPgin[0].length > 0) {
                                            status = 1;
                                        } else {
                                            status = 1000;
                                        }
                                    } else {
                                        status = 1000;
                                    }
                                } else {
                                    session.setAttribute("RefreshTokenReportSessPaging", null);
                                    session.setAttribute("pIaTokenReport", null);
                                    session.setAttribute("pIbTokenReport", null);
                                    session.setAttribute("SearchShareStoreTokenReport", null);
                                    session.setAttribute("SearchIPageNoPagingTokenReport", null);
                                    session.setAttribute("SearchISwRwsPagingTokenReport", null);
                                    session.setAttribute("sessFromCreateDateTokenReport", null);
                                    session.setAttribute("sessToCreateDateTokenReport", null);
                                    session.setAttribute("sessIsByImportTokenReport", null);
                                    session.setAttribute("sessTOKEN_SNTokenReport", null);
                                    session.setAttribute("sessTOKEN_VERSIONTokenReport", "All");
                                    session.setAttribute("sessAGENT_IDTokenReport", "All");
                                }
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
<!--                                   <div class="x_title">
                                        <h2><i class="fa fa-search"></i> <script>document.write(reportcertexpire_table_search);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <button type="button" class="btn btn-info" onClick="searchForm('<=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>-->
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="form" method="post" class="form-horizontal">
                                            <input id="idHiddenLoad" name="nameHiddenLoad" value="" type="hidden"/>
                                            <script>
                                                function checkboxChange() {
                                                    if ($("#idCheck").is(':checked')) {
                                                        document.getElementById("demo1").disabled = false;
                                                        document.getElementById("demo2").disabled = false;
                                                    }
                                                    else
                                                    {
                                                        document.getElementById("demo1").disabled = true;
                                                        document.getElementById("demo2").disabled = true;
                                                    }
                                                }
                                            </script>
                                            <!--<div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">-->
<!--                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_choose_owner_cert);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                                            <select name="IsByImport" id="IsByImport" class="form-control123">
                                                                <option value="1" <= session.getAttribute("sessIsByImportTokenReport") != null && "1".equals(session.getAttribute("sessIsByImportTokenReport").toString()) ? "selected" : ""%>>
                                                                    <script>document.write(tokenreport_fm_choose_time_import);</script>
                                                                </option>
                                                                <option value="0" < session.getAttribute("sessIsByImportTokenReport") != null && "0".equals(session.getAttribute("sessIsByImportTokenReport").toString()) ? "selected" : ""%>>
                                                                    <script>document.write(tokenreport_fm_choose_time_export);</script>
                                                                </option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_FromDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo1" name="FromCreateDate"
                                                                value="<= session.getAttribute("sessFromCreateDateTokenReport") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromCreateDateTokenReport").toString()) : com.ConvertMonthSub(30)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_ToDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo2" name="ToCreateDate"
                                                                value="<= session.getAttribute("sessToCreateDateTokenReport") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToCreateDateTokenReport").toString()) : com.ConvertMonthSub(0)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(token_fm_tokenid);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="TOKEN_SN" maxlength="150" value="<= session.getAttribute("sessTOKEN_SNTokenReport") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessTOKEN_SNTokenReport").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(token_fm_version);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="TOKEN_VERSION" id="TOKEN_VERSION" class="form-control123">
                                                                <option value="<= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <= session.getAttribute("sessTOKEN_VERSIONTokenReport") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessTOKEN_VERSIONTokenReport").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <
                                                                    TOKEN_VERSION[][] rstTOKEN_VERSION = new TOKEN_VERSION[1][];
                                                                    db.S_BO_TOKEN_VERSION_COMBOBOX(sessLanguageGlobal, rstTOKEN_VERSION);
                                                                    if (rstTOKEN_VERSION[0].length > 0) {
                                                                        for (TOKEN_VERSION temp1 : rstTOKEN_VERSION[0]) {
                                                                %>
                                                                <option value="<=String.valueOf(temp1.ID)%>" <= session.getAttribute("sessTOKEN_VERSIONTokenReport") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessTOKEN_VERSIONTokenReport").toString()) ? "selected" : ""%>><=temp1.REMARK%></option>
                                                                <
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>-->
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_mounth);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="FromCreateDate" id="FromCreateDate" class="form-control123">
                                                                <option value="01" <%= session.getAttribute("sessFromCreateDateTokenReport") != null
                                                                    && session.getAttribute("sessFromCreateDateTokenReport").toString().trim().equals("01") ? "selected" : "" %>>01</option>
                                                                <option value="02" <%= session.getAttribute("sessFromCreateDateTokenReport") != null
                                                                    && session.getAttribute("sessFromCreateDateTokenReport").toString().trim().equals("02") ? "selected" : "" %>>02</option>
                                                                <option value="03" <%= session.getAttribute("sessFromCreateDateTokenReport") != null
                                                                    && session.getAttribute("sessFromCreateDateTokenReport").toString().trim().equals("03") ? "selected" : "" %>>03</option>
                                                                <option value="04" <%= session.getAttribute("sessFromCreateDateTokenReport") != null
                                                                    && session.getAttribute("sessFromCreateDateTokenReport").toString().trim().equals("04") ? "selected" : "" %>>04</option>
                                                                <option value="05" <%= session.getAttribute("sessFromCreateDateTokenReport") != null
                                                                    && session.getAttribute("sessFromCreateDateTokenReport").toString().trim().equals("05") ? "selected" : "" %>>05</option>
                                                                <option value="06" <%= session.getAttribute("sessFromCreateDateTokenReport") != null
                                                                    && session.getAttribute("sessFromCreateDateTokenReport").toString().trim().equals("06") ? "selected" : "" %>>06</option>
                                                                <option value="07" <%= session.getAttribute("sessFromCreateDateTokenReport") != null
                                                                    && session.getAttribute("sessFromCreateDateTokenReport").toString().trim().equals("07") ? "selected" : "" %>>07</option>
                                                                <option value="08" <%= session.getAttribute("sessFromCreateDateTokenReport") != null
                                                                    && session.getAttribute("sessFromCreateDateTokenReport").toString().trim().equals("08") ? "selected" : "" %>>08</option>
                                                                <option value="09" <%= session.getAttribute("sessFromCreateDateTokenReport") != null
                                                                    && session.getAttribute("sessFromCreateDateTokenReport").toString().trim().equals("09") ? "selected" : "" %>>09</option>
                                                                <option value="10" <%= session.getAttribute("sessFromCreateDateTokenReport") != null
                                                                    && session.getAttribute("sessFromCreateDateTokenReport").toString().trim().equals("10") ? "selected" : "" %>>10</option>
                                                                <option value="11" <%= session.getAttribute("sessFromCreateDateTokenReport") != null
                                                                    && session.getAttribute("sessFromCreateDateTokenReport").toString().trim().equals("11") ? "selected" : "" %>>11</option>
                                                                <option value="12" <%= session.getAttribute("sessFromCreateDateTokenReport") != null
                                                                    && session.getAttribute("sessFromCreateDateTokenReport").toString().trim().equals("12") ? "selected" : "" %>>12</option>
                                                            </select>
                                                            <%
                                                                if(session.getAttribute("sessFromCreateDateTokenReport") == null)
                                                                {
                                                            %>
                                                                <script type="text/javascript">
                                                                        $("#FromCreateDate").val('<%=CommonFunction.getMonthCurrentForSearch()%>');
                                                                </script>
                                                                <%
                                                                }
                                                            %>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_year);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="ToCreateDate" id="ToCreateDate" class="form-control123">
                                                                <%
                                                                    for(int i=18; i<68; i++) {
                                                                        String sYearAfter = String.valueOf(i);
                                                                        if(sYearAfter.length() == 1)
                                                                        {
                                                                            sYearAfter = "0" + sYearAfter;
                                                                        }
                                                                        String sYearAfterID = "20" + sYearAfter;
                                                                %>
                                                                <option value="<%=sYearAfterID%>" <%= session.getAttribute("sessToCreateDateTokenReport") != null
                                                                    && session.getAttribute("sessToCreateDateTokenReport").toString().trim().equals(sYearAfterID) ? "selected" : "" %>><%=sYearAfterID%></option>
                                                                <%
                                                                    }
                                                                %>
                                                            </select>
                                                            <%
                                                                if(session.getAttribute("sessToCreateDateTokenReport") == null)
                                                                {
                                                            %>
                                                                <script type="text/javascript">
                                                                    $("#ToCreateDate").val('<%=CommonFunction.getYearCurrentForSearch()%>');
                                                                </script>
                                                                <%
                                                                }
                                                            %>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_form);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;text-align: right;">
                                                            <select id="METHOD" name="METHOD" class="form-control123">
                                                                <option value="1" <%= session.getAttribute("sessMethodTokenReport") != null && "1".equals(session.getAttribute("sessMethodTokenReport").toString()) ? "selected" : ""%>>
                                                                    <script>document.write(global_fm_exists_form);</script>
                                                                </option>
                                                                <option value="2" <%= session.getAttribute("sessMethodTokenReport") != null && "2".equals(session.getAttribute("sessMethodTokenReport").toString()) ? "selected" : ""%>>
                                                                    <script>document.write(global_fm_Deposit_form);</script>
                                                                </option>
                                                                <option value="3" <%= session.getAttribute("sessMethodTokenReport") != null && "3".equals(session.getAttribute("sessMethodTokenReport").toString()) ? "selected" : ""%>>
                                                                    <script>document.write(global_fm_use_form);</script>
                                                                </option>
                                                                <option value="4" <%= session.getAttribute("sessMethodTokenReport") != null && "4".equals(session.getAttribute("sessMethodTokenReport").toString()) ? "selected" : ""%>>
                                                                    <script>document.write(global_fm_end_form);</script>
                                                                </option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <!--</div>-->
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_Branch);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="AGENT_ID" id="AGENT_ID" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessAGENT_IDTokenReport") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessAGENT_IDTokenReport").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    //BRANCH[][] rst = new BRANCH[1][];
                                                                    //db.S_BO_BRANCH_COMBOBOX(sessLanguageGlobal, rst);
                                                                    BRANCH[][] rst = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                                                                    if (rst[0].length > 0) {
                                                                        for (BRANCH temp1 : rst[0]) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessAGENT_IDTokenReport") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessAGENT_IDTokenReport").toString()) ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                                <%
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                        </div>
                                                    </div>
                                                </div>
                                            <!--</div>-->
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                            <input id="idHiddenLoad" name="nameHiddenLoad" value="" type="hidden"/>
                                        </form>
                                    </div>
                                </div>
                                <%
                                    if (status == 1 && statusLoad == 1) {
                                %>
                                <script type="text/javascript">
                                    $(document).ready(function () {
                                        goToByScroll("idShowResultSearch");
                                    });
                                </script>
                                <div class="x_panel" id="idShowResultSearch">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(certlist_title_table);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <script>document.write(global_label_grid_sum);</script><%= strMess%>&nbsp;&nbsp;
                                                <%
                                                    if(rsPgin[0].length > 0) {
                                                %>
                                                <button type="button" class="btn btn-info" onClick="ExportTokenReport('<%= String.valueOf(iTotRslts)%>', '<%= anticsrf%>');"><script>document.write(global_fm_button_export_csv);</script></button>
                                                <script>
                                                    function ExportTokenReport(countList, idCSRF)
                                                    {
                                                        $('body').append('<div id="over"></div>');
                                                        $(".loading-gif").show();
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../ExportCSVParam",
                                                            data: {
                                                                idParam: "exporttokenreport",
                                                                countList: countList,
                                                                CsrfToken: idCSRF
                                                            },
                                                            catche: false,
                                                            success: function (html) {
                                                                var arr = sSpace(html).split('#');
                                                                if (arr[0] === "0")
                                                                {
                                                                    var f = document.form;
                                                                    f.method = "post";
                                                                    f.action = '../DownFromSaveFile?idParam=downfileexportquick&name=' + arr[2];
                                                                    f.submit();
                                                                }
                                                                else if (arr[0] === JS_EX_CSRF) {
                                                                    funCsrfAlert();
                                                                }
                                                                else if (arr[0] === JS_EX_LOGIN) {
                                                                    RedirectPageLoginNoSess(global_alert_login);
                                                                }
                                                                else if (arr[0] === JS_EX_ANOTHERLOGIN)
                                                                {
                                                                    RedirectPageLoginNoSess(global_alert_another_login);
                                                                }
                                                                else if (arr[0] === "1") {
                                                                    funErrorAlert(global_error_export_excel);
                                                                }
                                                                else if (arr[0] === "2") {
                                                                    funErrorAlert(global_succ_NoResult);
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
                                                %>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <input type="hidden" name="iPagNo" value="<%=iPagNo%>">
                                        <input type="hidden" name="cPagNo" value="<%=cPagNo%>">
                                        <input type="hidden" name="iSwRws" value="<%=iSwRws%>">
                                        <style type="text/css">
                                            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                            .table > thead > tr > th{border-bottom: none;}
                                            .btn{margin-bottom: 0px;}
                                            .table{font-size: 12px;}
                                        </style>
                                        <div class="table-responsive">
                                            <table id="idTableList" class="table table-bordered table-striped projects">
                                                <thead>
                                                <th><script>document.write(global_fm_STT);</script></th>
                                                <th><script>document.write(token_fm_tokenid);</script></th>
                                                <th><script>document.write(token_fm_version);</script></th>
                                                <th><script>document.write(token_fm_agent);</script></th>
                                                <th><script>document.write(global_fm_Status);</script></th>
                                                <th><script>document.write(tokenreport_fm_agenct_date_export);</script></th>
                                                </thead>
                                                <tbody>
                                                    <%
                                                        if (iPaNoSS > 1) {
                                                            j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                        }
                                                        session.setAttribute("RefreshTokenReportSessNumberPaging", String.valueOf(iPaNoSS));
                                                        if (rsPgin[0].length > 0) {
                                                            for (TOKEN temp1 : rsPgin[0]) {
                                                    %>
                                                    <tr>
                                                        <td style="text-align: center;"><%= com.convertMoney(j)%></td>
                                                        <td><%= temp1.TOKEN_SN %></td>
                                                        <td><%= temp1.TOKEN_VERSION_DESC%></td>
                                                        <td><%= temp1.BRANCH_DESC%></td>
                                                        <td><%= temp1.TOKEN_STATE_DESC %></td>
                                                        <td><%= temp1.IMPORT_EXPORT_DT%></td>
                                                    <%
                                                                j++;
                                                            }
                                                        }
                                                    %>
                                                    <%
                                                        if (iTotRslts < (iPagNo + iSwRws)) {
                                                            iEnRsNo = iTotRslts;
                                                        } else {
                                                            iEnRsNo = (iPagNo + iSwRws);
                                                        }
                                                        iTotPags = ((int) (Math.ceil((double) iTotRslts / iSwRws)));
                                                    %>
                                                    <tr>
                                                        <script>
                                                            $(document).ready(function () {
                                                                document.getElementById("idTDPaging").colSpan = document.getElementById('idTableList').rows[0].cells.length;
                                                            });
                                                        </script>
                                                        <td id="idTDPaging" style="text-align: right;">
                                                            <div class="paging_table">
                                                                <%
                                                                    int i = 0;
                                                                    int cPge = 0;
                                                                    if (iTotRslts > iSwRws) {
                                                                        String uriPage = request.getRequestURI();
                                                                        String namePage = uriPage.substring(uriPage.lastIndexOf("/")+1);
                                                                        cPge = ((int) (Math.ceil((double) iEnRsNo / (iTotSrhRcrds * iSwRws))));
                                                                        int prePageNo = (cPge * iTotSrhRcrds) - ((iTotSrhRcrds - 1) + iTotSrhRcrds);
                                                                        if ((cPge * iTotSrhRcrds) - (iTotSrhRcrds) > 0) {
                                                                %>
                                                                <a href="<%=namePage%>?iPagNo=<%=prePageNo%>&cPagNo=<%=prePageNo%>"><< <script>document.write(global_paging_Before);</script></a>
                                                                &nbsp;
                                                                <%
                                                                    }
                                                                    for (i = ((cPge * iTotSrhRcrds) - (iTotSrhRcrds - 1)); i <= (cPge * iTotSrhRcrds); i++) {
                                                                        if (i == ((iPagNo / iSwRws) + 1)) {
                                                                %>
                                                                <a href="<%=namePage%>?iPagNo=<%=i%>" style="cursor:pointer;color:red;"><b><%=i%></b></a>
                                                                        <%                   } else if (i <= iTotPags) {
                                                                        %>
                                                                &nbsp;<a href="<%=namePage%>?iPagNo=<%=i%>"><%=i%></a>
                                                                <%
                                                                        }
                                                                    }

                                                                    if (iTotPags > iTotSrhRcrds && i <= iTotPags) {
                                                                %>
                                                                &nbsp;
                                                                <a href="<%=namePage%>?iPagNo=<%=i%>&cPagNo=<%=i%>">>> <script>document.write(global_paging_last);</script></a>
                                                                <%
                                                                    }
                                                                } else {
                                                                %>
                                                                &nbsp;<a style="cursor: pointer;">1</a>
                                                                <%
                                                                    }
                                                                %>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="x_panel" id="idX_Panel_Edit" style="display: none;">
                                    <div class="x_content">
                                        <div id="contentEdit"></div>
                                    </div>
                                </div>
                                <%
                                    }
                                    if (status == 1000 && statusLoad == 1) {
                                %>
                                <script type="text/javascript">
                                    $(document).ready(function () {
                                        goToByScroll("idShowResultSearch");
                                    });
                                </script>
                                <div class="x_panel" id="idShowResultSearch">
                                    <div class="x_content" style="text-align: center;">
                                        <span style="color: red; font-size: 15px;"><script>document.write(global_succ_NoResult);</script></span>
                                        <div class="clearfix"></div>
                                    </div>
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
                    </div>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
            <script src="../style/jquery.min.js"></script>
            <script src="../style/bootstrap.min.js"></script>
            <script src="../style/custom.min.js"></script>
            <link href="../js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
            <script src="../js/checkphone/intlTelInput.js" type="text/javascript"></script>
            <script src="../js/moment.min.js"></script>
            <script src="../js/daterangepicker.js"></script>
        </div>
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