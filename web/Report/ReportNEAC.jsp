<%-- 
    Document   : ReportNEAC
    Created on : Oct 18, 2018, 9:09:39 AM
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
            document.title = reportneac_title_list;
            changeFavicon("../");
            $(document).ready(function () {
                $('.loading-gif').hide();
                localStorage.setItem("sessStoreTag_IDNEAC2", null);
                localStorage.setItem("sessStoreTag_IDNEAC", null);
//                $('[data-toggle="tooltipPrefix"]').tooltip();
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
            $(document).ready(function () {
                $('.loading-gifHardware2').hide();
                $('#myModalOTPHardware2').modal({
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
            }.x_panel {
                padding:15px 17px 0 17px;
            }
            .x_content {
                padding: 0 5px 0 5px;
            }
            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
            .table > thead > tr > th{border-bottom: none;}
            .btn{margin-bottom: 0px;}
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
                        document.getElementById("idNameURL").innerHTML = reportneac_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%
                                int status = 1000;
                                int statusLoad = 0;
                                try {
                                    REPORT_CONTROL_NEAC[][] rsReportControl = new REPORT_CONTROL_NEAC[1][];
                                    REPORT_RECURRING_NEAC[][] rsReportRecurring = new REPORT_RECURRING_NEAC[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (request.getMethod().equals("POST")) {
                                        String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                        statusLoad = 1;
                                        request.setCharacterEncoding("UTF-8");
                                        String FromCreateDate = request.getParameter("idYear");
                                        String ToCreateDate = request.getParameter("idMounth");
                                        session.setAttribute("sessYearDateNEACControl", FromCreateDate);
                                        session.setAttribute("sessMountDateNEACControl", ToCreateDate);
                                        db.S_BO_REPORT_NEAC_LIST(ToCreateDate, FromCreateDate, Integer.parseInt(isCALoad), rsReportControl);
                                        db.S_BO_REPORT_PERIODIC_LIST(ToCreateDate, FromCreateDate, sessLanguageGlobal, Integer.parseInt(isCALoad), rsReportRecurring);
                                        if (rsReportControl[0].length > 0 || rsReportRecurring[0].length > 0) {
                                            status = 1;
                                        } else {
                                            status = 1000;
                                        }
                                    } else {
                                        session.setAttribute("sessMountDateNEACControl", null);
                                        session.setAttribute("sessYearDateNEACControl", null);
                                    }
                                %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="form" method="post" class="form-horizontal">
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <div class="col-sm-5" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_Quater);</script></label>
                                                        <div class="col-sm-8" style="padding-right: 0px;">
                                                            <select name="idMounth" id="idMounth" class="form-control123">
                                                                <option value="1" <%= session.getAttribute("sessMountDateNEACControl") != null
                                                                    && session.getAttribute("sessMountDateNEACControl").toString().trim().equals("1") ? "selected" : "" %>>I</option>
                                                                <option value="2" <%= session.getAttribute("sessMountDateNEACControl") != null
                                                                    && session.getAttribute("sessMountDateNEACControl").toString().trim().equals("2") ? "selected" : "" %>>II</option>
                                                                <option value="3" <%= session.getAttribute("sessMountDateNEACControl") != null
                                                                    && session.getAttribute("sessMountDateNEACControl").toString().trim().equals("3") ? "selected" : "" %>>III</option>
                                                                <option value="4" <%= session.getAttribute("sessMountDateNEACControl") != null
                                                                    && session.getAttribute("sessMountDateNEACControl").toString().trim().equals("4") ? "selected" : "" %>>IV</option>
                                                            </select>
                                                            <%
                                                                if(session.getAttribute("sessMountDateNEACControl") == null)
                                                                {
                                                            %>
                                                            <script type="text/javascript">
                                                                $(document).ready(function () {
                                                                    var dDate = new Date();
                                                                    var sTime = dDate.getMonth();
                                                                    var sValueEmbed = "1";
                                                                    if(sTime === 0 || sTime === 1 || sTime === 2)
                                                                    {
                                                                        sValueEmbed = "1";
                                                                    } else if(sTime === 3 || sTime === 4 || sTime === 5)
                                                                    {
                                                                        sValueEmbed = "2";
                                                                    } else if(sTime === 6 || sTime === 7 || sTime === 8)
                                                                    {
                                                                        sValueEmbed = "3";
                                                                    }  else if(sTime === 9 || sTime === 10 || sTime === 11)
                                                                    {
                                                                        sValueEmbed = "4";
                                                                    } else {}
                                                                    $("#idMounth").val(sValueEmbed);
                                                                });
                                                            </script>
                                                            <%
                                                                }
                                                            %>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-5" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_year);</script></label>
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
                                                                <option value="<%=sYearAfterID%>" <%= session.getAttribute("sessYearDateNEACControl") != null
                                                                    && session.getAttribute("sessYearDateNEACControl").toString().trim().equals(sYearAfterID) ? "selected" : "" %>><%=sYearAfterID%></option>
                                                                <%
                                                                    }
                                                                %>
                                                            </select>
                                                            <%
                                                                if(session.getAttribute("sessYearDateNEACControl") == null)
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
                                                <div class="col-sm-2" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
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
                                    <div class="x_content">
                                        <form name="myname" method="post" class="form-horizontal">
                                            <div class="" role="tabpanel" data-example-id="togglable-tabs">
                                                <input id="idTempChoiseTypeKey" name="idTempChoiseTypeKey" type="text" style="display: none;" />
                                                <ul id="myTabTypeKey" class="nav nav-tabs bar_tabs" role="tablist">
                                                    <li role="presentation" class="active" id="idLi_contentKeyUse">
                                                        <a href="#tab_contentKeyUse" role="tab" id="profile-tab2" data-toggle="tab" aria-expanded="true">
                                                            <script>document.write(reportneac_fm_tab_control);</script>
                                                        </a>
                                                    </li>
                                                    <li role="presentation" class="" id="idLi_contentKeyNew">
                                                        <a href="#tab_contentKeyNew" id="home-tab" role="tab" data-toggle="tab" aria-expanded="false">
                                                            <script>document.write(reportneac_fm_tab_recurring);</script>
                                                        </a>
                                                    </li>
                                                </ul>
                                                <div id="myTabContentTypeKey" class="tab-content">
                                                    <div role="tabpanel" class="tab-pane fade active in" id="tab_contentKeyUse" aria-labelledby="home-tab1">
                                                        <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                                            <h2><i class="fa fa-list-ul"></i> <script>document.write(reportneac_fm_tab_control);</script></h2>
                                                            <ul class="nav navbar-right panel_toolbox">
                                                                <li style="color: red;font-weight: bold;">
                                                                    <%
                                                                        if(rsReportControl[0].length > 0) {
                                                                    %>
                                                                    <button type="button" class="btn btn-info" onClick="PrintControlCert('<%= anticsrf%>');"><script>document.write(global_fm_button_export_word);</script></button>
                                                                    <script>
                                                                        function PrintControlCert(idCSRF)
                                                                        {
                                                                            $('body').append('<div id="over"></div>');
                                                                            $(".loading-gif").show();
                                                                            $.ajax({
                                                                                type: "post",
                                                                                url: "../PrintFormCommon",
                                                                                data: {
                                                                                    idParam: "wordcontrolcert",
                                                                                    sDate: global_fm_report_date,
                                                                                    sPrefixContent: reportneac_fm_control_content,
                                                                                    CsrfToken: idCSRF
                                                                                },
                                                                                catche: false,
                                                                                success: function (html) {
                                                                                    var arr = sSpace(html).split('###');
                                                                                    if (arr[0] === "0")
                                                                                    {
                                                                                        var f = document.myname;
                                                                                        f.method = "post";
                                                                                        f.action = '../DownFromSaveFile?idParam=downfileword&name=' + arr[2];
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
                                                                    <%
                                                                        }
                                                                    %>
                                                                </li>
                                                            </ul>
                                                            <div class="clearfix"></div>
                                                        </div>
                                                        <div class="x_content">
                                                            <%
                                                                if(rsReportControl[0].length > 0) {
                                                            %>
                                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                                            <style type="text/css">
                                                                .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                                                .btn{margin-bottom: 0px;}
                                                            </style>
                                                            <div class="table-responsive">
                                                            <table id="mytable" class="table table-bordered table-striped projects">
                                                                <thead>
                                                                <th><script>document.write(global_fm_STT);</script></th>
                                                                <th style="width: 650px;"><script>document.write(global_fm_Content);</script></th>
                                                                <th><script>document.write(global_fm_cert_count);</script></th>
                                                                </thead>
                                                                <tbody>
                                                                    <tr>
                                                                        <td>1</td>
                                                                        <td><script>document.write(reportneac_fm_control_content);</script> <%= EscapeUtils.CheckTextNull(rsReportControl[0][0].TO_DATE)%></td>
                                                                        <td>
                                                                            <a style="cursor: pointer; text-decoration: underline;color: blue;" href="#myModalOTPHardware1" data-toggle="modal" data-book-id="1"> <%= rsReportControl[0][0].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportControl[0][0].TOTAL_ENTERPRISE) : "0" %></a>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>2</td>
                                                                        <td><script>document.write(reportneac_fm_control_content);</script> <%= EscapeUtils.CheckTextNull(rsReportControl[0][1].TO_DATE)%></td>
                                                                        <td>
                                                                            <a style="cursor: pointer; text-decoration: underline;color: blue;" href="#myModalOTPHardware1" data-toggle="modal" data-book-id="2"> <%= rsReportControl[0][1].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportControl[0][1].TOTAL_ENTERPRISE) : "0" %></a>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>3</td>
                                                                        <td><script>document.write(reportneac_fm_control_content);</script> <%= EscapeUtils.CheckTextNull(rsReportControl[0][2].TO_DATE)%></td>
                                                                        <td>
                                                                            <a style="cursor: pointer; text-decoration: underline;color: blue;" href="#myModalOTPHardware1" data-toggle="modal" data-book-id="3"> <%= rsReportControl[0][2].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportControl[0][2].TOTAL_ENTERPRISE) : "0" %></a>
                                                                        </td>
                                                                    </tr>
                                                                </tbody>
                                                            </table>
                                                            </div>
                                                            <%
                                                                }
                                                            %>
                                                        </div>
                                                    </div>
                                                    <div role="tabpanel" class="tab-pane fade" id="tab_contentKeyNew" aria-labelledby="home-tab">
                                                        <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                                            <h2><i class="fa fa-list-ul"></i> <script>document.write(reportneac_fm_tab_recurring);</script></h2>
                                                            <ul class="nav navbar-right panel_toolbox">
                                                                <li style="color: red;font-weight: bold;">
                                                                    <%
                                                                        if(rsReportRecurring[0].length > 0) {
                                                                    %>
                                                                    <button type="button" class="btn btn-info" onClick="PrintRecurringCert('<%= anticsrf%>');"><script>document.write(global_fm_button_export_word);</script></button>
                                                                    <script>
                                                                        function PrintRecurringCert(idCSRF)
                                                                        {
                                                                            $('body').append('<div id="over"></div>');
                                                                            $(".loading-gif").show();
                                                                            $.ajax({
                                                                                type: "post",
                                                                                url: "../PrintFormCommon",
                                                                                data: {
                                                                                    idParam: "wordrecurringcert_new",
                                                                                    sDate: global_fm_report_date,
                                                                                    CsrfToken: idCSRF
                                                                                },
                                                                                catche: false,
                                                                                success: function (html) {
                                                                                    var arr = sSpace(html).split('###');
                                                                                    if (arr[0] === "0")
                                                                                    {
                                                                                        var f = document.myname;
                                                                                        f.method = "post";
                                                                                        f.action = '../DownFromSaveFile?idParam=downfileword&name=' + arr[2];
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
                                                                    <%
                                                                        }
                                                                    %>
                                                                </li>
                                                            </ul>
                                                            <div class="clearfix"></div>
                                                        </div>
                                                        <div class="x_content">
                                                            <%
                                                                if(rsReportRecurring[0].length > 0) {
                                                            %>
                                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                                            <div class="table-responsive">
                                                            <table id="mytable" class="table table-bordered table-striped projects">
                                                                <thead>
                                                                <th><script>document.write(global_fm_STT);</script></th>
                                                                <th><script>document.write(reportneac_fm_cert_enterprise);</script></th>
                                                                <th><script>document.write(reportneac_fm_cert_staff);</script></th>
                                                                <th><script>document.write(reportneac_fm_cert_personal);</script></th>
                                                                <th><script>document.write(global_fm_Status);</script></th>
                                                                <th><script>document.write(reportquick_fm_total);</script></th>
                                                                </thead>
                                                                <tbody>
                                                                    <%
                                                                        int i=1;
                                                                        for(REPORT_RECURRING_NEAC rsReportRecurring1 : rsReportRecurring[0]) {
                                                                            String sRecurEnter = Definitions.CONFIG_CERTTYPE_DESC_ENTERPRISE + "#" + rsReportRecurring1.STATUS_NAME;
                                                                            String sRecurPersonal = Definitions.CONFIG_CERTTYPE_DESC_PERSONAL + "#" + rsReportRecurring1.STATUS_NAME;
                                                                            String sRecurStaff = Definitions.CONFIG_CERTTYPE_DESC_STAFF + "#" + rsReportRecurring1.STATUS_NAME;
                                                                    %>
                                                                    <tr>
                                                                        <td><%= com.convertMoney(i) %></td>
                                                                        <td>
                                                                            <a style="cursor: pointer; text-decoration: underline;color: blue;" href="#myModalOTPHardware2" data-toggle="modal" data-book-id="<%= sRecurEnter%>">
                                                                                <%= rsReportRecurring1.TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring1.TOTAL_ENTERPRISE) : "0"%>
                                                                            </a>
                                                                        </td>
                                                                        <td>
                                                                            <a style="cursor: pointer; text-decoration: underline;color: blue;" href="#myModalOTPHardware2" data-toggle="modal" data-book-id="<%= sRecurStaff%>">
                                                                                <%= rsReportRecurring1.TOTAL_STAFF != 0 ? com.convertMoney(rsReportRecurring1.TOTAL_STAFF) : "0" %>
                                                                            </a>
                                                                        </td>
                                                                        <td>
                                                                            <a style="cursor: pointer; text-decoration: underline;color: blue;" href="#myModalOTPHardware2" data-toggle="modal" data-book-id="<%= sRecurPersonal%>">
                                                                                <%= rsReportRecurring1.TOTAL_PERSONAL != 0 ? com.convertMoney(rsReportRecurring1.TOTAL_PERSONAL) : "0"%>
                                                                            </a>
                                                                        </td>
                                                                        <td><%= EscapeUtils.CheckTextNull(rsReportRecurring1.STATUS)%></td>
                                                                        <td><%= rsReportRecurring1.SUM != 0 ? com.convertMoney(rsReportRecurring1.SUM) : "0"%></td>
                                                                    </tr>
                                                                    <%
                                                                            i++;
                                                                        }
                                                                    %>
                                                                </tbody>
                                                            </table>
                                                            </div>
                                                            <%
                                                                }
                                                            %>
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
                            localStorage.setItem("sessStoreTag_IDNEAC", null);
                            $.ajax({
                                type: "post",
                                url: "../JSONCommon",
                                data: {
                                    idParam: 'neaclistcertlist',
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
                                            localStorage.setItem("sessStoreTag_IDNEAC", Tag_ID);
                                            var content = "";
                                            $("#idTemplateAssign1").empty();
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
                                                        '<td><a style="color: blue;" data-toggle="tooltipPrefix" title="'+ vMST_MNS_REMARK +'">'+vMST_MNS+'</a></td>' +
                                                        '<td>' + obj[i].PERSONAL_NAME + '</td>' +
                                                        '<td><a style="color: blue;" data-toggle="tooltipPrefix" title="'+ vCMND_HC_REMARK +'">'+vCMND_HC+'</a></td>' +
                                                        '<td>' + obj[i].DOMAIN_NAME + '</td>' +
                                                        '<td>' + obj[i].PKI_FORMFACTOR_DESC + '</td>' +
                                                        '<td>' + obj[i].CERTIFICATION_PROFILE_NAME + '</td>' +
                                                        '<td>' + obj[i].CERTIFICATION_PURPOSE_DESC + '</td>' +
                                                        '<td>' + obj[i].CERTIFICATION_STATE_DESC + '</td>' +
                                                        '<td>' + obj[i].CERTIFICATION_ATTR_TYPE_DESC + '</td>' +
                                                        '<td>' + obj[i].CREATED_BY + '</td>' +
                                                        '<td>' + obj[i].BRANCH_DESC + '</td>' +
                                                        '<td>' + obj[i].CREATED_DT + '</td>' +
                                                        '<td>' + obj[i].REVOKED_DT + '</td>' +
                                                        '</tr>';
                                                j++;
                                            }
                                            $("#idTemplateAssign1").append(content);
                                            if (parseInt(sNum) > 0)
                                            {
                                                $('#green').smartpaginator({totalrecords: sNum, recordsperpage: 10, datacontainer: 'tblCertUse1', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
                                            }
                                        }
                                        else if (obj[0].Code === "1")
                                        {
                                            $("#idTemplateAssign1").empty();
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
                                            $("#idTemplateAssign1").empty();
                                            funErrorAlert(global_errorsql);
                                        }
                                    }
                                    else
                                    {
                                        $("#idTemplateAssign1").empty();
                                    }
                                }
                            });
                        }
                        function LoadCert2(Tag_ID)
                        {
                            localStorage.setItem("sessStoreTag_IDNEAC2", null);
                            $.ajax({
                                type: "post",
                                url: "../JSONCommon",
                                data: {
                                    idParam: 'neaclistcertlist2',
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
                                            localStorage.setItem("sessStoreTag_IDNEAC2", Tag_ID);
                                            var content = "";
                                            $("#idTemplateAssign2").empty();
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
                                                        '<td><a style="color: blue;" data-toggle="tooltipPrefix" title="'+ vMST_MNS_REMARK +'">'+vMST_MNS+'</a></td>' +
                                                        '<td>' + obj[i].PERSONAL_NAME + '</td>' +
                                                        '<td><a style="color: blue;" data-toggle="tooltipPrefix" title="'+ vCMND_HC_REMARK +'">'+vCMND_HC+'</a></td>' +
                                                        '<td>' + obj[i].DOMAIN_NAME + '</td>' +
                                                        '<td>' + obj[i].PKI_FORMFACTOR_DESC + '</td>' +
                                                        '<td>' + obj[i].CERTIFICATION_PROFILE_NAME + '</td>' +
                                                        '<td>' + obj[i].CERTIFICATION_PURPOSE_DESC + '</td>' +
                                                        '<td>' + obj[i].CERTIFICATION_STATE_DESC + '</td>' +
                                                        '<td>' + obj[i].CERTIFICATION_ATTR_TYPE_DESC + '</td>' +
                                                        '<td>' + obj[i].CREATED_BY + '</td>' +
                                                        '<td>' + obj[i].BRANCH_DESC + '</td>' +
                                                        '<td>' + obj[i].CREATED_DT + '</td>' +
                                                        '<td>' + obj[i].REVOKED_DT + '</td>' +
                                                        '</tr>';
                                                j++;
                                            }
                                            $("#idTemplateAssign2").append(content);
                                            if (parseInt(sNum) > 0)
                                            {
                                                $('#green2').smartpaginator({totalrecords: sNum, recordsperpage: 10, datacontainer: 'tblCertUse2', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
                                            }
                                        }
                                        else if (obj[0].Code === "1")
                                        {
                                            $("#idTemplateAssign2").empty();
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
                                            $("#idTemplateAssign2").empty();
                                            funErrorAlert(global_errorsql);
                                        }
                                    }
                                    else
                                    {
                                        $("#idTemplateAssign2").empty();
                                    }
                                }
                            });
                        }
                        $(document).ready(function () {
                            $('#myModalOTPHardware1').on('show.bs.modal', function (e) {
                                var Tag_ID = $(e.relatedTarget).data('book-id');
                                LoadCert(Tag_ID);
                            });
                        });
                        function CloseDialog()
                        {
                            $('#myModalOTPHardware1').modal('hide');
                            $(".loading-gifHardware").hide();
                            $(".loading-gif").hide();
                            $('#over').remove();
                        }
                        $(document).ready(function () {
                            $('#myModalOTPHardware2').on('show.bs.modal', function (e) {
                                var Tag_ID = $(e.relatedTarget).data('book-id');
                                LoadCert2(Tag_ID);
                            });
                        });
                        function CloseDialog2()
                        {
                            $('#myModalOTPHardware2').modal('hide');
                            $(".loading-gifHardware2").hide();
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
                                if (tr.hasClass('collapse_abc')) {
                                    tr.removeClass('collapse_abc').addClass('expand_abc');
                                    children.hide();
                                }
                            });
                            $('#mytable').on('click', '.toggle_abc', function () {
                                var findChildren = function (tr) {
                                    var depth = tr.data('depth');
                                    return tr.nextUntil($('tr').filter(function () {
                                        return $(this).data('depth') <= depth;
                                    }));
                                };
                                var el = $(this);
                                var tr = el.closest('tr');
                                var children = findChildren(tr);
                                var subnodes = children.filter('.expand_abc');
                                subnodes.each(function () {
                                    var subnode = $(this);
                                    var subnodeChildren = findChildren(subnode);
                                    children = children.not(subnodeChildren);
                                });
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
                        function calExportList()
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../ExportCSVParam",
                                data: {
                                    idParam: "neacexportdetaillist",
                                    Tag_ID: localStorage.getItem("sessStoreTag_IDNEAC")
                                },
                                catche: false,
                                success: function (html) {
                                    var arr = sSpace(html).split('#');
                                    if (arr[0] === "0")
                                    {
                                        var f = document.formOTPHardware;
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
                        
                        function calExportList2()
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../ExportCSVParam",
                                data: {
                                    idParam: "neacexportdetaillist2",
                                    Tag_ID: localStorage.getItem("sessStoreTag_IDNEAC2")
                                },
                                catche: false,
                                success: function (html) {
                                    var arr = sSpace(html).split('#');
                                    if (arr[0] === "0")
                                    {
                                        var f = document.formOTPHardware2;
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
                    
                    <style>
                        @media (min-width: 768px) {
                            .modal-dialog {
                                width: 1200px;
                                margin: 30px auto;
                            }
                        }
                    </style>
                    <div id="myModalOTPHardware1" class="modal fade" role="dialog">
                        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                             left: 0; height: 100%;" class="loading-gifHardware">
                            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
                        </div>
                        <div class="modal-dialog modal-800" id="myDialogOTPHardware1">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <div style="width: 70%; float: left;">
                                        <h3 class="modal-title" style="font-size: 18px;"><script>document.write(global_fm_cert_list);</script></h3>
                                    </div>
                                    <div style="width: 29%; float: right;text-align: right;">
                                        <button type="button" onclick="calExportList();" class="btn btn-info"><script>document.write(global_fm_button_export_csv);</script></button>
                                        <button type="button" onclick="CloseDialog();" class="btn btn-info"><script>document.write(global_fm_button_close);</script></button>
                                    </div>
                                </div>
                                <div class="modal-body">
                                    <form role="formAddOTPHardware" id="formOTPHardware" name="formOTPHardware">
                                        <div class="table-responsive">
                                            <table id="tblCertUse1" class="table table-striped projects">
                                                <thead>
                                                <th><script>document.write(global_fm_STT);</script></th>
                                                <th style="width: 120px;"><script>document.write(global_fm_grid_company);</script></th>
                                                <th style="width: 100px;"><script>document.write(global_fm_enterprise_id);</script></th>
                                                <th style="width: 120px;"><script>document.write(global_fm_grid_personal);</script></th>
                                                <th style="width: 100px;"><script>document.write(global_fm_personal_id);</script></th>
                                                <th style="width: 120px;"><script>document.write(global_fm_grid_domain);</script></th>
                                                <th><script>document.write(global_fm_Method);</script></th>
                                                <th><script>document.write(global_fm_duration_cts);</script></th>
                                                <th><script>document.write(global_fm_certtype);</script></th>
                                                <!--<th style="width: 80px;"><script>document.write(global_fm_Status_cert);</script></th>-->
                                                <th><script>document.write(global_fm_Status_request);</script></th>
                                                <th><script>document.write(cert_fm_type_request);</script></th>
                                                <th><script>document.write(global_fm_user_create);</script></th>
                                                <th><script>document.write(token_fm_agent);</script></th>
                                                <th><script>document.write(global_fm_date_create);</script></th>
                                                <th><script>document.write(global_fm_date_revoke);</script></th>
                                                </thead>
                                                <tbody id="idTemplateAssign1"></tbody>
                                            </table>
                                        </div>
                                        <div id="green" style="margin: 5px 0 10px 0;width: 100%;"> </div>
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
                    
                    <div id="myModalOTPHardware2" class="modal fade" role="dialog">
                        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                             left: 0; height: 100%;" class="loading-gifHardware2">
                            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
                        </div>
                        <div class="modal-dialog modal-800" id="myDialogOTPHardware2">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <div style="width: 70%; float: left;">
                                        <h3 class="modal-title" style="font-size: 18px;"><script>document.write(global_fm_cert_list);</script></h3>
                                    </div>
                                    <div style="width: 29%; float: right;text-align: right;">
                                        <button type="button" onclick="calExportList2();" class="btn btn-info"><script>document.write(global_fm_button_export_csv);</script></button>
                                        <button type="button" onclick="CloseDialog2();" class="btn btn-info"><script>document.write(global_fm_button_close);</script></button>
                                    </div>
                                </div>
                                <div class="modal-body">
                                    <form role="formAddOTPHardware2" id="formOTPHardware2" name="formOTPHardware2">
                                        <div class="table-responsive">
                                            <table id="tblCertUse2" class="table table-striped projects">
                                                <thead>
                                                <th><script>document.write(global_fm_STT);</script></th>
                                                <th style="width: 120px;"><script>document.write(global_fm_grid_company);</script></th>
                                                <th style="width: 100px;"><script>document.write(global_fm_enterprise_id);</script></th>
                                                <th style="width: 120px;"><script>document.write(global_fm_grid_personal);</script></th>
                                                <th style="width: 100px;"><script>document.write(global_fm_personal_id);</script></th>
                                                <th style="width: 120px;"><script>document.write(global_fm_grid_domain);</script></th>
                                                <th><script>document.write(global_fm_Method);</script></th>
                                                <th><script>document.write(global_fm_duration_cts);</script></th>
                                                <th><script>document.write(global_fm_certtype);</script></th>
                                                <!--<th style="width: 80px;"><script>document.write(global_fm_Status_cert);</script></th>-->
                                                <th><script>document.write(global_fm_Status_request);</script></th>
                                                <th><script>document.write(cert_fm_type_request);</script></th>
                                                <th><script>document.write(global_fm_user_create);</script></th>
                                                <th><script>document.write(token_fm_agent);</script></th>
                                                <th><script>document.write(global_fm_date_create);</script></th>
                                                <th><script>document.write(global_fm_date_revoke);</script></th>
                                                </thead>
                                                <tbody id="idTemplateAssign2"></tbody>
                                            </table>
                                        </div>
                                        <div id="green2" style="margin: 5px 0 10px 0;width: 100%;"> </div>
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