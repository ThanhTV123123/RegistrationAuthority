<%-- 
    Document   : TemplateDN
    Created on : Jun 18, 2018, 3:56:25 PM
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
    String anticsrf = "" + Math.random();
    request.getSession().setAttribute("anticsrf", anticsrf);
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
        <script src="../js/vn.js"></script>
        <script src="../js/process_javajs.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script>
            changeFavicon("../");
            document.title = tempdn_title_list;
            $(document).ready(function () {
                $('.loading-gif').hide();
                $("#idPanelAssignResult").css("display","none");
                $("#idPanelAssignNoResult").css("display","none");
                $("#idTemplateAssign").empty();
            });
            function ApplyRecord(idCSRF)
            {
                var result = new Array();
                $("#idTemplateAssign td input[type=checkbox]").each(function () {
                    var id = $(this).attr("id");
                    result.push(id + "c" + ($(this).is(":checked") ? "1" : "0"));
                });
                if (result.length > 0) {
                    $('body').append('<div id="over"></div>');
                    $(".loading-gif").show();
                    var dataString = 'idParam=updatemultitemplate&TemplateIDList=' + result
                        + '&CsrfToken=' + idCSRF;
                    $.ajax({
                        type: "post",
                        url: "../TemplateCNCommon",
                        data: dataString,
                        cache: false,
                        success: function (html)
                        {
                            var myStrings = sSpace(html).split('#');
                            if (myStrings[0] === "0")
                            {
                                var vCertType = document.getElementById("idCertType").value;
                                NotAssignTemplate(vCertType, idCSRF);
                                YesAssignTemplate(vCertType, idCSRF);
                                funSuccNoLoad(tempdn_succ_edit);
                            }
                            else if (myStrings[0] === JS_EX_CSRF)
                            {
                                funCsrfAlert();
                            }
                            else if (myStrings[0] === "1") {
                                funErrorAlert(tempdn_error_edit);
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
                else {
                    funErrorAlert(tempdn_error_edit);
                }
            }
            function AssignRecord(idCSRF) {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../TemplateCNCommon",
                    data: {
                        idParam: 'addtemplate',
                        CertTypeID: document.getElementById('idCertType').value,
                        SubjectDNAttrID: document.getElementById('idTemplateNotAssign').value,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            var vCertType = document.getElementById("idCertType").value;
                            NotAssignTemplate(vCertType, idCSRF);
                            YesAssignTemplate(vCertType, idCSRF);
                            funSuccNoLoad(tempdn_succ_insert);
                        }
                        else if (myStrings[0] === JS_EX_CSRF)
                        {
                            funCsrfAlert();
                        }
                        else if (myStrings[0] === "1") {
                            funErrorAlert(tempdn_error_insert);
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
            function deleteRecord(id, idCSRF) {
                swal({
                    title: "",
                    text: tempdn_conform_delete,
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
                            url: "../TemplateCNCommon",
                            data: {
                                idParam: 'deletetemplate',
                                id: id,
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html)
                            {
                                var myStrings = sSpace(html).split('#');
                                if (myStrings[0] === "0")
                                {
                                    var vCertType = document.getElementById("idCertType").value;
                                    NotAssignTemplate(vCertType, idCSRF);
                                    YesAssignTemplate(vCertType, idCSRF);
                                    funSuccNoLoad(tempdn_succ_delete);
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
            function onChangeCertType(vCertType, idCSRF) {
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
                        idParam: 'listnotassigntemplate',
                        TypeCertID: doID,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var cbxTemplateNotAssign = document.getElementById("idTemplateNotAssign");
                        removeOptions(cbxTemplateNotAssign);
                        if (html.length > 0)
                        {
                            var obj = JSON.parse(html);
                            if (obj[0].Code === "0")
                            {
                                for (var i = 0; i < obj.length; i++) {
                                    cbxTemplateNotAssign.options[cbxTemplateNotAssign.options.length] = new Option(obj[i].REMARK + ' (' + obj[i].NAME + '=' + obj[i].PRE_FIX + ')', obj[i].CERTIFICATION_PROFILE_ATTR_TYPE_ID);
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
                                funErrorAlert(global_errorsql);
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
                        idParam: 'listyesassigntemplate',
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
                                for (var i = 0; i < obj.length; i++) {
                                    var vCheck = "";
                                    if(obj[i].IsRequired === '1')
                                    {
                                        vCheck = "checked";
                                    }
                                    content += '<tr>' +
                                        '<td>' + obj[i].Index + '</td>' +
                                        '<td>' + obj[i].REMARK + ' (' + obj[i].NAME + '=' + obj[i].PRE_FIX + ')' + '</td>' +
                                        '<td><input type="checkbox" id=' + obj[i].CERTIFICATION_PROFILE_ATTR_ID +' '+vCheck+' /></td>' +
                                        '<td>' + obj[i].CREATED_DT + '</td>' +
                                        '<td>' +
                                        '<a style="cursor: pointer;" onclick="deleteRecord(\'' + obj[i].CERTIFICATION_PROFILE_ATTR_ID + '\', \'' + <%= anticsrf%> + '\');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> '+ global_button_grid_delete + ' </a>' +
                                        '</td>' +
                                        '</tr>';
                                }
                                $("#idTemplateAssign").append(content);
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
    </head>
    <body class="nav-md">
        <%
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
                        document.getElementById("idNameURL").innerHTML = tempdn_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="myname" role="form" method="post" class="form-horizontal">
                                            <fieldset class="scheduler-border">
                                                <legend class="scheduler-border"><script>document.write(tempdn_group_Role);</script></legend>
                                                <div class="form-group" style="padding-right: 0px;">
                                                    <select class="form-control123" name="idCertType" id="idCertType" onchange="onChangeCertType(this.value, '<%=anticsrf%>');">
                                                        <%
                                                            String sCertTypeFrist = "";
                                                            CERTIFICATION_PROFILE[][] rsCertType = new CERTIFICATION_PROFILE[1][];
                                                            try {
                                                                db.S_BO_CERTIFICATION_PROFILE_COMBOBOX(sessLanguageGlobal, rsCertType);
                                                                if (rsCertType[0].length > 0) {
                                                                    for (int i = 0; i < rsCertType[0].length; i++) {
                                                                        sCertTypeFrist = String.valueOf(rsCertType[0][0].ID);
                                                        %>
                                                        <option value="<%=String.valueOf(rsCertType[0][i].ID)%>"><%=rsCertType[0][i].REMARK %></option>
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
                                            </fieldset>
                                            <fieldset class="scheduler-border">
                                                <legend class="scheduler-border"><script>document.write(tempdn_group_assign);</script></legend>
                                                <div class="form-group" style="padding: 0px 10px 0 0;margin: 0;">
                                                    <div style="width: 100%;padding-top: 5px;clear: both;">
                                                        <div style="float: left;width: 90%;">
                                                            <select name="idTemplateNotAssign" class="form-control123" id="idTemplateNotAssign"></select>
                                                        </div>
                                                        <div style="float: right;text-align: right;">
                                                            <input class="btn btn-info" style="width: 85px;" id="btnAssign" type="button" onclick="AssignRecord('<%= anticsrf%>');"/>
                                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                                        </div>
                                                        <script>
                                                            document.getElementById("btnAssign").value = menu_fm_button_assign;
                                                        </script>
                                                    </div>
                                                </div>
                                            </fieldset>
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                            <input id="idHiddenLoad" name="nameHiddenLoad" value="" type="hidden"/>
                                        </form>
                                    </div>
                                </div>
                                <div class="x_panel" id="idPanelAssignResult">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(tempdn_table_assigned);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <input class="btn btn-info" id="btnApplyTable" type="button" onclick="ApplyRecord('<%= anticsrf%>');"/>
                                                <script>
                                                    document.getElementById("btnApplyTable").value = global_fm_button_edit;
                                                </script>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <style type="text/css">
                                            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                            .btn{margin-bottom: 0px;}
                                        </style>
                                        <div class="table-responsive">
                                            <table class="table table-striped projects">
                                                <thead>
                                                <th><script>document.write(global_fm_STT);</script></th>
                                                <th><script>document.write(tempdn_fm_subjectdn);</script></th>
                                                <th><script>document.write(tempdn_fm_required);</script></th>
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
                                </div>
                            </div>
                        </div>
                    </div>
                    <script>
                        $(document).ready(function () {
                            onChangeCertType('<%= sCertTypeFrist%>', '<%=anticsrf%>');
                        });
                    </script>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
        </div>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../style/custom.min.js"></script>
    </body>
</html>