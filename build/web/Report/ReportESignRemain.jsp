<%-- 
    Document   : ReportESignRemain
    Created on : Aug 5, 2022, 3:27:22 PM
    Author     : vanth
--%>

<%@page import="java.net.URLEncoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<!DOCTYPE html>
<%    String strAlertAllTimeEffective = "0";
      String strAlertAllTimeExpire = "0";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <%
            Config conf = new Config();
            String sNewRefreshJS = conf.GetPropertybyCode(Definitions.CONFIG_JS_REFRESH_STRING_RANDOM);
        %>
        <script src="../js/Language.js?t=<%=sNewRefreshJS%>"></script>
        <script src="../js/process_javajs.js?t=<%=sNewRefreshJS%>"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type='text/javascript' src='../js/date.moment.js'></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <title></title>
        <script language="javascript">
            document.title = esignremain_title_list;
            changeFavicon("../");
            $(document).ready(function () {
                localStorage.setItem("LOCAL_PARAM_BRANCHLIST", null);
                $('#effectiveFrom').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
                $('#effectiveTo').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
                $('#expireFrom').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
                $('#expireTo').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
                $('.loading-gif').hide();
                $('#idCheck').removeClass("js-switch");
                $('#idCheck').addClass("js-switch");
            });
            function searchForm(id)
            {
                var idEffectiveFrom = document.myform.effectiveFrom.value;
                var idEffectiveTo = document.myform.effectiveTo.value;
                var idExpireFrom = document.myform.expireFrom.value;
                var idExpireTo = document.myform.expireTo.value;
                if (!JSCheckDateDDMMYYYYSlash(idEffectiveFrom)) {
                    funErrorAlert(global_fm_FromDate + global_error_invalid);
                    return false;
                }
                if (!JSCheckDateDDMMYYYYSlash(idEffectiveTo)) {
                    funErrorAlert(global_fm_ToDate + global_error_invalid);
                    return false;
                }
                if (parseDate(idEffectiveTo).getTime() < parseDate(idEffectiveFrom).getTime()) {
                    funErrorAlert(global_check_datesearch);
                    return false;
                }
                if (!JSCheckDateDDMMYYYYSlash(idExpireFrom))
                {
                    funErrorAlert(global_fm_FromDate + global_error_invalid);
                    return false;
                }
                if (!JSCheckDateDDMMYYYYSlash(idExpireTo))
                {
                    funErrorAlert(global_fm_ToDate + global_error_invalid);
                    return false;
                }
                if (parseDate(idExpireTo).getTime() < parseDate(idExpireFrom).getTime())
                {
                    funErrorAlert(global_check_datesearch);
                    return false;
                }
                document.getElementById("idHiddenLoad").value = JS_STR_GRID_SEARCH_RESET;
                document.getElementById("tempCsrfToken").value = id;
                var f = document.myform;
                f.method = "post";
                f.action = '';
                f.submit();
            }
            function popupEdit(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('NEACSynchView.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
        </script>
        <style>
            .projects th{font-weight: bold;}
            .navbar-right{margin-right: 0;padding-right:10px;}
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.4em 7px 1.4em !important;
                margin: 0 0 1.5em 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
        </style>
    </head>
    <body class="nav-md">
        <%        if ((session.getAttribute("UserID")) != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String sessTreeArrayBranchID = session.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                String sessAgentID = session.getAttribute("SessAgentID").toString().trim();
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
                        document.getElementById("idNameURL").innerHTML = esignremain_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%                            int status = 1000;
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
                                String sDefaulCheckAll = "";
                                String isLoadPeronalPrefix = "";
                                String isLoadEnterprisePrefix = "";
                                String sEnterpriseCert = "";
                                String sPersonalCert = "";
                                try {
                                    CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (session.getAttribute("SessRefreshRemain") != null && session.getAttribute("sessEffectiveFromRemain") != null
                                            && session.getAttribute("sessEffectiveToRemain") != null) {
                                        session.setAttribute("RefreshRemainSessPaging", "1");
                                        session.setAttribute("SearchSharePagingRemain", "0");
                                        statusLoad = 1;
                                        String effectiveFrom = (String) session.getAttribute("sessEffectiveFromRemain");
                                        String effectiveTo = (String) session.getAttribute("sessEffectiveToRemain");
                                        String expireFrom = (String) session.getAttribute("sessExpireFromRemain");
                                        String expireTo = (String) session.getAttribute("sessExpireToRemain");
                                        String CERT_SN = (String) session.getAttribute("sessCERT_SNRemain");
                                        String SIGNING_COUNTER = (String) session.getAttribute("sessSIGNING_COUNTERRemain");
                                        String CERTIFICATION_HASH = (String) session.getAttribute("sessCERTIFICATION_HASHRemain");
                                        String BranchOffice = (String) session.getAttribute("sessBranchOfficeRemain");
                                        strAlertAllTimeEffective = (String) session.getAttribute("AlertAllTimeEffectiveRemain");
                                        strAlertAllTimeExpire = (String) session.getAttribute("AlertAllTimeExpireRemain");
                                        String COMPANY_NAME = (String) session.getAttribute("sessCOMPANY_NAMERemain");
                                        String PERSONAL_NAME = (String) session.getAttribute("sessPERSONAL_NAMERemain");
                                        String sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXRemain");
                                        String sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXRemain");
                                        String sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDRemain");
                                        String sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDRemain");
                                        
                                        session.setAttribute("SessRefreshRemain", null);
                                        session.setAttribute("sessEffectiveFromRemain", effectiveFrom);
                                        session.setAttribute("sessEffectiveToRemain", effectiveTo);
                                        session.setAttribute("sessExpireFromRemain", expireFrom);
                                        session.setAttribute("sessExpireToRemain", expireTo);
                                        session.setAttribute("sessCERT_SNRemain", CERT_SN);
                                        session.setAttribute("sessSIGNING_COUNTERRemain", SIGNING_COUNTER);
                                        session.setAttribute("sessCERTIFICATION_HASHRemain", CERTIFICATION_HASH);
                                        session.setAttribute("sessBranchOfficeRemain", BranchOffice);
                                        session.setAttribute("sessCOMPANY_NAMERemain", COMPANY_NAME);
                                        session.setAttribute("sessPERSONAL_NAMERemain", PERSONAL_NAME);
                                        session.setAttribute("sessENTERPRISE_PREFIXRemain", sENTERPRISE_PREFIX);
                                        session.setAttribute("sessPERSONAL_PREFIXRemain", sPERSONAL_PREFIX);
                                        session.setAttribute("sessENTERPRISE_IDRemain", sENTERPRISE_ID);
                                        session.setAttribute("sessPERSONAL_IDRemain", sPERSONAL_ID);
                                        session.setAttribute("AlertAllTimeEffectiveRemain", strAlertAllTimeEffective);
                                        session.setAttribute("AlertAllTimeExpireRemain", strAlertAllTimeExpire);
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                            BranchOffice = "";
                                        }
                                        isLoadEnterprisePrefix = sENTERPRISE_PREFIX;
                                        isLoadPeronalPrefix = sPERSONAL_PREFIX;
                                        if(!"".equals(sENTERPRISE_ID)){
                                            sEnterpriseCert = sENTERPRISE_PREFIX + "%"+sENTERPRISE_ID+"%";
                                        }
                                        if(!"".equals(sPERSONAL_ID)){
                                            sPersonalCert = sPERSONAL_PREFIX + "%"+sPERSONAL_ID+"%";
                                        }
                                        if ("1".equals(strAlertAllTimeEffective)) {
                                            effectiveFrom = "";
                                            effectiveTo = "";
                                        }
                                        if ("1".equals(strAlertAllTimeExpire)) {
                                            expireFrom = "";
                                            expireTo = "";
                                        }
                                        int ss = 0;
                                        if ((session.getAttribute("CountListRemain")) == null) {
                                            ss = dbTwo.S_BO_CERTIFICATION_RSSP_TOTAL(CERT_SN, CERTIFICATION_HASH, SIGNING_COUNTER,
                                                COMPANY_NAME, PERSONAL_NAME, sEnterpriseCert, sPersonalCert, EscapeUtils.escapeHtmlSearch(effectiveFrom),
                                                EscapeUtils.escapeHtmlSearch(effectiveTo), EscapeUtils.escapeHtmlSearch(expireFrom),
                                                EscapeUtils.escapeHtmlSearch(expireTo), BranchOffice, sessTreeArrayBranchID);
                                            session.setAttribute("CountListRemain", String.valueOf(ss));
                                        } else {
                                            String sCount = (String) session.getAttribute("CountListRemain");
                                            ss = Integer.parseInt(sCount);
                                            session.setAttribute("CountListRemain", String.valueOf(ss));
                                        }
                                        if (session.getAttribute("SearchIPageNoPagingRemain") != null) {
                                            String sPage = (String) session.getAttribute("SearchIPageNoPagingRemain");
                                            iPagNo = Integer.parseInt(sPage);
                                        }
                                        if (session.getAttribute("SearchISwRwsPagingRemain") != null) {
                                            String sSumPage = (String) session.getAttribute("SearchISwRwsPagingRemain");
                                            iSwRws = Integer.parseInt(sSumPage);
                                        }
                                        if (session.getAttribute("SessRefreshBranchNumberRemain") != null) {
                                            String sNoPage = (String) session.getAttribute("SessRefreshBranchNumberRemain");
                                            iPaNoSS = Integer.parseInt(sNoPage);
                                        }
                                        session.setAttribute("SearchIPageNoPagingRemain", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingRemain", String.valueOf(iSwRws));
                                        if (ss > 0) {
                                            dbTwo.S_BO_CERTIFICATION_RSSP_LIST(CERT_SN, CERTIFICATION_HASH, SIGNING_COUNTER,
                                                COMPANY_NAME, PERSONAL_NAME, sEnterpriseCert, sPersonalCert, EscapeUtils.escapeHtmlSearch(effectiveFrom),
                                                EscapeUtils.escapeHtmlSearch(effectiveTo), EscapeUtils.escapeHtmlSearch(expireFrom),
                                                EscapeUtils.escapeHtmlSearch(expireTo), BranchOffice, sessTreeArrayBranchID,
                                                Integer.parseInt(sessLanguageGlobal), iPagNo, iSwRws, rsPgin);
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
                                        session.setAttribute("RefreshRemainSessPaging", null);
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("SearchShareStoreRemain", null);
                                            session.setAttribute("SearchIPageNoPagingRemain", null);
                                            session.setAttribute("SearchISwRwsPagingRemain", null);
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
                                    String effectiveFrom = request.getParameter("effectiveFrom");
                                    String effectiveTo = request.getParameter("effectiveTo");
                                    String expireFrom = request.getParameter("expireFrom");
                                    String expireTo = request.getParameter("expireTo");

                                    String CERT_SN = EscapeUtils.ConvertStringToUnicode(request.getParameter("CERT_SN"));
                                    String SIGNING_COUNTER = request.getParameter("SIGNING_COUNTER");
                                    String CERTIFICATION_HASH = EscapeUtils.ConvertStringToUnicode(request.getParameter("CERTIFICATION_HASH"));
                                    String PERSONAL_NAME = EscapeUtils.ConvertStringToUnicode(request.getParameter("PERSONAL_NAME"));
                                    String COMPANY_NAME = EscapeUtils.ConvertStringToUnicode(request.getParameter("COMPANY_NAME"));
                                    
                                    String BranchOffice = request.getParameter("BranchOffice");
                                    Boolean nameCheckEffective = Boolean.valueOf(request.getParameter("nameCheckEffective") != null);
                                    Boolean nameCheckExpire = Boolean.valueOf(request.getParameter("nameCheckExpire") != null);
                                    if (nameCheckEffective == false) {
                                        effectiveFrom = "";
                                        effectiveTo = "";
                                        strAlertAllTimeEffective = "1";
                                    }
                                    if (nameCheckExpire == false) {
                                        expireFrom = "";
                                        expireTo = "";
                                        strAlertAllTimeExpire = "1";
                                    }
                                    String sENTERPRISE_PREFIX = EscapeUtils.ConvertStringToUnicode(request.getParameter("ENTERPRISE_PREFIX"));
                                    String sPERSONAL_PREFIX = EscapeUtils.ConvertStringToUnicode(request.getParameter("PERSONAL_PREFIX"));
                                    String sENTERPRISE_ID = request.getParameter("ENTERPRISE_ID");
                                    String sPERSONAL_ID = request.getParameter("PERSONAL_ID");
                                    if ("1".equals(hasPaging)) {
                                        session.setAttribute("SearchSharePagingRemain", "0");
                                        effectiveFrom = (String) session.getAttribute("sessEffectiveFromRemain");
                                        effectiveTo = (String) session.getAttribute("sessEffectiveToRemain");
                                        expireFrom = (String) session.getAttribute("sessExpireFromRemain");
                                        expireTo = (String) session.getAttribute("sessExpireToRemain");
                                        CERT_SN = (String) session.getAttribute("sessCERT_SNRemain");
                                        SIGNING_COUNTER = (String) session.getAttribute("sessSIGNING_COUNTERRemain");
                                        CERTIFICATION_HASH = (String) session.getAttribute("sessCERTIFICATION_HASHRemain");
                                        BranchOffice = (String) session.getAttribute("sessBranchOfficeRemain");
                                        strAlertAllTimeEffective = (String) session.getAttribute("AlertAllTimeEffectiveRemain");
                                        strAlertAllTimeExpire = (String) session.getAttribute("AlertAllTimeExpireRemain");
                                        COMPANY_NAME = (String) session.getAttribute("sessCOMPANY_NAMERemain");
                                        PERSONAL_NAME = (String) session.getAttribute("sessPERSONAL_NAMERemain");
                                        sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXRemain");
                                        sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXRemain");
                                        sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDRemain");
                                        sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDRemain");
                                        session.setAttribute("SessParamOnPagingCertList", null);
                                    } else {
                                        session.setAttribute("SearchSharePagingRemain", "1");
                                        session.setAttribute("CountListRemain", null);
                                    }
                                    session.setAttribute("sessEffectiveFromRemain", effectiveFrom);
                                    session.setAttribute("sessEffectiveToRemain", effectiveTo);
                                    session.setAttribute("sessExpireFromRemain", expireFrom);
                                    session.setAttribute("sessExpireToRemain", expireTo);
                                    session.setAttribute("sessCERT_SNRemain", CERT_SN);
                                    session.setAttribute("sessSIGNING_COUNTERRemain", SIGNING_COUNTER);
                                    session.setAttribute("sessCERTIFICATION_HASHRemain", CERTIFICATION_HASH);
                                    session.setAttribute("sessBranchOfficeRemain", BranchOffice);
                                    session.setAttribute("sessCOMPANY_NAMERemain", COMPANY_NAME);
                                    session.setAttribute("sessPERSONAL_NAMERemain", PERSONAL_NAME);
                                    session.setAttribute("sessENTERPRISE_PREFIXRemain", sENTERPRISE_PREFIX);
                                    session.setAttribute("sessPERSONAL_PREFIXRemain", sPERSONAL_PREFIX);
                                    session.setAttribute("sessENTERPRISE_IDRemain", sENTERPRISE_ID);
                                    session.setAttribute("sessPERSONAL_IDRemain", sPERSONAL_ID);
                                    session.setAttribute("AlertAllTimeEffectiveRemain", strAlertAllTimeEffective);
                                    session.setAttribute("AlertAllTimeExpireRemain", strAlertAllTimeExpire);
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                        BranchOffice = "";
                                    }
                                    isLoadEnterprisePrefix = sENTERPRISE_PREFIX;
                                    isLoadPeronalPrefix = sPERSONAL_PREFIX;
                                    if(!"".equals(sENTERPRISE_ID)){
                                        sEnterpriseCert = sENTERPRISE_PREFIX + "%"+sENTERPRISE_ID+"%";
                                    }
                                    if(!"".equals(sPERSONAL_ID)){
                                        sPersonalCert = sPERSONAL_PREFIX + "%"+sPERSONAL_ID+"%";
                                    }
                                    int ss = 0;
                                    if ((session.getAttribute("CountListRemain")) == null) {
                                        ss = dbTwo.S_BO_CERTIFICATION_RSSP_TOTAL(CERT_SN, CERTIFICATION_HASH, SIGNING_COUNTER,
                                            COMPANY_NAME, PERSONAL_NAME, sEnterpriseCert, sPersonalCert, EscapeUtils.escapeHtmlSearch(effectiveFrom),
                                            EscapeUtils.escapeHtmlSearch(effectiveTo), EscapeUtils.escapeHtmlSearch(expireFrom),
                                            EscapeUtils.escapeHtmlSearch(expireTo), BranchOffice, sessTreeArrayBranchID);
                                        session.setAttribute("CountListRemain", String.valueOf(ss));
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListRemain");
                                        ss = Integer.parseInt(sCount);
                                        session.setAttribute("CountListRemain", String.valueOf(ss));
                                    }
                                    iTotRslts = ss;
                                    if (iTotRslts > 0) {
                                        dbTwo.S_BO_CERTIFICATION_RSSP_LIST(CERT_SN, CERTIFICATION_HASH, SIGNING_COUNTER,
                                            COMPANY_NAME, PERSONAL_NAME, sEnterpriseCert, sPersonalCert, EscapeUtils.escapeHtmlSearch(effectiveFrom),
                                            EscapeUtils.escapeHtmlSearch(effectiveTo), EscapeUtils.escapeHtmlSearch(expireFrom),
                                            EscapeUtils.escapeHtmlSearch(expireTo), BranchOffice, sessTreeArrayBranchID,
                                            Integer.parseInt(sessLanguageGlobal), iPagNo, iSwRws, rsPgin);
                                        session.setAttribute("SearchIPageNoPagingRemain", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingRemain", String.valueOf(iSwRws));
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
                                    session.setAttribute("RefreshRemainSessPaging", null);
                                    session.setAttribute("SearchShareStoreRemain", null);
                                    session.setAttribute("SearchIPageNoPagingRemain", null);
                                    session.setAttribute("SearchISwRwsPagingRemain", null);
                                    session.setAttribute("sessEffectiveFromRemain", null);
                                    session.setAttribute("sessEffectiveToRemain", null);
                                    session.setAttribute("sessExpireFromRemain", null);
                                    session.setAttribute("sessExpireToRemain", null);
                                    session.setAttribute("sessCERT_SNRemain", null);
                                    session.setAttribute("sessSIGNING_COUNTERRemain", null);
                                    session.setAttribute("sessCERTIFICATION_HASHRemain", null);
                                    session.setAttribute("sessCOMPANY_NAMERemain", null);
                                    session.setAttribute("sessPERSONAL_NAMERemain", null);
                                    session.setAttribute("sessENTERPRISE_PREFIXRemain", null);
                                    session.setAttribute("sessPERSONAL_PREFIXRemain", null);
                                    session.setAttribute("sessENTERPRISE_IDRemain", null);
                                    session.setAttribute("sessPERSONAL_IDRemain", null);
                                    session.setAttribute("sessBranchOfficeRemain", "All");
                                    session.setAttribute("AlertAllTimeEffectiveRemain", null);
                                    session.setAttribute("AlertAllTimeExpireRemain", null);
                                }
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="myform" method="post" class="form-horizontal">
                                            <input id="idHiddenLoad" name="nameHiddenLoad" value="" type="hidden"/>
                                            <script>
                                                $(document).ready(function () {
                                                    document.getElementById("effectiveFrom").disabled = false;
                                                    document.getElementById("effectiveTo").disabled = false;
                                                    document.getElementById("expireFrom").disabled = true;
                                                    document.getElementById("expireTo").disabled = true;
                                                    $('#idCheckExpire').prop('checked', false);
                                                });
                                                function checkChangeEffective() {
                                                    if ($("#idCheckEffective").is(':checked')) {
                                                        document.getElementById("effectiveFrom").disabled = false;
                                                        document.getElementById("effectiveTo").disabled = false;
                                                        document.getElementById("expireFrom").disabled = true;
                                                        document.getElementById("expireTo").disabled = true;
                                                        $('#idCheckExpire').prop('checked', false);
                                                    }
                                                    else
                                                    {
                                                        document.getElementById("effectiveFrom").disabled = true;
                                                        document.getElementById("effectiveTo").disabled = true;
                                                    }
                                                }
                                                function checkChangeExpire() {
                                                    if ($("#idCheckExpire").is(':checked')) {
                                                        document.getElementById("expireFrom").disabled = false;
                                                        document.getElementById("expireTo").disabled = false;
                                                        document.getElementById("effectiveFrom").disabled = true;
                                                        document.getElementById("effectiveTo").disabled = true;
                                                        $('#idCheckEffective').prop('checked', false);
                                                    }
                                                    else
                                                    {
                                                        document.getElementById("expireFrom").disabled = true;
                                                        document.getElementById("expireTo").disabled = true;
                                                    }
                                                }
                                            </script>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_From_effective);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                                            <input type="Text" id="effectiveFrom" name="effectiveFrom" <%= session.getAttribute("AlertAllTimeEffectiveRemain") != null
                                                                    && "1".equals(session.getAttribute("AlertAllTimeEffectiveRemain").toString()) ? "disabled" : ""%>
                                                                   value="<%= session.getAttribute("sessEffectiveFromRemain") != null
                                                                           && !"1".equals(session.getAttribute("AlertAllTimeEffectiveRemain").toString())
                                                                                ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessEffectiveFromRemain").toString())
                                                                                : com.ConvertMonthSub(3)%>" maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_To_effective);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                                            <input type="Text" id="effectiveTo" name="effectiveTo" <%= session.getAttribute("AlertAllTimeEffectiveRemain") != null
                                                                    && "1".equals(session.getAttribute("AlertAllTimeEffectiveRemain").toString()) ? "disabled" : ""%>
                                                                   value="<%= session.getAttribute("sessEffectiveToRemain") != null
                                                                           && !"1".equals(session.getAttribute("AlertAllTimeEffectiveRemain").toString())
                                                                                   ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessEffectiveToRemain").toString()) : com.ConvertMonthSub(0)%>"
                                                                   maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <div class="col-sm-5" style="padding-right: 0px;text-align: right;">
                                                            <label class="switch" for="idCheckEffective">
                                                                <input type="checkbox" name="nameCheckEffective" id="idCheckEffective" onchange="checkChangeEffective();" <%= session.getAttribute("AlertAllTimeEffectiveRemain") != null && "1".equals(session.getAttribute("AlertAllTimeEffectiveRemain").toString()) ? "" : "checked"%>/>
                                                                <div class="slider round"></div>
                                                            </label>
                                                        </div>
                                                        <label class="control-label col-sm-7" style="color: #000000; font-weight: bold;text-align: left;"><script>document.write(global_fm_check_date);</script></label>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_From_expire);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                                            <input type="Text" id="expireFrom" name="expireFrom" <%= session.getAttribute("AlertAllTimeExpireRemain") != null
                                                                && "1".equals(session.getAttribute("AlertAllTimeExpireRemain").toString()) ? "disabled" : ""%>
                                                               value="<%= session.getAttribute("sessExpireFromRemain") != null
                                                                && !"1".equals(session.getAttribute("AlertAllTimeExpireRemain").toString())
                                                                     ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessExpireFromRemain").toString())
                                                                     : com.ConvertMonthSub(3)%>" maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_To_expire);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                                            <input type="Text" id="expireTo" name="expireTo" <%= session.getAttribute("AlertAllTimeExpireRemain") != null
                                                                && "1".equals(session.getAttribute("AlertAllTimeExpireRemain").toString()) ? "disabled" : ""%>
                                                               value="<%= session.getAttribute("sessExpireToRemain") != null
                                                                       && !"1".equals(session.getAttribute("AlertAllTimeExpireRemain").toString())
                                                                               ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessExpireToRemain").toString()) : com.ConvertMonthSub(0)%>"
                                                               maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <div class="col-sm-5" style="padding-right: 0px;text-align: right;">
                                                            <label class="switch" for="idCheckExpire">
                                                                <input type="checkbox" name="nameCheckExpire" id="idCheckExpire" onchange="checkChangeExpire();" <%= session.getAttribute("AlertAllTimeExpireRemain") != null && "1".equals(session.getAttribute("AlertAllTimeExpireRemain").toString()) ? "" : "checked"%>/>
                                                                <div class="slider round"></div>
                                                            </label>
                                                        </div>
                                                        <label class="control-label col-sm-7" style="color: #000000; font-weight: bold;text-align: left;"><script>document.write(global_fm_check_date);</script></label>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                            <script>document.write(global_fm_grid_company);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="COMPANY_NAME" maxlength="150" value="<%= session.getAttribute("sessCOMPANY_NAMERemain") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCOMPANY_NAMERemain").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
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
                                                            <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessENTERPRISE_PREFIXRemain") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessENTERPRISE_PREFIXRemain").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                            <%
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" id="idLblTooltipEnterprise" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                            <script>document.write(global_fm_enter);</script> <%= dbTwo.GET_PREFIX_UUID_TOOLTIP("ENTERPRISE", sessLanguageGlobal, isLoadEnterprisePrefix) %>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="ENTERPRISE_ID" maxlength="45" value="<%= session.getAttribute("sessENTERPRISE_IDRemain") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessENTERPRISE_IDRemain").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                    <script>
                                                        function changeEnterprise() {
                                                            $("#idLblTooltipEnterprise").text(global_fm_enter + $("#ENTERPRISE_PREFIX option:selected").text());
                                                        }
                                                    </script>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                            <script>document.write(global_fm_grid_personal);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="PERSONAL_NAME" maxlength="150" value="<%= session.getAttribute("sessPERSONAL_NAMERemain") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_NAMERemain").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
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
                                                            <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessPERSONAL_PREFIXRemain") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessPERSONAL_PREFIXRemain").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                            <%
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" id="idLblTooltipPersonal" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                            <script>document.write(global_fm_enter);</script> <%= dbTwo.GET_PREFIX_UUID_TOOLTIP("PERSONAL", sessLanguageGlobal, isLoadPeronalPrefix) %>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="PERSONAL_ID" maxlength="45" value="<%= session.getAttribute("sessPERSONAL_IDRemain") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_IDRemain").toString()) : ""%>"
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
                                            </div>
                                            
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_serial);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                                            <input type="text" name="CERT_SN" maxlength="<%= Definitions.CONFIG_LENGTH_TOKEN_SN%>" value="<%= session.getAttribute("sessCERT_SNRemain") != null
                                                                    ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCERT_SNRemain").toString()) : ""%>" class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(token_fm_thumbprintcert);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                                            <input type="text" name="CERTIFICATION_HASH" value="<%= session.getAttribute("sessCERTIFICATION_HASHRemain") != null
                                                                    ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCERTIFICATION_HASHRemain").toString()) : ""%>" class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_remainingSigning_agreement);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                                            <input type="text" name="SIGNING_COUNTER" maxlength="<%= Definitions.CONFIG_LENGTH_TOKEN_SN%>" value="<%= session.getAttribute("sessSIGNING_COUNTERRemain") != null
                                                                    ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessSIGNING_COUNTERRemain").toString()) : ""%>" class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                                        
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_Branch);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="BranchOffice" id="idBranchOffice" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessBranchOfficeRemain") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessBranchOfficeRemain").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    try {
                                                                        BRANCH[][] rst = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                                                                        if (rst[0].length > 0) {
                                                                            for (BRANCH temp1 : rst[0]) {
                                                                                if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>"  <%= session.getAttribute("sessBranchOfficeRemain") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeRemain").toString()) ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
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
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group"></div>
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
                                <div class="x_panel">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(esignremain_title_table);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <script>document.write(global_label_grid_sum);</script><%= strMess%>
                                                &nbsp;&nbsp;
                                                <button type="button" class="btn btn-info" onClick="ExportReportList('<%= String.valueOf(iTotRslts)%>', '<%= anticsrf%>');"><script>document.write(global_fm_button_export_csv);</script></button>
                                                <script>
                                                    function ExportReportList(countList, idCSRF)
                                                    {
                                                        $('body').append('<div id="over"></div>');
                                                        $(".loading-gif").show();
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../ExportCSVParam",
                                                            data: {
                                                                idParam: "exportesignclousremainlist",
                                                                countList: countList,
                                                                CsrfToken: idCSRF
                                                            },
                                                            catche: false,
                                                            success: function (html) {
                                                                var arr = sSpace(html).split('#');
                                                                if (arr[0] === "0")
                                                                {
                                                                    var f = document.myform;
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
                                        </style>
                                        <div class="table-responsive">
                                            <table id="mtToken" class="table table-bordered table-striped projects">
                                                <thead>
                                                    <th><script>document.write(global_fm_STT);</script></th>
                                                    <th><script>document.write(token_fm_agent);</script></th>
                                                    <th style="width: 120px;"><script>document.write(global_fm_grid_company);</script></th>
                                                    <th style="width: 100px;"><script>document.write(global_fm_enterprise_id);</script></th>
                                                    <th style="width: 120px;"><script>document.write(global_fm_grid_personal);</script></th>
                                                    <th style="width: 100px;"><script>document.write(global_fm_personal_id);</script></th>
                                                    <th><script>document.write(global_fm_remainingSigning_agreement);</script></th>
                                                    <th><script>document.write(global_fm_valid_cert);</script></th>
                                                    <th><script>document.write(global_fm_Expire_cert);</script></th>
                                                    <th><script>document.write(global_fm_serial);</script></th>
                                                    <th><script>document.write(token_fm_thumbprintcert);</script></th>
                                                </thead>
                                                <tbody>
                                                    <%
                                                        int j = 1;
                                                        if (iPaNoSS > 1) {
                                                            j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                        }
                                                        session.setAttribute("NumPageSessListBranch", String.valueOf(iPaNoSS));
                                                        if (rsPgin[0].length > 0) {
                                                            for (int i = 0; i < rsPgin[0].length; i++) {
//                                                                String strID = String.valueOf(rsPgin[0][i].ID);
                                                    %>
                                                    <tr>
                                                        <td><%= com.convertMoney(j)%></td>
                                                        <td><%= rsPgin[0][i].BRANCH_NAME + " - " + rsPgin[0][i].BRANCH_DESC%></td>
                                                        <td><%= rsPgin[0][i].COMPANY_NAME%></td>
                                                        <td><%= rsPgin[0][i].ENTERPRISE_ID%></td>
                                                        <td><%= rsPgin[0][i].PERSONAL_NAME%></td>
                                                        <td><%= rsPgin[0][i].PERSONAL_ID%></td>
                                                        <td><%= rsPgin[0][i].REMAINING_SIGNING_COUNTER%></td>
                                                        <td><%= rsPgin[0][i].EFFECTIVE_DT%></td>
                                                        <td><%= rsPgin[0][i].EXPIRATION_DT%></td>
                                                        <td><%= rsPgin[0][i].CERTIFICATION_SN%></td>
                                                        <td><%= rsPgin[0][i].CERTIFICATION_HASH%></td>
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
                                                                document.getElementById("idTDPaging").colSpan = document.getElementById('mtToken').rows[0].cells.length;
                                                            });
                                                        </script>
                                                        <td id="idTDPaging" style="text-align: right;">
                                                            <div class="paging_table">
                                                                <%
                                                                    int i = 0;
                                                                    int cPge = 0;
                                                                    if (iTotRslts > iSwRws) {
                                                                        String uriPage = request.getRequestURI();
                                                                        String namePage = uriPage.substring(uriPage.lastIndexOf("/") + 1);
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
                                                                        <%
                                                                        } else if (i <= iTotPags) {
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
                                                                %>&nbsp;&nbsp;
                                                                <b> <script>document.write(global_fm_paging_total);</script><%=com.convertMoney(iTotRslts)%></b>
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
                            if('<%=sDefaulCheckAll%>' === "") {
//                                $("input.group1").attr("disabled", true);
                                $("#checkAll").attr('disabled','disabled');
                            } 
                        });
                    </script>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
            <script src="../style/jquery.min.js"></script>
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