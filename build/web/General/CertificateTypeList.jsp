<%-- 
    Document   : CertificateTypeList
    Created on : Oct 10, 2019, 11:11:09 AM
    Author     : THANH-PC
--%>

<%@page import="java.net.URLEncoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<!DOCTYPE html>
<%    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"
        <META HTTP-EQUIV="Expires" CONTENT="-1">
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
        <script type="text/javascript">
            changeFavicon("../");
            $(document).ready(function () {
                localStorage.setItem("LOCAL_PARAM_CERTTYPELIST", null);
            });
            document.title = certtype_title_list;
            function popupEdit(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $('#contentEdit').load('CertificateTypeEdit.jsp', {id:id}, function () {
                    goToByScroll("contentEdit");
                });
                $(".loading-gif").hide();
                $('#over').remove();
            }
            function addForm() {
                window.location = 'CertificateTypeNew.jsp';
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
                        document.getElementById("idNameURL").innerHTML = certtype_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%                                        int status = 1000;
                                int statusLoad = 0;
                                int j = 1;
                                String tempIPagNo = request.getParameter("iPagNo");
                                int iTotRslts = com.Converter(request.getParameter("iTotRslts"));
                                int iTotPags = com.Converter(request.getParameter("iTotPags"));
                                String idHiddenLoad = request.getParameter("idHiddenLoad");
                                int iPagNo = com.Converter(tempIPagNo);
                                int iPaNoSS = iPagNo;
                                int cPagNo = com.Converter(request.getParameter("cPagNo"));
                                if (Definitions.CONFIG_GRID_SEARCH_RESET.equals(idHiddenLoad)) {
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
                                Boolean hasPaging = false;
                                if (!"".equals(EscapeUtils.CheckTextNull(tempIPagNo)) && !"null".equals(tempIPagNo)) {
                                    hasPaging = true;
                                }
                                CERTIFICATION_PURPOSE[][] rsPgin = new CERTIFICATION_PURPOSE[1][];
                                try {
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (session.getAttribute("SessRefreshCertType") != null) {
                                        session.setAttribute("SessRefreshCertType", null);
                                        int intCount = 0;
                                        intCount = db.S_BO_CERTIFICATION_PURPOSE_TOTAL();
                                        if (intCount > 0) {
                                            if (session.getAttribute("IPageNoSessListCertType") != null) {
                                                String sPage = (String) session.getAttribute("IPageNoSessListCertType");
                                                iPagNo = Integer.parseInt(sPage);
                                            }
                                            if (session.getAttribute("ISwRwsSessListCertType") != null) {
                                                String sSumPage = (String) session.getAttribute("ISwRwsSessListCertType");
                                                iSwRws = Integer.parseInt(sSumPage);
                                            }
                                            if (session.getAttribute("NumPageSessListCertType") != null) {
                                                String sNoPage = (String) session.getAttribute("NumPageSessListCertType");
                                                iPaNoSS = Integer.parseInt(sNoPage);
                                            }
                                            session.setAttribute("IPageNoSessListCertType", String.valueOf(iPagNo));
                                            session.setAttribute("ISwRwsSessListCertType", String.valueOf(iSwRws));
                                            db.S_BO_CERTIFICATION_PURPOSE_LIST(sessLanguageGlobal, iPagNo, iSwRws, rsPgin);
                                        }
                                        iTotRslts = intCount;
                                    } else {
                                        session.setAttribute("NumPageSessListCertType", null);
                                        session.setAttribute("IPageNoSessListCertType", null);
                                        session.setAttribute("ISwRwsSessListCertType", null);
                                        if (hasPaging != true) {
                                                session.setAttribute("CountBranchList", null);
                                            }
                                        int intCount = 0;
                                        if ((session.getAttribute("CountListCertType")) == null) {
                                            intCount = db.S_BO_CERTIFICATION_PURPOSE_TOTAL();
                                            session.setAttribute("CountListCertType", String.valueOf(intCount));
                                        } else {
                                            String sCount = (String) session.getAttribute("CountListCertType");
                                            intCount = Integer.parseInt(sCount);
                                            session.setAttribute("CountListCertType", String.valueOf(intCount));
                                        }
                                        if (intCount > 0) {
                                            db.S_BO_CERTIFICATION_PURPOSE_LIST(sessLanguageGlobal, iPagNo, iSwRws, rsPgin);
                                            session.setAttribute("IPageNoSessListCertType", String.valueOf(iPagNo));
                                            session.setAttribute("ISwRwsSessListCertType", String.valueOf(iSwRws));
                                        }
                                        iTotRslts = intCount;
                                    }
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <%
                                    if(iTotRslts > 0) {
                                %>
                                <div class="x_panel">
                                    <div class="x_title" style="border-bottom:none;margin-bottom:1px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(certtype_table_list);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
<!--                                                <input type="button" id="btnNew" class="btn btn-info" onclick="addForm();"/>
                                                <script>
                                                    document.getElementById("btnNew").value = global_fm_button_New;
                                                </script>-->
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form name="myform" method="post" class="form-horizontal">
                                            <input type="hidden" name="iPagNo" value="<%=iPagNo%>">
                                            <input type="hidden" name="cPagNo" value="<%=cPagNo%>">
                                            <input type="hidden" name="iSwRws" value="<%=iSwRws%>">
                                            <input type="hidden" name="CsrfToken" id="CsrfToken" value="<%=anticsrf%>"/>
                                            <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                            <input id="idHiddenLoad" name="idHiddenLoad" value="" type="hidden"/>
                                            <input id="idUrlFile" name="idUrlFile" type="text" style="display: none;"/>
                                            <style type="text/css">
                                                .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                                .table > thead > tr > th{border-bottom: none;}
                                                .btn{margin-bottom: 0px;}
                                            </style>
                                            <div class="table-responsive">
                                                <table id="idTableList" class="table table-bordered table-striped projects">
                                                    <thead>
                                                    <th><script>document.write(global_fm_STT);</script></th>
                                                    <th><script>document.write(certtype_fm_code);</script></th>
                                                    <th><script>document.write(global_fm_Description);</script></th>
                                                    <th><script>document.write(global_fm_date_create);</script></th>
                                                    <th><script>document.write(global_fm_active);</script></th>
                                                    <th><script>document.write(global_fm_action);</script></th>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            if (iPaNoSS > 1) {
                                                                j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                            }
                                                            session.setAttribute("NumPageSessListCertType", String.valueOf(iPaNoSS));
                                                            if (rsPgin[0].length > 0) {
                                                                for (int i = 0; i < rsPgin[0].length; i++) {
                                                                    String strID = String.valueOf(rsPgin[0][i].ID);
                                                                    String strActiveFlag = rsPgin[0][i].ENABLED ? Definitions.CONFIG_GRID_ACTIVE_TRUE : Definitions.CONFIG_GRID_ACTIVE_FALSE;
                                                        %>
                                                        <tr>
                                                            <td><%= com.convertMoney(j)%></td>
                                                            <td><%= EscapeUtils.CheckTextNull(rsPgin[0][i].NAME)%></td>
                                                            <td><%= EscapeUtils.CheckTextNull(rsPgin[0][i].REMARK)%></td>
                                                            <td><%= rsPgin[0][i].CREATED_DT%></td>
                                                            <td>
                                                                <script>
                                                                    if ('<%= strActiveFlag%>' === JS_STR_ACTIVE_TRUE) {
                                                                        document.write(global_fm_active_true);
                                                                    } else {
                                                                        document.write(global_fm_active_false);
                                                                    }
                                                                </script>
                                                            </td>
                                                            <td>
                                                                <a style="cursor: pointer;" onclick="popupEdit('<%= strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_button_grid_edit);</script> </a>
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
                                        </form>
                                    </div>
                                </div>
                                <div id="contentEdit"></div>
                                <%
                                    } else {
                                %>
                                <div class="x_panel">
                                    <div class="x_content" style="text-align: center;">
                                        <span style="color: red; font-size: 15px;"><script>document.write(global_succ_NoResult);</script></span>
                                        <div class="clearfix"></div>
                                    </div>
                                </div>
                                <%
                                    }
                                %>
                            </div>
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
<!--                    <script>
                        $(document).ready(function () {
                            if(localStorage.getItem("EDIT_CERTIFICATETYPE") !== null && localStorage.getItem("EDIT_CERTIFICATETYPE") !== "null")
                            {
                                var vIDEDIT_PROVINCE = localStorage.getItem("EDIT_CERTIFICATETYPE");
                                localStorage.setItem("EDIT_PROVINCE", null);
                                popupEdit(vIDEDIT_PROVINCE);
                            }
                        });
                        
                    </script>-->
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