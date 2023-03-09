<%-- 
    Document   : CAList
    Created on : May 2, 2018, 9:56:46 AM
    Author     : THANH-PC
--%>

<%@page import="java.net.URLEncoder"%>
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
            document.title = ca_title_list;
            changeFavicon("../");
            $(document).ready(function () {
                localStorage.setItem("LOCAL_PARAM_CALIST", null);
            });
            function popupEdit(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $('#contentEdit').load('CAEdit.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
//                localStorage.setItem("LOCAL_PARAM_CALIST", id);
//                window.location = 'CAEdit.jsp?id=' + id;
            }
            function addForm() {
                window.location = 'CANew.jsp';
            }
        </script>
    </head>
    <body class="nav-md">
        <%
            if (session.getAttribute("sUserID") != null) {
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
                        document.getElementById("idNameURL").innerHTML = ca_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(ca_table_list);</script></h2>
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
                                        CERTIFICATION_AUTHORITY[][] rsPgin = new CERTIFICATION_AUTHORITY[1][];
                                        String sessLanguageGlobal = session.getAttribute("sessVN").toString();
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
                                            if (session.getAttribute("SessRefreshCA") != null) {
                                                session.setAttribute("SessRefreshCA", null);
                                                int intCount = db.S_BO_CERTIFICATION_AUTHORITY_TOTAL();
                                                if (intCount > 0) {
                                                    if (session.getAttribute("IPageNoSessListCA") != null) {
                                                        String sPage = (String) session.getAttribute("IPageNoSessListCA");
                                                        iPagNo = Integer.parseInt(sPage);
                                                    }
                                                    if (session.getAttribute("ISwRwsSessListCA") != null) {
                                                        String sSumPage = (String) session.getAttribute("ISwRwsSessListCA");
                                                        iSwRws = Integer.parseInt(sSumPage);
                                                    }
                                                    if (session.getAttribute("NumPageSessListCA") != null) {
                                                        String sNoPage = (String) session.getAttribute("NumPageSessListCA");
                                                        iPaNoSS = Integer.parseInt(sNoPage);
                                                    }
                                                    session.setAttribute("IPageNoSessListCA", String.valueOf(iPagNo));
                                                    session.setAttribute("ISwRwsSessListCA", String.valueOf(iSwRws));
                                                    db.S_BO_CERTIFICATION_AUTHORITY_LIST(sessLanguageGlobal, iPagNo, iSwRws, rsPgin);
                                                }
                                                iTotRslts = intCount;
                                            }
                                            else
                                            {
                                                session.setAttribute("NumPageSessListCA", null);
                                                session.setAttribute("IPageNoSessListCA", null);
                                                session.setAttribute("ISwRwsSessListCA", null);
                                                if (hasPaging != true) {
                                                    session.setAttribute("CountCAList", null);
                                                }
                                                int intCount = 0;
                                                if ((session.getAttribute("CountCAList")) == null) {
                                                    intCount = db.S_BO_CERTIFICATION_AUTHORITY_TOTAL();
                                                    session.setAttribute("CountCAList", String.valueOf(intCount));
                                                } else {
                                                    String sCountTemp = (String) session.getAttribute("CountCAList");
                                                    intCount = Integer.parseInt(sCountTemp);
                                                    session.setAttribute("CountCAList", String.valueOf(intCount));
                                                }
                                                if (intCount > 0) {
                                                    db.S_BO_CERTIFICATION_AUTHORITY_LIST(sessLanguageGlobal, iPagNo, iSwRws, rsPgin);
                                                    session.setAttribute("IPageNoSessListCA", String.valueOf(iPagNo));
                                                    session.setAttribute("ISwRwsSessListCA", String.valueOf(iSwRws));
                                                }
                                                iTotRslts = intCount;
                                            }
                                    %>
                                    <form name="myform" method="post">
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
                                            <%
                                                if(iTotRslts > 0) {
                                            %>
                                            <div class="table-responsive">
                                                <table id="idTableList" class="table table-bordered table-striped projects">
                                                    <thead>
                                                    <th><script>document.write(global_fm_STT);</script></th>
                                                    <th><script>document.write(ca_fm_code);</script></th>
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
                                                            session.setAttribute("NumPageSessListCA", String.valueOf(iPaNoSS));
                                                            if (rsPgin[0].length > 0) {
                                                                for (int i = 0; i < rsPgin[0].length; i++) {
                                                                    String strID = String.valueOf(rsPgin[0][i].ID);
                                                                    String strActiveFlag = rsPgin[0][i].ENABLED ? Definitions.CONFIG_GRID_ACTIVE_TRUE : Definitions.CONFIG_GRID_ACTIVE_FALSE;
                                                        %>
                                                        <tr>
                                                            <td><%= com.convertMoney(j)%></td>
                                                            <td><%= EscapeUtils.CheckTextNull(rsPgin[0][i].NAME)%></td>
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
                                
                                <div class="x_panel" id="idX_Panel_Edit" style="display: none;">
                                    <div class="x_content">
                                        <div id="contentEdit"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <script>
                        $(document).ready(function () {
                            if(localStorage.getItem("EDIT_CA") !== null && localStorage.getItem("EDIT_CA") !== "null")
                            {
                                var vIDEDIT_CERTPROFILE = localStorage.getItem("EDIT_CA");
                                localStorage.setItem("EDIT_CA", null);
                                popupEdit(vIDEDIT_CERTPROFILE);
                            }
                        });
                    </script>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
        </div>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../style/custom.min.js"></script>
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