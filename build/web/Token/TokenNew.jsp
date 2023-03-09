<%-- 
    Document   : TokenNew
    Created on : Jun 19, 2019, 5:38:40 PM
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
        <link href="../js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
        <script src="../js/checkphone/intlTelInput.js" type="text/javascript"></script>
        <script type="text/javascript">
            document.title = token_title_add;
            changeFavicon("../");
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.TokenSN.value))
                {
                    document.myname.TokenSN.focus();
                    funErrorAlert(policy_req_empty + token_fm_tokenid);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.TokenSOPIN.value))
                {
                    document.myname.TokenSOPIN.focus();
                    funErrorAlert(policy_req_empty + token_fm_sopin);
                    return false;
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../TokenCommon",
                    data: {
                        idParam: 'addtoken',
                        TokenSN: document.myname.TokenSN.value,
                        TokenSOPIN: document.myname.TokenSOPIN.value,
                        TOKEN_VERSION: document.myname.TOKEN_VERSION.value,
                        BranchID: document.myname.BranchID.value,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccAlert(token_succ_add_renew, "TokenList.jsp");
                        }
                        else if (myStrings[0] === "1")
                        {
                            funErrorAlert(token_exists_tokensn);
                        }
                        else if (myStrings[0] === "2")
                        {
                            funErrorAlert(global_req_all);
                        }
                        else if (myStrings[0] === "3")
                        {
                            funErrorAlert(global_req_length);
                        }
                        else if (myStrings[0] === JS_EX_CSRF)
                        {
                            funCsrfAlert();
                        }
                        else if (myStrings[0] === JS_EX_LOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else
                        {
                            if (myStrings[1] === JS_STR_ERROR_CODE_99) {
                                funErrorAlert(global_error_login_info);
                            }
                            else {
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
                window.location = "TokenList.jsp";
            }
        </script>
    </head>
    <body class="nav-md">
        <%
            if ((session.getAttribute("sUserID")) != null) {
                String anticsrf = "";
                anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
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
                        document.getElementById("idNameURL").innerHTML = token_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleEdits" style="color: #36526D;"></span></h2>
                                        <script>$("#idLblTitleEdits").text(token_title_add);</script>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="padding-right: 10px;">
                                                <input id="btnSave" type="button" class="btn btn-info" onclick="return ValidateForm('<%=anticsrf%>');"/>
                                                <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                                                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
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
                                            <%
                                                try {
                                            %>
                                            <div class="form-group" style="padding-left: 0;">
                                                <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                    <label id="idLblTitleRemark"></label>
                                                    <label id="idLblNoteRemark"class="CssRequireField"></label>
                                                </label>
                                                <div class="col-sm-10" style="padding-right: 0px;">
                                                    <input class="form-control123" maxlength="<%= Definitions.CONFIG_LENGTH_TOKEN_SN %>" id="TokenSN" name="TokenSN"/>
                                                </div>
                                            </div>
                                            <script>
                                                $("#idLblTitleRemark").text(token_fm_tokenid);
                                                $("#idLblNoteRemark").text(global_fm_require_label);
                                            </script>
                                            <div class="form-group" style="padding-left: 0;">
                                                <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                    <label id="idLblTitleRemark_EN"></label>
                                                    <label id="idLblNoteRemark_EN"class="CssRequireField"></label>
                                                </label>
                                                <div class="col-sm-10" style="padding-right: 0px;">
                                                    <input class="form-control123" maxlength="<%= Definitions.CONFIG_LENGTH_TOKEN_SOPIN %>" id="TokenSOPIN" name="TokenSOPIN"/>
                                                </div>
                                            </div>
                                            <script>
                                                $("#idLblTitleRemark_EN").text(token_fm_sopin);
                                                $("#idLblNoteRemark_EN").text(global_fm_require_label);
                                            </script>
                                            <div class="form-group" style="padding-left: 0;">
                                                <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                    <script>document.write(token_fm_version);</script>
                                                </label>
                                                <div class="col-sm-10" style="padding-right: 0px;">
                                                    <select name="TOKEN_VERSION" id="TOKEN_VERSION" class="form-control123">
                                                        <%
                                                            TOKEN_VERSION[][] rstState = new TOKEN_VERSION[1][];
                                                            db.S_BO_TOKEN_VERSION_COMBOBOX(sessLanguageGlobal, rstState);
                                                            if (rstState[0].length > 0) {
                                                                for (TOKEN_VERSION temp1 : rstState[0]) {
                                                        %>
                                                        <option value="<%=String.valueOf(temp1.ID)%>"><%=temp1.REMARK%></option>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding-left: 0;">
                                                <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                    <script>document.write(token_fm_agent);</script>
                                                </label>
                                                <div class="col-sm-10" style="padding-right: 0px;">
                                                    <select name="BranchID" id="BranchID" class="form-control123">
                                                        <%
                                                            BRANCH[][] rsNoNull = new BRANCH[1][];
                                                            try {
                                                                db.S_BO_PARENT_BRANCH_COMBOBOX(sessLanguageGlobal, rsNoNull);
                                                                if (rsNoNull[0].length > 0) {
                                                                    for (int i = 0; i < rsNoNull[0].length; i++) {
                                                                        String sValueParent = String.valueOf(rsNoNull[0][i].ID);
                                                        %>
                                                        <option value="<%=sValueParent%>"><%= rsNoNull[0][i].NAME + " - " + rsNoNull[0][i].REMARK%></option>
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
