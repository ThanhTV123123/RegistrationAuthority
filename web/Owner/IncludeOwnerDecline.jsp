<%-- 
    Document   : IncludeOwnerDecline
    Created on : Nov 4, 2019, 11:01:20 AM
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<!DOCTYPE html>
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
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <script src="../js/jquery.PrintArea.js"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
//            document.title = certlist_title_print_hadover;
            function FormDecline(idATTR, idCSRF) {
                swal({
                    title: "",
                    text: request_conform_delete,
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
                            url: "../OwnerCommon",
                            data: {
                                idParam: 'declineowner',
                                sID: idATTR,
                                sDeclineReason: $("#DeclineReason").val(),
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var arr = sSpace(html).split('#');
                                if (arr[0] === "0")
                                {
//                                    if (arr[2] === "1")
//                                    {
//                                        pushNotificationDecline(idBranch, idUser);
//                                    }
                                    funSuccLocalAlert(request_succ_delete);
                                }
                                else if (arr[0] === JS_EX_CSRF) {
                                    funCsrfAlert();
                                }
                                else if (arr[0] === JS_EX_LOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_login);
                                }
                                else if (arr[0] === JS_EX_ANOTHERLOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_another_login);
                                }
                                else if (arr[0] === JS_EX_WRONG_AGENCY) {
                                    RedirectPageLoginNoSess(global_error_wrong_agency);
                                }
                                else if (arr[0] === JS_EX_STATUS)
                                {
                                    funErrorAlert(global_error_appove_status);
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
            function pushNotificationDecline(idBRANCH, user) {
                var xmlhttp = new XMLHttpRequest();
                xmlhttp.open("POST", "../PushNotiRequestDecline?t="+new Date(), false);
                xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                xmlhttp.send("name="+idBRANCH+"&user="+user);
            }
        </script>
        <style>
            @media (min-width: 768px){.modal-dialog{width: 900px;}}
            .modal-header{
                padding: 10px 10px 0px 10px;border-bottom:0px;
            }
        </style>
    </head>
    <body>
        <%         
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
        %>
        <div class="x_panel">
            <%
                try {
                    String attrId = request.getParameter("attrId");
//                    String branchId = request.getParameter("branchId");
//                    String userId = request.getParameter("userId");
            %>
            <div class="x_title">
                <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEditsDecline"></span></h2>
                <script>$("#idLblTitleEditsDecline").text(funrole_fm_deleterequest);</script>
                <ul class="nav navbar-right panel_toolbox">
                    <li>
                        <input id="btnPrintCertHandover" class="btn btn-info" type="button"
                            onclick="FormDecline('<%=attrId%>', '<%= anticsrf%>');" />
                    </li>
                    <script>
                        document.getElementById("btnPrintCertHandover").value = global_fm_button_decline;
                    </script>
                </ul>
                <div class="clearfix"></div>
            </div>
            <div class="x_content">
                <form name="mynameHandover" method="post" class="form-horizontal">
                    <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                    <div class="form-group">
                        <label id="idLblTitleReason" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="DeclineReason" id="DeclineReason" class="form-control123">
                        </div>
                    </div>
                    <script>$("#idLblTitleReason").text(global_fm_decline_desc);</script>
                </form>
            </div>
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
        <!--<script src="../style/jquery.min.js"></script>-->
        <script src="../style/bootstrap.min.js"></script>
        <!--<script src="../style/custom.min.js"></script>-->
        <script src="../js/moment.min.js"></script>
        <script src="../js/daterangepicker.js"></script>
    </body>
</html>