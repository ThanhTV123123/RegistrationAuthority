<%-- 
    Document   : ReportESignRemain
    Created on : Aug 5, 2022, 3:27:22 PM
    Author     : vanth
--%>

<%@page import="vn.ra.process.ConnectDatabaseReport"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/CommonPagingList.jsp" %>
<%@include file="../Admin/ConnectionParam.jsp" %>
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
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script language="javascript">
            changeFavicon("../");
            document.title = esignremain_title_list;
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
        </script>
    </head>
    <body class="nav-md">
        <%
            if ((session.getAttribute("sUserID")) != null) {
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
                        document.getElementById("idNameURL").innerHTML = esignremain_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <%
                                        String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_FPT) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_MID)){
                                            CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                            try {
                                                int iTotRslts = com.Converter(request.getParameter("iTotRslts"));
                                                int iTotPags = com.Converter(request.getParameter("iTotPags"));
                                                String tempIPagNo = request.getParameter("iPagNo");
                                                int iPagNo = com.Converter(tempIPagNo);
                                                int iPaNoSS = iPagNo;
                                                int cPagNo = com.Converter(request.getParameter("cPagNo"));
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
                                                ConnectDatabaseReport dbReport = new ConnectDatabaseReport();
                                                if (session.getAttribute("SessRefreshEsignRemain") != null) {
                                                    session.setAttribute("SessRefreshEsignRemain", null);
                                                    int ss = dbReport.SP_BO_TMS_REPORT_CERTIFICATE_TOTAL();
                                                    if (ss > 0) {
                                                        if (session.getAttribute("IPageNoSessListEasignRemain") != null) {
                                                            String sPage = (String) session.getAttribute("IPageNoSessListEasignRemain");
                                                            iPagNo = Integer.parseInt(sPage);
                                                        }
                                                        if (session.getAttribute("ISwRwsSessListEasignRemain") != null) {
                                                            String sSumPage = (String) session.getAttribute("ISwRwsSessListEasignRemain");
                                                            iSwRws = Integer.parseInt(sSumPage);
                                                        }
                                                        if (session.getAttribute("NumPageSessListEasignRemain") != null) {
                                                            String sNoPage = (String) session.getAttribute("NumPageSessListEasignRemain");
                                                            iPaNoSS = Integer.parseInt(sNoPage);
                                                        }
                                                        session.setAttribute("IPageNoSessListEasignRemain", String.valueOf(iPagNo));
                                                        session.setAttribute("ISwRwsSessListEasignRemain", String.valueOf(iSwRws));
                                                        dbReport.SP_BO_TMS_REPORT_CERTIFICATE_LIST(iPagNo, iSwRws, rsPgin);
                                                    }
                                                    iTotRslts = ss;
                                                }
                                                else
                                                {
                                                    session.setAttribute("NumPageSessListEasignRemain", null);
                                                    session.setAttribute("IPageNoSessListEasignRemain", null);
                                                    session.setAttribute("ISwRwsSessListEasignRemain", null);
                                                    if (hasPaging != true) {
                                                        session.setAttribute("CountListEasignRemain", null);
                                                    }
                                                    int ss = 0;
                                                    if ((session.getAttribute("CountListEasignRemain")) == null) {
                                                        ss = dbReport.SP_BO_TMS_REPORT_CERTIFICATE_TOTAL();
                                                        session.setAttribute("CountListEasignRemain", String.valueOf(ss));
                                                    } else {
                                                        String sCount = (String) session.getAttribute("CountListEasignRemain");
                                                        ss = Integer.parseInt(sCount);
                                                        session.setAttribute("CountListEasignRemain", String.valueOf(ss));
                                                    }
                                                    if (ss > 0) {
                                                        dbReport.SP_BO_TMS_REPORT_CERTIFICATE_LIST(iPagNo, iSwRws, rsPgin);
                                                        session.setAttribute("IPageNoSessListEasignRemain", String.valueOf(iPagNo));
                                                        session.setAttribute("ISwRwsSessListEasignRemain", String.valueOf(iSwRws));
                                                    }
                                                    iTotRslts = ss;
                                                }
                                    %>
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(esignremain_title_table);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <button type="button" class="btn btn-info" onClick="ExportReportList('<%= String.valueOf(iTotRslts)%>', '<%= anticsrf%>');"><script>document.write(global_fm_button_export_csv);</script></button>
                                                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                                <script>
                                                    function ExportReportList(countList, idCSRF)
                                                    {
                                                        $('body').append('<div id="over"></div>');
                                                        $(".loading-gif").show();
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../ExportCSVParam",
                                                            data: {
                                                                idParam: "exportesignclousremainlist",
                                                                countList: countList,
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
                                                </script>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <form name="form" method="post">
                                        <input type="hidden" name="iPagNo" value="<%=iPagNo%>">
                                        <input type="hidden" name="cPagNo" value="<%=cPagNo%>">
                                        <input type="hidden" name="iSwRws" value="<%=iSwRws%>">
                                        <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                        <div class="x_content">
                                            <style type="text/css">
                                                .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                                .table > thead > tr > th{border-bottom: none;}
                                                .btn{margin-bottom: 0px;}
                                                .panel_toolbox { min-width: 0;}
                                            </style>
                                            <div class="table-responsive">
                                                <table id="idTableList" class="table table-bordered table-striped projects">
                                                    <thead>
                                                    <th><script>document.write(global_fm_STT);</script></th>
                                                    <th><script>document.write(global_fm_serial);</script></th>
                                                    <th><script>document.write(token_fm_thumbprintcert);</script></th>
                                                    <th><script>document.write(global_fm_remainingSigning_agreement);</script></th>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            int j = 1;
                                                            if (iPaNoSS > 1) {
                                                                j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                            }
                                                            session.setAttribute("NumPageSessListEasignRemain", String.valueOf(iPaNoSS));
                                                            if(rsPgin[0].length > 0) {
                                                                for(int i = 0; i<rsPgin[0].length; i++) {
                                                        %>
                                                        <tr>
                                                            <td><%= com.convertMoney(j)%></td>
                                                            <td><%= rsPgin[0][i].CERTIFICATION_SN%></td>
                                                            <td><%= rsPgin[0][i].CERTIFICATE_THUMBPRINT%></td>
                                                            <td><%= rsPgin[0][i].REMAINING_SIGNING_COUNTER%></td>
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
                                                                            <%
                                                                            } else if (i <= iTotPags) {
                                                                            %>
                                                                    &nbsp;<a href="<%=namePage%>?iPagNo=<%=i%>"><%=i%></a>
                                                                    <%
                                                                            }
                                                                        }
                                                                        if (iTotPags > iTotSrhRcrds && i < iTotPags) {
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
                                                                    %>&nbsp;&nbsp;
                                                                    <b> <script>document.write(global_fm_paging_total);</script><%=com.convertMoney(iTotRslts)%></b>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </form>
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
                                        }
                                    %>
                                </div>
                                <div class="x_panel" id="idX_Panel_Edit" style="display: none;">
                                    <div class="x_content">
                                        <div id="contentEdit"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
            <script src="../style/jquery.min.js"></script>
            <script src="../style/bootstrap.min.js"></script>
            <script src="../style/custom.min.js"></script>
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