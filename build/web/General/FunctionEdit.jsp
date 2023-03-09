<%-- 
    Document   : FunctionEdit
    Created on : Apr 26, 2018, 5:31:48 PM
    Author     : THANH-PC
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<!DOCTYPE html>
<%    response.setHeader("Cache-Control", "no-cache");
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
        <link href="../Css/active/bootstrap-switch.css" rel="stylesheet">
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
            document.title = function_title_edit;
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'SessRefreshFunction'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "FunctionList.jsp";
                        }
                        else
                        {
                            window.location = "FunctionList.jsp";
                        }
                    }
                });
                return false;
            }
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.Remark.value))
                {
                    document.myname.Remark.focus();
                    funErrorAlert(policy_req_empty + global_fm_remark_vn);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.Remark_EN.value))
                {
                    document.myname.Remark_EN.focus();
                    funErrorAlert(policy_req_empty + global_fm_remark_en);
                    return false;
                }
                var sCheckActiveFlag = "0";
                var sTempActive = $("#ActiveFlag").bootstrapSwitch("state");
                if(sTempActive === true)
                {
                    sCheckActiveFlag = "1";
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../FunctionalityCommon",
                    data: {
                        idParam: 'editfunctionality',
                        id: document.myname.cityid.value,
                        citycode: document.myname.citycode.value,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.Remark_EN.value,
                        ActiveFlag: sCheckActiveFlag,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccAlert(function_succ_edit, "FunctionList.jsp");
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
                            if (myStrings[1] === "1") {
                                funErrorAlert(function_exists_code);
                            } else if (myStrings[1] === JS_STR_ERROR_CODE_99) {
                                funErrorAlert(global_error_login_info);
                            }
                            else
                            {
                                funErrorAlert(global_errorsql);
                            }
                        }
                        $(".loading-gif").hide();
                        $('#over').remove();
                    }
                });
                return false;
            }
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
                        document.getElementById("idNameURL").innerHTML = function_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(function_title_edit);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <input type="button" id="btnSave" data-switch-get="state" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');"/>
                                                <button type="button" class="btn btn-info" onclick="closeForm('<%=anticsrf%>');"><script>document.write(global_fm_button_back);</script></button>
                                                <script>
                                                    document.getElementById("btnSave").value = global_fm_button_edit;
                                                </script>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <%
                                            FUNCTIONALITY[][] rs = new FUNCTIONALITY[1][];
                                            try {
                                                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                                if (EscapeUtils.IsInteger(ids) == true) {
                                                    db.S_BO_FUNCTIONALITY_DETAIL(EscapeUtils.escapeHtml(ids), rs);
                                                    if (rs[0].length > 0) {
                                                        String strDesc = EscapeUtils.CheckTextNull(rs[0][0].REMARK);
                                                        String strDesc_EN = EscapeUtils.CheckTextNull(rs[0][0].REMARK_EN);
                                        %>
                                        <form name="myname" method="post" class="form-horizontal">
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(function_fm_code);</script></label>
                                                <input type="hidden" name="cityid" value="<%= rs[0][0].ID%>" />
                                                <input type="text" readonly="true" name="citycode" class="form-control123" value="<%= rs[0][0].NAME%>" />
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_remark_vn)</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input class="form-control123" value="<%= strDesc%>" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK%>" id="Remark" name="Remark"/>
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_remark_en)</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input class="form-control123" value="<%= strDesc_EN%>" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK%>" id="Remark_EN" name="Remark_EN"/>
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_active);</script></label>&nbsp;&nbsp;
                                                <input TYPE="checkbox" id="ActiveFlag" name="ActiveFlag" <%=rs[0][0].ENABLED ? "checked='checked'" : ""%> />
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_user_create);</script></label>
                                                <input type="text" readonly name="strCreateUser" class="form-control123" value="<%= rs[0][0].CREATED_BY%>" />
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_date_create);</script></label>
                                                <input type="text" readonly="true" name="dates" class="form-control123" value="<%= rs[0][0].CREATED_DT%>" />
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_user_endupdate);</script></label>
                                                <input type="text" readonly name="strUpdateUser" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_BY)%>" />
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_date_endupdate);</script></label>
                                                <input type="text" readonly="true" name="UpdateDate" class="form-control123" value="<%= rs[0][0].MODIFIED_DT%>" />
                                            </div>
                                        </form>
                                        <%
                                        } else {
                                        %>
                                        <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
                                            <label style="color: red;"><script>document.write(global_no_data);</script></label>
                                        </div>
                                        <%
                                            }
                                        } else {
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
        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/bootstrap-switch.js"></script>
        <script src="../js/active/main.js"></script>
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