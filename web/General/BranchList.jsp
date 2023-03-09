<%-- 
    Document   : BranchList
    Created on : Feb 25, 2014, 11:12:41 AM
    Author     : Thanh
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
            document.title = branch_title_list;
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
//            function popupEdit(id)
//            {
//                localStorage.setItem("LOCAL_PARAM_BRANCHLIST", id);
//                window.location = 'BranchEdit.jsp?id=' + id;
//            }
            function addForm() {
                window.location = 'BranchNew.jsp';
            }
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
                $('#contentEdit').load('BranchEdit.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function popupProfile(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('BranchProflieAccess.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function popupAPIAccess(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('BranchAPIAccess.jsp', {id:id}, function () {
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
                        document.getElementById("idNameURL").innerHTML = branch_title_list;
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
                                try {
                                    BRANCH[][] rsPgin = new BRANCH[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (session.getAttribute("SessRefreshBranch") != null && session.getAttribute("sessFromCreateDateBranchList") != null
                                            && session.getAttribute("sessToCreateDateBranchList") != null) {
                                        session.setAttribute("RefreshBranchListSessPaging", "1");
                                        session.setAttribute("SearchSharePagingBranchList", "0");
                                        statusLoad = 1;
                                        String ToCreateDate = (String) session.getAttribute("sessToCreateDateBranchList");
                                        String FromCreateDate = (String) session.getAttribute("sessFromCreateDateBranchList");
                                        String NAME = (String) session.getAttribute("sessNAMEBranchList");
                                        String CITY_PROVINCE = (String) session.getAttribute("sessCITY_PROVINCEBranchList");

                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSBranchList");
                                        session.setAttribute("SessRefreshBranch", null);
                                        session.setAttribute("sessFromCreateDateBranchList", FromCreateDate);
                                        session.setAttribute("sessToCreateDateBranchList", ToCreateDate);
                                        session.setAttribute("sessNAMEBranchList", NAME);

                                        session.setAttribute("sessCITY_PROVINCEBranchList", CITY_PROVINCE);
                                        session.setAttribute("AlertAllTimeSBranchList", strAlertAllTimes);
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CITY_PROVINCE)) {
                                            CITY_PROVINCE = "";
                                        }
                                        if ("1".equals(strAlertAllTimes)) {
                                            FromCreateDate = "";
                                            ToCreateDate = "";
                                        }
                                        int ss = 0;
                                        if ((session.getAttribute("CountListApproveReq")) == null) {
                                            ss = db.S_BO_BRANCH_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                    EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(NAME),
                                                    EscapeUtils.escapeHtmlSearch(CITY_PROVINCE), sessTreeArrayBranchID);
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
                                            db.S_BO_BRANCH_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(NAME), EscapeUtils.escapeHtmlSearch(CITY_PROVINCE),
                                                sessLanguageGlobal, sessTreeArrayBranchID, rsPgin, iPagNo, iSwRws);
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
                                    String NAME = EscapeUtils.ConvertStringToUnicode(request.getParameter("NAME"));
                                    String CITY_PROVINCE = request.getParameter("CITY_PROVINCE");
                                    Boolean nameCheck = Boolean.valueOf(request.getParameter("nameCheck") != null);
                                    if (nameCheck == false) {
                                        FromCreateDate = "";
                                        ToCreateDate = "";
                                        strAlertAllTimes = "1";
                                    }
                                    if ("1".equals(hasPaging)) {
                                        session.setAttribute("SearchSharePagingBranchList", "0");
                                        ToCreateDate = (String) session.getAttribute("sessToCreateDateBranchList");
                                        FromCreateDate = (String) session.getAttribute("sessFromCreateDateBranchList");
                                        NAME = (String) session.getAttribute("sessNAMEBranchList");
                                        CITY_PROVINCE = (String) session.getAttribute("sessCITY_PROVINCEBranchList");
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSBranchList");
                                        session.setAttribute("SessParamOnPagingCertList", null);
                                    } else {
                                        session.setAttribute("SearchSharePagingBranchList", "1");
                                        session.setAttribute("CountListApproveReq", null);
                                    }
                                    session.setAttribute("sessFromCreateDateBranchList", FromCreateDate);
                                    session.setAttribute("sessToCreateDateBranchList", ToCreateDate);
                                    session.setAttribute("sessNAMEBranchList", NAME);
                                    session.setAttribute("sessCITY_PROVINCEBranchList", CITY_PROVINCE);
                                    session.setAttribute("AlertAllTimeSBranchList", strAlertAllTimes);
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CITY_PROVINCE)) {
                                        CITY_PROVINCE = "";
                                    }
                                    int ss = 0;
                                    if ((session.getAttribute("CountListApproveReq")) == null) {
                                        ss = db.S_BO_BRANCH_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(NAME),
                                                EscapeUtils.escapeHtmlSearch(CITY_PROVINCE), sessTreeArrayBranchID);
                                        session.setAttribute("CountListApproveReq", String.valueOf(ss));
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListApproveReq");
                                        ss = Integer.parseInt(sCount);
                                        session.setAttribute("CountListApproveReq", String.valueOf(ss));
                                    }
                                    iTotRslts = ss;
                                    if (iTotRslts > 0) {
                                        db.S_BO_BRANCH_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(NAME), EscapeUtils.escapeHtmlSearch(CITY_PROVINCE),
                                            sessLanguageGlobal, sessTreeArrayBranchID, rsPgin, iPagNo, iSwRws);
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
                                    session.setAttribute("sessFromCreateDateBranchList", null);
                                    session.setAttribute("sessToCreateDateBranchList", null);
                                    session.setAttribute("sessNAMEBranchList", null);
                                    session.setAttribute("AlertAllTimeSBranchList", null);
                                    session.setAttribute("sessCITY_PROVINCEBranchList", "All");
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
                                                            <input type="Text" id="demo1" name="FromCreateDate" <%= session.getAttribute("AlertAllTimeSBranchList") != null
                                                                    && "1".equals(session.getAttribute("AlertAllTimeSBranchList").toString()) ? "disabled" : ""%>
                                                                   value="<%= session.getAttribute("sessFromCreateDateBranchList") != null
                                                                           && !"1".equals(session.getAttribute("AlertAllTimeSBranchList").toString())
                                                                                ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromCreateDateBranchList").toString())
                                                                                : com.ConvertMonthSub(30)%>" maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_ToDate);</script></label>
                                                        <div class="col-sm-8" style="padding-right: 0px;text-align: left;">
                                                            <input type="Text" id="demo2" name="ToCreateDate" <%= session.getAttribute("AlertAllTimeSBranchList") != null
                                                                    && "1".equals(session.getAttribute("AlertAllTimeSBranchList").toString()) ? "disabled" : ""%>
                                                                   value="<%= session.getAttribute("sessToCreateDateBranchList") != null
                                                                           && !"1".equals(session.getAttribute("AlertAllTimeSBranchList").toString())
                                                                                   ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToCreateDateBranchList").toString()) : com.ConvertMonthSub(0)%>"
                                                                   maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <div class="col-sm-4" style="padding-right: 0px;text-align: right;">
                                                            <label class="switch" for="idCheck">
                                                                <input type="checkbox" name="nameCheck" id="idCheck" onchange="checkboxChange();" <%= session.getAttribute("AlertAllTimeSBranchList") != null && "1".equals(session.getAttribute("AlertAllTimeSBranchList").toString()) ? "" : "checked"%>/>
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
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(branch_fm_code);</script></label>
                                                        <div class="col-sm-8" style="padding-right: 0px;text-align: left;">
                                                            <input type="text" name="NAME" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_NAME%>" value="<%= session.getAttribute("sessNAMEBranchList") != null
                                                                    ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessNAMEBranchList").toString()) : ""%>" class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_city);</script></label>
                                                        <div class="col-sm-8" style="padding-right: 0px;text-align: left;">
                                                            <select name="CITY_PROVINCE" id="CITY_PROVINCE" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCITY_PROVINCEBranchList") != null
                                                                        && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCITY_PROVINCEBranchList").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    CITY_PROVINCE[][] rsState = new CITY_PROVINCE[1][];
                                                                    db.S_BO_PROVINCE_COMBOBOX(sessLanguageGlobal, rsState);
                                                                    if (rsState[0].length > 0) {
                                                                        for (CITY_PROVINCE temp1 : rsState[0]) {
                                                                %>
                                                                        <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessCITY_PROVINCEBranchList") != null
                                                                        && String.valueOf(temp1.ID).equals(session.getAttribute("sessCITY_PROVINCEBranchList").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                        <div class="col-sm-4" style="padding-right: 0px;text-align: left;">
                                                        </div>
                                                        <div class="col-sm-8" style="padding-right: 0px;text-align: left;">
                                                            <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                            <input id="btnNew" type="button" class="btn btn-info" onclick="addForm();"/>
                                                            <script>
                                                                document.getElementById("btnNew").value = global_fm_button_New;
                                                            </script>
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
                                        String apiCongifEnabled = cogCommon.GetPropertybyCode(Definitions.CONFIG_API_ACCESS_ENABLED);
                                %>
                                <div class="x_panel">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(branch_title_table);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <script>document.write(global_label_grid_sum);</script><%= strMess%>
                                                &nbsp;&nbsp;
                                                <button type="button" class="btn btn-info" onClick="exportAgency('<%= iTotRslts%>');"><script>document.write(global_fm_button_export_csv);</script></button>
                                            </li>
                                            <script>
                                                function exportAgency(vSum)
                                                {
                                                    $('body').append('<div id="over"></div>');
                                                    $(".loading-gif").show();
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../ExportCSVParam",
                                                        data: {
                                                            idParam: "exportagencylist",
                                                            vSum: vSum
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
                                            <table id="idTableList" class="table table-bordered table-striped projects">
                                                <thead>
                                                <th><script>document.write(global_fm_STT);</script></th>
                                                <th><script>document.write(branch_fm_code);</script></th>
                                                <th><script>document.write(branch_fm_name);</script></th>
                                                <th><script>document.write(branch_fm_parent);</script></th>
                                                <th><script>document.write(branch_fm_level);</script></th>
                                                <th><script>document.write(global_fm_Status);</script></th>
                                                <th><script>document.write(global_fm_city);</script></th>
                                                <th><script>document.write(global_fm_date_create);</script></th>
                                                <th><script>document.write(global_fm_action);</script></th>
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
                                                                String strBranchNameParent = EscapeUtils.CheckTextNull(rsPgin[0][i].PARENT_NAME);
                                                    %>
                                                    <tr>
                                                        <td><%= com.convertMoney(j)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(rsPgin[0][i].NAME)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(rsPgin[0][i].REMARK)%></td>
                                                        <td><%= strBranchNameParent%></td>
                                                        <td><%= Integer.parseInt(rsPgin[0][i].AGENCY_LEVEL) - 1%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(rsPgin[0][i].BRANCH_STATE_DESC)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(rsPgin[0][i].PROVINCE_REMARK)%></td>
                                                        <td><%= rsPgin[0][i].CREATED_DT%></td>
                                                        <td>
                                                            <a style="cursor: pointer;" onclick="popupEdit('<%= strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_button_grid_edit);</script> </a>
                                                            <%
                                                                if("1".equals(apiCongifEnabled) && sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                            %>
                                                            <a style="cursor: pointer;" onclick="popupAPIAccess('<%= strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_API);</script> </a>
                                                            <a style="cursor: pointer;" onclick="popupProfile('<%= strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_configAPI);</script> </a>
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
<!--                    <script>
                        $(document).ready(function () {
                            if(localStorage.getItem("EDIT_BRANCH") !== null && localStorage.getItem("EDIT_BRANCH") !== "null")
                            {
                                var vIDEDIT_BRANCH = localStorage.getItem("EDIT_BRANCH");
                                localStorage.setItem("EDIT_BRANCH", null);
                                popupEdit(vIDEDIT_BRANCH);
                            }
                        });
                    </script>-->
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