var sVN = "1";
var JS_LANGUAGE_TEMPLATE_UID_PREFIX="1";//1-vn; 0-en; 2-prefix MDV
var global_version_web = "(V2.230308)";
//IsWhichCA: efy-1, feitian-2, mid-3; misa-4, trust-5, newtel-6, ncca-7, cmc-8, bcy-9,
// fpt-10, easy-11, lcs-12, viettel-13, winca-14, laos-lca-15, safeca-16, laos-gov-17,
// I-CA-18, TCC-19, hilo-20, mobileCA-21, matbao-22
var IsWhichCA = "7";
var LinkLogoPage = "";
var LinkLogoMenuPage = "";
var LinkBannerPage = "";
var LinkIconPageMiltiple = "";
if(IsWhichCA === "1") {
    LinkLogoPage = "Images/LOGO_EFY-CA.png";
    LinkLogoMenuPage = "Images/LOGO_EFY-CA.png";
    LinkBannerPage = "Images/Banner-EFY.png";
    LinkIconPageMiltiple = "Images/icon_efy.png";
} else if(IsWhichCA === "2") {
    LinkLogoPage = "Images/logo_feitian.png";
    LinkLogoMenuPage = "Images/logo_feitian.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicon.ico";
} else if(IsWhichCA === "3") {
    LinkLogoPage = "Images/Logo_MobileID.png";
    LinkLogoMenuPage = "Images/Logo_MobileID.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/icon_trusted.png";
} else if(IsWhichCA === "4") {
    LinkLogoPage = "Images/logo_MISA.png";
    LinkLogoMenuPage = "Images/logo_MISA.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicon_MISA.ico";
} else if(IsWhichCA === "5") {
    LinkLogoPage = "Images/Logo_TrustCA.png";
    LinkLogoMenuPage = "Images/Logo_TrustCA.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicon_TrustCA.ico";
} else if(IsWhichCA === "6") {
    LinkLogoPage = "Images/Logo_NewTelCA.png";
    LinkLogoMenuPage = "Images/Logo_NewTelCA.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicon_NewTelCA.ico";
} else if(IsWhichCA === "7") {
    LinkLogoPage = "Images/Logo_NCCA.jpg";
    LinkLogoMenuPage = "Images/Logo_NCCA.jpg";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicon_NCCA.ico";
} else if(IsWhichCA === "8") {
    LinkLogoPage = "Images/LOGO_CMC.png";
    LinkLogoMenuPage = "Images/LOGO_CMC.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicon_CMC.png";
} else if(IsWhichCA === "9") {
    LinkLogoPage = "Images/Logo-BCY.png";
    LinkLogoMenuPage = "Images/Logo-BCY.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicons-BCY.png";
} else if(IsWhichCA === "10") {
    LinkLogoPage = "Images/FPT-CA.png";
    LinkLogoMenuPage = "Images/FPT-CA.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicon_FPT.png";
} else if(IsWhichCA === "11") {
    LinkLogoPage = "Images/LOGO_EASY.png";
    LinkLogoMenuPage = "Images/LOGO_EASY.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicons-EASY.png";
} else if(IsWhichCA === "12") {
    LinkLogoPage = "Images/LOGO_LCS.png";
    LinkLogoMenuPage = "Images/LOGO_LCS.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicons-LCS.ico";
} else if(IsWhichCA === "13") {
    LinkLogoPage = "Images/Logo_Viettel.png";
    LinkLogoMenuPage = "Images/Logo_Viettel.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicon_Viettel.ico";
} else if(IsWhichCA === "14") {
    LinkLogoPage = "Images/Logo_WinCA.png";
    LinkLogoMenuPage = "Images/Logo_WinCA.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicon_WinCA.ico";
} else if(IsWhichCA === "15") {
    LinkLogoPage = "Images/LOGO_LCA.png";
    LinkLogoMenuPage = "Images/LOGO_LCA.png";
    LinkBannerPage = "Images/BannerLAOS-LCA.png";
    LinkIconPageMiltiple = "Images/favicon_LCA.ico";
    JS_LANGUAGE_TEMPLATE_UID_PREFIX="0";// EN
//    localStorage.setItem("VN", "0");
} else if(IsWhichCA === "16") {
    LinkLogoPage = "Images/LOGO_SAFECA.png";
    LinkLogoMenuPage = "Images/LOGO_SAFECA.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicon_SAFECA.ico";
    JS_LANGUAGE_TEMPLATE_UID_PREFIX="2"; // prefix MDV
} else if(IsWhichCA === "17") {
    LinkLogoPage = "Images/LOGO-GOV.png";
    LinkLogoMenuPage = "Images/LOGO-GOV.png";
    LinkBannerPage = "Images/BannerLAOS-gov.jpg";
    LinkIconPageMiltiple = "Images/favicon_LCA.ico";
    JS_LANGUAGE_TEMPLATE_UID_PREFIX="0";// EN
//    localStorage.setItem("VN", "0");
} else if(IsWhichCA === "18") {
    LinkLogoPage = "Images/Logo_ICA.png";
    LinkLogoMenuPage = "Images/LogoMenu_ICA.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicon_ICA.ico";
//    loadCssCA18();
} else if(IsWhichCA === "19") {
    LinkLogoPage = "Images/Logo_MobileID.png";
    LinkLogoMenuPage = "Images/Logo_MobileID.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/icon_trusted.png";
} else if(IsWhichCA === "20") {
    LinkLogoPage = "Images/Logo_HiloCA.png";
    LinkLogoMenuPage = "Images/Logo_MenuHiloCA.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/icon_hiloca.ico";
} else if(IsWhichCA === "21") {
    LinkLogoPage = "Images/Logo_MobileCA.png";
    LinkLogoMenuPage = "Images/Logo_MobileCA.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicon_MobileCA.ico";
} else if(IsWhichCA === "22") {
    LinkLogoPage = "Images/Logo_MatBaoCA.jpg";
    LinkLogoMenuPage = "Images/Menu_Logo_MatBaoCA.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicon_MatBaoCA.ico";
} else {}
function loadCssCA18()
{
    console.log("loadCssCA18");
    var fileref = document.createElement("link");
    fileref.rel = "stylesheet";
    fileref.type = "text/css";
    fileref.href = "../style/custom_extend_CA18.css";
    document.getElementsByTagName("head")[0].appendChild(fileref);
}
function changeFavicon(src)
{
    if (src !== "")
        LinkIconPageMiltiple = src + LinkIconPageMiltiple;
    var link = document.createElement('link'),
            oldLink = document.getElementById('dynamic-favicon');
    link.id = 'dynamic-favicon';
    link.rel = 'icon';
    link.href = LinkIconPageMiltiple;
    if (oldLink) {
        document.head.removeChild(oldLink);
    }
    document.head.appendChild(link);
}
if (localStorage.getItem("VN") === "0")
{
    var global_title_logo = "Administration Banner";
    var global_fm_button_New = " Add ";
    var global_fm_button_add = "Apply";
    var global_fm_button_add_print = "Apply And Print";
    var global_fm_button_edit = "Apply";
    var global_fm_button_restart = "Restart Server";
    var global_fm_button_back = "Back";
    var global_fm_button_close = "Close";
    var global_fm_button_search = "Search";
    var global_fm_button_profile_pettlement = "Profile Settlement";
    var global_fm_button_get_info = "Get Info";
    var global_fm_register_note = "Note: Please double check the information before registering the certificate";
    var global_fm_button_reload = "Reload";
    var global_fm_button_reload_profile = "Reload the list of default packages";
    var global_fm_button_reload_of_profileaccess = "Reload the latest package list by group of service pack permissions";
    var global_fm_button_export = "Export To CSV";
    var global_fm_button_export_csv = "Export To CSV";
    var global_fm_button_export_word = "Export To Word";
    var global_fm_button_profile = "Profiles";
    var global_fm_button_configAPI = "API Config";
    var global_fm_button_API = "API Access";
    var global_fm_action = "Action";
    var global_fm_STT = "No";
    var global_fm_Username = "Username";
    var global_fm_Username_esigncloud = "Username of Remote Signing";
    var global_fm_Username_esigncloud_exists = "Remote Signing username already exists";
    var global_fm_Password = "Password";
    var global_fm_date = "Create Date/Update Date";
    var global_fm_date_create = "Create Date";
    var global_fm_date_revoke = "Revoke Date";
    var global_fm_date_gencert = "Issuance date";
    var global_fm_date_cancel = "Cancel Date";
    var global_fm_date_gen = "Generate date";
    var global_fm_num_date_cancel = "Number of days canceled";
    var global_fm_date_request = "Request Date";
    var global_fm_date_endupdate = "Last Updated Date";
    var global_fm_scan_valid = "Valid scans";
    var global_fm_user_request = "User Requested";
    var global_fm_user_create = "Created by";
    if(IsWhichCA === "18") {
        global_fm_user_create = "Creator";
    }
    var global_fm_user_receive = "User Receives";
    var global_fm_user_endupdate = "Last Updated User";
    var global_fm_timestamp = "Create Date/Update Date";
    var global_fm_times_recovery = "Recovery Time";
    var global_fm_duration = "Duration (Days)";
    var global_fm_active = "Active";
    var global_fm_all_apply_user = "Apply the list of permissions for all users in the system under this role";
    var global_fm_image = "Image (Icon)";
    var global_fm_required_input = "Input Requirements";
    var global_fm_effective = "Effective";
    var global_fm_duration_promotion = "Duration + promotion (Date)";
    var global_fm_address = "Address";
    var global_fm_fullname = "Name";
    var global_fm_fax = "Fax";
    var global_fm_email = "Email";
    var global_fm_email_receive = "Email (For receiving tax notices)";
    var global_fm_email_contact = "Customer Email";
    var global_fm_email_contact_grid = "Customer Email";
    if(IsWhichCA === "18") {
        global_fm_email_contact_grid = "Email";
    }
    var global_fm_email_contact_real = "Customer Email (Real)";
    var global_fm_email_authen_rssp = "Signature verification email";
    var global_fm_option_owner_new = "Create new owner";
    var global_fm_option_owner_search = "Search for owners on the system";
    var global_fm_email_contact_signserver = "The email address of the customer to send the server certificate";
    var global_fm_ip = "IP Address";
    var global_fm_port = "Port";
    var global_fm_ward = "Ward";
    var global_fm_street = "Street";
    var global_fm_city = "Province/City";
    var global_fm_area = "Region";
    var global_fm_Description = "Description";
    var global_fm_Certificate = "Certificate";
    var global_fm_cert = "Certificate";
    var global_fm_FromDate = "From Date";
    var global_fm_year = "Year";
    var global_fm_mounth = "Month";
    var global_fm_Quater = "Quarter";
    var global_fm_Branch = "Agency";
    var global_fm_ToDate = "To Date";
    var global_fm_StatusAccount = "Status";
    var global_fm_combox_all = "All";
    var global_fm_combox_empty = "[Blank]";
    var global_fm_combox_choose = "-- Please Choose --";
    var global_fm_datatype_label = "Type Enter";
    var global_fm_datatype_numeric = "Alpha(numeric)";
    var global_fm_datatype_varchar = "Alphabet";
    var global_fm_datatype_boolean = "Select/Uncheck";
    var global_fm_combox_no_choise = "--- Root ---";
    var global_succ_NoResult = "Not Found";
    var global_fm_role = "Role";
    var global_fm_phone = "Phone";
    var global_fm_email_manager = "Email of representative";
    var global_fm_phone_manager = "Phone of representative";
    var global_fm_name_manager = "Representative name";
    var global_fm_name_contact = "Contact name";
    var global_fm_phone_contact = "Customer Phone Number";
    var global_fm_phone_contact_grid = "Customer Phone Number";
    if(IsWhichCA === "18") {
        global_fm_phone_contact_grid = "Phone Number";
    }
    var global_fm_phone_contact_real = "Customer Phone Number (Real)";
    var global_fm_phone_authen_rssp = "Signature verification Phone";
    var global_fm_vendor = "SIM Provider";
    var global_fm_display_mess = "Message Content";
    var global_fm_fileid = "File ID";
    var global_error_file_special = "File names are not allowed to contain special characters. Include: /\{};:,\"`~&*|+=%$@<>[]#'^!?";
    var global_title_hsm_confirm = "Confirmation of digital certificate activation HSM";

    var global_paging_Before = "Previous";
    var global_paging_last = "Next";
    var global_paging_first = "First";
    var global_paging_next = "Last";

    var global_req_Username = "Enter Username";
    var global_req_Password = "Enter Password";
    var global_req_Description = "Enter Description";
    var global_req_Pem = "Enter Certificate (PEM)";
    var global_req_Certificate = "Enter Certificate";
    var global_req_address = "Enter Address";
    var global_req_ward = "Enter Ward";
    var global_req_street = "Enter Street";
    var global_req_mail = "Enter Email";
    var global_req_mail_format = "Enter the correct email format";
    var global_req_ip_format = "Enter the correct IP Address format";
    var global_req_cer_format = "Enter the correct format file .cer, .txt, .pem";
    var global_req_csr_format = "Enter the correct format file .csr, .txt";
    var global_req_crl_format = "Enter the correct format file .crl";
    var global_req_image_format = "Enter the correct format image file .jpg, .png";
    var global_fm_active_true = "YES";
    var global_fm_active_false = "NO";
    var global_fm_remark_en = "Description (English)";
    var global_fm_remark_vn = "Description (Vietnamese)";
    var global_fm_amount_fee = "Total fee (VND)";
    var global_fm_amount = "Total fee (VND)";
    if(IsWhichCA === "15" || IsWhichCA === "17") {
        global_fm_remark_vn = "Description (Laos)";
        global_fm_amount_fee = "Total fee";
        global_fm_amount = "Total fee";
    }
    var global_fm_activation_code = "Activation Code";
    var global_fm_activation_date = "Activation Duration";
    var global_fm_amount_token = "Token amount";
    
    // ### FORM_FACTOR
    var global_fm_amount_renewal = "Renewal amount";
    var global_fm_amount_changeinfo = "Change Infor amount";
    var global_fm_amount_reissue = "Reissue amount";
    var global_fm_amount_goverment = "Other amount";
    
    var global_fm_date_free = "Free Extra Time";
    var global_fm_entity_ejbca = "Entity EJBCA";
    var global_fm_choose_csr = "Choose CSR";
    var global_fm_choose_genkey_server = "Generate key on the server (P12)";
    var global_fm_choose_genkey_client = "Choose CSR (The key is generated from the Client)";
    var global_fm_en = "English";
    var global_fm_vn = "Vietnamese";
    var global_fm_refresh = "Refresh";
    var global_fm_properties = "Properties";
    var global_fm_uuid = "UUID";
    var global_fm_uuid_agreement = "Agreement UUID";
    var global_fm_remainingSigning_agreement = "Remaining number of signatures";
    var global_fm_appid_uri = "APPID URI";
    var global_fm_signature_v4 = "Signature V.4";
    var global_fm_access_key = "Access Key";
    var global_fm_secret_key = "Secret Key";
    var global_fm_xapi_key = "X API key";
    var global_fm_regions = "Region Name";
    var global_fm_service = "Service Name";
    var global_fm_dns_name = "DNS Name";
    var global_fm_dns_list = "DNS List";
    var global_fm_confirm_customer = "Customer's Confirmation Information";
    var global_fm_confirm = "Status confirm";
    var global_fm_confirm_time = "Confirm time";
    var global_fm_confirm_ip = "Confirm ip";
    var global_fm_confirm_content = "Confirm content";
    var global_fm_exists_form = "Beginning token inventory";
    var global_fm_Deposit_form = "Token deposited in month";
    var global_fm_use_form = "Token used in month";
    var global_fm_end_form = "Ending token inventory";
    var global_fm_form = "Form";
    var global_fm_uri = "URI";
    var global_fm_url_callback = "URL CallBack";
    var global_req_format_http = "Enter the correct Link format";
    var global_fm_Function = "Function";
    var global_fm_MetaData = "MetaData";
    var global_fm_billcode = "Bill Code";
    var global_fm_Function_tran = "Functionality";
    var global_succ_NoCheck = "Please select a to do list";
    var global_succ_NoCheck_setup = "Please select a list to Setup";
    var global_fm_import_choise_text = "Text File";
    var global_fm_import_choise_image = "Image File";
    var global_req_text_format = "Enter the correct format text file .txt, .pem";

//Configuration -> branch
    var branch_title_list = "Agency Configuration";
    var branch_table_list = "Agency List";
    var branch_title_add = "Add Agency";
    var branch_title_edit = "Config Agency";
    var branch_req_name = "Enter Agency Name";
    var branch_req_code = "Enter Agency Code";
    var branch_succ_add = "Added Agency Successfully";
    var branch_warning_add = "Add new agent successfully. User creation failed, username already exists";
    var branch_succ_edit = "Updated Agency Successfully";
    var branch_exists_name = "Agency Name is already existed";
    var branch_exists_code = "Agency Code is already existed";
    var branch_fm_name = "Agency Name";
    var branch_fm_code = "Agency Code";
    var branch_fm_parent = "Regional Branch";
    var branch_fm_level = "Agent hierarchy";
    var branch_req_area_change = "Region Value is NOT blank";
    var branch_conform_delete = "Do You Want To Delete Agency ?";
    var branch_succ_delete = "Delete Agency Successfully";
    var branch_exists_user_delete = "Please delete all agent user accounts first";
    var branch_conform_default = "Do You Want To Set Default for Logo ?";
    // new
    var branch_fm_choise_new = "Agency Type";
    var branch_fm_choise_CN = "Branch";
    var branch_fm_choise_PGD = "Sub-Branch";
    var branch_fm_access_profile = "Profile Access";
    
    //report -> synchneac
    var synchneac_title_list = "NEAC synchronous management";
    var synchneac_table_list = "List";
    var synchneac_title_edit = "Certificate information";
    var synchneac_succ_edit = "Successful synchronization";
    var synchneac_conform_update_multi = "Do you want to configure a lot of information ?";
    var synchneac_conform_decline_multi = "Do you want to decline a lot of information ?";
    var synchneac_conform_synch_multi = "Do you want to synchronize a lot of information ?";
    var synchneac_fm_remaining = "Number of synchronization errors";
    var synchneac_fm_synch_auto = "Enable automatic synchronization to NEAC";
    
//Configuration -> channel
    var city_title_list = "Province/City Configuration";
    var city_table_list = "Province/City List";
    var city_table_search = "Search Province/City";
    var city_title_add = "Add Province/City";
    var city_title_edit = "Config Province/City";
    var city_req_name = "Enter Province/City Name";
    var city_req_code = "Enter Province/City Code";
    var city_succ_add = "Enter Province/City Successfully";
    var city_exists_code = "Province/City Code is already existed";
    var city_exists_name = "Province/City Name is already existed";
    var city_succ_edit = "Config Province/City Successfully";
    var city_fm_code = "Province/City Code";
    var city_fm_name = "Province/City Name";
    
    //General -> CertificateTypeList
    var certtype_title_list = "Certificate Type Management";
    var certtype_table_list = "Certificate Type List";
    var certtype_title_add = "Add Certificate Type";
    var certtype_title_edit = "Config Certificate Type";
    var certtype_exists_code = "Certificate Type Code is already existed";
    var certtype_fm_code = "Certificate Type Code";
    var certtype_succ_add = "Added Certificate Type Successfully";
    var certtype_succ_edit = "Config Certificate Type Successfully";
    var certtype_group_file_profile = "Config attachment profile";
    var certtype_component_attributetype = "Attribute type";
    var certtype_component_cntype = "Common name type";
    var certtype_component_field_code = "Field code";
    var certtype_component_field_code_exists = "Field code is already existed";
    var certtype_file_code_exists = "File type code is already existed";
    var certtype_file_code = "File type code";
    var certtype_fm_file = "File type";
    var certtype_fm_component_text = "Enter the word";
    var certtype_fm_component_uuid_company = "Choose the enterprise UID";
    var certtype_fm_component_uuid_personal = "Choose the personal UID";
    var certtype_fm_component_uuid_company_require = "Required enterprise UID";
    var certtype_fm_component_uuid_personal_require = "Required personal UID";
    
    //General -> Response Code
    var response_title_list = "Transaction Status Configuration";
    var response_table_list = "Transaction Status List";
    var response_title_add = "Add Transaction Status";
    var response_title_edit = "Config Transaction Status";
    var response_succ_add = "Add Transaction Status Successfully";
    var response_exists_code = "Transaction Status Code is already existed";
    var response_exists_name = "Transaction Status Name is already existed";
    var response_succ_edit = "Config Transaction Status Successfully";
    var response_fm_code = "Transaction Status Code";
    var response_fm_name = "Transaction Status Name";
    //General -> MNO
    var mno_title_list = "MNO Management";
    var mno_table_list = "MNO List";
    var mno_title_add = "Add New MNO";
    var mno_title_edit = "Config MNO";
    var mno_succ_add = "Add MNO Successfully";
    var mno_exists_code = "MNO Code Is Already Exists";
    var mno_exists_name = "MNO Name Is Already Exists";
    var mno_succ_edit = "Config MNO Successfully";
    var mno_fm_code = "MNO Code";
    //General -> InternalEntity
    var interentity_title_list = "Internal Entity Management";
    var interentity_table_list = "Internal Entity List";
    var interentity_title_add = "Add Internal Entity";
    var interentity_title_edit = "Config Internal Entity";
    var interentity_succ_add = "Add Internal Entity Successfully";
    var interentity_exists_code = "Internal Entity Code Is Already Exists";
    var interentity_succ_edit = "Config Internal Entity Successfully";
    var interentity_fm_code = "Internal Entity Code";
    //General -> ExternalEntity
    var exterentity_title_list = "External Entity Management";
    var exterentity_table_list = "External Entity List";
    var exterentity_title_add = "Add External Entity";
    var exterentity_title_edit = "Config External Entity";
    var exterentity_succ_add = "Add External Entity Successfully";
    var exterentity_exists_code = "External Entity Code Is Already Exists";
    var exterentity_succ_edit = "Config External Entity Successfully";
    var exterentity_fm_code = "External Entity Code";
    //General -> RelyingParty
    var relyparty_title_list = "Relying Party Management";
    var relyparty_table_list = "Relying Party List";
    var relyparty_title_add = "Add Relying Party";
    var relyparty_title_edit = "Config Relying Party";
    var relyparty_succ_add = "Add Relying Party Successfully";
    var relyparty_exists_code = "Relying Party Code Is Already Exists";
    var relyparty_succ_edit = "Config Relying Party Successfully";
    var relyparty_fm_code = "Relying Party Code";
    var relyparty_fm_choise_all = "Allows all IP addresses";
    var relyparty_fm_choise_ip = "Enter the IP address list";
    var relyparty_fm_choise_all_function = "Allows all functions";
    var relyparty_fm_choise_ip_function = "Select function list";
    var relyparty_fm_group_ip = "Configure IP address list access";
    var relyparty_fm_group_function = "Configure the Access function list";
    var relyparty_fm_group_metadata = "Configures the MetaData list";
    var relyparty_fm_group_facet = "Configures Custemer Inforation";
    var relyparty_exists_add_ip = "The IP address is already exists";
    var global_error_delete_ip = "Delete failed, please check again";
    var relyparty_all_add_ip = "Has access to all IP, add IP failures";
    var relyparty_error_delete_function = "Delete function failed, please check again";
    var global_exists_add_function = "Function code is already exists";
    var global_exists_add_metadata = "AAID is already exists";
    var global_exists_add_facet = "Custemer Information Code is already exists";
    var relyparty_all_add_function = "Has access to all functions, add function fails";
    var relyparty_req_add_function = "Please enter a list of Functions";
    var global_req_add_ip = "Please enter the IP list";
    var global_conform_delete_function = "Do You Want To Delete This Function ?";
    var global_conform_delete_ip = "Do You Want To Delete This IP ?";
    var global_conform_delete_metadata = "Do You Want To Delete This MetaData ?";
    var global_succ_enabled_function = "Config Active of function successfully";
    var global_succ_enabled_ip = "Config Active of IP successfully";
    var global_succ_enabled_metadata = "Config Active of MetaData successfully";
    var global_succ_enabled_facet = "Config Active of Customer Inforation successfully";
    var global_conform_delete_soap = "Do You Want To Delete This Properties ?";
    var global_conform_delete_restful = "Do You Want To Delete This Restful Properties ?";
    var global_succ_delete_soap = "Delete Properties Successfully";
    var global_succ_delete_restful = "Delete Restful Properties Successfully";
    var global_succ_edit_soap = "Config CA Properties Successfully";
    var global_succ_edit_restful = "Config Restful Properties Successfully";
    var global_succ_add_soap = "Add CA Properties Successfully";
    var global_succ_add_restful = "Add Restful Properties Successfully";
    var global_fm_restful = "Restful Properties";
    var global_fm_soap = "Properties";
    var global_title_soap_edit = "Config CA Properties";
    var global_title_restful_edit = "Config RestFul Properties";
    var global_title_soap_add = "Add Soap Properties";
    var global_title_propeties_ca_add = "Add CA Properties";
    var global_title_restful_add = "Add RestFul Properties";
    var global_fm_facet = "Customer Information";
    var global_fm_status_expire = "Expired";
    var global_fm_not_blank = " is NOT blank";
    //General -> ManagementParty
    var manaparty_title_list = "Party Management";
    var manaparty_table_list = "Party Management List";
    var manaparty_title_add = "Add Party Management";
    var manaparty_title_edit = "Config Party Management";
    var manaparty_succ_add = "Add Party Successfully";
    var manaparty_exists_code = "Party Management is already exists";
    var manaparty_succ_edit = "Config Management Party Successfully";
    var manaparty_fm_code = "Party Management Code";
    var manaparty_fm_message_mode = "Message Mode";
    var manaparty_fm_expire_duration = "Expired time";
    var branch_fm_expire_token = "Expired time (Minute)";
    var branch_fm_secretkey = "Secret Key";
    //General -> FacetManagement
    var facetmana_title_list = "Customer Information Management";
    var facetmana_table_list = "Customer Information List";
    var facetmana_title_add = "Add Customer Information";
    var facetmana_title_edit = "Config Customer Information";
    var facetmana_succ_add = "Add Customer Information Succesffully";
    var facetmana_exists_code = "Customer Information Code is already exists";
    var facetmana_exists_name = "Customer Information Name is already exists";
    var facetmana_succ_edit = "Config Customer Information Successfully";
    var facetmana_fm_code = "Customer Information Code";
    //General -> FacetManagement
    var smartversion_title_list = "System Version Management";
    var smartversion_table_list = "System Version List";
    var smartversion_title_add = "Add System Version";
    var smartversion_title_edit = "Config System Version";
    var smartversion_succ_add = "Add System Version Successfully";
    var smartversion_exists_code = "System Version Code is already exists";
    var smartversion_exists_name = "System Version Name is already exists";
    var smartversion_succ_edit = "Config System Version Successfully";
    var smartversion_fm_code = "System Version Code";
    //History -> TransactionList
    var smarttrans_title_list = "Transaction History";
    var smarttrans_table_list = "Transaction List";
    var smarttrans_search_list = "Search";
    var smarttrans_title_view = "Transaction Information Detail";
    var smarttrans_fm_data_body = "Body Data";
    var smarttrans_fm_data_header = "Header Data";
    //General -> SMPP Party
    var smpp_title_list = "SMPP Management";
    var smpp_table_list = "SMPP List";
    var smpp_title_add = "Add SMPP";
    var smpp_title_edit = "Config SMPP";
    var smpp_succ_add = "Add SMPP Successfully";
    var smpp_exists_code = "SMPP Code is already exists";
    var smpp_succ_edit = "Config SMPP Successfully";
    var smpp_fm_code = "SMPP Code";
    var smpp_fm_heartbeat_interval = "HeartBeat Interval";
    var smpp_fm_retry_attempt = "Retry Attempt";
    var smpp_fm_retry_delay_duration = "Delay Duration";
    //Config -> ServerParty
    var serverparty_title_list = "Server Entity Management";
    var serverparty_table_list = "Server Entity Management";
    var serverparty_title_add = "Add Server Entity";
    var serverparty_title_edit = "Config Server Entity";
    var serverparty_succ_add = "Th??m m???i Server Entity Successfully";
    var serverparty_exists_code = "M?? Server Entity is already exists";
    var serverparty_succ_edit = "Config Server Entity Successfully";
    var serverparty_fm_code = "Server Entity Code";
    //General -> Functionality
    var metadata_title_list = "MetaData Management";
    var metadata_table_list = "MetaData List";
    var metadata_title_edit = "MetaData Detail";
    var metadata_title_add = "Add MetaData";
    var metadata_succ_add = "Add MetaData Successfully";
    var metadata_fm_aaid = "AAID";
    var metadata_fm_metadata = "MetaData";
    var metadata_exists_aaid = "AAID";
    var metadata_fm_contenttype = "Context Type";
    var metadata_fm_authenalgorithm = "Authentication Algorithm";
    var metadata_fm_keyrestricted = "Key Restricted";

    //General -> Functionality
    var function_title_list = "Function Configuration";
    var function_table_list = "Function List";
    var function_title_add = "Add Function";
    var function_title_edit = "Config Function";
    var function_succ_add = "Add Function Successfully";
    var function_exists_code = "Function Code is already existed";
    var function_exists_name = "Function Name is already existed";
    var function_succ_edit = "Config Function Successfully";
    var function_fm_code = "Function Code";
    var function_fm_name = "Function Name";
    //General -> CA
    var ca_title_list = "CA Management";
    var ca_table_list = "CA List";
    var ca_title_add = "Add New CA";
    var ca_title_edit = "Config CA";
    var ca_succ_add = "Add CA Successfully";
    var ca_exists_code = "CA Code is already existed";
    var ca_exists_name = "CA Name is already existed";
    var ca_succ_edit = "Config CA Successfully";
    var ca_fm_short = "Short Code";
    var ca_fm_code = "CA Code";
    var ca_fm_name = "CA Name";
    var ca_fm_OCSP = "OCSP URL";
    var ca_fm_CRL = "CRL URL";
    var ca_fm_CRLPath = "CRL Name";
    var ca_fm_URI = "URI";
    var ca_fm_Cert_01 = "Certificate";
    var ca_fm_CheckOCSP = "Chosen By OCSP";
    var ca_fm_unique_DN = "Allows duplicate certificate subjects";
    var ca_group_CRLFile_1 = "CRL File";
    var ca_error_valid_cert_01 = "Certificate is Invalid";
    var ca_error_valid_cert_expire_01 = "The Effective time of the certificate has expired";
    var ca_succ_import_crl1 = "Config CRL File 1 Successfully";
    var ca_error_import_crl1 = "Config CRL File Failure";
    var ca_group_cert = "Certificate Detail";
    var ca_req_info_cert = "No Certificate Found";
    var ca_succ_reload = "Reload CRL Successfully";
    var ca_error_reload = "Reload CRL failed";
    //General -> Certificate Profile
    var certprofile_title_list = "Service Package Management";
    var certprofile_table_list = "Service Package List";
    var certprofile_title_add = "Add Service Package";
    var certprofile_title_edit = "Config Service Package";
    var certprofile_succ_add = "Add Service Package Successfully";
    var certprofile_exists_code = "Service Code Is Already Exists";
    var certprofile_exists_name = "Service Name Is Already Exists";
    var certprofile_succ_edit = "Config Service Package Successfully";
    var certprofile_fm_code = "Service Package Code";
    var certprofile_fm_service_type = "Service Type";
    var certprofile_fm_service_issue = "Issue";
    var certprofile_fm_service_renew = "Renew";
//admin -> confignamil
    var email_title_list = "Email Configuration";
    var email_req_smtp = "Enter SMTP Server";
    var email_req_port = "Enter Port";
    var email_succ_edit = "Config Mail Successfully";
    var email_fm_port = "Port";
    var email_fm_smtp = "SMTP Server";
//admin -> ManagePolicy
    var policy_title_list = "General Configuration";
    var policy_title_list_client = "Parameter";
    var policy_title_list_client_fo = "Customer Management";
    var policy_title_list_client_bo = "Adminstration Management";
    var policy_group_notification = "Config Default Notification";
    var policy_succ_edit = "Config General Information Successfully";
    var policy_req_empty = "Enter" + " ";
    var policy_req_empty_choose = "Choose" + " ";
    var policy_req_number = " " + "just consisted of alpha(numeric)";
    var policy_req_unicode = " " + "does't contain accented characters";
    //admin -> ConfigPolicy
    var policy_config_title_list = "Parameter Field Configuration";
    var policy_config_table_list = "Parameter Field List";
    var policy_title_edit = "Config Parameter Field";
    var policy_succ_add = "Add Parameter Field Successfully";
    var policy_fm_fo = "Customers";
    var policy_fm_bo = "Back-Office";
    var policy_fm_group_fo_bo = "Parameters for the module";
    var policy_title_add = "Add Parameter Field";
    var policy_fm_code = "Parameter Field Code";
    var policy_exists_code = "Parameter Field Code is already existed";

//admin -> menulink
    var menu_title_list = "Menu Link Configuration";
    var menu_title_table = "Menu Link";
    var menu_group_Role = "Choose Role";
    var menu_fm_Role = "Role";
    var menu_group_assign = "Menu is NOT assigned";
    var menu_fm_assign = "Menu Name";
    var menu_fm_parent_name = "Management Menu Name";
    var menu_fm_url = "Menu Path";
    var menu_table_assigned = "Assigned Menu";
    var menu_conform_delete = "Do You Want To Delete This Menu ?";
    var menu_succ_delete = "Delete Menu Successfully";
    var menu_succ_insert = "Add Menu Successfully";
    var menu_error_delete = "Delete Menu Failure";
    var menu_error_insert = "Add Menu Failure";
    var menu_fm_button_assign = "Assign";
    
    //general -> methodprofile
    var methodprofile_title_list = "Decentralize service pack access method";
    var methodprofile_title_table = "Method decentralization";
    var methodprofile_group_formfactor = "Choose method";
    var methodprofile_fm_Role = "Method";
    var methodprofile_group_assign = "Profiles not assigned";
    var methodprofile_fm_profile = "Profiles";
    var methodprofile_table_assigned = "Assigned Profiles";
    var methodprofile_conform_delete = "Do You Want To Delete This Profile ?";
    var methodprofile_succ_delete = "Delete Successfully";
    var methodprofile_succ_insert = "Add Successfully";
    var methodprofile_error_delete = "Delete Failure";
    var methodprofile_error_insert = "Add Failure";
    
//admin -> UserRole
    var role_title_list = "User Role Configuration";
    var role_title_table = "User Role";
    var role_title_edit = "Config User Role";
    var role_title_add = "Add User Role";
    var role_group_Role = "Choose User Role";
    var role_fm_code = "User Role Code";
    var role_fm_is_ca = "Position for CA";
    var role_fm_is_agent = "Position for Agency";
    var role_fm_name = "User Role Name";
    var role_succ_add = "Add User Role Successfully";
    var role_succ_edit = "Config User Role Successfully";
    var role_exists_code = "User Role Code is already existed";
    var role_exists_name = "User Role Name is already existed";
    var role_noexists_functions = "At Least One Function Should Be Chosen";
    var role_fm_function_name = "Function Name";
    
    //admin -> UserRole
    var esignremain_title_list = "Report Remaining Signing";
    var esignremain_title_table = "List";
    
    // FUNCTION ACCESS
    var funrole_fm_islock = "Set Lock";
    var funrole_fm_isunlock = "Set UnLock";
    var funrole_fm_issopin = "Set Change SOPIN";
    var funrole_fm_ispush = "Set Push Notification";
    var funrole_fm_isinit = "Set Initialization";
    var funrole_fm_isdynamic = "Set Dynamic Content";
    var funrole_fm_isinformation = "Set Information";
    var funrole_fm_isactive = "Set Active";
    var funrole_fm_editcert = "Configure Certificate";
    var funrole_fm_approvecert = "Approve Certificate";
    var funrole_fm_deleterequest = "Decline Manual Renewal";
    var funrole_fm_addrenewal = "Add Compensation";
    var funrole_fm_deleterenewal = "Decline Compensation";
    var funrole_fm_importrenewal = "Import Compensatory List";
    var funrole_fm_accessrenewal = "Access To Compensatory Function";
    var funrole_fm_revoke_cert = "Access to Certificate Revoke";
    var funrole_fm_export_cert = "Access to Certificate Export";
    //User -> MenuScreen
    var menusc_title_list = "Menu Configuration";
    var menusc_title_table = "Menu List";
    var menusc_title_edit = "Config Menu";
    var menusc_title_add = "Add Menu";
    var menusc_fm_nameparent = "Menu Management";
    var menusc_fm_name_vn = "Menu Name (Vietnamese)";
    if(IsWhichCA === "15" || IsWhichCA === "17") {
        menusc_fm_name_vn = "Menu Name (Laos)";
    }
    var menusc_fm_name = "Menu Name";
    var menusc_fm_name_en = "Menu Name (English)";
    var menusc_fm_code = "Menu Code";
    var menusc_fm_url = "Menu URL";
    var menusc_succ_add = "Added Menu Successfully";
    var menusc_succ_edit = "Config Menu Successfully";
    var menusc_exists_linkurl = "Menu URL is already existed";
    var menusc_exists_nameparent = "Menu Management is already existed";
    //user -> userlist
    var user_title_list = "BackOffice User Management";
    var user_title_search = "BackOffice User";
    var user_title_table = "BackOffice User List";
    var user_title_edit = "Config BackOffice User";
    var user_title_add = "Add BackOffice User";
    var user_title_info = "BackOffice User Information";
    var user_title_roleset = "Functions List";
    var user_title_roleset_token = "Functions for Token";
    var user_title_roleset_cert = "Functions for Certificate";
    var user_title_roleset_another = "Other Functions";
    var user_succ_add = "Add BackOffice User Successfully";
    var user_succ_edit = "Config BackOffice User Successfully";
    var user_exists_username = "Username is already existed";
    var user_exists_email = "Email is already existed";
    var user_exists_cert_hash = "Certificate Information is already existed";
    var user_exists_user_role_admin = "Existed Username For The Administrator Role In The System";
    var user_conform_cancel = "Do You Want To Cancel User ?";
    var user_title_delete = "Delete User";
    var user_title_delete_note = "Note: Please select a user to receive";
    
    //Rose -> RoseList
    var rose_title_list = "Commision Group Configuration";
    var rose_title_table = "Table";
    var rose_title_edit = "Config Commision Group";
    var rose_title_add = "Add Commision Group";
    var rose_fm_code = "Commision Group Code";
    var rose_fm_rose = "Commision Group";
    var rose_succ_edit = "Config succesfully";
    var rose_succ_add = "Added succesfully";
    var rose_exists_profile_properties = "Profiles is already existed";
    var rose_permission_profile_list = "Percentage of commission for the service pack";
    
    // profileaccss ->  profileaccss
    var profileaccss_title_list = "Profile Group Configuration";
    var profileaccss_title_table = "Table";
    var profileaccss_title_edit = "Config Profile Group";
    var profileaccss_title_add = "Add Profile Group";
    var profileaccss_fm_code = "Profile Group Code";
    var profileaccss_fm_agency = "Agency List";
    var profileaccss_fm_rose = "Profile Group";
    var profileaccss_fm_service_type = "Request Type Group";
    var profileaccss_fm_major_cert = "Certificate functions";
    var profileaccss_succ_edit = "Config succesfully";
    var profileaccss_succ_add = "Added succesfully";
    var profileaccss_exists_profile_properties = "Profiles is already existed";
    var profileaccss_apply_profile_agency = "Apply configuration to agent";
    var profileaccss_exists_code = "Profile Group Code is already existed";
    
    var role_group_Role = "Choose User Role";
    var role_fm_is_ca = "Position for CA";
    var role_fm_is_agent = "Position for Agency";
    var role_fm_name = "User Role Name";
    var role_succ_add = "Add User Role Successfully";
    var role_succ_edit = "Config User Role Successfully";
    var role_exists_code = "User Role Code is already existed";
    var role_exists_name = "User Role Name is already existed";
    var role_noexists_functions = "At Least One Function Should Be Chosen";
    var role_fm_function_name = "Function Name";
    
    //admin -> formfactor
    var formfactor_title_list = "Method Configuration";
    var formfactor_title_table = "Method List";
    var formfactor_title_edit = "Config Method";
    var formfactor_fm_code = "Method Code";
    var formfactor_fm_name = "Method Name";
    var formfactor_succ_edit = "Config Method Successfully";
    var formfactor_exists_name = "Method Name is already existed";
    var formfactor_title_properties = "Configure the system connection";

//SmartUser -> CertificateList
    var global_fm_certprofile = "Certificate Profile";
    var global_fm_certstatus = "Certificate Status";
    var global_fm_cert_expire_number = "Remaining day(s)";
    var global_fm_common = "Common Name";
    var global_fm_subject = "DN";
    var global_fm_public_key_hash = "Public Key Hash";
    var global_fm_certificate_hash = "Certificate Hash";
    var global_fm_key_id = "Key ID";
    var global_fm_key_selector = "KEY SELECTOR";
    var global_fm_service_deny = "SERVICE DENY";
    var global_fm_authority_key_id = "AUTHORITY KEY ID";
    var global_error_empty_cert = "Certificate is not existed";
    var global_error_exists_mst_budget_regis = "Tax code/ Budget code/ Identity card /Passport already exists in the system\nPlease visit buy more certificates";

    var global_fm_ca = "CA Provider";
    var global_fm_certpurpose = "Certificate Purpose";
    var global_fm_certalgorithm = "Certificate Algorithm";
    var global_fm_Password_new = "New Password";
    var global_fm_Password_conform = "Confirm Password";
    var global_fm_Password_old = "Current Password";
    var global_fm_Password_change = "Change Password";
    var global_fm_button_PasswordChange = "OK";
    var global_fm_button_setup = "Setup";
    var global_fm_button_setup_ejbca = "Setup From RA";
    var global_fm_button_import = "Upload";
    var global_fm_button_check = "Check";
    var global_fm_valid = "Effective Date";
    var global_fm_valid_cert = "Effective Date";
    var global_fm_browse_file = "Choose File";
    var global_fm_browse_cert_note = "Please select file smaller than ";
    var global_fm_fileattach_support = "Supported file formats: ";
    var global_fm_browse_cert_addnote = "Prioritize pdf, image files";
    var global_fm_Expire = "Expiration Date";
    var global_fm_Expire_cert = "Expiration Date";
    var global_fm_pass_p12 = "P12 Password";
    var global_fm_dateUpdate = "Update Date";
    var global_fm_dateUpdate_next = "Update Date Next";
    var global_fm_dateend = "Expiration Date";
    var global_fm_activation = "Activation Date";
    var global_fm_Method = "Method";
    var global_fm_mode = "Mode";
    var global_fm_Method_Smart_ID = "Smart ID Method";
    var global_fm_Method_Mobile_OTP = "Mobile OTP Method";
    var global_fm_Method_UAF = "UAF Method";
    var global_fm_worker = "Worker";
    var global_fm_isbackoffice_grid = "FrontOffice/BackOffice";
    var global_fm_isbackoffice = "FrontOffice/BackOffice";
    var global_fm_key = "Key Name";
    var global_fm_logout = "Log Out";
    var global_fm_title_account = "Account";
    var global_fm_otp_serial = "OTP SN";
    var global_fm_check_date = "By Date";
    var global_fm_check_date_profile = "By (Receipt date of collation)";
    var global_fm_check_date_control = "By (Control date)";
    var global_fm_expire_date = "Number of days remaining effect";
    var global_fm_check_month = "By Month";
    var global_fm_check_quarterly = "By Quarterly";
    var global_fm_check_token = "By Token SN";
    var global_fm_company = "Company";
    var global_fm_issue = "Issuance";
    var global_fm_size = "Size (KB)";
    var global_fm_OU = "Organization Unit (OU)";
    var global_fm_MST = "Tax Code";
    var global_fm_enterprise_id = "Enterprise UID";
    var global_fm_personal_id = "Personal UID";
    var global_fm_callback_url = "API integration workstation path to receive request message from Token Manager";
    var global_fm_callback_when_approve = "The value of callback URL when approved";
    var global_fm_decision = "Decision Number";
    var global_fm_share_mode_cert = "Allow additional certificate service information";
    var global_fm_issue_p12_enabled = "Allows issuing P12 certificates";
    var global_fm_ID = "Code ID";
    var global_fm_date_grant = "Issue Date";
    var global_fm_organi_grant = "Place of Issue";
    var global_fm_representative_legal = "Legal Representative";
    var global_fm_MNV = "Employee Code";
    var global_fm_CMND = "Personal ID";
    var global_fm_CMND_ID_Card = "Personal ID, ID card";
    var global_fm_place = "Place of Issue";
    var global_fm_cmnd_date = "Issue Date";
    var global_fm_O = "Organization (O)";
    var global_fm_O_notrefix = "Organization";
    var global_fm_T = "Title (T)";
    var global_fm_L = "Location (L)";
    var global_fm_C = "Country (C)";
    var global_fm_ST = "Province/City (ST)";
    var global_fm_CN = "Company Name (CN)";
    var global_fm_grid_CN = "Common Name (CN)";
    var global_fm_grid_personal = "Personal Name";
    var global_fm_grid_company = "Company Name";
    var global_fm_grid_domain = "Domain Name";
    var global_fm_CN_CN = "Full Name (CN)";
    var global_fm_serial = "Certificate SN";
    var global_fm_choose_owner_cert = "Search by";
    var global_fm_Status = "Status";
    var global_fm_branch_status = "Agency status";
    var global_fm_status_control = "Control status";
    var global_fm_Status_token = "Token status";
    var global_fm_user_lock_unlock_token = "User lock/unlock token";
    var global_fm_Status_signed = "Status signed";
    var global_fm_Status_notice = "Notification status";
    var global_fm_apply_signed = "Signed";
    var global_fm_unapply_signed = "Not signed";
    var global_fm_Status_cert = "Certificate Status";
    var global_fm_Status_request = "Request Status";
    var global_fm_Status_agreement = "Agreement Status";
    var global_fm_smart_version = "Smart Version";
    var global_fm_os_type = "OS Type";
    var global_fm_from_system = "From System";
    var global_fm_from_system_uri = "From URL";
    var global_fm_to_system = "To System";
    var global_fm_to_system_uri = "To URL";
    var global_fm_Status_OTP = "Wrong Consecutive Activation Code Authentication Remaining";
    var global_fm_Status_SignServer = "SignServer Agreement Status";
    var global_fm_Status_PKI = "PKI Agreement Status";
    var global_fm_status_profile = "Profile status";
    var global_fm_activity = "Operated";
    var global_fm_lost = "Lost";
    var global_fm_relost = "Cancel Lost";
    var global_fm_lock = "Block";
    var global_fm_type = "Model";
    var global_fm_value = "Value";
    var global_fm_chain_cert = "Issuance Certificate";
    var global_error_chain_cert = "Issuance Certificate is not existed";
    var global_error_cert_compare_ca = "Certificate is Invalid";
    var global_error_cert_compare_csr = "Certificate is Invalid";
    var global_fm_Note = "Note";
    var global_fm_Note_offset = "Notes offset records";
    //var global_fm_soft_copy = "Electronic version";
    //if(IsWhichCA === "7") {
    var global_fm_soft_copy = "Status of legal files";
    //}
    var global_fm_Content = "Content";
    var global_fm_tran_code = "Transaction Code";
    var global_fm_tran_timeout = "Timeout (Second)";
    var global_fm_filter_search = "Search";
    var global_fm_combox_success = "Successfully";
    var global_fm_combox_errorsend = "Failure";
    var global_fm_cert_circlelife = "Certificate life cycle";
    var global_req_all = "Please fill all";
    var global_req_length = "Length is invalid";
    var global_req_file = "Choose File";
    var global_req_file_has_data = " (The file has content)";
    var global_req = policy_req_empty;
    var global_errorsql = "An error occurred, Please Try Again";
    var global_print2_fullname_business = "Full transaction name (Capital, accented letters)";
    var global_req_email_subject_san = "Please enter email in the same certificate";
    var global_req_print_not_support = "Request type is not supported for printing";
    var global_req_warning_exists_cert = "A request for approval already exists with the above certificate information\nContinue to register?";

    var global_alert_login = "Login time has expired, Please login again";
    var global_alert_another_login = "The account has been locked/ has been accessed from other devices, Please check again";
    var global_alert_another_menu = "You can't to access this Function, Please check again";
    var global_alert_license_invalid = "License Invalid. Please contact Hotline 1900 6884 or Email info@mobile-id.vn to Supported";
    var global_error_login_info = "Account login information doesn't exist, please try again";
    var global_error_invalid = ": Invalid";

// Admin -> LicenseAdmin
    var license_title_list = "License Management";
    var license_table_list = "Import License File";
    var license_title_search = "Search";
    var license_title_edit = "Details";
    var license_req_file = "Choose License File";
    var license_fm_file = "Choose File";
    var license_succ_import = "Config License File Successfully";
    var license_group_hardware = "Hardware Detail";
    var license_group_view = "License Detail";
    var license_fm_token_sn = "Token SN";
    var license_fm_user_enabled = "Used";
    var license_fm_type = "Type";
    var license_group_Function = "Functionality Information";
    var license_error_file = "License File is invalid";
    var license_error_no_token_sn = "Import Failture.\[TOKEN_SN] value is not existed";
    var license_error_no_license_type = "Import Failture.\[LICENSE_TYPE] value is not existed";
    var license_succ_import_insert = ". Add: ";
    var license_succ_import_update = " ; Already Existed: ";
    var license_succ_import_error = " ; Error: ";

// sumary page website
    var CSRF_Mess = "Current Session Is Expired, Please Reload The Page";
    var TitleLoginPage = "Back-Office";
    var TitlePolicyPage = "Privacy Policy";
    var TitleTermsPage = "Terms of Service";
    var TitlePolicyLink = "Privacy Policy";
    var TitleTermsLink = "Terms of Service";
    var TitleHomePage = "Back-Office Homepage";
    var error_title_list = "System Error";
    var error_content_home = "Internal System Error. Please Back To Main Menu";
    var error_content_login = "Internal System Error. Please Retry Login";
    var error_content_link_download = "Download the error description file";
    var error_content_link_out = "link out";
    var login_req_captcha = "CAPTCHA Is NOT Correct";
    var login_title_captcha = "Refresh CAPTCHA";
    var login_fm_captcha = "CAPTCHA";
    var login_fm_forget = "Forgot Password ?";
    var login_fm_token_ssl = "Token";
    var login_title_forget = "Forgot Password";
    var login_succ_forget = "Please Check Email For New Password";
    var login_succ_forget_request = "Submit a successful password reset request. Please wait for the system administrator to approve";
    var login_fm_buton_login = "Login";
    var login_fm_buton_cancel = "Cancel";
    var login_fm_buton_OK = "OK";
    var login_fm_buton_continue = "Continue";
    var login_error_timeout = "Timeout";
    var login_error_exception = "System Error. Please Retry";
    var login_error_lock = "Account Is Temporary Block. Please Try Later";
    var login_error_incorrec = "Username/Password is NOT Correct";
    var login_error_inactive = "Account Locked. Please Contact Administrator";
    var login_error_token_ssl = "Access denied";
    var login_conform_forget = "Please confirm email information: {EMAIL}";
    var global_fm_detail = "Detail";
    var global_fm_expand = "Expand";
    var global_fm_collapse = "Collapse";
    var global_fm_hide = "Hide";
    var global_fm_search_expand = "Expand Search";
    var global_fm_search_hide = "Collapse Search";
    var global_fm_button_reset = "Reset";
    var global_fm_button_activate = "Activate";
    var global_fm_button_unactivate = "DeActivate";
    var global_fm_file_name = "File name";
    var global_fm_down = "Download";
    var global_fm_view = "View";
    var global_fm_p12_down = "P12 Download";
    var global_fm_p7p_down = "P7B Download";
    var global_fm_down_enterprise = "Enterprise certificate";
    var global_fm_down_personal = "Personal certificate";
    var global_fm_down_staff = "Staff certificate";
    var global_fm_down_pem = "Download PEM";
    var global_fm_license_down = "Download Digital Certificate License";
    var global_fm_license_create = "Re-create a digital certificate license";
    var global_fm_sign_confirmation = "Re-sign the confirmation page";
    var global_fm_wait_sign_confirmation = "Waiting for signing the confirmation";
    var global_cbx_wait_sign_confirmation = "Waiting for signing";
    var global_cbx_sign_confirmation = "Signed";
    var global_succ_license_create = "Create Certificate Successfully";
    var global_fm_down_cts = "Download Certificate";
    var global_fm_change = "Change";
    var global_fm_dispose = "Cancel";
    var global_fm_copy_all = "Copy To Clipboard";
    var global_succ_copy_all = "Copy To Clipboard Successfully";

    ///face 03 ///
    var global_req_formfactor_support = "The system does not yet support methods from BackOffice";
    var global_req_no_special = " Don???t Consist Of Special Character";
    var global_req_no_space = " Don???t Consist Of BLANK";
    var global_fm_button_delete = "Delete";
    var global_fm_paging_total = "Total rows ";
    var policy_check_length_pass = "Minimum Length Must Be Belower Than Maximum Length Of Password";
    var policy_check_number_zero = " " + "Must Be Greater 0";
    var global_fm_button_reset_pass = "Reset Password";
    var global_fm_button_check_pass_default = "Check Default Password";
    var global_fm_character = " " + "alphabet";
    var global_fm_phone_zero = " " + "Must Be Began With 0";
    var global_fm_phone_8_11 = "MobilePhone Length Is From 8 To 11 numbers";
    var pass_req_no_space = "Password Don???t Consist Of BLANK";
    var user_req_no_space = "Username Don???t Consist Of BLANK";
    var pass_req_min_greater = "Minimum Length for Password Must Be Greater ";
    var pass_req_max_less = "Maximum Length for Password Must Be Belower ";
    var pass_req_character = "Password Need To Have At Least ONE Alphabet";
    var pass_req_special = "Password Need To Have At Least ONE Special Character";
    var pass_req_number = "Password Need To Have At Least ONE Numeric";
    var pass_req_upcase = "Password Need To Have At Lease ONE Upcase Character";
    var pass_req_another_old = "New Password Must Be Different To Current Password";
    var pass_req_conform_new = "Confirmed New Password Is Wrong";
    var pass_error_old = "Current Password Is Wrong";
    var pass_error_choise_another = "Please Enter Other Password";
    var pass_error_choise_another_exists = "New password can't match the last {NUMBER} passwords! Please enter a new password";
    var pass_succ_change = "Changed Password Successfully";
    var pass_succ_change_show = ". Password is: ";
    var pass_error_account_old = "Current Account Is Wrong";
    var global_fm_check_search = "Please Check At Least ONE Condition";
    var pass_fm_Password_first = "Change Password Information";
    
    // Send mail
    var sendmail_error = "Send mail failed. Please Try Again";
    var sendmail_success = "Send mail successfully";
    var sendmail_notexists = "Email Is NOT Correct";
    var sendmail_notexists_account = "Username is NOT Correct";

    // Send mail Password Signserver
    var sendmail_error_signserver = "Send mail failed. Please Try Again";

//Global
    var global_alert_search_multiline = "Number of search result exceeds 10,000. System will be automatically optimized the search conditions";
    var global_error_export_excel = "Failed to export";
    var global_success_export_excel = "Trusted Hub has already received the exporting request";
    var global_error_insertmenulink = "Failed to add functional screen";
    var global_error_deletemenulink = "Failed to delete functional screen";
    var global_search_time_all = "At all time in the system";
// button
    var global_button_grid_delete = "Delete";
    var global_button_grid_edit = "Config";
    var global_button_grid_config = "Config";
    var global_button_grid_lock = "Block";
    var otp_button_grid_lock = "Block";
    var global_label_grid_sum = "Total ";
    var global_button_grid_OTP = "OTP";
    var global_button_grid_cancel = "Cancel";
    var global_button_grid_authen = "Authen";
    var global_button_grid_synch = "Synch";

    var global_button_grid_lost = "Lost";
    var global_button_grid_unlost = "UnLost";
    var global_button_grid_unlock = "UnBlock";
    var global_button_grid_resetpasscode = "Reset Passcode";
    var global_button_grid_sendmail = "Send Mail";
    var global_button_reconfirm = "Re-Confirm";
    var global_button_p12_sendmail = "P12 Sending";
    var otp_button_grid_unlock = "UnBlock";
    var global_button_grid_enable = "Enable";
    var global_button_grid_disable = "Disable";
    var global_fm_gen_pass = "Generate Password";
    var global_no_data = "No data found !";
    var global_no_getcompany_data = "No data found. Please enter information";
    var global_no_print_data = "Print template not found";
    var global_no_file_list = "File List is Empty";
    var global_check_datesearch = "Started Date should be before End Date";
    var global_check_date_expired = "The expiration time must be greater than the current date";
    var global_succ_succ = "Successfully";
    var global_fm_mess_in = "Request Data Information";
    var global_fm_mess_out = "Response Data Information";

    var global_req_phone_format = "Enter The Correct Phone Format";
    var user_error_no_data = "The BackOffice User information does't exist, please check again";
    var user_conform_reset_pass = "Do You Want To Reset Password ?";
    var token_confirm_delete = "Do You Want To Delete the Token ?";
    var user_conform_delete = "Do You Want To Delete User ?";
    var user_succ_delete = "Delete User Successfully";
    var global_fm_require_label = " " + "(*)";
    var global_fm_import_sample = "Refer Sample File: ";
    var global_req_info_cert = "Certificate information is invalid";

    //request -> tokenlist
    var token_title_list = "Token Management";
    var token_title_search = "Search";
    var token_title_table = "List";
    var token_title_edit = "Token Detail";
    var token_title_add = "Token New";
    var token_title_init = "Initialization";
    var token_title_changesopin = "Change SOPIN";
    var token_title_delete = "Delete Token";
    var token_exists_tokensn = "Token SN is already existed";
    var token_succ_edit = "Update Successful";
    var token_succ_delete = "Delete Successful";
    var token_conform_update_multi = "Do You Want To Update ?";
    var token_succ_add_renew = "Add Successful";
    var token_fm_tokenid = "Token SN";
    var token_fm_signing_number = "Number of signing";
    var token_fm_sopin = "SOPIN";
    var token_fm_tokenid_new = "Token SN New";
    var token_fm_subject = "Subject Name";
    var token_fm_company = "Company Name";
    var token_fm_valid = "Effective Time (Certificate)";
    var token_fm_expire = "Expiration Time (Certificate)";
    var global_fm_FromDate_valid = "Effective Time (Begin)";
    var global_fm_ToDate_valid = "Effective Time (End)";
    var global_fm_FromDate_expire = "Expiration Time (Begin)";
    var global_fm_ToDate_expire = "Expiration Time (End)";
    var global_fm_FromDate_profile = "From (Receipt date of collation)";
    var global_fm_ToDate_profile = "To (Receipt date of collation)";
	var global_fm_From_Control = "From (Control date)";
    var global_fm_To_Control = "To (Control date)";
    
    var global_fm_From_effective = "From (Effective date)";
    var global_fm_To_effective = "To (Effective date)";
    var global_fm_From_expire = "From (Expiration date)";
    var global_fm_To_expire = "To (Expiration date)";
    
    var token_fm_validexpire_search = "Effective Time and Expiration Time (Certificate)";
    var token_fm_personal = "Personal Name";
    var token_fm_serialcert = "Certificate SN";
    var token_fm_thumbprintcert = "Thumbprint";
    var token_fm_taxcode = "TIN (Tax Identification Number)";
    var token_fm_block = "Lock";
    var token_fm_reason_block = "Lock Reason";
    var token_fm_all_apply = "Apply to all Tokens in the system (ignore the upload file)";
    var token_fm_unblock = "UnBlock";
    var token_fm_csr = "CSR (Certificate Signing Request)";
    var token_fm_innit = "Initialize";
    var token_fm_change = "Change SOPIN";
    var token_fm_datelimit = "Expiration Time (Certificate will be issued)";
    var token_fm_mobile = "Phone";
    var token_fm_email = "Email";
    var token_fm_address = "Address";
    var token_fm_address_permanent = "Permanent address";
    var token_fm_address_billing = "Billing address";
    var token_fm_address_residence = "Permanent residence";
    var token_fm_menulink = "Menu Link";
    var token_fm_linkname = "Link Name";
    var token_fm_linkvalue = "Link Value";
    var token_fm_noticepush = "Display notification (sticker) on Token Manager";
    var token_fm_noticeinfor = "Notice Infomation";
    var token_fm_noticelink = "Notice Link";
    var token_fm_colortext = "Text Color";
    var token_fm_colorgkgd = "Background Color";
    var token_fm_infor = "Infomation";
    var token_fm_location = "Location";
    var token_fm_state = "State";
    var token_fm_enroll = "Enroll";
    var token_fm_TimeOffset = "Expiration Time (User Contract)";
    var token_fm_expire_mmyy = "Expired time";
    var token_fm_dn = "DN (Distinguished Name)";
    var token_fm_passport = "PID (Personal ID)";
    var token_fm_version = "Token Version";
    var token_fm_agent = "Agency";
    var token_fm_agent_level_one = "Agency level 1";
    var token_group_info = "Detail Infomation";
    var token_group_update = "Change Infomation";
    var token_group_notification = "Information displayed (sticker) on the horizontal bar of Token Manager";
    var token_group_dynamic = "Dynamic Content";
    var token_group_other = "Other";
    var token_group_cert_history = "Certificate history of Token";
    var token_group_request_edit = "The Request list requires editing Token";
    var token_title_import = "Import Token";
    var token_fm_typesearch = "Search For";
    var token_fm_import_sample = "Refer Sample File: ";
    var token_fm_datelimit_example = "Example: (ISO 8601 date: [yyyy-MM-dd HH:mm:ssZZ]: '2018-09-20 14:01:28+07:00')";
    // or days:hours:minutes
    var token_error_no_column = "Import Failure.\nThe information column format is incorrect";
    var token_error_no_tokenid = "Import Failure.\n[TOKEN_SN] Value Is Not Existed";
    var token_error_no_sopin = "Import Failure.\n[TOKEN_SOPIN] Value Is Not Existed";
    var token_error_no_agent = "Import Failure.\n[Agency] Value Is Not Existed";
    var token_error_datelimit_format = "Please Enter Valid Date Format";
    var token_error_datelimit_date = "Please Enter Expiration Time";
    var token_error_datelimit_format_date = "Date Format: (ddd:HH:MM)";
    var token_succ_import = "Upload Successful";
    var token_succ_setup = "Installed successfully";
    var token_succ_check_import = "The check was successful. Press the install button to save the information";
    var token_error_check_import = "An error occurred, please check the details in the result file";
    var token_succ_import_insert = ". Add: ";
    var token_succ_import_update = " ; Update: ";
    var token_succ_import_error = " ; Error: ";
    var token_succ_import_insert_replace = "Add: ";
    var token_succ_import_update_replace = "Update: ";
    var token_succ_import_error_replace = "Unsuccessful: ";
    var token_error_import_format = "Excel Format: XLS, XLSX, CSV";
    var token_fm_lock_opt = "Activation Code Authantication Status is Block";
    var token_fm_unlock_opt = "Activation Code Authantication Status is UnBlock";
    var token_confirm_unlock_temp = "Do You Want To Unblock ?";
    var token_confirm_resetpasscode_temp = "Do You Want To Reset Passcode ?";
    var token_confirm_lock_temp = "Do You Want To Block ?";
    var token_succ_reset_opt = "Activation Code Authentication Status Unblock is done Successfully";
    var tokenreport_title_list = "Dealer inventory report";
    var tokenreport_fm_choose_time_export = "Export time";
    var tokenreport_fm_choose_time_import = "Time to import";
    var tokenreport_fm_agenct_date_export = "Release date for agent";
    //request -> certificatelist
    var cert_title_list = "Certificate Approval Management";
    var cert_title_search = "Request Search";
    var cert_title_table = "Request List";
    var cert_title_edit = "Certificate Approval";
    var cert_title_register_cert = "Certificate Information";
    var cert_title_register_csr = "CSR Information";
    var cert_title_register_owner = "Owner Information";
    var cert_succ_approve = "Approve Successful";
    var cert_error_approve = "Approve Failure";
    var cert_succ_reissue = "Issue Successful";
    var cert_fm_type_request = "Request Type";
    var cert_fm_request = "request";
    var cert_fm_request_agent = "Approved in agency";
    var cert_fm_major_name = "Function name";
    var cert_fm_major_code = "Function code";
    var cert_fm_profile_list = "Certificate Type";
    var cert_fm_cert_profile = "Certificate Profile (CORECA)";
    var cert_fm_delete_cert = "Delete Certificate";
    var cert_fm_usereib = "Entity Name (CORECA)";
    var cert_fm_date_approve_fee = "Approval Time (Certificate)";
    var cert_fm_user_fee = "Approval User (Certificate)";
    var cert_succ_edit = "Update Certificate Successful";
    var cert_succ_returnfee = "Update Successful";
    var cert_fm_Status = "Certificate Status";
    var cert_fm_push_notice = "Allow To Send Email";
    var cert_fm_revoke_delete = "Delete revoked certificate";
    var cert_fm_restore_status_old = "Restore the operation state of the old certificate";
    var cert_fm_revoke_delete_old = "Delete the old certificate when new certificate is issued";
    var cert_confirm_otp_sendmail = "Do You Want To Send The Activation Code?";
    var cert_succ_otp_resend = "Send the activation code successfully";
    var global_error_appove_status = "Approval Status Invalid. Approve Failure";
    var global_error_method = "Invalid method";
    var global_error_keysize_csr = "Invalid CSR key length";
    var global_error_exist_csr = "CSR already exists in the system";
    var global_fm_button_renewal = "Add Compensation";
    var info_fm_cert_profile = "Certificate Profile (CORECA)";
    var info_fm_profile_name = "Certificate Type";
    var info_fm_type_request = "Request Type";
    var global_fm_renew_access = "The profile is applying for certificate renewal";
    var global_fm_renew_access_search = "Search by profiles is applying for certificate renewal";
    var global_fm_csr_info_cts = "Enroll Certificate Request Infomation";
    var global_fm_san_info_cts = "Additional Certificate Information";
    var global_fm_csr_info_cts_before = "Certificate Information before change";
    var global_fm_csr_info_cts_after = "Certificate Information after the change";
    var global_fm_Corporation = "Corporation";
    var global_group_cert = "Certificate Detail";
    var info_group_info = "Request Detail";
    //revoke cert
    var revoke_title_list = "Certificate Revoke Management";
    var revoke_title_detail = "Detail";
    var revoke_title_search = "Search";
    var revoke_title_table = "List";
    var global_fm_button_revoke = "Revoke";
    var global_fm_button_recovery = "Recovery";
    var global_fm_button_suspend = "Suspend";
    var global_fm_button_reissue = "Token ReIssue";
    var global_fm_button_detail = "Detail";
//    if(IsWhichCA === "18") {
//        global_fm_button_detail = "DETAIL";
//    }
    var global_fm_button_print_report = "Print Report";
    var global_fm_button_print_certificate = "Print Certification";
    var global_fm_button_print_handover = "Print Handover";
    var global_fm_button_print_regis = "Print Request";
    var global_fm_button_print_confirm = "Confirm Print";
    var global_fm_button_print = "Print";
    var global_fm_button_export_zip_word = "Zip File Word";
    var global_fm_button_export_zip_pdf = "Zip File PDF";
    var global_fm_button_regis = "Register";
    var global_fm_button_regis_soft = "Soft Token";
    var global_fm_button_re_regis = "Re-Register";
    var info_group_approve = "Approval Detail";
    var global_fm_approve = "Approve";
    var global_fm_approve_ca = "CA level approval";
    //report cert
    var certreport_title_list = "Certificate Report Management";
    var certreport_title_search = "Search";
    var certreport_title_table = "List";
    //request -> RegistrationCertificate
    var regiscert_title_list = "Certificate Registration Management";
    var regiscert_title_token_list = "Certificate Registration for the Token";
    var regiscert_soft_title_list = "Soft Token Certificate Registration Management";
    var regiscert_title_search = "Search";
    var regiscert_title_table = "List";
    var regiscert_title_view = "Registration Information";
    var buymorecert_title_view = "Register to buy more certificates";
    var regiscert_fm_datelimit_one = "1 Years";
    var regiscert_fm_datelimit_two = "2 Years";
    var regiscert_fm_datelimit_three = "3 Years";
    var regiscert_fm_check_backup_key = "Backup key on Server";
    var regiscert_fm_check_revoke = "Revoke certificate after changing infomation";
    var regiscert_fm_check_revoke_reissue = "Revoke certificate after re-issuing";
    var regiscert_fm_check_change_key = "Change Key";
    var regiscert_fm_keep_certsn = "Keep Certificate SN";
    var regiscert_succ_add = "Registration Successful";
    var regisapprove_title_list = "Certificate Registration Approve Management";
    var regisapprove_title_view = "Certificate Registration Approve";
    var approve_fm_confirm_mail = "Request confirmation via Email";
//    var regisapprove_error_status = "Existed Token SN In List, Certificate Registration failure";
    var regisapprove_succ_approve = "Approve Successful";
    //request -> tokenimport
    var tokenimport_title_list = "Token Import Management";
    var tokenimport_title_import = "Token Import";
    var tokenimport_title_search = "Search";
    var tokenimport_title_table = "List";
    var tokenimport_succ_edit = "Import Token Successful";
    var tokenimport_succ_add_renew = "Auto Renewal Successful";
    var tokenimport_fm_fromtokenSN = "Token SN (Begin)";
    var tokenimport_fm_totokenSN = "Token SN (End)";
    
    //token -> TokenActionImport
    var actionimport_title_list = "Manage import of Token editing list";
    var actionimport_title_import = "Token Import";
    var actionimport_title_search = "Search";
    var actionimport_title_table = "List";
    var actionimport_succ_edit = "Upload the completed";
    
    //cert -> certimport
    var certimport_title_list = "Certificate Import Management";
    var certimport_title_import = "Certificate Import";
    var certimport_file_format_invalid = "The file format is incorrect. Import Failure";
    var certimport_fm_error = "Error import: ";
    var certimport_error_not_size = "Maximum number of certificates to import from excel files: ";
    var certimport_error_not_ca = "The account does not have permission to import certificates from excel files";
    var certimport_error_not_format_file = "The system only supports file formats: XLS, XLSX, CSV";
    
    // new before translase
    var tokenimport_title_multi = "Multiple Token Setting";
    var tokenimport_fm_createdate_search = "Create Time";
    var tokenimport_fm_tokensn_search = "Token SN";
    var tokenimport_fm_result = "Import Result";
    var tokenimport_fm_createdate_tokensn_search = "Create Time v?? Token SN";
    //Request -> LogtList
    var log_title_list = "Decline Management";
    var log_table_list = "List";
    var log_title_search = "Search";
    var log_title_view = "Decline Detail";
    var log_fm_user_detete_request = "Decline User";
    var log_fm_date_detete_request = "Decline Time";
    //Request -> RequestList
    var request_title_list = "Request Management";
    var request_title_search = "Search";
    var request_table_list = "List";
    var request_title_view = "Detail";
    var request_conform_delete = "Do You Want To Decline ?";
    var request_conform_revoke = "Do You Want To Revoke ?";
    var request_succ_delete = "Decline Successful";
    var request_error_delete = "Approved, Decline Failure";
    //token -> backofficelog
    var backoffice_title_list = "Token Configuration";
    var backoffice_title_search = "Search";
    var backoffice_title_table = "List";
    var backoffice_title_view = "Detail Infomation";
    var global_fm_combox_true = "Yes";
    var global_fm_combox_false = "No";
    var global_req_enter_info_change = "Please select at least one information to change";
    var global_req_format_url = "Enter the correct format: ";
    var global_error_wrong_agency = "Access Deny To This Agency, Please check again";
    var global_error_wrong_role = "Access Deny To This Role, Please check again";
    
    //token -> pushimport
    var pushimport_title_list = "Manage notifications import";
    var pushimport_title_import = "Notifications Import";
    var pushimport_succ_edit = "Upload the completed notification list";
    var pushimport_succ_conform_down = "Download the results to the computer?";
    var pushimport_fm_set_push = "Update the notification by list";
    var pushimport_fm_delete_push = "Delete the notification by list";
    var pushimport_fm_text_push = "Notification content";
    var pushimport_fm_link_push = "Notification Link";
    //token -> collectprofile
    var collectimport_title_list = "Import control list updates";
    var collectimport_title_import = "Upload the updated control list";
    var collectimport_fm_set_push = "Updates have been checked";
    var collectimport_fm_delete_push = "Update without control";
    var collectimport_fm_control_cert = "Control of certificates";
    var collectimport_fm_control_profile = "Control records";
    
    //cert -> ImportDisallowanceList
    var disallowanceimport_title_list = "Blacklist Management";
    var disallowanceimport_title_import = "Blacklist Upload";
    var disallowanceimport_succ_edit = "Upload the completed Blacklist";
    var disallowanceimport_succ_conform_down = "Download the results to the computer?";
    var disallowanceimport_fm_set_push = "Update by list";
    var disallowanceimport_fm_delete_push = "Delete by list";
    var disallowanceimport_fm_title_blacklist = "Export blacklist CSV file";
    var disallowanceimport_fm_title_contact = "Export customer contact CSV file";
    var disallowanceimport_fm_contact_email = "Email Information";
    var disallowanceimport_fm_contact_phone = "Phone Information";
    var disallowanceimport_fm_note_blacklist = "Note: Export CSV file with blacklist currently available in the system";
    var disallowanceimport_fm_note_contact = "Note: Export the CSV file to list the contact information of the customer who registered the certificate in the system";
    
    // NO_TRANSLATE
    //Token -> token Approve
    var tokenapprove_title_list = "Token Request Management";
    var tokenapprove_table_list = "Token Management List";
    var tokenapprove_title_edit = "Token Management Detail";
    //Certificate -> Template DN
    var tempdn_title_list = "Certificate Template";
    var tempdn_title_table = "Danh s??ch tr?????ng";
    var tempdn_title_table = "Fields";
    var tempdn_group_Role = "Choose Certificate";
    var tempdn_group_assign = "Unassigned Fields";
    var tempdn_table_assigned = "Assigned Fields";
    var tempdn_conform_delete = "Do You Want To Delete This Field ?";
    var tempdn_succ_delete = "Deleted Field Successfully";
    var tempdn_succ_insert = "Added Field Successfully";
    var tempdn_succ_edit = "Configured Fields Successfully";
    var tempdn_error_edit = "Non-existed Fields";
    var tempdn_error_delete = "Deleted Field Failure";
    var tempdn_error_insert = "Added Field Failure";
    var global_fm_certtype = "Certificate Type";
    if(IsWhichCA === "18") {
        global_fm_certtype = "Certificate Type";
    }
    var global_fm_subjectdn = "Field";
    var global_fm_required = "Required";
    var global_fm_prefix = "Prefix";
    var global_fm_profile_signature = "Digital signature";
    var global_fm_profile_scan = "Scans";
    var global_fm_profile_paper = "Paper version";
    var global_fm_request_function = "Request Type";
    var token_confirm_cancel_request = "Do You Want To Decline This Request ?";
    var token_confirm_issue_request = "Do You Want to issue this certificate ?";
    var token_succ_cancel_request = "Decline Request Successfully";
    var global_fm_button_decline = "Decline";
    var global_fm_button_issue = "Issue Certificate";
    var global_fm_status_pendding = "Pre-Approval Request";
    var global_fm_status_approved = "Post-Approval Request";
    var global_tooltip_decline_request_token = "Request Approved, It Could Not Be Declined";
    var token_group_unlock = "Relevant Contact";
    var global_fm_duration_cts = "Certificate Service Package";
    var global_fm_duration_cts_choose = "Choose a service pack";
    var global_fm_rssp_authmodes = "Authentization Modes";
    var global_fm_rssp_signning_profiles = "Signing Profile";
    var global_fm_rssp_replying_party = "Replying Party";
    var global_fm_percent_cts = "Value of commission";
    var global_fm_rose_type = "Type of commission";
    var global_fm_rose_type_percen = "Percentage (%)";
    var global_fm_rose_type_money = "Amount of money";
    var global_fm_decline_desc = "Decline Reason";
    var global_fm_revoke_desc = "Revoke Reason (User)";
    var global_fm_dipose_desc = "Dipose Reason";
    var global_fm_suspend_desc = "Suspended Reason";
    var global_fm_revoke_reason_core = "Revoke Reason (CoreCA)";
    var global_fm_MNS = "Budget Code";
    var global_fm_HC = "Passport";
    var global_fm_CitizenId = "Citizen Identity";
    var global_fm_requesttype = "Request Type";
    var token_fm_choose_noticepush = "Choose display changes";
    var token_fm_set_no_noticepush = "Set Token default display";
    var token_fm_set_no_dynamic = "Disable Dynamic Link";
    var token_group_choose_dynamic = "Dynamic Link Configuration";
    var global_fm_button_renew = "Renewal";
    var global_fm_button_buymore = "Buy more";
    var global_fm_button_changeinfo = "Change";

    //Certificate -> RenewCertList
    var certlist_title_list = "Certificate Management";
    var certlist_title_search = "Search";
    var certlist_title_table = "List";
    var certlist_title_renew = "Certificate Renewal";
    var certlist_title_reissue = "Certificate Reissuance";
    var certlist_title_revoke = "Certificate Revocation";
    
    //Certificate -> CertificateShareList
    var certsharelist_title_list = "Additional management of service information";
    var certsharelist_title_search = "Search";
    var certsharelist_title_table = "List";
    
    var certlist_title_recovery = "Certificate Recovery";
    var certlist_title_suspend = "Certificate Suspend";
    
    var certlist_group_renew = "Certificate Renewal Detail";
    var certlist_group_reissue = "Certificate Reissuance Detail";
    var certlist_title_changeinfo = "Certificate Detail Configuration";
    var certlist_group_changeinfo = "Detail Change";
    var certlist_group_sender = "Sender";
    var certlist_group_add_info = "Additional Information";
    var certlist_group_add_bussiness_info = "Company Information";
    var certlist_group_add_buss_pers_info = "Company, Personal Information";
    var certlist_group_return_contact_info = "Contact information to return records";
    var certlist_group_add_personal_info = "Personal Information";
    var certlist_group_add_bussiness_contact = "Contact Information";
    var certlist_group_receiver = "Receiver";
    var certlist_fm_unnamed = "Anonymous Certificate";
    var certlist_title_detail = "Certificate Detail";
    var certlist_title_print_hadover = "Print delivery records";
    var certlist_title_print_register = "Print Registration";
    var certlist_title_print_changeinfo = "Print Change Infomation";
    var certlist_title_print_reissue_revoke = "Print Revoke and Reissue Infomation";
    var certlist_title_detail_old = "Certificate Old Detail";
    var certlist_succ_renew = "Certificate Renewal Sucessfully";
    var certlist_succ_reissue = "Certificate Reissuance Successfully";
    var certlist_succ_revoke = "Certificate Revocation Successfully";
    var certlist_succ_revoke_ca = "Certificate Revocation Successfully";
    var certlist_succ_changeinfo = "Certificate Configuration Successfully";
    var certlist_succ_changepass_p12 = "Change P12 password successfully";
    var certlist_error_changepass_p12 = "Password change P12 failed";
    
    var certlist_succ_recovery = "Certificate Recovered Successfully";
    var certlist_succ_recovery_ca = "Certificate Recovered Successfully";
    var certlist_succ_suspend = "Certificate Suspended Successfully";
    var certlist_succ_suspend_ca = "Certificate Suspended Successfully";
    var certlist_fm_device_uuid = "Device UID";
    
    //owner -> owner
    var owner_title_list = "Certificate Owner Management";
    var owner_title_search = "Search";
    var owner_title_table = "List";
    var owner_title_renew = "Certificate Renewal";
    var owner_title_reissue = "Certificate Reissuance";
    var owner_title_revoke = "Certificate Revocation";
    var owner_title_recovery = "Certificate Recovery";
    var owner_title_suspend = "Certificate Suspend";
    var owner_title_add = "Register Owner";
    var owner_title_view = "Owner Information";
    var owner_succ_add = "Register Successfully";
    var owner_succ_edit = "Config Successfully";
    var ownerapprove_title_list = "Owner Approval Management";
    var owner_fm_type = "Owner Type";
    /// no translase
    var owner_title_dispose = "Owner Dispose";
    var owner_title_change = "Change Owner Information";
    var owner_succ_dispose = "Request sent successfully";
    var owner_fm_private_uid = "Identification Information";
    var owner_succ_approve = "Approve Successful";
    var owner_title_cert_search = "Searching for the owner";
    
    // monitor -> serverlog
    var serverlog_title_list = "Server Log Management";
    var serverlog_title_todate = "Server Log Today";
    var serverlog_title_down = "Download Server Log";
    var serverlog_fm_typelog = "System";
    var serverlog_fm_numberlog = "Number Of Line";
    var serverlog_fm_timestamp = "Time";
    var serverlog_fm_detail = "Log Detail";
    var hastatus_fm_auto = "Automatically Reload Information (Seconds)";
    //
    var global_error_promotion_package_limit = "Promotion period cannot be greater than the duration of the profile";
    var global_error_amount_package_limit = "Charged Amount Is Not Reasonable";
    var global_fm_token_status_configed = "Current Token Configuration";
    var certlist_group_token_new = "New Token SN";
    var global_error_noexists_token = "Non-Existed Token";
    var global_error_token_status = "Invalid Token Status. Please Have A Check";
    var global_error_coreca_call_approve = "An error occurred when calling through CoreCA. Please check again";
    var global_error_exists_equals_dn = "The information doesn't include the = character";
    var branch_title_table = "List";
    var branch_title_info = "Agency Information";
    var branch_fm_representative = "Representative";
    var branch_fm_representative_position = "Representative Position";
    var branch_fm_logo = "Agency Logo";
    
    // branch access
    var branch_fm_profile_title_access = "API Config";
    var branch_fm_api_title_access = "API Access";
    var branch_fm_profile_group_profile = "Profile access";
    var branch_fm_profile_group_formfactor = "Method access";
    var branch_fm_api_tag_credential = "SOAP-API Credential";
    var branch_fm_rest_tag_credential = "REST-API Credential";
    var branch_fm_api_tag_ip = "IP Access";
    var branch_fm_api_tag_function = "Function Access";
    var branch_fm_api_allow_access = "Allow API configuration";
    var branch_fm_api_signture = "Signature";
    var branch_fm_api_publishkey = "Public Key";
    var branch_fm_check_reload_cert = "Choose to refresh the configuration of an existing profiles";
    
    var tokentransfer_title_list = "Transfer Token Management";
    var tokentransfer_title_search = "Search";
    var tokentransfer_title_table = "List";
    var certprofile_title_search = "Search";
    var certprofile_title_table = "List";
    var global_error_request_exists = "The pending request is already existed. Add Failure";
    var global_error_cert_exists_token = "Token has existed a cetificate. Your reuqest has failed";
    var global_error_request_exists_token = "The token has a pending request. Your request has failed";
    var global_error_approve_exists_cert = "Token has existed a cetificate. Approve Failure";
    var global_error_credential_external_invalid = "External system credentials are not valid, please check again.";
    
    var tokenexport_title_list = "Export the Token list";
    var tokenexport_title_search = "Search";
    var tokenexport_title_table = "List";
    
    var global_fm_checkbox_gcndk = "Certificate of business registration";
    var global_fm_checkbox_GPDT = "Investment License";
    var global_fm_checkbox_QDTL = "Decide to establish";
    var global_fm_choise = "Choose";
    var branch_fm_logo_note = "Note: The background image is transparent, size (210px width and 70px height), size < 500 KB, format: png, jpg, gif";
    var branch_fm_logo_down = "Download Logo Template";
    var branch_error_logo_great_size = "Please select images less than 500 KB";
    var branch_fm_logo_change = "Select changes for Logo";
    var branch_fm_logo_default = "Set Default";
    
    var global_succ_mst_register = "Tax Code is already registered";
    var global_succ_mns_register = "Budget Code is already registered";
    var global_succ_cmnd_register = "Personal ID is already registered";
    var global_succ_hc_register = "Passport is already registered";
    // NO_TRANSLATE 20180828
    var reportquick_fm_innit = "Certificate Initialization";
    var reportquick_fm_activation = "Certificate Operation";
    var reportquick_fm_revoke = "Certificate Revocation";
    var reportquick_fm_total = "Total";
    var global_fm_cert_list = "Certificate List";
    var reportquick_title_list = "Report Certificate";
    var reportquick_table_search = "Search";
    var reportquick_title_add = "Report Certificate";
    var reportquick_title_edit = "Report Certificate";
    var global_fm_date_approve_agency = "Agency Date of approval";
    var global_fm_user_approve_agency = "Agency User of approval";
    var global_fm_date_approve = "Date of approval";
    var global_fm_user_approve = "User of approval";
    var global_fm_date_approve_ca = "CA Date of approval";
    var global_fm_user_approve_ca = "CA User of approval";
    var global_error_not_user_create = "Create User doesn't exists";
    var global_succ_delete = "Delete Successfully";
    var global_error_delete = "Delete failure";
    // File management
    var file_succ_delete = "Delete Successfully";
    var file_conform_delete = "Do You Want To Delete File ?";
    var file_conform_upload = "Do You Want To Upload File ?";
    
    // NO_TRANSLATE 20180906
    var reportcertlist_title_list = "Certificate List Report";
    var reportcertlist_table_search = "Search";
    
    var reportcertexpire_title_list = "Certificate Expire";
    var reportcertexpire_table_search = "Search";
    
    // NO_TRANSLATE collation
    var collation_title_list = "Collation Data Management";
    var collation_fm_collated = "Collated";
    var collation_fm_uncollated = "Not Collated";
    var collation_button_change = "Change Collation Status";
    var collation_fm_change = "Change state";
    var collation_button_rose_agent = "Update commision";
    var collation_fm_change_change = "Status change date";
    var collation_fm_mounth = "Collation month";
    var collation_fm_time = "Collation date";
    var collation_fm_user = "Control user";
    var collation_fm_date_receipt = "Receipt date of collation";
    var collation_fm_type = "Form of record collection";
    var collation_fm_type_inmounth = "Records for the month";
    var collation_fm_type_compensation = "Compensation records";
    var collation_fm_date_compensation = "Date of compensation profile";
    var collation_alert_type_inmounth = "The profile missing in the month";
    var collation_alert_type_compensation = "Profiles are compensated in the month";
    var collation_fm_profile_overdue = "Profile overdue";
    var collation_fm_unapproved_profile = "Profile has unread files";
    var collation_fm_approved_profile = "File has read the new file";
    var collation_fm_money_overdue = "The amount of the overdue fine";
    var collation_fm_print_DK = "Registration form";
    var collation_fm_print_Confirm = "Certification form";
    var collation_fm_print_GPKD = "Business license form";
    var collation_fm_print_CMND = "ID Card form";
    
    var profile_title_list = "Profile management";
    var profile_title_detail = "Profile details";
    var profile_title_import_list = "Import profile information";
    var profile_fm_enoughed = "Reconciled records";
    var profile_fm_unenoughed = "Unreconciled records";
    var profile_conform_update = "Are you sure you update status of profile completion ?\nThe profile will not be updated again.";
    
    var inputcertlist_title_list = "Debt Reference Information";
    var inputcertlist_table_search = "Search";
    var inputcertlist_succ_edit = "Config Successfully";
    var inputcertlist_succ_add = "Add Successfully";
    var global_fm_monthly = "Monthly";
    var global_fm_title_push_approve1 = "There are ";
    var global_fm_title_push_approve2 = " requests waiting for issuing certificates";
    var global_fm_title_push_decline = " requests for decline of issuing certificates";
    // ICA
    var global_error_revoke_forbiden = "The certificate cannot be revoked 2 times in a row, please check again";
    var global_error_revoke_limit = "Revoke of certificates exceeding the specified number of times per month, please contact CA";
    var global_fm_limit_revoke = "Revoke limit for the month";
    var global_fm_RP_access_esign = "Permission access to the integrated channel eSigncloud";
    var global_fm_login_form = "SIGN IN";
    var global_fm_address_GPKD = "Address (Business license)";
    var global_fm_CitizenId_I = "Citizen ID";
    var global_fm_browse_file_upload = "Up to ";
    var global_fm_button_add_simple = " Add ";
    var global_fm_button_add_action = "ADD TRANSACTION";
    var global_fm_button_print_profile = "PRINT PROFILE";
    var global_fm_button_off_notice = "Turn off notifications";
    var global_fm_sign = "Sign";
    var file_conform_signprofile = "Do you want to sign this file ?";
    var fm_succ_signprofile = "Sign the file successfully";
    var global_fm_remark_agency_en = "Agent name (English)";
    var global_fm_remark_agency_vn = "Agent name (Vietnamese)";
    var global_fm_identifier_type = "Identifier type";
    if(IsWhichCA !== "18"){
        global_fm_identifier_type = "Business UID";
    }
    var global_fm_document_type = "Document type";
    if(IsWhichCA !== "18") {
        global_fm_document_type = "Personal UID";
    }
    var global_fm_enter = "Enter ";
    var global_fm_enter_number = "Enter number ";
    var request_conform_approve = "Do You Want To Approve ?";
    var certlist_title_detail_current = "Certificate Current Detail";
    var global_fm_register_date = "Select registration date";
    var global_fm_register_info = "Get a digital certificate according to the information below";
    
    //request -> historylist
    var history_title_list = "History Management";
    var history_title_search = "History Search";
    var history_title_table = "History List";
    var history_title_detail = "History Detail";
    var history_fm_response = "Status";
    var history_fm_function = "Functionality";
    var history_fm_request_data = "Resquest Data";
    var history_fm_response_data = "Response Data";
    var history_fm_request_ip = "IP";
    var history_fm_source_entity = "System Implementation";
    
    var reportneac_title_list = "Report NEAC";
    var reportneac_title_search = "Search";
    var reportneac_title_table = "List";
    var reportneac_fm_tab_control = "Control Report";
    var reportneac_fm_tab_recurring = "Periodic Report";
    var reportneac_fm_tab_cts_signserver = "Certificate SignServer";
    var reportneac_fm_tab_cts_token = "Certficate List";
    var global_fm_cert_count = "Number of Certificates";
    var reportneac_fm_cert_personal = "Personal Certificate";
    var reportneac_fm_cert_enterprise = "Business Certificate";
    var reportneac_fm_cert_staff = "Staff Certificate";
    var reportneac_fm_control_content = "The number of certificates granted by public authorities to subscribers is organizations, enterprises (excluding personal) from 01/01/2017 and still valid up to date ";
    var global_fm_report_date = "........., day.....month.....year ......";
    var global_fm_report_print_date = "........., day [DD] month [MM] year [YYYY]";
    var global_fm_report_print_only_date = "day [DD] month [MM] year [YYYY]";
    if(IsWhichCA === "14" || IsWhichCA === "7") {
        global_fm_report_print_date = "Day [DD] month [MM] year [YYYY]";
    }
    var global_fm_choose_cert = "Select Certificate";
    var global_fm_unchoose_cert = "Uncheck Certificate";
    var global_fm_login_ssl = "Login Mechanism Via Token Device";
    var global_ssl_conform_delete = "Do You Want To Uncheck Certificate ?";
    var global_confirm_print_register = "Do you want to print registration form ?";
    var global_confirm_print_renew = "Do you want to print renewal form ?";
    // send mail hsm
    var hsm_confirm_cert_actived = "The certificate has been activated";
    var hsm_confirm_data_not_found = "No certificate activation information found. Please contact your service provider";
    var hsm_confirm_url_invalid = "Invalid activation path";
    var hsm_confirm_time_expire = "The required activation time has expired. Please contact your service provider";
    var hsm_confirm_encryption_notfound = "Encoding string not found";
    var hsm_confirm_acteve_status_invalid = "Invalid activation request status, please contact service provider";
    var hsm_confirm_cert_issue_error = "The certificate issue error, please contact service provider";
    var hsm_confirm_request_declined = "The request has been declined to activate the certificate.<br />Time: [TIME]. Reason for rejection: [REASON]";
    var hsm_confirm_request_type_invalid = "Invalid request type";
    var hsm_confirm_actived_success = "Certificate of successful activation.\nPlease check your E-Mail to receive the digital certificate";
    var hsm_confirm_declined_success = "Deactivation of certificate successfully";
    var hsm_confirm_title_page = "You have a request to activate the issuance of HSM digital certificates, please check the information and confirm for the system to issue digital certificates:";
    var hsm_confirm_note_page = "Note: OK (agree to activate digital certificates); Decline (disagree to activate digital certificates)";
    
    // footer page
    var footer_name = "";
    var footer_name_inner = "";
    var footer_address = "";
    var footer_office = "";
    var footer_email = "";
    var header_hotline = "";
    var footer_hotline = "";
    
    var footer_name_minvoice = "";
    var footer_name_inner_minvoice = "";
    var footer_address_minvoice = "";
    var footer_email_minvoice = "";
    var header_hotline_minvoice = "";
    var footer_hotline_minvoice = "";
    if(IsWhichCA === "1") {
        footer_name = "2018 - {DATE_YEAR} ?? Vietnam EFY Informatics Technology JSC";
        footer_name_inner = "2018 - {DATE_YEAR} ?? ";
        footer_address = "Address: T???ng 9 t??a nh?? Sannam, s??? 78 Duy T??n, ph?????ng D???ch V???ng H???u, C???u Gi???y, H?? N???i";
        footer_email = "efy@ihd.vn";
        header_hotline = "1900 6142 - 1900 6139";
        footer_hotline = "1900 6142 - 1900 6139";
    } else if(IsWhichCA === "2") {
        footer_name = "2018 - {DATE_YEAR} ?? FEITIAN Technologies Co.,Ltd.";
        footer_name_inner = "2018 - {DATE_YEAR} ?? ";
        footer_address = "Address: Tower B, Huizhi Mansion, No.9 Xueqing Road, Haidian District, 100085 Beijing, China";
        // footer_email = "email@ihd.vn";
        header_hotline = "+86 10 6230 4466";
        footer_hotline = "+86 10 6230 4466";
    } else if(IsWhichCA === "3") {
        footer_name = "Copyright ?? 2019 - {DATE_YEAR} Mobile-ID Technologies and Services Joint Stock Company";
        footer_name_inner = "2019 - {DATE_YEAR} ?? ";
        footer_address = "Address: 19 Dang Tien Dong, An Phu Ward, District 2nd, Ho Chi Minh City, Vietnam";
        footer_email = "info@mobile-id.vn";
        header_hotline = "1900 6884";
        footer_hotline = "1900 6884";
    } else if(IsWhichCA === "4") {
        footer_name = "Copyright ?? 2019 - {DATE_YEAR} MISA JSC";
        footer_name_inner = "2019 - {DATE_YEAR} ?? ";
        footer_address = "Address: MISA Building, Quang Trung software city, 49 To Ky, Tan Chanh Hiep Ward, 12th District, HCM city";
        footer_email = "esales@han.misa.com.vn";
        header_hotline = "1900 8677";
        footer_hotline = "1900 8677";
    } else if(IsWhichCA === "5") {
        footer_name = "Copyright ?? 2019 - {DATE_YEAR}. All Rights Reserved";
        footer_name_inner = "2019 - {DATE_YEAR} ?? ";
        footer_address = "Address: Floor 9, Viet A Building, No. 9, Duy Tan Street, Dich Vong Hau Ward, Cau Giay District, Hanoi";
        footer_email = "info@savis.com.vn";
        header_hotline = "+(84-24) 3782 2345";
        footer_hotline = "+(84-24) 3782 2345";
    } else if(IsWhichCA === "6") {
        footer_name = "Copyright ?? 2019 - {DATE_YEAR} NewTel-CA. All Rights Reserved";
        footer_name_inner = "2019 - {DATE_YEAR} ?? ";
        footer_address = "Address: Room 305, GP Invest Building, No. 170 De La Thanh, Dong Da District, Hanoi";
        footer_email = "info@newca.vn";
        header_hotline = "+(84-24) 38374999";
        footer_hotline = "+(84-24) 38374999";
    } else if(IsWhichCA === "7") {
        footer_name = "Copyright ?? 2019 - {DATE_YEAR} NC-CA. All Rights Reserved";
        footer_name_inner = "2019 - {DATE_YEAR} ?? ";
        footer_address = "Address: 8th Floor, Newhouse Xala Building, Xala Urban Area, Ha Dong, Hanoi";
        footer_email = "info@nc-ca.com.vn";
        header_hotline = "+(84-24) 6297 1010";
        footer_hotline = "+(84-24) 6297 1010";
    } else if(IsWhichCA === "8") {
        footer_name = "Copyright ?? 2020 - {DATE_YEAR} CMC TECHNOLOGY AND SOLUTION COMPANY LIMITED";
        footer_name_inner = "2020 - {DATE_YEAR} ?? ";
        footer_address = "Address: floors 14-16, CMC Building, 11 Duy Tan Street, Cau Giay District, Hanoi";
        footer_email = "ca-support@cmc.vn";
        header_hotline = "1900 2323 62";
        footer_hotline = "Technical support: 024 3972 2425";
    } else if(IsWhichCA === "9") {
        footer_name = "Copyright ?? 2020 - {DATE_YEAR} VG-CA";
        footer_name_inner = "2020 - {DATE_YEAR} ?? ";
        footer_address = "Address: 23 Nguy Nhu Kon Tum, Thanh Xuan District, Hanoi City";
        footer_email = "ca@bcy.gov.vn";
        header_hotline = "(+84.24) 37738668";
        footer_hotline = "(+84.24) 37738668";
    } else if(IsWhichCA === "10") {
        footer_name = "Copyright ?? 2020 - {DATE_YEAR} FPT-CA.COM.VN";
        footer_name_inner = "2020 - {DATE_YEAR} ?? ";
        footer_address = "Address: 6th Floor, Saigon Prime Building, 107-109-111 Nguyen Dinh Chieu, P6, Q3, HCMC";
        footer_email = "kinhdoanh@fpt-ca.com.vn";
        header_hotline = "0911666467";
        footer_hotline = "0911666467";
    } else if(IsWhichCA === "11") {
        footer_name = "Copyright ?? 2020 - {DATE_YEAR} SoftDreams";
        footer_name_inner = "2020 - {DATE_YEAR} ?? ";
        footer_address = "Address: ATS guest house, 8 Pham Hung, Me Tri Ward, Nam Tu Liem District, Hanoi";
        footer_email = "contact@softdreams.vn";
        header_hotline = "1900 56 56 53";
        footer_hotline = "1900 56 56 53";
    } else if(IsWhichCA === "12") {
        footer_name = "Copyright ?? 2020 - {DATE_YEAR} LCS-CA Co., Ltd";
        footer_name_inner = "2020 - {DATE_YEAR} ?? ";
        footer_address = "Address: 210/16A Cach Mang Thang 8, Ward 10, District 3, Ho Chi Minh City";
        footer_email = "hotro@lcs-ca.vn";
        header_hotline = "1900 4533";
        footer_hotline = "1900 4533";
    } else if(IsWhichCA === "13") {
        footer_name = "Copyright ?? 2020 - {DATE_YEAR} VIETTELCA.VN";
        footer_name_inner = "2020 - {DATE_YEAR} ?? ";
        footer_address = "Address: No. 1, Tran Huu Duc Street, My Dinh 2 Ward, Nam Tu Liem District, Hanoi City";
        footer_email = "lienhe@viettelca.vn";
        header_hotline = "1800 8000";
        footer_hotline = "1800 8000";
    } else if(IsWhichCA === "14") {
        footer_name = "Copyright ?? 2020 - {DATE_YEAR} KHANH LINH CONSULTANT - SERVICE COMPANY LIMITED";
        footer_name_inner = "2020 - {DATE_YEAR} ?? ";
        footer_address = "Address: 232/17 Cong Hoa, Ward 12, Tan Binh District, Ho Chi Minh City";
        footer_email = "info@ketoanvn.com.vn";
        header_hotline = "1900 1129";
        footer_hotline = "1900 1129";
    } else if(IsWhichCA === "15") {
        footer_name = "Copyright ?? 2018 - {DATE_YEAR} Lao National Root Certificate Authority";
        footer_name_inner = "2018 - {DATE_YEAR} ?? ";
        footer_address = "Address: Saylom village, Chanthabouli district, Vientiane Capital, Lao PDR";
        footer_email = "lanic_office@lanic.la";
        header_hotline = "+856 254150";
        footer_hotline = "+856 254150, PO Box: 2225";
    } else if(IsWhichCA === "16") {
        footer_name = "Copyright ?? 2013 - {DATE_YEAR} SAFECert Corp";
        footer_name_inner = "2018 - {DATE_YEAR} ?? ";
        footer_address = "Address: X-04.77, North Towers Building, Sunrise City, 27 Nguyen Huu Tho, Tan Hung Ward, District 7, City. Ho Chi Minh City";
        footer_email = "info@safecert.com.vn";
        header_hotline = "(028)-668-23732";
        footer_hotline = "(028)-668-23732";
        global_fm_decision = "Unit Code";
    } else if(IsWhichCA === "17") {
        footer_name = "Copyright ?? 2018 - {DATE_YEAR} Lao National Root Certificate Authority";
        footer_name_inner = "2018 - {DATE_YEAR} ?? ";
        footer_address = "Address: Saylom village, Chanthabouli district, Vientiane Capital, Lao PDR";
        footer_email = "lanic_office@lanic.la";
        header_hotline = "+856 254150";
        footer_hotline = "+856 254150, PO Box: 2225";
    } else if(IsWhichCA === "18") {
        footer_name = "ICORP JOINT STOCK COMPANY";
        footer_name_minvoice = "M-INVOICE ELECTRONIC INVOICE CO., LTD";
        footer_name_inner = "2021 - {DATE_YEAR} ?? ";
        footer_name_inner_minvoice = "2021 - {DATE_YEAR} ?? ";
        footer_office = "Sales Office: Room 1212 Tower A, The Park Home Building, No. 1 Thanh Thai Street, Dich Vong Ward, Cau Giay District, Hanoi";
        footer_address = "Address: 32/21 Truong Cong Giai Street, Dich Vong Ward, Cau Giay District, City. Hanoi City";
        footer_address_minvoice = "Address: Dich Vong Hau Ward, Cau Giay District, Hanoi City";
        footer_email = "ica@icorp.vn";
        footer_email_minvoice = "cskh@minvoice.vn";
        header_hotline = "1900 0099";
        header_hotline_minvoice = "098111111";
        footer_hotline = "1900 0099";
        footer_hotline_minvoice = "098111111";
    } else if(IsWhichCA === "19") {
        footer_name = "Copyright ?? 2019 - {DATE_YEAR} Mobile-ID Technologies and Services Joint Stock Company";
        footer_name_inner = "2019 - {DATE_YEAR} ?? ";
        footer_address = "Address: 19 Dang Tien Dong, An Phu Ward, District 2nd, Ho Chi Minh City, Vietnam";
        footer_email = "info@mobile-id.vn";
        header_hotline = "1900 6884";
        footer_hotline = "1900 6884";
    } else if(IsWhichCA === "20") {
        footer_name = "Copyright ?? 2019 - {DATE_YEAR} T-VAN HILO SERVICE JOINT STOCK COMPANY";
        footer_name_inner = "2019 - {DATE_YEAR} ?? ";
        footer_address = "Address: No. 2/95 Chua Boc, Dong Da District, Hanoi City, Vietnam";
        footer_email = "support@hilo.com.vn";
        header_hotline = "1900 2929 62";
        footer_hotline = "1900 2929 62";
    } else if(IsWhichCA === "21") {
        footer_name = "Copyright ?? 2019 - {DATE_YEAR} MobiFone IT Center";
        footer_name_inner = "2019 - {DATE_YEAR} ?? ";
        footer_address = "Address: No. 5, Lane 82 Duy Tan, Cau Giay District, Hanoi City, Vietnam";
        footer_email = "contact-itc@mobifone.vn";
        header_hotline = "0936 110 116";
        footer_hotline = "0936 110 116";
    } else if(IsWhichCA === "22") {
        footer_name = "Copyright ?? 2022 - {DATE_YEAR} Mat Bao Company. All Reserved";
        footer_name_inner = "2022 - {DATE_YEAR} ?? ";
        footer_address = "Address: 3rd Floor Anna Building, Quang Trung Software Park, District 12, Ho Chi Minh City";
        footer_email = "info@matbao.com";
        header_hotline = "1900 1830";
        footer_hotline = "1900 1830";
    } else {}
}
else
{
    var global_title_logo = "Banner qu???n tr??? h??? th???ng";
    var global_fm_button_New = " Th??m m???i ";
    var global_fm_button_add = "L??u";
    var global_fm_button_add_print = "L??u v?? In";
    var global_fm_button_edit = "C???p nh???t";
    var global_fm_button_restart = "Kh???i ?????ng l???i h??? th???ng";
    var global_fm_button_back = "Quay l???i";
    var global_fm_button_close = "??o??ng";
    var global_fm_button_search = "T??m ki???m";
    var global_fm_button_profile_pettlement = "K???t to??n h??? s??";
    var global_fm_button_get_info = "L???y th??ng tin";
    var global_fm_register_note = "L??u ??: Qu?? kh??ch vui l??ng ki???m tra l???i th??ng tin tr?????c khi ????ng k?? ch???ng th?? s???";
    var global_fm_button_reload = "T???i l???i";
    var global_fm_button_reload_profile = "T???i danh s??ch g??i m???c ?????nh";
    var global_fm_button_reload_of_profileaccess = "T???i danh s??ch g??i m???i nh???t theo nh??m quy???n g??i d???ch v???";
    var global_fm_button_export = "Xu???t Excel";
    var global_fm_button_export_csv = "Xu???t File CSV";
    var global_fm_button_export_word = "Xu???t File Word";
    var global_fm_button_profile = "G??i d???ch v???";
    var global_fm_button_configAPI = "C???u h??nh API";
    var global_fm_button_API = "Truy c???p API";
    var global_fm_action = "Thao t??c";
    var global_fm_STT = "STT";
    var global_fm_Username = "T??n ????ng nh???p";
    var global_fm_Username_esigncloud = "T??n ????ng nh???p Remote Signing";
    var global_fm_Username_esigncloud_exists = "T??n ????ng nh???p Remote Signing ???? t???n t???i";
    var global_fm_Password = "M???t kh???u";
    var global_fm_date = "Ng??y t???o/c???p nh???t";
    var global_fm_date_create = "Ng??y t???o";
    var global_fm_date_revoke = "Ng??y thu h???i";
    var global_fm_date_gencert = "Ng??y gen";
    var global_fm_date_cancel = "Ng??y hu??y";
    var global_fm_date_gen = "Nga??y gen";
    var global_fm_num_date_cancel = "S???? ng??y hu??y";
    var global_fm_date_request = "Ng??y y??u c???u";
    var global_fm_date_endupdate = "Ng??y c???p nh???t cu???i";
    var global_fm_scan_valid = "B???n scan h???p l???";

    var global_fm_user_request = "Nh??n vi??n y??u c???u";
    var global_fm_user_create = "Nh??n vi??n t???o";
    if(IsWhichCA === "18") {
        global_fm_user_create = "Ng?????i t???o";
    }
    var global_fm_user_receive = "Nh??n vi??n nh????n";
    var global_fm_user_endupdate = "Nh??n vi??n c???p nh???t cu???i";

    var global_fm_timestamp = "Th???i gian";
    var global_fm_times_recovery = "Th???i gian ph???c h???i";
    var global_fm_duration = "Th???i h???n (Ng??y)";
    var global_fm_active = "Hi???u l???c";
    var global_fm_all_apply_user = "??p d???ng danh s??ch quy???n cho t???t c??? nh??n vi??n trong h??? th???ng thu???c ch???c v??? n??y";
    var global_fm_image = "H??nh ???nh (Icon)";
    var global_fm_required_input = "Y??u c???u nh???p";
    var global_fm_effective = "Hi???u l???c";
    var global_fm_duration_promotion = "Th???i h???n + khuy???n m??i (Ng??y)";
    var global_fm_address = "?????a ch???";
    var global_fm_fullname = "H??? t??n";
    var global_fm_fax = "S??? Fax";
    var global_fm_email = "?????a ch??? email";
    var global_fm_email_receive = "Email (Nh???n th??ng b??o thu???)";
    var global_fm_email_contact = "?????a ch??? email li??n h??? KH";
    var global_fm_email_contact_grid = "?????a ch??? email li??n h??? KH";
    if(IsWhichCA === "18") {
        global_fm_email_contact_grid = "Email";
    }
    var global_fm_email_contact_real = "Email li??n h??? KH (Th???t)";
    var global_fm_email_authen_rssp = "Email x??c th???c k?? s???";
    var global_fm_option_owner_new = "T???o m???i ch??? s??? h???u";
    var global_fm_option_owner_search = "T??m ki???m ch??? s??? h???u tr??n h??? th???ng";
    var global_fm_email_contact_signserver = "?????a ch??? email KH b??n giao CTS Server";
    var global_fm_ip = "?????a ch??? IP";
    var global_fm_port = "Port";
    var global_fm_ward = "T??n ph?????ng";
    var global_fm_street = "T??n ???????ng";
    var global_fm_city = "T???nh/Th??nh ph???";
    var global_fm_area = "Khu v???c";
    var global_fm_Description = "M?? t???";
    var global_fm_Certificate = "Ch???ng th?? s???";
    var global_fm_cert = "Ch???ng th?? s???";
    var global_fm_FromDate = "T??? ng??y";
    var global_fm_year = "N??m";
    var global_fm_mounth = "Tha??ng";
    var global_fm_Quater = "Quy??";
    var global_fm_Branch = "?????i l??";
    var global_fm_ToDate = "?????n ng??y";
    var global_fm_StatusAccount = "Tr???ng th??i t??i kho???n";
    var global_fm_combox_all = "T???t c???";
    var global_fm_combox_empty = "[?????? tr????ng]";
    var global_fm_combox_choose = "-- Vui l??ng ch???n --";
    var global_fm_datatype_label = "Ki???u d??? li???u nh???p";
    var global_fm_datatype_numeric = "K?? t??? s???";
    var global_fm_datatype_varchar = "K?? t??? ch???";
    var global_fm_datatype_boolean = "Ch???n/Kh??ng ch???n";
    var global_fm_combox_no_choise = "--- Danh m???c g???c ---";
    var global_succ_NoResult = "Kh??ng t??m th???y danh s??ch";
    var global_fm_role = "Ch???c v???";
    var global_fm_phone = "??i???n tho???i";
    var global_fm_phone_manager = "??i???n tho???i ng?????i ?????i di???n";
    var global_fm_email_manager = "Email ng?????i ?????i di???n";
    var global_fm_name_manager = "T??n ng?????i ?????i di???n";
    var global_fm_name_contact = "T??n ng?????i li??n h???";
    var global_fm_phone_contact = "??i???n tho???i li??n h??? KH";
    var global_fm_phone_contact_grid = "??i???n tho???i li??n h??? KH";
    if(IsWhichCA === "18") {
        global_fm_phone_contact_grid = "S??T";
    }
    var global_fm_phone_contact_real = "??i???n tho???i li??n h??? KH (Th???t)";
    var global_fm_phone_authen_rssp = "??i???n tho???i x??c th???c k?? s???";
    var global_fm_vendor = "Nh?? cung c???p SIM";
    var global_fm_display_mess = "N???i dung th??ng b??o";
    var global_fm_fileid = "File ID";
    var global_error_file_special = "T??n t???p tin kh??ng cho ph??p ch???a k?? t??? ?????c bi???t. Bao g???m: /\{};:,\"`~&*|+=%$@<>[]#'^!?";
    var global_title_hsm_confirm = "X??c nh???n k??ch ho???t ch???ng th?? s??? HSM";
    var global_fm_Function = "H??m";
    var global_fm_MetaData = "MetaData";
    var global_fm_billcode = "Bill Code";
    var global_succ_NoCheck = "Vui l??ng ch???n danh s??ch c???n th???c hi???n";
    var global_succ_NoCheck_setup = "Vui l??ng ch???n danh s??ch c???n c??i ?????t ch???ng th?? s???";

    var global_paging_Before = "Tr?????c";
    var global_paging_last = "Sau";
    var global_paging_first = "?????u ti??n";
    var global_paging_next = "Cu???i";

    var global_req_Username = "Vui l??ng nh???p T??n ????ng nh???p";
    var global_req_Password = "Vui l??ng nh???p M???t kh???u";
    var global_req_Description = "Vui l??ng nh???p M?? t???";
    var global_req_Pem = "Vui l??ng nh???p Pem";
    var global_req_Certificate = "Vui l??ng nh???p Ch???ng th?? s???";
    var global_req_address = "Vui l??ng nh???p ?????a ch???";
    var global_req_ward = "Vui l??ng nh???p T??n ph?????ng";
    var global_req_street = "Vui l??ng nh???p t??n ???????ng";
    var global_req_mail = "Vui l??ng nh???p ?????a ch??? Email";
    var global_req_mail_format = "Vui l??ng nh???p ????ng ?????nh d???ng Email";
    var global_req_ip_format = "Vui l??ng nh???p ????ng ?????nh d???ng ?????a ch??? IP";
    var global_req_cer_format = "Vui l??ng nh???p ????ng ?????nh d???ng file .cer, .txt, .pem";
    var global_req_csr_format = "Vui l??ng nh???p ????ng ?????nh d???ng file .csr, .txt";
    var global_req_crl_format = "Vui l??ng nh???p ????ng ?????nh d???ng file .crl";
    var global_req_image_format = "Vui l??ng nh???p ????ng ?????nh d???ng file h??nh ???nh .jpg, .png";
    var global_fm_active_true = "C??";
    var global_fm_active_false = "KH??NG";
    var global_fm_remark_en = "M?? t??? (Ti???ng Anh)";
    var global_fm_amount_fee = "S??? ti???n ph?? (VN??)";
    var global_fm_amount = "S??? ti???n (VN??)";
    var global_fm_activation_code = "M?? k??ch ho???t";
    var global_fm_activation_date = "Th???i h???n k??ch ho???t";
    var global_fm_amount_token = "Gi?? ti???n Token (VN??)";
    
    // ### FORM_FACTOR
    var global_fm_amount_renewal = "S??? ti???n ph?? gia h???n";
    var global_fm_amount_changeinfo = "S??? ti???n ph?? thay ?????i th??ng tin";
    var global_fm_amount_reissue = "S??? ti???n ph?? c???p l???i";
    var global_fm_amount_goverment = "S??? ti???n ph?? kh??c";
    
    var global_fm_date_free = "Th???i gian khuy???n m??i (Ng??y)";
    var global_fm_entity_ejbca = "Entity EJBCA";
    var global_fm_choose_csr = "Ch???n CSR";
    var global_fm_choose_genkey_server = "Sinh kh??a tr??n server (P12)";
    var global_fm_choose_genkey_client = "Ch???n CSR (kh??a ???????c sinh t??? Client)";
    var global_fm_remark_vn = "M?? t??? (Ti???ng Vi???t)";
    var global_fm_en = "Ti???ng Anh";
    var global_fm_vn = "Ti???ng Vi???t";
    var global_fm_refresh = "L??m l???i";
    var global_fm_properties = "Thu???c t??nh";
    var global_fm_uuid = "UUID";
    var global_fm_uuid_agreement = "UUID h???p ?????ng";
    var global_fm_remainingSigning_agreement = "S??? l?????t k?? c??n l???i";
    var global_fm_appid_uri = "APPID URI";
    var global_fm_signature_v4 = "Ch??? k?? V.4";
    var global_fm_access_key = "Access Key";
    var global_fm_secret_key = "Secret Key";
    var global_fm_xapi_key = "X API key";
    var global_fm_regions = "T??n khu v???c";
    var global_fm_service = "T??n d???ch v???";
    var global_fm_dns_name = "T??n DNS";
    var global_fm_dns_list = "Danh s??ch DNS";
    var global_fm_confirm_customer = "Th??ng tin x??c nh???n c???a kh??ch h??ng";
    var global_fm_confirm = "Tr???ng th??i x??c nh???n";
    var global_fm_confirm_time = "Th???i gian x??c nh???n";
    var global_fm_confirm_ip = "?????a ch??? IP";
    var global_fm_confirm_content = "N???i dung";
    var global_fm_exists_form = "Token t???n ?????u k???";
    var global_fm_Deposit_form = "Token ?????t c???c trong th??ng";
    var global_fm_use_form = "Token s??? d???ng trong th??ng";
    var global_fm_end_form = "Token t???n cu???i k???";
    var global_fm_form = "H??nh th???c";
    var global_fm_uri = "URI";
    var global_fm_url_callback = "G???i l???i ???????ng d???n";
    var global_req_format_http = "Vui l??ng nh???p ????ng ?????nh d???ng ???????ng d???n ";


//General -> branch
    var branch_title_list = "Qu???n l?? ?????i l??";
    var branch_table_list = "Danh s??ch ?????i l??";
    var branch_title_add = "Th??m m???i ?????i l??";
    var branch_title_edit = "Ch???nh s???a ?????i l??";
    var branch_req_name = "Vui l??ng nh???p t??n ?????i l??";
    var branch_req_code = "Vui l??ng nh???p m?? ?????i l??";
    var branch_succ_add = "Th??m m???i ?????i l?? th??nh c??ng";
    var branch_warning_add = "Th??m m???i ?????i l?? th??nh c??ng. T???o ng?????i d??ng th???t b???i, t??n ????ng nh???p ???? t???n t???i";
    var branch_succ_edit = "C???p nh???t ?????i l?? th??nh c??ng";
    var branch_exists_name = "T??n ?????i l?? ???? t???n t???i";
    var branch_exists_code = "M?? ?????i l?? ???? t???n t???i";
    var branch_fm_name = "T??n ?????i l??";
    var branch_fm_code = "M?? ?????i l??";
    var branch_fm_parent = "?????i l?? qu???n l??";
    var branch_fm_level = "Ph??n c???p ?????i l??";
    var branch_req_area_change = "Gi?? tr??? Khu v???c kh??ng th??? tr???ng";
    var branch_conform_delete = "B???n mu???n x??a ?????i l?? n??y ?";
    var branch_succ_delete = "X??a ?????i l?? th??nh c??ng";
    var branch_exists_user_delete = "Vui l??ng x??a t???t c??? t??i kho???n nh??n vi??n c???a ?????i l?? tr?????c";
    var branch_conform_default = "B???n mu???n ?????t m???c ?????nh cho Logo ?";
    // new
    var branch_fm_choise_new = "Ch???n t???o m???i";
    var branch_fm_choise_CN = "Chi nh??nh";
    var branch_fm_choise_PGD = "Ph??ng giao d???ch";
    var branch_fm_access_profile = "Truy xu???t g??i d???ch v???";
    
    //report -> synchneac
    var synchneac_title_list = "Qu???n l?? ?????ng b??? NEAC";
    var synchneac_table_list = "Danh s??ch ch???ng th?? s???";
    var synchneac_title_edit = "Th??ng tin ch???ng th?? s???";
    var synchneac_succ_edit = "?????ng b??? th??nh c??ng";
    var synchneac_conform_update_multi = "B???n mu???n c???u h??nh nhi???u th??ng tin ?";
    var synchneac_conform_decline_multi = "B???n mu???n t??? ch???i nhi???u th??ng tin ?";
    var synchneac_conform_synch_multi = "B???n mu???n ?????ng b??? nhi???u th??ng tin ?";
    var synchneac_fm_remaining = "S??? l???n ?????ng b??? l???i";
    var synchneac_fm_synch_auto = "Cho ph??p ?????ng b??? t??? ?????ng l??n NEAC";
    
//General -> city
    var city_title_list = "Qu???n l?? T???nh/th??nh ph???";
    var city_table_list = "Danh sa??ch T???nh/th??nh ph???";
    var city_table_search = "T??m ki???m T???nh/th??nh ph???";
    var city_title_add = "Th??m m???i T???nh/th??nh ph???";
    var city_title_edit = "Ch???nh s???a T???nh/th??nh ph???";
    var city_req_name = "Vui l??ng nh???p t??n T???nh/Th??nh ph???";
    var city_req_code = "Vui l??ng nh???p M?? T???nh/Th??nh ph???";
    var city_succ_add = "Th??m m???i T???nh/th??nh ph??? th??nh c??ng";
    var city_exists_code = "M?? T???nh/th??nh ph??? ???? t???n t???i";
    var city_exists_name = "T??n T???nh/th??nh ph??? ???? t???n t???i";
    var city_succ_edit = "C???p nh???t T???nh/th??nh ph??? th??nh c??ng";
    var city_fm_code = "M?? T???nh/th??nh ph???";
    var city_fm_name = "T??n T???nh/th??nh ph???";
    
    //General -> CertificateTypeList
    var certtype_title_list = "Qu???n l?? lo???i ch???ng th?? s???";
    var certtype_table_list = "Danh s??ch";
    var certtype_title_add = "Th??m m???i lo???i ch???ng th?? s???";
    var certtype_title_edit = "C???u h??nh lo???i ch???ng th?? s???";
    var certtype_exists_code = "M?? lo???i ???? t???n t???i";
    var certtype_fm_code = "M?? lo???i ch???ng th?? s???";
    var certtype_succ_add = "Th??m lo???i ch???ng th?? s??? th??nh c??ng";
    var certtype_succ_edit = "C???u h??nh lo???i ch???ng th?? s??? th??nh c??ng";
    var certtype_group_file_profile = "C???u h??nh lo???i t???p ????nh k??m";
    var certtype_component_attributetype = "Lo???i thu???c t??nh";
    var certtype_component_cntype = "Lo???i t??n chung";
    var certtype_component_field_code = "M?? tr?????ng";
    var certtype_component_field_code_exists = "M?? tr?????ng ???? t???n t???i";
    var certtype_file_code_exists = "M?? lo???i t???p ???? t???n t???i";
    var certtype_file_code = "M?? lo???i t???p";
    var certtype_fm_file = "Lo???i t???p";
    var certtype_fm_component_text = "Nh???p ch???";
    var certtype_fm_component_uuid_company = "Ch???n UID doanh nghi???p";
    var certtype_fm_component_uuid_personal = "Ch???n UID c?? nh??n";
    var certtype_fm_component_uuid_company_require = "B???t bu???c UID doanh nghi???p";
    var certtype_fm_component_uuid_personal_require = "B???t bu???c UID c?? nh??n";
    
    //General -> Response Code
    var response_title_list = "Qu???n l?? tr???ng th??i giao d???ch";
    var response_table_list = "Danh sa??ch tr???ng th??i giao d???ch";
    var response_title_add = "Th??m m???i tr???ng th??i giao d???ch";
    var response_title_edit = "Ch???nh s???a tr???ng th??i giao d???ch";
    var response_succ_add = "Th??m m???i tr???ng th??i giao d???ch th??nh c??ng";
    var response_exists_code = "M?? tr???ng th??i giao d???ch ???? t???n t???i";
    var response_exists_name = "T??n tr???ng th??i giao d???ch ???? t???n t???i";
    var response_succ_edit = "C???p nh???t tr???ng th??i giao d???ch th??nh c??ng";
    var response_fm_code = "M?? tr???ng th??i giao d???ch";
    var response_fm_name = "T??n tr???ng th??i giao d???ch";
    //General -> MNO
    var mno_title_list = "Qu???n l?? MNO";
    var mno_table_list = "Danh sa??ch MNO";
    var mno_title_add = "Th??m m???i MNO";
    var mno_title_edit = "Ch???nh s???a MNO";
    var mno_succ_add = "Th??m m???i MNO th??nh c??ng";
    var mno_exists_code = "M?? MNO ???? t???n t???i";
    var mno_exists_name = "T??n MNO ???? t???n t???i";
    var mno_succ_edit = "C???p nh???t MNO th??nh c??ng";
    var mno_fm_code = "M?? MNO";
    //General -> InternalEntity
    var interentity_title_list = "Qu???n l?? th???c th??? k???t n???i n???i b???";
    var interentity_table_list = "Danh sa??ch th???c th??? k???t n???i n???i b???";
    var interentity_title_add = "Th??m m???i th???c th??? k???t n???i n???i b???";
    var interentity_title_edit = "Ch???nh s???a th???c th??? k???t n???i n???i b???";
    var interentity_succ_add = "Th??m m???i th???c th??? k???t n???i n???i b??? th??nh c??ng";
    var interentity_exists_code = "M?? th???c th??? k???t n???i n???i b??? ???? t???n t???i";
    var interentity_succ_edit = "C???p nh???t th???c th??? k???t n???i n???i b??? th??nh c??ng";
    var interentity_fm_code = "M?? th???c th???";
    //General -> ExternalEntity
    var exterentity_title_list = "Qu???n l?? th???c th??? k???t n???i b??n ngo??i";
    var exterentity_table_list = "Danh sa??ch th???c th??? k???t n???i b??n ngo??i";
    var exterentity_title_add = "Th??m m???i th???c th??? k???t n???i b??n ngo??i";
    var exterentity_title_edit = "Ch???nh s???a th???c th??? k???t n???i b??n ngo??i";
    var exterentity_succ_add = "Th??m m???i th???c th??? k???t n???i b??n ngo??i th??nh c??ng";
    var exterentity_exists_code = "M?? th???c th??? k???t n???i b??n ngo??i ???? t???n t???i";
    var exterentity_succ_edit = "C???p nh???t th???c th??? k???t n???i b??n ngo??i th??nh c??ng";
    var exterentity_fm_code = "M?? th???c th???";
    //General -> RelyingParty
    var relyparty_title_list = "Qu???n l?? Relying Party";
    var relyparty_table_list = "Danh sa??ch Relying Party";
    var relyparty_title_add = "Th??m m???i Relying Party";
    var relyparty_title_edit = "Ch???nh s???a Relying Party";
    var relyparty_succ_add = "Th??m m???i Relying Party th??nh c??ng";
    var relyparty_exists_code = "M?? Relying Party ???? t???n t???i";
    var relyparty_succ_edit = "C???p nh???t Relying Party th??nh c??ng";
    var relyparty_fm_code = "M?? Relying Party";
    var relyparty_fm_choise_all = "Cho ph??p t???t c??? ?????a ch??? IP";
    var relyparty_fm_choise_ip = "Nh???p danh s??ch ?????a ch??? IP";
    var relyparty_fm_choise_all_function = "Cho ph??p t???t c??? h??m";
    var relyparty_fm_choise_ip_function = "Ch???n danh s??ch h??m";
    var relyparty_fm_group_ip = "C???u h??nh danh s??ch ?????a ch??? IP truy c???p";
    var relyparty_fm_group_function = "C???u h??nh danh s??ch H??m truy c???p";
    var relyparty_fm_group_metadata = "C???u h??nh danh s??ch MetaData";
    var relyparty_fm_group_facet = "C???u h??nh th??ng tin kh??ch h??ng";
    var relyparty_exists_add_ip = "?????a ch??? IP ???? t???n t???i";
    var global_error_delete_ip = "X??a th???t b???i, vui l??ng ki???m tra l???i";
    var relyparty_all_add_ip = "???? ph??n quy???n truy c???p t???t c??? IP, th??m IP th???t b???i";
    var relyparty_error_delete_function = "X??a h??m th???t b???i, vui l??ng ki???m tra l???i";
    var global_exists_add_function = "M?? h??m ???? t???n t???i";
    var global_exists_add_metadata = "AAID ???? t???n t???i";
    var global_exists_add_facet = "M?? th??ng tin kh??ch h??ng ???? t???n t???i";
    var relyparty_all_add_function = "???? ph??n quy???n truy c???p t???t c??? h??m, th??m h??m th???t b???i";
    var relyparty_req_add_function = "Vui l??ng nh???p danh s??ch H??m";
    var global_req_add_ip = "Vui l??ng nh???p danh s??ch IP";
    var global_conform_delete_function = "B???n ch???c ch???n mu???n x??a h??m n??y ?";
    var global_conform_delete_ip = "B???n ch???c ch???n mu???n x??a ?????a ch??? IP n??y ?";
    var global_conform_delete_metadata = "B???n ch???c ch???n mu???n x??a MetaData n??y ?";
    var global_succ_enabled_function = "C???p nh???t hi???u l???c h??m th??nh c??ng";
    var global_succ_enabled_ip = "C???p nh???t hi???u l???c ?????a ch??? IP th??nh c??ng";
    var global_succ_enabled_metadata = "C???p nh???t hi???u l???c MetaData th??nh c??ng";
    var global_succ_enabled_facet = "C???p nh???t hi???u l???c th??ng tin kh??ch h??ng th??nh c??ng";
    var global_conform_delete_soap = "B???n mu???n x??a c???u h??nh thu???c t??nh n??y ?";
    var global_conform_delete_restful = "B???n mu???n x??a c???u h??nh thu???c t??nh Restful n??y ?";
    var global_succ_delete_soap = "X??a c???u h??nh thu???c t??nh th??nh c??ng";
    var global_succ_delete_restful = "X??a c???u h??nh thu???c t??nh Restful th??nh c??ng";
    var global_succ_edit_soap = "C???p nh???t c???u h??nh thu???c t??nh CA th??nh c??ng";
    var global_succ_edit_restful = "C???p nh???t c???u h??nh thu???c t??nh Restful th??nh c??ng";
    var global_succ_add_soap = "Th??m m???i c???u h??nh thu???c t??nh CA th??nh c??ng";
    var global_succ_add_restful = "Th??m m???i c???u h??nh thu???c t??nh Restful th??nh c??ng";
    var global_fm_restful = "Thu???c t??nh Restful";
    var global_fm_soap = "Thu???c t??nh";
    var global_title_soap_edit = "Ch???nh s???a thu???c t??nh CA";
    var global_title_restful_edit = "Ch???nh s???a thu???c t??nh RestFul";
    var global_title_soap_add = "Th??m m???i thu???c t??nh Soap";
    var global_title_propeties_ca_add = "Th??m m???i thu???c t??nh CA";
    var global_title_restful_add = "Th??m m???i thu???c t??nh RestFul";
    var global_fm_facet = "Th??ng tin kh??ch h??ng";
    var global_fm_status_expire = "H???t h???n";
    var global_fm_not_blank = " kh??ng th??? tr???ng";

    //General -> ManagementParty
    var manaparty_title_list = "Qu???n l?? Management Party";
    var manaparty_table_list = "Danh sa??ch Management Party";
    var manaparty_title_add = "Th??m m???i Management Party";
    var manaparty_title_edit = "Ch???nh s???a Management Party";
    var manaparty_succ_add = "Th??m m???i Management Party th??nh c??ng";
    var manaparty_exists_code = "M?? Management Party ???? t???n t???i";
    var manaparty_succ_edit = "C???p nh???t Management Party th??nh c??ng";
    var manaparty_fm_code = "M?? Management Party";
    var manaparty_fm_message_mode = "Ch??? ????? th??ng b??o";
    var manaparty_fm_expire_duration = "Th???i gian h???t h???n";
    var branch_fm_expire_token = "Th???i gian h???t h???n (Ph??t)";
    var branch_fm_secretkey = "Kh??a b???o m???t";
    //General -> FacetManagement
    var facetmana_title_list = "Qu???n l?? th??ng tin kh??ch h??ng";
    var facetmana_table_list = "Danh sa??ch th??ng tin kh??ch h??ng";
    var facetmana_title_add = "Th??m m???i th??ng tin kh??ch h??ng";
    var facetmana_title_edit = "Ch???nh s???a th??ng tin kh??ch h??ng";
    var facetmana_succ_add = "Th??m m???i th??ng tin kh??ch h??ng th??nh c??ng";
    var facetmana_exists_code = "M?? tr???ng th??ng tin kh??ch h??ng ???? t???n t???i";
    var facetmana_exists_name = "T??n th??ng tin kh??ch h??ng ???? t???n t???i";
    var facetmana_succ_edit = "C???p nh???t th??ng tin kh??ch h??ng th??nh c??ng";
    var facetmana_fm_code = "M?? th??ng tin kh??ch h??ng";
    //General -> FacetManagement
    var smartversion_title_list = "Qu???n l?? phi??n b???n h??? th???ng";
    var smartversion_table_list = "Danh sa??ch phi??n b???n h??? th???ng";
    var smartversion_title_add = "Th??m m???i phi??n b???n h??? th???ng";
    var smartversion_title_edit = "Ch???nh s???a phi??n b???n h??? th???ng";
    var smartversion_succ_add = "Th??m m???i phi??n b???n h??? th???ng th??nh c??ng";
    var smartversion_exists_code = "M?? tr???ng phi??n b???n h??? th???ng ???? t???n t???i";
    var smartversion_exists_name = "T??n phi??n b???n h??? th???ng ???? t???n t???i";
    var smartversion_succ_edit = "C???p nh???t phi??n b???n h??? th???ng th??nh c??ng";
    var smartversion_fm_code = "M?? phi??n b???n h??? th???ng";
    //History -> TransactionList
    var smarttrans_title_list = "L???ch s??? giao d???ch";
    var smarttrans_table_list = "Danh sa??ch giao d???ch";
    var smarttrans_search_list = "T??m ki???m giao d???ch";
    var smarttrans_title_view = "Th??ng tin chi ti???t giao d???ch";
    var smarttrans_fm_data_body = "Body Data";
    var smarttrans_fm_data_header = "Header Data";
    //General -> SMPP Party
    var smpp_title_list = "Qu???n l?? SMPP";
    var smpp_table_list = "Danh sa??ch SMPP";
    var smpp_title_add = "Th??m m???i SMPP";
    var smpp_title_edit = "Ch???nh s???a SMPP";
    var smpp_succ_add = "Th??m m???i SMPP th??nh c??ng";
    var smpp_exists_code = "M?? SMPP ???? t???n t???i";
    var smpp_succ_edit = "C???p nh???t SMPP th??nh c??ng";
    var smpp_fm_code = "M?? SMPP";
    var smpp_fm_heartbeat_interval = "HeartBeat Interval";
    var smpp_fm_retry_attempt = "Retry Attempt";
    var smpp_fm_retry_delay_duration = "Delay Duration";
    //Config -> ServerParty
    var serverparty_title_list = "Qu???n l?? Server Entity";
    var serverparty_table_list = "Danh sa??ch Server Entity";
    var serverparty_title_add = "Th??m m???i Server Entity";
    var serverparty_title_edit = "Ch???nh s???a Server Entity";
    var serverparty_succ_add = "Th??m m???i Server Entity th??nh c??ng";
    var serverparty_exists_code = "M?? Server Entity ???? t???n t???i";
    var serverparty_succ_edit = "C???p nh???t Server Entity th??nh c??ng";
    var serverparty_fm_code = "M?? Server Entity";
    //General -> Functionality
    var metadata_title_list = "Qu???n l?? MetaData";
    var metadata_table_list = "Danh sa??ch MetaData";
    var metadata_title_edit = "Chi ti???t MetaData";
    var metadata_title_add = "Th??m m???i MetaData";
    var metadata_succ_add = "Th??m m???i MetaData th??nh c??ng";
    var metadata_fm_aaid = "AAID";
    var metadata_fm_metadata = "MetaData";
    var metadata_exists_aaid = "AAID";
    var metadata_fm_contenttype = "Lo???i n???i dung";
    var metadata_fm_authenalgorithm = "Thu???t to??n x??c th???c";
    var metadata_fm_keyrestricted = "Kh??a b??? h???n ch???";

    //General -> Functionality
    var function_title_list = "Qu???n l?? h??m";
    var function_table_list = "Danh sa??ch h??m";
    var function_title_add = "Th??m m???i h??m";
    var function_title_edit = "Ch???nh s???a h??m";
    var function_succ_add = "Th??m m???i h??m th??nh c??ng";
    var function_exists_code = "M?? tr???ng h??m ???? t???n t???i";
    var function_exists_name = "T??n h??m ???? t???n t???i";
    var function_succ_edit = "C???p nh???t h??m th??nh c??ng";
    var function_fm_code = "M?? h??m";
    var function_fm_name = "T??n h??m";
    //General -> CA
    var ca_title_list = "Qu???n l?? nh?? cung c???p CA";
    var ca_table_list = "Danh sa??ch nh?? cung c???p CA";
    var ca_title_add = "Th??m m???i nh?? cung c???p CA";
    var ca_title_edit = "Ch???nh s???a nh?? cung c???p CA";
    var ca_succ_add = "Th??m m???i nh?? cung c???p CA th??nh c??ng";
    var ca_exists_code = "M?? nh?? cung c???p CA ???? t???n t???i";
    var ca_exists_name = "T??n nh?? cung c???p CA ???? t???n t???i";
    var ca_succ_edit = "C???p nh???t nh?? cung c???p CA th??nh c??ng";
    var ca_fm_short = "Short Code";
    var ca_fm_code = "M?? CA";
    var ca_fm_name = "T??n CA";
    var ca_fm_OCSP = "OCSP URL";
    var ca_fm_CRL = "???????ng d???n CRL";
    var ca_fm_CRLPath = "T??n CRL";
    var ca_fm_URI = "URI";
    var ca_fm_Cert_01 = "Ch???ng th?? s???";
    var ca_fm_CheckOCSP = "Ch???n b???i OCSP";
    var ca_fm_unique_DN = "Cho ph??p tr??ng ch??? th??? CTS (Subject DN)";
    var ca_group_CRLFile_1 = "CRL File";
    var ca_error_valid_cert_01 = "Ch???ng th?? s??? kh??ng h???p l???";
    var ca_error_valid_cert_expire_01 = "Th???i gian hi???u l???c ch???ng th?? s??? ???? h???t h???n";
    var ca_succ_import_crl1 = "????ng t???i t???p tin CRL th??nh c??ng";
    var ca_error_import_crl1 = "????ng t???i t???p tin CRL th???t b???i";
    var ca_group_cert = "Chi ti???t ch???ng th?? s???";
    var ca_req_info_cert = "Kh??ng t??m th???y th??ng tin ch???ng th?? s???";
    var ca_succ_reload = "T???i l???i file CRL th??nh c??ng";
    var ca_error_reload = "T???i l???i file CRL th???t b???i";
    //General -> Certificate Profile
    var certprofile_title_list = "Qu???n l?? g??i d???ch v???";
    var certprofile_table_list = "Danh sa??ch g??i d???ch v???";
    var certprofile_title_add = "Th??m m???i g??i d???ch v???";
    var certprofile_title_edit = "Ch???nh s???a g??i d???ch v???";
    var certprofile_succ_add = "Th??m m???i g??i d???ch v??? th??nh c??ng";
    var certprofile_exists_code = "M?? g??i d???ch v??? ???? t???n t???i";
    var certprofile_exists_name = "T??n g??i d???ch v??? ???? t???n t???i";
    var certprofile_succ_edit = "C???p nh???t g??i d???ch v??? th??nh c??ng";
    var certprofile_fm_code = "M?? g??i d???ch v???";
    var certprofile_fm_service_type = "Lo???i d???ch v???";
    var certprofile_fm_service_issue = "????ng k??";
    var certprofile_fm_service_renew = "Gia h???n";
    //admin -> confignamil
    var email_title_list = "C???u h??nh Email h??? th???ng";
    var email_req_smtp = "Vui l??ng nh???p SMTP Server";
    var email_req_port = "Vui l??ng nh???p Port";
    var email_succ_edit = "C???p nh???t c???u h??nh mail server th??nh c??ng";
    var email_fm_port = "Port";
    var email_fm_smtp = "SMTP Server";
    //admin -> ManagePolicy
    var policy_title_list = "C???u h??nh tham s??? h??? th???ng";
    var policy_title_list_client = "Tham s???";
    var policy_title_list_client_fo = "Ph??n h??? Kh??ch h??ng";
    var policy_title_list_client_bo = "Ph??n h??? Qu???n tr???";
    var policy_group_notification = "C???u h??nh th??ng b??o m???c ?????nh";
    var policy_succ_edit = "C???p nh???t tham s??? h??? th???ng th??nh c??ng";
    var policy_req_empty = "Vui l??ng nh???p" + " ";
    var policy_req_empty_choose = "Vui l??ng ch???n" + " ";
    var policy_req_number = " " + "ch??? bao g???m k?? t??? s???";
    var policy_req_unicode = " " + "kh??ng ch???a k?? t??? c?? d???u";
//admin -> ConfigPolicy
    var policy_config_title_list = "Qu???n l?? tr?????ng tham s???";
    var policy_config_table_list = "Danh s??ch tr?????ng tham s???";
    var policy_title_edit = "Ch???nh s???a tr?????ng tham s???";
    var policy_title_add = "Th??m m???i tr?????ng tham s???";
    var policy_succ_add = "Th??m m???i tr?????ng tham s??? th??nh c??ng";
    var policy_fm_fo = "Kh??ch h??ng";
    var policy_fm_bo = "Qu???n tr???";
    var policy_fm_group_fo_bo = "Tham s??? cho ph??n h???";
    var policy_fm_code = "M?? tr?????ng tham s???";
    var policy_exists_code = "M?? tr?????ng tham s??? ???? t???n t???i";

//admin -> menulink
    var menu_title_list = "Qu???n l?? ph??n quy???n m??n h??nh";
    var menu_title_table = "Ph??n quy???n m??n h??nh";
    var menu_group_Role = "Ch???n ch???c v???";
    var menu_fm_Role = "Ch???c v???";
    var menu_group_assign = "Menu ch??a g??n";
    var menu_fm_assign = "T??n menu";
    var menu_fm_parent_name = "T??n menu qu???n l??";
    var menu_fm_url = "???????ng d???n menu";
    var menu_table_assigned = "Danh sa??ch Menu ???? g??n";
    var menu_conform_delete = "B???n mu???n x??a Menu n??y ?";
    var menu_succ_delete = "X??a Menu th??nh c??ng";
    var menu_succ_insert = "Th??m m???i Menu th??nh c??ng";
    var menu_error_delete = "X??a Menu th???t b???i";
    var menu_error_insert = "Th??m m???i Menu th???t b???i";
    var menu_fm_button_assign = "G??n";
    
    //general -> MethodProfile
    var methodprofile_title_list = "Ph??n quy???n ph????ng th???c truy c???p g??i d???ch v???";
    var methodprofile_title_table = "Ph??n quy???n ph????ng th???c";
    var methodprofile_group_formfactor = "Ch???n ph????ng th???c";
    var methodprofile_fm_formfactor = "Ph????ng th???c";
    var methodprofile_group_assign = "G??i d???ch v??? ch??a g??n";
    var methodprofile_fm_profile = "G??i d???ch v???";
    var methodprofile_table_assigned = "Danh sa??ch g??i d???ch v??? ???? g??n";
    var methodprofile_conform_delete = "B???n mu???n x??a g??i n??y ?";
    var methodprofile_succ_delete = "X??a th??nh c??ng";
    var methodprofile_succ_insert = "Th??m m???i th??nh c??ng";
    var methodprofile_error_delete = "X??a th???t b???i";
    var methodprofile_error_insert = "Th??m m???i th???t b???i";
    
    //admin -> UserRole
    var role_title_list = "Qu???n l?? ch???c v???";
    var role_title_table = "Danh sa??ch ch???c v???";
    var role_title_edit = "Ch???nh s???a ch???c v???";
    var role_title_add = "Th??m m???i ch???c v???";
    var role_group_Role = "Ch???n ch???c v???";
    var role_fm_code = "M?? ch???c v???";
    var role_fm_is_ca = "Ch???c v??? cho CA";
    var role_fm_is_agent = "Ch???c v??? cho ?????i l??";
    var role_fm_name = "T??n ch???c v???";
    var role_succ_add = "Th??m m???i ch???c v??? th??nh c??ng";
    var role_succ_edit = "C???p nh???t ch???c v??? th??nh c??ng";
    var role_exists_code = "M?? ch???c v??? ???? t???n t???i";
    var role_exists_name = "T??n ch???c v??? ???? t???n t???i";
    var role_noexists_functions = "Vui l??ng ch???n ??t nh???t m???t quy???n ch???c n??ng";
    var role_fm_function_name = "T??n ch????c n??ng";
    
    //admin -> esignremain
    var esignremain_title_list = "B??o c??o s??? l?????t k?? c??n l???i";
    var esignremain_title_table = "Danh s??ch";
    
    //admin -> formfactor
    var formfactor_title_list = "C???u h??nh ph????ng th???c";
    var formfactor_title_table = "Danh s??ch ph????ng th???c";
    var formfactor_title_edit = "Ch???nh s???a ph????ng th???c";
    var formfactor_fm_code = "M?? ph????ng th???c";
    var formfactor_fm_name = "T??n ph????ng th???c";
    var formfactor_succ_edit = "C???u h??nh ph????ng th???c th??nh c??ng";
    var formfactor_exists_name = "T??n ph????ng th???c ???? t???n t???i";
    var formfactor_title_properties = "C???u h??nh k???t n???i h??? th???ng";
    
    // FUNCTION ACCESS
    var funrole_fm_islock = "C???u h??nh kh??a";
    var funrole_fm_isunlock = "C???u h??nh m??? kh??a";
    var funrole_fm_issopin = "C???u h??nh thay ?????i SOPIN";
    var funrole_fm_ispush = "C???u h??nh g???i th??ng b??o";
    var funrole_fm_isinit = "C???u h??nh kh???i t???o";
    var funrole_fm_isdynamic = "C???u h??nh n???i dung ?????ng";
    var funrole_fm_isinformation = "C???u h??nh th??ng tin";
    var funrole_fm_isactive = "C???u h??nh hi???u l???c";
    var funrole_fm_editcert = "C???u h??nh ch???ng th?? s???";
    var funrole_fm_approvecert = "Duy???t ch???ng th?? s???";
    var funrole_fm_deleterequest = "T??? ch???i y??u c???u";
    var funrole_fm_addrenewal = "Th??m y??u c???u c???p b??";
    var funrole_fm_deleterenewal = "T??? ch???i y??u c???u c???p b??";
    var funrole_fm_importrenewal = "T???i l??n danh s??ch y??u c???u c???p b??";
    var funrole_fm_accessrenewal = "Truy c???p ch???c n??ng y??u c???u c???p b??";
    var funrole_fm_revoke_cert = "Truy c???p ch???c n??ng thu h???i ch???ng th?? s???";
    var funrole_fm_export_cert = "Truy c???p ch???c n??ng l??u file ch???ng th?? s???";
    //User -> MenuScreen
    var menusc_title_list = "Qu???n l?? Menu";
    var menusc_title_table = "Danh sa??ch Menu";
    var menusc_title_edit = "Ch???nh s???a Menu";
    var menusc_title_add = "Th??m m???i Menu";
    var menusc_fm_nameparent = "Menu qu???n l??";
    var menusc_fm_name_vn = "T??n Menu (Ti???ng Vi???t)";
    var menusc_fm_name = "T??n Menu";
    var menusc_fm_name_en = "T??n Menu (Ti???ng Anh)";
    var menusc_fm_code = "M?? Menu";
    var menusc_fm_url = "???????ng d???n Menu";
    var menusc_succ_add = "Th??m m???i Menu th??nh c??ng";
    var menusc_succ_edit = "C???p nh???t Menu th??nh c??ng";
    var menusc_exists_linkurl = "???????ng d???n Menu ???? t???n t???i";
    var menusc_exists_nameparent = "T??n Menu qu???n l?? ???? t???n t???i";
    //user -> userlist
    var user_title_list = "Qu???n l?? nh??n vi??n";
    var user_title_search = "T??m ki???m nh??n vi??n";
    var user_title_table = "Danh s??ch nh??n vi??n";
    var user_title_edit = "Ch???nh s???a nh??n vi??n";
    var user_title_add = "Th??m m???i nh??n vi??n";
    var user_title_info = "Th??ng tin nh??n vi??n";
    var user_title_roleset = "Danh s??ch quy???n ch???c n??ng";
    var user_title_roleset_token = "Quy???n ch???c n??ng Token";
    var user_title_roleset_cert = "Quy???n ch???c n??ng ch????ng th?? s????";
    var user_title_roleset_another = "Quy???n ch???c n??ng kha??c";
    var user_succ_add = "Th??m m???i nh??n vi??n th??nh c??ng";
    var user_succ_edit = "C???p nh???t nh??n vi??n th??nh c??ng";
    var user_exists_username = "T??n ????ng nh???p ???? t???n t???i";
    var user_exists_email = "?????a ch??? email ???? t???n t???i";
    var user_exists_cert_hash = "Th??ng tin ch???ng th?? s??? ???? t???n t???i";
    var user_exists_user_role_admin = "???? t???n t???i t??n ????ng nh???p c?? vai tr?? qu???n tr??? trong h??? th???ng";
    var user_conform_cancel = "B???n ch???c ch???n mu???n h???y nh??n vi??n n??y ?";
    var user_title_delete = "Xo??a nh??n vi??n";
    var user_title_delete_note = "Ghi chu??: Vui lo??ng cho??n nh??n vi??n ti????p nh????n va?? qua??n ly?? ch????ng th?? s???? cu??a nh??n vi??n bi?? xo??a";
    
    //Rose -> RoseList
    var rose_title_list = "C???u h??nh nh??m hoa h???ng";
    var rose_title_table = "Danh s??ch";
    var rose_title_edit = "Ch???nh s???a nh??m hoa h???ng";
    var rose_title_add = "Th??m m???i nh??m hoa h???ng";
    var rose_fm_code = "M?? nh??m hoa h???ng";
    var rose_fm_rose = "Nh??m hoa h???ng";
    var rose_succ_edit = "Ch???nh s???a th??nh c??ng";
    var rose_succ_add = "Th??m m???i th??nh c??ng";
    var rose_exists_profile_properties = "G??i d???ch v??? ???? t???n t???i";
    var rose_permission_profile_list = "T??? l??? hoa h???ng cho g??i d???ch v???";
    
    // profileaccss ->  profileaccss
    var profileaccss_title_list = "C???u h??nh nh??m quy???n g??i d???ch v???";
    var profileaccss_title_table = "Danh s??ch";
    var profileaccss_title_edit = "Ch???nh s???a nh??m quy???n g??i d???ch v???";
    var profileaccss_title_add = "Th??m nh??m quy???n g??i d???ch v???";
    var profileaccss_fm_code = "M?? nh??m quy???n";
    var profileaccss_fm_agency = "Danh s??ch ?????i l??";
    var profileaccss_fm_rose = "Nh??m quy???n g??i d???ch v???";
    var profileaccss_fm_service_type = "Lo???i y??u c???u CTS";
    var profileaccss_fm_major_cert = "Ch???c n??ng CTS";
    var profileaccss_succ_edit = "C???u h??nh th??nh c??ng";
    var profileaccss_succ_add = "Th??m m???i th??nh c??ng";
    var profileaccss_exists_profile_properties = "G??i d???ch v??? ???? t???n t???i";
    var profileaccss_apply_profile_agency = "??p d???ng c???u h??nh cho ?????i l??";
    var profileaccss_exists_code = "M?? nh??m quy???n ???? t???n t???i";

    var global_fm_certprofile = "H??? s?? ch???ng th?? s???";
    var global_fm_certstatus = "Tr???ng th??i ch???ng th?? s???";
    var global_fm_cert_expire_number = "S??? ng??y c??n hi???u l???c";
    var global_fm_common = "T??n ch??? th???";
    var global_fm_subject = "DN";
    var global_fm_public_key_hash = "Public Key Hash";
    var global_fm_certificate_hash = "Ch???ng th?? s??? Hash";
    var global_fm_key_id = "Key ID";
    var global_fm_key_selector = "KEY SELECTOR";
    var global_fm_service_deny = "SERVICE DENY";
    var global_fm_authority_key_id = "AUTHORITY KEY ID";
    var global_error_empty_cert = "Ch???ng th?? s??? kh??ng t???n t???i";
    var global_error_exists_mst_budget_regis = "M?? s??? thu???/ M?? ng??n s??ch/ CMND/ H??? chi???u ???? t???n t???i trong h??? th???ng\nVui l??ng truy c???p mua th??m CTS";

    var global_fm_ca = "Nh?? cung c???p CA";
    var global_fm_certpurpose = "Lo???i ch???ng th?? s???";
    if(IsWhichCA === "18") {
        global_fm_certpurpose = "Lo???i CTS";
    }
    var global_fm_certalgorithm = "Thu???t to??n ch???ng th?? s???";
    var global_fm_Password_new = "M???t kh???u m???i";
    var global_fm_Password_conform = "X??c nh???n m???t kh???u m???i";
    var global_fm_Password_old = "M???t kh???u hi???n t???i";
    var global_fm_Password_change = "Thay ?????i m???t kh???u";
    var global_fm_button_PasswordChange = "?????ng ??";
    var global_fm_button_setup = "C??i ?????t";
    var global_fm_button_setup_ejbca = "C??i ?????t t??? RA";
    var global_fm_button_import = "????ng t???i";
    var global_fm_button_check = "Ki???m tra";
    var global_fm_valid = "Ng??y hi???u l???c ch???ng th?? s???";
    var global_fm_valid_cert = "Ng??y hi???u l???c";
    var global_fm_browse_file = "Ch???n file";
    var global_fm_browse_cert_note = "Vui l??ng ch???n file c?? dung l?????ng nh??? h??n ";
    var global_fm_fileattach_support = "C??c ?????nh d???ng file ???????c h??? tr???: ";
    var global_fm_browse_cert_addnote = "??u ti??n c??c file pdf, image";
    var global_fm_Expire = "Ng??y h???t hi???u l???c ch???ng th?? s???";
    var global_fm_Expire_cert = "Ng??y h???t hi???u l???c";
    var global_fm_pass_p12 = "M???t kh???u P12";
    var global_fm_dateUpdate = "Ng??y c???p nh???t";
    var global_fm_dateUpdate_next = "Ng??y c???p nh???t k??? ti???p";
    var global_fm_dateend = "Ng??y k???t th??c";
    var global_fm_activation = "Ng??y k??ch ho???t";
    var global_fm_Method = "Ph????ng th???c";
    var global_fm_mode = "Ch??? ?????";
    var global_fm_worker = "T??n worker";
    var global_fm_isbackoffice_grid = "FrontOffice/BackOffice";
    var global_fm_isbackoffice = "FrontOffice/BackOffice";
    var global_fm_key = "T??n kh??a";
    var global_fm_logout = "????ng xu???t";
    var global_fm_title_account = "Th??ng tin t??i kho???n";
    var global_fm_otp_serial = "OTP Serial";
    var global_fm_check_date = "T??m theo ng??y";
    var global_fm_check_date_profile = "T??m theo (Ng??y nh???n h??? s??)";
    var global_fm_check_date_control = "T??m theo (Ng??y ?????i so??t)";
    var global_fm_expire_date = "S??? ng??y c??n hi???u l???c";
    var global_fm_check_month = "T??m ki???m theo tha??ng";
    var global_fm_check_quarterly = "T??m ki???m theo quy??";
    var global_fm_check_token = "T??m ki???m theo M?? token";
    var global_fm_company = "C??ng ty";
    var global_fm_issue = "Ph??t h??nh";
    var global_fm_size = "K??ch th?????c (KB)";
    var global_fm_OU = "????n v??? t??? ch???c (OU)";
    var global_fm_MST = "M?? s??? thu???";
// BK->   var global_fm_enterprise_id = "M?? s??? thu???/ M?? ng??n s??ch/ S??? quy???t ?????nh/ B???o hi???m x?? h???i";
// BK->   var global_fm_personal_id = "CMND/ C??n c?????c c??ng d??n/ H??? chi???u/ M?? s??? thu???/ B???o hi???m x?? h???i";
    var global_fm_enterprise_id = "UID doanh nghi???p";
    var global_fm_personal_id = "UID c?? nh??n";
    var global_fm_callback_url = "???????ng d???n m??y tr???m t??ch h???p API ????? nh???n th??ng b??o y??u c???u t??? Token Manager";
    var global_fm_callback_when_approve = "Gi?? tr??? c???a callback URL khi duy???t";
    var global_fm_decision = "S??? quy???t ?????nh";
    var global_fm_share_mode_cert = "Cho ph??p b??? sung th??ng tin d???ch v??? CTS";
    var global_fm_issue_p12_enabled = "Cho ph??p c???p ph??t CTS P12";
    var global_fm_ID = "M?? s???";
    var global_fm_date_grant = "C???p ng??y";
    var global_fm_organi_grant = "T??? ch???c c???p";
    var global_fm_representative_legal = "Ng?????i ?????i di???n ph??p lu???t";
    var global_fm_MNV = "M?? nh??n vi??n";
    var global_fm_CMND = "CMND";
    var global_fm_CMND_ID_Card = "S??? CMND, th??? c??n c?????c";
    var global_fm_place = "N??i c???p";
    var global_fm_cmnd_date = "Ng??y c???p";
    var global_fm_O = "T??? ch???c (O)";
    var global_fm_O_notrefix = "T??? ch???c";
    var global_fm_L = "Qu???n/Huy???n (L)";
    var global_fm_T = "Ch???c v??? (T)";
    var global_fm_C = "Qu???c gia (C)";
    var global_fm_ST = "T???nh/Th??nh ph??? (ST)";
    var global_fm_CN = "T??n c??ng ty (CN)";
    var global_fm_grid_CN = "T??n ch??? th??? (CN)";
    var global_fm_grid_personal = "T??n c?? nh??n";
    var global_fm_grid_company = "T??n c??ng ty";
    var global_fm_grid_domain = "T??n mi???n";
    var global_fm_CN_CN = "H??? v?? t??n (CN)";
    var global_fm_serial = "S??? Serial CTS";
    var global_fm_choose_owner_cert = "T??m theo";
    var global_fm_Status = "Tr???ng th??i";
    var global_fm_branch_status = "Tr???ng th??i ?????i l??";
    var global_fm_status_control = "Tr???ng th??i ?????i so??t";
    var global_fm_Status_token = "Tr???ng th??i token";
    var global_fm_user_lock_unlock_token = "Nh??n vi??n l??m l???nh kh??a/m??? kh??a token";
    var global_fm_Status_signed = "Tr???ng th??i k??";
    var global_fm_Status_notice = "Tr???ng th??i th??ng b??o";
    var global_fm_apply_signed = "???? k??";
    var global_fm_unapply_signed = "Ch??a k??";
    var global_fm_Status_cert = "Tra??ng tha??i ch????ng th?? s????";
    var global_fm_Status_request = "Tr???ng th??i y??u c????u";
    var global_fm_Status_agreement = "Tr???ng th??i h???p ?????ng";
    var global_fm_os_type = "Lo???i OS";
    var global_fm_smart_version = "Phi??n b???n h??? th???ng";
    var global_fm_from_system = "T??? h??? th???ng";
    var global_fm_from_system_uri = "T??? ???????ng d???n";
    var global_fm_to_system = "?????n h??? th???ng";
    var global_fm_to_system_uri = "?????n ???????ng d???n";
    var global_fm_activity = "Ho???t ?????ng";
    var global_fm_lost = "B??o m???t";
    var global_fm_relost = "H???y b??? b??o m???t";
    var global_fm_lock = "Kh??a";
    var global_fm_type = "Lo???i";
    var global_fm_value = "Gi?? tr???";
    var global_fm_chain_cert = "Ch???ng th?? s??? nh?? cung c???p";
    var global_error_chain_cert = "Ch???ng th?? s??? nh?? cung c???p kh??ng t???n t???i";
    var global_error_cert_compare_ca = "Ch???ng th?? s??? kh??ng h???p l???";
    var global_error_cert_compare_csr = "Ch???ng th?? s??? kh??ng h???p l???";
    var global_fm_Note = "Ghi ch??";
    var global_fm_Note_offset = "Ghi ch?? h??? s??";
//    var global_fm_soft_copy = "B???n ??i???n t???";
//    if(IsWhichCA === "7") {
    var global_fm_soft_copy = "Tr???ng th??i c???a c??c h??? s?? ph??p l??";
    //}
    var global_fm_Content = "N???i dung";
    var global_fm_tran_code = "M?? giao d???ch";
    var global_fm_tran_timeout = "Timeout giao d???ch (gi??y)";
    var global_fm_filter_search = "Ti??u ch?? t??m ki???m";
    var global_fm_combox_success = "Th??nh c??ng";
    var global_fm_combox_errorsend = "G???i l???i";
    var global_fm_cert_circlelife = "V??ng ?????i ch???ng th?? s???";
    var global_req_all = "Vui l??ng nh???p ?????y ????? th??ng tin";
    var global_req_length = "Chi???u d??i kh??ng h???p l???";
    var global_req_file = "Vui l??ng ch???n file";
    var global_req_file_has_data = " (File c?? n???i dung)";
    var global_req = policy_req_empty;
    var global_errorsql = "C?? l???i x???y ra, vui l??ng th??? l???i sau";
    var global_print2_fullname_business = "T??n giao d???ch ?????y ????? (Vi???t hoa, c?? d???u)";
    var global_req_email_subject_san = "Vui l??ng nh???p email trong ch???ng th?? s??? gi???ng nhau";
    var global_req_print_not_support = "Lo???i y??u c???u kh??ng ???????c h??? tr??? in";
    var global_req_warning_exists_cert = "???? t???n t???i y??u c???u ??ang ch??? duy???t v???i th??ng tin ch???ng th?? s??? nh?? tr??n\nTi???p t???c ????ng k???";
    
    var global_alert_login = "Th???i gian ????ng nh???p ???? h???t, vui l??ng ????ng nh???p l???i";
    var global_alert_another_login = "T??i kho???n ???? b??? kh??a/ b??? truy c???p t??? thi???t b??? kh??c, vui l??ng ki???m tra l???i";
    var global_alert_another_menu = "B???n kh??ng c?? quy???n truy c???p ch???c n??ng n??y, vui l??ng ki???m tra l???i";
    var global_alert_license_invalid = "License kh??ng h???p l???. Xin vui l??ng li??n h??? Hotline 1900 6884 \nho???c Email info@mobile-id.vn ????? ???????c h??? tr???";
    var global_error_login_info = "Th??ng tin t??i kho???n ????ng nh???p kh??ng t???n t???i, vui l??ng th??? l???i";
    var global_error_invalid = ": Kh??ng h???p l???";
// Admin -> LicenseList
    var license_title_list = "Qu???n l?? b???n quy???n h??? th???ng";
    var license_table_list = "????ng t???i t???p tin b???n quy???n";
    var license_title_search = "T??m ki???m";
    var license_title_edit = "Th??ng tin chi ti???t";
    var license_req_file = "Vui l??ng ch???n t???p tin danh s??ch b???n quy???n";
    var license_fm_file = "Ch???n t???p tin";
    var license_succ_import = "????ng t???i t???p tin b???n quy???n th??nh c??ng";
    var license_group_hardware = "Th??ng tin Hardware";
    var license_group_view = "Th??ng tin chi ti???t b???n quy???n";
    var license_fm_type = "Lo???i b???n quy???n";
    var license_fm_token_sn = "Token SN";
    var license_fm_user_enabled = "???????c s??? d???ng";
    var license_group_Function = "Th??ng tin v??? t??nh n??ng";
    var license_error_file = "t???p tin danh s??ch b???n quy???n kh??ng h???p l???";
    var license_error_no_token_sn = "????ng t???i th???t b???i.\nGi?? tr??? [TOKEN_SN] kh??ng t???n t???i";
    var license_error_no_license_type = "????ng t???i th???t b???i.\nGi?? tr??? [LICENSE_TYPE] kh??ng t???n t???i";
    var license_succ_import_insert = ". Th??m m???i: ";
    var license_succ_import_update = " ; ???? t???n t???i: ";
    var license_succ_import_error = " ; L???i: ";

// sumary page website
    var CSRF_Mess = 'Phi??n s??? d???ng ???? h???t, vui l??ng t???i l???i trang';
    var TitleLoginPage = "H??? th???ng qu???n tr??? ?????i l??";
    var TitlePolicyPage = "Ch??nh s??ch b???o m???t";
    var TitleTermsPage = "??i???u kho???n d???ch v???";
    var TitlePolicyLink = "Ch???nh s??ch b???o m???t";
    var TitleTermsLink = "??i???u kho???n d???ch v???";
    var TitleHomePage = "Trang ch??? Back-Office";
    var error_title_list = "L???i h??? th???ng";
    var error_content_home = "C?? l???i h??? th???ng x???y ra. Vui l??ng quay l???i trang ch???";
    var error_content_login = "C?? l???i h??? th???ng x???y ra. Vui l??ng quay l???i ????ng nh???p";
    var error_content_link_download = "T???i xu???ng t???p tin m?? t??? l???i";
    var error_content_link_out = "t???i ????y";
    var login_req_captcha = "M?? CAPTCHA kh??ng ch??nh x??c";
    var login_title_captcha = "L??m m???i m?? captcha";
    var login_fm_captcha = "M?? CAPTCHA";
    var login_fm_forget = "Qu??n m???t kh???u ?";
    var login_fm_token_ssl = "Thi???t b??? token";
    var login_title_forget = "Qu??n m???t kh???u";
    var login_succ_forget = "Vui l??ng ki???m tra email ????? nh???n m???t kh???u m???i";
    var login_succ_forget_request = "Y??u c???u c???p m???i m???t kh???u ???? th???c hi???n th??nh c??ng. Vui l??ng ch??? qu???n tr??? h??? th???ng ph?? duy???t";
    var login_fm_buton_login = "????ng nh???p";
    var login_fm_buton_cancel = "H???y";
    var login_fm_buton_OK = "?????ng ??";
    var login_fm_buton_continue = "Ti???p t???c";
    var login_error_timeout = "Kh??ng nh???n ???????c ph???n h???i t??? m??y ch???";
    var login_error_exception = "L???i h??? th???ng. Vui l??ng th??? l???i";
    var login_error_lock = "T??i kho???n b??? t???m kh??a, vui l??ng th??? l???i sau";
    var login_error_incorrec = "Th??ng tin ????ng nh???p kh??ng ch??nh x??c";
    var login_error_inactive = "T??i kho???n b??? kh??a. Vui l??ng li??n h??? qu???n tr??? h??? th???ng";
    var login_error_token_ssl = "B???n kh??ng c???p quy???n ????ng nh???p theo ph????ng th???c Token";
    var login_conform_forget = "Vui lo??ng xa??c nh????n la??i th??ng tin email: {EMAIL}";

    var global_fm_detail = "Chi ti???t";
    var global_fm_expand = "M??? r???ng";
    var global_fm_collapse = "Thu g???n";
    var global_fm_hide = "???n";
    var global_fm_search_expand = "M??? r???ng t??m ki???m";
    var global_fm_search_hide = "Thu g???n t??m ki???m";
    
    var global_fm_button_reset = "Reset";
    var global_fm_button_activate = "K??ch ho???t";
    var global_fm_button_unactivate = "H???y k??ch ho???t";
    var global_fm_file_name = "T??n file";
    var global_fm_down = "T???i xu???ng";
    var global_fm_view = "Xem";
    var global_fm_p12_down = "T???i xu???ng P12";
    var global_fm_p7p_down = "T???i xu???ng P7B";
    var global_fm_down_enterprise = "CTS doanh nghi???p";
    var global_fm_down_personal = "CTS c?? nh??n";
    var global_fm_down_staff = "CTS c?? nh??n trong doanh nghi???p";
    var global_fm_down_pem = "T???i xu???ng PEM";
    var global_fm_license_down = "T???i gi???y ch???ng nh???n";
    var global_fm_license_create = "T???o gi???y ch???ng nh???n";
    var global_fm_sign_confirmation = "K?? l???i gi???y x??c nh???n";
    var global_fm_wait_sign_confirmation = "??ang ch??? k?? gi???y x??c nh???n";
    var global_cbx_wait_sign_confirmation = "??ang ch??? k?? l???i";
    var global_cbx_sign_confirmation = "???? k?? l???i";
    var global_succ_license_create = "T???o gi???y ch???ng nh???n ??i???n t??? th??nh c??ng";
    var global_fm_down_cts = "T???i ch???ng th?? s???";
    var global_fm_change = "Thay ?????i";
    var global_fm_dispose = "H???y b???";
    var global_fm_copy_all = "Ch??p v??o Clipboard";
    var global_succ_copy_all = "Ch??p v??o Clipboard th??nh c??ng";

    ///face 03 ///
    var global_req_formfactor_support = "H??? th???ng ch??a h??? tr??? ph????ng th???c t??? BackOffice";
    var global_req_no_special = " kh??ng bao g???m k?? t??? ?????c bi???t";
    var global_req_no_space = " kh??ng bao g???m kho???ng tr???ng";
    var global_fm_button_delete = "X??a";
    var global_fm_paging_total = "T???ng s??? d??ng ";
    var policy_check_length_pass = "Chi???u d??i t???i thi???u ph???i nh??? h??n chi???u d??i t???i ??a m???t kh???u";
    var policy_check_number_zero = " " + "ph???i l???n h??n 0";
    var global_fm_button_reset_pass = "C???p m???i m???t kh???u";
    var global_fm_button_check_pass_default = "Ki???m tra m???t kh???u m???c ?????nh";
    var global_fm_character = " " + "k?? t???";
    var global_fm_phone_zero = " " + "ph???i b???t ?????u b???ng s??? 0";
    var global_fm_phone_8_11 = "S??? ??i???n tho???i ph???i n???m trong kho???ng t??? 8 ?????n 11 k?? t???";
    var pass_req_no_space = "M???t kh???u kh??ng bao g???m kho???ng tr???ng";
    var user_req_no_space = "T??n ????ng nh???p kh??ng bao g???m kho???ng tr???ng";
    var pass_req_min_greater = "Chi???u d??i t???i thi???u m???t kh???u ph???i >= ";
    var pass_req_max_less = "Chi???u d??i t???i ??a m???t kh???u ph???i <= ";
    var pass_req_character = "M???t kh???u ph???i t???n t???i ??t nh???t 1 k?? t??? ch???";
    var pass_req_special = "M???t kh???u ph???i t???n t???i ??t nh???t 1 k?? t??? ?????c bi???t";
    var pass_req_number = "M???t kh???u ph???i t???n t???i ??t nh???t 1 k?? t??? s???";
    var pass_req_upcase = "M???t kh???u ph???i t???n t???i ??t nh???t 1 k?? t??? hoa";
    var pass_req_another_old = "M???t kh???u m???i ph???i kh??c m???t kh???u hi???n t???i";
    var pass_req_conform_new = "X??c nh???n m???t kh???u m???i kh??ng ch??nh x??c";
    var pass_error_old = "M???t kh???u hi???n t???i kh??ng ????ng";
    var pass_error_choise_another = "Vui l??ng ch???n m???t kh???u kh??c";
    var pass_error_choise_another_exists = "M???t kh???u m???i kh??ng th??? tr??ng v???i {NUMBER} m???t kh???u cu???i c??ng! Vui l??ng nh???p m???t kh???u m???i";
    var pass_succ_change = "Thay ?????i m???t kh???u th??nh c??ng";
    var pass_succ_change_show = ". M???t kh???u l??: ";
    var pass_error_account_old = "Th??ng tin t??i kho???n hi???n t???i kh??ng ch??nh x??c";

    var global_fm_check_search = "Vui l??ng ch???n ??t nh???t 1 ti??u ch?? ????? t??m ki???m";
    var pass_fm_Password_first = "Th??ng tin thay ?????i m???t kh???u";

// Send mail
    var sendmail_error = "G???i mail th???t b???i. Vui l??ng th??? l???i sau";
    var sendmail_success = "G???i th?? th??nh c??ng";
    var sendmail_notexists = "?????a ch??? email kh??ng ch??nh x??c";
    var sendmail_notexists_account = "T??n ????ng nh???p kh??ng ch??nh x??c";

    // Send mail Password Signserver
    var sendmail_error_signserver = "G???i mail th???t b???i. Vui l??ng th??? l???i sau";

//Global
    var global_alert_search_multiline = "S??? l?????ng t??m ki???m v?????t qu?? 10,000 d??ng, h??? th???ng s??? t??? ?????ng t??m";
    var global_error_export_excel = "Xu???t file Excel th???t b???i";
    var global_success_export_excel = "H??? th???ng ???? ti???p nh???n y??u c???u k???t xu???t file th??nh c??ng";
    var global_error_insertmenulink = "Th??m m???i m??n h??nh ch???c danh th???t b???i";
    var global_error_deletemenulink = "X??a m??n h??nh ch???c danh th???t b???i";
    var global_search_time_all = "Theo t???t c??? th???i gian trong h??? th???ng";
// button
    var global_button_grid_delete = "X??a";
    var global_button_grid_edit = "Ch???nh s???a";
    var global_button_grid_smart_id = "SMART-ID";
    var global_button_grid_mobile_otp = "MOBILE-OTP";
    var global_button_grid_uaf = "UAF";
    var global_button_grid_config = "C???u h??nh";
    var global_button_grid_pki = "PKI";
    var global_button_grid_OTP = "OTP";
    var global_button_grid_lock = "Kh??a";
    var otp_button_grid_lock = "Block";
    var global_label_grid_sum = "T???ng c???ng ";
    var global_button_grid_cancel = "H???y";
    var global_button_grid_authen = "X??c th???c";
    var global_button_grid_synch = "?????ng b???";

    var global_button_grid_lost = "Lost";
    var global_button_grid_unlost = "UnLost";
    var global_button_grid_unlock = "M??? kh??a";
    var global_button_grid_resetpasscode = "C???p l???i Passcode";
    var global_button_grid_sendmail = "G???i Mail";
    var global_button_reconfirm = "X??c nh???n l???i";
    var global_button_p12_sendmail = "G???i Mail P12";
    var otp_button_grid_unlock = "UnBlock";
    var global_button_grid_enable = "Enable";
    var global_button_grid_disable = "Disable";
    var global_fm_gen_pass = "T???o m???t kh???u";
    var global_no_data = "Kh??ng t??m th???y d??? li???u !";
    var global_no_getcompany_data = "Kh??ng t??m ???????c d??? li???u. Vui l??ng nh???p th??ng tin";
    var global_no_print_data = "Kh??ng t??m th???y m???u in !";
    var global_no_file_list = "Danh s??ch File tr???ng";
    var global_check_datesearch = "T??? ng??y ph???i nh??? h??n ho???c b???ng ?????n ng??y";
    var global_check_date_expired = "Th???i gian h???t h???n ph???i l???n h??n ng??y hi???n t???i";
    var global_succ_succ = "Th??nh c??ng";
    var global_fm_mess_in = "Th??ng tin d??? li???u y??u c???u";
    var global_fm_mess_out = "Th??ng tin d??? li???u tr??? v???";
    var global_fm_Status_OTP = "S??? l???n x??c th???c m?? k??ch ho???t sai c??n l???i";
    var global_fm_Status_SignServer = "Tr???ng th??i h???p ?????ng SignServer";
    var global_fm_Method_Smart_ID = "Ph????ng th???c Smart ID";
    var global_fm_Method_Mobile_OTP = "Ph????ng th???c Mobile OTP";
    var global_fm_Method_UAF = "Ph????ng th???c UAF";
    var global_fm_Status_PKI = "Tr???ng th??i h???p ?????ng PKI";
    var global_fm_status_profile = "Tr???ng th??i h??? s??";
    var global_fm_Function_tran = "H??m";
    var global_fm_import_choise_text = "File ch???";
    var global_fm_import_choise_image = "File h??nh ???nh";
    var global_req_text_format = "Vui l??ng nh???p ????ng ?????nh d???ng file ch??? .txt, .pem";

    var global_req_phone_format = "Vui l??ng nh???p ????ng ?????nh d???ng s??? ??i???n tho???i";
    var user_error_no_data = "Th??ng tin nh??n vi??n kh??ng t???n t???i, vui l??ng ki???m tra l???i";
    var user_conform_reset_pass = "B???n mu???n c???p m???i m???t kh???u ?";
    var token_confirm_delete = "B???n c?? mu???n x??a Token n??y ?";
    var user_conform_delete = "B???n mu???n x??a nh??n vi??n n??y ?";
    var user_succ_delete = "Xo??a nh??n vi??n tha??nh c??ng";
    var global_fm_require_label = " " + "(*)";
    var global_fm_import_sample = "Tham kh???o t???p tin m???u: ";
    var global_req_info_cert = "Th??ng tin ch???ng th?? s??? kh??ng h???p l???";
    //request -> tokenlist
    var token_title_list = "Qu???n l?? Token";
    var token_title_search = "T??m ki???m";
    var token_title_table = "Danh s??ch";
    var token_title_edit = "Chi ti???t Token";
    var token_title_add = "Th??m m???i Token";
    var token_title_init = "Kh???i t???o Token";
    var token_title_changesopin = "Thay ?????i SOPIN c???a Token";
    var token_title_delete = "X??a token";
    var token_exists_tokensn = "Token SN ???? t???n t???i";
    var token_succ_edit = "C???u h??nh th??nh c??ng";
    var token_succ_delete = "X??a th??nh c??ng";
    var token_conform_update_multi = "B???n mu???n c???u h??nh nhi???u Token ?";
    var token_succ_add_renew = "Th??m m???i th??nh c??ng";
    var token_fm_tokenid = "M?? Token";
    var token_fm_signing_number = "S??? l???n k??";
    var token_fm_sopin = "SOPIN";
    var token_fm_tokenid_new = "M?? Token m????i";
    var token_fm_subject = "T??n ?????i t?????ng";
    var token_fm_company = "T??n c??ng ty";
    var token_fm_valid = "Hi???u l???c t??? (Ch???ng th?? s???)";
    var token_fm_expire = "Hi???u l???c ?????n (Ch???ng th?? s???)";
    var global_fm_FromDate_valid = "Hi???u l???c t??? (B???t ?????u)";
    var global_fm_ToDate_valid = "Hi???u l???c t??? (K???t th??c)";
    var global_fm_FromDate_expire = "Hi???u l???c ?????n (B???t ?????u)";
    var global_fm_FromDate_profile = "T??? (Ng??y nh???n h??? s??)";
    var global_fm_ToDate_profile = "?????n (Ng??y nh???n h??? s??)";
    var global_fm_From_Control = "T??? (Ng??y ?????i so??t)";
    var global_fm_To_Control = "?????n (Ng??y ?????i so??t)";
    
    var global_fm_From_effective = "T??? (Ng??y hi???u l???c)";
    var global_fm_To_effective = "?????n (Ng??y hi???u l???c)";
    var global_fm_From_expire = "T??? (Ng??y h???t hi???u l???c)";
    var global_fm_To_expire = "?????n (Ng??y h???t hi???u l???c)";
    
    var global_fm_ToDate_expire = "Hi???u l???c ?????n (K???t th??c)";
    var token_fm_validexpire_search = "Th???i gian hi???u l???c v?? h???t hi???u l???c (Ch???ng th?? s???)";
    var token_fm_personal = "T??n c?? nh??n";
    var token_fm_serialcert = "Certificate SN";
    var token_fm_thumbprintcert = "Thumbprint";
    var token_fm_taxcode = "MST";
    var token_fm_block = "Kh??a";
    var token_fm_reason_block = "L?? do kh??a";
    var token_fm_all_apply = "??p d???ng cho t???t c??? Token trong h??? th???ng (b??? qua file ????ng t???i)";
    var token_fm_unblock = "M??? kh??a";
    var token_fm_csr = "CSR (Y??u c???u c???p ph??t ch???ng th?? s???)";
    var token_fm_innit = "Kh???i t???o";
    var token_fm_change = "Thay ?????i SOPIN";
    var token_fm_datelimit = "Ch???ng th?? s??? hi???u l???c ?????n";
    var token_fm_mobile = "??i???n tho???i";
    var token_fm_email = "Email";
    var token_fm_address = "?????a ch???";
    var token_fm_address_permanent = "?????a ch??? th?????ng tr??";
    var token_fm_address_billing = "?????a ch??? xu???t h??a ????n";
    var token_fm_address_residence = "H??? kh???u th?????ng tr??";
    var token_fm_menulink = "Menu ?????ng";
    var token_fm_linkname = "T??n Menu";
    var token_fm_linkvalue = "Website";
    var token_fm_noticepush = "Hi???n th??? th??ng b??o (sticker) tr??n Token Manager";
    var token_fm_noticeinfor = "N???i dung th??ng b??o";
    var token_fm_noticelink = "Li??n k???t th??ng b??o";
    var token_fm_colortext = "M??u ch???";
    var token_fm_colorgkgd = "M??u n???n";
    var token_fm_infor = "Th??ng tin";
    var token_fm_location = "V??? tr??";
    var token_fm_state = "Qu???n/Huy???n";
    var token_fm_enroll = "C???p ph??t";
    var token_fm_TimeOffset = "Th???i h???n h???p ?????ng";
    var token_fm_expire_mmyy = "Th???i gian h???t h???n";
    var token_fm_dn = "DN (Distinguished Name)";
    var token_fm_passport = "CMND/HC";
    var token_fm_version = "Phi??n b???n Token";
    var token_fm_agent = "?????i l??";
    var token_fm_agent_level_one = "?????i l?? c???p 1";
    var token_group_info = "Th??ng tin chi ti???t";
    var token_group_update = "C???u h??nh";
    var token_group_notification = "Th??ng tin hi???n th??? (sticker) tr??n thanh ngang c???a Token Manager";
    var token_group_dynamic = "Menu ?????ng";
    var token_group_other = "C???u h??nh kh??c";
    var token_group_cert_history = "L???ch s??? ch???ng th?? s??? c???a Token";
    var token_group_request_edit = "Danh s??ch y??u c???u ch???nh s???a Token";
    var token_title_import = "T???i l??n danh s??ch Token";
    var token_fm_typesearch = "T??m ki???m cho";
    var token_fm_import_sample = "Tham kh???o file m???u: ";
    var token_fm_datelimit_example = "V?? d???: (ISO 8601 date: [yyyy-MM-dd HH:mm:ssZZ]: '2018-09-20 14:01:28+07:00')";
    var token_error_no_column = "T???i l??n th???t b???i.\n?????nh d???ng c???t th??ng tin kh??ng ????ng";
    var token_error_no_tokenid = "T???i l??n th???t b???i.\nGi?? tr??? [TOKEN_SN] kh??ng t???n t???i";
    var token_error_no_sopin = "T???i l??n th???t b???i.\nGi?? tr??? [TOKEN_SOPIN] kh??ng t???n t???i";
    var token_error_no_agent = "T???i l??n th???t b???i.\nGi?? tr??? [Agency] kh??ng t???n t???i";
    var token_error_datelimit_format = "Vui l??ng nh???p ?????nh d???ng ng??y h???p l???";
    var token_error_datelimit_date = "Vui l??ng nh???p th???i gian h???t hi???u l???c";
    var token_error_datelimit_format_date = "?????nh d???ng ng??y: (ddd:HH:MM)";
    var token_succ_import = "Qu?? tr??nh t???i l??n th??nh c??ng";
    var token_succ_setup = "C??i ?????t th??nh c??ng";
    var token_succ_check_import = "Qu?? tr??nh ki???m tra th??nh c??ng. Nh???n n??t c??i ?????t ????? l??u th??ng tin";
    var token_error_check_import = "C?? l???i x???y ra, vui l??ng ki???m tra chi ti???t trong file k???t qu???";
    var token_succ_import_insert = ". Th??m m???i: ";
    var token_succ_import_update = " ; C???p nh???t: ";
    var token_succ_import_error = " ; L???i: ";
    var token_succ_import_insert_replace = "Th??m m???i: ";
    var token_succ_import_update_replace = "C???p nh???t: ";
    var token_succ_import_error_replace = "Kh??ng th??nh c??ng: ";
    var token_error_import_format = "?????nh d???ng Excel: XLS, XLSX, CSV";
    var token_fm_lock_opt = "Tr???ng th??i x??c th???c m?? k??ch ho???t b??? kh??a";
    var token_fm_unlock_opt = "Tr???ng th??i x??c th???c m?? k??ch ho???t ??ang ho???t ?????ng";
    var token_confirm_unlock_temp = "B???n ch???c ch???n mu???n m??? kh??a ?";
    var token_confirm_resetpasscode_temp = "B???n ch???c ch???n mu???n c???p l???i Passcode ?";
    var token_confirm_lock_temp = "B???n ch???c ch???n mu???n kh??a ?";
    var token_succ_reset_opt = "M??? kh??a tr???ng th??i x??c th???c m?? k??ch ho???t th??nh c??ng";
    
    var tokenreport_title_list = "B??o c??o t???n kho ?????i l??";
    var tokenreport_fm_choose_time_export = "Th???i gian xu???t kho";
    var tokenreport_fm_choose_time_import = "Th???i gian nh???p kho";
    var tokenreport_fm_agenct_date_export = "Ng??y xu???t kho cho ?????i l??";
    
    //request -> certificatelist
    var cert_title_list = "Qu???n l?? duy???t ch???ng th?? s???";
    var cert_title_search = "T??m ki???m";
    var cert_title_table = "Danh s??ch";
    var cert_title_edit = "Th??ng tin y??u c???u";
    var cert_title_register_cert = "Th??ng tin ch???ng th?? s???";
    var cert_title_register_csr = "Th??ng tin CSR";
    var cert_title_register_owner = "Th??ng tin ch??? s??? h???u";
    var cert_succ_approve = "Duy???t ch???ng th?? s??? th??nh c??ng";
    var cert_error_approve = "Duy???t ch???ng th?? s??? th???t b???i";
    var cert_succ_reissue = "C???p ph??t ch???ng th?? s??? th??nh c??ng";
    var cert_fm_type_request = "Lo???i y??u c???u";
    var cert_fm_request = "y??u c???u";
    var cert_fm_request_agent = "Duy???t trong ?????i l??";
    var cert_fm_major_name = "T??n ch???c n??ng";
    var cert_fm_major_code = "M?? ch???c n??ng";
    var cert_fm_profile_list = "Lo???i ch???ng th?? s???";
    var cert_fm_cert_profile = "Certificate Profile (CORECA)";
    var cert_fm_delete_cert = "X??a ch???ng th?? s???";
    var cert_fm_usereib = "Entity Name (CORECA)";
    var cert_fm_date_approve_fee = "Th???i gian duy???t c???p ch???ng th?? s???";
    var cert_fm_user_fee = "T??i kho???n c???p ch???ng th?? s???";
    var cert_succ_edit = "C???u h??nh th??nh c??ng";
    var cert_succ_returnfee = "C???u h??nh th??nh c??ng";
    var cert_fm_Status = "Duy???t ch???ng th?? s???";
    var cert_fm_push_notice = "Cho ph??p g???i Email";
    var cert_fm_revoke_delete = "X??a ch???ng th?? s??? b??? thu h???i";
    var cert_fm_restore_status_old = "Kh??i ph???c tr???ng th??i ho???t ?????ng c???a ch???ng th?? s??? c??";
    var cert_fm_revoke_delete_old = "X??a ch???ng th?? s??? c?? khi ch???ng th?? s??? m???i ???????c c???p";
    var cert_confirm_otp_sendmail = "B???n ch???c ch???n mu???n g????i mail ma?? ki??ch hoa??t ?";
    var cert_succ_otp_resend = "G????i ma?? ki??ch hoa??t tha??nh c??ng";
    var global_error_appove_status = "Tr???ng th??i duy???t y??u c???u kh??ng h???p l???. Duy???t th???t b???i";
    var global_error_method = "Ph????ng th???c kh??ng h???p l???";
    var global_error_keysize_csr = "????? d??i kh??a CSR kh??ng h???p l???";
    var global_error_exist_csr = "CSR ???? t???n t???i trong h??? th???ng";
    var global_fm_button_renewal = "Th??m m???i y??u c???u c???p b??";
    var info_group_info = "Chi ti???t y??u c???u";
    var global_group_cert = "Chi ti???t ch???ng th?? s???";
    var global_fm_Corporation = "Corporation";
    var global_fm_renew_access = "G??i ??ang ??p d???ng cho gia h???n ch???ng th?? s???";
    var global_fm_renew_access_search = "T??m theo g??i ??ang ??p d???ng cho gia h???n ch???ng th?? s???";
    var global_fm_csr_info_cts = "Th??ng tin y??u c???u c???p ph??t ch???ng th?? s???";
    var global_fm_san_info_cts = "Th??ng tin ch???ng th?? s??? b??? sung";
    var global_fm_csr_info_cts_before = "Th??ng tin ch???ng th?? s??? tr??????c khi thay ??????i";
    var global_fm_csr_info_cts_after = "Th??ng tin ch???ng th?? s??? sau khi thay ??????i";
    var info_fm_profile_name = "Lo???i ch???ng th?? s???";
    var info_fm_type_request = "Lo???i y??u c???u";
    var info_fm_cert_profile = "Certificate Profile (CORECA)";
    //revoke cert
    var revoke_title_list = "Qu???n l?? thu h???i ch???ng th?? s???";
    var revoke_title_detail = "Chi ti???t";
    var revoke_title_search = "T??m ki???m";
    var revoke_title_table = "Danh s??ch";
    var global_fm_button_revoke = "Thu h???i";
    var global_fm_button_recovery = "Ph???c h???i";
    var global_fm_button_suspend = "T???m ng??ng";
    var global_fm_button_reissue = "C???p l???i Token";
    var global_fm_button_detail = "Chi ti???t";
//    if(IsWhichCA === "18") {
//        global_fm_button_detail = "CHI TI???T";
//    }
    var global_fm_button_print_report = "In Bi???u M???u";
    var global_fm_button_print_certificate = "In ch???ng nh???n";
    var global_fm_button_print_handover = "In b??n giao";
    var global_fm_button_print_regis = "In y??u c???u";
    var global_fm_button_print_confirm = "In x??c nh???n";
    var global_fm_button_print = "In";
    var global_fm_button_export_zip_word = "Zip File Word";
    var global_fm_button_export_zip_pdf = "Zip File PDF";
    var global_fm_button_regis = "????ng k??";
    var global_fm_button_regis_soft = "Soft Token";
    var global_fm_button_re_regis = "????ng ky?? la??i";
    var info_group_approve = "Chi ti???t duy???t y??u c???u";
    var global_fm_approve = "Duy???t";
    var global_fm_approve_ca = "Ph?? duy???t c???p CA";
    //report cert
    var certreport_title_list = "Qu???n l?? b??o c??o ch???ng th?? s???";
    var certreport_title_search = "T??m ki???m";
    var certreport_title_table = "Danh s??ch";
    //request -> RegistrationCertificate
    var regiscert_title_list = "????ng k?? ch???ng th?? s???";
    var regiscert_title_token_list = "????ng k?? ch???ng th?? s??? cho Token";
    var regiscert_soft_title_list = "????ng k?? ch???ng th?? s??? Soft Token";
    var regiscert_title_search = "T??m ki???m";
    var regiscert_title_table = "Danh s??ch";
    var regiscert_title_view = "Th??ng tin ????ng k??";
    var buymorecert_title_view = "Mua th??m ch???ng th?? s???";
    var regiscert_fm_datelimit_one = "1 n??m";
    var regiscert_fm_datelimit_two = "2 n??m";
    var regiscert_fm_datelimit_three = "3 n??m";
    var regiscert_fm_check_backup_key = "Sao l??u kh??a tr??n Server";
    var regiscert_fm_check_revoke = "Thu h???i ch???ng th?? s??? sau khi thay ?????i th??ng tin";
    var regiscert_fm_check_revoke_reissue = "Thu h???i ch???ng th?? s??? sau khi c???p l???i";
    var regiscert_fm_check_change_key = "Thay ?????i kh??a";
    var regiscert_fm_keep_certsn = "Gi??? s??? S??-ri CTS";
    var regiscert_succ_add = "????ng k?? th??nh c??ng";
    var regisapprove_title_list = "Y??u c???u ????ng k?? ch???ng th?? s???";
    var regisapprove_title_view = "Duy???t y??u c???u ????ng k?? ch???ng th?? s???";
    var approve_fm_confirm_mail = "Y??u c???u x??c nh???n qua Email";
//    var regisapprove_error_status = "Existed Token SN In List, Certificate Registration failure";
    var regisapprove_succ_approve = "Duy???t th??nh c??ng";
    //request -> tokenimport
    var tokenimport_title_list = "Qu???n l?? t???i l??n danh s??ch Token";
    var tokenimport_title_import = "T???i l??n danh s??ch Token";
    var tokenimport_title_search = "T??m ki???m";
    var tokenimport_title_table = "Danh s??ch";
    var tokenimport_succ_edit = "T???i l??n danh s??ch Token th??nh c??ng";
    var tokenimport_succ_add_renew = "T??? ?????ng gia h???n th??nh c??ng";
    var tokenimport_fm_fromtokenSN = "M?? Token (B???t ?????u)";
    var tokenimport_fm_totokenSN = "M?? Token (K???t th??c)";
    
    //token -> TokenActionImport
    var actionimport_title_list = "Qu???n l?? t???i l??n danh s??ch ch???nh s???a Token";
    var actionimport_title_import = "T???i l??n danh s??ch";
    var actionimport_title_search = "T??m ki???m";
    var actionimport_title_table = "Danh s??ch";
    var actionimport_succ_edit = "T???i l??n danh s??ch ho??n t???t";
    
    //cert -> certimport
    var certimport_title_list = "Qu???n l?? t???i l??n danh s??ch ch???ng th?? s???";
    var certimport_title_import = "T???i l??n ch???ng th?? s???";
    var certimport_file_format_invalid = "?????nh d???ng t???p kh??ng ????ng. T???i l??n th???t b???i";
    var certimport_fm_error = "Import l???i: ";
    var certimport_error_not_size = "S??? l?????ng ch???ng th?? s??? t???i ??a ???????c ph??p nh???p t??? file excel: ";
    var certimport_error_not_ca = "T??i kho???n kh??ng ???????c ph??p nh???p ch???ng th?? s??? t??? t???p excel";
    var certimport_error_not_format_file = "H??? th???ng ch??? h??? tr??? c??c ?????nh d???ng: XLS, XLSX, CSV";
    // new before translase
    var tokenimport_title_multi = "C???u h??nh nhi???u Token";
    var tokenimport_fm_createdate_search = "Th???i gian";
    var tokenimport_fm_tokensn_search = "M?? Token";
    var tokenimport_fm_result = "K???t qu??? ????ng t???i";
    var tokenimport_fm_createdate_tokensn_search = "Th???i gian v?? M?? Token";
    //Request -> LogtList
    var log_title_list = "Qu???n l?? y??u c???u b??? t??? ch???i";
    var log_table_list = "Danh s??ch";
    var log_title_search = "T??m ki???m";
    var log_title_view = "Chi ti???t y??u c???u";
    var log_fm_user_detete_request = "T??i kho???n t??? ch???i y??u c???u";
    var log_fm_date_detete_request = "Th???i gian t??? ch???i y??u c???u";
    //Request -> RequestList
    var request_title_list = "Qu???n l?? y??u c???u";
    var request_title_search = "T??m ki???m";
    var request_table_list = "Danh s??ch";
    var request_title_view = "Chi ti???t";
    var request_conform_delete = "B???n mu???n t??? ch???i y??u c???u n??y ?";
    var request_conform_revoke = "B???n mu???n thu h???i ch???ng th?? s??? n??y ?";
    var request_succ_delete = "T??? ch???i y??u c???u th??nh c??ng";
    var request_error_delete = "Y??u c???u ???? ???????c duy???t, T??? ch???i th???t b???i";
    //token -> backofficelog
    var backoffice_title_list = "C???u h??nh Token";
    var backoffice_title_search = "T??m ki???m";
    var backoffice_title_table = "Danh s??ch";
    var backoffice_title_view = "Th??ng tin chi ti???t";
    var global_fm_combox_true = "C??";
    var global_fm_combox_false = "Kh??ng";
    var global_req_enter_info_change = "Vui l??ng ch???n ??t nh???t m???t th??ng tin c???n thay ?????i";
    var global_req_format_url = "Vui l??ng nh???p ????ng ?????nh d???ng: ";
    var global_error_wrong_agency = "Truy c???p b??? t??? ch???i ?????n ?????i l?? n??y, Vui l??ng ki???m tra l???i";
    var global_error_wrong_role = "Truy c???p b??? t??? ch???i ?????n ch???c v??? n??y, Vui l??ng ki???m tra l???i";
    
    //token -> pushimport
    var pushimport_title_list = "Qu???n l?? t???i danh s??ch th??ng b??o";
    var pushimport_title_import = "T???i l??n danh s??ch th??ng b??o";
    var pushimport_succ_edit = "T???i l??n danh s??ch th??ng b??o ho??n t???t";
    var pushimport_succ_conform_down = "T???i k???t qu??? v??? m??y t??nh ?";
    var pushimport_fm_set_push = "C???p nh???t th??ng b??o theo danh s??ch";
    var pushimport_fm_delete_push = "X??a th??ng b??o theo danh s??ch";
    var pushimport_fm_text_push = "N???i dung th??ng b??o";
    var pushimport_fm_link_push = "Link th??ng b??o";
    
    //token -> collectprofile
    var collectimport_title_list = "Qu???n l?? t???i danh s??ch c???p nh???t ?????i so??t";
    var collectimport_title_import = "T???i l??n danh s??ch c???p nh???t ?????i so??t";
    var collectimport_fm_set_push = "C???p nh???t ???? ?????i so??t";
    var collectimport_fm_delete_push = "C???p nh???t kh??ng ?????i so??t";
    var collectimport_fm_control_cert = "?????i so??t ch???ng th?? s???";
    var collectimport_fm_control_profile = "?????i so??t h??? s??";
    
    //cert -> ImportDisallowanceList
    var disallowanceimport_title_list = "Qu???n l?? danh s??ch ??en";
    var disallowanceimport_title_import = "T???i danh s??ch";
    var disallowanceimport_succ_edit = "T???i th??nh c??ng.";
    var disallowanceimport_succ_conform_down = "T???i k???t qu??? v??? m??y t??nh?";
    var disallowanceimport_fm_set_push = "C???p nh???t theo danh s??ch";
    var disallowanceimport_fm_delete_push = "X??a theo danh s??ch";
    var disallowanceimport_fm_title_blacklist = "Xu???t t???p tin CSV danh s??ch ??en";
    var disallowanceimport_fm_title_contact = "Xu???t t???p tin CSV th??ng tin li??n h??? kh??ch h??ng";
    var disallowanceimport_fm_contact_email = "Th??ng tin email";
    var disallowanceimport_fm_contact_phone = "Th??ng tin s??? ??i???n tho???i";
    var disallowanceimport_fm_note_blacklist = "Ghi ch??: xu???t file CSV danh s??ch ??en hi???n t???i c?? trong h??? th???ng";
    var disallowanceimport_fm_note_contact = "Ghi ch??: xu???t file CSV danh s??ch th??ng tin li??n h??? c???a kh??ch h??ng ????ng k?? ch???ng th?? s??? trong h??? th???ng";
    
    // NO_TRANSLATE
    //Token -> token Approve
    var tokenapprove_title_list = "Qu???n l?? danh s??ch y??u c???u ch???nh s???a Token";
    var tokenapprove_table_list = "Danh s??ch y??u c???u ch???nh s???a Token";
    var tokenapprove_title_edit = "Th??ng tin y??u c???u ch???nh s???a Token";
    //Certificate -> Template DN
    var tempdn_title_list = "?????nh d???ng ch???ng th?? s???";
    var tempdn_title_table = "Danh s??ch tr?????ng";
    var tempdn_group_Role = "Ch???n lo???i ch???ng th?? s???";
    var tempdn_group_assign = "Danh sa??ch tr?????ng ch??a g??n";
    var tempdn_table_assigned = "Danh sa??ch tr?????ng ???? g??n";
    var tempdn_conform_delete = "B???n mu???n x??a tr?????ng n??y ?";
    var tempdn_succ_delete = "X??a tr?????ng th??nh c??ng";
    var tempdn_succ_insert = "Th??m m???i tr?????ng th??nh c??ng";
    var tempdn_succ_edit = "C???p nh???t danh s??ch tr?????ng th??nh c??ng";
    var tempdn_error_edit = "Danh s??ch tr?????ng kh??ng t???n t???i";
    var tempdn_error_delete = "X??a Th??nh ph???n DN th???t b???i";
    var tempdn_error_insert = "Th??m m???i tr?????ng th???t b???i";
    var global_fm_certtype = "Lo???i ch???ng th?? s???";
    if(IsWhichCA === "18") {
        global_fm_certtype = "Lo???i CTS";
    }
    var global_fm_subjectdn = "Tr?????ng";
    var global_fm_required = "B???t bu???c";
    var global_fm_prefix = "Ti???n t???";
    var global_fm_profile_signature = "B???n k?? s???";
    var global_fm_profile_scan = "B???n scan";
    var global_fm_profile_paper = "B???n gi???y";
    var global_fm_request_function = "Y??u c???u ch???c n??ng";
    var token_confirm_cancel_request = "B???n ch???c ch???n mu???n T??? ch???i y??u c???u ?";
    var token_confirm_issue_request = "B???n ch???c ch???n mu???n ph??t h??nh ch???ng th?? s??? n??y ?";
    var token_succ_cancel_request = "T??? ch???i y??u c???u th??nh c??ng";
    var global_fm_button_decline = "T??? ch???i";
    var global_fm_button_issue = "C???p CTS";
    var global_fm_status_pendding = "Y??u c???u ch??? duy???t";
    var global_fm_status_approved = "y??u c???u ???? ???????c duy???t";
    var global_tooltip_decline_request_token = "Y??u c???u ???? ???????c duy???t, kh??ng th??? t??? ch???i";
    var token_group_unlock = "Th??ng tin m??? kh??a";
    var global_fm_duration_cts = "G??i d???ch v???";
    var global_fm_duration_cts_choose = "Ch???n g??i d???ch v???";
    var global_fm_rssp_authmodes = "Ch??? ????? x??c th???c";
    var global_fm_rssp_signning_profiles = "G??i d???ch v??? k?? s???";
    var global_fm_rssp_replying_party = "K??nh t??ch h???p";
    var global_fm_percent_cts = "Gi?? tr??? hoa h???ng";
    var global_fm_rose_type = "Lo???i hoa h???ng";
    var global_fm_rose_type_percen = "Ph???n tr??m (%)";
    var global_fm_rose_type_money = "S??? ti???n";
    var global_fm_decline_desc = "Ly?? do t???? ch????i";
    var global_fm_revoke_desc = "Ly?? do thu h???i (Ng?????i d??ng)";
    var global_fm_dipose_desc = "L?? do h???y b???";
    var global_fm_suspend_desc = "Ly?? do t???m ng??ng";
    var global_fm_revoke_reason_core = "Ly?? do thu h???i (CoreCA)";
    var global_fm_MNS = "M?? ng??n s??ch";
    var global_fm_HC = "H??? chi???u";
    if(IsWhichCA === "18") {
        global_fm_HC = "HC";
    }
    var global_fm_CitizenId = "C??n c?????c c??ng d??n";
    var global_fm_requesttype = "Lo???i y??u c???u";
    var token_fm_choose_noticepush = "Ch???n thay ?????i hi???n th???";
    var token_fm_set_no_noticepush = "?????t hi???n th??? m???c ?????nh cho Token";
    var token_fm_set_no_dynamic = "H???y b??? Menu ?????ng cho Token";
    var token_group_choose_dynamic = "Ch???n thay ?????i Menu ?????ng";
    var global_fm_button_renew = "Gia h???n";
    var global_fm_button_buymore = "Mua th??m";
    var global_fm_button_changeinfo = "Thay ?????i";
    //Certificate -> RenewCertList
    var certlist_title_list = "Qu???n l?? ch???ng th?? s???";
    var certlist_title_search = "T??m ki???m";
    var certlist_title_table = "Danh s??ch";
    var certlist_title_renew = "Gia h???n ch???ng th?? s???";
    var certlist_title_reissue = "C???p l???i ch???ng th?? s???";
    var certlist_title_revoke = "Thu h???i ch???ng th?? s???";
    
    //Certificate -> CertificateShareList
    var certsharelist_title_list = "Qu???n l?? b??? sung th??ng tin d???ch v???";
    var certsharelist_title_search = "T??m ki???m";
    var certsharelist_title_table = "Danh s??ch";
    
    var certlist_title_recovery = "Ph???c h???i ch???ng th?? s???";
    var certlist_title_suspend = "T???m ng??ng ch???ng th?? s???";
    
    var certlist_group_renew = "Th??ng tin y??u c???u gia h???n";
    var certlist_group_reissue = "Th??ng tin y??u c???u c???p l???i";
    var certlist_title_changeinfo = "Thay ?????i th??ng tin ch???ng th?? s???";
    var certlist_group_changeinfo = "Thay ?????i th??ng tin";
    var certlist_group_sender = "B??n giao";
    var certlist_group_add_info = "B??? sung th??ng tin";
    var certlist_group_add_bussiness_info = "Th??ng tin t??? ch???c, doanh nghi???p";
    var certlist_group_add_buss_pers_info = "Th??ng tin t??? ch???c, doanh nghi???p, c?? nh??n";
    var certlist_group_return_contact_info = "Th??ng tin li??n h??? tr??? h??? s??";
    var certlist_group_add_personal_info = "Th??ng tin c?? nh??n";
    var certlist_group_add_bussiness_contact = "Th??ng tin ng?????i li??n h???";
    var certlist_group_receiver = "B??n nh???n";
    var certlist_fm_unnamed = "CTS v?? danh";
    var certlist_title_detail = "Th??ng tin ch???ng th?? s???";
    var certlist_title_print_hadover = "In Bi??n B???n B??n Giao";
    var certlist_title_print_register = "In gi???y ????ng k??";
    var certlist_title_print_changeinfo = "In gi???y thay ?????i th??ng tin";
    var certlist_title_print_reissue_revoke = "In gi???y c???p l???i v?? thu h???i";
    var certlist_title_detail_old = "Th??ng tin ch???ng th?? s??? c??";
    var certlist_succ_renew = "Th??m y??u c???u gia h???n ch???ng th?? s??? th??nh c??ng";
    var certlist_succ_reissue = "Th??m y??u c???u c???p l???i ch???ng th?? s??? th??nh c??ng";
    var certlist_succ_revoke = "Th??m y??u c???u thu h???i ch???ng th?? s??? th??nh c??ng";
    var certlist_succ_revoke_ca = "Thu h???i ch???ng th?? s??? th??nh c??ng";
    var certlist_succ_changeinfo = "Th??m y??u c???u thay ?????i th??ng tin ch???ng th?? s??? th??nh c??ng";
    var certlist_succ_changepass_p12 = "Thay ?????i m???t kh???u P12 th??nh c??ng";
    var certlist_error_changepass_p12 = "Thay ?????i m???t kh???u P12 th???t b???i";
    
    var certlist_succ_recovery = "Th??m y??u c???u ph???c h???i ch???ng th?? s??? th??nh c??ng";
    var certlist_succ_recovery_ca = "Ph???c h???i ch???ng th?? s??? th??nh c??ng";
    var certlist_succ_suspend = "Th??m y??u c???u t???m ng??ng ch???ng th?? s??? th??nh c??ng";
    var certlist_succ_suspend_ca = "T???m ng??ng ch???ng th?? s??? th??nh c??ng";
    var certlist_fm_device_uuid = "UID thi???t b???";
    
    //owner -> owner
    var owner_title_list = "Qu???n l?? ch??? s??? h???u ch???ng th?? s???";
    var owner_title_search = "T??m ki???m";
    var owner_title_table = "Danh s??ch";
    var owner_title_renew = "Certificate Renewal";
    var owner_title_reissue = "Certificate Reissuance";
    var owner_title_revoke = "Certificate Revocation";
    var owner_title_recovery = "Certificate Recovery";
    var owner_title_suspend = "Certificate Suspend";
    var owner_title_add = "????ng k?? ch??? s??? h???u";
    var owner_title_view = "Th??ng tin ch??? s??? h???u";
    var owner_succ_add = "Y??u c???u ????ng k?? th??nh c??ng";
    var owner_succ_edit = "Y??u c???u thay ?????i th??ng tin th??nh c??ng";
    var ownerapprove_title_list = "Qu???n tr??? duy???t ch??? s??? h???u";
    var owner_fm_type = "Lo???i ch??? s??? h???u";
    /// no translase
    var owner_title_dispose = "H???y b??? ch??? s??? h???u";
    var owner_title_change = "Thay ?????i th??ng tin ch??? s??? h???u";
    var owner_succ_dispose = "G???i y??u c???u th??nh c??ng";
    var owner_fm_private_uid = "Th??ng tin ?????nh danh";
    var owner_succ_approve = "Duy???t th??nh c??ng";
    var owner_title_cert_search = "T??m ki???m ch??? s??? h???u";
    
// monitor -> serverlog
    var serverlog_title_list = "Qu???n l?? Log h??? th???ng";
    var serverlog_title_todate = "Xem th??ng tin Log h??m nay";
    var serverlog_title_down = "T???i Log h??? th???ng";
    var serverlog_fm_typelog = "H??? th???ng";
    var serverlog_fm_numberlog = "S??? d??ng";
    var serverlog_fm_timestamp = "Th???i gian";
    var serverlog_fm_detail = "Chi ti???t Log";
    var hastatus_fm_auto = "T??? ?????ng t???i l???i th??ng tin (Gi??y)";
    //
    
    var global_error_promotion_package_limit = "Th???i gian khuy???n m??i kh??ng ???????c l???n h??n th???i h???n g??i d???ch v???";
    var global_error_amount_package_limit = "S??? ti???n ph?? (VN??) kh??ng ???????c l???n h??n c?????c g??i d???ch v???";
    var global_fm_token_status_configed = "Tr???ng th??i ??ang c???u h??nh cho Token";
    var certlist_group_token_new = "C???u h??nh Token m???i";
    var global_error_noexists_token = "Token kh??ng t???n t???i trong h??? th???ng";
    var global_error_token_status = "Tr???ng th??i Token kh??ng h???p l???. Vui l??ng ki???m tra l???i";
    var global_error_coreca_call_approve = "C?? l???i x???y ra khi g???i qua CoreCA. Vui l??ng ki???m tra l???i";
    
    var global_error_exists_equals_dn = "Th??ng tin kh??ng bao g???m k?? t??? d???u =";
    var branch_title_table = "Danh s??ch";
    var branch_title_info = "Th??ng tin ?????i l??";
    var branch_fm_representative = "Ng?????i ?????i di???n";
    var branch_fm_representative_position = "V??? tr?? ng?????i ?????i di???n";
    var branch_fm_logo = "Logo ?????i l??";
    
    // branch access
    var branch_fm_profile_title_access = "C???u h??nh API";
    var branch_fm_api_title_access = "Truy c???p API";
    var branch_fm_profile_group_profile = "Truy c???p g??i d???ch v???";
    var branch_fm_profile_group_formfactor = "Truy c???p ph????ng th???c";
    var branch_fm_api_tag_credential = "X??c th???c SOAP-API";
    var branch_fm_rest_tag_credential = "X??c th???c REST-API";
    var branch_fm_api_tag_ip = "IP truy c???p";
    var branch_fm_api_tag_function = "H??m truy c???p";
    var branch_fm_api_allow_access = "Cho ph??p c???u h??nh API";
    var branch_fm_api_signture = "Ch??? k?? s???";
    var branch_fm_api_publishkey = "Public Key";
    var branch_fm_check_reload_cert = "Ch???n l??m m???i c???u h??nh c???a g??i d???ch v??? ???? t???n t???i";
    
    //token -> tokentransfer
    var tokentransfer_title_list = "Qu???n l?? chuy???n Token v??? ?????i l??";
    var tokentransfer_title_search = "T??m ki???m";
    var tokentransfer_title_table = "Danh s??ch";
    var certprofile_title_search = "T??m ki???m";
    var certprofile_title_table = "Danh s??ch";
    var global_error_request_exists = "???? t???n t???i y??u c???u ??ang ch??? x??? l?? c???a ch???ng th?? s??? n??y. Th??m y??u c???u th???t b???i";
    var global_error_cert_exists_token = "Token ???? t???n t???i ch???ng th?? s???. Th??m y??u c???u th???t b???i";
    var global_error_request_exists_token = "???? t???n t???i y??u c???u ??ang ch??? x??? l?? c???a Token n??y. Th??m y??u c???u th???t b???i";
    var global_error_approve_exists_cert = "Token ???? t???n t???i ch???ng th?? s???. Duy???t th???t b???i";
    var global_error_credential_external_invalid = "Th??ng tin x??c th???c h??? th???ng b??n ngo??i kh??ng h???p l???, vui l??ng ki???m tra l???i.";
    
    var tokenexport_title_list = "Xu???t danh s??ch Token";
    var tokenexport_title_search = "T??m ki???m";
    var tokenexport_title_table = "Danh s??ch";

    var global_fm_checkbox_gcndk = "Gi???y ch???ng nh???n ??KDN";
    var global_fm_checkbox_GPDT = "Gi???y ph??p ?????u t??";
    var global_fm_checkbox_QDTL = "Quy???t ?????nh th??nh l???p";
    var global_fm_choise = "Ch???n";
    var branch_fm_logo_note = "Ghi ch??: n???n h??nh ???nh trong su???t, k??ch th?????c (r???ng 210px va?? cao 70px), dung l?????ng < 500 KB, ?????nh d???ng file: png, jpg, gif";
    var branch_fm_logo_down = "T???i Logo m???u";
    var branch_error_logo_great_size = "Vui l??ng ch???n h??nh ???nh c?? dung l?????ng nh??? h??n 500 KB";
    var branch_fm_logo_change = "Thay ?????i Logo";
    var branch_fm_logo_default = "C??i ?????t m???c ?????nh";
    
    var global_succ_mst_register = "M?? s??? thu??? ???? ????ng k?? s??? d???ng ch???ng th?? s??? tr?????c ????";
    var global_succ_mns_register = "M?? ng??n s??ch ???? ????ng k?? s??? d???ng ch???ng th?? s??? tr?????c ????";
    var global_succ_cmnd_register = "S??? CMND ???? ????ng k?? s??? d???ng ch???ng th?? s??? tr?????c ????";
    var global_succ_hc_register = "S??? h??? chi???u ???? ????ng k?? s??? d???ng ch???ng th?? s??? tr?????c ????";
    
    var reportquick_fm_innit = "CTS Kh???i t???o";
    var reportquick_fm_activation = "CTS Hoa??t ??????ng";
    var reportquick_fm_revoke = "CTS Thu h????i";
    var reportquick_fm_total = "T????ng s????";
    var global_fm_cert_list = "Danh sa??ch ch????ng th?? s???? ";
    var reportquick_title_list = "Qu???n l?? ba??o ca??o ch????ng th?? s????";
    var reportquick_table_search = "Ti??m ki????m";
    var reportquick_title_add = "Ba??o ca??o ch????ng th?? s????";
    var reportquick_title_edit = "Ba??o ca??o ch????ng th?? s????";
    var global_fm_date_approve_agency = "Ng??y ??a??i ly?? duy????t";
    var global_fm_user_approve_agency = "Nh??n vi??n ??a??i ly?? duy????t";
    var global_fm_date_approve = "Ng??y duy????t";
    var global_fm_user_approve = "Nh??n vi??n duy????t";
    var global_fm_date_approve_ca = "Ng??y CA duy????t";
    var global_fm_user_approve_ca = "Nh??n vi??n CA duy????t";
    var global_error_not_user_create = "Kh??ng t????n ta??i th??ng tin ng??????i ta??o";
    var global_succ_delete = "X??a th??nh c??ng";
    var global_error_delete = "X??a th???t b???i";
    // File management
    var file_succ_delete = "X??a file th??nh c??ng";
    var file_conform_delete = "B???n mu???n x??a file n??y ?";
    var file_conform_upload = "B???n mu???n ????ng t???i file n??y ?";
    
    // NO_TRANSLATE 20180906
    var reportcertlist_title_list = "Ba??o ca??o danh sa??ch ch????ng th?? s????";
    var reportcertlist_table_search = "Ti??m ki????m";
    
    var reportcertexpire_title_list = "Danh s??ch ch???ng th?? s??? h???t h???n";
    var reportcertexpire_table_search = "Ti??m ki????m";
    
    // NO_TRANSLATE collation
    var collation_title_list = "Qu???n l?? ?????i so??t";
    var collation_fm_collated = "???? ?????i so??t";
    var collation_fm_uncollated = "Ch??a ?????i so??t";
    var collation_button_change = "?????i tr???ng th??i ?????i so??t";
    var collation_fm_change = "?????i tr???ng th??i";
    var collation_button_rose_agent = "C???p nh???t hoa h???ng";
    var collation_fm_change_change = "Ng??y ?????i tr???ng th??i ?????i so??t";
    var collation_fm_mounth = "Th??ng ?????i so??t";
    var collation_fm_time = "Ng??y ?????i so??t";
    var collation_fm_user = "Nh??n vi??n ?????i so??t";
    var collation_fm_date_receipt = "Ng??y nh???n h??? s??";
    var collation_fm_type = "H??nh th???c thu h??? s??";
    var collation_fm_type_inmounth = "H??? s?? trong th??ng";
    var collation_fm_type_compensation = "H??? s?? tr??? b??";
    var collation_fm_date_compensation = "Ng??y b?? h??? s??";
    var collation_alert_type_inmounth = "H??? s?? c??n thi???u trong th??ng";
    var collation_alert_type_compensation = "H??? s?? ???????c tr??? b?? trong th??ng";
    var collation_fm_profile_overdue = "H??? s?? qu?? h???n";
    var collation_fm_unapproved_profile = "H??? s?? c?? file ch??a ?????c";
    var collation_fm_approved_profile = "H??? s?? ???? ?????c file m???i";
    var collation_fm_money_overdue = "S??? ti???n ph???t qu?? h???n";
    var collation_fm_print_DK = "Gi???y ??K";
    var collation_fm_print_Confirm = "Gi???y x??c nh???n";
    var collation_fm_print_GPKD = "GPKD";
    var collation_fm_print_CMND = "Gi???y CMND";
    
    var profile_title_list = "Qu???n l?? h??? s??";
    var profile_title_detail = "Chi ti???t h??? s??";
    var profile_title_import_list = "????ng t???i th??ng tin h??? s??";
    var profile_fm_enoughed = "???? ?????i so??t h??? s??";
    var profile_fm_unenoughed = "Ch??a ?????i so??t h??? s??";
    var profile_conform_update = "B???n ch???c ch???n c???p nh???t tr???ng th??i ????? h??? s?? ?\nH??? s?? s??? kh??ng ???????c c???p nh???t l???n n???a.";
    
    var inputcertlist_title_list = "Nh????p li????u ??????i chi????u c??ng n????";
    var inputcertlist_table_search = "Ti??m ki????m";
    var inputcertlist_succ_edit = "C???p nh???t th??nh c??ng";
    var inputcertlist_succ_add = "Th??m m????i th??nh c??ng";
    var global_fm_monthly = "Ky?? tha??ng";
    var global_fm_title_push_approve1 = "Co?? ";
    var global_fm_title_push_approve2 = " y??u c????u ch???? c????p pha??t ch????ng th?? s??";
    var global_fm_title_push_decline = " y??u c????u bi?? t???? ch????i c????p pha??t ch????ng th?? s????";
    // ICA
    var global_error_revoke_forbiden = "Ch???ng th?? s??? kh??ng th??? thu h???i 2 l???n li??n t???c, vui l??ng ki???m tra l???i";
    var global_error_revoke_limit = "Thu h???i ch???ng th?? s??? v?????t s??? l???n quy ?????nh trong th??ng, vui l??ng li??n h??? CA";
    var global_fm_limit_revoke = "H???n m???c thu h???i trong th??ng";
    var global_fm_RP_access_esign = "Ph??n quy???n truy xu???t k??nh t??ch h???p eSigncloud";
    var global_fm_login_form = "????NG NH???P H??? TH???NG";
    var global_fm_address_GPKD = "?????a ch??? tr??n ??KKD";
    var global_fm_CitizenId_I = "CCCD";
    var global_fm_browse_file_upload = "T???i ??a ";
    var global_fm_button_add_simple = " Th??m ";
    var global_fm_button_add_action = "TH??M GIAO D???CH";
    var global_fm_button_print_profile = "IN H??? S??";
    var global_fm_button_off_notice = "T???t th??ng b??o";
    var global_fm_sign = "K??";
    var file_conform_signprofile = "B???n c?? mu???n k?? file n??y kh??ng ?";
    var fm_succ_signprofile = "K?? file th??nh c??ng";
    var global_fm_remark_agency_en = "T??n CTY ?????i l?? (ti???ng Anh)";
    var global_fm_remark_agency_vn = "T??n CTY ?????i l?? (ti???ng Vi???t)";
    var global_fm_identifier_type = "Lo???i ?????nh danh";
    if(IsWhichCA !== "18"){
        global_fm_identifier_type = "UID doanh nghi???p";
    }
    var global_fm_document_type = "Lo???i gi???y t???";
    if(IsWhichCA !== "18") {
        global_fm_document_type = "UID c?? nh??n";
    }
    var global_fm_enter = "Nh???p ";
    var global_fm_enter_number = "Nh???p s??? ";
    var request_conform_approve = "B???n mu???n ph?? duy???t y??u c???u n??y ?";
    var certlist_title_detail_current = "Chi ti???t ch???ng th?? s??? hi???n th???i";
    var global_fm_register_date = "Ch???n ng??y ????ng k??";
    var global_fm_register_info = "Nh???n ch???ng th?? s??? theo th??ng tin d?????i ????y";
    
    //request -> historylist
    var history_title_list = "Li??ch s???? truy c????p";
    var history_title_search = "Ti??m ki????m";
    var history_title_table = "Danh sa??ch";
    var history_title_detail = "Chi ti????t";
    var history_fm_response = "Tra??ng tha??i";
    var history_fm_function = "Ha??m";
    var history_fm_request_data = "D???? li????u y??u c????u";
    var history_fm_response_data = "D???? li????u tra?? v????";
    var history_fm_request_ip = "IP";
    var history_fm_source_entity = "H???? th????ng th????c hi????n";
    
    var reportneac_title_list = "Ba??o ca??o NEAC";
    var reportneac_title_search = "Ti??m ki????m";
    var reportneac_title_table = "Danh sa??ch";
    var reportneac_fm_tab_control = "Ba??o ca??o ??????i soa??t";
    var reportneac_fm_tab_recurring = "Ba??o ca??o ??i??nh ky??";
    var reportneac_fm_tab_cts_signserver = "Ch???ng th?? s??? SignServer";
    var reportneac_fm_tab_cts_token = "Danh s??ch ch???ng th?? s???";
    var global_fm_cert_count = "S??? l??????ng ch????ng th?? s????";
    var reportneac_fm_cert_personal = "Ch????ng th?? s???? ca?? nh??n";
    var reportneac_fm_cert_enterprise = "Ch????ng th?? s???? t???? ch????c";
    var reportneac_fm_cert_staff = "Ch????ng th?? s???? ca?? nh??n trong t???? ch????c";
    var reportneac_fm_control_content = "S??? l?????ng ch???ng th?? s??? do CA c??ng c???ng c???p cho thu?? bao l?? t??? ch???c, doanh nghi???p (kh??ng bao g???m c?? nh??n) t??? ng??y 01/01/2017 v?? c??n hi???u l???c s??? d???ng ?????n ng??y ";
    var global_fm_report_date = "........., ng??y.....th??ng.....n??m ......";
    var global_fm_report_print_date = "........., ng??y [DD] th??ng [MM] n??m [YYYY]";
    var global_fm_report_print_only_date = "ng??y [DD] th??ng [MM] n??m [YYYY]";
    if(IsWhichCA === "14" || IsWhichCA === "7") {
        global_fm_report_print_date = "Ng??y [DD] th??ng [MM] n??m [YYYY]";
    }
    var global_fm_choose_cert = "Ch???n ch???ng th?? s???";
    var global_fm_unchoose_cert = "B??? ch???n ch???ng th?? s???";
    var global_fm_login_ssl = "C?? ch??? ????ng nh???p qua thi???t b??? Token";
    var global_ssl_conform_delete = "B???n mu???n b??? ch???n ch???ng th?? s??? ?";
    var global_confirm_print_register = "B???n c?? mu???n in gi???y ????ng k?? ?";
    var global_confirm_print_renew = "B???n c?? mu???n in gi???y gia h???n ?";
    // send mail hsm
    var hsm_confirm_cert_actived = "Ch???ng th?? s??? ???? ???????c k??ch ho???t";
    var hsm_confirm_data_not_found = "Kh??ng t??m th???y th??ng tin k??ch ho???t ch???ng th?? s???. Vui l??ng li??n h??? nh?? cung c???p d???ch v???";
    var hsm_confirm_url_invalid = "???????ng d???n k??ch ho???t kh??ng h???p l???";
    var hsm_confirm_time_expire = "Th???i gian k??ch ho???t y??u c???u ???? h???t. Vui l??ng li??n h??? nh?? cung c???p d???ch v???";
    var hsm_confirm_encryption_notfound = "Kh??ng t??m th???y chu???i m?? h??a";
    var hsm_confirm_acteve_status_invalid = "Tr???ng th??i y??u c???u k??ch ho???t kh??ng h???p l???, vui l??ng li??n h??? nh?? cung c???p d???ch v???";
    var hsm_confirm_cert_issue_error = "L???i ph??t h??nh ch???ng th?? s???, vui l??ng li??n h??? nh?? cung c???p d???ch v???";
    var hsm_confirm_request_declined = "Y??u c???u ???? ???????c t??? ch???i k??ch ho???t ch???ng th?? s???.<br />Th???i gian: [TIME]. L?? do t??? ch???i: [REASON]";
    var hsm_confirm_request_type_invalid = "Lo???i y??u c???u kh??ng h???p l???";
    var hsm_confirm_actived_success = "Ch???ng th?? s??? k??ch ho???t th??nh c??ng.\nVui l??ng ki???m tra h???p th?? E-Mail ????? nh???n ch???ng th?? s???";
    var hsm_confirm_declined_success = "H???y k??ch ho???t ch???ng th?? s??? th??nh c??ng";
    var hsm_confirm_title_page = "Qu?? kh??ch c?? y??u c???u k??ch ho???t c???p ch???ng th?? s??? HSM, vui l??ng ki???m tra th??ng tin v?? x??c nh???n ????? h??? th???ng c???p ch???ng th?? s???:";
    var hsm_confirm_note_page = "Ch?? ??: ?????ng ?? (?????ng ?? k??ch ho???t ch???ng th?? s???); T??? ch???i (kh??ng ?????ng ?? k??ch ho???t ch???ng th?? s???)";
    var hsm_confirm_not_confirm = "Ch??a x??c nh???n";
    var hsm_confirm_has_confirm = "???? x??c nh???n";
    
    // footer page
    var footer_name = "";
    var footer_name_inner = "";
    var footer_address = "";
    var footer_office = "";
    var footer_email = "";
    var header_hotline = "";
    var footer_hotline = "";
    
    var footer_name_minvoice = "";
    var footer_name_inner_minvoice = "";
    var footer_address_minvoice = "";
    var footer_email_minvoice = "";
    var header_hotline_minvoice = "";
    var footer_hotline_minvoice = "";
    if(IsWhichCA === "1") {
        footer_name = "2018 - {DATE_YEAR} ?? C??ng ty c??? ph???n c??ng ngh??? tin h???c EFY Vi???t Nam";
        footer_name_inner = "2018 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: T???ng 9 t??a nh?? Sannam, s??? 78 Duy T??n, ph?????ng D???ch V???ng H???u, C???u Gi???y, H?? N???i";
        footer_email = "efy@ihd.vn";
        header_hotline = "1900 6142 - 1900 6139";
        footer_hotline = "1900 6142 - 1900 6139";
    } else if(IsWhichCA === "2") {
        footer_name = "2018 - {DATE_YEAR} ?? FEITIAN Technologies Co.,Ltd.";
        footer_name_inner = "2018 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: Tower B, Huizhi Mansion, No.9 Xueqing Road, Haidian District, 100085 Beijing, China";
//        footer_email = "email@ihd.vn";
        header_hotline = "+86 10 6230 4466";
        footer_hotline = "+86 10 6230 4466";
    } else if(IsWhichCA === "3") {
        footer_name = "Copyright ?? 2019 - {DATE_YEAR} Mobile-ID Technologies and Services Joint Stock Company";
        footer_name_inner = "2019 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: 19 ?????ng Ti???n ????ng, Ph?????ng An Ph??, Qu???n 2, TP. H??? Ch?? Minh, Vi???t Nam";
        footer_email = "info@mobile-id.vn";
        header_hotline = "1900 6884";
        footer_hotline = "1900 6884";
    } else if(IsWhichCA === "4") {
        footer_name = "Copyright ?? 2019 - {DATE_YEAR} MISA JSC";
        footer_name_inner = "2019 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: T????ng 7 to??a nha?? C??ng ??oa??n Ng??n ha??ng Vi????t Nam, S???? 6 ngo?? 82, ph???? Di??ch Vo??ng H????u, Q. C????u Gi????y, Ha?? N????i";
        footer_email = "fsales@misa.com.vn";
        header_hotline = "1900 8677";
        footer_hotline = "1900 8677";
    } else if(IsWhichCA === "5") {
        footer_name = "Copyright ?? 2019 - {DATE_YEAR}. All Rights Reserved";
        footer_name_inner = "2019 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: T9, T??a nh?? Vi???t ??, s??? 9 ph??? Duy T??n, P. D???ch V???ng H???u, Q. C???u Gi???y, TP. H?? N???i";
        footer_email = "info@savis.com.vn";
        header_hotline = "+(84-24) 3782 2345";
        footer_hotline = "+(84-24) 3782 2345";
    } else if(IsWhichCA === "6") {
        footer_name = "Copyright ?? 2019 - {DATE_YEAR} NewTel-CA. All Rights Reserved";
        footer_name_inner = "2019 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: Ph??ng 305, To?? Nh?? GP Invest, S??? 170 ???? La Th??nh, Q.?????ng ??a, H?? N???i";
        footer_email = "info@newca.vn";
        header_hotline = "+(84-24) 38374999";
        footer_hotline = "+(84-24) 38374999";
    } else if(IsWhichCA === "7") {
        footer_name = "Copyright ?? 2019 - {DATE_YEAR} NC-CA. All Rights Reserved";
        footer_name_inner = "2019 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: T???ng 8 t??a nh?? Newhouse Xala, Khu ???? th??? Xala, H?? ????ng, H?? N???i";
        footer_email = "info@nc-ca.com.vn";
        header_hotline = "+(84-24) 6297 1010";
        footer_hotline = "+(84-24) 6297 1010";
    } else if(IsWhichCA === "8") {
        footer_name = "Copyright ?? 2020 - {DATE_YEAR} C??ng ty TNHH T???ng c??ng ty C??ng ngh??? v?? Gi???i ph??p CMC";
        footer_name_inner = "2020 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: T???ng 14-16, T??a nh?? CMC, s??? 11 ph??? Duy T??n, qu???n C???u Gi???y, H?? N???i";
        footer_email = "ca-support@cmc.vn";
        header_hotline = "1900 2323 62";
        footer_hotline = "H??? tr??? k??? thu???t: 024 3972 2425";
    } else if(IsWhichCA === "9") {
        footer_name = "Copyright ?? 2020 - {DATE_YEAR} VG-CA";
        footer_name_inner = "2020 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: 23 Ng???y Nh?? Kon Tum, Qu???n Thanh Xu??n, Thanh ph??? H?? N???i";
        footer_email = "ca@bcy.gov.vn";
        header_hotline = "(+84.24) 37738668";
        footer_hotline = "(+84.24) 37738668";
    } else if(IsWhichCA === "10") {
        footer_name = "Copyright ?? 2020 - {DATE_YEAR} FPT-CA.COM.VN";
        footer_name_inner = "2020 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: L???u 6, T??a nh?? S??i G??n Prime, 107-109-111 Nguy???n ????nh Chi???u, P6, Q3, TPHCM";
        footer_email = "kinhdoanh@fpt-ca.com.vn";
        header_hotline = "0911666467";
        footer_hotline = "0911666467";
    } else if(IsWhichCA === "11") {
        footer_name = "Copyright ?? 2020 - {DATE_YEAR} SoftDreams";
        footer_name_inner = "2020 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: Nh?? kh??ch ATS, s??? 8 Ph???m H??ng, Ph?????ng M??? Tr??, Qu???n Nam T??? Li??m, H?? N???i";
        footer_email = "contact@softdreams.vn";
        header_hotline = "1900 56 56 53";
        footer_hotline = "1900 56 56 53";
    } else if(IsWhichCA === "12") {
        footer_name = "Copyright ?? 2020 - {DATE_YEAR} C??ng ty TNHH LCS-CA";
        footer_name_inner = "2020 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: 210/16A C??ch M???ng Th??ng 8, P.10, Q.3, TP H??? Ch?? Minh";
        footer_email = "hotro@lcs-ca.vn";
        header_hotline = "1900 4533";
        footer_hotline = "1900 4533";
    } else if(IsWhichCA === "13") {
        footer_name = "Copyright ?? 2020 - {DATE_YEAR} VIETTELCA.VN";
        footer_name_inner = "2020 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: S??? 1 ???????ng Tr???n H???u D???c, Ph?????ng M??? ????nh 2, Qu???n Nam T??? Li??m, H?? N???i";
        footer_email = "lienhe@viettelca.vn";
        header_hotline = "1800 8000";
        footer_hotline = "1800 8000";
    } else if(IsWhichCA === "14") {
        footer_name = "Copyright ?? 2020 - {DATE_YEAR} C??NG TY TNHH T?? V???N - TH????NG M???I KH??NH LINH";
        footer_name_inner = "2020 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: 232/17 C???ng H??a, Ph?????ng 12, Qu???n T??n B??nh, Th??nh ph??? H??? Ch?? Minh";
        footer_email = "info@ketoanvn.com.vn";
        header_hotline = "1900 1129";
        footer_hotline = "1900 1129";
    } else if(IsWhichCA === "15") {
        footer_name = "Copyright ?? 2018 - {DATE_YEAR} Lao National Root Certificate Authority";
        footer_name_inner = "2018 - {DATE_YEAR} ?? ";
        footer_address = "Address: Saylom village, Chanthabouli district, Vientiane Capital, Lao PDR";
        footer_email = "lanic_office@lanic.la";
        header_hotline = "+856 254150";
        footer_hotline = "+856 254150, PO Box: 2225";
    } else if(IsWhichCA === "16") {
        footer_name = "Copyright ?? 2013 - {DATE_YEAR} SAFECert Corp";
        footer_name_inner = "2018 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: X-04.77, T??a nh?? North Towers, Sunrise City, 27 Nguy???n H???u Th???, Ph?????ng T??n H??ng, Qu???n 7, TP. H??? Ch?? Minh";
        footer_email = "info@safecert.com.vn";
        header_hotline = "(028)-668-23732";
        footer_hotline = "(028)-668-23732";
        global_fm_decision = "M?? ????n v???";
    } else if(IsWhichCA === "17") {
        footer_name = "Copyright ?? 2018 - {DATE_YEAR} Lao National Root Certificate Authority";
        footer_name_inner = "2018 - {DATE_YEAR} ?? ";
        footer_address = "Address: Saylom village, Chanthabouli district, Vientiane Capital, Lao PDR";
        footer_email = "lanic_office@lanic.la";
        header_hotline = "+856 254150";
        footer_hotline = "+856 254150, PO Box: 2225";
    } else if(IsWhichCA === "18") {
        footer_name = "C??NG TY C??? PH???N ICORP";
        footer_name_minvoice = "C??NG TY TNHH H??A ????N ??I???N T??? M-INVOICE";
        footer_name_inner = "2021 - {DATE_YEAR} ?? ";
//        footer_office = "VPGD: T???ng 6, S??? 82, Ph??? Tr???n Th??i T??ng, Ph?????ng D???ch V???ng H???u, Qu???n C???u Gi???y, TP H?? N???i";
        footer_office = "VPGD: Ph??ng 1212 Th??p A, To?? nh?? The Park Home, S??? 1 Ph??? Th??nh Th??i, Ph?????ng D???ch V???ng, Qu???n C???u Gi???y, TP H?? N???i";
        footer_address = "?????a ch???: S??? 32/21 Ph??? Tr????ng C??ng Giai, Ph?????ng D???ch V???ng, Qu???n C???u Gi???y, TP. H?? N???i";
        footer_address_minvoice = "?????a ch???: Qu???n C???u Gi???y, TP H?? N???i";
        footer_email = "ica@icorp.vn";
        footer_email_minvoice = "cskh@minvoice.vn";
        header_hotline = "1900 0099";
        header_hotline_minvoice = "0908111111";
        footer_hotline = "1900 0099";
        footer_hotline_minvoice = "0908111111";
    } else if(IsWhichCA === "19") {
        footer_name = "Copyright ?? 2019 - {DATE_YEAR} Mobile-ID Technologies and Services Joint Stock Company";
        footer_name_inner = "2019 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: 19 ?????ng Ti???n ????ng, Ph?????ng An Ph??, Qu???n 2, TP. H??? Ch?? Minh, Vi???t Nam";
        footer_email = "info@mobile-id.vn";
        header_hotline = "1900 6884";
        footer_hotline = "1900 6884";
    } else if(IsWhichCA === "20") {
        footer_name = "Copyright ?? 2019 - {DATE_YEAR} C??NG TY CP D???CH V??? T-VAN HILO";
        footer_name_inner = "2019 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: S??? 2/95 Ch??a B???c, Q.?????ng ??a, TP. H?? N???i, Vi???t Nam";
        footer_email = "support@hilo.com.vn";
        header_hotline = "1900 2929 62";
        footer_hotline = "1900 2929 62";
    } else if(IsWhichCA === "21") {
        footer_name = "Copyright ?? 2019 - {DATE_YEAR} Trung t??m CNTT MobiFone";
        footer_name_inner = "2019 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: S??? 5, ng?? 82 Duy T??n, Qu???n C???u Gi???y, TP H?? N???i";
        footer_email = "contact-itc@mobifone.vn";
        header_hotline = "0936 110 116";
        footer_hotline = "0936 110 116";
    } else if(IsWhichCA === "22") {
        footer_name = "Copyright ?? 2022 - {DATE_YEAR} Mat Bao Company. All Reserved";
        footer_name_inner = "2022 - {DATE_YEAR} ?? ";
        footer_address = "?????a ch???: T???ng 3 Anna Building, C??ng Vi??n Ph???n M???m Quang Trung, Q.12, TP.H??? Ch?? Minh";
        footer_email = "info@matbao.com";
        header_hotline = "1900 1830";
        footer_hotline = "1900 1830";
    } else {}
}