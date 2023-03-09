<%-- 
    Document   : sDemoColor2
    Created on : Oct 9, 2018, 1:41:01 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <!--<link href="../style/icheck/skins/flat/green.css" rel="stylesheet">-->
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title>JSP Page</title>
        <script>
//            function testConfirm123()
//            {
//                $.ajax({
//                    type: "post",
//                    url: "../SomeCommon",
//                    data: {idParam: 'checkphonevalid',phoneValue: $("#idPhone").val()},
//                    cache:false,async:false,
//                    success: function (html_first) {
//                        var s = sSpace(html_first).split('#');
//                        if(s[0] === "0")
//                        {
//                            
//                        } else {
//                            $("#idPhone").focus();
//                            funErrorAlert("Failed");
//                            return false;
//                        }
//                    }
//                });
//            }
//            function getCChatStatusA(digits) {
//                var CChat = $.ajax({
//                    type: "POST",
//                    url: "../SomeCommon",
//                    data: { idParam: 'checkphonevalid',  phoneValue: digits}
//                });
//                return CChat;
//            }
//            function JSCheckFormatPhoneNewPolicyGeneral(digits)
//            {
//                var vValue = trimAllString(digits);
//                alert(vValue);
//                if (vValue !== "")
//                {
//                    alert("11");
//                    var cChatStatus = getCChatStatusA(vValue);
//                    cChatStatus.done(function(msg) {
//                        alert(msg);
//                        var s = sSpace(msg).split('#');
//                        if(s[0] === "0")
//                        {
//                            alert("333");
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    });
//                } else {
//                    return false;
//                }
//            }
//            function onCheckPhone()
//            {
//                if (!JSCheckFormatPhoneNewPolicyGeneral($("#idPhone").val()))
//                {
//                    $("#idPhone").focus();
//                    funErrorAlert(global_req_phone_format);
//                    return false;
//                }
//                alert("OK");
//            } 
//                $.ajax({
//                    type: "post",
//                    url: "../SomeCommon",
//                    data: {
//                        idParam: 'checkphonevalid',
//                        phoneValue: digits
//                    },
//                    async:false,
//                    success: function (html_first) {
//                        var s = sSpace(html_first).split('#');
//                        if(s[0] === "0")
//                        {
//                            console.log("yes");
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    }
//                });
//                return false;
//                $.ajax({
//                    type: "POST",
//                    url: "index.php?page=CChatAction",
//                    data: { idParam: 'checkphonevalid', phoneValue: digits}
//                }).done(function(msg) {
//                    var s = sSpace(msg).split('#');
//                    callback.call(s[0]);
//                });
//            }
//            function testConfirm() {
//                getCChatStatus(function(test) {
//                    alert(test);
//                });
//                if (JSCheckFormatPhone123($("#idPhone").val()))
//                {
//                    $("#idPhone").focus();
//                    funErrorAlert("Failed");
//                    return false;
//                }
//                funErrorAlert("OK");
//            }
            
//            function getCChatStatus(digits) {
//                var CChat = $.ajax({
//                    type: "POST",
//                    url: "../SomeCommon",
//                    data: { idParam: 'checkphonevalid',  phoneValue: digits}
//                });
//                return CChat;
//            }

            function JSCheckFormatPhoneNewPolicy(sPhone) {
                var vValue = trimAllString(sPhone);
                var cChatStatus = getCChatStatus(vValue);
                cChatStatus.done(function(msg) {
                    var s = sSpace(msg).split('#');
                    if(s[0] !== "0")
                    {
                        funErrorAlert(global_req_phone_format);
                        return false;
                    }
                });
            }
            function checkNewEmailAbc()
            {
                var vValuePhone = sSpace($("#idPhone").val());
                var cChatStatus = getCheckStatusEmail(vValuePhone);
                cChatStatus.done(function(msg) {
                    var s = sSpace(msg).split('#');
                    if(s[0] !== "0")
                    {
                        $("#idPhone").focus();
                        funErrorAlert(global_req_mail_format);
                        return false;
                    }
                });
            }
            function checkNewPhoenAbc()
            {
                if (!JSCheckEmptyField($("#idPhone").val()))
                {
                    $("#idPhone").focus();
                    funErrorAlert("vui long nhap phone");
                    return false;
                } else {
                    var vValuePhoneNum = trimAllString($("#idPhone").val());
                    var cChatStatusNum = getCheckStatusPhone(vValuePhoneNum);
                    cChatStatusNum.done(function(msg) {
                        var s = sSpace(msg).split('#');
                        if(s[0] !== "0")
                        {
                            $("#idPhone").focus();
                            funErrorAlert(global_req_phone_format);
                            return false;
                        }
                    });
                }
                if (!JSCheckEmptyField($("#idEmail").val()))
                {
                    $("#idEmail").focus();
                    funErrorAlert("vui long nhap email");
                    return false;
                } else {
                    var vValuePhoneNum = sSpace($("#idEmail").val());
                    var cChatStatusNum = getCheckStatusEmail(vValuePhoneNum);
                    cChatStatusNum.done(function(msg) {
                        var s = sSpace(msg).split('#');
                        if(s[0] !== "0")
                        {
                            $("#idPhone").focus();
                            funErrorAlert(global_req_phone_format);
                            return false;
                        }
                    });
                }
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: "abctest"
                    },
                    async:false,
                    success: function (html_first) {
                        var s = sSpace(html_first).split('#');
                        if(s[0] === "0")
                        {
                            funSuccLocalAlert("success");
                        } else {
                            funErrorAlert("error");
                        }
                    }
                });
                return false;
            }
            function FormCheckEmailSearchHand(regex, sValue)
            {
                if (sSpace(sValue.val())) !== "")
                {
                    var filter = new RegExp(regex);
//                    var filter = /^[A-Za-z0-9._]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/;
//                    var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
                    if (!filter.test(sValue.val()))
                    {
                        return false;
                    }
                }
                return true;
            }
            function checkEmailHand()
            {
                if (!JSCheckEmptyField($("#idEmail").val()))
                {
                    $("#idEmail").focus();
                    funErrorAlert("nhap email");
                    return false;
                } else {
                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#idEmail").val()))
                    {
                        $("#idEmail").focus();
                        funErrorAlert("khong dung format");
                        return false;
                    }
                }
            }
            
            function FormCheckPhoneHand(regex, sValue)
            {
                if (sSpace(sValue) !== "")
                {
                    var intStart = regex.lastIndexOf('{');
                    var intEnd = regex.lastIndexOf('}');
                    var regex1 = regex.substring(intStart+1, intEnd);
                    var intMaxChar = 20;
                    if(regex1.indexOf(',') !== -1)
                    {
                        intMaxChar = regex1.split(',')[1];
                        intMaxChar = regex1.split(',')[1];
                    }
                    var filter = new RegExp(regex);
                    if (!filter.test(sValue))
                    {
                        return false;
                    }
                    if(sValue.length >= intMaxChar)
                    {
                        return false;
                    }
                }
                return true;
            }
            function checkPhoneHand()
            {
                if (!JSCheckEmptyField($("#idPhone").val()))
                {
                    $("#idPhone").focus();
                    funErrorAlert("nhap phone");
                    return false;
                } else {
                    if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#idPhone").val()))
                    {
                        $("#idPhone").focus();
                        funErrorAlert("khong dung format");
                        return false;
                    }
                }
            }
        </script>
    </head>
    <body>
        <input id="idPhone" name="idPhone" type="text" />
        <br/>
        <input id="idEmail" name="idEmail" type="text" />
        <br/>
        <input id="btnConfirm" value=" Confirm " onclick="checkPhoneHand();" type="button" />
        <script src="../style/jquery.min.js"></script>
            <script src="../style/bootstrap.min.js"></script>
            <script src="../style/custom.min.js"></script>
            <script src="../js/moment.min.js"></script>
            <script src="../js/daterangepicker.js"></script>
    </body>
</html>
