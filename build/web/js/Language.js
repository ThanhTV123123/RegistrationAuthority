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
    var serverparty_succ_add = "Thêm mới Server Entity Successfully";
    var serverparty_exists_code = "Mã Server Entity is already exists";
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
    var global_req_no_special = " Don’t Consist Of Special Character";
    var global_req_no_space = " Don’t Consist Of BLANK";
    var global_fm_button_delete = "Delete";
    var global_fm_paging_total = "Total rows ";
    var policy_check_length_pass = "Minimum Length Must Be Belower Than Maximum Length Of Password";
    var policy_check_number_zero = " " + "Must Be Greater 0";
    var global_fm_button_reset_pass = "Reset Password";
    var global_fm_button_check_pass_default = "Check Default Password";
    var global_fm_character = " " + "alphabet";
    var global_fm_phone_zero = " " + "Must Be Began With 0";
    var global_fm_phone_8_11 = "MobilePhone Length Is From 8 To 11 numbers";
    var pass_req_no_space = "Password Don’t Consist Of BLANK";
    var user_req_no_space = "Username Don’t Consist Of BLANK";
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
    var tokenimport_fm_createdate_tokensn_search = "Create Time và Token SN";
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
    var tempdn_title_table = "Danh sách trường";
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
        footer_name = "2018 - {DATE_YEAR} © Vietnam EFY Informatics Technology JSC";
        footer_name_inner = "2018 - {DATE_YEAR} © ";
        footer_address = "Address: Tầng 9 tòa nhà Sannam, số 78 Duy Tân, phường Dịch Vọng Hậu, Cầu Giấy, Hà Nội";
        footer_email = "efy@ihd.vn";
        header_hotline = "1900 6142 - 1900 6139";
        footer_hotline = "1900 6142 - 1900 6139";
    } else if(IsWhichCA === "2") {
        footer_name = "2018 - {DATE_YEAR} © FEITIAN Technologies Co.,Ltd.";
        footer_name_inner = "2018 - {DATE_YEAR} © ";
        footer_address = "Address: Tower B, Huizhi Mansion, No.9 Xueqing Road, Haidian District, 100085 Beijing, China";
        // footer_email = "email@ihd.vn";
        header_hotline = "+86 10 6230 4466";
        footer_hotline = "+86 10 6230 4466";
    } else if(IsWhichCA === "3") {
        footer_name = "Copyright © 2019 - {DATE_YEAR} Mobile-ID Technologies and Services Joint Stock Company";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Address: 19 Dang Tien Dong, An Phu Ward, District 2nd, Ho Chi Minh City, Vietnam";
        footer_email = "info@mobile-id.vn";
        header_hotline = "1900 6884";
        footer_hotline = "1900 6884";
    } else if(IsWhichCA === "4") {
        footer_name = "Copyright © 2019 - {DATE_YEAR} MISA JSC";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Address: MISA Building, Quang Trung software city, 49 To Ky, Tan Chanh Hiep Ward, 12th District, HCM city";
        footer_email = "esales@han.misa.com.vn";
        header_hotline = "1900 8677";
        footer_hotline = "1900 8677";
    } else if(IsWhichCA === "5") {
        footer_name = "Copyright © 2019 - {DATE_YEAR}. All Rights Reserved";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Address: Floor 9, Viet A Building, No. 9, Duy Tan Street, Dich Vong Hau Ward, Cau Giay District, Hanoi";
        footer_email = "info@savis.com.vn";
        header_hotline = "+(84-24) 3782 2345";
        footer_hotline = "+(84-24) 3782 2345";
    } else if(IsWhichCA === "6") {
        footer_name = "Copyright © 2019 - {DATE_YEAR} NewTel-CA. All Rights Reserved";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Address: Room 305, GP Invest Building, No. 170 De La Thanh, Dong Da District, Hanoi";
        footer_email = "info@newca.vn";
        header_hotline = "+(84-24) 38374999";
        footer_hotline = "+(84-24) 38374999";
    } else if(IsWhichCA === "7") {
        footer_name = "Copyright © 2019 - {DATE_YEAR} NC-CA. All Rights Reserved";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Address: 8th Floor, Newhouse Xala Building, Xala Urban Area, Ha Dong, Hanoi";
        footer_email = "info@nc-ca.com.vn";
        header_hotline = "+(84-24) 6297 1010";
        footer_hotline = "+(84-24) 6297 1010";
    } else if(IsWhichCA === "8") {
        footer_name = "Copyright © 2020 - {DATE_YEAR} CMC TECHNOLOGY AND SOLUTION COMPANY LIMITED";
        footer_name_inner = "2020 - {DATE_YEAR} © ";
        footer_address = "Address: floors 14-16, CMC Building, 11 Duy Tan Street, Cau Giay District, Hanoi";
        footer_email = "ca-support@cmc.vn";
        header_hotline = "1900 2323 62";
        footer_hotline = "Technical support: 024 3972 2425";
    } else if(IsWhichCA === "9") {
        footer_name = "Copyright © 2020 - {DATE_YEAR} VG-CA";
        footer_name_inner = "2020 - {DATE_YEAR} © ";
        footer_address = "Address: 23 Nguy Nhu Kon Tum, Thanh Xuan District, Hanoi City";
        footer_email = "ca@bcy.gov.vn";
        header_hotline = "(+84.24) 37738668";
        footer_hotline = "(+84.24) 37738668";
    } else if(IsWhichCA === "10") {
        footer_name = "Copyright © 2020 - {DATE_YEAR} FPT-CA.COM.VN";
        footer_name_inner = "2020 - {DATE_YEAR} © ";
        footer_address = "Address: 6th Floor, Saigon Prime Building, 107-109-111 Nguyen Dinh Chieu, P6, Q3, HCMC";
        footer_email = "kinhdoanh@fpt-ca.com.vn";
        header_hotline = "0911666467";
        footer_hotline = "0911666467";
    } else if(IsWhichCA === "11") {
        footer_name = "Copyright © 2020 - {DATE_YEAR} SoftDreams";
        footer_name_inner = "2020 - {DATE_YEAR} © ";
        footer_address = "Address: ATS guest house, 8 Pham Hung, Me Tri Ward, Nam Tu Liem District, Hanoi";
        footer_email = "contact@softdreams.vn";
        header_hotline = "1900 56 56 53";
        footer_hotline = "1900 56 56 53";
    } else if(IsWhichCA === "12") {
        footer_name = "Copyright © 2020 - {DATE_YEAR} LCS-CA Co., Ltd";
        footer_name_inner = "2020 - {DATE_YEAR} © ";
        footer_address = "Address: 210/16A Cach Mang Thang 8, Ward 10, District 3, Ho Chi Minh City";
        footer_email = "hotro@lcs-ca.vn";
        header_hotline = "1900 4533";
        footer_hotline = "1900 4533";
    } else if(IsWhichCA === "13") {
        footer_name = "Copyright © 2020 - {DATE_YEAR} VIETTELCA.VN";
        footer_name_inner = "2020 - {DATE_YEAR} © ";
        footer_address = "Address: No. 1, Tran Huu Duc Street, My Dinh 2 Ward, Nam Tu Liem District, Hanoi City";
        footer_email = "lienhe@viettelca.vn";
        header_hotline = "1800 8000";
        footer_hotline = "1800 8000";
    } else if(IsWhichCA === "14") {
        footer_name = "Copyright © 2020 - {DATE_YEAR} KHANH LINH CONSULTANT - SERVICE COMPANY LIMITED";
        footer_name_inner = "2020 - {DATE_YEAR} © ";
        footer_address = "Address: 232/17 Cong Hoa, Ward 12, Tan Binh District, Ho Chi Minh City";
        footer_email = "info@ketoanvn.com.vn";
        header_hotline = "1900 1129";
        footer_hotline = "1900 1129";
    } else if(IsWhichCA === "15") {
        footer_name = "Copyright © 2018 - {DATE_YEAR} Lao National Root Certificate Authority";
        footer_name_inner = "2018 - {DATE_YEAR} © ";
        footer_address = "Address: Saylom village, Chanthabouli district, Vientiane Capital, Lao PDR";
        footer_email = "lanic_office@lanic.la";
        header_hotline = "+856 254150";
        footer_hotline = "+856 254150, PO Box: 2225";
    } else if(IsWhichCA === "16") {
        footer_name = "Copyright © 2013 - {DATE_YEAR} SAFECert Corp";
        footer_name_inner = "2018 - {DATE_YEAR} © ";
        footer_address = "Address: X-04.77, North Towers Building, Sunrise City, 27 Nguyen Huu Tho, Tan Hung Ward, District 7, City. Ho Chi Minh City";
        footer_email = "info@safecert.com.vn";
        header_hotline = "(028)-668-23732";
        footer_hotline = "(028)-668-23732";
        global_fm_decision = "Unit Code";
    } else if(IsWhichCA === "17") {
        footer_name = "Copyright © 2018 - {DATE_YEAR} Lao National Root Certificate Authority";
        footer_name_inner = "2018 - {DATE_YEAR} © ";
        footer_address = "Address: Saylom village, Chanthabouli district, Vientiane Capital, Lao PDR";
        footer_email = "lanic_office@lanic.la";
        header_hotline = "+856 254150";
        footer_hotline = "+856 254150, PO Box: 2225";
    } else if(IsWhichCA === "18") {
        footer_name = "ICORP JOINT STOCK COMPANY";
        footer_name_minvoice = "M-INVOICE ELECTRONIC INVOICE CO., LTD";
        footer_name_inner = "2021 - {DATE_YEAR} © ";
        footer_name_inner_minvoice = "2021 - {DATE_YEAR} © ";
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
        footer_name = "Copyright © 2019 - {DATE_YEAR} Mobile-ID Technologies and Services Joint Stock Company";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Address: 19 Dang Tien Dong, An Phu Ward, District 2nd, Ho Chi Minh City, Vietnam";
        footer_email = "info@mobile-id.vn";
        header_hotline = "1900 6884";
        footer_hotline = "1900 6884";
    } else if(IsWhichCA === "20") {
        footer_name = "Copyright © 2019 - {DATE_YEAR} T-VAN HILO SERVICE JOINT STOCK COMPANY";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Address: No. 2/95 Chua Boc, Dong Da District, Hanoi City, Vietnam";
        footer_email = "support@hilo.com.vn";
        header_hotline = "1900 2929 62";
        footer_hotline = "1900 2929 62";
    } else if(IsWhichCA === "21") {
        footer_name = "Copyright © 2019 - {DATE_YEAR} MobiFone IT Center";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Address: No. 5, Lane 82 Duy Tan, Cau Giay District, Hanoi City, Vietnam";
        footer_email = "contact-itc@mobifone.vn";
        header_hotline = "0936 110 116";
        footer_hotline = "0936 110 116";
    } else if(IsWhichCA === "22") {
        footer_name = "Copyright © 2022 - {DATE_YEAR} Mat Bao Company. All Reserved";
        footer_name_inner = "2022 - {DATE_YEAR} © ";
        footer_address = "Address: 3rd Floor Anna Building, Quang Trung Software Park, District 12, Ho Chi Minh City";
        footer_email = "info@matbao.com";
        header_hotline = "1900 1830";
        footer_hotline = "1900 1830";
    } else {}
}
else
{
    var global_title_logo = "Banner quản trị hệ thống";
    var global_fm_button_New = " Thêm mới ";
    var global_fm_button_add = "Lưu";
    var global_fm_button_add_print = "Lưu và In";
    var global_fm_button_edit = "Cập nhật";
    var global_fm_button_restart = "Khởi động lại hệ thống";
    var global_fm_button_back = "Quay lại";
    var global_fm_button_close = "Đóng";
    var global_fm_button_search = "Tìm kiếm";
    var global_fm_button_profile_pettlement = "Kết toán hồ sơ";
    var global_fm_button_get_info = "Lấy thông tin";
    var global_fm_register_note = "Lưu ý: Quý khách vui lòng kiểm tra lại thông tin trước khi đăng ký chứng thư số";
    var global_fm_button_reload = "Tải lại";
    var global_fm_button_reload_profile = "Tải danh sách gói mặc định";
    var global_fm_button_reload_of_profileaccess = "Tải danh sách gói mới nhất theo nhóm quyền gói dịch vụ";
    var global_fm_button_export = "Xuất Excel";
    var global_fm_button_export_csv = "Xuất File CSV";
    var global_fm_button_export_word = "Xuất File Word";
    var global_fm_button_profile = "Gói dịch vụ";
    var global_fm_button_configAPI = "Cấu hình API";
    var global_fm_button_API = "Truy cập API";
    var global_fm_action = "Thao tác";
    var global_fm_STT = "STT";
    var global_fm_Username = "Tên đăng nhập";
    var global_fm_Username_esigncloud = "Tên đăng nhập Remote Signing";
    var global_fm_Username_esigncloud_exists = "Tên đăng nhập Remote Signing đã tồn tại";
    var global_fm_Password = "Mật khẩu";
    var global_fm_date = "Ngày tạo/cập nhật";
    var global_fm_date_create = "Ngày tạo";
    var global_fm_date_revoke = "Ngày thu hồi";
    var global_fm_date_gencert = "Ngày gen";
    var global_fm_date_cancel = "Ngày hủy";
    var global_fm_date_gen = "Ngày gen";
    var global_fm_num_date_cancel = "Số ngày hủy";
    var global_fm_date_request = "Ngày yêu cầu";
    var global_fm_date_endupdate = "Ngày cập nhật cuối";
    var global_fm_scan_valid = "Bản scan hợp lệ";

    var global_fm_user_request = "Nhân viên yêu cầu";
    var global_fm_user_create = "Nhân viên tạo";
    if(IsWhichCA === "18") {
        global_fm_user_create = "Người tạo";
    }
    var global_fm_user_receive = "Nhân viên nhận";
    var global_fm_user_endupdate = "Nhân viên cập nhật cuối";

    var global_fm_timestamp = "Thời gian";
    var global_fm_times_recovery = "Thời gian phục hồi";
    var global_fm_duration = "Thời hạn (Ngày)";
    var global_fm_active = "Hiệu lực";
    var global_fm_all_apply_user = "Áp dụng danh sách quyền cho tất cả nhân viên trong hệ thống thuộc chức vụ này";
    var global_fm_image = "Hình ảnh (Icon)";
    var global_fm_required_input = "Yêu cầu nhập";
    var global_fm_effective = "Hiệu lực";
    var global_fm_duration_promotion = "Thời hạn + khuyến mãi (Ngày)";
    var global_fm_address = "Địa chỉ";
    var global_fm_fullname = "Họ tên";
    var global_fm_fax = "Số Fax";
    var global_fm_email = "Địa chỉ email";
    var global_fm_email_receive = "Email (Nhận thông báo thuế)";
    var global_fm_email_contact = "Địa chỉ email liên hệ KH";
    var global_fm_email_contact_grid = "Địa chỉ email liên hệ KH";
    if(IsWhichCA === "18") {
        global_fm_email_contact_grid = "Email";
    }
    var global_fm_email_contact_real = "Email liên hệ KH (Thật)";
    var global_fm_email_authen_rssp = "Email xác thực ký số";
    var global_fm_option_owner_new = "Tạo mới chủ sở hữu";
    var global_fm_option_owner_search = "Tìm kiếm chủ sở hữu trên hệ thống";
    var global_fm_email_contact_signserver = "Địa chỉ email KH bàn giao CTS Server";
    var global_fm_ip = "Địa chỉ IP";
    var global_fm_port = "Port";
    var global_fm_ward = "Tên phường";
    var global_fm_street = "Tên đường";
    var global_fm_city = "Tỉnh/Thành phố";
    var global_fm_area = "Khu vực";
    var global_fm_Description = "Mô tả";
    var global_fm_Certificate = "Chứng thư số";
    var global_fm_cert = "Chứng thư số";
    var global_fm_FromDate = "Từ ngày";
    var global_fm_year = "Năm";
    var global_fm_mounth = "Tháng";
    var global_fm_Quater = "Quý";
    var global_fm_Branch = "Đại lý";
    var global_fm_ToDate = "Đến ngày";
    var global_fm_StatusAccount = "Trạng thái tài khoản";
    var global_fm_combox_all = "Tất cả";
    var global_fm_combox_empty = "[Để trống]";
    var global_fm_combox_choose = "-- Vui lòng chọn --";
    var global_fm_datatype_label = "Kiểu dữ liệu nhập";
    var global_fm_datatype_numeric = "Ký tự số";
    var global_fm_datatype_varchar = "Ký tự chữ";
    var global_fm_datatype_boolean = "Chọn/Không chọn";
    var global_fm_combox_no_choise = "--- Danh mục gốc ---";
    var global_succ_NoResult = "Không tìm thấy danh sách";
    var global_fm_role = "Chức vụ";
    var global_fm_phone = "Điện thoại";
    var global_fm_phone_manager = "Điện thoại người đại diện";
    var global_fm_email_manager = "Email người đại diện";
    var global_fm_name_manager = "Tên người đại diện";
    var global_fm_name_contact = "Tên người liên hệ";
    var global_fm_phone_contact = "Điện thoại liên hệ KH";
    var global_fm_phone_contact_grid = "Điện thoại liên hệ KH";
    if(IsWhichCA === "18") {
        global_fm_phone_contact_grid = "SĐT";
    }
    var global_fm_phone_contact_real = "Điện thoại liên hệ KH (Thật)";
    var global_fm_phone_authen_rssp = "Điện thoại xác thực ký số";
    var global_fm_vendor = "Nhà cung cấp SIM";
    var global_fm_display_mess = "Nội dung thông báo";
    var global_fm_fileid = "File ID";
    var global_error_file_special = "Tên tệp tin không cho phép chứa ký tự đặc biệt. Bao gồm: /\{};:,\"`~&*|+=%$@<>[]#'^!?";
    var global_title_hsm_confirm = "Xác nhận kích hoạt chứng thư số HSM";
    var global_fm_Function = "Hàm";
    var global_fm_MetaData = "MetaData";
    var global_fm_billcode = "Bill Code";
    var global_succ_NoCheck = "Vui lòng chọn danh sách cần thực hiện";
    var global_succ_NoCheck_setup = "Vui lòng chọn danh sách cần cài đặt chứng thư số";

    var global_paging_Before = "Trước";
    var global_paging_last = "Sau";
    var global_paging_first = "Đầu tiên";
    var global_paging_next = "Cuối";

    var global_req_Username = "Vui lòng nhập Tên đăng nhập";
    var global_req_Password = "Vui lòng nhập Mật khẩu";
    var global_req_Description = "Vui lòng nhập Mô tả";
    var global_req_Pem = "Vui lòng nhập Pem";
    var global_req_Certificate = "Vui lòng nhập Chứng thư số";
    var global_req_address = "Vui lòng nhập Địa chỉ";
    var global_req_ward = "Vui lòng nhập Tên phường";
    var global_req_street = "Vui lòng nhập tên đường";
    var global_req_mail = "Vui lòng nhập Địa chỉ Email";
    var global_req_mail_format = "Vui lòng nhập đúng định dạng Email";
    var global_req_ip_format = "Vui lòng nhập đúng định dạng địa chỉ IP";
    var global_req_cer_format = "Vui lòng nhập đúng định dạng file .cer, .txt, .pem";
    var global_req_csr_format = "Vui lòng nhập đúng định dạng file .csr, .txt";
    var global_req_crl_format = "Vui lòng nhập đúng định dạng file .crl";
    var global_req_image_format = "Vui lòng nhập đúng định dạng file hình ảnh .jpg, .png";
    var global_fm_active_true = "CÓ";
    var global_fm_active_false = "KHÔNG";
    var global_fm_remark_en = "Mô tả (Tiếng Anh)";
    var global_fm_amount_fee = "Số tiền phí (VNĐ)";
    var global_fm_amount = "Số tiền (VNĐ)";
    var global_fm_activation_code = "Mã kích hoạt";
    var global_fm_activation_date = "Thời hạn kích hoạt";
    var global_fm_amount_token = "Giá tiền Token (VNĐ)";
    
    // ### FORM_FACTOR
    var global_fm_amount_renewal = "Số tiền phí gia hạn";
    var global_fm_amount_changeinfo = "Số tiền phí thay đổi thông tin";
    var global_fm_amount_reissue = "Số tiền phí cấp lại";
    var global_fm_amount_goverment = "Số tiền phí khác";
    
    var global_fm_date_free = "Thời gian khuyến mãi (Ngày)";
    var global_fm_entity_ejbca = "Entity EJBCA";
    var global_fm_choose_csr = "Chọn CSR";
    var global_fm_choose_genkey_server = "Sinh khóa trên server (P12)";
    var global_fm_choose_genkey_client = "Chọn CSR (khóa được sinh từ Client)";
    var global_fm_remark_vn = "Mô tả (Tiếng Việt)";
    var global_fm_en = "Tiếng Anh";
    var global_fm_vn = "Tiếng Việt";
    var global_fm_refresh = "Làm lại";
    var global_fm_properties = "Thuộc tính";
    var global_fm_uuid = "UUID";
    var global_fm_uuid_agreement = "UUID hợp đồng";
    var global_fm_remainingSigning_agreement = "Số lượt ký còn lại";
    var global_fm_appid_uri = "APPID URI";
    var global_fm_signature_v4 = "Chữ ký V.4";
    var global_fm_access_key = "Access Key";
    var global_fm_secret_key = "Secret Key";
    var global_fm_xapi_key = "X API key";
    var global_fm_regions = "Tên khu vực";
    var global_fm_service = "Tên dịch vụ";
    var global_fm_dns_name = "Tên DNS";
    var global_fm_dns_list = "Danh sách DNS";
    var global_fm_confirm_customer = "Thông tin xác nhận của khách hàng";
    var global_fm_confirm = "Trạng thái xác nhận";
    var global_fm_confirm_time = "Thời gian xác nhận";
    var global_fm_confirm_ip = "Địa chỉ IP";
    var global_fm_confirm_content = "Nội dung";
    var global_fm_exists_form = "Token tồn đầu kỳ";
    var global_fm_Deposit_form = "Token đặt cọc trong tháng";
    var global_fm_use_form = "Token sử dụng trong tháng";
    var global_fm_end_form = "Token tồn cuối kỳ";
    var global_fm_form = "Hình thức";
    var global_fm_uri = "URI";
    var global_fm_url_callback = "Gọi lại đường dẫn";
    var global_req_format_http = "Vui lòng nhập đúng định dạng đường dẫn ";


//General -> branch
    var branch_title_list = "Quản lý đại lý";
    var branch_table_list = "Danh sách đại lý";
    var branch_title_add = "Thêm mới đại lý";
    var branch_title_edit = "Chỉnh sửa đại lý";
    var branch_req_name = "Vui lòng nhập tên đại lý";
    var branch_req_code = "Vui lòng nhập mã đại lý";
    var branch_succ_add = "Thêm mới đại lý thành công";
    var branch_warning_add = "Thêm mới đại lý thành công. Tạo người dùng thất bại, tên đăng nhập đã tồn tại";
    var branch_succ_edit = "Cập nhật đại lý thành công";
    var branch_exists_name = "Tên đại lý đã tồn tại";
    var branch_exists_code = "Mã đại lý đã tồn tại";
    var branch_fm_name = "Tên đại lý";
    var branch_fm_code = "Mã đại lý";
    var branch_fm_parent = "Đại lý quản lý";
    var branch_fm_level = "Phân cấp đại lý";
    var branch_req_area_change = "Giá trị Khu vực không thể trống";
    var branch_conform_delete = "Bạn muốn xóa đại lý này ?";
    var branch_succ_delete = "Xóa đại lý thành công";
    var branch_exists_user_delete = "Vui lòng xóa tất cả tài khoản nhân viên của đại lý trước";
    var branch_conform_default = "Bạn muốn đặt mặc định cho Logo ?";
    // new
    var branch_fm_choise_new = "Chọn tạo mới";
    var branch_fm_choise_CN = "Chi nhánh";
    var branch_fm_choise_PGD = "Phòng giao dịch";
    var branch_fm_access_profile = "Truy xuất gói dịch vụ";
    
    //report -> synchneac
    var synchneac_title_list = "Quản lý đồng bộ NEAC";
    var synchneac_table_list = "Danh sách chứng thư số";
    var synchneac_title_edit = "Thông tin chứng thư số";
    var synchneac_succ_edit = "Đồng bộ thành công";
    var synchneac_conform_update_multi = "Bạn muốn cấu hình nhiều thông tin ?";
    var synchneac_conform_decline_multi = "Bạn muốn từ chối nhiều thông tin ?";
    var synchneac_conform_synch_multi = "Bạn muốn đồng bộ nhiều thông tin ?";
    var synchneac_fm_remaining = "Số lần đồng bộ lỗi";
    var synchneac_fm_synch_auto = "Cho phép đồng bộ tự động lên NEAC";
    
//General -> city
    var city_title_list = "Quản lý Tỉnh/thành phố";
    var city_table_list = "Danh sách Tỉnh/thành phố";
    var city_table_search = "Tìm kiếm Tỉnh/thành phố";
    var city_title_add = "Thêm mới Tỉnh/thành phố";
    var city_title_edit = "Chỉnh sửa Tỉnh/thành phố";
    var city_req_name = "Vui lòng nhập tên Tỉnh/Thành phố";
    var city_req_code = "Vui lòng nhập Mã Tỉnh/Thành phố";
    var city_succ_add = "Thêm mới Tỉnh/thành phố thành công";
    var city_exists_code = "Mã Tỉnh/thành phố đã tồn tại";
    var city_exists_name = "Tên Tỉnh/thành phố đã tồn tại";
    var city_succ_edit = "Cập nhật Tỉnh/thành phố thành công";
    var city_fm_code = "Mã Tỉnh/thành phố";
    var city_fm_name = "Tên Tỉnh/thành phố";
    
    //General -> CertificateTypeList
    var certtype_title_list = "Quản lý loại chứng thư số";
    var certtype_table_list = "Danh sách";
    var certtype_title_add = "Thêm mới loại chứng thư số";
    var certtype_title_edit = "Cấu hình loại chứng thư số";
    var certtype_exists_code = "Mã loại đã tồn tại";
    var certtype_fm_code = "Mã loại chứng thư số";
    var certtype_succ_add = "Thêm loại chứng thư số thành công";
    var certtype_succ_edit = "Cấu hình loại chứng thư số thành công";
    var certtype_group_file_profile = "Cấu hình loại tệp đính kèm";
    var certtype_component_attributetype = "Loại thuộc tính";
    var certtype_component_cntype = "Loại tên chung";
    var certtype_component_field_code = "Mã trường";
    var certtype_component_field_code_exists = "Mã trường đã tồn tại";
    var certtype_file_code_exists = "Mã loại tệp đã tồn tại";
    var certtype_file_code = "Mã loại tệp";
    var certtype_fm_file = "Loại tệp";
    var certtype_fm_component_text = "Nhập chữ";
    var certtype_fm_component_uuid_company = "Chọn UID doanh nghiệp";
    var certtype_fm_component_uuid_personal = "Chọn UID cá nhân";
    var certtype_fm_component_uuid_company_require = "Bắt buộc UID doanh nghiệp";
    var certtype_fm_component_uuid_personal_require = "Bắt buộc UID cá nhân";
    
    //General -> Response Code
    var response_title_list = "Quản lý trạng thái giao dịch";
    var response_table_list = "Danh sách trạng thái giao dịch";
    var response_title_add = "Thêm mới trạng thái giao dịch";
    var response_title_edit = "Chỉnh sửa trạng thái giao dịch";
    var response_succ_add = "Thêm mới trạng thái giao dịch thành công";
    var response_exists_code = "Mã trạng thái giao dịch đã tồn tại";
    var response_exists_name = "Tên trạng thái giao dịch đã tồn tại";
    var response_succ_edit = "Cập nhật trạng thái giao dịch thành công";
    var response_fm_code = "Mã trạng thái giao dịch";
    var response_fm_name = "Tên trạng thái giao dịch";
    //General -> MNO
    var mno_title_list = "Quản lý MNO";
    var mno_table_list = "Danh sách MNO";
    var mno_title_add = "Thêm mới MNO";
    var mno_title_edit = "Chỉnh sửa MNO";
    var mno_succ_add = "Thêm mới MNO thành công";
    var mno_exists_code = "Mã MNO đã tồn tại";
    var mno_exists_name = "Tên MNO đã tồn tại";
    var mno_succ_edit = "Cập nhật MNO thành công";
    var mno_fm_code = "Mã MNO";
    //General -> InternalEntity
    var interentity_title_list = "Quản lý thực thể kết nối nội bộ";
    var interentity_table_list = "Danh sách thực thể kết nối nội bộ";
    var interentity_title_add = "Thêm mới thực thể kết nối nội bộ";
    var interentity_title_edit = "Chỉnh sửa thực thể kết nối nội bộ";
    var interentity_succ_add = "Thêm mới thực thể kết nối nội bộ thành công";
    var interentity_exists_code = "Mã thực thể kết nối nội bộ đã tồn tại";
    var interentity_succ_edit = "Cập nhật thực thể kết nối nội bộ thành công";
    var interentity_fm_code = "Mã thực thể";
    //General -> ExternalEntity
    var exterentity_title_list = "Quản lý thực thể kết nối bên ngoài";
    var exterentity_table_list = "Danh sách thực thể kết nối bên ngoài";
    var exterentity_title_add = "Thêm mới thực thể kết nối bên ngoài";
    var exterentity_title_edit = "Chỉnh sửa thực thể kết nối bên ngoài";
    var exterentity_succ_add = "Thêm mới thực thể kết nối bên ngoài thành công";
    var exterentity_exists_code = "Mã thực thể kết nối bên ngoài đã tồn tại";
    var exterentity_succ_edit = "Cập nhật thực thể kết nối bên ngoài thành công";
    var exterentity_fm_code = "Mã thực thể";
    //General -> RelyingParty
    var relyparty_title_list = "Quản lý Relying Party";
    var relyparty_table_list = "Danh sách Relying Party";
    var relyparty_title_add = "Thêm mới Relying Party";
    var relyparty_title_edit = "Chỉnh sửa Relying Party";
    var relyparty_succ_add = "Thêm mới Relying Party thành công";
    var relyparty_exists_code = "Mã Relying Party đã tồn tại";
    var relyparty_succ_edit = "Cập nhật Relying Party thành công";
    var relyparty_fm_code = "Mã Relying Party";
    var relyparty_fm_choise_all = "Cho phép tất cả địa chỉ IP";
    var relyparty_fm_choise_ip = "Nhập danh sách địa chỉ IP";
    var relyparty_fm_choise_all_function = "Cho phép tất cả hàm";
    var relyparty_fm_choise_ip_function = "Chọn danh sách hàm";
    var relyparty_fm_group_ip = "Cấu hình danh sách địa chỉ IP truy cập";
    var relyparty_fm_group_function = "Cấu hình danh sách Hàm truy cập";
    var relyparty_fm_group_metadata = "Cấu hình danh sách MetaData";
    var relyparty_fm_group_facet = "Cấu hình thông tin khách hàng";
    var relyparty_exists_add_ip = "Địa chỉ IP đã tồn tại";
    var global_error_delete_ip = "Xóa thất bại, vui lòng kiểm tra lại";
    var relyparty_all_add_ip = "Đã phân quyền truy cập tất cả IP, thêm IP thất bại";
    var relyparty_error_delete_function = "Xóa hàm thất bại, vui lòng kiểm tra lại";
    var global_exists_add_function = "Mã hàm đã tồn tại";
    var global_exists_add_metadata = "AAID đã tồn tại";
    var global_exists_add_facet = "Mã thông tin khách hàng đã tồn tại";
    var relyparty_all_add_function = "Đã phân quyền truy cập tất cả hàm, thêm hàm thất bại";
    var relyparty_req_add_function = "Vui lòng nhập danh sách Hàm";
    var global_req_add_ip = "Vui lòng nhập danh sách IP";
    var global_conform_delete_function = "Bạn chắc chắn muốn xóa hàm này ?";
    var global_conform_delete_ip = "Bạn chắc chắn muốn xóa địa chỉ IP này ?";
    var global_conform_delete_metadata = "Bạn chắc chắn muốn xóa MetaData này ?";
    var global_succ_enabled_function = "Cập nhật hiệu lực hàm thành công";
    var global_succ_enabled_ip = "Cập nhật hiệu lực địa chỉ IP thành công";
    var global_succ_enabled_metadata = "Cập nhật hiệu lực MetaData thành công";
    var global_succ_enabled_facet = "Cập nhật hiệu lực thông tin khách hàng thành công";
    var global_conform_delete_soap = "Bạn muốn xóa cấu hình thuộc tính này ?";
    var global_conform_delete_restful = "Bạn muốn xóa cấu hình thuộc tính Restful này ?";
    var global_succ_delete_soap = "Xóa cấu hình thuộc tính thành công";
    var global_succ_delete_restful = "Xóa cấu hình thuộc tính Restful thành công";
    var global_succ_edit_soap = "Cập nhật cấu hình thuộc tính CA thành công";
    var global_succ_edit_restful = "Cập nhật cấu hình thuộc tính Restful thành công";
    var global_succ_add_soap = "Thêm mới cấu hình thuộc tính CA thành công";
    var global_succ_add_restful = "Thêm mới cấu hình thuộc tính Restful thành công";
    var global_fm_restful = "Thuộc tính Restful";
    var global_fm_soap = "Thuộc tính";
    var global_title_soap_edit = "Chỉnh sửa thuộc tính CA";
    var global_title_restful_edit = "Chỉnh sửa thuộc tính RestFul";
    var global_title_soap_add = "Thêm mới thuộc tính Soap";
    var global_title_propeties_ca_add = "Thêm mới thuộc tính CA";
    var global_title_restful_add = "Thêm mới thuộc tính RestFul";
    var global_fm_facet = "Thông tin khách hàng";
    var global_fm_status_expire = "Hết hạn";
    var global_fm_not_blank = " không thể trống";

    //General -> ManagementParty
    var manaparty_title_list = "Quản lý Management Party";
    var manaparty_table_list = "Danh sách Management Party";
    var manaparty_title_add = "Thêm mới Management Party";
    var manaparty_title_edit = "Chỉnh sửa Management Party";
    var manaparty_succ_add = "Thêm mới Management Party thành công";
    var manaparty_exists_code = "Mã Management Party đã tồn tại";
    var manaparty_succ_edit = "Cập nhật Management Party thành công";
    var manaparty_fm_code = "Mã Management Party";
    var manaparty_fm_message_mode = "Chế độ thông báo";
    var manaparty_fm_expire_duration = "Thời gian hết hạn";
    var branch_fm_expire_token = "Thời gian hết hạn (Phút)";
    var branch_fm_secretkey = "Khóa bảo mật";
    //General -> FacetManagement
    var facetmana_title_list = "Quản lý thông tin khách hàng";
    var facetmana_table_list = "Danh sách thông tin khách hàng";
    var facetmana_title_add = "Thêm mới thông tin khách hàng";
    var facetmana_title_edit = "Chỉnh sửa thông tin khách hàng";
    var facetmana_succ_add = "Thêm mới thông tin khách hàng thành công";
    var facetmana_exists_code = "Mã trạng thông tin khách hàng đã tồn tại";
    var facetmana_exists_name = "Tên thông tin khách hàng đã tồn tại";
    var facetmana_succ_edit = "Cập nhật thông tin khách hàng thành công";
    var facetmana_fm_code = "Mã thông tin khách hàng";
    //General -> FacetManagement
    var smartversion_title_list = "Quản lý phiên bản hệ thống";
    var smartversion_table_list = "Danh sách phiên bản hệ thống";
    var smartversion_title_add = "Thêm mới phiên bản hệ thống";
    var smartversion_title_edit = "Chỉnh sửa phiên bản hệ thống";
    var smartversion_succ_add = "Thêm mới phiên bản hệ thống thành công";
    var smartversion_exists_code = "Mã trạng phiên bản hệ thống đã tồn tại";
    var smartversion_exists_name = "Tên phiên bản hệ thống đã tồn tại";
    var smartversion_succ_edit = "Cập nhật phiên bản hệ thống thành công";
    var smartversion_fm_code = "Mã phiên bản hệ thống";
    //History -> TransactionList
    var smarttrans_title_list = "Lịch sử giao dịch";
    var smarttrans_table_list = "Danh sách giao dịch";
    var smarttrans_search_list = "Tìm kiếm giao dịch";
    var smarttrans_title_view = "Thông tin chi tiết giao dịch";
    var smarttrans_fm_data_body = "Body Data";
    var smarttrans_fm_data_header = "Header Data";
    //General -> SMPP Party
    var smpp_title_list = "Quản lý SMPP";
    var smpp_table_list = "Danh sách SMPP";
    var smpp_title_add = "Thêm mới SMPP";
    var smpp_title_edit = "Chỉnh sửa SMPP";
    var smpp_succ_add = "Thêm mới SMPP thành công";
    var smpp_exists_code = "Mã SMPP đã tồn tại";
    var smpp_succ_edit = "Cập nhật SMPP thành công";
    var smpp_fm_code = "Mã SMPP";
    var smpp_fm_heartbeat_interval = "HeartBeat Interval";
    var smpp_fm_retry_attempt = "Retry Attempt";
    var smpp_fm_retry_delay_duration = "Delay Duration";
    //Config -> ServerParty
    var serverparty_title_list = "Quản lý Server Entity";
    var serverparty_table_list = "Danh sách Server Entity";
    var serverparty_title_add = "Thêm mới Server Entity";
    var serverparty_title_edit = "Chỉnh sửa Server Entity";
    var serverparty_succ_add = "Thêm mới Server Entity thành công";
    var serverparty_exists_code = "Mã Server Entity đã tồn tại";
    var serverparty_succ_edit = "Cập nhật Server Entity thành công";
    var serverparty_fm_code = "Mã Server Entity";
    //General -> Functionality
    var metadata_title_list = "Quản lý MetaData";
    var metadata_table_list = "Danh sách MetaData";
    var metadata_title_edit = "Chi tiết MetaData";
    var metadata_title_add = "Thêm mới MetaData";
    var metadata_succ_add = "Thêm mới MetaData thành công";
    var metadata_fm_aaid = "AAID";
    var metadata_fm_metadata = "MetaData";
    var metadata_exists_aaid = "AAID";
    var metadata_fm_contenttype = "Loại nội dung";
    var metadata_fm_authenalgorithm = "Thuật toán xác thực";
    var metadata_fm_keyrestricted = "Khóa bị hạn chế";

    //General -> Functionality
    var function_title_list = "Quản lý hàm";
    var function_table_list = "Danh sách hàm";
    var function_title_add = "Thêm mới hàm";
    var function_title_edit = "Chỉnh sửa hàm";
    var function_succ_add = "Thêm mới hàm thành công";
    var function_exists_code = "Mã trạng hàm đã tồn tại";
    var function_exists_name = "Tên hàm đã tồn tại";
    var function_succ_edit = "Cập nhật hàm thành công";
    var function_fm_code = "Mã hàm";
    var function_fm_name = "Tên hàm";
    //General -> CA
    var ca_title_list = "Quản lý nhà cung cấp CA";
    var ca_table_list = "Danh sách nhà cung cấp CA";
    var ca_title_add = "Thêm mới nhà cung cấp CA";
    var ca_title_edit = "Chỉnh sửa nhà cung cấp CA";
    var ca_succ_add = "Thêm mới nhà cung cấp CA thành công";
    var ca_exists_code = "Mã nhà cung cấp CA đã tồn tại";
    var ca_exists_name = "Tên nhà cung cấp CA đã tồn tại";
    var ca_succ_edit = "Cập nhật nhà cung cấp CA thành công";
    var ca_fm_short = "Short Code";
    var ca_fm_code = "Mã CA";
    var ca_fm_name = "Tên CA";
    var ca_fm_OCSP = "OCSP URL";
    var ca_fm_CRL = "Đường dẫn CRL";
    var ca_fm_CRLPath = "Tên CRL";
    var ca_fm_URI = "URI";
    var ca_fm_Cert_01 = "Chứng thư số";
    var ca_fm_CheckOCSP = "Chọn bởi OCSP";
    var ca_fm_unique_DN = "Cho phép trùng chủ thể CTS (Subject DN)";
    var ca_group_CRLFile_1 = "CRL File";
    var ca_error_valid_cert_01 = "Chứng thư số không hợp lệ";
    var ca_error_valid_cert_expire_01 = "Thời gian hiệu lục chứng thư số đã hết hạn";
    var ca_succ_import_crl1 = "Đăng tải tập tin CRL thành công";
    var ca_error_import_crl1 = "Đăng tải tập tin CRL thất bại";
    var ca_group_cert = "Chi tiết chứng thư số";
    var ca_req_info_cert = "Không tìm thấy thông tin chứng thư số";
    var ca_succ_reload = "Tải lại file CRL thành công";
    var ca_error_reload = "Tải lại file CRL thất bại";
    //General -> Certificate Profile
    var certprofile_title_list = "Quản lý gói dịch vụ";
    var certprofile_table_list = "Danh sách gói dịch vụ";
    var certprofile_title_add = "Thêm mới gói dịch vụ";
    var certprofile_title_edit = "Chỉnh sửa gói dịch vụ";
    var certprofile_succ_add = "Thêm mới gói dịch vụ thành công";
    var certprofile_exists_code = "Mã gói dịch vụ đã tồn tại";
    var certprofile_exists_name = "Tên gói dịch vụ đã tồn tại";
    var certprofile_succ_edit = "Cập nhật gói dịch vụ thành công";
    var certprofile_fm_code = "Mã gói dịch vụ";
    var certprofile_fm_service_type = "Loại dịch vụ";
    var certprofile_fm_service_issue = "Đăng ký";
    var certprofile_fm_service_renew = "Gia hạn";
    //admin -> confignamil
    var email_title_list = "Cấu hình Email hệ thống";
    var email_req_smtp = "Vui lòng nhập SMTP Server";
    var email_req_port = "Vui lòng nhập Port";
    var email_succ_edit = "Cập nhật cấu hình mail server thành công";
    var email_fm_port = "Port";
    var email_fm_smtp = "SMTP Server";
    //admin -> ManagePolicy
    var policy_title_list = "Cấu hình tham số hệ thống";
    var policy_title_list_client = "Tham số";
    var policy_title_list_client_fo = "Phân hệ Khách hàng";
    var policy_title_list_client_bo = "Phân hệ Quản trị";
    var policy_group_notification = "Cấu hình thông báo mặc định";
    var policy_succ_edit = "Cập nhật tham số hệ thống thành công";
    var policy_req_empty = "Vui lòng nhập" + " ";
    var policy_req_empty_choose = "Vui lòng chọn" + " ";
    var policy_req_number = " " + "chỉ bao gồm ký tự số";
    var policy_req_unicode = " " + "không chứa ký tự có dấu";
//admin -> ConfigPolicy
    var policy_config_title_list = "Quản lý trường tham số";
    var policy_config_table_list = "Danh sách trường tham số";
    var policy_title_edit = "Chỉnh sửa trường tham số";
    var policy_title_add = "Thêm mới trường tham số";
    var policy_succ_add = "Thêm mới trường tham số thành công";
    var policy_fm_fo = "Khách hàng";
    var policy_fm_bo = "Quản trị";
    var policy_fm_group_fo_bo = "Tham số cho phân hệ";
    var policy_fm_code = "Mã trường tham số";
    var policy_exists_code = "Mã trường tham số đã tồn tại";

//admin -> menulink
    var menu_title_list = "Quản lý phân quyền màn hình";
    var menu_title_table = "Phân quyền màn hình";
    var menu_group_Role = "Chọn chức vụ";
    var menu_fm_Role = "Chức vụ";
    var menu_group_assign = "Menu chưa gán";
    var menu_fm_assign = "Tên menu";
    var menu_fm_parent_name = "Tên menu quản lý";
    var menu_fm_url = "Đường dẫn menu";
    var menu_table_assigned = "Danh sách Menu đã gán";
    var menu_conform_delete = "Bạn muốn xóa Menu này ?";
    var menu_succ_delete = "Xóa Menu thành công";
    var menu_succ_insert = "Thêm mới Menu thành công";
    var menu_error_delete = "Xóa Menu thất bại";
    var menu_error_insert = "Thêm mới Menu thất bại";
    var menu_fm_button_assign = "Gán";
    
    //general -> MethodProfile
    var methodprofile_title_list = "Phân quyền phương thức truy cập gói dịch vụ";
    var methodprofile_title_table = "Phân quyền phương thức";
    var methodprofile_group_formfactor = "Chọn phương thức";
    var methodprofile_fm_formfactor = "Phương thức";
    var methodprofile_group_assign = "Gói dịch vụ chưa gán";
    var methodprofile_fm_profile = "Gói dịch vụ";
    var methodprofile_table_assigned = "Danh sách gói dịch vụ đã gán";
    var methodprofile_conform_delete = "Bạn muốn xóa gói này ?";
    var methodprofile_succ_delete = "Xóa thành công";
    var methodprofile_succ_insert = "Thêm mới thành công";
    var methodprofile_error_delete = "Xóa thất bại";
    var methodprofile_error_insert = "Thêm mới thất bại";
    
    //admin -> UserRole
    var role_title_list = "Quản lý chức vụ";
    var role_title_table = "Danh sách chức vụ";
    var role_title_edit = "Chỉnh sửa chức vụ";
    var role_title_add = "Thêm mới chức vụ";
    var role_group_Role = "Chọn chức vụ";
    var role_fm_code = "Mã chức vụ";
    var role_fm_is_ca = "Chức vụ cho CA";
    var role_fm_is_agent = "Chức vụ cho Đại lý";
    var role_fm_name = "Tên chức vụ";
    var role_succ_add = "Thêm mới chức vụ thành công";
    var role_succ_edit = "Cập nhật chức vụ thành công";
    var role_exists_code = "Mã chức vụ đã tồn tại";
    var role_exists_name = "Tên chức vụ đã tồn tại";
    var role_noexists_functions = "Vui lòng chọn ít nhất một quyền chức năng";
    var role_fm_function_name = "Tên chức năng";
    
    //admin -> esignremain
    var esignremain_title_list = "Báo cáo số lượt ký còn lại";
    var esignremain_title_table = "Danh sách";
    
    //admin -> formfactor
    var formfactor_title_list = "Cấu hình phương thức";
    var formfactor_title_table = "Danh sách phương thức";
    var formfactor_title_edit = "Chỉnh sửa phương thức";
    var formfactor_fm_code = "Mã phương thức";
    var formfactor_fm_name = "Tên phương thức";
    var formfactor_succ_edit = "Cấu hình phương thức thành công";
    var formfactor_exists_name = "Tên phương thức đã tồn tại";
    var formfactor_title_properties = "Cấu hình kết nối hệ thống";
    
    // FUNCTION ACCESS
    var funrole_fm_islock = "Cấu hình khóa";
    var funrole_fm_isunlock = "Cấu hình mở khóa";
    var funrole_fm_issopin = "Cấu hình thay đổi SOPIN";
    var funrole_fm_ispush = "Cấu hình gửi thông báo";
    var funrole_fm_isinit = "Cấu hình khởi tạo";
    var funrole_fm_isdynamic = "Cấu hình nội dung động";
    var funrole_fm_isinformation = "Cấu hình thông tin";
    var funrole_fm_isactive = "Cấu hình hiệu lực";
    var funrole_fm_editcert = "Cấu hình chứng thư số";
    var funrole_fm_approvecert = "Duyệt chứng thư số";
    var funrole_fm_deleterequest = "Từ chối yêu cầu";
    var funrole_fm_addrenewal = "Thêm yêu cầu cấp bù";
    var funrole_fm_deleterenewal = "Từ chối yêu cầu cấp bù";
    var funrole_fm_importrenewal = "Tải lên danh sách yêu cầu cấp bù";
    var funrole_fm_accessrenewal = "Truy cập chức năng yêu cầu cấp bù";
    var funrole_fm_revoke_cert = "Truy cập chức năng thu hồi chứng thư số";
    var funrole_fm_export_cert = "Truy cập chức năng lưu file chứng thư số";
    //User -> MenuScreen
    var menusc_title_list = "Quản lý Menu";
    var menusc_title_table = "Danh sách Menu";
    var menusc_title_edit = "Chỉnh sửa Menu";
    var menusc_title_add = "Thêm mới Menu";
    var menusc_fm_nameparent = "Menu quản lý";
    var menusc_fm_name_vn = "Tên Menu (Tiếng Việt)";
    var menusc_fm_name = "Tên Menu";
    var menusc_fm_name_en = "Tên Menu (Tiếng Anh)";
    var menusc_fm_code = "Mã Menu";
    var menusc_fm_url = "Đường dẫn Menu";
    var menusc_succ_add = "Thêm mới Menu thành công";
    var menusc_succ_edit = "Cập nhật Menu thành công";
    var menusc_exists_linkurl = "Đường dẫn Menu đã tồn tại";
    var menusc_exists_nameparent = "Tên Menu quản lý đã tồn tại";
    //user -> userlist
    var user_title_list = "Quản lý nhân viên";
    var user_title_search = "Tìm kiếm nhân viên";
    var user_title_table = "Danh sách nhân viên";
    var user_title_edit = "Chỉnh sửa nhân viên";
    var user_title_add = "Thêm mới nhân viên";
    var user_title_info = "Thông tin nhân viên";
    var user_title_roleset = "Danh sách quyền chức năng";
    var user_title_roleset_token = "Quyền chức năng Token";
    var user_title_roleset_cert = "Quyền chức năng chứng thư số";
    var user_title_roleset_another = "Quyền chức năng khác";
    var user_succ_add = "Thêm mới nhân viên thành công";
    var user_succ_edit = "Cập nhật nhân viên thành công";
    var user_exists_username = "Tên đăng nhập đã tồn tại";
    var user_exists_email = "Địa chỉ email đã tồn tại";
    var user_exists_cert_hash = "Thông tin chứng thư số đã tồn tại";
    var user_exists_user_role_admin = "Đã tồn tại tên đăng nhập có vai trò quản trị trong hệ thống";
    var user_conform_cancel = "Bạn chắc chắn muốn hủy nhân viên này ?";
    var user_title_delete = "Xóa nhân viên";
    var user_title_delete_note = "Ghi chú: Vui lòng chọn nhân viên tiếp nhận và quản lý chứng thư số của nhân viên bị xóa";
    
    //Rose -> RoseList
    var rose_title_list = "Cấu hình nhóm hoa hồng";
    var rose_title_table = "Danh sách";
    var rose_title_edit = "Chỉnh sửa nhóm hoa hồng";
    var rose_title_add = "Thêm mới nhóm hoa hồng";
    var rose_fm_code = "Mã nhóm hoa hồng";
    var rose_fm_rose = "Nhóm hoa hồng";
    var rose_succ_edit = "Chỉnh sửa thành công";
    var rose_succ_add = "Thêm mới thành công";
    var rose_exists_profile_properties = "Gói dịch vụ đã tồn tại";
    var rose_permission_profile_list = "Tỷ lệ hoa hồng cho gói dịch vụ";
    
    // profileaccss ->  profileaccss
    var profileaccss_title_list = "Cấu hình nhóm quyền gói dịch vụ";
    var profileaccss_title_table = "Danh sách";
    var profileaccss_title_edit = "Chỉnh sửa nhóm quyền gói dịch vụ";
    var profileaccss_title_add = "Thêm nhóm quyền gói dịch vụ";
    var profileaccss_fm_code = "Mã nhóm quyền";
    var profileaccss_fm_agency = "Danh sách đại lý";
    var profileaccss_fm_rose = "Nhóm quyền gói dịch vụ";
    var profileaccss_fm_service_type = "Loại yêu cầu CTS";
    var profileaccss_fm_major_cert = "Chức năng CTS";
    var profileaccss_succ_edit = "Cấu hình thành công";
    var profileaccss_succ_add = "Thêm mới thành công";
    var profileaccss_exists_profile_properties = "Gói dịch vụ đã tồn tại";
    var profileaccss_apply_profile_agency = "Áp dụng cấu hình cho đại lý";
    var profileaccss_exists_code = "Mã nhóm quyền đã tồn tại";

    var global_fm_certprofile = "Hồ sơ chứng thư số";
    var global_fm_certstatus = "Trạng thái chứng thư số";
    var global_fm_cert_expire_number = "Số ngày còn hiệu lực";
    var global_fm_common = "Tên chủ thể";
    var global_fm_subject = "DN";
    var global_fm_public_key_hash = "Public Key Hash";
    var global_fm_certificate_hash = "Chứng thư số Hash";
    var global_fm_key_id = "Key ID";
    var global_fm_key_selector = "KEY SELECTOR";
    var global_fm_service_deny = "SERVICE DENY";
    var global_fm_authority_key_id = "AUTHORITY KEY ID";
    var global_error_empty_cert = "Chứng thư số không tồn tại";
    var global_error_exists_mst_budget_regis = "Mã số thuế/ Mã ngân sách/ CMND/ Hộ chiếu đã tồn tại trong hệ thống\nVui lòng truy cập mua thêm CTS";

    var global_fm_ca = "Nhà cung cấp CA";
    var global_fm_certpurpose = "Loại chứng thư số";
    if(IsWhichCA === "18") {
        global_fm_certpurpose = "Loại CTS";
    }
    var global_fm_certalgorithm = "Thuật toán chứng thư số";
    var global_fm_Password_new = "Mật khẩu mới";
    var global_fm_Password_conform = "Xác nhận mật khẩu mới";
    var global_fm_Password_old = "Mật khẩu hiện tại";
    var global_fm_Password_change = "Thay đổi mật khẩu";
    var global_fm_button_PasswordChange = "Đồng ý";
    var global_fm_button_setup = "Cài đặt";
    var global_fm_button_setup_ejbca = "Cài đặt từ RA";
    var global_fm_button_import = "Đăng tải";
    var global_fm_button_check = "Kiểm tra";
    var global_fm_valid = "Ngày hiệu lực chứng thư số";
    var global_fm_valid_cert = "Ngày hiệu lực";
    var global_fm_browse_file = "Chọn file";
    var global_fm_browse_cert_note = "Vui lòng chọn file có dung lượng nhỏ hơn ";
    var global_fm_fileattach_support = "Các định dạng file được hỗ trợ: ";
    var global_fm_browse_cert_addnote = "Ưu tiên các file pdf, image";
    var global_fm_Expire = "Ngày hết hiệu lực chứng thư số";
    var global_fm_Expire_cert = "Ngày hết hiệu lực";
    var global_fm_pass_p12 = "Mật khẩu P12";
    var global_fm_dateUpdate = "Ngày cập nhật";
    var global_fm_dateUpdate_next = "Ngày cập nhật kế tiếp";
    var global_fm_dateend = "Ngày kết thúc";
    var global_fm_activation = "Ngày kích hoạt";
    var global_fm_Method = "Phương thức";
    var global_fm_mode = "Chế độ";
    var global_fm_worker = "Tên worker";
    var global_fm_isbackoffice_grid = "FrontOffice/BackOffice";
    var global_fm_isbackoffice = "FrontOffice/BackOffice";
    var global_fm_key = "Tên khóa";
    var global_fm_logout = "Đăng xuất";
    var global_fm_title_account = "Thông tin tài khoản";
    var global_fm_otp_serial = "OTP Serial";
    var global_fm_check_date = "Tìm theo ngày";
    var global_fm_check_date_profile = "Tìm theo (Ngày nhận hồ sơ)";
    var global_fm_check_date_control = "Tìm theo (Ngày đối soát)";
    var global_fm_expire_date = "Số ngày còn hiệu lực";
    var global_fm_check_month = "Tìm kiếm theo tháng";
    var global_fm_check_quarterly = "Tìm kiếm theo quý";
    var global_fm_check_token = "Tìm kiếm theo Mã token";
    var global_fm_company = "Công ty";
    var global_fm_issue = "Phát hành";
    var global_fm_size = "Kích thước (KB)";
    var global_fm_OU = "Đơn vị tổ chức (OU)";
    var global_fm_MST = "Mã số thuế";
// BK->   var global_fm_enterprise_id = "Mã số thuế/ Mã ngân sách/ Số quyết định/ Bảo hiểm xã hội";
// BK->   var global_fm_personal_id = "CMND/ Căn cước công dân/ Hộ chiếu/ Mã số thuế/ Bảo hiểm xã hội";
    var global_fm_enterprise_id = "UID doanh nghiệp";
    var global_fm_personal_id = "UID cá nhân";
    var global_fm_callback_url = "Đường dẫn máy trạm tích hợp API để nhận thông báo yêu cầu từ Token Manager";
    var global_fm_callback_when_approve = "Giá trị của callback URL khi duyệt";
    var global_fm_decision = "Số quyết định";
    var global_fm_share_mode_cert = "Cho phép bổ sung thông tin dịch vụ CTS";
    var global_fm_issue_p12_enabled = "Cho phép cấp phát CTS P12";
    var global_fm_ID = "Mã số";
    var global_fm_date_grant = "Cấp ngày";
    var global_fm_organi_grant = "Tổ chức cấp";
    var global_fm_representative_legal = "Người đại diện pháp luật";
    var global_fm_MNV = "Mã nhân viên";
    var global_fm_CMND = "CMND";
    var global_fm_CMND_ID_Card = "Số CMND, thẻ căn cước";
    var global_fm_place = "Nơi cấp";
    var global_fm_cmnd_date = "Ngày cấp";
    var global_fm_O = "Tổ chức (O)";
    var global_fm_O_notrefix = "Tổ chức";
    var global_fm_L = "Quận/Huyện (L)";
    var global_fm_T = "Chức vụ (T)";
    var global_fm_C = "Quốc gia (C)";
    var global_fm_ST = "Tỉnh/Thành phố (ST)";
    var global_fm_CN = "Tên công ty (CN)";
    var global_fm_grid_CN = "Tên chủ thể (CN)";
    var global_fm_grid_personal = "Tên cá nhân";
    var global_fm_grid_company = "Tên công ty";
    var global_fm_grid_domain = "Tên miền";
    var global_fm_CN_CN = "Họ và tên (CN)";
    var global_fm_serial = "Số Serial CTS";
    var global_fm_choose_owner_cert = "Tìm theo";
    var global_fm_Status = "Trạng thái";
    var global_fm_branch_status = "Trạng thái đại lý";
    var global_fm_status_control = "Trạng thái đối soát";
    var global_fm_Status_token = "Trạng thái token";
    var global_fm_user_lock_unlock_token = "Nhân viên làm lệnh khóa/mở khóa token";
    var global_fm_Status_signed = "Trạng thái ký";
    var global_fm_Status_notice = "Trạng thái thông báo";
    var global_fm_apply_signed = "Đã ký";
    var global_fm_unapply_signed = "Chưa ký";
    var global_fm_Status_cert = "Trạng thái chứng thư số";
    var global_fm_Status_request = "Trạng thái yêu cầu";
    var global_fm_Status_agreement = "Trạng thái hợp đồng";
    var global_fm_os_type = "Loại OS";
    var global_fm_smart_version = "Phiên bản hệ thống";
    var global_fm_from_system = "Từ hệ thống";
    var global_fm_from_system_uri = "Từ đường dẫn";
    var global_fm_to_system = "Đến hệ thống";
    var global_fm_to_system_uri = "Đến đường dẫn";
    var global_fm_activity = "Hoạt động";
    var global_fm_lost = "Báo mất";
    var global_fm_relost = "Hủy bỏ báo mất";
    var global_fm_lock = "Khóa";
    var global_fm_type = "Loại";
    var global_fm_value = "Giá trị";
    var global_fm_chain_cert = "Chứng thư số nhà cung cấp";
    var global_error_chain_cert = "Chứng thư số nhà cung cấp không tồn tại";
    var global_error_cert_compare_ca = "Chứng thư số không hợp lệ";
    var global_error_cert_compare_csr = "Chứng thư số không hợp lệ";
    var global_fm_Note = "Ghi chú";
    var global_fm_Note_offset = "Ghi chú hồ sơ";
//    var global_fm_soft_copy = "Bản điện tử";
//    if(IsWhichCA === "7") {
    var global_fm_soft_copy = "Trạng thái của các hồ sơ pháp lý";
    //}
    var global_fm_Content = "Nội dung";
    var global_fm_tran_code = "Mã giao dịch";
    var global_fm_tran_timeout = "Timeout giao dịch (giây)";
    var global_fm_filter_search = "Tiêu chí tìm kiếm";
    var global_fm_combox_success = "Thành công";
    var global_fm_combox_errorsend = "Gửi lỗi";
    var global_fm_cert_circlelife = "Vòng đời chứng thư số";
    var global_req_all = "Vui lòng nhập đầy đủ thông tin";
    var global_req_length = "Chiều dài không hợp lệ";
    var global_req_file = "Vui lòng chọn file";
    var global_req_file_has_data = " (File có nội dung)";
    var global_req = policy_req_empty;
    var global_errorsql = "Có lỗi xảy ra, vui lòng thử lại sau";
    var global_print2_fullname_business = "Tên giao dịch đầy đủ (Viết hoa, có dấu)";
    var global_req_email_subject_san = "Vui lòng nhập email trong chứng thư số giống nhau";
    var global_req_print_not_support = "Loại yêu cầu không được hỗ trợ in";
    var global_req_warning_exists_cert = "Đã tồn tại yêu cầu đang chờ duyệt với thông tin chứng thư số như trên\nTiếp tục đăng ký?";
    
    var global_alert_login = "Thời gian đăng nhập đã hết, vui lòng đăng nhập lại";
    var global_alert_another_login = "Tài khoản đã bị khóa/ bị truy cập từ thiết bị khác, vui lòng kiểm tra lại";
    var global_alert_another_menu = "Bạn không có quyền truy cập chức năng này, vui lòng kiểm tra lại";
    var global_alert_license_invalid = "License không hợp lệ. Xin vui lòng liên hệ Hotline 1900 6884 \nhoặc Email info@mobile-id.vn để được hỗ trợ";
    var global_error_login_info = "Thông tin tài khoản đăng nhập không tồn tại, vui lòng thử lại";
    var global_error_invalid = ": Không hợp lệ";
// Admin -> LicenseList
    var license_title_list = "Quản lý bản quyền hệ thống";
    var license_table_list = "Đăng tải tập tin bản quyền";
    var license_title_search = "Tìm kiếm";
    var license_title_edit = "Thông tin chi tiết";
    var license_req_file = "Vui lòng chọn tập tin danh sách bản quyền";
    var license_fm_file = "Chọn tập tin";
    var license_succ_import = "Đăng tải tập tin bản quyền thành công";
    var license_group_hardware = "Thông tin Hardware";
    var license_group_view = "Thông tin chi tiết bản quyền";
    var license_fm_type = "Loại bản quyền";
    var license_fm_token_sn = "Token SN";
    var license_fm_user_enabled = "Được sử dụng";
    var license_group_Function = "Thông tin về tính năng";
    var license_error_file = "tập tin danh sách bản quyền không hợp lệ";
    var license_error_no_token_sn = "Đăng tải thất bại.\nGiá trị [TOKEN_SN] không tồn tại";
    var license_error_no_license_type = "Đăng tải thất bại.\nGiá trị [LICENSE_TYPE] không tồn tại";
    var license_succ_import_insert = ". Thêm mới: ";
    var license_succ_import_update = " ; Đã tồn tại: ";
    var license_succ_import_error = " ; Lỗi: ";

// sumary page website
    var CSRF_Mess = 'Phiên sử dụng đã hết, vui lòng tải lại trang';
    var TitleLoginPage = "Hệ thống quản trị đại lý";
    var TitlePolicyPage = "Chính sách bảo mật";
    var TitleTermsPage = "Điều khoản dịch vụ";
    var TitlePolicyLink = "Chỉnh sách bảo mật";
    var TitleTermsLink = "Điều khoản dịch vụ";
    var TitleHomePage = "Trang chủ Back-Office";
    var error_title_list = "Lỗi hệ thống";
    var error_content_home = "Có lỗi hệ thống xảy ra. Vui lòng quay lại trang chủ";
    var error_content_login = "Có lỗi hệ thống xảy ra. Vui lòng quay lại đăng nhập";
    var error_content_link_download = "Tải xuống tệp tin mô tả lỗi";
    var error_content_link_out = "tại đây";
    var login_req_captcha = "Mã CAPTCHA không chính xác";
    var login_title_captcha = "Làm mới mã captcha";
    var login_fm_captcha = "Mã CAPTCHA";
    var login_fm_forget = "Quên mật khẩu ?";
    var login_fm_token_ssl = "Thiết bị token";
    var login_title_forget = "Quên mật khẩu";
    var login_succ_forget = "Vui lòng kiểm tra email để nhận mật khẩu mới";
    var login_succ_forget_request = "Yêu cầu cấp mới mật khẩu đã thực hiện thành công. Vui lòng chờ quản trị hệ thống phê duyệt";
    var login_fm_buton_login = "Đăng nhập";
    var login_fm_buton_cancel = "Hủy";
    var login_fm_buton_OK = "Đồng ý";
    var login_fm_buton_continue = "Tiếp tục";
    var login_error_timeout = "Không nhận được phản hồi từ máy chủ";
    var login_error_exception = "Lỗi hệ thống. Vui lòng thử lại";
    var login_error_lock = "Tài khoản bị tạm khóa, vui lòng thử lại sau";
    var login_error_incorrec = "Thông tin đăng nhập không chính xác";
    var login_error_inactive = "Tài khoản bị khóa. Vui lòng liên hệ quản trị hệ thống";
    var login_error_token_ssl = "Bạn không cấp quyền đăng nhập theo phương thức Token";
    var login_conform_forget = "Vui lòng xác nhận lại thông tin email: {EMAIL}";

    var global_fm_detail = "Chi tiết";
    var global_fm_expand = "Mở rộng";
    var global_fm_collapse = "Thu gọn";
    var global_fm_hide = "Ẩn";
    var global_fm_search_expand = "Mở rộng tìm kiếm";
    var global_fm_search_hide = "Thu gọn tìm kiếm";
    
    var global_fm_button_reset = "Reset";
    var global_fm_button_activate = "Kích hoạt";
    var global_fm_button_unactivate = "Hủy kích hoạt";
    var global_fm_file_name = "Tên file";
    var global_fm_down = "Tải xuống";
    var global_fm_view = "Xem";
    var global_fm_p12_down = "Tải xuống P12";
    var global_fm_p7p_down = "Tải xuống P7B";
    var global_fm_down_enterprise = "CTS doanh nghiệp";
    var global_fm_down_personal = "CTS cá nhân";
    var global_fm_down_staff = "CTS cá nhân trong doanh nghiệp";
    var global_fm_down_pem = "Tải xuống PEM";
    var global_fm_license_down = "Tải giấy chứng nhận";
    var global_fm_license_create = "Tạo giấy chứng nhận";
    var global_fm_sign_confirmation = "Ký lại giấy xác nhận";
    var global_fm_wait_sign_confirmation = "Đang chờ ký giấy xác nhận";
    var global_cbx_wait_sign_confirmation = "Đang chờ ký lại";
    var global_cbx_sign_confirmation = "Đã ký lại";
    var global_succ_license_create = "Tạo giấy chứng nhận điện tử thành công";
    var global_fm_down_cts = "Tải chứng thư số";
    var global_fm_change = "Thay đổi";
    var global_fm_dispose = "Hủy bỏ";
    var global_fm_copy_all = "Chép vào Clipboard";
    var global_succ_copy_all = "Chép vào Clipboard thành công";

    ///face 03 ///
    var global_req_formfactor_support = "Hệ thống chưa hỗ trợ phương thức từ BackOffice";
    var global_req_no_special = " không bao gồm ký tự đặc biệt";
    var global_req_no_space = " không bao gồm khoảng trắng";
    var global_fm_button_delete = "Xóa";
    var global_fm_paging_total = "Tổng số dòng ";
    var policy_check_length_pass = "Chiều dài tối thiểu phải nhỏ hơn chiều dài tối đa mật khẩu";
    var policy_check_number_zero = " " + "phải lớn hơn 0";
    var global_fm_button_reset_pass = "Cấp mới mật khẩu";
    var global_fm_button_check_pass_default = "Kiểm tra mật khẩu mặc định";
    var global_fm_character = " " + "ký tự";
    var global_fm_phone_zero = " " + "phải bắt đầu bằng số 0";
    var global_fm_phone_8_11 = "Số điện thoại phải nằm trong khoảng từ 8 đến 11 ký tự";
    var pass_req_no_space = "Mật khẩu không bao gồm khoảng trắng";
    var user_req_no_space = "Tên đăng nhập không bao gồm khoảng trắng";
    var pass_req_min_greater = "Chiều dài tối thiếu mật khẩu phải >= ";
    var pass_req_max_less = "Chiều dài tối đa mật khẩu phải <= ";
    var pass_req_character = "Mật khẩu phải tồn tại ít nhất 1 ký tự chữ";
    var pass_req_special = "Mật khẩu phải tồn tại ít nhất 1 ký tự đặc biệt";
    var pass_req_number = "Mật khẩu phải tồn tại ít nhất 1 ký tự số";
    var pass_req_upcase = "Mật khẩu phải tồn tại ít nhất 1 ký tự hoa";
    var pass_req_another_old = "Mật khẩu mới phải khác mật khẩu hiện tại";
    var pass_req_conform_new = "Xác nhận mật khẩu mới không chính xác";
    var pass_error_old = "Mật khẩu hiện tại không đúng";
    var pass_error_choise_another = "Vui lòng chọn mật khẩu khác";
    var pass_error_choise_another_exists = "Mật khẩu mới không thể trùng với {NUMBER} mật khẩu cuối cùng! Vui lòng nhập mật khẩu mới";
    var pass_succ_change = "Thay đổi mật khẩu thành công";
    var pass_succ_change_show = ". Mật khẩu là: ";
    var pass_error_account_old = "Thông tin tài khoản hiện tại không chính xác";

    var global_fm_check_search = "Vui lòng chọn ít nhất 1 tiêu chí để tìm kiếm";
    var pass_fm_Password_first = "Thông tin thay đổi mật khẩu";

// Send mail
    var sendmail_error = "Gửi mail thất bại. Vui lòng thử lại sau";
    var sendmail_success = "Gửi thư thành công";
    var sendmail_notexists = "Địa chỉ email không chính xác";
    var sendmail_notexists_account = "Tên đăng nhập không chính xác";

    // Send mail Password Signserver
    var sendmail_error_signserver = "Gửi mail thất bại. Vui lòng thử lại sau";

//Global
    var global_alert_search_multiline = "Số lượng tìm kiếm vượt quá 10,000 dòng, hệ thống sẽ tự động tìm";
    var global_error_export_excel = "Xuất file Excel thất bại";
    var global_success_export_excel = "Hệ thống đã tiếp nhận yêu cầu kết xuất file thành công";
    var global_error_insertmenulink = "Thêm mới màn hình chức danh thất bại";
    var global_error_deletemenulink = "Xóa màn hình chức danh thất bại";
    var global_search_time_all = "Theo tất cả thời gian trong hệ thống";
// button
    var global_button_grid_delete = "Xóa";
    var global_button_grid_edit = "Chỉnh sửa";
    var global_button_grid_smart_id = "SMART-ID";
    var global_button_grid_mobile_otp = "MOBILE-OTP";
    var global_button_grid_uaf = "UAF";
    var global_button_grid_config = "Cấu hình";
    var global_button_grid_pki = "PKI";
    var global_button_grid_OTP = "OTP";
    var global_button_grid_lock = "Khóa";
    var otp_button_grid_lock = "Block";
    var global_label_grid_sum = "Tổng cộng ";
    var global_button_grid_cancel = "Hủy";
    var global_button_grid_authen = "Xác thực";
    var global_button_grid_synch = "Đồng bộ";

    var global_button_grid_lost = "Lost";
    var global_button_grid_unlost = "UnLost";
    var global_button_grid_unlock = "Mở khóa";
    var global_button_grid_resetpasscode = "Cấp lại Passcode";
    var global_button_grid_sendmail = "Gửi Mail";
    var global_button_reconfirm = "Xác nhận lại";
    var global_button_p12_sendmail = "Gửi Mail P12";
    var otp_button_grid_unlock = "UnBlock";
    var global_button_grid_enable = "Enable";
    var global_button_grid_disable = "Disable";
    var global_fm_gen_pass = "Tạo mật khẩu";
    var global_no_data = "Không tìm thấy dữ liệu !";
    var global_no_getcompany_data = "Không tìm được dữ liệu. Vui lòng nhập thông tin";
    var global_no_print_data = "Không tìm thấy mẫu in !";
    var global_no_file_list = "Danh sách File trống";
    var global_check_datesearch = "Từ ngày phải nhỏ hơn hoặc bằng đến ngày";
    var global_check_date_expired = "Thời gian hết hạn phải lớn hơn ngày hiện tại";
    var global_succ_succ = "Thành công";
    var global_fm_mess_in = "Thông tin dữ liệu yêu cầu";
    var global_fm_mess_out = "Thông tin dữ liệu trả về";
    var global_fm_Status_OTP = "Số lần xác thực mã kích hoạt sai còn lại";
    var global_fm_Status_SignServer = "Trạng thái hợp đồng SignServer";
    var global_fm_Method_Smart_ID = "Phương thức Smart ID";
    var global_fm_Method_Mobile_OTP = "Phương thức Mobile OTP";
    var global_fm_Method_UAF = "Phương thức UAF";
    var global_fm_Status_PKI = "Trạng thái hợp đồng PKI";
    var global_fm_status_profile = "Trạng thái hồ sơ";
    var global_fm_Function_tran = "Hàm";
    var global_fm_import_choise_text = "File chữ";
    var global_fm_import_choise_image = "File hình ảnh";
    var global_req_text_format = "Vui lòng nhập đúng định dạng file chữ .txt, .pem";

    var global_req_phone_format = "Vui lòng nhập đúng định dạng số điện thoại";
    var user_error_no_data = "Thông tin nhân viên không tồn tại, vui lòng kiểm tra lại";
    var user_conform_reset_pass = "Bạn muốn cấp mới mật khẩu ?";
    var token_confirm_delete = "Bạn có muốn xóa Token này ?";
    var user_conform_delete = "Bạn muốn xóa nhân viên này ?";
    var user_succ_delete = "Xóa nhân viên thành công";
    var global_fm_require_label = " " + "(*)";
    var global_fm_import_sample = "Tham khảo tập tin mẫu: ";
    var global_req_info_cert = "Thông tin chứng thư số không hợp lệ";
    //request -> tokenlist
    var token_title_list = "Quản lý Token";
    var token_title_search = "Tìm kiếm";
    var token_title_table = "Danh sách";
    var token_title_edit = "Chi tiết Token";
    var token_title_add = "Thêm mới Token";
    var token_title_init = "Khởi tạo Token";
    var token_title_changesopin = "Thay đổi SOPIN của Token";
    var token_title_delete = "Xóa token";
    var token_exists_tokensn = "Token SN đã tồn tại";
    var token_succ_edit = "Cấu hình thành công";
    var token_succ_delete = "Xóa thành công";
    var token_conform_update_multi = "Bạn muốn cấu hình nhiều Token ?";
    var token_succ_add_renew = "Thêm mới thành công";
    var token_fm_tokenid = "Mã Token";
    var token_fm_signing_number = "Số lần ký";
    var token_fm_sopin = "SOPIN";
    var token_fm_tokenid_new = "Mã Token mới";
    var token_fm_subject = "Tên đối tượng";
    var token_fm_company = "Tên công ty";
    var token_fm_valid = "Hiệu lực từ (Chứng thư số)";
    var token_fm_expire = "Hiệu lực đến (Chứng thư số)";
    var global_fm_FromDate_valid = "Hiệu lực từ (Bắt đầu)";
    var global_fm_ToDate_valid = "Hiệu lực từ (Kết thúc)";
    var global_fm_FromDate_expire = "Hiệu lực đến (Bắt đầu)";
    var global_fm_FromDate_profile = "Từ (Ngày nhận hồ sơ)";
    var global_fm_ToDate_profile = "Đến (Ngày nhận hồ sơ)";
    var global_fm_From_Control = "Từ (Ngày đối soát)";
    var global_fm_To_Control = "Đến (Ngày đối soát)";
    
    var global_fm_From_effective = "Từ (Ngày hiệu lực)";
    var global_fm_To_effective = "Đến (Ngày hiệu lực)";
    var global_fm_From_expire = "Từ (Ngày hết hiệu lực)";
    var global_fm_To_expire = "Đến (Ngày hết hiệu lực)";
    
    var global_fm_ToDate_expire = "Hiệu lực đến (Kết thúc)";
    var token_fm_validexpire_search = "Thời gian hiệu lực và hết hiệu lực (Chứng thư số)";
    var token_fm_personal = "Tên cá nhân";
    var token_fm_serialcert = "Certificate SN";
    var token_fm_thumbprintcert = "Thumbprint";
    var token_fm_taxcode = "MST";
    var token_fm_block = "Khóa";
    var token_fm_reason_block = "Lý do khóa";
    var token_fm_all_apply = "Áp dụng cho tất cả Token trong hệ thống (bỏ qua file đăng tải)";
    var token_fm_unblock = "Mở khóa";
    var token_fm_csr = "CSR (Yêu cầu cấp phát chứng thư số)";
    var token_fm_innit = "Khởi tạo";
    var token_fm_change = "Thay đổi SOPIN";
    var token_fm_datelimit = "Chứng thư số hiệu lực đến";
    var token_fm_mobile = "Điện thoại";
    var token_fm_email = "Email";
    var token_fm_address = "Địa chỉ";
    var token_fm_address_permanent = "Địa chỉ thường trú";
    var token_fm_address_billing = "Địa chỉ xuất hóa đơn";
    var token_fm_address_residence = "Hộ khẩu thường trú";
    var token_fm_menulink = "Menu động";
    var token_fm_linkname = "Tên Menu";
    var token_fm_linkvalue = "Website";
    var token_fm_noticepush = "Hiển thị thông báo (sticker) trên Token Manager";
    var token_fm_noticeinfor = "Nội dung thông báo";
    var token_fm_noticelink = "Liên kết thông báo";
    var token_fm_colortext = "Màu chữ";
    var token_fm_colorgkgd = "Màu nền";
    var token_fm_infor = "Thông tin";
    var token_fm_location = "Vị trí";
    var token_fm_state = "Quận/Huyện";
    var token_fm_enroll = "Cấp phát";
    var token_fm_TimeOffset = "Thời hạn hợp đồng";
    var token_fm_expire_mmyy = "Thời gian hết hạn";
    var token_fm_dn = "DN (Distinguished Name)";
    var token_fm_passport = "CMND/HC";
    var token_fm_version = "Phiên bản Token";
    var token_fm_agent = "Đại lý";
    var token_fm_agent_level_one = "Đại lý cấp 1";
    var token_group_info = "Thông tin chi tiết";
    var token_group_update = "Cấu hình";
    var token_group_notification = "Thông tin hiển thị (sticker) trên thanh ngang của Token Manager";
    var token_group_dynamic = "Menu động";
    var token_group_other = "Cấu hình khác";
    var token_group_cert_history = "Lịch sử chứng thư số của Token";
    var token_group_request_edit = "Danh sách yêu cầu chỉnh sửa Token";
    var token_title_import = "Tải lên danh sách Token";
    var token_fm_typesearch = "Tìm kiếm cho";
    var token_fm_import_sample = "Tham khảo file mẫu: ";
    var token_fm_datelimit_example = "Ví dụ: (ISO 8601 date: [yyyy-MM-dd HH:mm:ssZZ]: '2018-09-20 14:01:28+07:00')";
    var token_error_no_column = "Tải lên thất bại.\nĐịnh dạng cột thông tin không đúng";
    var token_error_no_tokenid = "Tải lên thất bại.\nGiá trị [TOKEN_SN] không tồn tại";
    var token_error_no_sopin = "Tải lên thất bại.\nGiá trị [TOKEN_SOPIN] không tồn tại";
    var token_error_no_agent = "Tải lên thất bại.\nGiá trị [Agency] không tồn tại";
    var token_error_datelimit_format = "Vui lòng nhập định dạng ngày hợp lệ";
    var token_error_datelimit_date = "Vui lòng nhập thời gian hết hiệu lực";
    var token_error_datelimit_format_date = "Định dạng ngày: (ddd:HH:MM)";
    var token_succ_import = "Quá trình tải lên thành công";
    var token_succ_setup = "Cài đặt thành công";
    var token_succ_check_import = "Quá trình kiểm tra thành công. Nhấn nút cài đặt để lưu thông tin";
    var token_error_check_import = "Có lỗi xảy ra, vui lòng kiểm tra chi tiết trong file kết quả";
    var token_succ_import_insert = ". Thêm mới: ";
    var token_succ_import_update = " ; Cập nhật: ";
    var token_succ_import_error = " ; Lỗi: ";
    var token_succ_import_insert_replace = "Thêm mới: ";
    var token_succ_import_update_replace = "Cập nhật: ";
    var token_succ_import_error_replace = "Không thành công: ";
    var token_error_import_format = "Định dạng Excel: XLS, XLSX, CSV";
    var token_fm_lock_opt = "Trạng thái xác thực mã kích hoạt bị khóa";
    var token_fm_unlock_opt = "Trạng thái xác thực mã kích hoạt đang hoạt động";
    var token_confirm_unlock_temp = "Bạn chắc chắn muốn mở khóa ?";
    var token_confirm_resetpasscode_temp = "Bạn chắc chắn muốn cấp lại Passcode ?";
    var token_confirm_lock_temp = "Bạn chắc chắn muốn khóa ?";
    var token_succ_reset_opt = "Mở khóa trạng thái xác thực mã kích hoạt thành công";
    
    var tokenreport_title_list = "Báo cáo tồn kho đại lý";
    var tokenreport_fm_choose_time_export = "Thời gian xuất kho";
    var tokenreport_fm_choose_time_import = "Thời gian nhập kho";
    var tokenreport_fm_agenct_date_export = "Ngày xuất kho cho đại lý";
    
    //request -> certificatelist
    var cert_title_list = "Quản lý duyệt chứng thư số";
    var cert_title_search = "Tìm kiếm";
    var cert_title_table = "Danh sách";
    var cert_title_edit = "Thông tin yêu cầu";
    var cert_title_register_cert = "Thông tin chứng thư số";
    var cert_title_register_csr = "Thông tin CSR";
    var cert_title_register_owner = "Thông tin chủ sở hữu";
    var cert_succ_approve = "Duyệt chứng thư số thành công";
    var cert_error_approve = "Duyệt chứng thư số thất bại";
    var cert_succ_reissue = "Cấp phát chứng thư số thành công";
    var cert_fm_type_request = "Loại yêu cầu";
    var cert_fm_request = "yêu cầu";
    var cert_fm_request_agent = "Duyệt trong đại lý";
    var cert_fm_major_name = "Tên chức năng";
    var cert_fm_major_code = "Mã chức năng";
    var cert_fm_profile_list = "Loại chứng thư số";
    var cert_fm_cert_profile = "Certificate Profile (CORECA)";
    var cert_fm_delete_cert = "Xóa chứng thư số";
    var cert_fm_usereib = "Entity Name (CORECA)";
    var cert_fm_date_approve_fee = "Thời gian duyệt cấp chứng thư số";
    var cert_fm_user_fee = "Tài khoản cấp chứng thư số";
    var cert_succ_edit = "Cấu hình thành công";
    var cert_succ_returnfee = "Cấu hình thành công";
    var cert_fm_Status = "Duyệt chứng thư số";
    var cert_fm_push_notice = "Cho phép gửi Email";
    var cert_fm_revoke_delete = "Xóa chứng thư số bị thu hồi";
    var cert_fm_restore_status_old = "Khôi phục trạng thái hoạt động của chứng thư số cũ";
    var cert_fm_revoke_delete_old = "Xóa chứng thư số cũ khi chứng thư số mới được cấp";
    var cert_confirm_otp_sendmail = "Bạn chắc chắn muốn gửi mail mã kích hoạt ?";
    var cert_succ_otp_resend = "Gửi mã kích hoạt thành công";
    var global_error_appove_status = "Trạng thái duyệt yêu cầu không hợp lệ. Duyệt thất bại";
    var global_error_method = "Phương thức không hợp lệ";
    var global_error_keysize_csr = "Độ dài khóa CSR không hợp lệ";
    var global_error_exist_csr = "CSR đã tồn tại trong hệ thống";
    var global_fm_button_renewal = "Thêm mới yêu cầu cấp bù";
    var info_group_info = "Chi tiết yêu cầu";
    var global_group_cert = "Chi tiết chứng thư số";
    var global_fm_Corporation = "Corporation";
    var global_fm_renew_access = "Gói đang áp dụng cho gia hạn chứng thư số";
    var global_fm_renew_access_search = "Tìm theo gói đang áp dụng cho gia hạn chứng thư số";
    var global_fm_csr_info_cts = "Thông tin yêu cầu cấp phát chứng thư số";
    var global_fm_san_info_cts = "Thông tin chứng thư số bổ sung";
    var global_fm_csr_info_cts_before = "Thông tin chứng thư số trước khi thay đổi";
    var global_fm_csr_info_cts_after = "Thông tin chứng thư số sau khi thay đổi";
    var info_fm_profile_name = "Loại chứng thư số";
    var info_fm_type_request = "Loại yêu cầu";
    var info_fm_cert_profile = "Certificate Profile (CORECA)";
    //revoke cert
    var revoke_title_list = "Quản lý thu hồi chứng thư số";
    var revoke_title_detail = "Chi tiết";
    var revoke_title_search = "Tìm kiếm";
    var revoke_title_table = "Danh sách";
    var global_fm_button_revoke = "Thu hồi";
    var global_fm_button_recovery = "Phục hồi";
    var global_fm_button_suspend = "Tạm ngưng";
    var global_fm_button_reissue = "Cấp lại Token";
    var global_fm_button_detail = "Chi tiết";
//    if(IsWhichCA === "18") {
//        global_fm_button_detail = "CHI TIẾT";
//    }
    var global_fm_button_print_report = "In Biểu Mẫu";
    var global_fm_button_print_certificate = "In chứng nhận";
    var global_fm_button_print_handover = "In bàn giao";
    var global_fm_button_print_regis = "In yêu cầu";
    var global_fm_button_print_confirm = "In xác nhận";
    var global_fm_button_print = "In";
    var global_fm_button_export_zip_word = "Zip File Word";
    var global_fm_button_export_zip_pdf = "Zip File PDF";
    var global_fm_button_regis = "Đăng ký";
    var global_fm_button_regis_soft = "Soft Token";
    var global_fm_button_re_regis = "Đăng ký lại";
    var info_group_approve = "Chi tiết duyệt yêu cầu";
    var global_fm_approve = "Duyệt";
    var global_fm_approve_ca = "Phê duyệt cấp CA";
    //report cert
    var certreport_title_list = "Quản lý báo cáo chứng thư số";
    var certreport_title_search = "Tìm kiếm";
    var certreport_title_table = "Danh sách";
    //request -> RegistrationCertificate
    var regiscert_title_list = "Đăng ký chứng thư số";
    var regiscert_title_token_list = "Đăng ký chứng thư số cho Token";
    var regiscert_soft_title_list = "Đăng ký chứng thư số Soft Token";
    var regiscert_title_search = "Tìm kiếm";
    var regiscert_title_table = "Danh sách";
    var regiscert_title_view = "Thông tin đăng ký";
    var buymorecert_title_view = "Mua thêm chứng thư số";
    var regiscert_fm_datelimit_one = "1 năm";
    var regiscert_fm_datelimit_two = "2 năm";
    var regiscert_fm_datelimit_three = "3 năm";
    var regiscert_fm_check_backup_key = "Sao lưu khóa trên Server";
    var regiscert_fm_check_revoke = "Thu hồi chứng thư số sau khi thay đổi thông tin";
    var regiscert_fm_check_revoke_reissue = "Thu hồi chứng thư số sau khi cấp lại";
    var regiscert_fm_check_change_key = "Thay đổi khóa";
    var regiscert_fm_keep_certsn = "Giữ số Sê-ri CTS";
    var regiscert_succ_add = "Đăng ký thành công";
    var regisapprove_title_list = "Yêu cầu đăng ký chứng thư số";
    var regisapprove_title_view = "Duyệt yêu cầu đăng ký chứng thư số";
    var approve_fm_confirm_mail = "Yêu cầu xác nhận qua Email";
//    var regisapprove_error_status = "Existed Token SN In List, Certificate Registration failure";
    var regisapprove_succ_approve = "Duyệt thành công";
    //request -> tokenimport
    var tokenimport_title_list = "Quản lý tải lên danh sách Token";
    var tokenimport_title_import = "Tải lên danh sách Token";
    var tokenimport_title_search = "Tìm kiếm";
    var tokenimport_title_table = "Danh sách";
    var tokenimport_succ_edit = "Tải lên danh sách Token thành công";
    var tokenimport_succ_add_renew = "Tự động gia hạn thành công";
    var tokenimport_fm_fromtokenSN = "Mã Token (Bắt đầu)";
    var tokenimport_fm_totokenSN = "Mã Token (Kết thúc)";
    
    //token -> TokenActionImport
    var actionimport_title_list = "Quản lý tải lên danh sách chỉnh sửa Token";
    var actionimport_title_import = "Tải lên danh sách";
    var actionimport_title_search = "Tìm kiếm";
    var actionimport_title_table = "Danh sách";
    var actionimport_succ_edit = "Tải lên danh sách hoàn tất";
    
    //cert -> certimport
    var certimport_title_list = "Quản lý tải lên danh sách chứng thư số";
    var certimport_title_import = "Tải lên chứng thư số";
    var certimport_file_format_invalid = "Định dạng tệp không đúng. Tải lên thất bại";
    var certimport_fm_error = "Import lỗi: ";
    var certimport_error_not_size = "Số lượng chứng thư số tối đa được phép nhập từ file excel: ";
    var certimport_error_not_ca = "Tài khoản không được phép nhập chứng thư số từ tệp excel";
    var certimport_error_not_format_file = "Hệ thống chỉ hỗ trợ các định dạng: XLS, XLSX, CSV";
    // new before translase
    var tokenimport_title_multi = "Cấu hình nhiều Token";
    var tokenimport_fm_createdate_search = "Thời gian";
    var tokenimport_fm_tokensn_search = "Mã Token";
    var tokenimport_fm_result = "Kết quả đăng tải";
    var tokenimport_fm_createdate_tokensn_search = "Thời gian và Mã Token";
    //Request -> LogtList
    var log_title_list = "Quản lý yêu cầu bị từ chối";
    var log_table_list = "Danh sách";
    var log_title_search = "Tìm kiếm";
    var log_title_view = "Chi tiết yêu cầu";
    var log_fm_user_detete_request = "Tài khoản từ chối yêu cầu";
    var log_fm_date_detete_request = "Thời gian từ chối yêu cầu";
    //Request -> RequestList
    var request_title_list = "Quản lý yêu cầu";
    var request_title_search = "Tìm kiếm";
    var request_table_list = "Danh sách";
    var request_title_view = "Chi tiết";
    var request_conform_delete = "Bạn muốn từ chối yêu cầu này ?";
    var request_conform_revoke = "Bạn muốn thu hồi chứng thư số này ?";
    var request_succ_delete = "Từ chối yêu cầu thành công";
    var request_error_delete = "Yêu cầu đã được duyệt, Từ chối thất bại";
    //token -> backofficelog
    var backoffice_title_list = "Cấu hình Token";
    var backoffice_title_search = "Tìm kiếm";
    var backoffice_title_table = "Danh sách";
    var backoffice_title_view = "Thông tin chi tiết";
    var global_fm_combox_true = "Có";
    var global_fm_combox_false = "Không";
    var global_req_enter_info_change = "Vui lòng chọn ít nhất một thông tin cần thay đổi";
    var global_req_format_url = "Vui lòng nhập đúng định dạng: ";
    var global_error_wrong_agency = "Truy cập bị từ chối đến đại lý này, Vui lòng kiểm tra lại";
    var global_error_wrong_role = "Truy cập bị từ chối đến chức vụ này, Vui lòng kiểm tra lại";
    
    //token -> pushimport
    var pushimport_title_list = "Quản lý tải danh sách thông báo";
    var pushimport_title_import = "Tải lên danh sách thông báo";
    var pushimport_succ_edit = "Tải lên danh sách thông báo hoàn tất";
    var pushimport_succ_conform_down = "Tải kết quả về máy tính ?";
    var pushimport_fm_set_push = "Cập nhật thông báo theo danh sách";
    var pushimport_fm_delete_push = "Xóa thông báo theo danh sách";
    var pushimport_fm_text_push = "Nội dung thông báo";
    var pushimport_fm_link_push = "Link thông báo";
    
    //token -> collectprofile
    var collectimport_title_list = "Quản lý tải danh sách cập nhật đối soát";
    var collectimport_title_import = "Tải lên danh sách cập nhật đối soát";
    var collectimport_fm_set_push = "Cập nhật đã đối soát";
    var collectimport_fm_delete_push = "Cập nhật không đối soát";
    var collectimport_fm_control_cert = "Đối soát chứng thư số";
    var collectimport_fm_control_profile = "Đối soát hồ sơ";
    
    //cert -> ImportDisallowanceList
    var disallowanceimport_title_list = "Quản lý danh sách đen";
    var disallowanceimport_title_import = "Tải danh sách";
    var disallowanceimport_succ_edit = "Tải thành công.";
    var disallowanceimport_succ_conform_down = "Tải kết quả về máy tính?";
    var disallowanceimport_fm_set_push = "Cập nhật theo danh sách";
    var disallowanceimport_fm_delete_push = "Xóa theo danh sách";
    var disallowanceimport_fm_title_blacklist = "Xuất tập tin CSV danh sách đen";
    var disallowanceimport_fm_title_contact = "Xuất tập tin CSV thông tin liên hệ khách hàng";
    var disallowanceimport_fm_contact_email = "Thông tin email";
    var disallowanceimport_fm_contact_phone = "Thông tin số điện thoại";
    var disallowanceimport_fm_note_blacklist = "Ghi chú: xuất file CSV danh sách đen hiện tại có trong hệ thống";
    var disallowanceimport_fm_note_contact = "Ghi chú: xuất file CSV danh sách thông tin liên hệ của khách hàng đăng ký chứng thư số trong hệ thống";
    
    // NO_TRANSLATE
    //Token -> token Approve
    var tokenapprove_title_list = "Quản lý danh sách yêu cầu chỉnh sửa Token";
    var tokenapprove_table_list = "Danh sách yêu cầu chỉnh sửa Token";
    var tokenapprove_title_edit = "Thông tin yêu cầu chỉnh sửa Token";
    //Certificate -> Template DN
    var tempdn_title_list = "Định dạng chứng thư số";
    var tempdn_title_table = "Danh sách trường";
    var tempdn_group_Role = "Chọn loại chứng thư số";
    var tempdn_group_assign = "Danh sách trường chưa gán";
    var tempdn_table_assigned = "Danh sách trường đã gán";
    var tempdn_conform_delete = "Bạn muốn xóa trường này ?";
    var tempdn_succ_delete = "Xóa trường thành công";
    var tempdn_succ_insert = "Thêm mới trường thành công";
    var tempdn_succ_edit = "Cập nhật danh sách trường thành công";
    var tempdn_error_edit = "Danh sách trường không tồn tại";
    var tempdn_error_delete = "Xóa Thành phần DN thất bại";
    var tempdn_error_insert = "Thêm mới trường thất bại";
    var global_fm_certtype = "Loại chứng thư số";
    if(IsWhichCA === "18") {
        global_fm_certtype = "Loại CTS";
    }
    var global_fm_subjectdn = "Trường";
    var global_fm_required = "Bắt buộc";
    var global_fm_prefix = "Tiền tố";
    var global_fm_profile_signature = "Bản ký số";
    var global_fm_profile_scan = "Bản scan";
    var global_fm_profile_paper = "Bản giấy";
    var global_fm_request_function = "Yêu cầu chức năng";
    var token_confirm_cancel_request = "Bạn chắc chắn muốn Từ chối yêu cầu ?";
    var token_confirm_issue_request = "Bạn chắc chắn muốn phát hành chứng thư số này ?";
    var token_succ_cancel_request = "Từ chối yêu cầu thành công";
    var global_fm_button_decline = "Từ chối";
    var global_fm_button_issue = "Cấp CTS";
    var global_fm_status_pendding = "Yêu cầu chờ duyệt";
    var global_fm_status_approved = "yêu cầu đã được duyệt";
    var global_tooltip_decline_request_token = "Yêu cầu đã được duyệt, không thể từ chối";
    var token_group_unlock = "Thông tin mở khóa";
    var global_fm_duration_cts = "Gói dịch vụ";
    var global_fm_duration_cts_choose = "Chọn gói dịch vụ";
    var global_fm_rssp_authmodes = "Chế độ xác thực";
    var global_fm_rssp_signning_profiles = "Gói dịch vụ ký số";
    var global_fm_rssp_replying_party = "Kênh tích hợp";
    var global_fm_percent_cts = "Giá trị hoa hồng";
    var global_fm_rose_type = "Loại hoa hồng";
    var global_fm_rose_type_percen = "Phần trăm (%)";
    var global_fm_rose_type_money = "Số tiền";
    var global_fm_decline_desc = "Lý do từ chối";
    var global_fm_revoke_desc = "Lý do thu hồi (Người dùng)";
    var global_fm_dipose_desc = "Lý do hủy bỏ";
    var global_fm_suspend_desc = "Lý do tạm ngưng";
    var global_fm_revoke_reason_core = "Lý do thu hồi (CoreCA)";
    var global_fm_MNS = "Mã ngân sách";
    var global_fm_HC = "Hộ chiếu";
    if(IsWhichCA === "18") {
        global_fm_HC = "HC";
    }
    var global_fm_CitizenId = "Căn cước công dân";
    var global_fm_requesttype = "Loại yêu cầu";
    var token_fm_choose_noticepush = "Chọn thay đổi hiển thị";
    var token_fm_set_no_noticepush = "Đặt hiển thị mặc định cho Token";
    var token_fm_set_no_dynamic = "Hủy bỏ Menu động cho Token";
    var token_group_choose_dynamic = "Chọn thay đổi Menu động";
    var global_fm_button_renew = "Gia hạn";
    var global_fm_button_buymore = "Mua thêm";
    var global_fm_button_changeinfo = "Thay đổi";
    //Certificate -> RenewCertList
    var certlist_title_list = "Quản lý chứng thư số";
    var certlist_title_search = "Tìm kiếm";
    var certlist_title_table = "Danh sách";
    var certlist_title_renew = "Gia hạn chứng thư số";
    var certlist_title_reissue = "Cấp lại chứng thư số";
    var certlist_title_revoke = "Thu hồi chứng thư số";
    
    //Certificate -> CertificateShareList
    var certsharelist_title_list = "Quản lý bổ sung thông tin dịch vụ";
    var certsharelist_title_search = "Tìm kiếm";
    var certsharelist_title_table = "Danh sách";
    
    var certlist_title_recovery = "Phục hồi chứng thư số";
    var certlist_title_suspend = "Tạm ngưng chứng thư số";
    
    var certlist_group_renew = "Thông tin yêu cầu gia hạn";
    var certlist_group_reissue = "Thông tin yêu cầu cấp lại";
    var certlist_title_changeinfo = "Thay đổi thông tin chứng thư số";
    var certlist_group_changeinfo = "Thay đổi thông tin";
    var certlist_group_sender = "Bên giao";
    var certlist_group_add_info = "Bổ sung thông tin";
    var certlist_group_add_bussiness_info = "Thông tin tổ chức, doanh nghiệp";
    var certlist_group_add_buss_pers_info = "Thông tin tổ chức, doanh nghiệp, cá nhân";
    var certlist_group_return_contact_info = "Thông tin liên hệ trả hồ sơ";
    var certlist_group_add_personal_info = "Thông tin cá nhân";
    var certlist_group_add_bussiness_contact = "Thông tin người liên hệ";
    var certlist_group_receiver = "Bên nhận";
    var certlist_fm_unnamed = "CTS vô danh";
    var certlist_title_detail = "Thông tin chứng thư số";
    var certlist_title_print_hadover = "In Biên Bản Bàn Giao";
    var certlist_title_print_register = "In giấy đăng ký";
    var certlist_title_print_changeinfo = "In giấy thay đổi thông tin";
    var certlist_title_print_reissue_revoke = "In giấy cấp lại và thu hồi";
    var certlist_title_detail_old = "Thông tin chứng thư số cũ";
    var certlist_succ_renew = "Thêm yêu cầu gia hạn chứng thư số thành công";
    var certlist_succ_reissue = "Thêm yêu cầu cấp lại chứng thư số thành công";
    var certlist_succ_revoke = "Thêm yêu cầu thu hồi chứng thư số thành công";
    var certlist_succ_revoke_ca = "Thu hồi chứng thư số thành công";
    var certlist_succ_changeinfo = "Thêm yêu cầu thay đổi thông tin chứng thư số thành công";
    var certlist_succ_changepass_p12 = "Thay đổi mật khẩu P12 thành công";
    var certlist_error_changepass_p12 = "Thay đổi mật khẩu P12 thất bại";
    
    var certlist_succ_recovery = "Thêm yêu cầu phục hồi chứng thư số thành công";
    var certlist_succ_recovery_ca = "Phục hồi chứng thư số thành công";
    var certlist_succ_suspend = "Thêm yêu cầu tạm ngưng chứng thư số thành công";
    var certlist_succ_suspend_ca = "Tạm ngưng chứng thư số thành công";
    var certlist_fm_device_uuid = "UID thiết bị";
    
    //owner -> owner
    var owner_title_list = "Quản lý chủ sở hữu chứng thư số";
    var owner_title_search = "Tìm kiếm";
    var owner_title_table = "Danh sách";
    var owner_title_renew = "Certificate Renewal";
    var owner_title_reissue = "Certificate Reissuance";
    var owner_title_revoke = "Certificate Revocation";
    var owner_title_recovery = "Certificate Recovery";
    var owner_title_suspend = "Certificate Suspend";
    var owner_title_add = "Đăng ký chủ sở hữu";
    var owner_title_view = "Thông tin chủ sở hữu";
    var owner_succ_add = "Yêu cầu đăng ký thành công";
    var owner_succ_edit = "Yêu cầu thay đổi thông tin thành công";
    var ownerapprove_title_list = "Quản trị duyệt chủ sở hữu";
    var owner_fm_type = "Loại chủ sở hữu";
    /// no translase
    var owner_title_dispose = "Hủy bỏ chủ sở hữu";
    var owner_title_change = "Thay đổi thông tin chủ sở hữu";
    var owner_succ_dispose = "Gửi yêu cầu thành công";
    var owner_fm_private_uid = "Thông tin định danh";
    var owner_succ_approve = "Duyệt thành công";
    var owner_title_cert_search = "Tìm kiếm chủ sở hữu";
    
// monitor -> serverlog
    var serverlog_title_list = "Quản lý Log hệ thống";
    var serverlog_title_todate = "Xem thông tin Log hôm nay";
    var serverlog_title_down = "Tải Log hệ thống";
    var serverlog_fm_typelog = "Hệ thống";
    var serverlog_fm_numberlog = "Số dòng";
    var serverlog_fm_timestamp = "Thời gian";
    var serverlog_fm_detail = "Chi tiết Log";
    var hastatus_fm_auto = "Tự động tải lại thông tin (Giây)";
    //
    
    var global_error_promotion_package_limit = "Thời gian khuyến mãi không được lớn hơn thời hạn gói dịch vụ";
    var global_error_amount_package_limit = "Số tiền phí (VNĐ) không được lớn hơn cước gói dịch vụ";
    var global_fm_token_status_configed = "Trạng thái đang cấu hình cho Token";
    var certlist_group_token_new = "Cấu hình Token mới";
    var global_error_noexists_token = "Token không tồn tại trong hệ thống";
    var global_error_token_status = "Trạng thái Token không hợp lệ. Vui lòng kiểm tra lại";
    var global_error_coreca_call_approve = "Có lỗi xảy ra khi gọi qua CoreCA. Vui lòng kiểm tra lại";
    
    var global_error_exists_equals_dn = "Thông tin không bao gồm ký tự dấu =";
    var branch_title_table = "Danh sách";
    var branch_title_info = "Thông tin đại lý";
    var branch_fm_representative = "Người đại diện";
    var branch_fm_representative_position = "Vị trí người đại diện";
    var branch_fm_logo = "Logo đại lý";
    
    // branch access
    var branch_fm_profile_title_access = "Cấu hình API";
    var branch_fm_api_title_access = "Truy cập API";
    var branch_fm_profile_group_profile = "Truy cập gói dịch vụ";
    var branch_fm_profile_group_formfactor = "Truy cập phương thức";
    var branch_fm_api_tag_credential = "Xác thực SOAP-API";
    var branch_fm_rest_tag_credential = "Xác thực REST-API";
    var branch_fm_api_tag_ip = "IP truy cập";
    var branch_fm_api_tag_function = "Hàm truy cập";
    var branch_fm_api_allow_access = "Cho phép cấu hình API";
    var branch_fm_api_signture = "Chữ ký số";
    var branch_fm_api_publishkey = "Public Key";
    var branch_fm_check_reload_cert = "Chọn làm mới cấu hình của gói dịch vụ đã tồn tại";
    
    //token -> tokentransfer
    var tokentransfer_title_list = "Quản lý chuyển Token về đại lý";
    var tokentransfer_title_search = "Tìm kiếm";
    var tokentransfer_title_table = "Danh sách";
    var certprofile_title_search = "Tìm kiếm";
    var certprofile_title_table = "Danh sách";
    var global_error_request_exists = "Đã tồn tại yêu cầu đang chờ xử lý của chứng thư số này. Thêm yêu cầu thất bại";
    var global_error_cert_exists_token = "Token đã tồn tại chứng thư số. Thêm yêu cầu thất bại";
    var global_error_request_exists_token = "Đã tồn tại yêu cầu đang chờ xử lý của Token này. Thêm yêu cầu thất bại";
    var global_error_approve_exists_cert = "Token đã tồn tại chứng thư số. Duyệt thất bại";
    var global_error_credential_external_invalid = "Thông tin xác thực hệ thống bên ngoài không hợp lệ, vui lòng kiểm tra lại.";
    
    var tokenexport_title_list = "Xuất danh sách Token";
    var tokenexport_title_search = "Tìm kiếm";
    var tokenexport_title_table = "Danh sách";

    var global_fm_checkbox_gcndk = "Giấy chứng nhận ĐKDN";
    var global_fm_checkbox_GPDT = "Giấy phép đầu tư";
    var global_fm_checkbox_QDTL = "Quyết định thành lập";
    var global_fm_choise = "Chọn";
    var branch_fm_logo_note = "Ghi chú: nền hình ảnh trong suốt, kích thước (rộng 210px và cao 70px), dung lượng < 500 KB, định dạng file: png, jpg, gif";
    var branch_fm_logo_down = "Tải Logo mẫu";
    var branch_error_logo_great_size = "Vui lòng chọn hình ảnh có dung lượng nhỏ hơn 500 KB";
    var branch_fm_logo_change = "Thay đổi Logo";
    var branch_fm_logo_default = "Cài đặt mặc định";
    
    var global_succ_mst_register = "Mã số thuế đã đăng ký sử dụng chứng thư số trước đó";
    var global_succ_mns_register = "Mã ngân sách đã đăng ký sử dụng chứng thư số trước đó";
    var global_succ_cmnd_register = "Số CMND đã đăng ký sử dụng chứng thư số trước đó";
    var global_succ_hc_register = "Số hộ chiếu đã đăng ký sử dụng chứng thư số trước đó";
    
    var reportquick_fm_innit = "CTS Khởi tạo";
    var reportquick_fm_activation = "CTS Hoạt động";
    var reportquick_fm_revoke = "CTS Thu hồi";
    var reportquick_fm_total = "Tổng số";
    var global_fm_cert_list = "Danh sách chứng thư số ";
    var reportquick_title_list = "Quản lý báo cáo chứng thư số";
    var reportquick_table_search = "Tìm kiếm";
    var reportquick_title_add = "Báo cáo chứng thư số";
    var reportquick_title_edit = "Báo cáo chứng thư số";
    var global_fm_date_approve_agency = "Ngày đại lý duyệt";
    var global_fm_user_approve_agency = "Nhân viên đại lý duyệt";
    var global_fm_date_approve = "Ngày duyệt";
    var global_fm_user_approve = "Nhân viên duyệt";
    var global_fm_date_approve_ca = "Ngày CA duyệt";
    var global_fm_user_approve_ca = "Nhân viên CA duyệt";
    var global_error_not_user_create = "Không tồn tại thông tin người tạo";
    var global_succ_delete = "Xóa thành công";
    var global_error_delete = "Xóa thất bại";
    // File management
    var file_succ_delete = "Xóa file thành công";
    var file_conform_delete = "Bạn muốn xóa file này ?";
    var file_conform_upload = "Bạn muốn đăng tải file này ?";
    
    // NO_TRANSLATE 20180906
    var reportcertlist_title_list = "Báo cáo danh sách chứng thư số";
    var reportcertlist_table_search = "Tìm kiếm";
    
    var reportcertexpire_title_list = "Danh sách chứng thư số hết hạn";
    var reportcertexpire_table_search = "Tìm kiếm";
    
    // NO_TRANSLATE collation
    var collation_title_list = "Quản lý đối soát";
    var collation_fm_collated = "Đã đối soát";
    var collation_fm_uncollated = "Chưa đối soát";
    var collation_button_change = "Đổi trạng thái đổi soát";
    var collation_fm_change = "Đổi trạng thái";
    var collation_button_rose_agent = "Cập nhật hoa hồng";
    var collation_fm_change_change = "Ngày đổi trạng thái đối soát";
    var collation_fm_mounth = "Tháng đối soát";
    var collation_fm_time = "Ngày đối soát";
    var collation_fm_user = "Nhân viên đối soát";
    var collation_fm_date_receipt = "Ngày nhận hồ sơ";
    var collation_fm_type = "Hình thức thu hồ sơ";
    var collation_fm_type_inmounth = "Hồ sơ trong tháng";
    var collation_fm_type_compensation = "Hồ sơ trả bù";
    var collation_fm_date_compensation = "Ngày bù hồ sơ";
    var collation_alert_type_inmounth = "Hồ sơ còn thiếu trong tháng";
    var collation_alert_type_compensation = "Hồ sơ được trả bù trong tháng";
    var collation_fm_profile_overdue = "Hồ sơ quá hạn";
    var collation_fm_unapproved_profile = "Hồ sơ có file chưa đọc";
    var collation_fm_approved_profile = "Hồ sơ đã đọc file mới";
    var collation_fm_money_overdue = "Số tiền phạt quá hạn";
    var collation_fm_print_DK = "Giấy ĐK";
    var collation_fm_print_Confirm = "Giấy xác nhận";
    var collation_fm_print_GPKD = "GPKD";
    var collation_fm_print_CMND = "Giấy CMND";
    
    var profile_title_list = "Quản lý hồ sơ";
    var profile_title_detail = "Chi tiết hồ sơ";
    var profile_title_import_list = "Đăng tải thông tin hồ sơ";
    var profile_fm_enoughed = "Đã đối soát hồ sơ";
    var profile_fm_unenoughed = "Chưa đối soát hồ sơ";
    var profile_conform_update = "Bạn chắc chắn cập nhật trạng thái đủ hồ sơ ?\nHồ sơ sẽ không được cập nhật lần nữa.";
    
    var inputcertlist_title_list = "Nhập liệu đối chiếu công nợ";
    var inputcertlist_table_search = "Tìm kiếm";
    var inputcertlist_succ_edit = "Cập nhật thành công";
    var inputcertlist_succ_add = "Thêm mới thành công";
    var global_fm_monthly = "Kỳ tháng";
    var global_fm_title_push_approve1 = "Có ";
    var global_fm_title_push_approve2 = " yêu cầu chờ cấp phát chứng thư sô";
    var global_fm_title_push_decline = " yêu cầu bị từ chối cấp phát chứng thư số";
    // ICA
    var global_error_revoke_forbiden = "Chứng thư số không thể thu hồi 2 lần liên tục, vui lòng kiểm tra lại";
    var global_error_revoke_limit = "Thu hồi chứng thư số vượt số lần quy định trong tháng, vui lòng liên hệ CA";
    var global_fm_limit_revoke = "Hạn mức thu hồi trong tháng";
    var global_fm_RP_access_esign = "Phân quyền truy xuất kênh tích hợp eSigncloud";
    var global_fm_login_form = "ĐĂNG NHẬP HỆ THỐNG";
    var global_fm_address_GPKD = "Địa chỉ trên ĐKKD";
    var global_fm_CitizenId_I = "CCCD";
    var global_fm_browse_file_upload = "Tối đa ";
    var global_fm_button_add_simple = " Thêm ";
    var global_fm_button_add_action = "THÊM GIAO DỊCH";
    var global_fm_button_print_profile = "IN HỒ SƠ";
    var global_fm_button_off_notice = "Tắt thông báo";
    var global_fm_sign = "Ký";
    var file_conform_signprofile = "Bạn có muốn ký file này không ?";
    var fm_succ_signprofile = "Ký file thành công";
    var global_fm_remark_agency_en = "Tên CTY đại lý (tiếng Anh)";
    var global_fm_remark_agency_vn = "Tên CTY đại lý (tiếng Việt)";
    var global_fm_identifier_type = "Loại định danh";
    if(IsWhichCA !== "18"){
        global_fm_identifier_type = "UID doanh nghiệp";
    }
    var global_fm_document_type = "Loại giấy tờ";
    if(IsWhichCA !== "18") {
        global_fm_document_type = "UID cá nhân";
    }
    var global_fm_enter = "Nhập ";
    var global_fm_enter_number = "Nhập số ";
    var request_conform_approve = "Bạn muốn phê duyệt yêu cầu này ?";
    var certlist_title_detail_current = "Chi tiết chứng thư số hiện thời";
    var global_fm_register_date = "Chọn ngày đăng ký";
    var global_fm_register_info = "Nhận chứng thư số theo thông tin dưới đây";
    
    //request -> historylist
    var history_title_list = "Lịch sử truy cập";
    var history_title_search = "Tìm kiếm";
    var history_title_table = "Danh sách";
    var history_title_detail = "Chi tiết";
    var history_fm_response = "Trạng thái";
    var history_fm_function = "Hàm";
    var history_fm_request_data = "Dữ liệu yêu cầu";
    var history_fm_response_data = "Dữ liệu trả về";
    var history_fm_request_ip = "IP";
    var history_fm_source_entity = "Hệ thống thực hiện";
    
    var reportneac_title_list = "Báo cáo NEAC";
    var reportneac_title_search = "Tìm kiếm";
    var reportneac_title_table = "Danh sách";
    var reportneac_fm_tab_control = "Báo cáo đối soát";
    var reportneac_fm_tab_recurring = "Báo cáo định kỳ";
    var reportneac_fm_tab_cts_signserver = "Chứng thư số SignServer";
    var reportneac_fm_tab_cts_token = "Danh sách chứng thư số";
    var global_fm_cert_count = "Số lượng chứng thư số";
    var reportneac_fm_cert_personal = "Chứng thư số cá nhân";
    var reportneac_fm_cert_enterprise = "Chứng thư số tổ chức";
    var reportneac_fm_cert_staff = "Chứng thư số cá nhân trong tổ chức";
    var reportneac_fm_control_content = "Số lượng chứng thư số do CA công cộng cấp cho thuê bao là tổ chức, doanh nghiệp (không bao gồm cá nhân) từ ngày 01/01/2017 và còn hiệu lực sử dụng đến ngày ";
    var global_fm_report_date = "........., ngày.....tháng.....năm ......";
    var global_fm_report_print_date = "........., ngày [DD] tháng [MM] năm [YYYY]";
    var global_fm_report_print_only_date = "ngày [DD] tháng [MM] năm [YYYY]";
    if(IsWhichCA === "14" || IsWhichCA === "7") {
        global_fm_report_print_date = "Ngày [DD] tháng [MM] năm [YYYY]";
    }
    var global_fm_choose_cert = "Chọn chứng thư số";
    var global_fm_unchoose_cert = "Bỏ chọn chứng thư số";
    var global_fm_login_ssl = "Cơ chế đăng nhập qua thiết bị Token";
    var global_ssl_conform_delete = "Bạn muốn bỏ chọn chứng thư số ?";
    var global_confirm_print_register = "Bạn có muốn in giấy đăng ký ?";
    var global_confirm_print_renew = "Bạn có muốn in giấy gia hạn ?";
    // send mail hsm
    var hsm_confirm_cert_actived = "Chứng thư số đã được kích hoạt";
    var hsm_confirm_data_not_found = "Không tìm thấy thông tin kích hoạt chứng thư số. Vui lòng liên hệ nhà cung cấp dịch vụ";
    var hsm_confirm_url_invalid = "Đường dẫn kích hoạt không hợp lệ";
    var hsm_confirm_time_expire = "Thời gian kích hoạt yêu cầu đã hết. Vui lòng liên hệ nhà cung cấp dịch vụ";
    var hsm_confirm_encryption_notfound = "Không tìm thấy chuỗi mã hóa";
    var hsm_confirm_acteve_status_invalid = "Trạng thái yêu cầu kích hoạt không hợp lệ, vui lòng liên hệ nhà cung cấp dịch vụ";
    var hsm_confirm_cert_issue_error = "Lỗi phát hành chứng thư số, vui lòng liên hệ nhà cung cấp dịch vụ";
    var hsm_confirm_request_declined = "Yêu cầu đã được từ chối kích hoạt chứng thư số.<br />Thời gian: [TIME]. Lý do từ chối: [REASON]";
    var hsm_confirm_request_type_invalid = "Loại yêu cầu không hợp lệ";
    var hsm_confirm_actived_success = "Chứng thư số kích hoạt thành công.\nVui lòng kiểm tra hộp thư E-Mail để nhận chứng thư số";
    var hsm_confirm_declined_success = "Hủy kích hoạt chứng thư số thành công";
    var hsm_confirm_title_page = "Quý khách có yêu cầu kích hoạt cấp chứng thư số HSM, vui lòng kiểm tra thông tin và xác nhận để hệ thống cấp chứng thư số:";
    var hsm_confirm_note_page = "Chú ý: Đồng ý (đồng ý kích hoạt chứng thư số); Từ chối (không đồng ý kích hoạt chứng thư số)";
    var hsm_confirm_not_confirm = "Chưa xác nhận";
    var hsm_confirm_has_confirm = "Đã xác nhận";
    
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
        footer_name = "2018 - {DATE_YEAR} © Công ty cổ phần công nghệ tin học EFY Việt Nam";
        footer_name_inner = "2018 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: Tầng 9 tòa nhà Sannam, số 78 Duy Tân, phường Dịch Vọng Hậu, Cầu Giấy, Hà Nội";
        footer_email = "efy@ihd.vn";
        header_hotline = "1900 6142 - 1900 6139";
        footer_hotline = "1900 6142 - 1900 6139";
    } else if(IsWhichCA === "2") {
        footer_name = "2018 - {DATE_YEAR} © FEITIAN Technologies Co.,Ltd.";
        footer_name_inner = "2018 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: Tower B, Huizhi Mansion, No.9 Xueqing Road, Haidian District, 100085 Beijing, China";
//        footer_email = "email@ihd.vn";
        header_hotline = "+86 10 6230 4466";
        footer_hotline = "+86 10 6230 4466";
    } else if(IsWhichCA === "3") {
        footer_name = "Copyright © 2019 - {DATE_YEAR} Mobile-ID Technologies and Services Joint Stock Company";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: 19 Đặng Tiến Đông, Phường An Phú, Quận 2, TP. Hồ Chí Minh, Việt Nam";
        footer_email = "info@mobile-id.vn";
        header_hotline = "1900 6884";
        footer_hotline = "1900 6884";
    } else if(IsWhichCA === "4") {
        footer_name = "Copyright © 2019 - {DATE_YEAR} MISA JSC";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: Tầng 7 tòa nhà Công đoàn Ngân hàng Việt Nam, Số 6 ngõ 82, phố Dịch Vọng Hậu, Q. Cầu Giấy, Hà Nội";
        footer_email = "fsales@misa.com.vn";
        header_hotline = "1900 8677";
        footer_hotline = "1900 8677";
    } else if(IsWhichCA === "5") {
        footer_name = "Copyright © 2019 - {DATE_YEAR}. All Rights Reserved";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: T9, Tòa nhà Việt Á, số 9 phố Duy Tân, P. Dịch Vọng Hậu, Q. Cầu Giấy, TP. Hà Nội";
        footer_email = "info@savis.com.vn";
        header_hotline = "+(84-24) 3782 2345";
        footer_hotline = "+(84-24) 3782 2345";
    } else if(IsWhichCA === "6") {
        footer_name = "Copyright © 2019 - {DATE_YEAR} NewTel-CA. All Rights Reserved";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: Phòng 305, Toà Nhà GP Invest, Số 170 Đê La Thành, Q.Đống Đa, Hà Nội";
        footer_email = "info@newca.vn";
        header_hotline = "+(84-24) 38374999";
        footer_hotline = "+(84-24) 38374999";
    } else if(IsWhichCA === "7") {
        footer_name = "Copyright © 2019 - {DATE_YEAR} NC-CA. All Rights Reserved";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: Tầng 8 tòa nhà Newhouse Xala, Khu đô thị Xala, Hà Đông, Hà Nội";
        footer_email = "info@nc-ca.com.vn";
        header_hotline = "+(84-24) 6297 1010";
        footer_hotline = "+(84-24) 6297 1010";
    } else if(IsWhichCA === "8") {
        footer_name = "Copyright © 2020 - {DATE_YEAR} Công ty TNHH Tổng công ty Công nghệ và Giải pháp CMC";
        footer_name_inner = "2020 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: Tầng 14-16, Tòa nhà CMC, số 11 phố Duy Tân, quận Cầu Giấy, Hà Nội";
        footer_email = "ca-support@cmc.vn";
        header_hotline = "1900 2323 62";
        footer_hotline = "Hỗ trợ kỹ thuật: 024 3972 2425";
    } else if(IsWhichCA === "9") {
        footer_name = "Copyright © 2020 - {DATE_YEAR} VG-CA";
        footer_name_inner = "2020 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: 23 Ngụy Như Kon Tum, Quận Thanh Xuân, Thanh phố Hà Nội";
        footer_email = "ca@bcy.gov.vn";
        header_hotline = "(+84.24) 37738668";
        footer_hotline = "(+84.24) 37738668";
    } else if(IsWhichCA === "10") {
        footer_name = "Copyright © 2020 - {DATE_YEAR} FPT-CA.COM.VN";
        footer_name_inner = "2020 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: Lầu 6, Tòa nhà Sài Gòn Prime, 107-109-111 Nguyễn Đình Chiểu, P6, Q3, TPHCM";
        footer_email = "kinhdoanh@fpt-ca.com.vn";
        header_hotline = "0911666467";
        footer_hotline = "0911666467";
    } else if(IsWhichCA === "11") {
        footer_name = "Copyright © 2020 - {DATE_YEAR} SoftDreams";
        footer_name_inner = "2020 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: Nhà khách ATS, số 8 Phạm Hùng, Phường Mễ Trì, Quận Nam Từ Liêm, Hà Nội";
        footer_email = "contact@softdreams.vn";
        header_hotline = "1900 56 56 53";
        footer_hotline = "1900 56 56 53";
    } else if(IsWhichCA === "12") {
        footer_name = "Copyright © 2020 - {DATE_YEAR} Công ty TNHH LCS-CA";
        footer_name_inner = "2020 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: 210/16A Cách Mạng Tháng 8, P.10, Q.3, TP Hồ Chí Minh";
        footer_email = "hotro@lcs-ca.vn";
        header_hotline = "1900 4533";
        footer_hotline = "1900 4533";
    } else if(IsWhichCA === "13") {
        footer_name = "Copyright © 2020 - {DATE_YEAR} VIETTELCA.VN";
        footer_name_inner = "2020 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: Số 1 đường Trần Hữu Dực, Phường Mỹ Đình 2, Quận Nam Từ Liêm, Hà Nội";
        footer_email = "lienhe@viettelca.vn";
        header_hotline = "1800 8000";
        footer_hotline = "1800 8000";
    } else if(IsWhichCA === "14") {
        footer_name = "Copyright © 2020 - {DATE_YEAR} CÔNG TY TNHH TƯ VẤN - THƯƠNG MẠI KHÁNH LINH";
        footer_name_inner = "2020 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: 232/17 Cộng Hòa, Phường 12, Quận Tân Bình, Thành phố Hồ Chí Minh";
        footer_email = "info@ketoanvn.com.vn";
        header_hotline = "1900 1129";
        footer_hotline = "1900 1129";
    } else if(IsWhichCA === "15") {
        footer_name = "Copyright © 2018 - {DATE_YEAR} Lao National Root Certificate Authority";
        footer_name_inner = "2018 - {DATE_YEAR} © ";
        footer_address = "Address: Saylom village, Chanthabouli district, Vientiane Capital, Lao PDR";
        footer_email = "lanic_office@lanic.la";
        header_hotline = "+856 254150";
        footer_hotline = "+856 254150, PO Box: 2225";
    } else if(IsWhichCA === "16") {
        footer_name = "Copyright © 2013 - {DATE_YEAR} SAFECert Corp";
        footer_name_inner = "2018 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: X-04.77, Tòa nhà North Towers, Sunrise City, 27 Nguyễn Hữu Thọ, Phường Tân Hưng, Quận 7, TP. Hồ Chí Minh";
        footer_email = "info@safecert.com.vn";
        header_hotline = "(028)-668-23732";
        footer_hotline = "(028)-668-23732";
        global_fm_decision = "Mã đơn vị";
    } else if(IsWhichCA === "17") {
        footer_name = "Copyright © 2018 - {DATE_YEAR} Lao National Root Certificate Authority";
        footer_name_inner = "2018 - {DATE_YEAR} © ";
        footer_address = "Address: Saylom village, Chanthabouli district, Vientiane Capital, Lao PDR";
        footer_email = "lanic_office@lanic.la";
        header_hotline = "+856 254150";
        footer_hotline = "+856 254150, PO Box: 2225";
    } else if(IsWhichCA === "18") {
        footer_name = "CÔNG TY CỔ PHẦN ICORP";
        footer_name_minvoice = "CÔNG TY TNHH HÓA ĐƠN ĐIỆN TỬ M-INVOICE";
        footer_name_inner = "2021 - {DATE_YEAR} © ";
//        footer_office = "VPGD: Tầng 6, Số 82, Phố Trần Thái Tông, Phường Dịch Vọng Hậu, Quận Cầu Giấy, TP Hà Nội";
        footer_office = "VPGD: Phòng 1212 Tháp A, Toà nhà The Park Home, Số 1 Phố Thành Thái, Phường Dịch Vọng, Quận Cầu Giấy, TP Hà Nội";
        footer_address = "Địa chỉ: Số 32/21 Phố Trương Công Giai, Phường Dịch Vọng, Quận Cầu Giấy, TP. Hà Nội";
        footer_address_minvoice = "Địa chỉ: Quận Cầu Giấy, TP Hà Nội";
        footer_email = "ica@icorp.vn";
        footer_email_minvoice = "cskh@minvoice.vn";
        header_hotline = "1900 0099";
        header_hotline_minvoice = "0908111111";
        footer_hotline = "1900 0099";
        footer_hotline_minvoice = "0908111111";
    } else if(IsWhichCA === "19") {
        footer_name = "Copyright © 2019 - {DATE_YEAR} Mobile-ID Technologies and Services Joint Stock Company";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: 19 Đặng Tiến Đông, Phường An Phú, Quận 2, TP. Hồ Chí Minh, Việt Nam";
        footer_email = "info@mobile-id.vn";
        header_hotline = "1900 6884";
        footer_hotline = "1900 6884";
    } else if(IsWhichCA === "20") {
        footer_name = "Copyright © 2019 - {DATE_YEAR} CÔNG TY CP DỊCH VỤ T-VAN HILO";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: Số 2/95 Chùa Bộc, Q.Đống Đa, TP. Hà Nội, Việt Nam";
        footer_email = "support@hilo.com.vn";
        header_hotline = "1900 2929 62";
        footer_hotline = "1900 2929 62";
    } else if(IsWhichCA === "21") {
        footer_name = "Copyright © 2019 - {DATE_YEAR} Trung tâm CNTT MobiFone";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: Số 5, ngõ 82 Duy Tân, Quận Cầu Giấy, TP Hà Nội";
        footer_email = "contact-itc@mobifone.vn";
        header_hotline = "0936 110 116";
        footer_hotline = "0936 110 116";
    } else if(IsWhichCA === "22") {
        footer_name = "Copyright © 2022 - {DATE_YEAR} Mat Bao Company. All Reserved";
        footer_name_inner = "2022 - {DATE_YEAR} © ";
        footer_address = "Địa chỉ: Tầng 3 Anna Building, Công Viên Phần Mềm Quang Trung, Q.12, TP.Hồ Chí Minh";
        footer_email = "info@matbao.com";
        header_hotline = "1900 1830";
        footer_hotline = "1900 1830";
    } else {}
}