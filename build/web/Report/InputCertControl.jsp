<%-- 
    Document   : InputCertControl
    Created on : Sep 7, 2018, 2:20:23 PM
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
        <title></title>
        <script language="javascript">
            document.title = inputcertlist_title_list;
            changeFavicon("../");
            $(document).ready(function () {
                if(localStorage.getItem("RefreshFirstInsertControl") !== null && localStorage.getItem("RefreshFirstInsertControl") !== "")
                {
                    var vRefreshFirstInsertControl = localStorage.getItem("RefreshFirstInsertControl");
                    localStorage.setItem("RefreshFirstInsertControl","");
                    $('html,body').animate({scrollTop: $("#" + vRefreshFirstInsertControl).offset().top}, 'slow');
                    
                }
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
            $(document).ready(function () {
                $(".toggle-accordion").on("click", function () {
                    var accordionId = $(this).attr("accordion-id"),
                            numPanelOpen = $(accordionId + ' .collapse.in').length;
                    $(this).toggleClass("active");
                    if (numPanelOpen === 0) {
                        openAllPanels(accordionId);
                    }
                    else {
                        closeAllPanels(accordionId);
                    }
                });
                openAllPanels = function (aId) {
                    console.log("setAllPanelOpen");
                    $(aId + ' .panel-collapse:not(".in")').collapse('show');
                };
                closeAllPanels = function (aId) {
                    console.log("setAllPanelclose");
                    $(aId + ' .panel-collapse.in').collapse('hide');
                };
            });
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
            
/*            .container {
                max-width: 960px;
            }*/

            .panel-default>.panel-heading {
                color: #333;
                background-color: #fff;
                border-color: #e4e5e7;
                padding: 0;
                -webkit-user-select: none;
                -moz-user-select: none;
                -ms-user-select: none;
                user-select: none;
            }

            .panel-default>.panel-heading a {
                display: block;
                padding: 10px 15px;
            }

            .panel-default>.panel-heading a:after {
                content: "";
                position: relative;
                top: 1px;
                display: inline-block;
                font-family: 'Glyphicons Halflings';
                font-style: normal;
                font-weight: 400;
                line-height: 1;
                -webkit-font-smoothing: antialiased;
                -moz-osx-font-smoothing: grayscale;
                float: right;
                transition: transform .25s linear;
                -webkit-transition: -webkit-transform .25s linear;
            }

            .panel-default>.panel-heading a[aria-expanded="true"] {
                background-color: #eee;
            }

            .panel-default>.panel-heading a[aria-expanded="true"]:after {
                content: "\2212";
                -webkit-transform: rotate(180deg);
                transform: rotate(180deg);background-color: #eee;
            }

            .panel-default>.panel-heading a[aria-expanded="false"]:after {
                content: "\002b";
                -webkit-transform: rotate(90deg);
                transform: rotate(90deg);
            }

            .accordion-option {
                width: 100%;
                float: left;
                clear: both;
                /*margin: 15px 0;*/
            }

            .accordion-option .title {
                font-size: 20px;
                /*font-weight: bold;*/
                float: left;
                padding: 0;
                margin: 0;
            }

            .accordion-option .toggle-accordion {
                float: right;
                font-size: 16px;
                color: #6a6c6f;
            }

            .accordion-option .toggle-accordion:before {
                content: "Expand All";
            }

            .accordion-option .toggle-accordion.active:before {
                content: "Collapse All";
            }
            .panel-body{
                padding: 15px 10px 0 10px;
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
                        document.getElementById("idNameURL").innerHTML = inputcertlist_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%
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
                                        session.setAttribute("pIaInputCertControl", null);
                                        session.setAttribute("pIbInputCertControl", null);
                                        request.setCharacterEncoding("UTF-8");
                                        String FromCreateDate = request.getParameter("idYear");
                                        String ToCreateDate = request.getParameter("idMounth");
                                        String idBranchOffice = request.getParameter("idBranchOffice");
                                        session.setAttribute("sessYearInputCertControl", FromCreateDate);
                                        session.setAttribute("sessMonthInputCertControl", ToCreateDate);
                                        session.setAttribute("sessBranchOfficeInputCertControl", idBranchOffice);
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idBranchOffice)) {
                                            idBranchOffice = "";
                                        }
                                        db.S_BO_PAYMENT_REPORT_PER_MONTH(ToCreateDate, FromCreateDate,
                                            EscapeUtils.escapeHtmlSearch(idBranchOffice),sessLanguageGlobal, rsReportBranch);
                                        if (rsReportBranch[0].length > 0) {
                                            status = 1;
                                        } else {
                                            status = 1000;
                                        }
                                    } else {
                                        session.setAttribute("pIaInputCertControl", null);
                                        session.setAttribute("pIbInputCertControl", null);
                                        session.setAttribute("sessYearInputCertControl", null);
                                        session.setAttribute("sessMonthInputCertControl", null);
                                        session.setAttribute("sessBranchOfficeInputCertControl", "All");
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
                                                                <option value="01" <%= session.getAttribute("sessMonthInputCertControl") != null
                                                                    && session.getAttribute("sessMonthInputCertControl").toString().trim().equals("01") ? "selected" : "" %>>01</option>
                                                                <option value="02" <%= session.getAttribute("sessMonthInputCertControl") != null
                                                                    && session.getAttribute("sessMonthInputCertControl").toString().trim().equals("02") ? "selected" : "" %>>02</option>
                                                                <option value="03" <%= session.getAttribute("sessMonthInputCertControl") != null
                                                                    && session.getAttribute("sessMonthInputCertControl").toString().trim().equals("03") ? "selected" : "" %>>03</option>
                                                                <option value="04" <%= session.getAttribute("sessMonthInputCertControl") != null
                                                                    && session.getAttribute("sessMonthInputCertControl").toString().trim().equals("04") ? "selected" : "" %>>04</option>
                                                                <option value="05" <%= session.getAttribute("sessMonthInputCertControl") != null
                                                                    && session.getAttribute("sessMonthInputCertControl").toString().trim().equals("05") ? "selected" : "" %>>05</option>
                                                                <option value="06" <%= session.getAttribute("sessMonthInputCertControl") != null
                                                                    && session.getAttribute("sessMonthInputCertControl").toString().trim().equals("06") ? "selected" : "" %>>06</option>
                                                                <option value="07" <%= session.getAttribute("sessMonthInputCertControl") != null
                                                                    && session.getAttribute("sessMonthInputCertControl").toString().trim().equals("07") ? "selected" : "" %>>07</option>
                                                                <option value="08" <%= session.getAttribute("sessMonthInputCertControl") != null
                                                                    && session.getAttribute("sessMonthInputCertControl").toString().trim().equals("08") ? "selected" : "" %>>08</option>
                                                                <option value="09" <%= session.getAttribute("sessMonthInputCertControl") != null
                                                                    && session.getAttribute("sessMonthInputCertControl").toString().trim().equals("09") ? "selected" : "" %>>09</option>
                                                                <option value="10" <%= session.getAttribute("sessMonthInputCertControl") != null
                                                                    && session.getAttribute("sessMonthInputCertControl").toString().trim().equals("10") ? "selected" : "" %>>10</option>
                                                                <option value="11" <%= session.getAttribute("sessMonthInputCertControl") != null
                                                                    && session.getAttribute("sessMonthInputCertControl").toString().trim().equals("11") ? "selected" : "" %>>11</option>
                                                                <option value="12" <%= session.getAttribute("sessMonthInputCertControl") != null
                                                                    && session.getAttribute("sessMonthInputCertControl").toString().trim().equals("12") ? "selected" : "" %>>12</option>
                                                            </select>
                                                            <%
                                                                if(session.getAttribute("sessMonthInputCertControl") == null)
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
                                                                <option value="<%=sYearAfterID%>" <%= session.getAttribute("sessYearInputCertControl") != null
                                                                    && session.getAttribute("sessYearInputCertControl").toString().trim().equals(sYearAfterID) ? "selected" : "" %>><%=sYearAfterID%></option>
                                                                <%
                                                                    }
                                                                %>
                                                            </select>
                                                            <%
                                                                if(session.getAttribute("sessYearInputCertControl") == null)
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
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessBranchOfficeInputCertControl") != null
                                                                    && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeInputCertControl").toString())
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
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(token_fm_infor);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <div class="accordion-option">
                                                    <a href="javascript:void(0)" class="toggle-accordion active" accordion-id="#accordion"></a>
                                                </div>
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
                                                
                                                String sPAYMENT_ID1 = "";
                                                String sPAYMENT_ID2 = "";
                                                String sPAYMENT_ID3 = "";
                                                String sPAYMENT_ID4 = "";
                                                String sPAYMENT_ID5 = "";
                                                String sPAYMENT_ID6 = "";
                                                String sPAYMENT_ID7 = "";
                                                
                                                String sPAYMENT_TYPE_ID1 = "";
                                                String sPAYMENT_TYPE_ID2 = "";
                                                String sPAYMENT_TYPE_ID3 = "";
                                                String sPAYMENT_TYPE_ID4 = "";
                                                String sPAYMENT_TYPE_ID5 = "";
                                                String sPAYMENT_TYPE_ID6 = "";
                                                String sPAYMENT_TYPE_ID7 = "";
                                                for(int i=0; i<rsReportBranch[0].length; i++) {
                                                    if(rsReportBranch[0][i].PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_DEBIT_AMOUNT)
                                                    {
                                                        Is_Exists1 = rsReportBranch[0][i].PAYMENT_ENABLED;
                                                        sREMARK1 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].PAYMENT_TYPE_REMARK);
                                                        sAMOUNT1 = com.convertMoneyDoubleZero(rsReportBranch[0][i].AMOUNT);
                                                        sNOTE1 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].NOTE);
                                                        sPAYMENT_ID1 = String.valueOf(rsReportBranch[0][i].PAYMENT_ID);
                                                        sPAYMENT_TYPE_ID1 = String.valueOf(rsReportBranch[0][i].PAYMENT_TYPE_ID);
                                                    }
                                                    if(rsReportBranch[0][i].PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_TOTAL_AMOUNT)
                                                    {
                                                        Is_Exists2 = rsReportBranch[0][i].PAYMENT_ENABLED;
                                                        sREMARK2 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].PAYMENT_TYPE_REMARK);
                                                        sAMOUNT2 = com.convertMoneyDoubleZero(rsReportBranch[0][i].AMOUNT);
                                                        sNOTE2 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].NOTE);
                                                        sPAYMENT_ID2 = String.valueOf(rsReportBranch[0][i].PAYMENT_ID);
                                                        sPAYMENT_TYPE_ID2 = String.valueOf(rsReportBranch[0][i].PAYMENT_TYPE_ID);
                                                    }
                                                    if(rsReportBranch[0][i].PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_DEPOSIT_DEVICE)
                                                    {
                                                        Is_Exists3 = rsReportBranch[0][i].PAYMENT_ENABLED;
                                                        sREMARK3 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].PAYMENT_TYPE_REMARK);
                                                        sAMOUNT3 = com.convertMoneyDoubleZero(rsReportBranch[0][i].AMOUNT);
                                                        sNOTE3 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].NOTE);
                                                        sPAYMENT_ID3 = String.valueOf(rsReportBranch[0][i].PAYMENT_ID);
                                                        sPAYMENT_TYPE_ID3 = String.valueOf(rsReportBranch[0][i].PAYMENT_TYPE_ID);
                                                    }
                                                    if(rsReportBranch[0][i].PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_DEPOSIT_DEVICE_MINUS)
                                                    {
                                                        Is_Exists4 = rsReportBranch[0][i].PAYMENT_ENABLED;
                                                        sREMARK4 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].PAYMENT_TYPE_REMARK);
                                                        sAMOUNT4 = com.convertMoneyDoubleZero(rsReportBranch[0][i].AMOUNT);
                                                        sNOTE4 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].NOTE);
                                                        sPAYMENT_ID4 = String.valueOf(rsReportBranch[0][i].PAYMENT_ID);
                                                        sPAYMENT_TYPE_ID4 = String.valueOf(rsReportBranch[0][i].PAYMENT_TYPE_ID);
                                                    }
                                                    if(rsReportBranch[0][i].PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_FILE_AMOUNT)
                                                    {
                                                        Is_Exists5 = rsReportBranch[0][i].PAYMENT_ENABLED;
                                                        sREMARK5 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].PAYMENT_TYPE_REMARK);
                                                        sAMOUNT5 = com.convertMoneyDoubleZero(rsReportBranch[0][i].AMOUNT);
                                                        sNOTE5 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].NOTE);
                                                        sPAYMENT_ID5 = String.valueOf(rsReportBranch[0][i].PAYMENT_ID);
                                                        sPAYMENT_TYPE_ID5 = String.valueOf(rsReportBranch[0][i].PAYMENT_TYPE_ID);
                                                    }
                                                    if(rsReportBranch[0][i].PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_RETURN_FILE_AMOUNT)
                                                    {
                                                        Is_Exists6 = rsReportBranch[0][i].PAYMENT_ENABLED;
                                                        sREMARK6 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].PAYMENT_TYPE_REMARK);
                                                        sAMOUNT6 = com.convertMoneyDoubleZero(rsReportBranch[0][i].AMOUNT);
                                                        sNOTE6 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].NOTE);
                                                        sPAYMENT_ID6 = String.valueOf(rsReportBranch[0][i].PAYMENT_ID);
                                                        sPAYMENT_TYPE_ID6 = String.valueOf(rsReportBranch[0][i].PAYMENT_TYPE_ID);
                                                    }
                                                    if(rsReportBranch[0][i].PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_PAID)
                                                    {
                                                        Is_Exists7 = rsReportBranch[0][i].PAYMENT_ENABLED;
                                                        sREMARK7 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].PAYMENT_TYPE_REMARK);
                                                        sAMOUNT7 = com.convertMoneyDoubleZero(rsReportBranch[0][i].AMOUNT);
                                                        sNOTE7 = EscapeUtils.CheckTextNull(rsReportBranch[0][i].NOTE);
                                                        sPAYMENT_ID7 = String.valueOf(rsReportBranch[0][i].PAYMENT_ID);
                                                        sPAYMENT_TYPE_ID7 = String.valueOf(rsReportBranch[0][i].PAYMENT_TYPE_ID);
                                                    }
                                                    
                                                }
                                            %>
                                            <script>
                                                function EditCertControl(vPAYMENT_ID, vPAYMENT_TYPE_ID, idNote, idAmount, idCSRF)
                                                {
//                                                    if (!JSCheckEmptyField($("#" + idNote).val()))
//                                                    {
//                                                        $("#" + idNote).focus();
//                                                        funErrorAlert(policy_req_empty + global_fm_Description);
//                                                        return false;
//                                                    }
                                                    if (!JSCheckEmptyField($("#" + idAmount).val()))
                                                    {
                                                        $("#" + idAmount).focus();
                                                        funErrorAlert(policy_req_empty + global_fm_amount);
                                                        return false;
                                                    } else {
                                                        if (!JSCheckNumericField($("#" + idAmount).val().replace(/\,/g,""))) {
                                                            $("#" + idAmount).focus();
                                                            funErrorAlert(global_fm_amount + policy_req_number);
                                                            return false;
                                                        }
                                                    }
                                                    $('body').append('<div id="over"></div>');
                                                    $(".loading-gif").show();
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../PaymentCommon",
                                                        data: {
                                                            idParam: 'editinputcertcontrol',
                                                            pPAYMENT_ID: vPAYMENT_ID,
                                                            pPAYMENT_TYPE_ID: vPAYMENT_TYPE_ID,
                                                            pAMOUNT: $("#" + idAmount).val(),
                                                            pNOTE: $("#" + idNote).val(),
                                                            CsrfToken: idCSRF
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            var myStrings = sSpace(html).split('#');
                                                            if (myStrings[0] === "0")
                                                            {
                                                                funSuccNoLoad(inputcertlist_succ_edit);
                                                            }
                                                            else if (myStrings[0] === JS_EX_CSRF)
                                                            {
                                                                funCsrfAlert();
                                                            } else if (myStrings[0] === JS_EX_LOGIN)
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
                                                            $(".loading-gif").hide();
                                                            $('#over').remove();
                                                        }
                                                    });
                                                    return false;
                                                }
                                                function AddCertControl(vPAYMENT_TYPE_ID, idNote, idAmount, idCSRF)
                                                {
                                                    if (!JSCheckEmptyField($("#" + idAmount).val()))
                                                    {
                                                        $("#" + idAmount).focus();
                                                        funErrorAlert(policy_req_empty + global_fm_amount);
                                                        return false;
                                                    } else {
                                                        if (!JSCheckNumericField($("#" + idAmount).val().replace(/\,/g,""))) {
                                                            $("#" + idAmount).focus();
                                                            funErrorAlert(global_fm_amount + policy_req_number);
                                                            return false;
                                                        }
                                                    }
                                                    $('body').append('<div id="over"></div>');
                                                    $(".loading-gif").show();
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../PaymentCommon",
                                                        data: {
                                                            idParam: 'addinputcertcontrol',
                                                            pPAYMENT_TYPE_ID: vPAYMENT_TYPE_ID,
                                                            pAMOUNT: $("#" + idAmount).val(),
                                                            pNOTE: $("#" + idNote).val(),
                                                            CsrfToken: idCSRF
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            var myStrings = sSpace(html).split('#');
                                                            if (myStrings[0] === "0")
                                                            {
                                                                funSuccLocalAlert(inputcertlist_succ_add);
                                                                localStorage.setItem("RefreshFirstInsertControl", idNote);
                                                            }
                                                            else if (myStrings[0] === JS_EX_CSRF)
                                                            {
                                                                funCsrfAlert();
                                                            } else if (myStrings[0] === JS_EX_LOGIN)
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
                                                            $(".loading-gif").hide();
                                                            $('#over').remove();
                                                        }
                                                    });
                                                    return false;
                                                }
                                            </script>
                                            <div class="container">
                                                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                                    <div class="panel panel-default">
                                                        <div class="panel-heading" role="tab" id="headingOne">
                                                            <h4 class="panel-title">
                                                                <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                                                                    <%= sREMARK1%>
                                                                </a>
                                                            </h4>
                                                        </div>
                                                        <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
                                                            <div class="panel-body">
                                                                <div class="col-sm-5" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;">
                                                                            <script>document.write(global_fm_Description);</script>
                                                                        </label>
                                                                        <div class="col-sm-8" style="padding-left: 0;">
                                                                            <input name="nameDesc1" value="<%= sNOTE1%>" id="nameDesc1" class="form-control123" type="text"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-5" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;">
                                                                            <script>document.write(global_fm_amount);</script>
                                                                        </label>
                                                                        <div class="col-sm-8" style="padding-left: 0;">
                                                                            <input name="nameType1" value="<%= sAMOUNT1%>" id="nameType1" oninput="autoConvertMoney(this.value, $('#nameType1'))" class="form-control123" type="text"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-2" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <%
                                                                                if(Is_Exists1 != 0) {
                                                                            %>
                                                                            <input class="btn btn-info" id="btnUpdate1" type="button" onclick="EditCertControl('<%= sPAYMENT_ID1%>', '<%= sPAYMENT_TYPE_ID1%>', 'nameDesc1', 'nameType1', '<%= anticsrf%>');"/>
                                                                            <script>
                                                                                document.getElementById("btnUpdate1").value = global_fm_button_edit;
                                                                            </script>
                                                                            <%
                                                                                } else {
                                                                            %>
                                                                            <input class="btn btn-info" id="btnSave1" type="button" onclick="AddCertControl('<%= sPAYMENT_TYPE_ID1%>', 'nameDesc1', 'nameType1', '<%= anticsrf%>');"/>
                                                                            <script>
                                                                                document.getElementById("btnSave1").value = global_fm_button_add;
                                                                            </script>
                                                                            <%
                                                                                }
                                                                            %>
                                                                        </div>
                                                                        <div class="col-sm-4" style="padding-right: 0px;">
                                                                            
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="panel panel-default">
                                                        <div class="panel-heading" role="tab" id="headingTwo">
                                                            <h4 class="panel-title">
                                                                <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="true" aria-controls="collapseTwo">
                                                                    <%= sREMARK2%>
                                                                </a>
                                                            </h4>
                                                        </div>
                                                        <div id="collapseTwo" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingTwo">
                                                            <div class="panel-body">
                                                                <div class="col-sm-5" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;padding-left: 0;">
                                                                                <script>document.write(global_fm_Description);</script>
                                                                        </label>
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <input name="nameDesc2" value="<%= sNOTE2%>" id="nameDesc2" class="form-control123" type="text"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-5" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;">
                                                                            <script>document.write(global_fm_amount);</script>
                                                                        </label>
                                                                        <div class="col-sm-8" style="padding-left: 0;">
                                                                            <input name="nameType2" value="<%= sAMOUNT2%>" id="nameType2" oninput="autoConvertMoney(this.value, $('#nameType2'))" class="form-control123" type="text"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-2" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <%
                                                                                if(Is_Exists2 != 0) {
                                                                            %>
                                                                            <input class="btn btn-info" id="btnUpdate2" type="button" onclick="EditCertControl('<%= sPAYMENT_ID2%>', '<%= sPAYMENT_TYPE_ID2%>', 'nameDesc2', 'nameType2', '<%= anticsrf%>');"/>
                                                                            <script>
                                                                                document.getElementById("btnUpdate2").value = global_fm_button_edit;
                                                                            </script>
                                                                            <%
                                                                                } else {
                                                                            %>
                                                                            <input class="btn btn-info" id="btnSave2" type="button" onclick="AddCertControl('<%= sPAYMENT_TYPE_ID2%>', 'nameDesc2', 'nameType2', '<%= anticsrf%>');"/>
                                                                            <script>
                                                                                document.getElementById("btnSave2").value = global_fm_button_add;
                                                                            </script>
                                                                            <%
                                                                                }
                                                                            %>
                                                                        </div>
                                                                        <div class="col-sm-4" style="padding-right: 0px;">
                                                                            
                                                                        </div>
                                                                    </div>
                                                                </div>       
                                                            </div>
                                                        </div>
                                                    </div>
                                                    
                                                    <div class="panel panel-default">
                                                        <div class="panel-heading" role="tab" id="headingThree">
                                                            <h4 class="panel-title">
                                                                <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseThree" aria-expanded="true" aria-controls="collapseThree">
                                                                    <%= sREMARK3%>
                                                                </a>
                                                            </h4>
                                                        </div>
                                                        <div id="collapseThree" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingThree">
                                                            <div class="panel-body">
                                                                <div class="col-sm-5" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;padding-left: 0;">
                                                                            <label id="idLblTitleEmail"><script>document.write(global_fm_Description);</script></label>
                                                                        </label>
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <input name="nameDesc3" value="<%= sNOTE3%>" id="nameDesc3" class="form-control123" type="text"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-5" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;padding-left: 0;">
                                                                            <label id="idLblTitleEmail"><script>document.write(global_fm_amount);</script></label>
                                                                        </label>
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <input name="nameType3" value="<%= sAMOUNT3%>" id="nameType3" oninput="autoConvertMoney(this.value, $('#nameType3'))" class="form-control123" type="text"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-2" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <%
                                                                                if(Is_Exists3 != 0) {
                                                                            %>
                                                                            <input class="btn btn-info" id="btnUpdate3" type="button" onclick="EditCertControl('<%= sPAYMENT_ID3%>', '<%= sPAYMENT_TYPE_ID3%>', 'nameDesc3', 'nameType3', '<%= anticsrf%>');"/>
                                                                            <script>
                                                                                document.getElementById("btnUpdate3").value = global_fm_button_edit;
                                                                            </script>
                                                                            <%
                                                                                } else {
                                                                            %>
                                                                            <input class="btn btn-info" id="btnSave3" type="button" onclick="AddCertControl('<%= sPAYMENT_TYPE_ID3%>', 'nameDesc3', 'nameType3', '<%= anticsrf%>');"/>
                                                                            <script>
                                                                                document.getElementById("btnSave3").value = global_fm_button_add;
                                                                            </script>
                                                                            <%
                                                                                }
                                                                            %>
                                                                        </div>
                                                                        <div class="col-sm-4" style="padding-right: 0px;">
                                                                            
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                                                
                                                    <div class="panel panel-default">
                                                        <div class="panel-heading" role="tab" id="headingFour">
                                                            <h4 class="panel-title">
                                                                <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseFour" aria-expanded="true" aria-controls="collapseFour">
                                                                    <%= sREMARK4%>
                                                                </a>
                                                            </h4>
                                                        </div>
                                                        <div id="collapseFour" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingFour">
                                                            <div class="panel-body">
                                                                <div class="col-sm-5" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;padding-left: 0;">
                                                                            <label id="idLblTitleEmail"><script>document.write(global_fm_Description);</script></label>
                                                                        </label>
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <input name="nameDesc4" value="<%= sNOTE4%>" id="nameDesc4" class="form-control123" type="text"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-5" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;padding-left: 0;">
                                                                            <label id="idLblTitleEmail"><script>document.write(global_fm_amount);</script></label>
                                                                        </label>
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <input name="nameType4" value="<%= sAMOUNT4%>" id="nameType4" oninput="autoConvertMoney(this.value, $('#nameType4'))" class="form-control123" type="text"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-2" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <%
                                                                                if(Is_Exists4 != 0) {
                                                                            %>
                                                                            <input class="btn btn-info" id="btnUpdate4" type="button" onclick="EditCertControl('<%= sPAYMENT_ID4%>', '<%= sPAYMENT_TYPE_ID4%>', 'nameDesc4', 'nameType4', '<%= anticsrf%>');"/>
                                                                            <script>
                                                                                document.getElementById("btnUpdate4").value = global_fm_button_edit;
                                                                            </script>
                                                                            <%
                                                                                } else {
                                                                            %>
                                                                            <input class="btn btn-info" id="btnSave4" type="button" onclick="AddCertControl('<%= sPAYMENT_TYPE_ID4%>', 'nameDesc4', 'nameType4', '<%= anticsrf%>');"/>
                                                                            <script>
                                                                                document.getElementById("btnSave4").value = global_fm_button_add;
                                                                            </script>
                                                                            <%
                                                                                }
                                                                            %>
                                                                        </div>
                                                                        <div class="col-sm-4" style="padding-right: 0px;">
                                                                            
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                          
                                                    <div class="panel panel-default">
                                                        <div class="panel-heading" role="tab" id="headingFive">
                                                            <h4 class="panel-title">
                                                                <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseFive" aria-expanded="true" aria-controls="collapseFive">
                                                                    <%= sREMARK5%>
                                                                </a>
                                                            </h4>
                                                        </div>
                                                        <div id="collapseFive" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingFive">
                                                            <div class="panel-body">
                                                                <div class="col-sm-5" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;padding-left: 0;">
                                                                            <label id="idLblTitleEmail"><script>document.write(global_fm_Description);</script></label>
                                                                        </label>
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <input name="nameDesc5" value="<%= sNOTE5%>" id="nameDesc5" class="form-control123" type="text"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-5" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;padding-left: 0;">
                                                                            <label id="idLblTitleEmail"><script>document.write(global_fm_amount);</script></label>
                                                                        </label>
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <input name="nameType5" value="<%= sAMOUNT5%>" id="nameType5" oninput="autoConvertMoney(this.value, $('#nameType5'))" class="form-control123" type="text"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-2" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <%
                                                                                if(Is_Exists5 != 0) {
                                                                            %>
                                                                            <input class="btn btn-info" id="btnUpdate5" type="button" onclick="EditCertControl('<%= sPAYMENT_ID5%>', '<%= sPAYMENT_TYPE_ID5%>', 'nameDesc5', 'nameType5', '<%= anticsrf%>');"/>
                                                                            <script>
                                                                                document.getElementById("btnUpdate5").value = global_fm_button_edit;
                                                                            </script>
                                                                            <%
                                                                                } else {
                                                                            %>
                                                                            <input class="btn btn-info" id="btnSave5" type="button" onclick="AddCertControl('<%= sPAYMENT_TYPE_ID5%>', 'nameDesc5', 'nameType5', '<%= anticsrf%>');"/>
                                                                            <script>
                                                                                document.getElementById("btnSave5").value = global_fm_button_add;
                                                                            </script>
                                                                            <%
                                                                                }
                                                                            %>
                                                                        </div>
                                                                        <div class="col-sm-4" style="padding-right: 0px;">
                                                                            
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                                                
                                                    <div class="panel panel-default">
                                                        <div class="panel-heading" role="tab" id="headingSix">
                                                            <h4 class="panel-title">
                                                                <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseSix" aria-expanded="true" aria-controls="collapseSix">
                                                                    <%= sREMARK6%>
                                                                </a>
                                                            </h4>
                                                        </div>
                                                        <div id="collapseSix" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingSix">
                                                            <div class="panel-body">
                                                                <div class="col-sm-5" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;padding-left: 0;">
                                                                            <label id="idLblTitleEmail"><script>document.write(global_fm_Description);</script></label>
                                                                        </label>
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <input name="nameDesc6" value="<%= sNOTE6%>" id="nameDesc6" class="form-control123" type="text"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-5" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;padding-left: 0;">
                                                                            <label id="idLblTitleEmail"><script>document.write(global_fm_amount);</script></label>
                                                                        </label>
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <input name="nameType6" value="<%= sAMOUNT6%>" id="nameType6" oninput="autoConvertMoney(this.value, $('#nameType6'))" class="form-control123" type="text"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-2" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <%
                                                                                if(Is_Exists6 != 0) {
                                                                            %>
                                                                            <input class="btn btn-info" id="btnUpdate6" type="button" onclick="EditCertControl('<%= sPAYMENT_ID6%>', '<%= sPAYMENT_TYPE_ID6%>', 'nameDesc6', 'nameType6', '<%= anticsrf%>');"/>
                                                                            <script>
                                                                                document.getElementById("btnUpdate6").value = global_fm_button_edit;
                                                                            </script>
                                                                            <%
                                                                                } else {
                                                                            %>
                                                                            <input class="btn btn-info" id="btnSave6" type="button" onclick="AddCertControl('<%= sPAYMENT_TYPE_ID6%>', 'nameDesc6', 'nameType6', '<%= anticsrf%>');"/>
                                                                            <script>
                                                                                document.getElementById("btnSave6").value = global_fm_button_add;
                                                                            </script>
                                                                            <%
                                                                                }
                                                                            %>
                                                                        </div>
                                                                        <div class="col-sm-4" style="padding-right: 0px;">
                                                                            
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                                                
                                                    <div class="panel panel-default">
                                                        <div class="panel-heading" role="tab" id="headingSeven">
                                                            <h4 class="panel-title">
                                                                <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseSeven" aria-expanded="true" aria-controls="collapseSeven">
                                                                    <%= sREMARK7%>
                                                                </a>
                                                            </h4>
                                                        </div>
                                                        <div id="collapseSeven" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingSeven">
                                                            <div class="panel-body">
                                                                <div class="col-sm-5" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;padding-left: 0;">
                                                                            <label id="idLblTitleEmail"><script>document.write(global_fm_Description);</script></label>
                                                                        </label>
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <input name="nameDesc7" value="<%= sNOTE7%>" id="nameDesc7" class="form-control123" type="text"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-5" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;padding-left: 0;">
                                                                            <label id="idLblTitleEmail"><script>document.write(global_fm_amount);</script></label>
                                                                        </label>
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <input name="nameType7" value="<%= sAMOUNT7%>" id="nameType7" oninput="autoConvertMoney(this.value, $('#nameType7'))" class="form-control123" type="text"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-2" style="padding-left: 0;">
                                                                    <div class="form-group">
                                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                                            <%
                                                                                if(Is_Exists7 != 0) {
                                                                            %>
                                                                            <input class="btn btn-info" id="btnUpdate7" type="button" onclick="EditCertControl('<%= sPAYMENT_ID7%>', '<%= sPAYMENT_TYPE_ID7%>', 'nameDesc7', 'nameType7', '<%= anticsrf%>');"/>
                                                                            <script>
                                                                                document.getElementById("btnUpdate7").value = global_fm_button_edit;
                                                                            </script>
                                                                            <%
                                                                                } else {
                                                                            %>
                                                                            <input class="btn btn-info" id="btnSave7" type="button" onclick="AddCertControl('<%= sPAYMENT_TYPE_ID7%>', 'nameDesc7', 'nameType7', '<%= anticsrf%>');"/>
                                                                            <script>
                                                                                document.getElementById("btnSave7").value = global_fm_button_add;
                                                                            </script>
                                                                            <%
                                                                                }
                                                                            %>
                                                                        </div>
                                                                        <div class="col-sm-4" style="padding-right: 0px;">
                                                                            
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
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
                                        <h3 class="modal-title" style="font-size: 18px;"><script>document.write(global_fm_cert_list)</script></h3>
                                    </div>
                                    <div style="width: 29%; float: right;text-align: right;">
                                        <button type="button" onclick="CloseDialog();" class="btn btn-info"><script>document.write(global_fm_button_close)</script></button>
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