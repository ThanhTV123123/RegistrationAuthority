<%-- 
    Document   : sDemoReport
    Created on : Aug 24, 2018, 3:19:17 PM
    Author     : THANH-PC
--%>

<%@page import="vn.ra.utility.Definitions"%>
<%@page import="vn.ra.utility.EscapeUtils"%>
<%@page import="vn.ra.object.ROLE"%>
<%@page import="vn.ra.process.ConnectDatabase"%>
<%@page import="vn.ra.process.CommonFunction"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <title></title>
        <script language="javascript">
            changeFavicon("../");
            document.title = role_title_list;
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
        </script>
    </head>
    <body class="nav-md">
        <%
            int checkExFinnaly = 0;
            CommonFunction com = new CommonFunction();
            ConnectDatabase db = new ConnectDatabase();
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
        %>
        <div class="container body">
            <div class="main_container">
                <div class="col-md-3 left_col">
                    <div class="left_col scroll-view">
                    </div>
                </div>
                <div class="top_nav">
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(role_title_table);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <input type="button" id="btnNew" class="btn btn-info" onclick="addForm();" />
                                                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                                <script>
                                                    document.getElementById("btnNew").value = global_fm_button_New;
                                                </script>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <%
                                        ROLE[][] rsPgin = new ROLE[1][];
                                        int iSwRws = 20;
                                        int iTotSrhRcrds = 5;
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
                                            String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                            if (session.getAttribute("SessRefreshRole") != null) {
                                                session.setAttribute("SessRefreshRole", null);
                                                int ss = db.S_BO_ROLE_TOTAL();
                                                if (ss > 0) {
                                                    if (session.getAttribute("IPageNoSessListRole") != null) {
                                                        String sPage = (String) session.getAttribute("IPageNoSessListRole");
                                                        iPagNo = Integer.parseInt(sPage);
                                                    }
                                                    if (session.getAttribute("ISwRwsSessListRole") != null) {
                                                        String sSumPage = (String) session.getAttribute("ISwRwsSessListRole");
                                                        iSwRws = Integer.parseInt(sSumPage);
                                                    }
                                                    if (session.getAttribute("NumPageSessListRole") != null) {
                                                        String sNoPage = (String) session.getAttribute("NumPageSessListRole");
                                                        iPaNoSS = Integer.parseInt(sNoPage);
                                                    }
                                                    session.setAttribute("IPageNoSessListRole", String.valueOf(iPagNo));
                                                    session.setAttribute("ISwRwsSessListRole", String.valueOf(iSwRws));
                                                    db.S_BO_ROLE_LIST(rsPgin, iPagNo, iSwRws, sessLanguageGlobal);
                                                }
                                                iTotRslts = ss;
                                            }
                                            else
                                            {
                                                session.setAttribute("NumPageSessListRole", null);
                                                session.setAttribute("IPageNoSessListRole", null);
                                                session.setAttribute("ISwRwsSessListRole", null);
                                                if (hasPaging != true) {
                                                    session.setAttribute("CountListRole", null);
                                                }
                                                int ss = 0;
                                                if ((session.getAttribute("CountListRole")) == null) {
                                                    ss = db.S_BO_ROLE_TOTAL();
                                                    session.setAttribute("CountListRole", String.valueOf(ss));
                                                } else {
                                                    String sCount = (String) session.getAttribute("CountListRole");
                                                    ss = Integer.parseInt(sCount);
                                                    session.setAttribute("CountListRole", String.valueOf(ss));
                                                }
                                                if (ss > 0) {
                                                    db.S_BO_ROLE_LIST(rsPgin, iPagNo, iSwRws, sessLanguageGlobal);
                                                    session.setAttribute("IPageNoSessListRole", String.valueOf(iPagNo));
                                                    session.setAttribute("ISwRwsSessListRole", String.valueOf(iSwRws));
                                                }
                                                iTotRslts = ss;
                                            }
                                    %>
                                    <form name="myname" method="post">
                                        <input type="hidden" name="iPagNo" value="<%=iPagNo%>">
                                        <input type="hidden" name="cPagNo" value="<%=cPagNo%>">
                                        <input type="hidden" name="iSwRws" value="<%=iSwRws%>">
                                        <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                        <div class="x_content">
                                            <style type="text/css">
                                                .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                                .btn{margin-bottom: 0px;}
                                                .panel_toolbox { min-width: 0;}
                                            </style>
                                            <div class="table-responsive">
                                                <table id="idTableList" class="table table-striped projects">
                                                    <thead>
                                                    <th><script>document.write(global_fm_STT);</script></th>
                                                    <th><script>document.write(role_fm_code);</script></th>
                                                    <th><script>document.write(global_fm_Description);</script></th>
                                                    <th><script>document.write(global_fm_date_create);</script></th>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            int j = 1;
                                                            if (iPaNoSS > 1) {
                                                                j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                            }
                                                            session.setAttribute("NumPageSessListRole", String.valueOf(iPaNoSS));
                                                            if(rsPgin[0].length > 0) {
                                                                for(int i = 0; i<rsPgin[0].length; i++) {
                                                                    String strID = String.valueOf(rsPgin[0][i].ID);
                                                                    String strCode = EscapeUtils.CheckTextNull(rsPgin[0][i].NAME);
                                                                    String strDesc = EscapeUtils.CheckTextNull(rsPgin[0][i].REMARK);
                                                        %>
                                                        <tr>
                                                            <td><%= com.convertMoney(j)%></td>
                                                            <td><%= strCode%></td>
                                                            <td><%= strDesc%></td>
                                                            <td><%= rsPgin[0][i].CREATED_DT%></td>
                                                        </tr>
                                                        <tr style="display: none;">
                                                            
                                                        </tr>
                                                        <%
                                                                    j++;
                                                                }
                                                            }
                                                        %>
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
                                    %>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <script src="../style/jquery.min.js"></script>
            <script src="../style/bootstrap.min.js"></script>
            <script src="../style/custom.min.js"></script>
        </div>
    </body>
</html>
