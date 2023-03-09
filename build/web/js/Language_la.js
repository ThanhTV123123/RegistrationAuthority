var sVN = "1";
var JS_LANGUAGE_TEMPLATE_UID_PREFIX="1";//1-vn; 0-en; 2-prefix MDV
var global_version_web = "(V2.211220)";
//IsWhichCA: efy-1, feitian-2, mid-3; misa-4, trust-5, newtel-6, ncca-7, cmc-8, bcy-9,
// fpt-10, easy-11, lcs-12, viettel-13, winca-14, laos-lca-15, safeca-16, laos-gov-17, I-CA-18, TCC-19
var IsWhichCA = "15";
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
    //localStorage.setItem("VN", "0");
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
    //localStorage.setItem("VN", "0");
} else if(IsWhichCA === "18") {
    LinkLogoPage = "Images/Logo_ICA.png";
    LinkLogoMenuPage = "Images/LogoMenu_ICA.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/favicon_ICA.ico";
    loadCssCA18();
} else if(IsWhichCA === "19") {
    LinkLogoPage = "Images/Logo_MobileID.png";
    LinkLogoMenuPage = "Images/Logo_MobileID.png";
    LinkBannerPage = "Images/Banner-Company.png";
    LinkIconPageMiltiple = "Images/icon_trusted.png";
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
    var global_fm_phone_authen_rssp = "Signature verification Phone";
    var global_fm_vendor = "SIM Provider";
    var global_fm_display_mess = "Message Content";
    var global_fm_fileid = "File ID";
    var global_error_file_special = "File names are not allowed to contain special characters. Include: /\{};:,\"`~&*|+=%$@<>[]#'^!?";

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
    var global_fm_confirm = "Status confirmed";
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
    var branch_succ_edit = "Updated Agency Successfully";
    var branch_exists_name = "Agency Name is already existed";
    var branch_exists_code = "Agency Code is already existed";
    var branch_fm_name = "Agency Name";
    var branch_fm_code = "Agency Code";
    var branch_fm_parent = "Regional Branch";
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
    var global_fm_decision = "Decision Number";
    var global_fm_share_mode_cert = "Allow additional certificate service information";
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
    var global_fm_soft_copy = "Electronic version";
    if(IsWhichCA === "7") {
        global_fm_soft_copy = "Status of legal files";
    }
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
    var global_button_grid_sendmail = "Send Mail";
    var global_button_p12_sendmail = "P12 Sending";
    var otp_button_grid_unlock = "UnBlock";
    var global_button_grid_enable = "Enable";
    var global_button_grid_disable = "Disable";
    var global_fm_gen_pass = "Generate Password";
    var global_no_data = "No data found !";
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
    var token_fm_validexpire_search = "Effective Time and Expiration Time (Certificate)";
    var token_fm_personal = "Personal Name";
    var token_fm_serialcert = "Certificate SN";
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
    var token_fm_dn = "DN (Distinguished Name)";
    var token_fm_passport = "PID (Personal ID)";
    var token_fm_version = "Token Version";
    var token_fm_agent = "Agency";
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
    var cert_title_register_cert = "Certificate Information";
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
    var global_fm_rssp_authmodes = "Authentization Modes";
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
        global_fm_identifier_type = "Business identification";
    }
    var global_fm_document_type = "Document type";
    if(IsWhichCA !== "18") {
        global_fm_document_type = "Personal identification";
    }
    var global_fm_enter = "Enter ";
    var global_fm_enter_number = "Enter number ";
    var request_conform_approve = "Do You Want To Approve ?";
    var certlist_title_detail_current = "Certificate Current Detail";
    var global_fm_register_date = "Select registration date";
    
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
    var global_fm_choose_cert = "Select Certificate";
    var global_fm_unchoose_cert = "Uncheck Certificate";
    var global_fm_login_ssl = "Login Mechanism Via Token Device";
    var global_ssl_conform_delete = "Do You Want To Uncheck Certificate ?";
    var global_confirm_print_register = "Do you want to print registration form ?";
    var global_confirm_print_renew = "Do you want to print renewal form ?";
    
    // footer page
    var footer_name = "";
    var footer_name_inner = "";
    var footer_address = "";
    var footer_email = "";
    var header_hotline = "";
    var footer_hotline = "";
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
        header_hotline = "1900 2101";
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
        footer_name = "VIETNAM ONLINE ENTERTAINMENT AND COMMUNICATION CO., LTD";
        footer_name_inner = "2021 - {DATE_YEAR} © ";
        footer_address = "Address: No. 32/21, Truong Cong Giai Street, Dich Vong Ward, Cau Giay District, Hanoi City";
        footer_email = "cskh@i-ca.vn";
        header_hotline = "1900 0099";
        footer_hotline = "1900 0099";
    } else if(IsWhichCA === "19") {
        footer_name = "Copyright © 2019 - {DATE_YEAR} Mobile-ID Technologies and Services Joint Stock Company";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Address: 19 Dang Tien Dong, An Phu Ward, District 2nd, Ho Chi Minh City, Vietnam";
        footer_email = "info@mobile-id.vn";
        header_hotline = "1900 6884";
        footer_hotline = "1900 6884";
    } else {}
}
else
{
    var global_title_logo = "ປ້າຍໂຄສະນາການບໍລິຫານ";
	var global_fm_button_New = "ເພີ່ມ";
	var global_fm_button_add = "ສະຫມັກ";
	var global_fm_button_add_print = "ສະໝັກ ແລະ ພິມ";
	var global_fm_button_edit = "ສະຫມັກ";
	var global_fm_button_restart = "ຣີສະຕາດເຊີບເວີ";
	var global_fm_button_back = "ກັບຄືນ";
	var global_fm_button_close = "ປິດ";
	var global_fm_button_search = "ຄົ້ນຫາ";
	var global_fm_button_profile_pettlement = "ການຕັ້ງຄ່າໂປຣໄຟລ";
	var global_fm_button_get_info = "ເອົາຂໍ້ມູນ";
	var global_fm_button_reload = "ໂຫຼດຄືນໃໝ່";
	var global_fm_button_reload_profile = "ໂຫຼດລາຍຊື່ແພັກເກດມາດຕະຖານຄືນໃໝ່";
	var global_fm_button_reload_of_profileaccess = "ໂຫຼດລາຍການຊຸດຫຼ້າສຸດຄືນໃໝ່ຕາມກຸ່ມຂອງການອະນຸຍາດຊຸດບໍລິການ";
	var global_fm_button_export = "ສົ່ງອອກເປັນ CSV";
	var global_fm_button_export_csv = "ສົ່ງອອກເປັນ CSV";
	var global_fm_button_export_word = "ສົ່ງອອກເປັນ ຄຳ";
	var global_fm_button_profile = "ໂປຣໄຟລ໌";
	var global_fm_button_configAPI = "ການຕັ້ງຄ່າ API";
	var global_fm_button_API = "ການເຂົ້າເຖິງ API";
	var global_fm_action = "ການກະທຳ";
	var global_fm_STT = "ບໍ່";
	var global_fm_Username = "ຊື່ຜູ້ໃຊ້";
	var global_fm_Username_esigncloud = "ຊື່ຜູ້ໃຊ້ການລົງນາມທາງໄກ";
	var global_fm_Password = "ລະຫັດຜ່ານ";
	var global_fm_date = "ສ້າງວັນທີ/ວັນທີອັບເດດ";
	var global_fm_date_create = "ສ້າງວັນທີ";
	var global_fm_date_revoke = "ຖອນຄືນວັນທີ";
	var global_fm_date_gencert = "ວັນທີອອກໃຫ້";
	var global_fm_date_cancel = "ຍົກເລີກວັນທີ";
	var global_fm_date_gen = "ສ້າງວັນທີ";
	var global_fm_num_date_cancel = "ຈຳນວນມື້ທີ່ຍົກເລີກ";
	var global_fm_date_request = "ວັນທີຮ້ອງຂໍ";
	var global_fm_date_endupdate = "ວັນທີອັບເດດລ່າສຸດ";
	var global_fm_user_request = "ຜູ້ໃຊ້ຮ້ອງຂໍແລ້ວ";
	var global_fm_user_create = "ສ້າງໂດຍ";
        if(IsWhichCA === "18") {
            global_fm_user_create = "ຜູ້ສ້າງ";
        }
	var global_fm_user_receive = "ຜູ້ໃຊ້ໄດ້ຮັບ";
	var global_fm_user_endupdate = "ຜູ້ໃຊ້ອັບເດດລ່າສຸດ";
	var global_fm_timestamp = "ສ້າງວັນທີ/ວັນທີອັບເດດ";
	var global_fm_times_recovery = "ເວລາກູ້ຄືນ";
	var global_fm_duration = "ໄລຍະເວລາ (ວັນ)";
	var global_fm_active = "ເຄື່ອນໄຫວ";
	var global_fm_all_apply_user = "ນຳໃຊ້ລາຍຊື່ການອະນຸຍາດສຳລັບຜູ້ໃຊ້ທັງໝົດໃນລະບົບພາຍໃຕ້ພາລະບົດບາດນີ້";
	var global_fm_image = "ຮູບພາບ (ໄອຄອນ)";
	var global_fm_required_input = "ຂໍ້ກໍານົດການປ້ອນຂໍ້ມູນ";
	var global_fm_effective = "ມີປະສິດທິພາບ";
    var global_fm_duration_promotion = "ໄລຍະເວລາ + ໂປຣໂມຊັນ (ວັນທີ)";
	var global_fm_address = "ທີ່ຢູ່່";
	var global_fm_fullname = "ຊື່ເຕັມ";
	var global_fm_fax = "ແຟັກ";
	var global_fm_email = "ອີເມວ";
	var global_fm_email_receive = "ອີເມວ (ສໍາລັບການໄດ້ຮັບແຈ້ງເຕືອນພາສີ)";
	var global_fm_email_contact = "ອີເມວຂອງລູກຄ້າ";
	var global_fm_email_authen_rssp = "ອີເມວກວດສອບການຢັ້ງຢືນລາຍເຊັນ";
	var global_fm_option_owner_new = "ສ້າງເຈົ້າຂອງໃໝ່";
	var global_fm_option_owner_search = "ຄົ້ນຫາຜູ້ດູແລລະບົບ";
	var global_fm_email_contact_signserver = "ທີ່ຢູ່ອີເມວຂອງລູກຄ້າເພື່ອສົ່ງໃບຢັ້ງຢືນເຊີບເວີ";
	var global_fm_ip = "ທີ່ຢູ່ IP";
	var global_fm_port = "ພອດ";
	var global_fm_ward = "ທ້ອງທີ່";
	var global_fm_street = "ຖະຫນົນ";
	var global_fm_city = "ແຂວງ/ເມືອງ";
	var global_fm_area = "ພາກພື້ນ";
	var global_fm_Description = "ລາຍລະອຽດ";
	var global_fm_Certificate = "ໃບຢັ້ງຢືນ";
	var global_fm_cert = "ໃບຢັ້ງຢືນ";
	var global_fm_FromDate = "ຈາກວັນທີ";
	var global_fm_year = "ປີ";
	var global_fm_mounth = "ເດືອນ";
	var global_fm_Quater = "ໄຕມາດ";
	var global_fm_Branch = "ໜ່ວຍງານ";
	var global_fm_ToDate = "ເຖິງວັນທີ";
	var global_fm_StatusAccount = "ສະຖານະ";
	var global_fm_combox_all = "ທັງໝົດ";
	var global_fm_combox_empty = "[ຫວ່າງເປົ່າ]";
	var global_fm_combox_choose = "-ກະລຸນາເລືອກ-";
	var global_fm_datatype_label = "ພິມ";
	var global_fm_datatype_numeric = "ອັນຟາ (ຕົວເລກ)";
	var global_fm_datatype_varchar = "ອັກສານ";
	var global_fm_datatype_boolean = "ເລືອກ/ຍົກເລີກການເລືອກ";
	var global_fm_combox_no_choise = "--- ຣູດ ---";
	var global_succ_NoResult = "ບໍ່ພົບ";
	var global_fm_role = "ໜ້າທີ່";
	var global_fm_phone = "ໂທລະສັບ";
	var global_fm_email_manager = "ອີເມວຂອງຕົວແທນ";
	var global_fm_phone_manager = "ໂທລະສັບຂອງຕົວແທນ";
	var global_fm_name_manager = "ຊື່ຕົວແທນ";
	var global_fm_name_contact = "ຊື່ຜູ້ຕິດຕໍ່";
	var global_fm_phone_contact = "ເບີໂທລະສັບຂອງລູກຄ້າ";
	var global_fm_phone_authen_rssp = "ຢືນຢືນລາຍເຊັນໂດຍມືຖື";
	var global_fm_vendor = "ຜູ້ໃຫ້ບໍລິການຊິມ";
	var global_fm_display_mess = "ເນື້ອຫາຂໍ້ຄວາມ";
	var global_fm_fileid = "ລະຫັດໄຟລ໌ ";
	var global_error_file_special = "ຊື່ບໍ່ໄດ້ຮັບອະນຸຍາດໃຫ້ມີຕົວອັກສອນພິເສດ. ປະກອບມີ: /\ {};:, \" `~ &*|+=%$@<> []#'^!?";

	var global_paging_Before = "ກ່ອນ ໜ້ານີ້";
	var global_paging_last = "ສຸດທ້າຍ";
	var global_paging_first = "ທໍາອິດ";
	var global_paging_next = "ຕໍ່ໄປ";

	var global_req_Username = "ນາມສະກຸນ";
	var global_req_Password = "ລະຫັດຜ່ານ";
	var global_req_Description = "ຄຳອະທິບາຍ";
	var global_req_Pem = "ໃບຢັ້ງຢືນ(PEM)";
	var global_req_Certificate = "ໃບຢັ້ງຢືນ";
	var global_req_address = "ທີ່ຢູ່";
	var global_req_ward = "ເຂົ້າ Ward";
	var global_req_street = "ຖະໜົນ";
	var global_req_mail = "ອີເມວ";
	var global_req_mail_format = "ຮູບແບບອີເມວ";
	var global_req_ip_format = "ຮູບແບບ IP";
	var global_req_cer_format = "ຮູບແບບໃບຢັ້ງຢືນ .cer, .txt, .pem";
	var global_req_csr_format = "ປ້ອນໄຟລ format ຮູບແບບທີ່ຖືກຕ້ອງ .csr, .txt";
	var global_req_crl_format = "ປ້ອນໄຟລ format ຮູບແບບທີ່ຖືກຕ້ອງ .crl";
	var global_req_image_format = "ຮູບແບບຂອງຮູບພາບ.jpg,.png";
	var global_fm_active_true = "ການໃຊ້ງານຈິງ";
	var global_fm_active_false = "ການໃຊ້ງານຫຼົ້ມ";
	var global_fm_remark_en = "ລາຍລະອຽດ(ພາສາອັງກິດ)";
	var global_fm_remark_vn = "ລາຍລະອຽດ(ລາວ)";
	var global_fm_amount_fee = "ຄ່າທຳນຽມທັງ(ໝົດ (VND)";
	var global_fm_amount = "ຄ່າທຳນຽມທັງ(ໝົດ(VND)";
	if (IsWhichCA === "15" || IsWhichCA === "17"){
		global_fm_remark_vn = "ລາຍລະອຽດ(ລາວ)";
		global_fm_amount_fee = "ຄ່າທຳນຽມທັງໝົດ";
		global_fm_amount = "ຄ່າທຳນຽມທັງໝົດ";
	}
	var global_fm_activation_code = "ລະຫັດການເປີດນຳໃຊ້";
	var global_fm_activation_date = "ໄລຍະເວລາການເປີດນຳໃຊ້";
	var global_fm_amount_token = "ຈຳນວນToken";

	// ### FORM_FACTOR
	var global_fm_amount_renewal = "ຈໍານວນການຕໍ່ອາຍຸ";
	var global_fm_amount_changeinfo = "ຈໍານວນການປ່ຽນຂໍ້ມູນ";
	var global_fm_amount_reissue = "ຈຳນວນທີ່ອອກໃໝ່";
	var global_fm_amount_goverment = "ຈໍານວນລັດຖະບານ";

	var global_fm_date_free = "ເວລາຫວ່າງ";
	var global_fm_entity_ejbca = "ໜ່ວຍງານ EJBCA";
	var global_fm_choose_csr = "ເລືອກ CSR";
	var global_fm_choose_genkey_server = "ເລືອກລະຫັດເຊີເວີ (P12)";
	var global_fm_choose_genkey_client = "ເລືອກ CSR (ກຸນແຈຖືກສ້າງຈາກລູກຄ້າ)";
	var global_fm_en = "ພາສາອັງກິດ";
	var global_fm_vn = "ພາສາຫວຽດນາມ";
	var global_fm_refresh = "ໂຫຼດໜ້າຄືນໃໝ່";
	var global_fm_properties = "ຄຸນສົມບັດ";
	var global_fm_uuid = "UUID";
	var global_fm_appid_uri = "APPID URI";
	var global_fm_signature_v4 = "ລາຍເຊັນ V.4";
	var global_fm_access_key = "ລະຫັດເຂົ້າເຖິງ";
	var global_fm_secret_key = "ລະຫັດລັບ";
	var global_fm_xapi_key = "X ກະແຈ API";
	var global_fm_regions = "ພາກພື້ນ";
	var global_fm_service = "ບໍລິການ";
	var global_fm_dns_name = "ຊື່ DNS";
	var global_fm_dns_list = "ລາຍຊື່ DNS";
	var global_fm_confirm_customer = "ຂໍ້ມູນການຢືນຢັນຂອງລູກຄ້າ";
	var global_fm_confirm = "ຢືນຢັນ";
	var global_fm_confirm_time = "ຢືນຢັນເວລາ";
	var global_fm_confirm_ip = "ຢືນຢັນ ip";
	var global_fm_confirm_content = "ຢືນຢັນເນື້ອຫາ";
	var global_fm_exists_form = "ຮູບແບບທີ່ມີຢູ່";
	var global_fm_Deposit_form = "ຮູບແບບການຝາກເງິນ";
	var global_fm_use_form = "ຮູບແບບການນໍາໃຊ້";
	var global_fm_end_form = "ຮູບບແບບການສິ້ນສຸດ";
	var global_fm_form = "ແບບຟອມ";
	var global_fm_uri = "URI";
	var global_fm_url_callback = "ການໂທກັບຄືນ URL";
	var global_req_format_http = "ປ້ອນຮູບແບບການເຊື່ອມຕໍ່ທີ່ຖືກຕ້ອງ";
	var global_fm_Function = "ການເຮັດວຽກ";
	var global_fm_MetaData = "ຂໍ້ມູນ Meta";
	var global_fm_billcode = "ລະຫັດໃບບິນ";
	var global_fm_Function_tran = "ການທໍາງານ";
	var global_succ_NoCheck = "ກະລຸນາເລືອກລາຍການທີ່ຕ້ອງເຮັດ";
	var global_succ_NoCheck_setup = "ກະລຸນາເລືອກລາຍການທີ່ຈະຕັ້ງ";
	var global_fm_import_choise_text = "ນໍາເຂົ້າຮູບແບບຂໍ້ຄວາມ";
	var global_fm_import_choise_image = "ນໍາເຂົ້າຮູບພາບ";
	var global_req_text_format = "text ຮູບແບບຂໍ້ຄວາມ.txt, .pem";
	
	// ການຕັ້ງຄ່າ -> ສາຂາ
	var branch_title_list = "ລາຍຊື່ຫົວຂໍ້";
	var branch_table_list = "ຕາຕະລາງລາຍຊື່";
	var branch_title_add = "ເພີ່ມຫົວຂໍ້";
	var branch_title_edit = "ແກ້ໄຂຫົວຂໍ້";
	var branch_req_name = "ໃສ່ຊື່ຕົວແທນ";
	var branch_req_code = "ໃສ່ລະຫັດຕົວແທນ";
	var branch_succ_add = "ເພີ່ມສໍາເລັດແລ້ວ";
	var branch_succ_edit = "ແກ້ໄຂສໍາເລັດແລ້ວ";
	var branch_exists_name = "ຊື່ທີ່ມີຢູ່";
	var branch_exists_code = "ລະຫັດອົງການທີ່ມີຢູ່ແລ້ວ";
	var branch_fm_name = "ຊື່";
	var branch_fm_code = "ລະຫັດ";
	var branch_fm_parent = "ພໍ່ແມ່";
	var branch_req_area_change = "ປ່ຽນພຶ້ນທີ່";
	var branch_conform_delete = "ຢືນຢັນການລົບ?";
	var branch_succ_delete = "ລຶບສໍາເລັດ";
	var branch_exists_user_delete = "ລົບຜູ້ໃຊ້ທີ່ມີຢູ່";
	var branch_conform_default = "ຢືນຢັນຄ່າເລີ່ມຕົ້ນ?";
	// ໃຫມ່
	var branch_fm_choise_new = "ທາງເລືອກໃໝ່";
	var branch_fm_choise_CN = "ທາງເລືອກ CN";
	var branch_fm_choise_PGD = "ທາງເລືອກ PGD";
	var branch_fm_access_profile = "ການເຂົ້າເຖິງໂປຣໄຟລ";

	//report -> synchneac
    var synchneac_title_list = "ການຈັດການ synchronous NEAC";
    var synchneac_table_list = "ລາຍການ";
    var synchneac_title_edit = "ຂໍ້ມູນໃບຢັ້ງຢືນ";
    var synchneac_succ_edit = "ການຊິ້ງຂໍ້ມູນທີ່ສຳເລັດ";
    var synchneac_conform_update_multi = "ທ່ານຕ້ອງການກຳນົດຄ່າຂໍ້ມູນຫຼາຍບໍ?";
    var synchneac_conform_decline_multi = "ທ່ານຕ້ອງການປະຕິເສດຂໍ້ມູນຫຼາຍບໍ?";
    var synchneac_conform_synch_multi = "ທ່ານຕ້ອງການ synchronize ຂໍ້ມູນຫຼາຍບໍ?";
    var synchneac_fm_remaining = "ຈໍານວນຂໍ້ຜິດພາດຂອງ synchronization";
    var synchneac_fm_synch_auto = "ເປີດໃຊ້ງານ synchronization ອັດຕະໂນມັດເປັນ NEAC";
    
//Configuration -> channel
    var city_title_list = "ການກຳນົດຄ່າແຂວງ/ເມືອງ";
    var city_table_list = "ລາຍຊື່ແຂວງ/ເມືອງ";
    var city_table_search = "ແຂວງ/ເມືອງຊອກຫາ";
    var city_title_add = "ເພີ່ມແຂວງ/ເມືອງ";
    var city_title_edit = "ກຳນົດຄ່າແຂວງ/ເມືອງ";
    var city_req_name = "ໃສ່ຊື່ແຂວງ/ເມືອງ";
    var city_req_code = "ໃສ່ລະຫັດແຂວງ/ເມືອງ";
    var city_succ_add = "ເຂົ້າແຂວງ/ເມືອງສຳເລັດ";
    var city_exists_code = "ລະຫັດແຂວງ/ເມືອງມີຢູ່ແລ້ວ";
    var city_exists_name = "ຊື່ແຂວງ/ເມືອງມີຢູ່ແລ້ວ";
    var city_succ_edit = "ກຳນົດຄ່າແຂວງ/ເມືອງສຳເລັດແລ້ວ";
    var city_fm_code = "ລະຫັດແຂວງ/ເມືອງ";
    var city_fm_name = "ຊື່ແຂວງ/ເມືອງ";
	
	// ທົ່ວໄປ -> CertificateTypeList
	var certtype_title_list = "ການຈັດການປະເພດໃບຢັ້ງຢືນ";
	var certtype_table_list = "ລາຍການປະເພດໃບຢັ້ງຢືນ";
	var certtype_title_add = "ເພີ່ມປະເພດໃບຢັ້ງຢືນ";
	var certtype_title_edit = "ກໍານົດປະເພດໃບຢັ້ງຢືນ";
	var certtype_exists_code = "ລະຫັດປະເພດໃບຢັ້ງຢືນມີຢູ່ແລ້ວ";
	var certtype_fm_code = "ລະຫັດປະເພດໃບຢັ້ງຢືນ";
	var certtype_succ_add = "ເພີ່ມປະເພດໃບຢັ້ງຢືນ ສຳເລັດແລ້ວ";
	var certtype_succ_edit = "ກຳນົດຄ່າປະເພດໃບຢັ້ງຢືນ ສຳເລັດແລ້ວ";
	var certtype_group_file_profile = "ຕັ້ງຄ່າໂປຣໄຟລ ment ແນບ";
	var certtype_component_attributetype = "ປະເພດຄຸນລັກສະນະ";
	var certtype_component_cntype = "ປະເພດຊື່ສາມັນ";
	var certtype_component_field_code = "ລະຫັດພາກສະຫນາມ";
	var certtype_component_field_code_exists = "ລະຫັດຊ່ອງຂໍ້ມູນມີຢູ່ແລ້ວ";
	var certtype_file_code_exists = "ລະຫັດປະເພດໄຟລມີຢູ່ແລ້ວ";
	var certtype_file_code = "ລະຫັດປະເພດໄຟລ";
	var certtype_fm_file = "ປະເພດໄຟລ";
	var certtype_fm_component_text = "ໃສ່ ຄຳ ວ່າ";
	var certtype_fm_component_uuid_company = "ເລືອກ UID ວິສາຫະກິດ";
	var certtype_fm_component_uuid_personal = "ເລືອກ UID ສ່ວນຕົວ";
	var certtype_fm_component_uuid_company_require = "UID ວິສາຫະກິດທີ່ຕ້ອງການ";
	var certtype_fm_component_uuid_personal_require = "UID ສ່ວນຕົວທີ່ຕ້ອງການ";
	
	// ທົ່ວໄປ -> ລະຫັດການຕອບໂຕ້
	var response_title_list = "ການຕັ້ງຄ່າສະຖານະການເຮັດທຸລະກໍາ";
	var response_table_list = "ລາຍການສະຖານະການເຮັດທຸລະກຳ";
	var response_title_add = "ເພີ່ມສະຖານະທຸລະກຳ";
	var response_title_edit = "ຕັ້ງຄ່າສະຖານະການເຮັດທຸລະກໍາ";
	var response_succ_add = "ເພີ່ມສະຖານະການເຮັດທຸລະ ກຳສຳເລັດ";
	var response_exists_code = "ລະຫັດສະຖານະການເຮັດທຸລະ ກຳມີຢູ່ແລ້ວ";
	var response_exists_name = "ຊື່ສະຖານະການເຮັດທຸລະ ກຳ ມີຢູ່ແລ້ວ";
	var response_succ_edit = "ຕັ້ງຄ່າສະຖານະການເຮັດທຸລະ ກຳ ສຳ ເລັດ";
	var response_fm_code = "ລະຫັດສະຖານະການເຮັດທຸລະ ກຳ";
	var response_fm_name = "ຊື່ສະຖານະການເຮັດທຸລະ ກຳ";
	// ທົ່ວໄປ -> MNO
	var mno_title_list = "ການຈັດການ MNO";
	var mno_table_list = "ລາຍການ MNO";
	var mno_title_add = "ເພີ່ມ MNO ໃໝ່";
	var mno_title_edit = "ຕັ້ງຄ່າ MNO";
	var mno_succ_add = "ເພີ່ມ MNO ສຳ ເລັດແລ້ວ";
	var mno_exists_code = "ລະຫັດ MNO ມີຢູ່ແລ້ວ";
	var mno_exists_name = "ຊື່ MNO ມີຢູ່ແລ້ວ";
	var mno_succ_edit = "ຕັ້ງຄ່າ MNO ສຳ ເລັດແລ້ວ";
	var mno_fm_code = "ລະຫັດ MNO";
	// ທົ່ວໄປ -> InternalEntity
	var interentity_title_list = "ການຈັດການ ໜ່ວຍ ງານພາຍໃນ";
	var interentity_table_list = "ລາຍການ ໜ່ວຍ ງານພາຍໃນ";
	var interentity_title_add = "ເພີ່ມຫົວ ໜ່ວຍ ພາຍໃນ";
	var interentity_title_edit = "ຕັ້ງຄ່າ ໜ່ວຍ ງານພາຍໃນ";
	var interentity_succ_add = "ເພີ່ມຫົວ ໜ່ວຍ ພາຍໃນ ສຳ ເລັດ";
	var interentity_exists_code = "ລະຫັດນິຕິບຸກຄົນພາຍໃນມີຢູ່ແລ້ວ";
	var interentity_succ_edit = "ຕັ້ງຄ່າ ໜ່ວຍ ງານພາຍໃນສໍາເລັດ";
	var interentity_fm_code = "ລະຫັດນິຕິບຸກຄົນພາຍໃນ";
	// ທົ່ວໄປ -> ExternalEntity
	var exterentity_title_list = "ການຈັດການ ໜ່ວຍ ງານພາຍນອກ";
	var exterentity_table_list = "ລາຍການນິຕິບຸກຄົນພາຍນອກ";
	var exterentity_title_add = "ເພີ່ມນິຕິບຸກຄົນພາຍນອກ";
	var exterentity_title_edit = "ຕັ້ງຄ່າ ໜ່ວຍ ງານພາຍນອກ";
	var exterentity_succ_add = "ເພີ່ມ ໜ່ວຍ ງານພາຍນອກສໍາເລັດ";
	var exterentity_exists_code = "ລະຫັດນິຕິບຸກຄົນພາຍນອກມີຢູ່ແລ້ວ";
	var exterentity_succ_edit = "ຕັ້ງຄ່າ ໜ່ວຍ ງານພາຍນອກສໍາເລັດ";
	var exterentity_fm_code = "ລະຫັດນິຕິບຸກຄົນພາຍນອກ";
        
        var global_error_delete_ip = "ລຶບລົ້ມເຫລວ, ກະລຸນາກວດເບິ່ງອີກຄັ້ງ";
        var Relyparty_all_add_ip = "ມີການເຂົ້າເຖິງ IP ທັງໝົດ, ເພີ່ມຄວາມລົ້ມເຫຼວຂອງ IP";
        var Relyparty_error_delete_function = "ລຶບຫນ້າທີ່ລົ້ມເຫລວ, ກະລຸນາກວດເບິ່ງອີກຄັ້ງ";
        var global_exists_add_function = "ລະຫັດຟັງຊັນມີຢູ່ແລ້ວ";
        var global_exists_add_metadata = "AAID ມີຢູ່ແລ້ວ";
        var global_exists_add_facet = "ລະຫັດຂໍ້ມູນລູກຄ້າມີຢູ່ແລ້ວ";
        var Relyparty_all_add_function = "ມີການເຂົ້າເຖິງທຸກຟັງຊັນ, ເພີ່ມຟັງຊັນລົ້ມເຫລວ";
        var referparty_req_add_function = "ກະລຸນາໃສ່ລາຍການຟັງຊັນ";
        var global_req_add_ip = "ກະລຸນາໃສ່ລາຍການ IP";
        var global_conform_delete_function = "ທ່ານຕ້ອງການລຶບຟັງຊັນນີ້ບໍ?";
        var global_conform_delete_ip = "ທ່ານຕ້ອງການລຶບ IP ນີ້ບໍ?";
        var global_conform_delete_metadata = "ທ່ານຕ້ອງການລຶບ MetaData ນີ້ບໍ?";
        var global_succ_enabled_function = "ການຕັ້ງຄ່າການເຄື່ອນໄຫວຂອງຟັງຊັນສຳເລັດ";
        var global_succ_enabled_ip = "ການກຳນົດຄ່າເຄື່ອນໄຫວຂອງ IP ສຳເລັດ";
        var global_succ_enabled_metadata = "ການຕັ້ງຄ່າ Active ຂອງ MetaData ສຳເລັດ";
        var global_succ_enabled_facet = "ການຕັ້ງຄ່າ Active ຂອງຂໍ້ມູນລູກຄ້າສຳເລັດ";
        var global_conform_delete_soap = "ທ່ານຕ້ອງການລຶບຄຸນສົມບັດນີ້ບໍ?";
        var global_conform_delete_restful = "ທ່ານຕ້ອງການລຶບຄຸນສົມບັດ Restful ນີ້ບໍ?";
        var global_succ_delete_soap = "ລຶບຄຸນສົມບັດສຳເລັດ";
        var global_succ_delete_restful = "ລຶບຄຸນສົມບັດ Restful ສຳເລັດ";
        var global_succ_edit_soap = "ການກຳນົດຄ່າຄຸນສົມບັດ CA ສຳເລັດແລ້ວ";
        var global_succ_edit_restful = "ການກຳນົດຄ່າ Restful Properties ສຳເລັດແລ້ວ";
        var global_succ_add_soap = "ເພີ່ມຄຸນສົມບັດ CA ສຳເລັດແລ້ວ";
        var global_succ_add_restful = "ເພີ່ມຄຸນສົມບັດການພັກຜ່ອນຢ່າງສຳເລັດຜົນ";
        var global_fm_restful = "ຄຸນສົມບັດທີ່ພັກຜ່ອນ";
        var global_fm_soap = "ຄຸນສົມບັດ" ;
        var global_title_soap_edit = "ຄຸນສົມບັດ CA ການຕັ້ງຄ່າ";
        var global_title_restful_edit = "ຄຸນສົມບັດ RestFul ການຕັ້ງຄ່າ";
        var global_title_soap_add = "ເພີ່ມຄຸນສົມບັດຂອງສະບູ";
        var global_title_propeties_ca_add = "ເພີ່ມຄຸນສົມບັດ CA";
        var global_title_restful_add = "ເພີ່ມຄຸນສົມບັດ RestFul";
        var global_fm_facet = "ຂໍ້ມູນລູກຄ້າ" ;
        var global_fm_status_expire = "ໝົດອາຍຸ";
        var global_fm_not_blank = "ບໍ່ຫວ່າງ";
        
	// ລາຍງານ -> synchneac
	var synchneac_title_list = "ການຈັດການ NEACປະສານງານກັນ ";
	var synchneac_table_list = "ລາຍຊື່";
	var synchneac_title_edit = "ຂໍ້ມູນໃບຢັ້ງຢືນ";
	var synchneac_succ_edit = "ການຊິ້ງຂໍ້ມູນສໍາເລັດ";
	var synchneac_conform_update_multi = "ເຈົ້າຕ້ອງການກຳນົດຄ່າຂໍ້ມູນບໍ່?";
	var synchneac_conform_synch_multi = "ເຈົ້າຕ້ອງການຊິ້ງຂໍ້ມູນບໍ່?";
	var synchneac_fm_remaining = "ຈໍານວນຂອງຄວາມຜິດພາດການຊິ້ງຂໍ້ມູນ";
	// ການຕັ້ງຄ່າ -> ຊ່ອງທາງ
	var city_title_list = "ການຕັ້ງຄ່າແຂວງ/ເມືອງ";
	var city_table_list = "ລາຍຊື່ແຂວງ/ເມືອງ";
	var city_table_search = "ຄົ້ນຫາແຂວງ/ເມືອງ";
	var city_title_add = "ເພີ່ມແຂວງ/ເມືອງ";
	var city_title_edit = "ຕັ້ງຄ່າແຂວງ/ເມືອງ";
	var city_req_name = "ໃສ່ຊື່ແຂວງ/ຊື່ເມືອງ";
	var city_req_code = "ໃສ່ລະຫັດແຂວງ/ເມືອງ";
	var city_succ_add = "ເຂົ້າສູ່ແຂວງ/ເມືອງສຳເລັດ";
	var city_exists_code = "ລະຫັດແຂວງ/ເມືອງມີຢູ່ແລ້ວ";
	var city_exists_name = "ແຂວງ/ຊື່ເມືອງມີຢູ່ແລ້ວ";
	var city_succ_edit = "ຕັ້ງຄ່າແຂວງ/ເມືອງ ສໍາເລັດ";
	var city_fm_code = "ແຂວງ/ລະຫັດເມືອງ";
	var city_fm_name = "ແຂວງ/ຊື່ເມືອງ";
	// ທົ່ວໄປ -> ການທໍາງານ
	var function_title_list = "ການຕັ້ງຄ່າຟັງຊັນ";
	var function_table_list = "ລາຍການຟັງຊັນ";
	var function_title_add = "ເພີ່ມຟັງຊັນ";
	var function_title_edit = "ຕັ້ງຄ່າ ໜ້າ ທີ່";
	var function_succ_add = "ເພີ່ມ ໜ້າ ທີ່ໃຫ້ ສຳ ເລັດ";
	var function_exists_code = "ລະຫັດຟັງຊັນມີຢູ່ແລ້ວ";
	var function_exists_name = "ຊື່ ໜ້າ ທີ່ມີຢູ່ແລ້ວ";
	var function_succ_edit = "ຕັ້ງຄ່າຟັງຊັນໃຫ້ ສຳ ເລັດ";
	var function_fm_code = "ລະຫັດຟັງຊັນ";
	var function_fm_name = "ຊື່ ໜ້າ ທີ່";
	// ທົ່ວໄປ -> CA
	var ca_title_list = "ການຈັດການ CA";
	var ca_table_list = "ລາຍຊື່ CA";
	var ca_title_add = "ເພີ່ມ CA ໃໝ່";
	var ca_title_edit = "ຕັ້ງຄ່າ CA";
	var ca_succ_add = "ເພີ່ມ CA ສຳເລັດແລ້ວ";
	var ca_exists_code = "ລະຫັດ CA ມີຢູ່ແລ້ວ";
	var ca_exists_name = "ຊື່ CA ມີຢູ່ແລ້ວ";
	var ca_succ_edit = "ຕັ້ງຄ່າ CA ສຳເລັດແລ້ວ";
	var ca_fm_short = "ລະຫັດສັ້ນ";
	var ca_fm_code = "ລະຫັດ CA";
	var ca_fm_name = "ຊື່ CA";
	var ca_fm_OCSP = "URL ຂອງ OCSP";
	var ca_fm_CRL = "CRL URL";
	var ca_fm_CRLPath = "ຊື່ CRL";
	var ca_fm_URI = "URI";
	var ca_fm_Cert_01 = "ໃບຢັ້ງຢືນ";
	var ca_fm_CheckOCSP = "ເລືອກໂດຍ OCSP";
	var ca_fm_unique_DN = "ອະນຸຍາດໃຫ້ວິຊາໃບຢັ້ງຢືນຊໍ້າກັນ";
	var ca_group_CRLFile_1 = "ໄຟລ C CRL";
	var ca_error_valid_cert_01 = "ໃບຮັບຮອງບໍ່ຖືກຕ້ອງ";
	var ca_error_valid_cert_expire_01 = "ເວລາທີ່ມີຜົນບັງຄັບໃຊ້ຂອງໃບຢັ້ງຢືນໝົດອາຍຸແລ້ວ";
	var ca_succ_import_crl1 = "ຕັ້ງຄ່າເອກະສານ CRL 1 ສຳເລັດ";
	var ca_error_import_crl1 = "ຕັ້ງຄ່າໄຟລ  CRL1 ລົ້ມເຫຼວ";
	var ca_group_cert = "ລາຍລະອຽດໃບຢັ້ງຢືນ";
	var ca_req_info_cert = "ບໍ່ພົບໃບຢັ້ງຢືນ";
	var ca_succ_reload = "ໂຫລດ CRL ຄືນໃໝ່ສໍາເລັດ";
	var ca_error_reload = "ໂຫລດ CRL ລົ້ມເຫລວຄືນໃໝ່";
	// ທົ່ວໄປ -> ປະຫວັດໃບຢັ້ງຢືນ
	var certprofile_title_list = "ການຈັດການຊຸດບໍລິການ";
	var certprofile_table_list = "ລາຍການຊຸດບໍລິການ";
	var certprofile_title_add = "ເພີ່ມແພັກເກດບໍລິການ";
	var certprofile_title_edit = "ຕັ້ງຄ່າແພັກເກດບໍລິການ";
	var certprofile_succ_add = "ເພີ່ມແພັກເກດບໍລິການສຳເລັດ";
	var certprofile_exists_code = "ລະຫັດການບໍລິການມີຢູ່ແລ້ວ";
	var certprofile_exists_name = "ຊື່ບໍລິການມີຢູ່ແລ້ວ";
	var certprofile_succ_edit = "ຕັ້ງຄ່າແພັກເກດບໍລິການ ສຳເລັດແລ້ວ";
	var certprofile_fm_code = "ລະຫັດແພັກເກດການບໍລິການ";
	var certprofile_fm_service_type = "ປະເພດການບໍລິການ";
	var certprofile_fm_service_issue = "ບັນຫາ";
	var certprofile_fm_service_renew = "ຕໍ່ອາຍຸ";
	// admin -> confignamil
	var email_title_list = "ການຕັ້ງຄ່າອີເມລ";
	var email_req_smtp = "ໃສ່ SMTP Server";
	var email_req_port = "ເຂົ້າທີ່ Port";
	var email_succ_edit = "ຕັ້ງຄ່າອີເມລສໍາເລັດ";
	var email_fm_port = "ພອດ";
	var email_fm_smtp = "ເຊີບເວີ SMTP";
	// admin -> ຈັດການນະໂຍບາຍ
	var policy_title_list = "ການຕັ້ງຄ່າທົ່ວໄປ";
	var policy_title_list_client = "ພາຣາມິເຕີ";
	var policy_title_list_client_fo = "ການຈັດການລູກຄ້າ";
	var policy_title_list_client_bo = "ການຈັດການການບໍລິຫານ";
	var policy_group_notification = "ຕັ້ງຄ່າການແຈ້ງເຕືອນເລີ່ມຕົ້ນ";
	var policy_succ_edit = "ຕັ້ງຄ່າຂໍ້ມູນທົ່ວໄປ ສຳ ເລັດ";
	var policy_req_empty = "ປ້ອນ" + "";
	var policy_req_empty_choose = "ເລືອກ" + "";
	var policy_req_number = "" + "ພຽງແຕ່ປະກອບມີຕົວອັກສອນບໍ່ມີເພດ; (ຕົວເລກ)";
	var policy_req_unicode = "" + "ບໍ່ມີຕົວອັກສອນເນັ້ນ";
	// admin -> ConfigPolicy
	var policy_config_title_list = "ການຕັ້ງຄ່າຊ່ອງຂໍ້ມູນພາຣາມິເຕີ";
	var policy_config_table_list = "ລາຍການຊ່ອງຂໍ້ມູນພາຣາມິເຕີ";
	var policy_title_edit = "ຕັ້ງຄ່າພາກສະ ໜາມ ພາຣາມິເຕີ";
	var policy_succ_add = "ເພີ່ມຊ່ອງຂໍ້ມູນພາຣາມິເຕີ ສຳ ເລັດ";
	var policy_fm_fo = "ລູກຄ້າ";
	var policy_fm_bo = "ຫ້ອງການກັບຄືນ";
	var policy_fm_group_fo_bo = "ພາຣາມິເຕີ ສຳ ລັບໂມດູນ";
	var policy_title_add = "ເພີ່ມເຂດພາຣາມິເຕີ";
	var policy_fm_code = "ລະຫັດຊ່ອງຂໍ້ມູນພາຣາມິເຕີ";
	var policy_exists_code = "ລະຫັດຊ່ອງຂໍ້ມູນພາຣາມິເຕີມີຢູ່ແລ້ວ";

	// admin -> menulink
	var menu_title_list = "ການຕັ້ງຄ່າລິ້ງເມນູ";
	var menu_title_table = "ລິ້ງເມນູ";
	var menu_group_Role = "ເລືອກບົດບາດ";
	var menu_fm_Role = "ບົດບາດ";
	var menu_group_assign = "ເມນູບໍ່ໄດ້ມອບໝາຍໃຫ້";
	var menu_fm_assign = "ຊື່ເມນູ";
	var menu_fm_parent_name = "ຊື່ເມນູການຈັດການ";
	var menu_fm_url = "ເສັ້ນທາງເມນູ";
	var menu_table_assigned = "ເມນູທີ່ມອບໝາຍ";
	var menu_conform_delete = "ເຈົ້າຕ້ອງການລຶບເມນູນີ້ບໍ?";
	var menu_succ_delete = "ລຶບເມນູ ສຳເລັດ";
	var menu_succ_insert = "ເພີ່ມເມນູ ສຳເລັດ";
	var menu_error_delete = "ລຶບເມນູລົ້ມເຫຼວ";
	var menu_error_insert = "ເພີ່ມເມນູລົ້ມເຫຼວ";
	var menu_fm_button_assign = "ມອບໝາຍ";

	// ທົ່ວໄປ -> methodprofile
	var methodprofile_title_list = "ແບ່ງຂັ້ນຕອນວິທີການເຂົ້າຫາຊຸດບໍລິການ";
	var methodprofile_title_table = "ວິທີການແບ່ງຂັ້ນຄຸ້ມຄອງ";
	var methodprofile_group_formfactor = "ເລືອກວິທີ";
	var methodprofile_fm_Role = "ວິທີການ";
	var methodprofile_group_assign = "ໂປຣໄຟລ ບໍ່ໄດ້ມອບໝາຍ";
	var methodprofile_fm_profile = "ໂປຣໄຟລ";
	var methodprofile_table_assigned = "ໂປຣໄຟລ ທີ່ມອບໝາຍ";
	var methodprofile_conform_delete = "ເຈົ້າຕ້ອງການລຶບໂປຣໄຟລ ນີ້ອອກບໍ?";
	var methodprofile_succ_delete = "ລຶບ ສຳເລັດ";
	var methodprofile_succ_insert = "ເພີ່ມ ສຳເລັດ";
	var methodprofile_error_delete = "ລຶບຄວາມລົ້ມເຫລວ";
	var methodprofile_error_insert = "ເພີ່ມຄວາມລົ້ມເຫຼວ";

	// admin -> ບົດບາດຜູ້ໃຊ້
	var role_title_list = "ການຕັ້ງຄ່າຜູ້ໃຊ້";
	var role_title_table = "ຜູ້ໃຊ້";
	var role_title_edit = "ກຳນົດດຜູ້ໃຊ້";
	var role_title_add = "ເພີ່ມຜູ້ໃຊ້";
	var role_group_Role = "ເລືອກກຸ່ມຜູ້ໃຊ້";
	var role_fm_code = "ລະຫັດຜູ້ໃຊ້";
	var role_fm_is_ca = "ຕຳແໜ່ງສຳລັບ CA";
	var role_fm_is_agent = "ຕຳແໜ່ງອົງການ";
	var role_fm_name = "ຊື່ຂອງຜູ້ໃຊ້";
	var role_succ_add = "ເພີ່ມຜູ້ໃຊ້ໃຫ້ສຳເລັດ";
	var role_succ_edit = "ຕັ້ງຄ່າຜູ້ໃຊ້ໃຫ້ສຳເລັດ";
	var role_exists_code = "ລະຫັດຜູ້ໃຊ້ມີຢູ່ແລ້ວ";
	var role_exists_name = "ຊື່ຂອງຜູ້ໃຊ້ມີຢູ່ແລ້ວ";
	var role_noexists_functions = "ເລືອກຢ່າງນ້ອຍໜຶ່ງອັນ";
	var role_fm_function_name = "ຊື່ໜ້າທີ່";
	// ການເຂົ້າເຖິງການທໍາງານ
	var funrole_fm_islock = "ຕັ້ງລັອກ";
	var funrole_fm_isunlock = "ຕັ້ງປົດລັອກ";
	var funrole_fm_issopin = "ຕັ້ງຄ່າປ່ຽນລະຫັດ";
	var funrole_fm_ispush = "ຕັ້ງຄ່າການແຈ້ງເຕືອນ";
	var funrole_fm_isinit = "ຕັ້ງການລິເລີ່ມເບື້ອງຕົ້ນ";
	var funrole_fm_isdynamic = "ກໍານົດເນື້ອໃນໄດນາມິກ";
	var funrole_fm_isinformation = "ຕັ້ງຂໍ້ມູນ";
	var funrole_fm_isactive = "ຕັ້ງການເຄື່ອນໄຫວ";
	var funrole_fm_editcert = "ຕັ້ງຄ່າໃບຢັ້ງຢືນ";
	var funrole_fm_approvecert = "ອະນຸມັດໃບຮັບຮອງ";
	var funrole_fm_deleterequest = "ປະຕິເສດການຕໍ່ອາຍຸຄູ່ມື";
	var funrole_fm_addrenewal = "ເພີ່ມການຊົດເຊີຍ";
	var funrole_fm_deleterenewal = "ປະຕິເສດການຊົດເຊີຍ";
	var funrole_fm_importrenewal = "ນໍາເຂົ້າລາຍການຊົດເຊີຍ";
	var funrole_fm_accessrenewal = "ການເຂົ້າເຖິງໜ້າທີ່ການຊົດເຊີຍ";
	var funrole_fm_revoke_cert = "ການເຂົ້າຫາການຖອນໃບຮັບຮອງ";
	var funrole_fm_export_cert = "ການເຂົ້າຫາໃບຢັ້ງຢືນການສົ່ງອອກ";
	// ຜູ້ໃຊ້ -> MenuScreen
	var menusc_title_list = "ການຕັ້ງຄ່າເມນູ";
	var menusc_title_table = "ລາຍການເມນູ";
	var menusc_title_edit = "ຕັ້ງຄ່າເມນູ";
	var menusc_title_add = "ເພີ່ມເມນູ";
	var menusc_fm_nameparent = "ການຈັດການເມນູ";
	var menusc_fm_name_vn = "ຊື່ເມນູ (ພາສາຫວຽດນາມ)";
	if (IsWhichCA === "15" || IsWhichCA === "17") {
		menusc_fm_name_vn = "ຊື່ເມນູ (ລາວ)";
	}
	var menusc_fm_name = "ຊື່ເມນູ";
	var menusc_fm_name_en = "ຊື່ເມນູ (ພາສາອັງກິດ)";
	var menusc_fm_code = "ລະຫັດເມນູ";
	var menusc_fm_url = "URL ເມນູ";
	var menusc_succ_add = "ເພີ່ມເມນູ ສຳ ເລັດແລ້ວ";
	var menusc_succ_edit = "ຕັ້ງຄ່າເມນູ ສຳ ເລັດ";
	var menusc_exists_linkurl = "URL ເມນູມີຢູ່ແລ້ວ";
	var menusc_exists_nameparent = "ການຈັດການເມນູມີຢູ່ແລ້ວ";
	// ຜູ້ໃຊ້ -> ລາຍຊື່ຜູ້ໃຊ້
	var user_title_list = "ການຄຸ້ມຄອງຜູ້ໃຊ້ BackOffice";
	var user_title_search = "ຜູ້ໃຊ້ BackOffice";
	var user_title_table = "ລາຍຊື່ຜູ້ໃຊ້ BackOffice";
	var user_title_edit = "ຕັ້ງຄ່າຜູ້ໃຊ້ BackOffice";
	var user_title_add = "ເພີ່ມຜູ້ໃຊ້ BackOffice";
	var user_title_info = "ຂໍ້ມູນຜູ້ໃຊ້ BackOffice";
	var user_title_roleset = "ລາຍການຟັງຊັນ";
	var user_title_roleset_token = "ຟັງຊັນສໍາລັບ Token";
	var user_title_roleset_cert = "ຟັງຊັນສໍາລັບໃບຢັ້ງຢືນ";
	var user_title_roleset_another = "ໜ້າ ທີ່ອື່ນ";
	var user_succ_add = "ເພີ່ມຜູ້ໃຊ້ BackOffice ສໍາເລັດ";
	var user_succ_edit = "ຕັ້ງຄ່າຜູ້ໃຊ້ BackOffice ສໍາເລັດ";
	var user_exists_username = "ຊື່ຜູ້ໃຊ້ມີຢູ່ແລ້ວ";
	var user_exists_email = "ອີເມວມີຢູ່ແລ້ວ";
	var user_exists_cert_hash = "ຂໍ້ມູນໃບຢັ້ງຢືນມີຢູ່ແລ້ວ";
	var user_exists_user_role_admin = "ຊື່ຜູ້ໃຊ້ທີ່ມີຢູ່ແລ້ວສໍາລັບບົດບາດຜູ້ບໍລິຫານໃນລະບົບ";
	var user_conform_cancel = "ເຈົ້າຕ້ອງການຍົກເລີກຜູ້ໃຊ້ບໍ?";
	var user_title_delete = "ລຶບຜູ້ໃຊ້";
	var user_title_delete_note = "ໝ່າຍເຫດ: ກະລຸນາເລືອກຜູ້ໃຊ້ເພື່ອຮັບ";

	// Rose -> RoseList
	var rose_title_list = "ການຕັ້ງຄ່າກຸ່ມ ຄຳ ສັ່ງ";
	var rose_title_table = "ໂຕະ";
	var rose_title_edit = "ກຸ່ມການຕັ້ງຄ່າການກຳນົດຄ່າ";
	var rose_title_add = "ເພີ່ມກຸ່ມຄຳສັ່ງ";
	var rose_fm_code = "ລະຫັດກຸ່ມຄຳສັ່ງ";
	var rose_fm_rose = "ກຸ່ມຄະນະກຳມະການ";
	var rose_succ_edit = "ຕັ້ງຄ່າໃຫ້ສຳເລັດຜົນ";
	var rose_succ_add = "ເພີ່ມແລ້ວຢ່າງສຳເລັດຜົນ";
	var rose_exists_profile_properties = "ໂປຣໄຟລມີຢູ່ແລ້ວ";
	var rose_permission_profile_list = "ເປີເຊັນຂອງຄ່ານາຍໜ້າສຳລັບຊຸດບໍລິການ";

	// profileaccss -> profileaccss
	var profileaccss_title_list = "ການຕັ້ງຄ່າກຸ່ມໂປຣໄຟລ";
	var profileaccss_title_table = "ຕາຕະລາງ";
	var profileaccss_title_edit = "ຕັ້ງກຸ່ມໂປຣໄຟລ";
	var profileaccss_title_add = "ເພີ່ມກຸ່ມໂປຣໄຟລ";
	var profileaccss_fm_code = "ລະຫັດກຸ່ມໂປຣໄຟລ";
	var profileaccss_fm_agency = "ລາຍຊື່ອົງການ";
	var profileaccss_fm_rose = "ກຸ່ມໂປຣໄຟລ";
	var profileaccss_fm_service_type = "ກຸ່ມປະເພດການຮ້ອງຂໍ";
	var profileaccss_fm_major_cert = "ໜ້າທີ່ໃບຢັ້ງຢືນ";
	var profileaccss_succ_edit = "ຕັ້ງຄ່າໃຫ້ສຳເລັດຜົນ";
	var profileaccss_succ_add = "ເພີ່ມໄດ້ສຳເລັດ";
	var profileaccss_exists_profile_properties = "ໂປຣໄຟລມີຢູ່ແລ້ວ";
	var profileaccss_apply_profile_agency = "ນຳໃຊ້ການກຳນົດຄ່າກັບຕົວແທນ";
	var profileaccss_exists_code = "ລະຫັດກຸ່ມໂປຣໄຟລມີຢູ່ແລ້ວ";
	
	var role_group_Role = "ເລືອກບົດບາດຜູ້ໃຊ້";
	var role_fm_is_ca = "ຕຳແໜ່ງສຳລັບ CA";
	var role_fm_is_agent = "ຕຳແໜ່ງອົງການ";
	var role_fm_name = "ຊື່ບົດບາດຂອງຜູ້ໃຊ້";
	var role_succ_add = "ເພີ່ມບົດບາດຜູ້ໃຊ້ໃຫ້ ສຳເລັດ";
	var role_succ_edit = "ຕັ້ງຄ່າບົດບາດຜູ້ໃຊ້ໃຫ້ ສຳເລັດ";
	var role_exists_code = "ລະຫັດບົດບາດຜູ້ໃຊ້ມີຢູ່ແລ້ວ";
	var role_exists_name = "ຊື່ບົດບາດຂອງຜູ້ໃຊ້ມີຢູ່ແລ້ວ";
	var role_noexists_functions = "ເລືອກຢ່າງໜ້ອຍໜຶ່ງອັນ";
	var role_fm_function_name = "ຊື່ໜ້າທີ່";

	// admin -> formfactor
	var formfactor_title_list = "ການຕັ້ງຄ່າວິທີການ";
	var formfactor_title_table = "ລາຍການວິທີການ";
	var formfactor_title_edit = "ວິທີການຕັ້ງຄ່າ";
	var formfactor_fm_code = "ລະຫັດວິທີການ";
	var formfactor_fm_name = "ຊື່ວິທີການ";
	var formfactor_succ_edit = "ຕັ້ງຄ່າວິທີການສໍາເລັດ";
	var formfactor_exists_name = "ຊື່ວິທີການມີຢູ່ແລ້ວ";
	var formfactor_title_properties = "ຕັ້ງຄ່າການເຊື່ອມຕໍ່ລະບົບ";

	// SmartUser -> ໃບຮັບຮອງ
	var global_fm_certprofile = "ຂໍ້ມູນໃບຢັ້ງຢືນ";
	var global_fm_certstatus = "ສະຖານະຂອງໃບຢັ້ງຢືນ";
	var global_fm_cert_expire_number = "ມື້ທີ່ເຫຼືອ";
	var global_fm_common = "ຊື່ສາມັນ";
	var global_fm_subject = "DN";
	var global_fm_public_key_hash = "Hash ກະແຈສາທາລະນະ";
	var global_fm_certificate_hash = "ໃບຢັ້ງຢືນ Hash";
	var global_fm_key_id = "ໄອດີກະແຈ";
	var global_fm_key_selector = "ຕົວເລືອກ KEY";
	var global_fm_service_deny = "ວັນບໍລິການ";
	var global_fm_authority_key_id = "ລະຫັດສິດອໍານາດ";
	var global_error_empty_cert = "ບໍ່ມີໃບຮັບຮອງ";
	var global_error_exists_mst_budget_regis = "ລະຫັດພາສີ/ ລະຫັດງົບປະມານ/ ບັດປະຈໍາຕົວ/ ບັດປະຈໍາຕົວມີຢູ່ໃນລະບົບແລ້ວ \ n ກະລຸນາເຂົ້າໄປຊື້ໃບຮັບຮອງເພີ່ມເຕີມ";

	var global_fm_ca = "ຜູ້ໃຫ້ບໍລິການ CA";
	var global_fm_certpurpose = "ຈຸດປະສົງໃບຢັ້ງຢືນ";
	var global_fm_certalgorithm = "ຂັ້ນຕອນໃບຢັ້ງຢືນ";
	var global_fm_Password_new = "ລະຫັດຜ່ານໃໝ່";
	var global_fm_Password_conform = "ຢືນຢັນລະຫັດຜ່ານ";
	var global_fm_Password_old = "ລະຫັດຜ່ານປັດຈຸບັນ";
	var global_fm_Password_change = "ປ່ຽນລະຫັດຜ່ານ";
	var global_fm_button_PasswordChange = "ຕົກລົງ";
	var global_fm_button_setup = "ການຕິດຕັ້ງ";
	var global_fm_button_setup_ejbca = "ຕັ້ງຄ່າຈາກ RA";
	var global_fm_button_import = "ອັບໂຫລດ";
	var global_fm_button_check = "ກວດສອບ";
	var global_fm_valid = "ວັນທີມີຜົນບັງຄັບໃຊ້";
	var global_fm_valid_cert = "ວັນທີມີຜົນບັງຄັບໃຊ້";
	var global_fm_browse_file = "ເລືອກໄຟລ";
	var global_fm_browse_cert_note = "ກະລຸນາເລືອກໄຟລ smaller ນ້ອຍກວ່າ";
	var global_fm_fileattach_support = "ຮູບແບບໄຟລ ed ທີ່ຮອງຮັບ:";
	var global_fm_browse_cert_addnote = "ຈັດບຸລິມະສິດ pdf, ໄຟລ image ຮູບພາບ";
	var global_fm_Expire = "ວັນໝົດອາຍຸ";
	var global_fm_Expire_cert = "ວັນໝົດອາຍຸ";
	var global_fm_pass_p12 = "ລະຫັດ P12";
	var global_fm_dateUpdate = "ວັນທີອັບເດດ";
	var global_fm_dateUpdate_next = "ອັບເດດວັນຕໍ່ໄປ";
	var global_fm_dateend = "ວັນໝົດອາຍຸ";
	var global_fm_activation = "ວັນທີເປີດ ນຳໃຊ້";
	var global_fm_Method = "ວິທີການ";
	var global_fm_mode = "ໂໝດ";
	var global_fm_Method_Smart_ID = "ວິທີ Smart ID";
	var global_fm_Method_Mobile_OTP = "ວິທີ OTP ມືຖື";
	var global_fm_Method_UAF = "ວິທີ UAF";
	var global_fm_worker = "ຜູ້ເຮັດວຽກ";
	var global_fm_isbackoffice_grid = "FrontOffice/BackOffice";
	var global_fm_isbackoffice = "FrontOffice/BackOffice";
	var global_fm_key = "ຊື່ກະແຈ";
	var global_fm_logout = "ອອກຈາກລະບົບ";
	var global_fm_title_account = "ບັນຊີ";
	var global_fm_otp_serial = "OTP SN";
	var global_fm_check_date = "ຕາມວັນທີ";
	var global_fm_check_date_profile = "ໂດຍ (ວັນທີໄດ້ຮັບການລວບລວມ)";
	var global_fm_expire_date = "ຈຳ ນວນຂອງມື້ທີ່ຍັງເຫຼືອຜົນ";
	var global_fm_check_month = "ຕາມເດືອນ";
	var global_fm_check_quarterly = "ຕາມລາຍໄຕມາດ";
	var global_fm_check_token = "ໂດຍ Token SN";
	var global_fm_company = "ບໍລິສັດ";
	var global_fm_issue = "ການອອກ";
	var global_fm_size = "ຂະໜາດ (KB)";
	var global_fm_OU = "ໜ່ວຍງານອົງກອນ (OU)";
	var global_fm_MST = "ລະຫັດພາສີ";
        var global_fm_enterprise_id = "ວິສາຫະກິດ UID";
        var global_fm_personal_id = "ສ່ວນຕົວ UID";
	var global_fm_callback_url = "ເສັ້ນທາງເຊື່ອມຕໍ່ບ່ອນເຮັດວຽກ API ເພື່ອຮັບຂໍ້ຄວາມການຮ້ອງຂໍຈາກຕົວຈັດການໂທເຄັນ";
	var global_fm_decision = "ໝາຍເລກຕັດສິນ";
	var global_fm_share_mode_cert = "ອະນຸຍາດຂໍ້ມູນການບໍລິການໃບຢັ້ງຢືນເພີ່ມເຕີມ";
	var global_fm_ID = "ລະຫັດໄອດີ";
	var global_fm_date_grant = "ວັນທີອອກ";
	var global_fm_organi_grant = "ສະຖານທີ່ຂອງບັນຫາ";
	var global_fm_representative_legal = "ຕົວແທນທາງກົດໝາຍ";
	var global_fm_MNV = "ລະຫັດພະນັກງານ";
    var global_fm_CMND = "ລະຫັດສ່ວນຕົວ";
    var global_fm_CMND_ID_Card = "ບັດປະຈຳຕົວ, ບັດປະຈຳຕົວ";
    var global_fm_place = "ສະຖານທີ່ຂອງບັນຫາ";
    var global_fm_cmnd_date = "ວັນທີອອກ";
    var global_fm_O = "ອົງການ (O)" ;
    var global_fm_O_notrefix = "ອົງການຈັດຕັ້ງ" ;
    var global_fm_T = "ຫົວຂໍ້ (T)" ;
    var global_fm_L = "ສະຖານທີ່ (L)" ;
    var global_fm_C = "ປະເທດ (C)" ;
    var global_fm_ST = "ແຂວງ/ເມືອງ (ST)" ;
    var global_fm_CN = "ຊື່ບໍລິສັດ (CN)" ;
    var global_fm_grid_CN = "ຊື່ສາມັນ (CN)" ;
    var global_fm_grid_personal = "ຊື່ສ່ວນຕົວ" ;
    var global_fm_grid_company = "ຊື່ບໍລິສັດ" ;
    var global_fm_grid_domain = "ຊື່ໂດເມນ";
    var global_fm_CN_CN = "ຊື່ເຕັມ (CN)" ;
    var global_fm_serial = "ໃບຢັ້ງຢືນ SN";
    var global_fm_choose_owner_cert = "ຄົ້ນຫາໂດຍ";
    var global_fm_Status = "ສະຖານະ";
    var global_fm_branch_status = "ສະຖານະອົງການ";
    var global_fm_status_control = "ສະຖານະການຄວບຄຸມ";
    var global_fm_Status_token = "ສະຖານະໂທເຄັນ" ;
    var global_fm_Status_signed = "ສະຖານະການເຊັນ";
    var global_fm_Status_notice = "ສະຖານະການແຈ້ງເຕືອນ";
    var global_fm_apply_signed = "ເຊັນ";
    var global_fm_unapply_signed = "ບໍ່ໄດ້ເຊັນ";
    var global_fm_Status_cert = "ສະຖານະໃບຢັ້ງຢືນ";
    var global_fm_Status_request = "ການຮ້ອງຂໍສະຖານະ";
    var global_fm_Status_agreement = "ສະຖານະຂໍ້ຕົກລົງ";
    var global_fm_smart_version = "ສະບັບສະຫຼາດ";
    var global_fm_os_type = "ປະເພດ OS";
    var global_fm_from_system = "ຈາກລະບົບ" ;
    var global_fm_from_system_uri = "ຈາກ URL" ;
var global_fm_to_system = "ເຖິງລະບົບ" ;
    var global_fm_to_system_uri = "ເຖິງ URL" ;
    var global_fm_Status_OTP = "ການຢືນຢັນລະຫັດການເປີດໃຊ້ຜິດຕິດຕໍ່ກັນທີ່ຍັງເຫຼືອ";
    var global_fm_Status_SignServer = "ສະຖານະສັນຍາເຊັນເຊີເວີ";
    var global_fm_Status_PKI = "ສະຖານະຂໍ້ຕົກລົງ PKI";
    var global_fm_status_profile = "ສະຖານະໂປຣໄຟລ໌";
    var global_fm_activity = "ດໍາເນີນການ";
    var global_fm_lost = "ສູນເສຍ";
    var global_fm_relost = "ຍົກເລີກການສູນເສຍ";
    var global_fm_lock = "ບລັອກ" ;
    var global_fm_type = "ແບບ" ;
    var global_fm_value = "ຄ່າ" ;
    var global_fm_chain_cert = "ການອອກໃບຢັ້ງຢືນ";
    var global_error_chain_cert = "ການອອກໃບຢັ້ງຢືນບໍ່ມີຢູ່";
    var global_error_cert_compare_ca = "ໃບຢັ້ງຢືນບໍ່ຖືກຕ້ອງ";
    var global_error_cert_compare_csr = "ໃບຢັ້ງຢືນບໍ່ຖືກຕ້ອງ";
    var global_fm_Note = "ບັນທຶກ";
    var global_fm_Note_offset = "ບັນທຶກການຊົດເຊີຍ";
    var global_fm_soft_copy = "ສະບັບເອເລັກໂຕຣນິກ";
    if(IsWhichCA === "7") {
        global_fm_soft_copy = "ສະຖານະຂອງໄຟລ໌ທາງດ້ານກົດໝາຍ";
    }
    var global_fm_Content = "ເນື້ອໃນ";
    var global_fm_tran_code = "ລະຫັດທຸລະກໍາ";
    var global_fm_tran_timeout = "ໝົດເວລາ (ທີສອງ)" ;
    var global_fm_filter_search = "ຄົ້ນຫາ";
    var global_fm_combox_success = "ປະສົບຜົນສໍາເລັດ";
    var global_fm_combox_errorsend = "ຄວາມລົ້ມເຫຼວ" ;
    var global_fm_cert_circlelife = "ວົງຈອນຊີວິດໃບຢັ້ງຢືນ";
    var global_req_all = "ກະລຸນາຕື່ມຂໍ້ມູນທັງໝົດ";
    var global_req_length = "ຄວາມຍາວບໍ່ຖືກຕ້ອງ";
    var global_req_file = "ເລືອກໄຟລ໌";
    var global_req_file_has_data = "(ໄຟລ໌ມີເນື້ອໃນ)";
    var global_req = policy_req_empty;
    var global_errorsql = "ມີຂໍ້ຜິດພາດເກີດຂຶ້ນ, ກະລຸນາລອງໃໝ່ອີກຄັ້ງ";
    var global_print2_fullname_business = "ຊື່ທຸລະກຳເຕັມ (ນະຄອນຫຼວງ, ຕົວອັກສອນສຳນຽງ)";
    var global_req_email_subject_san = "ກະລຸນາໃສ່ອີເມລ໌ໃນໃບຢັ້ງຢືນດຽວກັນ";
    var global_req_print_not_support = "ປະເພດຄຳຮ້ອງຂໍບໍ່ຮອງຮັບການພິມ";
    var global_req_warning_exists_cert = "ການຮ້ອງຂໍການອະນຸມັດມີຢູ່ແລ້ວກັບຂໍ້ມູນໃບຮັບຮອງຂ້າງເທິງ\nສືບຕໍ່ການລົງທະບຽນ?";
	
	var global_alert_login = "ເວລາເຂົ້າສູ່ລະບົບiredໝົດອາຍຸ, ກະລຸນາເຂົ້າສູ່ລະບົບອີກຄັ້ງ";
	var global_alert_another_login = "ບັນຊີຖືກລັອກ/ ຖືກເຂົ້າຫາຈາກອຸປະກອນອື່ນ, ກະລຸນາກວດເບິ່ງອີກຄັ້ງ";
	var global_alert_another_menu = "ເຈົ້າບໍ່ສາມາດເຂົ້າຫາຟັງຊັນນີ້ໄດ້, ກະລຸນາກວດເບິ່ງອີກຄັ້ງ";
	var global_alert_license_invalid = "ໃບອະນຸຍາດບໍ່ຖືກຕ້ອງ. ກະລຸນາຕິດຕໍ່ສາຍດ່ວນ ອີເມວ support@lca.la ເພື່ອສະ ໜັບ ສະ ໜູນ";
	var global_error_login_info = "ບໍ່ມີຂໍ້ມູນເຂົ້າສູ່ລະບົບບັນຊີ, ກະລຸນາລອງໃໝ່່ອີກຄັ້ງ";
	var global_error_invalid = ": ບໍ່ຖືກຕ້ອງ";

	// Admin -> LicenseAdmin
	var license_title_list = "ການຈັດການໃບອະນຸຍາດ";
	var license_table_list = "ນໍາເຂົ້າໄຟລ  ໃບອະນຸຍາດ";
	var license_title_search = "ຄົ້ນຫາ";
	var license_title_edit = "ລາຍລະອຽດ";
	var license_req_file = "ເລືອກໄຟລ ໃບອະນຸຍາດ";
	var license_fm_file = "ເລືອກໄຟລ";
	var license_succ_import = "ຕັ້ງຄ່າໄຟລ ໃບອະນຸຍາດສໍາເລັດ";
	var license_group_hardware = "ລາຍລະອຽດຂອງຮາດແວ";
	var license_group_view = "ລາຍລະອຽດໃບອະນຸຍາດ";
	var license_fm_token_sn = "ໂທເຄັນ SN";
	var license_fm_user_enabled = "ໃຊ້ແລ້ວ";
	var license_fm_type = "ປະເພດ";
	var license_group_Function = "ຂໍ້ມູນການທໍາງານ";
	var license_error_file = "ເອກະສານໃບອະນຸຍາດບໍ່ຖືກຕ້ອງ";
	var license_error_no_token_sn = "ການນໍາເຂົ້າລົ້ມເຫລວ. \ [TOKEN_SN] ບໍ່ມີຄ່າ";
	var license_error_no_license_type = "ການນໍາເຂົ້າລົ້ມເຫລວ. \ [LICENSE_TYPE] ບໍ່ມີຄ່າ";
	var license_succ_import_insert = ". ເພີ່ມ:";
	var license_succ_import_update = "; ມີຢູ່ແລ້ວ:";
	var license_succ_import_error = "; ຜິດພາດ:";

	// ເວັບໄຊທ page ຂອງຫນ້າສະຫຼຸບ
	var CSRF_Mess = "ເຊັກຊັ້ນປະຈຸບັນໝົດອາຍຸ, ກະລຸນາໂຫຼດໜ້າຄືນໃໝ່";
	var TitleLoginPage = "ກັບຄືນໄປບ່ອນຫ້ອງການ";
	var TitlePolicyPage = "ນະໂຍບາຍຄວາມເປັນສ່ວນຕົວ";
	var TitleTermsPage = "ເງື່ອນໄຂການໃຫ້ບໍລິການ";
	var TitlePolicyLink = "ນະໂຍບາຍຄວາມເປັນສ່ວນຕົວ";
	var TitleTermsLink = "ເງື່ອນໄຂການໃຫ້ບໍລິການ";
	var TitleHomePage = "ໜ້າຫຼັກຂອງຫ້ອງການກັບຄືນ";
	var error_title_list = "ລະບົບຜິດພາດ";
	var error_content_home = "ລະບົບພາຍໃນຜິດພາດ. ກະລຸນາກັບໄປທີ່ເມນູຫຼັກ";
	var error_content_login = "ລະບົບພາຍໃນຜິດພາດ. ກະລຸນາລອງເຂົ້າສູ່ລະບົບໃໝ່";
	var error_content_link_download = "ດາວໂຫຼດໄຟລເອກະສານຜິດພາດ";
	var error_content_link_out = "ເຊື່ອມຕໍ່ອອກ";
	var login_req_captcha = "CAPTCHA ບໍ່ຖືກຕ້ອງ";
	var login_title_captcha = "ໂຫຼດ CAPTCHA ຄືນໃໝ່";
	var login_fm_captcha = "CAPTCHA";
	var login_fm_forget = "ລືມລະຫັດຜ່ານ?";
	var login_fm_token_ssl = "Token";
	var login_title_forget = "ລືມລະຫັດຜ່ານ";
	var login_succ_forget = "ກະລຸນາກວດອີເມລສໍາລັບລະຫັດຜ່ານໃໝ່";
	var login_succ_forget_request = "ສົ່ງຄໍາຮ້ອງຂໍການຕັ້ງລະຫັດຜ່ານຄືນໃໝ່ສໍາເລັດ. ກະລຸນາລໍຖ້າໃຫ້ຜູ້ຄວບຄຸມລະບົບອະນຸມັດ";
	var login_fm_buton_login = "ເຂົ້າສູ່ລະບົບ";
	var login_fm_buton_cancel = "ຍົກເລີກ";
	var login_fm_buton_OK = "ຕົກລົງ";
	var login_fm_buton_continue = "ສືບຕໍ່";
	var login_error_timeout = "ໝົດເວລາ";
	var login_error_exception = "ລະບົບຜິດພາດ. ກະລຸນາລອງໃໝ່";
	var login_error_lock = "ບັນຊີຖືກບລັອກຊົ່ວຄາວ. ກະລຸນາລອງໃໝ່ພາຍຫຼັງ";
	var login_error_incorrec = "ຊື່ຜູ້ໃຊ້/ລະຫັດຜ່ານບໍ່ຖືກຕ້ອງ";
	var login_error_inactive = "ບັນຊີຖືກລັອກ. ກະລຸນາຕິດຕໍ່ຜູ້ບໍລິຫານ";
	var login_error_token_ssl = "ການປະຕິເສດການເຂົ້າເຖິງ";
	var login_conform_forget = "ກະລຸນາຢືນຢັນຂໍ້ມູນອີເມວ: {EMAIL}";
	var global_fm_detail = "ລາຍລະອຽດ";
	var global_fm_expand = "ຂະຫຍາຍ";
	var global_fm_collapse = "ຫຍໍ້ລົງ";
	var global_fm_hide = "ເຊື່ອງ";
	var global_fm_search_expand = "ຂະຫຍາຍການຄົ້ນຫາ";
	var global_fm_search_hide = "ຫຍໍ້ການຄົ້ນຫາ";
	var global_fm_button_reset = "ຣີເຊັດ";
	var global_fm_button_activate = "ເປີດນຳໃຊ້";
	var global_fm_button_unactivate = "ເປີດນຳໃຊ້";
	var global_fm_file_name = "ຊື່ໄຟລ";
	var global_fm_down = "ດາວໂຫລດ";
	var global_fm_view = "ເບິ່ງ";
	var global_fm_p12_down = "ດາວໂຫລດ P12";
	var global_fm_p7p_down = "ດາວໂຫລດ P7B";
	var global_fm_down_enterprise = "ໃບຢັ້ງຢືນວິສາຫະກິດ";
	var global_fm_down_personal = "ໃບຮັບຮອງສ່ວນຕົວ";
	var global_fm_down_staff = "ໃບຢັ້ງຢືນພະນັກງານ";
	var global_fm_down_pem = "ດາວໂຫລດ PEM";
	var global_fm_license_down = "ດາວໂຫລດໃບຮັບຮອງໃບຢັ້ງຢືນດິຈິຕອລ";
	var global_fm_license_create = "ສ້າງໃບອະນຸຍາດໃບຢັ້ງຢືນດິຈິຕອລຄືນໃໝ່";
    var global_fm_sign_confirmation = "ລົງນາມໃນໜ້າການຢືນຢັນຄືນໃໝ່";
     var global_fm_wait_sign_confirmation = "ລໍຖ້າການເຊັນຢືນຢັນ";
     var global_cbx_wait_sign_confirmation = "ລໍຖ້າການເຊັນ" ;
     var global_cbx_sign_confirmation = "ເຊັນ" ;
	var global_succ_license_create = "ສ້າງໃບຮັບຮອງ ສຳ ເລັດ";
	var global_fm_down_cts = "ດາວໂຫລດໃບຮັບຮອງ";
	var global_fm_change = "ປ່ຽນ";
	var global_fm_dispose = "ຍົກເລີກ";
	var global_fm_copy_all = "ສຳເນົາໄວ້ໃນຄລິບບອດ";
	var global_succ_copy_all = "ອັດສຳເນົາໃສ່ຄລິບບອດສຳເລັດ";

	/// ໃບ ໜ້າ 03 ///
	var global_req_formfactor_support = "ລະບົບຍັງບໍ່ທັນສະ ໜັບ ສະ ໜູນ ວິທີການຈາກ BackOffice";
	var global_req_no_special = "ຢ່າປະກອບດ້ວຍລັກສະນະພິເສດ";
	var global_req_no_space = "ຢ່າປະກອບດ້ວຍບັນຫາຫວ່າງເປົ່າ";
	var global_fm_button_delete = "ລຶບ";
	var global_fm_paging_total = "ແຖວທັງົດ";
	var policy_check_length_pass = "ຄວາມຍາວຕໍ່າສຸດຕ້ອງຕໍ່າກວ່າຄວາມຍາວສູງສຸດຂອງລະຫັດຜ່ານ";
	var policy_check_number_zero = "" + "ຕ້ອງຫຼາຍກວ່າ 0";
	var global_fm_button_reset_pass = "ຣີເຊັດລະຫັດຜ່ານ";
	var global_fm_button_check_pass_default = "ກວດລະຫັດຜ່ານເລີ່ມຕົ້ນ";
	var global_fm_character = "" + "ຕົວອັກສອນ";
	var global_fm_phone_zero = "" + "ຕ້ອງເລີ່ມດ້ວຍ 0";
	var global_fm_phone_8_11 = "ຄວາມຍາວໂທລະສັບມືຖືແມ່ນຕັ້ງແຕ່ 8 ຫາ 11 ຕົວເລກ";
	var pass_req_no_space = "ລະຫັດຜ່ານບໍ່ປະກອບດ້ວຍເປົ່າຫວ່າງ";
	var user_req_no_space = "ຊື່ຜູ້ໃຊ້ບໍ່ປະກອບດ້ວຍການເປົ່າຫວ່າງ";
	var pass_req_min_greater = "ຄວາມຍາວຕໍ່າສຸດສໍາລັບລະຫັດຜ່ານຕ້ອງໃຫຍ່ກວ່າ";
	var pass_req_max_less = "ຄວາມຍາວສູງສຸດສໍາລັບລະຫັດຜ່ານຕ້ອງເປັນຕົວກວ່າ";
	var pass_req_character = "ລະຫັດຜ່ານຈໍາເປັນຕ້ອງມີຢ່າງໜ້ອຍ ໜຶ່ງ ຕົວອັກສອນ";
	var pass_req_special = "ລະຫັດຜ່ານຕ້ອງມີຕົວອັກສອນພິເສດຢ່າງໜ້ອຍ ໜຶ່ງ ຕົວ";
	var pass_req_number = "ຕ້ອງການໃຫ້ມີລະຫັດຜ່ານຢ່າງໜ້ອຍມີຈໍານວນດຽວ";
	var pass_req_upcase = "ລະຫັດຜ່ານຈໍາເປັນຕ້ອງມີຕົວອັກສອນຍົກຕົວຢ່າງອັນດຽວ";
	var pass_req_another_old = "ລະຫັດຜ່ານໃໝ່ຕ້ອງແຕກຕ່າງຈາກລະຫັດຜ່ານປັດຈຸບັນ";
	var pass_req_conform_new = "ລະຫັດຜ່ານທີ່ຢືນຢັນໃໝ່ບໍ່ຖືກຕ້ອງ";
	var pass_error_old = "ລະຫັດຜ່ານປະຈຸບັນບໍ່ຖືກຕ້ອງ";
	var pass_error_choise_another = "ກະລຸນາໃສ່ລະຫັດຜ່ານອື່ນ";
	var pass_error_choise_another_exists = "ລະຫັດຜ່ານໃcan't່ບໍ່ສາມາດກົງກັບ {NUMBER} ລະຫັດຜ່ານສຸດທ້າຍໄດ້! ກະລຸນາໃສ່ລະຫັດໃໝ່";
	var pass_succ_change = "ປ່ຽນລະຫັດຜ່ານ ສຳ ເລັດແລ້ວ";
	var pass_succ_change_show = ". ລະຫັດຜ່ານແມ່ນ:";
	var pass_error_account_old = "ບັນຊີປັດຈຸບັນຜິດພາດ";
	var global_fm_check_search = "ກະລຸນາກວດສອບຢ່າງໜ້ອຍໜຶ່ງເງື່ອນໄຂ";
	var pass_fm_Password_first = "ປ່ຽນຂໍ້ມູນລະຫັດຜ່ານ";

	// ສົ່ງຈົດາຍ
	var sendmail_error = "ສົ່ງຈົດໝາຍບໍ່ສຳເລັດ. ກະລຸນາລອງໃໝ່ອີກ";
	var sendmail_success = "ສົ່ງຈົດໝາຍສຳເລັດ";
	var sendmail_notexists = "ອີເມວບໍ່ຖືກຕ້ອງ";
	var sendmail_notexists_account = "ຊື່ຜູ້ໃຊ້ບໍ່ຖືກຕ້ອງ";

	// ສົ່ງອີເມລ Password ຕົວເຊັນເຂົ້າລະຫັດຜ່ານ
	var sendmail_error_signserver = "ສົ່ງຈົດໝາຍບໍ່ສຳເລັດ. ກະລຸນາລອງໃໝ່ອີກຄັ້ງ";

	// ທົ່ວໂລກ
	var global_alert_search_multiline = "ຈໍານວນຂອງຜົນການຄົ້ນຫາເກີນ 10,000. ລະບົບຈະຖືກປັບໃຫ້ເໝາະສົມກັບເງື່ອນໄຂການຄົ້ນຫາໂດຍອັດຕະໂນມັດ";
	var global_error_export_excel = "ສົ່ງອອກບໍ່ສຳເລັດ";
	var global_success_export_excel = "ສູນທີ່ເຊື່ອຖືໄດ້ຮັບການຮ້ອງຂໍການສົ່ງອອກໄປແລ້ວ";
	var global_error_insertmenulink = "ບໍ່ສາມາດເພີ່ມໜ້າຈໍທີ່ເຮັດວຽກໄດ້";
	var global_error_deletemenulink = "ລົ້ມເຫລວໃນການລຶບໜ້າຈໍທີ່ໃຊ້ໄດ້";
	var global_search_time_all = "ຕະຫຼອດເວລາຢູ່ໃນລະບົບ";
	// ປຸ່ມ
	var global_button_grid_delete = "ລຶບ";
	var global_button_grid_edit = "ຕັ້ງຄ່າ";
	var global_button_grid_config = "ຕັ້ງຄ່າ";
	var global_button_grid_lock = "ບລັອກ";
	var otp_button_grid_lock = "ບລັອກ";
	var global_label_grid_sum = "ທັງໝົດ";
	var global_button_grid_OTP = "OTP";
	var global_button_grid_cancel = "ຍົກເລີກ";
	var global_button_grid_authen = "ອັດຕະໂນມັດ";
	var global_button_grid_synch = "ຊິ້ງຂໍ້ມູນ";

	var global_button_grid_lost = "ເສຍ";
	var global_button_grid_unlost = "ຍົກເລີກ";
	var global_button_grid_unlock = "UnBlock";
	var global_button_grid_sendmail = "ສົ່ງຈົດໝ່າຍ";
	var global_button_p12_sendmail = "ການສົ່ງ P12";
	var otp_button_grid_unlock = "UnBlock";
	var global_button_grid_enable = "ເປີດໃຊ້ງານ";
	var global_button_grid_disable = "ປິດການໃຊ້ງານ";
	var global_fm_gen_pass = "ສ້າງລະຫັດຜ່ານ";
	var global_no_data = "ບໍ່ພົບຂໍ້ມູນ!";
	var global_no_print_data = "ບໍ່ພົບແມ່ແບບການພິມ";
	var global_no_file_list = "ລາຍການໄຟລຫວ່າງເປົ່າ";
	var global_check_datesearch = "ວັນທີເລີ່ມຕົ້ນຄວນຈະແມ່ນກ່ອນວັນທີສິ້ນສຸດ";
	var global_check_date_expired = "ເວລາໝົດອາຍຸຕ້ອງຫຼາຍກວ່າວັນທີປັດຈຸບັນ";
	var global_succ_succ = "ສຳເລັດແລ້ວ";
	var global_fm_mess_in = "ຂໍຂໍ້ມູນຂໍ້ມູນ";
	var global_fm_mess_out = "ຂໍ້ມູນການຕອບສະໜອງຂໍ້ມູນ";

	var global_req_phone_format = "ປ້ອນຮູບແບບໂທລະສັບທີ່ຖືກຕ້ອງ";
	var user_error_no_data = "ບໍ່ມີຂໍ້ມູນຜູ້ໃຊ້ BackOffice, ກະລຸນາກວດເບິ່ງອີກຄັ້ງ";
	var user_conform_reset_pass = "ເຈົ້າຕ້ອງການຕັ້ງລະຫັດຜ່ານໃໝ່ບໍ?";
	var token_confirm_delete = "ເຈົ້າຕ້ອງການລຶບ Token ບໍ?";
	var user_conform_delete = "ເຈົ້າຕ້ອງການລຶບຜູ້ໃຊ້ບໍ?";
	var user_succ_delete = "ລຶບຜູ້ໃຊ້ ສຳເລັດ";
	var global_fm_require_label = "" + "(*)";
	var global_fm_import_sample = "ອ້າງໂປຣໄຟລ໌ຕົວຢ່າງ::";
	var global_req_info_cert = "ຂໍ້ມູນໃບຢັ້ງຢືນບໍ່ຖືກຕ້ອງ";

	// ຮ້ອງຂໍ -> tokenlist
	var token_title_list = "ການຈັດການ Token";
	var token_title_search = "ຄົ້ນຫາ";
	var token_title_table = "ລາຍຊື່";
	var token_title_edit = "ລາຍລະອຽດ Token";
	var token_title_add = "Token ໃໝ່";
	var token_title_init = "ການລິເລີ່ມເບື້ອງຕົ້ນ";
	var token_title_changesopin = "ປ່ຽນ SOPIN";
	var token_title_delete = "ລຶບToken";
	var token_exists_tokensn = "Token SN ມີຢູ່ແລ້ວ";
	var token_succ_edit = "ອັບເດດສຳເລັດແລ້ວ";
	var token_succ_delete = "ລຶບສຳເລັດ";
	var token_conform_update_multi = "ເຈົ້າຕ້ອງການອັບເດດບໍ?";
	var token_succ_add_renew = "ເພີ່ມຄວາມສຳເລັດ";
	var token_fm_tokenid = "Token SN";
	var token_fm_signing_number = "ຈຳນວນຂອງການເຊັນ";
	var token_fm_sopin = "SOPIN";
	var token_fm_tokenid_new = "Token SN ໃຫມ່";
	var token_fm_subject = "ຊື່ຫົວຂໍ້";
	var token_fm_company = "ຊື່ບໍລິສັດ";
	var token_fm_valid = "ເວລາມີຜົນບັງຄັບໃຊ້ (ໃບຮັບຮອງ)";
	var token_fm_expire = "ເວລາໝົດອາຍຸ(ໃບຢັ້ງຢືນ)";
	var global_fm_FromDate_valid = "ເວລາມີຜົນບັງຄັບໃຊ້ (ເລີ່ມຕົ້ນ)";
	var global_fm_ToDate_valid = "ເວລາມີຜົນ (ຈົບ)";
	var global_fm_FromDate_expire = "ເວລາໝົດອາຍຸ (ເລີ່ມຕົ້ນ)";
	var global_fm_ToDate_expire = "ເວລາໝົດອາຍຸ (ຈົບ)";
	var global_fm_FromDate_profile = "ຈາກ(ວັນທີຮັບ)";
	var global_fm_ToDate_profile = "ເຖິງ(ວັນທີຮັບ)";
	var token_fm_validexpire_search = "ເວລາທີ່ສາມາດນຳໃຊ້ແລະເວລາໝົດອາຍຸ (ໃບຢັ້ງຢືນ)";
	var token_fm_personal = "ຊື່ສ່ວນຕົວ";
	var token_fm_serialcert = "ໃບຢັ້ງຢືນ SN";
	var token_fm_taxcode = "TIN (ເລກປະຈໍາຕົວຜູ້ເສຍພາສີ)";
	var token_fm_block = "ລັອກ";
	var token_fm_reason_block = "ເຫດຜົນລັອກ";
	var token_fm_all_apply = "ນຳ ໃຊ້ກັບໂທເຄັນທັງໝົດໃນລະບົບ (ລະເລີຍໄຟລອັບໂຫຼດ)";
	var token_fm_unblock = "UnBlock";
	var token_fm_csr = "CSR (ຄໍາຮ້ອງຂໍການເຊັນໃບຢັ້ງຢືນ)";
	var token_fm_innit = "ເລີ່ມຕົ້ນ";
	var token_fm_change = "ປ່ຽນ SOPIN";
	var token_fm_datelimit = "ເວລາໝົດອາຍຸ (ໃບຢັ້ງຢືນຈະອອກໃຫ້)";
	var token_fm_mobile = "ໂທລະສັບ";
	var token_fm_email = "ອີເມວ";
	var token_fm_address = "ທີ່ຢູ່";
	var token_fm_address_permanent = "ທີ່ຢູ່ຖາວອນ";
	var token_fm_address_billing = "ທີ່ຢູ່ຮຽກເກັບເງິນ";
	var token_fm_address_residence = "ທີ່ຢູ່ອາໄສຖາວອນ";
	var token_fm_menulink = "ລິ້ງເມນູ";
	var token_fm_linkname = "ຊື່ເຊື່ອມຕໍ່";
	var token_fm_linkvalue = "ຄ່າເຊື່ອມຕໍ່";
	var token_fm_noticepush = "ສະແດງການແຈ້ງເຕືອນ (ສະຕິກເກີ) ຢູ່ໃນຕົວຈັດການໂທເຄັນ";
	var token_fm_noticeinfor = "ຂໍ້ມູນແຈ້ງການ";
	var token_fm_noticelink = "ແຈ້ງການເຊື່ອມຕໍ່";
	var token_fm_colortext = "ສີຂໍ້ຄວາມ";
	var token_fm_colorgkgd = "ສີພື້ນຫຼັງ";
	var token_fm_infor = "ຂໍ້ມູນຂ່າວສານ";
	var token_fm_location = "ສະຖານທີ່";
	var token_fm_state = "ລັດ";
	var token_fm_enroll = "ລົງທະບຽນ";
	var token_fm_TimeOffset = "ເວລາໝົດອາຍຸ (ສັນຍາຂອງຜູ້ໃຊ້)";
	var token_fm_dn = "DN (ຊື່ທີ່ໂດດເດັ່ນ)";
    var token_fm_passport = "PID (ບັດປະຈຳຕົວ)";
    var token_fm_version = "ສະບັບໂທເຄັນ";
    var token_fm_agent = "ອົງການ";
    var token_group_info = "ຂໍ້ມູນລາຍລະອຽດ";
    var token_group_update = "ປ່ຽນຂໍ້ມູນ";
    var token_group_notification = "ຂໍ້ມູນສະແດງ (ສະຕິກເກີ) ໃນແຖບແນວນອນຂອງຕົວຈັດການໂທເຄັນ";
    var token_group_dynamic = "ເນື້ອໃນແບບເຄື່ອນໄຫວ";
    var token_group_other = "ອື່ນໆ";
    var token_group_cert_history = "ປະຫວັດການຢັ້ງຢືນຂອງໂທເຄັນ";
    var token_group_request_edit = "ລາຍການການຮ້ອງຂໍຕ້ອງການດັດແກ້ Token";
    var token_title_import = "ການນໍາເຂົ້າໂທເຄັນ";
    var token_fm_typesearch = "ຊອກຫາ";
    var token_fm_import_sample = "ເອກະສານຕົວຢ່າງ: ";
    var token_fm_datelimit_example = "ຕົວຢ່າງ: (ISO 8601 ວັນທີ: [yyyy-MM-dd HH:mm:ssZZ]: '2018-09-20 14:01:28+07:00')";
	// ຫຼື ວັນ: ຊົ່ວໂມງ: ນາທີ
	var token_error_no_column = "ການນໍາເຂົ້າລົ້ມເຫລວ.\nຮູບແບບຖັນຂໍ້ມູນບໍ່ຖືກຕ້ອງ";
    var token_error_no_tokenid = "ການນໍາເຂົ້າລົ້ມເຫລວ.\n[TOKEN_SN] ຄ່າບໍ່ມີຢູ່";
    var token_error_no_sopin = "ການນໍາເຂົ້າລົ້ມເຫລວ.\n[TOKEN_SOPIN] ຄ່າບໍ່ມີຢູ່";
    var token_error_no_agent = "ການນໍາເຂົ້າລົ້ມເຫລວ.\n[Agency] ຄ່າບໍ່ມີຢູ່";
    var token_error_datelimit_format = "ກະລຸນາໃສ່ຮູບແບບວັນທີທີ່ຖືກຕ້ອງ";
    var token_error_datelimit_date = "ກະລຸນາໃສ່ເວລາໝົດອາຍຸ";
    var token_error_datelimit_format_date = "ຮູບແບບວັນທີ: (ddd:HH:MM)";
    var token_succ_import = "ອັບໂຫຼດສຳເລັດ";
    var token_succ_setup = "ຕິດຕັ້ງສຳເລັດແລ້ວ";
    var token_succ_check_import = "ການກວດສອບສຳເລັດແລ້ວ. ກົດປຸ່ມຕິດຕັ້ງເພື່ອບັນທຶກຂໍ້ມູນ";
    var token_error_check_import = "ມີຂໍ້ຜິດພາດເກີດຂຶ້ນ, ກະລຸນາກວດເບິ່ງລາຍລະອຽດໃນໄຟລ໌ຜົນໄດ້ຮັບ";
    var token_succ_import_insert = ".ຕື່ມ: ";
    var token_succ_import_update = " ; ປັບປຸງ: ";
    var token_succ_import_error = " ; ຂໍ້ຜິດພາດ: ";
    var token_succ_import_insert_replace = "ຕື່ມ: ";
    var token_succ_import_update_replace = "ອັບເດດ: ";
    var token_succ_import_error_replace = "ບໍ່ສຳເລັດ: ";
    var token_error_import_format = "ຮູບແບບ Excel: XLS, XLSX, CSV";
    var token_fm_lock_opt = "ລະຫັດເປີດໃຊ້ງານ ສະຖານະການຢືນຢັນຖືກບລັອກ";
var token_fm_unlock_opt = "ການເປີດໃຊ້ລະຫັດການຢືນຢັນສະຖານະແມ່ນ UnBlock";
    var token_confirm_unlock_temp = "ທ່ານຕ້ອງການປົດບລັອກບໍ?";
    var token_succ_reset_opt = "ການເປີດໃຊ້ລະຫັດການຢືນຢັນສະຖານະ Unblock ສຳເລັດແລ້ວ";
    var tokenreport_title_list = "ລາຍງານສິນຄ້າຄົງຄັງຂອງຕົວແທນຈໍາໜ່າຍ";
    var tokenreport_fm_choose_time_export = "ເວລາສົ່ງອອກ";
    var tokenreport_fm_choose_time_import = "ເວລານໍາເຂົ້າ";
    var tokenreport_fm_agenct_date_export = "ວັນທີອອກຕົວແທນ";
	//request -> certificatelist
    var cert_title_list = "ການຄຸ້ມຄອງການອະນຸມັດໃບຢັ້ງຢືນ";
    var cert_title_search = "ຮ້ອງຂໍການຊອກຫາ";
    var cert_title_table = "ລາຍການຮ້ອງຂໍ";
    var cert_title_edit = "ການອະນຸມັດໃບຢັ້ງຢືນ";
    var cert_title_register_cert = "ຂໍ້ມູນໃບຢັ້ງຢືນ";
    var cert_title_register_cert = "ຂໍ້ມູນໃບຢັ້ງຢືນ";
    var cert_title_register_owner = "ຂໍ້ມູນເຈົ້າຂອງ";
    var cert_succ_approve = "ອະນຸມັດສຳເລັດ";
    var cert_error_approve = "ອະນຸມັດຄວາມລົ້ມເຫລວ";
    var cert_succ_reissue = "ບັນຫາທີ່ປະສົບຜົນສໍາເລັດ";
    var cert_fm_type_request = "ປະເພດການຮ້ອງຂໍ";
    var cert_fm_request = "ຄໍາຮ້ອງຂໍ";
    var cert_fm_request_agent = "ອະນຸມັດໃນອົງການ";
    var cert_fm_major_name = "ຊື່ຟັງຊັນ";
    var cert_fm_major_code = "ລະຫັດຟັງຊັນ";
    var cert_fm_profile_list = "ປະເພດໃບຢັ້ງຢືນ";
    var cert_fm_cert_profile = "ໂປຣໄຟລ໌ໃບຢັ້ງຢືນ (CORECA)" ;
    var cert_fm_delete_cert = "ລຶບໃບຢັ້ງຢືນ";
    var cert_fm_usereib = "ຊື່ໜ່ວຍງານ (CORECA)" ;
    var cert_fm_date_approve_fee = "ເວລາອະນຸມັດ (ໃບຢັ້ງຢືນ)";
    var cert_fm_user_fee = "ຜູ້ໃຊ້ອະນຸມັດ (ໃບຢັ້ງຢືນ)";
    var cert_succ_edit = "ການອັບເດດໃບຢັ້ງຢືນສຳເລັດ";
    var cert_succ_returnfee = "ອັບເດດສຳເລັດ";
    var cert_fm_Status = "ສະຖານະໃບຢັ້ງຢືນ";
    var cert_fm_push_notice = "ອະນຸຍາດໃຫ້ສົ່ງອີເມວ";
    var cert_fm_revoke_delete = "ລຶບໃບຢັ້ງຢືນທີ່ຖອນຄືນ";
    var cert_fm_revoke_delete_old = "ລຶບໃບຮັບຮອງເກົ່າເມື່ອອອກໃບຢັ້ງຢືນໃໝ່";
    var cert_confirm_otp_sendmail = "ທ່ານຕ້ອງການສົ່ງລະຫັດເປີດໃຊ້ງານບໍ?";
    var cert_succ_otp_resend = "ສົ່ງລະຫັດເປີດໃຊ້ສຳເລັດ";
var global_error_appove_status = "ສະຖານະອະນຸມັດບໍ່ຖືກຕ້ອງ. ອະນຸມັດຄວາມລົ້ມເຫລວ";
    var global_error_method = "ວິທີການທີ່ບໍ່ຖືກຕ້ອງ";
    var global_error_keysize_csr = "ຄວາມຍາວກະແຈ CSR ບໍ່ຖືກຕ້ອງ";
    var global_error_exist_csr = "CSR ມີຢູ່ໃນລະບົບແລ້ວ";
    var global_fm_button_renewal = "ເພີ່ມຄ່າຊົດເຊີຍ";
    var info_fm_cert_profile = "ໂປຣໄຟລ໌ໃບຢັ້ງຢືນ (CORECA)" ;
    var info_fm_profile_name = "ປະເພດໃບຢັ້ງຢືນ";
    var info_fm_type_request = "ປະເພດການຮ້ອງຂໍ";
    var global_fm_renew_access = "ໂປຣໄຟລ໌ກຳລັງສະໝັກການຕໍ່ອາຍຸໃບຢັ້ງຢືນ";
    var global_fm_renew_access_search = "ຊອກຫາຕາມໂປຣໄຟລ໌ກຳລັງສະໝັກການຕໍ່ອາຍຸໃບຢັ້ງຢືນ";
    var global_fm_csr_info_cts = "ຂໍ້ມູນການຮ້ອງຂໍໃບຢັ້ງຢືນການລົງທະບຽນ";
    var global_fm_san_info_cts = "ຂໍ້ມູນໃບຢັ້ງຢືນເພີ່ມເຕີມ";
    var global_fm_csr_info_cts_before = "ຂໍ້ມູນໃບຢັ້ງຢືນກ່ອນການປ່ຽນແປງ";
    var global_fm_csr_info_cts_after = "ຂໍ້ມູນໃບຢັ້ງຢືນຫຼັງການປ່ຽນແປງ";
    var global_fm_Corporation = "ບໍລິສັດ" ;
    var global_group_cert = "ລາຍລະອຽດຂອງໃບຢັ້ງຢືນ";
    var info_group_info = "ລາຍລະອຽດການຮ້ອງຂໍ";
    //revoke cert
    var revoke_title_list = "ການຄຸ້ມຄອງການຖອນໃບຢັ້ງຢືນ";
    var revoke_title_detail = "ລາຍລະອຽດ";
    var revoke_title_search = "ຄົ້ນຫາ";
    var revoke_title_table = "ລາຍການ";
    var global_fm_button_revoke = "ຖອນຄືນ" ;
    var global_fm_button_recovery = "ການຟື້ນຕົວ";
    var global_fm_button_suspend = "ໂຈະ" ;
    var global_fm_button_reissue = "Token ReIssue" ;
    var global_fm_button_detail = "ລາຍລະອຽດ" ;
	var global_fm_button_print_report = "ລາຍງານການພິມ";
    var global_fm_button_print_certificate = "ການຢັ້ງຢືນການພິມ";
    var global_fm_button_print_handover = "ການມອບຮັບການພິມ" ;
    var global_fm_button_print_regis = "ການຮ້ອງຂໍການພິມ" ;
    var global_fm_button_print_confirm = "ຢືນຢັນການພິມ";
    var global_fm_button_print = "ພິມ" ;
    var global_fm_button_export_zip_word = "ໄຟລ໌ Zip Word" ;
    var global_fm_button_export_zip_pdf = "ໄຟລ໌ Zip PDF" ;
    var global_fm_button_regis = "ລົງທະບຽນ";
    var global_fm_button_regis_soft = "Soft Token" ;
    var global_fm_button_re_regis = "ລົງທະບຽນຄືນໃໝ່" ;
    var info_group_approve = "ລາຍລະອຽດການອະນຸມັດ";
    var global_fm_approve = "ອະນຸມັດ";
    var global_fm_approve_ca = "ການອະນຸມັດລະດັບ CA";
	//report cert
    var certreport_title_list = "ການຄຸ້ມຄອງບົດລາຍງານໃບຢັ້ງຢືນ";
    var certreport_title_search = "ຄົ້ນຫາ";
    var certreport_title_table = "ລາຍການ";
//request -> ໃບຢັ້ງຢືນການລົງທະບຽນ
    var regicert_title_list = "ການຄຸ້ມຄອງການລົງທະບຽນໃບຢັ້ງຢືນ";
    var regicert_title_token_list = "ການລົງທະບຽນໃບຢັ້ງຢືນສຳລັບໂທເຄັນ";
    var regicert_soft_title_list = "ການຄຸ້ມຄອງການລົງທະບຽນໃບຢັ້ງຢືນ Soft Token";
    var regicert_title_search = "ຄົ້ນຫາ";
    var regicert_title_table = "ລາຍການ";
    var regicert_title_view = "ຂໍ້ມູນການລົງທະບຽນ";
    var buymorecert_title_view = "ລົງທະບຽນເພື່ອຊື້ໃບຢັ້ງຢືນເພີ່ມເຕີມ";
    var regicert_fm_datelimit_one = "1 ປີ";
    var regicert_fm_datelimit_two = "2 ປີ";
    var regicert_fm_datelimit_three = "3 ປີ";
    var regicert_fm_check_backup_key = "ກະແຈສຳຮອງໃນເຊີບເວີ";
    var regicert_fm_check_revoke = "ຖອນໃບຢັ້ງຢືນຫຼັງຈາກອອກໃໝ່ ຫຼືປ່ຽນຂໍ້ມູນ";
    var regicert_fm_check_change_key = "ປ່ຽນລະຫັດ";
    var regicert_fm_keep_certsn = "ຮັກສາໃບຢັ້ງຢືນ SN";
    var regicert_succ_add = "ການລົງທະບຽນສຳເລັດ";
    var regisapprove_title_list = "ການລົງທະບຽນໃບຢັ້ງຢືນອະນຸມັດການຄຸ້ມຄອງ";
    var regisapprove_title_view = "ອະນຸມັດການລົງທະບຽນໃບຢັ້ງຢືນ";
var regisapprove_succ_approve = "ອະນຸມັດສຳເລັດ";
    //request -> tokenimport
    var tokenimport_title_list = "ການຄຸ້ມຄອງການນໍາເຂົ້າໂທເຄັນ";
    var tokenimport_title_import = "ການນໍາເຂົ້າໂທເຄັນ";
    var tokenimport_title_search = "ຄົ້ນຫາ";
    var tokenimport_title_table = "ລາຍການ";
    var tokenimport_succ_edit = "ການນໍາເຂົ້າໂທເຄັນສຳເລັດ";
    var tokenimport_succ_add_renew = "ການຕໍ່ອາຍຸອັດຕະໂນມັດສຳເລັດ";
    var tokenimport_fm_fromtokenSN = "ໂທເຄັນ SN (ເລີ່ມຕົ້ນ)";
    var tokenimport_fm_totokenSN = "ໂທເຄັນ SN (ສິ້ນສຸດ)";
    
    // token -> TokenActionImport
    var actionimport_title_list = "ຈັດການການນໍາເຂົ້າລາຍການແກ້ໄຂ Token";
    var actionimport_title_import = "ການນໍາເຂົ້າໂທເຄັນ";
    var actionimport_title_search = "ຄົ້ນຫາ";
    var actionimport_title_table = "ລາຍການ";
    var actionimport_succ_edit = "ອັບໂຫຼດສຳເລັດ";
    
    //cert -> certimport
    var certimport_title_list = "ການຄຸ້ມຄອງການນໍາເຂົ້າໃບຢັ້ງຢືນ";
    var certimport_title_import = "ການນໍາເຂົ້າໃບຢັ້ງຢືນ";
    var certimport_file_format_invalid = "ຮູບແບບໄຟລ໌ບໍ່ຖືກຕ້ອງ. ການນໍາເຂົ້າລົ້ມເຫລວ";
    var certimport_fm_error = "ການນໍາເຂົ້າຜິດພາດ: ";
    var certimport_error_not_size = "ຈໍານວນສູງສຸດຂອງໃບຢັ້ງຢືນທີ່ຈະນໍາເຂົ້າຈາກໄຟລ໌ excel: ";
    var certimport_error_not_ca = "ບັນຊີບໍ່ມີການອະນຸຍາດນໍາເຂົ້າໃບຢັ້ງຢືນຈາກໄຟລ໌ excel";
    var certimport_error_not_format_file = "ລະບົບພຽງແຕ່ຮອງຮັບຮູບແບບໄຟລ໌: XLS, XLSX, CSV";
// ໃໝ່​ກ່ອນ​ແປ
    var tokenimport_title_multi = "ການຕັ້ງຄ່າໂທເຄັນຫຼາຍອັນ";
    var tokenimport_fm_createdate_search = "ສ້າງເວລາ";
    var tokenimport_fm_tokensn_search = "ໂທເຄັນ SN";
    var tokenimport_fm_result = "ຜົນໄດ້ຮັບການນໍາເຂົ້າ";
    var tokenimport_fm_createdate_tokensn_search = "ສ້າງເວລາ và Token SN";
// ຂໍ -> LogtList
    var log_title_list = "ການຈັດການປະຕິເສດ";
    var log_table_list = "ລາຍການ";
    var log_title_search = "ຄົ້ນຫາ";
    var log_title_view = "ລາຍລະອຽດຫຼຸດລົງ";
    var log_fm_user_detete_request = "ປະຕິເສດຜູ້ໃຊ້";
    var log_fm_date_detete_request = "ເວລາຫຼຸດລົງ";
	//Request -> RequestList
    var request_title_list = "ການຄຸ້ມຄອງການຮ້ອງຂໍ";
    var request_title_search = "ຄົ້ນຫາ";
    var request_table_list = "ລາຍການ";
    var request_title_view = "ລາຍລະອຽດ";
    var request_conform_delete = "ທ່ານຕ້ອງການປະຕິເສດບໍ?";
    var request_conform_revoke = "ທ່ານຕ້ອງການຖອນຄືນບໍ?";
    var request_succ_delete = "ປະຕິເສດສຳເລັດ";
    var request_error_delete = "ອະນຸມັດ, ປະຕິເສດຄວາມລົ້ມເຫລວ";
    // token -> backofficelog
    var backoffice_title_list = "ການກຳນົດຄ່າໂທເຄັນ";
    var backoffice_title_search = "ຄົ້ນຫາ";
    var backoffice_title_table = "ລາຍການ";
    var backoffice_title_view = "ຂໍ້ມູນລາຍລະອຽດ";
    var global_fm_combox_true = "ແມ່ນ" ;
    var global_fm_combox_false = "ບໍ່" ;
    var global_req_enter_info_change = "ກະລຸນາເລືອກຂໍ້ມູນຢ່າງໜ້ອຍໜຶ່ງອັນເພື່ອປ່ຽນແປງ";
    var global_req_format_url = "ໃສ່ຮູບແບບທີ່ຖືກຕ້ອງ: ";
    var global_error_wrong_agency = "ການເຂົ້າເຖິງປະຕິເສດອົງການນີ້, ກະລຸນາກວດເບິ່ງອີກຄັ້ງ";
    var global_error_wrong_role = "ການເຂົ້າເຖິງປະຕິເສດບົດບາດນີ້, ກະລຸນາກວດເບິ່ງອີກຄັ້ງ";
// token -> pushimport
    var pushimport_title_list = "ຈັດການການນໍາເຂົ້າການແຈ້ງເຕືອນ";
    var pushimport_title_import = "ການນໍາເຂົ້າການແຈ້ງເຕືອນ";
    var pushimport_succ_edit = "ອັບໂຫລດລາຍການແຈ້ງການທີ່ສຳເລັດແລ້ວ";
    var pushimport_succ_conform_down = "ດາວໂຫຼດຜົນໄດ້ຮັບໃສ່ຄອມພິວເຕີ?";
    var pushimport_fm_set_push = "ອັບເດດການແຈ້ງເຕືອນຕາມລາຍການ";
    var pushimport_fm_delete_push = "ລຶບການແຈ້ງເຕືອນຕາມລາຍການ";
    var pushimport_fm_text_push = "ເນື້ອໃນການແຈ້ງເຕືອນ";
    var pushimport_fm_link_push = "ການເຊື່ອມໂຍງການແຈ້ງເຕືອນ";
    //token -> ເກັບກໍາຂໍ້ມູນ
    var collectimport_title_list = "ການ​ນໍາ​ເຂົ້າ​ການ​ປັບ​ປຸງ​ບັນ​ຊີ​ລາຍ​ການ​ຄວບ​ຄຸມ​";
    var collectimport_title_import = "ອັບໂຫລດລາຍການຄວບຄຸມທີ່ປັບປຸງ";
    var collectimport_fm_set_push = "ການອັບເດດໄດ້ຖືກກວດສອບແລ້ວ";
    var collectimport_fm_delete_push = "ອັບເດດໂດຍບໍ່ມີການຄວບຄຸມ";
    var collectimport_fm_control_cert = "ການຄວບຄຸມໃບຢັ້ງຢືນ";
    var collectimport_fm_control_profile = "ບັນທຶກການຄວບຄຸມ";
    
    //cert -> ImportDisallowanceList
    var disallowanceimport_title_list = "ການຈັດການບັນຊີດໍາ";
     var disallowanceimport_title_import = "ບັນຊີດຳອັບໂຫຼດ";
     var disallowanceimport_succ_edit = "ອັບໂຫລດບັນຊີດຳທີ່ສຳເລັດແລ້ວ";
     var disallowanceimport_succ_conform_down = "ດາວໂຫຼດຜົນໄດ້ຮັບໃສ່ຄອມພິວເຕີ?";
     var disallowanceimport_fm_set_push = "ປັບປຸງຕາມລາຍການ";
     var disallowanceimport_fm_delete_push = "ລົບຕາມລາຍການ";
     var disallowanceimport_fm_title_blacklist = "ການສົ່ງອອກໄຟລ໌ CSV ບັນຊີດໍາ";
     var disallowanceimport_fm_title_contact = "ສົ່ງອອກໄຟລ໌ CSV ຕິດຕໍ່ລູກຄ້າ";
     var disallowanceimport_fm_contact_email = "ຂໍ້ມູນອີເມວ";
     var disallowanceimport_fm_contact_phone = "ຂໍ້ມູນໂທລະສັບ";
     var disallowanceimport_fm_note_blacklist = "ໝາຍເຫດ: ສົ່ງອອກໄຟລ໌ CSV ທີ່ມີບັນຊີດຳທີ່ມີຢູ່ໃນລະບົບ";
     var disallowanceimport_fm_note_contact = "ຫມາຍເຫດ: ສົ່ງອອກໄຟລ໌ CSV ເພື່ອລາຍຊື່ຂໍ້ມູນການຕິດຕໍ່ຂອງລູກຄ້າທີ່ລົງທະບຽນໃບຢັ້ງຢືນໃນລະບົບ";
	// NO_TRANSLATE
    //Token -> token Approve
    var tokenapprove_title_list = "ການຄຸ້ມຄອງການຮ້ອງຂໍໂທເຄັນ";
    var tokenapprove_table_list = "ລາຍການການຈັດການໂທເຄັນ";
    var tokenapprove_title_edit = "ລາຍລະອຽດການຄຸ້ມຄອງໂທເຄັນ";
    // ໃບຢັ້ງຢືນ -> ແມ່ແບບ DN
    var tempdn_title_list = "ແມ່ແບບໃບຢັ້ງຢືນ";
    var tempdn_title_table = "ດານ sách trường" ;
    var tempdn_title_table = "ທົ່ງນາ";
    var tempdn_group_Role = "ເລືອກໃບຢັ້ງຢືນ";
    var tempdn_group_assign = "ຊ່ອງທີ່ບໍ່ໄດ້ມອບໝາຍ";
    var tempdn_table_assigned = "ເຂດພື້ນທີ່ທີ່ຖືກມອບໝາຍ";
    var tempdn_conform_delete = "ທ່ານຕ້ອງການລຶບຊ່ອງຂໍ້ມູນນີ້ບໍ?";
    var tempdn_succ_delete = "ລຶບຊ່ອງຂໍ້ມູນສຳເລັດແລ້ວ";
    var tempdn_succ_insert = "ຕື່ມຂໍ້ມູນໃສ່ສຳເລັດແລ້ວ";
    var tempdn_succ_edit = "ການກຳນົດຄ່າທົ່ງນາສຳເລັດແລ້ວ";
    var tempdn_error_edit = "ຊ່ອງຂໍ້ມູນທີ່ບໍ່ມີຢູ່";
    var tempdn_error_delete = "ລຶບ Field Failure";
    var tempdn_error_insert = "ເພີ່ມຄວາມລົ້ມເຫລວໃນພາກສະຫນາມ";
    var global_fm_certtype = "ປະເພດໃບຢັ້ງຢືນ";
    if(IsWhichCA === "18") {
        global_fm_certtype = "ປະເພດໃບຢັ້ງຢືນ";
    }
	var global_fm_subjectdn = "ພາກສະຫນາມ" ;
    var global_fm_required = "ຕ້ອງການ";
    var global_fm_prefix = "ຄຳນຳໜ້າ" ;
    var global_fm_request_function = "ປະເພດການຮ້ອງຂໍ";
    var token_confirm_cancel_request = "ທ່ານຕ້ອງການປະຕິເສດການຮ້ອງຂໍນີ້ບໍ?";
    var token_confirm_issue_request = "ເຈົ້າຕ້ອງການອອກໃບຢັ້ງຢືນນີ້ບໍ?";
    var token_succ_cancel_request = "ປະຕິເສດຄຳຮ້ອງຂໍສຳເລັດ";
    var global_fm_button_decline = "ຫຼຸດລົງ" ;
    var global_fm_button_issue = "ອອກໃບຢັ້ງຢືນ";
    var global_fm_status_pending = "ການຮ້ອງຂໍການອະນຸມັດກ່ອນ";
    var global_fm_status_approved = "ຄໍາຮ້ອງຂໍການອະນຸມັດຫຼັງ";
    var global_tooltip_decline_request_token = "ການຮ້ອງຂໍໄດ້ຮັບການອະນຸມັດ, ມັນບໍ່ສາມາດຖືກປະຕິເສດໄດ້";
    var token_group_unlock = "ຕິດຕໍ່ພົວພັນ";
    var global_fm_duration_cts = "ຊຸດບໍລິການໃບຢັ້ງຢືນ";
    var global_fm_rssp_authmodes = "ໂຫມດການພິສູດຢືນຢັນ";
    var global_fm_rssp_replying_party = "ພັກຕອບ" ;
    var global_fm_percent_cts = "ມູນຄ່າຂອງຄະນະກໍາມະ";
    var global_fm_rose_type = "ປະເພດຂອງຄະນະກໍາມະ";
    var global_fm_rose_type_percen = "ເປີເຊັນ (%)" ;
    var global_fm_rose_type_money = "ຈຳນວນເງິນ" ;
    var global_fm_decline_desc = "ເຫດຜົນການປະຕິເສດ";
    var global_fm_revoke_desc = "ຖອນເຫດຜົນ (ຜູ້ໃຊ້)";
    var global_fm_dipose_desc = "ເຫດຜົນ Dipose" ;
    var global_fm_suspend_desc = "ເຫດຜົນຖືກໂຈະ";
    var global_fm_revoke_reason_core = "ຖອນເຫດຜົນ (CoreCA)" ;
    var global_fm_MNS = "ລະຫັດງົບປະມານ";
    var global_fm_HC = "ໜັງສືຜ່ານແດນ" ;
    var global_fm_CitizenId = "ຕົວຕົນຂອງພົນລະເມືອງ";
    var global_fm_requesttype = "ປະເພດການຮ້ອງຂໍ";
    var token_fm_choose_noticepush = "ເລືອກການປ່ຽນແປງການສະແດງຜົນ";
    var token_fm_set_no_noticepush = "ຕັ້ງຄ່າການສະແດງຜົນເລີ່ມຕົ້ນຂອງໂທເຄັນ";
    var token_fm_set_no_dynamic = "ປິດການເຊື່ອມຕໍ່ແບບໄດນາມິກ";
    var token_group_choose_dynamic = "ການຕັ້ງຄ່າການເຊື່ອມໂຍງແບບໄດນາມິກ";
    var global_fm_button_renew = "ການຕໍ່ອາຍຸ";
    var global_fm_button_buymore = "ຊື້ຫຼາຍ" ;
    var global_fm_button_changeinfo = "ປ່ຽນ" ;

    //Certificate -> RenewCertList
    var certlist_title_list = "ການຄຸ້ມຄອງໃບຢັ້ງຢືນ";
    var certlist_title_search = "ຄົ້ນຫາ";
    var certlist_title_table = "ລາຍການ";
    var certlist_title_renew = "ການຕໍ່ອາຍຸໃບຢັ້ງຢືນ";
    var certlist_title_reissue = "ການອອກໃບຢັ້ງຢືນ";
    var certlist_title_revoke = "ການຖອນໃບຢັ້ງຢືນ";
// ໃບຢັ້ງຢືນ -> CertificateShareList
    var certsharelist_title_list = "ການຈັດການຂໍ້ມູນການບໍລິການເພີ່ມເຕີມ";
    var certsharelist_title_search = "ຄົ້ນຫາ";
    var certsharelist_title_table = "ລາຍການ";
    
    var certlist_title_recovery = "ການຟື້ນຕົວໃບຢັ້ງຢືນ";
    var certlist_title_suspend = "ໃບຢັ້ງຢືນການໂຈະ";
    
    var certlist_group_renew = "ລາຍລະອຽດການຕໍ່ອາຍຸໃບຢັ້ງຢືນ";
    var certlist_group_reissue = "ລາຍລະອຽດການອອກໃບຢັ້ງຢືນ";
    var certlist_title_changeinfo = "ການກຳນົດຄ່າໃບຢັ້ງຢືນ";
    var certlist_group_changeinfo = "ລາຍລະອຽດການປ່ຽນແປງ";
    var certlist_group_sender = "ຜູ້ສົ່ງ";
    var certlist_group_add_info = "ຂໍ້ມູນເພີ່ມເຕີມ";
    var certlist_group_add_bussiness_info = "ຂໍ້ມູນບໍລິສັດ";
    var certlist_group_add_buss_pers_info = "ບໍລິສັດ, ຂໍ້ມູນສ່ວນຕົວ";
    var certlist_group_return_contact_info = "ຂໍ້ມູນຕິດຕໍ່ເພື່ອສົ່ງຄືນບັນທຶກ";
    var certlist_group_add_personal_info = "ຂໍ້ມູນສ່ວນຕົວ";
    var certlist_group_add_bussiness_contact = "ຂໍ້ມູນການຕິດຕໍ່";
    var certlist_group_receiver = "ຜູ້ຮັບ";
    var certlist_fm_unnamed = "ໃບຢັ້ງຢືນທີ່ບໍ່ເປີດເຜີຍຊື່";
    var certlist_title_detail = "ລາຍລະອຽດຂອງໃບຢັ້ງຢືນ";
    var certlist_title_print_hadover = "ບັນທຶກການຈັດສົ່ງການພິມ";
    var certlist_title_print_register = "ການລົງທະບຽນພິມ";
    var certlist_title_print_changeinfo = "ຂໍ້ມູນການປ່ຽນແປງການພິມ";
    var certlist_title_print_reissue_revoke = "ການຍົກເລີກການພິມ ແລະຂໍ້ມູນການອອກໃໝ່";
    var certlist_title_detail_old = "ລາຍລະອຽດຂອງໃບຢັ້ງຢືນເກົ່າ";
    var certlist_succ_renew = "ການຕໍ່ອາຍຸໃບຢັ້ງຢືນຢ່າງສຳເລັດຜົນ";
    var certlist_succ_reissue = "ການອອກໃບຢັ້ງຢືນສຳເລັດແລ້ວ";
    var certlist_succ_revoke = "ການຖອນໃບຢັ້ງຢືນສຳເລັດແລ້ວ";
    var certlist_succ_revoke_ca = "ການຖອນໃບຢັ້ງຢືນສຳເລັດແລ້ວ";
    var certlist_succ_changeinfo = "ການກຳນົດຄ່າໃບຢັ້ງຢືນສຳເລັດແລ້ວ";
    var certlist_succ_changepass_p12 = "ປ່ຽນລະຫັດຜ່ານ P12 ສຳເລັດ";
    var certlist_error_changepass_p12 = "ການປ່ຽນລະຫັດຜ່ານ P12 ລົ້ມເຫລວ";
    
    var certlist_succ_recovery = "ໃບຮັບຮອງຖືກຟື້ນຟູສຳເລັດແລ້ວ";
    var certlist_succ_recovery_ca = "ໃບຮັບຮອງຖືກຟື້ນຟູສຳເລັດແລ້ວ";
    var certlist_succ_suspend = "ໃບຮັບຮອງຖືກໂຈະສຳເລັດແລ້ວ";
    var certlist_succ_suspend_ca = "ໃບຮັບຮອງຖືກໂຈະສຳເລັດແລ້ວ";
    var certlist_fm_device_uuid = "ອຸປະກອນ UID" ;
    
    //owner -> owner
    var owner_title_list = "ການຄຸ້ມຄອງເຈົ້າຂອງໃບຢັ້ງຢືນ";
    var owner_title_search = "ຄົ້ນຫາ";
    var owner_title_table = "ລາຍການ";
    var owner_title_renew = "ການຕໍ່ອາຍຸໃບຢັ້ງຢືນ";
    var owner_title_reissue = "ການອອກໃບຢັ້ງຢືນ";
    var owner_title_revoke = "ການຖອນໃບຢັ້ງຢືນ";
    var owner_title_recovery = "ການຟື້ນຕົວໃບຢັ້ງຢືນ";
    var owner_title_suspend = "ໃບຢັ້ງຢືນການໂຈະ";
    var owner_title_add = "ລົງທະບຽນເຈົ້າຂອງ";
    var owner_title_view = "ຂໍ້ມູນເຈົ້າຂອງ";
    var owner_succ_add = "ລົງທະບຽນສຳເລັດ";
    var owner_succ_edit = "Config ສຳເລັດແລ້ວ";
    var ownerapprove_title_list = "ການຄຸ້ມຄອງການອະນຸມັດເຈົ້າຂອງ";
    var owner_fm_type = "ປະເພດເຈົ້າຂອງ";
    /// ບໍ່ມີການແປ
    var owner_title_dispose = "ເຈົ້າຂອງຖິ້ມ";
    var owner_title_change = "ປ່ຽນຂໍ້ມູນເຈົ້າຂອງ";
    var owner_succ_dispose = "ຄໍາຮ້ອງຂໍຖືກສົ່ງໄປສໍາເລັດ";
    var owner_fm_private_uid = "ຂໍ້ມູນການລະບຸຕົວຕົນ";
    var owner_succ_approve = "ອະນຸມັດສຳເລັດ";
    var owner_title_cert_search = "ຊອກຫາເຈົ້າຂອງ";
    
    // monitor -> serverlog
    var serverlog_title_list = "ການຈັດການບັນທຶກເຊີບເວີ";
    var serverlog_title_todate = "ບັນທຶກເຊີບເວີມື້ນີ້";
    var serverlog_title_down = "ດາວໂຫຼດບັນທຶກເຊີບເວີ";
    var serverlog_fm_typelog = "ລະບົບ";
    var serverlog_fm_numberlog = "ຈໍານວນແຖວ";
    var serverlog_fm_timestamp = "ເວລາ";
    var serverlog_fm_detail = "ລາຍລະອຽດບັນທຶກ";
    var hastatus_fm_auto = "ໂຫຼດຂໍ້ມູນອັດຕະໂນມັດ (ວິນາທີ)" ;
    //
    var global_error_promotion_package_limit = "ໄລຍະເວລາໂປຣໂມຊັນບໍ່ສາມາດໃຫຍ່ກວ່າໄລຍະເວລາຂອງໂປຣໄຟລ໌ໄດ້";
    var global_error_amount_package_limit = "ຈຳນວນທີ່ຄິດຄ່າບໍລິການບໍ່ສົມເຫດສົມຜົນ";
    var global_fm_token_status_configed = "ການກຳນົດຄ່າໂທເຄັນປັດຈຸບັນ";
    var certlist_group_token_new = "New Token SN";
    var global_error_noexists_token = "ໂທເຄັນບໍ່ມີຢູ່ແລ້ວ" ;
    var global_error_token_status = "ສະຖານະໂທເຄັນບໍ່ຖືກຕ້ອງ. ກະລຸນາກວດສອບ";
    var global_error_coreca_call_approve = "ມີຂໍ້ຜິດພາດເກີດຂຶ້ນໃນເວລາໂທຜ່ານ CoreCA. ກະລຸນາກວດເບິ່ງອີກຄັ້ງ";
    var global_error_exists_equals_dn = "ຂໍ້ມູນບໍ່ໄດ້ລວມເອົາ = ລັກສະນະ";
    var branch_title_table = "ລາຍການ";
    var branch_title_info = "ຂໍ້ມູນອົງການ";
    var branch_fm_representative = "ຕົວແທນ";
    var branch_fm_representative_position = "ຕຳແໜ່ງຕົວແທນ";
    var branch_fm_logo = "ໂລໂກ້ອົງການ";
    
    // ສາຂາເຂົ້າ
    var branch_fm_profile_title_access = "ການຕັ້ງຄ່າ API" ;
    var branch_fm_api_title_access = "ການເຂົ້າເຖິງ API";
    var branch_fm_profile_group_profile = "ການເຂົ້າເຖິງໂປຣໄຟລ໌";
    var branch_fm_profile_group_formfactor = "ວິທີການເຂົ້າເຖິງ" ;
    var branch_fm_api_tag_credential = "SOAP-API ຂໍ້ມູນປະຈຳຕົວ" ;
    var branch_fm_rest_tag_credential = "ຂໍ້ມູນປະຈໍາຕົວ REST-API" ;
    var branch_fm_api_tag_ip = "ການເຂົ້າຫາ IP";
    var branch_fm_api_tag_function = "ການເຂົ້າໃຊ້ຟັງຊັນ";
    var branch_fm_api_allow_access = "ອະນຸຍາດໃຫ້ຕັ້ງຄ່າ API";
    var branch_fm_api_signture = "ລາຍເຊັນ";
    var branch_fm_api_publishkey = "ກະແຈສາທາລະນະ";
    var branch_fm_check_reload_cert = "ເລືອກການໂຫຼດຂໍ້ມູນການຕັ້ງຄ່າຄືນໃໝ່";
    
    var tokentransfer_title_list = "ການຈັດການໂທເຄັນໂອນ";
    var tokentransfer_title_search = "ຄົ້ນຫາ";
    var tokentransfer_title_table = "ລາຍການ";
    var certprofile_title_search = "ຄົ້ນຫາ";
    var certprofile_title_table = "ລາຍການ";
	
	var global_error_request_exists = "ການຮ້ອງຂໍທີ່ຄ້າງຢູ່ມີຢູ່ແລ້ວ. ເພີ່ມຄວາມລົ້ມເຫລວ";
	var global_error_cert_exists_token = "Tokenໄດ້ມີໃບຮັບຮອງ. ການຂໍຄືນຂອງທ່ານບໍ່ສໍາເລັດ";
	var global_error_request_exists_token = "Tokenມີການຮ້ອງຂໍທີ່ຄ້າງຢູ່.ການຮ້ອງຂໍຂອງທ່ານບໍ່ສໍາເລັດ";
	var global_error_approve_exists_cert = "Tokenມີໃບຮັບຮອງ. ອະນຸມັດຄວາມລົ້ມເຫຼວ";
	var global_error_credential_external_invalid = "ຂໍ້ມູນປະຈໍາຕົວຂອງລະບົບພາຍນອກບໍ່ຖືກຕ້ອງ, ກະລຸນາກວດເບິ່ງຄືນໃໝ່";

	var tokenexport_title_list = "ສົ່ງອອກລາຍການ Token";
	var tokenexport_title_search = "ຄົ້ນຫາ";
	var tokenexport_title_table = "ລາຍການ";

	var global_fm_checkbox_gcndk = "ໃບຢັ້ງຢືນການຈົດທະບຽນທຸລະກິດ";
	var global_fm_checkbox_GPDT = "ໃບອະນຸຍາດລົງທຶນ";
	var global_fm_checkbox_QDTL = "ຕັດສິນໃຈສ້າງຕັ້ງ";
	var global_fm_choise = "ເລືອກ";
	var branch_fm_logo_note = "ໝາຍເຫດ: ພາບພື້ນຫຼັງມີຄວາມໂປ່ງໃສ, ຂະໜາດ (ຄວາມກວ້າງ 210px ແລະຄວາມສູງ 70px), ຂະໜາດ <500 KB, ຮູບແບບ: png, jpg, gif";
	var branch_fm_logo_down = "ດາວໂຫຼດແມ່ແບບໂລໂກ້";
	var branch_error_logo_great_size = "ກະລຸນາເລືອກຮູບນ້ອຍກວ່າ 500 KB";
	var branch_fm_logo_change = "ເລືອກການປ່ຽນແປງສໍາລັບໂລໂກ້";
	var branch_fm_logo_default = "ຕັ້ງຄ່າເລີ່ມຕົ້ນ";

	var global_succ_mst_register = "ລະຫັດພາສີໄດ້ລົງທະບຽນແລ້ວ";
	var global_succ_mns_register = "ລະຫັດງົບປະມານໄດ້ລົງທະບຽນແລ້ວ";
	var global_succ_cmnd_register = "ID ສ່ວນຕົວໄດ້ລົງທະບຽນແລ້ວ";
	var global_succ_hc_register = "ໜັງສືຜ່ານແດນໄດ້ລົງທະບຽນແລ້ວ";
	// NO_TRANSLATE 20180828
	var reportquick_fm_innit = "ການລິເລີ່ມການຢັ້ງຢືນ";
	var reportquick_fm_activation = "ການດຳເນີນການໃບຢັ້ງຢືນ";
	var reportquick_fm_revoke = "ການຖອນໃບຢັ້ງຢືນ";
	var reportquick_fm_total = "ທັງໝົດ";
	var global_fm_cert_list = "ລາຍຊື່ໃບຢັ້ງຢືນ";
	var reportquick_title_list = "ໃບຢັ້ງຢືນການລາຍງານ";
	var reportquick_table_search = "ຄົ້ນຫາ";
	var reportquick_title_add = "ໃບຢັ້ງຢືນການລາຍງານ";
	var reportquick_title_edit = "ໃບຢັ້ງຢືນການລາຍງານ";
	var global_fm_date_approve_agency = "ວັນທີອົງການອະນຸມັດ";
	var global_fm_user_approve_agency = "ຜູ້ໃຊ້ອົງການອະນຸມັດ";
	var global_fm_date_approve = "ວັນທີອະນຸມັດ";
	var global_fm_user_approve = "ຜູ້ນຳໃຊ້ການອະນຸມັດ";
	var global_fm_date_approve_ca = "CA ວັນທີອະນຸມັດ";
	var global_fm_user_approve_ca = "CA ຜູ້ໃຊ້ອະນຸມັດ";
	var global_error_not_user_create = "ບໍ່ມີຜູ້ໃຊ້ສ້າງ";
	var global_succ_delete = "ລຶບສຳເລັດ";
	var global_error_delete = "ລຶບຄວາມລົ້ມເຫຼວ";
	// ການຈັດການໄຟລ
	var file_succ_delete = "ລຶບສຳເລັດ";
	var file_conform_delete = "ເຈົ້າຕ້ອງການລຶບໄຟລ? ບໍ?";
	var file_conform_upload = "ເຈົ້າຕ້ອງການອັບໂຫລດໄຟລ? ບໍ?";

	// NO_TRANSLATE 20180906
	var reportcertlist_title_list = "ລາຍງານລາຍການໃບຢັ້ງຢືນ";
	var reportcertlist_table_search = "ຄົ້ນຫາ";

	var reportcertexpire_title_list = "ໃບຢັ້ງຢືນໝົດອາຍຸ";
	var reportcertexpire_table_search = "ຄົ້ນຫາ";
	
	// ການລວບລວມ NO_TRANSLATE
	var collation_title_list = "ການຈັດການຂໍ້ມູນການຮວບຮວມ";
	var collation_fm_collated = "ຮວບຮວມແລ້ວ";
	var collation_fm_uncollated = "ບໍ່ໄດ້ຮວບຮວມ";
	var collation_button_change = "ປ່ຽນສະຖານະການລວບລວມ";
	var collation_fm_change = "ສະຖານະການປ່ຽນແປງ";
	var collation_button_rose_agent = "ອັບເດດຄະນະກໍາມະການ";
	var collation_fm_change_change = "ວັນທີປ່ຽນສະຖານະ";
	var collation_fm_mounth = "ເດືອນໂຮມເຂົ້າກັນ";
	var collation_fm_time = "ວັນທີຮວບຮວມ";
	var collation_fm_date_receipt = "ວັນທີໄດ້ຮັບການລວບລວມ";
	var collation_fm_type = "ຮູບແບບການເກັບກໍາບັນທຶກ";
	var collation_fm_type_inmounth = "ບັນທຶກລາຍເດືອນ";
	var collation_fm_type_compensation = "ບັນທຶກການຊົດເຊີຍ";
	var collation_fm_date_compensation = "ວັນທີຂອງໂປຣໄຟລການຊົດເຊີຍ";
	var collation_alert_type_inmounth = "ໂປຣໄຟລຫາຍໄປໃນເດືອນ";
	var collation_alert_type_compensation = "ໂປຣໄຟລໄດ້ຮັບການຊົດເຊີຍໃນເດືອນ";
	var collation_fm_profile_overdue = "ໂປຣໄຟລເກີນກຳນົດ";
	var collation_fm_unapproved_profile = "ໂປຣໄຟລມີໄຟລທີ່ຍັງບໍ່ໄດ້ອ່ານ";
	var collation_fm_approved_profile = "ໄຟລໄດ້ອ່ານໄຟລໃໝ່";
	var collation_fm_money_overdue = "ຈຳນວນຂອງຄ່າປັບໄໝທີ່ຄ້າງຈ່າຍ";
	var collation_fm_print_DK = "ແບບຟອມລົງທະບຽນ";
	var collation_fm_print_Confirm = "ຮູບແບບການຢັ້ງຢືນ";
	var collation_fm_print_GPKD = "ແບບຟອມໃບອະນຸຍາດທຸລະກິດ";
	var collation_fm_print_CMND = "ຮູບແບບບັດປະຈໍາຕົວ";

	var profile_title_list = "ການຈັດການໂປຣໄຟລ";
	var profile_title_detail = "ລາຍລະອຽດໂປຣໄຟລ";
	var profile_title_import_list = "ນໍາເຂົ້າຂໍ້ມູນໂປຣໄຟລ";
	var profile_fm_enoughed = "ບັນທຶກການຄືນດີກັນ";
	var profile_fm_unenoughed = "ບັນທຶກທີ່ຍັງບໍ່ທັນໄດ້ຮວບຮວມ";
	var profile_conform_update = "ເຈົ້າແນ່ໃຈບໍ່ວ່າເຈົ້າອັບເດດສະຖານະການສໍາເລັດໂປຣໄຟລ?? \  ໂປຣໄຟລຈະບໍ່ຖືກອັບເດດອີກຄັ້ງ.";

	var inputcertlist_title_list = "ຂໍ້ມູນອ້າງອີງ ໜີ້";
	var inputcertlist_table_search = "ຄົ້ນຫາ";
	var inputcertlist_succ_edit = "ຕັ້ງຄ່າສໍາເລັດ";
	var inputcertlist_succ_add = "ຕື່ມການສໍາເລັດ";
	var global_fm_monthly = "ລາຍເດືອນ";
	var global_fm_title_push_approve1 = "ມີ";
	var global_fm_title_push_approve2 = "ການຮ້ອງຂໍລໍຖ້າການອອກໃບຢັ້ງຢືນ";
	var global_fm_title_push_decline = "ການຮ້ອງຂໍການປະຕິເສດການອອກໃບຢັ້ງຢືນ";
	// ICA
    var global_error_revoke_forbiden = "ໃບຮັບຮອງບໍ່ສາມາດຖືກຖອນຄືນ 2 ຄັ້ງຕິດຕໍ່ກັນ, ກະລຸນາກວດເບິ່ງອີກຄັ້ງ";
    var global_error_revoke_limit = "ຖອນໃບຮັບຮອງເກີນຈຳນວນທີ່ກຳນົດຕໍ່ເດືອນ, ກະລຸນາຕິດຕໍ່ CA";
    var global_fm_limit_revoke = "ຍົກເລີກການຈຳກັດເດືອນ";
    var global_fm_login_form = "ເຂົ້າສູ່ລະບົບ";
    var global_fm_address_GPKD = "ທີ່ຢູ່ (ໃບອະນຸຍາດທຸລະກິດ)" ;
    var global_fm_CitizenId_I = "ລະຫັດພົນລະເມືອງ";
    var global_fm_browse_file_upload = "ເຖິງ" ;
    var global_fm_button_add_simple = "ຕື່ມ";
    var global_fm_button_add_action = "ເພີ່ມທຸລະກໍາ" ;
    var global_fm_button_print_profile = "ການພິມໂປຣໄຟລ໌" ;
    var global_fm_button_off_notice = "ປິດການແຈ້ງເຕືອນ";
    var global_fm_sign = "ເຊັນ";
    var file_conform_signprofile = "ທ່ານຕ້ອງການເຊັນໄຟລ໌ນີ້ບໍ?";
    var fm_succ_signprofile = "ເຊັນໄຟລ໌ສຳເລັດ";
    var global_fm_remark_agency_en = "ຊື່ຕົວແທນ (ພາສາອັງກິດ)" ;
    var global_fm_remark_agency_vn = "ຊື່ຕົວແທນ (ຫວຽດນາມ)" ;
    var global_fm_identifier_type = "ປະເພດຕົວລະບຸ";
    if(IsWhichCA !== "18"){
        global_fm_identifier_type = "UID ທຸລະກິດ";
    }
    var global_fm_document_type = "ປະເພດເອກະສານ" ;
    if(IsWhichCA !== "18") {
        global_fm_document_type = "UID ສ່ວນຕົວ";
    }
    var global_fm_enter = "ປ້ອນ" ;
    var global_fm_enter_number = "ໃສ່ຕົວເລກ" ;
    var request_conform_approve = "ທ່ານຕ້ອງການອະນຸມັດບໍ?";
    var certlist_title_detail_current = "ລາຍລະອຽດປະຈຸບັນຂອງໃບຢັ້ງຢືນ";
    var global_fm_register_date = "ເລືອກວັນທີລົງທະບຽນ";
    // ຮ້ອງຂໍ -> ລາຍຊື່ປະຫວັດ
    var history_title_list = "ການຈັດການປະຫວັດ";
    var history_title_search = "ຊອກຫາປະຫວັດ";
    var history_title_table = "ລາຍຊື່ປະຫວັດ";
    var history_title_detail = "ລາຍລະອຽດປະຫວັດ";
    var history_fm_response = "ສະຖານະ";
    var history_fm_function = "ຟັງຊັນໃຊ້ງານ";
    var history_fm_request_data = "ການຮ້ອງຂໍຂໍ້ມູນ";
    var history_fm_response_data = "ຂໍ້ມູນການຕອບສະໜອງ";
    var history_fm_request_ip = "IP";
    var history_fm_source_entity = "ການຈັດຕັ້ງປະຕິບັດລະບົບ";

    var reportneac_title_list = "ລາຍການວຽກງານ NEAC";
    var reportneac_title_search = "ຄົ້ນຫາ";
    var reportneac_title_table = "ລາຍຊື່";
    var reportneac_fm_tab_control = "ລາຍການວຽກງານອຸປະກອນ";
    var reportneac_fm_tab_recurring = "ລາຍການລາຍການແຕ່ໄລຍະ";
    var reportneac_fm_tab_cts_signserver = "ເຊີບເວີເປີດການເຂົ້າຫາຊື່ການປຶກສາຫາລື";
    var reportneac_fm_tab_cts_token = "ລາຍການວິຊາການແກ້ໄຂ";
    var global_fm_cert_count = "ຈຳ ນວນໃບຮັບໃບຮອງ";
    var reportneac_fm_cert_personal = "ໃບຢັ້ງຢືນວິຊາສະເພາະ";
    var reportneac_fm_cert_enterprise = "ໃບຢັ້ງຢືນວິຊາການທຸລະກິດ";
    var reportneac_fm_cert_staff = "ໃບຄໍາແນະນໍາວິຊາການນັກສຶກສາ";
    var reportneac_fm_control_content = "ຈໍານວນຈໍານວນໃບຮັບຮອງທີ່ມອບໃຫ້ໂດຍເຈົ້າ ໜ້າ ທີ່ສາທາລະນະທີ່ໃຫ້ກັບຜູ້ທີ່ສະຖານະການແມ່ນໃຊ້ເປັນກອງກໍາລັງ, ວິສາຫະກິດ (ບໍ່ລວມເອົາຕົວຢ່າງ) ຕັ້ງແຕ່ວັນທີ 01/01/2017 ແລະຍັງໃຊ້ໄດ້ຕະຫຼອດເວລາ";
    var global_fm_report_date = "......... , ວັນທີ ..... ເດືອນ ..... ປີ ...... ";
    var global_fm_report_print_date = "......... , ມື້ [DD] ເດືອນ [MM] ປີ [YYYY]";
    var global_fm_choose_cert = "ເລືອກໃບຮັບຮອງ";
    var global_fm_unchoose_cert = "ການແຂ່ງຂັນເຕະບານການກວດສອບວິຊາການແກ້ໄຂບັນຫາ";
    var global_fm_login_ssl = "ຄວາມສຸກໄກເຂົ້າສູ່ລະບົບອົງການປົກຄອງປະເທດໂທເຄັນ";
    var global_ssl_conform_delete = "ເຈົ້າຕ້ອງການທີ່ ໜ້າ ຕື່ນເຕັ້ນໃນການກວດສອບວິທະຍາສາດການສຶກສາບໍ?";
    var global_confirm_print_register = "ເຈົ້າຕ້ອງການການພິມແບບຟອມໂຄງສ້າງການລົງທຶນບໍ?";
    var global_confirm_print_renew = "ເຈົ້າຕ້ອງການພິມຮູບແບບການ ດຳ ເນີນການຕໍ່ອາຍຸບໍ່?";
    
    // footer page
    var footer_name = "";
    var footer_name_inner = "";
    var footer_address = "";
    var footer_email = "";
    var header_hotline = "";
    var footer_hotline = "";
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
        footer_address = "Địa chỉ: Phòng 305, Toà Nhà GP Invest, Số 170 Đê La Thành, Q.Đống Đa, Hà Nội";
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
        header_hotline = "1900 2101";
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
        footer_name = "ສະຖິຕິທາງດ້ານນິຕິກຳວິຊາການ© 2018 - {DATE_YEAR} ເຈົ້າ ໜາ ທີ່ຮັບຮອງຂັ້ນສອງຂັ້ນພື້ນຖານຂອງປະເທດລາວ";
        footer_name_inner = "2018 - {DATE_YEAR} © ";
        footer_address = "ຢູ່ທີ່: ບ້ານສາຍລົມ, ເມືອງຈັນທະບູລີ, ນະຄອນຫຼວງວຽງຈັນ, ສປປລາວ";
        footer_email = "lanic_office@lanic.la";
        header_hotline = "+856 254150";
        footer_hotline = "+856 254150, ຕູ້ໄປສະນີ: 2225";
    } else if(IsWhichCA === "16") {
        footer_name = "Copyright © 2013 - {DATE_YEAR} SAFECert Corp";
        footer_name_inner = "2018 - {DATE_YEAR} © ";
        footer_address = "Address: X-04.77, North Towers Building, Sunrise City, 27 Nguyen Huu Tho, Tan Hung Ward, District 7, City. Ho Chi Minh City";
        footer_email = "info@safecert.com.vn";
        header_hotline = "(028)-668-23732";
        footer_hotline = "(028)-668-23732";
        global_fm_decision = "Unit Code";
    } else if(IsWhichCA === "17") {
        footer_name = "ສະຖິຕິການລົງທືນທາງດ້ານວິຊາການ© 2018 - {DATE_YEAR} ອົງການວິຊາການວິທີການວິທີການແຫ່ງຊາດລາວ";
        footer_name_inner = "2018 - {DATE_YEAR} © ";
        footer_address = "ຢູ່ທີ່: ບ້ານສາຍລົມ, ເມືອງຈັນທະບູລີ, ນະຄອນຫຼວງວຽງຈັນ, ສປປລາວ";
        footer_email = "lanic_office@lanic.la";
        header_hotline = "+856 254150";
        footer_hotline = "+856 254150, ຕູ້ໄປສະນີ: 2225";
    } else if(IsWhichCA === "18") {
        footer_name = "VIETNAM ONLINE ENTERTAINMENT AND COMMUNICATION CO., LTD";
        footer_name_inner = "2021 - {DATE_YEAR} © ";
        footer_address = "Address: No. 32/21, Truong Cong Giai Street, Dich Vong Ward, Cau Giay District, Hanoi City";
        footer_email = "cskh@i-ca.vn";
        header_hotline = "1900 0099";
        footer_hotline = "1900 0099";
    } else if(IsWhichCA === "19") {
        footer_name = "Copyright © 2019 - {DATE_YEAR} Mobile-ID Technologies and Services Joint Stock Company";
        footer_name_inner = "2019 - {DATE_YEAR} © ";
        footer_address = "Address: 19 Dang Tien Dong, An Phu Ward, District 2nd, Ho Chi Minh City, Vietnam";
        footer_email = "info@mobile-id.vn";
        header_hotline = "1900 6884";
        footer_hotline = "1900 6884";
    } else {}
}