<%-- 
    Document   : Search
    Created on : Mar 26, 2018, 11:07:37 AM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"> 
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
            document.title = _title_list;
            function popupEdit(id)
            {
                window.location = 'Edit.jsp?id=' + id;
            }
            function addForm() {
                window.location = 'New.jsp';
            }
            function exportRecordAbc(id) {
                document.getElementById("idHiddenLoad").value = JS_STR_GRID_SEARCH_RESET;
                document.getElementById("tempCsrfToken").value = id;
                var f = document.myname;
                f.action = "";
                f.method = "post";
                f.submit();
                return true;
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
                            <%
                                    int status = 1000;
                                    int statusLoad = 0;
                                    int j = 1;
                                    String tempIPagNo = request.getParameter("iPagNo");
                                    int iTotRslts = com.Converter(request.getParameter("iTotRslts"));
                                    int iTotPags = com.Converter(request.getParameter("iTotPags"));
                                    String stempCsrfToken = request.getParameter("tempCsrfToken");
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
//                                    int[] pIa = new int[1];
//                                    int[] pIb = new int[1];
                                    CITY_PROVINCE[][] rsPgin = new CITY_PROVINCE[1][];
                                    try {
                                        if (session.getAttribute("SessRefreshCity") != null && session.getAttribute("IPSessSearchCity") != null
                                                && session.getAttribute("RegionSessSearchCity") != null) {
                                            statusLoad = 1;
                                            String Channel = (String) session.getAttribute("RegionSessSearchCity");
                                            String Ip = (String) session.getAttribute("IPSessSearchCity");
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(Channel)) {
                                                Channel = "";
                                            }
                                            session.setAttribute("RegionSessSearchCity", Channel);
                                            session.setAttribute("IPSessSearchCity", Ip);
                                            session.setAttribute("SessRefreshCity", null);
                                            if (session.getAttribute("IPageNoSessSearchCity") != null) {
                                                String sPage = (String) session.getAttribute("IPageNoSessSearchCity");
                                                iPagNo = Integer.parseInt(sPage);
                                            }
                                            if (session.getAttribute("ISwRwsSessSearchCity") != null) {
                                                String sSumPage = (String) session.getAttribute("ISwRwsSessSearchCity");
                                                iSwRws = Integer.parseInt(sSumPage);
                                            }
                                            if (session.getAttribute("NumPageSessSearchCity") != null) {
                                                String sNoPage = (String) session.getAttribute("NumPageSessSearchCity");
                                                iPaNoSS = Integer.parseInt(sNoPage);
                                            }
                                            session.setAttribute("IPageNoSessSearchCity", String.valueOf(iPagNo));
                                            session.setAttribute("ISwRwsSessSearchCity", String.valueOf(iSwRws));
                                            int intCount = 0;
                                            intCount = db.BO_PROVINCE_TOTAL(EscapeUtils.escapeHtmlSearch(Ip), EscapeUtils.escapeHtmlSearch(Channel));
//                                            session.setAttribute("pIaCity", String.valueOf(pIa[0]));
//                                            session.setAttribute("pIbCity", String.valueOf(pIb[0]));
//                                            if (session.getAttribute("pIaCity") != null) {
//                                                pIa[0] = Integer.parseInt((String) session.getAttribute("pIaCity"));
//                                            }
//                                            if (session.getAttribute("pIbCity") != null) {
//                                                pIb[0] = Integer.parseInt((String) session.getAttribute("pIbCity"));
//                                            }
                                            iTotRslts = intCount;
                                            if (iTotRslts > 0) {
                                                db.BO_PROVINCE_LIST(EscapeUtils.escapeHtmlSearch(Ip), EscapeUtils.escapeHtmlSearch(Channel), iPagNo,
                                                    iSwRws, rsPgin);
                                                status = 1;
                                            } else {
                                                status = 1000;
                                            }
                                        } else if (request.getMethod().equals("POST") || hasPaging == true) {
                                            if (request.getMethod().equals("POST")) {
//                                                session.setAttribute("pIaCity", null);
//                                                session.setAttribute("pIbCity", null);
                                                session.setAttribute("NumPageSessSearchCity", null);
                                                session.setAttribute("IPageNoSessSearchCity", null);
                                                session.setAttribute("ISwRwsSessSearchCity", null);
                                            }
                                            String sCsrfToken = request.getParameter("CsrfToken");
                                            if (hasPaging != true) {
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
                                        String Channel = "";
                                        String Ip = "";
                                        if (hasPaging == true) {
                                            Channel = (String) session.getAttribute("RegionSessSearchCity");
                                            Ip = (String) session.getAttribute("IPSessSearchCity");
                                        } else {
                                            Channel = request.getParameter("Region");
                                            Ip = EscapeUtils.ConvertStringToUnicode(request.getParameter("Ip"));
                                            session.setAttribute("CountListCity", null);
                                        }
                                        session.setAttribute("RegionSessSearchCity", Channel);
                                        session.setAttribute("IPSessSearchCity", Ip);
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(Channel)) {
                                            Channel = "";
                                        }
                                        int ss = 0;
                                        if ((session.getAttribute("CountListCity")) == null) {
                                            ss = db.BO_PROVINCE_TOTAL(EscapeUtils.escapeHtmlSearch(Ip), EscapeUtils.escapeHtmlSearch(Channel));
//                                        session.setAttribute("pIaCity", String.valueOf(pIa[0]));
//                                        session.setAttribute("pIbCity", String.valueOf(pIb[0]));
//                                        if (session.getAttribute("pIaCity") != null) {
//                                            pIa[0] = Integer.parseInt((String) session.getAttribute("pIaCity"));
//                                        }
//                                        if (session.getAttribute("pIbCity") != null) {
//                                            pIb[0] = Integer.parseInt((String) session.getAttribute("pIbCity"));
//                                        }
                                            session.setAttribute("CountListCity", String.valueOf(ss));
                                        } else {
                                            String sCount = (String) session.getAttribute("CountListCity");
                                            ss = Integer.parseInt(sCount);
                                            session.setAttribute("CountListCity", String.valueOf(ss));
                                        }
                                        iTotRslts = ss;
                                        if (iTotRslts > 0) {
                                            db.BO_PROVINCE_LIST(EscapeUtils.escapeHtmlSearch(Ip), EscapeUtils.escapeHtmlSearch(Channel),
                                                iPagNo, iSwRws, rsPgin);
                                            status = 1;
                                            session.setAttribute("IPageNoSessSearchCity", String.valueOf(iPagNo));
                                            session.setAttribute("ISwRwsSessSearchCity", String.valueOf(iSwRws));
                                        } else {
                                            status = 1000;
                                        }
                                    } else {
//                                        session.setAttribute("pIaCity", null);
//                                        session.setAttribute("pIbCity", null);
                                        session.setAttribute("RegionSessSearchCity", null);
                                        session.setAttribute("IPSessSearchCity", null);
                                        session.setAttribute("SessRefreshCity", null);
                                        session.setAttribute("NumPageSessSearchCity", null);
                                        session.setAttribute("IPageNoSessSearchCity", null);
                                        session.setAttribute("ISwRwsSessSearchCity", null);
                                    }
                                %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-search"></i> <script>document.write(_table_search);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <button type="button" class="btn btn-info" onClick="return exportRecordAbc('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                <input type="button" id="btnNew" class="btn btn-info" onclick="addForm();"/>
                                                <script>
                                                    document.getElementById("btnNew").value = global_fm_button_New;
                                                </script>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="myname" method="post" class="form-horizontal">
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_area);</script></label>
                                                <select class="form-control123" name="BRANCH" id="BRANCH">
                                                    <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("RegionSessSearchCity") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("RegionSessSearchCity").toString().trim()) ? "selected" : "" %>><script>document.write(global_fm_combox_all);</script></option>
                                                    <%
                                                        BRANCH[][] rsRegions = new BRANCH[1][];
                                                        try {
                                                            db.BO_BRANCH_COMBOBOX(rsRegions);
                                                            if (rsRegions[0].length > 0) {
                                                                for (int i = 0; i < rsRegions[0].length; i++) {
                                                    %>
                                                    <option value="<%=String.valueOf(rsRegions[0][i].BRANCH_ID)%>" <%= session.getAttribute("RegionSessSearchCity") != null && String.valueOf(rsRegions[0][i].BRANCH_ID).equals(session.getAttribute("RegionSessSearchCity").toString().trim()) ? "selected" : "" %>><%=rsRegions[0][i].NAME %></option>
                                                    <%
                                                            }
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
                                                </select>
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(city_fm_name);</script></label>
                                                <input type="text" name="Ip" value="<%= session.getAttribute("IPSessSearchCity") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("IPSessSearchCity").toString()) : ""%>" maxlength="50" class="form-control123">
                                            </div>
                                            <input type="hidden" name="CsrfToken" id="CsrfToken" value="<%=anticsrf%>"/>
                                            <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                            <input id="idHiddenLoad" name="idHiddenLoad" value="" type="hidden"/>
                                            <input id="idUrlFile" name="idUrlFile" type="text" style="display: none;"/>
                                        </form>
                                    </div>
                                </div>
                                <%
                                    if (status == 1 && statusLoad == 1) {
                                %>
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(_table_list);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <script>document.write(global_label_grid_sum);</script><%= com.convertMoney(iTotRslts)%>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <input type="hidden" name="iPagNo" value="<%=iPagNo%>">
                                        <input type="hidden" name="cPagNo" value="<%=cPagNo%>">
                                        <input type="hidden" name="iSwRws" value="<%=iSwRws%>">
                                        <style type="text/css">
                                            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                            .btn{margin-bottom: 0px;}
                                        </style>
                                        <div class="table-responsive">
                                            <table id="idTableList" class="table table-striped projects">
                                                <thead>
                                                <th><script>document.write(global_fm_STT);</script></th>
                                                <th><script>document.write(city_fm_name);</script></th>
                                                <th><script>document.write(global_fm_date_create);</script></th>
                                                <th><script>document.write(global_fm_active);</script></th>
                                                <th><script>document.write(global_fm_action);</script></th>
                                                </thead>
                                                <tbody>
                                                    <%
                                                        if (iPaNoSS > 1) {
                                                            j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                        }
                                                        session.setAttribute("NumPageSessSearchCity", String.valueOf(iPaNoSS));
                                                        if (rsPgin[0].length > 0) {
                                                            for (int i = 0; i < rsPgin[0].length; i++) {
                                                                String strID = String.valueOf(rsPgin[0][i].PROVINCE_ID);
                                                                String strActiveFlag = rsPgin[0][i].ENABLED ?
                                                                    Definitions.CONFIG_GRID_ACTIVE_TRUE : Definitions.CONFIG_GRID_ACTIVE_FALSE;
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
                                                                %>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <%
                                    }
                                    if (status == 1000 && statusLoad == 1) {
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
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
        </div>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../style/custom.min.js"></script>
    </body>
</html>