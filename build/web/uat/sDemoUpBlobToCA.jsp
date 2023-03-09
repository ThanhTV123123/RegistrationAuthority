<%-- 
    Document   : sDemoUpBlobToCA
    Created on : Apr 8, 2019, 1:53:24 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
<!--        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <link href="../Css/active/bootstrap-switch.css" rel="stylesheet">-->
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <script>
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function downForm() {
                if (!JSCheckEmptyField(document.getElementById("idValue").value))
                {
                    document.getElementById("idValue").focus();
                    funErrorAlert("Please Enter");
                    return false;
                }
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'demodownfromca_blob',
                        idValue: document.getElementById("idValue").value
                    },
                    cache: false,
                    success: function (html)
                    {
                        console.log(html);
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            var f = document.myname;
                            f.method = "post";
                            f.action = '../DowloadFile?filename=' + myStrings[1];
                            f.submit();
                        }
                        else
                        {
                            funErrorAlert("Error: " + myStrings[1]);
                        }
                    }
                });
                return false;
            }
            function UploadImageBase64()
            {
                var input1 = document.getElementById("input-file");
                if (input1.value !== '')
                {
                    var checkFileName = input1.value.substring(input1.value.lastIndexOf('.') + 1);
                    if (checkFileName === "jpg" || checkFileName === "png" || checkFileName === "JPG"
                        || checkFileName === "PNG" || checkFileName === "gif" || checkFileName === "GIF")
                    {
                        $('body').append('<div id="over"></div>');
                        $(".loading-gif").show();
                        file1 = input1.files[0];
                        var data1 = new FormData();
                        data1.append('file', file1);
                        $.ajax({
                            url: "../ReadFileSomeCommon",
                            data: data1,
                            cache: false,
                            contentType: false,
                            processData: false,
                            type: 'POST',
                            enctype: "multipart/form-data",
                            success: function (html) {
                                var myStrings = sSpace(html).split('###');
                                if (myStrings[0] === "0")
                                {
                                    ValidateLogoForm(myStrings[1]);
                                }
                                else if (myStrings[0] === JS_EX_LOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_login);
                                }
                                else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_another_login);
                                }
                                else if (myStrings[0] === JS_EX_GREAT_SIZE)
                                {
                                    funErrorAlert(branch_error_logo_great_size);
                                }
                                else
                                {
                                    funErrorAlert(global_errorsql);
                                }
                                $(".loading-gif").hide();
                                $('#over').remove();
                            }
                        });
                    }
                    else
                    {
                        funErrorAlert(global_req_image_format);
                    }
                }
                else
                {
                    funErrorAlert(global_req_file);
                }
            }
            function ValidateLogoForm(Logo)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'demoupfromca_blob',
                        idValue: document.getElementById("idValueUp").value,
                        Logo: Logo
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccLocalAlert(branch_succ_edit);
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
                            if (myStrings[1] === JS_STR_ERROR_CODE_99) {
                                funErrorAlert(global_error_login_info);
                            }
                            else {
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
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <form name="myname" method="post">
            <div style="padding: 10px 0 10px 0; text-align: left;width: 500px">
                <h2>Tải tệp hình ảnh từ BLOB</h2>
            </div>
            <div style="padding: 0px 0 10px 0; text-align: left;width: 800px">
                CA_ATTR_TYPE_NAME: <input id="idValue" name="idValue" type="text" style="width: 400px;"/>
            </div>
            <div style="padding: 0px 0 10px 0; text-align: left;width: 500px;">
                <input id="btnSubmit" name="btnSubmit" type="button" onclick="downForm();" value=" Download "/>
            </div>
            <div style="padding: 10px 0 10px 0; text-align: left;width: 500px">
                <h2>Update tệp hình ảnh đến BLOB</h2>
            </div>
            <div style="padding: 0px 0 10px 0; text-align: left;width: 800px">
                CA_ATTR_TYPE_NAME: <input id="idValueUp" name="idValueUp" type="text" style="width: 400px;"/>
            </div>
            <div style="padding: 10px 0 10px 0; text-align: left;width: 500px">
                <div class="cssFileImport">Chọn tệp</div>
                <input type="file" id="input-file" NAME="input-file" accept=".jpg,.png,.gif"/>
            </div>
            <div style="padding: 0px 0 10px 0; text-align: left;width: 500px;">
                <input id="btnSubmitUp" name="btnSubmitUp" type="button" onclick="UploadImageBase64();" value=" Upload "/>
            </div>
        </form>
    </body>
</html>
