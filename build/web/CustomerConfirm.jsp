<%-- 
    Document   : CustomerConfirm
    Created on : Jan 14, 2022, 9:11:42 AM
    Author     : vanth
--%>

<%@page import="java.net.URLDecoder"%>
<%@page import="vn.ra.object.CERTIFICATION_PROPERTIES_JSON"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="vn.ra.process.ConnectDatabase"%>
<%@page import="vn.ra.object.CERTIFICATION"%>
<%@page import="vn.ra.utility.EscapeUtils"%>
<%@page import="vn.ra.process.CommonFunction"%>
<%@page import="java.text.Format"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="vn.ra.utility.Definitions"%>
<%@page import="java.util.Date"%>
<%@page import="vn.ra.process.DESEncryption"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    response.setHeader("X-Frame-Options", "SAMEORIGIN");
    Config conf = new Config();
    String sIsCA = conf.GetTryPropertybyCode(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
    String isChangeContact = "0";
    if(sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)){
        if(request.getRequestURL().toString().replace(request.getRequestURI(), "").toUpperCase().contains("MINVOICE.")){
            isChangeContact = "1";
        }
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="js/bootstrap.min.css" rel="stylesheet">
        <link href="style/font-awesome.css" rel="stylesheet">
        <link href="style/nprogress.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.js"></script>
        <script src="js/Language.js"></script>
        <script src="js/process_javajs.js"></script>
        <link rel="stylesheet" href="js/cssLogin_1.css">
        <script src="js/sweetalert-dev.js"></script>
        <link rel="stylesheet" href="js/sweetalert.css"/>
        <script type="text/javascript" src="Css/GlobalAlert.js"></script>
        <script src="style/jquery.min.js"></script>
        <script>
            changeFavicon("");
            $(document).ready(function () {
                var image = document.getElementById('idLogoPageHeader');
                image.src = LinkLogoPage;
                localStorage.setItem("VN", "1");
                if('<%=isChangeContact%>' === "1") {
                    image.src = "Images/Logo_Minvoice_210.png";
                }
            });
        </script>
    </head>
    <body>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <div id="header-two">
            <div class="header-two-123" style="padding: 5px 10px 5px 10px;">
                <div class="container">
                    <div class="col-md-5">
                        <div class="col-sm-6" style="padding: 0px;">
                            <img id="idLogoPageHeader" style="max-width: 210px;" class="img-responsive" />
                        </div>
                    </div>
                    <div class="col-md-7" style="text-align: right;">
                        <div class="form-group" style="padding: 0px;">
                            <h4 style="color: #1F9EBF; font-weight: bold; font-size: 16px;"> HOTLINE: <script>document.write(header_hotline);</script> </h4>
                        </div>
                    </div>
                    <div style="clear: both;"></div>
                </div>
            </div>
        </div>
        <div class="container">
            <div>
                <div style="padding: 0px 0 15px 0; text-align: center;color: #56C2E1; font-size: 20px;font-weight: bold;"><label id="idTitleCommon"></label></div>
                <form name="loginform" action="" method="post" autocomplete="off">
                    <div class="col-md-2"></div>
                    <div class="col-md-8" style="border: 1px solid #56C2E1;">
                        <div style="padding: 30px 10px 20px 10px;">
                            <%
                                String strKey = EscapeUtils.CheckTextNullDecoder(request.getParameter("key"));
                                String resCode = "";
                                String sDeclineReason = "";
                                String sDeclineDate = "";
                                String sSessLanguage = "1";
                                if(!"".equals(strKey)) {
                                    try {
                                        DESEncryption clsEnrypt=new DESEncryption();
                                        String sParse = clsEnrypt.decrypt(strKey);
                                        if(!"".equals(sParse)) {
                                            String sCertificateID = sParse.split("#")[0].trim();
                                            String sTime = sParse.split("#")[1];
                                            sSessLanguage = sParse.split("#")[2];
                                            Date date = new Date(Long.parseLong(sTime));
                                            Format format = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
                                            System.out.println("CustomerConfirm.jsp: " + format.format(date));
                                            boolean checkExpire = CommonFunction.checkDateBiggerCurrent(format.format(date), Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
                                            if(checkExpire == true) {
                                                if(!"".equals(sCertificateID)){
                                                    CERTIFICATION[][] rs = new CERTIFICATION[1][];
                                                    ConnectDatabase db = new ConnectDatabase();
                                                    db.S_BO_CERTIFICATION_DETAIL(sCertificateID, "1", rs);
                                                    if(rs[0].length > 0) {
                                                        if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED) {
                                                            boolean isConfirmEnabled = false;
                                                            if(rs[0][0].ACTIVATED_ENABLED == 2) {
                                                                isConfirmEnabled = true;
                                                            }
                                                            String sCONFIRMATION_HSM = rs[0][0].CONFIRMATION_PROPERTIES;
                                                            if(!"".equals(sCONFIRMATION_HSM)){
                                                                ObjectMapper objectMapper = new ObjectMapper();
                                                                CERTIFICATION_PROPERTIES_JSON itemParsePush = objectMapper.readValue(sCONFIRMATION_HSM, CERTIFICATION_PROPERTIES_JSON.class);
                                                                if(itemParsePush.getAttributes().size() > 0) {
                                                                    for (int i = 0; i < itemParsePush.getAttributes().size(); i++) {
//                                                                        if(EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getKey()).equals(Definitions.CONFIG_VALUE_HSM_CONFIRM_DECLINE_ENABLED)){
//                                                                            if(EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getValue()).equals("true")){
//                                                                                isConfirmEnabled = true;
//                                                                            }
//                                                                        }
                                                                        if(EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getKey()).equals(Definitions.CONFIG_VALUE_HSM_CONFIRM_DECLINE_REASON)){
                                                                            sDeclineReason = EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getValue());
                                                                        }
                                                                        if(EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getKey()).equals(Definitions.CONFIG_VALUE_HSM_CONFIRM_DECLINE_TIME)){
                                                                            String sDateValue = EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getValue());
                                                                            if(!"".equals(sDateValue)){
                                                                                Date dateParse = new Date(Long.parseLong(sDateValue));
                                                                                sDeclineDate = format.format(dateParse);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            if(isConfirmEnabled == false) {
                            %>
                            <script>
                                $(document).ready(function () {
                                    var sLanguage = '<%=sSessLanguage%>';
                                    console.log("sLanguage: " + sLanguage);
                                    if(sLanguage === '0'){
                                       localStorage.setItem("VN", "0");
                                    }
                                 });
                            </script>    
                            <table style="width: 100%;">
                                <colgroup>
                                    <col style="width: 30%;">
                                    <col style="width:1%">
                                    <col style="width:69%">
                                </colgroup>
                                <tr>
                                    <td colspan="3" style="text-align: left;padding-bottom: 20px;">
                                        <span><script>document.write(hsm_confirm_title_page);</script></span>
                                        <br/>
                                        <span style="font-style: italic; font-size: 13px;font-weight: bold;color: red;"><script>document.write(hsm_confirm_note_page);</script></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <script>document.write(synchneac_title_edit);</script>
                                    </td>
                                    <td></td>
                                    <td>
                                        <span style="font-weight: bold;"><%= EscapeUtils.CheckTextNull(rs[0][0].SUBJECT)%></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3" style="padding-top: 20px;text-align: center;">
                                        <input type="button" name="idConfirm" id="idConfirm" onclick="onConfirm('1', '<%=sCertificateID%>', '<%=strKey%>');" class="buttonlog" />
                                        <input type="button" name="idCancel" id="idCancel" onclick="popupDecline();" class="buttonlog" />
                                        <script>
                                            document.getElementById("idConfirm").value = global_fm_button_PasswordChange;
                                            document.getElementById("idCancel").value = global_fm_button_decline;
                                        </script>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3" style="padding-top: 10px;">
                                        
                                    </td>
                                </tr>
                                <tr id="idViewReason" style="display: none;padding-top: 10px;">
                                    <td>
                                        <script>document.write(global_fm_decline_desc);</script>
                                    </td>
                                    <td></td>
                                    <td>
                                        <input type="text" name="DeclineReason" id="DeclineReason" class="form-control123">
                                        <input type="button" name="idDecline" id="idDecline" onclick="onConfirm('0', '<%=sCertificateID%>', '<%=strKey%>');" class="buttonlog" />
                                        <input type="button" name="idDeclineClose" id="idDeclineClose" onclick="closeReason();" class="buttonlog" />
                                        <script>
                                            document.getElementById("idDecline").value = login_fm_buton_OK;
                                            document.getElementById("idDeclineClose").value = login_fm_buton_cancel;
                                        </script>
                                    </td>
                                </tr>
                            </table>
                            <script>
                                function popupDecline()
                                {
                                    $("#idViewReason").css("display", "");
                                }
                                function closeReason()
                                {
                                    $("#idViewReason").css("display", "none");
                                }
                                function onConfirm(isType, vCertID, vKey){
                                    var vTitleConfirm = token_confirm_cancel_request;
                                    if(isType === "1"){
                                        vTitleConfirm = token_confirm_issue_request;
                                    }
                                    swal({
                                        title: "",
                                        text: vTitleConfirm,
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
                                                url: "SomeCommon",
                                                data: {
                                                    idParam: 'confirmCustomerHSM',
                                                    isType: isType,
                                                    vCertID: vCertID,
                                                    vKey: vKey,
                                                    vDeclineReason: $("#DeclineReason").val()
                                                },
                                                cache: false,
                                                async: false,
                                                success: function (html)
                                                {
                                                    var myStrings = sSpace(html).split('#');
                                                    if (myStrings[0] === "0") {
                                                        if (myStrings[1] === "1") {
                                                            swal({
                                                                title: "", text: hsm_confirm_actived_success, imageUrl: "Images/success.png", imageSize: "45x45",
                                                                allowOutsideClick: false, allowEscapeKey: false},
                                                            function () {
                                                                location.reload();
                                                            });
                                                        } else {
                                                            swal({
                                                                title: "", text: hsm_confirm_declined_success, imageUrl: "Images/success.png", imageSize: "45x45",
                                                                allowOutsideClick: false, allowEscapeKey: false},
                                                            function () {
                                                                location.reload();
                                                            });
                                                        }
                                                    } else if(myStrings[0] ==="TYPE_NOT") {
                                                        funErrorLoginAlert(hsm_confirm_request_type_invalid);
                                                    } else if(myStrings[0] ==="DATA_FOUND") {
                                                        funErrorLoginAlert(hsm_confirm_data_not_found);
                                                    } else if(myStrings[0] ==="EXPIRE") {
                                                        funErrorLoginAlert(hsm_confirm_time_expire);
                                                    } else if(myStrings[0] ==="CERT_FAIL") {
                                                        funErrorLoginAlert(global_req_info_cert);
                                                    } else if(myStrings[0] === 'ISSUE_ERROR') {
                                                        funErrorLoginAlert(hsm_confirm_cert_issue_error);
                                                    } else if(myStrings[0] ==="DECLINED") {
                                                        hsm_confirm_request_declined = hsm_confirm_request_declined.replace("[TIME]",'<%= sDeclineDate%>');
                                                        hsm_confirm_request_declined = hsm_confirm_request_declined.replace("[REASON]",'<%=sDeclineReason%>');
                                                        hsm_confirm_request_declined = hsm_confirm_request_declined.replace("<br />","\n");
                                                        funErrorLoginAlert(hsm_confirm_request_declined);
                                                    } else {
                                                        funErrorLoginAlert(global_errorsql);
                                                    }
                                                }
                                            });
                                        }, JS_STR_ACTION_TIMEOUT);
                                    });
                                }
                            </script>
                            <%
                                                            } else {resCode = "DECLINED";}
                                                        } else {
                                                            if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_COMMITED) {
                                                                resCode = "ACTIVATED";
                                                            } else if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                                || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED
                                                                || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_DECLINED
                                                                || rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT) {
                                                                resCode = "STATUS_INVALID";
                                                            } else {
                                                                resCode = "ISSUE_ERROR";
                                                            }
                                                        }
                                                    } else {resCode = "DATA_FOUND";}
                                                } else {resCode = "KEY_INVALID";}
                                            } else {resCode = "KEY_EXPIRE";}
                                        } else {
                                            resCode = "KEY_INVALID";
                                            CommonFunction.LogErrorServlet(null, "CustomerConfirm.jsp: Key value is invalid");
                                        }
                                    } catch(Exception e) {
                                        resCode = "EXCEPTION";
                                        CommonFunction.LogExceptionJSP("CustomerConfirm.jsp", e.getMessage(), e);
                                    }
                                } else {
                                    resCode = "KEY_NULL";
                                }
                            %>
                            <script>
                                $(document).ready(function () {
                                    $(".loading-gif").hide();
                                    if('<%=sSessLanguage%>' === '0'){
                                        localStorage.setItem("VN", "0");
                                    }
                                    document.title = global_title_hsm_confirm;
                                    $("#idTitleCommon").text(global_title_hsm_confirm);
                                });
                            </script>
                            <%
                                if(!"".equals(resCode)){
                             %>
                             <table style="width: 100%;">
                                <colgroup>
                                    <col style="width: 30%;">
                                    <col style="width:1%">
                                    <col style="width:69%">
                                </colgroup>
                                <tr>
                                    <td colspan="3" style="text-align: left;padding-bottom: 0px;">
                                        <span><label id="idViewMess"></label></span>
                                    </td>
                                </tr>
                             </table>
                             <script>
                                 $(document).ready(function () {
                                    if('<%=sSessLanguage%>' === '0'){
                                        localStorage.setItem("VN", "0");
                                    }
                                    var varCode = '<%= resCode%>';
                                    if(varCode === 'ACTIVATED'){
                                        $("#idViewMess").text(hsm_confirm_cert_actived);
                                    } else if(varCode === 'DATA_FOUND'){
                                        $("#idViewMess").text(hsm_confirm_data_not_found);
                                    }else if(varCode === 'KEY_INVALID'){
                                        $("#idViewMess").text(hsm_confirm_url_invalid);
                                    }else if(varCode === 'KEY_EXPIRE'){
                                        $("#idViewMess").text(hsm_confirm_time_expire);
                                    } else if(varCode === 'KEY_NULL'){
                                        $("#idViewMess").text(hsm_confirm_encryption_notfound);
                                    }else if(varCode === 'DECLINED') {
                                        hsm_confirm_request_declined = hsm_confirm_request_declined.replace("[TIME]",'<%= sDeclineDate%>');
                                        hsm_confirm_request_declined = hsm_confirm_request_declined.replace("[REASON]",'<%=sDeclineReason%>');
                                        $("#idViewMess").html(hsm_confirm_request_declined);
                                    }else if(varCode === 'STATUS_INVALID') {
                                        $("#idViewMess").text(hsm_confirm_acteve_status_invalid);
                                    }else if(varCode === 'ISSUE_ERROR') {
                                        $("#idViewMess").text(hsm_confirm_cert_issue_error);
                                    } else {
                                        $("#idViewMess").text(global_errorsql);
                                    }
                                });
                             </script>
                             <%
                                }
                            %>
                        </div>
                    </div>
                    <div class="col-md-2"></div>
                </form>
                <script>
                    $(document).ready(function () {
                        $(".loading-gif").hide();
                    });
                </script>
                <div style="clear: both;"></div>
            </div>
            <a href="http://metop.info" style="display: none;">metop.info close</a>
        </div>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.min.js"></script>
        <link href="js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
        <script src="js/checkphone/intlTelInput.js" type="text/javascript"></script>
        <script src="js/moment.min.js"></script>
        <div id="footer1">
            <%@ include file="LFooter.jsp"%>
        </div>
    </body>
</html>
