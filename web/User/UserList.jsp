<%-- 
    Document   : UserList
    Created on : Oct 10, 2013, 2:51:03 PM
    Author     : Thanh
--%>
<%@page import="vn.ra.utility.PropertiesContent"%>
<%@page import="java.net.URLEncoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<%
    String strAlertAllTimes = "0";
%>
<%  response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
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
            document.title = user_title_list;
            $(document).ready(function () {
                localStorage.setItem("LOCAL_PARAM_USERLIST", null);
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
//                $('#idCheck').removeClass("js-switch");
//                $('#idCheck').addClass("js-switch");
            });
            function popupaddpackage(id)
            {
                localStorage.setItem("LOCAL_PARAM_USERLIST", id);
                window.location = 'UserEdit.jsp?id=' + id;
            }
            function NewCity() {
                window.location = 'UserNew.jsp';
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
                    if (JSCheckEmptyField($("#RegistrationEmail").val())) {
//                        var vValuePhone = sSpace($("#RegistrationEmail").val());
//                        var cChatStatus = getCheckStatusEmail(vValuePhone);
//                        cChatStatus.done(function(msg) {
//                            var s = sSpace(msg).split('#');
//                            if(s[0] !== "0")
//                            {
//                                $("#RegistrationEmail").focus();
//                                funErrorAlert(global_req_mail_format);
//                                return false;
//                            }
//                        });
                        if(!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), document.form.RegistrationEmail.value))
                        {
                            document.form.RegistrationEmail.focus();
                            funErrorAlert(global_req_mail_format);
                            return false;
                        }
                    }
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
            .projects th{font-weight: bold;}
            .navbar-right{margin-right: 0;padding-right:10px;}
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.2em 10px 1.2em !important;
                margin: 0 0 12px 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
            .x_panel {
                padding:10px 17px 15px 17px;
            }
            .x_content {
                padding: 0 5px 0 5px;
            }
        </style>
        <!-- Load Edit Info Script -->
        <script>
            function OnLoadding()
            {
                $('#idBodyUserList').append('<div id="over"></div>');
                $(".loading-gif").show();
            }
            function popupEdit(id)
            {
                OnLoadding();
                $('#contentEdit').empty();
                $('#contentEdit').load('UserEdit.jsp', {id:id}, function () {
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
                $(".loading-gif").hide();
                $('#over').remove();
            }
        </script>
    </head>
    <body class="nav-md" id="idBodyUserList">
        <%
        if ((session.getAttribute("sUserID")) != null) {
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
            String sessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
            String sessTreeArrayBranchID = session.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
            String sRegexPolicy = "";
                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                if (sessGeneralPolicy[0].length > 0) {
                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                    {
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_REGEX_FOR_PHONE_EMAIL))
                        {
                            sRegexPolicy = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                            break;
                        }
                    }
                }
                String sREGEX_PHONE = Definitions.CONFIG_DEFAULT_VALUE_REGEX_PHONE;
                String sREGEX_EMAIL = Definitions.CONFIG_DEFAULT_VALUE_REGEX_EMAIL;
                if(!"".equals(sRegexPolicy))
                {
                    sREGEX_PHONE = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_PHONE);
                    sREGEX_EMAIL = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_EMAIL);
                }
        %>
        <script>
            $(document).ready(function () {
                localStorage.setItem("sessLocal_REGEX_PHONE", '<%=sREGEX_PHONE%>');
                localStorage.setItem("sessLocal_REGEX_EMAIL", '<%=sREGEX_EMAIL%>');
            });
        </script>
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
                        document.getElementById("idNameURL").innerHTML = user_title_list;
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
                                Boolean hasPaging = false;
                                if (!"".equals(EscapeUtils.CheckTextNull(tempIPagNo)) && !"null".equals(tempIPagNo)) {
                                    hasPaging = true;
                                }
                                String strMess = "";
                                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                try {
                                    BACKOFFICE_USER[][] rsPgin = new BACKOFFICE_USER[1][];
                                    if (session.getAttribute("RefreshUserSess") != null && session.getAttribute("FromCreateDateSUserlist") != null
                                            && session.getAttribute("ToCreateDateSUserlist") != null) {
                                        session.setAttribute("RefreshUserSess", "1");
                                        session.setAttribute("SearchSharePagingUser", "0");
                                        statusLoad = 1;
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSUserlist");
                                        String ToCreateDate = (String) session.getAttribute("ToCreateDateSUserlist");
                                        String FromCreateDate = (String) session.getAttribute("FromCreateDateSUserlist");
                                        String RegistrationEmail = (String) session.getAttribute("RegistrationEmailS");
                                        String BranchOffice = (String) session.getAttribute("BranchOfficeSUserList");
                                        String UserRole = (String) session.getAttribute("UserRoleS");
                                        String USER_STATE = (String) session.getAttribute("USER_STATES");
                                        String Username = (String) session.getAttribute("UsernameS");
                                        session.setAttribute("FromCreateDateSUserlist", FromCreateDate);
                                        session.setAttribute("ToCreateDateSUserlist", ToCreateDate);
                                        session.setAttribute("RegistrationEmailS", RegistrationEmail);
                                        session.setAttribute("UserRoleS", UserRole);
                                        session.setAttribute("USER_STATES", USER_STATE);
                                        session.setAttribute("BranchOfficeSUserList", BranchOffice);
                                        session.setAttribute("UsernameS", Username);
                                        session.setAttribute("AlertAllTimeSUserlist", strAlertAllTimes);
                                        session.setAttribute("RefreshUserSess", null);
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(UserRole)) {
                                            UserRole = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(USER_STATE)) {
                                            USER_STATE = "";
                                        }
                                        if(sessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            sessTreeArrayBranchID = "";
                                        }
                                        String sBRANCH_LIST = sessTreeArrayBranchID;
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                            BranchOffice = "";
                                        } else {
                                            sBRANCH_LIST = "";
                                        }
                                        if("1".equals(strAlertAllTimes))
                                        {
                                            FromCreateDate = "";
                                            ToCreateDate = "";
                                        }
                                        int ss = 0;
                                        ss = db.S_BO_USER_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(RegistrationEmail),
                                            EscapeUtils.escapeHtmlSearch(UserRole), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                            EscapeUtils.escapeHtmlSearch(Username), EscapeUtils.escapeHtmlSearch(USER_STATE), sBRANCH_LIST);
                                        if (session.getAttribute("IPageNoSessSearchUser") != null) {
                                            String sPage = (String) session.getAttribute("IPageNoSessSearchUser");
                                            iPagNo = Integer.parseInt(sPage);
                                        }
                                        if (session.getAttribute("ISwRwsSessSearchUser") != null) {
                                            String sSumPage = (String) session.getAttribute("ISwRwsSessSearchUser");
                                            iSwRws = Integer.parseInt(sSumPage);
                                        }
                                        if (session.getAttribute("NumPageSessSearchUser") != null) {
                                            String sNoPage = (String) session.getAttribute("NumPageSessSearchUser");
                                            iPaNoSS = Integer.parseInt(sNoPage);
                                        }
                                        session.setAttribute("IPageNoSessSearchUser", String.valueOf(iPagNo));
                                        session.setAttribute("ISwRwsSessSearchUser", String.valueOf(iSwRws));
                                        if (ss > 0) {
                                            db.S_BO_USER_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(RegistrationEmail),
                                                EscapeUtils.escapeHtmlSearch(UserRole), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                                EscapeUtils.escapeHtmlSearch(Username), EscapeUtils.escapeHtmlSearch(USER_STATE),
                                                sessLanguageGlobal, sBRANCH_LIST, rsPgin, iPagNo, iSwRws);
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
                                    } else if (request.getMethod().equals("POST") || hasPaging == true) {
                                        session.setAttribute("RefreshUserSess", null);
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("IPageNoSessSearchUser", null);
                                            session.setAttribute("ISwRwsSessSearchUser", null);
                                        }
                                        String sCsrfToken = request.getParameter("CsrfToken");
                                        String stempCsrfToken = request.getParameter("tempCsrfToken");
                                        if (hasPaging != true) {
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
                                    String RegistrationEmail = request.getParameter("RegistrationEmail");
                                    String UserRole = request.getParameter("UserRole");
                                    String USER_STATE = request.getParameter("USER_STATE");
                                    String BranchOffice = request.getParameter("BranchOffice");
                                    String Username = EscapeUtils.ConvertStringToUnicode(request.getParameter("Username"));
                                    Boolean nameCheck = Boolean.valueOf(request.getParameter("nameCheck") != null);
                                    if (nameCheck == false) {
                                        FromCreateDate = "";
                                        ToCreateDate = "";
                                        strAlertAllTimes = "1";
                                    }
                                    if (hasPaging == true) {
                                        session.setAttribute("SearchSharePagingUser", "0");
                                        ToCreateDate = (String) session.getAttribute("ToCreateDateSUserlist");
                                        FromCreateDate = (String) session.getAttribute("FromCreateDateSUserlist");
                                        RegistrationEmail = (String) session.getAttribute("RegistrationEmailS");
                                        BranchOffice = (String) session.getAttribute("BranchOfficeSUserList");
                                        UserRole = (String) session.getAttribute("UserRoleS");
                                        USER_STATE = (String) session.getAttribute("USER_STATES");
                                        Username = (String) session.getAttribute("UsernameS");
                                        strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSUserlist");
                                    } else {
                                        session.setAttribute("SearchSharePagingUser", "1");
                                        session.setAttribute("CountListUser", null);
                                    }
                                    session.setAttribute("FromCreateDateSUserlist", FromCreateDate);
                                    session.setAttribute("ToCreateDateSUserlist", ToCreateDate);
                                    session.setAttribute("RegistrationEmailS", RegistrationEmail);
                                    session.setAttribute("UserRoleS", UserRole);
                                    session.setAttribute("USER_STATES", USER_STATE);
                                    session.setAttribute("BranchOfficeSUserList", BranchOffice);
                                    session.setAttribute("UsernameS", Username);
                                    session.setAttribute("AlertAllTimeSUserlist", strAlertAllTimes);
                                    if(sessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                        sessTreeArrayBranchID = "";
                                    }
                                    String sBRANCH_LIST = sessTreeArrayBranchID;
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(UserRole)) {
                                        UserRole = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(USER_STATE)) {
                                        USER_STATE = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                        BranchOffice = "";
                                    } else{sBRANCH_LIST = "";}
                                    int ss = 0;
                                    if ((session.getAttribute("CountListUser")) == null) {
                                        ss = db.S_BO_USER_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(RegistrationEmail),
                                            EscapeUtils.escapeHtmlSearch(UserRole), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                            EscapeUtils.escapeHtmlSearch(Username), EscapeUtils.escapeHtmlSearch(USER_STATE), sBRANCH_LIST);
                                        session.setAttribute("CountListUser", String.valueOf(ss));
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListUser");
                                        ss = Integer.parseInt(sCount);
                                        session.setAttribute("CountListUser", String.valueOf(ss));
                                    }
                                    iTotRslts = ss;
                                    if (iTotRslts > 0) {
                                        db.S_BO_USER_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(RegistrationEmail), EscapeUtils.escapeHtmlSearch(UserRole),
                                            EscapeUtils.escapeHtmlSearch(BranchOffice), EscapeUtils.escapeHtmlSearch(Username),
                                            EscapeUtils.escapeHtmlSearch(USER_STATE), sessLanguageGlobal, sBRANCH_LIST, rsPgin, iPagNo, iSwRws);
                                        session.setAttribute("IPageNoSessSearchUser", String.valueOf(iPagNo));
                                        session.setAttribute("ISwRwsSessSearchUser", String.valueOf(iSwRws));
                                        strMess = com.convertMoney(ss);
                                        if (rsPgin[0].length > 0) {
                                            status = 1;
                                        } else {
                                            status = 1000;
                                        }
                                    } else {
                                        status = 1000;
                                    }
                                } else 
                                {
                                    session.setAttribute("FromCreateDateSUserlist", null);
                                    session.setAttribute("ToCreateDateSUserlist", null);
                                    session.setAttribute("RegistrationEmailS", null);
                                    session.setAttribute("UserRoleS", null);
                                    session.setAttribute("USER_STATES", null);
                                    session.setAttribute("BranchOfficeSUserList", null);
                                    session.setAttribute("UsernameS", null);
                                    session.setAttribute("AlertAllTimeSUserlist", null);
                                    session.setAttribute("RefreshUserSess", null);
                                }
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
<!--                                    <div class="x_title">
                                        <h2><i class="fa fa-search"></i> <script>document.write(user_title_search);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <button type="button" class="btn btn-info" onClick="searchForm('<=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                <input id="btnNew" class="btn btn-info" type="button" onclick="NewCity();" />
                                            </li>
                                            <script>
                                                document.getElementById("btnNew").value = global_fm_button_New;
                                            </script>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>-->
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="form" method="post" class="form-horizontal">
                                            <script>
//                                                $(document).ready(function () {
//                                                    $('#idCheck').on('ifChanged', function () {
//                                                        if ($("#idCheck").is(':checked')) {
//                                                        document.getElementById("demo1").disabled = false;
//                                                        document.getElementById("demo2").disabled = false;
//                                                    }
//                                                    else
//                                                    {
//                                                        document.getElementById("demo1").disabled = true;
//                                                        document.getElementById("demo2").disabled = true;
//                                                    }
//                                                    });
//                                                });
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
                                                <!--onchange="checkboxChange();"--> 
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
                                                                value="<%= session.getAttribute("FromCreateDateSUserlist") != null && !"1".equals(session.getAttribute("AlertAllTimeSUserlist").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("FromCreateDateSUserlist").toString()) : com.ConvertMonthSub(30)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_ToDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo2" name="ToCreateDate" <%= session.getAttribute("AlertAllTimeSUserlist") != null && "1".equals(session.getAttribute("AlertAllTimeSUserlist").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("ToCreateDateSUserlist") != null && !"1".equals(session.getAttribute("AlertAllTimeSUserlist").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("ToCreateDateSUserlist").toString()) : com.ConvertMonthSub(0)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_email);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" id="RegistrationEmail" name="RegistrationEmail" maxlength="150" value="<%= session.getAttribute("RegistrationEmailS") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("RegistrationEmailS").toString()) : ""%>"
                                                                   class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_Username);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="Username" value="<%= session.getAttribute("UsernameS") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("UsernameS").toString()) : ""%>" maxlength="30" 
                                                                   class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;padding-right: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_role);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="UserRole" id="idUserRole" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("UserRoleS") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("UserRoleS").toString().trim()) ? "selected" : "" %>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    ROLE[][] rsUserRole = new ROLE[1][];
                                                                    try {
                                                                        db.S_BO_ROLE_COMBOBOX(sessLanguageGlobal, rsUserRole);
                                                                        if (rsUserRole[0].length > 0) {
                                                                            if (SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                                for (int i = 0; i < rsUserRole[0].length; i++) {
                                                                %>
                                                                <option value="<%=String.valueOf(rsUserRole[0][i].ID) %>" <%= session.getAttribute("UserRoleS") != null && String.valueOf(rsUserRole[0][i].ID).equals(session.getAttribute("UserRoleS").toString().trim()) ? "selected" : "" %>><%=rsUserRole[0][i].REMARK %></option>
                                                                <%
                                                                            }
                                                                        } else {
                                                                            for (int i = 0; i < rsUserRole[0].length; i++)
                                                                            {
                                                                                if(rsUserRole[0][i].CA_ENABLED == false)
                                                                                {
                                                                %>
                                                                <option value="<%=String.valueOf(rsUserRole[0][i].ID) %>" <%= session.getAttribute("UserRoleS") != null && String.valueOf(rsUserRole[0][i].ID).equals(session.getAttribute("UserRoleS").toString().trim()) ? "selected" : "" %>><%=rsUserRole[0][i].REMARK %></option>
                                                                <%
                                                                                }
                                                                            }
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
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_Status);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="USER_STATE" id="USER_STATE" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("USER_STATES") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("USER_STATES").toString().trim()) ? "selected" : "" %>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    USER_STATE[][] rsState = new USER_STATE[1][];
                                                                    try {
                                                                        db.S_BO_USER_STATE_COMBOBOX(sessLanguageGlobal, rsState);
                                                                        if (rsState[0].length > 0) {
                                                                            for (int i = 0; i < rsState[0].length; i++) {
                                                                                if(rsState[0][i].ID != Definitions.CONFIG_USER_STATE_CANCEL_ID)
                                                                                {
                                                                %>
                                                                <option value="<%=String.valueOf(rsState[0][i].ID) %>" <%= session.getAttribute("USER_STATES") != null && String.valueOf(rsState[0][i].ID).equals(session.getAttribute("USER_STATES").toString().trim()) ? "selected" : "" %>><%=rsState[0][i].REMARK %></option>
                                                                <%
                                                                            }
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
                                                    </div>
                                                </div>
                                                <%
//                                                    if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                                %>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_Branch);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="BranchOffice" id="BranchOffice" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("BranchOfficeSUserList") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("BranchOfficeSUserList").toString().trim()) ? "selected" : "" %>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
//                                                                    BRANCH[][] rsBranch = new BRANCH[1][];
                                                                    try {
//                                                                        db.S_BO_BRANCH_COMBOBOX(sessLanguageGlobal, rsBranch);
                                                                        String sSessTreeName = "sessTreeBranchSystem";
                                                                        if(sessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                            sSessTreeName = "sessTreeBranchSystemRoot";
                                                                        }
                                                                        BRANCH[][] rsBranch = (BRANCH[][]) session.getAttribute(sSessTreeName);
                                                                        if (rsBranch[0].length > 0) {
                                                                            for (BRANCH temp1 : rsBranch[0]) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("BranchOfficeSUserList") != null && String.valueOf(temp1.ID).equals(session.getAttribute("BranchOfficeSUserList").toString().trim()) ? "selected" : "" %>><%=temp1.NAME + " - " + temp1.REMARK %></option>
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
                                                    </div>
                                                </div>
                                                <%
                                                //} else {
                                                %>
                                                <!--<input type="text" style="display: none;" name="BranchOffice" id="BranchOffice" value="<= SessUserAgentID%>" class="form-control123"/>-->
                                                <%
                                                    //}
                                                %>
                                                <div class="col-sm-4" style="padding-left: 0;text-align: right;">
                                                    <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                    <input id="btnNew" class="btn btn-info" type="button" onclick="NewCity();" />
                                                    <script>
                                                        document.getElementById("btnNew").value = global_fm_button_New;
                                                        
                                                        
                                                    </script>
<!--                                                    <div class="col-sm-8" style="padding-right: 0px;">
                                                        
                                                    </div>
                                                    <div class="col-sm-4" style="padding-right: 0px;">
                                                        
                                                    </div>-->
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
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(user_title_table);</script></h2>
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
                                        </style>
                                        <div class="table-responsive">
                                            <table id="idTableList" class="table table-bordered table-striped projects">
                                                <thead>
                                                <th><script>document.write(global_fm_STT);</script></th>
                                                <th><script>document.write(global_fm_Username);</script></th>
                                                <th><script>document.write(global_fm_fullname);</script></th>
                                                <th><script>document.write(global_fm_role);</script></th>
                                                <th><script>document.write(global_fm_phone);</script></th>
                                                <th><script>document.write(global_fm_Branch);</script></th>
                                                <th><script>document.write(global_fm_Status);</script></th>
                                                <th><script>document.write(global_fm_date_create);</script></th>
                                                <th><script>document.write(global_fm_action);</script></th>
                                                </thead>
                                                <tbody>
                                                    <%
                                                        if (iPaNoSS > 1) {
                                                            j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                        }
                                                        session.setAttribute("NumPageSessSearchUser", String.valueOf(iPaNoSS));
                                                        if (rsPgin[0].length > 0) {
                                                            for (BACKOFFICE_USER temp1 : rsPgin[0]) {
                                                                String strID = String.valueOf(temp1.ID);
                                                                if (session.getAttribute("IDMAXUser") == null) {
                                                                    session.setAttribute("IDMAXUser", strID);
                                                                }
                                                    %>
                                                    <tr>
                                                        <td><%= com.convertMoney(j)%></td>
                                                        <td><%= temp1.USERNAME%></td>
                                                        <td><%= temp1.FULL_NAME%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.ROLE_REMARK)%></td>
                                                        <td><%= temp1.MSISDN %></td>
                                                        <td><%= temp1.BRANCH_NAME + " - " + EscapeUtils.CheckTextNull(temp1.BRANCH_REMARK)%></td>
                                                        <td>
                                                            <%= EscapeUtils.CheckTextNull(temp1.USER_STATE_DESC)%>
                                                        </td>
                                                        <td><%= temp1.CREATED_DT%></td>
                                                        <td><a style="cursor: pointer;" onclick="popupEdit('<%= strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_button_grid_edit);</script> </a></td>
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
                                <div class="x_panel">
                                    <div class="x_content" style="text-align: center;">
                                        <span style="color: red; font-size: 15px;"><script>document.write(global_succ_NoResult);</script></span>
                                        <div class="clearfix"></div>
                                    </div>
                                </div>
                                <%
                                    }
                                %>
                            </div>
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
                    <script>
                        $(document).ready(function () {
                            if(localStorage.getItem("EDIT_USER") !== null && localStorage.getItem("EDIT_USER") !== "null")
                            {
                                var vIDEDIT_USER = localStorage.getItem("EDIT_USER");
                                localStorage.setItem("EDIT_USER", null);
                                popupEdit(vIDEDIT_USER);
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
            <!--<script src="../style/icheck/icheck.min.js"></script>-->
            <script src="../js/daterangepicker.js"></script>
<!--            <script src="../js/active/highlight.js"></script>
            <script src="../js/active/bootstrap-switch.js"></script>
            <script src="../js/active/main.js"></script>-->
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