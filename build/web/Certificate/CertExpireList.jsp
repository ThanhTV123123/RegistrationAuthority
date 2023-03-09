<%-- 
    Document   : CertExpireList
    Created on : Feb 19, 2019, 3:42:02 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<%  response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
    String strAlertAllTimes = "0";
    String anticsrf = "" + Math.random();
    request.getSession().setAttribute("anticsrf", anticsrf);
%>
<!DOCTYPE html>
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
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script language="javascript">
            document.title = reportcertexpire_title_list;
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
                $('[data-toggle="tooltipPrefix"]').tooltip();
                $('.loading-gif').hide();
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
                    localStorage.setItem("LOCAL_TAB_REPORTCERT", null);
                    document.getElementById("idHiddenLoad").value = JS_STR_GRID_SEARCH_RESET;
                    document.getElementById("tempCsrfToken").value = id;
                    var f = document.myform;
                    f.method = "post";
                    f.action = '';
                    f.submit();
                }
            }
            function ExportExcelNew()
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../ExportCSVParam",
                    data: {
                        idParam: "exportcertexpire",
                        CsrfToken: '<%= anticsrf%>'
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
            function popupEdit(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('CertExpireView.jsp', {id:id}, function () {
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
                $(".loading-gif").hide();
                $('#over').remove();
//                localStorage.setItem("LOCAL_PARAM_CERTEXPIRELIST", id);
//                window.location = 'CertExpireView.jsp?id=' + id;
            }
        </script>
        <style>
            .projects th{font-weight: bold;}
            .navbar-right{margin-right: 0;padding-right:10px;}
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
        <%        if ((session.getAttribute("UserID")) != null) {
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
                        document.getElementById("idNameURL").innerHTML = reportcertexpire_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%
                                int status = 1000;
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
                                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                                String SessRoleID = session.getAttribute("RoleID_ID").toString().trim();
                                String SessUserID = session.getAttribute("UserID").toString().trim();
                                String sessTreeArrayBranchID = session.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                try {
                                    CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (session.getAttribute("RefreshCertExpireSess") != null && session.getAttribute("sessFromCreateDateCertExpireList") != null) {
                                        session.setAttribute("RefreshCertExpireSessPaging", "1");
                                        session.setAttribute("SearchSharePagingCertExpire", "0");
                                        statusLoad = 1;
//                                        String DATE_EXPIRE = (String) session.getAttribute("sessDATE_EXPIRECertExpireList");
                                        String FromCreateDate = (String) session.getAttribute("sessFromCreateDateCertExpireList");
                                        String ToCreateDate = (String) session.getAttribute("sessToCreateDateCertExpireList");
                                        String BranchOffice = (String) session.getAttribute("sessBranchOfficeCertExpireList");
                                        String UserCert = (String) session.getAttribute("sessUserCertExpireList");
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeCertExpireList");
                                        session.setAttribute("RefreshCertExpireSess", null);
//                                        session.setAttribute("sessDATE_EXPIRECertExpireList", DATE_EXPIRE);
                                        session.setAttribute("sessFromCreateDateCertExpireList", FromCreateDate);
                                        session.setAttribute("sessToCreateDateCertExpireList", ToCreateDate);
                                        session.setAttribute("sessBranchOfficeCertExpireList", BranchOffice);
                                        session.setAttribute("sessUserCertExpireList", UserCert);
                                        session.setAttribute("AlertAllTimeCertExpireList", strAlertAllTimes);
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                            BranchOffice = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(UserCert)) {
                                            UserCert = "";
                                        }
                                        if("1".equals(strAlertAllTimes)) {
                                            FromCreateDate = "";
                                            ToCreateDate = "";
                                        }
                                        int ss = 0;
                                        if ((session.getAttribute("CountListCertExpire")) == null) {
                                            ss = db.S_BO_CERTIFICATION_WARNING_EXPIRED_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(BranchOffice), UserCert, sessTreeArrayBranchID);
                                            session.setAttribute("CountListCertExpire", String.valueOf(ss));
                                        } else {
                                            String sCount = (String) session.getAttribute("CountListCertExpire");
                                            ss = Integer.parseInt(sCount);
                                            session.setAttribute("CountListCertExpire", String.valueOf(ss));
                                        }
                                        if (session.getAttribute("SearchIPageNoPagingCertExpire") != null) {
                                            String sPage = (String) session.getAttribute("SearchIPageNoPagingCertExpire");
                                            iPagNo = Integer.parseInt(sPage);
                                        }
                                        if (session.getAttribute("SearchISwRwsPagingCertExpire") != null) {
                                            String sSumPage = (String) session.getAttribute("SearchISwRwsPagingCertExpire");
                                            iSwRws = Integer.parseInt(sSumPage);
                                        }
                                        if (session.getAttribute("RefreshCertSessNumberPaging") != null) {
                                            String sNoPage = (String) session.getAttribute("RefreshCertSessNumberPaging");
                                            iPaNoSS = Integer.parseInt(sNoPage);
                                        }
                                        session.setAttribute("SearchIPageNoPagingCertExpire", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingCertExpire", String.valueOf(iSwRws));
                                        if (ss > 0) {
                                            db.S_BO_CERTIFICATION_WARNING_EXPIRED_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(BranchOffice), UserCert, sessLanguageGlobal, rsPgin, iPagNo,
                                                iSwRws, sessTreeArrayBranchID);
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
                                        session.setAttribute("RefreshCertExpireSessPaging", null);
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("SearchShareStoreCertExpire", null);
                                            session.setAttribute("SearchIPageNoPagingCertExpire", null);
                                            session.setAttribute("SearchISwRwsPagingCertExpire", null);
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
//                                    String DATE_EXPIRE = request.getParameter("expireDate");
                                    String BranchOffice = request.getParameter("idBranchOffice");
                                    String UserCert = request.getParameter("USER");
                                    Boolean nameCheck = Boolean.valueOf(request.getParameter("nameCheck") != null);
                                    if (nameCheck == false) {
                                        FromCreateDate = "";
                                        ToCreateDate = "";
                                        strAlertAllTimes = "1";
                                    }
                                    if ("1".equals(hasPaging)) {
                                        session.setAttribute("SearchSharePagingCertExpire", "0");
//                                        DATE_EXPIRE = (String) session.getAttribute("sessDATE_EXPIRECertExpireList");
                                        FromCreateDate = (String) session.getAttribute("sessFromCreateDateCertExpireList");
                                        ToCreateDate = (String) session.getAttribute("sessToCreateDateCertExpireList");
                                        BranchOffice = (String) session.getAttribute("sessBranchOfficeCertExpireList");
                                        UserCert = (String) session.getAttribute("sessUserCertExpireList");
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeCertExpireList");
                                        session.setAttribute("SessParamOnPagingCertList", null);
                                    } else {
                                        session.setAttribute("SearchSharePagingCertExpire", "1");
                                        session.setAttribute("CountListCertExpire", null);
                                    }
                                    session.setAttribute("sessFromCreateDateCertExpireList", FromCreateDate);
                                    session.setAttribute("sessToCreateDateCertExpireList", ToCreateDate);
//                                    session.setAttribute("sessDATE_EXPIRECertExpireList", DATE_EXPIRE);
                                    session.setAttribute("sessBranchOfficeCertExpireList", BranchOffice);
                                    session.setAttribute("sessUserCertExpireList", UserCert);
                                    session.setAttribute("AlertAllTimeCertExpireList", strAlertAllTimes);
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                        BranchOffice = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(UserCert)) {
                                        UserCert = "";
                                    }
                                    int ss = 0;
                                    if ((session.getAttribute("CountListCertExpire")) == null) {
                                        ss = db.S_BO_CERTIFICATION_WARNING_EXPIRED_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(BranchOffice), UserCert, sessTreeArrayBranchID);
                                        session.setAttribute("CountListCertExpire", String.valueOf(ss));
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListCertExpire");
                                        ss = Integer.parseInt(sCount);
                                        session.setAttribute("CountListCertExpire", String.valueOf(ss));
                                    }
                                    iTotRslts = ss;
                                    if (iTotRslts > 0) {
                                        db.S_BO_CERTIFICATION_WARNING_EXPIRED_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(BranchOffice), UserCert, sessLanguageGlobal, rsPgin, iPagNo,
                                            iSwRws, sessTreeArrayBranchID);
                                        session.setAttribute("SearchIPageNoPagingCertExpire", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingCertExpire", String.valueOf(iSwRws));
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
                                    session.setAttribute("RefreshCertExpireSessPaging", null);
                                    session.setAttribute("SearchShareStoreCertExpire", null);
                                    session.setAttribute("SearchIPageNoPagingCertExpire", null);
                                    session.setAttribute("SearchISwRwsPagingCertExpire", null);
//                                    session.setAttribute("sessDATE_EXPIRECertExpireList", null);
                                    session.setAttribute("sessBranchOfficeCertExpireList", null);
                                    session.setAttribute("sessUserCertExpireList", null);
                                    session.setAttribute("sessFromCreateDateCertExpireList", null);
                                    session.setAttribute("sessToCreateDateCertExpireList", null);
                                    session.setAttribute("AlertAllTimeCertExpireList", null);
                                }
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="myform" method="post" class="form-horizontal">
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
<!--                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-7" style="color: #000000; font-weight: bold;text-align: left;">
                                                        <script>document.write(global_fm_expire_date);</script> <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                    </label>
                                                    <div class="col-sm-5" style="padding-right: 0px;">
                                                        <input type="text" name="expireDate" id="expireDate" maxlength="4" class="form-control123"
                                                            value="<= session.getAttribute("sessDATE_EXPIRECertExpireList") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessDATE_EXPIRECertExpireList").toString()) : "30"%>"/>
                                                    </div>
                                                </div>
                                            </div>-->
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_FromDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo1" name="FromCreateDate" <%= session.getAttribute("AlertAllTimeCertExpireList") != null && "1".equals(session.getAttribute("AlertAllTimeCertExpireList").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessFromCreateDateCertExpireList") != null && !"1".equals(session.getAttribute("AlertAllTimeCertExpireList").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromCreateDateCertExpireList").toString()) : com.ConvertMonthSub(30)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_ToDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo2" name="ToCreateDate" <%= session.getAttribute("AlertAllTimeCertExpireList") != null && "1".equals(session.getAttribute("AlertAllTimeCertExpireList").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessToCreateDateCertExpireList") != null && !"1".equals(session.getAttribute("AlertAllTimeCertExpireList").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToCreateDateCertExpireList").toString()) : com.ConvertMonthSub(0)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_check_date);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;padding-top: 7px; text-align: left;">
                                                            <label class="switch" for="idCheck">
                                                                <input type="checkbox" name="nameCheck" id="idCheck" onchange="checkboxChange();" <%= session.getAttribute("AlertAllTimeCertExpireList") != null && "1".equals(session.getAttribute("AlertAllTimeCertExpireList").toString()) ? "" : "checked" %>/>
                                                                <div class="slider round"></div>
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_Branch);</script></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="idBranchOffice" id="idBranchOffice" class="form-control123" onchange="LOAD_BACKOFFICE_USER(this.value, '<%= anticsrf%>');">
                                                            <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessBranchOfficeCertExpireList") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessBranchOfficeCertExpireList").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            <%
//                                                                BRANCH[][] rsBranch = new BRANCH[1][];
//                                                                db.S_BO_BRANCH_COMBOBOX(sessLanguageGlobal, rsBranch);
                                                                BRANCH[][] rsBranch = (BRANCH[][]) session.getAttribute("sessTreeBranchSystemAgency");
                                                                String sFristBranch_ID = Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL;
                                                                if (rsBranch[0].length > 0) {
                                                                    for (BRANCH temp1 : rsBranch[0]) {
                                                                        if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT))
                                                                        {
                                                            %>
                                                            <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessBranchOfficeCertExpireList") != null
                                                                && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeCertExpireList").toString().trim())
                                                                ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
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
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_user_create);</script></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="USER" id="USER" class="form-control123">
                                                            <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessUserCertExpireList") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessUserCertExpireList").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            <%
                                                                if(session.getAttribute("sessBranchOfficeCertExpireList") != null)
                                                                {
                                                                    sFristBranch_ID = session.getAttribute("sessBranchOfficeCertExpireList").toString().trim();
                                                                }
                                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(sFristBranch_ID)) {
                                                                    sFristBranch_ID = "";
                                                                }
                                                                if(!"".equals(sFristBranch_ID)) {
                                                                    BACKOFFICE_USER[][] rssUser;
                                                                    if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                                        rssUser = new BACKOFFICE_USER[1][];
                                                                        db.S_BO_GET_USER_BRANCH_ALL(sFristBranch_ID, rssUser);
                                                                        if (rssUser[0].length > 0) {
                                                                            for (int i = 0; i < rssUser[0].length; i++) {
                                                            %>
                                                            <option value="<%=String.valueOf(rssUser[0][i].ID)%>" <%= session.getAttribute("sessUserCertExpireList") != null && String.valueOf(rssUser[0][i].ID).equals(session.getAttribute("sessUserCertExpireList").toString()) ? "selected" : ""%>><%=rssUser[0][i].FULL_NAME + " (" + rssUser[0][i].USERNAME + ")" %></option>
                                                            <%
                                                                            }
                                                                        }
                                                                    } else {
                                                                        if(SessUserAgentID.equals(sFristBranch_ID)) {
                                                                            rssUser = new BACKOFFICE_USER[1][];
                                                                            db.S_BO_USER_DETAIL(SessUserID, sessLanguageGlobal, rssUser);
                                                                            if(rssUser[0].length > 0) {
                                                                    %>
                                                                    <option value="<%=String.valueOf(rssUser[0][0].ID)%>" <%= session.getAttribute("sessUserCertExpireList") != null && String.valueOf(rssUser[0][0].ID).equals(session.getAttribute("sessUserCertExpireList").toString()) ? "selected" : ""%>><%=rssUser[0][0].FULL_NAME + " (" + rssUser[0][0].USERNAME + ")" %></option>
                                                                    <%
                                                                                }
                                                                        } else {
                                                                            rssUser = new BACKOFFICE_USER[1][];
                                                                                db.S_BO_GET_USER_BRANCH_ALL(sFristBranch_ID, rssUser);
                                                                                if (rssUser[0].length > 0) {
                                                                                    for (int i = 0; i < rssUser[0].length; i++) {
                                                            %>
                                                            <option value="<%=String.valueOf(rssUser[0][i].ID)%>" <%= session.getAttribute("sessUserCertExpireList") != null && String.valueOf(rssUser[0][i].ID).equals(session.getAttribute("sessUserCertExpireList").toString()) ? "selected" : ""%>><%=rssUser[0][i].FULL_NAME + " (" + rssUser[0][i].USERNAME + ")" %></option>
                                                            <%
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <script>
                                                function LOAD_BACKOFFICE_USER(objAgency, idCSRF)
                                                {
                                                    if(objAgency === JS_STR_GRID_COMBOBOX_VALUE_ALL)
                                                    {
                                                        var cbxUSER = document.getElementById("USER");
                                                        removeOptions(cbxUSER);
                                                        cbxUSER.options[cbxUSER.options.length] = new Option(global_fm_combox_all, JS_STR_GRID_COMBOBOX_VALUE_ALL);
                                                    }
                                                    else
                                                    {
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../JSONCommon",
                                                            data: {
                                                                idParam: 'loadadminuser_ofagency',
                                                                BRANCH_ID: objAgency,
                                                                CsrfToken: idCSRF
                                                            },
                                                            cache: false,
                                                            success: function (html)
                                                            {
                                                                if (html.length > 0)
                                                                {
                                                                    var cbxUSER = document.getElementById("USER");
                                                                    removeOptions(cbxUSER);
                                                                    var obj = JSON.parse(html);
                                                                    if (obj[0].Code === "0")
                                                                    {
                                                                        cbxUSER.options[cbxUSER.options.length] = new Option(global_fm_combox_all, JS_STR_GRID_COMBOBOX_VALUE_ALL);
                                                                        for (var i = 0; i < obj.length; i++) {
                                                                            cbxUSER.options[cbxUSER.options.length] = new Option(obj[i].FULL_NAME + " (" + obj[i].USERNAME + ")", obj[i].ID);
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
                                                                        cbxUSER.options[cbxUSER.options.length] = new Option(global_fm_combox_all, JS_STR_GRID_COMBOBOX_VALUE_ALL);
                                                                    }
                                                                    else {
                                                                        funErrorAlert(global_errorsql);
                                                                    }
                                                                }
                                                            }
                                                        });
                                                        return false;
                                                    }
                                                }
                                            </script>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <div class="col-sm-6" style="padding-right: 0px;text-align: left;">
                                                        <button name="btnSearch" type="button" onclick="searchForm('<%=anticsrf%>');" class="btn btn-info"><script>document.write(global_fm_button_search);</script></button>
                                                    </div>
                                                    <div class="col-sm-6" style="padding-right: 0px;">
                                                        
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
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(certprofile_title_table);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <script>document.write(global_label_grid_sum);</script><%= strMess%>
                                                &nbsp;&nbsp;
                                                <button type="button" class="btn btn-info" onClick="ExportExcelNew();"><script>document.write(global_fm_button_export_csv);</script></button>
                                            </li>
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
                                                <th><script>document.write(global_fm_action);</script></th>
                                                <th><script>document.write(global_fm_STT);</script></th>
                                                <%
                                                    if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                                %>
                                                <th><script>document.write(token_fm_agent);</script></th>
                                                <%
                                                    }
                                                %>
                                                <%
                                                    if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)
                                                        || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                        || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                %>
                                                <th><script>document.write(global_fm_user_create);</script></th>
                                                <%
                                                    }
                                                %>
                                                <th style="width: 120px;"><script>document.write(global_fm_grid_company);</script></th>
                                                <th style="width: 100px;"><script>document.write(global_fm_enterprise_id);</script></th>
                                                <th style="width: 120px;"><script>document.write(global_fm_grid_personal);</script></th>
                                                <th style="width: 100px;"><script>document.write(global_fm_personal_id);</script></th>
                                                <th><script>document.write(global_fm_phone_contact);</script></th>
                                                <!--<th><script>document.write(global_fm_email_contact);</script></th>-->
                                                <th><script>document.write(global_fm_valid);</script></th>
                                                <th><script>document.write(global_fm_Expire);</script></th>
                                                </thead>
                                                <tbody>
                                                <%
                                                    int j = 1;
                                                    if (iPaNoSS > 1) {
                                                        j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                    }
                                                    session.setAttribute("NumPageSessListCertProfile", String.valueOf(iPaNoSS));
                                                    if (rsPgin[0].length > 0) {
                                                        for (CERTIFICATION temp1 : rsPgin[0]) {
//                                                            String sSMTOrMSN = EscapeUtils.CheckTextNull(temp1.TAX_CODE);
//                                                            if("".equals(sSMTOrMSN)) {
//                                                                sSMTOrMSN = EscapeUtils.CheckTextNull(temp1.BUDGET_CODE);
//                                                            }
//                                                            if("".equals(sSMTOrMSN)) {
//                                                                sSMTOrMSN = EscapeUtils.CheckTextNull(temp1.DECISION);
//                                                            }
//                                                            String sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_ID);
//                                                            if("".equals(sCMNDOrHC)) {
//                                                                sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_EID);
//                                                            }
//                                                            if("".equals(sCMNDOrHC)) {
//                                                                sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.PASSPORT);
//                                                            }
                                                            String strID = String.valueOf(temp1.ID);
                                                %>
                                                <tr>
                                                    <td>
                                                        <a style="cursor: pointer;" onclick="popupEdit('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_detail);</script> </a>
                                                    </td>
                                                    <td><%= com.convertMoney(j)%></td>
                                                    <%
                                                        if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                                    %>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.BRANCH_DESC)%></td>
                                                    <%
                                                        }
                                                    %>
                                                    <%
                                                        if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)
                                                            || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                            || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                    %>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_BY)%></td>
                                                    <%
                                                        }
                                                    %>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.COMPANY_NAME)%></td>
                                                    <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= temp1.ENTERPRISE_ID_REMARK%>"><%= temp1.ENTERPRISE_ID%></a></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME)%></td>
                                                    <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= temp1.PERSONAL_ID_REMARK%>"><%= temp1.PERSONAL_ID%></a></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.PHONE_CONTRACT) %></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.EFFECTIVE_DT) %></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.EXPIRATION_DT) %></td>
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
                                                                    cPge = ((int) (Math.ceil((double) iEnRsNo / (iTotSrhRcrds * iSwRws))));
                                                                    int prePageNo = (cPge * iTotSrhRcrds) - ((iTotSrhRcrds - 1) + iTotSrhRcrds);
                                                                    if ((cPge * iTotSrhRcrds) - (iTotSrhRcrds) > 0) {
                                                            %>
                                                            <a href="?iPagNo=<%=prePageNo%>&cPagNo=<%=prePageNo%>"><< <script>document.write(global_paging_Before);</script></a>
                                                            &nbsp;
                                                            <%
                                                                }
                                                                for (i = ((cPge * iTotSrhRcrds) - (iTotSrhRcrds - 1)); i <= (cPge * iTotSrhRcrds); i++) {
                                                                    if (i == ((iPagNo / iSwRws) + 1)) {
                                                            %>
                                                            <a href="?iPagNo=<%=i%>" style="cursor:pointer;color:red;"><b><%=i%></b></a>
                                                                    <%
                                                                    } else if (i <= iTotPags) {
                                                                    %>
                                                            &nbsp;<a href="?iPagNo=<%=i%>"><%=i%></a>
                                                            <%
                                                                    }
                                                                }
                                                                if (iTotPags > iTotSrhRcrds && i <= iTotPags) {
                                                            %>
                                                            &nbsp;
                                                            <a href="?iPagNo=<%=i%>&cPagNo=<%=i%>">>> <script>document.write(global_paging_last);</script></a>
                                                            <%
                                                                }
                                                            } else {
                                                            %>
                                                            &nbsp;<a style="cursor: pointer;">1</a>
                                                            <%
                                                                }
                                                            %>
                                                            <!--&nbsp;&nbsp;<b> <script>document.write(global_fm_paging_total);</script><=com.convertMoney(iTotRslts)%></b>-->
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
                                %>
                                <%
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
            <script src="../style/bootstrap.min.js"></script>
            <script src="../style/custom.min.js"></script>
            <script src="../js/moment.min.js"></script>
            <!--<script src="../js/daterangepicker.js"></script>-->
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