<%-- 
    Document   : TokenApproveList
    Created on : Jul 5, 2018, 5:11:03 PM
    Author     : THANH-PC
--%>

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
        <script type='text/javascript' src='../Css/jscolor.js'></script>
        <title></title>
        <script language="javascript">
            changeFavicon("../");
            document.title = tokenapprove_title_list;
            $(document).ready(function () {
                localStorage.setItem("LOCAL_PARAM_TOKENAPPROVELIST", null);
                localStorage.setItem("storeAnticsrfTokenApprove", null);
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
                $('#idCheck').removeClass("js-switch");
                $('#idCheck').addClass("js-switch");
            });
            function popupEdit(id)
            {
                $('#idCheck').removeClass("js-switch");
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('TokenApproveView.jsp', {id:id}, function () {
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
                $(".loading-gif").hide();
                $('#over').remove();
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
            function popupCancelList(TokenID, idID, idCSRF)
            {
                if(localStorage.getItem("storeAnticsrfTokenApprove") !== null && localStorage.getItem("storeAnticsrfTokenApprove") !== ""
                     && localStorage.getItem("storeAnticsrfTokenApprove") !== "null") {
                    idCSRF = sSpace(localStorage.getItem("storeAnticsrfTokenApprove"));
                }
                swal({
                    title: "",
                    text: token_confirm_cancel_request,
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
                                idParam: 'cancelrequest',
                                TokenID: TokenID,
                                id: idID,
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var myStrings = sSpace(html);
                                var arr = myStrings.split('#');
                                if (arr[0] === "0")
                                {
                                    funSuccLocalAlert(token_succ_cancel_request);
                                }
                                else if (arr[0] === JS_EX_CSRF)
                                {
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
        </script>
        <style>
            .projects th{font-weight: bold;}.navbar-right{margin-right: 0;padding-right:10px;}
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.4em 7px 1.4em !important;
                margin: 0 0 1.5em 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
            .table > thead > tr > th{border-bottom: none;}
            .btn{margin-bottom: 0px;}
            .x_panel {
                padding:10px 17px 0 17px;
            }
            .x_content {
                padding: 0 5px 0 5px;
            }
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
                        document.getElementById("idNameURL").innerHTML = tokenapprove_title_list;
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
                                    if (session.getAttribute("RefreshApproveTokenSess") != null && session.getAttribute("sessTOKEN_IDApprove") != null
                                            && session.getAttribute("sessTOKEN_IDApprove") != null) {
                                        session.setAttribute("RefreshApproveTokenSessPaging", "1");
                                        session.setAttribute("SearchSharePagingToken", "0");
                                        statusLoad = 1;
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSApproveToken");
                                        String FromDateValid = (String) session.getAttribute("FromCreateDateSApproveTokenlist");
                                        String ToDateValid = (String) session.getAttribute("ToCreateDateSApproveTokenlist");
                                        String TOKEN_ID = (String) session.getAttribute("sessTOKEN_IDApprove");
                                        String AGENT_ID = (String) session.getAttribute("sessAGENT_IDApprove");
                                        String TOKEN_ATTR_TYPE = (String) session.getAttribute("sessTOKEN_ATTR_TYPEApprove");
                                        String TOKEN_ATTR_STATE = (String) session.getAttribute("sessTOKEN_ATTR_STATEApprove");
                                        String TAX_CODE = (String) session.getAttribute("sessTAX_CODEApprove");
                                        String BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODEApprove");
                                        String DECISION = (String) session.getAttribute("sessDECISIONApprove");
                                        String P_ID = (String) session.getAttribute("sessP_IDApprove");
                                        String CCCD = (String) session.getAttribute("sessCCCDApprove");
                                        String PASSPORT = (String) session.getAttribute("sessPASSPORTApprove");
                                        session.setAttribute("FromCreateDateSApproveTokenlist", FromDateValid);
                                        session.setAttribute("ToCreateDateSApproveTokenlist", ToDateValid);
                                        session.setAttribute("sessTOKEN_IDApprove", TOKEN_ID);
                                        session.setAttribute("sessTOKEN_ATTR_TYPEApprove", TOKEN_ATTR_TYPE);
                                        session.setAttribute("sessTOKEN_ATTR_STATEApprove", TOKEN_ATTR_STATE);
                                        session.setAttribute("sessAGENT_IDApprove", AGENT_ID);
                                        session.setAttribute("RefreshApproveTokenSess", null);
                                        session.setAttribute("AlertAllTimeSApproveToken", strAlertAllTimes);
                                        session.setAttribute("sessTAX_CODEApprove", TAX_CODE);
                                        session.setAttribute("sessBUDGET_CODEApprove", BUDGET_CODE);
                                        session.setAttribute("sessDECISIONApprove", DECISION);
                                        session.setAttribute("sessP_IDApprove", P_ID);
                                        session.setAttribute("sessCCCDApprove", CCCD);
                                        session.setAttribute("sessPASSPORTApprove", PASSPORT);
                                        if(isUIDCollection.equals("1")) {
                                            String sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDApproveTokenlist");
                                            String sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDApproveTokenlist");
                                            String sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXApproveTokenlist");
                                            String sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXApproveTokenlist");
                                            session.setAttribute("sessENTERPRISE_IDApproveTokenlist", sENTERPRISE_ID);
                                            session.setAttribute("sessPERSONAL_IDApproveTokenlist", sPERSONAL_ID);
                                            session.setAttribute("sessENTERPRISE_PREFIXApproveTokenlist", sENTERPRISE_PREFIX);
                                            session.setAttribute("sessPERSONAL_PREFIXApproveTokenlist", sPERSONAL_PREFIX);
                                            isLoadEnterprisePrefix = sENTERPRISE_PREFIX;
                                            isLoadPeronalPrefix = sPERSONAL_PREFIX;
                                            if(!"".equals(sENTERPRISE_ID)){
                                                sEnterpriseCert = sENTERPRISE_PREFIX + sENTERPRISE_ID;
                                            }
                                            if(!"".equals(sPERSONAL_ID)){
                                                sPersonalCert = sPERSONAL_PREFIX + sPERSONAL_ID;
                                            }
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_ATTR_TYPE)) {
                                            TOKEN_ATTR_TYPE = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_ATTR_STATE)) {
                                            TOKEN_ATTR_STATE = "";
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
                                        if("1".equals(strAlertAllTimes)) {
                                            FromDateValid = "";
                                            ToDateValid = "";
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
                                        ss = db.S_BO_TOKEN_ATTR_TOTAL(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
                                            EscapeUtils.escapeHtmlSearch(TOKEN_ID), EscapeUtils.escapeHtmlSearch(AGENT_ID),
                                            EscapeUtils.escapeHtmlSearch(TOKEN_ATTR_TYPE), EscapeUtils.escapeHtmlSearch(TOKEN_ATTR_STATE),
                                            SessUserAgentID, sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert);
                                        if (session.getAttribute("IPageNoSessSearchApproveToken") != null) {
                                            String sPage = (String) session.getAttribute("IPageNoSessSearchApproveToken");
                                            iPagNo = Integer.parseInt(sPage);
                                        }
                                        if (session.getAttribute("ISwRwsSessSearchApproveToken") != null) {
                                            String sSumPage = (String) session.getAttribute("ISwRwsSessSearchApproveToken");
                                            iSwRws = Integer.parseInt(sSumPage);
                                        }
                                        if (session.getAttribute("NumPageSessSearchApproveToken") != null) {
                                            String sNoPage = (String) session.getAttribute("NumPageSessSearchApproveToken");
                                            iPaNoSS = Integer.parseInt(sNoPage);
                                        }
                                        session.setAttribute("IPageNoSessSearchApproveToken", String.valueOf(iPagNo));
                                        session.setAttribute("ISwRwsSessSearchApproveToken", String.valueOf(iSwRws));
                                        if (ss > 0) {
                                            db.S_BO_TOKEN_ATTR_LIST(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
                                                EscapeUtils.escapeHtmlSearch(TOKEN_ID),
                                                EscapeUtils.escapeHtmlSearch(AGENT_ID), EscapeUtils.escapeHtmlSearch(TOKEN_ATTR_TYPE),
                                                EscapeUtils.escapeHtmlSearch(TOKEN_ATTR_STATE), SessUserAgentID,
                                                sessLanguageGlobal, rsPgin, iPagNo, iSwRws, sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert);
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
                                        session.setAttribute("RefreshApproveTokenSessPaging", null);
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
                                    String AGENT_ID = request.getParameter("AGENT_ID");
                                    String TOKEN_ATTR_TYPE = request.getParameter("TOKEN_ATTR_TYPE");
                                    String TOKEN_ATTR_STATE = request.getParameter("TOKEN_ATTR_STATE");
                                    String TAX_CODE ="";
                                    String BUDGET_CODE ="";
                                    String DECISION ="";
                                    String P_ID = "";
                                    String CCCD = "";
                                    String PASSPORT = "";
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
                                            sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXApproveTokenlist");
                                            sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXApproveTokenlist");
                                            sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDApproveTokenlist");
                                            sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDApproveTokenlist");
                                        }
                                        session.setAttribute("sessENTERPRISE_IDApproveTokenlist", sENTERPRISE_ID);
                                        session.setAttribute("sessPERSONAL_IDApproveTokenlist", sPERSONAL_ID);
                                        session.setAttribute("sessENTERPRISE_PREFIXApproveTokenlist", sENTERPRISE_PREFIX);
                                        session.setAttribute("sessPERSONAL_PREFIXApproveTokenlist", sPERSONAL_PREFIX);
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
                                        FromDateValid = (String) session.getAttribute("FromCreateDateSApproveTokenlist");
                                        ToDateValid = (String) session.getAttribute("ToCreateDateSApproveTokenlist");
                                        TOKEN_ID = (String) session.getAttribute("sessTOKEN_IDApprove");
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSApproveToken");
                                        AGENT_ID = (String) session.getAttribute("sessAGENT_IDApprove");
                                        TOKEN_ATTR_TYPE = (String) session.getAttribute("sessTOKEN_ATTR_TYPEApprove");
                                        TOKEN_ATTR_STATE = (String) session.getAttribute("sessTOKEN_ATTR_STATEApprove");
                                        TAX_CODE = (String) session.getAttribute("sessTAX_CODEApprove");
                                        BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODEApprove");
                                        DECISION = (String) session.getAttribute("sessDECISIONApprove");
                                        P_ID = (String) session.getAttribute("sessP_IDApprove");
                                        CCCD = (String) session.getAttribute("sessCCCDApprove");
                                        PASSPORT = (String) session.getAttribute("sessPASSPORTApprove");
                                        session.setAttribute("SessParamOnPagingTokenList", null);
                                    } else {
                                        session.setAttribute("SearchSharePagingToken", "1");
                                        session.setAttribute("CountListToken", null);
                                    }
                                    session.setAttribute("FromCreateDateSApproveTokenlist", FromDateValid);
                                    session.setAttribute("ToCreateDateSApproveTokenlist", ToDateValid);
                                    session.setAttribute("sessTOKEN_IDApprove", TOKEN_ID);
                                    session.setAttribute("sessTOKEN_ATTR_STATEApprove", TOKEN_ATTR_STATE);
                                    session.setAttribute("sessTOKEN_ATTR_TYPEApprove", TOKEN_ATTR_TYPE);
                                    session.setAttribute("sessAGENT_IDApprove", AGENT_ID);
                                    session.setAttribute("AlertAllTimeSApproveToken", strAlertAllTimes);
                                    session.setAttribute("sessTAX_CODEApprove", TAX_CODE);
                                    session.setAttribute("sessBUDGET_CODEApprove", BUDGET_CODE);
                                    session.setAttribute("sessDECISIONApprove", DECISION);
                                    session.setAttribute("sessP_IDApprove", P_ID);
                                    session.setAttribute("sessCCCDApprove", CCCD);
                                    session.setAttribute("sessPASSPORTApprove", PASSPORT);
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_ATTR_TYPE)) {
                                        TOKEN_ATTR_TYPE = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_ATTR_STATE)) {
                                        TOKEN_ATTR_STATE = "";
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
                                        ss = db.S_BO_TOKEN_ATTR_TOTAL(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
                                            EscapeUtils.escapeHtmlSearch(TOKEN_ID), EscapeUtils.escapeHtmlSearch(AGENT_ID),
                                            EscapeUtils.escapeHtmlSearch(TOKEN_ATTR_TYPE), EscapeUtils.escapeHtmlSearch(TOKEN_ATTR_STATE),
                                            SessUserAgentID, sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert);
                                        session.setAttribute("CountListToken", String.valueOf(ss));
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListToken");
                                        ss = Integer.parseInt(sCount);
                                        session.setAttribute("CountListToken", String.valueOf(ss));
                                    }
                                    iTotRslts = ss;
                                    iTotRslts = ss;
                                    if (iTotRslts > 0) {
                                        db.S_BO_TOKEN_ATTR_LIST(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
                                                EscapeUtils.escapeHtmlSearch(TOKEN_ID),
                                                EscapeUtils.escapeHtmlSearch(AGENT_ID), EscapeUtils.escapeHtmlSearch(TOKEN_ATTR_TYPE),
                                                EscapeUtils.escapeHtmlSearch(TOKEN_ATTR_STATE), SessUserAgentID,
                                                sessLanguageGlobal, rsPgin, iPagNo, iSwRws, sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert);
                                        session.setAttribute("IPageNoSessSearchApproveToken", String.valueOf(iPagNo));
                                        session.setAttribute("ISwRwsSessSearchApproveToken", String.valueOf(iSwRws));
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
                                    session.setAttribute("RefreshApproveTokenSessPaging", null);
                                    session.setAttribute("SearchShareStoreToken", null);
                                    session.setAttribute("SearchIPageNoPagingToken", null);
                                    session.setAttribute("SearchISwRwsPagingToken", null);
                                    session.setAttribute("FromCreateDateSApproveTokenlist", null);
                                    session.setAttribute("ToCreateDateSApproveTokenlist", null);
                                    session.setAttribute("sessTOKEN_IDApprove", null);
                                    session.setAttribute("sessTAX_CODEApprove", null);
                                    session.setAttribute("sessBUDGET_CODEApprove", null);
                                    session.setAttribute("sessDECISIONApprove", null);
                                    session.setAttribute("sessP_IDApprove", null);
                                    session.setAttribute("sessCCCDApprove", null);
                                    session.setAttribute("sessPASSPORTApprove", null);
                                    session.setAttribute("sessTOKEN_ATTR_STATEApprove", "1");
                                    session.setAttribute("sessTOKEN_ATTR_TYPEApprove", "All");
                                    session.setAttribute("sessAGENT_IDApprove", null);
                                    if(isUIDCollection.equals("1")) {
                                        session.setAttribute("sessENTERPRISE_IDApproveTokenlist", null);
                                        session.setAttribute("sessPERSONAL_IDApproveTokenlist", null);
                                        session.setAttribute("sessENTERPRISE_PREFIXApproveTokenlist", null);
                                        session.setAttribute("sessPERSONAL_PREFIXApproveTokenlist", null);
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
                                        <form name="form" method="post" class="form-horizontal">
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <div class="col-sm-5" style="padding-right: 0px;text-align: right;">
                                                            <label class="switch" for="idCheck">
                                                                <input type="checkbox" name="nameCheck" id="idCheck" onchange="checkboxChange();" <%= session.getAttribute("AlertAllTimeSApproveToken") != null && "1".equals(session.getAttribute("AlertAllTimeSApproveToken").toString()) ? "" : "checked" %>/>
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
                                                            <input type="Text" id="demo1" name="FromCreateDate" <%= session.getAttribute("AlertAllTimeSApproveToken") != null && "1".equals(session.getAttribute("AlertAllTimeSApproveToken").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("FromCreateDateSApproveTokenlist") != null && !"1".equals(session.getAttribute("AlertAllTimeSApproveToken").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("FromCreateDateSApproveTokenlist").toString()) : com.ConvertMonthSub(30)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_ToDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo2" name="ToCreateDate" <%= session.getAttribute("AlertAllTimeSApproveToken") != null && "1".equals(session.getAttribute("AlertAllTimeSApproveToken").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("ToCreateDateSApproveTokenlist") != null && !"1".equals(session.getAttribute("AlertAllTimeSApproveToken").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("ToCreateDateSApproveTokenlist").toString()) : com.ConvertMonthSub(0)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(token_fm_tokenid);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="TOKEN_ID" name="TOKEN_ID" maxlength="45" class="form-control123"
                                                                value="<%= session.getAttribute("sessTOKEN_IDApprove") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessTOKEN_IDApprove").toString()) : ""%>"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_Status);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="TOKEN_ATTR_STATE" id="TOKEN_ATTR_STATE" class="form-control123">
                                                                <%
                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_ALL,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_SEARCH, sessFunctionToken) == true) {
                                                                %>
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessTOKEN_ATTR_STATEApprove") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessTOKEN_ATTR_STATEApprove").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    }
                                                                %>
                                                                <%
                                                                    TOKEN_ATTR_STATE[][] rstSTATE = new TOKEN_ATTR_STATE[1][];
                                                                    db.S_BO_TOKEN_ATTR_STATE_COMBOBOX(sessLanguageGlobal, rstSTATE);
                                                                    if (rstSTATE[0].length > 0) {
                                                                        for (TOKEN_ATTR_STATE temp1 : rstSTATE[0]) {
                                                                            if(CommonFunction.CheckRoleFuncValid(EscapeUtils.CheckTextNull(temp1.NAME),Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_SEARCH, sessFunctionToken) == true) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessTOKEN_ATTR_STATEApprove") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessTOKEN_ATTR_STATEApprove").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_requesttype);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="TOKEN_ATTR_TYPE" id="TOKEN_ATTR_TYPE" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessTOKEN_ATTR_TYPEApprove") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessTOKEN_ATTR_TYPEApprove").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    TOKEN_ATTR_TYPE[][] rstATTR = new TOKEN_ATTR_TYPE[1][];
                                                                    db.S_BO_TOKEN_ATTR_TYPE_COMBOBOX(sessLanguageGlobal, rstATTR);
                                                                    if (rstATTR[0].length > 0) {
                                                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                                                        {
                                                                            for (TOKEN_ATTR_TYPE temp1 : rstATTR[0]) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessTOKEN_ATTR_TYPEApprove") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessTOKEN_ATTR_TYPEApprove").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                                <%
                                                                            }
                                                                        }
                                                                        else {
                                                                            for (TOKEN_ATTR_TYPE temp1 : rstATTR[0]) {
                                                                                if(temp1.ID != Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_INITIALIZE
                                                                                    && temp1.ID != Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_CHANGE_SOPIN) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessTOKEN_ATTR_TYPEApprove") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessTOKEN_ATTR_TYPEApprove").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                            <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessENTERPRISE_PREFIXApproveTokenlist") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessENTERPRISE_PREFIXApproveTokenlist").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                            <input type="text" name="ENTERPRISE_ID" maxlength="45" value="<%= session.getAttribute("sessENTERPRISE_IDApproveTokenlist") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessENTERPRISE_IDApproveTokenlist").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                    <script>
                                                        function changeEnterprise() {
                                                            $("#idLblTooltipEnterprise").text(global_fm_enter + $("#ENTERPRISE_PREFIX option:selected").text());
                                                        }
                                                    </script>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;<%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR) ? "display:none;" : "" %>">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_Branch);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="AGENT_ID" id="AGENT_ID" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessAGENT_IDApprove") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessAGENT_IDApprove").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    BRANCH[][] rst = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                                                                    if (rst[0].length > 0) {
                                                                        for (BRANCH temp1 : rst[0]) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessAGENT_IDApprove") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessAGENT_IDApprove").toString()) ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                                <%
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
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
                                                            <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessPERSONAL_PREFIXApproveTokenlist") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessPERSONAL_PREFIXApproveTokenlist").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                            <input type="text" name="PERSONAL_ID" maxlength="45" value="<%= session.getAttribute("sessPERSONAL_IDApproveTokenlist") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_IDApproveTokenlist").toString()) : ""%>"
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
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <%
                                                } else {
                                            %>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_MST);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="TAX_CODE" maxlength="45" value="<%= session.getAttribute("sessTAX_CODEApprove") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessTAX_CODEApprove").toString()) : ""%>"
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
                                                        <input type="text" name="BUDGET_CODE" maxlength="45" value="<%= session.getAttribute("sessBUDGET_CODEApprove") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessBUDGET_CODEApprove").toString()) : ""%>"
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
                                                        <input type="text" name="DECISION" maxlength="45" value="<%= session.getAttribute("sessDECISIONApprove") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessDECISIONApprove").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_CMND);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="P_ID" maxlength="32" value="<%= session.getAttribute("sessP_IDApprove") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessP_IDApprove").toString()) : ""%>"
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
                                                        <input type="text" name="CCCD" maxlength="32" value="<%= session.getAttribute("sessCCCDApprove") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCCCDApprove").toString()) : ""%>"
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
                                                        <input type="text" name="PASSPORT" maxlength="32" value="<%= session.getAttribute("sessPASSPORTApprove") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPASSPORTApprove").toString()) : ""%>"
                                                               class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;<%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR) ? "display:none;" : "" %>">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_Branch);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="AGENT_ID" id="AGENT_ID" class="form-control123">
                                                            <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessAGENT_IDApprove") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessAGENT_IDApprove").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            <%
                                                                BRANCH[][] rst = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                                                                if (rst[0].length > 0) {
                                                                    for (BRANCH temp1 : rst[0]) {
                                                            %>
                                                            <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessAGENT_IDApprove") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessAGENT_IDApprove").toString()) ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
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
                                            <%
                                                }
                                            %>
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
                                                <script>document.write(global_label_grid_sum);</script><%= strMess%>
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
                                        <div class="table-responsive">
                                        <table class="table table-bordered table-striped projects" id="mtToken">
                                            <thead>
                                            <th><script>document.write(global_fm_STT);</script></th>
                                            <th><script>document.write(token_fm_tokenid);</script></th>
                                            <th><script>document.write(global_fm_request_function);</script></th>
                                            <th><script>document.write(global_fm_Status);</script></th>
                                            <th><script>document.write(global_fm_date_create);</script></th>
                                            <th><script>document.write(global_fm_user_create);</script></th>
                                            <th><script>document.write(global_fm_action);</script></th>
                                            </thead>
                                            <tbody>
                                                <%
                                                    if (iPaNoSS > 1) {
                                                        j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                    }
                                                    session.setAttribute("RefreshApproveTokenSessNumberPaging", String.valueOf(iPaNoSS));
                                                    if (rsPgin[0].length > 0) {
                                                        for (TOKEN temp1 : rsPgin[0]) {
                                                            String strID = String.valueOf(temp1.ID);
                                                            String strStatus = EscapeUtils.CheckTextNull(temp1.TOKEN_ATTR_STATE_DESC);
                                                            int intStatus = temp1.TOKEN_ATTR_STATE;
                                                            String sRole = session.getAttribute("RoleID_ID").toString().trim();
                                                %>
                                                <tr>
                                                    <td><%= com.convertMoney(j)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.TOKEN_SN)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.TOKEN_ATTR_TYPE_DESC)%></td>
                                                    <td><%= strStatus%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_DT)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_BY)%></td>
                                                    <td>
                                                        <a style="cursor: pointer;" onclick="popupEdit('<%=String.valueOf(temp1.TOKEN_ATTR_ID)%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_detail);</script> </a>
                                                        <%
                                                            if(intStatus == Definitions.CONFIG_TOKEN_STATE_ID_PENDDING) {
                                                        %>
                                                        <%
                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                                        %>
                                                        <a style="cursor: pointer;" onclick="popupCancelList('<%=strID%>', '<%=temp1.TOKEN_ATTR_ID %>', '<%=anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_decline);</script></a>
                                                        <%
                                                            }
                                                        %>
                                                        <%
                                                            } else if(intStatus == Definitions.CONFIG_TOKEN_STATE_ID_APPROVED) {
                                                                if(sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                                    || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                                    || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                        %>
                                                        <%
                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                                        %>
                                                        <a style="cursor: pointer;" onclick="popupCancelList('<%=strID%>', '<%=temp1.TOKEN_ATTR_ID %>', '<%=anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_decline);</script></a>
                                                        <%
                                                            }
                                                        %>
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
                                    <div class="x_content" style="text-align: center;padding-bottom: 10px;">
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
