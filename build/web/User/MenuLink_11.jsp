<%-- 
    Document   : MenuLink
    Created on : Nov 24, 2017, 5:32:04 PM
    Author     : THANH-PC
--%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<!DOCTYPE html>
<%  response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"> 
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link id="themecss" rel="stylesheet" type="text/css" href="//www.shieldui.com/shared/components/latest/css/light-glow/all.min.css" />
    <script type="text/javascript" src="//www.shieldui.com/shared/components/latest/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="//www.shieldui.com/shared/components/latest/js/shieldui-all.min.js"></script>
        <title></title>
        <script>
            changeFavicon("../");
            document.title = menu_title_list;
            $(document).ready(function () {
                $('.loading-gif').hide();
                document.getElementById("idUserRole").selectedIndex = 0;
            });
            function exportRecord(idCSRF) {
                if(JSCheckEmptyField(document.getElementById('idUserRoleNot').value))
                {
                    $('body').append('<div id="over"></div>');
                    $(".loading-gif").show();
                    $.ajax({
                        type: "post",
                        url: "../MenuLinkCommon",
                        data: {
                            idParam: 'addmenu',
                            id: document.getElementById('idUserRoleNot').value,
                            role: document.getElementById('idUserRole').value,
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
                                var vCertType = document.getElementById("idUserRole").value;
                                NotAssignTemplate(vCertType, idCSRF);
                                YesAssignTemplate(vCertType, idCSRF);
                                funSuccNoLoad(menu_succ_insert);
                            }
                            else if (myStrings[0] === JS_EX_CSRF)
                            {
                                funCsrfAlert();
                            }
                            else if (myStrings[0] === "1") {
                                funErrorAlert(menu_error_insert);
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
                                funErrorAlert(global_errorsql);
                            }
                            $(".loading-gif").hide();
                            $('#over').remove();
                        }
                    });
                    return false;
                }
            }
            function deleteRecord(id, idCSRF) {
                swal({
                    title: "",
                    text: menu_conform_delete,
                    imageUrl: "../Images/icon_warning.png",
                    imageSize: "45x45",
                    showCancelButton: true,
                    closeOnConfirm: true,
                    allowOutsideClick: false,
                    confirmButtonText: login_fm_buton_OK,
                    cancelButtonText: global_button_grid_cancel,
                    cancelButtonColor: "#199DC0"
                },
                function () {
                    $('body').append('<div id="over"></div>');
                    $(".loading-gif").show();
                    setTimeout(function () {
                        $.ajax({
                            type: "post",
                            url: "../MenuLinkCommon",
                            data: {
                                idParam: 'deletemenu',
                                id: id,
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
                                    var vCertType = document.getElementById("idUserRole").value;
                                    NotAssignTemplate(vCertType, idCSRF);
                                    YesAssignTemplate(vCertType, idCSRF);
                                    funSuccNoLoad(menu_succ_delete);
                                }
                                else if (myStrings[0] === JS_EX_CSRF)
                                {
                                    funCsrfAlert();
                                }
                                else if (myStrings[0] === "1") {
                                    funErrorAlert(menu_error_delete);
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
                                    funErrorAlert(global_errorsql);
                                }
                                $(".loading-gif").hide();
                                $('#over').remove();
                            }
                        });
                        return false;
                    }, JS_STR_ACTION_TIMEOUT);
                });
            }
            function confirm_delete(id, idCSRF) {
                swal({
                    title: "",
                    text: menu_conform_delete,
                    imageUrl: "../Images/icon_warning.png",
                    imageSize: "45x45",
                    showCancelButton: true,
                    closeOnConfirm: true,
                    allowOutsideClick: false,
                    confirmButtonText: login_fm_buton_OK,
                    cancelButtonText: global_button_grid_cancel,
                    cancelButtonColor: "#199DC0"
                },
                function () {
                    $('body').append('<div id="over"></div>');
                    $(".loading-gif").show();
                    setTimeout(function () {
                        deleteRecord(id, idCSRF);
                    }, JS_STR_ACTION_TIMEOUT);
                });
            }
            function exportRecordAbc(id) {
                document.getElementById("idHiddenLoad").value = JS_STR_GRID_SEARCH_RESET;
                document.getElementById("tempCsrfToken").value = id;
                var f = document.myname;
                f.action = "";
                f.method = "post";
                f.submit();
            }
        </script>
        <style>
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.4em 1.4em 1.4em !important;
                margin: 0 0 1.5em 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }

            legend.scheduler-border {
                font-size: 14px !important;
                font-weight: bold !important;
                text-align: left !important;
                width:auto;
                padding:0 10px;
                border-bottom:none;
            }
        </style>
        <style>
            .container1
            {
                max-width: 400px;
                margin: auto;
            }
            .treeview-icon
            {
                width: 16px;
                height: 16px;
                background-image: url("/Content/img/file/file-icons-sprite.png");
            }
            .icon-folder
            {
                background-position: 0px 0px;
            }
            .icon-png
            {
                background-position: -16px 0px;
            }
            .icon-txt
            {
                background-position: -32px 0px;
            }
            .icon-pdf
            {
                background-position: -48px 0px;
            }
            .icon-doc
            {
                background-position: -64px 0px;
            }
            .icon-xls
            {
                background-position: -80px 0px;
            }
        </style>
    </head>
    <body class="nav-md">
        <%
        if ((session.getAttribute("sUserID")) != null) {
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String sessLanguageGlobal = session.getAttribute("sessVN").toString();
            String sRoleFrist = "";
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
                        document.getElementById("idNameURL").innerHTML = menu_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <%
                                    try{
                                %>
                                <div class="x_panel">
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="myname" role="form" method="post" class="form-horizontal">
                                            <div class="container1">
                                                <ul id="treeview">
                                                    <li data-icon-cls="treeview-icon icon-folder" data-expanded="true">Web Project
                                                        <ul>
                                                            <li data-icon-cls="treeview-icon icon-folder" data-expanded="true">css
                                                                <ul>
                                                                    <li data-icon-cls="treeview-icon icon-txt">theme.css</li>
                                                                </ul>
                                                            </li>
                                                            <li data-icon-cls="treeview-icon icon-folder" data-expanded="true">images
                                                                <ul>
                                                                    <li data-icon-cls="treeview-icon icon-png">background.png</li>
                                                                    <li data-icon-cls="treeview-icon icon-png">buttons-sprite.png</li>
                                                                    <li data-icon-cls="treeview-icon icon-png">logo.png</li>
                                                                </ul>
                                                            </li>
                                                            <li data-icon-cls="treeview-icon icon-folder" data-expanded="true">js
                                                                <ul>
                                                                    <li data-icon-cls="treeview-icon icon-txt">jquery-1.10.2.min.js</li>
                                                                    <li data-icon-cls="treeview-icon icon-txt">site.js</li>
                                                                </ul>
                                                            </li>
                                                            <li data-icon-cls="treeview-icon icon-folder" data-expanded="true">files
                                                                <ul>
                                                                    <li data-icon-cls="treeview-icon icon-xls">annual_report.xlsx</li>
                                                                    <li data-icon-cls="treeview-icon icon-pdf">eula.pdf</li>
                                                                    <li data-icon-cls="treeview-icon icon-pdf">statement_2015.pdf</li>
                                                                </ul>
                                                            </li>
                                                            <li data-icon-cls="treeview-icon icon-txt">about.html</li>
                                                            <li data-icon-cls="treeview-icon icon-txt">contacts.html</li>
                                                            <li data-icon-cls="treeview-icon icon-txt">index.html</li>
                                                        </ul>
                                                    </li>
                                                </ul>
                                            </div>
                                            <script type="text/javascript">
                                                jQuery(function ($) {
                                                    $("#treeview").shieldTreeView();
                                                });
                                            </script>
<!--                                            <fieldset class="scheduler-border">
                                                <legend class="scheduler-border"><script>document.write(menu_group_Role);</script></legend>
                                                <div class="form-group" style="padding-right: 0px;">
                                                    <select class="form-control123" name="UserRole" id="idUserRole" onchange="exportRecordAbc(this.value, '<%=anticsrf%>');">
                                                        <
                                                            ROLE[][] rsGroup = new ROLE[1][];
                                                            try {
                                                                db.S_BO_ROLE_COMBOBOX(sessLanguageGlobal, rsGroup);
                                                                if (rsGroup[0].length > 0) {
                                                                    for (int i = 0; i < rsGroup[0].length; i++) {
                                                                        sRoleFrist = String.valueOf(rsGroup[0][0].ID);
                                                        %>
                                                        <option value="<=String.valueOf(rsGroup[0][i].ID)%>"><=rsGroup[0][i].REMARK %></option>
                                                        <
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
                                                        <
                                                                }
                                                            }
                                                        %>
                                                    </select>
                                                </div>
                                                <input id="idUrlFile" name="idUrlFile" type="text" style="display: none;"/>
                                            </fieldset>-->
<!--                                            <fieldset class="scheduler-border">
                                                <legend class="scheduler-border"><script>document.write(menu_group_assign);</script></legend>
                                                <div class="form-group">
                                                    <label class="control-label123"><script>document.write(menu_fm_assign + " (" + menu_fm_parent_name + ")");</script></label>
                                                    <br/>
                                                    <div style="width: 100%;padding-top: 5px;clear: both;">
                                                        <div style="float: left;width: 91%;">
                                                            <select name="UserRoleNot" class="form-control123" id="idUserRoleNot">
                                                            </select>
                                                        </div>
                                                        <div style="float: right;text-align: right;padding-left: 6px;">
                                                            <input class="btn btn-info" style="width: 80px;" id="btnAssign" type="button" onclick="exportRecord('<%= anticsrf%>');"/>
                                                        </div>
                                                        <script>
                                                            document.getElementById("btnAssign").value = menu_fm_button_assign;
                                                        </script>
                                                    </div>
                                                </div>
                                            </fieldset>-->
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                            <input id="idHiddenLoad" name="nameHiddenLoad" value="" type="hidden"/>
                                        </form>
                                    </div>
                                </div>
                                <script>
                                function exportRecordAbc(vCertType, idCSRF) {
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gif").show();
                                    NotAssignTemplate(vCertType, idCSRF);
                                    YesAssignTemplate(vCertType, idCSRF);
                                    $(".loading-gif").hide();
                                    $('#over').remove();
                                }
                                function NotAssignTemplate(doID, idCSRF)
                                {
                                    $.ajax({
                                        type: "post",
                                        url: "../JSONCommon",
                                        data: {
                                            idParam: 'listnotassignmenulink',
                                            TypeCertID: doID,
                                            CsrfToken: idCSRF
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            var cbxTemplateNotAssign = document.getElementById("idUserRoleNot");
                                            removeOptions(cbxTemplateNotAssign);
                                            if (html.length > 0)
                                            {
                                                var obj = JSON.parse(html);
                                                if (obj[0].Code === "0")
                                                {
                                                    for (var i = 0; i < obj.length; i++) {
                                                        cbxTemplateNotAssign.options[cbxTemplateNotAssign.options.length] = new Option(obj[i].REMARK + ' (' + obj[i].PARENTLINK_REMARK + ')', obj[i].ID);
                                                    }
                                                }
                                                else if (obj[0].Code === JS_EX_CSRF)
                                                {
                                                    funCsrfAlert();
                                                } else if (obj[0].Code === JS_EX_LOGIN)
                                                {
                                                    RedirectPageLoginNoSess(global_alert_login);
                                                }
                                                else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                                {
                                                    RedirectPageLoginNoSess(global_alert_another_login);
                                                }
                                                else {
                                                    //funErrorAlert(global_errorsql);
                                                }
                                            }
                                            else
                                            {
                                                cbxTemplateNotAssign.options[cbxTemplateNotAssign.options.length] = new Option("--", "0");
                                            }
                                        }
                                    });
                                    return false;
                                }
                                function YesAssignTemplate(doID, idCSRF)
                                {
                                    $.ajax({
                                        type: "post",
                                        url: "../JSONCommon",
                                        data: {
                                            idParam: 'listyesassignmenulink',
                                            TypeCertID: doID,
                                            CsrfToken: idCSRF
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            if (html.length > 0)
                                            {
                                                var obj = JSON.parse(html);
                                                if (obj[0].Code === "0")
                                                {
                                                    $("#idPanelAssignResult").css("display","");
                                                    $("#idPanelAssignNoResult").css("display","none");
                                                    var content = "";
                                                    $("#idTemplateAssign").empty();
                                                    var j=0;
                                                    for (var i = 0; i < obj.length; i++) {
                                                        content += '<tr>' +
                                                            '<td>' + obj[i].Index + '</td>' +
                                                            '<td>' + obj[i].REMARK + '</td>' +
                                                            '<td>' + obj[i].PARENTLINK_REMARK + '</td>' +
                                                            '<td>' + obj[i].LINKURL + '</td>' +
                                                            '<td>' + obj[i].CREATED_DT + '</td>' +
                                                            '<td>' +
                                                            '<a style="cursor: pointer;" onclick="deleteRecord(\'' + obj[i].MENULINK_ROLE_ID + '\', \'' + <%= anticsrf%> + '\');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> '+ global_button_grid_delete + ' </a>' +
                                                            '</td>' +
                                                            '</tr>';
                                                        j++;
                                                    }
                                                    $("#idTemplateAssign").append(content);
                                                    $("#idSumRecord").text(j);
                                                }
                                                else if (obj[0].Code === "1")
                                                {
                                                    $("#idPanelAssignResult").css("display","none");
                                                    $("#idPanelAssignNoResult").css("display","");
                                                    $("#idTemplateAssign").empty();
                                                }
                                                else if (obj[0].Code === JS_EX_CSRF)
                                                {
                                                    funCsrfAlert();
                                                } else if (obj[0].Code === JS_EX_LOGIN)
                                                {
                                                    RedirectPageLoginNoSess(global_alert_login);
                                                }
                                                else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                                {
                                                    RedirectPageLoginNoSess(global_alert_another_login);
                                                }
                                                else {
                                                    $("#idPanelAssignResult").css("display","none");
                                                    $("#idPanelAssignNoResult").css("display","");
                                                    $("#idTemplateAssign").empty();
                                                    funErrorAlert(global_errorsql);
                                                }
                                            }
                                            else
                                            {
                                                $("#idPanelAssignResult").css("display","none");
                                                $("#idPanelAssignNoResult").css("display","");
                                                $("#idTemplateAssign").empty();
                                            }
                                        }
                                    });
                                    return false;
                                }
                                </script>
                                            
<!--                                <div class="x_panel" id="idPanelAssignResult">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(menu_table_assigned);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <script>document.write(global_label_grid_sum);</script> <label id="idSumRecord"></label>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <style type="text/css">
                                            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                            .table > thead > tr > th{border-bottom: none;}
                                            .btn{margin-bottom: 0px;}
                                        </style>
                                        <div class="table-responsive">
                                            <table class="table table-bordered table-striped projects">
                                                <thead>
                                                <th><script>document.write(global_fm_STT);</script></th>
                                                <th><script>document.write(menu_fm_assign);</script></th>
                                                <th><script>document.write(menu_fm_parent_name);</script></th>
                                                <th><script>document.write(menu_fm_url);</script></th>
                                                <th><script>document.write(global_fm_date_create);</script></th>
                                                <th><script>document.write(global_fm_action);</script></th>
                                                </thead>
                                                <tbody id="idTemplateAssign"></tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="x_panel" id="idPanelAssignNoResult">
                                    <div class="x_content" style="text-align: center;">
                                        <span style="color: red; font-size: 15px;"><script>document.write(global_succ_NoResult);</script></span>
                                        <div class="clearfix"></div>
                                    </div>
                                </div>-->
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
                            </div>
                        </div>
                        <script>
                            $(document).ready(function () {
                                exportRecordAbc('<%= sRoleFrist%>', '<%=anticsrf%>');
                            });
                        </script>
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