<%-- 
    Document   : HistoryList
    Created on : Oct 17, 2018, 2:34:37 PM
    Author     : THANH-PC
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
            document.title = history_title_list;
            $(document).ready(function () {
                localStorage.setItem("LOCAL_PARAM_HISTORYLIST", null);
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
            });
            function popupDetail(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $('#idCheck').removeClass("js-switch");
                $('#contentEdit').load('HistoryView.jsp', {id:id}, function () {
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
                $(".loading-gif").hide();
                $('#over').remove();
//                localStorage.setItem("LOCAL_PARAM_HISTORYLIST", id);
//                window.location = 'HistoryView.jsp?id=' + id;
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
            .x_panel {
                padding:10px 17px 0 17px;
            }
            .x_content {
                padding: 0 5px 0 5px;
            }
        </style>
    </head>
    <body class="nav-md">
        <%
        if (session.getAttribute("sUserID") != null) {
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
//            String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
//            String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
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
                        document.getElementById("idNameURL").innerHTML = history_title_list;
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
                                try {
                                    SYSTEM_LOG[][] rsPgin = new SYSTEM_LOG[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (session.getAttribute("RefreshSestemLogSess") != null && session.getAttribute("sessFromCreateDateSystemLog") != null
                                            && session.getAttribute("sessToCreateDateApproveReq") != null) {
                                        session.setAttribute("RefreshSestemLogSessPaging", "1");
                                        session.setAttribute("SearchSharePagingRegisToken", "0");
                                        statusLoad = 1;
                                        String ToCreateDate = (String) session.getAttribute("sessToCreateDateApproveReq");
                                        String FromCreateDate = (String) session.getAttribute("sessFromCreateDateSystemLog");
                                        String RESPONSE_CODE_ID = (String) session.getAttribute("sessRESPONSE_CODE_IDApproveReq");
                                        String FUNCTIONALITY_ID = (String) session.getAttribute("sessFUNCTIONALITY_IDApproveReq");
                                        String BillCode = (String) session.getAttribute("sessBillCodeApproveReq");
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSSystemLog");
                                        session.setAttribute("RefreshSestemLogSess", null);
                                        session.setAttribute("sessFromCreateDateSystemLog", FromCreateDate);
                                        session.setAttribute("sessToCreateDateApproveReq", ToCreateDate);
                                        session.setAttribute("sessRESPONSE_CODE_IDApproveReq", RESPONSE_CODE_ID);
                                        session.setAttribute("sessFUNCTIONALITY_IDApproveReq", FUNCTIONALITY_ID);
                                        session.setAttribute("sessBillCodeApproveReq", BillCode);
                                        session.setAttribute("AlertAllTimeSSystemLog", strAlertAllTimes);
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(RESPONSE_CODE_ID)) {
                                            RESPONSE_CODE_ID = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(FUNCTIONALITY_ID)) {
                                            FUNCTIONALITY_ID = "";
                                        }
                                        if("1".equals(strAlertAllTimes))
                                        {
                                            FromCreateDate = "";
                                            ToCreateDate = "";
                                        }
                                        int ss = 0;
                                        if ((session.getAttribute("CountListApproveReq")) == null) {
                                            ss = db.S_BO_SYSTEM_LOG_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FUNCTIONALITY_ID)
                                                ,EscapeUtils.escapeHtmlSearch(RESPONSE_CODE_ID), EscapeUtils.escapeHtmlSearch(BillCode));
                                            session.setAttribute("CountListApproveReq", String.valueOf(ss));
                                        } else {
                                            String sCount = (String) session.getAttribute("CountListApproveReq");
                                            ss = Integer.parseInt(sCount);
                                            session.setAttribute("CountListApproveReq", String.valueOf(ss));
                                        }
                                        if (session.getAttribute("SearchIPageNoPagingApproveReq") != null) {
                                            String sPage = (String) session.getAttribute("SearchIPageNoPagingApproveReq");
                                            iPagNo = Integer.parseInt(sPage);
                                        }
                                        if (session.getAttribute("SearchISwRwsPagingApproveReq") != null) {
                                            String sSumPage = (String) session.getAttribute("SearchISwRwsPagingApproveReq");
                                            iSwRws = Integer.parseInt(sSumPage);
                                        }
                                        if (session.getAttribute("RefreshSestemLogSessNumberPaging") != null) {
                                            String sNoPage = (String) session.getAttribute("RefreshSestemLogSessNumberPaging");
                                            iPaNoSS = Integer.parseInt(sNoPage);
                                        }
                                        session.setAttribute("SearchIPageNoPagingApproveReq", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingApproveReq", String.valueOf(iSwRws));
                                        if (ss > 0) {
                                            db.S_BO_SYSTEM_LOG_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(FUNCTIONALITY_ID), EscapeUtils.escapeHtmlSearch(RESPONSE_CODE_ID),
                                                EscapeUtils.escapeHtmlSearch(BillCode), sessLanguageGlobal, rsPgin, iPagNo, iSwRws);
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
                                        session.setAttribute("RefreshSestemLogSessPaging", null);
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("SearchShareStoreRegisToken", null);
                                            session.setAttribute("SearchIPageNoPagingApproveReq", null);
                                            session.setAttribute("SearchISwRwsPagingApproveReq", null);
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
                                    String RESPONSE_CODE_ID =request.getParameter("RESPONSE_CODE_ID");
                                    String FUNCTIONALITY_ID = request.getParameter("FUNCTIONALITY_ID");
                                    String BillCode = request.getParameter("BillCode");
                                    Boolean nameCheck = Boolean.valueOf(request.getParameter("nameCheck") != null);
                                    if (nameCheck == false) {
                                        FromCreateDate = "";
                                        ToCreateDate = "";
                                        strAlertAllTimes = "1";
                                    }
                                    if ("1".equals(hasPaging)) {
                                        session.setAttribute("SearchSharePagingRegisToken", "0");
                                        ToCreateDate = (String) session.getAttribute("sessToCreateDateApproveReq");
                                        FromCreateDate = (String) session.getAttribute("sessFromCreateDateSystemLog");
                                        RESPONSE_CODE_ID = (String) session.getAttribute("sessRESPONSE_CODE_IDApproveReq");
                                        FUNCTIONALITY_ID = (String) session.getAttribute("sessFUNCTIONALITY_IDApproveReq");
                                        BillCode = (String) session.getAttribute("sessBillCodeApproveReq");
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSSystemLog");
                                        session.setAttribute("SessParamOnPagingCertList", null);
                                    } else {
                                        session.setAttribute("SearchSharePagingRegisToken", "1");
                                        session.setAttribute("CountListApproveReq", null);
                                    }
                                    session.setAttribute("sessFromCreateDateSystemLog", FromCreateDate);
                                    session.setAttribute("sessToCreateDateApproveReq", ToCreateDate);
                                    session.setAttribute("sessRESPONSE_CODE_IDApproveReq", RESPONSE_CODE_ID);
                                    session.setAttribute("sessFUNCTIONALITY_IDApproveReq", FUNCTIONALITY_ID);
                                    session.setAttribute("sessBillCodeApproveReq", BillCode);
                                    session.setAttribute("AlertAllTimeSSystemLog", strAlertAllTimes);
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(RESPONSE_CODE_ID)) {
                                        RESPONSE_CODE_ID = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(FUNCTIONALITY_ID)) {
                                        FUNCTIONALITY_ID = "";
                                    }
                                    int ss = 0;
                                    if ((session.getAttribute("CountListApproveReq")) == null) {
                                        ss = db.S_BO_SYSTEM_LOG_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(ToCreateDate),EscapeUtils.escapeHtmlSearch(FUNCTIONALITY_ID),
                                            EscapeUtils.escapeHtmlSearch(RESPONSE_CODE_ID), EscapeUtils.escapeHtmlSearch(BillCode));
                                        session.setAttribute("CountListApproveReq", String.valueOf(ss));
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListApproveReq");
                                        ss = Integer.parseInt(sCount);
                                        session.setAttribute("CountListApproveReq", String.valueOf(ss));
                                    }
                                    iTotRslts = ss;
                                    if (iTotRslts > 0) {
                                        db.S_BO_SYSTEM_LOG_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(FUNCTIONALITY_ID), EscapeUtils.escapeHtmlSearch(RESPONSE_CODE_ID),
                                            EscapeUtils.escapeHtmlSearch(BillCode), sessLanguageGlobal, rsPgin, iPagNo, iSwRws);
                                        session.setAttribute("SearchIPageNoPagingApproveReq", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingApproveReq", String.valueOf(iSwRws));
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
                                    session.setAttribute("RefreshSestemLogSessPaging", null);
                                    session.setAttribute("SearchShareStoreRegisToken", null);
                                    session.setAttribute("SearchIPageNoPagingApproveReq", null);
                                    session.setAttribute("SearchISwRwsPagingApproveReq", null);
                                    session.setAttribute("sessFromCreateDateSystemLog", null);
                                    session.setAttribute("sessToCreateDateApproveReq", null);
                                    session.setAttribute("AlertAllTimeSSystemLog", null);
                                    session.setAttribute("sessBillCodeApproveReq", null);
                                    session.setAttribute("sessRESPONSE_CODE_IDApproveReq", "All");
                                    session.setAttribute("sessFUNCTIONALITY_IDApproveReq", "All");
                                }
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
<!--                                    <div class="x_title">
                                        <h2><i class="fa fa-search"></i> <script>document.write(history_title_search);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                
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
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <div class="col-sm-5" style="padding-right: 0px;text-align: right;">
                                                            <label class="switch" for="idCheck">
                                                                <input type="checkbox" name="nameCheck" id="idCheck" onchange="checkboxChange();" <%= session.getAttribute("AlertAllTimeSSystemLog") != null && "1".equals(session.getAttribute("AlertAllTimeSSystemLog").toString()) ? "" : "checked" %>/>
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
                                                            <input type="Text" id="demo1" name="FromCreateDate" <%= session.getAttribute("AlertAllTimeSSystemLog") != null && "1".equals(session.getAttribute("AlertAllTimeSSystemLog").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessFromCreateDateSystemLog") != null && !"1".equals(session.getAttribute("AlertAllTimeSSystemLog").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromCreateDateSystemLog").toString()) : com.ConvertMonthSub(30)%>" maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_ToDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo2" name="ToCreateDate" <%= session.getAttribute("AlertAllTimeSSystemLog") != null && "1".equals(session.getAttribute("AlertAllTimeSSystemLog").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessToCreateDateApproveReq") != null && !"1".equals(session.getAttribute("AlertAllTimeSSystemLog").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToCreateDateApproveReq").toString()) : com.ConvertMonthSub(0)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(history_fm_function);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="FUNCTIONALITY_ID" id="FUNCTIONALITY_ID" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessFUNCTIONALITY_IDApproveReq") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessFUNCTIONALITY_IDApproveReq").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    FUNCTIONALITY[][] rsType = new FUNCTIONALITY[1][];
                                                                    db.S_BO_FUNCTIONALITY_COMBOBOX(sessLanguageGlobal, rsType);
                                                                    if (rsType[0].length > 0) {
                                                                        for (FUNCTIONALITY temp1 : rsType[0]) {
                                                                            if(temp1.ID != Definitions.CONFIG_LOG_FUNCTIONALITY_INT_ALL) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessFUNCTIONALITY_IDApproveReq") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessFUNCTIONALITY_IDApproveReq").toString()) ? "selected" : ""%>><%= EscapeUtils.CheckTextNull(temp1.REMARK)%></option>
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
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(history_fm_response);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="RESPONSE_CODE_ID" id="RESPONSE_CODE_ID" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessRESPONSE_CODE_IDApproveReq") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessRESPONSE_CODE_IDApproveReq").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    RESPONSE_CODE[][] rsState = new RESPONSE_CODE[1][];
                                                                    db.S_BO_RESPONSE_CODE_COMBOBOX(sessLanguageGlobal, rsState);
                                                                    if (rsState[0].length > 0) {
                                                                        for (RESPONSE_CODE temp1 : rsState[0]) {
                                                                 %>
                                                                 <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessRESPONSE_CODE_IDApproveReq") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessRESPONSE_CODE_IDApproveReq").toString()) ? "selected" : ""%>><%= EscapeUtils.CheckTextNull(temp1.REMARK)%></option>
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
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_billcode);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="BillCode" name="BillCode"
                                                                value="<%= session.getAttribute("sessBillCodeApproveReq") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessBillCodeApproveReq").toString()) : ""%>"
                                                                maxlength="64" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <div class="col-sm-5" style="padding-right: 0px;text-align: right;">
                                                            <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                        </div>
                                                        <div class="col-sm-7" style="padding-right: 0px;"></div>
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
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(history_title_table);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;"><script>document.write(global_label_grid_sum);</script><%= strMess%></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <input type="hidden" name="iPagNo" value="<%=iPagNo%>">
                                        <input type="hidden" name="cPagNo" value="<%=cPagNo%>">
                                        <input type="hidden" name="iSwRws" value="<%=iSwRws%>">
                                        <div class="table-responsive">
                                        <table id="idTableList" class="table table-bordered table-striped projects">
                                            <thead>
                                            <th><script>document.write(global_fm_STT);</script></th>
                                            <th><script>document.write(history_fm_function);</script></th>
                                            <th><script>document.write(global_fm_billcode);</script></th>
                                            <th><script>document.write(history_fm_response);</script></th>
                                            <th><script>document.write(global_fm_user_create);</script></th>
                                            <th><script>document.write(token_fm_agent);</script></th>
                                            <th><script>document.write(global_fm_date_create);</script></th>
                                            <th><script>document.write(global_fm_action);</script></th>
                                            </thead>
                                            <tbody>
                                                <%
                                                    if (iPaNoSS > 1) {
                                                        j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                    }
                                                    session.setAttribute("RefreshSestemLogSessNumberPaging", String.valueOf(iPaNoSS));
                                                    if (rsPgin[0].length > 0) {
                                                        for (SYSTEM_LOG temp1 : rsPgin[0]) {
                                                            String sSYSTEM_LOG_ID = String.valueOf(temp1.ID);
                                                %>
                                                <tr>
                                                    <td><%= com.convertMoney(j)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.FUNCTIONALITY_DESC)%></td>
                                                    <td><%= temp1.BILLCODE%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.RESPONSE_CODE_DESC)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_BY)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.BRANCH_DESC)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_DT)%></td>
                                                    <td>
                                                        <a style="cursor: pointer;" onclick="popupDetail('<%=sSYSTEM_LOG_ID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_detail);</script> </a>
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