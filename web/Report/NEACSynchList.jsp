<%-- 
    Document   : NEACSynchList
    Created on : Mar 4, 2021, 5:29:27 PM
    Author     : USER
--%>

<%@page import="java.net.URLEncoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<!DOCTYPE html>
<%    String strAlertAllTimes = "0";
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
            document.title = synchneac_title_list;
            changeFavicon("../");
            $(document).ready(function () {
                localStorage.setItem("LOCAL_PARAM_BRANCHLIST", null);
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
            function searchForm(id)
            {
                var idFromCreateDate = document.myform.FromCreateDate.value;
                var idToCreateDate = document.myform.ToCreateDate.value;
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
                    localStorage.setItem("CountCheckNEACImport", "");
                    localStorage.setItem("CountCheckAllImportNEAC", "");
                    var f = document.myform;
                    f.method = "post";
                    f.action = '';
                    f.submit();
                }
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
                        document.getElementById("idNameURL").innerHTML = synchneac_title_list;
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
//                                String sDefaulCheckAllSynch = "";
                                try {
                                    NEAC_LOG[][] rsPgin = new NEAC_LOG[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (session.getAttribute("SessRefreshNEACLog") != null && session.getAttribute("sessFromCreateDateNEACLog") != null
                                            && session.getAttribute("sessToCreateDateNEACLog") != null) {
                                        session.setAttribute("RefreshBranchListSessPaging", "1");
                                        session.setAttribute("SearchSharePagingBranchList", "0");
                                        statusLoad = 1;
                                        String ToCreateDate = (String) session.getAttribute("sessToCreateDateNEACLog");
                                        String FromCreateDate = (String) session.getAttribute("sessFromCreateDateNEACLog");
                                        String CERT_SN = (String) session.getAttribute("sessCERT_SNNEACLog");
                                        String NEAC_SYNC_STATE = (String) session.getAttribute("NEAC_SYNC_STATE");
                                        String CERTIFICATION_ATTR_TYPE = (String) session.getAttribute("sessCERTIFICATION_ATTR_TYPENEACLog");
                                        String purpose = (String) session.getAttribute("sessCERTIFICATION_PURPOSENEACLog");
                                        String profile = (String) session.getAttribute("sessCERTIFICATION_DURATIONNEACLog");

                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSNEACLogList");
                                        session.setAttribute("SessRefreshNEACLog", null);
                                        session.setAttribute("sessFromCreateDateNEACLog", FromCreateDate);
                                        session.setAttribute("sessToCreateDateNEACLog", ToCreateDate);
                                        session.setAttribute("sessCERT_SNNEACLog", CERT_SN);
                                        session.setAttribute("sessCERTIFICATION_PURPOSENEACLog", purpose);
                                        session.setAttribute("sessCERTIFICATION_DURATIONNEACLog", profile);
                                        session.setAttribute("sessCERTIFICATION_ATTR_TYPENEACLog", CERTIFICATION_ATTR_TYPE);

                                        session.setAttribute("sessNEAC_SYNC_STATE", NEAC_SYNC_STATE);
                                        session.setAttribute("AlertAllTimeSNEACLogList", strAlertAllTimes);
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(NEAC_SYNC_STATE)) {
                                            NEAC_SYNC_STATE = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_ATTR_TYPE)) {
                                            CERTIFICATION_ATTR_TYPE = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(profile)) {
                                            profile = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(purpose)) {
                                            purpose = "";
                                        }
                                        if ("1".equals(strAlertAllTimes)) {
                                            FromCreateDate = "";
                                            ToCreateDate = "";
                                        }
                                        int ss = 0;
                                        if ((session.getAttribute("CountListApproveReq")) == null) {
                                            ss = db.S_BO_NEAC_LOG_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                    EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(NEAC_SYNC_STATE),
                                                    EscapeUtils.escapeHtmlSearch(CERT_SN), EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE),
                                                    sessTreeArrayBranchID, profile, purpose);
                                            session.setAttribute("CountListApproveReq", String.valueOf(ss));
                                        } else {
                                            String sCount = (String) session.getAttribute("CountListApproveReq");
                                            ss = Integer.parseInt(sCount);
                                            session.setAttribute("CountListApproveReq", String.valueOf(ss));
                                        }
                                        if (session.getAttribute("SearchIPageNoPagingBranchList") != null) {
                                            String sPage = (String) session.getAttribute("SearchIPageNoPagingBranchList");
                                            iPagNo = Integer.parseInt(sPage);
                                        }
                                        if (session.getAttribute("SearchISwRwsPagingBranchList") != null) {
                                            String sSumPage = (String) session.getAttribute("SearchISwRwsPagingBranchList");
                                            iSwRws = Integer.parseInt(sSumPage);
                                        }
                                        if (session.getAttribute("SessRefreshBranchNumberPaging") != null) {
                                            String sNoPage = (String) session.getAttribute("SessRefreshBranchNumberPaging");
                                            iPaNoSS = Integer.parseInt(sNoPage);
                                        }
                                        session.setAttribute("SearchIPageNoPagingBranchList", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingBranchList", String.valueOf(iSwRws));
                                        if (ss > 0) {
                                            db.S_BO_NEAC_LOG_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(NEAC_SYNC_STATE), EscapeUtils.escapeHtmlSearch(CERT_SN),
                                                EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE), sessLanguageGlobal, sessTreeArrayBranchID,
                                                rsPgin, iPagNo, iSwRws, profile, purpose);
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
                                        session.setAttribute("RefreshBranchListSessPaging", null);
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("SearchShareStoreBranchList", null);
                                            session.setAttribute("SearchIPageNoPagingBranchList", null);
                                            session.setAttribute("SearchISwRwsPagingBranchList", null);
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
                                    String CERT_SN = EscapeUtils.ConvertStringToUnicode(request.getParameter("CERT_SN"));
                                    String NEAC_SYNC_STATE = request.getParameter("NEAC_SYNC_STATE");
                                    String CERTIFICATION_ATTR_TYPE = request.getParameter("CERTIFICATION_ATTR_TYPE");
                                    String purpose = request.getParameter("CERTIFICATION_PURPOSE");
                                    String profile = request.getParameter("CERTIFICATION_DURATION");
                                    Boolean nameCheck = Boolean.valueOf(request.getParameter("nameCheck") != null);
                                    if (nameCheck == false) {
                                        FromCreateDate = "";
                                        ToCreateDate = "";
                                        strAlertAllTimes = "1";
                                    }
                                    if ("1".equals(hasPaging)) {
                                        session.setAttribute("SearchSharePagingBranchList", "0");
                                        ToCreateDate = (String) session.getAttribute("sessToCreateDateNEACLog");
                                        FromCreateDate = (String) session.getAttribute("sessFromCreateDateNEACLog");
                                        CERT_SN = (String) session.getAttribute("sessCERT_SNNEACLog");
                                        NEAC_SYNC_STATE = (String) session.getAttribute("sessNEAC_SYNC_STATE");
                                        CERTIFICATION_ATTR_TYPE = (String) session.getAttribute("sessCERTIFICATION_ATTR_TYPENEACLog");
                                        purpose = (String) session.getAttribute("sessCERTIFICATION_PURPOSENEACLog");
                                        profile = (String) session.getAttribute("sessCERTIFICATION_DURATIONNEACLog");
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSNEACLogList");
                                        session.setAttribute("SessParamOnPagingCertList", null);
                                    } else {
                                        session.setAttribute("SearchSharePagingBranchList", "1");
                                        session.setAttribute("CountListApproveReq", null);
                                    }
                                    session.setAttribute("sessFromCreateDateNEACLog", FromCreateDate);
                                    session.setAttribute("sessToCreateDateNEACLog", ToCreateDate);
                                    session.setAttribute("sessCERT_SNNEACLog", CERT_SN);
                                    session.setAttribute("sessNEAC_SYNC_STATE", NEAC_SYNC_STATE);
                                    session.setAttribute("sessCERTIFICATION_ATTR_TYPENEACLog", CERTIFICATION_ATTR_TYPE);
                                    session.setAttribute("sessCERTIFICATION_PURPOSENEACLog", purpose);
                                    session.setAttribute("sessCERTIFICATION_DURATIONNEACLog", profile);
                                    session.setAttribute("AlertAllTimeSNEACLogList", strAlertAllTimes);
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(NEAC_SYNC_STATE)) {
                                        NEAC_SYNC_STATE = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_ATTR_TYPE)) {
                                        CERTIFICATION_ATTR_TYPE = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(profile)) {
                                        profile = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(purpose)) {
                                        purpose = "";
                                    }
                                    int ss = 0;
                                    if ((session.getAttribute("CountListApproveReq")) == null) {
                                        ss = db.S_BO_NEAC_LOG_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(NEAC_SYNC_STATE),
                                            EscapeUtils.escapeHtmlSearch(CERT_SN), EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE),
                                            sessTreeArrayBranchID, profile, purpose);
                                        session.setAttribute("CountListApproveReq", String.valueOf(ss));
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListApproveReq");
                                        ss = Integer.parseInt(sCount);
                                        session.setAttribute("CountListApproveReq", String.valueOf(ss));
                                    }
                                    iTotRslts = ss;
                                    if (iTotRslts > 0) {
                                        db.S_BO_NEAC_LOG_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(NEAC_SYNC_STATE), EscapeUtils.escapeHtmlSearch(CERT_SN),
                                            EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE), sessLanguageGlobal, sessTreeArrayBranchID, rsPgin,
                                            iPagNo, iSwRws, profile, purpose);
                                        session.setAttribute("SearchIPageNoPagingBranchList", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingBranchList", String.valueOf(iSwRws));
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
                                    session.setAttribute("RefreshBranchListSessPaging", null);
                                    session.setAttribute("SearchShareStoreBranchList", null);
                                    session.setAttribute("SearchIPageNoPagingBranchList", null);
                                    session.setAttribute("SearchISwRwsPagingBranchList", null);
                                    session.setAttribute("sessFromCreateDateNEACLog", null);
                                    session.setAttribute("sessToCreateDateNEACLog", null);
                                    session.setAttribute("sessCERT_SNNEACLog", null);
                                    session.setAttribute("sessCERTIFICATION_PURPOSENEACLog", "All");
                                    session.setAttribute("sessCERTIFICATION_DURATIONNEACLog", "All");
                                    session.setAttribute("AlertAllTimeSNEACLogList", null);
                                    session.setAttribute("sessNEAC_SYNC_STATE", "All");
                                }
                                String sApproveEnabled = "";
                                GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                                if (sessGeneralPolicy[0].length > 0) {
                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0]){
                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_NEAC_ASYNC_NEED_APPROVE_BY_USER)) {
                                            sApproveEnabled = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                            break;
                                        }
                                    }
                                }
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="myform" method="post" class="form-horizontal">
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
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_FromDate);</script></label>
                                                        <div class="col-sm-8" style="padding-right: 0px;text-align: left;">
                                                            <input type="Text" id="demo1" name="FromCreateDate" <%= session.getAttribute("AlertAllTimeSNEACLogList") != null
                                                                    && "1".equals(session.getAttribute("AlertAllTimeSNEACLogList").toString()) ? "disabled" : ""%>
                                                                   value="<%= session.getAttribute("sessFromCreateDateNEACLog") != null
                                                                           && !"1".equals(session.getAttribute("AlertAllTimeSNEACLogList").toString())
                                                                                ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromCreateDateNEACLog").toString())
                                                                                : com.ConvertMonthSub(3)%>" maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_ToDate);</script></label>
                                                        <div class="col-sm-8" style="padding-right: 0px;text-align: left;">
                                                            <input type="Text" id="demo2" name="ToCreateDate" <%= session.getAttribute("AlertAllTimeSNEACLogList") != null
                                                                    && "1".equals(session.getAttribute("AlertAllTimeSNEACLogList").toString()) ? "disabled" : ""%>
                                                                   value="<%= session.getAttribute("sessToCreateDateNEACLog") != null
                                                                           && !"1".equals(session.getAttribute("AlertAllTimeSNEACLogList").toString())
                                                                                   ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToCreateDateNEACLog").toString()) : com.ConvertMonthSub(0)%>"
                                                                   maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <div class="col-sm-4" style="padding-right: 0px;text-align: right;">
                                                            <label class="switch" for="idCheck">
                                                                <input type="checkbox" name="nameCheck" id="idCheck" onchange="checkboxChange();" <%= session.getAttribute("AlertAllTimeSNEACLogList") != null && "1".equals(session.getAttribute("AlertAllTimeSNEACLogList").toString()) ? "" : "checked"%>/>
                                                                <div class="slider round"></div>
                                                            </label>
                                                        </div>
                                                        <label class="control-label col-sm-8" style="color: #000000; font-weight: bold;text-align: left;"><script>document.write(global_fm_check_date);</script></label>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_serial);</script></label>
                                                        <div class="col-sm-8" style="padding-right: 0px;text-align: left;">
                                                            <input type="text" name="CERT_SN" maxlength="<%= Definitions.CONFIG_LENGTH_TOKEN_SN%>" value="<%= session.getAttribute("sessCERT_SNNEACLog") != null
                                                                    ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCERT_SNNEACLog").toString()) : ""%>" class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_Status);</script></label>
                                                        <div class="col-sm-8" style="padding-right: 0px;text-align: left;">
                                                            <select name="NEAC_SYNC_STATE" id="NEAC_SYNC_STATE" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessNEAC_SYNC_STATE") != null
                                                                        && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessNEAC_SYNC_STATE").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    NEAC_SYNC_STATE[][] rsState = new NEAC_SYNC_STATE[1][];
                                                                    db.S_BO_NEAC_SYNC_STATE_COMBOBOX(sessLanguageGlobal, rsState);
                                                                    if (rsState[0].length > 0) {
                                                                        int sStateOut = 0;
                                                                        for (NEAC_SYNC_STATE temp1 : rsState[0]) {
                                                                            if(sStateOut == 0) {
                                                                                if("1".equals(sApproveEnabled)) {
                                                                                    sStateOut = Definitions.CONFIG_SYNCH_NEAC_STATE_PROCESSING_MANUALLY;
                                                                                }
                                                                            }
                                                                            if(temp1.ID != sStateOut) {
                                                                %>
                                                                        <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessNEAC_SYNC_STATE") != null
                                                                        && String.valueOf(temp1.ID).equals(session.getAttribute("sessNEAC_SYNC_STATE").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(info_fm_type_request);</script></label>
                                                        <div class="col-sm-8" style="padding-right: 0px;text-align: left;">
                                                            <select name="CERTIFICATION_ATTR_TYPE" id="CERTIFICATION_ATTR_TYPE" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCERTIFICATION_ATTR_TYPENEACLog") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCERTIFICATION_ATTR_TYPENEACLog").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    CERTIFICATION_ATTR_TYPE[][] rsType = new CERTIFICATION_ATTR_TYPE[1][];
                                                                    db.S_BO_CERTIFICATION_ATTR_TYPE_COMBOBOX(sessLanguageGlobal, rsType);
                                                                    if (rsType[0].length > 0) {
                                                                        for (CERTIFICATION_ATTR_TYPE temp1 : rsType[0]) {
                                                                            if(temp1.ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                || temp1.ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessCERTIFICATION_ATTR_TYPENEACLog") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessCERTIFICATION_ATTR_TYPENEACLog").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                                <%
                                                                            }
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
                                                    <div class="form-group">
                                                        <label id="idLblTitleProfileCertPurpose" class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;text-align: right;"></label>
                                                        <div class="col-sm-8" style="padding-right: 0px;text-align: left;">
                                                            <select id="CERTIFICATION_PURPOSE" name="CERTIFICATION_PURPOSE" class="form-control123"
                                                                onchange="LOAD_PROFILE_BY_PURPOSE(this.value, '<%= anticsrf%>');">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCERTIFICATION_PURPOSENEACLog") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCERTIFICATION_PURPOSENEACLog").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    String pCERTIFICATION_PURPOSE_ID = "";
                                                                    CERTIFICATION_PURPOSE[][] rsCertPro = new CERTIFICATION_PURPOSE[1][];
                                                                    db.S_BO_CERTIFICATION_PURPOSE_COMBOBOX(sessLanguageGlobal, rsCertPro);
                                                                    if (rsCertPro.length > 0) {
                                                                        for (int i = 0; i < rsCertPro[0].length; i++) {
                                                                            if("".equals(pCERTIFICATION_PURPOSE_ID)){
                                                                                pCERTIFICATION_PURPOSE_ID = String.valueOf(rsCertPro[0][i].ID);
                                                                            }
                                                                %>
                                                                <option value="<%= String.valueOf(rsCertPro[0][i].ID)%>" <%= session.getAttribute("sessCERTIFICATION_PURPOSENEACLog") != null && String.valueOf(rsCertPro[0][i].ID).equals(session.getAttribute("sessCERTIFICATION_PURPOSENEACLog").toString()) ? "selected" : ""%>><%= rsCertPro[0][i].REMARK%></option>
                                                                <%
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <script>$("#idLblTitleProfileCertPurpose").text(global_fm_certpurpose);</script>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleProfileCertDuration" class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;text-align: right;"></label>
                                                        <div class="col-sm-8" style="padding-right: 0px;text-align: left;">
                                                            <select id="CERTIFICATION_DURATION" name="CERTIFICATION_DURATION" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCERTIFICATION_DURATIONNEACLog") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCERTIFICATION_DURATIONNEACLog").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleProfileCertDuration").text(global_fm_duration_cts);
                                                        function LOAD_PROFILE_BY_PURPOSE(objPurpose, idCSRF)
                                                        {
                                                            if(objPurpose === JS_STR_GRID_COMBOBOX_VALUE_ALL)
                                                            {
                                                                var cbxUSER = document.getElementById("CERTIFICATION_DURATION");
                                                                removeOptions(cbxUSER);
                                                                cbxUSER.options[cbxUSER.options.length] = new Option(global_fm_combox_all, JS_STR_GRID_COMBOBOX_VALUE_ALL);
                                                            }
                                                            else
                                                            {
                                                                $.ajax({
                                                                    type: "post",
                                                                    url: "../JSONCommon",
                                                                    data: {
                                                                        idParam: 'loadprofile_bypurpose',
                                                                        idPurpose: objPurpose
                                                                    },
                                                                    cache: false,
                                                                    success: function (html)
                                                                    {
                                                                        if (html.length > 0)
                                                                        {
                                                                            var cbxCERTIFICATION_DURATION = document.getElementById("CERTIFICATION_DURATION");
                                                                            removeOptions(cbxCERTIFICATION_DURATION);
                                                                            var obj = JSON.parse(html);
                                                                            if (obj[0].Code === "0")
                                                                            {
                                                                                var idSessUSER = '<%= session.getAttribute("sessCERTIFICATION_DURATIONNEACLog").toString().trim() %>';
                                                                                cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option(global_fm_combox_all, JS_STR_GRID_COMBOBOX_VALUE_ALL);
                                                                                for (var i = 0; i < obj.length; i++) {
                                                                                    cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option("["+obj[i].NAME + "] " + obj[i].REMARK, obj[i].ID);
                                                                                    if(obj[i].ID === idSessUSER)
                                                                                    {
                                                                                        $("#CERTIFICATION_DURATION option[value='" + obj[i].ID + "']").attr("selected", "selected");
                                                                                    }
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
                                                                                cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option("---", "");
                                                                            }
                                                                            else {
                                                                                funErrorAlert(global_errorsql);
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                            return false;
                                                        }
                                                        $(document).ready(function () {
                                                            if(document.getElementById("CERTIFICATION_PURPOSE").value !== JS_STR_GRID_COMBOBOX_VALUE_ALL)
                                                            {
                                                                LOAD_PROFILE_BY_PURPOSE(document.getElementById("CERTIFICATION_PURPOSE").value, '<%= anticsrf%>');
                                                            }
                                                        });
                                                    </script>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group" style="text-align: center;">
                                                        <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
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
                                <div class="x_panel">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(synchneac_table_list);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <%
                                                    if("1".equals(sApproveEnabled)){
                                                %>
                                                <button type="button" class="btn btn-info" onClick="approveMultiple('<%=anticsrf%>');"><script>document.write(global_fm_approve);</script></button>
                                                <script>
                                                    function approveMultiple(idCSRF)
                                                    {
                                                        var sCheckCheckAll = "0";
                                                        if ($("#checkAll").is(':checked')) {
                                                            sCheckCheckAll = "1";
                                                        }
                                                        if(sCheckCheckAll === "0")
                                                        {
                                                            if(localStorage.getItem("CountCheckNEACImport") === "")
                                                            {
                                                                funErrorAlert(global_succ_NoCheck);
                                                                return false;
                                                            }
                                                        }
                                                        console.log(localStorage.getItem("CountCheckNEACImport"));
                                                        swal({
                                                            title: "",
                                                            text: synchneac_conform_update_multi,
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
                                                                    url: "../SomeCommon",
                                                                    data: {
                                                                        idParam: "approveneacbundles",
                                                                        idValue: localStorage.getItem("CountCheckNEACImport"),
                                                                        isCheckAll: sCheckCheckAll,
                                                                        isAGENT_IDMulti: $("#AGENT_IDMulti").val(),
                                                                        CsrfToken: idCSRF
                                                                    },
                                                                    catche: false,
                                                                    success: function (html) {
                                                                        var arr = sSpace(html).split('#');
                                                                        if (arr[0] === "0")
                                                                        {
                                                                            localStorage.setItem("CountCheckNEACImport", "");
                                                                            localStorage.setItem("CountCheckAllImportNEAC", "");
                                                                            funSuccAlert(token_succ_edit, "NEACSynchList.jsp");
                                                                        }
                                                                        else if (arr[0] === "10")
                                                                        {
                                                                            funErrorAlert(global_req_all);
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
                                                                        else if (arr[0] === JS_EX_WRONG_AGENCY) {
                                                                            RedirectPageLoginNoSess(global_error_wrong_agency);
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
                                                </script>
                                                <%
                                                    }
                                                %>
                                                <button type="button" class="btn btn-info" onClick="synchMultiple('<%=anticsrf%>');"><script>document.write(global_button_grid_synch);</script></button>
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
                                        </style>
                                        <script>
                                            function synchMultiple(idCSRF)
                                            {
                                                var sCheckCheckAll = "0";
                                                if ($("#checkAll").is(':checked')) {
                                                    sCheckCheckAll = "1";
                                                }
                                                if(sCheckCheckAll === "0")
                                                {
                                                    if(localStorage.getItem("CountCheckNEACImport") === "") {
                                                        funErrorAlert(global_succ_NoCheck);
                                                        return false;
                                                    }
                                                }
//                                                console.log(localStorage.getItem("CountCheckNEACImport"));
//                                                return false;
                                                swal({
                                                    title: "",
                                                    text: synchneac_conform_synch_multi,
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
                                                            url: "../SomeCommon",
                                                            data: {
                                                                idParam: "synchneacbundles",
                                                                idValue: localStorage.getItem("CountCheckNEACImport"),
                                                                isCheckAll: sCheckCheckAll,
                                                                isAGENT_IDMulti: $("#AGENT_IDMulti").val(),
                                                                CsrfToken: idCSRF
                                                            },
                                                            catche: false,
                                                            success: function (html) {
                                                                var arr = sSpace(html).split('#');
                                                                if (arr[0] === "0")
                                                                {
                                                                    localStorage.setItem("CountCheckNEACImport", "");
                                                                    localStorage.setItem("CountCheckAllImportNEAC", "");
                                                                    funSuccAlert(token_succ_edit, "NEACSynchList.jsp");
                                                                }
                                                                else if (arr[0] === "10")
                                                                {
                                                                    funErrorAlert(global_req_all);
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
                                                                else if (arr[0] === JS_EX_WRONG_AGENCY) {
                                                                    RedirectPageLoginNoSess(global_error_wrong_agency);
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
                                                if(localStorage.getItem("CountCheckAllImportNEAC") === "1")
                                                {
                                                    $('tbody tr td input[type="checkbox"]').prop('checked', true);
                                                    $('tbody tr').css('background', '#d8d8d8');
                                                    localStorage.setItem("CountCheckAllImportNEAC", "1");
                                                    $('#checkAll').prop('checked', true);
                                                    CheckAllTableValue();
                                                    $("#divEditTokenMulti").css("display", "");
                                                }
                                                else
                                                {
                                                    $('tbody tr').css('background', '');
                                                    localStorage.setItem("CountCheckAllImportNEAC", "");
                                                    $('#checkAll').prop('checked', false);
                                                }
                                                if(localStorage.getItem("CountCheckNEACImport") !== "")
                                                {
                                                    ForCheckTable(localStorage.getItem("CountCheckNEACImport"));
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
                                                    var sValueCheck = localStorage.getItem("CountCheckNEACImport");
                                                    var ischecked = $(this).is(':checked');
                                                    if (!ischecked) {
                                                        $(this).closest('tr').css('background', '');
                                                        var sss = sSpace($(this).closest('tr').find("td:eq(7)").text()) + ",";
                                                        sValueCheck = sValueCheck.replace(sss, "");
                                                        $('#checkAll').prop('checked', false);
                                                        localStorage.setItem("CountCheckAllImportNEAC", "");
                                                    }
                                                    else
                                                    {
                                                        $(this).closest('tr').css('background', '#d8d8d8');
                                                        var sCheck = sSpace($(this).closest('tr').find("td:eq(7)").text()) + ",";
                                                        if(sValueCheck.indexOf(sCheck) === -1)
                                                        {
                                                            sValueCheck = sValueCheck + sCheck;
                                                        }
                                                        $("#divEditTokenMulti").css("display", "");
                                                    }
                                                    localStorage.setItem("CountCheckNEACImport", sValueCheck);
                                                });
                                            });
                                            function ForCheckTable(sValueCheck)
                                            {
                                                $('#mtToken').find('tr').each(function () {
                                                    var sCheck = sSpace($(this).find('td:eq(7)').text());
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
                                                var currentCheck = localStorage.getItem("CountCheckNEACImport");
                                                $('#mtToken').find('tr').each(function () {
                                                    var sCheck = sSpace($(this).find('td:eq(7)').text());
                                                    if(sCheck !== "" && currentCheck.indexOf(sCheck + ',') === -1)
                                                    {
                                                        currentCheck = currentCheck + sCheck + ",";
                                                    }
                                                });
                                                localStorage.setItem("CountCheckNEACImport", currentCheck);
                                            }
                                            function checkAllTable()
                                            {
                                                $('#checkAll').change(function () {
                                                    $('tbody tr td input[type="checkbox"]').prop('checked', $(this).prop('checked'));
                                                    $('tbody tr').css('background', '#d8d8d8');
                                                    if ($("#checkAll").is(':checked')) {
                                                        CheckAllTableValue();
                                                        localStorage.setItem("CountCheckAllImportNEAC", "1");
                                                        $("#divEditTokenMulti").css("display", "");
                                                    }
                                                    else
                                                    {
                                                        localStorage.setItem("CountCheckNEACImport", "");
                                                        localStorage.setItem("CountCheckAllImportNEAC", "");
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
                                            <table id="mtToken" class="table table-bordered table-striped projects">
                                                <thead>
                                                    <th style="width: 60px;"><script>document.write(global_fm_STT);</script></th>
                                                    <th><script>document.write(global_fm_serial);</script></th>
                                                    <th><script>document.write(global_fm_Status);</script></th>
                                                    <th><script>document.write(global_fm_duration_cts);</script></th>
                                                    <th><script>document.write(cert_fm_type_request);</script></th>
                                                    <th><script>document.write(global_fm_date_create);</script></th>
                                                    <th><script>document.write(global_fm_action);</script></th>
                                                    <th><input id="checkAll" name="checkAll" onclick="checkAllTable();" type="checkbox"/></th>
<!--                                                    <th><input id="checkAllSynch" name="checkAllSynch" onclick="checkAllTableSynch();" type="checkbox"/> Synch</th>-->
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
                                                                String strID = String.valueOf(rsPgin[0][i].ID);
                                                                String sDisabled = "";
//                                                                String sDisabledSynch = "";
                                                                String sColor = "EMPTY";
                                                                if(rsPgin[0][i].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_CANCEL
                                                                    || rsPgin[0][i].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_SUCCESS
                                                                    || rsPgin[0][i].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_ERROR_RESYNCHRONIZE) {
                                                                    sDisabled = "1";
                                                                } else {
                                                                    sDefaulCheckAll = "1";
                                                                }
//                                                                if(rsPgin[0][i].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_CANCEL
//                                                                    || rsPgin[0][i].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_INITIALIZE
//                                                                    || rsPgin[0][i].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_SUCCESS
//                                                                    || rsPgin[0][i].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_ERROR_RESYNCHRONIZE) {
//                                                                    sDisabledSynch = "1";
//                                                                } else {
//                                                                    sDefaulCheckAllSynch = "1";
//                                                                }
                                                    %>
                                                    <tr>
                                                        <td style="width: 60px;"><%= com.convertMoney(j)%></td>
                                                        <td><%= rsPgin[0][i].CERTIFICATION_SN%></td>
                                                        <td><%= rsPgin[0][i].NEAC_SYNC_STATE_REMARK%></td>
                                                        <td><%= "[" + rsPgin[0][i].CERTIFICATION_PROFILE_NAME + "] " + rsPgin[0][i].CERTIFICATION_PROFILE_DESC %></td>
                                                        <td><%= rsPgin[0][i].CERTIFICATION_ATTR_TYPE_REMARK%></td>
                                                        <td><%= rsPgin[0][i].CREATED_DT%></td>
                                                        <td>
                                                            <a style="cursor: pointer;" onclick="popupEdit('<%= strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_detail);</script> </a>
                                                        </td>
                                                        <td>
                                                            <%
                                                                if("".equals(sDisabled)) {
                                                            %>
                                                            <input id="checkChilren<%= Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX + strID%>" value="value-<%= strID%>" name="checkChilren" class='uncheck' type="checkbox"/>
                                                            <%
                                                                } else {
                                                            %>
                                                            <img src="../Images/icon_checkboxdisable.png" id="idImgCheckBox"/>
                                                            <%
                                                                }
                                                            %>
                                                        </td>
                                                        <td style="display: none;">
                                                            <%
                                                                if("".equals(sDisabled)) {
                                                            %>
                                                            <%= Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX + strID%>
                                                            <%
                                                                } else {
                                                            %>
                                                            <%=sColor%>
                                                            <%
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
<!--                                                        <script>
                                                            $(document).ready(function () {
                                                                document.getElementById("idTDPaging").colSpan = document.getElementById('mtToken').rows[0].cells.length;
                                                            });
                                                        </script>-->
                                                        <td colspan="9" id="idTDPaging" style="text-align: right;">
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
                                <!-- Load Combobox Info Script -->
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
                //global_alert_login
            }();
        </script>
        <%
            }
        %> 
    </body>
</html>