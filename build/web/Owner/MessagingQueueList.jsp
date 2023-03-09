<%-- 
    Document   : MessagingQueueList
    Created on : Nov 1, 2019, 5:09:22 PM
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
            document.title = ownerapprove_title_list;
            $(document).ready(function () {
//                reload_js("../style/bootstrap.min.js");
                localStorage.setItem("LOCAL_PARAM_OWNERAPPROVE_LIST", null);
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
                $('#contentEdit').load('MessagingQueueApprove.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    reload_js("../style/bootstrap.min.js");
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function FormDecline(idATTR, idCSRF, idBranch, idUser) {
                swal({
                    title: "",
                    text: request_conform_delete,
                    imageUrl: "../Images/icon_warning.png",
                    imageSize: "45x45",
                    showCancelButton: true,
                    closeOnConfirm: true,
                    allowOutsideClick: false,
                    confirmButtonText: login_fm_buton_OK,
                    cancelButtonText: global_button_grid_cancel,
                    cancelButtonColor: "#199DC0"
                },
                function () {
                    $('body').append('<div id="over"></div>');
                    $(".loading-gif").show();
                    setTimeout(function () {
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
                    }, JS_STR_ACTION_TIMEOUT);
                });
            }
            function pushNotificationDecline(idBRANCH, user) {
                var xmlhttp = new XMLHttpRequest();
                xmlhttp.open("POST", "../PushNotiRequestDecline?t="+new Date(), false);
                xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                xmlhttp.send("name="+idBRANCH+"&user="+user);
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
            function popupDialogCertDecline(attrId)
            {
                $('#myModalCertDecline').modal('show');
                $('#contentCertDecline').empty();
                $('#contentCertDecline').load('IncludeOwnerDecline.jsp', {attrId: attrId}, function () {
                });
                $(".loading-gifCertDecline").hide();
                $(".loading-gif").hide();
                $('#over').remove();
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
        </style>
    </head>
    <body class="nav-md">
        <%
        if (request.getSession(false).getAttribute("sUserID") != null) {
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
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
                        document.getElementById("idNameURL").innerHTML = ownerapprove_title_list;
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
                                String strExpandFilter = "0";
                                try {
                                    String isLoadPeronalPrefix = "";
                                    String isLoadEnterprisePrefix = "";
                                    CERTIFICATION_OWNER[][] rsPgin = new CERTIFICATION_OWNER[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (session.getAttribute("RefreshApproveOwnerSess") != null && session.getAttribute("sessFromCreateDateQueueList") != null
                                            && session.getAttribute("sessToCreateDateQueueList") != null) {
                                        session.setAttribute("RefreshApproveOwnerSessPaging", "1");
                                        statusLoad = 1;
                                        String ToCreateDate = (String) session.getAttribute("sessToCreateDateQueueList");
                                        String FromCreateDate = (String) session.getAttribute("sessFromCreateDateQueueList");
                                        String MESSAGING_QUEUE_STATE = (String) session.getAttribute("sessMESSAGING_QUEUE_STATEQueueList");
                                        String MESSAGING_QUEUE_FUNCTION = (String) session.getAttribute("sessMESSAGING_QUEUE_FUNCTIONQueueList");

                                        String EMAIL_CONTRACT_SEARCH = (String) session.getAttribute("sessEMAIL_CONTRACTQueueList");
                                        String PHONE_CONTRACT_SEARCH = (String) session.getAttribute("sessPHONE_CONTRACTQueueList");
                                        String PASSPORT = (String) session.getAttribute("sessPASSPORTQueueList");
                                        String P_ID = (String) session.getAttribute("sessP_IDQueueList");
                                        String CCCD = (String) session.getAttribute("sessCCCDQueueList");
                                        
                                        String PERSONAL_NAME = (String) session.getAttribute("sessPERSONAL_NAMEQueueList");
                                        String BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODEQueueList");
                                        String TAX_CODE = (String) session.getAttribute("sessTAX_CODEQueueList");
                                        String DECISION = (String) session.getAttribute("sessDECISIONQueueList");
                                        String COMPANY_NAME = (String) session.getAttribute("sessCOMPANY_NAMEQueueList");
                                        
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSQueueList");
                                        session.setAttribute("RefreshApproveOwnerSess", null);
                                        session.setAttribute("sessFromCreateDateQueueList", FromCreateDate);
                                        session.setAttribute("sessToCreateDateQueueList", ToCreateDate);
                                        
                                        session.setAttribute("sessPERSONAL_NAMEQueueList", PERSONAL_NAME);
                                        session.setAttribute("sessCOMPANY_NAMEAQueueList", COMPANY_NAME);
                                        session.setAttribute("sessMESSAGING_QUEUE_STATEQueueList", MESSAGING_QUEUE_STATE);
                                        session.setAttribute("sessMESSAGING_QUEUE_FUNCTIONQueueList", MESSAGING_QUEUE_FUNCTION);
                                        session.setAttribute("sessTAX_CODEQueueList", TAX_CODE);
                                        session.setAttribute("sessBUDGET_CODEQueueList", BUDGET_CODE);
                                        session.setAttribute("sessDECISIONQueueList", DECISION);
                                        session.setAttribute("sessP_IDQueueList", P_ID);
                                        session.setAttribute("sessCCCDQueueList", CCCD);
                                        session.setAttribute("sessPASSPORTQueueList", PASSPORT);
                                        session.setAttribute("sessEMAIL_CONTRACTQueueList", EMAIL_CONTRACT_SEARCH);
                                        session.setAttribute("sessPHONE_CONTRACTQueueList", PHONE_CONTRACT_SEARCH);
                                        
                                        session.setAttribute("AlertAllTimeSQueueList", strAlertAllTimes);
                                        String sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDQueueList");
                                        String sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDQueueList");
                                        String sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXQueueList");
                                        String sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXQueueList");
                                        session.setAttribute("sessENTERPRISE_IDQueueList", sENTERPRISE_ID);
                                        session.setAttribute("sessPERSONAL_IDQueueList", sPERSONAL_ID);
                                        session.setAttribute("sessENTERPRISE_PREFIXQueueList", sENTERPRISE_PREFIX);
                                        session.setAttribute("sessPERSONAL_PREFIXQueueList", sPERSONAL_PREFIX);
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
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(MESSAGING_QUEUE_STATE)) {
                                            MESSAGING_QUEUE_STATE = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(MESSAGING_QUEUE_FUNCTION)) {
                                            MESSAGING_QUEUE_FUNCTION = "";
                                        }
                                        if("1".equals(strAlertAllTimes))
                                        {
                                            FromCreateDate = "";
                                            ToCreateDate = "";
                                        }
                                        if(!"".equals(PHONE_CONTRACT_SEARCH) || !"".equals(EMAIL_CONTRACT_SEARCH) || !"".equals(sENTERPRISE_ID)
                                            || !"".equals(COMPANY_NAME) || !"".equals(sPERSONAL_ID) || !"".equals(PERSONAL_NAME))
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
                                        ss = db.S_BO_MESSAGING_QUEUE_APPROVED_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(MESSAGING_QUEUE_STATE),
                                            EscapeUtils.escapeHtmlSearch(MESSAGING_QUEUE_FUNCTION), "", "", EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                            EscapeUtils.escapeHtmlSearch(pENTERPRISE_ID), EscapeUtils.escapeHtmlSearch(PERSONAL_NAME),
                                            EscapeUtils.escapeHtmlSearch(pPERSONAL_ID), EscapeUtils.escapeHtmlSearch(PHONE_CONTRACT_SEARCH),
                                            EscapeUtils.escapeHtmlSearch(EMAIL_CONTRACT_SEARCH));
                                        session.setAttribute("CountListQueueList", String.valueOf(ss));
                                        if (session.getAttribute("SearchIPageNoPagingQueueList") != null) {
                                            String sPage = (String) session.getAttribute("SearchIPageNoPagingQueueList");
                                            iPagNo = Integer.parseInt(sPage);
                                        }
                                        if (session.getAttribute("SearchISwRwsPagingQueueList") != null) {
                                            String sSumPage = (String) session.getAttribute("SearchISwRwsPagingQueueList");
                                            iSwRws = Integer.parseInt(sSumPage);
                                        }
                                        if (session.getAttribute("RefreshApproveOwnerSessNumberPaging") != null) {
                                            String sNoPage = (String) session.getAttribute("RefreshApproveOwnerSessNumberPaging");
                                            iPaNoSS = Integer.parseInt(sNoPage);
                                        }
                                        session.setAttribute("SearchIPageNoPagingQueueList", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingQueueList", String.valueOf(iSwRws));
                                        if (ss > 0) {
                                            db.S_BO_MESSAGING_QUEUE_APPROVED_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(MESSAGING_QUEUE_STATE),
                                            EscapeUtils.escapeHtmlSearch(MESSAGING_QUEUE_FUNCTION), "", "", EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
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
                                        session.setAttribute("RefreshApproveOwnerSessPaging", null);
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("SearchIPageNoPagingQueueList", null);
                                            session.setAttribute("SearchISwRwsPagingQueueList", null);
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
                                    String MESSAGING_QUEUE_STATE = request.getParameter("MESSAGING_QUEUE_STATE");
                                    String MESSAGING_QUEUE_FUNCTION = EscapeUtils.ConvertStringToUnicode(request.getParameter("MESSAGING_QUEUE_FUNCTION"));
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
                                        ToCreateDate = (String) session.getAttribute("sessToCreateDateQueueList");
                                        FromCreateDate = (String) session.getAttribute("sessFromCreateDateQueueList");
                                        PERSONAL_NAME = (String) session.getAttribute("sessPERSONAL_NAMEQueueList");
                                        COMPANY_NAME = (String) session.getAttribute("sessCOMPANY_NAMEQueueList");
                                        MESSAGING_QUEUE_STATE = (String) session.getAttribute("sessMESSAGING_QUEUE_STATEQueueList");
                                        MESSAGING_QUEUE_FUNCTION = (String) session.getAttribute("sessMESSAGING_QUEUE_FUNCTIONQueueList");
                                        TAX_CODE = (String) session.getAttribute("sessTAX_CODEQueueList");
                                        BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODEQueueList");
                                        DECISION = (String) session.getAttribute("sessDECISIONQueueList");
                                        P_ID = (String) session.getAttribute("sessP_IDQueueList");
                                        CCCD = (String) session.getAttribute("sessCCCDQueueList");
                                        PASSPORT = (String) session.getAttribute("sessPASSPORTQueueList");
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSQueueList");
                                        PHONE_CONTRACT_SEARCH = (String) session.getAttribute("sessPHONE_CONTRACTQueueList");
                                        EMAIL_CONTRACT_SEARCH = (String) session.getAttribute("sessEMAIL_CONTRACTQueueList");
                                        session.setAttribute("SessParamOnPagingCertList", null);
                                    } else {
                                        session.setAttribute("CountListQueueList", null);
                                    }
                                    session.setAttribute("sessFromCreateDateQueueList", FromCreateDate);
                                    session.setAttribute("sessToCreateDateQueueList", ToCreateDate);
                                    session.setAttribute("sessPERSONAL_NAMEQueueList", PERSONAL_NAME);
                                    session.setAttribute("sessCOMPANY_NAMEQueueList", COMPANY_NAME);
                                    session.setAttribute("sessMESSAGING_QUEUE_STATEQueueList", MESSAGING_QUEUE_STATE);
                                    session.setAttribute("sessMESSAGING_QUEUE_FUNCTIONQueueList", MESSAGING_QUEUE_FUNCTION);
                                    session.setAttribute("sessTAX_CODEQueueList", TAX_CODE);
                                    session.setAttribute("sessBUDGET_CODEQueueList", BUDGET_CODE);
                                    session.setAttribute("sessDECISIONQueueList", DECISION);
                                    session.setAttribute("sessP_IDQueueList", P_ID);
                                    session.setAttribute("sessCCCDQueueList", CCCD);
                                    session.setAttribute("sessPASSPORTQueueList", PASSPORT);
                                    session.setAttribute("sessPHONE_CONTRACTQueueList", PHONE_CONTRACT_SEARCH);
                                    session.setAttribute("sessEMAIL_CONTRACTQueueList", EMAIL_CONTRACT_SEARCH);
                                    session.setAttribute("AlertAllTimeSQueueList", strAlertAllTimes);
                                    String sENTERPRISE_ID = request.getParameter("ENTERPRISE_ID");
                                    String sPERSONAL_ID = request.getParameter("PERSONAL_ID");
                                    String sENTERPRISE_PREFIX = EscapeUtils.ConvertStringToUnicode(request.getParameter("ENTERPRISE_PREFIX"));
                                    String sPERSONAL_PREFIX = EscapeUtils.ConvertStringToUnicode(request.getParameter("PERSONAL_PREFIX"));
                                    if ("1".equals(hasPaging)) {
                                        sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXQueueList");
                                        sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXQueueList");
                                        sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDQueueList");
                                        sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDQueueList");
                                    }
                                    session.setAttribute("sessENTERPRISE_IDQueueList", sENTERPRISE_ID);
                                    session.setAttribute("sessPERSONAL_IDQueueList", sPERSONAL_ID);
                                    session.setAttribute("sessENTERPRISE_PREFIXQueueList", sENTERPRISE_PREFIX);
                                    session.setAttribute("sessPERSONAL_PREFIXQueueList", sPERSONAL_PREFIX);
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
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(MESSAGING_QUEUE_STATE)) {
                                        MESSAGING_QUEUE_STATE = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(MESSAGING_QUEUE_FUNCTION)) {
                                        MESSAGING_QUEUE_FUNCTION = "";
                                    }
                                    if(!"".equals(PHONE_CONTRACT_SEARCH) || !"".equals(EMAIL_CONTRACT_SEARCH) || !"".equals(pENTERPRISE_ID)
                                        || !"".equals(COMPANY_NAME) || !"".equals(pPERSONAL_ID) || !"".equals(PERSONAL_NAME))
                                    {
                                        strExpandFilter = "1";
                                    }
                                    int ss = 0;
//                                    String pENTERPRISE_ID = "";
//                                    String pPERSONAL_ID = "";
//                                    if(!"".equals(TAX_CODE))
//                                    {
//                                        pENTERPRISE_ID = TAX_CODE;
//                                    } else {
//                                        if(!"".equals(BUDGET_CODE)) {
//                                            pENTERPRISE_ID = BUDGET_CODE;
//                                        } else {
//                                            if(!"".equals(DECISION)) {
//                                                pENTERPRISE_ID = DECISION;
//                                            }
//                                        }
//                                    }
//                                    if(!"".equals(P_ID))
//                                    {
//                                        pPERSONAL_ID = P_ID;
//                                    } else {
//                                        if(!"".equals(CCCD))
//                                        {
//                                            pPERSONAL_ID = CCCD;
//                                        } else {
//                                            if(!"".equals(PASSPORT))
//                                            {
//                                                pPERSONAL_ID = PASSPORT;
//                                            }
//                                        }
//                                    }
                                    if ((session.getAttribute("CountListQueueList")) == null) {
                                        ss = db.S_BO_MESSAGING_QUEUE_APPROVED_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(MESSAGING_QUEUE_STATE),
                                            EscapeUtils.escapeHtmlSearch(MESSAGING_QUEUE_FUNCTION), "", "", EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                            EscapeUtils.escapeHtmlSearch(pENTERPRISE_ID), EscapeUtils.escapeHtmlSearch(PERSONAL_NAME),
                                            EscapeUtils.escapeHtmlSearch(pPERSONAL_ID), EscapeUtils.escapeHtmlSearch(PHONE_CONTRACT_SEARCH),
                                            EscapeUtils.escapeHtmlSearch(EMAIL_CONTRACT_SEARCH));
                                        session.setAttribute("CountListQueueList", String.valueOf(ss));
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListQueueList");
                                        ss = Integer.parseInt(sCount);
                                        session.setAttribute("CountListQueueList", String.valueOf(ss));
                                    }
                                    iTotRslts = ss;
                                    if (iTotRslts > 0) {
                                        db.S_BO_MESSAGING_QUEUE_APPROVED_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(MESSAGING_QUEUE_STATE),
                                            EscapeUtils.escapeHtmlSearch(MESSAGING_QUEUE_FUNCTION), "", "", EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                            EscapeUtils.escapeHtmlSearch(pENTERPRISE_ID), EscapeUtils.escapeHtmlSearch(PERSONAL_NAME),
                                            EscapeUtils.escapeHtmlSearch(pPERSONAL_ID), EscapeUtils.escapeHtmlSearch(PHONE_CONTRACT_SEARCH),
                                            EscapeUtils.escapeHtmlSearch(EMAIL_CONTRACT_SEARCH),
                                            Integer.parseInt(sessLanguageGlobal), iPagNo, iSwRws, rsPgin);
                                        session.setAttribute("SearchIPageNoPagingQueueList", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingQueueList", String.valueOf(iSwRws));
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
                                    session.setAttribute("RefreshApproveOwnerSessPaging", null);
                                    session.setAttribute("SearchIPageNoPagingQueueList", null);
                                    session.setAttribute("SearchISwRwsPagingQueueList", null);
                                    session.setAttribute("sessFromCreateDateQueueList", null);
                                    session.setAttribute("sessToCreateDateQueueList", null);
                                    session.setAttribute("sessPERSONAL_NAMEQueueList", null);
                                    session.setAttribute("sessCOMPANY_NAMEQueueList", null);
                                    session.setAttribute("sessTAX_CODEQueueList", null);
                                    session.setAttribute("sessBUDGET_CODEQueueList", null);
                                    session.setAttribute("sessDECISIONQueueList", null);
                                    session.setAttribute("sessP_IDQueueList", null);
                                    session.setAttribute("sessCCCDQueueList", null);
                                    session.setAttribute("sessPASSPORTQueueList", null);
                                    session.setAttribute("AlertAllTimeSQueueList", null);
                                    session.setAttribute("sessENTERPRISE_PREFIXQueueList", null);
                                    session.setAttribute("sessPERSONAL_PREFIXQueueList", null);
                                    session.setAttribute("sessENTERPRISE_IDQueueList", null);
                                    session.setAttribute("sessPERSONAL_IDQueueList", null);
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
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_FromDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo1" name="FromCreateDate" <%= session.getAttribute("AlertAllTimeSQueueList") != null && "1".equals(session.getAttribute("AlertAllTimeSQueueList").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessFromCreateDateQueueList") != null && !"1".equals(session.getAttribute("AlertAllTimeSQueueList").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromCreateDateQueueList").toString()) : com.ConvertMonthSub(30)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_ToDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo2" name="ToCreateDate" <%= session.getAttribute("AlertAllTimeSQueueList") != null && "1".equals(session.getAttribute("AlertAllTimeSQueueList").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessToCreateDateQueueList") != null && !"1".equals(session.getAttribute("AlertAllTimeSQueueList").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToCreateDateQueueList").toString()) : com.ConvertMonthSub(0)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_check_date);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;padding-top: 7px; text-align: left;">
                                                            <label class="switch" for="idCheck">
                                                                <input type="checkbox" name="nameCheck" id="idCheck" onchange="checkboxChange();" <%= session.getAttribute("AlertAllTimeSQueueList") != null && "1".equals(session.getAttribute("AlertAllTimeSQueueList").toString()) ? "" : "checked" %>/>
                                                                <div class="slider round"></div>
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(cert_fm_type_request);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="MESSAGING_QUEUE_FUNCTION" id="MESSAGING_QUEUE_FUNCTION" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessMESSAGING_QUEUE_FUNCTIONQueueList") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessMESSAGING_QUEUE_FUNCTIONQueueList").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    MESSAGING_QUEUE_FUNCTION[][] rsType = new MESSAGING_QUEUE_FUNCTION[1][];
                                                                    db.S_BO_MESSAGING_QUEUE_FUNCTION_COMBOBOX(sessLanguageGlobal, rsType);
                                                                    if (rsType[0].length > 0) {
                                                                        for (MESSAGING_QUEUE_FUNCTION temp1 : rsType[0]) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessMESSAGING_QUEUE_FUNCTIONQueueList") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessMESSAGING_QUEUE_FUNCTIONQueueList").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_Status_request);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="MESSAGING_QUEUE_STATE" id="MESSAGING_QUEUE_STATE" class="form-control123">
                                                                <%
                                                                    //if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_ALL,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_SEARCH, sessFunctionCert) == true) {
                                                                %>
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessMESSAGING_QUEUE_STATEQueueList") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessMESSAGING_QUEUE_STATEQueueList").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    //}
                                                                %>
                                                                <%
                                                                    MESSAGING_QUEUE_STATE[][] rsState = new MESSAGING_QUEUE_STATE[1][];
                                                                    db.S_BO_MESSAGING_QUEUE_STATE_COMBOBOX(sessLanguageGlobal, rsState);
                                                                    if (rsState[0].length > 0) {
                                                                        for (MESSAGING_QUEUE_STATE temp1 : rsState[0]) {
                                                                            //if(CommonFunction.CheckRoleFuncValid(EscapeUtils.CheckTextNull(temp1.NAME),Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_SEARCH, sessFunctionCert) == true) {
                                                                 %>
                                                                 <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessMESSAGING_QUEUE_STATEQueueList") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessMESSAGING_QUEUE_STATEQueueList").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                        <div class="col-sm-5" style="padding-right: 0px;text-align: right;">
                                                            <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                        </div>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            
                                                        </div>
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
                                                <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
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
                                                                <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessENTERPRISE_PREFIXQueueList") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessENTERPRISE_PREFIXQueueList").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                                <input type="text" name="ENTERPRISE_ID" maxlength="45" value="<%= session.getAttribute("sessENTERPRISE_IDQueueList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessENTERPRISE_IDQueueList").toString()) : ""%>"
                                                                    class="form-control123">
                                                            </div>
                                                        </div>
                                                        <script>
                                                            function changeEnterprise() {
                                                                $("#idLblTooltipEnterprise").text(global_fm_enter + $("#ENTERPRISE_PREFIX option:selected").text());
                                                            }
                                                        </script>
                                                    </div>
<!--                                                    <div class="col-sm-4" style="padding-left: 0;">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                                <script>document.write(global_fm_MST);</script>
                                                            </label>
                                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                                <input type="text" name="TAX_CODE" maxlength="45" value="<= session.getAttribute("sessTAX_CODEQueueList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessTAX_CODEQueueList").toString()) : ""%>"
                                                                    class="form-control123">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4" style="padding-left: 0;">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                                <script>document.write(global_fm_MNS);</script>
                                                            </label>
                                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                                <input type="text" name="BUDGET_CODE" maxlength="45" value="<= session.getAttribute("sessBUDGET_CODEQueueList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessBUDGET_CODEQueueList").toString()) : ""%>"
                                                                    class="form-control123">
                                                            </div>
                                                        </div>
                                                    </div>-->
                                                </div>
                                                <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
<!--                                                    <div class="col-sm-4" style="padding-left: 0;">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                                <script>document.write(global_fm_decision);</script>
                                                            </label>
                                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                                <input type="text" name="DECISION" maxlength="45" value="<= session.getAttribute("sessDECISIONQueueList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessDECISIONQueueList").toString()) : ""%>"
                                                                    class="form-control123">
                                                            </div>
                                                        </div>
                                                    </div>-->
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
                                                                <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessPERSONAL_PREFIXQueueList") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessPERSONAL_PREFIXQueueList").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                                <input type="text" name="PERSONAL_ID" maxlength="45" value="<%= session.getAttribute("sessPERSONAL_IDQueueList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_IDQueueList").toString()) : ""%>"
                                                                    class="form-control123">
                                                            </div>
                                                        </div>
                                                        <script>
                                                            function changePersonal() {
                                                                $("#idLblTooltipPersonal").text(global_fm_enter + $("#PERSONAL_PREFIX option:selected").text());
                                                            }
                                                        </script>
                                                    </div>
<!--                                                    <div class="col-sm-4" style="padding-left: 0;">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                                <script>document.write(global_fm_CMND);</script>
                                                            </label>
                                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                                <input type="text" name="P_ID" maxlength="32" value="<= session.getAttribute("sessP_IDQueueList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessP_IDQueueList").toString()) : ""%>"
                                                                    class="form-control123">
                                                            </div>
                                                        </div>
                                                    </div>-->
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_CitizenId);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="CCCD" maxlength="32" value="<%= session.getAttribute("sessCCCDQueueList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCCCDQueueList").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_phone);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="PHONE_CONTRACT_SEARCH" maxlength="16" value="<%= session.getAttribute("sessPHONE_CONTRACTQueueList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPHONE_CONTRACTQueueList").toString()) : ""%>"
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
                                                            <input type="text" name="EMAIL_CONTRACT_SEARCH" maxlength="150" value="<%= session.getAttribute("sessEMAIL_CONTRACTQueueList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessEMAIL_CONTRACTQueueList").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_HC);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="PASSPORT" maxlength="32" value="<%= session.getAttribute("sessPASSPORTQueueList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPASSPORTQueueList").toString()) : ""%>"
                                                                   class="form-control123">
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
                                                    session.setAttribute("RefreshApproveOwnerSessNumberPaging", String.valueOf(iPaNoSS));
                                                    if (rsPgin[0].length > 0) {
                                                        for (CERTIFICATION_OWNER temp1 : rsPgin[0]) {
                                                            String sMESSAGING_QUEUE_ID = String.valueOf(temp1.MESSAGING_QUEUE_ID);
                                                %>
                                                <tr>
                                                    <td style="text-align: center;"><%= com.convertMoney(j)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.COMPANY_NAME)%></td>
                                                    <td><%= temp1.ENTERPRISE_ID_GRID%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME)%></td>
                                                    <td><%= temp1.PERSONAL_ID_GRID%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.MESSAGING_QUEUE_FUNCTION_DESC)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.MESSAGING_QUEUE_STATE_DESC)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_DT)%></td>
                                                    <td>
                                                        <a style="cursor: pointer;margin-bottom: 2px;" onclick="popupDetail('<%=sMESSAGING_QUEUE_ID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_detail);</script> </a>
                                                        <%
                                                            if(temp1.MESSAGING_QUEUE_STATE_ID == Definitions.CONFIG_MESSAGING_QUEUE_STATE_ID_PENDING
                                                                || temp1.MESSAGING_QUEUE_STATE_ID == Definitions.CONFIG_MESSAGING_QUEUE_STATE_ID_INITIALIZED)
                                                            {
                                                        %>
                                                        <%
                                                            //if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                        %>
                                                        <a style="cursor: pointer;" onclick="popupDialogCertDecline('<%=sMESSAGING_QUEUE_ID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_decline);</script> </a>
                                                        <%
                                                                //}
                                                            } else if(temp1.MESSAGING_QUEUE_STATE_ID != Definitions.CONFIG_MESSAGING_QUEUE_STATE_ID_COMMITED
                                                                && temp1.MESSAGING_QUEUE_STATE_ID != Definitions.CONFIG_MESSAGING_QUEUE_STATE_ID_DECLINED)
                                                            {
                                                                if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                         %>
                                                        <a style="cursor: pointer;" onclick="popupDialogCertDecline('<%=sMESSAGING_QUEUE_ID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_decline);</script> </a>
                                                        <%           
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
//                            <
//                                if (status == 1 && statusLoad == 1) {
//                            >
//                            if(localStorage.getItem("EDIT_CERTAPPROVE") !== null && localStorage.getItem("EDIT_CERTAPPROVE") !== "null")
//                            {
//                                var CERTAPPROVE = localStorage.getItem("EDIT_CERTAPPROVE");
//                                localStorage.setItem("EDIT_CERTAPPROVE", null);
//                                popupDetail(CERTAPPROVE);
//                            }
//                            <
//                                } else {
//                            >
//                                localStorage.setItem("EDIT_CERTAPPROVE", null);
//                            <
//                                }
//                            >
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