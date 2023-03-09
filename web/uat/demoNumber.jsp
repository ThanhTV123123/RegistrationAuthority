<%-- 
    Document   : demoNumber
    Created on : Jul 7, 2022, 11:51:02 AM
    Author     : vanth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
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
        <script>
            function autoConvertMoney(vMoney, vMoneyID)
            {
                var ValidateChar = /[^a-zA-Z]/g;
                var charString = vMoney.replace(ValidateChar, "");
                if (charString < 1)
                {
                    
                } else {
                    alert("1");
                }
//                if(isNaN(vMoney) === true) {
//                    alert("fail");
//                } else {
//                    if (vMoney !== "")
//                    {
//                        vMoney = vMoney.replace(/,/g, '');
//                        vMoneyID.val(ConvertMoneySeparatorsNF(vMoney, '.', '.', ','));
//                    } else {
//                        vMoneyID.val("0");
//                    }
//                }
            }
        </script>
    </head>
    <body>
        <h1>Hello World!</h1>
        <br/>
        <input type="text" name="FINE_FOR_LACK_OF_BRIEF" id="FINE_FOR_LACK_OF_BRIEF" class="form-control123"
            onblur="autoConvertMoney(this.value, $('#FINE_FOR_LACK_OF_BRIEF'))" oninput="autoConvertMoney(this.value, $('#FINE_FOR_LACK_OF_BRIEF'))">
    </body>
</html>
