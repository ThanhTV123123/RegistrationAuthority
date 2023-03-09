<%-- 
    Document   : CertProfileEdit
    Created on : May 3, 2018, 11:01:49 AM
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
        <link href="../Css/active/bootstrap-switch.css" rel="stylesheet">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            document.title = certprofile_title_edit;
            changeFavicon("../");
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.citycode.value))
                {
                    document.myname.citycode.focus();
                    funErrorAlert(policy_req_empty + certprofile_fm_code);
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
                    url: "../CertProfileCommon",
                    data: {
                        idParam: 'editcertprofile',
                        id: document.myname.ID.value,
                        Code: document.myname.citycode.value,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.Remark_EN.value,
                        CERTIFICATION_AUTHORITY: document.myname.CERTIFICATION_AUTHORITY.value,
                        CERTIFICATION_PURPOSE: document.myname.CERTIFICATION_PURPOSE.value,
                        CERTIFICATION_ALGORITHM: document.myname.CERTIFICATION_ALGORITHM.value,
                        ActiveFlag: sCheckActiveFlag,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccAlert(certprofile_succ_edit, "CertProfileList.jsp");
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
                                funErrorAlert(certprofile_exists_code);
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
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'SessRefreshCertProfile'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "CertProfileList.jsp";
                        }
                        else
                        {
                            window.location = "CertProfileList.jsp";
                        }
                    }
                });
                return false;
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
                        document.getElementById("idNameURL").innerHTML = certprofile_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(certprofile_title_edit);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="padding-right: 10px;">
                                                <input id="btnSave" type="button" data-switch-get="state" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');"/>
                                                <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                                                <script>
                                                    document.getElementById("btnSave").value = global_fm_button_edit;
                                                    document.getElementById("btnClose").value = global_fm_button_back;
                                                </script>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <%
                                            CERTIFICATION_PROFILE[][] rs = new CERTIFICATION_PROFILE[1][];
                                            try {
                                                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                                if (EscapeUtils.IsInteger(ids) == true) {
                                                    db.S_BO_CERTIFICATION_PROFILE_DETAIL(ids, rs);
                                                    if (rs[0].length > 0) {
                                                        String strID =String.valueOf(rs[0][0].ID);
                                                        String strBranchCode = EscapeUtils.CheckTextNull(rs[0][0].NAME);
                                                        String strDateLimit = EscapeUtils.CheckTextNull(rs[0][0].CREATED_DT);
                                        %>
                                        <form name="myname" method="post" class="form-horizontal">
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(certprofile_fm_code);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input value="<%= strID%>" class="form-control123" readonly style="display: none;" id="ID" name="ID">
                                                <input value="<%= strBranchCode%>" class="form-control123" readonly id="citycode" name="citycode">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_remark_vn)</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input class="form-control123" value="<%= rs[0][0].REMARK %>" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK %>" id="Remark" name="Remark"/>
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_remark_en)</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input class="form-control123" value="<%= rs[0][0].REMARK_EN %>" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK %>" id="Remark_EN" name="Remark_EN"/>
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_amount_fee)</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input class="form-control123" value="<%= rs[0][0].AMOUNT %>" id="AMOUNT" name="AMOUNT"/>
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_ca);</script></label>
                                                <select name="CERTIFICATION_AUTHORITY" id="CERTIFICATION_AUTHORITY" class="form-control123">
                                                    <%
                                                        CERTIFICATION_AUTHORITY[][] rsNoNull = new CERTIFICATION_AUTHORITY[1][];
                                                        try {
                                                            db.S_BO_CERTIFICATION_AUTHORITY_COMBOBOX(sessLanguageGlobal, rsNoNull);
                                                            if (rsNoNull[0].length > 0) {
                                                                for (int i = 0; i < rsNoNull[0].length; i++) {
                                                    %>
                                                    <option value="<%=String.valueOf(rsNoNull[0][i].ID)%>" <%= rsNoNull[0][i].ID==rs[0][0].CERTIFICATION_AUTHORITY_ID ? "selected" : ""%>>
                                                        <%=rsNoNull[0][i].REMARK%>
                                                    </option>
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
                                                <label class="control-label123"><script>document.write(global_fm_certpurpose);</script></label>
                                                <select name="CERTIFICATION_PURPOSE" id="CERTIFICATION_PURPOSE" class="form-control123">
                                                    <%
                                                        CERTIFICATION_PURPOSE[][] rsCertPuspose = new CERTIFICATION_PURPOSE[1][];
                                                        try {
                                                            db.S_BO_CERTIFICATION_PURPOSE_COMBOBOX(sessLanguageGlobal, rsCertPuspose);
                                                            if (rsCertPuspose[0].length > 0) {
                                                                for (int i = 0; i < rsCertPuspose[0].length; i++) {
                                                    %>
                                                    <option value="<%=String.valueOf(rsCertPuspose[0][i].ID)%>" <%= rsCertPuspose[0][i].ID==rs[0][0].CERTIFICATION_PURPOSE_ID ? "selected" : ""%>>
                                                        <%=rsCertPuspose[0][i].REMARK%>
                                                    </option>
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
                                                <label class="control-label123"><script>document.write(global_fm_certalgorithm);</script></label>
                                                <select name="CERTIFICATION_ALGORITHM" id="CERTIFICATION_ALGORITHM" class="form-control123">
                                                    <%
                                                        CERTIFICATION_ALGORITHM[][] rsCertAlgorithm = new CERTIFICATION_ALGORITHM[1][];
                                                        try {
                                                            db.S_BO_CERTIFICATION_ALGORITHM_COMBOBOX(sessLanguageGlobal, rsCertAlgorithm);
                                                            if (rsCertAlgorithm[0].length > 0) {
                                                                for (int i = 0; i < rsCertAlgorithm[0].length; i++) {
                                                    %>
                                                    <option value="<%=String.valueOf(rsCertAlgorithm[0][i].ID)%>" <%= rsCertAlgorithm[0][i].ID==rs[0][0].CERTIFICATION_AUTHORITY_ID ? "selected" : ""%>>
                                                        <%=rsCertAlgorithm[0][i].REMARK%>
                                                    </option>
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
                                                <label class="control-label123"><script>document.write(global_fm_active);</script></label>&nbsp;&nbsp;
                                                <input TYPE="checkbox" id="ActiveFlag" name="ActiveFlag" <%=rs[0][0].ENABLED ? "checked" : ""%> />
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_user_create);</script></label>
                                                <input type="text" readonly name="strCreateUser" class="form-control123" value="<%= rs[0][0].CREATED_BY%>" />
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_date_create);</script></label>
                                                <input class="form-control123" name="Date" readonly value="<%= strDateLimit%>">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_user_endupdate);</script></label>
                                                <input type="text" readonly name="strUpdateUser" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_BY)%>" />
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_date_endupdate);</script></label>
                                                <input class="form-control123" name="UpdateDate" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_DT) %>">
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
        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/bootstrap-switch.js"></script>
        <script src="../js/active/main.js"></script>
    </body>
</html>