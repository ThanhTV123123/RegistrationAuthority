<%-- 
    Document   : sDemoConnectWS
    Created on : Aug 2, 2018, 1:50:16 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <script src="../js/jquery.PrintArea.js"></script>
        <title>Demo Call Connector</title>
        <script>
            function CallWS() {
                $.ajax({
                    type: "post",
                    url: "../TraceSystemCommon",
                    data: {
                        idParam: 'callws'
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccNoLoad(myStrings[1]);
                        }
                        else
                        {
                            funErrorAlert(global_errorsql);
                        }
                    }
                });
                return false;
            }
            function CallDB() {
                $.ajax({
                    type: "post",
                    url: "../TraceSystemCommon",
                    data: {
                        idParam: 'calldb'
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccNoLoad("OK: " + myStrings[1]);
                        }
                        else
                        {
                            funErrorAlert("Error: " + myStrings[1]);
                        }
                    }
                });
                return false;
            }
            function ExportRarPDF() {
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'demoprintrar'
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccNoLoad("OK: " + myStrings[1]);
                        }
                        else
                        {
                            funErrorAlert("Error: " + myStrings[1]);
                        }
                    }
                });
                return false;
            }
        </script>
    </head>
    <body>
        (V1.190319.1615)
        <BR/>
        <input id="btnConnector" value="Call Connector" type="button" onclick="CallWS();" />
        <input id="btnDB" value="Call DB" type="button" onclick="CallDB();" />
        <br/>
        <input id="btnDB" value="Test file rar_pdf" style="display: none;" type="button" onclick="ExportRarPDF();" />
    </body>
</html>
