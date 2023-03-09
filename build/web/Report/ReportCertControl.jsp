<%-- 
    Document   : ReportCertControl
    Created on : Sep 6, 2018, 11:35:17 AM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <script src="../js/jquery.PrintArea.js"></script>
        <title></title>
        <script language="javascript">
            document.title = inputcertlist_title_list;
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
                    document.getElementById("idHiddenLoad").value = JS_STR_GRID_SEARCH_RESET;
                    document.getElementById("tempCsrfToken").value = id;
                    var f = document.form;
                    f.method = "post";
                    f.action = '';
                    f.submit();
            }
            $(document).ready(function () {
                $('.loading-gifHardware').hide();
                $('#myModalOTPHardware').modal({
                    backdrop: 'static',
                    keyboard: true,
                    show: false
                });
            });
            function ExportControlCert(idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../ExportCSVParam",
                    data: {
                        idParam: "exportreportcertcontrol",
                        CsrfToken: idCSRF
                    },
                    catche: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            var f = document.form;
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
            function PrintPreview(vText) {
                var popupWin = window.open('', '_blank', 'width=1050,height=900,location=no,left=200px');
                popupWin.document.open();
                popupWin.document.write('<html><title></title><link rel="stylesheet" type="text/css" href="Print.css" media="screen"/></head><body onload="window.print()">');
                popupWin.document.write(vText);
                popupWin.document.write('</html>');
                popupWin.document.close();
            }
            function PrintControlCert(idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../PrintFormCommon",
                    data: {
                        idParam: "printreportcertcontrol",
                        CsrfToken: idCSRF
                    },
                    catche: false,
                    success: function (html) {
                        var arr = sSpace(html).split('###');
                        if (arr[0] === "0")
                        {
                            $(".loading-gif").hide();
                            $('#over').remove();
                            PrintPreview(arr[1]);
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
                        document.getElementById("idNameURL").innerHTML = inputcertlist_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%
                                int[] pIa = new int[1];
                                int[] pIb = new int[1];
                                int status = 1000;
                                int statusLoad = 0;
                                String sPage_Num_Cert = cogCommon.GetPropertybyCode(Definitions.CONFIG_PAGING_NUMBER_REPORT_CERT).trim();
                                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                                try {
                                    PAYMENT[][] rsReportBranch = new PAYMENT[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (request.getMethod().equals("POST"))
                                    {
                                        statusLoad = 1;
                                        session.setAttribute("pIaReportQuickAgent", null);
                                        session.setAttribute("pIbReportQuickAgent", null);
                                        request.setCharacterEncoding("UTF-8");
                                        String FromCreateDate = request.getParameter("idYear");
                                        String ToCreateDate = request.getParameter("idMounth");
                                        String idBranchOffice = request.getParameter("idBranchOffice");
                                        session.setAttribute("sessYearDateReportCertControl", FromCreateDate);
                                        session.setAttribute("sessMountDateReportCertControl", ToCreateDate);
                                        session.setAttribute("sessBranchOfficeReportCertControl", idBranchOffice);
                                        db.S_BO_PAYMENT_REPORT_PER_MONTH(ToCreateDate, FromCreateDate,
                                            EscapeUtils.escapeHtmlSearch(idBranchOffice),sessLanguageGlobal, rsReportBranch);
                                        if (rsReportBranch[0].length > 0) {
                                            status = 1;
                                        } else {
                                            status = 1000;
                                        }
                                    } else {
                                        session.setAttribute("pIaReportQuickAgent", null);
                                        session.setAttribute("pIbReportQuickAgent", null);
                                        session.setAttribute("sessMountDateReportCertControl", null);
                                        session.setAttribute("sessYearDateReportCertControl", null);
                                        session.setAttribute("sessBranchOfficeReportCertControl", "All");
                                    }
                                %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-search"></i> <script>document.write(inputcertlist_table_search);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="form" method="post" class="form-horizontal">
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_mounth);</script>
                                                        </label>
                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                            <select name="idMounth" id="idMounth" class="form-control123">
                                                                <option value="01" <%= session.getAttribute("sessMountDateReportCertControl") != null
                                                                    && session.getAttribute("sessMountDateReportCertControl").toString().trim().equals("01") ? "selected" : "" %>>01</option>
                                                                <option value="02" <%= session.getAttribute("sessMountDateReportCertControl") != null
                                                                    && session.getAttribute("sessMountDateReportCertControl").toString().trim().equals("02") ? "selected" : "" %>>02</option>
                                                                <option value="03" <%= session.getAttribute("sessMountDateReportCertControl") != null
                                                                    && session.getAttribute("sessMountDateReportCertControl").toString().trim().equals("03") ? "selected" : "" %>>03</option>
                                                                <option value="04" <%= session.getAttribute("sessMountDateReportCertControl") != null
                                                                    && session.getAttribute("sessMountDateReportCertControl").toString().trim().equals("04") ? "selected" : "" %>>04</option>
                                                                <option value="05" <%= session.getAttribute("sessMountDateReportCertControl") != null
                                                                    && session.getAttribute("sessMountDateReportCertControl").toString().trim().equals("05") ? "selected" : "" %>>05</option>
                                                                <option value="06" <%= session.getAttribute("sessMountDateReportCertControl") != null
                                                                    && session.getAttribute("sessMountDateReportCertControl").toString().trim().equals("06") ? "selected" : "" %>>06</option>
                                                                <option value="07" <%= session.getAttribute("sessMountDateReportCertControl") != null
                                                                    && session.getAttribute("sessMountDateReportCertControl").toString().trim().equals("07") ? "selected" : "" %>>07</option>
                                                                <option value="08" <%= session.getAttribute("sessMountDateReportCertControl") != null
                                                                    && session.getAttribute("sessMountDateReportCertControl").toString().trim().equals("08") ? "selected" : "" %>>08</option>
                                                                <option value="09" <%= session.getAttribute("sessMountDateReportCertControl") != null
                                                                    && session.getAttribute("sessMountDateReportCertControl").toString().trim().equals("09") ? "selected" : "" %>>09</option>
                                                                <option value="10" <%= session.getAttribute("sessMountDateReportCertControl") != null
                                                                    && session.getAttribute("sessMountDateReportCertControl").toString().trim().equals("10") ? "selected" : "" %>>10</option>
                                                                <option value="11" <%= session.getAttribute("sessMountDateReportCertControl") != null
                                                                    && session.getAttribute("sessMountDateReportCertControl").toString().trim().equals("11") ? "selected" : "" %>>11</option>
                                                                <option value="12" <%= session.getAttribute("sessMountDateReportCertControl") != null
                                                                    && session.getAttribute("sessMountDateReportCertControl").toString().trim().equals("12") ? "selected" : "" %>>12</option>
                                                            </select>
                                                            <%
                                                                if(session.getAttribute("sessMountDateReportCertControl") == null)
                                                                {
                                                            %>
                                                                <script type="text/javascript">
                                                                        $("#idMounth").val('<%=CommonFunction.getMonthCurrentForSearch()%>');
                                                                </script>
                                                                <%
                                                                }
                                                            %>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_year);</script>
                                                        </label>
                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                            <select name="idYear" id="idYear" class="form-control123">
                                                                <%
                                                                    for(int i=18; i<68; i++) {
                                                                        String sYearAfter = String.valueOf(i);
                                                                        if(sYearAfter.length() == 1)
                                                                        {
                                                                            sYearAfter = "0" + sYearAfter;
                                                                        }
                                                                        String sYearAfterID = "20" + sYearAfter;
                                                                %>
                                                                <option value="<%=sYearAfterID%>" <%= session.getAttribute("sessYearDateReportCertControl") != null
                                                                    && session.getAttribute("sessYearDateReportCertControl").toString().trim().equals(sYearAfterID) ? "selected" : "" %>><%=sYearAfterID%></option>
                                                                <%
                                                                    }
                                                                %>
                                                            </select>
                                                            <%
                                                                if(session.getAttribute("sessYearDateReportCertControl") == null)
                                                                {
                                                            %>
                                                                <script type="text/javascript">
                                                                    $("#idYear").val('<%=CommonFunction.getYearCurrentForSearch()%>');
                                                                </script>
                                                                <%
                                                                }
                                                            %>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <%
                                                            if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                                        %>
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_Branch);</script>
                                                        </label>
                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                            <select name="idBranchOffice" id="idBranchOffice" class="form-control123">
                                                                <%
                                                                    BRANCH[][] rsBranch = new BRANCH[1][];
                                                                    db.S_BO_BRANCH_COMBOBOX(sessLanguageGlobal, rsBranch);
                                                                    if (rsBranch[0].length > 0) {
                                                                        for (BRANCH temp1 : rsBranch[0]) {
                                                                            if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT))
                                                                            {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessBranchOfficeReportCertControl") != null
                                                                    && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeReportCertControl").toString())
                                                                    ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                                <%
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                        <%
                                                            } else {
                                                        %>
                                                        <input type="text" readonly style="display: none;" name="idBranchOffice" id="idBranchOffice" value="<%= SessUserAgentID%>" class="form-control123">
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
                                <div class="x_panel">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(token_fm_infor);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <button type="button" class="btn btn-info" onClick="ExportControlCert('<%= anticsrf%>');"><script>document.write(global_fm_button_export_csv);</script></button>
                                                &nbsp;&nbsp;
                                                <button type="button" class="btn btn-info" onClick="PrintControlCert('<%= anticsrf%>');"><script>document.write(global_fm_button_print_report);</script></button>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form name="myname" method="post" class="form-horizontal">
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <%
                                                int Is_Exists1 = 0;
                                                String sREMARK1 = "";
                                                String sAMOUNT1 = "";
                                                String sNOTE1 = "";
                                                int Is_Exists2 = 0;
                                                String sREMARK2 = "";
                                                String sAMOUNT2 = "";
                                                String sNOTE2 = "";
                                                int Is_Exists3 = 0;
                                                String sREMARK3 = "";
                                                String sAMOUNT3 = "";
                                                String sNOTE3 = "";
                                                int Is_Exists4 = 0;
                                                String sREMARK4 = "";
                                                String sAMOUNT4 = "";
                                                String sNOTE4 = "";
                                                int Is_Exists5 = 0;
                                                String sREMARK5 = "";
                                                String sAMOUNT5 = "";
                                                String sNOTE5 = "";
                                                int Is_Exists6 = 0;
                                                String sREMARK6 = "";
                                                String sAMOUNT6 = "";
                                                String sNOTE6 = "";
                                                int Is_Exists7 = 0;
                                                String sREMARK7 = "";
                                                String sAMOUNT7 = "";
                                                String sNOTE7 = "";
                                                int Is_Exists8 = 0;
                                                String sREMARK8 = "";
                                                String sAMOUNT8 = "";
                                                String sNOTE8 = "";
                                                
                                                double sInt_AMOUNT1=0;
                                                double sInt_AMOUNT2=0;
                                                double sInt_AMOUNT3=0;
                                                double sInt_AMOUNT4=0;
                                                double sInt_AMOUNT5=0;
                                                double sInt_AMOUNT6=0;
                                                double sInt_AMOUNT7=0;
                                                double sInt_AMOUNT8=0;
                                                for(int i=0; i<rsReportBranch[0].length; i++) {
                                                    if(rsReportBranch[0][i].PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_DEBIT_AMOUNT)
                                                    {
                                                        Is_Exists1 = rsReportBranch[0][i].PAYMENT_ENABLED;
                                                        sREMARK1 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].PAYMENT_TYPE_REMARK);
                                                        sAMOUNT1 = com.convertMoneyDoubleZero(rsReportBranch[0][i].AMOUNT);
                                                        sNOTE1 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].NOTE);
                                                        sInt_AMOUNT1 = rsReportBranch[0][i].AMOUNT;
                                                    }
                                                    if(rsReportBranch[0][i].PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_TOTAL_AMOUNT)
                                                    {
                                                        Is_Exists2 = rsReportBranch[0][i].PAYMENT_ENABLED;
                                                        sREMARK2 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].PAYMENT_TYPE_REMARK);
                                                        sAMOUNT2 = com.convertMoneyDoubleZero(rsReportBranch[0][i].AMOUNT);
                                                        sNOTE2 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].NOTE);
                                                        sInt_AMOUNT2 = rsReportBranch[0][i].AMOUNT;
                                                    }
                                                    if(rsReportBranch[0][i].PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_DEPOSIT_DEVICE)
                                                    {
                                                        Is_Exists3 = rsReportBranch[0][i].PAYMENT_ENABLED;
                                                        sREMARK3 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].PAYMENT_TYPE_REMARK);
                                                        sAMOUNT3 = com.convertMoneyDoubleZero(rsReportBranch[0][i].AMOUNT);
                                                        sNOTE3 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].NOTE);
                                                        sInt_AMOUNT3 = rsReportBranch[0][i].AMOUNT;
                                                    }
                                                    if(rsReportBranch[0][i].PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_DEPOSIT_DEVICE_MINUS)
                                                    {
                                                        Is_Exists4 = rsReportBranch[0][i].PAYMENT_ENABLED;
                                                        sREMARK4 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].PAYMENT_TYPE_REMARK);
                                                        sAMOUNT4 = com.convertMoneyDoubleZero(rsReportBranch[0][i].AMOUNT);
                                                        sNOTE4 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].NOTE);
                                                        sInt_AMOUNT4 = rsReportBranch[0][i].AMOUNT;
                                                    }
                                                    if(rsReportBranch[0][i].PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_FILE_AMOUNT)
                                                    {
                                                        Is_Exists5 = rsReportBranch[0][i].PAYMENT_ENABLED;
                                                        sREMARK5 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].PAYMENT_TYPE_REMARK);
                                                        sAMOUNT5 = com.convertMoneyDoubleZero(rsReportBranch[0][i].AMOUNT);
                                                        sNOTE5 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].NOTE);
                                                        sInt_AMOUNT5 = rsReportBranch[0][i].AMOUNT;
                                                    }
                                                    if(rsReportBranch[0][i].PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_RETURN_FILE_AMOUNT)
                                                    {
                                                        Is_Exists6 = rsReportBranch[0][i].PAYMENT_ENABLED;
                                                        sREMARK6 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].PAYMENT_TYPE_REMARK);
                                                        sAMOUNT6 = com.convertMoneyDoubleZero(rsReportBranch[0][i].AMOUNT);
                                                        sNOTE6 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].NOTE);
                                                        sInt_AMOUNT6 = rsReportBranch[0][i].AMOUNT;
                                                    }
                                                    if(rsReportBranch[0][i].PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_PAID)
                                                    {
                                                        Is_Exists7 = rsReportBranch[0][i].PAYMENT_ENABLED;
                                                        sREMARK7 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].PAYMENT_TYPE_REMARK);
                                                        sAMOUNT7 = com.convertMoneyDoubleZero(rsReportBranch[0][i].AMOUNT);
                                                        sNOTE7 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].NOTE);
                                                        sInt_AMOUNT7 = rsReportBranch[0][i].AMOUNT;
                                                    }
                                                    if(rsReportBranch[0][i].PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_TOTAL)
                                                    {
                                                        Is_Exists8 = rsReportBranch[0][i].PAYMENT_ENABLED;
                                                        sREMARK8 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].PAYMENT_TYPE_REMARK);
                                                        sInt_AMOUNT8 = sInt_AMOUNT1 + sInt_AMOUNT2 - sInt_AMOUNT4
                                                            + sInt_AMOUNT5 + sInt_AMOUNT5 - sInt_AMOUNT6 - sInt_AMOUNT7;
                                                        sAMOUNT8 = com.convertMoneyDoubleZero(sInt_AMOUNT8);
                                                        sNOTE8 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].NOTE);
                                                    }
                                                    
                                                }
                                            %>
                                        <div class="table-responsive">
                                        <table id="mytable" class="table table-bordered table-striped projects">
                                            <thead>
                                            <th><script>document.write(global_fm_STT);</script></th>
                                            <th><script>document.write(global_fm_Content);</script></th>
                                            <th><script>document.write(global_fm_amount);</script></th>
                                            <th><script>document.write(global_fm_Note);</script></th>
                                            </thead>
                                            <tbody>
                                                <tr><td>1</td><td><%= sREMARK1%></td><td><%= sAMOUNT1%></td><td><%= sNOTE1%></td></tr>
                                                <tr><td>2</td><td><%= sREMARK2%></td><td><%= sAMOUNT2%></td><td><%= sNOTE2%></td></tr>
                                                <tr><td>3</td><td><%= sREMARK3%></td><td><%= sAMOUNT3%></td><td><%= sNOTE3%></td></tr>
                                                <tr><td>4</td><td><%= sREMARK4%></td><td><%= sAMOUNT4%></td><td><%= sNOTE4%></td></tr>
                                                <tr><td>5</td><td><%= sREMARK5%></td><td><%= sAMOUNT5%></td><td><%= sNOTE5%></td></tr>
                                                <tr><td>6</td><td><%= sREMARK6%></td><td><%= sAMOUNT6%></td><td><%= sNOTE6%></td></tr>
                                                <tr><td>7</td><td><%= sREMARK7%></td><td><%= sAMOUNT7%></td><td><%= sNOTE7%></td></tr>
                                                <tr><td>8</td><td><%= sREMARK8%></td><td><%= sAMOUNT8%></td><td><%= sNOTE8%></td></tr>
                                            </tbody>
                                        </table>
                                        </div>
                                        </form>
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
//                                                if (vMST_MNS === "") {
//                                                    vMST_MNS = obj[i].BUDGET_CODE;
//                                                }
//                                                if (vMST_MNS === "") {
//                                                    vMST_MNS = obj[i].DECISION;
//                                                }
                                                var vCMND_HC = obj[i].PERSONAL_ID;
//                                                if (vCMND_HC === "")
//                                                {
//                                                    vCMND_HC = obj[i].PASSPORT;
//                                                }
                                                content += '<tr>' +
                                                        '<td>' + obj[i].Index + '</td>' +
                                                        '<td>' + obj[i].COMPANY_NAME + '</td>' +
                                                        '<td>' + vMST_MNS + '</td>' +
                                                        '<td>' + obj[i].PERSONAL_NAME + '</td>' +
                                                        '<td>' + vCMND_HC+ '</td>' +
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