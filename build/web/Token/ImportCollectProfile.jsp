<%-- 
    Document   : ImportPushNoticeList
    Created on : May 20, 2019, 4:39:15 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<%  response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"> 
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <link href="../Css/active/bootstrap-switch.css" rel="stylesheet">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script language="javascript">
            changeFavicon("../");
            document.title = collectimport_title_list;
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function downloadErrorFile()
            {
                $.ajax({
                    type: "post",
                    url: "../DownloadFileCSR",
                    data: {
                        idParam: "savefileimportpush"
                    },
                    catche: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            var f = document.formimport;
                            f.method = "post";
                            f.action = '../DownFromSaveFile?idParam=downfileimportpush&name=' + arr[1];
                            f.submit();
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        }
                        else if (arr[0] === JS_EX_LOGIN) {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (arr[0] === JS_EX_ANOTHERLOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else if (arr[0] === "1") {
                            funErrorAlert(global_error_export_excel);
                        }
                        else if (arr[0] === "2") {
                            funErrorAlert(global_succ_NoResult);
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
            function calUpload(idCSRF)
            {
                var input1 = document.getElementById('input-file');
                if (input1.value !== '')
                {
                    var checkFileName = input1.value.substring(input1.value.lastIndexOf('.') + 1);
                    if (checkFileName === "xls" || checkFileName === "xlsx" || checkFileName === "csv")
                    {   
                        console.log("idSessIsChoise " + $("#idSessIsChoise").val());
                        console.log("ControlType " + $("#ControlType").val());
                        $.ajax({
                            type: "post",
                            url: '../TokenCommon',
                            data: {
                                idParam: 'checkcsrfforcollectprofile',
                                ControlType: $("#ControlType").val(),
                                idSessIsChoise: $("#idSessIsChoise").val(),
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var arr = sSpace(html).split('#');
                                if (arr[0] === "0")
                                {
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gif").show();
                                    file1 = input1.files[0];
                                    var data1 = new FormData();
                                    data1.append('file', file1);
                                    $.ajax({
                                        type: 'POST',
                                        url: '../CollectProfileImport',
                                        data: data1,
                                        cache: false,
                                        contentType: false,
                                        processData: false,
                                        enctype: "multipart/form-data",
                                        success: function (html) {
                                            var arr = sSpace(html).split('###');
                                            if (arr[0] === "0")
                                            {
                                                if(arr[3] !== "")
                                                {
                                                    swal({
                                                        title: "",
                                                        text: pushimport_succ_edit + "\n"
                                                            + global_fm_combox_success + ": " + arr[1] + token_succ_import_error + arr[2] + "\n"
                                                            + pushimport_succ_conform_down,
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
                                                        downloadErrorFile();
                                                    }); 
                                                } else {
                                                    var sAlert = pushimport_succ_edit + "\n" + global_fm_combox_success + ": " + arr[1];
                                                    funSuccAlert(sAlert, "ImportCollectProfile.jsp");
                                                }                                                
                                            }
                                            else if (arr[0] === JS_EX_LOGIN)
                                            {
                                                RedirectPageLoginNoSess(global_alert_login);
                                            }
                                            else if (arr[0] === JS_EX_ANOTHERLOGIN)
                                            {
                                                RedirectPageLoginNoSess(global_alert_another_login);
                                            }
                                            else
                                            {
                                                if (arr[1] === JS_EX_SPECIAL)
                                                {
                                                    funErrorAlert(global_error_file_special);
                                                }
                                                else if (arr[1] === JS_EX_CSV_NO_TOKENID)
                                                {
                                                    funErrorAlert(token_error_no_tokenid);
                                                }
                                                else if (arr[1] === JS_EX_CSV_NO_SOPIN)
                                                {
                                                    funErrorAlert(token_error_no_sopin);
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
                                }
                                else
                                {
                                    funCsrfAlert();
                                }
                            }
                        });
                    }
                    else
                    {
                        funErrorAlert(token_error_import_format);
                    }
                }
                else
                {
                    funErrorAlert(global_req_file);
                }
            }
        </script>
        <style>.projects th{font-weight: bold;}</style>
    </head>
    <body class="nav-md">
        <%
            if ((session.getAttribute("sUserID")) != null) {
                String anticsrf = "" + Math.random();
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
                        document.getElementById("idNameURL").innerHTML = collectimport_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%
                                try {
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(collectimport_title_import);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <input id="btnImport" class="btn btn-info" onclick="calUpload('<%= anticsrf%>')" type="button"/>
                                                <script>$("#btnImport").val(global_fm_button_import);</script>
                                                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="formimport" method="post" class="form-horizontal">
                                            <div class="form-group" style="padding: 0 0 10px 0;margin: 0;">
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <label id="idLblTitleRequestType" class="control-label col-sm-3" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <script>document.write(cert_fm_major_name);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                                        <select name="ControlType" id="ControlType" class="form-control123">
                                                            <option value="0"><script>document.write(collectimport_fm_control_cert);</script></option>
                                                            <option value="1"><script>document.write(collectimport_fm_control_profile);</script></option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <label class="radio-inline" style="font-weight: bold;">
                                                        <input type="radio" name="nameCheck" id="nameCheck1" checked>
                                                        <script>document.write(collectimport_fm_set_push);</script>
                                                    </label>&nbsp;&nbsp;
                                                    <label class="radio-inline" style="font-weight: bold;"><input type="radio" name="nameCheck" id="nameCheck2">
                                                        <script>document.write(collectimport_fm_delete_push);</script>
                                                    </label>
                                                    <input type="text" id="idSessIsChoise" style="display: none;" name="idSessIsChoise"/>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_browse_file);</script></label>
                                                <input type="file" id="input-file" accept=".xlsx,.xls,.csv" style="width: 100%;"
                                                    class="btn btn-default btn-file select_file">
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;">
                                                <label class="control-label"><script>document.write(token_fm_import_sample);</script></label> <a style="cursor: pointer;" onclick="return downloadSamplePush();"><script>document.write(global_fm_down);</script></a>
                                            </div>
                                            <script>
                                                $("#idSessIsChoise").val('1');
                                                $('.radio-inline').on('click', function () {
                                                    var s = $(this).find('input').attr('id');
                                                    if (s === 'nameCheck1')
                                                    {
                                                        $("#idSessIsChoise").val('1');
//                                                        $('#divBranchParent').css('display', 'none');
                                                        //document.getElementById("idPushText").readOnly = false;
                                                        //document.getElementById("idPushLink").readOnly = false;
                                                    }
                                                    if (s === 'nameCheck2')
                                                    {
                                                        $("#idSessIsChoise").val('0');
//                                                        $('#divBranchParent').css('display', '');
                                                       // $("#idPushText").val("");
                                                        //$("#idPushLink").val("");
                                                        //document.getElementById("idPushText").readOnly = true;
                                                       // document.getElementById("idPushLink").readOnly = true;
                                                    }
                                                });
                                                function downloadSamplePush()
                                                {
                                                    var f = document.formimport;
                                                    f.method = "post";
                                                    f.action = '../DownFromSaveFile?idParam=downfilesamplecontrolprofile';
                                                    f.submit();
                                                }
//                                                function onChangeSetPush()
//                                                {
//                                                    if ($("#SetPush").is(':checked'))
//                                                    {
//                                                        document.getElementById("idPushText").readOnly = false;
//                                                        document.getElementById("idPushLink").readOnly = false;
//                                                    }
//                                                    else
//                                                    {
//                                                        $("#idPushText").val("");
//                                                        $("#idPushLink").val("");
//                                                        document.getElementById("idPushText").readOnly = true;
//                                                        document.getElementById("idPushLink").readOnly = true;
//                                                    }
//                                                }
                                            </script>
                                        </form>
                                    </div>
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
                        </div>
                    </div>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
            <script src="../style/jquery.min.js"></script>
            <script src="../style/bootstrap.min.js"></script>
            <script src="../style/custom.min.js"></script>
            <script src="../js/moment.min.js"></script>
            <script src="../js/active/highlight.js"></script>
            <script src="../js/active/bootstrap-switch.js"></script>
            <script src="../js/active/main.js"></script>
            <script src="../js/daterangepicker.js"></script>
        </div>
        <%        } else {
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
