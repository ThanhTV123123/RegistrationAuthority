<%-- 
    Document   : Forgot
    Created on : Jun 17, 2015, 3:07:43 PM
    Author     : VanThanh
--%>

<%@page import="vn.ra.utility.Definitions"%>
<%@page import="vn.ra.utility.Config"%>
<%@page import="vn.ra.utility.EscapeUtils"%>
<%@page import="java.sql.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    response.setHeader("X-Frame-Options", "SAMEORIGIN");
    Config conf = new Config();
    String sIsCA = conf.GetTryPropertybyCode(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
    String isChangeContact = "0";
    if(sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)){
        if(request.getRequestURL().toString().replace(request.getRequestURI(), "").toUpperCase().contains("MINVOICE.")){
//        if(request.getRequestURL().toString().replace(request.getRequestURI(), "").toUpperCase().contains("LOCALHOST")){
            isChangeContact = "1";
        }
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <script src="js/bootstrap.min.js"></script>
        <link href="js/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <script src="js/jquery.min.js"></script>
        <link rel="stylesheet" href="js/cssLogin_1.css">
        <script src="js/sweetalert-dev.js"></script>
        <link rel="stylesheet" href="js/sweetalert.css"/>
        <script src="js/Language.js"></script>
        <script src="js/process_javajs.js"></script>
        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript" src="Css/GlobalAlert.js"></script>
        <script>
            changeFavicon("");
            document.title = login_title_forget;
            $(document).ready(function () {
                $(".loading-gif").hide();
                $("#sUsername").focus(function () {
                    if ($("#sUsername").val() === global_fm_Username)
                    {
                        $("#sUsername").val('');
                    }
                });
                $("#sUsername").blur(function () {
                    if ($("#sUsername").val() === "")
                    {
                        $("#sUsername").val(global_fm_Username);
                    }
                });
//                $("#sEmail").focus(function () {
//                    if ($("#sEmail").val() === global_fm_email)
//                    {
//                        $("#sEmail").val('');
//                    }
//                });
//                $("#sEmail").blur(function () {
//                    if ($("#sEmail").val() === "")
//                    {
//                        $("#sEmail").val(global_fm_email);
//                    }
//                });
            });
            function randomString() {
                var chars1 = "0123456789";
                var string_length1 = 8;
                var randomstring1 = '';
                for (var i = 0; i < string_length1; i++)
                {
                    var rnum = Math.floor(Math.random() * chars1.length);
                    randomstring1 += chars1.substring(rnum, rnum + 1);
                }
                document.loginform.randomCode.value = randomstring1;
            }
            function exportRecordAbc(idCSRF) {
//                funErrorLoginAlert("Functions is under construction, please come back later");
//                return false;
//                var email = document.loginform.sEmail;
                var Username = document.loginform.sUsername;
                if (!JSCheckEmptyField(Username.value) || Username.value === global_fm_Username)
                {
                    Username.focus();
                    funErrorLoginAlert(policy_req_empty + global_fm_Username);
                    return false;
                }
//                if (!JSCheckEmptyField(email.value) || email.value === global_fm_email)
//                {
//                    email.focus();
//                    funErrorLoginAlert(policy_req_empty + global_fm_email);
//                    return false;
//                }
//                else
//                {
//                    if (!FormCheckEmailSearch(email.value)) {
//                        email.focus();
//                        funErrorLoginAlert(global_req_mail_format);
//                        return false;
//                    }
//                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "LoginCommon",
                    data: {
                        idParam: 'beforeforgotpass',
                        sUsername: Username.value,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            var vEMAIL_RECEIVE = BlurEmailCharacter(arr[2]);
                            var vLogin_conform_forget = login_conform_forget.replace(JS_STR_FORGOT_CONFIRM_EMAIL, vEMAIL_RECEIVE);
                            swal({
                                title: "",
                                text: vLogin_conform_forget,
                                imageUrl: "Images/icon_warning.png",
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
                                        url: "LoginCommon",
                                        data: {
                                            idParam: 'afterforgotpass',
                                            randomCode: $("#randomCode").val(),
                                            sUsername: Username.value,
                                            id: arr[1],
                                            CsrfToken: idCSRF
                                        },
                                        cache: false,
                                        success: function (html1) {
                                            var arr1 = sSpace(html1).split('#');
                                            if (arr1[0] === "0")
                                            {
                                                swal({
                                                    title: "", text: login_succ_forget, imageUrl: "Images/success.png", imageSize: "45x45"},
                                                function () {
                                                    window.location = "Login.jsp";
                                                });
                                            }
                                            else if (arr1[0] === "1") {
                                                funErrorLoginAlert(sendmail_error);
                                            }
                                            else
                                            {
                                                funErrorLoginAlert(global_errorsql);
                                            }
                                            $(".loading-gif").hide();
                                            $('#over').remove();
                                        }
                                    });
                                    return false;
                                }, JS_STR_ACTION_TIMEOUT);
                            });
//                            if (arr[1] !== "0")
//                            {
//                                swal({
//                                    title: "", text: login_succ_forget_request, imageUrl: "Images/success.png", imageSize: "45x45"},
//                                function () {
//                                    window.location = "Login.jsp";
//                                });
//                            }
//                            else
//                            {
//                                swal({
//                                    title: "", text: login_succ_forget, imageUrl: "Images/success.png", imageSize: "45x45"},
//                                function () {
//                                    window.location = "Login.jsp";
//                                });
//                            }   
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            swal({title: "", text: CSRF_Mess, imageUrl: "Images/icon_error.png", imageSize: "45x45",
                                allowOutsideClick: false, allowEscapeKey: false},
                            function () {
                                window.location = "Modules/Logout.jsp";
                            });
                        }
                        else if (arr[0] === "1") {
                            funErrorLoginAlert(sendmail_notexists_account);
                        }
                        else
                        {
                            funErrorLoginAlert(global_errorsql);
                        }
                        $(".loading-gif").hide();
                        $('#over').remove();
                    }
                });
                return false;
            }
        </script>
    </head>
    <%        String anticsrf = "" + Math.random();
        request.getSession().setAttribute("anticsrf", anticsrf);
    %>
    <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
         left: 0; height: 100%;" class="loading-gif">
        <img src="Images/ajax-loader1.gif" alt="Please wait..." />
    </div>
    <body onload="randomString();">
        <div id="header-two">
            <div class="header-two-123">
                <div class="container">
                    <div class="col-md-5">
                        <div class="col-sm-6" style="padding: 0px;">
                            <a href="Login.jsp"><img id="idLogoPageHeader" style="max-width: 210px;" class="img-responsive" /></a>
                        </div>
                    </div>
                    <div class="col-md-7" style="text-align: right;">
                        <div class="form-group" style="padding: 0px;">
                            <h4 style="color: #1F9EBF; font-weight: bold; font-size: 16px;"> HOTLINE: <script>document.write(header_hotline);</script> </h4>
                            <p style="color: #000000;" id="idDivLanguage"><img title="English" style="width: 18px;height: 18px;cursor: pointer;" onclick="loginEN('1');" src="Images/en_flag.png" /> | <img onclick="loginEN('0');" style="width: 18px;height: 18px;cursor: pointer;" id="idLoginBannerLanguageRight" title="Vietnamese" src="Images/vn_flag.png" /></p>
                        </div>
                    </div>
                    <div style="clear: both;"></div>
                </div>
            </div>
            <script>
                $(document).ready(function () {
                    if(IsWhichCA === "15" || IsWhichCA === "17") {
                        $("#idLoginBannerLanguageRight").attr("src", "Images/Laos_flag.png");
                        //document.getElementById("idDivLanguage").style.display = "none";
                        //localStorage.setItem("VN", "0");
                        document.getElementById("idLoginBannerLanguageRight").setAttribute("title", "Laos");
                    }
                });
                var image = document.getElementById('idLogoPageHeader');
                image.src = LinkLogoPage;
                if('<%=isChangeContact%>' === "1") {
                    image.src = "Images/Logo_Minvoice_210.png";
                }
                function loginEN(id)
                {
                    //localStorage.setItem("LoadFirtVN", "1");
                    if (id === "1")
                    {
                        localStorage.setItem("VN", "0");
                    }
                    else
                    {
                        localStorage.setItem("VN", "1");
                    }
                    location.reload();
                }
            </script>
        </div>
        <div class="container">
            <%
                if(!sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
            %>
            <div style="border: 1px solid #56C2E1; padding: 10px;">
                <form name="loginform" method="post" autocomplete="off">
                    <div class="col-md-6">
                        <table style="width: 100%;">
                            <colgroup>
                                <col width="100%">
                            </colgroup>
                            <tbody>
                            <tr>
                                <td>
                                    <div style="padding-bottom: 10px;color: #73879C; font-weight: 600; font-size: 20px;">
                                        <span><script>document.write(login_title_forget);</script></span>
                                    </div>
                                </td>
                                <script>
                                    var image = document.getElementById('idLogoPageHeader');
                                    image.src = LinkLogoPage;
                                    if('<%=isChangeContact%>' === "1") {
                                        image.src = "Images/Logo_Minvoice_210.png";
                                    }
                                </script>
                            </tr>
                            <tr>
                                <td>
                                    <div class="form-group" style="padding: 10px 0px 10px 0px;margin: 0;">
                                        <label class="control-label"><script>document.write(global_fm_Username);</script></label>
                                        <input class="form-control123" style="width: 100%;" name="sUsername"
                                            maxlength="50" id="sUsername" type="text">
                                        <script>
                                            document.getElementById('sUsername').value = global_fm_Username;
                                        </script>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="form-group" style="padding: 10px 0px 10px 0px;margin: 0;">
                                        <span style="padding-right: 5px;">
                                            <input type="button" id="idSave" onclick="exportRecordAbc('<%=anticsrf%>');" class="buttonlog" name="sSubmit" />
                                        </span>
                                        <input type="button" id="idClose" onclick="window.location = 'Login.jsp'" name="sCancel" class="buttonlog" />
                                        <input type="hidden" name="randomCode" id="randomCode" />
                                        <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                        <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                        <script>
                                            document.getElementById('idClose').value = login_fm_buton_cancel;
                                            document.getElementById('idSave').value = login_fm_buton_OK;
                                        </script>
                                    </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="col-md-1">

                    </div>
                    <div class="col-md-5" style="border-left: 1px dashed #56C2E1;">
                        <div style="text-align: center;padding-left: 10px;">
                            <div class="aOpen">
                                <img id="idBannerPageHeader" class="img-responsive" />
                            </div>
                            <script>
                                $(document).ready(function () {
                                    var imageBanner = document.getElementById('idBannerPageHeader');
                                    imageBanner.src = LinkBannerPage;
                                });
                            </script>
                        </div>
                    </div>
                    <div style="clear: both;"></div>
                </form>
            </div>
            <%
                } else {
            %>
            <div>
                <form name="loginform" method="post" autocomplete="off">
                    <div style="padding: 0px 0 15px 0; text-align: center;color: #56C2E1; font-size: 20px;font-weight: bold;"><script>document.write(login_title_forget);</script></div>
                    <div class="col-md-3"></div>
                    <div class="col-md-6" style="border: 1px solid #56C2E1;">
                        <div style="padding: 10px;">
                            <table style="width: 100%;">
                                <colgroup>
                                    <col width="100%">
                                </colgroup>
                                <tbody>
                                <tr>
                                    <td>
                                        <div style="padding-bottom: 10px;color: #73879C; font-weight: 600; font-size: 20px;display: none;">
                                            <span><script>document.write(login_title_forget);</script></span>
                                        </div>
                                    </td>
                                    <script>
                                        var image = document.getElementById('idLogoPageHeader');
                                        image.src = LinkLogoPage;
                                        if('<%=isChangeContact%>' === "1") {
                                            image.src = "Images/Logo_Minvoice_210.png";
                                        }
                                    </script>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="form-group" style="padding: 10px 0px 10px 0px;margin: 0;">
                                            <label class="control-label"><script>document.write(global_fm_Username);</script></label>
                                            <input class="form-control123" style="width: 100%;" name="sUsername"
                                                maxlength="50" id="sUsername" type="text">
                                            <script>
                                                document.getElementById('sUsername').value = global_fm_Username;
                                            </script>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="form-group" style="padding: 10px 0px 10px 0px;margin: 0;text-align: center;">
                                            <span style="padding-right: 5px;">
                                                <input type="button" id="idSave" onclick="exportRecordAbc('<%=anticsrf%>');" class="buttonlog" name="sSubmit" />
                                            </span>
                                            <input type="button" id="idClose" onclick="window.location = 'Login.jsp'" name="sCancel" class="buttonlog" />
                                            <input type="hidden" name="randomCode" id="randomCode" />
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                            <script>
                                                document.getElementById('idClose').value = login_fm_buton_cancel;
                                                document.getElementById('idSave').value = login_fm_buton_OK;
                                            </script>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="col-md-3"></div>
                    <div style="clear: both;"></div>
                </form>
            </div>
            <%
                }
            %>
        </div>
        <%
            String sTermEnable = conf.GetPropertybyCode(Definitions.CONFIG_WEBSITE_POLICY_TERMS_ENABLED).trim();
            if("1".equals(sTermEnable)) {
        %>
        <div id="footer1" style="padding-bottom: 15px;clear: both;">
            <%@ include file="TwoFooter.jsp"%>
        </div>
        <%
            } else {
        %>
        <div id="footer1" style="<%=sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "background: #f40000;" : ""%>">
            <%@ include file="LFooter.jsp"%>
        </div>
        <%
            }
        %>
    </body>
</html>