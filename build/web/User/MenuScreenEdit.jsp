<%-- 
    Document   : MenuScreenEdit
    Created on : Nov 24, 2017, 11:23:15 AM
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
        <!--<link href="../Css/active/bootstrap-switch.css" rel="stylesheet">-->
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            document.title = menusc_title_edit;
            $(document).ready(function () {
                $('.loading-gif').hide();
                $("#idLblTitleEdits").text(menusc_title_edit);
                $("#idLblTitleParentID").text(menusc_fm_nameparent);
                $("#idLblTitleLinkName").text(menusc_fm_code);
                $("#idLblTitleLinkUrl").text(menusc_fm_url);
                $("#idLblTitleActiveFlag").text(global_fm_active);
                $("#idLblTitleRemark").text(global_fm_remark_vn);
                $("#idLblNoteRemark").text(global_fm_require_label);
                $("#idLblTitleRemark_EN").text(global_fm_remark_en);
                $("#idLblNoteRemark_EN").text(global_fm_require_label);
                $("#idLblTitleCreateUser").text(global_fm_user_create);
                $("#idLblTitleCreateDate").text(global_fm_date_create);
                $("#idLblTitleUpdateUser").text(global_fm_user_endupdate);
                $("#idLblTitleUpdateDate").text(global_fm_date_endupdate);
//                if(localStorage.getItem("LOCAL_PARAM_MENUSCREENLIST") !== null && localStorage.getItem("LOCAL_PARAM_MENUSCREENLIST") !== "null")
//                {
//                    var vParamUrl = getUrlParam("id", "");
//                    if(vParamUrl !== localStorage.getItem("LOCAL_PARAM_MENUSCREENLIST"))
//                    {
//                        window.location = "../Admin/Home.jsp";
//                    }
//                } else {
//                    window.location = "MenuScreenList.jsp";
//                }
            });
            function ValidateForm(idCSRF)
            {
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
                if (document.myname.ParentID.value !== JS_STR_GRID_COMBOBOX_VALUE_ALL)
                {
                    if (!JSCheckEmptyField(document.myname.LinkUrl.value))
                    {
                        document.myname.LinkUrl.focus();
                        funErrorAlert(policy_req_empty + menusc_fm_url);
                        return false;
                    }
                }
                var sCheckActiveFlag = "0";
//                var sTempActive = $("#ActiveFlag").bootstrapSwitch("state");
                if ($("#ActiveFlag").is(':checked'))
                {
                    sCheckActiveFlag = "1";
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../MenuLinkCommon",
                    data: {
                        idParam: 'editmenuscreen',
                        id: document.myname.MenuLinkID.value,
                        ParentId: document.myname.ParentID.value,
                        LinkName: document.myname.LinkName.value,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.Remark_EN.value,
                        LinkUrl: document.myname.LinkUrl.value,
                        ActiveFlag: sCheckActiveFlag,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0") {
                            localStorage.setItem("EDIT_MENUSCREEN", document.myname.MenuLinkID.value);
                            funSuccAlert(menusc_succ_edit, "MenuScreenList.jsp");
                        } else if (myStrings[0] === "10")
                        {
                            funErrorAlert(global_req_all);
                        }
                        else if (myStrings[0] === "11")
                        {
                            funErrorAlert(global_req_length);
                        } else if (myStrings[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        } else if (myStrings[0] === JS_EX_LOGIN) {
                            RedirectPageLoginNoSess(global_alert_login);
                        } else if (myStrings[0] === JS_EX_ANOTHERLOGIN) {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        } else {
                            if (myStrings[1] === "1") {
                                if (document.myname.ParentID.value === JS_STR_GRID_COMBOBOX_VALUE_ALL)
                                {
                                    funErrorAlert(menusc_exists_nameparent);
                                }
                                else {
                                    funErrorAlert(menusc_exists_linkurl);
                                }
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
                        idSession: 'SessRefreshMenuScreen'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "MenuScreenList.jsp";
                        }
                        else
                        {
                            window.location = "MenuScreenList.jsp";
                        }
                    }
                });
                return false;
            }
        </script>
    </head>
    <body>
        <%
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEdits"></span></h2>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <input type="button" id="btnSave" data-switch-get="state" class="btn btn-info" onclick="return ValidateForm('<%=anticsrf%>');"/>
                    <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                    <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                    <input id="idUrlFile" name="idUrlFile" type="text" style="display: none;"/>
                    <script>
                        document.getElementById("btnSave").value = global_fm_button_edit;
                        document.getElementById("btnClose").value = global_fm_button_close;
                    </script>
                </li>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <%
                MENULINK[][] rs = new MENULINK[1][];
                try {
                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                    String ids = request.getParameter("id");
                    if (EscapeUtils.IsInteger(ids) == true) {
                        db.S_BO_URI_DETAIL(EscapeUtils.escapeHtml(ids), rs);
                        if (rs[0].length > 0) {
                            String strCode = EscapeUtils.CheckTextNull(rs[0][0].NAME);
                            String strDescEN = EscapeUtils.CheckTextNull(rs[0][0].REMARK_EN);
                            String strDescVN = EscapeUtils.CheckTextNull(rs[0][0].REMARK);
                            String strUrl = EscapeUtils.CheckTextNull(rs[0][0].LINKURL);
                            String isDisabled = "";
                            if (rs[0][0].PARENT_ID == Definitions.CONFIG_RESULTSET_VALUE_INT_NULL ||
                                rs[0][0].PARENT_ID == Definitions.CONFIG_EMPTY_N_A_GLOBAL) {
                                isDisabled = "disabled";
                            }
            %>
            <form name="myname" method="post" class="form-horizontal">
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleParentID" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="ParentID" id="ParentID" class="form-control123" onchange="onChangeParent(this);">
                                <%
                                    if (rs[0][0].PARENT_ID == Definitions.CONFIG_RESULTSET_VALUE_INT_NULL ||
                                        rs[0][0].PARENT_ID == Definitions.CONFIG_EMPTY_N_A_GLOBAL) {
                                %>
                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" id="idLblTitleParentIDAll"></option>
                                <%
                                } else {
                                %>
                                <%
                                    MENULINK[][] rsParams = new MENULINK[1][];
                                    try {
                                        db.S_BO_URI_PARENT_COMBOBOX(rsParams, sessLanguageGlobal);
                                        if (rsParams[0].length > 0) {
                                            for (int j = 0; j < rsParams[0].length; j++) {
                                %>
                                <option value="<%= String.valueOf(rsParams[0][j].ID)%>" <%= rsParams[0][j].ID == rs[0][0].PARENT_ID ? "selected" : ""%>><%=rsParams[0][j].PARENTLINK_REMARK%></option>
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
                                <%
                                    }
                                %>
                            </select>
                            <script>
                                $("#idLblTitleParentIDAll").text(global_fm_combox_no_choise);
                                function onChangeParent(obj)
                                {
                                    if (obj.value === JS_STR_GRID_COMBOBOX_VALUE_ALL)
                                    {
                                        document.getElementById("LinkUrl").disabled = true;
                                    }
                                    else {
                                        document.getElementById("LinkUrl").disabled = false;
                                    }
                                }
                            </script>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleLinkName" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="MenuLinkID" style="display: none;" value="<%= rs[0][0].ID%>" />
                            <input type="text" readonly name="LinkName" class="form-control123" value="<%= strCode%>" />
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                            <label id="idLblTitleRemark"></label>
                            <label class="CssRequireField" id="idLblNoteRemark"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input maxlength="256" type="text" name="Remark" class="form-control123" value="<%= strDescVN%>" />
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                            <label id="idLblTitleRemark_EN"></label>
                            <label class="CssRequireField" id="idLblNoteRemark_EN"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" maxlength="256" name="Remark_EN" class="form-control123" value="<%= strDescEN%>" />
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                            <label id="idLblTitleLinkUrl"></label>
                            <%
                                if("".equals(isDisabled)) {
                            %>
                            <label class="CssRequireField" id="idLblNoteidLblTitleLinkUrl"></label>
                            <script>
                                $(document).ready(function () {
                                    $("#idLblNoteidLblTitleLinkUrl").text(global_fm_require_label);
                                });
                            </script>
                            <%
                                }
                            %>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" maxlength="256" id="LinkUrl" name="LinkUrl" class="form-control123" value="<%= strUrl%>" <%= isDisabled%>/>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleActiveFlag" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="ActiveFlag">
                                <input TYPE="checkbox" id="ActiveFlag" name="ActiveFlag" <%=rs[0][0].ENABLED ? "checked" : ""%> />
                                <div class="slider round"></div>
                            </label>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCreateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strCreateUser" class="form-control123" value="<%= rs[0][0].CREATED_BY%>" />
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCreateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strDateLimit" class="form-control123" value="<%= rs[0][0].CREATED_DT%>" />
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUpdateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strUpdateUser" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_BY)%>" />
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUpdateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strUpdateDate" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_DT)%>" />
                        </div>
                    </div>
                </div>
            </form>
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
        </div>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <!--<script src="../style/custom.min.js"></script>-->
<!--        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/bootstrap-switch.js"></script>
        <script src="../js/active/main.js"></script>-->
    </body> 
</html>