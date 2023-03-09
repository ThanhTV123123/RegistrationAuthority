<%-- 
    Document   : UserRole
    Created on : Feb 25, 2014, 10:57:39 AM
    Author     : Thanh
--%>

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
            document.title = role_title_list;
            $(document).ready(function () {
                localStorage.setItem("LOCAL_PARAM_USERROLELIST", null);
            });
            function popupEdit(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $('#contentEdit').load('UserRoleEdit.jsp', {id:id}, function () {
                $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
                $(".loading-gif").hide();
                $('#over').remove();
//                localStorage.setItem("LOCAL_PARAM_USERROLELIST", id);
//                window.location = 'UserRoleEdit.jsp?id=' + id;
            }
            function addForm()
            {
                window.location = 'UserRoleNew.jsp';
            }
        </script>
    </head>
    <body class="nav-md">
        <%
            if ((session.getAttribute("sUserID")) != null) {
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
        %>
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
                        document.getElementById("idNameURL").innerHTML = role_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
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
                                                    <th><script>document.write(role_fm_code);</script></th>
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
                                                            session.setAttribute("NumPageSessListRole", String.valueOf(iPaNoSS));
                                                            if(rsPgin[0].length > 0) {
                                                                for(int i = 0; i<rsPgin[0].length; i++) {
                                                                String strID = String.valueOf(rsPgin[0][i].ID);
                                                                String strCode = EscapeUtils.CheckTextNull(rsPgin[0][i].NAME);
                                                                String strDesc = EscapeUtils.CheckTextNull(rsPgin[0][i].REMARK);
                                                                String strActiveFlag = rsPgin[0][i].ENABLED ? Definitions.CONFIG_GRID_ACTIVE_TRUE : Definitions.CONFIG_GRID_ACTIVE_FALSE;
                                                        %>
                                                        <tr>
                                                            <td><%= com.convertMoney(j)%></td>
                                                            <td><%= strCode%></td>
                                                            <td><%= strDesc%></td>
                                                            <td><%= rsPgin[0][i].CREATED_DT%></td>
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
<!--                    <script>
                        $(document).ready(function () {
                            if(localStorage.getItem("EDIT_USERROLE") !== null && localStorage.getItem("EDIT_USERROLE") !== "null")
                            {
                                var vIDEDIT_USERROLE = localStorage.getItem("EDIT_USERROLE");
                                localStorage.setItem("EDIT_USERROLE", null);
                                popupEdit(vIDEDIT_USERROLE);
                            }
                        });
                    </script>-->
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