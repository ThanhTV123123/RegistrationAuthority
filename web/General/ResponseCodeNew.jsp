<%-- 
    Document   : ResponseCodeNew
    Created on : Apr 24, 2018, 5:46:41 PM
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
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            document.title = response_title_add;
            changeFavicon("../");
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.citycode.value))
                {
                    document.myname.citycode.focus();
                    funErrorAlert(policy_req_empty + response_fm_code);
                    return false;
                }
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
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../ResponseCodeCommon",
                    data: {
                        idParam: 'addresponse',
                        citycode: document.myname.citycode.value,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.Remark_EN.value,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        $("#idUrlFile").val(sSpace(html));
                        var myStrings = $("#idUrlFile").val().split('#');
                        $("#idUrlFile").val('');
                        if (myStrings[0] === "0")
                        {
                            funSuccAlert(response_succ_add, "ResponseCodeList.jsp");
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
                                funErrorAlert(response_exists_code);
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
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'SessRefreshResponse'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "ResponseCodeList.jsp";
                        }
                        else
                        {
                            window.location = "ResponseCodeList.jsp";
                        }
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
                        document.getElementById("idNameURL").innerHTML = response_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(response_title_add);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <input type="button" id="btnSave" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');"/>
                                                <input id="btnClose" class="btn btn-info" onclick="closeForm();" type="button" />
                                                <input id="idUrlFile" name="idUrlFile" type="text" style="display: none;"/>
                                                <script>
                                                    document.getElementById("btnSave").value = global_fm_button_add;
                                                    document.getElementById("btnClose").value = global_fm_button_back;
                                                </script>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form name="myname" method="post" class="form-horizontal">
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(response_fm_code);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input type="text" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_NAME%>" name="citycode" class="form-control123" />
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_remark_vn)</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input class="form-control123" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK%>" id="Remark" name="Remark"/>
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_remark_en)</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input class="form-control123" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK%>" id="Remark_EN" name="Remark_EN"/>
                                            </div>

                                        </form>
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