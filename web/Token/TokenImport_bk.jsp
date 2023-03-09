<%-- 
    Document   : TokenImport
    Created on : Jun 28, 2018, 8:54:33 AM
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
            document.title = tokenimport_title_list;
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
            function calUpload(idCSRF)
            {
                var input1 = document.getElementById('input-file');
                if (input1.value !== '')
                {
                    var checkFileName = input1.value.substring(input1.value.lastIndexOf('.') + 1);
                    if (checkFileName === "xls" || checkFileName === "xlsx" || checkFileName === "csv")
                    {
                        $('body').append('<div id="over"></div>');
                        $(".loading-gif").show();
                        $.ajax({
                            type: "post",
                            url: '../TokenCommon',
                            data: {
                                idParam: 'checkcsrf',
                                TOKEN_VERSION: document.formimport.TOKEN_VERSION.value,
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var arr = sSpace(html).split('#');
                                if (arr[0] === "0")
                                {
                                    file1 = input1.files[0];
                                    var data1 = new FormData();
                                    data1.append('file', file1);
                                    $.ajax({
                                        type: 'POST',
                                        url: '../TokenImport',
                                        data: data1,
                                        cache: false,
                                        contentType: false,
                                        processData: false,
                                        enctype: "multipart/form-data",
                                        success: function (html) {
                                            var arr = sSpace(html).split('###');
                                            if (arr[0] === "0")
                                            {
                                                var sAlert = token_succ_import + token_succ_import_insert + arr[1]
                                                        + token_succ_import_update + arr[3]
                                                        + token_succ_import_error + arr[2];
                                                funSuccAlert(sAlert, "TokenImport.jsp");
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
                                                if (arr[1] === JS_EX_SPECIAL)
                                                {
                                                    funErrorAlert(global_error_file_special);
                                                }
                                                else if (arr[1] === JS_EX_CSV_NO_TOKENID)
                                                {
                                                    funErrorAlert(token_error_no_tokenid);
                                                }
                                                else if (arr[1] === JS_EX_CSV_NO_SOPIN)
                                                {
                                                    funErrorAlert(token_error_no_sopin);
                                                }
                                                else
                                                {
                                                    funErrorAlert(global_errorsql);
                                                }
                                            }
                                            $(".loading-gif").hide();
                                            $('#over').remove();
                                        }
                                    });
                                }
                                else
                                {
                                    funCsrfAlert();
                                }
                                $(".loading-gif").hide();
                                $('#over').remove();
                            }
                        });
                    }
                    else
                    {
                        funErrorAlert(token_error_import_format);
                    }
                }
                else
                {
                    funErrorAlert(global_req_file);
                }
            }
            function downloadSampleToken()
            {
                var f = document.formimport;
                f.method = "post";
                f.action = '../DownFromSaveFile?idParam=downfilesamplesim';
                f.submit();
            }
        </script>
        <style>.projects th{font-weight: bold;}</style>
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
                        document.getElementById("idNameURL").innerHTML = tokenimport_title_list;
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
                                int[] pIa = new int[1];
                                int[] pIb = new int[1];
                                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                try {
                                    TOKEN[][] rsPgin = new TOKEN[1][];
                                    if (session.getAttribute("RefreshTokenImportSess") != null) {
                                        session.setAttribute("RefreshTokenImportSessPaging", "1");
                                        session.setAttribute("SearchSharePagingTokenImport", "0");
                                        statusLoad = 1;
                                        String idSessIsDate = (String) session.getAttribute("idSessIsDateSImport");
                                        String FromDateValid = (String) session.getAttribute("sessFromDateValidImport");
                                        String ToDateValid = (String) session.getAttribute("sessToDateValidImport");
                                        String FromTOKEN_ID = (String) session.getAttribute("sessFromTOKEN_IDImport");
                                        String ToTOKEN_ID = (String) session.getAttribute("sessToTOKEN_IDImport");
                                        String AGENT_ID = (String) session.getAttribute("sessAGENT_IDImport");
                                        session.setAttribute("idSessIsDateSImport", idSessIsDate);
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
                                        if ("1".equals(idSessIsDate)) {
                                            FromDateValid = "";
                                            ToDateValid = "";
                                        } else if ("3".equals(idSessIsDate)) {
                                            FromDateValid = "";
                                            ToDateValid = "";
                                            FromTOKEN_ID = "";
                                            ToTOKEN_ID = "";
                                        } else {
                                            FromTOKEN_ID = "";
                                            ToTOKEN_ID = "";
                                        }
                                        int ss = 0;
                                        ss = db.S_BO_TOKEN_IMPORT_TOTAL(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
                                            EscapeUtils.escapeHtmlSearch(FromTOKEN_ID),
                                            EscapeUtils.escapeHtmlSearch(ToTOKEN_ID), EscapeUtils.escapeHtmlSearch(AGENT_ID),
                                            pIa, pIb,"");
                                        session.setAttribute("pIaImport", String.valueOf(pIa[0]));
                                        session.setAttribute("pIbImport", String.valueOf(pIb[0]));
                                        if (session.getAttribute("pIaImport") != null) {
                                            pIa[0] = Integer.parseInt((String) session.getAttribute("pIaImport"));
                                        }
                                        if (session.getAttribute("pIbImport") != null) {
                                            pIb[0] = Integer.parseInt((String) session.getAttribute("pIbImport"));
                                        }
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
                                        if (session.getAttribute("pIaImport") != null) {
                                            pIa[0] = Integer.parseInt((String) session.getAttribute("pIaImport"));
                                        }
                                        if (session.getAttribute("pIbImport") != null) {
                                            pIb[0] = Integer.parseInt((String) session.getAttribute("pIbImport"));
                                        }
                                        if (ss > 0) {
                                            db.S_BO_TOKEN_IMPORT_LIST(String.valueOf(pIa[0]), String.valueOf(pIb[0]),
                                                EscapeUtils.escapeHtmlSearch(FromTOKEN_ID),
                                                EscapeUtils.escapeHtmlSearch(ToTOKEN_ID), EscapeUtils.escapeHtmlSearch(AGENT_ID),
                                                sessLanguageGlobal, rsPgin, iPagNo, iSwRws, "");
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
                                            session.setAttribute("pIaImport", null);
                                            session.setAttribute("pIbImport", null);
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
                                    String idSessIsDate = request.getParameter("idSessIsDate");
                                    String FromDateValid = request.getParameter("FromDateValid");
                                    String ToDateValid = request.getParameter("ToDateValid");
                                    if ("1".equals(hasPaging)) {
                                        session.setAttribute("SearchSharePagingImport", "0");
                                        idSessIsDate = (String) session.getAttribute("idSessIsDateSImport");
                                        FromDateValid = (String) session.getAttribute("sessFromDateValidImport");
                                        ToDateValid = (String) session.getAttribute("sessToDateValidImport");
                                        FromTOKEN_ID = (String) session.getAttribute("sessFromTOKEN_IDImport");
                                        ToTOKEN_ID = (String) session.getAttribute("sessToTOKEN_IDImport");
                                        AGENT_ID = (String) session.getAttribute("sessAGENT_IDImport");
                                        pIa[0] = Integer.parseInt((String) session.getAttribute("pIaImport"));
                                        pIb[0] = Integer.parseInt((String) session.getAttribute("pIbImport"));
                                        session.setAttribute("SessParamOnPagingImport", null);
                                    } else {
                                        session.setAttribute("SearchSharePagingImport", "1");
                                        session.setAttribute("CountListTokenImport", null);
                                    }
                                    session.setAttribute("idSessIsDateSImport", idSessIsDate);
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
                                    if ("1".equals(idSessIsDate)) {
                                        FromDateValid = "";
                                        ToDateValid = "";
                                    } else if ("3".equals(idSessIsDate)) {
                                        FromDateValid = "";
                                        ToDateValid = "";
                                        FromTOKEN_ID = "";
                                        ToTOKEN_ID = "";
                                    } else {
                                        FromTOKEN_ID = "";
                                        ToTOKEN_ID = "";
                                    }
                                    int ss = 0;
                                    if ((session.getAttribute("CountListTokenImport")) == null) {
                                        ss = db.S_BO_TOKEN_IMPORT_TOTAL(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
                                            EscapeUtils.escapeHtmlSearch(FromTOKEN_ID),
                                            EscapeUtils.escapeHtmlSearch(ToTOKEN_ID), EscapeUtils.escapeHtmlSearch(AGENT_ID),
                                            pIa, pIb, "");
                                        session.setAttribute("CountListTokenImport", String.valueOf(ss));
                                        session.setAttribute("pIaImport", String.valueOf(pIa[0]));
                                        session.setAttribute("pIbImport", String.valueOf(pIb[0]));
                                        if (session.getAttribute("pIaImport") != null) {
                                            pIa[0] = Integer.parseInt((String) session.getAttribute("pIaImport"));
                                        }
                                        if (session.getAttribute("pIbImport") != null) {
                                            pIb[0] = Integer.parseInt((String) session.getAttribute("pIbImport"));
                                        }
                                    } else {
                                        String sCount = (String) session.getAttribute("CountListTokenImport");
                                        ss = Integer.parseInt(sCount);
                                        session.setAttribute("CountListTokenImport", String.valueOf(ss));
                                    }
                                    iTotRslts = ss;
                                    if (iTotRslts > 0) {
                                        db.S_BO_TOKEN_IMPORT_LIST(String.valueOf(pIa[0]), String.valueOf(pIb[0]),
                                            EscapeUtils.escapeHtmlSearch(FromTOKEN_ID),
                                            EscapeUtils.escapeHtmlSearch(ToTOKEN_ID), EscapeUtils.escapeHtmlSearch(AGENT_ID),
                                            sessLanguageGlobal, rsPgin, iPagNo, iSwRws,"");
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
                                    session.setAttribute("pIaImport", null);
                                    session.setAttribute("pIbImport", null);
                                    session.setAttribute("SearchShareStoreTokenImport", null);
                                    session.setAttribute("SearchIPageNoPagingTokenImport", null);
                                    session.setAttribute("SearchISwRwsPagingTokenImport", null);
                                    session.setAttribute("idSessIsDateSImport", null);
                                    session.setAttribute("sessFromDateValidImport", null);
                                    session.setAttribute("sessToDateValidImport", null);
                                    session.setAttribute("sessFromTOKEN_IDImport", null);
                                    session.setAttribute("sessToTOKEN_IDImport", null);
                                    session.setAttribute("sessAGENT_IDImport", null);
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
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="formimport" method="post" class="form-horizontal">
                                            <fieldset class="scheduler-border">
                                                <legend class="scheduler-border"><script>document.write(tokenimport_title_import);</script></legend>
                                                <div class="form-group" style="padding: 0px;margin: 0;">
                                                    <input type="file" id="input-file" accept=".xlsx,.xls,.csv" style="width: 100%;"
                                                        onchange="calUpload('<%= anticsrf%>');" class="btn btn-default btn-file select_file">
                                                </div>
                                                <div class="form-group" style="padding: 0px;margin: 0;">
                                                    <label class="control-label123"><script>document.write(token_fm_version);</script></label>
                                                    <select name="TOKEN_VERSION" id="TOKEN_VERSION" class="form-control123">
                                                        <%
                                                            TOKEN_VERSION[][] rstState = new TOKEN_VERSION[1][];
                                                            db.S_BO_TOKEN_VERSION_COMBOBOX(sessLanguageGlobal, rstState);
                                                            if (rstState[0].length > 0) {
                                                                for (TOKEN_VERSION temp1 : rstState[0]) {
                                                        %>
                                                        <option value="<%=String.valueOf(temp1.ID)%>"><%=temp1.REMARK%></option>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </select>
                                                </div>
                                                <div class="form-group" style="padding: 0px 0px 0px 0px;">
                                                    <label class="control-label"><script>document.write(token_fm_import_sample);</script></label> <a style="cursor: pointer;" onclick="return downloadSampleToken();"><script>document.write(global_fm_down);</script></a>
                                                </div>
                                            </fieldset>
                                        </form>
                                    </div>
                                </div>
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2>
                                            <i class="fa fa-search"></i> <script>document.write(token_title_search);</script>
                                        </h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="form" method="post" class="form-horizontal">
                                            <div class="form-group">
                                               <label class="control-label col-sm-2" style="text-align: left;padding-left: 0;color: #000000;"><script>document.write(token_fm_typesearch);</script></label>
                                               <div class="col-sm-9" style="padding: 0px 0 0 0;text-align: left;">
                                                &nbsp;&nbsp;
                                                <label class="radio-inline"><input type="radio" name="nameCheck" id="nameCheck3"><script>document.write(global_fm_combox_all);</script></label>
                                                <label class="radio-inline"><input type="radio" name="nameCheck" id="nameCheck1"><script>document.write(tokenimport_fm_createdate_search);</script></label>
                                                <label class="radio-inline"><input type="radio" name="nameCheck" id="nameCheck2"><script>document.write(tokenimport_fm_tokensn_search);</script></label>
                                                <input type="text" style="display: none;" id="idSessIsDate" name="idSessIsDate" />
                                                </div>
                                                <script>
                                                    $(document).ready(function () {
                                                        var sSessIsDate = localStorage.getItem('StoreIsDateImport');
                                                        if (sSessIsDate === '1')
                                                        {
                                                            document.getElementById("idSessIsDate").value = "1";
                                                            $("#nameCheck2").prop("checked", true);
                                                            $("#nameCheck3").prop("checked", false);
                                                            $("#nameCheck1").prop("checked", false);
                                                        }
                                                        else if (sSessIsDate === '0')
                                                        {
                                                            document.getElementById("idSessIsDate").value = "0";
                                                            $("#nameCheck1").prop("checked", true);
                                                            $("#nameCheck2").prop("checked", false);
                                                            $("#nameCheck3").prop("checked", false);
                                                        }
                                                        else
                                                        {
                                                            document.getElementById("idSessIsDate").value = "3";
                                                            $("#nameCheck3").prop("checked", true);
                                                            $("#nameCheck2").prop("checked", false);
                                                            $("#nameCheck1").prop("checked", false);
                                                        }
                                                        if (document.getElementById("idSessIsDate").value === '1')
                                                        {
                                                            document.getElementById("FromTOKEN_ID").disabled = false;
                                                            document.getElementById("ToTOKEN_ID").disabled = false;
                                                            document.getElementById("FromDateValid").disabled = true;
                                                            document.getElementById("ToDateValid").disabled = true;
                                                        } else if (document.getElementById("idSessIsDate").value === '0')
                                                        {
                                                            document.getElementById("FromTOKEN_ID").disabled = true;
                                                            document.getElementById("ToTOKEN_ID").disabled = true;
                                                            document.getElementById("FromDateValid").disabled = false;
                                                            document.getElementById("ToDateValid").disabled = false;
                                                        }
                                                        else
                                                        {
                                                            document.getElementById("FromTOKEN_ID").disabled = true;
                                                            document.getElementById("ToTOKEN_ID").disabled = true;
                                                            document.getElementById("FromDateValid").disabled = true;
                                                            document.getElementById("ToDateValid").disabled = true;
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
                                                    $('.radio-inline').on('click', function () {
                                                        var s = $(this).find('input').attr('id');
                                                        if (s === 'nameCheck1')
                                                        {
                                                            $("#idSessIsDate").val('0');
                                                            document.getElementById("FromTOKEN_ID").disabled = true;
                                                            document.getElementById("ToTOKEN_ID").disabled = true;
                                                            document.getElementById("FromDateValid").disabled = false;
                                                            document.getElementById("ToDateValid").disabled = false;
                                                            localStorage.setItem("StoreIsDateImport", "0");
                                                        }
                                                        if (s === 'nameCheck2')
                                                        {
                                                            $("#idSessIsDate").val('1');
                                                            document.getElementById("FromTOKEN_ID").disabled = false;
                                                            document.getElementById("ToTOKEN_ID").disabled = false;
                                                            document.getElementById("FromDateValid").disabled = true;
                                                            document.getElementById("ToDateValid").disabled = true;
                                                            localStorage.setItem("StoreIsDateImport", "1");
                                                        }
                                                        if (s === 'nameCheck3')
                                                        {
                                                            $("#idSessIsDate").val('3');
                                                            document.getElementById("FromTOKEN_ID").disabled = true;
                                                            document.getElementById("ToTOKEN_ID").disabled = true;
                                                            document.getElementById("FromDateValid").disabled = true;
                                                            document.getElementById("ToDateValid").disabled = true;
                                                            localStorage.setItem("StoreIsDateImport", "3");
                                                        }
                                                    });
                                                </script>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_FromDate);</script></label>
                                                <input type="Text" id="FromDateValid" name="FromDateValid" maxlength="25" class="form-control123"
                                                       value="<%= session.getAttribute("sessFromDateValidImport") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromDateValidImport").toString()) : com.ConvertMonthSub(30)%>"/>
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label123"><script>document.write(global_fm_ToDate);</script></label>
                                                <input type="Text" id="ToDateValid" name="ToDateValid" maxlength="25" class="form-control123"
                                                    value="<%= session.getAttribute("sessToDateValidImport") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToDateValidImport").toString()) : com.ConvertMonthSub(0)%>"/>
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label123"><script>document.write(tokenimport_fm_fromtokenSN);</script></label>
                                                <input type="Text" id="FromTOKEN_ID" name="FromTOKEN_ID" maxlength="40" class="form-control123"
                                                    value="<%= session.getAttribute("sessFromTOKEN_IDImport") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromTOKEN_IDImport").toString()) : ""%>"/>
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label123"><script>document.write(tokenimport_fm_totokenSN);</script></label>
                                                <input type="Text" id="ToTOKEN_ID" name="ToTOKEN_ID" maxlength="40" class="form-control123"
                                                       value="<%= session.getAttribute("sessToTOKEN_IDImport") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToTOKEN_IDImport").toString()) : ""%>"/>
                                            </div>
                                            <%
                                                if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                            %>
                                            <div class="form-group">
                                                <label class="control-label123"><script>document.write(token_fm_agent);</script></label>
                                                <select name="AGENT_ID" id="AGENT_ID" class="form-control123">
                                                    <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessAGENT_IDImport") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessAGENT_IDImport").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                    <%
                                                        BRANCH[][] rsBranch = new BRANCH[1][];
                                                        db.S_BO_BRANCH_COMBOBOX(sessLanguageGlobal, rsBranch);
                                                        if (rsBranch[0].length > 0) {
                                                            for (BRANCH temp1 : rsBranch[0]) {
                                                        %>
                                                        <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessBranchOfficeCert") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeCert").toString()) ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
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
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                            <input id="idHiddenLoad" name="nameHiddenLoad" value="" type="hidden"/>
                                        </form>
                                    </div>
                                </div>
                                <%
                                    if (status == 1 && statusLoad == 1) {
                                %>
                                <div class="x_panel" id="divEditTokenMulti">
                                    <div class="x_content" style="margin-top: 0px;">
                                        <fieldset class="scheduler-border">
                                            <legend class="scheduler-border"><script>document.write(tokenimport_title_multi);</script></legend>
                                            <div class="form-group" style="padding: 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(token_fm_agent);</script></label>
                                                <div style="width: 100%;padding-top: 5px;clear: both;">
                                                    <div style="float: left;width: 91%;">
                                                        <select name="AGENT_IDMulti" id="AGENT_IDMulti" class="form-control123">
                                                            <%
                                                                BRANCH[][] rst = new BRANCH[1][];
                                                                db.S_BO_BRANCH_COMBOBOX(sessLanguageGlobal, rst);
                                                                if (rst[0].length > 0) {
                                                                    for (BRANCH temp1 : rst[0]) {
                                                            %>
                                                            <option value="<%=String.valueOf(temp1.ID)%>"><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                            <%
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
                                        </fieldset>
                                    </div>
                                </div>
                                <script type="text/javascript">
                                    $(document).ready(function () {
                                        $("#divEditTokenMulti").css("display", "none");
                                        goToByScroll("idShowResultSearch");
                                    });
                                </script>
                                <div class="x_panel" id="idShowResultSearch">
                                    <div class="x_title">
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
                                                                    funSuccAlert(token_succ_edit, "TokenImport.jsp");
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
                                        <table class="table table-striped projects" id="mtToken">
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
                                                    <td colspan="8" style="text-align: center;">
                                                        <div class="paging_table">
                                                            <%
                                                                int i = 0;
                                                                int cPge = 0;

                                                                if (iTotRslts > iSwRws) {
                                                                    cPge = ((int) (Math.ceil((double) iEnRsNo / (iTotSrhRcrds * iSwRws))));
                                                                    int prePageNo = (cPge * iTotSrhRcrds) - ((iTotSrhRcrds - 1) + iTotSrhRcrds);
                                                                    if ((cPge * iTotSrhRcrds) - (iTotSrhRcrds) > 0) {
                                                            %>
                                                            <a href="TokenImport.jsp?iPagNo=<%=prePageNo%>&cPagNo=<%=prePageNo%>"><< <script>document.write(global_paging_Before);</script></a>
                                                            &nbsp;
                                                            <%
                                                                }
                                                                for (i = ((cPge * iTotSrhRcrds) - (iTotSrhRcrds - 1)); i <= (cPge * iTotSrhRcrds); i++) {
                                                                    if (i == ((iPagNo / iSwRws) + 1)) {
                                                            %>
                                                            <a href="TokenImport.jsp?iPagNo=<%=i%>" style="cursor:pointer;color:red;"><b><%=i%></b></a>
                                                                    <%                   } else if (i <= iTotPags) {
                                                                    %>
                                                            &nbsp;<a href="TokenImport.jsp?iPagNo=<%=i%>"><%=i%></a>
                                                            <%
                                                                    }
                                                                }

                                                                if (iTotPags > iTotSrhRcrds && i <= iTotPags) {
                                                            %>
                                                            &nbsp;
                                                            <a href="TokenImport.jsp?iPagNo=<%=i%>&cPagNo=<%=i%>">>> <script>document.write(global_paging_last);</script></a>
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
