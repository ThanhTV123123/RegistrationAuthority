<%-- 
    Document   : sDemoGetCert
    Created on : Dec 18, 2018, 3:02:40 PM
    Author     : thanh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search For Certificate Information</title>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script>
            changeFavicon("../");
            $(document).ready(function () {
                onClear();
            });
            function onSubmit()
            {
                $.ajax({
                    type: "post",
                    url: "../GetCertificateInfo",
                    data: {
                        REQUEST_TYPE: 'GetInfoDateExpire',
                        idCERT_TYPE: $("#idCERT_TYPE").val(),
                        idMST: $("#idMST").val(),
                        idCMND: $("#idCMND").val(),
                        KEY_AUTHEN: 'NcP4kkDrpDiAT+AQQ90tjg=='
                    },
                    cache: false,
                    success: function (html)
                    {
                        console.log(html);
                        $("#idCERT_TYPE_RESULT").val('');
                        $("#idMST_RESULT").val('');
                        $("#idCMND_RESULT").val('');
                        $("#idDATE_VALID_RESULT").val('');
                        $("#idDATE_EXPIRE_RESULT").val('');
                        if (html.length > 0)
                        {
                            var arr = JSON.parse(html);
                            if (arr[0].CODE === "0")
                            {
                                $("#idCERT_TYPE_RESULT").val($('#idCERT_TYPE option:selected').text());
                                $("#idMST_RESULT").val(arr[0].MST);
                                $("#idCMND_RESULT").val(arr[0].CMND);
                                $("#idDATE_VALID_RESULT").val(arr[0].DATE_VALID);
                                $("#idDATE_EXPIRE_RESULT").val(arr[0].DATE_EXPIRE);
                            }
                            else {
                                swal({
                                    title: "", text: arr[0].MESSAGE, imageUrl: "../Images/icon_error.png", imageSize: "45x45"
                                });
                            }
                        }
                    }
                });
                return false;
            }
            function onClear()
            {
                $("select#idCERT_TYPE").prop('selectedIndex', 0);
                $("#idMST").val('');
                $("#idCMND").val('');
                $("#idCERT_TYPE_RESULT").val('');
                $("#idMST_RESULT").val('');
                $("#idCMND_RESULT").val('');
                $("#idDATE_VALID_RESULT").val('');
                $("#idDATE_EXPIRE_RESULT").val('');
            }
        </script>
    </head>
    <body>
        <h1>Search For Certificate Information</h1>
        <form>
            <table style="width: 500px;">
                <col width="200">
                <col width="300">
                <tr>
                    <td>Loại CTS: </td>
                    <td>
                        <select id="idCERT_TYPE" name="idCERT_TYPE" style="width: 100%;">
                            <option value="ENTERPRISE">Doanh nghiệp (CTS Token)</option>
                            <option value="STAFF">Cá nhân trong doanh nghiệp (CTS Token)</option>
                            <option value="PERSONAL">Cá nhân (CTS Token)</option>
                            <option value="SIGNSERVER_ENTERPRISE">Doanh Nghiệp (CTS SignServer)</option>
                            <option value="SIGNSERVER_STAFF">Cá nhân trong doanh nghiệp (CTS SignServer)</option>
                            <option value="SIGNSERVER_PERSONAL">Cá nhân (CTS SignServer)</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Mã số thuế: </td>
                    <td>
                        <input id="idMST" name="idMST" type="text" style="width: 100%;"/>
                    </td>
                </tr>
                <tr>
                    <td>CMND: </td>
                    <td>
                        <input id="idCMND" name="idCMND" type="text" style="width: 100%;"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><div style="height: 10px; width: 100%;"></div></td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center;">
                        <input id="idHiddenKey" value="NcP4kkDrpDiAT+AQQ90tjg==" type="hidden" />
                        <input id="btnSubmit" value="Search" type="button" onclick="onSubmit();"/>
                        <input id="btnReset" value="Reset" type="button" onclick="onClear();"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><div style="height: 20px; width: 100%;"></div></td>
                </tr>
                <tr>
                    <td>Loại CTS: </td>
                    <td>
                        <input id="idCERT_TYPE_RESULT" name="idCERT_TYPE_RESULT" type="text" readonly style="width: 100%;background: #E6ECEE;" />
                    </td>
                </tr>
                <tr>
                    <td>Mã số thuế: </td>
                    <td>
                        <input id="idMST_RESULT" name="idMST_RESULT" type="text" readonly style="width: 100%;background: #E6ECEE;"/>
                    </td>
                </tr>
                <tr>
                    <td>CMND: </td>
                    <td>
                        <input id="idCMND_RESULT" name="idCMND_RESULT" type="text" readonly style="width: 100%;background: #E6ECEE;"/>
                    </td>
                </tr>
                <tr>
                    <td>Ngày hiệu lực CTS: </td>
                    <td>
                        <input id="idDATE_VALID_RESULT" name="idDATE_VALID_RESULT" type="text" readonly style="width: 100%;background: #E6ECEE;"/>
                    </td>
                </tr>
                <tr>
                    <td>Ngày hết hiệu lực CTS: </td>
                    <td>
                        <input id="idDATE_EXPIRE_RESULT" name="idDATE_EXPIRE_RESULT" type="text" readonly style="width: 100%;background: #E6ECEE;"/>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
