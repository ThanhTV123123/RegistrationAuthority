<%-- 
    Document   : TokenList
    Created on : Jun 26, 2018, 5:16:12 PM
    Author     : THANH-PC
--%>

<%@page import="vn.ra.process.CommonReferServlet"%>
<%@page import="java.net.URLEncoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<%  response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
    String strAlertAllTimes = "0";
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
            document.title = token_title_list;
            $(document).ready(function () {
                $('.loading-gif').hide();
                localStorage.setItem("LOCAL_PARAM_TOKENLIST", null);
                localStorage.setItem("storeAnticsrfTokenList", null);
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
            });
            function popupEdit(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('TokenEdit.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function addForm() {
                window.location = 'TokenNew.jsp';
            }
            function popupaddChangeSOPIN(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('ChangeSOPIN.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function popupDelete(id, idCSRF)
            {
//                if(localStorage.getItem("storeAnticsrfTokenList") !== null && localStorage.getItem("storeAnticsrfTokenList") !== ""
//                     && localStorage.getItem("storeAnticsrfTokenList") !== "null") {
//                    idCSRF = sSpace(localStorage.getItem("storeAnticsrfTokenList"));
//                }
//                swal({
//                    title: "",
//                    text: token_confirm_delete,
//                    imageUrl: "../Images/icon_warning.png",
//                    imageSize: "45x45",
//                    showCancelButton: true,
//                    closeOnConfirm: true,
//                    allowOutsideClick: false,
//                    confirmButtonText: login_fm_buton_OK,
//                    cancelButtonText: global_button_grid_cancel,
//                    cancelButtonColor: "#199DC0"
//                },
//                function () {
//                    $('body').append('<div id="over"></div>');
//                    $(".loading-gif").show();
//                    $.ajax({
//                        type: "post",
//                        url: "../TokenCommon",
//                        data: {
//                            idParam: 'deletetoken',
//                            ID: id,
//                            CsrfToken: idCSRF
//                        },
//                        cache: false,
//                        success: function (html) {
//                            var arr = sSpace(html).split('#');
//                            if (arr[0] === "0") {
//                                funSuccLocalAlert(token_succ_delete);
//                            }
//                            else if (arr[0] === JS_EX_CSRF) {
//                                funCsrfAlert();
//                            }
//                            else if (arr[0] === JS_EX_LOGIN)
//                            {
//                                RedirectPageLoginNoSess(global_alert_login);
//                            }
//                            else if (arr[0] === JS_EX_ANOTHERLOGIN)
//                            {
//                                RedirectPageLoginNoSess(global_alert_another_login);
//                            }
//                            else if (arr[0] === JS_EX_WRONG_ROLE) {
//                                RedirectPageLoginNoSess(global_error_wrong_role);
//                            }
//                            else
//                            {
//                                funErrorAlert(global_errorsql);
//                            }
//                            $(".loading-gif").hide();
//                            $('#over').remove();
//                        }
//                    });
//                    return false;
//                });
            }
            function popupaddInit(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('InitToken.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
//                localStorage.setItem("LOCAL_PARAM_TOKENLIST", id);
//                window.location = 'InitToken.jsp?id=' + id;
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
                    localStorage.setItem("CountCheckTokenList", "");
                    localStorage.setItem("CountCheckAllTokenList", "");
                    var f = document.form;
                    f.method = "post";
                    f.action = '';
                    f.submit();
                }
            }
            function downloadSampleToken()
            {
                var f = document.form;
                f.method = "post";
                f.action = '../DownFromSaveFile?idParam=downfilesamplesim';
                f.submit();
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
            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
            .table > thead > tr > th{border-bottom: none;}
            .btn{margin-bottom: 0px;}
            .x_panel{padding: 10px 15px 0px 15px;}
            .x_content{padding: 3px 5px 3px 5px;}
            .col-sm-5{padding-right: 5px;}
        </style>
    </head>
    <body class="nav-md">
        <%
        if ((session.getAttribute("sUserID")) != null) {
            ROLE_DATA[][] sessFunctionToken = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Token");
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
            String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
            String sessTreeArrayBranchID = session.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
            String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
            String SessRoleID = session.getAttribute("RoleID_ID").toString().trim();
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
                        document.getElementById("idNameURL").innerHTML = token_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                        <%
                                int status = 1000;
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
                                String isLoadPeronalPrefix = "";
                                String isLoadEnterprisePrefix = "";
                                String sEnterpriseCert = "";
                                String sPersonalCert = "";
                                try {
                                    TOKEN[][] rsPgin = new TOKEN[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (session.getAttribute("RefreshTokenSess") != null && session.getAttribute("sessTOKEN_ID") != null
                                            && session.getAttribute("sessTOKEN_STATE") != null) {
                                        session.setAttribute("RefreshTokenSessPaging", "1");
                                        session.setAttribute("SearchSharePagingToken", "0");
                                        statusLoad = 1;
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSUserlist");
                                        String FromDateValid = (String) session.getAttribute("FromCreateDateSTokenlist");
                                        String ToDateValid = (String) session.getAttribute("ToCreateDateSTokenlist");
                                        String TOKEN_ID = (String) session.getAttribute("sessTOKEN_ID");
                                        String TOKEN_STATE = (String) session.getAttribute("sessTOKEN_STATE");
                                        String TOKEN_VERSION = (String) session.getAttribute("sessTOKEN_VERSION");
                                        String AGENT_ID = (String) session.getAttribute("sessAGENT_ID");
                                        String TAX_CODE = (String) session.getAttribute("sessTAX_CODESTokenlist");
                                        String BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODESTokenlist");
                                        String DECISION = (String) session.getAttribute("sessDECISIONSTokenlist");
                                        String P_ID = (String) session.getAttribute("sessP_IDSTokenlist");
                                        String CCCD = (String) session.getAttribute("sessCCCDSTokenlist");
                                        String PASSPORT = (String) session.getAttribute("sessPASSPORTSTokenlist");
                                        String TOKEN_SIGNED = (String) session.getAttribute("sessTOKEN_SIGNED");
                                        String EMAIL_CONTACT = (String) session.getAttribute("sessEMAIL_CONTACTSTokenlist");
                                        String PHONE_CONTACT = (String) session.getAttribute("sessPHONE_CONTACTSTokenlist");
                                        session.setAttribute("FromCreateDateSTokenlist", FromDateValid);
                                        session.setAttribute("ToCreateDateSTokenlist", ToDateValid);
                                        session.setAttribute("sessTOKEN_ID", TOKEN_ID);
                                        session.setAttribute("sessTOKEN_STATE", TOKEN_STATE);
                                        session.setAttribute("sessTOKEN_VERSION", TOKEN_VERSION);
                                        session.setAttribute("sessAGENT_ID", AGENT_ID);
                                        session.setAttribute("RefreshTokenSess", null);
                                        session.setAttribute("AlertAllTimeSUserlist", strAlertAllTimes);
                                        session.setAttribute("sessTAX_CODESTokenlist", TAX_CODE);
                                        session.setAttribute("sessBUDGET_CODESTokenlist", BUDGET_CODE);
                                        session.setAttribute("sessDECISIONSTokenlist", DECISION);
                                        session.setAttribute("sessP_IDSTokenlist", P_ID);
                                        session.setAttribute("sessCCCDSTokenlist", CCCD);
                                        session.setAttribute("sessPASSPORTSTokenlist", PASSPORT);
                                        session.setAttribute("sessTOKEN_SIGNED", TOKEN_SIGNED);
                                        session.setAttribute("sessEMAIL_CONTACTSTokenlist", EMAIL_CONTACT);
                                        session.setAttribute("sessPHONE_CONTACTSTokenlist", PHONE_CONTACT);
                                        if(isUIDCollection.equals("1")) {
                                            String sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDTokenlist");
                                            String sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDTokenlist");
                                            String sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXTokenlist");
                                            String sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXTokenlist");
                                            session.setAttribute("sessENTERPRISE_IDTokenlist", sENTERPRISE_ID);
                                            session.setAttribute("sessPERSONAL_IDTokenlist", sPERSONAL_ID);
                                            session.setAttribute("sessENTERPRISE_PREFIXTokenlist", sENTERPRISE_PREFIX);
                                            session.setAttribute("sessPERSONAL_PREFIXTokenlist", sPERSONAL_PREFIX);
                                            isLoadEnterprisePrefix = sENTERPRISE_PREFIX;
                                            isLoadPeronalPrefix = sPERSONAL_PREFIX;
                                            if(!"".equals(sENTERPRISE_ID)){
                                                sEnterpriseCert = sENTERPRISE_PREFIX + sENTERPRISE_ID;
                                            }
                                            if(!"".equals(sPERSONAL_ID)){
                                                sPersonalCert = sPERSONAL_PREFIX + sPERSONAL_ID;
                                            }
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_STATE)) {
                                            TOKEN_STATE = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_VERSION)) {
                                            TOKEN_VERSION = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_SIGNED)) {
                                            TOKEN_SIGNED = "";
                                        }
                                        if (!Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                            AGENT_ID = SessUserAgentID;
                                        } else {
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(AGENT_ID)) {
                                                AGENT_ID = "";
                                            }
                                        }
                                        if("1".equals(strAlertAllTimes)) {
                                            FromDateValid = "";
                                            ToDateValid = "";
                                        }
                                        if(SessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            sessTreeArrayBranchID = "";
                                        }
                                        if(!isUIDCollection.equals("1")) {
                                            String[] sUIDResult = new String[2];
                                            CommonReferServlet.collectFieldToUID(EscapeUtils.escapeHtmlSearch(TAX_CODE), EscapeUtils.escapeHtmlSearch(BUDGET_CODE),
                                                EscapeUtils.escapeHtmlSearch(DECISION), EscapeUtils.escapeHtmlSearch(P_ID),
                                                EscapeUtils.escapeHtmlSearch(PASSPORT), EscapeUtils.escapeHtmlSearch(CCCD), sUIDResult);
                                            sEnterpriseCert = sUIDResult[0];
                                            sPersonalCert = sUIDResult[1];
                                        }
                                        int ss = 0;
                                        ss = db.S_BO_TOKEN_TOTAL(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
                                            EscapeUtils.escapeHtmlSearch(TOKEN_ID), EscapeUtils.escapeHtmlSearch(TOKEN_STATE),
                                            EscapeUtils.escapeHtmlSearch(TOKEN_VERSION),
                                            EscapeUtils.escapeHtmlSearch(AGENT_ID), TAX_CODE, EscapeUtils.escapeHtmlSearch(BUDGET_CODE),
                                            P_ID, PASSPORT, CCCD, EscapeUtils.escapeHtmlSearch(TOKEN_SIGNED),
                                            EscapeUtils.escapeHtmlSearch(PHONE_CONTACT), EscapeUtils.escapeHtmlSearch(EMAIL_CONTACT),
                                            sessTreeArrayBranchID, DECISION, sEnterpriseCert, sPersonalCert);
                                        if (session.getAttribute("IPageNoSessSearchToken") != null) {
                                            String sPage = (String) session.getAttribute("IPageNoSessSearchToken");
                                            iPagNo = Integer.parseInt(sPage);
                                        }
                                        if (session.getAttribute("ISwRwsSessSearchToken") != null) {
                                            String sSumPage = (String) session.getAttribute("ISwRwsSessSearchToken");
                                            iSwRws = Integer.parseInt(sSumPage);
                                        }
                                        if (session.getAttribute("NumPageSessSearchToken") != null) {
                                            String sNoPage = (String) session.getAttribute("NumPageSessSearchToken");
                                            iPaNoSS = Integer.parseInt(sNoPage);
                                        }
                                        session.setAttribute("IPageNoSessSearchToken", String.valueOf(iPagNo));
                                        session.setAttribute("ISwRwsSessSearchToken", String.valueOf(iSwRws));
                                        if (ss > 0) {
                                            db.S_BO_TOKEN_LIST(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
                                                EscapeUtils.escapeHtmlSearch(TOKEN_ID), EscapeUtils.escapeHtmlSearch(TOKEN_STATE),
                                                EscapeUtils.escapeHtmlSearch(TOKEN_VERSION), EscapeUtils.escapeHtmlSearch(AGENT_ID), sessLanguageGlobal,
                                                rsPgin, iPagNo, iSwRws, TAX_CODE, BUDGET_CODE, P_ID, PASSPORT,
                                                CCCD, EscapeUtils.escapeHtmlSearch(TOKEN_SIGNED),
                                                EscapeUtils.escapeHtmlSearch(PHONE_CONTACT), EscapeUtils.escapeHtmlSearch(EMAIL_CONTACT),
                                                sessTreeArrayBranchID, DECISION, sEnterpriseCert, sPersonalCert);
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
                                        session.setAttribute("RefreshTokenSessPaging", null);
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("SearchShareStoreToken", null);
                                            session.setAttribute("SearchIPageNoPagingToken", null);
                                            session.setAttribute("SearchISwRwsPagingToken", null);
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
                                    String FromDateValid = request.getParameter("FromCreateDate");
                                    String ToDateValid = request.getParameter("ToCreateDate");
                                    String TOKEN_ID = request.getParameter("TOKEN_ID");
                                    String TOKEN_STATE = request.getParameter("TOKEN_STATE");
                                    String TOKEN_VERSION = request.getParameter("TOKEN_VERSION");
                                    String AGENT_ID = request.getParameter("AGENT_ID");
                                    String TAX_CODE = "";
                                    String BUDGET_CODE ="";
                                    String DECISION = "";
                                    String P_ID = "";
                                    String CCCD = "";
                                    String PASSPORT = "";
                                    String TOKEN_SIGNED = request.getParameter("TOKEN_SIGNED");
                                    String EMAIL_CONTACT = request.getParameter("EMAIL_CONTACT");
                                    String PHONE_CONTACT = request.getParameter("PHONE_CONTACT");
                                    Boolean nameCheck = Boolean.valueOf(request.getParameter("nameCheck") != null);
                                    if (nameCheck == false) {
                                        FromDateValid = "";
                                        ToDateValid = "";
                                        strAlertAllTimes = "1";
                                    }
                                    if(!isUIDCollection.equals("1")) {
                                        TAX_CODE = EscapeUtils.ConvertStringToUnicode(request.getParameter("TAX_CODE"));
                                        BUDGET_CODE = EscapeUtils.ConvertStringToUnicode(request.getParameter("BUDGET_CODE"));
                                        P_ID = EscapeUtils.ConvertStringToUnicode(request.getParameter("P_ID"));
                                        CCCD = EscapeUtils.ConvertStringToUnicode(request.getParameter("CCCD_SEARCH"));
                                        PASSPORT = EscapeUtils.ConvertStringToUnicode(request.getParameter("PASSPORT"));
                                        DECISION = EscapeUtils.ConvertStringToUnicode(request.getParameter("DECISION"));
                                    } else {
                                        String sENTERPRISE_ID = request.getParameter("ENTERPRISE_ID");
                                        String sPERSONAL_ID = request.getParameter("PERSONAL_ID");
                                        String sENTERPRISE_PREFIX = EscapeUtils.ConvertStringToUnicode(request.getParameter("ENTERPRISE_PREFIX"));
                                        String sPERSONAL_PREFIX = EscapeUtils.ConvertStringToUnicode(request.getParameter("PERSONAL_PREFIX"));
                                        if ("1".equals(hasPaging)) {
                                            sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXTokenlist");
                                            sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXTokenlist");
                                            sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDTokenlist");
                                            sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDTokenlist");
                                        }
                                        session.setAttribute("sessENTERPRISE_IDTokenlist", sENTERPRISE_ID);
                                        session.setAttribute("sessPERSONAL_IDTokenlist", sPERSONAL_ID);
                                        session.setAttribute("sessENTERPRISE_PREFIXTokenlist", sENTERPRISE_PREFIX);
                                        session.setAttribute("sessPERSONAL_PREFIXTokenlist", sPERSONAL_PREFIX);
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
                                        session.setAttribute("SearchSharePagingToken", "0");
                                        FromDateValid = (String) session.getAttribute("FromCreateDateSTokenlist");
                                        ToDateValid = (String) session.getAttribute("ToCreateDateSTokenlist");
                                        TOKEN_ID = (String) session.getAttribute("sessTOKEN_ID");
                                        TOKEN_STATE = (String) session.getAttribute("sessTOKEN_STATE");
                                        TOKEN_VERSION = (String) session.getAttribute("sessTOKEN_VERSION");
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSUserlist");
                                        AGENT_ID = (String) session.getAttribute("sessAGENT_ID");
                                        TAX_CODE = (String) session.getAttribute("sessTAX_CODESTokenlist");
                                        BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODESTokenlist");
                                        DECISION = (String) session.getAttribute("sessDECISIONSTokenlist");
                                        P_ID = (String) session.getAttribute("sessP_IDSTokenlist");
                                        CCCD = (String) session.getAttribute("sessCCCDSTokenlist");
                                        PASSPORT = (String) session.getAttribute("sessPASSPORTSTokenlist");
                                        TOKEN_SIGNED = (String) session.getAttribute("sessTOKEN_SIGNED");
                                        EMAIL_CONTACT = (String) session.getAttribute("sessEMAIL_CONTACTSTokenlist");
                                        PHONE_CONTACT = (String) session.getAttribute("sessPHONE_CONTACTSTokenlist");
                                        session.setAttribute("SessParamOnPagingTokenList", null);
                                    } else {
                                        session.setAttribute("SearchSharePagingToken", "1");
                                        session.setAttribute("CountListToken", null);
                                    }
                                    session.setAttribute("FromCreateDateSTokenlist", FromDateValid);
                                    session.setAttribute("ToCreateDateSTokenlist", ToDateValid);
                                    session.setAttribute("sessTOKEN_ID", TOKEN_ID);
                                    session.setAttribute("sessTOKEN_STATE", TOKEN_STATE);
                                    session.setAttribute("sessTOKEN_VERSION", TOKEN_VERSION);
                                    session.setAttribute("sessAGENT_ID", AGENT_ID);
                                    session.setAttribute("AlertAllTimeSUserlist", strAlertAllTimes);
                                    session.setAttribute("sessTAX_CODESTokenlist", TAX_CODE);
                                    session.setAttribute("sessBUDGET_CODESTokenlist", BUDGET_CODE);
                                    session.setAttribute("sessDECISIONSTokenlist", DECISION);
                                    session.setAttribute("sessP_IDSTokenlist", P_ID);
                                    session.setAttribute("sessCCCDSTokenlist", CCCD);
                                    session.setAttribute("sessPASSPORTSTokenlist", PASSPORT);
                                    session.setAttribute("sessTOKEN_SIGNED", TOKEN_SIGNED);
                                    session.setAttribute("sessEMAIL_CONTACTSTokenlist", EMAIL_CONTACT);
                                    session.setAttribute("sessPHONE_CONTACTSTokenlist", PHONE_CONTACT);
                                    
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_STATE)) {
                                        TOKEN_STATE = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_VERSION)) {
                                        TOKEN_VERSION = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_SIGNED)) {
                                        TOKEN_SIGNED = "";
                                    }
                                    if (!Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                        AGENT_ID = SessUserAgentID;
                                    } else {
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(AGENT_ID)) {
                                            AGENT_ID = "";
                                        }
                                    }
                                    if(SessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                        sessTreeArrayBranchID = "";
                                    }
                                    if(!isUIDCollection.equals("1")) {
                                        String[] sUIDResult = new String[2];
                                        CommonReferServlet.collectFieldToUID(EscapeUtils.escapeHtmlSearch(TAX_CODE), EscapeUtils.escapeHtmlSearch(BUDGET_CODE),
                                            EscapeUtils.escapeHtmlSearch(DECISION), EscapeUtils.escapeHtmlSearch(P_ID),
                                            EscapeUtils.escapeHtmlSearch(PASSPORT), EscapeUtils.escapeHtmlSearch(CCCD), sUIDResult);
                                        sEnterpriseCert = sUIDResult[0];
                                        sPersonalCert = sUIDResult[1];
                                    }
                                    int ss = 0;
                                    if ((session.getAttribute("CountListToken")) == null) {
                                        ss = db.S_BO_TOKEN_TOTAL(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
                                            EscapeUtils.escapeHtmlSearch(TOKEN_ID), EscapeUtils.escapeHtmlSearch(TOKEN_STATE),
                                            EscapeUtils.escapeHtmlSearch(TOKEN_VERSION), EscapeUtils.escapeHtmlSearch(AGENT_ID),
                                            TAX_CODE, BUDGET_CODE, P_ID, PASSPORT, CCCD, EscapeUtils.escapeHtmlSearch(TOKEN_SIGNED),
                                            EscapeUtils.escapeHtmlSearch(PHONE_CONTACT), EscapeUtils.escapeHtmlSearch(EMAIL_CONTACT),
                                            sessTreeArrayBranchID, DECISION, sEnterpriseCert, sPersonalCert);
                                        session.setAttribute("CountListToken", String.valueOf(ss));
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListToken");
                                        ss = Integer.parseInt(sCount);
                                        session.setAttribute("CountListToken", String.valueOf(ss));
                                    }
                                    iTotRslts = ss;
                                    iTotRslts = ss;
                                    if (iTotRslts > 0) {
                                        db.S_BO_TOKEN_LIST(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
                                            EscapeUtils.escapeHtmlSearch(TOKEN_ID), EscapeUtils.escapeHtmlSearch(TOKEN_STATE),
                                            EscapeUtils.escapeHtmlSearch(TOKEN_VERSION), EscapeUtils.escapeHtmlSearch(AGENT_ID), sessLanguageGlobal,
                                            rsPgin, iPagNo, iSwRws, TAX_CODE, BUDGET_CODE, P_ID, PASSPORT,
                                            CCCD, EscapeUtils.escapeHtmlSearch(TOKEN_SIGNED),
                                            EscapeUtils.escapeHtmlSearch(PHONE_CONTACT), EscapeUtils.escapeHtmlSearch(EMAIL_CONTACT),
                                            sessTreeArrayBranchID, DECISION, sEnterpriseCert, sPersonalCert);
                                        session.setAttribute("IPageNoSessSearchToken", String.valueOf(iPagNo));
                                        session.setAttribute("ISwRwsSessSearchToken", String.valueOf(iSwRws));
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
                                    session.setAttribute("RefreshTokenSessPaging", null);
                                    session.setAttribute("SearchShareStoreToken", null);
                                    session.setAttribute("SearchIPageNoPagingToken", null);
                                    session.setAttribute("SearchISwRwsPagingToken", null);
                                    session.setAttribute("FromCreateDateSTokenlist", null);
                                    session.setAttribute("ToCreateDateSTokenlist", null);
                                    session.setAttribute("sessTOKEN_ID", null);
                                    session.setAttribute("sessTAX_CODESTokenlist", null);
                                    session.setAttribute("sessBUDGET_CODESTokenlist", null);
                                    session.setAttribute("sessDECISIONSTokenlist", null);
                                    session.setAttribute("sessP_IDSTokenlist", null);
                                    session.setAttribute("sessCCCDSTokenlist", null);
                                    session.setAttribute("sessPASSPORTSTokenlist", null);
                                    session.setAttribute("sessTOKEN_STATE", null);
                                    session.setAttribute("sessTOKEN_VERSION", null);
                                    session.setAttribute("sessAGENT_ID", null);
                                    session.setAttribute("sessTOKEN_SIGNED", null);
                                    session.setAttribute("sessEMAIL_CONTACTSTokenlist", null);
                                    session.setAttribute("sessPHONE_CONTACTSTokenlist", null);
                                    if(isUIDCollection.equals("1")) {
                                        session.setAttribute("sessENTERPRISE_IDTokenlist", null);
                                        session.setAttribute("sessPERSONAL_IDTokenlist", null);
                                        session.setAttribute("sessENTERPRISE_PREFIXTokenlist", null);
                                        session.setAttribute("sessPERSONAL_PREFIXTokenlist", null);
                                    }
                                %>
                            <script>
                                $(document).ready(function () {
                                    localStorage.setItem('StoreIsDate', '');
                                });
                            </script>
                            <%
                                }
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="form" method="post" class="form-horizontal">
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
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <div class="col-sm-5" style="padding-right: 0px;text-align: right;">
                                                            <label class="switch" for="idCheck">
                                                                <input type="checkbox" name="nameCheck" id="idCheck" onchange="checkboxChange();" <%= session.getAttribute("AlertAllTimeSUserlist") != null && "1".equals(session.getAttribute("AlertAllTimeSUserlist").toString()) ? "" : "checked" %>/>
                                                                <div class="slider round"></div>
                                                            </label>
                                                        </div>
                                                        <label class="control-label col-sm-7" style="color: #000000; font-weight: bold;text-align: left;"><script>document.write(global_fm_check_date);</script></label>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_FromDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo1" name="FromCreateDate" <%= session.getAttribute("AlertAllTimeSUserlist") != null && "1".equals(session.getAttribute("AlertAllTimeSUserlist").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("FromCreateDateSTokenlist") != null && !"1".equals(session.getAttribute("AlertAllTimeSUserlist").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("FromCreateDateSTokenlist").toString()) : com.ConvertMonthSub(30)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_ToDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo2" name="ToCreateDate" <%= session.getAttribute("AlertAllTimeSUserlist") != null && "1".equals(session.getAttribute("AlertAllTimeSUserlist").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("ToCreateDateSTokenlist") != null && !"1".equals(session.getAttribute("AlertAllTimeSUserlist").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("ToCreateDateSTokenlist").toString()) : com.ConvertMonthSub(0)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(token_fm_tokenid);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="TOKEN_ID" name="TOKEN_ID" maxlength="45" class="form-control123"
                                                                value="<%= session.getAttribute("sessTOKEN_ID") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessTOKEN_ID").toString()) : ""%>"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_Status_token);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="TOKEN_STATE" id="TOKEN_STATE" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessTOKEN_STATE") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessTOKEN_STATE").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    TOKEN_STATE[][] rstState = new TOKEN_STATE[1][];
                                                                    db.S_BO_TOKEN_STATE_COMBOBOX(sessLanguageGlobal, rstState);
                                                                    if (rstState[0].length > 0) {
                                                                        for (TOKEN_STATE temp1 : rstState[0]) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessTOKEN_STATE") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessTOKEN_STATE").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(token_fm_version);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="TOKEN_VERSION" id="TOKEN_VERSION" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessTOKEN_VERSION") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessTOKEN_VERSION").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    TOKEN_VERSION[][] rstTOKEN_VERSION = new TOKEN_VERSION[1][];
                                                                    db.S_BO_TOKEN_VERSION_COMBOBOX(sessLanguageGlobal, rstTOKEN_VERSION);
                                                                    if (rstTOKEN_VERSION[0].length > 0) {
                                                                        for (TOKEN_VERSION temp1 : rstTOKEN_VERSION[0]) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessTOKEN_VERSION") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessTOKEN_VERSION").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                                <%
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <%
                                                if(isUIDCollection.equals("1")) {
                                            %>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
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
                                                            <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessENTERPRISE_PREFIXTokenlist") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessENTERPRISE_PREFIXTokenlist").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                            <input type="text" name="ENTERPRISE_ID" maxlength="45" value="<%= session.getAttribute("sessENTERPRISE_IDTokenlist") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessENTERPRISE_IDTokenlist").toString()) : ""%>"
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
                                                            <script>document.write(global_fm_email);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="EMAIL_CONTACT" maxlength="150" value="<%= session.getAttribute("sessEMAIL_CONTACTSTokenlist") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessEMAIL_CONTACTSTokenlist").toString()) : ""%>"
                                                                   class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
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
                                                            <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessPERSONAL_PREFIXTokenlist") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessPERSONAL_PREFIXTokenlist").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                            <input type="text" name="PERSONAL_ID" maxlength="45" value="<%= session.getAttribute("sessPERSONAL_IDTokenlist") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_IDTokenlist").toString()) : ""%>"
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
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_phone);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="PHONE_CONTACT" maxlength="16" value="<%= session.getAttribute("sessPHONE_CONTACTSTokenlist") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPHONE_CONTACTSTokenlist").toString()) : ""%>"
                                                                   class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <%
                                                } else {
                                            %>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_MST);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="TAX_CODE" maxlength="45" value="<%= session.getAttribute("sessTAX_CODESTokenlist") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessTAX_CODESTokenlist").toString()) : ""%>"
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
                                                            <input type="text" name="BUDGET_CODE" maxlength="45" value="<%= session.getAttribute("sessBUDGET_CODESTokenlist") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessBUDGET_CODESTokenlist").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_decision);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="DECISION" maxlength="45" value="<%= session.getAttribute("sessDECISIONSTokenlist") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessDECISIONSTokenlist").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_CMND);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="P_ID" maxlength="32" value="<%= session.getAttribute("sessP_IDSTokenlist") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessP_IDSTokenlist").toString()) : ""%>"
                                                                   class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_CitizenId);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="CCCD" maxlength="32" value="<%= session.getAttribute("sessCCCDSTokenlist") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCCCDSTokenlist").toString()) : ""%>"
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
                                                            <input type="text" name="PASSPORT" maxlength="32" value="<%= session.getAttribute("sessPASSPORTSTokenlist") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPASSPORTSTokenlist").toString()) : ""%>"
                                                                   class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_email);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="EMAIL_CONTACT" maxlength="150" value="<%= session.getAttribute("sessEMAIL_CONTACTSTokenlist") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessEMAIL_CONTACTSTokenlist").toString()) : ""%>"
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
                                                        <input type="text" name="PHONE_CONTACT" maxlength="16" value="<%= session.getAttribute("sessPHONE_CONTACTSTokenlist") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPHONE_CONTACTSTokenlist").toString()) : ""%>"
                                                               class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <%
                                                }
                                            %>
                                            
                                            <%
                                                Config conf = new Config();
                                                String sViewSigned = "none";
                                                String sSignedEnabled = conf.GetPropertybyCode(Definitions.CONFIG_SIGNED_OF_TOKEN_ENABLED);
                                                if("1".equals(sSignedEnabled)) {
                                                    sViewSigned = "";
                                                }
                                            %>
                                            <div class="col-sm-4" style="padding-left: 0;display: <%=sViewSigned%>">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_Status_signed);</script></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="TOKEN_SIGNED" id="TOKEN_SIGNED" class="form-control123">
                                                            <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessTOKEN_SIGNED") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessTOKEN_SIGNED").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            <option value="1" <%= session.getAttribute("sessTOKEN_SIGNED") != null && "1".equals(session.getAttribute("sessTOKEN_SIGNED").toString()) ? "selected" : ""%>><script>document.write(global_fm_apply_signed);</script></option>
                                                            <option value="0" <%= session.getAttribute("sessTOKEN_SIGNED") != null && "0".equals(session.getAttribute("sessTOKEN_SIGNED").toString()) ? "selected" : ""%>><script>document.write(global_fm_unapply_signed);</script></option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;<%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR) ? "display:none;" : "" %>">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_Branch);</script></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="AGENT_ID" id="AGENT_ID" class="form-control123">
                                                            <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessAGENT_ID") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessAGENT_ID").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            <%
                                                                BRANCH[][] rst = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                                                                if (rst[0].length > 0) {
                                                                    for (BRANCH temp1 : rst[0]) {
                                                            %>
                                                            <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessAGENT_ID") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessAGENT_ID").toString()) ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                            <%
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group" style="text-align: right;">
                                                        <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                        <input id="btnNew" type="button" class="btn btn-info" onclick="addForm();"/>
                                                        <script>
                                                            document.getElementById("btnNew").value = global_fm_button_New;
                                                        </script>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">

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
                                <div class="x_panel" id="divEditTokenMulti" style="display: none;">
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="myname" method="post" class="form-horizontal">
                                            <fieldset class="scheduler-border">
                                                <legend class="scheduler-border"><script>document.write(tokenimport_title_multi);</script></legend>
                                                <fieldset class="scheduler-border">
                                                    <legend class="scheduler-border"><script>document.write(token_group_notification);</script></legend>
                                                    <%
//                                                        if (CommonFunction.StrCheckRoleUser(SessRoleSetFuns, Definitions.CONFIG_FUNROLE_PUSH) == true) {
                                                    %>
                                                    <div class="form-group" style="padding-left: 10px;">
                                                        <input TYPE="checkbox" id="strIS_PUSH_NOTICE" name="strIS_PUSH_NOTICE" onchange="clickCheckIS_PUSH_NOTICE();"/>&nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(token_fm_noticepush);</script></label>
                                                        <input TYPE="text" id="sIS_PUSH_NOTICE" style="display: none;" value="1"/>
                                                    </div>
                                                    <script>
                                                        function clickCheckIS_PUSH_NOTICE()
                                                        {
                                                            if ($("#strIS_PUSH_NOTICE").is(':checked'))
                                                            {
                                                                document.getElementById("strCOLOR_TEXT").readOnly = false;
                                                                $("#strCOLOR_TEXT").addClass('color');
                                                                document.getElementById("strCOLOR_BKGR").readOnly = false;
                                                                $("#strCOLOR_BKGR").addClass('color');
                                                                document.getElementById("strLINK_NOTICE").readOnly = false;
                                                                document.getElementById("strNOTICE_INFO").readOnly = false;
                                                            }
                                                            else
                                                            {
                                                                document.getElementById("strCOLOR_TEXT").readOnly = true;
                                                                $("#strCOLOR_TEXT").removeClass('color');
                                                                document.getElementById("strCOLOR_BKGR").readOnly = true;
                                                                $("#strCOLOR_TEXT").removeClass('color');
                                                                document.getElementById("strLINK_NOTICE").readOnly = true;
                                                                document.getElementById("strNOTICE_INFO").readOnly = true;
                                                            }
                                                        }
                                                    </script>
                                                    <%
//                                                    } else {
                                                    %>
                                                    <input TYPE="text" id="sIS_PUSH_NOTICE" style="display: none;" value="0"/>
                                                    <%
//                                                        }
                                                    %>
                                                    <div class="form-group">
                                                        <div class="col-sm-6">
                                                            <label class="control-label123"><script>document.write(token_fm_colortext);</script></label>
                                                            <input type="Text" id="strCOLOR_TEXT" name="strCOLOR_TEXT" readonly class="form-control123 color"/>
                                                        </div>
                                                        <div class="col-sm-6">
                                                            <label class="control-label123"><script>document.write(token_fm_colorgkgd);</script></label>
                                                            <input type="Text" id="strCOLOR_BKGR" name="strCOLOR_BKGR" readonly class="form-control123 color"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <div class="col-sm-6">
                                                            <label class="control-label123"><script>document.write(token_fm_noticelink);</script></label>
                                                            <textarea type="Text" id="strLINK_NOTICE" name="strLINK_NOTICE" readonly style="height: 50px;" class="form-control123"></textarea>
                                                        </div>
                                                        <div class="col-sm-6">
                                                            <label class="control-label123"><script>document.write(token_fm_noticeinfor);</script></label>
                                                            <textarea type="Text" id="strNOTICE_INFO" name="strNOTICE_INFO" readonly style="height: 50px;" class="form-control123"></textarea>
                                                        </div>
                                                    </div>
                                                </fieldset>
                                                <fieldset class="scheduler-border">
                                                    <legend class="scheduler-border"><script>document.write(token_group_dynamic);</script></legend>
                                                    <%
//                                                        if (CommonFunction.StrCheckRoleUser(SessRoleSetFuns, Definitions.CONFIG_FUNROLE_DYNAMIC) == true) {
                                                    %>
                                                    <div class="form-group" style="padding-left: 10px;">
                                                        <input TYPE="checkbox" id="strIS_MENU_LINK" name="strIS_MENU_LINK" onchange="clickCheckIS_MENU_LINK();" />&nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(token_fm_menulink);</script></label>
                                                        <input TYPE="text" id="sIS_MENU_LINK" style="display: none;" value="1"/>
                                                    </div>
                                                    <script>
                                                        function clickCheckIS_MENU_LINK()
                                                        {
                                                            if ($("#strIS_MENU_LINK").is(':checked'))
                                                            {
                                                                document.getElementById("strNAME_LINK").readOnly = false;
                                                                document.getElementById("strLINK_VALUE").readOnly = false;
                                                            }
                                                            else
                                                            {
                                                                document.getElementById("strNAME_LINK").readOnly = true;
                                                                document.getElementById("strLINK_VALUE").readOnly = true;
                                                            }
                                                        }
                                                    </script>
                                                    <%
//                                                    } else {
                                                    %>
                                                    <input TYPE="text" id="sIS_MENU_LINK" style="display: none;" value="0"/>
                                                    <%
//                                                        }
                                                    %>
                                                    <div class="form-group">
                                                        <div class="col-sm-6">
                                                            <label class="control-label123"><script>document.write(token_fm_linkname);</script></label>
                                                            <textarea type="Text" id="strNAME_LINK" readonly name="strNAME_LINK" class="form-control123"></textarea>
                                                        </div>
                                                        <div class="col-sm-6">
                                                            <label class="control-label123"><script>document.write(token_fm_linkvalue);</script></label>
                                                            <textarea type="Text" id="strLINK_VALUE" readonly name="strLINK_VALUE" class="form-control123"></textarea>
                                                        </div>
                                                    </div>
                                                </fieldset>
                                                <fieldset class="scheduler-border">
                                                    <legend class="scheduler-border"><script>document.write(token_group_other);</script></legend>
                                                    <div class="form-group" style="padding-left: 10px;">
                                                        <%
//                                                            if (CommonFunction.StrCheckRoleUser(SessRoleSetFuns, Definitions.CONFIG_FUNROLE_LOCK) == true) {
                                                        %>
                                                        <div class="col-sm-4" style="padding-left: 0;">
                                                            <input TYPE="checkbox" id="strIS_LOCK" name="strIS_LOCK"/>
                                                            &nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(token_fm_block);</script></label>
                                                            <input TYPE="text" id="sIS_LOCK" style="display: none;" value="1"/>
                                                        </div>
                                                        <%
//                                                        } else {
                                                        %>
                                                        <input TYPE="text" id="sIS_LOCK" style="display: none;" value="0"/>
                                                        <%
//                                                            }
                                                        %>
                                                        <%
//                                                            if (CommonFunction.StrCheckRoleUser(SessRoleSetFuns, Definitions.CONFIG_FUNROLE_UNLOCK) == true) {
                                                        %>
                                                        <div class="col-sm-4" style="padding-left: 0;">
                                                            <input TYPE="checkbox" id="strIS_UNLOCK" name="strIS_UNLOCK"/>
                                                            &nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(token_fm_unblock);</script></label>
                                                            <input TYPE="text" id="sIS_UNLOCK" style="display: none;" value="1"/>
                                                        </div>
                                                        <%
//                                                        } else {
                                                        %>
                                                        <input TYPE="text" id="sIS_UNLOCK" style="display: none;" value="0"/>
                                                        <%
//                                                            }
                                                        %>
                                                        <%
//                                                            if (CommonFunction.StrCheckRoleUser(SessRoleSetFuns, Definitions.CONFIG_FUNROLE_INITIALIZE) == true) {
                                                        %>
                                                        <div class="col-sm-4" style="padding-left: 0;">
                                                            <input TYPE="checkbox" id="strIS_INITIALIZE" name="strIS_INITIALIZE"/>
                                                            &nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(token_fm_innit);</script></label>
                                                            <input TYPE="text" id="sIS_INITIALIZE" style="display: none;" value="1"/>
                                                        </div>
                                                        <%
//                                                        } else {
                                                        %>
                                                        <input TYPE="text" id="sIS_INITIALIZE" style="display: none;" value="0"/>
                                                        <%
//                                                            }
                                                        %>
                                                        <%
//                                                            if (CommonFunction.StrCheckRoleUser(SessRoleSetFuns, Definitions.CONFIG_FUNROLE_SOPIN) == true) {
                                                        %>
                                                        <div class="col-sm-4" style="padding-left: 0;">
                                                            <input TYPE="checkbox" id="strIS_CHANGE_SOPIN" name="strIS_CHANGE_SOPIN"/>
                                                            &nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(token_fm_change);</script></label>
                                                            <input TYPE="text" id="sIS_CHANGE_SOPIN" style="display: none;" value="1"/>
                                                        </div>
                                                        <%
//                                                        } else {
                                                        %>
                                                        <input TYPE="text" id="sIS_CHANGE_SOPIN" style="display: none;" value="0"/>
                                                        <%
//                                                            }
                                                        %>
                                                        <%
//                                                            if (CommonFunction.StrCheckRoleUser(SessRoleSetFuns, Definitions.CONFIG_FUNROLE_ACTIVE) == true) {
                                                        %>
                                                        <div class="col-sm-4" style="padding-left: 0;">
                                                            <input TYPE="checkbox" id="strACTIVE_FLAG" name="strACTIVE_FLAG" checked/>
                                                            &nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(global_fm_active);</script></label>
                                                            <input TYPE="text" id="sACTIVE_FLAG" style="display: none;" value="1"/>
                                                        </div>
                                                        <%
//                                                        } else {
                                                        %>
                                                        <input TYPE="text" id="sACTIVE_FLAG" style="display: none;" value="0"/>
                                                        <%
//                                                            }
                                                        %>
                                                    </div>
                                                    <div class="form-group" style="padding: 10px 10px 0 10px; text-align: right;">
                                                        <button type="button" class="btn btn-info" onClick="editMultiple('<%=anticsrf%>');"><script>document.write(global_fm_button_add);</script></button>
                                                    </div>
                                                </fieldset>
                                            </fieldset>
                                        </form>
                                    </div>
                                </div>
                                <script type="text/javascript">
                                    $(document).ready(function () {
                                        $("#divEditTokenMulti").css("display", "none");
                                        goToByScroll("idShowResultSearch");
                                    });
                                </script>
                                <div class="x_panel" id="idShowResultSearch">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(token_title_table);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <%
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                                                    {} else {
                                                %>
                                                <script>document.write(global_label_grid_sum);</script><%= strMess%>&nbsp;&nbsp;
                                                <%
                                                    }
                                                %>
                                                <%
                                                    boolean isExportEnable = false;
                                                    if(rsPgin[0].length > 0) {
                                                        if (!Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                                            String exportTokenEnabled = conf.GetPropertybyCode(Definitions.CONFIG_EXPORT_TOKENLIST_AGENCY_ENABLED);
                                                            if("1".equals(exportTokenEnabled)) {
                                                                isExportEnable = true;
                                                            }
                                                        } else {
                                                            isExportEnable = true;
                                                        }
                                                    }
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)) {
                                                        isExportEnable = false;
                                                    }
                                                    if(isExportEnable == true) {
                                                %>
                                                <button type="button" class="btn btn-info" onClick="ExporTokenList('<%= String.valueOf(iTotRslts)%>', '<%= anticsrf%>');"><script>document.write(global_fm_button_export_csv);</script></button>
                                                <script>
                                                    function ExporTokenList(countList, idCSRF)
                                                    {
                                                        $('body').append('<div id="over"></div>');
                                                        $(".loading-gif").show();
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../ExportCSVParam",
                                                            data: {
                                                                idParam: "exporttokenlist",
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
                                        <input id="idValueCheck" name="idValueCheck" type="hidden" />
                                        <script>
                                            function editMultiple(idCSRF)
                                            {
                                                var vDateLimit = "";
                                                var sCheckIS_PUSH_NOTICE = "0";
                                                if ($("#sIS_PUSH_NOTICE").val() === "1")
                                                {
                                                    if ($("#strIS_PUSH_NOTICE").is(':checked')) {
                                                        if (!JSCheckEmptyField(document.getElementById("strLINK_NOTICE").value))
                                                        {
                                                            document.getElementById("strLINK_NOTICE").focus();
                                                            funErrorAlert(policy_req_empty + token_fm_noticelink);
                                                            return false;
                                                        }
                                                        if (!JSCheckEmptyField(document.getElementById("strNOTICE_INFO").value))
                                                        {
                                                            document.getElementById("strNOTICE_INFO").focus();
                                                            funErrorAlert(policy_req_empty + token_fm_noticeinfor);
                                                            return false;
                                                        }
                                                        sCheckIS_PUSH_NOTICE = "1";
                                                    } else {sCheckIS_PUSH_NOTICE = "";}
                                                } else {
                                                    sCheckIS_PUSH_NOTICE = "";
                                                }
                                                var sCheckIS_MENU_LINK = "0";
                                                if ($("#sIS_MENU_LINK").val() === "1")
                                                {
                                                    if ($("#strIS_MENU_LINK").is(':checked')) {
                                                        if (!JSCheckEmptyField(document.getElementById("strNAME_LINK").value))
                                                        {
                                                            document.getElementById("strNAME_LINK").focus();
                                                            funErrorAlert(policy_req_empty + token_fm_linkname);
                                                            return false;
                                                        }
                                                        if (!JSCheckEmptyField(document.getElementById("strLINK_VALUE").value))
                                                        {
                                                            document.getElementById("strLINK_VALUE").focus();
                                                            funErrorAlert(policy_req_empty + token_fm_linkvalue);
                                                            return false;
                                                        } else {
                                                            if(!JSCheckLinkURLFormat(document.getElementById("strLINK_VALUE").value))
                                                            {
                                                                document.getElementById("strLINK_VALUE").focus();
                                                                funErrorAlert(global_req_format_url + token_fm_linkvalue);
                                                                return false;
                                                            }
                                                        }
                                                        sCheckIS_MENU_LINK = "1";
                                                    } else {sCheckIS_MENU_LINK = "";}
                                                } else {
                                                    sCheckIS_MENU_LINK = "";
                                                }
                                                var sCheckIS_LOCK = "0";
                                                if ($("#sIS_LOCK").val() === "1")
                                                {
                                                    if ($("#strIS_LOCK").is(':checked')) {
                                                        sCheckIS_LOCK = "1";
                                                    } else {sCheckIS_LOCK = "";}
                                                } else {
                                                    sCheckIS_LOCK = "";
                                                }
                                                var sCheckIS_UNLOCK = "0";
                                                if ($("#sIS_UNLOCK").val() === "1")
                                                {
                                                    if ($("#strIS_UNLOCK").is(':checked')) {
                                                        sCheckIS_UNLOCK = "1";
                                                    } else {sCheckIS_UNLOCK = "";}
                                                } else {
                                                    sCheckIS_UNLOCK = "";
                                                }
                                                var sCheckIS_INITIALIZE = "0";
                                                if ($("#sIS_INITIALIZE").val() === "1")
                                                {
                                                    if ($("#strIS_INITIALIZE").is(':checked')) {
                                                        sCheckIS_INITIALIZE = "1";
                                                    } else {sCheckIS_INITIALIZE = "";}
                                                } else {
                                                    sCheckIS_INITIALIZE = "";
                                                }
                                                var sCheckIS_CHANGE_SOPIN = "0";
                                                if ($("#sIS_CHANGE_SOPIN").val() === "1")
                                                {
                                                    if ($("#strIS_CHANGE_SOPIN").is(':checked')) {
                                                        sCheckIS_CHANGE_SOPIN = "1";
                                                    } else {sCheckIS_CHANGE_SOPIN = "";}
                                                } else {
                                                    sCheckIS_CHANGE_SOPIN = "";
                                                }
                                                var sCheckActiveFlag = "0";
                                                if ($("#sACTIVE_FLAG").val() === "1")
                                                {
                                                    if ($("#strACTIVE_FLAG").is(':checked')) {
                                                        sCheckActiveFlag = "1";
                                                    } else {sCheckActiveFlag = "";}
                                                } else {
                                                    sCheckActiveFlag = "";
                                                }
                                                var sCheckCheckAll = "0";
                                                if ($("#checkAll").is(':checked')) {
                                                    sCheckCheckAll = "1";
                                                }
                                                if (sCheckCheckAll === "0")
                                                {
                                                    if (localStorage.getItem("CountCheckTokenList") === "")
                                                    {
                                                        funErrorAlert(global_succ_NoCheck_add_renewal);
                                                        return false;
                                                    }
                                                }
                                                swal({
                                                    title: "",
                                                    text: token_conform_update_multi,
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
                                                            url: "../TokenCommon",
                                                            data: {
                                                                idParam: "edittokenmultiple",
                                                                idValue: localStorage.getItem("CountCheckTokenList"), //$("#idValueCheck").val(),
                                                                isCheckAll: sCheckCheckAll,
                                                                LINK_NOTICE: document.myname.strLINK_NOTICE.value,
                                                                COLOR_TEXT: document.myname.strCOLOR_TEXT.value,
                                                                COLOR_BKGR: document.myname.strCOLOR_BKGR.value,
                                                                NOTICE_INFO: document.myname.strNOTICE_INFO.value,
                                                                NAME_LINK: document.myname.strNAME_LINK.value,
                                                                LINK_VALUE: document.myname.strLINK_VALUE.value,
                                                                DATE_LIMIT: vDateLimit,
                                                                IS_PUSH_NOTICE: sCheckIS_PUSH_NOTICE,
                                                                IS_MENU_LINK: sCheckIS_MENU_LINK,
                                                                IS_LOCK: sCheckIS_LOCK,
                                                                IS_UNLOCK: sCheckIS_UNLOCK,
                                                                IS_INITIALIZE: sCheckIS_INITIALIZE,
                                                                IS_CHANGE_SOPIN: sCheckIS_CHANGE_SOPIN,
//                                                                IS_INFORMATION: sCheckIS_INFORMATION,
                                                                ACTIVE_FLAG: sCheckActiveFlag,
                                                                CsrfToken: idCSRF
                                                            },
                                                            catche: false,
                                                            success: function (html) {
                                                                var arr = sSpace(html).split('#');
                                                                if (arr[0] === "0")
                                                                {
                                                                    localStorage.setItem("CountCheckTokenList", "");
                                                                    localStorage.setItem("CountCheckAllTokenList", "");
                                                                    funSuccAlert(token_succ_edit, "TokenList.jsp");
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
                                                    }, JS_STR_ACTION_TIMEOUT);
                                                });
                                            }
                                            $(document).ready(function () {
                                                if(localStorage.getItem("CountCheckAllTokenList") === "1")
                                                {
                                                    $('tbody tr td input[type="checkbox"]').prop('checked', true);
                                                    $('tbody tr').css('background', '#d8d8d8');
                                                    localStorage.setItem("CountCheckAllTokenList", "1");
                                                    $('#checkAll').prop('checked', true);
                                                    CheckAllTableValue();
                                                        $("#divEditTokenMulti").css("display", "");
                                                }
                                                else
                                                {
                                                    $('tbody tr').css('background', '');
                                                    localStorage.setItem("CountCheckAllTokenList", "");
                                                    $('#checkAll').prop('checked', false);
                                                }
                                                if(localStorage.getItem("CountCheckTokenList") !== "")
                                                {
                                                    ForCheckTable(localStorage.getItem("CountCheckTokenList"));
                                                        $("#divEditTokenMulti").css("display", "");
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
                                                    var sValueCheck = localStorage.getItem("CountCheckTokenList");
                                                    var ischecked = $(this).is(':checked');
                                                    if (!ischecked) {
                                                        $(this).closest('tr').css('background', '');
                                                        var sss = $(this).closest('tr').find("td:eq(9)").text() + ",";
                                                        sValueCheck = sValueCheck.replace(sss, "");
                                                        $('#checkAll').prop('checked', false);
                                                        localStorage.setItem("CountCheckAllTokenList", "");
                                                    }
                                                    else
                                                    {
                                                        $(this).closest('tr').css('background', '#d8d8d8');
                                                        var sCheck = $(this).closest('tr').find("td:eq(9)").text() + ",";
                                                        if(sValueCheck.indexOf(sCheck) === -1)
                                                        {
                                                            sValueCheck = sValueCheck + sCheck;
                                                        }
                                                        $("#divEditTokenMulti").css("display", "");
                                                    }
                                                    localStorage.setItem("CountCheckTokenList", sValueCheck);
                                                });
                                            });
                                            function ForCheckTable(sValueCheck)
                                            {
                                                $('#mtToken').find('tr').each(function () {
                                                    var sCheck = sSpace($(this).find('td:eq(9)').text());
                                                    if(sCheck !== "")
                                                    {
                                                        if (sValueCheck.indexOf(sCheck + ",") !== -1)
                                                        {
                                                            $('#checkChilren' + sCheck).prop('checked', true);
                                                            $(this).closest('tr').css('background', '#d8d8d8');
                                                        }
                                                    }
                                                });
                                            }
                                            function CheckAllTableValue()
                                            {
                                                var currentCheck = localStorage.getItem("CountCheckTokenList");
                                                $('#mtToken').find('tr').each(function () {
                                                    var sCheck = $(this).find('td:eq(9)').text();
                                                    if(sCheck !== "" && currentCheck.indexOf(sCheck + ',') === -1)
                                                    {
                                                        currentCheck = currentCheck + sCheck + ",";
                                                    }
                                                });
                                                localStorage.setItem("CountCheckTokenList", currentCheck);
                                            }
                                            function checkAllTable()
                                            {
                                                $('#checkAll').change(function () {
                                                    $('tbody tr td input[type="checkbox"]').prop('checked', $(this).prop('checked'));
                                                    $('tbody tr').css('background', '#d8d8d8');
                                                    if ($("#checkAll").is(':checked')) {
                                                        CheckAllTableValue();
                                                        localStorage.setItem("CountCheckAllTokenList", "1");
                                                        $("#divEditTokenMulti").css("display", "");
                                                    }
                                                    else
                                                    {
                                                        localStorage.setItem("CountCheckTokenList", "");
                                                        localStorage.setItem("CountCheckAllTokenList", "");
                                                        $('tbody tr').css('background', '');
                                                    }
                                                });
                                            }
                                            function stopRKey(evt) {
                                                var evt = (evt) ? evt : ((event) ? event : null);
                                                var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
                                                if ((evt.keyCode === 13) && (node.type === "text")) {
                                                    return false;
                                                }
                                            }
                                            document.onkeypress = stopRKey;
                                        </script>
                                        <div class="table-responsive">
                                        <table class="table table-bordered table-striped projects" id="mtToken">
                                            <thead>
                                            <th><script>document.write(global_fm_STT);</script></th>
                                            <th><script>document.write(token_fm_tokenid);</script></th>
                                            <th><script>document.write(token_fm_version);</script></th>
                                            <th><script>document.write(global_fm_Status);</script></th>
                                            <th><script>document.write(token_fm_agent);</script></th>
                                            <th><script>document.write(global_fm_date_create);</script></th>
                                            <th><script>document.write(global_fm_action);</script></th>
                                            </thead>
                                            <tbody>
                                                <%
                                                    if (iPaNoSS > 1) {
                                                        j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                    }
                                                    session.setAttribute("RefreshTokenSessNumberPaging", String.valueOf(iPaNoSS));
                                                    if (rsPgin[0].length > 0) {
                                                        for (TOKEN temp1 : rsPgin[0]) {
                                                            String strID = String.valueOf(temp1.ID);
                                                %>
                                                <tr>
                                                    <td><%= com.convertMoney(j)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.TOKEN_SN)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.TOKEN_VERSION_DESC)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.TOKEN_STATE_DESC)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.BRANCH_DESC)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_DT)%></td>
                                                    <td>
                                                        <a style="cursor: pointer;" onclick="popupEdit('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_detail);</script> </a>
                                                        <%
                                                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                                            {
                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_INITIALIZE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                                        %>
                                                        <a style="cursor: pointer;" onclick="popupaddInit('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(token_fm_innit);</script> </a>
                                                        <%
                                                                }
                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_CHANGE_SOPIN,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                                        %>
                                                        <a style="cursor: pointer;" onclick="popupaddChangeSOPIN('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(token_fm_change);</script> </a>
                                                        <%
                                                                }
                                                                //if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)) {
                                                        %>
<!--                                                        <a style="cursor: pointer;" onclick="popupDelete('<=strID%>', '<=anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_delete);</script> </a>-->
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
                            if(localStorage.getItem("EDIT_TOKEN_PUSH") !== null && localStorage.getItem("EDIT_TOKEN_PUSH") !== "null")
                            {
                                var vIDEDIT_TOKEN = localStorage.getItem("EDIT_TOKEN_PUSH");
                                localStorage.setItem("EDIT_TOKEN_PUSH", null);
                                popupEdit(vIDEDIT_TOKEN);
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
            <!--<script src="../style/switchery/dist/switchery.min.js"></script>-->
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
