<%-- 
    Document   : TokenTransfer
    Created on : Jul 25, 2018, 4:08:04 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
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
        <link href="../style/switchery/dist/switchery.min.css" rel="stylesheet">
        <!--<link href="../Css/active/bootstrap-switch.css" rel="stylesheet">-->
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
            document.title = tokentransfer_title_list;
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
                var sSessIsDate = $("#idSessIsDate").val();
                if (sSessIsDate === "3"){
                    var idFromCreateDate = $("#FromDateValid").val();
                    var idToCreateDate = $("#ToDateValid").val();
                    if (parseDate(idToCreateDate).getTime() < parseDate(idFromCreateDate).getTime())
                    {
                        funErrorAlert(global_check_datesearch);
                        return false;
                    }
                }
                else
                {
                    if (sSessIsDate === "1")
                    {
                    } else
                    {
                        var idFromCreateDate = $("#FromDateValid").val();
                        var idToCreateDate = $("#ToDateValid").val();
                        if (parseDate(idToCreateDate).getTime() < parseDate(idFromCreateDate).getTime())
                        {
                            funErrorAlert(global_check_datesearch);
                            return false;
                        }
                    }
                }
                document.getElementById("idHiddenLoad").value = JS_STR_GRID_SEARCH_RESET;
                document.getElementById("tempCsrfToken").value = id;
                localStorage.setItem("CountCheckImport", "");
                localStorage.setItem("CountCheckAllImport", "");
                var f = document.form;
                f.method = "post";
                f.action = '';
                f.submit();
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
                padding:10px 17px 0 17px;
            }
            .x_content {
                padding: 0 5px 0 5px;
            }
        </style>
    </head>
    <body class="nav-md">
        <%
            if ((session.getAttribute("sUserID")) != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
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
                        document.getElementById("idNameURL").innerHTML = tokentransfer_title_list;
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
                                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                String sessTreeArrayBranchID = session.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                try {
                                    TOKEN[][] rsPgin = new TOKEN[1][];
                                    if (session.getAttribute("RefreshTokenImportSess") != null) {
                                        session.setAttribute("RefreshTokenImportSessPaging", "1");
                                        session.setAttribute("SearchSharePagingTokenImport", "0");
                                        statusLoad = 1;
                                        String idSessIsDate = (String) session.getAttribute("idSessIsDateSImport");
                                        String idSessIsToken = (String) session.getAttribute("idSessIsTokenSImport");
                                        String FromDateValid = (String) session.getAttribute("sessFromDateValidImport");
                                        String ToDateValid = (String) session.getAttribute("sessToDateValidImport");
                                        String FromTOKEN_ID = (String) session.getAttribute("sessFromTOKEN_IDImport");
                                        String ToTOKEN_ID = (String) session.getAttribute("sessToTOKEN_IDImport");
                                        String AGENT_ID = (String) session.getAttribute("sessAGENT_IDImport");
                                        session.setAttribute("idSessIsDateSImport", idSessIsDate);
                                        session.setAttribute("idSessIsTokenSImport", idSessIsToken);
                                        session.setAttribute("sessFromDateValidImport", FromDateValid);
                                        session.setAttribute("sessToDateValidImport", ToDateValid);
                                        session.setAttribute("sessFromTOKEN_IDImport", FromTOKEN_ID);
                                        session.setAttribute("sessToTOKEN_IDImport", ToTOKEN_ID);
                                        session.setAttribute("sessAGENT_IDImport", AGENT_ID);
                                        session.setAttribute("RefreshTokenImportSess", null);
                                        if (!Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                            AGENT_ID = SessUserAgentID;
                                        } else {
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(AGENT_ID)) {
                                                AGENT_ID = "";
                                            }
                                        }
                                        if (!"1".equals(idSessIsDate)) {
                                            FromDateValid = "";
                                            ToDateValid = "";
                                        }
                                        if (!"1".equals(idSessIsToken)) {
                                            FromTOKEN_ID = "";
                                            ToTOKEN_ID = "";
                                        }
                                        if(SessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            sessTreeArrayBranchID = "";
                                        }
                                        int ss = 0;
                                        ss = db.S_BO_TOKEN_IMPORT_TOTAL(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
                                            EscapeUtils.escapeHtmlSearch(FromTOKEN_ID),
                                            EscapeUtils.escapeHtmlSearch(ToTOKEN_ID), EscapeUtils.escapeHtmlSearch(AGENT_ID), sessTreeArrayBranchID);
                                        if (session.getAttribute("SearchIPageNoPagingTokenImport") != null) {
                                            String sPage = (String) session.getAttribute("SearchIPageNoPagingTokenImport");
                                            iPagNo = Integer.parseInt(sPage);
                                        }
                                        if (session.getAttribute("SearchISwRwsPagingTokenImport") != null) {
                                            String sSumPage = (String) session.getAttribute("SearchISwRwsPagingTokenImport");
                                            iSwRws = Integer.parseInt(sSumPage);
                                        }
                                        if (session.getAttribute("RefreshTokenImportSessNumberPaging") != null) {
                                            String sNoPage = (String) session.getAttribute("RefreshTokenImportSessNumberPaging");
                                            iPaNoSS = Integer.parseInt(sNoPage);
                                        }
                                        session.setAttribute("SearchIPageNoPagingTokenImport", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingTokenImport", String.valueOf(iSwRws));
                                        if (ss > 0) {
                                            db.S_BO_TOKEN_IMPORT_LIST(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
                                                EscapeUtils.escapeHtmlSearch(FromTOKEN_ID),
                                                EscapeUtils.escapeHtmlSearch(ToTOKEN_ID), EscapeUtils.escapeHtmlSearch(AGENT_ID),
                                                sessLanguageGlobal, rsPgin, iPagNo, iSwRws, sessTreeArrayBranchID);
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
                                        session.setAttribute("RefreshTokenImportSessPaging", null);
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("SearchShareStoreTokenImport", null);
                                            session.setAttribute("SearchIPageNoPagingTokenImport", null);
                                            session.setAttribute("SearchISwRwsPagingTokenImport", null);
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
                                    String FromTOKEN_ID = request.getParameter("FromTOKEN_ID");
                                    String ToTOKEN_ID = request.getParameter("ToTOKEN_ID");
                                    String AGENT_ID = request.getParameter("AGENT_ID");
                                    String idSessIsDate = request.getParameter("idSessDateTransfer");
                                    String idSessIsToken = request.getParameter("idSessTokenTransfer");
                                    String FromDateValid = request.getParameter("FromDateValid");
                                    String ToDateValid = request.getParameter("ToDateValid");
                                    if ("1".equals(hasPaging)) {
                                        session.setAttribute("SearchSharePagingImport", "0");
                                        idSessIsDate = (String) session.getAttribute("idSessIsDateSImport");
                                        idSessIsToken = (String) session.getAttribute("idSessIsTokenSImport");
                                        FromDateValid = (String) session.getAttribute("sessFromDateValidImport");
                                        ToDateValid = (String) session.getAttribute("sessToDateValidImport");
                                        FromTOKEN_ID = (String) session.getAttribute("sessFromTOKEN_IDImport");
                                        ToTOKEN_ID = (String) session.getAttribute("sessToTOKEN_IDImport");
                                        AGENT_ID = (String) session.getAttribute("sessAGENT_IDImport");
                                        session.setAttribute("SessParamOnPagingImport", null);
                                    } else {
                                        session.setAttribute("SearchSharePagingImport", "1");
                                        session.setAttribute("CountListTokenImport", null);
                                    }
                                    session.setAttribute("idSessIsDateSImport", idSessIsDate);
                                    session.setAttribute("idSessIsTokenSImport", idSessIsToken);
                                    session.setAttribute("sessFromDateValidImport", FromDateValid);
                                    session.setAttribute("sessToDateValidImport", ToDateValid);
                                    session.setAttribute("sessFromTOKEN_IDImport", FromTOKEN_ID);
                                    session.setAttribute("sessToTOKEN_IDImport", ToTOKEN_ID);
                                    session.setAttribute("sessAGENT_IDImport", AGENT_ID);
                                    if (!Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                        AGENT_ID = SessUserAgentID;
                                    } else {
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(AGENT_ID)) {
                                            AGENT_ID = "";
                                        }
                                    }
                                    if (!"1".equals(idSessIsDate)) {
                                        FromDateValid = "";
                                        ToDateValid = "";
                                    }
                                    if (!"1".equals(idSessIsToken)) {
                                        FromTOKEN_ID = "";
                                        ToTOKEN_ID = "";
                                    }
                                    if(SessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                        sessTreeArrayBranchID = "";
                                    }
                                    int ss = 0;
                                    if ((session.getAttribute("CountListTokenImport")) == null) {
                                        ss = db.S_BO_TOKEN_IMPORT_TOTAL(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
                                            EscapeUtils.escapeHtmlSearch(FromTOKEN_ID),
                                            EscapeUtils.escapeHtmlSearch(ToTOKEN_ID), EscapeUtils.escapeHtmlSearch(AGENT_ID), sessTreeArrayBranchID);
                                        session.setAttribute("CountListTokenImport", String.valueOf(ss));
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListTokenImport");
                                        ss = Integer.parseInt(sCount);
                                        session.setAttribute("CountListTokenImport", String.valueOf(ss));
                                    }
                                    iTotRslts = ss;
                                    if (iTotRslts > 0) {
                                        db.S_BO_TOKEN_IMPORT_LIST(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
                                            EscapeUtils.escapeHtmlSearch(FromTOKEN_ID),
                                            EscapeUtils.escapeHtmlSearch(ToTOKEN_ID), EscapeUtils.escapeHtmlSearch(AGENT_ID),
                                            sessLanguageGlobal, rsPgin, iPagNo, iSwRws, sessTreeArrayBranchID);
                                        session.setAttribute("SearchIPageNoPagingTokenImport", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingTokenImport", String.valueOf(iSwRws));
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
                                    session.setAttribute("RefreshTokenImportSessPaging", null);
                                    session.setAttribute("SearchShareStoreTokenImport", null);
                                    session.setAttribute("SearchIPageNoPagingTokenImport", null);
                                    session.setAttribute("SearchISwRwsPagingTokenImport", null);
                                    session.setAttribute("idSessIsDateSImport", null);
                                    session.setAttribute("sessFromDateValidImport", null);
                                    session.setAttribute("sessToDateValidImport", null);
                                    session.setAttribute("sessFromTOKEN_IDImport", null);
                                    session.setAttribute("sessToTOKEN_IDImport", null);
                                    session.setAttribute("sessAGENT_IDImport", "2");
                                    %>
                                    <script>
                                        $(document).ready(function () {
                                            localStorage.setItem('StoreIsDateImport', '');
                                        });
                                    </script>
                                    <%
                                }
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
<!--                                    <div class="x_title">
                                        <h2>
                                            <i class="fa fa-search"></i> <script>document.write(tokentransfer_title_search);</script>
                                        </h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>-->
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="form" method="post" class="form-horizontal">
                                            <input type="text" style="display: none;" id="idSessTokenTransfer" name="idSessTokenTransfer" />
                                            <input type="text" style="display: none;" id="idSessDateTransfer" name="idSessDateTransfer" />       
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <div class="col-sm-5" style="padding-right: 0px;text-align: right;">
                                                            <label class="switch" for="CheckDate">
                                                                <input TYPE="checkbox" id="CheckDate" name="CheckDate" onchange="clickCheckChooseDate();" />
                                                                <div class="slider round"></div>
                                                            </label>
                                                        </div>
                                                        <label class="control-label col-sm-7" style="color: #000000; font-weight: bold;text-align: left;"><script>document.write(global_fm_check_date);</script></label>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_FromDate);</script></label>
                                                        <div class="col-sm-6" style="padding-right: 0px;">
                                                            <input type="Text" id="FromDateValid" name="FromDateValid" maxlength="25" class="form-control123"
                                                                value="<%= session.getAttribute("sessFromDateValidImport") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromDateValidImport").toString()) : com.ConvertMonthSub(30)%>"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_ToDate);</script></label>
                                                        <div class="col-sm-6" style="padding-right: 0px;">
                                                            <input type="Text" id="ToDateValid" name="ToDateValid" maxlength="25" class="form-control123"
                                                                value="<%= session.getAttribute("sessToDateValidImport") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToDateValidImport").toString()) : com.ConvertMonthSub(0)%>"/>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                                        
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <div class="col-sm-5" style="padding-right: 0px;text-align: right;">
                                                            <label class="switch" for="CheckToken">
                                                                <input TYPE="checkbox" id="CheckToken" name="CheckToken" onchange="clickCheckChooseToken();" />
                                                                <div class="slider round"></div>
                                                            </label>
                                                        </div>
                                                        <label class="control-label col-sm-7" style="color: #000000; font-weight: bold;text-align: left;"><script>document.write(global_fm_check_token);</script></label>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(tokenimport_fm_fromtokenSN);</script></label>
                                                        <div class="col-sm-6" style="padding-right: 0px;">
                                                            <input type="Text" id="FromTOKEN_ID" name="FromTOKEN_ID" maxlength="40" class="form-control123"
                                                                            value="<%= session.getAttribute("sessFromTOKEN_IDImport") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromTOKEN_IDImport").toString()) : ""%>"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(tokenimport_fm_totokenSN);</script></label>
                                                        <div class="col-sm-6" style="padding-right: 0px;">
                                                            <input type="Text" id="ToTOKEN_ID" name="ToTOKEN_ID" maxlength="40" class="form-control123"
                                                                value="<%= session.getAttribute("sessToTOKEN_IDImport") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToTOKEN_IDImport").toString()) : ""%>"/>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <%
                                                            if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                                        %>
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(token_fm_agent);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="AGENT_ID" id="AGENT_ID" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessAGENT_IDImport") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessAGENT_IDImport").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
//                                                                    BRANCH[][] rsBranch = new BRANCH[1][];
//                                                                    db.S_BO_BRANCH_COMBOBOX(sessLanguageGlobal, rsBranch);
                                                                    BRANCH[][] rsBranch = (BRANCH[][]) session.getAttribute("sessTreeBranchSystemRoot");
                                                                    if (rsBranch[0].length > 0) {
                                                                        for (BRANCH temp1 : rsBranch[0]) {
                                                                    %>
                                                                    <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessAGENT_IDImport") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessAGENT_IDImport").toString()) ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                                    <%
                                                                            }
                                                                        }
                                                                    %>
                                                            </select>
                                                        </div>
                                                        <%
                                                            } else {
                                                        %>
                                                        <input type="text" style="display: none;" name="AGENT_ID" id="AGENT_ID" value="<%= SessUserAgentID%>" class="form-control123"/>
                                                        <%
                                                            }
                                                        %>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: right;"></label>
                                                        <div class="col-sm-6" style="padding-right: 0px;text-align: left;">
                                                            <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                    </div>
                                                </div>
                                            </div>
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                            <input id="idHiddenLoad" name="nameHiddenLoad" value="" type="hidden"/>
                                            <script>
                                                function clickCheckChooseDate()
                                                {
                                                    if ($("#CheckDate").is(':checked'))
                                                    {
                                                        localStorage.setItem("StoreIsDateTransfer", "1");
                                                        document.getElementById("idSessDateTransfer").value = "1";
                                                        document.getElementById("FromDateValid").disabled = false;
                                                        document.getElementById("ToDateValid").disabled = false;
                                                    } else {
                                                        localStorage.setItem("StoreIsDateTransfer", null);
                                                        document.getElementById("idSessDateTransfer").value = "";
                                                        document.getElementById("FromDateValid").disabled = true;
                                                        document.getElementById("ToDateValid").disabled = true;
                                                    }
                                                }
                                                function clickCheckChooseToken()
                                                {
                                                    if ($("#CheckToken").is(':checked'))
                                                    {
                                                        localStorage.setItem("StoreIsTokenTransfer", "1");
                                                        document.getElementById("idSessTokenTransfer").value = "1";
                                                        document.getElementById("FromTOKEN_ID").disabled = false;
                                                        document.getElementById("ToTOKEN_ID").disabled = false;
                                                    }
                                                    else
                                                    {
                                                        localStorage.setItem("StoreIsTokenTransfer", null);
                                                        document.getElementById("idSessTokenTransfer").value = "";
                                                        document.getElementById("FromTOKEN_ID").disabled = true;
                                                        document.getElementById("ToTOKEN_ID").disabled = true;
                                                    }
                                                }
                                                $(document).ready(function () {
                                                    var sSessIsDate = localStorage.getItem('StoreIsDateTransfer');
                                                    if (sSessIsDate === '1')
                                                    {
                                                        document.getElementById("idSessDateTransfer").value = "1";
                                                        document.getElementById("FromDateValid").disabled = false;
                                                        document.getElementById("ToDateValid").disabled = false;
                                                        $('#CheckDate').prop('checked', true);
                                                    } else{
                                                        document.getElementById("idSessDateTransfer").value = "";
                                                        document.getElementById("FromDateValid").disabled = true;
                                                        document.getElementById("ToDateValid").disabled = true;
                                                        $('#CheckDate').prop('checked', false);
                                                    }
                                                    var sSessIsToken = localStorage.getItem('StoreIsTokenTransfer');
                                                    if (sSessIsToken === '1')
                                                    {
                                                        document.getElementById("idSessTokenTransfer").value = "1";
                                                        document.getElementById("FromTOKEN_ID").disabled = false;
                                                        document.getElementById("ToTOKEN_ID").disabled = false;
                                                        $('#CheckToken').prop('checked', true);
                                                    }
                                                    else {
                                                        document.getElementById("idSessTokenTransfer").value = "";
                                                        document.getElementById("FromTOKEN_ID").disabled = true;
                                                        document.getElementById("ToTOKEN_ID").disabled = true;
                                                        $('#CheckToken').prop('checked', false);
                                                    }

                                                    $('#FromDateValid').daterangepicker({
                                                        singleDatePicker: true,
                                                        showDropdowns: true
                                                    }, function (start, end, label) {
                                                        console.log(start.toISOString(), end.toISOString(), label);
                                                    });
                                                    $('#ToDateValid').daterangepicker({
                                                        singleDatePicker: true,
                                                        showDropdowns: true
                                                    }, function (start, end, label) {
                                                        console.log(start.toISOString(), end.toISOString(), label);
                                                    });
                                                    $(".loading-gif").hide();
                                                });
                                            </script>
                                        </form>
                                    </div>
                                </div>
                                <%
                                    if (status == 1 && statusLoad == 1) {
                                %>
                                <div class="x_panel" id="divEditTokenMulti" style="padding-bottom: 15px;">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(tokenimport_title_multi);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content" style="margin-top: 0px;">
<!--                                        <fieldset class="scheduler-border">
                                            <legend class="scheduler-border"><script>document.write(tokenimport_title_multi);</script></legend>-->
                                            <div class="col-sm-13" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-1" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;padding-top: 5px;"><script>document.write(token_fm_agent);</script></label>
                                                    <div class="col-sm-11" style="padding-right: 0px;">
                                                        <div style="float: left;width: 91%;">
                                                            <select name="AGENT_IDMulti" id="AGENT_IDMulti" class="form-control123">
                                                                <%
//                                                                    BRANCH[][] rst = new BRANCH[1][];
//                                                                    db.S_BO_BRANCH_COMBOBOX(sessLanguageGlobal, rst);
                                                                    BRANCH[][] rst = (BRANCH[][]) session.getAttribute("sessTreeBranchSystemAgency");
                                                                    if (rst[0].length > 0) {
                                                                        for (BRANCH temp1 : rst[0]) {
                                                                            if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT))
                                                                            {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>"><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                                <%
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                        <div style="float: right;text-align: right;">
                                                            <button type="button" class="btn btn-info" onClick="editMultiple('<%=anticsrf%>');"><script>document.write(global_fm_button_add);</script></button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
<!--                                            <div class="form-group" style="padding: 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write();</script></label>
                                                <div style="width: 100%;padding-top: 5px;clear: both;">
                                                    
                                                </div>
                                            </div>-->
                                        <!--</fieldset>-->
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
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(tokenimport_title_table);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <script>document.write(global_label_grid_sum);</script><%= strMess%>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <input type="hidden" name="iPagNo" value="<%=iPagNo%>">
                                        <input type="hidden" name="cPagNo" value="<%=cPagNo%>">
                                        <input type="hidden" name="iSwRws" value="<%=iSwRws%>">
                                        <input id="idValueCheck" name="idValueCheck" type="hidden" />
                                        <style type="text/css">
                                            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                            .table > thead > tr > th{border-bottom: none;}
                                            .btn{margin-bottom: 0px;}
                                        </style>
                                        <script>
                                            function editMultiple(idCSRF)
                                            {
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
                                                    var sCheckCheckAll = "0";
                                                    if ($("#checkAll").is(':checked')) {
                                                        sCheckCheckAll = "1";
                                                    }
                                                    if(sCheckCheckAll === "0")
                                                    {
                                                        if(localStorage.getItem("CountCheckImport") === "")
                                                        {
                                                            funErrorAlert(global_succ_NoCheck_add_renewal);
                                                            return false;
                                                        }
                                                    }
                                                    $('body').append('<div id="over"></div>');
                                                    $(".loading-gif").show();
                                                    setTimeout(function () {
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../TokenCommon",
                                                            data: {
                                                                idParam: "edittokenmultipleagent",
                                                                idValue: localStorage.getItem("CountCheckImport"),
                                                                isCheckAll: sCheckCheckAll,
                                                                isAGENT_IDMulti: $("#AGENT_IDMulti").val(),
                                                                CsrfToken: idCSRF
                                                            },
                                                            catche: false,
                                                            success: function (html) {
                                                                var arr = sSpace(html).split('#');
                                                                if (arr[0] === "0")
                                                                {
                                                                    localStorage.setItem("CountCheckImport", "");
                                                                    localStorage.setItem("CountCheckAllImport", "");
                                                                    funSuccAlert(token_succ_edit, "TokenTransfer.jsp");
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
                                                if(localStorage.getItem("CountCheckAllImport") === "1")
                                                {
                                                    $('tbody tr td input[type="checkbox"]').prop('checked', true);
                                                    $('tbody tr').css('background', '#d8d8d8');
                                                    localStorage.setItem("CountCheckAllImport", "1");
                                                    $('#checkAll').prop('checked', true);
                                                    CheckAllTableValue();
                                                    $("#divEditTokenMulti").css("display", "");
                                                }
                                                else
                                                {
                                                    $('tbody tr').css('background', '');
                                                    localStorage.setItem("CountCheckAllImport", "");
                                                    $('#checkAll').prop('checked', false);
                                                }
                                                if(localStorage.getItem("CountCheckImport") !== "")
                                                {
                                                    ForCheckTable(localStorage.getItem("CountCheckImport"));
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
                                                    var sValueCheck = localStorage.getItem("CountCheckImport");
                                                    var ischecked = $(this).is(':checked');
                                                    if (!ischecked) {
                                                        $(this).closest('tr').css('background', '');
                                                        var sss = $(this).closest('tr').find("td:eq(6)").text() + ",";
                                                        sValueCheck = sValueCheck.replace(sss, "");
                                                        $('#checkAll').prop('checked', false);
                                                        localStorage.setItem("CountCheckAllImport", "");
                                                    }
                                                    else
                                                    {
                                                        $(this).closest('tr').css('background', '#d8d8d8');
                                                        var sCheck = $(this).closest('tr').find("td:eq(6)").text() + ",";
                                                        if(sValueCheck.indexOf(sCheck) === -1)
                                                        {
                                                            sValueCheck = sValueCheck + sCheck;
                                                        }
                                                        $("#divEditTokenMulti").css("display", "");
                                                    }
                                                    localStorage.setItem("CountCheckImport", sValueCheck);
                                                });
                                            });
                                            function ForCheckTable(sValueCheck)
                                            {
                                                $('#mtToken').find('tr').each(function () {
                                                    var sCheck = sSpace($(this).find('td:eq(6)').text());
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
                                                var currentCheck = localStorage.getItem("CountCheckImport");
                                                $('#mtToken').find('tr').each(function () {
                                                    var sCheck = $(this).find('td:eq(6)').text();
                                                    if(sCheck !== "" && currentCheck.indexOf(sCheck + ',') === -1)
                                                    {
                                                        currentCheck = currentCheck + sCheck + ",";
                                                    }
                                                });
                                                localStorage.setItem("CountCheckImport", currentCheck);
                                            }
                                            function checkAllTable()
                                            {
                                                $('#checkAll').change(function () {
                                                    $('tbody tr td input[type="checkbox"]').prop('checked', $(this).prop('checked'));
                                                    $('tbody tr').css('background', '#d8d8d8');
                                                    if ($("#checkAll").is(':checked')) {
                                                        CheckAllTableValue();
                                                        localStorage.setItem("CountCheckAllImport", "1");
                                                        $("#divEditTokenMulti").css("display", "");
                                                    }
                                                    else
                                                    {
                                                        localStorage.setItem("CountCheckImport", "");
                                                        localStorage.setItem("CountCheckAllImport", "");
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
                                            function SplitCheckUnTable(sValueCheck, keyCheck)
                                            {
                                                var vResult = "0";
                                                if(sValueCheck !== "")
                                                {
                                                    var intForCheck = sValueCheck.split(',');
                                                    for(j = 0; j< intForCheck.length; j++)
                                                    {
                                                        if(intForCheck[j] === keyCheck)
                                                        {
                                                            vResult = "1";
                                                            break;
                                                        }
                                                    }
                                                }
                                                return vResult;
                                            }
                                        </script>
                                        <div class="table-responsive">
                                        <table class="table table-bordered table-striped projects" id="mtToken">
                                            <thead>
                                            <th><script>document.write(global_fm_STT);</script></th>
                                            <th><script>document.write(token_fm_tokenid);</script></th>
                                            <th><script>document.write(global_fm_Status);</script></th>
                                            <th><script>document.write(token_fm_agent);</script></th>
                                            <th><script>document.write(global_fm_date_create);</script></th>
                                            <th><input id="checkAll" name="checkAll" onclick="checkAllTable();" type="checkbox"/></th>
                                            </thead>
                                            <tbody>
                                                <%
                                                    if (iPaNoSS > 1) {
                                                        j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                    }
                                                    session.setAttribute("RefreshTokenImportSessNumberPaging", String.valueOf(iPaNoSS));
                                                    if (rsPgin[0].length > 0) {
                                                        for (TOKEN temp1 : rsPgin[0]) {
                                                            String strID = String.valueOf(temp1.ID);
                                                            String strState = EscapeUtils.CheckTextNull(temp1.TOKEN_STATE_DESC);
                                                %>
                                                <tr>
                                                    <td><%= com.convertMoney(j)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.TOKEN_SN)%></td>
                                                    <td><%= strState%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.BRANCH_DESC)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_DT)%></td>
                                                    <td>
                                                        <input id="checkChilren<%= Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX + strID%>" value="value-<%= strID%>" name="checkChilren" class='uncheck' type="checkbox"/>
                                                    </td>
                                                    <td style="display: none;"><%= Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX + strID%></td>
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
                                                    <td colspan="8" style="text-align: right;">
                                                        <div class="paging_table">
                                                            <%
                                                                int i = 0;
                                                                int cPge = 0;

                                                                if (iTotRslts > iSwRws) {
                                                                    cPge = ((int) (Math.ceil((double) iEnRsNo / (iTotSrhRcrds * iSwRws))));
                                                                    int prePageNo = (cPge * iTotSrhRcrds) - ((iTotSrhRcrds - 1) + iTotSrhRcrds);
                                                                    if ((cPge * iTotSrhRcrds) - (iTotSrhRcrds) > 0) {
                                                            %>
                                                            <a href="TokenTransfer.jsp?iPagNo=<%=prePageNo%>&cPagNo=<%=prePageNo%>"><< <script>document.write(global_paging_Before);</script></a>
                                                            &nbsp;
                                                            <%
                                                                }
                                                                for (i = ((cPge * iTotSrhRcrds) - (iTotSrhRcrds - 1)); i <= (cPge * iTotSrhRcrds); i++) {
                                                                    if (i == ((iPagNo / iSwRws) + 1)) {
                                                            %>
                                                            <a href="TokenTransfer.jsp?iPagNo=<%=i%>" style="cursor:pointer;color:red;"><b><%=i%></b></a>
                                                                    <%                   } else if (i <= iTotPags) {
                                                                    %>
                                                            &nbsp;<a href="TokenTransfer.jsp?iPagNo=<%=i%>"><%=i%></a>
                                                            <%
                                                                    }
                                                                }

                                                                if (iTotPags > iTotSrhRcrds && i <= iTotPags) {
                                                            %>
                                                            &nbsp;
                                                            <a href="TokenTransfer.jsp?iPagNo=<%=i%>&cPagNo=<%=i%>">>> <script>document.write(global_paging_last);</script></a>
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
                                        <span style="color: red; font-size: 15px;padding-bottom: 10px;"><script>document.write(global_succ_NoResult);</script></span>
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
            <script src="../style/switchery/dist/switchery.min.js"></script>
            <script src="../js/daterangepicker.js"></script>
<!--        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/bootstrap-switch.js"></script>
        <script src="../js/active/main.js"></script>-->
        </div>
        <%        } else {
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
