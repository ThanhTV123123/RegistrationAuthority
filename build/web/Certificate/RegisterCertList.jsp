<%-- 
    Document   : RegisterCertList
    Created on : Jun 27, 2018, 5:03:01 PM
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
            document.title = regiscert_title_list;
            $(document).ready(function () {
                localStorage.setItem("LOCAL_PARAM_REGISTERCERTLIST", null);
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
                $('#myModalRegisterPrint').modal({
                    backdrop: 'static',
                    keyboard: true,
                    show: false
                });
                $('#myModalRegisterPrint').on('hidden.bs.modal', function () {
                    $('#myModalRegisterPrint').modal({
                        backdrop: 'static',
                        keyboard: true,
                        show: false
                    });
                    window.location = 'RegisterCertList.jsp';
                });
            });
            $(document).ready(function () {
                if(localStorage.getItem("EDIT_LOCAL_PARAM_REGISTERCERTLIST") !== null && localStorage.getItem("EDIT_LOCAL_PARAM_REGISTERCERTLIST") !== "null")
                {
                    localStorage.setItem("EDIT_LOCAL_PARAM_REGISTERCERTLIST", null);
                } else {
                    localStorage.setItem("LOCAL_PARAM_REGISTERCERTLIST", null);
                }
            });
            function popupaddpackage(id)
            {
                localStorage.setItem("LOCAL_PARAM_REGISTERCERTLIST", id);
                window.location = 'RegisterCertView.jsp?id=' + id;
            }
            function addForm()
            {
                window.location = 'RegisterCertUnAssign.jsp';
            }
            function addFormSoft()
            {
                window.location = 'RegisterCertSoft.jsp';
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
            var checkForSpecialChar = function(string){
             for(i = 0; i < specialChars.length;i++){
               if(string.indexOf(specialChars[i]) > -1){
                   return true;
                }
             }
             return false;
            };
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
        </style>
        <style>
            .x_content {
                margin-top: 0px;
                padding: 0 3px 6px 3px;
            }
            .x_panel{
                margin-bottom: 10px;
                padding: 14px 5px 0px 5px;
            }
            .col-sm-3{
                width: 20%;
            }
            .form-control123 {
                padding: 4px 6px;
                height: 32px;
                font-size: 14px;
            }
            .table{
                margin-bottom: 0px;
            }
            .paging_table a{
                padding: 3px 6px;
            }
            input::placeholder {
                font-size: 12px;
                font-style: italic;
            }
        </style>
        <style>
            .form-control123[readonly]{background-color:#eee;opacity:1}
            .form-control123[disabled]{background-color:#eee;opacity:1}
            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
            .table > thead > tr > th{border-bottom: none;}
            .btn{margin-bottom: 0px;}
        </style>
    </head>
    <body class="nav-md">
        <%
            if (session.getAttribute("sUserID") != null) {
                String sMaxLengthFile = cogCommon.GetPropertybyCode(Definitions.CONFIG_JACK_RABBIT_MAX_LENGTH_FILE).trim();
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                String sDN_Country = "";
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Cert");
                GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                String boCheckBackUpKey = "0";
                if (sessGeneralPolicy[0].length > 0) {
                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                    {
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_CERTIFICATE_ATTRIBUTE_COUNTRY))
                        {
                            sDN_Country = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                        }
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DEFAULT_PRIVATE_KEY_ENABLED))
                        {
                            boCheckBackUpKey = rsPolicy1.VALUE;
                        }
                    }
                }
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
                        document.getElementById("idNameURL").innerHTML = regiscert_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%                            int status = 1000;
                                int statusLoad = 0;
                                int j = 1;
                                iSwRws = 10;
                                iTotSrhRcrds = 5;
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
                                int[] pIa = new int[1];
                                int[] pIb = new int[1];
                                try {
                                    TOKEN[][] rsPgin = new TOKEN[1][];
                                    if (session.getAttribute("RefreshRegisTokenSess") != null && session.getAttribute("sessFromCreateDateRegisToken") != null
                                            && session.getAttribute("sessToCreateDateRegisToken") != null) {
                                        session.setAttribute("RefreshRegisTokenSessPaging", "1");
                                        session.setAttribute("SearchSharePagingRegisToken", "0");
                                        statusLoad = 1;
                                        String ToCreateDate = (String) session.getAttribute("sessToCreateDateRegisToken");
                                        String FromCreateDate = (String) session.getAttribute("sessFromCreateDateRegisToken");
                                        String TOKEN_ID = (String) session.getAttribute("sessTOKEN_IDRegisToken");
                                        String BranchOffice = (String) session.getAttribute("sessBranchOfficeRegisToken");
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSAgreeList");
                                        session.setAttribute("RefreshRegisTokenSess", null);
                                        session.setAttribute("sessFromCreateDateRegisToken", FromCreateDate);
                                        session.setAttribute("sessToCreateDateRegisToken", ToCreateDate);
                                        session.setAttribute("sessTOKEN_IDRegisToken", TOKEN_ID);
                                        session.setAttribute("sessBranchOfficeRegisToken", BranchOffice);
                                        session.setAttribute("AlertAllTimeSAgreeList", strAlertAllTimes);
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                            BranchOffice = "";
                                        }
                                        if (!Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                            BranchOffice = SessUserAgentID;
                                        }
                                        if("1".equals(strAlertAllTimes))
                                        {
                                            FromCreateDate = "";
                                            ToCreateDate = "";
                                        }
                                        int ss = 0;
                                        if ((session.getAttribute("CountListRegisToken")) == null) {
                                            ss = db.S_BO_TOKEN_ISSUED_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(TOKEN_ID),
                                                EscapeUtils.escapeHtmlSearch(BranchOffice), pIa, pIb);
                                            session.setAttribute("pIaRegisToken", String.valueOf(pIa[0]));
                                            session.setAttribute("pIbRegisToken", String.valueOf(pIb[0]));
                                            if (session.getAttribute("pIaRegisToken") != null) {
                                                pIa[0] = Integer.parseInt((String) session.getAttribute("pIaRegisToken"));
                                            }
                                            if (session.getAttribute("pIbRegisToken") != null) {
                                                pIb[0] = Integer.parseInt((String) session.getAttribute("pIbRegisToken"));
                                            }
                                            session.setAttribute("CountListRegisToken", String.valueOf(ss));
                                        } else {
                                            String sCount = (String) session.getAttribute("CountListRegisToken");
                                            ss = Integer.parseInt(sCount);
                                            session.setAttribute("CountListRegisToken", String.valueOf(ss));
                                        }
                                        if (session.getAttribute("SearchIPageNoPagingRegisToken") != null) {
                                            String sPage = (String) session.getAttribute("SearchIPageNoPagingRegisToken");
                                            iPagNo = Integer.parseInt(sPage);
                                        }
                                        if (session.getAttribute("SearchISwRwsPagingRegisToken") != null) {
                                            String sSumPage = (String) session.getAttribute("SearchISwRwsPagingRegisToken");
                                            iSwRws = Integer.parseInt(sSumPage);
                                        }
                                        if (session.getAttribute("RefreshCertSessNumberPaging") != null) {
                                            String sNoPage = (String) session.getAttribute("RefreshCertSessNumberPaging");
                                            iPaNoSS = Integer.parseInt(sNoPage);
                                        }
                                        session.setAttribute("SearchIPageNoPagingRegisToken", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingRegisToken", String.valueOf(iSwRws));
                                        if (session.getAttribute("pIaRegisToken") != null) {
                                            pIa[0] = Integer.parseInt((String) session.getAttribute("pIaRegisToken"));
                                        }
                                        if (session.getAttribute("pIbRegisToken") != null) {
                                            pIb[0] = Integer.parseInt((String) session.getAttribute("pIbRegisToken"));
                                        }
                                        if (ss > 0) {
                                            db.S_BO_TOKEN_ISSUED_LIST(String.valueOf(pIa[0]), String.valueOf(pIb[0]),
                                                EscapeUtils.escapeHtmlSearch(TOKEN_ID), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                                sessLanguageGlobal, rsPgin, iPagNo, iSwRws);
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
                                        session.setAttribute("RefreshRegisTokenSessPaging", null);
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("pIaRegisToken", null);
                                            session.setAttribute("pIbRegisToken", null);
                                            session.setAttribute("SearchShareStoreRegisToken", null);
                                            session.setAttribute("SearchIPageNoPagingRegisToken", null);
                                            session.setAttribute("SearchISwRwsPagingRegisToken", null);
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
                                    String TOKEN_ID = request.getParameter("TOKEN_ID");
                                    String BranchOffice = request.getParameter("BranchOffice");
                                    Boolean nameCheck = Boolean.valueOf(request.getParameter("nameCheck") != null);
                                    if (nameCheck == false) {
                                        FromCreateDate = "";
                                        ToCreateDate = "";
                                        strAlertAllTimes = "1";
                                    }
                                    if ("1".equals(hasPaging)) {
                                        session.setAttribute("SearchSharePagingRegisToken", "0");
                                        ToCreateDate = (String) session.getAttribute("sessToCreateDateRegisToken");
                                        FromCreateDate = (String) session.getAttribute("sessFromCreateDateRegisToken");
                                        TOKEN_ID = (String) session.getAttribute("sessTOKEN_IDRegisToken");
                                        BranchOffice = (String) session.getAttribute("sessBranchOfficeRegisToken");
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSAgreeList");
                                        pIa[0] = Integer.parseInt((String) session.getAttribute("pIaRegisToken"));
                                        pIb[0] = Integer.parseInt((String) session.getAttribute("pIbRegisToken"));
                                        session.setAttribute("SessParamOnPagingCertList", null);
                                    } else {
                                        session.setAttribute("SearchSharePagingRegisToken", "1");
                                        session.setAttribute("CountListRegisToken", null);
                                    }
                                    session.setAttribute("sessFromCreateDateRegisToken", FromCreateDate);
                                    session.setAttribute("sessToCreateDateRegisToken", ToCreateDate);
                                    session.setAttribute("sessTOKEN_IDRegisToken", TOKEN_ID);
                                    session.setAttribute("sessBranchOfficeRegisToken", BranchOffice);
                                    session.setAttribute("AlertAllTimeSAgreeList", strAlertAllTimes);
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                        BranchOffice = "";
                                    }
                                    if (!Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                        BranchOffice = SessUserAgentID;
                                    }
                                    int ss = 0;
                                    if ((session.getAttribute("CountListRegisToken")) == null) {
                                        ss = db.S_BO_TOKEN_ISSUED_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(TOKEN_ID),
                                            EscapeUtils.escapeHtmlSearch(BranchOffice), pIa, pIb);
                                        session.setAttribute("pIaRegisToken", String.valueOf(pIa[0]));
                                        session.setAttribute("pIbRegisToken", String.valueOf(pIb[0]));
                                        if (session.getAttribute("pIaRegisToken") != null) {
                                            pIa[0] = Integer.parseInt((String) session.getAttribute("pIaRegisToken"));
                                        }
                                        if (session.getAttribute("pIbRegisToken") != null) {
                                            pIb[0] = Integer.parseInt((String) session.getAttribute("pIbRegisToken"));
                                        }
                                        session.setAttribute("CountListRegisToken", String.valueOf(ss));
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListRegisToken");
                                        ss = Integer.parseInt(sCount);
                                        session.setAttribute("CountListRegisToken", String.valueOf(ss));
                                    }
                                    iTotRslts = ss;
                                    if (iTotRslts > 0) {
                                        db.S_BO_TOKEN_ISSUED_LIST(String.valueOf(pIa[0]), String.valueOf(pIb[0]),
                                            EscapeUtils.escapeHtmlSearch(TOKEN_ID), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                            sessLanguageGlobal, rsPgin, iPagNo, iSwRws);
                                        session.setAttribute("SearchIPageNoPagingRegisToken", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingRegisToken", String.valueOf(iSwRws));
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
                                    session.setAttribute("RefreshRegisTokenSessPaging", null);
                                    session.setAttribute("pIaRegisToken", null);
                                    session.setAttribute("pIbRegisToken", null);
                                    session.setAttribute("SearchShareStoreRegisToken", null);
                                    session.setAttribute("SearchIPageNoPagingRegisToken", null);
                                    session.setAttribute("SearchISwRwsPagingRegisToken", null);
                                    session.setAttribute("sessFromCreateDateRegisToken", null);
                                    session.setAttribute("sessToCreateDateRegisToken", null);
                                    session.setAttribute("sessTOKEN_IDRegisToken", null);
                                    session.setAttribute("sessBranchOfficeRegisToken", null);
                                    session.setAttribute("AlertAllTimeSAgreeList", null);
                                }
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
<!--                                    <div class="x_title">
                                        <h2><i class="fa fa-search"></i> <script>document.write(regiscert_title_search);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <button type="button" class="btn btn-info" onClick="searchForm('<=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                <
                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_ISSUE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                %>
                                                <button type="button" class="btn btn-info" onClick="addForm();"><script>document.write(global_fm_button_regis);</script></button>
                                                <button type="button" class="btn btn-info" onClick="addFormSoft();"><script>document.write(global_fm_button_regis_soft);</script></button>
                                                <
                                                    }
                                                %>
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
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <div class="col-sm-4" style="padding-right: 0px;text-align: right;padding-top: 7px;">
                                                            <label class="switch" for="idCheck">
                                                                <input type="checkbox" name="nameCheck" id="idCheck" onchange="checkboxChange();" <%= session.getAttribute("AlertAllTimeSAgreeList") != null && "1".equals(session.getAttribute("AlertAllTimeSAgreeList").toString()) ? "" : "checked" %>/>
                                                                <div class="slider round"></div>
                                                            </label>
                                                        </div>
                                                        <label class="control-label col-sm-8" style="color: #000000; font-weight: bold;text-align: left;"><script>document.write(global_fm_check_date);</script></label>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_FromDate);</script></label>
                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                            <input type="Text" id="demo1" name="FromCreateDate" <%= session.getAttribute("AlertAllTimeSAgreeList") != null && "1".equals(session.getAttribute("AlertAllTimeSAgreeList").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessFromCreateDateRegisToken") != null && !"1".equals(session.getAttribute("AlertAllTimeSAgreeList").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromCreateDateRegisToken").toString()) : com.ConvertMonthSub(30)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_ToDate);</script></label>
                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                            <input type="Text" id="demo2" name="ToCreateDate" <%= session.getAttribute("AlertAllTimeSAgreeList") != null && "1".equals(session.getAttribute("AlertAllTimeSAgreeList").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessToCreateDateRegisToken") != null && !"1".equals(session.getAttribute("AlertAllTimeSAgreeList").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToCreateDateRegisToken").toString()) : com.ConvertMonthSub(0)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(token_fm_tokenid);</script></label>
                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                            <input type="text" id="TOKEN_ID" name="TOKEN_ID" maxlength="45" value="<%= session.getAttribute("sessTOKEN_IDRegisToken") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessTOKEN_IDRegisToken").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <%
                                                    if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                                %>
                                                <div class="col-sm-4" style="padding-left: 0;margin: 0 0 0 0px;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(token_fm_tokenid);</script></label>
                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                            <select name="BranchOffice" id="idBranchOffice" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessBranchOfficeRegisToken") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessBranchOfficeRegisToken").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    BRANCH[][] rsBranch = new BRANCH[1][];
                                                                    db.S_BO_BRANCH_COMBOBOX(sessLanguageGlobal, rsBranch);
                                                                    if (rsBranch[0].length > 0) {
                                                                        for (BRANCH temp1 : rsBranch[0]) {
                                                                            if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT))
                                                                            {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessBranchOfficeRegisToken") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeRegisToken").toString()) ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                                <%
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <%
                                                    } else {
                                                %>
                                                <input type="text" style="display: none;" name="BranchOffice" id="idBranchOffice" value="<%= SessUserAgentID%>" class="form-control123">
                                                <%
                                                    }
                                                %>
                                                <div class="col-sm-4" style="padding-left: 0;text-align: right;">
                                                    <div class="form-group">
                                                        <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                        <%
                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_ISSUE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                        %>
                                                        <button type="button" class="btn btn-info" onClick="addForm();"><script>document.write(global_fm_button_regis);</script></button>
                                                        <button type="button" class="btn btn-info" onClick="addFormSoft();"><script>document.write(global_fm_button_regis_soft);</script></button>
                                                        <%
                                                            }
                                                        %>
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
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 5px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(regiscert_title_table);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;"><script>document.write(global_label_grid_sum);</script><%= strMess%></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <input type="hidden" name="iPagNo" value="<%=iPagNo%>">
                                        <input type="hidden" name="cPagNo" value="<%=cPagNo%>">
                                        <input type="hidden" name="iSwRws" value="<%=iSwRws%>">
                                        <!--<table id="idTableList" class="table table-striped projects">-->
                                        <table id="idTableList" class="table table-bordered table-striped projects">
                                            <thead>
                                            <th><script>document.write(global_fm_STT);</script></th>
                                            <th><script>document.write(token_fm_tokenid);</script></th>
                                            <th><script>document.write(token_fm_version);</script></th>
                                            <th><script>document.write(global_fm_Status);</script></th>
                                            <th><script>document.write(token_fm_agent);</script></th>
                                            <th><script>document.write(global_fm_date_create);</script></th>
                                            <%
                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_ISSUE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                            %>
                                            <th><script>document.write(global_fm_action);</script></th>
                                            <%
                                                }
                                            %>
                                            </thead>
                                            <tbody>
                                                <%
                                                    if (iPaNoSS > 1) {
                                                        j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                    }
                                                    session.setAttribute("RefreshRegisTokenSessNumberPaging", String.valueOf(iPaNoSS));
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
                                                    <%
                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_ISSUE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                    %>
                                                    <!--<td><a style="cursor: pointer;" onclick="popupaddpackage('<=URLEncoder.encode(seEncript.encrypt(strID))%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_regis);</script> </a></td>-->
                                                    <td><a style="cursor: pointer;" onclick="popupEdit('<%=strID %>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_regis);</script> </a></td>
                                                    <%
                                                        }
                                                    %>
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
                                <!-- Load Combobox Info Script -->
                                <script>
                                    function LOAD_BACKOFFICE_USER(objAgency, idCSRF)
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
                                                    var cbxUSER = document.getElementById("dTokenUSER");
                                                    removeOptions(cbxUSER);
                                                    var obj = JSON.parse(html);
                                                    if (obj[0].Code === "0")
                                                    {
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
                                                        cbxUSER.options[cbxUSER.options.length] = new Option("---", "");
                                                    }
                                                    else {
                                                        funErrorAlert(global_errorsql);
                                                    }
                                                }
                                            }
                                        });
                                        return false;
                                    }
                                    function LOAD_CERTIFICATION_DURATION(objProfile, idCSRF)
                                    {
                                        LOAD_CERTIFICATION_PROFILE(objProfile);
                                    }
                                    function LOAD_CERTIFICATION_PROFILE(vCertDurationOrProfileID)
                                    {
                                        $("#idHiddenCerDurationOrProfileID").val(vCertDurationOrProfileID);
                                        $.ajax({
                                            type: "post",
                                            url: "../JSONCommon",
                                            data: {
                                                idParam: 'loadcert_profile_frist',
                                                vCertDurationOrProfileID: vCertDurationOrProfileID
                                            },
                                            cache: false,
                                            success: function (html)
                                            {
                                                if (html.length > 0)
                                                {
                                                    var obj = JSON.parse(html);
                                                    if (obj[0].Code === "0")
                                                    {
                                                        $("#dTokenFEE_AMOUNT").val(obj[0].AMOUNT);
                                                        $("#dTokenDURATION_FREE").val(obj[0].DURATION_FREE);
                                                        LoadFormSubjectDN(vCertDurationOrProfileID, "idViewCertInfoToken");
                                                        if(obj[0].DURATION_FREE === "0" || obj[0].DURATION_FREE === 0)
                                                        {
                                                            $("#dTokenDURATION_FREE").val('');
//                                                            $("#idViewDURATION_FREE").css("display", "none");
                                                        } else {
                                                            $("#dTokenDURATION_FREE").val('');
//                                                            $("#idViewDURATION_FREE").css("display", "");
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
                                                    else {
                                                        funErrorAlert(global_errorsql);
                                                    }
                                                }
                                            }
                                        });
                                        return false;
                                    }
                                    function LOAD_CERTIFICATION_PURPOSE(objCA, objPurpose, idCSRF)
                                    {
                                        $("#idHiddenCerPurpose").val(objPurpose);
                                        LoadFileManage(objPurpose);
                                        $.ajax({
                                            type: "post",
                                            url: "../JSONCommon",
                                            data: {
                                                idParam: 'loadcert_purpose',
                                                idCA: objCA,
                                                idPurpose: objPurpose,
                                                pISSUE_ENABLED: $("#pISSUE_ENABLED").val(),
                                                CsrfToken: idCSRF
                                            },
                                            cache: false,
                                            success: function (html)
                                            {
                                                if (html.length > 0)
                                                {
                                                    var cbxCERTIFICATION_DURATION = document.getElementById("dTokenCERTIFICATION_DURATION");
                                                    removeOptions(cbxCERTIFICATION_DURATION);
                                                    var obj = JSON.parse(html);
                                                    if (obj[0].Code === "0")
                                                    {
                                                        $("#idHiddenCerDurationOrProfileID").val(obj[0].ID);
                                                        for (var i = 0; i < obj.length; i++) {
                                                            cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                                        }
                                                        LOAD_CERTIFICATION_DURATION(obj[0].ID, idCSRF);
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
                                        return false;
                                    }
                                    function LOAD_CERTIFICATION_AUTHORITY(objCA, idCSRF)
                                    {
                                        $("#idHiddenCerCA").val(objCA.split('###')[0]);
                                        $("#idHiddenCerCoreSubject").val(objCA.split('###')[1]);
                                        $.ajax({
                                            type: "post",
                                            url: "../JSONCommon",
                                            data: {
                                                idParam: 'loadcert_authority_oftoken',
                                                idCA: objCA.split('###')[0],
                                                CsrfToken: idCSRF
                                            },
                                            cache: false,
                                            success: function (html)
                                            {
                                                if (html.length > 0)
                                                {
                                                    var cbxCERTIFICATION_PURPOSE = document.getElementById("dTokenCERTIFICATION_PURPOSE");
                                                    removeOptions(cbxCERTIFICATION_PURPOSE);
                                                    var obj = JSON.parse(html);
                                                    if (obj[0].Code === "0")
                                                    {
                                                        for (var i = 0; i < obj.length; i++) {
                                                            cbxCERTIFICATION_PURPOSE.options[cbxCERTIFICATION_PURPOSE.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                                        }
                                                        $("#idHiddenCerPurpose").val(obj[0].ID);
                                                        LOAD_CERTIFICATION_PURPOSE(objCA.split('###')[0], obj[0].ID, idCSRF);
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
                                                        cbxCERTIFICATION_PURPOSE.options[cbxCERTIFICATION_PURPOSE.options.length] = new Option("---", "");
                                                    }
                                                    else {
                                                        funErrorAlert(global_errorsql);
                                                    }
                                                }
                                            }
                                        });
                                        return false;
                                    }
                                    function LoadCACombobox_Frist(cbxData, idCSRF)
                                    {
                                        $.ajax({
                                            type: "post",
                                            url: "../JSONCommon",
                                            data: {
                                                idParam: 'listcacombobox',
                                                CsrfToken: idCSRF
                                            },
                                            cache: false,
                                            success: function (html)
                                            {
                                                var cbxTemplateData = document.getElementById(cbxData);
                                                removeOptions(cbxTemplateData);
                                                if (html.length > 0)
                                                {
                                                    var obj = JSON.parse(html);
                                                    if (obj[0].Code === "0")
                                                    {
                                                        var sFrist_IDCA = "";
                                                        for (var i = 0; i < obj.length; i++) {
                                                            sFrist_IDCA = obj[0].ID + "###" + obj[0].CERTIFICATION_AUTHORITY_CORECA_SUBJECT;
                                                            cbxTemplateData.options[cbxTemplateData.options.length] = new Option(obj[i].REMARK, obj[i].ID + "###" + obj[i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT);
                                                        }
                                                        LOAD_CERTIFICATION_AUTHORITY(sFrist_IDCA, idCSRF);
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
                                                        funErrorAlert(global_errorsql);
                                                    }
                                                }
                                            }
                                        });
                                        return false;
                                    }
                                    function popupEdit(id, idCSRF)
                                    {
                                        localStorage.setItem("LOCAL_PARAM_REGISTERCERTLIST", id);
                                        $('body').append('<div id="over"></div>');
                                        $(".loading-gif").show();
                                        $.ajax({
                                            type: "post",
                                            url: "../JSONDetailCommon",
                                            data: {
                                                idParam: 'tokendetailcert',
                                                id: id,
                                                CsrfToken: idCSRF
                                            },
                                            cache: false,
                                            success: function (html)
                                            {
                                                if (html.length > 0)
                                                {
                                                    var obj = JSON.parse(html);
                                                    if (obj[0].Code === "0")
                                                    {
                                                        $("#idDivDetailToken").css("display", "");
                                                        $("#dTokenID").val(obj[0].TOKEN_ID);
                                                        $("#dTokenSN").val(obj[0].TOKEN_SN);
                                                        $("#dTokenAGENT_NAME").val(obj[0].BRANCH_NAME);
                                                        LoadCACombobox_Frist("dTokenCERTIFICATION_AUTHORITY", idCSRF);
                                                        LOAD_BACKOFFICE_USER(obj[0].BRANCH_ID, idCSRF);
//                                                        if (obj[0].ENABLED === "1") {
//                                                            $("#dActiveFlag").bootstrapSwitch('state', true);
//                                                        } else {
//                                                            $("#dActiveFlag").bootstrapSwitch('state', false);
//                                                        }
                                                        goToByScroll("idDivDetailToken");
                                                    }
                                                    else if (obj[0].Code === "1")
                                                    {
                                                        $("#idDivDetailToken").css("display", "none");
                                                        $("#dTokenID").val("");
                                                        $("#dTokenSN").val("");
                                                        $("#dTokenAGENT_NAME").val("");
                                                    }
                                                    else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                                    {
                                                        RedirectPageLoginNoSess(global_alert_another_login);
                                                    }
                                                    else {
                                                        funErrorAlert(global_errorsql);
                                                    }
                                                } else {
                                                    $("#idDivDetailToken").css("display", "none");
                                                    $("#dTokenID").val("");
                                                    $("#dTokenSN").val("");
                                                    $("#dTokenAGENT_NAME").val("");
                                                }
                                                $(".loading-gif").hide();
                                                $('#over').remove();
                                            }
                                        });
                                        return false;
                                    }
                                </script>
                                <!-- Load file Manager Script -->
                                <script>
                                    function calUploadFile(input1, idType)
                                    {
                                        if (input1.value !== '')
                                        {
                                            var data1 = new FormData();
                                            $.each($('#input-file' + idType)[0].files, function(k, value)
                                            {
                                                data1.append(k, value);
                                            });
                                            data1.append('pFILE_PROFILE', idType);
                                            $.ajax({
                                                type: 'POST',
                                                url: '../FileManageUpload',
                                                data: data1,
                                                cache: false,
                                                contentType: false,
                                                processData: false,
                                                enctype: "multipart/form-data",
                                                success: function (html) {
                                                    if (html.length > 0)
                                                    {
                                                        console.log(html);
                                                        var obj = JSON.parse(html);
                                                        if (obj[0].Code === "0")
                                                        {
                                                            input1.value = '';
                                                            $("#idTBody" + idType).empty();
                                                            var content = "";
                                                            for (var i = 0; i < obj.length; i++) {
                                                                if(obj[i].FILE_PROFILE === idType)
                                                                {
                                                                    var sActionCRL = '<a style="cursor: pointer;" class="btn btn-info\n\
                                                                        btn-xs" onclick="DeleteTempFile(\'' + obj[i].FILE_PROFILE + '\', \'' + obj[i].FILE_NAME + '\');">\n\
                                                                        <i class="fa fa-pencil"></i> ' + global_fm_button_delete + '</a>';
                                                                    content += "<tr>" +
                                                                        "<td>" + obj[i].Index + "</td>" +
                                                                        "<td>" + obj[i].FILE_NAME + "</td>" +
                                                                        "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                        "<td>" + sActionCRL +"</td>" +
                                                                        "</tr>";
                                                               }
                                                            }
                                                            $("#idTBody" + idType).append(content);
                                                            $("#idDiv" + idType).css("display", "");
                                                        }
                                                        else if (obj[0].Code === JS_EX_CSRF)
                                                        {
                                                            funCsrfAlert();
                                                        }
                                                        else if (obj[0].Code === JS_EX_SPECIAL)
                                                        {
                                                            funErrorAlert(global_error_file_special);
                                                        }
                                                        else if (obj[0].Code === JS_EX_LOGIN)
                                                        {
                                                            RedirectPageLoginNoSess(global_alert_login);
                                                        }
                                                        else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                                        {
                                                            RedirectPageLoginNoSess(global_alert_another_login);
                                                        }
                                                        else {
                                                            funErrorAlert(global_errorsql);
                                                        }
                                                    }
                                                }
                                            });
                                            return false;
                                        } else
                                        {
                                            funErrorAlert(global_req_file);
                                        }
                                    }
                                    function DeleteTempFile(idType, vFILE_NAME)
                                    {
                                        swal({
                                            title: "",
                                            text: file_conform_delete,
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
                                                    type: 'POST',
                                                    url: '../FileManageCommon',
                                                    data: {
                                                        idParam: 'deletetempfilecert',
                                                        idType: idType,
                                                        vFILE_NAME: vFILE_NAME
                                                    },
                                                    cache: false,
                                                    success: function (html) {
                                                        if (html.length > 0)
                                                        {
                                                            console.log(html);
                                                            var obj = JSON.parse(html);
                                                            if (obj[0].Code === "0")
                                                            {
                                                                $("#idTBody" + idType).empty();
                                                                var content = "";
                                                                for (var i = 0; i < obj.length; i++) {
                                                                    if(obj[i].FILE_PROFILE === idType)
                                                                    {
                                                                        var sActionCRL = '<a style="cursor: pointer;" class="btn btn-info\n\
                                                                            btn-xs" onclick="DeleteTempFile(\'' + obj[i].FILE_PROFILE + '\', \'' + obj[i].FILE_NAME + '\');">\n\
                                                                            <i class="fa fa-pencil"></i> ' + global_fm_button_delete + '</a>';
                                                                        content += "<tr>" +
                                                                            "<td>" + obj[i].Index + "</td>" +
                                                                            "<td>" + obj[i].FILE_NAME + "</td>" +
                                                                            "<td>" + obj[i].FILE_SIZE + "</td>" +
                                                                            "<td>" + sActionCRL +"</td>" +
                                                                            "</tr>";
                                                                   }
                                                                }
                                                                $("#idTBody" + idType).append(content);
                                                                $("#idDiv" + idType).css("display", "");
                                                                funSuccNoLoad(file_succ_delete);
                                                            } else if(obj[0].Code === "1")
                                                            {
                                                                $("#idTBody" + idType).empty();
                                                                $("#idDiv" + idType).css("display", "none");
                                                                funSuccNoLoad(file_succ_delete);
                                                            }
                                                            else {
                                                                funErrorAlert(global_errorsql);
                                                            }
                                                        }
                                                        $(".loading-gif").hide();
                                                        $('#over').remove();
                                                    }
                                                });
                                                return false;
                                            }, JS_STR_ACTION_TIMEOUT);
                                        });
                                    }
                                    function LoadFileManage(idPurpose)
                                    {
                                        $.ajax({
                                            type: 'POST',
                                            url: '../JSONCommon',
                                            data: {
                                                idParam: 'loadfilemanageofpurpose',
                                                idPurpose: idPurpose
                                            },
                                            cache: false,
                                            success: function (html) {
                                                if (html.length > 0)
                                                {
                                                    console.log(html);
                                                    var obj = JSON.parse(html);
                                                    if (obj[0].Code === "0")
                                                    {
                                                        $("#idDivShowFileManaToken").empty();
                                                        var content = "";
                                                        for (var i = 0; i < obj.length; i++) {
                                                            content += '<fieldset class="scheduler-border">'+
                                                                '<legend class="scheduler-border">'+obj[i].REMARK+'</legend>'+
                                                                '<div class="col-sm-12" style="padding: 0px;margin: 0;">'+
                                                                    '<div class="form-group">'+
                                                                        '<label class="control-label col-sm-1" style="color: #000000; font-weight: bold;text-align:left;">'+global_fm_browse_file+'</label>'+
                                                                        '<div class="col-sm-11" style="padding-right: 0px;">'+
                                                                            '<input type="file" id="input-file'+obj[i].NAME+'" style="width: 100%;"'+
                                                                                'onchange="calUploadFile(this, \'' + obj[i].NAME + '\');" class="btn btn-default btn-file select_file" multiple>'+
                                                                        '</div>'+
                                                                    '</div>'+
                                                                '</div>'+
                                                                '<div class="col-sm-12" style="padding: 0px;margin: 0;">'+
                                                                    '<div class="form-group">'+
                                                                        '<div style="height:10px;"></div><label class="control-label123" style="color:red;font-weight: 200;">' + global_fm_browse_cert_note + '<%= Integer.parseInt(sMaxLengthFile) / 1024 %>' + ' MB. ' + global_fm_browse_cert_addnote +'</label>'+
                                                                    '</div>'+
                                                                '</div>'+
                                                                '<div style="padding: 10px 0 10px 0;display:none;" id="idDiv'+obj[i].NAME+'">'+
                                                                     '<table id="idTable'+obj[i].NAME+'" class="table table-bordered table-striped projects">'+
                                                                         '<thead>'+
                                                                            '<th>'+global_fm_STT+'</th>'+
                                                                            '<th>'+global_fm_file_name+'</th>'+
                                                                            '<th>'+global_fm_size+'</th>'+
                                                                            '<th>'+global_fm_action+'</th>'+
                                                                        '</thead>'+
                                                                        '<tbody id="idTBody'+obj[i].NAME+'">'+
                                                                        '</tbody>'+
                                                                     '</table>'+
                                                                '</div>'+
                                                            '</fieldset>';
                                                        }
                                                        $("#idDivShowFileManaToken").append(content);
                                                        $("#idDivShowFileManaToken").css("display", "");
                                                    } else if(obj[0].Code === "1")
                                                    {
                                                        $("#idDivShowFileManaToken").css("display", "none");
                                                    }
                                                    else {
                                                        funErrorAlert(global_errorsql);
                                                    }
                                                }
                                            }
                                        });
                                        return false;
                                    }
                                </script>
                                <!-- Load Component Cert Script -->
                                <script>
                                    function LoadDNCity(cbxCity)
                                    {
                                        $.ajax({
                                            type: "post",
                                            url: "../JSONCommon",
                                            data: {
                                                idParam: 'listcitycombobox',
                                                CsrfToken: '<%= anticsrf%>'
                                            },
                                            cache: false,
                                            success: function (html)
                                            {
                                                var cbxTemplateCity = document.getElementById(cbxCity);
                                                removeOptions(cbxTemplateCity);
                                                if (html.length > 0)
                                                {
                                                    var obj = JSON.parse(html);
                                                    if (obj[0].Code === "0")
                                                    {
                                                        for (var i = 0; i < obj.length; i++) {
                                                            cbxTemplateCity.options[cbxTemplateCity.options.length] = new Option(obj[i].cityprovincedesc, obj[i].cityprovinceId);
                                                        }
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
                                                        funErrorAlert(global_errorsql);
                                                    }
                                                }
                                            }
                                        });
                                        return false;
                                    }
                                    function LoadFormSubjectDN(vCertDurationOrProfileID, idDivShow)
                                    {
                                        $.ajax({
                                            type: "post",
                                            url: "../JSONCommon",
                                            data: {
                                                idParam: 'loadcert_profile_list',
                                                vCertDurationOrProfileID: vCertDurationOrProfileID
                                            },
                                            cache: false,
                                            success: function (html)
                                            {
                                                if (html.length > 0)
                                                {
                                                    var obj = JSON.parse(html);
                                                    $("#"+idDivShow).empty();
                                                    if (obj[0].Code === "0")
                                                    {
                                                        $("#"+idDivShow).css("display", "");
                                                        localStorage.setItem("localStoreCompSTID", null);
                                                        localStorage.setItem("localStoreCompSTValue", null);
                                                        var vContent = "";
                                                        var localStoreRequired = new Array();
                                                        var localStoreInput = new Array();
                                                        var localStoreInputID_Info = "";
                                                        var localStoreInputID_OnInput = "";
                                                        for (var i = 0; i < obj.length; i++) {
                                                            var vLabelRequired = "";
                                                            if (obj[i].IsRequired === '1') {
                                                                var vInputRequireID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID;
                                                                if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_RADIO_BUTTON) {
                                                                    vLabelRequired = '<label class="CssRequireField">' + global_fm_require_label + '</label>';
                                                                    for (var j = 0; j < obj[i].RADIO_LIST.length; j++) {
                                                                        vInputRequireID = obj[i].RADIO_LIST[j].SubjectDNAttrCode + obj[i].RADIO_LIST[j].CertTemplateID;
                                                                        localStoreRequired.push(vInputRequireID + '###' + obj[i].RADIO_LIST[j].SubjectDNAttrDesc + '###' + obj[i].RADIO_LIST[j].SubjectDNAttrPreFix);
                                                                    }
                                                                } else {
                                                                    vLabelRequired = '<label class="CssRequireField">' + global_fm_require_label + '</label>';
                                                                    localStoreRequired.push(vInputRequireID + '###' + obj[i].SubjectDNAttrDesc + '###' + obj[i].SubjectDNAttrPreFix);
                                                                }
                                                            }
                                                            if(obj[i].SubjectDNAttrType === JS_STR_COMPONENT_DN_VALUE_UID_RADIO_BUTTON)
                                                            {
//                                                                var vContentButton_MST_Radio = "<div class='form-group' style='margin-bottom:0px;'>";
                                                                var vContentButton_MST_Radio = "<div class='col-sm-12' style='margin-bottom:0px;padding-left: 0;'>";
                                                                var vContentButton_CMND_Radio = "<div class='col-sm-12' style='margin-bottom:0px;padding-left: 0;'>";
                                                                var vContentButton_MST_Text = "";
                                                                var vContentButton_CMND_Text = "";
                                                                var vContentButton_MST_Check = "";
                                                                var vContentButton_CMND_Check = "";
                                                                for (var j = 0; j < obj[i].RADIO_LIST.length; j++) {
                                                                    var vInputID = obj[i].RADIO_LIST[j].SubjectDNAttrCode + obj[i].RADIO_LIST[j].CertTemplateID;
                                                                    localStoreInput.push(vInputID + "###" + obj[i].RADIO_LIST[j].SubjectDNAttrCode + "###" + obj[i].RADIO_LIST[j].SubjectDNAttrPreFix
                                                                        + "###" + obj[i].RADIO_LIST[j].SubjectDNAttrCNType + "###" + obj[i].RADIO_LIST[j].SubjectDNAttrDesc);
                                                                    if(obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS 
                                                                        || obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                                                    {
                                                                        vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                                        if(vContentButton_MST_Check !== "")
                                                                        {
                                                                            vContentButton_MST_Radio += '<label class="radio-inline"><input type="radio" name="'+JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS
                                                                                +'" id="'+vInputID+'" onclick="OnChangeRadioMST();" value="'+obj[i].RADIO_LIST[j].SubjectDNAttrPreFix+'">' + obj[i].RADIO_LIST[j].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label>';
                                                                        }
                                                                        else
                                                                        {
                                                                            vContentButton_MST_Check = "checked";
                                                                            vContentButton_MST_Radio += '<label class="radio-inline"><input type="radio" '+vContentButton_MST_Check+' name="'+JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS
                                                                                +'" id="'+vInputID+'" onclick="OnChangeRadioMST();" value="'+obj[i].RADIO_LIST[j].SubjectDNAttrPreFix+'">' + obj[i].RADIO_LIST[j].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label>';
                                                                        }
                                                                        if(vContentButton_MST_Text === "")
                                                                        {
                                                                            if(obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                                                                            {
                                                                                var vInputID_Text = JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
//                                                                                vContentButton_MST_Text = '<div class="col-sm-6" style="padding-left: 10px;">'+
//                                                                                    '<div class="form-group">'+
//                                                                                    '<div style="width: 80%;padding-top: 5px;clear: both;">'+
//                                                                                    '<div style="float: left;width: 86%;">' +
//                                                                                    '<input class="form-control123" type="text" id="' + vInputID_Text + '" oninput="GetAlrmCertMSTMNS(this.value);" /></div>'+
//                                                                                    '</div>' +
//                                                                                    '<div style="float: right;text-align: right;">'+
//                                                                                    '<input class="btn btn-info" style="width: 120px;" disabled id="btnGetInfoMST" value="'+global_fm_button_get_info+'" type="button"/>'+
//                                                                                    '</div>' +
//                                                                                    '</div>'+
//                                                                                    '<div style="width: 100%;padding-top: 5px;">'+
//                                                                                    '<label style="color: red;display:none;" id="idHintMSTMNS"></label>'+
//                                                                                    '</div>'+
//                                                                                    '</div>'+
//                                                                                    '</div>';
                                                                                vContentButton_MST_Text = '<div class="col-sm-12" style="margin-bottom:5px;padding-left: 0;">'+
                                                                                    '<div style="width: 100%;padding-top: 5px;clear: both;">'+
                                                                                    '<div style="float: left;width: 86%;">' +
                                                                                    '<input class="form-control123" type="text" id="' + vInputID_Text + '" oninput="GetAlrmCertMSTMNS(this.value);" /></div>'+
                                                                                    '</div>' +
                                                                                    '<div style="float: right;text-align: right;">'+
                                                                                    '<input class="btn btn-info" style="width: 120px;" disabled id="btnGetInfoMST" value="'+global_fm_button_get_info+'" type="button"/>'+
                                                                                    '</div>' +
                                                                                    '</div>'+
                                                                                    '<div style="width: 100%;padding-top: 5px;">'+
                                                                                    '<label style="color: red;display:none;" id="idHintMSTMNS"></label>'+
                                                                                    '</div>'+
                                                                                    '</div>';
                                                                            }
                                                                            if(obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                                                            {
                                                                                var vInputID_Text = JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
//                                                                                vContentButton_MST_Text = '<div class="col-sm-6" style="padding-left: 10px;">'+
//                                                                                    '<div class="form-group">'+
//                                                                                    '<div style="width: 80%;padding-top: 5px;clear: both;">'+
//                                                                                    '<div style="float: left;width: 86%;">' +
//                                                                                    '<input class="form-control123" type="text" id="' + vInputID_Text + '" oninput="GetAlrmCertMSTMNS(this.value);" /></div>'+
//                                                                                    '</div>' +
//                                                                                    '<div style="float: right;text-align: right;">'+
//                                                                                    '<input class="btn btn-info" style="width: 120px;" id="btnGetInfoMST" value="'+global_fm_button_get_info+'" type="button" onclick="GetInfoMST();"/>'+
//                                                                                    '</div>' +
//                                                                                    '</div>'+
//                                                                                    '<div style="width: 100%;padding-top: 5px;">'+
//                                                                                    '<label style="color: red;display:none;" id="idHintMSTMNS"></label>'+
//                                                                                    '</div>'+
//                                                                                    '</div>'+
//                                                                                    '</div>';
                                                                                vContentButton_MST_Text = '<div class="col-sm-12" style="margin-bottom:5px;padding-left: 0;">'+
                                                                                    '<div style="width: 100%;padding-top: 5px;clear: both;">'+
                                                                                    '<div style="float: left;width: 86%;">' +
                                                                                    '<input class="form-control123" type="text" id="' + vInputID_Text + '" oninput="GetAlrmCertMSTMNS(this.value);" /></div>'+
                                                                                    '</div>' +
                                                                                    '<div style="float: right;text-align: right;">'+
                                                                                    '<input class="btn btn-info" style="width: 120px;" id="btnGetInfoMST" value="'+global_fm_button_get_info+'" type="button" onclick="GetInfoMST();"/>'+
                                                                                    '</div>' +
                                                                                    '</div>'+
                                                                                    '<div style="width: 100%;padding-top: 5px;">'+
                                                                                    '<label style="color: red;display:none;" id="idHintMSTMNS"></label>'+
                                                                                    '</div>'+
                                                                                    '</div>';
                                                                            }
                                                                        }
                                                                    }
                                                                    else if(obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND || obj[i].RADIO_LIST[j].SubjectDNAttrPreFix === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                                                                    {
                                                                        vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                                        if(vContentButton_CMND_Check !== "")
                                                                        {
                                                                            vContentButton_CMND_Radio += '<label class="radio-inline"><input type="radio" name="'+JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC
                                                                                +'" id="'+vInputID+'" value="'+obj[i].RADIO_LIST[j].SubjectDNAttrPreFix+'">' + obj[i].RADIO_LIST[j].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label>';
                                                                        }
                                                                        else
                                                                        {
                                                                            vContentButton_CMND_Check = "checked";
                                                                            vContentButton_CMND_Radio += '<label class="radio-inline"><input type="radio" '+vContentButton_CMND_Check+' name="'+JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC
                                                                                +'" id="'+vInputID+'" value="'+obj[i].RADIO_LIST[j].SubjectDNAttrPreFix+'">' + obj[i].RADIO_LIST[j].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label>';
                                                                        }
                                                                        if(vContentButton_CMND_Text === "")
                                                                        {
                                                                            var vInputID_Text = JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
//                                                                            vContentButton_CMND_Text ='<div class="col-sm-6" style="padding-left: 10px;">'+
//                                                                                '<div class="form-group">' +
//                                                                                '<input class="form-control123" oninput="GetAlrmCertCMNDHC(this.value);" type="text" id="' + vInputID_Text + '" />'+
//                                                                                '<label style="color: red;display:none;" id="idHintCMNDHC"></label>'+
//                                                                                '</div>'+
//                                                                                '</div>';
                                                                            vContentButton_CMND_Text ='<div class="col-sm-12" style="margin-bottom:10px;padding-left: 0;">'+
                                                                                '<input class="form-control123" oninput="GetAlrmCertCMNDHC(this.value);" type="text" id="' + vInputID_Text + '" />'+
                                                                                '<label style="color: red;display:none;" id="idHintCMNDHC"></label>'+
                                                                                '</div>';
                                                                        }
                                                                    }
                                                                    else { }
                                                                }
                                                                if(vContentButton_MST_Radio !== "<div class='form-group' style='margin-bottom:0px;'>")
                                                                {
                                                                    vContentButton_MST_Radio = vContentButton_MST_Radio + "</div></div>" + vContentButton_MST_Text;
                                                                    vContent = vContent + vContentButton_MST_Radio;
                                                                }
                                                                if(vContentButton_CMND_Radio !== "<div class='form-group' style='margin-bottom:0px;'>")
                                                                {
                                                                    vContentButton_CMND_Radio = vContentButton_CMND_Radio + "</div></div>" + vContentButton_CMND_Text;
                                                                    vContent = vContent + vContentButton_CMND_Radio;
                                                                }
                                                            }
                                                            else
                                                            {
                                                                var vInputID = obj[i].SubjectDNAttrCode + obj[i].CertTemplateID;
                                                                localStoreInput.push(vInputID + "###" + obj[i].SubjectDNAttrCode + "###" + obj[i].SubjectDNAttrPreFix + "###" + obj[i].SubjectDNAttrCNType + "###" + obj[i].SubjectDNAttrDesc);
                                                                if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE)
                                                                {
//                                                                    vContent += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
//                                                                        '<label class="control-label123">' + obj[i].SubjectDNAttrDesc + '</label> ' +
//                                                                        vLabelRequired +
//                                                                        '<select class="form-control123" id="' + vInputID + '"></select>' +
//                                                                        '</div>';
                                                                    vContent += '<div class="col-sm-6" style="padding-left: 0px;">' +
                                                                        '<div class="form-group">' +
                                                                        '<label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                                        '<div class="col-sm-7" style="padding-right: 0px;">'+
                                                                        '<select class="form-control123" id="' + vInputID + '"></select>' +
                                                                        '</div>' +
                                                                        '</div>' +
                                                                        '</div>';
                                                                    LoadDNCity(vInputID);
                                                                }
                                                                else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_PHONE)
                                                                {
                                                                    vContent += '<div class="col-sm-6" style="padding-left: 0px;">' +
                                                                        '<div class="form-group">' +
                                                                        '<label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                                        '<div class="col-sm-7" style="padding-right: 0px;">'+
                                                                        '<input class="form-control123" type="text" id="' + vInputID + '" onblur="autoTrimTextField("' + vInputID + '", this.value);" />' +
                                                                        '</div>' +
                                                                        '</div>' +
                                                                        '</div>';
                                                                }
                                                                else if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COUNTRY)
                                                                {
                                                                    var valueCountry = '<%= sDN_Country%>';
                                                                    vContent += '<div class="col-sm-6" style="padding-left: 10px;padding-left: 0;">' +
                                                                        '<div class="form-group">' +
                                                                        '<label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                                        '<div class="col-sm-7" style="padding-right: 0px;">'+
                                                                        '<input disabled class="form-control123" type="text" id="' + vInputID + '" value="'+valueCountry+'" />' +
                                                                        '</div>' +
                                                                        '</div>' +
                                                                        '</div>';
//                                                                    vContent += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
//                                                                        '<label class="control-label123">' + obj[i].SubjectDNAttrDesc + '</label> ' +
//                                                                        vLabelRequired +
//                                                                        '<input disabled class="form-control123" type="text" id="' + vInputID + '" value="'+valueCountry+'" />' +
//                                                                        '</div>';
                                                                }
                                                                else
                                                                {
                                                                    vInputID = vInputID.replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                                    vContent += '<div class="col-sm-6" style="padding-left: 0px;">' +
                                                                        '<div class="form-group">' +
                                                                        '<label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left:0;">' + obj[i].SubjectDNAttrDesc + ' ' + vLabelRequired + '</label> ' +
                                                                        '<div class="col-sm-7" style="padding-right: 0px;">'+
                                                                        '<input class="form-control123" type="text" id="' + vInputID + '" />' +
                                                                        '</div>' +
                                                                        '</div>' +
                                                                        '</div>';
//                                                                    vContent += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
//                                                                        '<label class="control-label123">' + obj[i].SubjectDNAttrDesc + '</label> ' +
//                                                                        vLabelRequired +
//                                                                        '<input class="form-control123" type="text" id="' + vInputID + '" />' +
//                                                                        '</div>';
                                                                }
                                                                if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_LOCALITY
                                                                    || obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE
                                                                    || obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COMMONNAME || obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                {
                                                                    if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_LOCALITY)
                                                                    {
                                                                        localStoreInputID_Info = localStoreInputID_Info + JS_STR_COMPONENT_DN_VALUE_LOCALITY + "###" + vInputID + ", ";
                                                                    }
                                                                    if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE)
                                                                    {
                                                                        localStoreInputID_Info = localStoreInputID_Info + JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE + "###" + vInputID + ", ";
                                                                    }
                                                                    if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                                                                    {
                                                                        localStoreInputID_Info = localStoreInputID_Info + JS_STR_COMPONENT_DN_VALUE_COMMONNAME + "###" + vInputID + ", ";
                                                                        localStoreInputID_OnInput = localStoreInputID_OnInput + JS_STR_COMPONENT_DN_VALUE_COMMONNAME + "###" + vInputID + ", ";
                                                                    }
                                                                    if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                    {
                                                                        localStoreInputID_OnInput = localStoreInputID_OnInput + JS_STR_COMPONENT_DN_VALUE_ORGANI + "###" + vInputID + ", ";
                                                                    }
                                                                    if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_STAFF
                                                                        || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_SIGNSERVER_STAFF
                                                                        || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE
                                                                        || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_SIGNSERVER_ENTERPRISE)
                                                                    {
                                                                        if(obj[i].SubjectDNAttrCode === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                        {
                                                                            localStoreInputID_Info = localStoreInputID_Info + JS_STR_COMPONENT_DN_VALUE_ORGANI + "###" + vInputID + ", ";
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        $("#"+idDivShow).append(vContent);
                                                        localStorage.setItem("localStoreRequiredPersonal", localStoreRequired);
                                                        localStorage.setItem("localStoreInputPersonal", localStoreInput);
                                                        localStorage.setItem("localStoreInputID_Info", localStoreInputID_Info);
                                                        if(localStoreInputID_OnInput !== null && localStoreInputID_OnInput !== "null" && localStoreInputID_OnInput !== "")
                                                        {
                                                            if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE
                                                                || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_SIGNSERVER_ENTERPRISE)
                                                            {
                                                                var idCNTemp="";
                                                                var idOTemp = "";
                                                                var sListInputCheckID_Info = localStoreInputID_OnInput.split(',');
                                                                for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                                                                    if(sSpace(sListInputCheckID_Info[i].split('###')[0]) === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                    {
                                                                        idOTemp = sSpace(sListInputCheckID_Info[i].split('###')[1]);
                                                                    }
                                                                    if(sSpace(sListInputCheckID_Info[i].split('###')[0]) === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                                                                    {
                                                                        idCNTemp = sSpace(sListInputCheckID_Info[i].split('###')[1]);
                                                                    }
                                                                }
                                                                if(idCNTemp !== "" && idOTemp !== "")
                                                                {
                                                                    document.getElementById(idOTemp).readOnly = true;
                                                                    document.getElementById(idCNTemp).oninput = function() { OnBlurCompany(idOTemp, this.value);};
                                                                }
                                                            }
                                                        }
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
                                                        $("#"+idDivShow).css("display", "none");
                                                        funErrorAlert(global_errorsql);
                                                    }
                                                }
                                            }
                                        });
                                        return false;
                                    }
                                    function GetAlrmCertMSTMNS()
                                    {
                                        var IsMST = "1";
                                        var IsCall = "0";
                                        $("#idHintMSTMNS").text('');
                                        $("#idHintMSTMNS").css("display", "none");
                                        var vInputID_Text = JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
                                        var vMST = $("#"+vInputID_Text).val();
                                        var sSelectedMST_MNS = $("input[name='"+JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS+"']:checked").val();
                                        if(sSelectedMST_MNS !== "") {
                                            if(vMST.length > 6) {
                                                IsCall = "1";
                                                vMST = sSelectedMST_MNS + vMST;
                                            }
                                        }
//                                        if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
//                                        {
//                                            if(vMST.length > 8)
//                                            {
//                                                IsCall = "1";
//                                                IsMST = "0";
//                                            }
//                                        }
//                                        if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
//                                        {
//                                            if(vMST.length > 8)
//                                            {
//                                                IsCall = "1";
//                                                IsMST = "1";
//                                            }
//                                        }
                                        if(IsCall === "1")
                                        {
                                            $.ajax({
                                                type: "post",
                                                url: "../RequestCommon",
                                                data: {
                                                    idParam: 'gethiscertmst',
                                                    IsMST: IsMST,
                                                    vMST: vMST
                                                },
                                                cache: false,
                                                success: function (html)
                                                {
                                                    var myStrings = sSpace(html).split('#');
                                                    if (myStrings[0] === "0")
                                                    {
                                                        $("#idHintMSTMNS").css("display", "");
                                                        if(IsMST === "1")
                                                        {
                                                            $("#idHintMSTMNS").text(global_succ_mst_register);
                                                        }
                                                        if(IsMST === "0")
                                                        {
                                                            $("#idHintMSTMNS").text(global_succ_mns_register);
                                                        }
                                                    }
                                                    else if (myStrings[0] === "1")
                                                    {
                                                    }
                                                    else if (myStrings[0] === JS_EX_CSRF)
                                                    {
                                                        funCsrfAlert();
                                                    }
                                                    else if (myStrings[0] === JS_EX_NO_DATA_WRITE)
                                                    {
                                                        funErrorAlert(global_no_data);
                                                    }
                                                    else if (myStrings[0] === JS_EX_LOGIN)
                                                    {
                                                        RedirectPageLoginNoSess(global_alert_login);
                                                    }
                                                    else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
                                                    {
                                                        RedirectPageLoginNoSess(global_alert_another_login);
                                                    }
                                                    else
                                                    {
                                                        funErrorAlert(global_errorsql);
                                                    }
                                                }
                                            });
                                            return false;
                                        }
                                    }
                                    function GetAlrmCertCMNDHC()
                                    {
                                        var vInputID_Text = JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND;
                                        var vCMND= $("#" + vInputID_Text).val();
                                        var IsCMND = "1";
                                        var IsCall = "0";
                                        $("#idHintCMNDHC").css("display", "none");
                                        $("#idHintCMNDHC").text('');
                                        var sSelectedMST_MNS = $("input[name='"+JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC+"']:checked").val();
                                        if(sSelectedMST_MNS !== "") {
                                                        if(vCMND.length > 6) {
                                                            IsCall = "1";
                                                            vCMND = sSelectedMST_MNS + vCMND;
                                                        }
                                                    }
//                                        if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND)
//                                        {
//                                            if(vCMND.length > 8)
//                                            {
//                                                IsCall = "1";
//                                                IsCMND = "1";
//                                            }
//                                        }
//                                        if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
//                                        {
//                                            if(vCMND.length > 8)
//                                            {
//                                                IsCall = "1";
//                                                IsCMND = "0";
//                                            }
//                                        }
                                        if(IsCall === "1")
                                        {
                                            $.ajax({
                                                type: "post",
                                                url: "../RequestCommon",
                                                data: {
                                                    idParam: 'gethiscertcmnd',
                                                    IsCMND: IsCMND,
                                                    vCMND: vCMND
                                                },
                                                cache: false,
                                                success: function (html)
                                                {
                                                    var myStrings = sSpace(html).split('#');
                                                    if (myStrings[0] === "0")
                                                    {
                                                        $("#idHintCMNDHC").css("display", "");
                                                        if(IsCMND === "1")
                                                        {
                                                            $("#idHintCMNDHC").text(global_succ_cmnd_register);
                                                        }
                                                        if(IsCMND === "0")
                                                        {
                                                            $("#idHintCMNDHC").text(global_succ_hc_register);
                                                        }
                                                    }
                                                    else if (myStrings[0] === "1")
                                                    {
                                                    }
                                                    else if (myStrings[0] === JS_EX_CSRF)
                                                    {
                                                        funCsrfAlert();
                                                    }
                                                    else if (myStrings[0] === JS_EX_NO_DATA_WRITE)
                                                    {
                                                        funErrorAlert(global_no_data);
                                                    }
                                                    else if (myStrings[0] === JS_EX_LOGIN)
                                                    {
                                                        RedirectPageLoginNoSess(global_alert_login);
                                                    }
                                                    else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
                                                    {
                                                        RedirectPageLoginNoSess(global_alert_another_login);
                                                    }
                                                    else
                                                    {
                                                        funErrorAlert(global_errorsql);
                                                    }
                                                }
                                            });
                                            return false;
                                        }
                                    }
                                    function GetInfoMST()
                                    {
                                        var vMST = $("#MST_MNS_TEXT").val();
                                        if(vMST !== "")
                                        {
                                            var vID_Info = localStorage.getItem("localStoreInputID_Info");
                                            if(vID_Info !== "")
                                            {
                                                $.ajax({
                                                type: "post",
                                                url: "../JSONCommon",
                                                data: {
                                                    idParam: 'getcompanyinfomst',
                                                    vMST: vMST
                                                },
                                                cache: false,
                                                success: function (html)
                                                {
                                                    if (html.length > 0)
                                                    {
                                                        var obj = JSON.parse(html);
                                                        if (obj[0].Code === "0")
                                                        {
                                                            var sListInputCheckID_Info = localStorage.getItem("localStoreInputID_Info").split(',');
                                                            for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                                                                var idCheckEmptyID_Info = sSpace(sListInputCheckID_Info[i].split('###')[0]);
                                                                if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_STAFF
                                                                    || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_SIGNSERVER_STAFF)
                                                                {
                                                                    if(idCheckEmptyID_Info === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                    {
                                                                        $("#" + sSpace(sListInputCheckID_Info[i].split('###')[1])).val(obj[0].NAME);
                                                                    }
                                                                }
                                                                else
                                                                {
                                                                    if(idCheckEmptyID_Info === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                                                                    {
                                                                        $("#" + sSpace(sListInputCheckID_Info[i].split('###')[1])).val(obj[0].NAME);
                                                                    }
                                                                    if($("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE
                                                                        || $("#CERTIFICATION_PURPOSE").val() === JS_STR_CERTIFICATION_PURPOSE_ID_SIGNSERVER_ENTERPRISE)
                                                                    {
                                                                        if(idCheckEmptyID_Info === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                        {
                                                                            $("#" + sSpace(sListInputCheckID_Info[i].split('###')[1])).val(obj[0].NAME);
                                                                        }
                                                                    }
                                                                }
                                                                if(idCheckEmptyID_Info === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE)
                                                                {
                                                                    $("#" +sListInputCheckID_Info[i].split('###')[1]).val(obj[0].PROVINCE);
                                                                }
                                                                if(idCheckEmptyID_Info === JS_STR_COMPONENT_DN_VALUE_LOCALITY)
                                                                {
                                                                    $("#" + sSpace(sListInputCheckID_Info[i].split('###')[1])).val(obj[0].LOCALTION);
                                                                }
                                                            }
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
                                                            funErrorAlert(global_errorsql);
                                                        }
                                                    }
                                                    else
                                                    {
                                                        funErrorAlert(global_errorsql);
                                                    }
                                                }
                                            });
                                            return false;
                                                localStorage.setItem("localStoreInputID_Info", null);
                                            }
                                        } else {
                                            funErrorAlert(policy_req_empty + global_fm_MST);
                                        }
                                    }
                                    function OnChangeRadioMST()
                                    {
                                        var sSelectedMST_MNS = $("input[name='"+JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS+"']:checked").val();
                                        if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                                        {
                                            document.getElementById("btnGetInfoMST").disabled = true;
                                        }
                                        if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                        {
                                            document.getElementById("btnGetInfoMST").disabled = false;
                                        }
                                    }
                                </script>
                                <div class="x_panel" id="idDivDetailToken" style="margin-bottom: 10px;display: none;">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(regiscert_title_view);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <button type="button" class="btn btn-info" onClick="detailUpdate('<%=anticsrf%>');"><script>document.write(global_fm_button_regis);</script></button>
                                                <button type="button" class="btn btn-info" onClick="detailClose();"><script>document.write(global_fm_button_close);</script></button>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form name="formToken" method="post" class="form-horizontal">
                                            <input type="hidden" name="dTokenID" id="dTokenID" hidden="true" readonly="true">
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"><script>document.write(token_fm_tokenid);</script></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" readonly maxlength="50" id="dTokenSN" name="dTokenSN" />
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"><script>document.write(global_fm_Branch);</script></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" name="dTokenAGENT_NAME" id="dTokenAGENT_NAME" readonly />
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"><script>document.write(global_fm_user_create);</script></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="dTokenUSER" id="dTokenUSER" class="form-control123"></select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                                        <script>document.write(global_fm_phone_contact);</script> <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" id="dTokenPHONE_CONTRACT" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_PHONE%>"
                                                            class="form-control123" onblur="autoTrimTextField('dTokenPHONE_CONTRACT', this.value);"/>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                                        <script>document.write(global_fm_email_contact);</script> <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" id="dTokenEMAIL_CONTRACT" class="form-control123" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_EMAIL%>">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"><script>document.write(global_fm_ca);</script></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="dTokenCERTIFICATION_AUTHORITY" id="dTokenCERTIFICATION_AUTHORITY"
                                                            class="form-control123" onchange="LOAD_CERTIFICATION_AUTHORITY(this.value, '<%= anticsrf%>');">
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                                        <script>document.write(global_fm_certpurpose);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select id="dTokenCERTIFICATION_PURPOSE" name="dTokenCERTIFICATION_PURPOSE" class="form-control123"
                                                            onchange="LOAD_CERTIFICATION_PURPOSE($('#idHiddenCerCA').val().split('###')[0], this.value, '<%= anticsrf%>');">
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"><script>document.write(global_fm_duration_cts);</script></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select id="dTokenCERTIFICATION_DURATION" name="dTokenCERTIFICATION_DURATION" class="form-control123"
                                                            onchange="LOAD_CERTIFICATION_DURATION(this.value, '<%= anticsrf%>');">
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <input id="idHiddenCerCA" readonly style="display: none;"/>
                                            <input id="idHiddenCerCoreSubject" readonly style="display: none;"/>
                                            <input id="idHiddenCerPurpose" readonly style="display: none;"/>
                                            <input id="idHiddenCerDurationOrProfileID" readonly style="display: none;"/>
                                            <input id="pISSUE_ENABLED" value="1" readonly style="display: none;"/>
                                                
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                                        <script>document.write(global_fm_amount_fee);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="dTokenFEE_AMOUNT" disabled id="dTokenFEE_AMOUNT" class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;display: none;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"><script>document.write(global_fm_date_free);</script></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="dTokenDURATION_FREE" disabled id="dTokenDURATION_FREE" class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleBackUpKey" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <label class="switch" for="iddTokenCheckBackUpKey">
                                                            <input type="checkbox" name="iddTokenCheckBackUpKey" id="iddTokenCheckBackUpKey"
                                                                <%= "1".equals(boCheckBackUpKey) ? "checked" : "" %> />
                                                            <div class="slider round"></div>
                                                        </label>
                                                    </div>
                                                </div>
                                                <script>$("#idLblTitleBackUpKey").text(regiscert_fm_check_backup_key);</script>
                                            </div>
                                            <div id="idDivShowFileManaToken" class="col-sm-13" style="padding: 0px 0px 10px 0px;margin: 0;display: none;clear: both;">
                                            </div>
                                            <fieldset class="scheduler-border" style="clear: both;">
                                                <legend class="scheduler-border"><script>document.write(global_fm_csr_info_cts);</script></legend>
                                                <div id="idViewCertInfoToken"></div>
                                            </fieldset>
                                            
                                            <!-- OLD -->
                                            <div class="col-sm-6" style="padding-left: 10px;text-align: left;">
                                                <div class="form-group">
                                                    
                                                </div>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">

                                                    </div>
                                                </div>
                                            </div>
                                            <!-- Update Script -->
                                            <script>
                                                function detailUpdate(idCSRF)
                                                {
                                                    var vSTR_COMPONENT_DN_VALUE_COMMONNAME = "";
                                                    var vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = "";
                                                    var vSTR_COMPONENT_DN_VALUE_MST = "";
                                                    var vSTR_COMPONENT_DN_VALUE_MNS = "";
                                                    var vSTR_COMPONENT_DN_VALUE_CMND = "";
                                                    var vSTR_COMPONENT_DN_VALUE_HC = "";
                                                    var vSTR_COMPONENT_DN_VALUE_PROVINCE_ID = "";
                                                    var vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC = "";
                                                    if (!JSCheckEmptyField($("#dTokenSN").val()))
                                                    {
                                                        funErrorAlert(policy_req_empty + token_fm_tokenid);
                                                        return false;
                                                    }
                                                    if (!JSCheckEmptyField($("#dTokenUSER").val()))
                                                    {
                                                        funErrorAlert(global_error_not_user_create);
                                                        return false;
                                                    }
                                                    if (!JSCheckEmptyField($("#dTokenPHONE_CONTRACT").val()))
                                                    {
                                                        $("#dTokenPHONE_CONTRACT").focus();
                                                        funErrorAlert(policy_req_empty + global_fm_phone_contact);
                                                        return false;
                                                    } else {
                                                        if (!JSCheckFormatPhoneNew_EditOne($("#dTokenPHONE_CONTRACT")))
                                                        {
                                                            $("#dTokenPHONE_CONTRACT").focus();
                                                            funErrorAlert(global_req_phone_format);
                                                            return false;
                                                        }
                                                    }
                                                    if (!JSCheckEmptyField($("#dTokenEMAIL_CONTRACT").val()))
                                                    {
                                                        $("#dTokenEMAIL_CONTRACT").focus();
                                                        funErrorAlert(policy_req_empty + global_fm_email_contact);
                                                        return false;
                                                    } else {
                                                        if (!FormCheckEmailSearch($("#dTokenEMAIL_CONTRACT").val()))
                                                        {
                                                            $("#dTokenEMAIL_CONTRACT").focus();
                                                            funErrorAlert(global_req_mail_format);
                                                            return false;
                                                        }
                                                    }
                                                    if (!JSCheckEmptyField($("#dTokenCERTIFICATION_DURATION").val()))
                                                    {
                                                        $("#dTokenCERTIFICATION_DURATION").focus();
                                                        funErrorAlert(policy_req_empty_choose + global_fm_duration_cts);
                                                        return false;
                                                    }
                                                    var vDNResult = "";
                                                    var sChoiseCert = $("#dTokenCERTIFICATION_AUTHORITY").val();
                                                    if (sChoiseCert !== "")
                                                    {
                                                        // CHECK SPECIAL FIELDS
                                                        var sListInputCheckSpecial = localStorage.getItem("localStoreInputPersonal").split(',');
                                                        for (var i = 0; i < sListInputCheckSpecial.length; i++) {
                                                            var idCheckEmptySpecial = sListInputCheckSpecial[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                            if(checkForSpecialChar($("#" + idCheckEmptySpecial).val())) {
                                                                $("#" + sListInputCheckSpecial[i].split('###')[0]).focus();
                                                                funErrorAlert(sListInputCheckSpecial[i].split('###')[4] + global_req_no_special + " (" + specialChars + ")");
                                                                return false;
                                                            }
                                                        }
                                                        // CHECK REQUIRED FIELDS
                                                        var sListRequire = localStorage.getItem("localStoreRequiredPersonal").split(',');
                                                        for (var i = 0; i < sListRequire.length; i++) {
                                                            var idCheckEmpty = sListRequire[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                            if (!JSCheckEmptyField($("#" + idCheckEmpty).val()))
                                                            {
                                                                $("#" + sListRequire[i].split('###')[0]).focus();
                                                                funErrorAlert(policy_req_empty + sListRequire[i].split('###')[1]);
                                                                return false;
                                                            }
                                                            if(sListRequire[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST
                                                                || sListRequire[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                                                            {
                                                                var sSelectedRequireMST_MNS = $("input[name='"+JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS+"']:checked").val();
                                                                if(sListRequire[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                                                {
                                                                    if(sSelectedRequireMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                                                    {
                                                                        if (!JSCheckEmptyField($("#" + JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).val()))
                                                                        {
                                                                            $("#" + JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).focus();
                                                                            funErrorAlert(policy_req_empty + sListRequire[i].split('###')[1]);
                                                                            return false;
                                                                        }
                                                                    }
                                                                }
                                                                if(sListRequire[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                                                                {
                                                                    if(sSelectedRequireMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                                                                    {
                                                                        if (!JSCheckEmptyField($("#" + JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).val()))
                                                                        {
                                                                            $("#" + JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).focus();
                                                                            funErrorAlert(policy_req_empty + sListRequire[i].split('###')[1]);
                                                                            return false;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            if(sListRequire[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND
                                                                || sListRequire[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                                                            {
                                                                var sSelectedRequireCMND_HC = $("input[name='"+JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC+"']:checked").val();
                                                                if(sListRequire[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND)
                                                                {
                                                                    if(sSelectedRequireCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND)
                                                                    {
                                                                        if (!JSCheckEmptyField($("#" + JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).val()))
                                                                        {
                                                                            $("#" + JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).focus();
                                                                            funErrorAlert(policy_req_empty + sListRequire[i].split('###')[1]);
                                                                            return false;
                                                                        }
                                                                    }
                                                                }
                                                                if(sListRequire[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                                                                {
                                                                    if(sSelectedRequireCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                                                                    {
                                                                        if (!JSCheckEmptyField($("#" + JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).val()))
                                                                        {
                                                                            $("#" + JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).focus();
                                                                            funErrorAlert(policy_req_empty + sListRequire[i].split('###')[1]);
                                                                            return false;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            if(sListRequire[i].split('###')[0].indexOf(JS_STR_COMPONENT_DN_VALUE_EMAIL) !== -1)
                                                            {
                                                                if (JSCheckEmptyField($("#" + sListRequire[i].split('###')[0]).val()))
                                                                {
                                                                    if (!FormCheckEmailSearch($("#" + sListRequire[i].split('###')[0]).val()))
                                                                    {
                                                                        $("#" + sListRequire[i].split('###')[0]).focus();
                                                                        funErrorAlert(global_req_mail_format);
                                                                        return false;
                                                                    }
                                                                }
                                                            }
                                                            if(sListRequire[i].split('###')[0].indexOf(JS_STR_COMPONENT_DN_VALUE_PHONE) !== -1)
                                                            {
                                                                if (JSCheckEmptyField($("#" + sListRequire[i].split('###')[0]).val()))
                                                                {
                                                                    if (!JSCheckFormatPhoneNew_EditOne($("#" + sListRequire[i].split('###')[0])))
                                                                    {
                                                                        $("#" + sListRequire[i].split('###')[0]).focus();
                                                                        funErrorAlert(global_req_phone_format);
                                                                        return false;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        // CHECK OUT REQUIRED FOR FORMAT PHONE, EMAIL
                                                        var sListInputOutRequired = localStorage.getItem("localStoreInputPersonal").split(',');
                                                        for (var i = 0; i < sListInputOutRequired.length; i++) {
                                                            var idCheckOutRequired = sListInputOutRequired[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                            if(idCheckOutRequired.indexOf(JS_STR_COMPONENT_DN_VALUE_EMAIL) !== -1)
                                                            {
                                                                if (JSCheckEmptyField($("#" + idCheckOutRequired).val()))
                                                                {
                                                                    if (!FormCheckEmailSearch($("#" + idCheckOutRequired).val()))
                                                                    {
                                                                        $("#" + idCheckOutRequired).focus();
                                                                        funErrorAlert(global_req_mail_format);
                                                                        return false;
                                                                    }
                                                                }
                                                            }
                                                            if(idCheckOutRequired.indexOf(JS_STR_COMPONENT_DN_VALUE_PHONE) !== -1)
                                                            {
                                                                if (JSCheckEmptyField($("#" + idCheckOutRequired).val()))
                                                                {
                                                                    if (!JSCheckFormatPhoneNew_EditOne($("#" + idCheckOutRequired)))
                                                                    {
                                                                        $("#" + idCheckOutRequired).focus();
                                                                        funErrorAlert(global_req_phone_format);
                                                                        return false;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        // CHECK EXISTS CHARACTER EQUALS
                                                        var sListEquals = localStorage.getItem("localStoreInputPersonal").split(',');
                                                        for (var m = 0; m < sListEquals.length; m++) {
                                                            var idItemValueEquals = sListEquals[m].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                            var itemValueEquals = $("#" + idItemValueEquals).val();
                                                            if (!JSCheckEqualsDN(itemValueEquals))
                                                                {
                                                                    $("#" + idItemValueEquals).focus();
                                                                    funErrorAlert(global_error_exists_equals_dn);
                                                                    return false;
                                                                }
                                                        }
                                                        // OUTPUT STRING DN
                                                        var sListInput = localStorage.getItem("localStoreInputPersonal").split(',');
                                                        var Is_Has_MST_MNS = "";
                                                        var Is_Has_CMND_HC = "";
                                                        var Is_Default_CN_TYPE_PERSON = "";
                                                        for (var i = 0; i < sListInput.length; i++) {
                                                            var idItemValue = sListInput[i].split('###')[0].replace(JS_STR_COMPONENT_DN_VALUE_UID, JS_STR_COMPONENT_DN_VALUE_UID_BEFORE);
                                                            var itemValue = $("#" + idItemValue).val();
                                                            if(sListInput[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST || sListInput[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS
                                                                || sListInput[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND || sListInput[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                                                            {
                                                                if(sListInput[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST || sListInput[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                                                                {
                                                                    Is_Has_MST_MNS = JS_STR_COMPONENT_DN_RADIO_ID_MST_MNS + "@@@" + sListInput[i].split('###')[1];
                                                                }
                                                                if(sListInput[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND || sListInput[i].split('###')[2] === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                                                                {
                                                                    Is_Has_CMND_HC = JS_STR_COMPONENT_DN_RADIO_ID_CMND_HC + "@@@" + sListInput[i].split('###')[1];
                                                                }
                                                            }
                                                            else
                                                            {
                                                                if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME)
                                                                {
                                                                    if(sListInput[i].split('###')[3] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_PERSON)
                                                                    {
                                                                        vSTR_COMPONENT_DN_VALUE_COMMONNAME = itemValue;
                                                                    } else if(sListInput[i].split('###')[3] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_COMPANY)
                                                                    {
                                                                        vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = itemValue;
                                                                    } else {}
                                                                    Is_Default_CN_TYPE_PERSON = sListInput[i].split('###')[3];
                                                                }
                                                                if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                {
                                                                    if(Is_Default_CN_TYPE_PERSON === JS_STR_COMPONENT_DN_VALUE_COMMONNAME_TYPE_PERSON)
                                                                    {
                                                                        vSTR_COMPONENT_DN_VALUE_COMPANY_NAME = itemValue;
                                                                    }
                                                                }
                                                                if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_CITYPROVINCE)
                                                                {
                                                                    vSTR_COMPONENT_DN_VALUE_PROVINCE_ID = itemValue;
                                                                    itemValue = $("#" + idItemValue + " option:selected").text();
                                                                    vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC = $("#" + idItemValue + " option:selected").text();
                                                                }
                                                                if(sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_LOCALITY ||
                                                                    sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_COUNTRY ||
                                                                    sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_TITLE ||
                                                                    sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_COMMONNAME ||
                                                                    sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_ORGANUNIT ||
                                                                    sListInput[i].split('###')[1] === JS_STR_COMPONENT_DN_VALUE_ORGANI)
                                                                {
                                                                    if (itemValue.indexOf(",") !== -1) {
                                                                        itemValue = itemValue.replace(/,/g , '\\,');
                                                                    }
                                                                    // ADD_PLUS_CHARACTER
                                                                    if (itemValue.indexOf("+") !== -1) {
                                                                        itemValue = itemValue.replace('+' , '\\+');
                                                                    }
                                                                }
                                                                if(itemValue !== "") {
                                                                    vDNResult += sListInput[i].split('###')[1] + "=" + sListInput[i].split('###')[2] +
                                                                        itemValue + ", ";
                                                                }
                                                            }
                                                        }
                                                        if(Is_Has_MST_MNS !== "")
                                                        {
                                                            var sSelectedMST_MNS = $("input[name='"+Is_Has_MST_MNS.split('@@@')[0]+"']:checked").val();
                                                            var sValueMST_MNS = $("#" + Is_Has_MST_MNS.split('@@@')[0] + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).val();
                                                            vDNResult = vDNResult + Is_Has_MST_MNS.split('@@@')[1] + "=" + sSelectedMST_MNS + sValueMST_MNS + ", ";
                                                            if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS)
                                                            {
                                                                vSTR_COMPONENT_DN_VALUE_MNS = sValueMST_MNS;
                                                            }
                                                            if(sSelectedMST_MNS === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST)
                                                            {
                                                                vSTR_COMPONENT_DN_VALUE_MST = sValueMST_MNS;
                                                            }
                                                        }
                                                        if(Is_Has_CMND_HC !== "")
                                                        {
                                                            var sSelectedCMND_HC = $("input[name='"+Is_Has_CMND_HC.split('@@@')[0]+"']:checked").val();
                                                            var sValueCMND_HC = $("#" + Is_Has_CMND_HC.split('@@@')[0] + JS_STR_COMPONENT_DN_RADIO_ID_EXTEND).val();
                                                            vDNResult = vDNResult + Is_Has_CMND_HC.split('@@@')[1] + "=" + sSelectedCMND_HC + sValueCMND_HC + ", ";
                                                            if(sSelectedCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND)
                                                            {
                                                                vSTR_COMPONENT_DN_VALUE_CMND = sValueCMND_HC;
                                                            }
                                                            if(sSelectedCMND_HC === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC)
                                                            {
                                                                vSTR_COMPONENT_DN_VALUE_HC = sValueCMND_HC;
                                                            }
                                                        }
                                                        var intSub = vDNResult.lastIndexOf(',');
                                                        vDNResult = vDNResult.substring(0, intSub);
                                                    }
                                                    var isCheckBackUpKey = "0";
                                                    if ($("#iddTokenCheckBackUpKey").is(':checked')) {
                                                        isCheckBackUpKey = "1";
                                                    }
                                                    $('body').append('<div id="over"></div>');
                                                    $(".loading-gif").show();
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../RequestCommon",
                                                        data: {
                                                            idParam: 'checkcertarlamuser',
                                                            pCERTIFICATION_PURPOSE: $("#dTokenCERTIFICATION_PURPOSE").val(),
                                                            pMNS: vSTR_COMPONENT_DN_VALUE_MNS,
                                                            pMST: vSTR_COMPONENT_DN_VALUE_MST,
                                                            pCMND: vSTR_COMPONENT_DN_VALUE_CMND,
                                                            pHC: vSTR_COMPONENT_DN_VALUE_HC
                                                        },
                                                        cache: false,
                                                        success: function (html_first) {
                                                            var arr_first = sSpace(html_first).split('#');
                                                            if (arr_first[0] === "0")
                                                            {
                                                                swal({
                                                                    title: "",
                                                                    text: arr_first[2],
                                                                    imageUrl: "../Images/icon_warning.png",
                                                                    imageSize: "45x45",
                                                                    showCancelButton: true,
                                                                    closeOnConfirm: true,
                                                                    allowOutsideClick: false,
                                                                    confirmButtonText: login_fm_buton_continue,
                                                                    cancelButtonText: global_button_grid_cancel,
                                                                    cancelButtonColor: "#199DC0"
                                                                },
                                                                function () {
                                                                    $('body').append('<div id="over"></div>');
                                                                    $(".loading-gif").show();
                                                                    setTimeout(function () {
                                                                        $.ajax({
                                                                            type: "post",
                                                                            url: "../RequestCommon",
                                                                            data: {
                                                                                idParam: 'registrationcert',
                                                                                sID: $("#dTokenID").val(),
                                                                                sTypeRegister: JS_STR_CERTIFICATION_PURPOSE_CODE_TOKEN,
                                                                                CertProfileID: $("#idHiddenCerDurationOrProfileID").val(),
                                                                                CACoreSubject: $("#idHiddenCerCoreSubject").val(),
                                                                                DN: vDNResult,
                                                                                pPERSONAL_NAME: vSTR_COMPONENT_DN_VALUE_COMMONNAME,
                                                                                pCOMPANY_NAME: vSTR_COMPONENT_DN_VALUE_COMPANY_NAME,
                                                                                pDOMAIN_NAME: "",
                                                                                pTAX_CODE: vSTR_COMPONENT_DN_VALUE_MST,
                                                                                pBUDGET_CODE: vSTR_COMPONENT_DN_VALUE_MNS,
                                                                                pP_ID: vSTR_COMPONENT_DN_VALUE_CMND,
                                                                                pPASSPORT: vSTR_COMPONENT_DN_VALUE_HC,
                                                                                PHONE_CONTRACT: $("#dTokenPHONE_CONTRACT").val(),
                                                                                EMAIL_CONTRACT: $("#dTokenEMAIL_CONTRACT").val(),
                                                                                CREATE_USER: $("#dTokenUSER").val(),
                                                                                pPROVINCE_ID: vSTR_COMPONENT_DN_VALUE_PROVINCE_ID,
                                                                                pPROVINCE_DESC: vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC,
                                                                                CheckPRIVATE_KEY: isCheckBackUpKey,
                                                                                CsrfToken: idCSRF
                                                                            },
                                                                            cache: false,
                                                                            success: function (html) {
                                                                                var arr = sSpace(html).split('#');
                                                                                if (arr[0] === "0")
                                                                                {
                                                                                    if(arr[3] === "1")
                                                                                    {
                                                                                        pushNotificationApprove($("#dTokenID").val(), $("#idHiddenCerDurationOrProfileID").val());
                                                                                    }
                                                                                    if(arr[2] === "1")
                                                                                    {
                                                                                        localStorage.setItem("PrintRegisterPersonal", arr[1]);
                                                                                        localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", arr[1]);
                                                                                        localStorage.setItem("PrintRegisterBusiness", null);
//                                                                                        window.location = "PrintRegisterPersonal.jsp?id=" + arr[1];
                                                                                        swal({
                                                                                            title: "",
                                                                                            text: regiscert_succ_add + '. \n' + global_confirm_print_register,
                                                                                            imageUrl: "../Images/success.png",
                                                                                            imageSize: "45x45",
                                                                                            showCancelButton: true,
                                                                                            closeOnConfirm: true,
                                                                                            allowOutsideClick: false,
                                                                                            confirmButtonText: login_fm_buton_OK,
                                                                                            cancelButtonText: global_button_grid_cancel,
                                                                                            cancelButtonColor: "#199DC0"
                                                                                        },
                                                                                        function () {
                                                                                            ShowDialogPrint(arr[1], "0");
                                                                                        });
                                                                                    }
                                                                                    if(arr[2] === "0")
                                                                                    {
                                                                                        localStorage.setItem("PrintRegisterPersonal", null);
                                                                                        localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", arr[1]);
                                                                                        localStorage.setItem("PrintRegisterBusiness", arr[1]);
//                                                                                        window.location = "PrintRegisterBusiness.jsp?id=" + arr[1];
                                                                                        swal({
                                                                                            title: "",
                                                                                            text: regiscert_succ_add + '. \n' + global_confirm_print_register,
                                                                                            imageUrl: "../Images/success.png",
                                                                                            imageSize: "45x45",
                                                                                            showCancelButton: true,
                                                                                            closeOnConfirm: true,
                                                                                            allowOutsideClick: false,
                                                                                            confirmButtonText: login_fm_buton_OK,
                                                                                            cancelButtonText: global_button_grid_cancel,
                                                                                            cancelButtonColor: "#199DC0"
                                                                                        },
                                                                                        function () {
                                                                                            ShowDialogPrint(arr[1], "1");
                                                                                        });
                                                                                    }
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
                                                                                else if (arr[0] === "1")
                                                                                {
                                                                                    funErrorAlert(global_error_request_exists);
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
                                                            } else if (arr_first[0] === "1")
                                                            {
                                                                $.ajax({
                                                                    type: "post",
                                                                    url: "../RequestCommon",
                                                                    data: {
                                                                        idParam: 'registrationcert',
                                                                        sID: $("#dTokenID").val(),
                                                                        sTypeRegister: JS_STR_CERTIFICATION_PURPOSE_CODE_TOKEN,
                                                                        CertProfileID: $("#idHiddenCerDurationOrProfileID").val(),
                                                                        CACoreSubject: $("#idHiddenCerCoreSubject").val(),
                                                                        DN: vDNResult,
                                                                        pPERSONAL_NAME: vSTR_COMPONENT_DN_VALUE_COMMONNAME,
                                                                        pCOMPANY_NAME: vSTR_COMPONENT_DN_VALUE_COMPANY_NAME,
                                                                        pDOMAIN_NAME: "",
                                                                        pTAX_CODE: vSTR_COMPONENT_DN_VALUE_MST,
                                                                        pBUDGET_CODE: vSTR_COMPONENT_DN_VALUE_MNS,
                                                                        pP_ID: vSTR_COMPONENT_DN_VALUE_CMND,
                                                                        pPASSPORT: vSTR_COMPONENT_DN_VALUE_HC,
                                                                        PHONE_CONTRACT: $("#dTokenPHONE_CONTRACT").val(),
                                                                        EMAIL_CONTRACT: $("#dTokenEMAIL_CONTRACT").val(),
                                                                        CREATE_USER: $("#dTokenUSER").val(),
                                                                        pPROVINCE_ID: vSTR_COMPONENT_DN_VALUE_PROVINCE_ID,
                                                                        pPROVINCE_DESC: vSTR_COMPONENT_DN_VALUE_PROVINCE_DESC,
                                                                        CheckPRIVATE_KEY: isCheckBackUpKey,
                                                                        CsrfToken: idCSRF
                                                                    },
                                                                    cache: false,
                                                                    success: function (html) {
                                                                        var arr = sSpace(html).split('#');
                                                                        if (arr[0] === "0")
                                                                        {
                                                                            if(arr[3] === "1")
                                                                            {
                                                                                pushNotificationApprove($("#dTokenID").val(), $("#idHiddenCerDurationOrProfileID").val());
                                                                            }
                                                                            if(arr[2] === "1")
                                                                            {
                                                                                localStorage.setItem("PrintRegisterPersonal", arr[1]);
                                                                                localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", arr[1]);
                                                                                localStorage.setItem("PrintRegisterBusiness", null);
//                                                                                window.location = "PrintRegisterPersonal.jsp?id=" + arr[1];
                                                                                swal({
                                                                                    title: "",
                                                                                    text: regiscert_succ_add + '. \n' + global_confirm_print_register,
                                                                                    imageUrl: "../Images/success.png",
                                                                                    imageSize: "45x45",
                                                                                    showCancelButton: true,
                                                                                    closeOnConfirm: true,
                                                                                    allowOutsideClick: false,
                                                                                    confirmButtonText: login_fm_buton_OK,
                                                                                    cancelButtonText: global_button_grid_cancel,
                                                                                    cancelButtonColor: "#199DC0"
                                                                                },
                                                                                function () {
                                                                                    ShowDialogPrint(arr[1], "0");
                                                                                });
                                                                            }
                                                                            if(arr[2] === "0")
                                                                            {
                                                                                localStorage.setItem("PrintRegisterPersonal", null);
                                                                                localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", arr[1]);
                                                                                localStorage.setItem("PrintRegisterBusiness", arr[1]);
//                                                                                window.location = "PrintRegisterBusiness.jsp?id=" + arr[1];
                                                                                swal({
                                                                                    title: "",
                                                                                    text: regiscert_succ_add + '. \n' + global_confirm_print_register,
                                                                                    imageUrl: "../Images/success.png",
                                                                                    imageSize: "45x45",
                                                                                    showCancelButton: true,
                                                                                    closeOnConfirm: true,
                                                                                    allowOutsideClick: false,
                                                                                    confirmButtonText: login_fm_buton_OK,
                                                                                    cancelButtonText: global_button_grid_cancel,
                                                                                    cancelButtonColor: "#199DC0"
                                                                                },
                                                                                function () {
                                                                                    ShowDialogPrint(arr[1], "1");
                                                                                });
                                                                            }
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
                                                                        else if (arr[0] === "1")
                                                                        {
                                                                            funErrorAlert(global_error_request_exists);
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
                                                            else {
                                                                funErrorAlert(global_errorsql);
                                                            }
                                                            $(".loading-gif").hide();
                                                            $('#over').remove();
                                                        }
                                                    });
                                                    return false;
                                                }
                                                function pushNotificationApprove(name, desc) {
                                                    var xmlhttp = new XMLHttpRequest();
                                                    xmlhttp.open("POST", "../PushNotiRequestApprove?t="+new Date(), false);
                                                    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                                                    xmlhttp.send("name="+name+"&message="+desc);
                                                }
                                                function detailClose()
                                                {
                                                    $("#idDivDetailToken").css("display", "none");
                                                    $("#dTokenID").val("");
                                                    $("#dTokenSN").val("");
                                                    $("#dTokenAGENT_NAME").val("");
                                                    $("#dTokenPHONE_CONTRACT").val("");
                                                    $("#dTokenEMAIL_CONTRACT").val("");
                                                    removeOptions(document.getElementById("dTokenCERTIFICATION_AUTHORITY"));
                                                    removeOptions(document.getElementById("dTokenCERTIFICATION_PURPOSE"));
                                                    removeOptions(document.getElementById("dTokenCERTIFICATION_DURATION"));
                                                    $("#dTokenFEE_AMOUNT").val("");
                                                    $("#dTokenDURATION_FREE").val("");
                                                    $("#idHiddenCerCA").val("");
                                                    $("#idHiddenCerCoreSubject").val("");
                                                    $("#idHiddenCerPurpose").val("");
                                                    $("#idHiddenCerDurationOrProfileID").val("");
                                                    goToByScroll("idShowResultSearch");
                                                }
                                            </script>
                                        </form>
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
                        <script>
                            $(document).ready(function () {
                                if(localStorage.getItem("LOCAL_PARAM_REGISTERCERTLIST") !== null && localStorage.getItem("LOCAL_PARAM_REGISTERCERTLIST") !== "null")
                                {
                                    popupEdit(localStorage.getItem("LOCAL_PARAM_REGISTERCERTLIST"));
                                }
                            });
                       </script>
                    </div>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
            <script src="../style/jquery.min.js"></script>
            <script src="../style/bootstrap.min.js"></script>
            <script src="../style/custom.min.js"></script>
            <script src="../js/moment.min.js"></script>
            <script src="../js/daterangepicker.js"></script>
            <!-- Modal Registration Print -->
            <div id="myModalRegisterPrint" class="modal fade" role="dialog">
                <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                     left: 0; height: 100%;" class="loading-gifRegisterPrint">
                    <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
                </div>
                <div class="modal-dialog modal-800" id="myDialogRegisterPrint">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <div id="contentRegisterPrint"></div>
                        </div>
                    </div>
                </div>
            </div>
            <script>
                function ShowDialogPrint(id, isEnterprise)
                {
                    $('#myModalRegisterPrint').modal('show');
                    $('#contentRegisterPrint').empty();
                    localStorage.setItem("sessCertToPrint", "1");
                    if(isEnterprise === "1")
                    {
                        localStorage.setItem("PrintRegisterPersonal", null);
                        localStorage.setItem("PrintRegisterBusiness", id);
                        $('#contentRegisterPrint').load('PrintRegisterBusiness.jsp', {id:id}, function () {
                        });
                    }
                    else {
                        localStorage.setItem("PrintRegisterPersonal", id);
                        localStorage.setItem("PrintRegisterBusiness", null);
                        $('#contentRegisterPrint').load('PrintRegisterPersonal.jsp', {id:id}, function () {
                        });
                    }
                    $(".loading-gifRegisterPrint").hide();
                    $(".loading-gif").hide();
                    $('#over').remove();
                }
                function CloseDialogPrint()
                {
                    $('#myModalRegisterPrint').modal('hide');
                    $(".loading-gifRegisterPrint").hide();
                    $(".loading-gif").hide();
                    $('#over').remove();
                }
            </script>
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