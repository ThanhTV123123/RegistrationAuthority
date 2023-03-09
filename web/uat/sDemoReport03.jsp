<%-- 
    Document   : sDemoReport03
    Created on : Aug 26, 2018, 4:33:04 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
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
        <!--<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>-->
        <style>
            /*            table{
                            background-color:yellow;
                            width:400px;
                        }
                        table td {
                            border: 1px solid #eee;
                        }
                        .level1 td:first-child {
                            padding-left: 15px;
                        }
                        .level2 td:first-child {
                            padding-left: 35px;
                        }
                        .level1{
                            background-color:pink;
                        }
                        .level2{
                            background-color:orange;
            
                        }*/
            .level0{
                background: red;
            }
            .collapse_abc .toggle_abc {
                background: url("http://mleibman.github.com/SlickGrid/images/collapse.gif");
            }
            .expand_abc .toggle_abc {
                background: url("http://mleibman.github.com/SlickGrid/images/expand.gif");

            }
            .toggle_abc {
                height: 9px;
                width: 9px;
                display: inline-block;   
            }
            /*            thead{
                            background-color:red;
                            text-align:center;
                        }*/
        </style>
        <script>
            $(function () {
                $('#mytable').on('click', '.toggle_abc', function () {
                    //Gets all <tr>'s  of greater depth
                    //below element in the table
                    var findChildren = function (tr) {
                        var depth = tr.data('depth');
                        return tr.nextUntil($('tr').filter(function () {
                            return $(this).data('depth') <= depth;
                        }));
                    };
                    var el = $(this);
                    var tr = el.closest('tr'); //Get <tr> parent of toggle button
                    var children = findChildren(tr);
                    //Remove already collapsed nodes from children so that we don't
                    //make them visible. 
                    //(Confused? Remove this code and close Item 2, close Item 1 
                    //then open Item 1 again, then you will understand)
                    var subnodes = children.filter('.expand_abc');
                    subnodes.each(function () {
                        var subnode = $(this);
                        var subnodeChildren = findChildren(subnode);
                        children = children.not(subnodeChildren);
                    });
                    //Change icon and hide/show children
                    if (tr.hasClass('expand_abc')) {
                        tr.removeClass('expand_abc').addClass('collapse_abc');
                        children.show();
                    } else {
                        tr.removeClass('collapse_abc').addClass('expand_abc');
                        children.hide();
                    }
                    return children;
                });
            });
            $(document).ready(function () {
                $('.loading-gifHardware').hide();
                $('#myModalOTPHardware').modal({
                    backdrop: 'static',
                    keyboard: true,
                    show: false
                });
            });
        </script>
        <title>thanh</title>
    </head>
    <body class="nav-md">
        <div class="container body">
            <div class="main_container">
                <div class="col-md-3 left_col">
                    <div class="left_col scroll-view">
                        <br />
                        <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
                        </div>
                    </div>
                </div>
                <div class="top_nav">
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title"></div>
                                    <div class="x_content">
                                        <style type="text/css">
                                            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                            .btn{margin-bottom: 0px;}
                                            .panel_toolbox { min-width: 0;}
                                        </style>
                                        <div class="table-responsive">
                                            <%                                                REPORT_QUICK_BRANCH[][] rsReportBranch = new REPORT_QUICK_BRANCH[1][];
                                                int[] pIa = new int[1];
                                                int[] pIb = new int[1];
                                                String anticsrf = "" + Math.random();
                                                request.getSession().setAttribute("anticsrf", anticsrf);
                                                db.S_BO_REPORT_TOTAL_BRANCH("01/01/2018", "08/09/2018", "", "1", rsReportBranch);
                                                if (rsReportBranch[0].length > 0) {

                                            %>
                                            <table id="mytable" class="table table-striped projects">
                                                <thead>
                                                <th><script>document.write(global_fm_Branch)</script></th>
                                                <th><script>document.write(reportquick_fm_innit)</script></th>
                                                <th><script>document.write(reportquick_fm_activation)</script></th>
                                                <th><script>document.write(reportquick_fm_revoke)</script></th>
                                                <th><script>document.write(reportquick_fm_total)</script></th>
                                                </thead>
                                                <tbody>
                                                    <%                                                        for (int i = 0; i < rsReportBranch[0].length; i++) {
                                                            int sSUMBranch = rsReportBranch[0][i].TOTAL_INITIALIZED
                                                                + rsReportBranch[0][i].TOTAL_OPERATED + rsReportBranch[0][i].TOTAL_REVOKED;
                                                    %>
                                                    <tr data-depth="0" class="collapse_abc level0" style="background: #C1DAD7;">
                                                        <td>
                                                            <span class="toggle_abc collapse_abc"></span>
                                                            <%= EscapeUtils.CheckTextNull(rsReportBranch[0][i].BRANCH_NAME)%>
                                                        </td>
                                                        <td><%= rsReportBranch[0][i].TOTAL_INITIALIZED%></td>
                                                        <td><%= rsReportBranch[0][i].TOTAL_OPERATED%></td>
                                                        <td><%= rsReportBranch[0][i].TOTAL_REVOKED%></td>
                                                        <td><%= sSUMBranch%></td>
                                                    </tr>
                                                    <%
                                                        REPORT_QUICK_BRANCH[][] rsReportUser = new REPORT_QUICK_BRANCH[1][];
                                                        db.S_BO_REPORT_TOTAL_BRANCH_USER(String.valueOf(pIa[0]), String.valueOf(pIb[0]),
                                                                String.valueOf(rsReportBranch[0][i].BRANCH_ID), rsReportUser);
                                                        if (rsReportUser[0].length > 0) {
                                                            for (int j = 0; j < rsReportUser[0].length; j++) {
                                                                String sIDInit = String.valueOf(pIa[0]) + "###" + String.valueOf(pIb[0])
                                                                        + "###" + String.valueOf(rsReportBranch[0][i].BRANCH_ID)
                                                                        + "###" + String.valueOf(rsReportUser[0][j].CREATED_BY)
                                                                        + "###" + String.valueOf(Definitions.CONFIG_CERTIFICATION_STATE_NEW)
                                                                        + "###" + anticsrf + "###" + EscapeUtils.CheckTextNull(rsReportBranch[0][i].BRANCH_NAME);
                                                                String sIDActivation = String.valueOf(pIa[0]) + "###" + String.valueOf(pIb[0])
                                                                        + "###" + String.valueOf(rsReportBranch[0][i].BRANCH_ID)
                                                                        + "###" + String.valueOf(rsReportUser[0][j].CREATED_BY)
                                                                        + "###" + String.valueOf(Definitions.CONFIG_CERTIFICATION_STATE_OPERATED)
                                                                        + "###" + anticsrf + "###" + EscapeUtils.CheckTextNull(rsReportBranch[0][i].BRANCH_NAME);
                                                                String sIDRevoke = String.valueOf(pIa[0]) + "###" + String.valueOf(pIb[0])
                                                                        + "###" + String.valueOf(rsReportBranch[0][i].BRANCH_ID)
                                                                        + "###" + String.valueOf(rsReportUser[0][j].CREATED_BY)
                                                                        + "###" + String.valueOf(Definitions.CONFIG_CERTIFICATION_STATE_REVOKED)
                                                                        + "###" + anticsrf + "###" + EscapeUtils.CheckTextNull(rsReportBranch[0][i].BRANCH_NAME);
                                                                int sSUMUser = rsReportUser[0][j].TOTAL_INITIALIZED + rsReportUser[0][j].TOTAL_OPERATED + rsReportUser[0][j].TOTAL_REVOKED;
                                                    %>
                                                    <tr data-depth="1" class="level1">
                                                        <td>
                                                            <span class="toggle_abc"></span>
                                                            <%= EscapeUtils.CheckTextNull(rsReportUser[0][j].USERNAME)%>
                                                        </td>
                                                        <td>
                                                            <%
                                                                if (rsReportUser[0][j].TOTAL_INITIALIZED != 0) {
                                                            %>
                                                            <a style="color: blue;text-decoration: underline;" href="#myModalOTPHardware" data-toggle="modal" data-book-id="<%= sIDInit%>"><%= rsReportUser[0][j].TOTAL_INITIALIZED%></a>
                                                            <%
                                                            } else {
                                                            %>
                                                            <%= rsReportUser[0][j].TOTAL_INITIALIZED%>
                                                            <%
                                                                }
                                                            %>
                                                        </td>
                                                        <td>
                                                            <%
                                                                if (rsReportUser[0][j].TOTAL_OPERATED != 0) {
                                                            %>
                                                            <a style="color: blue;text-decoration: underline;" href="#myModalOTPHardware" data-toggle="modal" data-book-id="<%= sIDActivation%>"><%= rsReportUser[0][j].TOTAL_OPERATED%></a>
                                                            <%
                                                            } else {
                                                            %>
                                                            <%= rsReportUser[0][j].TOTAL_OPERATED%>
                                                            <%
                                                                }
                                                            %>
                                                        </td>
                                                        <td>
                                                            <%
                                                                if (rsReportUser[0][j].TOTAL_REVOKED != 0) {
                                                            %>
                                                            <a style="color: blue;text-decoration: underline;" href="#myModalOTPHardware" data-toggle="modal" data-book-id="<%= sIDRevoke%>"><%= rsReportUser[0][j].TOTAL_REVOKED%></a>
                                                            <%
                                                            } else {
                                                            %>
                                                            <%= rsReportUser[0][j].TOTAL_REVOKED%>
                                                            <%
                                                                }
                                                            %>
                                                        </td>
                                                        <td><%= sSUMUser%></td>
                                                    </tr>
                                                    <%
                                                                }
                                                            }
                                                        }
                                                    %>
                                                </tbody>
                                            </table>
                                            <%
                                                }
                                            %>
                                            <script>
                                                function LoadCert(Tag_ID)
                                                {
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'listcertreportquick',
                                                            Tag_ID: Tag_ID
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            if (html.length > 0)
                                                            {
                                                                var obj = JSON.parse(html);
                                                                if (obj[0].Code === "0")
                                                                {
                                                                    var content = "";
                                                                    $("#idTemplateAssign").empty();
                                                                    var j = 0;
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        var vMST_MNS = obj[i].ENTERPRISE_ID;
                                                                        var vCMND_HC = obj[i].PERSONAL_ID;
                                                                        content += '<tr>' +
                                                                                '<td>' + obj[i].Index + '</td>' +
                                                                                '<td>' + obj[i].COMPANY_NAME + '</td>' +
                                                                                '<td>' + vMST_MNS + '</td>' +
                                                                                '<td>' + obj[i].PERSONAL_NAME + '</td>' +
                                                                                '<td>' + vCMND_HC+ '</td>' +
                                                                                '<td>' + obj[i].CERTIFICATION_PROFILE_DESC + '</td>' +
                                                                                '<td>' + obj[i].CERTIFICATION_PURPOSE_DESC + '</td>' +
                                                                                '<td>' + obj[i].CERTIFICATION_STATE_DESC + '</td>' +
                                                                                '<td>' + obj[i].BRANCH_NAME + '</td>' +
                                                                                '</tr>';
                                                                        j++;
                                                                    }
                                                                    $("#idTemplateAssign").append(content);
                                                                }
                                                                else if (obj[0].Code === "1")
                                                                {
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
                                                                    $("#idTemplateAssign").empty();
                                                                    funErrorAlert(global_errorsql);
                                                                }
                                                            }
                                                            else
                                                            {
                                                                $("#idTemplateAssign").empty();
                                                            }
                                                        }
                                                    });
                                                }
                                                $(document).ready(function () {
                                                    $('#myModalOTPHardware').on('show.bs.modal', function (e) {
                                                        var Tag_ID = $(e.relatedTarget).data('book-id');
                                                        LoadCert(Tag_ID);
                                                    });
                                                });
                                                function CloseDialog()
                                                {
                                                    $('#myModalOTPHardware').modal('hide');
                                                    $(".loading-gifHardware").hide();
                                                    $(".loading-gif").hide();
                                                    $('#over').remove();
                                                }
                                            </script>
                                            <style>
                                                @media (min-width: 768px) {
                                                    .modal-dialog {
                                                        width: 850px;
                                                        margin: 30px auto;
                                                    }
                                                }
                                                .table-fixed{
                                                    width: 100%;
                                                    /*background-color: #f3f3f3;*/
                                                    tbody{
                                                      height:200px;
                                                      overflow-y:auto;
                                                      width: 100%;
                                                      }
                                                    thead,tbody,tr,td,th{
                                                      display:block;
                                                    }
                                                    tbody{
                                                      td{
                                                        float:left;
                                                      }
                                                    }
                                                    thead {
                                                      tr{
                                                        th{
                                                        float:left;
                                                         background-color: #f39c12;
                                                         border-color:#e67e22;
                                                        }
                                                      }
                                                    }
                                                  }
                                            </style>
                                            <div id="myModalOTPHardware" class="modal fade" role="dialog">
                                                <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                                                     left: 0; height: 100%;" class="loading-gifHardware">
                                                    <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
                                                </div>
                                                <div class="modal-dialog modal-800" id="myDialogOTPHardware">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <div style="width: 70%; float: left;">
                                                                <h3 class="modal-title" style="font-size: 18px;"><script>document.write(global_fm_cert_list)</script></h3>
                                                            </div>
                                                            <div style="width: 29%; float: right;text-align: right;">
                                                                <button type="button" onclick="CloseDialog();" class="btn btn-info"><script>document.write(global_fm_button_close)</script></button>
                                                            </div>
                                                        </div>
                                                        <div class="modal-body">
                                                            <form role="formAddOTPHardware" id="formOTPHardware">
                                                                <table class="table table-striped projects table-fixed">
                                                                    <thead>
                                                                    <th><script>document.write(global_fm_STT);</script></th>
                                                                    <th style="width: 120px;"><script>document.write(global_fm_grid_company);</script></th>
                                                                    <th style="width: 100px;"><script>document.write(global_fm_MST + '/' + global_fm_MNS);</script></th>
                                                                    <th style="width: 120px;"><script>document.write(global_fm_grid_personal);</script></th>
                                                                    <th style="width: 100px;"><script>document.write(global_fm_CMND + '/' + global_fm_HC);</script></th>
                                                                    <th><script>document.write(global_fm_duration_cts);</script></th>
                                                                    <th><script>document.write(global_fm_certtype);</script></th>
                                                                    <th><script>document.write(global_fm_Status);</script></th>
                                                                    <th><script>document.write(token_fm_agent);</script></th>
                                                                    </thead>
                                                                    <tbody id="idTemplateAssign"></tbody>
                                                                </table>
                                                            </form>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <table id="mytable11" class="table table-striped projects" style="display: none;">
                                                <thead>
                                                <th>Name</th>
                                                <th>Des 1</th>
                                                <th>Des 2</th>
                                                </thead>
                                                <tbody>
                                                    <tr data-depth="0" class="collapse_abc level0">
                                                        <td><span class="toggle_abc collapse_abc"></span> Table A1</td>
                                                        <td>1</td>
                                                        <td>2</td>
                                                    </tr>
                                                    <tr data-depth="1" class="level1">
                                                        <td><span class="toggle_abc"></span>Table B1 </td>
                                                        <td>121</td>
                                                        <td>111</td>
                                                    </tr>
                                                    <tr data-depth="1" class="level1">
                                                        <td><span class="toggle_abc"></span>Table B2</td>
                                                        <td>123</td>
                                                        <td>123</td>
                                                    </tr>
                                                    <tr data-depth="0" class="collapse_abc level0">
                                                        <td><span class="toggle_abc collapse_abc"></span> Table A2</td>
                                                        <td>123</td>
                                                        <td>123</td>
                                                    </tr>
                                                    <tr data-depth="1" class="level1">
                                                        <td><span class="toggle_abc"></span>Table B1 </td>
                                                        <td>123</td>
                                                        <td>123</td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>                      
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../style/custom.min.js"></script>
    </body>
</html>
<!--<script>
            $(function () {
                $('#mytable').on('click', '.toggle', function () {
                    //Gets all <tr>'s  of greater depth
                    //below element in the table
                    var findChildren = function (tr) {
                        var depth = tr.data('depth');
                        return tr.nextUntil($('tr').filter(function () {
                            return $(this).data('depth') <= depth;
                        }));
                    };
                    var el = $(this);
                    var tr = el.closest('tr'); //Get <tr> parent of toggle button
                    var children = findChildren(tr);
                    //Remove already collapsed nodes from children so that we don't
                    //make them visible. 
                    //(Confused? Remove this code and close Item 2, close Item 1 
                    //then open Item 1 again, then you will understand)
                    var subnodes = children.filter('.expand');
                    subnodes.each(function () {
                        var subnode = $(this);
                        var subnodeChildren = findChildren(subnode);
                        children = children.not(subnodeChildren);
                    });
                    //Change icon and hide/show children
                    if (tr.hasClass('expand')) {
                        tr.removeClass('expand').addClass('collapse');
                        children.show();
                    } else {
                        tr.removeClass('collapse').addClass('expand');
                        children.hide();
                    }
                    return children;
                });
            });
        </script>-->
<!--<table id="mytable">
            <thead>Table </thead>
            <tbody>
                <tr data-depth="0" class="collapse level0">
                    <td><span class="toggle collapse"></span>Table A1</td>
                     <td>123</td>
                </tr>
                <tr data-depth="1" class="collapse level1" Data-toggle="collapse">
                    <td><span class="toggle"></span>Table B1 </td>
                    <td colspan="2">123</td>
                </tr>
                <tr data-depth="2" class="collapse level2">
                    <td>row1</td>
                    <td>Item 3</td>
                    <td>123</td>
                </tr>
                <tr data-depth="1" class="collapse level1">
                    <td><span class="toggle"></span>Table B2</td>
                    <td colspan="2">123</td>
                </tr>
                <tr data-depth="2" class="collapse level2">
                    <td>row1</td>
                    <td>Item 3</td>
                    <td>123</td>
                </tr>
                <tr data-depth="2" class="collapse level2">
                    <td>row2</td>
                    <td>Item 3</td>
                    <td>123</td>
                </tr>
                <tr data-depth="1" class="collapse level1">
                    <td><span class="toggle"></span>Table B3</td>
                    <td colspan="2">123</td>
                </tr>
                <tr data-depth="2" class="collapse level2">
                    <td>row1</td>
                    <td>Item 3</td>
                    <td>123</td>
                </tr>
                <tr data-depth="0" class="collapse collapsable level0">
                    <td><span class="toggle collapse"></span>Table A2</td>
                    <td>123</td>
                </tr>

                <tr data-depth="1" class="collapse level1">
                    <td><span class="toggle"></span>Table B1 </td>
                    <td colspan="2">123</td>
                </tr>
                <tr data-depth="2" class="collapse level2">
                    <td>row1</td>
                    <td>Item 3</td>
                    <td>123</td>
                </tr>
                <tr data-depth="2" class="collapse level2">
                    <td>row2</td>
                    <td>Item 3</td>
                    <td>123</td>
                </tr>
                <tr data-depth="1" class="collapse level1">
                    <td><span class="toggle"></span>Table B2 </td>
                    <td colspan="2">123</td>
                </tr>
                <tr data-depth="2" class="collapse level2">
                    <td>row1</td>
                    <td>Item 3</td>
                    <td>123</td>
                </tr>
                <tr data-depth="2" class="collapse level2">
                    <td>row2</td>
                    <td>Item 3</td>
                    <td>123</td>
                </tr>
            </tbody>
        </table>-->