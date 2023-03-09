<%-- 
    Document   : IncludeCertDecline
    Created on : Jun 18, 2019, 9:17:48 AM
    Author     : THANH-PC
--%>

<%@page import="vn.ra.process.CommonFunction"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"> 
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="style/bootstrap.min.css" rel="stylesheet">
        <link href="style/font-awesome.css" rel="stylesheet">
        <link href="style/nprogress.css" rel="stylesheet">
        <link href="style/custom.min.css" rel="stylesheet">
        <script src="js/Language.js"></script>
        <script src="js/process_javajs.js"></script>
        <link href="style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.js"></script>
        <link rel="stylesheet" href="js/sweetalert.css"/>
        <script src="js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="Css/GlobalAlert.js"></script>
        <script src="js/jquery.PrintArea.js"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            document.title = certlist_title_print_hadover;
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
                int checkExFinnaly = 0;
                try {
                    String certId = request.getParameter("certId");
                    String keyID = request.getParameter("keyID");
            %>
            <div class="x_title">
                <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEditsDecline"></span></h2>
                <script>$("#idLblTitleEditsDecline").text(funrole_fm_deleterequest);</script>
                <ul class="nav navbar-right panel_toolbox">
                    <li>
                        <input id="btnPrintCertHandover" class="btn btn-info" type="button"
                            onclick="FormDecline('<%=certId%>', '<%= keyID%>');" />
                    </li>
                    <script>
                        document.getElementById("btnPrintCertHandover").value = global_fm_button_decline;
                        function FormDecline(vCertID, vKey) {
                            var vDNResult = "";
                            $.ajax({
                                type: "post",
                                url: "SomeCommon",
                                data: {
                                    idParam: 'confirmCustomerHSM',
                                    isType: '0',
                                    vCertID: vCertID,
                                    vDeclineReason: $("#DeclineReason").val(),
                                    vKey: vKey
                                },
                                cache: false,
                                async: false,
                                success: function (html)
                                {
                                    var myStrings = sSpace(html).split('#');
                                    if (myStrings[0] === "0") {
                                        swal({
                                            title: "", text: 'Hủy xác nhận cấp chứng thư số thành công.', imageUrl: "Images/success.png", imageSize: "45x45",
                                            allowOutsideClick: false, allowEscapeKey: false},
                                        function () {
                                            location.reload();
                                        });
                                    } else if(myStrings[0] ==="TYPE_NOT") {
                                        funErrorLoginAlert("Loại yêu cầu không hợp lệ");
                                    } else if(myStrings[0] ==="DATA_FOUND") {
                                        funErrorLoginAlert("Không tìm thấy thông tin chứng chỉ trên hệ thống");
                                    } else if(myStrings[0] ==="EXPIRE") {
                                        funErrorLoginAlert("Thời gian xác nhận đã hết hạn, vui lòng liên hệ nhà cung cấp dịch vụ");
                                    } else if(myStrings[0] ==="CERT_FAIL") {
                                        funErrorLoginAlert("Mã chứng thư không hợp lệ");
                                    } else {
                                        funErrorLoginAlert(global_errorsql);
                                    }
                                }
                            });
                            return vDNResult;
                        }
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
        <script src="style/bootstrap.min.js"></script>
        <script src="js/moment.min.js"></script>
        <script src="js/daterangepicker.js"></script>
    </body>
</html>