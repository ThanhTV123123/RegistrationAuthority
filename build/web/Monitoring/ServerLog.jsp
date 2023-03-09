<%-- 
    Document   : ServerLog
    Created on : Aug 28, 2020, 2:47:42 PM
    Author     : USER
--%>

<%@page import="vn.ra.utility.ConfigLog"%>
<%@page import="java.util.ArrayList"%>
<%@page import="vn.ra.utility.PropertiesContent"%>
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
            document.title = serverlog_title_list;
            changeFavicon("../");
            $(document).ready(function () {
                $('.loading-gif').hide();
                $('#DateDown').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
            });
            function popupDownloadLog()
            {
                var f = document.myname;
                f.method = "post";
                f.action = '../DownFromSaveFile?idParam=downloadlog&textDateLog='+ $("#DateDown").val() + '&textTypeLog=' + $("#idTypeLogDown").val();
                f.submit();
            }
        </script>
    </head>
    <body class="nav-md">
        <%
        if ((session.getAttribute("sUserID")) != null) {
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String sHasLog = "none";
            String sFileName = "";
            MonitorLogType[][] rsLogTypeStart = new MonitorLogType[1][];
            CommonFunction.ListLogTypeConfig(rsLogTypeStart);
            if(rsLogTypeStart[0].length > 0) {
                sFileName = EscapeUtils.CheckTextNull(rsLogTypeStart[0][0].LogFileName);
            }
            ConfigLog cnf = new ConfigLog();
            String pathLogTemp = cnf.GetPropertybyCode("log");
            pathLogTemp = pathLogTemp.replace("$", "");
            pathLogTemp = pathLogTemp.replace("{", "");
            pathLogTemp = pathLogTemp.replace("}", "");
            pathLogTemp = pathLogTemp.replace("/", "");
            String sPath = System.getProperty(pathLogTemp).trim() + "/";
            String sLogView = CommonFunction.GetMonitorServerLogString(100, sFileName, sPath);
            if(!"".equals(sLogView))
            {
                sHasLog = "";
            }
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
                        document.getElementById("idNameURL").innerHTML = serverlog_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
<!--                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(serverlog_fm_detail);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>-->
                                    <div class="x_content">
                                        <form name="myname" method="post" class="form-horizontal">
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <fieldset class="scheduler-border" style="padding-bottom: 5px !important; ">
                                                <legend class="scheduler-border"><script>document.write(serverlog_title_down);</script></legend>
                                                <div class="col-sm-5" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <div class='col-sm-5' style='padding-left: 0px;margin-left: 0px;padding-top: 7px;'>
                                                            <label class="control-label123"><script>document.write(serverlog_fm_typelog);</script></label>
                                                        </div>
                                                        <div class='col-sm-7' style='padding-right: 0px;'>
                                                            <select name="idTypeLogDown" id="idTypeLogDown" class="form-control123">
                                                                <%
                                                                    MonitorLogType[][] rsLogTypeDown = new MonitorLogType[1][];
                                                                    CommonFunction.ListLogTypeConfig(rsLogTypeDown);
                                                                    if(rsLogTypeDown[0].length > 0)
                                                                    {
                                                                        for (MonitorLogType item : rsLogTypeDown[0]) {
                                                                %>
                                                                <option value="<%= item.LogFileName%>"><%= item.LogTypeDesc%></option>
                                                                <%
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-5" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <div class='col-sm-5' style='padding-left: 0px;margin-left: 0px;padding-top: 7px;'>
                                                            <label class="control-label123"><script>document.write(serverlog_fm_timestamp);</script></label>
                                                        </div>
                                                        <div class='col-sm-7' style='padding-right: 0px;'>
                                                            <input type="text" id="DateDown" name="DateDown" value="<%= com.ConvertMonthSub(0)%>" maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-2" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <input id="btnDown" class="btn btn-info" style="width: 100px;" type="button" onclick="popupDownloadLog();" />
                                                        <script>
                                                            document.getElementById("btnDown").value = global_fm_down;
                                                        </script>
                                                    </div>
                                                </div>
                                            </fieldset>
                                            <fieldset class="scheduler-border" style="padding-bottom: 5px !important; ">
                                                <legend class="scheduler-border"><script>document.write(serverlog_title_todate);</script></legend>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <div class='col-sm-5' style='padding-left: 0px;margin-left: 0px;padding-top: 7px;'>
                                                            <label class="control-label123"><script>document.write(serverlog_fm_typelog);</script></label>
                                                        </div>
                                                        <div class='col-sm-7' style='padding-right: 0px;'>
                                                            <select name="idTypeLogView" id="idTypeLogView" class="form-control123">
                                                            <%
                                                                MonitorLogType[][] rsLogTypeView = new MonitorLogType[1][];
                                                                CommonFunction.ListLogTypeConfig(rsLogTypeView);
                                                                if(rsLogTypeView[0].length > 0)
                                                                {
                                                                    for (MonitorLogType item : rsLogTypeView[0]) {
                                                            %>
                                                            <option value="<%= item.LogFileName%>"><%= item.LogTypeDesc%></option>
                                                            <%
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <input TYPE="checkbox" id="IsAutoReloadLog" name="IsAutoReloadLog" onchange="changeCheckReload('<%= anticsrf%>');"/> <script>document.write(hastatus_fm_auto);</script>
                                                        </label>
                                                        <div class="col-sm-6" style="padding-right: 0px;">
                                                            <select id="idTimeReLoad" name="idTimeReLoad" class="form-control123">
                                                                <%
                                                                    ArrayList<String> sValueLog = CommonFunction.ReloadLogTimeConfig();
                                                                    for (String itemLog : sValueLog) {
                                                                %>
                                                                <option value="<%= itemLog.trim()%>"><%= itemLog.trim()%></option>
                                                                <%
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <input id="btReload" class="btn btn-info" style="width: 100px;" type="button" onclick="SearchLogTodate('<%=anticsrf%>');" />
                                                        <script>
                                                            document.getElementById("btReload").value = global_fm_button_reload;
                                                        </script>
                                                    </div>
                                                </div>
                                                <script>
                                                    $(document).ready(function () {
                                                        $('#IsAutoReloadLog').prop('checked', false);
                                                    });
                                                    function SearchLogTodate()
                                                    {
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../JSONCommon",
                                                            data: {
                                                                idParam: 'loadserverlogonline',
                                                                TypeLogView: $("#idTypeLogView").val()
                                                                //, CsrfToken: idCSRF
                                                            },
                                                            cache: false,
                                                            success: function (html)
                                                            {
                                                                if (html.length > 0)
                                                                {
                                                                    var obj = JSON.parse(html);
                                                                    if (obj[0].Code === "0")
                                                                    {
                                                                        $("#idViewLog").val(obj[0].ResLogView);
                                                                        var psconsole = $('#idViewLog');
                                                                        if (psconsole.length)
                                                                            psconsole.scrollTop(psconsole[0].scrollHeight - psconsole.height());
                                                                        goToByScroll("idViewDetailScroll");
                                                                    }
                                                                    else if (obj[0].Code === "1")
                                                                    {
                                                                        $("#idViewLog").val('');
                                                                        goToByScroll("idViewDetailScroll");
                                                                    }
                                                                    else if (obj[0].Code === JS_EX_CSRF)
                                                                    {
                                                                        funCsrfAlert();
                                                                    }
                                                                    else if (obj[0].Code === JS_EX_LOGIN)
                                                                    {
                                                                        RedirectPageLoginNoSess(global_alert_login);
                                                                    }
                                                                    else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                                                    {
                                                                        RedirectPageLoginNoSess(global_alert_another_login);
                                                                    }
                                                                    else {
                                                                        $("#idViewLog").val('');
                                                                        goToByScroll("idViewDetailScroll");
                                                                        funErrorAlert(global_errorsql);
                                                                    }
                                                                }
                                                                else
                                                                {
                                                                    $("#idViewLog").val('');
                                                                    goToByScroll("idViewDetailScroll");
                                                                }
                                                            }
                                                        });
                                                        return false;
                                                    }
                                                    function changeCheckReload() {
                                                        var sTime = $("#idTimeReLoad").val();
                                                        if ($("#IsAutoReloadLog").is(':checked')) {
                                                            $("#idTimeReLoad").prop("disabled", true);
                                                            $("#idTypeLogView").prop("disabled", true);
                                                        } else {
                                                            $("#idTimeReLoad").prop("disabled", false);
                                                            $("#idTypeLogView").prop("disabled", false);
                                                        }
                                                        var id = setInterval(frame, sTime * 1000);
                                                        function frame() {
                                                            if ($("#IsAutoReloadLog").is(':checked')) {
                                                                SearchLogTodate();
                                                            } else {
                                                                clearInterval(id);
                                                            }
                                                        }
                                                    }
                                                </script>
                                                <div class="form-group" id="idViewDetailScroll" style="padding-top: 5px; clear: both; padding-right: 0px;display: <%= sHasLog%>">
                                                    <textarea type="text" id="idViewLog" name="idViewLog"
                                                        readonly class="form-control123" style="height: 500px;background: #000000;color: #ffffff;"><%= sLogView%></textarea>
                                                </div>
                                                <script>
                                                    function moveCaretToEnd(el) {
                                                        if (typeof el.selectionStart == "number") {
                                                            el.selectionStart = el.selectionEnd = el.value.length;
                                                        } else if (typeof el.createTextRange != "undefined") {
                                                            el.focus();
                                                            var range = el.createTextRange();
                                                            range.collapse(false);
                                                            range.select();
                                                        }
                                                    }
                                                </script>
                                            </fieldset>
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
        <script src="../js/moment.min.js"></script>
        <script src="../js/daterangepicker.js"></script>
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