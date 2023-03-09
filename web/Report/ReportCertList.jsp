<%-- 
    Document   : ReportCertList
    Created on : Sep 6, 2018, 11:24:59 AM
    Author     : THANH-PC
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
        <title></title>
        <script language="javascript">
            document.title = reportcertlist_title_list;
            changeFavicon("../");
            $(document).ready(function () {
                $('#demo1').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
                if(localStorage.getItem("LOCAL_TAB_REPORTCERT") === JS_STR_EXPORT_TYPE_REPORT_CERT_TOKEN)
                {
                    $("#idLi_contentToken").addClass("active");
                    $("#tab_contentToken").addClass("active in");
                    $("#idLi_contentSignserver").removeClass("active");
                    $("#tab_contentSignServer").removeClass("active in");
                }
                if(localStorage.getItem("LOCAL_TAB_REPORTCERT") === JS_STR_EXPORT_TYPE_REPORT_CERT_SIGNSERVER)
                {
                    $("#idLi_contentToken").removeClass("active");
                    $("#tab_contentToken").removeClass("active in");
                    $("#idLi_contentSignserver").addClass("active");
                    $("#tab_contentSignServer").addClass("active in");
                }
                $("#idLi_contentToken").on("click", function () {
                    localStorage.setItem("LOCAL_TAB_REPORTCERT", JS_STR_EXPORT_TYPE_REPORT_CERT_TOKEN);
                });
                $("#idLi_contentSignserver").on("click", function () {
                    localStorage.setItem("LOCAL_TAB_REPORTCERT", JS_STR_EXPORT_TYPE_REPORT_CERT_SIGNSERVER);
                });
                $('.loading-gif').hide();
                $('[data-toggle="tooltipPrefix"]').tooltip();
            });
            function searchForm(id)
            {
                localStorage.setItem("LOCAL_TAB_REPORTCERT", null);
                document.getElementById("idHiddenLoad").value = JS_STR_GRID_SEARCH_RESET;
                document.getElementById("tempCsrfToken").value = id;
                var f = document.form;
                f.method = "post";
                f.action = '';
                f.submit();
            }
            $(document).ready(function () {
                $('.loading-gifHardware').hide();
                $('#myModalOTPHardware').modal({
                    backdrop: 'static',
                    keyboard: true,
                    show: false
                });
            });
            function ExportControlCert(idType, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                var servletUrl = "../ExportCSVParam";
                var servletParam = "exportreportcertlist";
                if(IsWhichCA === JS_IS_WHICH_ABOUT_CA_HILO) {
                    servletUrl = "../ExportCSVPhaseTwo";
                    servletParam = "exportreportcertlist_20";
                }
                $.ajax({
                    type: "post",
                    url: servletUrl,
                    data: {
                        idParam: servletParam,
                        idType: idType,
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
            function PrintControlCert(idType, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../PrintFormCommon",
                    data: {
                        idParam: "printreportcertlist",
                        idType: idType,
                        CsrfToken: idCSRF
                    },
                    catche: false,
                    success: function (html) {
                        var arr = sSpace(html).split('###');
                        if (arr[0] === "0")
                        {
                            $(".loading-gif").hide();
                            $('#over').remove();
                            PrintPreview(arr[1]);
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
        <%        if ((session.getAttribute("UserID")) != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                CERTIFICATION_POLICY_DATA[][] sessPolicyFormFactor_Data = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessPolicyFormFactor_Data");
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
                        document.getElementById("idNameURL").innerHTML = reportcertlist_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%
                                int status = 1000;
                                int statusLoad = 0;
                                int j = 1;
                                int n = 1;
                                String nameHiddenLoad = request.getParameter("nameHiddenLoad");
                                String tempIPagNo = request.getParameter("iPagNo");
                                String tempIPagNo1 = request.getParameter("iPagNo1");
                                int iTotRslts = com.Converter(request.getParameter("iTotRslts"));
                                int iTotRslts1 = com.Converter(request.getParameter("iTotRslts1"));
                                int iTotPags = com.Converter(request.getParameter("iTotPags"));
                                int iTotPags1 = com.Converter(request.getParameter("iTotPags1"));
                                int iPagNo = com.Converter(tempIPagNo);
                                int iPagNo1 = com.Converter(tempIPagNo1);
                                int iPaNoSS = iPagNo;
                                int iPaNoSS1 = iPagNo1;
                                int cPagNo = com.Converter(request.getParameter("cPagNo"));
                                int cPagNo1 = com.Converter(request.getParameter("cPagNo1"));
                                if (Definitions.CONFIG_GRID_SEARCH_RESET.equals(nameHiddenLoad)) {
                                    tempIPagNo = "null";
                                    tempIPagNo1 = "null";
                                    iPagNo = 0;
                                    cPagNo = 0;
                                    iPaNoSS = 0;
                                    iPagNo1 = 0;
                                    cPagNo1 = 0;
                                    iPaNoSS1 = 0;
                                }
                                int iEnRsNo = 0;
                                if (iPagNo == 0) {
                                    iPagNo = 0;
                                } else {
                                    iPagNo = Math.abs((iPagNo - 1) * iSwRws);
                                }
                                int iEnRsNo1 = 0;
                                if (iPagNo1 == 0) {
                                    iPagNo1 = 0;
                                } else {
                                    iPagNo1 = Math.abs((iPagNo1 - 1) * iSwRws);
                                }
                                String hasPaging = "0";
                                if (tempIPagNo != null && !"null".equals(tempIPagNo) && !"".equals(tempIPagNo)) {
                                    hasPaging = "1";
                                }
                                String hasPaging1 = "0";
                                if (tempIPagNo1 != null && !"null".equals(tempIPagNo1) && !"".equals(tempIPagNo1)) {
                                    hasPaging1 = "1";
                                }
                                String strMess = "";
//                                String strMessSignserver = "";
                                String sPage_Num_Cert = cogCommon.GetPropertybyCode(Definitions.CONFIG_PAGING_NUMBER_REPORT_CERT).trim();
                                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                                try {
                                    REPORT_PER_MONTH[][] rsPginToken = new REPORT_PER_MONTH[1][];
//                                    REPORT_PER_MONTH[][] rsPginSignServer = new REPORT_PER_MONTH[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (request.getMethod().equals("POST") || "1".equals(hasPaging) || "1".equals(hasPaging1)) {
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("pIaReportCertList", null);
                                            session.setAttribute("pIbReportCertList", null);
                                            session.setAttribute("pIaReportCertList1", null);
                                            session.setAttribute("pIbReportCertList1", null);
                                            session.setAttribute("SearchIPageNoPagingReportCertList", null);
                                            session.setAttribute("SearchISwRwsPagingReportCertList", null);
                                        }
                                        String sCsrfToken = request.getParameter("CsrfToken");
                                        String stempCsrfToken = request.getParameter("tempCsrfToken");
                                        if (!"1".equals(hasPaging) && !"1".equals(hasPaging1)) {
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
                                    String PKI_FORMFACTOR = request.getParameter("PKI_FORMFACTOR");
                                    String idBranchOffice = request.getParameter("idBranchOffice");
                                    String USER = request.getParameter("USER");
                                    if ("1".equals(hasPaging) || "1".equals(hasPaging1)) {
                                        session.setAttribute("SearchSharePagingRegisToken", "0");
                                        ToCreateDate = (String) session.getAttribute("sessMonthReportCertList");
                                        FromCreateDate = (String) session.getAttribute("sessYearReportCertList");
                                        PKI_FORMFACTOR = (String) session.getAttribute("sessFormFactorReportCertList");
                                        idBranchOffice = (String) session.getAttribute("sessBranchOfficeReportCertList");
                                        USER = (String) session.getAttribute("sessUserReportCertList");
                                        session.setAttribute("SessParamOnPagingCertList", null);
                                    } else {
                                        session.setAttribute("SearchSharePagingRegisToken", "1");
                                        session.setAttribute("CountListReportCertList", null);
                                        session.setAttribute("CountListReportCertList1", null);
                                    }
                                    session.setAttribute("sessYearReportCertList", FromCreateDate);
                                    session.setAttribute("sessMonthReportCertList", ToCreateDate);
                                    session.setAttribute("sessFormFactorReportCertList", PKI_FORMFACTOR);
                                    session.setAttribute("sessBranchOfficeReportCertList", idBranchOffice);
                                    session.setAttribute("sessUserReportCertList", USER);
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idBranchOffice)) {
                                        idBranchOffice = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(USER)) {
                                        USER = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(PKI_FORMFACTOR)) {
                                        PKI_FORMFACTOR = "";
                                    }
                                    int ssToken = 0;
                                    int ssSignserver = 0;
                                    String pBRANCH_BENEFICIARY_ID = SessUserAgentID;
                                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                        pBRANCH_BENEFICIARY_ID=EscapeUtils.escapeHtmlSearch(idBranchOffice);
                                    }
                                    if ((session.getAttribute("CountListReportCertList")) == null) {
                                        ssToken = db.S_BO_REPORT_PER_MONTH_TOTAL(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(FromCreateDate),EscapeUtils.escapeHtmlSearch(idBranchOffice),
                                            USER, PKI_FORMFACTOR, pBRANCH_BENEFICIARY_ID);
//                                        ssSignserver = db.S_BO_REPORT_PER_MONTH_SS_TOTAL(EscapeUtils.escapeHtmlSearch(ToCreateDate),
//                                            EscapeUtils.escapeHtmlSearch(FromCreateDate),EscapeUtils.escapeHtmlSearch(idBranchOffice),USER);
                                        session.setAttribute("CountListReportCertList", String.valueOf(ssToken));
                                        session.setAttribute("CountListReportCertList1", String.valueOf(ssSignserver));
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListReportCertList");
                                        String sCount1 = (String) session.getAttribute("CountListReportCertList1");
                                        ssToken = Integer.parseInt(sCount);
                                        ssSignserver = Integer.parseInt(sCount1);
                                        session.setAttribute("CountListReportCertList", String.valueOf(ssToken));
                                        session.setAttribute("CountListReportCertList1", String.valueOf(ssSignserver));
                                    }
                                    iTotRslts = ssToken;
                                    iTotRslts1 = ssSignserver;
                                    if (iTotRslts > 0 || iTotRslts1 > 0) {
                                        db.S_BO_REPORT_PER_MONTH_LIST(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(idBranchOffice),USER, PKI_FORMFACTOR,
                                            sessLanguageGlobal, rsPginToken, iPagNo, iSwRws, pBRANCH_BENEFICIARY_ID);
//                                        db.S_BO_REPORT_PER_MONTH_SS_LIST(EscapeUtils.escapeHtmlSearch(ToCreateDate),
//                                            EscapeUtils.escapeHtmlSearch(FromCreateDate),
//                                            EscapeUtils.escapeHtmlSearch(idBranchOffice),USER,
//                                            sessLanguageGlobal, rsPginSignServer, iPagNo1, iSwRws);
                                        session.setAttribute("SearchIPageNoPagingReportCertList", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingReportCertList", String.valueOf(iSwRws));
                                        strMess = com.convertMoney(ssToken);
//                                        strMessSignserver = com.convertMoney(ssSignserver);
//                                        if (rsPginToken[0].length > 0 || rsPginSignServer[0].length > 0) {
                                        if (rsPginToken[0].length > 0) {
                                            status = 1;
                                        } else {
                                            status = 1000;
                                        }
                                    } else {
                                        status = 1000;
                                    }
                                } else {
                                    session.setAttribute("SearchIPageNoPagingReportCertList", null);
                                    session.setAttribute("SearchISwRwsPagingReportCertList", null);
                                    session.setAttribute("sessYearReportCertList", null);
                                    session.setAttribute("sessMonthReportCertList", null);
                                    session.setAttribute("sessFormFactorReportCertList", null);
                                    session.setAttribute("sessUserReportCertList", null);
                                    session.setAttribute("sessBranchOfficeReportCertList", null);
                                }
                                %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="form" method="post" class="form-horizontal">
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_mounth);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="idMounth" id="idMounth" class="form-control123">
                                                                <option value="01" <%= session.getAttribute("sessMonthReportCertList") != null
                                                                    && session.getAttribute("sessMonthReportCertList").toString().trim().equals("01") ? "selected" : "" %>>01</option>
                                                                <option value="02" <%= session.getAttribute("sessMonthReportCertList") != null
                                                                    && session.getAttribute("sessMonthReportCertList").toString().trim().equals("02") ? "selected" : "" %>>02</option>
                                                                <option value="03" <%= session.getAttribute("sessMonthReportCertList") != null
                                                                    && session.getAttribute("sessMonthReportCertList").toString().trim().equals("03") ? "selected" : "" %>>03</option>
                                                                <option value="04" <%= session.getAttribute("sessMonthReportCertList") != null
                                                                    && session.getAttribute("sessMonthReportCertList").toString().trim().equals("04") ? "selected" : "" %>>04</option>
                                                                <option value="05" <%= session.getAttribute("sessMonthReportCertList") != null
                                                                    && session.getAttribute("sessMonthReportCertList").toString().trim().equals("05") ? "selected" : "" %>>05</option>
                                                                <option value="06" <%= session.getAttribute("sessMonthReportCertList") != null
                                                                    && session.getAttribute("sessMonthReportCertList").toString().trim().equals("06") ? "selected" : "" %>>06</option>
                                                                <option value="07" <%= session.getAttribute("sessMonthReportCertList") != null
                                                                    && session.getAttribute("sessMonthReportCertList").toString().trim().equals("07") ? "selected" : "" %>>07</option>
                                                                <option value="08" <%= session.getAttribute("sessMonthReportCertList") != null
                                                                    && session.getAttribute("sessMonthReportCertList").toString().trim().equals("08") ? "selected" : "" %>>08</option>
                                                                <option value="09" <%= session.getAttribute("sessMonthReportCertList") != null
                                                                    && session.getAttribute("sessMonthReportCertList").toString().trim().equals("09") ? "selected" : "" %>>09</option>
                                                                <option value="10" <%= session.getAttribute("sessMonthReportCertList") != null
                                                                    && session.getAttribute("sessMonthReportCertList").toString().trim().equals("10") ? "selected" : "" %>>10</option>
                                                                <option value="11" <%= session.getAttribute("sessMonthReportCertList") != null
                                                                    && session.getAttribute("sessMonthReportCertList").toString().trim().equals("11") ? "selected" : "" %>>11</option>
                                                                <option value="12" <%= session.getAttribute("sessMonthReportCertList") != null
                                                                    && session.getAttribute("sessMonthReportCertList").toString().trim().equals("12") ? "selected" : "" %>>12</option>
                                                            </select>
                                                            <%
                                                                if(session.getAttribute("sessMonthReportCertList") == null)
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
                                                            <select name="idYear" id="idYear" class="form-control123">
                                                                <%
                                                                    for(int i=18; i<68; i++) {
                                                                        String sYearAfter = String.valueOf(i);
                                                                        if(sYearAfter.length() == 1)
                                                                        {
                                                                            sYearAfter = "0" + sYearAfter;
                                                                        }
                                                                        String sYearAfterID = "20" + sYearAfter;
                                                                %>
                                                                <option value="<%=sYearAfterID%>" <%= session.getAttribute("sessYearReportCertList") != null
                                                                    && session.getAttribute("sessYearReportCertList").toString().trim().equals(sYearAfterID) ? "selected" : "" %>><%=sYearAfterID%></option>
                                                                <%
                                                                    }
                                                                %>
                                                            </select>
                                                            <%
                                                                if(session.getAttribute("sessYearReportCertList") == null)
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
                                                            <script>document.write(global_fm_Method);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;text-align: right;">
                                                            <select id="PKI_FORMFACTOR" name="PKI_FORMFACTOR" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessFormFactorReportCertList") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessFormFactorReportCertList").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    PKI_FORMFACTOR[][] rsPKIFormFactor = new PKI_FORMFACTOR[1][];
                                                                    db.S_BO_PKI_FORMFACTOR_COMBOBOX(sessLanguageGlobal, rsPKIFormFactor);
                                                                    if (rsPKIFormFactor.length > 0) {
                                                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                                                        {
                                                                            for (int i = 0; i < rsPKIFormFactor[0].length; i++) {
                                                                %>
                                                                <option value="<%= String.valueOf(rsPKIFormFactor[0][i].ID)%>" <%= session.getAttribute("sessFormFactorReportCertList") != null && String.valueOf(rsPKIFormFactor[0][i].ID).equals(session.getAttribute("sessFormFactorReportCertList").toString()) ? "selected" : ""%>><%= rsPKIFormFactor[0][i].REMARK%></option>
                                                                <%
                                                                            }
                                                                        } else {
                                                                            for (int i = 0; i < rsPKIFormFactor[0].length; i++) {
                                                                                if(CommonFunction.checkAccessPKIFormFactorForBranch(rsPKIFormFactor[0][i].NAME, sessPolicyFormFactor_Data) == true)
                                                                                {
                                                                %>
                                                                <option value="<%= String.valueOf(rsPKIFormFactor[0][i].ID)%>" <%= session.getAttribute("sessFormFactorReportCertList") != null && String.valueOf(rsPKIFormFactor[0][i].ID).equals(session.getAttribute("sessFormFactorReportCertList").toString()) ? "selected" : ""%>><%= rsPKIFormFactor[0][i].REMARK%></option>
                                                                <%
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <%
                                                //if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                            %>
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_Branch);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="idBranchOffice" id="idBranchOffice" class="form-control123" onchange="LOAD_BACKOFFICE_USER(this.value, '<%= anticsrf%>');">
                                                                <%
//                                                                    BRANCH[][] rsBranch = new BRANCH[1][];
//                                                                    db.S_BO_BRANCH_COMBOBOX(sessLanguageGlobal, rsBranch);
                                                                    BRANCH[][] rsBranch = (BRANCH[][]) session.getAttribute("sessTreeBranchSystemAgency");
                                                                    String sFristBranch_ID = "";
                                                                    if (rsBranch[0].length > 0) {
                                                                        for (BRANCH temp1 : rsBranch[0]) {
                                                                            if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT))
                                                                            {
                                                                                if("".equals(sFristBranch_ID))
                                                                                {
                                                                                    sFristBranch_ID = String.valueOf(temp1.ID);
                                                                                }
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessBranchOfficeReportCertList") != null
                                                                    && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeReportCertList").toString().trim())
                                                                    ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                                <%
                                                                            }
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
                                                            <script>document.write(global_fm_user_create);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="USER" id="USER" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessUserReportCertList") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessUserReportCertList").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    String sessUserID = session.getAttribute("UserID").toString().trim();
                                                                    String SessRoleID_ID = session.getAttribute("RoleID_ID").toString().trim();
                                                                    if(session.getAttribute("sessBranchOfficeReportCertList") != null)
                                                                    {
                                                                        sFristBranch_ID = session.getAttribute("sessBranchOfficeReportCertList").toString().trim();
                                                                    }
                                                                    if(!"".equals(sFristBranch_ID)) {
                                                                        BACKOFFICE_USER[][] rssUser;
                                                                        if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                                            rssUser = new BACKOFFICE_USER[1][];
                                                                            db.S_BO_GET_USER_BRANCH_ALL(sFristBranch_ID, rssUser);
                                                                            if (rssUser[0].length > 0) {
                                                                                for (int i = 0; i < rssUser[0].length; i++) {
                                                                %>
                                                                <option value="<%=String.valueOf(rssUser[0][i].ID)%>"  <%= session.getAttribute("sessUserReportCertList") != null && String.valueOf(rssUser[0][i].ID).equals(session.getAttribute("sessUserReportCertList").toString()) ? "selected" : ""%>><%=rssUser[0][i].FULL_NAME + " (" + rssUser[0][i].USERNAME + ")" %></option>
                                                                <%
                                                                                }
                                                                            }
                                                                        } else {
                                                                            if(SessUserAgentID.equals(sFristBranch_ID)) {
                                                                                rssUser = new BACKOFFICE_USER[1][];
                                                                                db.S_BO_USER_DETAIL(sessUserID, sessLanguageGlobal, rssUser);
                                                                                if(rssUser[0].length > 0) {
                                                                %>
                                                                <option value="<%=String.valueOf(rssUser[0][0].ID)%>"  <%= session.getAttribute("sessUserReportCertList") != null && String.valueOf(rssUser[0][0].ID).equals(session.getAttribute("sessUserReportCertList").toString()) ? "selected" : ""%>><%=rssUser[0][0].FULL_NAME + " (" + rssUser[0][0].USERNAME + ")" %></option>
                                                                <%
                                                                                }
                                                                            } else {
                                                                                rssUser = new BACKOFFICE_USER[1][];
                                                                                db.S_BO_GET_USER_BRANCH_ALL(sFristBranch_ID, rssUser);
                                                                                if (rssUser[0].length > 0) {
                                                                                    for (int i = 0; i < rssUser[0].length; i++) {
                                                                %>
                                                                <option value="<%=String.valueOf(rssUser[0][i].ID)%>"  <%= session.getAttribute("sessUserReportCertList") != null && String.valueOf(rssUser[0][i].ID).equals(session.getAttribute("sessUserReportCertList").toString()) ? "selected" : ""%>><%=rssUser[0][i].FULL_NAME + " (" + rssUser[0][i].USERNAME + ")" %></option>
                                                                <%
                                                                                    }
                                                                                }
                                                                            }
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
                                                <script>
                                                    function LOAD_BACKOFFICE_USER(objAgency, idCSRF)
                                                    {
                                                        if(objAgency === JS_STR_GRID_COMBOBOX_VALUE_ALL)
                                                        {
                                                            var cbxUSER = document.getElementById("USER");
                                                            removeOptions(cbxUSER);
                                                            cbxUSER.options[cbxUSER.options.length] = new Option(global_fm_combox_all, JS_STR_GRID_COMBOBOX_VALUE_ALL);
                                                        }
                                                        else
                                                        {
                                                            $.ajax({
                                                                type: "post",
                                                                url: "../JSONCommon",
                                                                data: {
                                                                    idParam: 'loadadminuser_ofagency',
                                                                    BRANCH_ID: objAgency,
                                                                    CsrfToken: idCSRF
                                                                },
                                                                cache: false,
                                                                success: function (html)
                                                                {
                                                                    if (html.length > 0)
                                                                    {
                                                                        var cbxUSER = document.getElementById("USER");
                                                                        removeOptions(cbxUSER);
                                                                        var obj = JSON.parse(html);
                                                                        if (obj[0].Code === "0")
                                                                        {
                                                                            cbxUSER.options[cbxUSER.options.length] = new Option(global_fm_combox_all, JS_STR_GRID_COMBOBOX_VALUE_ALL);
    //                                                                        document.getElementById("RoleID_ID").value = obj[0].ROLE_ID;
                                                                            for (var i = 0; i < obj.length; i++) {
                                                                                cbxUSER.options[cbxUSER.options.length] = new Option(obj[i].FULL_NAME + " (" + obj[i].USERNAME + ")", obj[i].ID);
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
                                                                            cbxUSER.options[cbxUSER.options.length] = new Option(global_fm_combox_all, JS_STR_GRID_COMBOBOX_VALUE_ALL);
                                                                        }
                                                                        else {
                                                                            funErrorAlert(global_errorsql);
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                            return false;
                                                        }
                                                    }
                                                </script>
                                            </div>
                                            <%
                                              //  } else {
                                            %>
<!--                                            <input type="text" readonly style="display: none;" name="idBranchOffice" id="idBranchOffice" value="<= SessUserAgentID%>">
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_user_create);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="USER" id="USER" class="form-control123">
                                                            <option value="<= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <= session.getAttribute("sessUserReportCertList") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessUserReportCertList").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            <
                                                                BACKOFFICE_USER[][] rssUser = new BACKOFFICE_USER[1][];
                                                                db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rssUser);
                                                                if (rssUser[0].length > 0) {
                                                                    for (int i = 0; i < rssUser[0].length; i++) {
                                                            %>
                                                            <option value="<=String.valueOf(rssUser[0][i].ID)%>"  <= session.getAttribute("sessUserReportCertList") != null && String.valueOf(rssUser[0][i].ID).equals(session.getAttribute("sessUserReportCertList").toString()) ? "selected" : ""%>><=rssUser[0][i].FULL_NAME + " (" + rssUser[0][i].USERNAME + ")" %></option>
                                                            <
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <div class="col-sm-5" style="padding-right: 0px;">
                                                            
                                                        </div>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <button type="button" class="btn btn-info" onClick="searchForm('<=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        
                                                    </div>
                                                </div>
                                            </div>-->
                                            <%
                                               // }
                                            %>
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                            <input id="idHiddenLoad" name="nameHiddenLoad" value="" type="hidden"/>
                                        </form>
                                    </div>
                                    <div class="x_content" style="margin-top: 0px;">
                                        <%
                                            if (status == 1 && statusLoad == 1) {
                                        %>
                                        <div class="" role="tabpanel" data-example-id="togglable-tabs">
                                            <ul id="myTabTypeKey" class="nav nav-tabs bar_tabs" role="tablist">
                                                <li role="presentation" class="active" id="idLi_contentToken">
                                                    <a href="#tab_contentToken" id="home-tab" role="tab" data-toggle="tab" aria-expanded="true">
                                                        <script>document.write(reportneac_fm_tab_cts_token);</script>
                                                    </a>
                                                </li>
                                            </ul>
                                            <div id="myTabContentTypeKey" class="tab-content">
                                                <div role="tabpanel" class="tab-pane fade active in" id="tab_contentToken" aria-labelledby="home-tab">
                                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                                        <h2>
                                                            <!--<i class="fa fa-list-ul"></i> <script>document.write(reportneac_fm_tab_cts_token);</script>-->
                                                        </h2>
                                                        <ul class="nav navbar-right panel_toolbox">
                                                            <li style="color: red;font-weight: bold;">
                                                                <script>document.write(global_label_grid_sum);</script><%= strMess%> &nbsp;&nbsp;
                                                                <%
                                                                    if(rsPginToken[0].length > 0) {
                                                                %>
                                                                <button type="button" class="btn btn-info" onClick="ExportControlCert(JS_STR_EXPORT_TYPE_REPORT_CERT_TOKEN, '<%= anticsrf%>');"><script>document.write(global_fm_button_export_csv);</script></button>
                                                                &nbsp;&nbsp;
                                                                <button type="button" class="btn btn-info" onClick="PrintControlCert(JS_STR_EXPORT_TYPE_REPORT_CERT_TOKEN, '<%= anticsrf%>');"><script>document.write(global_fm_button_print_report);</script></button>
                                                                <script>

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
                                                        <%
                                                            if(rsPginToken[0].length > 0) {
                                                        %>
                                                        <div class="table-responsive">
                                                            <table id="idTableList" class="table table-bordered table-striped projects">
                                                                <thead>
                                                                <th><script>document.write(global_fm_STT);</script></th>
                                                                <th><script>document.write(branch_fm_name);</script></th>
                                                                <th><script>document.write(branch_fm_code);</script></th>
                                                                <th><script>document.write(global_fm_user_create);</script></th>
                                                                <th style="width: 100px;"><script>document.write(global_fm_enterprise_id);</script></th>
                                                                <th><script>document.write(global_fm_Status);</script></th>
                                                                <th style="min-width:130px;"><script>document.write(global_fm_grid_company);</script></th>
                                                                <th style="width: 100px;"><script>document.write(global_fm_personal_id);</script></th>
                                                                <th style="width: 120px;"><script>document.write(global_fm_grid_personal);</script></th>
                                                                <th><script>document.write(global_fm_city);</script></th>
                                                                <th><script>document.write(global_fm_duration_cts);</script></th>
                                                                <th><script>document.write(cert_fm_type_request);</script></th>
                                                                <th><script>document.write(global_fm_Method);</script></th>
                                                                <th><script>document.write(global_fm_date_create);</script></th>
                                                                <th><script>document.write(global_fm_date_cancel);</script></th>
                                                                <th><script>document.write(global_fm_num_date_cancel);</script></th>
                                                                <th><script>document.write(global_fm_date_gen);</script></th>
                                                                <th><script>document.write(global_fm_valid_cert);</script></th>
                                                                <th><script>document.write(global_fm_Expire_cert);</script></th>
                                                                <th><script>document.write(token_fm_tokenid);</script></th>
                                                                <th><script>document.write(global_fm_serial);</script></th>
                                                                </thead>
                                                                <tbody>
                                                                    <%
                                                                        if (iPaNoSS > 1) {
                                                                            j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                                        }
                                                                        if (rsPginToken[0].length > 0) {
                                                                            for (REPORT_PER_MONTH temp1 : rsPginToken[0]) {
                                                                    %>
                                                                    <tr>
                                                                        <td><%= com.convertMoney(j)%></td>
                                                                        <td><%= EscapeUtils.CheckTextNull(temp1.BRANCH_NAME)%></td>
                                                                        <td><%= EscapeUtils.CheckTextNull(temp1.BRANCH_DESC)%></td>
                                                                        <td><%= EscapeUtils.CheckTextNull(temp1.USERNAME_CREATED)%></td>
                                                                        <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= temp1.ENTERPRISE_ID_REMARK%>"><%= temp1.ENTERPRISE_ID%></a></td>
                                                                        <td><%= temp1.CERTIFICATION_STATE_DESC%></td>
                                                                        <td><%= EscapeUtils.CheckTextNull(temp1.COMPANY_NAME)%></td>
                                                                        <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= temp1.PERSONAL_ID_REMARK%>"><%= temp1.PERSONAL_ID%></a></td>
                                                                        <td><%= EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME)%></td>
                                                                        <td><%= EscapeUtils.CheckTextNull(temp1.PROVINCE_NAME)%></td>
                                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PROFILE_NAME)%></td>
                                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_ATTR_TYPE_DESC)%></td>
                                                                        <td><%= EscapeUtils.CheckTextNull(temp1.FORM_FACTOR_DESC)%></td>
                                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_DT)%></td>
                                                                        <td><%= EscapeUtils.CheckTextNull(temp1.REVOKED_DT)%></td>
                                                                        <td><%= EscapeUtils.CheckTextNull(temp1.NUMBER_DELETED)%></td>
                                                                        <td><%= EscapeUtils.CheckTextNull(temp1.GENERATED_DT)%></td>
                                                                        <td><%= EscapeUtils.CheckTextNull(temp1.EFFECTIVE_DT)%></td>
                                                                        <td><%= EscapeUtils.CheckTextNull(temp1.EXPIRATION_DT)%></td>
                                                                        <td><%= CommonFunction.checkViewTokenValid(EscapeUtils.CheckTextNull(temp1.TOKEN_SN)) ? EscapeUtils.CheckTextNull(temp1.TOKEN_SN) : "" %></td>
                                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_SN)%></td>
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
                                                                    <tr>
                                                                        <script>
                                                                                $(document).ready(function () {
                                                                                    document.getElementById("idTDPaging").colSpan = document.getElementById('idTableList').rows[0].cells.length;
                                                                                });
                                                                            </script>
                                                                            <td id="idTDPaging" style="text-align: center;">
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
                                                </div>
                                            </div>
                                        </div>
                                        <!--end new-->
                                        <%
                                            } if (status == 1000 && statusLoad == 1) {
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
                                        %>
                                    </div>
                                </div>
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
                        function LoadCert(Tag_ID)
                        {
                            $.ajax({
                                type: "post",
                                url: "../JSONCommon",
                                data: {
                                    idParam: 'listcertreportquick',
                                    Tag_ID: Tag_ID
                                },
                                cache: false,
                                success: function (html)
                                {
                                    if (html.length > 0)
                                    {
                                        var obj = JSON.parse(html);
                                        if (obj[0].Code === "0")
                                        {
                                            var content = "";
                                            $("#idTemplateAssign").empty();
                                            var sNum = obj.length;
                                            var j = 0;
                                            for (var i = 0; i < obj.length; i++) {
                                                var vMST_MNS = obj[i].ENTERPRISE_ID;
//                                                if (vMST_MNS === "") {
//                                                    vMST_MNS = obj[i].BUDGET_CODE;
//                                                }
//                                                if (vMST_MNS === "") {
//                                                    vMST_MNS = obj[i].DECISION;
//                                                }
                                                var vCMND_HC = obj[i].PERSONAL_ID;
//                                                if (vCMND_HC === "")
//                                                {
//                                                    vCMND_HC = obj[i].PASSPORT;
//                                                }
                                                content += '<tr>' +
                                                        '<td>' + obj[i].Index + '</td>' +
                                                        '<td>' + obj[i].COMPANY_NAME + '</td>' +
                                                        '<td>' + vMST_MNS + '</td>' +
                                                        '<td>' + obj[i].PERSONAL_NAME + '</td>' +
                                                        '<td>' + vCMND_HC+ '</td>' +
                                                        '<td>' + obj[i].CERTIFICATION_PROFILE_DESC + '</td>' +
                                                        '<td>' + obj[i].CERTIFICATION_PURPOSE_DESC + '</td>' +
                                                        '<td>' + obj[i].CERTIFICATION_STATE_DESC + '</td>' +
                                                        '<td>' + obj[i].BRANCH_NAME + '</td>' +
                                                        '</tr>';
                                                j++;
                                            }
                                            $("#idTemplateAssign").append(content);
                                            if (parseInt(sNum) > 0)
                                            {
                                                $('#green').smartpaginator({totalrecords: sNum, recordsperpage: <%= sPage_Num_Cert%>, datacontainer: 'tblCertUse', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
                                            }
                                        }
                                        else if (obj[0].Code === "1")
                                        {
                                            $("#idTemplateAssign").empty();
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
                                            $("#idTemplateAssign").empty();
                                            funErrorAlert(global_errorsql);
                                        }
                                    }
                                    else
                                    {
                                        $("#idTemplateAssign").empty();
                                    }
                                }
                            });
                        }
                        $(document).ready(function () {
                            $('#myModalOTPHardware').on('show.bs.modal', function (e) {
                                var Tag_ID = $(e.relatedTarget).data('book-id');
                                LoadCert(Tag_ID);
                            });
                        });
                        function CloseDialog()
                        {
                            $('#myModalOTPHardware').modal('hide');
                            $(".loading-gifHardware").hide();
                            $(".loading-gif").hide();
                            $('#over').remove();
                        }
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
                    <style>
                        @media (min-width: 768px) {
                            .modal-dialog {
                                width: 900px;
                                margin: 30px auto;
                            }
                        }
                    </style>
                    <div id="myModalOTPHardware" class="modal fade" role="dialog">
                        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                             left: 0; height: 100%;" class="loading-gifHardware">
                            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
                        </div>
                        <div class="modal-dialog modal-800" id="myDialogOTPHardware">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <div style="width: 70%; float: left;">
                                        <h3 class="modal-title" style="font-size: 18px;"><script>document.write(global_fm_cert_list)</script></h3>
                                    </div>
                                    <div style="width: 29%; float: right;text-align: right;">
                                        <button type="button" onclick="CloseDialog();" class="btn btn-info"><script>document.write(global_fm_button_close)</script></button>
                                    </div>
                                </div>
                                <div class="modal-body">
                                    <form role="formAddOTPHardware" id="formOTPHardware">
                                        <div class="table-responsive">
                                        <table id="tblCertUse" class="table table-bordered table-striped projects">
                                            <thead>
                                            <th><script>document.write(global_fm_STT);</script></th>
                                            <th style="width: 120px;"><script>document.write(global_fm_grid_company);</script></th>
                                            <th style="width: 100px;"><script>document.write(global_fm_enterprise_id);</script></th>
                                            <th style="width: 120px;"><script>document.write(global_fm_grid_personal);</script></th>
                                            <th style="width: 100px;"><script>document.write(global_fm_personal_id);</script></th>
                                            <th><script>document.write(global_fm_duration_cts);</script></th>
                                            <th><script>document.write(global_fm_certtype);</script></th>
                                            <th><script>document.write(global_fm_Status);</script></th>
                                            <th><script>document.write(token_fm_agent);</script></th>
                                            </thead>
                                            <tbody id="idTemplateAssign"></tbody>
                                        </table>
                                        <div id="green" style="margin: 5px 0 10px 0;"> </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
            <!--<script src="../style/jquery.min.js"></script>-->
            <script src="../style/bootstrap.min.js"></script>
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