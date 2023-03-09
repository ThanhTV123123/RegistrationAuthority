<%-- 
    Document   : CityProvinEdit
    Created on : Feb 26, 2014, 2:36:20 PM
    Author     : Thanh
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
        <title></title>
        <script type="text/javascript">
//            changeFavicon("../");
//            document.title = city_title_edit;
            $(document).ready(function () {
                $('.loading-gif').hide();
                $("#idLblTitleEdits").text(city_title_edit);
                $("#idLblTitleNoData").text(global_no_data);
                $("#idLblTitleCode").text(city_fm_code);
                $("#idLblTitleDesc").text(global_fm_Description);
                $("#idLblTitleActiveFlag").text(global_fm_active);
                $("#idLblTitleCreateUser").text(global_fm_user_create);
                $("#idLblTitleCreateDate").text(global_fm_date_create);
                $("#idLblTitleUpdateUser").text(global_fm_user_endupdate);
                $("#idLblTitleUpdateDate").text(global_fm_date_endupdate);
                $("#idLblNoteDesc").text(global_fm_require_label);
                
//                if(localStorage.getItem("LOCAL_PARAM_CITYLIST") !== null && localStorage.getItem("LOCAL_PARAM_CITYLIST") !== "null")
//                {
//                    var vParamUrl = getUrlParam("id", "");
//                    if(vParamUrl !== localStorage.getItem("LOCAL_PARAM_CITYLIST"))
//                    {
//                        window.location = "../Admin/Home.jsp";
//                    }
//                } else {
//                    window.location = "CityProvin.jsp";
//                }
            });
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'SessRefreshCity'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "CityProvin.jsp";
                        }
                        else
                        {
                            window.location = "CityProvin.jsp";
                        }
                    }
                });
                return false;
            }
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.Remark.value))
                {
                    document.myname.Remark.focus();
                    funErrorAlert(policy_req_empty + global_fm_Description);
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
                    url: "../CityCommon",
                    data: {
                        idParam: 'editcity',
                        id: document.myname.cityid.value,
                        citycode: document.myname.citycode.value,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.Remark.value,
                        ActiveFlag: sCheckActiveFlag,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            localStorage.setItem("EDIT_PROVINCE", document.myname.cityid.value);
                            funSuccAlert(city_succ_edit, "CityProvin.jsp");
                        }
                        else if (myStrings[0] === "10")
                        {
                            funErrorAlert(global_req_all);
                        }
                        else if (myStrings[0] === "11")
                        {
                            funErrorAlert(global_req_length);
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
                                funErrorAlert(city_exists_code);
                            } else if (myStrings[1] === "2") {
                                funErrorAlert(city_exists_name);
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
                    CITY_PROVINCE[][] rs = new CITY_PROVINCE[1][];
                    try {
                        String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                        if (EscapeUtils.IsInteger(ids) == true) {
                            db.S_BO_PROVINCE_DETAIL(EscapeUtils.escapeHtml(ids), rs);
                            if (rs[0].length > 0) {
                                String strDesc = EscapeUtils.CheckTextNull(rs[0][0].REMARK);
                %>
                <form name="myname" method="post" class="form-horizontal">
                    <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
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
                                    <input class="form-control123" value="<%= strDesc%>" maxlength="256" id="Remark" name="Remark"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
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
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCreateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" readonly name="strCreateUser" class="form-control123" value="<%= rs[0][0].CREATED_BY%>" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
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
                                <label id="idLblTitleUpdateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" readonly name="strUpdateUser" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_BY)%>" />
                                </div>
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
        <!--<script src="../style/custom.min.js"></script>-->
        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/main.js"></script>
    </body>
</html>