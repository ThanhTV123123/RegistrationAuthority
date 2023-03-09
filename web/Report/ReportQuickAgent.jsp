<%-- 
    Document   : ReportQuickAgent
    Created on : Aug 28, 2018, 11:02:44 AM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<%    String strAlertAllTimes = "0";
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <link href="../style/switchery/dist/switchery.min.css" rel="stylesheet">
        <%
            String sNewRefreshJS = cogCommon.GetPropertybyCode(Definitions.CONFIG_JS_REFRESH_STRING_RANDOM);
        %>
        <script src="../js/Language.js?t=<%=sNewRefreshJS%>"></script>
        <script src="../js/process_javajs.js?t=<%=sNewRefreshJS%>"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <title></title>
        <script language="javascript">
            document.title = reportquick_title_list;
            changeFavicon("../");
            $(document).ready(function () {
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
            $(document).ready(function () {
                $('.loading-gifHardware').hide();
                $('#myModalOTPHardware').modal({
                    backdrop: 'static',
                    keyboard: true,
                    show: false
                });
            });
            function ExportExcelNew(idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../ExportCSVParam",
                    data: {
                        idParam: "exportcertquick",
                        CsrfToken: idCSRF
                    },
                    catche: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            var f = document.form;
                            f.method = "post";
                            f.action = '../DownFromSaveFile?idParam=downfileexportquick&&name=' + arr[2];
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
            .level0{
                background: red;
            }
            .collapse_abc .toggle_abc {
                background: url("../Images/collapse.gif");
            }
            .expand_abc .toggle_abc {
                background: url("../Images/expand.gif");

            }
            .toggle_abc {
                height: 9px;
                width: 9px;
                display: inline-block;   
            }
            .x_panel {
                padding:10px 17px 0 17px;
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
                        document.getElementById("idNameURL").innerHTML = reportquick_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%
                                int status = 1000;
                                int statusLoad = 0;
                                String FromCreateDate = "";
                                String ToCreateDate = "";
                                String sPage_Num_Cert = cogCommon.GetPropertybyCode(Definitions.CONFIG_PAGING_NUMBER_REPORT_CERT).trim();
                                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                                String sessTreeArrayBranchID = session.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                try {
                                    REPORT_QUICK_BRANCH[][] rsReportBranch = new REPORT_QUICK_BRANCH[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (request.getMethod().equals("POST"))
                                    {
                                        statusLoad = 1;
                                        session.setAttribute("pIaReportQuickAgent", null);
                                        session.setAttribute("pIbReportQuickAgent", null);
                                        request.setCharacterEncoding("UTF-8");
                                        FromCreateDate = request.getParameter("FromCreateDate");
                                        ToCreateDate = request.getParameter("ToCreateDate");
                                        String idBranchOffice = request.getParameter("idBranchOffice");
                                        String idBRANCH_STATE = request.getParameter("BRANCH_STATE");
                                        Boolean nameCheck = Boolean.valueOf(request.getParameter("nameCheck") != null);
                                        if (nameCheck == false) {
                                            FromCreateDate = "";
                                            ToCreateDate = "";
                                            strAlertAllTimes = "1";
                                        }
                                        session.setAttribute("sessFromCreateDateReportQuickAgent", FromCreateDate);
                                        session.setAttribute("sessToCreateDateReportQuickAgent", ToCreateDate);
                                        session.setAttribute("sessBranchOfficeReportQuickAgent", idBranchOffice);
                                        session.setAttribute("sessBranchStateReportQuickAgent", idBRANCH_STATE);
                                        session.setAttribute("AlertAllTimeSReportQuickAgent", strAlertAllTimes);
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idBranchOffice)) {
                                            idBranchOffice = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idBRANCH_STATE)) {
                                            idBRANCH_STATE = "";
                                        }
                                        db.S_BO_REPORT_TOTAL_BRANCH(EscapeUtils.CheckTextNull(FromCreateDate), EscapeUtils.CheckTextNull(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(idBranchOffice), idBRANCH_STATE, sessLanguageGlobal,
                                            rsReportBranch, SessUserAgentID, sessTreeArrayBranchID);
                                        if (rsReportBranch[0].length > 0) {
                                            status = 1;
                                        } else {
                                            status = 1000;
                                        }
                                    } else {
                                        session.setAttribute("pIaReportQuickAgent", null);
                                        session.setAttribute("pIbReportQuickAgent", null);
                                        session.setAttribute("sessFromCreateDateReportQuickAgent", null);
                                        session.setAttribute("sessToCreateDateReportQuickAgent", null);
                                        session.setAttribute("AlertAllTimeSReportQuickAgent", null);
                                        session.setAttribute("sessBranchOfficeReportQuickAgent", "All");
                                        session.setAttribute("sessBranchStateReportQuickAgent", "All");
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
                                                        <div class="col-sm-5" style="padding-right: 0px;text-align: right;">
                                                            <label class="switch" for="idCheck">
                                                                <input type="checkbox" name="nameCheck" id="idCheck" onchange="checkboxChange();" <%= session.getAttribute("AlertAllTimeSReportQuickAgent") != null && "1".equals(session.getAttribute("AlertAllTimeSReportQuickAgent").toString()) ? "" : "checked"%>/>
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
                                                            <input type="Text" id="demo1" name="FromCreateDate" <%= session.getAttribute("AlertAllTimeSReportQuickAgent") != null
                                                                && "1".equals(session.getAttribute("AlertAllTimeSReportQuickAgent").toString()) ? "disabled" : ""%>
                                                               value="<%= session.getAttribute("sessFromCreateDateReportQuickAgent") != null
                                                                    && !"1".equals(session.getAttribute("AlertAllTimeSReportQuickAgent").toString())
                                                                     ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromCreateDateReportQuickAgent").toString())
                                                                     : com.ConvertMonthSub(30)%>" maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_ToDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo2" name="ToCreateDate" <%= session.getAttribute("AlertAllTimeSReportQuickAgent") != null
                                                                && "1".equals(session.getAttribute("AlertAllTimeSReportQuickAgent").toString()) ? "disabled" : ""%>
                                                               value="<%= session.getAttribute("sessToCreateDateReportQuickAgent") != null
                                                                       && !"1".equals(session.getAttribute("AlertAllTimeSReportQuickAgent").toString())
                                                                               ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToCreateDateReportQuickAgent").toString()) : com.ConvertMonthSub(0)%>"
                                                               maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <%
//                                                            if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                                        %>
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_Branch);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="idBranchOffice" id="idBranchOffice" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessBranchOfficeReportQuickAgent") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessBranchOfficeReportQuickAgent").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    BRANCH[][] rsBranch = (BRANCH[][]) session.getAttribute("sessTreeBranchSystemAgency");
                                                                    if (rsBranch[0].length > 0) {
                                                                        for (BRANCH temp1 : rsBranch[0]) {
                                                                            if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT))
                                                                            {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessBranchOfficeReportQuickAgent") != null
                                                                    && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeReportQuickAgent").toString())
                                                                    ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                                <%
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                        <%
//                                                            } else {
                                                        %>
                                                        <!--<input type="text" readonly style="display: none;" name="idBranchOffice" id="idBranchOffice" value="<= SessUserAgentID%>" class="form-control123">-->
                                                        <%
//                                                            }
                                                        %>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-left: 0;">
                                                            <label id="idLblTitleBranchState"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                                            <select name="BRANCH_STATE" id="BRANCH_STATE" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessBranchStateReportQuickAgent") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessBranchStateReportQuickAgent").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    BRANCH_STATE[][] rsState = new BRANCH_STATE[1][];
                                                                    try {
                                                                        db.S_BO_BRANCH_STATE_COMBOBOX(sessLanguageGlobal, rsState);
                                                                        if (rsState[0].length > 0) {
                                                                            for (int i = 0; i < rsState[0].length; i++) {
                                                                %>
                                                                <option value="<%=String.valueOf(rsState[0][i].ID)%>" <%= session.getAttribute("sessBranchStateReportQuickAgent") != null
                                                                    && String.valueOf(rsState[0][i].ID).equals(session.getAttribute("sessBranchStateReportQuickAgent").toString())
                                                                    ? "selected" : ""%>><%=rsState[0][i].REMARK%></option>
                                                                <%
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
                                                        <script>
                                                            $("#idLblTitleBranchState").text(global_fm_branch_status);
                                                        </script>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <div class="col-sm-5" style="padding-right: 0px;">
                                                            
                                                        </div>
                                                        <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                                            <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
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
                                <div class="x_panel">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(branch_title_table);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <button type="button" class="btn btn-info" onClick="ExportExcelNew('<%= anticsrf%>');"><script>document.write(global_fm_button_export_csv);</script></button>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <style type="text/css">
                                            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                            .table > thead > tr > th{border-bottom: none;}
                                            .btn{margin-bottom: 0px;}
                                        </style>
                                        <div class="table-responsive">
                                        <table id="mytable" class="table table-bordered table-striped projects">
                                            <thead>
                                            <th><script>document.write(global_fm_Branch);</script></th>
                                            <th><script>document.write(reportquick_fm_innit);</script></th>
                                            <th><script>document.write(reportquick_fm_activation);</script></th>
                                            <th><script>document.write(reportquick_fm_revoke);</script></th>
                                            <th><script>document.write(reportquick_fm_total);</script></th>
                                            </thead>
                                            <tbody>
                                                <%
                                                    for (int i = 0; i < rsReportBranch[0].length; i++) {
                                                        int sSUMBranch = rsReportBranch[0][i].TOTAL_INITIALIZED + rsReportBranch[0][i].TOTAL_OPERATED + rsReportBranch[0][i].TOTAL_REVOKED;
                                                %>
                                                <tr data-depth="0" class="collapse_abc level0" style="background: #C1DAD7;">
                                                    <td>
                                                        <span class="toggle_abc collapse_abc"></span>
                                                        <%= EscapeUtils.CheckTextNull(rsReportBranch[0][i].BRANCH_DESC) + " (" + EscapeUtils.CheckTextNull(rsReportBranch[0][i].BRANCH_NAME) + ")" %>
                                                    </td>
                                                    <td><%= rsReportBranch[0][i].TOTAL_INITIALIZED%></td>
                                                    <td><%= rsReportBranch[0][i].TOTAL_OPERATED%></td>
                                                    <td><%= rsReportBranch[0][i].TOTAL_REVOKED%></td>
                                                    <td><%= sSUMBranch%></td>
                                                </tr>
                                                <%
                                                        REPORT_QUICK_BRANCH[][] rsReportUser = new REPORT_QUICK_BRANCH[1][];
                                                        db.S_BO_REPORT_TOTAL_BRANCH_USER(EscapeUtils.CheckTextNull(FromCreateDate), EscapeUtils.CheckTextNull(ToCreateDate),
                                                                String.valueOf(rsReportBranch[0][i].BRANCH_ID), rsReportUser, SessUserAgentID);
                                                        if (rsReportUser[0].length > 0) {
                                                            for (int j = 0; j < rsReportUser[0].length; j++) {
                                                                String sIDInit = EscapeUtils.CheckTextNull(FromCreateDate) + "###" + EscapeUtils.CheckTextNull(ToCreateDate)
                                                                        + "###" + String.valueOf(rsReportBranch[0][i].BRANCH_ID)
                                                                        + "###" + String.valueOf(rsReportUser[0][j].CREATED_BY)
                                                                        + "###" + String.valueOf(Definitions.CONFIG_CERTIFICATION_STATE_NEW)
                                                                        + "###" + anticsrf + "###" + EscapeUtils.CheckTextNull(rsReportBranch[0][i].BRANCH_DESC) + " (" + EscapeUtils.CheckTextNull(rsReportBranch[0][i].BRANCH_NAME) + ")";
                                                                String sIDActivation = EscapeUtils.CheckTextNull(FromCreateDate) + "###" + EscapeUtils.CheckTextNull(ToCreateDate)
                                                                        + "###" + String.valueOf(rsReportBranch[0][i].BRANCH_ID)
                                                                        + "###" + String.valueOf(rsReportUser[0][j].CREATED_BY)
                                                                        + "###" + String.valueOf(Definitions.CONFIG_CERTIFICATION_STATE_OPERATED)
                                                                        + "###" + anticsrf + "###" + EscapeUtils.CheckTextNull(rsReportBranch[0][i].BRANCH_DESC) + " (" + EscapeUtils.CheckTextNull(rsReportBranch[0][i].BRANCH_NAME) + ")";
                                                                String sIDRevoke = EscapeUtils.CheckTextNull(FromCreateDate) + "###" + EscapeUtils.CheckTextNull(ToCreateDate)
                                                                        + "###" + String.valueOf(rsReportBranch[0][i].BRANCH_ID)
                                                                        + "###" + String.valueOf(rsReportUser[0][j].CREATED_BY)
                                                                        + "###" + String.valueOf(Definitions.CONFIG_CERTIFICATION_STATE_REVOKED)
                                                                        + "###" + anticsrf + "###" + EscapeUtils.CheckTextNull(rsReportBranch[0][i].BRANCH_DESC) + " (" + EscapeUtils.CheckTextNull(rsReportBranch[0][i].BRANCH_NAME) + ")";
                                                                int sSUMUser = rsReportUser[0][j].TOTAL_INITIALIZED + rsReportUser[0][j].TOTAL_OPERATED + rsReportUser[0][j].TOTAL_REVOKED;
                                                                if(sSUMUser == 0 && rsReportUser[0][j].USER_STATE_ID == Definitions.CONFIG_USER_STATE_CANCEL_ID)
                                                                { } else {
                                                    %>
                                                    <tr data-depth="1" class="level1">
                                                        <td>
                                                            <span class="toggle_abc"></span>
                                                            <%= EscapeUtils.CheckTextNull(rsReportUser[0][j].USERNAME)%>
                                                        </td>
                                                        <td>
                                                            <%
                                                                if (rsReportUser[0][j].TOTAL_INITIALIZED != 0) {
                                                            %>
                                                            <a style="color: blue;text-decoration: underline;" href="#myModalOTPHardware" data-toggle="modal" data-book-id="<%= sIDInit%>"><%= rsReportUser[0][j].TOTAL_INITIALIZED%></a>
                                                            <%
                                                            } else {
                                                            %>
                                                            <%= rsReportUser[0][j].TOTAL_INITIALIZED%>
                                                            <%
                                                                }
                                                            %>
                                                        </td>
                                                        <td>
                                                            <%
                                                                if (rsReportUser[0][j].TOTAL_OPERATED != 0) {
                                                            %>
                                                            <a style="color: blue;text-decoration: underline;" href="#myModalOTPHardware" data-toggle="modal" data-book-id="<%= sIDActivation%>"><%= rsReportUser[0][j].TOTAL_OPERATED%></a>
                                                            <%
                                                            } else {
                                                            %>
                                                            <%= rsReportUser[0][j].TOTAL_OPERATED%>
                                                            <%
                                                                }
                                                            %>
                                                        </td>
                                                        <td>
                                                            <%
                                                                if (rsReportUser[0][j].TOTAL_REVOKED != 0) {
                                                            %>
                                                            <a style="color: blue;text-decoration: underline;" href="#myModalOTPHardware" data-toggle="modal" data-book-id="<%= sIDRevoke%>"><%= rsReportUser[0][j].TOTAL_REVOKED%></a>
                                                            <%
                                                            } else {
                                                            %>
                                                            <%= rsReportUser[0][j].TOTAL_REVOKED%>
                                                            <%
                                                                }
                                                            %>
                                                        </td>
                                                        <td><%= sSUMUser%></td>
                                                    </tr>
                                                <%
                                                                }
                                                            }
                                                        }
                                                    }
                                                %>
                                            </tbody>
                                        </table>
                                        </div>
                                    </div>
                                </div>
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
                                                var vMST_MNS_REMARK = obj[i].ENTERPRISE_ID_REMARK;
                                                var vCMND_HC = obj[i].PERSONAL_ID;
                                                var vCMND_HC_REMARK = obj[i].PERSONAL_ID_REMARK;
                                                content += '<tr>' +
                                                        '<td>' + obj[i].Index + '</td>' +
                                                        '<td>' + obj[i].COMPANY_NAME + '</td>' +
                                                        '<td><a style="color: blue;" data-toggle="tooltipPrefix" data-placement="top" title="'+ vMST_MNS_REMARK +'">'+vMST_MNS+'</a></td>' +
                                                        '<td>' + obj[i].PERSONAL_NAME + '</td>' +
                                                        '<td><a style="color: blue;" data-toggle="tooltipPrefix" data-placement="top" title="'+ vCMND_HC_REMARK +'">'+vCMND_HC+'</a></td>' +
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
                                        <h3 class="modal-title" style="font-size: 18px;"><script>document.write(global_fm_cert_list);</script></h3>
                                    </div>
                                    <div style="width: 29%; float: right;text-align: right;">
                                        <button type="button" onclick="CloseDialog();" class="btn btn-info"><script>document.write(global_fm_button_close);</script></button>
                                    </div>
                                </div>
                                <div class="modal-body">
                                    <form role="formAddOTPHardware" id="formOTPHardware">
                                        <div class="table-responsive">
                                        <table id="tblCertUse" class="table table-striped projects">
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
                                    <script>
                                        $(document).ready(function () {
                                            $('[data-toggle="tooltipPrefix"]').tooltip();
                                        });
                                    </script>
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
            <script src="../style/switchery/dist/switchery.min.js"></script>
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