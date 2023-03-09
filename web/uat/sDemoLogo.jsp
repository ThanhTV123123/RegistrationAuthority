<%-- 
    Document   : sDemoLogo
    Created on : Jul 31, 2018, 11:04:18 AM
    Author     : THANH-PC
--%>

<%@page import="vn.ra.process.CommonFunction"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--<%@include file="../Admin/ConnectionParam.jsp" %>--%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <script>
            function LoadEmail(vEmail)
            {
                var intEmail = vEmail.indexOf("@");
                if (intEmail !== -1)
                {
                    if (intEmail > 2)
                    {
                        var vEmailLengthSub = vEmail.substring(0, intEmail).length - 2;
                        var sAfterEmailSub = vEmail.substring(vEmailLengthSub, vEmail.length);
                        var sBeforeEmailSub = "";
                        for (var i = 0; i < vEmailLengthSub; i++)
                        {
                            sBeforeEmailSub += "*";
                        }
                        var sTotalEmailSub = sBeforeEmailSub + sAfterEmailSub;
                        alert(sTotalEmailSub);
                    } else {
                        var vEmailLengthSub = vEmail.substring(0, intEmail).length;
                        var sAfterEmailSub = vEmail.substring(vEmailLengthSub, vEmail.length);
                        var sBeforeEmailSub = "";
                        for (var i = 0; i < vEmailLengthSub; i++)
                        {
                            sBeforeEmailSub += "*";
                        }
                        var sTotalEmailSub = sBeforeEmailSub + sAfterEmailSub;
                        alert(sTotalEmailSub);
                    }
                }
            }
            function FileSelected(e)
            {
                file = document.getElementById('fu').files[document.getElementById('fu').files.length - 1];
                document.getElementById('fileName').innerHtml = file.name;
            }
        </script>
        <style>
            .file { position: relative; height: 30px; width: 200px; }
            .file > input[type="file"] { position: absolute; opacity: 0; top: 0; left: 0; right: 0; bottom: 0 }
            .file > label { position: absolute; top: 0; right: 0; left: 0; bottom: 0; background-color: #666; color: #fff; line-height: 30px; text-align: center; cursor: pointer; }
        </style>
    </head>
    <body>
        <h1>Hello World!</h1>
        <br/>

        <div>

            <!--<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.1.0/css/bootstrap.min.css">-->

            <!--<input data-com="fileBtn" placeholder="Select Image">-->
            <input data-com="fileBtn" placeholder="Select File">
            <input type="file" class="filestyle" data-classButton="btn btn-primary" data-input="false" data-classIcon="icon-plus" data-buttonText="Your label here.">
<!--            <div class="mt-2">
                <input id="build-by-myself" placeholder="Select Video" accept="video/mp4, video/webm">
            </div>-->

                    <script>
                        // 👇 Test
                        (() => {
                            window.onload = () => {
                                // FileButton.className ="btn btn-danger"
                                FileButton.BuildAll() // auto build all data-com="fileBtn"

                                // or you can specify the target that you wanted.
                                new FileButton(document.getElementById("build-by-myself"), "btn btn-danger")
                            }
                        })()

                        // 👇 script begin
                        class FileButton {
                            static className = "btn btn-primary"
                            static BuildAll() {
                                document.querySelectorAll(`input[data-com="fileBtn"]`).forEach(input => {
                                    new FileButton(input, FileButton.className)
                                })
                            }
                            /**
                             * @param {HTMLInputElement} input
                             * @param {string} btnClsName
                             * */
                            constructor(input, btnClsName) {
                                input.style.display = "none"; // [display is better than visibility](https://stackoverflow.com/a/48495293/9935654)
                                input.type = "file";
                                const frag = document.createRange().createContextualFragment(`<button class="${btnClsName}">${input.placeholder}</button>`)
                                const button = frag.querySelector(`button`)

                                input.parentNode.insertBefore(frag, input)

                                button.onclick = () => {
                                    input.click()
                                }
                                input.addEventListener(`change`, (e) => {
                                    // create a textNode to show the file name.
                                    const file = input.files[0]
                                    if (file === undefined) {
                                        return
                                    }
                                    const textNode = document.createTextNode(file.name)
                                    if (button.textNode) { // create a new attribute to record previous data.
                                        button.textNode.remove()
                                    }
                                    button.textNode = textNode
                                    button.parentNode.insertBefore(textNode, input)
                                })
                            }
                        }
                    </script>

        </div>

                                <!--                <br/>
                                                <br/>
                                                <input id="txtPhone" onblur="autoTrimTextField('txtPhone', this.value);" type="text"/>
                                                <br/>
                                                <input id="idBTN" value="Submit" type="button" onclick="LoadEmail('thanhtv@tomicalab.com');"/>-->
                </body>
                </html>
