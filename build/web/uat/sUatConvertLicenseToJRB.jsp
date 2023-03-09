<%-- 
    Document   : sUatConvertLicenseToJRB
    Created on : Dec 26, 2018, 4:05:25 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="../Css/GlobalAlert.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <title>Convert License To Jack Rabbit</title>
        <script>
            $(document).ready(function () {
                onClear();
            });
            function onSubmit()
            {
                $.ajax({
                    type: "post",
                    url: "../UatReferJRB",
                    data: {
                        REQUEST_TYPE: 'convertlicensetojrb',
                        idUSR: $("#idUSR").val(),
                        idPWD: $("#idPWD").val(),
                        idCERT_ID: $("#idCERT_ID").val()
                    },
                    cache: false,
                    success: function (html)
                    {
                        $("#idUSR").val('');
                        $("#idPWD").val('');
                        $("#idCERT_ID").val('');
                        $("#idResult").val('');
                        if (html.length > 0)
                        {
                            var arr = JSON.parse(html);
                            if (arr[0].CODE === "0")
                            {
                                $("#idResult").val('OK');
                            }
                            else {
                                $("#idResult").val(arr[0].MESSAGE);
                            }
                        }
                    }
                });
                return false;
            }
            function onClear()
            {
                $("#idUSR").val('');
                $("#idPWD").val('');
                $("#idCERT_ID").val('');
                $("#idResult").val('');
            }
        </script>
    </head>
    <body>
        <h1>Convert License To Jack Rabbit</h1>
        <br/>
        <form name="myname">
            <table style="width: 500px;">
                <col width="200">
                <col width="300">
                <tr>
                    <td>usr</td>
                    <td>
                        <input id="idUSR" name="idUSR" type="text" style="width: 100%;"/>
                    </td>
                </tr>
                <tr>
                    <td>pwd</td>
                    <td>
                        <input id="idPWD" name="idPWD" type="password" style="width: 100%;"/>
                    </td>
                </tr>
                <tr>
                    <td>CERTIFICATION ID (Empty is Choose All; And contrary)</td>
                    <td>
                        <input id="idCERT_ID" name="idCERT_ID" type="text" style="width: 100%;"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><div style="height: 10px; width: 100%;"></div></td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center;">
                        <input id="btnSubmit" value="Search" type="button" onclick="onSubmit();"/>
                        <input id="btnReset" value="Reset" type="button" onclick="onClear();"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><div style="height: 20px; width: 100%;"></div></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input id="idResult" name="idResult" readonly type="text" style="width: 100%;"/>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
