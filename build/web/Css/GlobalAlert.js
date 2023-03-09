/* global clipboardData */
function parseDate(str) {
    var mdy = str.split('/');
    var mdy2 = mdy[2] + "/" + mdy[1] + "/" + mdy[0];
    return new Date(mdy2);
}
function trimAllString(s)
{
    return s.replace(/ /g, "");
}
function trim(s)
{
    return s.replace(/^s*/, "").replace(/s*$/, "");
}
function sSpace(str)
{
    return str.replace(/^\s+|\s+$/g, '');
}
function find_occurences(str, char_to_count) {
    return str.split(char_to_count).length - 1;
}
// PASSWORD_HIDE
var CharactersPasswordHide = "********";
/// SYSTEM Bootstrap /////
var NameRootProject = "/" + getRootWebSitePath();
/////Mobile-ID////
//var LinkLogoPage = "Images/Logo_MobileID.png";
//var LinkBannerPage = "Images/Banner-Company.png";
//var LinkIconPageMiltiple = "Images/icon_trusted.png";
/////EFY////
//var LinkLogoPage = "Images/LOGO_EFY-CA.png";
//var LinkBannerPage = "Images/Banner-EFY.png";
//var LinkIconPageMiltiple = "Images/icon_efy.png";
////FEITAN////
//var LinkLogoPage = "Images/logo_feitian.png";
//var LinkBannerPage = "Images/Banner-Company.png";
//var LinkIconPageMiltiple = "Images/favicon.ico";
////MISA////
//var LinkLogoPage = "Images/logo_MISA.png";
//var LinkBannerPage = "Images/Banner-Company.png";
//var LinkIconPageMiltiple = "Images/favicon_MISA.ico";
////TRUSTCA////
//var LinkLogoPage = "Images/Logo_TrustCA.png";
//var LinkBannerPage = "Images/Banner-Company.png";
//var LinkIconPageMiltiple = "Images/favicon_TrustCA.ico";
////NEWTELCA////
//var LinkLogoPage = "Images/Logo_NewTelCA.png";
//var LinkBannerPage = "Images/Banner-Company.png";
//var LinkIconPageMiltiple = "Images/favicon_NewTelCA.ico";
////NC-CA////
//var LinkLogoPage = "Images/Logo_NCCA.jpg";
//var LinkBannerPage = "Images/Banner-Company.png";
//var LinkIconPageMiltiple = "Images/favicon_NCCA.ico";
////CMC-CA////
//var LinkLogoPage = "Images/LOGO_CMC.png";
//var LinkBannerPage = "Images/Banner-Company.png";
//var LinkIconPageMiltiple = "Images/favicon_CMC.png";
////BCY////
//var LinkLogoPage = "Images/Logo-BCY.png";
//var LinkBannerPage = "Images/Banner-Company.png";
//var LinkIconPageMiltiple = "Images/favicons-BCY.png";
////FPT-CA////
//var LinkLogoPage = "Images/FPT-CA.png";
//var LinkBannerPage = "Images/Banner-Company.png";
//var LinkIconPageMiltiple = "Images/favicon_FPT.png";
//// EASY ////
//var LinkLogoPage = "Images/LOGO_EASY.png";
//var LinkBannerPage = "Images/Banner-Company.png";
//var LinkIconPageMiltiple = "Images/favicons-EASY.png";
//// LCS ////
//var LinkLogoPage = "Images/LOGO_LCS.png";
//var LinkBannerPage = "Images/Banner-Company.png";
//var LinkIconPageMiltiple = "Images/favicons-LCS.ico";
////
var TitleHomeMain = "Back-Office";
var LinkErrorPage = NameRootProject + "/error.jsp";
var LinkError01Page = NameRootProject + "/error_01.jsp";
var LinkLoginPageNoSess = NameRootProject + "/Login.jsp";
var LinkHomePageNoLink = NameRootProject + "/Admin/Home.jsp";
//function changeFavicon(src)
//{
//    if (src !== "")
//        LinkIconPageMiltiple = src + LinkIconPageMiltiple;
//    var link = document.createElement('link'),
//            oldLink = document.getElementById('dynamic-favicon');
//    link.id = 'dynamic-favicon';
//    link.rel = 'icon';
//    link.href = LinkIconPageMiltiple;
//    if (oldLink) {
//        document.head.removeChild(oldLink);
//    }
//    document.head.appendChild(link);
//}
function getRootWebSitePath()
{
    var _location = document.location.toString();
    var applicationNameIndex = _location.indexOf('/', _location.indexOf('://') + 3);
    var applicationName = _location.substring(0, applicationNameIndex) + '/';
    var webFolderIndex = _location.indexOf('/', _location.indexOf(applicationName) + applicationName.length);
    var webFolderFullPath = _location.substring(0, webFolderIndex);
    return webFolderFullPath.replace(applicationName, "");
}
function funSuccAlert(text, url)
{
    swal({
        title: "", text: text, html: true, imageUrl: "../Images/success.png", imageSize: "45x45",
        allowOutsideClick: false, allowEscapeKey: false},
    function () {
        window.location = url;
    });
}
function funLocationBack(url)
{
    window.location = url;
}
function funSuccLocalAlert(text)
{
    swal({
        title: "", text: text, imageUrl: "../Images/success.png", imageSize: "45x45",
        allowOutsideClick: false, allowEscapeKey: false},
    function () {
        location.reload();
    });
}
function funSuccNoLoad(text)
{
    swal({
        title: "", text: text, imageUrl: "../Images/success.png", imageSize: "45x45"
    });
}
function funErrorAlert(text)
{
    swal({
        title: "", text: text, imageUrl: "../Images/icon_error.png", imageSize: "45x45"
    });
}
function funErrorAlertLocal(text)
{
    swal({
        title: "", text: text, imageUrl: "../Images/icon_error.png", imageSize: "45x45",
        allowOutsideClick: false, allowEscapeKey: false},
    function () {
        location.reload();
    });
}
function funErrorLoginAlert(text)
{
    swal({
        title: "", text: text, imageUrl: "Images/icon_error.png", imageSize: "45x45"
    });
}
function funCsrfAlert()
{
    swal({title: "", text: CSRF_Mess, imageUrl: "../Images/icon_error.png", imageSize: "45x45",
        allowOutsideClick: false, allowEscapeKey: false},
    function () {
        window.location = "../Modules/Logout.jsp";
    });
}
function confirmDelete(sText, funDelete)
{
    swal({
        title: "",
        text: "",
        imageUrl: "../Images/icon_warning.png",
        imageSize: "45x45",
        showCancelButton: true,
        closeOnConfirm: false,
        cancelButtonText: "Cancel",
        cancelButtonColor: "#199DC0"
    },
    function () {
        funDelete;
    });
}
function RedirectPageError()
{
    var link = document.createElement('a');
    link.setAttribute('href', document.location.href);
    window.location = link.protocol + "//" + document.location.host + LinkErrorPage;
}
function RedirectPageLoginNoSess(sText)
{
    swal({title: "", text: sText, imageUrl: "../Images/icon_error.png", imageSize: "45x45",
        allowOutsideClick: false, allowEscapeKey: false},
    function () {
        window.location = "../Modules/Logout.jsp";
    });
}
function RedirectPageHomeNoLink(sText)
{
    var link = document.createElement('a');
    link.setAttribute('href', document.location.href);
    swal({title: "", text: sText, imageUrl: "../Images/icon_error.png", imageSize: "45x45",
        allowOutsideClick: false, allowEscapeKey: false},
    function () {
        window.location = link.protocol + "//" + document.location.host + LinkHomePageNoLink;
    });
}
function copyToClipboard(text) {
    if (window.clipboardData && window.clipboardData.setData) {
        return clipboardData.setData("Text", text);

    } else if (document.queryCommandSupported && document.queryCommandSupported("copy")) {
        var textarea = document.createElement("textarea");
        textarea.textContent = text;
        textarea.style.position = "fixed";
        document.body.appendChild(textarea);
        textarea.select();
        try {
            return document.execCommand("copy");
        } catch (ex) {
            console.warn("Copy to clipboard failed.", ex);
            return false;
        } finally {
            document.body.removeChild(textarea);
            funSuccNoLoad(global_succ_copy_all);
        }
    }
}
function funSuccAlertDemo(text, url)
{
    swal({
        title: "", text: text,
        html: true,
        width: 350,
        padding: 20,
        imageUrl: "../Images/success.png", imageSize: "45x45",
        allowOutsideClick: false, allowEscapeKey: false},
    function () {
        window.location = url;
    });
}

function FormCheckMobiPhoneSearch(sValue)
{
    if (sValue !== "")
    {
        var regexPhone = /^[0-9]+$/;
        if (!sValue.match(regexPhone))
        {
            return false;
        }
    }
    return true;
}
function JSCheckMobiPhoneSearchForm(sValue)
{
    var regexPhone = /^[0-9]+$/;
    var sCheck = "1";
    if (sValue !== "")
    {
        if (sValue.indexOf("+") !== -1) {
            if (sValue.substring(0, 1) !== "+")
            {
                sCheck = "0";
            }
            else
            {
                var sSubValue = sValue.substring(1, sValue.length);
                if (sSubValue.indexOf("+") !== -1) {
                    sCheck = "0";
                }
            }
        }
        else
        {
            if (!sValue.match(regexPhone))
            {
                sCheck = "0";
            }
        }
        if (sCheck === "1")
        {
            var sSubValue = sValue.substring(1, sValue.length);
            if (sSubValue !== "")
            {
                if (!sSubValue.match(regexPhone))
                {
                    sCheck = "0";
                }
            }
        }
        if (sCheck === "0")
        {
            return false;
        }
    }
    return true;
}
function FormCheckMobiPhoneAction(sValue)
{
    if (sValue !== "")
    {
        var regexPhone = /^[0-9]+$/;
        if (!sValue.match(regexPhone))
        {
            return false;
        }
    }
    return true;
}
function JSCheckNumericField(digits)
{
    if (sSpace(digits) !== "")
    {
        var regexPhone = /^[0-9]+$/;
        if (!sSpace(digits).match(regexPhone))
        {
            return false;
        }
    }
    return true;
}
function JSCheckDateDDMMYYYY(digits)
{
    if (sSpace(digits) !== "")
    {
        var resDate = /^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
        if (!sSpace(digits).match(resDate))
        {
            return false;
        }
    }
    return true;
}
function JSCheckDateLessDateCurrent(digits)
{
    if (sSpace(digits) !== "")
    {
        var currentdate = new Date();
        var datetime = currentdate.getDay() + "/" + currentdate.getMonth() + "/" + currentdate.getFullYear();
        if (parseDate(sSpace(digits)).getTime() < parseDate(datetime).getTime())
        {
            return false;
        }
    }
    return true;
}
function JSCheckDateDDMMYYYYSlash(sValue)
{
    var aDate = moment(sValue, 'DD/MM/YYYY', true);
    var isValid = aDate.isValid();
    return isValid;
}
function JSCheckUnicode(digits)
{
    if (sSpace(digits) !== "")
    {
        var output = "0";
        for (var i = 0; i < digits.length; i++) {
            if (digits.charCodeAt(i) > 127)
            {
                output = "1";
            }
        }
        if (output === "1")
        {
            return true;
        }
    }
    return false;
}
function JSCheckSpaceField(digits)
{
    if (sSpace(digits) !== "")
    {
        if (sSpace(digits).split(' ').length > 1)
        {
            return true;
        }
    }
    return false;
}
function JSCheckFormatURL(digits)
{
    var regexUrl = /(http|https):\/\/(\w+:{0,1}\w*)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%!\-\/]))?/;
    if (!regexUrl.test(sSpace(digits)))
    {
        return false;
    }
    return true;
}
function JSCheckFormatPhone(digits)
{
    var phoneRe = /^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/im;
    if (!phoneRe.test(sSpace(digits)))
    {
        return false;
    }
    return true;
}
function JSCheckFormatPhoneNew_Edit(digits)
{
    if (sSpace(digits.val()) !== "")
    {
//        var regexPhone = /^[\+|0]?( |[1-9])+( |[0-9]{4,20})+$/im;
        var regexPhone = /^[0-9]+$/;
        if (!sSpace(digits.val()).match(regexPhone))
        {
            return false;
        }
    }
    return true;
}
function JSCheckFormatPhoneNew_EditOne(digits)
{
    var vValue = trimAllString(digits.val());
    if (vValue !== "")
    {
        var regexPhone = /^[\+|0-1][0-9]+$/;
        if (!vValue.match(regexPhone))
        {
            return false;
        }
        if(vValue.length < 4 || vValue.length > 20)
        {
            return false;
        }
    }
    return true;
}
function getCheckStatusPhone(digits) {
    var CChat = $.ajax({
        type: "POST",
        url: "../SomeCommon",
        data: { idParam: 'checkphonevalid',  phoneValue: digits}
    });
    return CChat;
}
function getCheckStatusEmail(digits) {
    var CChat = $.ajax({
        type: "POST",
        url: "../SomeCommon",
        data: { idParam: 'checkemailvalid',  emailValue: digits}
    });
    return CChat;
}
function FormCheckEmailSearchHand(regex, sValue)
{
    if (sSpace(sValue) !== "")
    {
        var filter = new RegExp(regex);
        if (!filter.test(sSpace(sValue)))
        {
            return false;
        }
    }
    return true;
}
function FormCheckPhoneHand(regex, sValue)
{
    if (sSpace(sValue.val()) !== "")
    {
        var intStart = regex.lastIndexOf('{');
        var intEnd = regex.lastIndexOf('}');
        var regex1 = regex.substring(intStart+1, intEnd);
        var intMaxChar = 20;
        if(regex1.indexOf(',') !== -1)
        {
            intMaxChar = regex1.split(',')[1];
            intMaxChar = regex1.split(',')[1];
        }
        var filter = new RegExp(regex);
        if (!filter.test(sSpace(sValue.val())))
        {
            return false;
        }
        if(sSpace(sValue.val()).length >= intMaxChar)
        {
            return false;
        }
    }
    return true;
}
function FormCheckPhoneHandProfile(regex, sValue)
{
    if (sValue !== "")
    {
        var intStart = regex.lastIndexOf('{');
        var intEnd = regex.lastIndexOf('}');
        var regex1 = regex.substring(intStart+1, intEnd);
        var intMaxChar = 20;
        if(regex1.indexOf(',') !== -1)
        {
            intMaxChar = regex1.split(',')[1];
            intMaxChar = regex1.split(',')[1];
        }
        var filter = new RegExp(regex);
        if (!filter.test(sValue))
        {
            return false;
        }
        if(sValue.length >= intMaxChar)
        {
            return false;
        }
    }
    return true;
}
//function JSCheckFormatPhoneNewPolicy(sPhone) {
//    var vValue = trimAllString(sPhone);
//    var cChatStatus = getCChatStatus(vValue);
//    cChatStatus.done(function(msg) {
//        var s = sSpace(msg).split('#');
//        if(s[0] !== "0")
//        {
//            funErrorAlert(global_req_phone_format);
//            return false;
//        }
//    });
//}
//function getCChatStatus(digits) {
//    var CChat = $.ajax({
//        type: "POST",
//        url: "../SomeCommon",
//        data: { idParam: 'checkphonevalid',  phoneValue: digits}
//    });
//    return CChat;
//}
//function JSCheckFormatPhoneNewPolicy(digits)
//{
//    var vValue = trimAllString(digits);
//    alert(vValue);
//    if (vValue !== "")
//    {
//        var cChatStatus = getCChatStatus(vValue);
//        cChatStatus.done(function(msg) {
//            var s = sSpace(msg).split('#');
//            if(s[0] === "0")
//            {
//                return true;
//            } else {
//                return false;
//            }
//        });
//    }
//    return true;
//}  
//    var vValue = trimAllString(digits.val());
//    if (vValue !== "")
//    {
//        var cChatStatus = getCChatStatus(vValue);
//        cChatStatus.done(function(msg) {
//            var s = sSpace(msg).split('#');
//            if(s[0] === "0")
//            {
//                return true;
//            } else {
//                return false;
//            }
//        });
//    }
//    return true;

function JSCheckFormatPhoneNew_InJava_EditOne(digits)
{
    var vValue = trimAllString(digits.val());
    if (vValue !== "")
    {
        var cChatStatus = getCChatStatus(vValue);
        cChatStatus.done(function(msg) {
            var s = sSpace(msg).split('#');
            if(s[0] === "0")
            {
                return true;
            } else {
                return false;
            }
        });
    }
    return true;
}
function JSLoadPhoneNew(digits)
{
    digits.intlTelInput({
        allowExtensions: true,
        autoFormat: false,
        autoPlaceholder: false,
        defaultCountry: "auto",
        ipinfoToken: "yolo",
        nationalMode: false,
        numberType: "MOBILE",
        preventInvalidNumbers: true,
        dropdownContainer: "body",
        initialCountry: "auto",
        geoIpLookup: function (callback) {
            $.get('https://ipinfo.io', function () {
            }, "jsonp").always(function (resp) {
                var countryCode = (resp && resp.country) ? resp.country : "";
                callback(countryCode);
            });
        },
        placeholderNumberType: "MOBILE",
        preventInvalidDialCodes: true,
        formatOnDisplay: false,
        utilsScript: "../js/checkphone/utils.js"
    });
}
function JSCheckEmptyField(sValue)
{
    if(sValue === null || sValue === "null")
    {
        return false;
    } else {
        if (sSpace(sValue) === "")
        {
            return false;
        }
    }
    return true;
}
function FormCheckEmailSearch(sValue)
{
    if (sSpace(sValue) !== "")
    {
        var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        if (!filter.test(sValue))
        {
            return false;
        }
    }
    return true;
}
function ValidateIPaddressAction(ipaddress)
{
    if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(ipaddress))
    {
        return (true);
    }
    return (false);
}
function goToByScroll(id) {
    $('html,body').animate({scrollTop: $("#" + id).offset().top}, 'slow');
}

function removeOptions(selectbox)
{
    for (var i = selectbox.options.length - 1; i >= 0; i--)
    {
        selectbox.remove(i);
    }
}

function selectedOptionsCombobox(selectbox, idParamID)
{
    var soption;
    for (var j = 0; j < selectbox.options.length; j++) {
        soption = selectbox.options[j];
        if (soption.value === idParamID) {
            soption.setAttribute('selected', true);
            return;
        }
    }
}
function randomString(sValue) {
    var chars1 = "0123456789";
    var string_length1 = 8;
    var randomstring1 = '';
    for (var i = 0; i < string_length1; i++)
    {
        var rnum = Math.floor(Math.random() * chars1.length);
        randomstring1 += chars1.substring(rnum, rnum + 1);
    }
    sValue = randomstring1;
}
function onBlurToUppercase(obj)
{
    obj.value = obj.value.toUpperCase();
}
function JSCheckExtenBrowseExcel(sValue)
{
    var checkFileName = sValue.substring(sValue.lastIndexOf('.') + 1).toUpperCase();
    if (checkFileName !== "" && checkFileName !== "XLS" && checkFileName !== "XLSX")
    {
        return false;
    }
    return true;
}
function JSConvertBooleanToString(vCheck)
{
    if (vCheck === true)
    {
        return "true";
    }
    else {
        return "false";
    }
}
function JSCheckFormatPhoneNew(digits)
{
    var regex = /^[0-9]+$/;
    digits.intlTelInput({
        utilsScript: "../js/checkphone/utils.js"
    });
    if (!digits.intlTelInput("isValidNumber")) {
        return false;
    } else {
        if (!digits.val().replace("+", "").match(regex))
        {
            return false;
        }
    }
    return true;
}
function JSCheckLinkURLFormat(digits)
{
    var regexUrl = /(http|https):\/\/(\w+:{0,1}\w*)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%!\-\/]))?/;
    if (!regexUrl.test(digits))
    {
        return false;
    }
    return true;
}

function ConvertMoneySeparatorsNF(nStr, inD, outD, sep)
{
    nStr += '';
    var dpos = nStr.indexOf(inD);
    var nStrEnd = '';
    if (dpos !== -1) {
        nStrEnd = outD + nStr.substring(dpos + 1, nStr.length);
        nStr = nStr.substring(0, dpos);
    }
    var rgx = /(\d+)(\d{3})/;
    while (rgx.test(nStr)) {
        nStr = nStr.replace(rgx, '$1' + sep + '$2');
    }
    return nStr + nStrEnd;
}
function autoConvertMoney(vMoney, vMoneyID)
{
    if (vMoney !== "")
    {
        vMoney = vMoney.replace(/,/g, '');
        vMoneyID.val(ConvertMoneySeparatorsNF(vMoney, '.', '.', ','));
    } else {
        vMoneyID.val("0");
    }
}

var entitySpecial = {
    'amp': '&',
    'apos': '\'',
    '#x27': '\'',
    '#x2F': '/',
    '#39': '\'',
    '#47': '/',
    'lt': '<',
    'gt': '>',
    'nbsp': ' ',
    'quot': '"',
    'equals': '=',
    'bsol': '\\'
};

var entityDau = {
    '+': '\\+',
    '\'': '\\',
    '"': '\\"',
    ',': '\\,'
};

function decodeHTMLEntityDau(text) {
    return text.replace(/&([^;]+);/gm, function (match, entity) {
        return entityDau[entity] || match;
    });
}

function JSEncodeHTMLEntityDash(text) {
    text = text.replace(/"/g, '\\"');
    text = text.replace(/\\/g, '\\\\');
    text = text.replace(/\+/g, '\\+');
    text = text.replace(/,/g, '\\,');
    return text;
}
function JSEscapeDashDN(text) {
    text = text.replace("###", ',');
    text = text.replace("!!!", '\\');
    text = text.replace("@@@", '+');
    text = text.replace("$$$", '"');
    return text;
}
function JSCheckEqualsDN(digits)
{
    var characterRe = "=";
    if (digits.indexOf(characterRe) !== -1)
    {
        return false;
    }
    return true;
}
//var specialChars = "<>$+'\"\\=";
//var specialChars = "+\"\\=";
//var specialChars = "\"\\=";
var specialChars = "\\=";

function BlurEmailCharacter(vEmail)
{
    var sTotalEmailSub = "";
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
            sTotalEmailSub = sBeforeEmailSub + sAfterEmailSub;
        } else {
            var vEmailLengthSub = vEmail.substring(0, intEmail).length;
            var sAfterEmailSub = vEmail.substring(vEmailLengthSub, vEmail.length);
            var sBeforeEmailSub = "";
            for (var i = 0; i < vEmailLengthSub; i++)
            {
                sBeforeEmailSub += "*";
            }
            sTotalEmailSub = sBeforeEmailSub + sAfterEmailSub;
        }
    }
    return sTotalEmailSub;
}
function ConvertDateTime(vValue)
{
    var vYear = vValue.substring(0, 4);
    var vMount = vValue.substring(5, 7);
    var vDay = vValue.substring(8, 10);
    return vDay + "/" + vMount + "/" + vYear;
}

function JSCheckExtenBrowseCSR(sValue)
{
    var checkFileName = sValue.substring(sValue.lastIndexOf('.') + 1);
    if (checkFileName !== "csr" && checkFileName !== "txt" && checkFileName !== "CSR" && checkFileName !== "TXT")
    {
        return false;
    }
    return true;
}

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

function getUrlParam(parameter, defaultvalue) {
    var urlparameter = defaultvalue;
    if(window.location.href.indexOf(parameter) > -1) {
        urlparameter = getUrlVars()[parameter];
    }
    return urlparameter;
}
function autoTrimTextField(objInput, objValue)
{
    $("#"+objInput).val(sSpace(objValue));
}

function reloadJSInPage(src, checkLoad) {
    if(checkLoad === "0")
    {
        console.log("0");
    }
    if(checkLoad === "1")
    {
        console.log("1");
    }
    $('script[src="' + src + '"]').remove();
    if(checkLoad === "1")
    {
        $('<script>').attr('src', src).appendTo('head');
    }
}
function replaceCharacterDN(sValuePushDBDataLast, isSide) {
    var vars = '';
    if(sValuePushDBDataLast !== "")
    {
        vars = sValuePushDBDataLast.replace(/###/g, ',');
        if(isSide === "1")
        {
            //console.log(vars);
            vars = vars.replace(/\\/gi, '');
            //console.log(vars);
        }
    }
    return vars;
}
function getDateFooterPrint()
{
    var dateString = global_fm_report_date;
    var vDay = new Date().getDate();
    var vMount = new Date().getMonth() + 1;
    var vYear = new Date().getFullYear();
    if(vDay !== '' && vMount !== '' && vYear !== '') {
        dateString = global_fm_report_print_date;
        dateString = dateString.replace('[DD]',vDay);
        dateString = dateString.replace('[MM]',vMount);
        dateString = dateString.replace('[YYYY]',vYear);
    }
    return dateString;
}

function escapeEntities(text) {
//    text = text.replace(/"/g, '\\"');
    //text = text.replace(/</g, '&lt;');
    //text = text.replace(/>/g, '&gt;');
//    text = text.replace(/,/g, '\\,');
    return text;
}

function getUIDCertEnterprise(sPrefix, sValue) {
    if(sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST){
        sValue = JS_STR_COMPONENT_DN_VALUE_TIN + ":" + sValue;
    } else if(sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS){
        sValue = JS_STR_COMPONENT_DN_VALUE_BGC + ":" + sValue;
    } else if(sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_QD){
        sValue = JS_STR_COMPONENT_DN_VALUE_DEC + ":" + sValue;
    } else if(sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_BHXH) {
        sValue = JS_STR_COMPONENT_DN_VALUE_SIC + ":" + sValue;
    } else if(sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MDV) {
        sValue = JS_STR_COMPONENT_DN_VALUE_UNC + ":" + sValue;
    } else {
        sValue = sPrefix + sValue;
    }
    return sValue;
}
function convertPrefixEnterpriseToVN(vUID) {
    if(vUID !== "") {
        if(vUID.split(":")[0] === JS_STR_COMPONENT_DN_VALUE_TIN){
            vUID = JS_STR_COMPONENT_DN_VALUE_PREFIX_MST;
        } else if(vUID.split(":")[0] === JS_STR_COMPONENT_DN_VALUE_BGC){
            vUID = JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS;
        } else if(vUID.split(":")[0] === JS_STR_COMPONENT_DN_VALUE_DEC){
            vUID = JS_STR_COMPONENT_DN_VALUE_PREFIX_QD;
        } else if(vUID.split(":")[0] === JS_STR_COMPONENT_DN_VALUE_SIC){
            vUID = JS_STR_COMPONENT_DN_VALUE_PREFIX_BHXH;
        } else if(vUID.split(":")[0] === JS_STR_COMPONENT_DN_VALUE_UNC){
            vUID = JS_STR_COMPONENT_DN_VALUE_PREFIX_MDV;
        }
    }
    return vUID;
}
function checkUIDVNEnterprise(sPrefix, vOWNER_TYPE_ID) {
//    alert(vOWNER_TYPE_ID);
    if(vOWNER_TYPE_ID === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL) {
        return false;
    } else {
        if(sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST || sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MNS
            || sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_QD || sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_BHXH
            || sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MDV){
            return true;
        } else {
            return false;
        }
    }
    return true;
}

function getUIDCertPersonal(sPrefix, sValue) {
    if(sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND){
        sValue = JS_STR_COMPONENT_DN_VALUE_PID + ":" + sValue;
    } else if(sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC){
        sValue = JS_STR_COMPONENT_DN_VALUE_PPID + ":" + sValue;
    } else if(sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_CCCD){
        sValue = JS_STR_COMPONENT_DN_VALUE_PEID + ":" + sValue;
    } else if(sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_BHXH) {
        sValue = JS_STR_COMPONENT_DN_VALUE_SIC + ":" + sValue;
    } else if(sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST) {
        sValue = JS_STR_COMPONENT_DN_VALUE_TIN + ":" + sValue;
    } else {
        sValue = sPrefix + sValue;
    }
    return sValue;
}
function convertPrefixPersonalToVN(vUID) {
    if(vUID !== "") {
        if(vUID.split(":")[0] === JS_STR_COMPONENT_DN_VALUE_PID){
            vUID = JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND;
        } else if(vUID.split(":")[0] === JS_STR_COMPONENT_DN_VALUE_PPID){
            vUID = JS_STR_COMPONENT_DN_VALUE_PREFIX_HC;
        } else if(vUID.split(":")[0] === JS_STR_COMPONENT_DN_VALUE_PEID){
            vUID = JS_STR_COMPONENT_DN_VALUE_PREFIX_CCCD;
        } else if(vUID.split(":")[0] === JS_STR_COMPONENT_DN_VALUE_SIC){
            vUID = JS_STR_COMPONENT_DN_VALUE_PREFIX_BHXH;
        } else if(vUID.split(":")[0] === JS_STR_COMPONENT_DN_VALUE_TIN){
            vUID = JS_STR_COMPONENT_DN_VALUE_PREFIX_MST;
        }
    }
    return vUID;
}
function checkUIDVNPersonal(sPrefix) {
    if(sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_CMND || sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_HC
        || sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_CCCD || sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_BHXH
        || sPrefix === JS_STR_COMPONENT_DN_VALUE_PREFIX_MST){
        return true;
    } else {
        return false;
    }
    return true;
}

function ViewTempFile(vFILE_MANAGER_ID){
    var popupWin = window.open('CertificateFileView.jsp?idFile='+vFILE_MANAGER_ID, '_blank', 'width=900,height=750,location=no,left=200px');
    popupWin.focus();
//    window.open('CertificateFileView.jsp?idFile='+vFILE_MANAGER_ID, '_blank');
}
function ViewTempTwoParamFile(vFILE_MANAGER_ID, vFILE_NAME){
    var popupWin = window.open('CertificateFileView.jsp?idFile='+vFILE_MANAGER_ID + '&idName=' + vFILE_NAME, '_blank', 'width=900,height=750,location=no,left=200px');
    popupWin.focus();
//    window.open('CertificateFileView.jsp?idFile='+vFILE_MANAGER_ID + '&idName=' + vFILE_NAME, '_blank');
}

// yyyy-mm-dd

//function JSCheckFormatPhoneNew(digits)
//{
//    digits.intlTelInput({
//        utilsScript: "../js/checkphone/utils.js"
//    });
//    if (!digits.intlTelInput("isValidNumber")) {
//        return false;
//    }
//    return true;
//}