var hSession = "";
function initPlugin()
{
    var xmlhttp;
    var response = "";
    if (window.XMLHttpRequest)
    {
        xmlhttp = new XMLHttpRequest();
    }
    else
    {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = function ()
    {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
        {
            response = xmlhttp.responseText;
            if (response != "")
            {
                hSession = response;
                return;
            }
            if (response == "")
            {
                window.open('./SignPluginV3_Installer.exe');
                alert("Vui long cai dat SignPlugin va bam f5 de tiep tuc");
                return;
            }
        }
    }
    xmlhttp.open("POST", "http://localhost:14004/getSession", true);
    xmlhttp.send();
}
function getCertifcate()
{
    var ReqCert;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        ReqCert = new XMLHttpRequest();
    }
    else
    {// code for IE6, IE5
        ReqCert = new ActiveXObject("Microsoft.XMLHTTP");
    }
    ReqCert.onreadystatechange = function ()
    {
        if (ReqCert.readyState == 4 && ReqCert.status == 200)
        {
            document.getElementById('idCertificateToken').value = ReqCert.responseText;
            //get info of certificate
            if (document.getElementById('idCertificateToken').value == "")
            {
                var ReqLastErr;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqLastErr = new XMLHttpRequest();
                }
                else
                {// code for IE6, IE5
                    ReqLastErr = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqLastErr.onreadystatechange = function ()
                {
                    if (ReqLastErr.readyState == 4 && ReqLastErr.status == 200)
                    {
                        alert("Error code = " + ReqLastErr.responseText);
                    }
                }
                ReqLastErr.open("POST", "http://localhost:14004/getLastErr", true);
                ReqLastErr.send();
            }
            else
            {
                alert(document.getElementById('idCertificateToken').value);
            }
        }
    }
    ReqCert.open("POST", "http://localhost:14004/getCertificate", true);
    ReqCert.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    ReqCert.send("sessionID=" + hSession);


}
function getCertifcateBackUp19()
{
    var ReqCert;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        ReqCert = new XMLHttpRequest();
    }
    else
    {// code for IE6, IE5
        ReqCert = new ActiveXObject("Microsoft.XMLHTTP");
    }
    ReqCert.onreadystatechange = function ()
    {
        if (ReqCert.readyState == 4 && ReqCert.status == 200)
        {
            document.getElementById('idCertificateToken').value = ReqCert.responseText;
            alert(ReqCert.responseText);
            //get info of certificate
            if (document.getElementById('idCertificateTokens').value == "")
            {
                document.getElementById('cert_SNB').value = "";
                document.getElementById('cert_CN').value = "";
                document.getElementById('cert_DN').value = "";
                document.getElementById('cert_Issuer').value = "";
                document.getElementById('cert_ValidFrom').value = "";
                document.getElementById('cert_ValidTo').value = "";
                //get infomation error
                //get serial number
                var ReqLastErr;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqLastErr = new XMLHttpRequest();
                }
                else
                {// code for IE6, IE5
                    ReqLastErr = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqLastErr.onreadystatechange = function ()
                {
                    if (ReqLastErr.readyState == 4 && ReqLastErr.status == 200)
                    {
                        alert("Error code = " + ReqLastErr.responseText);
                    }
                }
                ReqLastErr.open("POST", "http://localhost:14004/getLastErr", true);
                ReqLastErr.send();
            }
            else
            {
                //get serial number
                var ReqSNB;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqSNB = new XMLHttpRequest();
                }
                else
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
                ReqSNB.open("POST", "http://localhost:14004/getCertSNB", true);
                ReqSNB.send();

                //get CN
                var ReqCN;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqCN = new XMLHttpRequest();
                }
                else
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
                ReqCN.open("POST", "http://localhost:14004/getCertCN", true);
                ReqCN.send();

                //get DN
                var ReqDN;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqDN = new XMLHttpRequest();
                }
                else
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
                ReqDN.open("POST", "http://localhost:14004/getCertDN", true);
                ReqDN.send();

                //get issuer
                var ReqIssuer;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqIssuer = new XMLHttpRequest();
                }
                else
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
                ReqIssuer.open("POST", "http://localhost:14004/getCertIssuer", true);
                ReqIssuer.send();

                //get valid from
                var ReqValidDate;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqValidDate = new XMLHttpRequest();
                }
                else
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
                ReqValidDate.open("POST", "http://localhost:14004/getCertValidDate", true);
                ReqValidDate.send();

                //get expire date
                var ReqExpireDate;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqExpireDate = new XMLHttpRequest();
                }
                else
                {// code for IE6, IE5
                    ReqExpireDate = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqExpireDate.onreadystatechange = function ()
                {
                    if (ReqExpireDate.readyState == 4 && ReqExpireDate.status == 200)
                    {
                        document.getElementById('cert_ValidTo').value = ReqExpireDate.responseText;
                    }
                }
                ReqExpireDate.open("POST", "http://localhost:14004/getCertExpireDate", true);
                ReqExpireDate.send();
            }
        }
    }
    ReqCert.open("POST", "http://localhost:14004/getCertificate", true);
    ReqCert.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    ReqCert.send("sessionID=" + hSession);


}
function getSignCert()
{
    var ReqCert;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        ReqCert = new XMLHttpRequest();
    }
    else
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
                }
                else
                {// code for IE6, IE5
                    ReqLastErr = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqLastErr.onreadystatechange = function ()
                {
                    if (ReqLastErr.readyState == 4 && ReqLastErr.status == 200)
                    {
                        alert("Error code = " + ReqLastErr.responseText);
                    }
                }
                ReqLastErr.open("POST", "http://localhost:14004/getLastErr", true);
                ReqLastErr.send();
            }
            else
            {
                //get serial number
                var ReqSNB;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqSNB = new XMLHttpRequest();
                }
                else
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
                ReqSNB.open("POST", "http://localhost:14004/getCertSNB", true);
                ReqSNB.send();

                //get CN
                var ReqCN;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqCN = new XMLHttpRequest();
                }
                else
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
                ReqCN.open("POST", "http://localhost:14004/getCertCN", true);
                ReqCN.send();

                //get DN
                var ReqDN;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqDN = new XMLHttpRequest();
                }
                else
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
                ReqDN.open("POST", "http://localhost:14004/getCertDN", true);
                ReqDN.send();

                //get issuer
                var ReqIssuer;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqIssuer = new XMLHttpRequest();
                }
                else
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
                ReqIssuer.open("POST", "http://localhost:14004/getCertIssuer", true);
                ReqIssuer.send();

                //get valid from
                var ReqValidDate;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqValidDate = new XMLHttpRequest();
                }
                else
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
                ReqValidDate.open("POST", "http://localhost:14004/getCertValidDate", true);
                ReqValidDate.send();

                //get expire date
                var ReqExpireDate;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqExpireDate = new XMLHttpRequest();
                }
                else
                {// code for IE6, IE5
                    ReqExpireDate = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqExpireDate.onreadystatechange = function ()
                {
                    if (ReqExpireDate.readyState == 4 && ReqExpireDate.status == 200)
                    {
                        document.getElementById('cert_ValidTo').value = ReqExpireDate.responseText;
                    }
                }
                ReqExpireDate.open("POST", "http://localhost:14004/getCertExpireDate", true);
                ReqExpireDate.send();
            }
        }
    }
    ReqCert.open("POST", "http://localhost:14004/getSignCert", true);
    ReqCert.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    ReqCert.send("sessionID=" + hSession);
}
function signHash()
{
    var response = "";
    var text = document.getElementById('sign_Data').value;
    if (text == "")
    {
        alert("Vui long nhap van ban de ky");
        return;
    }
    var ReqCert;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        ReqCert = new XMLHttpRequest();
    }
    else
    {// code for IE6, IE5
        ReqCert = new ActiveXObject("Microsoft.XMLHTTP");
    }
    ReqCert.onreadystatechange = function ()
    {
        if (ReqCert.readyState == 4 && ReqCert.status == 200)
        {
            response = ReqCert.responseText;
            //get info of certificate
            if (response == "")
            {
                //get infomation error
                var ReqLastErr;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqLastErr = new XMLHttpRequest();
                }
                else
                {// code for IE6, IE5
                    ReqLastErr = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqLastErr.onreadystatechange = function ()
                {
                    if (ReqLastErr.readyState == 4 && ReqLastErr.status == 200)
                    {
                        alert("Error code = " + ReqLastErr.responseText);
                    }
                }
                ReqLastErr.open("POST", "http://localhost:14004/getLastErr", true);
                ReqLastErr.send();
            }
            else
            {
                var xmlhttp;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    xmlhttp = new XMLHttpRequest();
                }
                else
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
                            }
                            else
                            {// code for IE6, IE5
                                ReqLastErr = new ActiveXObject("Microsoft.XMLHTTP");
                            }
                            ReqLastErr.onreadystatechange = function ()
                            {
                                if (ReqLastErr.readyState == 4 && ReqLastErr.status == 200)
                                {
                                    alert("Error code = " + ReqLastErr.responseText);
                                }
                            }
                            ReqLastErr.open("POST", "http://localhost:14004/getLastErr", true);
                            ReqLastErr.send();
                        }
                        else
                        {
                            getSignCert();
                        }
                    }
                }
                xmlhttp.open("POST", "http://localhost:14004/signHash", true);
                xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                xmlhttp.send("sessionID=" + hSession + "&HashVal=" + text + "&HashOpt=0");
            }
        }
    }
    ReqCert.open("POST", "http://localhost:14004/getCertificate", true);
    ReqCert.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    ReqCert.send("sessionID=" + hSession);

}
var Certificate;
var Signature;
function getCertifcateHome(id)
{
    var ReqCert;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        ReqCert = new XMLHttpRequest();
    }
    else
    {// code for IE6, IE5
        ReqCert = new ActiveXObject("Microsoft.XMLHTTP");
    }
    ReqCert.onreadystatechange = function ()
    {
        if (ReqCert.readyState == 4 && ReqCert.status == 200)
        {
            Certificate = ReqCert.responseText;
            if (Certificate === "")
            {
                var ReqLastErr;
                if (window.XMLHttpRequest)
                {
                    ReqLastErr = new XMLHttpRequest();
                }
                else
                {
                    ReqLastErr = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqLastErr.onreadystatechange = function ()
                {
                    if (ReqLastErr.readyState === 4 && ReqLastErr.status === 200)
                    {
                        if (ReqLastErr.responseText === "100100")
                        {
                            alert("Lỗi: chọn chứng thư số không thành công.");
                        }
                        else if (ReqLastErr.responseText === "100201")
                        {
                            alert("Lỗi: Không tìm thấy chứng thư số.");
                        }
                        else if (ReqLastErr.responseText === "100103")
                        {
                            alert("Lỗi: Session không hợp lệ.");
                        }
                        else if (ReqLastErr.responseText === "100101")
                        {
                            alert("Lỗi Plugin.");
                        }
                        else if (ReqLastErr.responseText === "100202")
                        {
                            alert("Lỗi: Chứng thư số không hợp lệ.");
                        }
                        else
                        {
                            alert("Mã lỗi: " + ReqLastErr.responseText);
                        }
                    }
                }
                ReqLastErr.open("POST", "http://localhost:14004/getLastErr", true);
                ReqLastErr.send();
            }
            else
            {
                addCertificate(id);
            }
        }
    }
    ReqCert.open("POST", "http://localhost:14004/getCertificate", true);
    ReqCert.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    ReqCert.send("sessionID=" + hSession);


}

function getSignCertHome()
{
    var ReqCert;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        ReqCert = new XMLHttpRequest();
    }
    else
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
                var ReqLastErr;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqLastErr = new XMLHttpRequest();
                }
                else
                {// code for IE6, IE5
                    ReqLastErr = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqLastErr.onreadystatechange = function ()
                {
                    if (ReqLastErr.readyState == 4 && ReqLastErr.status == 200)
                    {
                        alert("Error code = " + ReqLastErr.responseText);
                    }
                }
                ReqLastErr.open("POST", "http://localhost:14004/getLastErr", true);
                ReqLastErr.send();
            }
        }
    }
    ReqCert.open("POST", "http://localhost:14004/getSignCert", true);
    ReqCert.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    ReqCert.send("sessionID=" + hSession);
}
function signHashHome(text)
{
    var response = "";
    //var text = document.getElementById('sign_Data').value;
    if (text == "")
    {
        alert("Vui long nhap van ban de ky");
        return;
    }
    var ReqCert;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        ReqCert = new XMLHttpRequest();
    }
    else
    {// code for IE6, IE5
        ReqCert = new ActiveXObject("Microsoft.XMLHTTP");
    }
    ReqCert.onreadystatechange = function ()
    {
        if (ReqCert.readyState == 4 && ReqCert.status == 200)
        {
            response = ReqCert.responseText;
            //get info of certificate
            if (response == "")
            {
                //get infomation error
                var ReqLastErr;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    ReqLastErr = new XMLHttpRequest();
                }
                else
                {// code for IE6, IE5
                    ReqLastErr = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ReqLastErr.onreadystatechange = function ()
                {
                    if (ReqLastErr.readyState == 4 && ReqLastErr.status == 200)
                    {
                        alert("Error code = " + ReqLastErr.responseText);
                    }
                }
                ReqLastErr.open("POST", "http://localhost:14004/getLastErr", true);
                ReqLastErr.send();
            }
            else
            {
                Certificate = response;
                var xmlhttp;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    xmlhttp = new XMLHttpRequest();
                }
                else
                {// code for IE6, IE5
                    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                xmlhttp.onreadystatechange = function ()
                {
                    if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
                    {
                        //document.getElementById('sign_Signature').value = xmlhttp.responseText;
                        Signature = xmlhttp.responseText;
                        if (Signature == "")
                        {
                            var ReqLastErr;
                            if (window.XMLHttpRequest)
                            {// code for IE7+, Firefox, Chrome, Opera, Safari
                                ReqLastErr = new XMLHttpRequest();
                            }
                            else
                            {// code for IE6, IE5
                                ReqLastErr = new ActiveXObject("Microsoft.XMLHTTP");
                            }
                            ReqLastErr.onreadystatechange = function ()
                            {
                                if (ReqLastErr.readyState == 4 && ReqLastErr.status == 200)
                                {
                                    alert("Error code = " + ReqLastErr.responseText);
                                }
                            }
                            ReqLastErr.open("POST", "http://localhost:14004/getLastErr", true);
                            ReqLastErr.send();
                        }
                        else
                        {
                            aaaa2();
                        }
                    }
                }
                xmlhttp.open("POST", "http://localhost:14004/signHash", true);
                xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                xmlhttp.send("sessionID=" + hSession + "&HashVal=" + text + "&HashOpt=0");
            }
        }
    }
    ReqCert.open("POST", "http://localhost:14004/getCertificate", true);
    ReqCert.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    ReqCert.send("sessionID=" + hSession);

}
