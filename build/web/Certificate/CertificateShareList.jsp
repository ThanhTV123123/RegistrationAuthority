<%-- 
    Document   : CertificateShareList
    Created on : Dec 12, 2019, 5:45:17 PM
    Author     : USER
--%>

<%@page import="vn.ra.process.CommonReferServlet"%>
<%@page import="java.net.URLEncoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<%  response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
    String isUIDCollection = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_UID_COLLECTION_DISPLAY_ENABLED);
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
            document.title = certsharelist_title_list;
            $(document).ready(function () {
                localStorage.setItem("LOCAL_PARAM_CERTSHARELIST", null);
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
                $('[data-toggle="tooltipPrefix"]').tooltip();
            });
            function popupChangeCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('CertificateShareChange.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function popupRevokeCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('RevokeCertView.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            
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
            function popupPrintHandoverList_bk(id)
            {
                localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", id);
                localStorage.setItem("TransferPage", "list");
                window.location = "PrintHandover.jsp?id="+ id;
            }
            function popupPrintRegis(id, isEnterprise, idRegis)
            {
                ShowDialogList(id, isEnterprise, idRegis);
            }
            function popupReIssueCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('CertificateShareReissue.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function popupRenewCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('CertificateShareRenew.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function popupBuyCertMore(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('CertificateShareAddMore.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            
            function popupSuspendCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('SuspendCertView.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            
            function popupRecoveryCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('RecoveryCertView.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function searchForm(id)
            {
                var isPrefixCollect = '<%=isUIDCollection%>';
                if(isPrefixCollect !== "1"){
                    var vTAX_CODE = document.form.TAX_CODE.value;
                    var vBUDGET_CODE = document.form.BUDGET_CODE.value;
                    var vDECISION = document.form.DECISION.value;
                    var vP_ID = document.form.P_ID.value;
                    var vPASSPORT = document.form.PASSPORT.value;
                    var vCCCD = document.form.CCCD.value;
                    if(sSpace(vTAX_CODE) === "" && sSpace(vBUDGET_CODE) === "" && sSpace(vDECISION) === ""
                        && sSpace(vP_ID) === "" && sSpace(vPASSPORT) === "" && sSpace(vCCCD) === "") {
                        funErrorAlert(global_fm_check_search);
                        return false;
                    }
                } else {
                    var vENTERPRISE_ID = document.form.ENTERPRISE_ID.value;
                    var vPERSONAL_ID = document.form.PERSONAL_ID.value;
                    if(sSpace(vENTERPRISE_ID) === "" && sSpace(vPERSONAL_ID) === "") {
                        funErrorAlert(global_fm_check_search);
                        return false;
                    }
                }
                document.getElementById("idHiddenLoad").value = JS_STR_GRID_SEARCH_RESET;
                document.getElementById("tempCsrfToken").value = id;
                var f = document.form;
                f.method = "post";
                f.action = '';
                f.submit();
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
            function popupHideCTSList()
            {
                document.getElementById('idViewCSRSList').style.display = 'none';
                document.getElementById('idAHideList').style.display = 'none';
                document.getElementById('idAShowList').style.display = '';
            }
            function popupViewCSRSList()
            {
                document.getElementById('idViewCSRSList').style.display = '';
                document.getElementById('idAHideList').style.display = '';
                document.getElementById('idAShowList').style.display = 'none';
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
            .col-sm-4{padding-right: 5px;}
        </style>
    </head>
    <body class="nav-md">
        <%
            if (session.getAttribute("sUserID") != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                String SessUserID = session.getAttribute("UserID").toString().trim();
                String SessRoleID_ID = session.getAttribute("RoleID_ID").toString().trim();
                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Cert");
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
                        document.getElementById("idNameURL").innerHTML = certsharelist_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%                            int status = 1000;
                                int statusLoad = 0;
                                int j = 1;
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
                                String strExpandFilter = "0";
                                try {
                                    String isLoadPeronalPrefix = "";
                                    String isLoadEnterprisePrefix = "";
                                    String sEnterpriseCert = "";
                                    String sPersonalCert = "";
                                    CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (session.getAttribute("RefreshCertShareSess") != null && session.getAttribute("sessTAX_CODECertShare") != null) {
                                        try{
                                            session.setAttribute("RefreshCertShareSessPaging", "1");
                                            session.setAttribute("SearchSharePagingCertShare", "0");
                                            statusLoad = 1;
                                            String TAX_CODE = (String) session.getAttribute("sessTAX_CODECertShare");
                                            String BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODECertShare");
                                            String DECISION = (String) session.getAttribute("sessDECISIONCertShare");
                                            String P_ID = (String) session.getAttribute("sessP_IDCertShare");
                                            String CCCD = (String) session.getAttribute("sessCCCDRenewCert");
                                            String PASSPORT = (String) session.getAttribute("sessPASSPORTCertShare");

                                            session.setAttribute("RefreshCertShareSess", null);
                                            session.setAttribute("sessTAX_CODECertShare", TAX_CODE);
                                            session.setAttribute("sessBUDGET_CODECertShare", BUDGET_CODE);
                                            session.setAttribute("sessDECISIONCertShare", DECISION);
                                            session.setAttribute("sessP_IDCertShare", P_ID);
                                            session.setAttribute("sessCCCDCertShare", CCCD);
                                            session.setAttribute("sessPASSPORTCertShare", PASSPORT);
                                            if(isUIDCollection.equals("1")) {
                                                String sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDCertShare");
                                                String sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDCertShare");
                                                String sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXCertShare");
                                                String sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXCertShare");
                                                session.setAttribute("sessENTERPRISE_IDCertShare", sENTERPRISE_ID);
                                                session.setAttribute("sessPERSONAL_IDCertShare", sPERSONAL_ID);
                                                session.setAttribute("sessENTERPRISE_PREFIXCertShare", sENTERPRISE_PREFIX);
                                                session.setAttribute("sessPERSONAL_PREFIXCertShare", sPERSONAL_PREFIX);
                                                isLoadEnterprisePrefix = sENTERPRISE_PREFIX;
                                                isLoadPeronalPrefix = sPERSONAL_PREFIX;
                                                if(!"".equals(sENTERPRISE_ID)){
                                                    sEnterpriseCert = sENTERPRISE_PREFIX + sENTERPRISE_ID;
                                                }
                                                if(!"".equals(sPERSONAL_ID)){
                                                    sPersonalCert = sPERSONAL_PREFIX + sPERSONAL_ID;
                                                }
                                            }
                                            if(!isUIDCollection.equals("1")) {
                                                String[] sUIDResult = new String[2];
                                                CommonReferServlet.collectFieldToUID(EscapeUtils.escapeHtmlSearch(TAX_CODE), EscapeUtils.escapeHtmlSearch(BUDGET_CODE),
                                                    EscapeUtils.escapeHtmlSearch(DECISION), EscapeUtils.escapeHtmlSearch(P_ID), EscapeUtils.escapeHtmlSearch(PASSPORT),
                                                    EscapeUtils.escapeHtmlSearch(CCCD), sUIDResult);
                                                sEnterpriseCert = sUIDResult[0];
                                                sPersonalCert = sUIDResult[1];
                                            }
                                            int ss = 0;
                                            if ((session.getAttribute("CountListCertShare")) == null) {
                                                ss = db.S_BO_CERTIFICATION_TOTAL_BY_OWNER(SessUserAgentID, SessUserID, sEnterpriseCert, sPersonalCert);
                                                session.setAttribute("CountListCertShare", String.valueOf(ss));
                                            } else {
                                                String sCount = (String) session.getAttribute("CountListCertShare");
                                                ss = Integer.parseInt(sCount);
                                                session.setAttribute("CountListCertShare", String.valueOf(ss));
                                            }
                                            if (session.getAttribute("SearchIPageNoPagingCertShare") != null) {
                                                String sPage = (String) session.getAttribute("SearchIPageNoPagingCertShare");
                                                iPagNo = Integer.parseInt(sPage);
                                            }
                                            if (session.getAttribute("SearchISwRwsPagingCertShare") != null) {
                                                String sSumPage = (String) session.getAttribute("SearchISwRwsPagingCertShare");
                                                iSwRws = Integer.parseInt(sSumPage);
                                            }
                                            if (session.getAttribute("RefreshCertShareSessNumberPaging") != null) {
                                                String sNoPage = (String) session.getAttribute("RefreshCertShareSessNumberPaging");
                                                iPaNoSS = Integer.parseInt(sNoPage);
                                            }
                                            session.setAttribute("SearchIPageNoPagingCertShare", String.valueOf(iPagNo));
                                            session.setAttribute("SearchISwRwsPagingCertShare", String.valueOf(iSwRws));
                                            if (ss > 0) {
                                                db.S_BO_CERTIFICATION_LIST_BY_OWNER(SessUserAgentID, SessUserID, sessLanguageGlobal, rsPgin,
                                                    iPagNo, iSwRws, sEnterpriseCert, sPersonalCert);
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
                                        } catch(Exception e){CommonFunction.LogExceptionServlet(null, "ShareList reload: " + e.getMessage(), e);}
                                    } else if (request.getMethod().equals("POST") || "1".equals(hasPaging)) {
                                        session.setAttribute("RefreshCertShareSessPaging", null);
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("SearchShareStoreRenewCert", null);
                                            session.setAttribute("SearchIPageNoPagingCertShare", null);
                                            session.setAttribute("SearchISwRwsPagingCertShare", null);
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
                                    try{
                                        statusLoad = 1;
                                        request.setCharacterEncoding("UTF-8");
                                        String TAX_CODE ="";
                                        String BUDGET_CODE ="";
                                        String DECISION ="";
                                        String P_ID = "";
                                        String CCCD = "";
                                        String PASSPORT = "";
                                        if(!isUIDCollection.equals("1")) {
                                            TAX_CODE =request.getParameter("TAX_CODE");
                                            BUDGET_CODE =request.getParameter("BUDGET_CODE");
                                            DECISION =request.getParameter("DECISION");
                                            P_ID = request.getParameter("P_ID");
                                            CCCD = request.getParameter("CCCD");
                                            PASSPORT = request.getParameter("PASSPORT");
                                        } else {
                                            String sENTERPRISE_PREFIX = EscapeUtils.ConvertStringToUnicode(request.getParameter("ENTERPRISE_PREFIX"));
                                            String sPERSONAL_PREFIX = EscapeUtils.ConvertStringToUnicode(request.getParameter("PERSONAL_PREFIX"));
                                            String sENTERPRISE_ID = request.getParameter("ENTERPRISE_ID");
                                            String sPERSONAL_ID = request.getParameter("PERSONAL_ID");
                                            if ("1".equals(hasPaging)) {
                                                sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXCertShare");
                                                sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXCertShare");
                                                sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDCertShare");
                                                sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDCertShare");
                                            }
                                            session.setAttribute("sessENTERPRISE_PREFIXCertShare", sENTERPRISE_PREFIX);
                                            session.setAttribute("sessPERSONAL_PREFIXCertShare", sPERSONAL_PREFIX);
                                            session.setAttribute("sessENTERPRISE_IDCertShare", sENTERPRISE_ID);
                                            session.setAttribute("sessPERSONAL_IDCertShare", sPERSONAL_ID);
                                            isLoadEnterprisePrefix = sENTERPRISE_PREFIX;
                                            isLoadPeronalPrefix = sPERSONAL_PREFIX;
                                            if(!"".equals(sENTERPRISE_ID)){
                                                sEnterpriseCert = sENTERPRISE_PREFIX + sENTERPRISE_ID;
                                            }
                                            if(!"".equals(sPERSONAL_ID)){
                                                sPersonalCert = sPERSONAL_PREFIX + sPERSONAL_ID;
                                            }
                                        }
                                        if ("1".equals(hasPaging)) {
                                            session.setAttribute("SearchSharePagingCertShare", "0");
                                            TAX_CODE = (String) session.getAttribute("sessTAX_CODECertShare");
                                            BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODECertShare");
                                            DECISION = (String) session.getAttribute("sessDECISIONCertShare");
                                            P_ID = (String) session.getAttribute("sessP_IDCertShare");
                                            CCCD = (String) session.getAttribute("sessCCCDCertShare");
                                            PASSPORT = (String) session.getAttribute("sessPASSPORTCertShare");
                                            session.setAttribute("SessParamOnPagingCertShareList", null);
                                        } else {
                                            session.setAttribute("SearchSharePagingCertShare", "1");
                                            session.setAttribute("CountListCertShare", null);
                                        }
                                        session.setAttribute("sessTAX_CODECertShare", TAX_CODE);
                                        session.setAttribute("sessBUDGET_CODECertShare", BUDGET_CODE);
                                        session.setAttribute("sessDECISIONCertShare", DECISION);
                                        session.setAttribute("sessP_IDCertShare", P_ID);
                                        session.setAttribute("sessCCCDCertShare", CCCD);
                                        session.setAttribute("sessPASSPORTCertShare", PASSPORT);
                                        if(!isUIDCollection.equals("1")) {
                                            String[] sUIDResult = new String[2];
                                            CommonReferServlet.collectFieldToUID(EscapeUtils.escapeHtmlSearch(TAX_CODE), EscapeUtils.escapeHtmlSearch(BUDGET_CODE),
                                                EscapeUtils.escapeHtmlSearch(DECISION), EscapeUtils.escapeHtmlSearch(P_ID), EscapeUtils.escapeHtmlSearch(PASSPORT),
                                                EscapeUtils.escapeHtmlSearch(CCCD), sUIDResult);
                                            sEnterpriseCert = sUIDResult[0];
                                            sPersonalCert = sUIDResult[1];
                                        }
                                        int ss = 0;
                                        if ((session.getAttribute("CountListCertShare")) == null) {
                                            ss = db.S_BO_CERTIFICATION_TOTAL_BY_OWNER(SessUserAgentID, SessUserID, sEnterpriseCert, sPersonalCert);
                                            session.setAttribute("CountListCertShare", String.valueOf(ss));
                                        } else {
                                            String sCount = (String) session.getAttribute("CountListCertShare");
                                            ss = Integer.parseInt(sCount);
                                            session.setAttribute("CountListCertShare", String.valueOf(ss));
                                        }
                                        iTotRslts = ss;
                                        if (iTotRslts > 0) {
                                            db.S_BO_CERTIFICATION_LIST_BY_OWNER(SessUserAgentID, SessUserID, sessLanguageGlobal, rsPgin,
                                                    iPagNo, iSwRws, sEnterpriseCert, sPersonalCert);
                                            session.setAttribute("SearchIPageNoPagingCertShare", String.valueOf(iPagNo));
                                            session.setAttribute("SearchISwRwsPagingCertShare", String.valueOf(iSwRws));
                                            strMess = com.convertMoney(ss);
                                            if (rsPgin[0].length > 0) {
                                                status = 1;
                                            } else {
                                                status = 1000;
                                            }
                                        } else {
                                            status = 1000;
                                        }
                                    } catch(Exception e){CommonFunction.LogExceptionServlet(null, "ShareList search new: " + e.getMessage(), e);}
                                } else {
                                    session.setAttribute("RefreshCertShareSessPaging", null);
                                    session.setAttribute("SearchShareStoreRenewCert", null);
                                    session.setAttribute("SearchIPageNoPagingCertShare", null);
                                    session.setAttribute("SearchISwRwsPagingCertShare", null);
                                    session.setAttribute("sessTAX_CODECertShare", null);
                                    session.setAttribute("sessDECISIONCertShare", null);
                                    session.setAttribute("sessDEVICE_UUIDRenewCert", null);
                                    session.setAttribute("sessBUDGET_CODECertShare", null);

                                    session.setAttribute("sessENTERPRISE_PREFIXCertShare", null);
                                    session.setAttribute("sessPERSONAL_PREFIXCertShare", null);
                                    session.setAttribute("sessENTERPRISE_IDCertShare", null);
                                    session.setAttribute("sessPERSONAL_IDCertShare", null);
                                }
                                String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2>
                                            <i class="fa fa-search"></i> <script>document.write(token_title_search);</script>
                                        </h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="form" method="post" class="form-horizontal">
                                            <input id="idHiddenLoad" name="nameHiddenLoad" value="" type="hidden"/>
                                            <%
                                                if("1".equals(isUIDCollection)){
                                            %>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                    <script>document.write(global_fm_identifier_type);</script>
                                                </label>
                                                <div class="col-sm-7" style="padding-right: 0px;">
                                                    <select name="ENTERPRISE_PREFIX" id="ENTERPRISE_PREFIX" onclick="changeEnterprise(this.value);" class="form-control123" style="text-align: left;">
                                                        <%
                                                            PREFIX_UUID[][] rsPrefix;
                                                            rsPrefix = new PREFIX_UUID[1][];
                                                            dbTwo.S_BO_PREFIX_UUID_COMBOBOX("ENTERPRISE", sessLanguageGlobal, rsPrefix);
                                                            if (rsPrefix[0].length > 0) {
                                                                for (PREFIX_UUID temp1 : rsPrefix[0]) {
                                                                    if("".equals(isLoadEnterprisePrefix)){
                                                                        isLoadEnterprisePrefix = temp1.PREFIX_DB;
                                                                    }
                                                        %>
                                                        <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessENTERPRISE_PREFIXCertShare") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessENTERPRISE_PREFIXCertShare").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" id="idLblTooltipEnterprise" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_enter);</script> <%= dbTwo.GET_PREFIX_UUID_TOOLTIP("ENTERPRISE", sessLanguageGlobal, isLoadEnterprisePrefix) %>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="ENTERPRISE_ID" maxlength="22" value="<%= session.getAttribute("sessENTERPRISE_IDCertShare") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessENTERPRISE_IDCertShare").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                                <script>
                                                    function changeEnterprise() {
                                                        $("#idLblTooltipEnterprise").text(global_fm_enter + $("#ENTERPRISE_PREFIX option:selected").text());
                                                    }
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                    <script>document.write(global_fm_document_type);</script>
                                                </label>
                                                <div class="col-sm-7" style="padding-right: 0px;">
                                                    <select name="PERSONAL_PREFIX" id="PERSONAL_PREFIX" class="form-control123" onclick="changePersonal();" style="text-align: left;">
                                                        <%
                                                            rsPrefix = new PREFIX_UUID[1][];
                                                            dbTwo.S_BO_PREFIX_UUID_COMBOBOX("PERSONAL", sessLanguageGlobal, rsPrefix);
                                                            if (rsPrefix[0].length > 0) {
                                                                for (PREFIX_UUID temp1 : rsPrefix[0]) {
                                                                    if("".equals(isLoadPeronalPrefix)){
                                                                        isLoadPeronalPrefix = temp1.PREFIX_DB;
                                                                    }
                                                        %>
                                                        <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessPERSONAL_PREFIXCertShare") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessPERSONAL_PREFIXCertShare").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" id="idLblTooltipPersonal" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_enter);</script> <%= dbTwo.GET_PREFIX_UUID_TOOLTIP("PERSONAL", sessLanguageGlobal, isLoadPeronalPrefix) %>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="PERSONAL_ID" maxlength="22" value="<%= session.getAttribute("sessPERSONAL_IDCertShare") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_IDCertShare").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                                <script>
                                                    function changePersonal()
                                                    {
                                                        $("#idLblTooltipPersonal").text(global_fm_enter + $("#PERSONAL_PREFIX option:selected").text());
                                                    }
                                                </script>
                                            </div>
                                            <%
                                                } else {
                                            %>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 4px;">
                                                            <script>document.write(global_fm_MST);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="TAX_CODE" maxlength="45" value="<%= session.getAttribute("sessTAX_CODECertShare") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessTAX_CODECertShare").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 4px;">
                                                            <script>document.write(global_fm_MNS);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="BUDGET_CODE" maxlength="45" value="<%= session.getAttribute("sessBUDGET_CODECertShare") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessBUDGET_CODECertShare").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 4px;">
                                                            <script>document.write(global_fm_decision);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="DECISION" maxlength="45" value="<%= session.getAttribute("sessDECISIONCertShare") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessDECISIONCertShare").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 4px;">
                                                            <script>document.write(global_fm_CMND);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="P_ID" maxlength="32" value="<%= session.getAttribute("sessP_IDCertShare") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessP_IDCertShare").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 4px;">
                                                            <script>document.write(global_fm_CitizenId);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="CCCD" maxlength="32" value="<%= session.getAttribute("sessCCCDCertShare") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCCCDCertShare").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                       <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 4px;">
                                                            <script>document.write(global_fm_HC);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="PASSPORT" maxlength="32" value="<%= session.getAttribute("sessPASSPORTCertShare") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPASSPORTCertShare").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <% } %>
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
                                                <script>document.write(global_label_grid_sum);</script><%= strMess%>
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
                                                <th style="width: 120px;"><script>document.write(global_fm_grid_company);</script></th>
                                                <th style="width: 100px;"><script>document.write(global_fm_enterprise_id);</script></th>
                                                <th style="width: 120px;"><script>document.write(global_fm_grid_personal);</script></th>
                                                <th style="width: 100px;"><script>document.write(global_fm_personal_id);</script></th>
                                                <th style="width: 120px; display: <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) ? "none" : ""%>"><script>document.write(global_fm_grid_domain);</script></th>
                                                <th style="width: 120px; display: <%=!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) ? "none" : ""%>"><script>document.write(global_fm_status_profile);</script></th>
                                                <th><script>document.write(global_fm_Method);</script></th>
                                                <th><script>document.write(global_fm_duration_cts);</script></th>
                                                <th><script>document.write(global_fm_certtype);</script></th>
                                                <th style="width: 80px;"><script>document.write(global_fm_Status_cert);</script></th>
                                                <%
                                                    if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_USER) || SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
                                                %>
                                                <th><script>document.write(global_fm_Status_request);</script></th>
                                                <th><script>document.write(cert_fm_type_request);</script></th>
                                                <%
                                                    }
                                                %>
                                                <th style="display: <%=(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) && !SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT) ? "none" : ""%>"><script>document.write(global_fm_Branch);</script></th>
                                                <th><script>document.write(global_fm_date_create);</script></th>
                                                <th style="width: 150px;"><script>document.write(global_fm_action);</script></th>
                                                </thead>
                                                <tbody>
                                                    <%
                                                        if (iPaNoSS > 1) {
                                                            j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                        }
                                                        session.setAttribute("RefreshCertShareSessNumberPaging", String.valueOf(iPaNoSS));
                                                        if (rsPgin[0].length > 0) {
                                                            for (CERTIFICATION temp1 : rsPgin[0]) {
                                                                String strID = String.valueOf(temp1.ID);
                                                    %>
                                                    <tr>
                                                        <td style="text-align: center;"><%= com.convertMoney(j)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.COMPANY_NAME)%></td>
                                                        <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= temp1.ENTERPRISE_ID_REMARK%>"><%= temp1.ENTERPRISE_ID%></a></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME)%></td>
                                                        <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= temp1.PERSONAL_ID_REMARK%>"><%= temp1.PERSONAL_ID%></a></td>
                                                        <td style="display: <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) ? "none" : ""%>"><%= EscapeUtils.CheckTextNull(temp1.DOMAIN_NAME)%></td>
                                                        <td style="display: <%=!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) ? "none" : ""%>"><%= temp1.FILE_MANAGER_STATE_DESC%></td>
                                                        <td><%= temp1.PKI_FORMFACTOR_DESC%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PROFILE_NAME)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PURPOSE_DESC)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_STATE_DESC)%></td>
                                                        <%
                                                            if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_USER) || SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
                                                        %>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_ATTR_STATE_DESC)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.SERVICE_TYPE_DESC)%></td>
                                                        <%
                                                            }
                                                        %>
                                                        <td style="display: <%=(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) && !SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT) ? "none" : ""%>"><%= EscapeUtils.CheckTextNull(temp1.BRANCH_DESC)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_DT)%></td>
                                                        <td>
                                                            <%
                                                                if(temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_DECLINED)
                                                                {
                                                            %>
                                                            <span><a style="cursor: pointer;" onclick="popupBuyCertMore('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_buymore);</script></a></span>
                                                            <%
                                                                }
                                                            %>
                                                            <%
                                                                if(temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED
                                                                    || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_EXPIRED)
                                                                {
                                                                    if(!EscapeUtils.CheckTextNull(temp1.TOKEN_SN).equals(Definitions.CONFIG_TOKEN_SN_LOST))
                                                                    {
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_RENEWAL,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                        {
                                                            %>
                                                            <span><a style="cursor: pointer;" onclick="popupRenewCert('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_renew);</script></a></span>
                                                            <%
                                                                        }
                                                                    }
                                                                }
                                                            %>
                                                            <%
                                                                if(temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED)
                                                                {
                                                                    if(!EscapeUtils.CheckTextNull(temp1.TOKEN_SN).equals(Definitions.CONFIG_TOKEN_SN_LOST))
                                                                    {
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_CHANGE_INFO,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                        {
                                                            %>
                                                            <span><a style="cursor: pointer;" onclick="popupChangeCert('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_changeinfo);</script></a></span>
                                                            <%
                                                                        }
                                                                    }
                                                                }
                                                            %>
                                                            <%
                                                                if(temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED)
                                                                {
                                                                    if(CommonFunction.checkHardTokenIDEnabled(temp1.PKI_FORMFACTOR_ID) == true)
                                                                    {
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_REISSUE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                        {
                                                            %>
                                                            <span><a style="cursor: pointer;" onclick="popupReIssueCert('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_reissue);</script></a></span>
                                                            <%
                                                                        }
                                                                    }
                                                                }
                                                            %>
                                                        </td>
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
                    <script>
                        $(document).ready(function () {
                            if('<%= strExpandFilter%>' === "1")
                            {
                                popupViewCSRSList();
                            }
                            if(localStorage.getItem("EDIT_RENEWCERT_DETAIL") !== null && localStorage.getItem("EDIT_RENEWCERT_DETAIL") !== "null")
                            {
                                var vIDEDIT_DETAIL = localStorage.getItem("EDIT_RENEWCERT_DETAIL");
                                localStorage.setItem("EDIT_RENEWCERT_DETAIL", null);
                                popupDetailCert(vIDEDIT_DETAIL);
                            }
                        });
                    </script>
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
        <!-- Modal Registration Print -->
        <div id="myModalRegisterPrint" class="modal fade" role="dialog">
            <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                 left: 0; height: 100%;" class="loading-gifRegisterPrint">
                <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
            </div>
            <div class="modal-dialog modal-800" id="myDialogRegisterPrint">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div id="contentRegisterPrint"></div>
                    </div>
                </div>
            </div>
        </div>
        <script>
            function popupPrintHandoverList(id)
            {
                $('#myModalRegisterPrint').modal('show');
                $('#contentRegisterPrint').empty();
                $('#contentRegisterPrint').load('PrintHandover.jsp', {id:id}, function () {
                });
                $(".loading-gifRegisterPrint").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
            function ShowDialogList(id, isEnterprise, idRegis)
            {
                $('#myModalRegisterPrint').modal('show');
                $('#contentRegisterPrint').empty();
                localStorage.setItem("sessCertToPrint", "1");
                if(idRegis === "1")
                {
                    if(isEnterprise === "1")
                    {
                        localStorage.setItem("PrintRegisterPersonal", null);
                        localStorage.setItem("PrintRegisterBusiness", id);
                        $('#contentRegisterPrint').load('PrintRegisterBusiness.jsp', {id:id}, function () {
//                            goToByScroll("contentRegisterPrint");
                        });
                    }
                    else {
                        localStorage.setItem("PrintRegisterPersonal", id);
                        localStorage.setItem("PrintRegisterBusiness", null);
                        $('#contentRegisterPrint').load('PrintRegisterPersonal.jsp', {id:id}, function () {
//                            goToByScroll("contentEdit");
                        });
                    }
                }
                else {
                    if(isEnterprise === "1")
                    {
                        localStorage.setItem("PrintRenewPersonal", null);
                        localStorage.setItem("PrintRenewBusiness", id);
                        $('#contentRegisterPrint').load('PrintRenewBusiness.jsp', {id:id}, function () {
//                            goToByScroll("contentEdit");
                        });
                    }
                    else {
                        localStorage.setItem("PrintRenewPersonal", id);
                        localStorage.setItem("PrintRenewBusiness", null);
                        $('#contentRegisterPrint').load('PrintRenewPersonal.jsp', {id:id}, function () {
//                            goToByScroll("contentEdit");
                        });
                    }
                }
                $(".loading-gifRegisterPrint").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
            function CloseDialog()
            {
                $('#myModalRegisterPrint').modal('hide');
                $(".loading-gifRegisterPrint").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
        </script>
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