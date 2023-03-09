<%-- 
    Document   : List
    Created on : Mar 26, 2018, 11:07:50 AM
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
        <title></title>
        <script language="javascript">
            document.title = _title_list;
            changeFavicon("../");
            function popupEdit(id)
            {
                window.location = 'Edit.jsp?id=' + id;
            }
            function addForm() {
                window.location = 'New.jsp';
            }
        </script>
    </head>
    <body class="nav-md">
        <%
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
                        document.getElementById("idNameURL").innerHTML = _title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(_table_list);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <input id="btnNew" type="button" class="btn btn-info" onclick="addForm();"/>
                                                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                                <script>
                                                    document.getElementById("btnNew").value = global_fm_button_New;
                                                </script>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <%
                                        BRANCH[][] rsPgin = new BRANCH[1][];
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
                                            if (session.getAttribute("SessRefreshCity") != null) {
                                                session.setAttribute("SessRefreshCity", null);
                                                int intCount = db.BO_BRANCH_TOTAL();
                                                if (intCount > 0) {
                                                    if (session.getAttribute("IPageNoSessListCity") != null) {
                                                        String sPage = (String) session.getAttribute("IPageNoSessListCity");
                                                        iPagNo = Integer.parseInt(sPage);
                                                    }
                                                    if (session.getAttribute("ISwRwsSessListCity") != null) {
                                                        String sSumPage = (String) session.getAttribute("ISwRwsSessListCity");
                                                        iSwRws = Integer.parseInt(sSumPage);
                                                    }
                                                    if (session.getAttribute("NumPageSessListCity") != null) {
                                                        String sNoPage = (String) session.getAttribute("NumPageSessListCity");
                                                        iPaNoSS = Integer.parseInt(sNoPage);
                                                    }
                                                    session.setAttribute("IPageNoSessListCity", String.valueOf(iPagNo));
                                                    session.setAttribute("ISwRwsSessListCity", String.valueOf(iSwRws));
                                                    db.BO_BRANCH_LIST(iPagNo, iSwRws, rsPgin);
                                                }
                                                iTotRslts = intCount;
                                            }
                                            else
                                            {
                                                session.setAttribute("NumPageSessListCity", null);
                                                session.setAttribute("IPageNoSessListCity", null);
                                                session.setAttribute("ISwRwsSessListCity", null);
                                                if (hasPaging != true) {
                                                    session.setAttribute("CountBranchList", null);
                                                }
                                                int intCount = 0;
                                                if ((session.getAttribute("CountBranchList")) == null) {
                                                    intCount = db.BO_BRANCH_TOTAL();
                                                    session.setAttribute("CountBranchList", String.valueOf(intCount));
                                                } else {
                                                    String sCountTemp = (String) session.getAttribute("CountBranchList");
                                                    intCount = Integer.parseInt(sCountTemp);
                                                    session.setAttribute("CountBranchList", String.valueOf(intCount));
                                                }
                                                if (intCount > 0) {
                                                    db.BO_BRANCH_LIST(iPagNo, iSwRws, rsPgin);
                                                    session.setAttribute("IPageNoSessListCity", String.valueOf(iPagNo));
                                                    session.setAttribute("ISwRwsSessListCity", String.valueOf(iSwRws));
                                                }
                                                iTotRslts = intCount;
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
                                            <%
                                                if(iTotRslts > 0) {
                                            %>
                                            <div class="table-responsive">
                                                <table id="idTableList" class="table table-striped projects">
                                                    <thead>
                                                    <th><script>document.write(global_fm_STT);</script></th>
                                                    <th><script>document.write(branch_fm_name);</script></th>
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
                                                            session.setAttribute("NumPageSessListCity", String.valueOf(iPaNoSS));
                                                            if (rsPgin[0].length > 0) {
                                                                for (int i = 0; i < rsPgin[0].length; i++) {
                                                                    String strID = String.valueOf(rsPgin[0][i].BRANCH_ID);
                                                                    String strActiveFlag = rsPgin[0][i].ENABLED ? Definitions.CONFIG_GRID_ACTIVE_TRUE : Definitions.CONFIG_GRID_ACTIVE_FALSE;
                                                        %>
                                                        <tr>
                                                            <td><%= com.convertMoney(j)%></td>
                                                            <td><%= EscapeUtils.CheckTextNull(rsPgin[0][i].NAME)%></td>
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
                                                                <a style="cursor: pointer;" onclick="popupEdit(<%=strID%>);" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_button_grid_edit);</script> </a>
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
                                                            <td id="idTDPaging" style="text-align: center;">
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
                                                                    %>&nbsp;&nbsp;
                                                                    <b> <script>document.write(global_fm_paging_total);</script><%=com.convertMoney(iTotRslts)%></b>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <%
                                            } else {
                                            %>
                                            <div class="form-group" style="padding: 10px 10px 0 10px;margin: 0;text-align: center;">
                                                <span style="color: red; font-size: 15px;"><script>document.write(global_succ_NoResult);</script></span>
                                            </div>
                                            <%
                                                }
                                            %>
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
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
        </div>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../style/custom.min.js"></script>
    </body>
</html>