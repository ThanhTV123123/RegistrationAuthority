<%-- 
    Document   : CAPropertiesEdit
    Created on : Jul 24, 2018, 4:17:08 PM
    Author     : THANH-PC
--%>

<%@page import="vn.ra.utility.PropertiesContent"%>
<%@page import="java.util.ArrayList"%>
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
            document.title = ca_title_edit;
            changeFavicon("../");
            $(document).ready(function () {
                $('#btnClose').click(function () {
                    parent.history.back();
                    return false;
                });
                $('.loading-gif').hide();
            });
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.nameType.value))
                {
                    funErrorAlert(global_req_all);
                    return false;
                }
                var vNameValue = "";
//                if(document.myname.nameType.value.indexOf(JS_STR_TAG_ENABLE) !== -1)
//                {
//                    vNameValue = "false";
//                    var sTempActive = $("#ActiveFlagSoap").bootstrapSwitch("state");
//                    if(sTempActive === true)
//                    {
//                        vNameValue = "true";
//                    }
//                }
//                else {
                if (!JSCheckEmptyField(document.myname.nameValue.value))
                {
                    funErrorAlert(global_req_all);
                    return false;
                }
                vNameValue = document.myname.nameValue.value;
//                alert(document.myname.idID.value);
//                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../CACommon",
                    data: {
                        idParam: 'editproperties',
                        id: document.myname.idID.value,
                        nameType: document.myname.nameType.value,
                        nameValue: vNameValue,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccAlert(global_succ_edit_soap, "CAEdit.jsp?id=" + document.myname.idID.value);
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
                            funErrorAlert(global_errorsql);
                        }
                        $(".loading-gif").hide();
                        $('#over').remove();
                    }
                });
                return false;
            }
            function closeForm()
            {
                window.location = "CAEdit.jsp?id=" + document.myname.idID.value;
            }
        </script>
    </head>
    <body class="nav-md">
        <%            
            if (session.getAttribute("sUserID") != null) {
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
                        document.getElementById("idNameURL").innerHTML = ca_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(global_title_soap_edit);</script></h2>
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
                                            try {
//                                                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                                String Type = EscapeUtils.CheckTextNull(request.getParameter("Type"));
                                                if (EscapeUtils.IsInteger(ids) == true) {
                                                    String sSoapProperties = "";
                                                    CERTIFICATION_AUTHORITY[][] rsRelying = new CERTIFICATION_AUTHORITY[1][];
                                                    db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(EscapeUtils.escapeHtml(ids), rsRelying);
                                                    if (rsRelying[0].length > 0) {
                                                        sSoapProperties = rsRelying[0][0].PROPERTIES;
                                                    }
                                                    String sValue = "";
                                                    if (!"".equals(sSoapProperties)) {
                                                        sValue = PropertiesContent.getPropertiesContentKey(sSoapProperties, Type);
                                                    }
                                                    if (!"".equals(Type)) {
                                        %>
                                        <form name="myname" method="post" class="form-horizontal">
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <input id="idID" value="<%= ids%>" name="idID" type="text" style="display: none;" />
                                            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_type);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input name="nameType" readonly="true" value="<%= Type%>" id="nameType" class="form-control123" type="text"/>
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_value)</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input class="form-control123" value="<%= sValue%>" id="nameValue" name="nameValue"/>
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