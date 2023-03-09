<%-- 
    Document   : ConfigPolicyEdit
    Created on : Apr 19, 2018, 3:13:24 PM
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
            document.title = policy_title_edit;
            changeFavicon("../");
            $(document).ready(function () {
                $('#btnClose').click(function () {
                    parent.history.back();
                    return false;
                });
                $('.loading-gif').hide();
//                JSLoadPhoneNew($("#WorkPhone"));
            });
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField($("#Name").val()))
                {
                    $("#Name").focus();
                    funErrorAlert(policy_req_empty+policy_fm_code);
                    return false;
                }
                if (!JSCheckEmptyField($("#Remark").val()))
                {
                    $("#Remark").focus();
                    funErrorAlert(policy_req_empty + global_fm_remark_vn);
                    return false;
                }
                if (!JSCheckEmptyField($("#Remark_EN").val()))
                {
                    $("#Remark_EN").focus();
                    funErrorAlert(policy_req_empty+global_fm_remark_vn);
                    return false;
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
//                var sCheckActiveFlag = "0";
//                if ($("#ActiveFlag").is(':checked')) {
//                    sCheckActiveFlag = "1";
//                }
//                var sCheckRequired = "0";
//                if ($("#Required").is(':checked')) {
//                    sCheckRequired = "1";
//                }
                var sCheckActiveFlag = "0";
                var sTempActive = $("#ActiveFlag").bootstrapSwitch("state");
                if(sTempActive === true)
                {
                    sCheckActiveFlag = "1";
                }
                var sCheckRequired = "0";
                var sTempRequired = $("#Required").bootstrapSwitch("state");
                if(sTempRequired === true)
                {
                    sCheckRequired = "1";
                }
                $.ajax({
                    type: "post",
                    url: "../PolicyCommon",
                    data: {
                        idParam: 'editpolicytype',
                        pID: $("#ID").val(),
                        pName: $("#Name").val(),
                        pRequired: sCheckRequired,
                        pEnable: sCheckActiveFlag,
                        pRemark: $("#Remark").val(),
                        pRemark_EN: $("#Remark_EN").val(),
                        pMIMETYPE: $("#MIME_TYPE").val(),
                        pIsModule: $("#idSessIsChoise").val(),
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccAlert(policy_succ_edit, "ConfigPolicyList.jsp");
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
                                funErrorAlert(policy_exists_code);
                            } else if (myStrings[1] === JS_STR_ERROR_CODE_99) {
                                funErrorAlert(global_error_login_info);
                            } else {
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
                        idSession: 'SessRefreshPolicyType'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "ConfigPolicyList.jsp";
                        }
                        else
                        {
                            window.location = "ConfigPolicyList.jsp";
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
                        document.getElementById("idNameURL").innerHTML = policy_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(policy_title_edit);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="padding-right: 10px;">
                                                <input id="btnSave" type="button" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');"/>
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
                                            GENERAL_POLICY[][] rs = new GENERAL_POLICY[1][];
                                            try {
                                                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                                if (EscapeUtils.IsInteger(ids) == true) {
                                                    db.S_BO_GENERAL_POLICY_ATTR_TYPE_DETAIL(ids, rs);
                                                    if (rs[0].length > 0) {
                                                        String strDateLimit = EscapeUtils.CheckTextNull(rs[0][0].CREATED_DT);
                                        %>
                                        <form name="myname" method="post" class="form-horizontal">
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(policy_fm_code);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input type="hidden" id="ID" value="<%= rs[0][0].ID %>" />
                                                <input class="form-control123" readonly id="Name" value="<%=rs[0][0].NAME%>" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_NAME%>">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_datatype_label);</script></label>
                                                <select name="MIME_TYPE" id="MIME_TYPE" class="form-control123">
                                                    <option value="<%=Definitions.CONFIG_POLICY_MIMETYPE_TEXT %>" <%= EscapeUtils.CheckTextNull(rs[0][0].MIMETYPE).equals(Definitions.CONFIG_POLICY_MIMETYPE_TEXT) ? "selected" : "" %>><script>document.write(global_fm_datatype_varchar);</script></option>
                                                    <option value="<%= Definitions.CONFIG_POLICY_MIMETYPE_NUMERIC%>" <%= EscapeUtils.CheckTextNull(rs[0][0].MIMETYPE).equals(Definitions.CONFIG_POLICY_MIMETYPE_NUMERIC) ? "selected" : "" %>><script>document.write(global_fm_datatype_numeric);</script></option>
                                                    <option value="<%=Definitions.CONFIG_POLICY_MIMETYPE_BOOLEAN %>" <%= EscapeUtils.CheckTextNull(rs[0][0].MIMETYPE).equals(Definitions.CONFIG_POLICY_MIMETYPE_BOOLEAN) ? "selected" : "" %>><script>document.write(global_fm_datatype_boolean);</script></option>
                                                </select>
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_remark_vn);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input class="form-control123" id="Remark" value="<%= EscapeUtils.CheckTextNull(rs[0][0].REMARK) %>" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK%>">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_remark_en);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input class="form-control123" id="Remark_EN" value="<%= EscapeUtils.CheckTextNull(rs[0][0].REMARK_EN) %>" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK%>">
                                            </div>
                                            <script>
                                                $(document).ready(function () {
                                                    $('.radio-inline').on('click', function () {
                                                        var s = $(this).find('input').attr('id');
                                                        if (s === 'nameCheck1')
                                                        {
                                                            $("#idSessIsChoise").val('1');
                                                        }
                                                        if (s === 'nameCheck2')
                                                        {
                                                            $("#idSessIsChoise").val('0');
                                                        }
                                                    });
                                                });
                                            </script>
                                            <div class="form-group">
                                                <label class="control-label col-sm-2" style="color: #000000; padding-left: 0; font-weight: bold;text-align: left;"><script>document.write(policy_fm_group_fo_bo);</script></label>
                                                <div class="col-sm-10">
                                                    <label class="radio-inline"><input type="radio" name="nameCheck" id="nameCheck1" <%= rs[0][0].FRONT_OFFICE_ENABLED ? "checked" : ""%>><script>document.write(policy_fm_fo);</script></label>
                                                    <label class="radio-inline"><input type="radio" name="nameCheck" id="nameCheck2" <%= rs[0][0].FRONT_OFFICE_ENABLED ? "" : "checked"%>><script>document.write(policy_fm_bo);</script></label>
                                                    <input type="text" style="display: none;" id="idSessIsChoise" value="<%= rs[0][0].FRONT_OFFICE_ENABLED ? "1" : "0" %>" name="idSessIsChoise"/>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_required_input);</script></label>&nbsp;&nbsp;<input TYPE="checkbox" id="Required" name="Required" <%=rs[0][0].REQUIRED ? "checked" : ""%> />
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_active);</script></label>&nbsp;&nbsp;<input TYPE="checkbox" id="ActiveFlag" name="ActiveFlag" <%=rs[0][0].ENABLED ? "checked" : ""%> />
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
        <link href="../js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
        <script src="../js/checkphone/intlTelInput.js" type="text/javascript"></script>
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