<%-- 
    Document   : detail
    Created on : Mar 26, 2018, 11:08:17 AM
    Author     : THANH-PC
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            document.title = _title_edit;
            changeFavicon("../");
            $(document).ready(function () {
                $('#btnClose').click(function () {
                    parent.history.back();
                    return false;
                });
                $('.loading-gif').hide();
            });
            function closeForm()
            {
                window.location = "List.jsp";
            }
        </script>
    </head>
    <body class="nav-md">
        <%        
                String anticsrf = "";
                anticsrf = "" + Math.random();
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
                        document.getElementById("idNameURL").innerHTML = _title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(_title_edit);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="padding-right: 10px;">
                                                <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                                                <script>
                                                    document.getElementById("btnClose").value = global_fm_button_back;
                                                </script>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <%
                                            BRANCH[][] rs = new BRANCH[1][];
                                            try {
                                                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                                if (EscapeUtils.IsInteger(ids) == true) {
                                                    db.BO_BRANCH_DETAIL(ids, rs);
                                                    if (rs[0].length > 0) {
                                                        String strDateLimit = EscapeUtils.CheckTextNull(rs[0][0].CREATED_DT);
                                        %>
                                        <form name="myname" method="post" class="form-horizontal">
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(branch_fm_name);</script></label>
                                                <input type="hidden" name="BranchID" value="<%= rs[0][0].BRANCH_ID %>" />
                                                <input class="form-control123" maxlength="50" id="BranchName" name="BranchName" value="<%=rs[0][0].NAME %>">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_date_create);</script></label>
                                                <input class="form-control123" name="Date" readonly value="<%= strDateLimit%>">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_date_endupdate);</script></label>
                                                <input class="form-control123" name="UpdateDate" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_DT) %>">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_active);</script></label>
                                                <input TYPE="checkbox" id="ActiveFlag" name="ActiveFlag" <%=rs[0][0].ENABLED ? "checked" : ""%> />
                                            </div>
                                        </form>
                                        <%
                                            } else
                                            {
                                        %>
                                        <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
                                            <label style="color: red;"><script>document.write(global_no_data);</script></label>
                                        </div>
                                        <%
                                                }
                                            } else
                                                {
                                        %>
                                        <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
                                            <label style="color: red;"><script>document.write(global_no_data);</script></label>
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