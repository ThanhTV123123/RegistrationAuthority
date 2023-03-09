<%-- 
    Document   : sDemoColor
    Created on : Jul 18, 2018, 11:52:56 AM
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
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <script type='text/javascript' src='../Css/jscolor.js'></script>
        <link rel="stylesheet" type="text/css" media="all" href="../js/daterangepicker.css" />
    </head>
    <body class="nav-md">
        
        <div class="x_content">
            <fieldset class="scheduler-border">
                <legend class="scheduler-border" id="idLblTitleGroupCertHis"></legend>
                <input id="btnSave" value="Click" type="button" onclick="LoadCert();"/>
                <script>
                    function LoadCert() {
                        var varLocal = "UID_CBX_COMPANY###UID_INPUT_COMPANY###0.9.2342.19200300.100.1.1,";
                        var sListInputCheckID_Info = varLocal.split(',');
                        for (var i = 0; i < sListInputCheckID_Info.length; i++) {
                            var idLocalStoreUID_Info = sSpace(sListInputCheckID_Info[i].split('###')[0]);
                            if(idLocalStoreUID_Info === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY_CBX_ID) {
                                console.log("aaa");
                            }
                        }
                    }
                </script>
            </fieldset>
        </div>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../style/custom.min.js"></script>
        <script src="../js/moment.min_limit.js"></script>
        <script src="../js/daterangepicker_limit.js"></script>
        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/main.js"></script>
    </body>
</html>
