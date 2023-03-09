<%-- 
    Document   : sUatCreateWorkSpace
    Created on : Jan 2, 2019, 11:53:16 AM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create WorkSpace</title>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script src="../Css/GlobalAlert.js"></script>
        <script>
            function onSubmit()
            {
                if (!JSCheckEmptyField($("#idUSR").val()))
                {
                    $("#idUSR").focus();
                    funErrorAlert("Vui lòng nhập Authen Name");
                    return false;
                }
                if (!JSCheckEmptyField($("#idPWD").val()))
                {
                    $("#idPWD").focus();
                    funErrorAlert("Vui lòng nhập Authen Pass");
                    return false;
                }
                if (!JSCheckEmptyField($("#idName").val()))
                {
                    $("#idName").focus();
                    funErrorAlert("Vui lòng nhập WorkSpace Name");
                    return false;
                }
                $.ajax({
                    type: "post",
                    url: "../UatReferJRB",
                    data: {
                        idParam: 'createworkspace',
                        idUSR: $("#idUSR").val(),
                        idPWD: $("#idPWD").val(),
                        idName: $("#idName").val()
                    },
                    cache: false,
                    success: function (html)
                    {
                        $("#idUSR").val('');
                        $("#idPWD").val('');
                        $("#idResult").val('');
                        console.log(html);
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            $("#idResult").val(arr[1]);
                        }
                        else
                        {
                            $("#idResult").val(arr[1]);
                        }
                    }
                });
                return false;
            }
        </script>
    </head>
    <body>
        <h1>Create WorkSpace for Jack Rabbit</h1>
        <form name="myname">
            <table style="width: 500px;">
                <col width="200">
                <col width="300">
                <tr>
                    <td>Authentication Name</td>
                    <td>
                        <input id="idUSR" name="idUSR" type="text" style="width: 100%;"/>
                    </td>
                </tr>
                <tr>
                    <td>Authentication Pass</td>
                    <td>
                        <input id="idPWD" name="idPWD" type="password" style="width: 100%;"/>
                    </td>
                </tr>
                <tr>
                    <td>WorkSpace Name</td>
                    <td>
                        <input id="idName" name="idName" value="TRUONGNNT" type="text" style="width: 100%;"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><div style="height: 10px; width: 100%;"></div></td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center;">
                        <input id="btnSubmit" value="Create" type="button" onclick="onSubmit();"/>
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
