<%-- 
    Document   : CollationManagement
    Created on : Dec 6, 2019, 5:27:21 PM
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
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
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <script type="text/javascript" src="../js/jquery-1.12.4.js"></script>
        <script type="text/javascript" src="../js/jquery-ui.js"></script>
        <!--<link rel="stylesheet" type="text/css" media="all" href="../style/jquery-ui.css" />-->
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <title></title>
        <script language="javascript">
            document.title = collation_title_list;
            changeFavicon("../");
            $(document).ready(function () {
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
                $('[data-toggle="tooltipPrefix"]').tooltip();
            });
            function searchForm(id)
            {
                localStorage.setItem("CountCheckExportCSR", "");
                document.getElementById("idHiddenLoad").value = JS_STR_GRID_SEARCH_RESET;
                document.getElementById("tempCsrfToken").value = id;
                var f = document.form;
                f.method = "post";
                f.action = '';
                f.submit();
            }
            function ExportCollationList(countList, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                var servletUrl = "../ExportCSVParam";
                var servletParam = "exportcollationcertlist";
                if(IsWhichCA === JS_IS_WHICH_ABOUT_CA_HILO) {
                    servletUrl = "../ExportCSVPhaseTwo";
                    servletParam = "exportcollationcertlist_20";
                }
                $.ajax({
                    type: "post",
                    url: servletUrl,
                    data: {
                        idParam: servletParam,
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
            function PrintPreview(vText) {
                var popupWin = window.open('', '_blank', 'width=1050,height=900,location=no,left=200px');
                popupWin.document.open();
                popupWin.document.write('<html><title></title><link rel="stylesheet" type="text/css" href="Print.css" media="screen"/></head><body onload="window.print()">');
                popupWin.document.write(vText);
                popupWin.document.write('</html>');
                popupWin.document.close();
            }
            function CollationChangeStatus(idCSRF)
            {
                var sCheckCheckAll = "0";
                if ($("#checkAll").is(':checked')) {
                    sCheckCheckAll = "1";
                }
                if(sCheckCheckAll === "0")
                {
                    if(localStorage.getItem("CountCheckExportCSR") === "")
                    {
                        funErrorAlert(global_succ_NoCheck);
                        return false;
                    }
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../CollationCertCommon",
                    data: {
                        idParam: "editcollationcert",
                        id: localStorage.getItem("CountCheckExportCSR"),
                        isCheckAll: sCheckCheckAll,
                        CsrfToken: idCSRF
                    },
                    catche: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            localStorage.setItem("CountCheckExportCSR", "");
                            localStorage.setItem("CountCheckAllExportCSR", "");
                            $('#idTableList input[type="checkbox"]').prop('checked', false);
                            $('#idTableList tr').css('background', '');
                            funSuccLocalAlert(inputcertlist_succ_edit);
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        }
                        else if (arr[0] === JS_EX_LOGIN) {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (arr[0] === JS_EX_ANOTHERLOGIN) {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else if (arr[0] === "2") {
                            funErrorAlert(global_succ_NoResult);
                        }
                        else if (arr[0] === JS_EX_NO_DATA) {
                            funErrorAlert(global_succ_NoResult);
                        } else {
                            funErrorAlert(global_errorsql);
                        }
                        $(".loading-gif").hide();
                        $('#over').remove();
                    }
                });
                return false;
            }
            function UpdateRoseAgent(idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../CollationCertCommon",
                    data: {
                        idParam: "updateroseagent",
                        CsrfToken: idCSRF
                    },
                    catche: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            funSuccLocalAlert(inputcertlist_succ_edit);
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        }
                        else if (arr[0] === JS_EX_LOGIN) {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (arr[0] === JS_EX_ANOTHERLOGIN) {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else if (arr[0] === "2") {
                            funErrorAlert(global_succ_NoResult);
                        }
                        else if (arr[0] === JS_EX_NO_DATA) {
                            funErrorAlert(global_succ_NoResult);
                        } else {
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
            .projects th{font-weight: bold;}
            .navbar-right{margin-right: 0;padding-right:10px;}
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
            .x_panel {
                padding:10px 17px 10px 17px;
            }
            .x_content {
                padding: 0 5px 0 5px;
            }
        </style>
    </head>
    <body class="nav-md">
        <%
            if ((session.getAttribute("UserID")) != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                String SessUserID = session.getAttribute("UserID").toString().trim();
                String SessRoleID = session.getAttribute("RoleID_ID").toString().trim();
                String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
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
                        document.getElementById("idNameURL").innerHTML = collation_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                            <%
                                int status = 1000;
                                int statusLoad = 0;
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
                                    String strAlertAllTimes = "0";
                                    CERTIFICATION[][] rsPginToken = new CERTIFICATION[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (session.getAttribute("RefreshStatusCollationSess") != null && session.getAttribute("sessMonthStatusCollation") != null
                                         && session.getAttribute("sessYearStatusCollation") != null) {
                                        statusLoad = 1;
                                        String ToCreateDate = (String) session.getAttribute("sessMonthStatusCollation");
                                        String FromCreateDate = (String) session.getAttribute("sessYearStatusCollation");
                                        String FromDate = (String) session.getAttribute("sessFromCreateDateCollation");
                                        String ToDate = (String) session.getAttribute("sessToCreateDateCollation");
                                        String STATUS_COLLATION = (String) session.getAttribute("sessStatusCollation");
                                        String idBranchOffice = (String) session.getAttribute("sessBranchOfficeStatusCollation");
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeCertCollation");
                                        session.setAttribute("RefreshStatusCollationSess", null);
                                        session.setAttribute("sessMonthStatusCollation", ToCreateDate);
                                        session.setAttribute("sessYearStatusCollation", FromCreateDate);
                                        session.setAttribute("sessFromCreateDateCollation", FromDate);
                                        session.setAttribute("sessToCreateDateCollation", ToDate);
                                        session.setAttribute("sessStatusCollation", STATUS_COLLATION);
                                        session.setAttribute("sessBranchOfficeStatusCollation", idBranchOffice);
                                        session.setAttribute("AlertAllTimeCertCollation", strAlertAllTimes);
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(STATUS_COLLATION)) {
                                            STATUS_COLLATION = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idBranchOffice)) {
                                            idBranchOffice = "";
                                        }
                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            SessUserID = "";
                                        } else {
                                            if(!SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
                                                SessUserID = "";
                                            }
                                        }
                                        if("1".equals(strAlertAllTimes)) {
                                            FromDate = "";
                                            ToDate = "";
                                        } else{
                                            ToCreateDate = "";
                                            FromCreateDate = "";
                                        }
                                        int ssToken = 0;
                                        String pBRANCH_BENEFICIARY_ID = SessUserAgentID;
                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            pBRANCH_BENEFICIARY_ID=EscapeUtils.escapeHtmlSearch(idBranchOffice);
                                        }
                                        if ((session.getAttribute("CountListStatusCollation")) == null) {
                                            ssToken = db.S_BO_CERTIFICATION_CROSS_CHECK_TOTAL(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(FromCreateDate),EscapeUtils.escapeHtmlSearch(idBranchOffice),
                                                STATUS_COLLATION, SessUserID, pBRANCH_BENEFICIARY_ID, FromDate, ToDate);
                                            session.setAttribute("CountListStatusCollation", String.valueOf(ssToken));
                                        } else {
                                            String sCount = (String) session.getAttribute("CountListStatusCollation");
                                            ssToken = Integer.parseInt(sCount);
                                            session.setAttribute("CountListStatusCollation", String.valueOf(ssToken));
                                        }
                                        if (session.getAttribute("SearchIPageNoPagingStatusCollation") != null) {
                                            String sPage = (String) session.getAttribute("SearchIPageNoPagingStatusCollation");
                                            iPagNo = Integer.parseInt(sPage);
                                        }
                                        if (session.getAttribute("SearchISwRwsPagingStatusCollation") != null) {
                                            String sSumPage = (String) session.getAttribute("SearchISwRwsPagingStatusCollation");
                                            iSwRws = Integer.parseInt(sSumPage);
                                        }
                                        if (session.getAttribute("RefreshCertSessNumberPaging") != null) {
                                            String sNoPage = (String) session.getAttribute("RefreshCertSessNumberPaging");
                                            iPaNoSS = Integer.parseInt(sNoPage);
                                        }
                                        session.setAttribute("SearchIPageNoPagingStatusCollation", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingStatusCollation", String.valueOf(iSwRws));
                                        if (ssToken > 0) {
                                            db.S_BO_CERTIFICATION_CROSS_CHECK_LIST(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(idBranchOffice),STATUS_COLLATION, SessUserID,
                                                sessLanguageGlobal, rsPginToken, iPagNo, iSwRws, pBRANCH_BENEFICIARY_ID, FromDate, ToDate);
                                        }
                                        iTotRslts = ssToken;
                                        if (ssToken > 0) {
                                            strMess = com.convertMoney(ssToken);
                                        }
                                        if (iTotRslts > 0 && rsPginToken[0].length > 0) {
                                            status = 1;
                                        } else {
                                            status = 1000;
                                        }
                                    } else if (request.getMethod().equals("POST") || "1".equals(hasPaging)) {
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("SearchIPageNoPagingStatusCollation", null);
                                            session.setAttribute("SearchISwRwsPagingStatusCollation", null);
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
                                    String FromCreateDate = request.getParameter("idYear");
                                    String ToCreateDate = request.getParameter("idMounth");
                                    String FromDate = request.getParameter("FromCreateDate");
                                    String ToDate = request.getParameter("ToCreateDate");
                                    String STATUS_COLLATION = request.getParameter("STATUS_COLLATION");
                                    String idBranchOffice = request.getParameter("idBranchOffice");
                                    Boolean nameCheck = Boolean.valueOf(request.getParameter("nameCheck") != null);
                                    if (nameCheck == false) {
                                        FromDate = "";
                                        ToDate = "";
                                        strAlertAllTimes = "1";
                                    }
                                    if ("1".equals(hasPaging)) {
                                        ToCreateDate = (String) session.getAttribute("sessMonthStatusCollation");
                                        FromCreateDate = (String) session.getAttribute("sessYearStatusCollation");
                                        FromDate = (String) session.getAttribute("sessFromCreateDateCollation");
                                        ToDate = (String) session.getAttribute("sessToCreateDateCollation");
                                        STATUS_COLLATION = (String) session.getAttribute("sessStatusCollation");
                                        idBranchOffice = (String) session.getAttribute("sessBranchOfficeStatusCollation");
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeCertCollation");
                                        session.setAttribute("SessParamOnPagingCertList", null);
                                    } else {
                                        session.setAttribute("CountListStatusCollation", null);
                                    }
                                    if("".equals(idBranchOffice) || "0".equals(idBranchOffice)){
                                        System.out.println("idBranchOffice 1: " + idBranchOffice);
                                        idBranchOffice = Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL;
                                    }
                                    session.setAttribute("sessYearStatusCollation", FromCreateDate);
                                    session.setAttribute("sessMonthStatusCollation", ToCreateDate);
                                    session.setAttribute("sessFromCreateDateCollation", FromDate);
                                    session.setAttribute("sessToCreateDateCollation", ToDate);
                                    session.setAttribute("sessStatusCollation", STATUS_COLLATION);
                                    session.setAttribute("sessBranchOfficeStatusCollation", idBranchOffice);
                                    session.setAttribute("AlertAllTimeCertCollation", strAlertAllTimes);
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idBranchOffice)) {
                                        idBranchOffice = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(STATUS_COLLATION)) {
                                        STATUS_COLLATION = "";
                                    }
                                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                        SessUserID = "";
                                    } else {
                                        if(!SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
                                            SessUserID = "";
                                        }
                                    }
System.out.println("idBranchOffice 2: " + idBranchOffice);
                                    if("1".equals(strAlertAllTimes)) {
                                        FromDate = "";
                                        ToDate = "";
                                    } else{
                                        ToCreateDate = "";
                                        FromCreateDate = "";
                                    }
                                    String pBRANCH_BENEFICIARY_ID = SessUserAgentID;
                                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                        pBRANCH_BENEFICIARY_ID=EscapeUtils.escapeHtmlSearch(idBranchOffice);
                                    }
                                    int ssToken = 0;
                                    if ((session.getAttribute("CountListStatusCollation")) == null) {
                                        ssToken = db.S_BO_CERTIFICATION_CROSS_CHECK_TOTAL(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(FromCreateDate),EscapeUtils.escapeHtmlSearch(idBranchOffice),
                                            STATUS_COLLATION, SessUserID, pBRANCH_BENEFICIARY_ID, FromDate, ToDate);
                                        session.setAttribute("CountListStatusCollation", String.valueOf(ssToken));
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListStatusCollation");
                                        ssToken = Integer.parseInt(sCount);
                                        session.setAttribute("CountListStatusCollation", String.valueOf(ssToken));
                                    }
                                    iTotRslts = ssToken;
                                    if (iTotRslts > 0) {
                                        db.S_BO_CERTIFICATION_CROSS_CHECK_LIST(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(idBranchOffice),STATUS_COLLATION, SessUserID,
                                            sessLanguageGlobal, rsPginToken, iPagNo, iSwRws, pBRANCH_BENEFICIARY_ID, FromDate, ToDate);
                                        session.setAttribute("SearchIPageNoPagingStatusCollation", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingStatusCollation", String.valueOf(iSwRws));
                                        strMess = com.convertMoney(ssToken);
                                        if (rsPginToken[0].length > 0) {
                                            status = 1;
                                        } else {
                                            status = 1000;
                                        }
                                    } else {
                                        status = 1000;
                                    }
                                } else {
                                    session.setAttribute("SearchIPageNoPagingStatusCollation", null);
                                    session.setAttribute("SearchISwRwsPagingStatusCollation", null);
                                    session.setAttribute("sessYearStatusCollation", null);
                                    session.setAttribute("sessMonthStatusCollation", null);
                                    session.setAttribute("sessStatusCollation", null);
                                    session.setAttribute("sessFromCreateDateCollation", null);
                                    session.setAttribute("sessToCreateDateCollation", null);
                                    session.setAttribute("sessBranchOfficeStatusCollation", null);
                                    session.setAttribute("AlertAllTimeCertCollation", null);
                                }
                                %>
                                <div class="x_panel">
                                    <div class="x_content" style="margin-top: 0px;">
                                        <script>
                                            function checkboxChange() {
                                                if ($("#idCheck").is(':checked')) {
                                                    document.getElementById("demo1").disabled = false;
                                                    document.getElementById("demo2").disabled = false;
                                                    document.getElementById("idMounth").disabled = true;
                                                    document.getElementById("idYear").disabled = true;
                                                }
                                                else
                                                {
                                                    document.getElementById("demo1").disabled = true;
                                                    document.getElementById("demo2").disabled = true;
                                                    document.getElementById("idMounth").disabled = false;
                                                    document.getElementById("idYear").disabled = false;
                                                }
                                            }
                                        </script>
                                        <form name="form" method="post" class="form-horizontal">
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_FromDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo1" name="FromCreateDate" <%= session.getAttribute("AlertAllTimeCertCollation") != null && "1".equals(session.getAttribute("AlertAllTimeCertCollation").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessFromCreateDateCollation") != null && !"1".equals(session.getAttribute("AlertAllTimeCertCollation").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromCreateDateCollation").toString()) : com.ConvertMonthSub(30)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_ToDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo2" name="ToCreateDate" <%= session.getAttribute("AlertAllTimeCertCollation") != null && "1".equals(session.getAttribute("AlertAllTimeCertCollation").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessToCreateDateCollation") != null && !"1".equals(session.getAttribute("AlertAllTimeCertCollation").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToCreateDateCollation").toString()) : com.ConvertMonthSub(0)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_check_date);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;padding-top: 7px; text-align: left;">
                                                            <label class="switch" for="idCheck">
                                                                <input type="checkbox" name="nameCheck" id="idCheck" onchange="checkboxChange();" <%= session.getAttribute("AlertAllTimeCertCollation") != null && "1".equals(session.getAttribute("AlertAllTimeCertCollation").toString()) ? "" : "checked" %>/>
                                                                <div class="slider round"></div>
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_mounth);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="idMounth" id="idMounth" class="form-control123" <%= session.getAttribute("AlertAllTimeCertCollation") != null && !"1".equals(session.getAttribute("AlertAllTimeCertCollation").toString()) ? "disabled" : "1".equals(strAlertAllTimes) ? "" : "disabled" %>>
                                                                <option value="01" <%= session.getAttribute("sessMonthStatusCollation") != null
                                                                    && session.getAttribute("sessMonthStatusCollation").toString().trim().equals("01") ? "selected" : "" %>>01</option>
                                                                <option value="02" <%= session.getAttribute("sessMonthStatusCollation") != null
                                                                    && session.getAttribute("sessMonthStatusCollation").toString().trim().equals("02") ? "selected" : "" %>>02</option>
                                                                <option value="03" <%= session.getAttribute("sessMonthStatusCollation") != null
                                                                    && session.getAttribute("sessMonthStatusCollation").toString().trim().equals("03") ? "selected" : "" %>>03</option>
                                                                <option value="04" <%= session.getAttribute("sessMonthStatusCollation") != null
                                                                    && session.getAttribute("sessMonthStatusCollation").toString().trim().equals("04") ? "selected" : "" %>>04</option>
                                                                <option value="05" <%= session.getAttribute("sessMonthStatusCollation") != null
                                                                    && session.getAttribute("sessMonthStatusCollation").toString().trim().equals("05") ? "selected" : "" %>>05</option>
                                                                <option value="06" <%= session.getAttribute("sessMonthStatusCollation") != null
                                                                    && session.getAttribute("sessMonthStatusCollation").toString().trim().equals("06") ? "selected" : "" %>>06</option>
                                                                <option value="07" <%= session.getAttribute("sessMonthStatusCollation") != null
                                                                    && session.getAttribute("sessMonthStatusCollation").toString().trim().equals("07") ? "selected" : "" %>>07</option>
                                                                <option value="08" <%= session.getAttribute("sessMonthStatusCollation") != null
                                                                    && session.getAttribute("sessMonthStatusCollation").toString().trim().equals("08") ? "selected" : "" %>>08</option>
                                                                <option value="09" <%= session.getAttribute("sessMonthStatusCollation") != null
                                                                    && session.getAttribute("sessMonthStatusCollation").toString().trim().equals("09") ? "selected" : "" %>>09</option>
                                                                <option value="10" <%= session.getAttribute("sessMonthStatusCollation") != null
                                                                    && session.getAttribute("sessMonthStatusCollation").toString().trim().equals("10") ? "selected" : "" %>>10</option>
                                                                <option value="11" <%= session.getAttribute("sessMonthStatusCollation") != null
                                                                    && session.getAttribute("sessMonthStatusCollation").toString().trim().equals("11") ? "selected" : "" %>>11</option>
                                                                <option value="12" <%= session.getAttribute("sessMonthStatusCollation") != null
                                                                    && session.getAttribute("sessMonthStatusCollation").toString().trim().equals("12") ? "selected" : "" %>>12</option>
                                                            </select>
                                                            <%
                                                                if(session.getAttribute("sessMonthStatusCollation") == null)
                                                                {
                                                            %>
                                                            <script type="text/javascript">
                                                                    $("#idMounth").val('<%=CommonFunction.getMonthCurrentForSearch()%>');
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
                                                            <select name="idYear" id="idYear" class="form-control123" <%= session.getAttribute("AlertAllTimeCertCollation") != null && !"1".equals(session.getAttribute("AlertAllTimeCertCollation").toString()) ? "disabled" : "1".equals(strAlertAllTimes) ? "" : "disabled" %>>
                                                                <%
                                                                    for(int i=18; i<68; i++) {
                                                                        String sYearAfter = String.valueOf(i);
                                                                        if(sYearAfter.length() == 1)
                                                                        {
                                                                            sYearAfter = "0" + sYearAfter;
                                                                        }
                                                                        String sYearAfterID = "20" + sYearAfter;
                                                                %>
                                                                <option value="<%=sYearAfterID%>" <%= session.getAttribute("sessYearStatusCollation") != null
                                                                    && session.getAttribute("sessYearStatusCollation").toString().trim().equals(sYearAfterID) ? "selected" : "" %>><%=sYearAfterID%></option>
                                                                <%
                                                                    }
                                                                %>
                                                            </select>
                                                            <%
                                                                if(session.getAttribute("sessYearStatusCollation") == null)
                                                                {
                                                            %>
                                                                <script type="text/javascript">
                                                                    $("#idYear").val('<%=CommonFunction.getYearCurrentForSearch()%>');
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
                                                            <script>document.write(global_fm_Status);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;text-align: right;">
                                                            <select id="STATUS_COLLATION" name="STATUS_COLLATION" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessStatusCollation") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessStatusCollation").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <option value="0" <%= session.getAttribute("sessStatusCollation") != null && "0".equals(session.getAttribute("sessStatusCollation").toString()) ? "selected" : ""%>><script>document.write(collation_fm_uncollated);</script></option>
                                                                <option value="1" <%= session.getAttribute("sessStatusCollation") != null && "1".equals(session.getAttribute("sessStatusCollation").toString()) ? "selected" : ""%>><script>document.write(collation_fm_collated);</script></option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
<!--                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_Branch);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="idBranchOffice" id="idBranchOffice" class="form-control123">
                                                                <
                                                                    if(SessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT) && SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)) {
                                                                %>
                                                                <option value="<= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <= session.getAttribute("sessBranchOfficeStatusCollation") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessBranchOfficeStatusCollation").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <
                                                                    }
                                                                %>
                                                                <
                                                                    BRANCH[][] rsBranch = (BRANCH[][]) session.getAttribute("sessTreeBranchSystemAgency");
                                                                    if (rsBranch[0].length > 0) {
                                                                        for (BRANCH temp1 : rsBranch[0]) {
                                                                            if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                %>
                                                                <option value="<=String.valueOf(temp1.ID)%>" <= session.getAttribute("sessBranchOfficeStatusCollation") != null
                                                                    && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeStatusCollation").toString().trim())
                                                                    ? "selected" : ""%>><=temp1.NAME + " - " + temp1.REMARK%></option>
                                                                <
                                                                            }
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
                                                            <script>document.write(global_fm_Branch);</script>
                                                        </label>
                                                        <%
                                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)
                                                                || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_MID)) {
                                                        %>
                                                        <div class="ui-widget">
                                                            <div class="col-sm-7">
                                                                <select name="idBranchOffice" id="idBranchOffice">
                                                                    <%
                                                                        if(SessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT) && SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)) {
                                                                            BRANCH[][] rsBranchFrist = (BRANCH[][]) session.getAttribute("sessTreeBranchSystemAgency");
                                                                            BRANCH[][] rsBranch = CommonFunction.cloneBranchAddAllOption(rsBranchFrist, "1");
                                                                            if (rsBranch[0].length > 0) {
                                                                                for (BRANCH temp1 : rsBranch[0]) {
                                                                                    if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                    %>
                                                                        <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessBranchOfficeStatusCollation") != null
                                                                            && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeStatusCollation").toString().trim())
                                                                            ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                                    <%
                                                                                    }
                                                                                }
                                                                            }
                                                                        } else {
                                                                            BRANCH[][] rsBranch = (BRANCH[][]) session.getAttribute("sessTreeBranchSystemAgency");
                                                                            if (rsBranch[0].length > 0) {
                                                                                for (BRANCH temp1 : rsBranch[0]) {
                                                                                    if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                    %>
                                                                    <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessBranchOfficeStatusCollation") != null
                                                                        && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeStatusCollation").toString().trim())
                                                                        ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                                    <%
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    %>
                                                                </select>
                                                            </div>
                                                            <script>
                                                                $(function () {
                                                                    $.widget("custom.combobox", {
                                                                        _create: function () {
                                                                            this.wrapper = $("<span>")
                                                                                    .addClass("custom-combobox")
                                                                                    .insertAfter(this.element);
                                                                            this.element.hide();
                                                                            this._createAutocomplete();
                                                                            this._createShowAllButton();
                                                                        },
                                                                        _createAutocomplete: function () {
                                                                            var selected = this.element.children(":selected"),
                                                                                    value = selected.val() ? selected.text() : "";
                                                                            this.input = $("<input>")
                                                                                    .appendTo(this.wrapper)
                                                                                    .val(value)
                                                                                    .attr("title", "")
                                                                                    .addClass("custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left")
                                                                                    .autocomplete({
                                                                                        delay: 0,
                                                                                        minLength: 0,
                                                                                        source: $.proxy(this, "_source")
                                                                                    })
                                                                                    .tooltip({
                                                                                        classes: {
                                                                                            "ui-tooltip": "ui-state-highlight"
                                                                                        }
                                                                                    });
                                                                            this._on(this.input, {
                                                                                autocompleteselect: function (event, ui) {
                                                                                    ui.item.option.selected = true;
                                                                                    this._trigger("select", event, {
                                                                                        item: ui.item.option
                                                                                    });
                                                                                },

                                                                                autocompletechange: "_removeIfInvalid"
                                                                            });
                                                                        },
                                                                        _createShowAllButton: function () {
                                                                            var input = this.input,
                                                                                    wasOpen = false
                                                                            $("<a>")
                                                                                    .attr("tabIndex", -1)
                                                                                    .attr("title", "Show All Items")
                                                                                    .attr("height", "")
                                                                                    .tooltip()
                                                                                    .appendTo(this.wrapper)
                                                                                    .button({
                                                                                        icons: {
                                                                                            primary: "ui-icon-triangle-1-s"
                                                                                        },
                                                                                        text: "false"
                                                                                    })
                                                                                    .removeClass("ui-corner-all")
                                                                                    .addClass("custom-combobox-toggle ui-corner-right")
                                                                                    .on("mousedown", function () {
                                                                                        wasOpen = input.autocomplete("widget").is(":visible");
                                                                                    })
                                                                                    .on("click", function () {
                                                                                        input.trigger("focus");
                                                                                        // Close if already visible
                                                                                        if (wasOpen) {
                                                                                            return;
                                                                                        }
                                                                                        // Pass empty string as value to search for, displaying all results
                                                                                        input.autocomplete("search", "");
                                                                                    });
                                                                        },
                                                                        _source: function (request, response) {
                                                                            var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term), "i");
                                                                            response(this.element.children("option").map(function () {
                                                                                var text = $(this).text();
                                                                                if (this.value && (!request.term || matcher.test(text)))
                                                                                    return {
                                                                                        label: text,
                                                                                        value: text,
                                                                                        option: this
                                                                                    };
                                                                            }));
                                                                        },
                                                                        _removeIfInvalid: function (event, ui) {
                                                                            // Selected an item, nothing to do
                                                                            if (ui.item) {
                                                                                return;
                                                                            }
                                                                            // Search for a match (case-insensitive)
                                                                            var value = this.input.val(),
                                                                                    valueLowerCase = value.toLowerCase(),
                                                                                    valid = false;
                                                                            this.element.children("option").each(function () {
                                                                                if ($(this).text().toLowerCase() === valueLowerCase) {
                                                                                    this.selected = valid = true;
                                                                                    return false;
                                                                                }
                                                                            });
                                                                            // Found a match, nothing to do
                                                                            if (valid) {
                                                                                return;
                                                                            }
                                                                            // Remove invalid value
                                                                            this.input
                                                                                    .val("All - Tất cả")
                                                                                    .attr("title", "")
                                                                                    .tooltip("open");
                                                                            this.element.val("");
                                                                            this._delay(function () {
                                                                                this.input.tooltip("close").attr("title", "");
                                                                            }, 2500);
                                                                            this.input.autocomplete("instance").term = "";
                                                                        },
                                                                        _destroy: function () {
                                                                            this.wrapper.remove();
                                                                            this.element.show();
                                                                        }
                                                                    });
                                                                    $("#idBranchOffice").combobox();
                                                                    $("#toggle").on("click", function () {
                                                                        $("#idBranchOffice").toggle();
                                                                    });
                                                                });
//                                                                $(function () {
//                                                                    $.widget("custom.combobox", {
//                                                                        _create: function () {
//                                                                            this.wrapper = $("<span>")
//                                                                                    .addClass("custom-combobox")
//                                                                                    .insertAfter(this.element);
//                                                                            this.element.hide();
//                                                                            this._createAutocomplete();
//                                                                            this._createShowAllButton();
//                                                                        },
//                                                                        _createAutocomplete: function () {
//                                                                            var selected = this.element.children(":selected"),
//                                                                                    value = selected.val() ? selected.text() : "";
//                                                                            this.input = $("<input>")
//                                                                                    .appendTo(this.wrapper)
//                                                                                    .val(value)
//                                                                                    .attr("title", "")
//                                                                                    .attr("placeholder", "")
//                                                                                    .addClass("custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left")
//                                                                                    .autocomplete({
//                                                                                        delay: 0,
//                                                                                        minLength: 0,
//                                                                                        source: $.proxy(this, "_source")
//                                                                                    })
//        //                                                                                .tooltip({
//        //                                                                                    classes: {
//        //                                                                                        "ui-tooltip": "ui-state-highlight"
//        //                                                                                    }
//        //                                                                                })
//                                                                                    ;
//                                                                            this._on(this.input, {
//                                                                                autocompleteselect: function (event, ui) {
//                                                                                    ui.item.option.selected = true;
//                                                                                    this._trigger("select", event, {
//                                                                                        item: ui.item.option
//                                                                                    });
//                                                                                },
//                                                                                autocompletechange: "_removeIfInvalid"
//                                                                            });
//                                                                        },
//                                                                        _createShowAllButton: function () {
//                                                                            var input = this.input,
//                                                                                    wasOpen = false;
//                                                                            $("<a>")
//                                                                                    .attr("tabIndex", -1)
//                                                                                    .attr("title", "Show All Items")
//                                                                                    .tooltip()
//                                                                                    .appendTo(this.wrapper)
//                                                                                    .button({
//                                                                                        icons: {
//                                                                                            primary: "ui-icon-triangle-1-s"
//                                                                                        },
//                                                                                        text: false
//                                                                                    })
//                                                                                    .removeClass("ui-corner-all")
//                                                                                    .addClass("custom-combobox-toggle ui-corner-right")
//                                                                                    .on("mousedown", function () {
//                                                                                        wasOpen = input.autocomplete("widget").is(":visible");
//                                                                                    })
//                                                                                    .on("click", function () {
//                                                                                        input.trigger("focus");
//                                                                                        // Close if already visible
//                                                                                        if (wasOpen) {
//                                                                                            return;
//                                                                                        }
//                                                                                        // Pass empty string as value to search for, displaying all results
//                                                                                        input.autocomplete("search", "");
//                                                                                    });
//                                                                        },
//                                                                        _source: function (request, response) {
//                                                                            var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term), "i");
//                                                                            response(this.element.children("option").map(function () {
//                                                                                var text = $(this).text();
//                                                                                if (this.value && (!request.term || matcher.test(text)))
//                                                                                    return {
//                                                                                        label: text,
//                                                                                        value: text,
//                                                                                        option: this
//                                                                                    };
//                                                                            }));
//                                                                        },
//                                                                        _removeIfInvalid: function (event, ui) {
//                                                                            // Selected an item, nothing to do
//                                                                            if (ui.item) {
//                                                                                return;
//                                                                            }
//                                                                            // Search for a match (case-insensitive)
//                                                                            var value = this.input.val(),
//                                                                                    valueLowerCase = value.toLowerCase(),
//                                                                                    valid = false;
//                                                                            this.element.children("option").each(function () {
//                                                                                if ($(this).text().toLowerCase() === valueLowerCase) {
//                                                                                    this.selected = valid = true;
//                                                                                    return false;
//                                                                                }
//                                                                            });
//                                                                            // Found a match, nothing to do
//                                                                            if (valid) {
//                                                                                return;
//                                                                            }
//                                                                            // Remove invalid value
//                                                                            this.input
//                                                                                    .val("All - Tất cả")
//                                                                                    .attr("title", value + " didn't match any item")
//                                                                                    .tooltip("open");
//                                                                            this.element.val("");
//                                                                            this._delay(function () {
//                                                                                this.input.tooltip("close").attr("title", "");
//                                                                            }, 2500);
//                                                                            this.input.autocomplete("instance").term = "";
//                                                                        },
//                                                                        _destroy: function () {
//                                                                            this.wrapper.remove();
//                                                                            this.element.show();
//                                                                        }
//                                                                    });
//                                                                    $("#idBranchOffice").combobox();
//                                                                });
                                                            </script>
                                                            <style>
                                                            .custom-combobox {
                                                                position: relative;
                                                                display: inline-block;
                                                            }
                                                            .custom-combobox-toggle {
                                                                position: absolute;
                                                                top: 0;
                                                                bottom: 0;
                                                                margin-left: -1px;
                                                                padding: 0;
                                                            }
                                                            .custom-combobox-input {
                                                                margin: 0;
                                                                padding: 5px 10px;
                                                            }
                                                            .ui-state-default, .ui-widget-content .ui-state-default, .ui-widget-header .ui-state-default, .ui-button, html .ui-button.ui-state-disabled:hover, html .ui-button.ui-state-disabled:active{
                                                                background:none;
                                                            }
                                                        </style>
                                                        </div>
                                                        <%
                                                            } else {
                                                        %>
                                                        <div class="col-sm-7">
                                                            <select name="idBranchOffice" id="idBranchOffice" class="form-control123">
                                                                <%
                                                                    if(SessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT) && SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)) {
                                                                %>
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessBranchOfficeStatusCollation") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessBranchOfficeStatusCollation").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    }
                                                                %>
                                                                <%
                                                                    BRANCH[][] rsBranch = (BRANCH[][]) session.getAttribute("sessTreeBranchSystemAgency");
                                                                    if (rsBranch[0].length > 0) {
                                                                        for (BRANCH temp1 : rsBranch[0]) {
                                                                            if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT))
                                                                            {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessBranchOfficeStatusCollation") != null
                                                                    && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeStatusCollation").toString().trim())
                                                                    ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                                <%
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                        <% } %>
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
                                            </div>
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                            <input id="idHiddenLoad" name="nameHiddenLoad" value="" type="hidden"/>
                                        </form>
                                    </div>
                                </div>
                                <%
                                    if (status == 1 && statusLoad == 1) {
                                %>
                                <div class="x_panel" style="margin-top: 0px;">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2>
                                            <i class="fa fa-list-ul"></i> <script>document.write(reportneac_title_table);</script>
                                        </h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <script>document.write(global_label_grid_sum);</script><%= strMess%> &nbsp;&nbsp;
                                                <%
                                                    if(rsPginToken[0].length > 0) {
                                                        if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                                %>
                                                <button type="button" class="btn btn-info" onClick="CollationChangeStatus('<%= anticsrf%>');"><script>document.write(collation_button_change);</script></button>
                                                &nbsp;&nbsp;
                                                <button type="button" class="btn btn-info" onClick="UpdateRoseAgent('<%= anticsrf%>');"><script>document.write(collation_button_rose_agent);</script></button>
                                                &nbsp;&nbsp;
                                                <%
                                                    }
                                                %>
                                                <button type="button" class="btn btn-info" onClick="ExportCollationList('<%= iTotRslts%>', '<%= anticsrf%>');"><script>document.write(global_fm_button_export_csv);</script></button>
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
                                        <%
                                            if(rsPginToken[0].length > 0) {
                                        %>
                                        <div class="table-responsive">
                                            <table id="idTableList" class="table table-bordered table-striped projects">
                                                <thead>
                                                <%
                                                    if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                                %>
                                                <th style="text-align: center;">
                                                    <script>document.write(collation_fm_change);</script><br/>
                                                    <input id="checkAll" name="checkAll" onclick="checkAllTable();" type="checkbox"/>
                                                </th>
                                                <%
                                                    }
                                                %>
                                                <th><script>document.write(global_fm_STT);</script></th>
                                                <th><script>document.write(global_fm_date_approve);</script></th>
                                                <th><script>document.write(branch_fm_code);</script></th>
                                                <th><script>document.write(branch_fm_name);</script></th>
                                                <th><script>document.write(global_fm_user_create);</script></th>
                                                <th><script>document.write(global_fm_enterprise_id);</script></th>
                                                <th><script>document.write(global_fm_grid_company);</script></th>
                                                <th><script>document.write(global_fm_personal_id);</script></th>
                                                <th><script>document.write(global_fm_grid_personal);</script></th>
                                                <th><script>document.write(global_fm_duration_cts);</script></th>
                                                <th><script>document.write(cert_fm_type_request);</script></th>
                                                <th><script>document.write(global_fm_Status);</script></th>
                                                <th><script>document.write(collation_fm_mounth);</script></th>
                                                <th><script>document.write(collation_fm_change_change);</script></th>
                                                </thead>
                                                <tbody>
                                                    <%
                                                        int j = 1;
                                                        if (iPaNoSS > 1) {
                                                            j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                        }
                                                        if (rsPginToken[0].length > 0) {
                                                            for (CERTIFICATION temp1 : rsPginToken[0]) {
                                                                String strID = String.valueOf(temp1.ID);
                                                                String sCROSS_CHECK_ENABLED = temp1.CROSS_CHECK_ENABLED ? "1" : "0";
                                                    %>
                                                    <tr>
                                                        <%
                                                            if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                                        %>
                                                        <td style="text-align: center;">
                                                            <%
                                                                if("0".equals(sCROSS_CHECK_ENABLED)) {
                                                            %>
                                                            <input id="checkChilren<%= Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX + strID%>" value="value-<%= strID%>" name="checkChilren" class='uncheck' type="checkbox"/>
                                                            <td style="display: none;"><%= Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX + strID%></td>
                                                            <%
                                                                }
                                                            %>
                                                        </td>
                                                        <%
                                                            }
                                                        %>
                                                        <td style="text-align: center;"><%= com.convertMoney(j)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.ISSUED_DT)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.BRANCH_NAME)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.BRANCH_DESC)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_BY)%></td>
                                                        <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= temp1.ENTERPRISE_ID_REMARK%>"><%= temp1.ENTERPRISE_ID%></a></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.COMPANY_NAME)%></td>
                                                        <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= temp1.PERSONAL_ID_REMARK%>"><%= temp1.PERSONAL_ID%></a></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PROFILE_NAME)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_ATTR_TYPE_DESC)%></td>
                                                        <td>
                                                            <script>
                                                                var vCROSS_CHECK_ENABLED = '<%=sCROSS_CHECK_ENABLED%>';
                                                                if(vCROSS_CHECK_ENABLED === '1') {
                                                                    document.write(collation_fm_collated);
                                                                } else {
                                                                    document.write(collation_fm_uncollated);
                                                                }
                                                            </script>
                                                        </td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CROSS_CHECKED_MOUNTH)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CROSS_CHECKED_DT)%></td>
                                                    </tr>
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
                                                    <tr id="idPagingTabel">
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
                                                                        cPge = ((int) (Math.ceil((double) iEnRsNo / (iTotSrhRcrds * iSwRws))));
                                                                        int prePageNo = (cPge * iTotSrhRcrds) - ((iTotSrhRcrds - 1) + iTotSrhRcrds);
                                                                        if ((cPge * iTotSrhRcrds) - (iTotSrhRcrds) > 0) {
                                                                %>
                                                                <a href="?iPagNo=<%=prePageNo%>&cPagNo=<%=prePageNo%>"><< <script>document.write(global_paging_Before);</script></a>
                                                                &nbsp;
                                                                <%
                                                                    }
                                                                    for (i = ((cPge * iTotSrhRcrds) - (iTotSrhRcrds - 1)); i <= (cPge * iTotSrhRcrds); i++) {
                                                                        if (i == ((iPagNo / iSwRws) + 1)) {
                                                                %>
                                                                <a href="?iPagNo=<%=i%>" style="cursor:pointer;color:red;"><b><%=i%></b></a>
                                                                        <%
                                                                        } else if (i <= iTotPags) {
                                                                        %>
                                                                &nbsp;<a href="?iPagNo=<%=i%>"><%=i%></a>
                                                                <%
                                                                        }
                                                                    }
                                                                    if (iTotPags > iTotSrhRcrds && i <= iTotPags) {
                                                                %>
                                                                &nbsp;
                                                                <a href="?iPagNo=<%=i%>&cPagNo=<%=i%>">>> <script>document.write(global_paging_last);</script></a>
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
                                            <script>
                                                function checkAllTable()
                                                {
                                                    $('#checkAll').change(function () {
                                                        $('tbody tr td input[type="checkbox"]').prop('checked', $(this).prop('checked'));
                                                        $('tbody tr').css('background', '#d8d8d8');// .prop('checked', $(this).prop('checked'));
                                                        $("#idPagingTabel").css('background', '');
                                                        if ($("#checkAll").is(':checked')) {
                                                            CheckAllTableValue();
                                                            localStorage.setItem("CountCheckAllExportCSR", "1");
                                                        }
                                                        else
                                                        {
                                                            localStorage.setItem("CountCheckExportCSR", "");
                                                            localStorage.setItem("CountCheckAllExportCSR", "");
                                                            $('tbody tr').css('background', '');
                                                        }
                                                    });
                                                }
                                                function ForCheckTable(sValueCheck)
                                                {
                                                    $('#idTableList').find('tr').each(function () {
                                                        var sCheck = sSpace($(this).find('td:eq(1)').text());
                                                        if(sCheck !== "")
                                                        {
                                                            if (sValueCheck.indexOf(sCheck + ",") !== -1)
                                                            {
                                                                $(this).closest('tr').css('background', '#d8d8d8');
                                                                $('#checkChilren' + sCheck).prop('checked', true);
                                                            }
                                                        }
                                                    });
                                                }
                                                function CheckAllTableValue()
                                                {
                                                    $("#idPagingTabel").css('background', '');
                                                    var currentCheck = localStorage.getItem("CountCheckExportCSR");
                                                    $('#idTableList').find('tr').each(function () {
                                                        var sCheck = $(this).find('td:eq(1)').text();
                                                        if(sCheck !== "" && currentCheck.indexOf(sCheck + ',') === -1)
                                                        {
                                                            currentCheck = currentCheck + sCheck + ",";
                                                        }
                                                    });
                                                    localStorage.setItem("CountCheckExportCSR", currentCheck);
                                                }
                                                $(document).ready(function () {
                                                    if(localStorage.getItem("CountCheckAllExportCSR") === "1")
                                                    {
                                                        $('tbody tr td input[type="checkbox"]').prop('checked', true);
                                                        $('tbody tr').css('background', '#d8d8d8');
                                                        localStorage.setItem("CountCheckAllExportCSR", "1");
                                                        $('#checkAll').prop('checked', true);
                                                        CheckAllTableValue();
                                                    }
                                                    else
                                                    {
                                                        $('tbody tr').css('background', '');
                                                        localStorage.setItem("CountCheckAllExportCSR", "");
                                                        $('#checkAll').prop('checked', false);
                                                    }
                                                    if(localStorage.getItem("CountCheckExportCSR") !== "")
                                                    {
                                                        ForCheckTable(localStorage.getItem("CountCheckExportCSR"));
                                                    }
                                                    $('tr').click(function () {
                                                        if (this.style.background === "" || this.style.background === "white") {
                                                            $(this).css('background', '');
                                                        }
                                                        else {
                                                            $(this).css('background', 'white');
                                                        }
                                                    });
                                                    $(".uncheck").change(function () {
                                                        var sValueCheck = localStorage.getItem("CountCheckExportCSR");
                                                        var ischecked = $(this).is(':checked');
                                                        if (!ischecked) {
                                                            $(this).closest('tr').css('background', '');
                                                            var vID = $(this).closest('tr').find("td:eq(1)").text() + ",";
//                                                                console.log("ID uncheck: " + vID);
                                                            sValueCheck = sValueCheck.replace(vID, "");
                                                            $('#checkAll').prop('checked', false);
                                                            localStorage.setItem("CountCheckAllExportCSR", "");
                                                        }
                                                        else
                                                        {
                                                            $(this).closest('tr').css('background', '#d8d8d8');
                                                            var vID = $(this).closest('tr').find("td:eq(1)").text() + ",";
                                                            if(sValueCheck.indexOf(vID) === -1)
                                                            {
                                                                sValueCheck = sValueCheck + vID;
                                                            }
//                                                                console.log("ID check: " + vID);
                                                        }
                                                        localStorage.setItem("CountCheckExportCSR", sValueCheck);
                                                        console.log(sValueCheck);
                                                    });
                                                });
                                                function stopRKey(evt) {
                                                    var evt = (evt) ? evt : ((event) ? event : null);
                                                    var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
                                                    if ((evt.keyCode === 13) && (node.type === "text")) {
                                                        return false;
                                                    }
                                                }
                                                document.onkeypress = stopRKey;
                                            </script>
                                        </div>
                                        <%
                                            } else {
                                        %>
                                        <div class="x_content" style="text-align: center;">
                                            <span style="color: red; font-size: 15px;"><script>document.write(global_succ_NoResult);</script></span>
                                            <div class="clearfix"></div>
                                        </div>
                                        <%
                                            }
                                        %>
                                    </div>
                                    <!--end new-->
                                </div>
                                <%
                                    } if (status == 1000 && statusLoad == 1) {
                                %>
                                <script type="text/javascript">
                                    $(document).ready(function () {
                                        //goToByScroll("idShowResultSearch");
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
                                %>
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
                            </div>
                        </div>
                    </div>
                    <script>
                        $(function () {
                            $('#mytable').find('tr').each(function () {
                                var findChildren = function (tr) {
                                    var depth = tr.data('depth');
                                    return tr.nextUntil($('tr').filter(function () {
                                        return $(this).data('depth') <= depth;
                                    }));
                                };
                                var el = $(this);
                                var tr = el.closest('tr');
                                var children = findChildren(tr);
//                                var subnodes = children.filter('.expand_abc');
//                                subnodes.each(function () {
//                                    var subnode = $(this);
//                                    var subnodeChildren = findChildren(subnode);
//                                    children = children.not(subnodeChildren);
//                                });
                                if (tr.hasClass('collapse_abc')) {
                                    tr.removeClass('collapse_abc').addClass('expand_abc');
                                    children.hide();
                                }
                            });
                            $('#mytable').on('click', '.toggle_abc', function () {
                                //Gets all <tr>'s  of greater depth
                                //below element in the table
                                var findChildren = function (tr) {
                                    var depth = tr.data('depth');
                                    return tr.nextUntil($('tr').filter(function () {
                                        return $(this).data('depth') <= depth;
                                    }));
                                };
                                var el = $(this);
                                var tr = el.closest('tr'); //Get <tr> parent of toggle button
                                var children = findChildren(tr);
                                //Remove already collapsed nodes from children so that we don't
                                //make them visible. 
                                //(Confused? Remove this code and close Item 2, close Item 1 
                                //then open Item 1 again, then you will understand)
                                var subnodes = children.filter('.expand_abc');
                                subnodes.each(function () {
                                    var subnode = $(this);
                                    var subnodeChildren = findChildren(subnode);
                                    children = children.not(subnodeChildren);
                                });
                                //Change icon and hide/show children
                                if (tr.hasClass('expand_abc')) {
                                    tr.removeClass('expand_abc').addClass('collapse_abc');
                                    children.show();
                                } else {
                                    tr.removeClass('collapse_abc').addClass('expand_abc');
                                    children.hide();
                                }
                                return children;
                            });
                        });
                    </script>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
            <script src="../style/bootstrap.min.js"></script>
            <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
            <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
            <script src="../style/custom.min.js"></script>
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