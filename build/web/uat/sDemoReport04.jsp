<%-- 
    Document   : sDemoReport04
    Created on : Aug 28, 2018, 4:53:38 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <!--<script type="text/javascript" src="../js/jquery.js"></script>-->
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <script type='text/javascript' src='../Css/jscolor.js'></script>
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            document.title = token_title_edit;
            $(document).ready(function () {
                alert("1");
//                $('.loading-gif').hide();
//                $('.loading-gif').hide();
//                $('.loading-gifHardware').hide();
//                $("#strIS_PUSH_NOTICE_SET_NO").prop("disabled", true);
//                $("#strIS_MENU_LINK_SET_NO").prop("disabled", true);
//                $("#strIS_PUSH_NOTICEEdit").prop('checked', false);
//                $("#strIS_PUSH_NOTICE_SET_NO").prop('checked', false);
//                $("#strIS_MENU_LINKEdit").prop('checked', false);
//                $("#strIS_MENU_LINK_SET_NO").prop('checked', false);
//                $("#strIS_UNLOCKEdit").prop('checked', false);
//                $("#strIS_LOCKEdit").prop('checked', false);
//                $("#strACTIVE_FLAGEdit").prop('checked', false);
//                $("#strCOLOR_TEXT_EditForm").prop("disabled", true);
//                $("#strCOLOR_BKGR_EditForm").prop("disabled", true);
            });
            //$(document).ready(function () {
//                $('#myModalOTPHardware').modal({
//                    backdrop: 'static',
//                    keyboard: true,
//                    show: false
//                });
            //});
        </script>
    </head>
    <body class="nav-md">
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        
        <div class="x_content">
            <fieldset class="scheduler-border">
                <legend class="scheduler-border" id="idLblTitleGroupCertHis"></legend>
                <script>$("#idLblTitleGroupCertHis").text(token_group_cert_history);</script>
                <div class="table-responsive">
                <table class="table table-bordered table-striped projects" id="mtTokenHisCert">
                    <thead>
                    <th id="idLblTitleTableHisSST"></th>
                    <th id="idLblTitleTableHisMST"></th>
                    <th id="idLblTitleTableHisCompany"></th>
                    <th id="idLblTitleTableHisCMND"></th>
                    <th id="idLblTitleTableHisPersonal"></th>
                    <th id="idLblTitleTableHisService"></th>
                    <th id="idLblTitleTableHisMinDate"></th>
                    <th id="idLblTitleTableHisMaxDate"></th>
                    <th id="idLblTitleTableHisAction"></th>
                    <script>
                        $("#idLblTitleTableHisSST").text(global_fm_STT);
                        $("#idLblTitleTableHisMST").text(global_fm_MST + '/ ' + global_fm_MNS);
                        $("#idLblTitleTableHisCompany").text(global_fm_grid_company);
                        $("#idLblTitleTableHisCMND").text(global_fm_CMND + '/ ' + global_fm_CitizenId + '/ ' + global_fm_HC);
                        $("#idLblTitleTableHisPersonal").text(global_fm_grid_personal);
                        $("#idLblTitleTableHisService").text(certprofile_fm_service_type);
                        $("#idLblTitleTableHisMinDate").text(global_fm_date_create);
                        $("#idLblTitleTableHisMaxDate").text(global_fm_date_cancel);
                        $("#idLblTitleTableHisAction").text(global_fm_action);
                    </script>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <a id="idLblTitleTableHisDetail" style="cursor: pointer;" class="btn btn-info btn-xs" href="#myModalOTPHardware" data-toggle="modal" data-book-id="123"></a>
                            </td>
                            <script>
                                $("#idLblTitleTableHisDetail").append('<i class="fa fa-pencil"></i> ' + global_fm_detail);
                            </script>
                        </tr>
                    </tbody>
                </table>
                </div>
                <script>
                    function LoadCert(Tag_ID)
                    {
                        $("#DetailCompany").val("123");
                        $("#DetailTaxcode").val("131321");
                        $("#DetailPersonal").val("âsasasa");
                        $("#DetailCMND").val('132432');
                        $("#DetailPhone").val('trycbv');
                        $("#DetailEmail").val('trycbv12');
                        $("#DetailPupore").val('trycbvdd');
                        $("#DetailStatus").val('trdá1ycbv');
                        $("#DetailEffective").val('tr2eycbv');
                        $("#DetailExpiration").val('trycb43v');
                        $("#DetailCertDN").val('trycbv1');
                    }
                    $(document).ready(function () {
                        $('#myModalOTPHardware').on('show.bs.modal', function (e) {
                        $(".loading-gifHardware").hide();
                        $(".loading-gif").hide();
                        $('#over').remove();
                            var Tag_ID = $(e.relatedTarget).data('book-id');
                            alert(Tag_ID);
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
            </fieldset>
            <style>
                @media (min-width: 768px) {
                    .modal-dialog {
                        width: 900px;
                        margin: 30px auto;
                    }
                }
            </style>
        </div>
        <div id="myModalOTPHardware" class="modal fade" role="dialog">
            <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                left: 0; height: 100%;" class="loading-gifHardware">
               <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
           </div>
            <div class="modal-dialog modal-800" id="myDialogOTPHardware">
                <div class="modal-content">
                    <div class="modal-header">
                        <div style="width: 70%; float: left;">
                            <h3 id="idLblTitleDetailModalGroup" class="modal-title" style="font-size: 18px;"></h3>
                        </div>
                        <div style="width: 29%; float: right;text-align: right;">
                            <button id="idLblTitleDetailModalClose" type="button" onclick="CloseDialog();" class="btn btn-info"></button>
                        </div>
                        <script>
                            $("#idLblTitleDetailModalGroup").text(ca_group_cert);
                            $("#idLblTitleDetailModalClose").text(global_fm_button_close);
                        </script>
                    </div>
                    <div class="modal-body">
                        <form role="formAddOTPHardware" id="formOTPHardware">
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailCompany" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input type="text" readonly id="DetailCompany" class="form-control123" />
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailCompany").text(global_fm_grid_company);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailTaxcode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input class="form-control123" type="text" id="DetailTaxcode" readonly/>
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailTaxcode").text(global_fm_MST + '/' + global_fm_MNS);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailPersonal" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input type="text" readonly id="DetailPersonal" class="form-control123" />
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailPersonal").text(global_fm_grid_personal);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailCMND" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input class="form-control123" type="text" id="DetailCMND" readonly/>
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailCMND").text(global_fm_CMND + '/' + global_fm_CitizenId + '/' + global_fm_HC);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailPhone" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input type="text" readonly id="DetailPhone" class="form-control123" />
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailPhone").text(token_fm_mobile);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailEmail" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input class="form-control123" type="text" id="DetailEmail" readonly/>
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailEmail").text(token_fm_email);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailPupore" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input type="text" readonly id="DetailPupore" class="form-control123" />
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailPupore").text(global_fm_certpurpose);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailStatus" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input class="form-control123" type="text" id="DetailStatus" readonly/>
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailStatus").text(global_fm_Status);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailEffective" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input type="text" readonly id="DetailEffective" class="form-control123" />
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailEffective").text(global_fm_valid_cert);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailExpiration" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input class="form-control123" type="text" id="DetailExpiration" readonly/>
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailExpiration").text(global_fm_Expire_cert);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailCertDN" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input class="form-control123" type="text" id="DetailCertDN" readonly/>
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailCertDN").text(global_fm_serial);
                                </script>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
       <script src="../js/active/highlight.js"></script>
        <script src="../js/active/main.js"></script>
    </body>
</html>
