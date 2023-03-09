<%-- 
    Document   : FormFactorEdit
    Created on : Feb 5, 2021, 4:49:26 PM
    Author     : USER
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
<!--        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">-->
        <link href="../style/custom.min.css" rel="stylesheet">
        <!--<script src="../js/Language.js"></script>-->
        <script src="../js/process_javajs.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link href="../js/jsoneditor.css" rel="stylesheet" type="text/css">
        <script src="../js/jsoneditor.js"></script>
        <title></title>
        <script type="text/javascript">
            $(document).ready(function () {
                $('.loading-gif').hide();
                $("#idLblTitleEdits").text(formfactor_title_edit);
                $("#idLblTitleNoData").text(global_no_data);
                $("#idLblTitleCode").text(formfactor_fm_code);
                $("#idLblTitleDesc").text(global_fm_remark_vn);
                $("#idLblTitleDescEN").text(global_fm_remark_en);
                $("#idLblTitleActiveFlag").text(global_fm_active);
//                $("#idLblTitleCreateUser").text(global_fm_user_create);
                $("#idLblTitleCreateDate").text(global_fm_date_create);
//                $("#idLblTitleUpdateUser").text(global_fm_user_endupdate);
                $("#idLblTitleUpdateDate").text(global_fm_date_endupdate);
                $("#idLblNoteDesc").text(global_fm_require_label);
            });
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'SessRefreshFormFactor'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "FormFactorList.jsp";
                        }
                        else
                        {
                            window.location = "FormFactorList.jsp";
                        }
                    }
                });
                return false;
            }
            const options = {};
            const editor = new JSONEditor(document.getElementById("divJsonProperties"), options);
            function LoadProperties() {
                var sPro = document.getElementById("idPROPERTIES").innerHTML;
                const initialJson = JSON.parse(sPro);
                editor.set(initialJson);
                editor.expandAll();
            }
            $(document).ready(function () {
                LoadProperties();
            });
            function ValidateForm(idCSRF) {
                var updatedJson = JSON.stringify(editor.get());
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
                        idParam: 'editformfactor',
                        id: document.myname.cityid.value,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.Remark_EN.value,
                        JsonProperties: updatedJson,
                        ActiveFlag: sCheckActiveFlag,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccAlert(formfactor_succ_edit, "FormFactorList.jsp");
                        }
                        else if (myStrings[0] === "10")
                        {
                            funErrorAlert(global_req_all);
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
                                funErrorAlert(formfactor_exists_name);
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
    <body>
        <%
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <div class="x_panel">
            <div class="x_title">
                <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEdits"></span></h2>
                <ul class="nav navbar-right panel_toolbox">
                    <li>
                        <input type="button" id="btnSave" data-switch-get="state" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');"/>
                        <input type="button" id="btnClose" class="btn btn-info" onclick="closeForm('<%=anticsrf%>');"/>
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
                    PKI_FORMFACTOR[][] rs = new PKI_FORMFACTOR[1][];
                    try {
                        String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                        if (EscapeUtils.IsInteger(ids) == true) {
                            db.S_BO_PKI_FORMFACTOR_DETAIL(EscapeUtils.escapeHtml(ids), rs);
                            if (rs[0].length > 0) {
                %>
                <form name="myname" method="post" class="form-horizontal">
                    <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                    <textarea id="idPROPERTIES" readonly="true" style="display: none;" name="idPROPERTIES"><%=rs[0][0].PROPERTIES %></textarea>
                    <!--<input type="text" id="idPROPERTIES" name="idPROPERTIES" value="<=rs[0][0].PROPERTIES %>"/>-->
                    <div class="form-group" style="padding: 0;">
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="hidden" name="cityid" value="<%= rs[0][0].ID%>" />
                                    <input type="text" readonly="true" name="citycode" class="form-control123" value="<%= rs[0][0].NAME%>" />
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                    <label id="idLblTitleDesc"></label>
                                    <label class="CssRequireField" id="idLblNoteDesc"></label>
                                </label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].REMARK)%>" maxlength="256" id="Remark" name="Remark"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                    <label id="idLblTitleDescEN"></label>
                                    <label class="CssRequireField" id="idLblNoteDesc"></label>
                                </label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].REMARK_EN)%>" maxlength="256" id="Remark_EN" name="Remark_EN"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleActiveFlag" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <label class="switch" for="ActiveFlag">
                                        <input TYPE="checkbox" id="ActiveFlag" name="ActiveFlag" <%=rs[0][0].ENABLED ? "checked='checked'" : ""%> />
                                        <div class="slider round"></div>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group" style="margin-top: 0;">
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCreateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" readonly="true" name="dates" class="form-control123" value="<%= rs[0][0].CREATED_DT%>" />
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleUpdateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" readonly="true" name="UpdateDate" class="form-control123" value="<%= rs[0][0].MODIFIED_DT%>" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <fieldset class="scheduler-border">
                            <legend class="scheduler-border" id="idLblTitleFactorPro"></legend>
                            <div style="padding: 0px 0px 0px 0px;margin: 0;height: 500px;" id="divJsonProperties"></div>
                            <script>$("#idLblTitleFactorPro").text(formfactor_title_properties);</script>
                        </fieldset>
                    </div>
<!--                    <script>
                        const options = {};
                        const editor = new JSONEditor(document.getElementById("divJsonProperties"), options);
                        function LoadProperties() {
//                            const container = document.getElementById("divJsonProperties");
//                            const options = {};
//                            const editor = new JSONEditor(container, options);
//                            const editor = new JSONEditor(document.getElementById("divJsonProperties"));
                            var sPro = document.getElementById("idPROPERTIES").innerHTML;
                            
                            const initialJson = JSON.parse(sPro);
                            editor.set(initialJson);
                            this.editor.expandAll();
                        }
//                        $(document).ready(function () {
//                        const container = document.getElementById("divJsonProperties");
//                        const options = {};
//                        const editor = new JSONEditor(container, options);
//                        var sPro = document.getElementById("idPROPERTIES").innerHTML;
//
//                        const initialJson = JSON.parse(sPro);
//                        editor.set(initialJson);
//                        this.editor.expandAll();
                        function onSavePro(){
//                            const container = document.getElementById("divJsonProperties");
//                            const options = {};
//                            const editor = new JSONEditor(container, options);
//                            var sPro = document.getElementById("idPROPERTIES").innerHTML;
                            
                            const updatedJson = editor.get();
                            
                            console.log(JSON.stringify(updatedJson));
//                            return JSON.stringify(document.getElementById("divJsonProperties").innerHTML);
                        }
//                        });

                        $(document).ready(function () {
                            LoadProperties();
                        });
                    </script>-->
                </form>
                <%
                } else {
                %>
                <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
                    <label style="color: red;" id="idLblTitleNoData"></label>
                </div>
                <%
                    }
                } else {
                %>
                <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
                    <label style="color: red;" id="idLblTitleNoData"></label>
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
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/main.js"></script>
    </body>
</html>