<%-- 
    Document   : MenuScreenNew
    Created on : Nov 24, 2017, 11:23:33 AM
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
            changeFavicon("../");
            document.title = menusc_title_add;
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.LinkName.value))
                {
                    document.myname.LinkName.focus();
                    funErrorAlert(policy_req_empty + menusc_fm_code);
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
                if(document.myname.ParentID.value !== JS_STR_GRID_COMBOBOX_VALUE_ALL)
                {
                    if (!JSCheckEmptyField(document.myname.LinkUrl.value))
                    {
                        document.myname.LinkUrl.focus();
                        funErrorAlert(policy_req_empty + menusc_fm_url);
                        return false;
                    }
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../MenuLinkCommon",
                    data: {
                        idParam: 'addmenuscreen',
                        ParentId: document.myname.ParentID.value,
                        LinkName: document.myname.LinkName.value,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.Remark_EN.value,
                        LinkUrl: document.myname.LinkUrl.value,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccAlert(menusc_succ_add, "MenuScreenList.jsp");
                        } else if (myStrings[0] === "10")
                        {
                            funErrorAlert(global_req_all);
                        } else if (myStrings[0] === "11")
                        {
                            funErrorAlert(global_req_length);
                        } else if (myStrings[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        } else if (myStrings[0] === JS_EX_LOGIN) {
                            RedirectPageLoginNoSess(global_alert_login);
                        } else if (myStrings[0] === JS_EX_ANOTHERLOGIN) {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else
                        {
                            if (myStrings[1] === "1") {
                                if(document.myname.ParentID.value === JS_STR_GRID_COMBOBOX_VALUE_ALL)
                                {
                                    funErrorAlert(menusc_exists_nameparent);
                                }
                                else
                                {
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
                funLocationBack("MenuScreenList.jsp");
            }
        </script>
    </head>
    <body class="nav-md">
    <%
        if ((session.getAttribute("sUserID")) != null) {
            String anticsrf = "" + Math.random();
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
                        document.getElementById("idNameURL").innerHTML = menusc_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleEdits" style="color: #36526D;"></span></h2>
                                        <script>$("#idLblTitleEdits").text(menusc_title_add);</script>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <input type="button" id="btnSave" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');"/>
                                                <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                                                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
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
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleParentID" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="ParentID" id="ParentID" class="form-control123" onchange="onChangeParent(this);">
                                                            <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" id="idLblTitleParentIDAll"></option>
                                                            <%
                                                                MENULINK[][] rsParams = new MENULINK[1][];
                                                                try {
                                                                    db.S_BO_URI_PARENT_COMBOBOX(rsParams, sessLanguageGlobal);
                                                                    if (rsParams[0].length > 0) {
                                                                        for (int j = 0; j < rsParams[0].length; j++) {
                                                            %>
                                                            <option value="<%= String.valueOf(rsParams[0][j].ID) %>"><%=rsParams[0][j].PARENTLINK_REMARK %></option>
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
                                                        <script>
                                                            $(document).ready(function () {
                                                                $("#idLblTitleParentID").text(menusc_fm_nameparent);
                                                                $("#idLblTitleParentIDAll").text(global_fm_combox_no_choise);
                                                                document.getElementById("LinkUrl").disabled = true;
                                                                $("#idReqLabel").css("display", "none");
                                                            });
                                                            function onChangeParent(obj)
                                                            {
                                                                if(obj.value === JS_STR_GRID_COMBOBOX_VALUE_ALL)
                                                                {
                                                                    document.getElementById("LinkUrl").disabled = true;
                                                                    $("#idReqLabel").css("display", "none");
                                                                }
                                                                else{
                                                                    document.getElementById("LinkUrl").disabled = false;
                                                                    $("#idReqLabel").css("display", "");
                                                                }
                                                            }
                                                        </script>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleCode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" maxlength="64" name="LinkName" class="form-control123" />
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleCode").text(menusc_fm_code);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleRemark"></label>
                                                        <label id="idLblNoteRemark"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input maxlength="256" type="text" name="Remark" class="form-control123" />
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleRemark").text(global_fm_remark_vn);
                                                    $("#idLblNoteRemark").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleRemark_EN"></label>
                                                        <label id="idLblNoteRemark_EN"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" maxlength="256" name="Remark_EN" class="form-control123" />
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleRemark_EN").text(global_fm_remark_en);
                                                    $("#idLblNoteRemark_EN").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleUrl"></label>
                                                        <label id="idReqLabel"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_URL%>" id="LinkUrl" name="LinkUrl" class="form-control123" />
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleUrl").text(menusc_fm_url);
                                                    $("#idReqLabel").text(global_fm_require_label);
                                                </script>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <script src="../style/jquery.min.js"></script>
                    <script src="../style/bootstrap.min.js"></script>
                    <script src="../style/custom.min.js"></script>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
        </div>
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