<%-- 
    Document   : CertProfileList
    Created on : Jul 25, 2018, 5:01:24 PM
    Author     : THANH-PC
--%>

<%@page import="java.net.URLEncoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<%  response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
    Config conf = new Config();
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
        <%
            String sNewRefreshJS = conf.GetPropertybyCode(Definitions.CONFIG_JS_REFRESH_STRING_RANDOM);
        %>
        <script src="../js/Language.js?t=<%=sNewRefreshJS%>"></script>
        <script src="../js/process_javajs.js?t=<%=sNewRefreshJS%>"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script language="javascript">
            changeFavicon("../");
            document.title = certprofile_title_list;
            $(document).ready(function () {
                localStorage.setItem("LOCAL_PARAM_CERTPROFILELIST", null);
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
//            function popupEdit(id)
//            {
//                localStorage.setItem("LOCAL_PARAM_CERTPROFILELIST", id);
//                window.location = 'CertProfileEdit.jsp?id=' + id;
//            }
            function addForm() {
                window.location = 'CertProfileNew.jsp';
            }
            function searchForm(id)
            {
                    document.getElementById("idHiddenLoad").value = JS_STR_GRID_SEARCH_RESET;
                    document.getElementById("tempCsrfToken").value = id;
                    var f = document.myform;
                    f.method = "post";
                    f.action = '';
                    f.submit();
            }
        </script>
        <style>.projects th{font-weight: bold;}</style>
    </head>
    <body class="nav-md">
        <%
            if (session.getAttribute("sUserID") != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
//                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
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
                        document.getElementById("idNameURL").innerHTML = certprofile_title_list;
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
                                    CERTIFICATION_PROFILE[][] rsPgin = new CERTIFICATION_PROFILE[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (session.getAttribute("RefreshCertProfileSess") != null && session.getAttribute("sessCERTIFICATION_AUTHORITYCertProfile") != null
                                            && session.getAttribute("sessCERTIFICATION_PURPOSECertProfile") != null) {
                                        session.setAttribute("RefreshCertProfileSessPaging", "1");
                                        session.setAttribute("SearchSharePagingCertProfile", "0");
                                        statusLoad = 1;
                                        String CERTIFICATION_AUTHORITY = (String) session.getAttribute("sessCERTIFICATION_AUTHORITYCertProfile");
                                        String CERTIFICATION_PURPOSE = (String) session.getAttribute("sessCERTIFICATION_PURPOSECertProfile");
                                        String ENABLED = (String) session.getAttribute("sessENABLEDCertProfile");
                                        String idCheckISSUE_ENABLED = (String) session.getAttribute("sessISSUE_ENABLEDCertProfile");
                                        session.setAttribute("RefreshCertProfileSess", null);
                                        session.setAttribute("sessCERTIFICATION_AUTHORITYCertProfile", CERTIFICATION_AUTHORITY);
                                        session.setAttribute("sessCERTIFICATION_PURPOSECertProfile", CERTIFICATION_PURPOSE);
                                        session.setAttribute("sessENABLEDCertProfile", ENABLED);
                                        session.setAttribute("sessISSUE_ENABLEDCertProfile", idCheckISSUE_ENABLED);
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_AUTHORITY)) {
                                            CERTIFICATION_AUTHORITY = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_PURPOSE)) {
                                            CERTIFICATION_PURPOSE = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(ENABLED)) {
                                            ENABLED = "";
                                        }
                                        if (Definitions.CONFIG_GRID_STRING_TRUE.equals(ENABLED)) {
                                            ENABLED = "1";
                                        }
                                        if (Definitions.CONFIG_GRID_STRING_FALSE.equals(ENABLED)) {
                                            ENABLED = "0";
                                        }
                                        int ss = 0;
                                        if ((session.getAttribute("CountListCertProfile")) == null) {
                                            ss = db.S_BO_CERTIFICATION_PROFILE_TOTAL(EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY),
                                                EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE), ENABLED, idCheckISSUE_ENABLED);
                                            session.setAttribute("CountListCertProfile", String.valueOf(ss));
                                        } else {
                                            String sCount = (String) session.getAttribute("CountListCertProfile");
                                            ss = Integer.parseInt(sCount);
                                            session.setAttribute("CountListCertProfile", String.valueOf(ss));
                                        }
                                        if (session.getAttribute("SearchIPageNoPagingCertProfile") != null) {
                                            String sPage = (String) session.getAttribute("SearchIPageNoPagingCertProfile");
                                            iPagNo = Integer.parseInt(sPage);
                                        }
                                        if (session.getAttribute("SearchISwRwsPagingCertProfile") != null) {
                                            String sSumPage = (String) session.getAttribute("SearchISwRwsPagingCertProfile");
                                            iSwRws = Integer.parseInt(sSumPage);
                                        }
                                        if (session.getAttribute("RefreshCertSessNumberPaging") != null) {
                                            String sNoPage = (String) session.getAttribute("RefreshCertSessNumberPaging");
                                            iPaNoSS = Integer.parseInt(sNoPage);
                                        }
                                        session.setAttribute("SearchIPageNoPagingCertProfile", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingCertProfile", String.valueOf(iSwRws));
                                        if (ss > 0) {
                                            db.S_BO_CERTIFICATION_PROFILE_LIST(EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY),
                                                EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE), ENABLED, idCheckISSUE_ENABLED,
                                                sessLanguageGlobal, iPagNo, iSwRws, rsPgin);
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
                                        session.setAttribute("RefreshCertProfileSessPaging", null);
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("SearchShareStoreCertProfile", null);
                                            session.setAttribute("SearchIPageNoPagingCertProfile", null);
                                            session.setAttribute("SearchISwRwsPagingCertProfile", null);
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
                                    String CERTIFICATION_AUTHORITY = request.getParameter("CERTIFICATION_AUTHORITY");
                                    String CERTIFICATION_PURPOSE = request.getParameter("CERTIFICATION_PURPOSE");
                                    String ENABLED = request.getParameter("ENABLED");
                                    String idCheckISSUE_ENABLED = "0";
                                    Boolean checkISSUE_ENABLED = Boolean.valueOf(request.getParameter("ISSUE_ENABLED") != null);
                                    if (checkISSUE_ENABLED == true) {
                                        idCheckISSUE_ENABLED = "1";
                                    }
                                    if ("1".equals(hasPaging)) {
                                        session.setAttribute("SearchSharePagingCertProfile", "0");
                                        CERTIFICATION_AUTHORITY = (String) session.getAttribute("sessCERTIFICATION_AUTHORITYCertProfile");
                                        CERTIFICATION_PURPOSE = (String) session.getAttribute("sessCERTIFICATION_PURPOSECertProfile");
                                        ENABLED = (String) session.getAttribute("sessENABLEDCertProfile");
                                        idCheckISSUE_ENABLED = (String) session.getAttribute("sessISSUE_ENABLEDCertProfile");
                                        session.setAttribute("SessParamOnPagingCertList", null);
                                    } else {
                                        session.setAttribute("SearchSharePagingCertProfile", "1");
                                        session.setAttribute("CountListCertProfile", null);
                                    }
                                    session.setAttribute("sessCERTIFICATION_AUTHORITYCertProfile", CERTIFICATION_AUTHORITY);
                                    session.setAttribute("sessCERTIFICATION_PURPOSECertProfile", CERTIFICATION_PURPOSE);
                                    session.setAttribute("sessENABLEDCertProfile", ENABLED);
                                    session.setAttribute("sessISSUE_ENABLEDCertProfile", idCheckISSUE_ENABLED);
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_AUTHORITY)) {
                                        CERTIFICATION_AUTHORITY = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_PURPOSE)) {
                                        CERTIFICATION_PURPOSE = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(ENABLED)) {
                                        ENABLED = "";
                                    }
                                    if (Definitions.CONFIG_GRID_STRING_TRUE.equals(ENABLED)) {
                                        ENABLED = "1";
                                    }
                                    if (Definitions.CONFIG_GRID_STRING_FALSE.equals(ENABLED)) {
                                        ENABLED = "0";
                                    }
                                    int ss = 0;
                                    if ((session.getAttribute("CountListCertProfile")) == null) {
                                        ss = db.S_BO_CERTIFICATION_PROFILE_TOTAL(EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY),
                                            EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE), ENABLED, idCheckISSUE_ENABLED);
                                        session.setAttribute("CountListCertProfile", String.valueOf(ss));
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListCertProfile");
                                        ss = Integer.parseInt(sCount);
                                        session.setAttribute("CountListCertProfile", String.valueOf(ss));
                                    }
                                    iTotRslts = ss;
                                    if (iTotRslts > 0) {
                                        db.S_BO_CERTIFICATION_PROFILE_LIST(EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY),
                                            EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE), ENABLED, idCheckISSUE_ENABLED,
                                            sessLanguageGlobal, iPagNo, iSwRws, rsPgin);
                                        session.setAttribute("SearchIPageNoPagingCertProfile", String.valueOf(iPagNo));
                                        session.setAttribute("SearchISwRwsPagingCertProfile", String.valueOf(iSwRws));
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
                                    session.setAttribute("RefreshCertProfileSessPaging", null);
                                    session.setAttribute("SearchShareStoreCertProfile", null);
                                    session.setAttribute("SearchIPageNoPagingCertProfile", null);
                                    session.setAttribute("SearchISwRwsPagingCertProfile", null);
                                    session.setAttribute("sessCERTIFICATION_AUTHORITYCertProfile", null);
                                    session.setAttribute("sessCERTIFICATION_PURPOSECertProfile", null);
                                    session.setAttribute("sessENABLEDCertProfile", Definitions.CONFIG_GRID_STRING_TRUE);
                                }
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2>
                                            <i class="fa fa-search"></i> <script>document.write(token_title_search);</script>
                                        </h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                <input id="btnNew" type="button" class="btn btn-info" onclick="addForm();"/>
                                                <script>
                                                    document.getElementById("btnNew").value = global_fm_button_New;
                                                </script>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="myform" method="post" class="form-horizontal">
                                            <input id="idHiddenLoad" name="nameHiddenLoad" value="" type="hidden"/>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;"><script>document.write(global_fm_ca);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="CERTIFICATION_AUTHORITY" id="CERTIFICATION_AUTHORITY" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCERTIFICATION_AUTHORITYCertProfile") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCERTIFICATION_AUTHORITYCertProfile").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    CERTIFICATION_AUTHORITY[][] rsBranch = new CERTIFICATION_AUTHORITY[1][];
                                                                    db.S_BO_CERTIFICATION_AUTHORITY_COMBOBOX(sessLanguageGlobal, rsBranch);
                                                                    if (rsBranch[0].length > 0) {
                                                                        for (CERTIFICATION_AUTHORITY temp1 : rsBranch[0]) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessCERTIFICATION_AUTHORITYCertProfile") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessCERTIFICATION_AUTHORITYCertProfile").toString().trim()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                                <%
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;"><script>document.write(global_fm_certpurpose);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="CERTIFICATION_PURPOSE" id="CERTIFICATION_PURPOSE" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCERTIFICATION_PURPOSECertProfile") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCERTIFICATION_PURPOSECertProfile").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    //CommonFunction.LogDebugString(null, "CERTIFICATION_PURPOSE - SESSION 2", session.getAttribute("sessCERTIFICATION_PURPOSECertProfile").toString());
                                                                    CERTIFICATION_PURPOSE[][] rsPurpose = new CERTIFICATION_PURPOSE[1][];
                                                                    db.S_BO_CERTIFICATION_PURPOSE_COMBOBOX(sessLanguageGlobal, rsPurpose);
                                                                    if (rsPurpose[0].length > 0) {
                                                                        for (CERTIFICATION_PURPOSE temp1 : rsPurpose[0]) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessCERTIFICATION_PURPOSECertProfile") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessCERTIFICATION_PURPOSECertProfile").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;"><script>document.write(global_fm_active);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="ENABLED" id="ENABLED" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessENABLEDCertProfile") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessENABLEDCertProfile").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <option value="<%= Definitions.CONFIG_GRID_STRING_TRUE%>" <%= session.getAttribute("sessENABLEDCertProfile") != null && Definitions.CONFIG_GRID_STRING_TRUE.equals(session.getAttribute("sessENABLEDCertProfile").toString()) ? "selected" : ""%>><script>document.write(global_fm_active_true);</script></option>
                                                                <option value="<%= Definitions.CONFIG_GRID_STRING_FALSE%>" <%= session.getAttribute("sessENABLEDCertProfile") != null && Definitions.CONFIG_GRID_STRING_FALSE.equals(session.getAttribute("sessENABLEDCertProfile").toString()) ? "selected" : ""%>><script>document.write(global_fm_active_false);</script></option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0; display: none;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleISSUE_ENABLEDSearch" class="control-label col-sm-5" style="color: #000000; font-weight: bold;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <label class="switch" for="ISSUE_ENABLED">
                                                                <input TYPE='checkbox' class='js-switch' data-switchery='true' name="ISSUE_ENABLED" id="ISSUE_ENABLED"
                                                                    <%= session.getAttribute("sessISSUE_ENABLEDCertProfile") != null && "1".equals(session.getAttribute("sessISSUE_ENABLEDCertProfile").toString()) ? "checked" : ""%>/>
                                                                <div class='slider round'></div>
                                                            </label>
                                                            <script>$("#idLblTitleISSUE_ENABLEDSearch").text(global_fm_renew_access_search);</script>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
<!--                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;text-align: center;">
                                                
                                            </div>-->
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
                                            <th><script>document.write(certprofile_fm_code);</script></th>
                                            <th><script>document.write(global_fm_ca);</script></th>
                                            <th><script>document.write(global_fm_certpurpose);</script></th>
                                            <th><script>document.write(global_fm_Description);</script></th>
                                            <th><script>document.write(global_fm_date_create);</script></th>
                                            <th><script>document.write(global_fm_active);</script></th>
                                            <th><script>document.write(global_fm_action);</script></th>
                                            </thead>
                                            <tbody>
                                            <%
                                                int j = 1;
                                                if (iPaNoSS > 1) {
                                                    j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                }
                                                session.setAttribute("NumPageSessListCertProfile", String.valueOf(iPaNoSS));
                                                if (rsPgin[0].length > 0) {
                                                    for (int i = 0; i < rsPgin[0].length; i++) {
                                                        String strID = String.valueOf(rsPgin[0][i].ID);
                                                        String strActiveFlag = rsPgin[0][i].ENABLED ? Definitions.CONFIG_GRID_ACTIVE_TRUE : Definitions.CONFIG_GRID_ACTIVE_FALSE;
                                            %>
                                            <tr>
                                                <td><%= com.convertMoney(j)%></td>
                                                <td><%= EscapeUtils.CheckTextNull(rsPgin[0][i].NAME)%></td>
                                                <td><%= EscapeUtils.CheckTextNull(rsPgin[0][i].CERTIFICATION_AUTHORITY_DESC)%></td>
                                                <td><%= EscapeUtils.CheckTextNull(rsPgin[0][i].CERTIFICATION_PURPOSE_DESC)%></td>
                                                <td><%= EscapeUtils.CheckTextNull(rsPgin[0][i].REMARK)%></td>
                                                <td><%= rsPgin[0][i].CREATED_DT %></td>
                                                <td>
                                                    <script>
                                                        if('<%= strActiveFlag%>' === JS_STR_ACTIVE_TRUE) {
                                                            document.write(global_fm_active_true);
                                                        } else {
                                                            document.write(global_fm_active_false);
                                                        }
                                                    </script>
                                                </td>
                                                <td>
                                                    <a style="cursor: pointer;" onclick="popupEdit('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_button_grid_edit);</script> </a>
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
                                <div id="contentEdit"></div>
                                <!-- Load Combobox Info Script -->
                                <script>
                                    function popupEdit(id)
                                    {
                                        $('body').append('<div id="over"></div>');
                                        $(".loading-gif").show();
                                        $('#contentEdit').load('CertProfileEdit.jsp', {id:id}, function () {
                                            goToByScroll("contentEdit");
                                        });
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                    }
                                </script>
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
//                        $(document).ready(function () {
//                            $("select#CERTIFICATION_AUTHORITY").prop('selectedIndex', 1);
//                        });
                        if(localStorage.getItem("EDIT_CERTPROFILE") !== null && localStorage.getItem("EDIT_CERTPROFILE") !== "null")
                        {
                            var vIDEDIT_CERTPROFILE = localStorage.getItem("EDIT_CERTPROFILE");
                            localStorage.setItem("EDIT_CERTPROFILE", null);
                            popupEdit(vIDEDIT_CERTPROFILE);
                        }
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
            }();
        </script>
        <%
            }
        %>
    </body>
</html>
