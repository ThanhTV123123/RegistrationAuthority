<%-- 
    Document   : UploadFile
    Created on : Dec 19, 2018, 2:03:15 PM
    Author     : thanh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Demo Upload File By Jack Rabbit</title>
        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript" src="Css/GlobalAlert.js"></script>
        <script>
            function calUpload()
            {
                var input1 = document.getElementById('input-file');
                if (input1.value !== '')
                {
                    file1 = input1.files[0];
                    var data1 = new FormData();
                    data1.append('file', file1);
                    $.ajax({
                        type: 'POST',
                        url: '../TestUploadFile',
                        data: data1,
                        cache: false,
                        contentType: false,
                        processData: false,
                        enctype: "multipart/form-data",
                        success: function (html) {
                            var arr = sSpace(html).split('###');
                            if (arr[0] === "0")
                            {
                                alert("OK");
                            } else
                            {

                                alert("Có lỗi xảy ra");
                            }
                        }
                    });
                } else
                {
                    alert("Vui lòng chọn file");
                }
            }
        </script>
    </head>
    <body>
        <form name="formimport" method="post" class="form-horizontal">
            <div class="form-group" style="padding: 0px;margin: 0;">
                <label class="control-label123">Chọn tệp hình ảnh</label>
                <input type="file" id="input-file" style="width: 100%;"
                       onchange="calUpload();">
            </div>
        </form>
    </body>
</html>
