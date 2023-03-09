<%-- 
    Document   : OwnerList
    Created on : Nov 1, 2019, 5:07:42 PM
    Author     : USER
--%>

<%@page import="java.net.URLEncoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<%  response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
    String strAlertAllTimes = "0";
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
            document.title = owner_title_list;
            $(document).ready(function () {
//                reload_js("../style/bootstrap.min.js");
                localStorage.setItem("LOCAL_PARAM_OWNER_LIST", null);
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
                $('#myModalCertDecline').modal({
                    backdrop: 'static',
                    keyboard: true,
                    show: false
                });
            });
            function reload_js(src) {
                $('script[src="' + src + '"]').remove();
            }
            function popupDetail(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $('#contentEdit').load('OwnerView.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    reload_js("../style/bootstrap.min.js");
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function popupDialogChange(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('ChangeOwnerInfo.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    reload_js("../style/bootstrap.min.js");
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function popupDialogDispose(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('OwnerDispose.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    reload_js("../style/bootstrap.min.js");
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function FormDecline(idATTR, idCSRF) {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../ReqApproveDeclineCommon",
                    data: {
                        idParam: 'declinecert',
                        idATTR: idATTR,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            if (arr[2] === "1")
                            {
                                pushNotificationDecline(idBranch, idUser);
                            }
                            funSuccLocalAlert(request_succ_delete);
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        }
                        else if (arr[0] === JS_EX_LOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (arr[0] === JS_EX_ANOTHERLOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else if (arr[0] === JS_EX_WRONG_AGENCY) {
                            RedirectPageLoginNoSess(global_error_wrong_agency);
                        }
                        else if (arr[0] === JS_EX_STATUS)
                        {
                            funErrorAlert(global_error_appove_status);
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
            function searchForm(id)
            {
                var idFromCreateDate = document.form.FromCreateDate.value;
                var idToCreateDate = document.form.ToCreateDate.value;
                if (!JSCheckDateDDMMYYYYSlash(idFromCreateDate))
                {
                    funErrorAlert(global_fm_FromDate + global_error_invalid);
                    return false;
                }
                if (!JSCheckDateDDMMYYYYSlash(idToCreateDate))
                {
                    funErrorAlert(global_fm_ToDate + global_error_invalid);
                    return false;
                }
                if (parseDate(idToCreateDate).getTime() < parseDate(idFromCreateDate).getTime())
                {
                    funErrorAlert(global_check_datesearch);
                    return false;
                }
                else
                {
                    document.getElementById("idHiddenLoad").value = JS_STR_GRID_SEARCH_RESET;
                    document.getElementById("tempCsrfToken").value = id;
                    var f = document.form;
                    f.method = "post";
                    f.action = '';
                    f.submit();
                }
            }
            function popupHideCTSList()
            {
                document.getElementById('idViewCSRList').style.display = 'none';
                document.getElementById('idAHideList').style.display = 'none';
                document.getElementById('idAShowList').style.display = '';
            }
            function popupViewCSRSList()
            {
                document.getElementById('idViewCSRList').style.display = '';
                document.getElementById('idAHideList').style.display = '';
                document.getElementById('idAShowList').style.display = 'none';
            }
            function closeDialogCertDecline()
            {
                $('#myModalCertDecline').modal('hide');
                $(".loading-gifCertDecline").hide();
                $(".loading-gif").hide();
                $('#over').remove();
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
            .x_panel{padding: 10px 10px}
            .col-sm-4{padding-right: 5px;}
        </style>
    </head>
    <body class="nav-md">
        <%
        if (request.getSession(false).getAttribute("sUserID") != null) {
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
            String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
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
                        document.getElementById("idNameURL").innerHTML = owner_title_list;
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
                                String isLoadPeronalPrefix = "";
                                String isLoadEnterprisePrefix = "";
                                try {
                                    CERTIFICATION_OWNER[][] rsPgin = new CERTIFICATION_OWNER[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (session.getAttribute("RefreshOwnerListSess") != null && session.getAttribute("sessFromCreateDateOwnerList") != null
                                            && session.getAttribute("sessToCreateDateOwnerList") != null) {
                                        session.setAttribute("RefreshOwnerListSessPaging", "1");
                                        statusLoad = 1;
                                        String ToCreateDate = (String) session.getAttribute("sessToCreateDateOwnerList");
                                        String FromCreateDate = (String) session.getAttribute("sessFromCreateDateOwnerList");
                                        String CERTIFICATION_OWNER_STATE = (String) session.getAttribute("sessCERTIFICATION_OWNER_STATEOwnerList");
                                        String CERTIFICATION_OWNER_TYPE = (String) session.getAttribute("sessCERTIFICATION_OWNER_TYPEOwnerList");

                                        String EMAIL_CONTRACT_SEARCH = (String) session.getAttribute("sessEMAIL_CONTRACTOwnerList");
                                        String PHONE_CONTRACT_SEARCH = (String) session.getAttribute("sessPHONE_CONTRACTOwnerList");
                                        String PASSPORT = (String) session.getAttribute("sessPASSPORTOwnerList");
                                        String P_ID = (String) session.getAttribute("sessP_IDOwnerList");
                                        String CCCD = (String) session.getAttribute("sessCCCDOwnerList");
                                        
                                        String PERSONAL_NAME = (String) session.getAttribute("sessPERSONAL_NAMEQueueList");
                                        String BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODEOwnerList");
                                        String DECISION = (String) session.getAttribute("sessDECISIONOwnerList");
                                        String TAX_CODE = (String) session.getAttribute("sessTAX_CODEOwnerList");
                                        String COMPANY_NAME = (String) session.getAttribute("sessCOMPANY_NAMEQueueList");
                                        
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSOwnerList");
                                        session.setAttribute("RefreshOwnerListSess", null);
                                        session.setAttribute("sessFromCreateDateOwnerList", FromCreateDate);
                                        session.setAttribute("sessToCreateDateOwnerList", ToCreateDate);
                                        
                                        session.setAttribute("sessPERSONAL_NAMEQueueList", PERSONAL_NAME);
                                        session.setAttribute("sessCOMPANY_NAMEAQueueList", COMPANY_NAME);
                                        session.setAttribute("sessCERTIFICATION_OWNER_STATEOwnerList", CERTIFICATION_OWNER_STATE);
                                        session.setAttribute("sessCERTIFICATION_OWNER_TYPEOwnerList", CERTIFICATION_OWNER_TYPE);
                                        session.setAttribute("sessTAX_CODEOwnerList", TAX_CODE);
                                        session.setAttribute("sessBUDGET_CODEOwnerList", BUDGET_CODE);
                                        session.setAttribute("sessDECISIONOwnerList", DECISION);
                                        session.setAttribute("sessP_IDOwnerList", P_ID);
                                        session.setAttribute("sessCCCDOwnerList", CCCD);
                                        session.setAttribute("sessPASSPORTOwnerList", PASSPORT);
                                        session.setAttribute("sessEMAIL_CONTRACTOwnerList", EMAIL_CONTRACT_SEARCH);
                                        session.setAttribute("sessPHONE_CONTRACTOwnerList", PHONE_CONTRACT_SEARCH);
                                        session.setAttribute("AlertAllTimeSOwnerList", strAlertAllTimes);
                                        String sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDOwnerList");
                                        String sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDOwnerList");
                                        String sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXOwnerList");
                                        String sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXOwnerList");
                                        session.setAttribute("sessENTERPRISE_IDOwnerList", sENTERPRISE_ID);
                                        session.setAttribute("sessPERSONAL_IDOwnerList", sPERSONAL_ID);
                                        session.setAttribute("sessENTERPRISE_PREFIXOwnerList", sENTERPRISE_PREFIX);
                                        session.setAttribute("sessPERSONAL_PREFIXOwnerList", sPERSONAL_PREFIX);
                                        isLoadEnterprisePrefix = sENTERPRISE_PREFIX;
                                        isLoadPeronalPrefix = sPERSONAL_PREFIX;
                                        String pENTERPRISE_ID = "";
                                        String pPERSONAL_ID = "";
                                        if(!"".equals(sENTERPRISE_ID)){
                                            pENTERPRISE_ID = sENTERPRISE_PREFIX + sENTERPRISE_ID;
                                        }
                                        if(!"".equals(sPERSONAL_ID)){
                                            pPERSONAL_ID = sPERSONAL_PREFIX + sPERSONAL_ID;
                                        }
                                            
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_OWNER_STATE)) {
                                            CERTIFICATION_OWNER_STATE = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_OWNER_TYPE)) {
                                            CERTIFICATION_OWNER_TYPE = "";
                                        }
                                        if("1".equals(strAlertAllTimes))
                                        {
                                            FromCreateDate = "";
                                            ToCreateDate = "";
                                        }
//                                        if(!"".equals(PHONE_CONTRACT_SEARCH) || !"".equals(EMAIL_CONTRACT_SEARCH)
//                                            || !"".equals(COMPANY_NAME) || !"".equals(PASSPORT) || !"".equals(P_ID) || !"".equals(TAX_CODE)
//                                            || !"".equals(BUDGET_CODE) || !"".equals(PERSONAL_NAME))
                                        if(!"".equals(PHONE_CONTRACT_SEARCH) || !"".equals(EMAIL_CONTRACT_SEARCH) || !"".equals(CERTIFICATION_OWNER_TYPE))
                                        {
                                            strExpandFilter = "1";
                                        }
                                        int ss = 0;
//                                        String pENTERPRISE_ID = "";
//                                        String pPERSONAL_ID = "";
//                                        if(!"".equals(TAX_CODE))
//                                        {
//                                            pENTERPRISE_ID = TAX_CODE;
//                                        } else {
//                                            if(!"".equals(BUDGET_CODE)) {
//                                                pENTERPRISE_ID = BUDGET_CODE;
//                                            } else {
//                                                if(!"".equals(DECISION)) {
//                                                    pENTERPRISE_ID = DECISION;
//                                                }
//                                            }
//                                        }
//                                        if(!"".equals(P_ID))
//                                        {
//                                            pPERSONAL_ID = P_ID;
//                                        } else {
//                                            if(!"".equals(CCCD))
//                                            {
//                                                pPERSONAL_ID = CCCD;
//                                            } else {
//                                                if(!"".equals(PASSPORT))
//                                                {
//                                                    pPERSONAL_ID = PASSPORT;
//                                                }
//                                            }
//                                        }
                                        ss = db.S_BO_CERTIFICATION_OWNER_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(CERTIFICATION_OWNER_STATE),
                                            EscapeUtils.escapeHtmlSearch(CERTIFICATION_OWNER_TYPE),EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                            EscapeUtils.escapeHtmlSearch(pENTERPRISE_ID), EscapeUtils.escapeHtmlSearch(PERSONAL_NAME),
                                            EscapeUtils.escapeHtmlSearch(pPERSONAL_ID), EscapeUtils.escapeHtmlSearch(PHONE_CONTRACT_SEARCH),
                                            EscapeUtils.escapeHtmlSearch(EMAIL_CONTRACT_SEARCH));
                                        session.setAttribute("CountListOwnerList", String.valueOf(ss));
                                        if (session.getAttribute("SearchIPageNoPagingOwnerList") != null) {
                                            String sPage = (String) session.getAttribute("SearchIPageNoPagingOwnerList");
                                            iPagNo = Integer.parseInt(sPage);
                                        }
                                        if (session.getAttribute("SearchISwRwsPagingOwnerList") != null) {
                                            String sSumPage = (String) session.getAttribute("SearchISwRwsPagingOwnerList");
                                            iSwRws = Integer.parseInt(sSumPage);
                                        }
                                        if (session.getAttribute("RefreshOwnerListSessNumberPaging") != null) {
                                            String sNoPage = (String) session.getAttribute("RefreshOwnerListSessNumberPaging");
                                            iPaNoSS = Integer.parseInt(sNoPage);
                                        }
                                        session.setAttribute("SearchIPageNoPagingOwnerList", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingOwnerList", String.valueOf(iSwRws));
                                        if (ss > 0) {
                                            db.S_BO_CERTIFICATION_OWNER_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(CERTIFICATION_OWNER_STATE),
                                                EscapeUtils.escapeHtmlSearch(CERTIFICATION_OWNER_TYPE), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                                EscapeUtils.escapeHtmlSearch(pENTERPRISE_ID), EscapeUtils.escapeHtmlSearch(PERSONAL_NAME),
                                                EscapeUtils.escapeHtmlSearch(pPERSONAL_ID), EscapeUtils.escapeHtmlSearch(PHONE_CONTRACT_SEARCH),
                                                EscapeUtils.escapeHtmlSearch(EMAIL_CONTRACT_SEARCH),
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
                                        session.setAttribute("RefreshOwnerListSessPaging", null);
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("SearchIPageNoPagingOwnerList", null);
                                            session.setAttribute("SearchISwRwsPagingOwnerList", null);
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
                                    String PERSONAL_NAME = EscapeUtils.ConvertStringToUnicode(request.getParameter("PERSONAL_NAME"));
                                    String COMPANY_NAME = EscapeUtils.ConvertStringToUnicode(request.getParameter("COMPANY_NAME"));
                                    String CERTIFICATION_OWNER_STATE = request.getParameter("CERTIFICATION_OWNER_STATE");
                                    String CERTIFICATION_OWNER_TYPE = EscapeUtils.ConvertStringToUnicode(request.getParameter("CERTIFICATION_OWNER_TYPE"));
                                    String TAX_CODE =request.getParameter("TAX_CODE");
                                    String BUDGET_CODE =request.getParameter("BUDGET_CODE");
                                    String DECISION =request.getParameter("DECISION");
                                    String P_ID = request.getParameter("P_ID");
                                    String CCCD = request.getParameter("CCCD");
                                    String PASSPORT = request.getParameter("PASSPORT");
                                    String PHONE_CONTRACT_SEARCH = request.getParameter("PHONE_CONTRACT_SEARCH");
                                    String EMAIL_CONTRACT_SEARCH = request.getParameter("EMAIL_CONTRACT_SEARCH");
                                    Boolean nameCheck = Boolean.valueOf(request.getParameter("nameCheck") != null);
                                    if (nameCheck == false) {
                                        FromCreateDate = "";
                                        ToCreateDate = "";
                                        strAlertAllTimes = "1";
                                    }
                                    if ("1".equals(hasPaging)) {
                                        ToCreateDate = (String) session.getAttribute("sessToCreateDateOwnerList");
                                        FromCreateDate = (String) session.getAttribute("sessFromCreateDateOwnerList");
                                        PERSONAL_NAME = (String) session.getAttribute("sessPERSONAL_NAMEQueueList");
                                        COMPANY_NAME = (String) session.getAttribute("sessCOMPANY_NAMEQueueList");
                                        CERTIFICATION_OWNER_STATE = (String) session.getAttribute("sessCERTIFICATION_OWNER_STATEOwnerList");
                                        CERTIFICATION_OWNER_TYPE = (String) session.getAttribute("sessCERTIFICATION_OWNER_TYPEOwnerList");
                                        TAX_CODE = (String) session.getAttribute("sessTAX_CODEOwnerList");
                                        BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODEOwnerList");
                                        DECISION = (String) session.getAttribute("sessDECISIONOwnerList");
                                        P_ID = (String) session.getAttribute("sessP_IDOwnerList");
                                        CCCD = (String) session.getAttribute("sessCCCDOwnerList");
                                        PASSPORT = (String) session.getAttribute("sessPASSPORTOwnerList");
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSOwnerList");
                                        PHONE_CONTRACT_SEARCH = (String) session.getAttribute("sessPHONE_CONTRACTOwnerList");
                                        EMAIL_CONTRACT_SEARCH = (String) session.getAttribute("sessEMAIL_CONTRACTOwnerList");
                                        session.setAttribute("SessParamOnPagingOwnerList", null);
                                    } else {
                                        session.setAttribute("CountListOwnerList", null);
                                    }
                                    session.setAttribute("sessFromCreateDateOwnerList", FromCreateDate);
                                    session.setAttribute("sessToCreateDateOwnerList", ToCreateDate);
                                    session.setAttribute("sessPERSONAL_NAMEQueueList", PERSONAL_NAME);
                                    session.setAttribute("sessCOMPANY_NAMEQueueList", COMPANY_NAME);
                                    session.setAttribute("sessCERTIFICATION_OWNER_STATEOwnerList", CERTIFICATION_OWNER_STATE);
                                    session.setAttribute("sessCERTIFICATION_OWNER_TYPEOwnerList", CERTIFICATION_OWNER_TYPE);
                                    session.setAttribute("sessTAX_CODEOwnerList", TAX_CODE);
                                    session.setAttribute("sessBUDGET_CODEOwnerList", BUDGET_CODE);
                                    session.setAttribute("sessDECISIONOwnerList", DECISION);
                                    session.setAttribute("sessP_IDOwnerList", P_ID);
                                    session.setAttribute("sessCCCDOwnerList", CCCD);
                                    session.setAttribute("sessPASSPORTOwnerList", PASSPORT);
                                    session.setAttribute("sessPHONE_CONTRACTOwnerList", PHONE_CONTRACT_SEARCH);
                                    session.setAttribute("sessEMAIL_CONTRACTOwnerList", EMAIL_CONTRACT_SEARCH);
                                    session.setAttribute("AlertAllTimeSOwnerList", strAlertAllTimes);
                                    String sENTERPRISE_ID = request.getParameter("ENTERPRISE_ID");
                                    String sPERSONAL_ID = request.getParameter("PERSONAL_ID");
                                    String sENTERPRISE_PREFIX = EscapeUtils.ConvertStringToUnicode(request.getParameter("ENTERPRISE_PREFIX"));
                                    String sPERSONAL_PREFIX = EscapeUtils.ConvertStringToUnicode(request.getParameter("PERSONAL_PREFIX"));
                                    if ("1".equals(hasPaging)) {
                                        sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXOwnerList");
                                        sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXOwnerList");
                                        sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDOwnerList");
                                        sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDOwnerList");
                                    }
                                    session.setAttribute("sessENTERPRISE_IDOwnerList", sENTERPRISE_ID);
                                    session.setAttribute("sessPERSONAL_IDOwnerList", sPERSONAL_ID);
                                    session.setAttribute("sessENTERPRISE_PREFIXOwnerList", sENTERPRISE_PREFIX);
                                    session.setAttribute("sessPERSONAL_PREFIXOwnerList", sPERSONAL_PREFIX);
                                    isLoadEnterprisePrefix = sENTERPRISE_PREFIX;
                                    isLoadPeronalPrefix = sPERSONAL_PREFIX;
                                    String pENTERPRISE_ID = "";
                                    String pPERSONAL_ID = "";
                                    if(!"".equals(sENTERPRISE_ID)){
                                        pENTERPRISE_ID = sENTERPRISE_PREFIX + sENTERPRISE_ID;
                                    }
                                    if(!"".equals(sPERSONAL_ID)){
                                        pPERSONAL_ID = sPERSONAL_PREFIX + sPERSONAL_ID;
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_OWNER_STATE)) {
                                        CERTIFICATION_OWNER_STATE = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_OWNER_TYPE)) {
                                        CERTIFICATION_OWNER_TYPE = "";
                                    }
                                    if(!"".equals(PHONE_CONTRACT_SEARCH) || !"".equals(EMAIL_CONTRACT_SEARCH) || !"".equals(CERTIFICATION_OWNER_TYPE))
                                    {
                                        strExpandFilter = "1";
                                    }
                                    int ss = 0;
//                                    if(!"".equals(TAX_CODE)) {
//                                        pENTERPRISE_ID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_TAXCODE + TAX_CODE;
//                                    } else {
//                                        if(!"".equals(BUDGET_CODE)) {
//                                            pENTERPRISE_ID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_BUDGETCODE + BUDGET_CODE;
//                                        } else {
//                                            if(!"".equals(DECISION)) {
//                                                pENTERPRISE_ID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION + DECISION;
//                                            }
//                                        }
//                                    }
//                                    if(!"".equals(P_ID))
//                                    {
//                                        pPERSONAL_ID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CMND + P_ID;
//                                    } else {
//                                        if(!"".equals(CCCD))
//                                        {
//                                            pPERSONAL_ID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CITIZEN_ID + CCCD;
//                                        } else {
//                                            if(!"".equals(PASSPORT))
//                                            {
//                                                pPERSONAL_ID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_PASSPORT + PASSPORT;
//                                            }
//                                        }
//                                    }
                                    if ((session.getAttribute("CountListOwnerList")) == null) {
                                        ss = db.S_BO_CERTIFICATION_OWNER_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(CERTIFICATION_OWNER_STATE),
                                            EscapeUtils.escapeHtmlSearch(CERTIFICATION_OWNER_TYPE),EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                            EscapeUtils.escapeHtmlSearch(pENTERPRISE_ID), EscapeUtils.escapeHtmlSearch(PERSONAL_NAME),
                                            EscapeUtils.escapeHtmlSearch(pPERSONAL_ID), EscapeUtils.escapeHtmlSearch(PHONE_CONTRACT_SEARCH),
                                            EscapeUtils.escapeHtmlSearch(EMAIL_CONTRACT_SEARCH));
                                        session.setAttribute("CountListOwnerList", String.valueOf(ss));
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListOwnerList");
                                        ss = Integer.parseInt(sCount);
                                        session.setAttribute("CountListOwnerList", String.valueOf(ss));
                                    }
                                    iTotRslts = ss;
                                    if (iTotRslts > 0) {
                                        db.S_BO_CERTIFICATION_OWNER_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(CERTIFICATION_OWNER_STATE),
                                            EscapeUtils.escapeHtmlSearch(CERTIFICATION_OWNER_TYPE), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                            EscapeUtils.escapeHtmlSearch(pENTERPRISE_ID), EscapeUtils.escapeHtmlSearch(PERSONAL_NAME),
                                            EscapeUtils.escapeHtmlSearch(pPERSONAL_ID), EscapeUtils.escapeHtmlSearch(PHONE_CONTRACT_SEARCH),
                                            EscapeUtils.escapeHtmlSearch(EMAIL_CONTRACT_SEARCH),
                                            Integer.parseInt(sessLanguageGlobal), iPagNo, iSwRws, rsPgin);
                                        session.setAttribute("SearchIPageNoPagingOwnerList", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingOwnerList", String.valueOf(iSwRws));
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
                                    session.setAttribute("RefreshOwnerListSessPaging", null);
                                    session.setAttribute("SearchIPageNoPagingOwnerList", null);
                                    session.setAttribute("SearchISwRwsPagingOwnerList", null);
                                    session.setAttribute("sessFromCreateDateOwnerList", null);
                                    session.setAttribute("sessToCreateDateOwnerList", null);
                                    session.setAttribute("sessPERSONAL_NAMEQueueList", null);
                                    session.setAttribute("sessCOMPANY_NAMEQueueList", null);
                                    session.setAttribute("sessTAX_CODEOwnerList", null);
                                    session.setAttribute("sessBUDGET_CODEOwnerList", null);
                                    session.setAttribute("sessDECISIONOwnerList", null);
                                    session.setAttribute("sessP_IDOwnerList", null);
                                    session.setAttribute("sessCCCDOwnerList", null);
                                    session.setAttribute("sessPASSPORTOwnerList", null);
                                    session.setAttribute("AlertAllTimeSOwnerList", null);
                                    session.setAttribute("sessENTERPRISE_PREFIXOwnerList", null);
                                    session.setAttribute("sessPERSONAL_PREFIXOwnerList", null);
                                    session.setAttribute("sessENTERPRISE_IDOwnerList", null);
                                    session.setAttribute("sessPERSONAL_IDOwnerList", null);
                                }
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
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
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_FromDate);</script></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="Text" id="demo1" name="FromCreateDate" <%= session.getAttribute("AlertAllTimeSOwnerList") != null && "1".equals(session.getAttribute("AlertAllTimeSOwnerList").toString()) ? "disabled" : ""%>
                                                            value="<%= session.getAttribute("sessFromCreateDateOwnerList") != null && !"1".equals(session.getAttribute("AlertAllTimeSOwnerList").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromCreateDateOwnerList").toString()) : com.ConvertMonthSub(30)%>"
                                                            maxlength="25" class="form-control123"/>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_ToDate);</script></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="Text" id="demo2" name="ToCreateDate" <%= session.getAttribute("AlertAllTimeSOwnerList") != null && "1".equals(session.getAttribute("AlertAllTimeSOwnerList").toString()) ? "disabled" : ""%>
                                                            value="<%= session.getAttribute("sessToCreateDateOwnerList") != null && !"1".equals(session.getAttribute("AlertAllTimeSOwnerList").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToCreateDateOwnerList").toString()) : com.ConvertMonthSub(0)%>"
                                                            maxlength="25" class="form-control123"/>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_check_date);</script></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;padding-top: 7px; text-align: left;">
                                                        <label class="switch" for="idCheck">
                                                            <input type="checkbox" name="nameCheck" id="idCheck" onchange="checkboxChange();" <%= session.getAttribute("AlertAllTimeSOwnerList") != null && "1".equals(session.getAttribute("AlertAllTimeSOwnerList").toString()) ? "" : "checked" %>/>
                                                            <div class="slider round"></div>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_grid_company);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="COMPANY_NAME" maxlength="150" value="<%= session.getAttribute("sessCOMPANY_NAMEQueueList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCOMPANY_NAMEQueueList").toString()) : ""%>"
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
                                                        <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessENTERPRISE_PREFIXOwnerList") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessENTERPRISE_PREFIXOwnerList").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                        <input type="text" name="ENTERPRISE_ID" maxlength="45" value="<%= session.getAttribute("sessENTERPRISE_IDOwnerList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessENTERPRISE_IDOwnerList").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                                <script>
                                                    function changeEnterprise() {
                                                        $("#idLblTooltipEnterprise").text(global_fm_enter + $("#ENTERPRISE_PREFIX option:selected").text());
                                                    }
                                                </script>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_grid_personal);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="PERSONAL_NAME" maxlength="150" value="<%= session.getAttribute("sessPERSONAL_NAMEQueueList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_NAMEQueueList").toString()) : ""%>"
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
                                                        <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessPERSONAL_PREFIXOwnerList") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessPERSONAL_PREFIXOwnerList").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                        <input type="text" name="PERSONAL_ID" maxlength="45" value="<%= session.getAttribute("sessPERSONAL_IDOwnerList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_IDOwnerList").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                                <script>
                                                    function changePersonal() {
                                                        $("#idLblTooltipPersonal").text(global_fm_enter + $("#PERSONAL_PREFIX option:selected").text());
                                                    }
                                                </script>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <div class="col-sm-5" style="padding-right: 0px;text-align: right;">
                                                        <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                    </div>
                                                    <div class="col-sm-7" style="padding-right: 0px;">

                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="clear: both;">
                                                <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;padding-top: 0;">
                                                    <a id="idAShowList" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupViewCSRSList();"><script>document.write(global_fm_search_expand);</script></a>
                                                    <a id="idAHideList" style="cursor: pointer; color: blue; text-decoration: underline;display: none;" onclick="popupHideCTSList();"><script>document.write(global_fm_search_hide);</script></a>
                                                </label>
                                                <div class="col-sm-10" style="padding-right: 0px;"></div>
                                            </div>
                                            <div id="idViewCSRList" style="display: none;clear: both;padding-top: 0px;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_Status);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="CERTIFICATION_OWNER_STATE" id="CERTIFICATION_OWNER_STATE" class="form-control123">
                                                                <%
                                                                    //if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_ALL,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_SEARCH, sessFunctionCert) == true) {
                                                                %>
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCERTIFICATION_OWNER_STATEOwnerList") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCERTIFICATION_OWNER_STATEOwnerList").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    //}
                                                                %>
                                                                <%
                                                                    CERTIFICATION_OWNER_STATE[][] rsState = new CERTIFICATION_OWNER_STATE[1][];
                                                                    db.S_BO_CERTIFICATION_OWNER_STATE_COMBOBOX(sessLanguageGlobal, rsState);
                                                                    if (rsState[0].length > 0) {
                                                                        for (CERTIFICATION_OWNER_STATE temp1 : rsState[0]) {
                                                                            //if(CommonFunction.CheckRoleFuncValid(EscapeUtils.CheckTextNull(temp1.NAME),Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_SEARCH, sessFunctionCert) == true) {
                                                                 %>
                                                                 <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessCERTIFICATION_OWNER_STATEOwnerList") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessCERTIFICATION_OWNER_STATEOwnerList").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                                 <%
                                                                            //}
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
                                                            <script>document.write(global_fm_phone);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="PHONE_CONTRACT_SEARCH" maxlength="16" value="<%= session.getAttribute("sessPHONE_CONTRACTOwnerList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPHONE_CONTRACTOwnerList").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(token_fm_email);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="EMAIL_CONTRACT_SEARCH" maxlength="150" value="<%= session.getAttribute("sessEMAIL_CONTRACTOwnerList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessEMAIL_CONTRACTOwnerList").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(owner_fm_type);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="CERTIFICATION_OWNER_TYPE" id="CERTIFICATION_OWNER_TYPE" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCERTIFICATION_OWNER_TYPEQueueList") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCERTIFICATION_OWNER_TYPEQueueList").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    CERTIFICATION_OWNER_TYPE[][] rsType = new CERTIFICATION_OWNER_TYPE[1][];
                                                                    db.S_BO_CERTIFICATION_OWNER_TYPE_COMBOBOX(sessLanguageGlobal, rsType);
                                                                    if (rsType[0].length > 0) {
                                                                        for (CERTIFICATION_OWNER_TYPE temp1 : rsType[0]) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessCERTIFICATION_OWNER_TYPEQueueList") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessCERTIFICATION_OWNER_TYPEQueueList").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                                <%
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
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
                                <script type="text/javascript">
                                    $(document).ready(function () {
                                        goToByScroll("idShowResultSearch");
                                    });
                                </script>
                                <div class="x_panel" id="idShowResultSearch">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(cert_title_table);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;"><script>document.write(global_label_grid_sum);</script><%= strMess%></li>
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
                                            <th><script>document.write(owner_fm_type);</script></th>
                                            <th><script>document.write(global_fm_Status);</script></th>
                                            <th><script>document.write(global_fm_date_create);</script></th>
                                            <th><script>document.write(global_fm_action);</script></th>
                                            </thead>
                                            <tbody>
                                                <%
                                                    if (iPaNoSS > 1) {
                                                        j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                    }
                                                    session.setAttribute("RefreshOwnerListSessNumberPaging", String.valueOf(iPaNoSS));
                                                    if (rsPgin[0].length > 0) {
                                                        for (CERTIFICATION_OWNER temp1 : rsPgin[0]) {
//                                                            String sSMTOrMSN = EscapeUtils.CheckTextNull(temp1.TAX_CODE);
//                                                            if("".equals(sSMTOrMSN)) {
//                                                                sSMTOrMSN = EscapeUtils.CheckTextNull(temp1.BUDGET_CODE);
//                                                            }
//                                                            if("".equals(sSMTOrMSN)) {
//                                                                sSMTOrMSN = EscapeUtils.CheckTextNull(temp1.DECISION);
//                                                            }
//                                                            String sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_ID);
//                                                            if("".equals(sCMNDOrHC)) {
//                                                                sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.CITIZEN_ID);
//                                                            }
//                                                            if("".equals(sCMNDOrHC)) {
//                                                                sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.PASSPORT);
//                                                            }
                                                            String sOWNER_ID = String.valueOf(temp1.ID);
                                                %>
                                                <tr>
                                                    <td style="text-align: center;"><%= com.convertMoney(j)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.COMPANY_NAME)%></td>
                                                    <td><%= temp1.ENTERPRISE_ID_GRID%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME)%></td>
                                                    <td><%= temp1.PERSONAL_ID_GRID%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_OWNER_TYPE_DESC)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_OWNER_STATE_DESC)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_DT)%></td>
                                                    <td>
                                                        <a style="cursor: pointer;margin-bottom: 2px;" onclick="popupDetail('<%=sOWNER_ID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_detail);</script> </a>
                                                        <%
                                                            if(temp1.CERTIFICATION_OWNER_STATE_ID == Definitions.CONFIG_CERTIFICATION_OWNER_STATE_ID_OPERATED)
                                                            {
                                                        %>
                                                        <%
                                                            //if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                        %>
                                                        <a style="cursor: pointer;" onclick="popupDialogChange('<%=sOWNER_ID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_change);</script> </a>
                                                        <a style="cursor: pointer;" onclick="popupDialogDispose('<%=sOWNER_ID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_dispose);</script> </a>
                                                        <%
                                                                //}
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
                    <!-- Modal Cert Decline -->
                    <div id="myModalCertDecline" class="modal fade" role="dialog">
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
                    </div>
                    <script>
                        $(document).ready(function () {
                            if('<%= strExpandFilter%>' === "1")
                            {
                                popupViewCSRSList();
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