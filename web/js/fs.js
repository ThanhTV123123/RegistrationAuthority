/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var hSession = "";
var domain = "https://127.0.0.1:14415/";
var LibList_MACOS = "libcastle.1.0.0.dylib;viettel-ca_v5.dylib;viettel-ca_v4.dylib";
var LibList_WIN = "*";
var process = false;
var Base64 = {_keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=", encode: function (e) {
        var t = "";
        var n, r, i, s, o, u, a;
        var f = 0;
        e = Base64._utf8_encode(e);
        while (f < e.length) {
            n = e.charCodeAt(f++);
            r = e.charCodeAt(f++);
            i = e.charCodeAt(f++);
            s = n >> 2;
            o = (n & 3) << 4 | r >> 4;
            u = (r & 15) << 2 | i >> 6;
            a = i & 63;
            if (isNaN(r)) {
                u = a = 64
            } else if (isNaN(i)) {
                a = 64
            }
            t = t + this._keyStr.charAt(s) + this._keyStr.charAt(o) + this._keyStr.charAt(u) + this._keyStr.charAt(a)
        }
        return t
    }, decode: function (e) {
        var t = "";
        var n, r, i;
        var s, o, u, a;
        var f = 0;
        e = e.replace(/[^A-Za-z0-9\+\/\=]/g, "");
        while (f < e.length) {
            s = this._keyStr.indexOf(e.charAt(f++));
            o = this._keyStr.indexOf(e.charAt(f++));
            u = this._keyStr.indexOf(e.charAt(f++));
            a = this._keyStr.indexOf(e.charAt(f++));
            n = s << 2 | o >> 4;
            r = (o & 15) << 4 | u >> 2;
            i = (u & 3) << 6 | a;
            t = t + String.fromCharCode(n);
            if (u != 64) {
                t = t + String.fromCharCode(r)
            }
            if (a != 64) {
                t = t + String.fromCharCode(i)
            }
        }
        t = Base64._utf8_decode(t);
        return t
    }, _utf8_encode: function (e) {
        e = e.replace(/\r\n/g, "\n");
        var t = "";
        for (var n = 0; n < e.length; n++) {
            var r = e.charCodeAt(n);
            if (r < 128) {
                t += String.fromCharCode(r)
            } else if (r > 127 && r < 2048) {
                t += String.fromCharCode(r >> 6 | 192);
                t += String.fromCharCode(r & 63 | 128)
            } else {
                t += String.fromCharCode(r >> 12 | 224);
                t += String.fromCharCode(r >> 6 & 63 | 128);
                t += String.fromCharCode(r & 63 | 128)
            }
        }
        return t
    }, _utf8_decode: function (e) {
        var t = "";
        var n = 0;
        var r = c1 = c2 = 0;
        while (n < e.length) {
            r = e.charCodeAt(n);
            if (r < 128) {
                t += String.fromCharCode(r);
                n++
            } else if (r > 191 && r < 224) {
                c2 = e.charCodeAt(n + 1);
                t += String.fromCharCode((r & 31) << 6 | c2 & 63);
                n += 2
            } else {
                c2 = e.charCodeAt(n + 1);
                c3 = e.charCodeAt(n + 2);
                t += String.fromCharCode((r & 15) << 12 | (c2 & 63) << 6 | c3 & 63);
                n += 3
            }
        }
        return t
    }}
function showErrMsg(code) {
    switch (code) {
        case '100100':
            funErrorAlert(agree_fm_choise_plugin_100100);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100101':
            funErrorAlert(agree_fm_choise_plugin_100101);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100102':
        case '100202':
            funErrorAlert(agree_fm_choise_plugin_100202);
            return false;
            break;
        case '100103':
        case '100205':
        case '100303':
            funErrorAlert(agree_fm_choise_plugin_100303);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100104':
            funErrorAlert(agree_fm_choise_plugin_100104);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100200':
            funErrorAlert(agree_fm_choise_plugin_100200);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100201':
            funErrorAlert(agree_fm_choise_plugin_100201);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100203':
            funErrorAlert(agree_fm_choise_plugin_100203);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100204':
            alert(agree_fm_choise_plugin_100204);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100300':
            funErrorAlert(agree_fm_choise_plugin_100300);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100301':
            funErrorAlert(agree_fm_choise_plugin_100301);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100302':
            funErrorAlert(agree_fm_choise_plugin_100302);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        default:
            funErrorAlert(agree_fm_choise_plugin_noerror + ": " + code);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
    }
}
function showErrMsgEdit(code) {
    switch (code) {
        case '100100':
            document.getElementById("Certificate").value = document.getElementById("CertificateHiden").value;
            funErrorAlert(agree_fm_choise_plugin_100100);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100101':
            document.getElementById("Certificate").value = document.getElementById("CertificateHiden").value;
            funErrorAlert(agree_fm_choise_plugin_100101);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100102':
        case '100202':
            document.getElementById("Certificate").value = document.getElementById("CertificateHiden").value;
            funErrorAlert(agree_fm_choise_plugin_100202);
            return false;
            break;
        case '100103':
        case '100205':
        case '100303':
            document.getElementById("Certificate").value = document.getElementById("CertificateHiden").value;
            funErrorAlert(agree_fm_choise_plugin_100303);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100104':
            document.getElementById("Certificate").value = document.getElementById("CertificateHiden").value;
            funErrorAlert(agree_fm_choise_plugin_100104);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100200':
            document.getElementById("Certificate").value = document.getElementById("CertificateHiden").value;
            funErrorAlert(agree_fm_choise_plugin_100200);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100201':
            document.getElementById("Certificate").value = document.getElementById("CertificateHiden").value;
            funErrorAlert(agree_fm_choise_plugin_100201);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100203':
            document.getElementById("Certificate").value = document.getElementById("CertificateHiden").value;
            funErrorAlert(agree_fm_choise_plugin_100203);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100204':
            document.getElementById("Certificate").value = document.getElementById("CertificateHiden").value;
            alert(agree_fm_choise_plugin_100204);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100300':
            document.getElementById("Certificate").value = document.getElementById("CertificateHiden").value;
            funErrorAlert(agree_fm_choise_plugin_100300);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100301':
            document.getElementById("Certificate").value = document.getElementById("CertificateHiden").value;
            funErrorAlert(agree_fm_choise_plugin_100301);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        case '100302':
            document.getElementById("Certificate").value = document.getElementById("CertificateHiden").value;
            funErrorAlert(agree_fm_choise_plugin_100302);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
        default:
            document.getElementById("Certificate").value = document.getElementById("CertificateHiden").value;
            funErrorAlert(agree_fm_choise_plugin_noerror + ": " + code);
            $(".loading-gif").hide();
            $('#over').remove();
            return false;
            break;
    }
}
function initPluginFS()
{
    //=================>>Check OS<<=================
    var OSName = "Unknown";
    if (window.navigator.userAgent.indexOf("Windows NT 6.2") != -1)
        OSName = "Windows 8";
    if (window.navigator.userAgent.indexOf("Windows NT 6.1") != -1)
        OSName = "Windows 7";
    if (window.navigator.userAgent.indexOf("Windows NT 6.0") != -1)
        OSName = "Windows Vista";
    if (window.navigator.userAgent.indexOf("Windows NT 5.1") != -1)
        OSName = "Windows XP";
    if (window.navigator.userAgent.indexOf("Windows NT 5.0") != -1)
        OSName = "Windows 2000";
    if (window.navigator.userAgent.indexOf("Mac") != -1)
        OSName = "Mac/iOS";
    if (window.navigator.userAgent.indexOf("X11") != -1)
        OSName = "UNIX";
    if (window.navigator.userAgent.indexOf("Linux") != -1)
        OSName = "Linux";
    //=================>>Check OS<<=================

    var xmlhttp;
    var response = "";
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else
    {// code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = function ()
    {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
        {
            response = xmlhttp.responseText;
            process = false;
            if (response != "")
            {
                hSession = response;
                return;
            }
            if (response == "")
            {
                alert("Vui long cai dat SignPlugin va bam f5 de tiep tuc");
                //window.open('./SignPlugin_Installer.exe');				
                return;
            }
        }
    }
    xmlhttp.open("POST", domain + "getSession", true);
    if (OSName == "Mac/iOS")
    {
        xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xmlhttp.send("liblist=" + LibList_MACOS);
    } else if ((OSName == "UNIX") || (OSName == "Linux"))
    {
        alert("Not Support");
    } else
    {
        //xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        //xmlhttp.send("liblist=" + LibList_WIN);
        xmlhttp.send();
    }
}
function getThumprint()
{
    //get thumprint
    var ReqThumprint;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        ReqThumprint = new XMLHttpRequest();
    } else
    {// code for IE6, IE5
        ReqThumprint = new ActiveXObject("Microsoft.XMLHTTP");
    }
    ReqThumprint.onreadystatechange = function ()
    {
        if (ReqThumprint.readyState == 4 && ReqThumprint.status == 200)
        {
            document.getElementById('cert_Thumprint').value = ReqThumprint.responseText;
            process = false;
        }
    }
    ReqThumprint.open("POST", domain + "getThumprint", true);
    ReqThumprint.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    ReqThumprint.send("sessionID=" + hSession);
}
function getCertifcateFS()
{
//    alert("a");
//    if (process == true)
//        return;
    $('body').append('<div id="over"></div>');
    $(".loading-gif").show();
    var ReqCert;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        ReqCert = new XMLHttpRequest();
    } else
    {// code for IE6, IE5
        ReqCert = new ActiveXObject("Microsoft.XMLHTTP");
    }
    ReqCert.onreadystatechange = function ()
    {
        if (ReqCert.readyState == 4 && ReqCert.status == 200)
        {
            document.getElementById('CertificatePKI').value = ReqCert.responseText;
            //get info of certificate
            if (document.getElementById('CertificatePKI').value == "")
            {
//                document.getElementById('cert_SNB').value = "";
//                document.getElementById('cert_CN').value = "";
//                document.getElementById('cert_DN').value = "";
//                document.getElementById('cert_Issuer').value = "";
//                document.getElementById('cert_ValidFrom').value = "";
//                document.getElementById('cert_ValidTo').value = "";
//                document.getElementById('cert_Thumprint').value = "";
                //get infomation error
                //get serial number
                var ReqLastErr;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqLastErr = new XMLHttpRequest();
                } else
                {// code for IE6, IE5
                    ReqLastErr = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqLastErr.onreadystatechange = function ()
                {
                    if (ReqLastErr.readyState == 4 && ReqLastErr.status == 200)
                    {
                        process = false;
                        showErrMsg(ReqLastErr.responseText);
                    }
                }
                ReqLastErr.open("POST", domain + "getLastErr", true);
                ReqLastErr.send();
            } else
            {
                //get serial number
//                var ReqSNB;
//                if (window.XMLHttpRequest)
//                {// code for IE7+, Firefox, Chrome, Opera, Safari
//                    ReqSNB = new XMLHttpRequest();
//                } else
//                {// code for IE6, IE5
//                    ReqSNB = new ActiveXObject("Microsoft.XMLHTTP");
//                }
//                ReqSNB.onreadystatechange = function ()
//                {
//                    if (ReqSNB.readyState == 4 && ReqSNB.status == 200)
//                    {
//                        document.getElementById('cert_SNB').value = ReqSNB.responseText;
//                    }
//                }
//                ReqSNB.open("POST", domain + "getCertSNB", true);
//                ReqSNB.send();
//
//                //get CN
//                var ReqCN;
//                if (window.XMLHttpRequest)
//                {// code for IE7+, Firefox, Chrome, Opera, Safari
//                    ReqCN = new XMLHttpRequest();
//                } else
//                {// code for IE6, IE5
//                    ReqCN = new ActiveXObject("Microsoft.XMLHTTP");
//                }
//                ReqCN.onreadystatechange = function ()
//                {
//                    if (ReqCN.readyState == 4 && ReqCN.status == 200)
//                    {
//                        document.getElementById('cert_CN').value = ReqCN.responseText;
//                    }
//                }
//                ReqCN.open("POST", domain + "getCertCN", true);
//                ReqCN.send();
//
//                //get DN
//                var ReqDN;
//                if (window.XMLHttpRequest)
//                {// code for IE7+, Firefox, Chrome, Opera, Safari
//                    ReqDN = new XMLHttpRequest();
//                } else
//                {// code for IE6, IE5
//                    ReqDN = new ActiveXObject("Microsoft.XMLHTTP");
//                }
//                ReqDN.onreadystatechange = function ()
//                {
//                    if (ReqDN.readyState == 4 && ReqDN.status == 200)
//                    {
//                        document.getElementById('cert_DN').value = ReqDN.responseText;
//                    }
//                }
//                ReqDN.open("POST", domain + "getCertDN", true);
//                ReqDN.send();
//
//                //get issuer
//                var ReqIssuer;
//                if (window.XMLHttpRequest)
//                {// code for IE7+, Firefox, Chrome, Opera, Safari
//                    ReqIssuer = new XMLHttpRequest();
//                } else
//                {// code for IE6, IE5
//                    ReqIssuer = new ActiveXObject("Microsoft.XMLHTTP");
//                }
//                ReqIssuer.onreadystatechange = function ()
//                {
//                    if (ReqIssuer.readyState == 4 && ReqIssuer.status == 200)
//                    {
//                        document.getElementById('cert_Issuer').value = ReqIssuer.responseText;
//                    }
//                }
//                ReqIssuer.open("POST", domain + "getCertIssuer", true);
//                ReqIssuer.send();
//
//                //get valid from
//                var ReqValidDate;
//                if (window.XMLHttpRequest)
//                {// code for IE7+, Firefox, Chrome, Opera, Safari
//                    ReqValidDate = new XMLHttpRequest();
//                } else
//                {// code for IE6, IE5
//                    ReqValidDate = new ActiveXObject("Microsoft.XMLHTTP");
//                }
//                ReqValidDate.onreadystatechange = function ()
//                {
//                    if (ReqValidDate.readyState == 4 && ReqValidDate.status == 200)
//                    {
//                        document.getElementById('cert_ValidFrom').value = ReqValidDate.responseText;
//                    }
//                }
//                ReqValidDate.open("POST", domain + "getCertValidDate", true);
//                ReqValidDate.send();
//
//                //get expire date
//                var ReqExpireDate;
//                if (window.XMLHttpRequest)
//                {// code for IE7+, Firefox, Chrome, Opera, Safari
//                    ReqExpireDate = new XMLHttpRequest();
//                } else
//                {// code for IE6, IE5
//                    ReqExpireDate = new ActiveXObject("Microsoft.XMLHTTP");
//                }
//                ReqExpireDate.onreadystatechange = function ()
//                {
//                    if (ReqExpireDate.readyState == 4 && ReqExpireDate.status == 200)
//                    {
//                        document.getElementById('cert_ValidTo').value = ReqExpireDate.responseText;
//                        getThumprint();
//                    }
//                }
//                ReqExpireDate.open("POST", domain + "getCertExpireDate", true);
//                ReqExpireDate.send();

            }
        } else
            process = true;
    }
    ReqCert.open("POST", domain + "getCertificate", true);
    ReqCert.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    ReqCert.send("sessionID=" + hSession);
    setTimeout(function(){ 
        $(".loading-gif").hide();
        $('#over').remove();
    }, 5000);
}

function getCertifcateEditFS()
{
//    alert("a");
//    if (process == true)
//        return;
    $('body').append('<div id="over"></div>');
    $(".loading-gif").show();
    var ReqCert;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        ReqCert = new XMLHttpRequest();
    } else
    {// code for IE6, IE5
        ReqCert = new ActiveXObject("Microsoft.XMLHTTP");
    }
    ReqCert.onreadystatechange = function ()
    {
        if (ReqCert.readyState == 4 && ReqCert.status == 200)
        {
            document.getElementById('Certificate').value = ReqCert.responseText;
            //get info of certificate
            if (document.getElementById('Certificate').value == "")
            {
//                document.getElementById('cert_SNB').value = "";
//                document.getElementById('cert_CN').value = "";
//                document.getElementById('cert_DN').value = "";
//                document.getElementById('cert_Issuer').value = "";
//                document.getElementById('cert_ValidFrom').value = "";
//                document.getElementById('cert_ValidTo').value = "";
//                document.getElementById('cert_Thumprint').value = "";
                //get infomation error
                //get serial number
                var ReqLastErr;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqLastErr = new XMLHttpRequest();
                } else
                {// code for IE6, IE5
                    ReqLastErr = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqLastErr.onreadystatechange = function ()
                {
                    if (ReqLastErr.readyState == 4 && ReqLastErr.status == 200)
                    {
                        process = false;
                        showErrMsgEdit(ReqLastErr.responseText);
                    }
                }
                ReqLastErr.open("POST", domain + "getLastErr", true);
                ReqLastErr.send();
            } else
            {
                //get serial number
//                var ReqSNB;
//                if (window.XMLHttpRequest)
//                {// code for IE7+, Firefox, Chrome, Opera, Safari
//                    ReqSNB = new XMLHttpRequest();
//                } else
//                {// code for IE6, IE5
//                    ReqSNB = new ActiveXObject("Microsoft.XMLHTTP");
//                }
//                ReqSNB.onreadystatechange = function ()
//                {
//                    if (ReqSNB.readyState == 4 && ReqSNB.status == 200)
//                    {
//                        document.getElementById('cert_SNB').value = ReqSNB.responseText;
//                    }
//                }
//                ReqSNB.open("POST", domain + "getCertSNB", true);
//                ReqSNB.send();
//
//                //get CN
//                var ReqCN;
//                if (window.XMLHttpRequest)
//                {// code for IE7+, Firefox, Chrome, Opera, Safari
//                    ReqCN = new XMLHttpRequest();
//                } else
//                {// code for IE6, IE5
//                    ReqCN = new ActiveXObject("Microsoft.XMLHTTP");
//                }
//                ReqCN.onreadystatechange = function ()
//                {
//                    if (ReqCN.readyState == 4 && ReqCN.status == 200)
//                    {
//                        document.getElementById('cert_CN').value = ReqCN.responseText;
//                    }
//                }
//                ReqCN.open("POST", domain + "getCertCN", true);
//                ReqCN.send();
//
//                //get DN
//                var ReqDN;
//                if (window.XMLHttpRequest)
//                {// code for IE7+, Firefox, Chrome, Opera, Safari
//                    ReqDN = new XMLHttpRequest();
//                } else
//                {// code for IE6, IE5
//                    ReqDN = new ActiveXObject("Microsoft.XMLHTTP");
//                }
//                ReqDN.onreadystatechange = function ()
//                {
//                    if (ReqDN.readyState == 4 && ReqDN.status == 200)
//                    {
//                        document.getElementById('cert_DN').value = ReqDN.responseText;
//                    }
//                }
//                ReqDN.open("POST", domain + "getCertDN", true);
//                ReqDN.send();
//
//                //get issuer
//                var ReqIssuer;
//                if (window.XMLHttpRequest)
//                {// code for IE7+, Firefox, Chrome, Opera, Safari
//                    ReqIssuer = new XMLHttpRequest();
//                } else
//                {// code for IE6, IE5
//                    ReqIssuer = new ActiveXObject("Microsoft.XMLHTTP");
//                }
//                ReqIssuer.onreadystatechange = function ()
//                {
//                    if (ReqIssuer.readyState == 4 && ReqIssuer.status == 200)
//                    {
//                        document.getElementById('cert_Issuer').value = ReqIssuer.responseText;
//                    }
//                }
//                ReqIssuer.open("POST", domain + "getCertIssuer", true);
//                ReqIssuer.send();
//
//                //get valid from
//                var ReqValidDate;
//                if (window.XMLHttpRequest)
//                {// code for IE7+, Firefox, Chrome, Opera, Safari
//                    ReqValidDate = new XMLHttpRequest();
//                } else
//                {// code for IE6, IE5
//                    ReqValidDate = new ActiveXObject("Microsoft.XMLHTTP");
//                }
//                ReqValidDate.onreadystatechange = function ()
//                {
//                    if (ReqValidDate.readyState == 4 && ReqValidDate.status == 200)
//                    {
//                        document.getElementById('cert_ValidFrom').value = ReqValidDate.responseText;
//                    }
//                }
//                ReqValidDate.open("POST", domain + "getCertValidDate", true);
//                ReqValidDate.send();
//
//                //get expire date
//                var ReqExpireDate;
//                if (window.XMLHttpRequest)
//                {// code for IE7+, Firefox, Chrome, Opera, Safari
//                    ReqExpireDate = new XMLHttpRequest();
//                } else
//                {// code for IE6, IE5
//                    ReqExpireDate = new ActiveXObject("Microsoft.XMLHTTP");
//                }
//                ReqExpireDate.onreadystatechange = function ()
//                {
//                    if (ReqExpireDate.readyState == 4 && ReqExpireDate.status == 200)
//                    {
//                        document.getElementById('cert_ValidTo').value = ReqExpireDate.responseText;
//                        getThumprint();
//                    }
//                }
//                ReqExpireDate.open("POST", domain + "getCertExpireDate", true);
//                ReqExpireDate.send();

            }
        }
        else
            process = true;
    }
    ReqCert.open("POST", domain + "getCertificate", true);
    ReqCert.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    ReqCert.send("sessionID=" + hSession);
    setTimeout(function(){ 
        $(".loading-gif").hide();
        $('#over').remove();
    }, 5000);
}

function getSignCert()
{

    var ReqCert;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        ReqCert = new XMLHttpRequest();
    } else
    {// code for IE6, IE5
        ReqCert = new ActiveXObject("Microsoft.XMLHTTP");
    }
    ReqCert.onreadystatechange = function ()
    {
        if (ReqCert.readyState == 4 && ReqCert.status == 200)
        {
            document.getElementById('cert_rawData').value = ReqCert.responseText;
            //get info of certificate
            if (document.getElementById('cert_rawData').value == "")
            {
                document.getElementById('cert_SNB').value = "";
                document.getElementById('cert_CN').value = "";
                document.getElementById('cert_DN').value = "";
                document.getElementById('cert_Issuer').value = "";
                document.getElementById('cert_ValidFrom').value = "";
                document.getElementById('cert_ValidTo').value = "";
                //get infomation error
                var ReqLastErr;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqLastErr = new XMLHttpRequest();
                } else
                {// code for IE6, IE5
                    ReqLastErr = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqLastErr.onreadystatechange = function ()
                {
                    if (ReqLastErr.readyState == 4 && ReqLastErr.status == 200)
                    {
                        //alert("Error code = " +ReqLastErr.responseText);
                        showErrMsg(ReqLastErr.responseText);
                        process = false;
                    }
                }
                ReqLastErr.open("POST", domain + "getLastErr", true);
                ReqLastErr.send();
            } else
            {
                //get serial number
                var ReqSNB;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqSNB = new XMLHttpRequest();
                } else
                {// code for IE6, IE5
                    ReqSNB = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqSNB.onreadystatechange = function ()
                {
                    if (ReqSNB.readyState == 4 && ReqSNB.status == 200)
                    {
                        document.getElementById('cert_SNB').value = ReqSNB.responseText;
                    }
                }
                ReqSNB.open("POST", domain + "getCertSNB", true);
                ReqSNB.send();

                //get CN
                var ReqCN;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqCN = new XMLHttpRequest();
                } else
                {// code for IE6, IE5
                    ReqCN = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqCN.onreadystatechange = function ()
                {
                    if (ReqCN.readyState == 4 && ReqCN.status == 200)
                    {
                        document.getElementById('cert_CN').value = ReqCN.responseText;
                    }
                }
                ReqCN.open("POST", domain + "getCertCN", true);
                ReqCN.send();

                //get DN
                var ReqDN;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqDN = new XMLHttpRequest();
                } else
                {// code for IE6, IE5
                    ReqDN = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqDN.onreadystatechange = function ()
                {
                    if (ReqDN.readyState == 4 && ReqDN.status == 200)
                    {
                        document.getElementById('cert_DN').value = ReqDN.responseText;
                    }
                }
                ReqDN.open("POST", domain + "getCertDN", true);
                ReqDN.send();

                //get issuer
                var ReqIssuer;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqIssuer = new XMLHttpRequest();
                } else
                {// code for IE6, IE5
                    ReqIssuer = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqIssuer.onreadystatechange = function ()
                {
                    if (ReqIssuer.readyState == 4 && ReqIssuer.status == 200)
                    {
                        document.getElementById('cert_Issuer').value = ReqIssuer.responseText;
                    }
                }
                ReqIssuer.open("POST", domain + "getCertIssuer", true);
                ReqIssuer.send();

                //get valid from
                var ReqValidDate;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqValidDate = new XMLHttpRequest();
                } else
                {// code for IE6, IE5
                    ReqValidDate = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqValidDate.onreadystatechange = function ()
                {
                    if (ReqValidDate.readyState == 4 && ReqValidDate.status == 200)
                    {
                        document.getElementById('cert_ValidFrom').value = ReqValidDate.responseText;
                    }
                }
                ReqValidDate.open("POST", domain + "getCertValidDate", true);
                ReqValidDate.send();

                //get expire date
                var ReqExpireDate;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqExpireDate = new XMLHttpRequest();
                } else
                {// code for IE6, IE5
                    ReqExpireDate = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqExpireDate.onreadystatechange = function ()
                {
                    if (ReqExpireDate.readyState == 4 && ReqExpireDate.status == 200)
                    {
                        document.getElementById('cert_ValidTo').value = ReqExpireDate.responseText;
                        process = false;
                    }
                }
                ReqExpireDate.open("POST", domain + "getCertExpireDate", true);
                ReqExpireDate.send();

                //get expire date
                var ReqThumprint;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqThumprint = new XMLHttpRequest();
                } else
                {// code for IE6, IE5
                    ReqThumprint = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqThumprint.onreadystatechange = function ()
                {
                    if (ReqThumprint.readyState == 4 && ReqThumprint.status == 200)
                    {
                        document.getElementById('cert_Thumprint').value = ReqThumprint.responseText;
                        process = false;
                    }
                }
                ReqThumprint.open("POST", domain + "getThumprint", true);
                ReqThumprint.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                ReqThumprint.send("sessionID=" + hSession);
            }
        }
    }
    ReqCert.open("POST", domain + "getSignCert", true);
    ReqCert.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    ReqCert.send("sessionID=" + hSession);
}


function signHash()
{
    if (process == true)
        return;
    var response = "";
    var text = document.getElementById('dcsigner_HashData').value;
    if (text == "")
    {
        alert("Vui long nhap van ban de ky");
        return;
    }
    var xmlhttp;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else
    {// code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = function ()
    {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
        {
            document.getElementById('dcsigner_result').value = xmlhttp.responseText;
            //get info of certificate
            if (document.getElementById('dcsigner_result').value == "")
            {
                //get infomation error
                var ReqLastErr;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqLastErr = new XMLHttpRequest();
                } else
                {// code for IE6, IE5
                    ReqLastErr = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqLastErr.onreadystatechange = function ()
                {
                    if (ReqLastErr.readyState == 4 && ReqLastErr.status == 200)
                    {
                        //alert("Error code = " +ReqLastErr.responseText);
                        showErrMsg(ReqLastErr.responseText);
                        process = false;
                    }
                }
                ReqLastErr.open("POST", domain + "getLastErr", true);
                ReqLastErr.send();
            }
        }
    }
    //xmlhttp.open("POST",domain + "signHash",true);
    xmlhttp.open("POST", domain + "DCSigner", true);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.send("sessionID=" + hSession + "&HashVal=" + text + "&HashOpt=0");

}

function signHashPKCS7()
{
    if (process == true)
        return;
    var response = "";
    var text = document.getElementById('sign_Data').value;
    if (text == "")
    {
        alert("Vui long nhap van ban de ky");
        return;
    }
    var xmlhttp;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else
    {// code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = function ()
    {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
        {
            document.getElementById('sign_Signature').value = xmlhttp.responseText;
            //get info of certificate
            if (document.getElementById('sign_Signature').value == "")
            {
                document.getElementById('cert_rawData').value = "";
                document.getElementById('cert_SNB').value = "";
                document.getElementById('cert_CN').value = "";
                document.getElementById('cert_DN').value = "";
                document.getElementById('cert_Issuer').value = "";
                document.getElementById('cert_ValidFrom').value = "";
                document.getElementById('cert_ValidTo').value = "";
                //get infomation error
                var ReqLastErr;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqLastErr = new XMLHttpRequest();
                } else
                {// code for IE6, IE5
                    ReqLastErr = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqLastErr.onreadystatechange = function ()
                {
                    if (ReqLastErr.readyState == 4 && ReqLastErr.status == 200)
                    {
                        //alert("Error code = " +ReqLastErr.responseText);
                        showErrMsg(ReqLastErr.responseText);
                        process = false;
                    }
                }
                ReqLastErr.open("POST", domain + "getLastErr", true);
                ReqLastErr.send();
            }
        }
    }
    xmlhttp.open("POST", domain + "signPKCS7Hash", true);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.send("sessionID=" + hSession + "&HashVal=" + text + "&HashOpt=0");

}

function signData()
{
    if (process == true)
        return;
    var text = document.getElementById('sign_Data').value;
    if (text == "")
    {
        alert("Vui long nhap van ban de ky");
        return;
    }
    text = Base64.encode(text);

    var xmlhttp;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else
    {// code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = function ()
    {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
        {
            document.getElementById('sign_Signature').value = xmlhttp.responseText;
            if (document.getElementById('sign_Signature').value == "")
            {
                //get infomation error
                var ReqLastErr;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqLastErr = new XMLHttpRequest();
                } else
                {// code for IE6, IE5
                    ReqLastErr = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqLastErr.onreadystatechange = function ()
                {
                    if (ReqLastErr.readyState == 4 && ReqLastErr.status == 200)
                    {
                        //alert("Error code = " +ReqLastErr.responseText);
                        showErrMsg(ReqLastErr.responseText);
                        process = false;
                    }
                }
                ReqLastErr.open("POST", domain + "getLastErr", true);
                ReqLastErr.send();
            } else
                process = false;
        } else
            process = true;
    }
    xmlhttp.open("POST", domain + "Sign", true);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.send("sessionID=" + hSession + "&inData=" + text);
}
function verifySignature()
{
    if (process == true)
        return;
    var msg = document.getElementById('sign_Data').value;
    var signature = document.getElementById('sign_Signature').value;
    if (signature == "")
    {
        document.getElementById('verify_Signature').value = "";
        alert("Vui long nhap chu ky");
        return;
    }
    msg = Base64.encode(msg);
    var xmlhttp;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else
    {// code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = function ()
    {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
        {
            document.getElementById('verify_Signature').value = xmlhttp.responseText;
            if (document.getElementById('verify_Signature').value == "")
            {
                //get infomation error
                var ReqLastErr;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqLastErr = new XMLHttpRequest();
                } else
                {// code for IE6, IE5
                    ReqLastErr = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqLastErr.onreadystatechange = function ()
                {
                    if (ReqLastErr.readyState == 4 && ReqLastErr.status == 200)
                    {
                        //alert("Error code = " +ReqLastErr.responseText);
                        showErrMsg(ReqLastErr.responseText);
                        process = false;
                    }
                }
                ReqLastErr.open("POST", domain + "getLastErr", true);
                ReqLastErr.send();
            } else
                process = false;
        } else
            process = true;
    }
    xmlhttp.open("POST", domain + "Verify", true);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.send("sessionID=" + hSession + "&signature=" + signature + "&inData=" + msg);
}
