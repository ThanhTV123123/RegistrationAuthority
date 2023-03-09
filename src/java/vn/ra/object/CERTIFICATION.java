/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.object;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Blob;

/**
 *
 * @author THANH-PC
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CERTIFICATION {

    @JsonProperty("ID")
    public int ID;
    @JsonProperty("TOKEN_ID")
    public int TOKEN_ID;
    @JsonProperty("ENABLED")
    public boolean ENABLED;
    @JsonProperty("PUSH_NOTICE_ENABLED")
    public boolean PUSH_NOTICE_ENABLED;
    @JsonProperty("PERSONAL_NAME")
    public String PERSONAL_NAME;
    @JsonProperty("COMPANY_NAME")
    public String COMPANY_NAME;
     @JsonProperty("DOMAIN_NAME")
    public String DOMAIN_NAME;
    @JsonProperty("TAX_CODE")
    public String TAX_CODE;
    @JsonProperty("BUDGET_CODE")
    public String BUDGET_CODE;
    @JsonProperty("DECISION")
    public String DECISION;
    @JsonProperty("P_ID")
    public String P_ID;
    @JsonProperty("PASSPORT")
    public String PASSPORT;
    @JsonProperty("P_EID")
    public String P_EID;
    @JsonProperty("SERVICE_UUID")
    public String SERVICE_UUID;//DEVICE_UUID
    @JsonProperty("CITY_PROVINCE_ID")
    public int CITY_PROVINCE_ID;
    @JsonProperty("CITY_PROVINCE")
    public String CITY_PROVINCE;
    @JsonProperty("CERTIFICATION_ATTR_ID")
    public int CERTIFICATION_ATTR_ID;
    @JsonProperty("CERTIFICATION_AUTHORITY_ID")
    public int CERTIFICATION_AUTHORITY_ID;
    @JsonProperty("CERTIFICATION_AUTHORITY_DESC")
    public String CERTIFICATION_AUTHORITY_DESC;
    @JsonProperty("CERTIFICATION_ATTR_TYPE_ID")
    public int CERTIFICATION_ATTR_TYPE_ID;
    @JsonProperty("CERTIFICATION_ATTR_TYPE_DESC")
    public String CERTIFICATION_ATTR_TYPE_DESC;
    @JsonProperty("CERTIFICATION_ATTR_STATE_ID")
    public int CERTIFICATION_ATTR_STATE_ID;
    @JsonProperty("CERTIFICATION_ATTR_STATE_DESC")
    public String CERTIFICATION_ATTR_STATE_DESC;
    @JsonProperty("CERTIFICATION_STATE_ID")
    public int CERTIFICATION_STATE_ID;
    @JsonProperty("CERTIFICATION_STATE_DESC")
    public String CERTIFICATION_STATE_DESC;
    @JsonProperty("CERTIFICATION_PROFILE_ID")
    public int CERTIFICATION_PROFILE_ID;
    @JsonProperty("CERTIFICATION_PURPOSE_ID")
    public int CERTIFICATION_PURPOSE_ID;
    @JsonProperty("CERTIFICATION_PURPOSE_DESC")
    public String CERTIFICATION_PURPOSE_DESC;
    @JsonProperty("CERTIFICATION_PROFILE_DESC")
    public String CERTIFICATION_PROFILE_DESC;
    @JsonProperty("CERTIFICATION_PROFILE_NAME")
    public String CERTIFICATION_PROFILE_NAME;
    @JsonProperty("DURATION")
    public int DURATION;
    @JsonProperty("CSR")
    public String CSR;
    @JsonProperty("CERTIFICATION")
    public String CERTIFICATION;
    @JsonProperty("TOKEN_SN")
    public String TOKEN_SN;
    @JsonProperty("EFFECTIVE_DT")
    public String EFFECTIVE_DT;
    @JsonProperty("EXPIRATION_DT")
    public String EXPIRATION_DT;
    @JsonProperty("EXPIRATION_DT_DESC")
    public String EXPIRATION_DT_DESC;
    @JsonProperty("EXPIRATION_CONTRACT_DT")
    public String EXPIRATION_CONTRACT_DT;
    @JsonProperty("FEE_AMOUNT")
    public int FEE_AMOUNT;
    @JsonProperty("TOKEN_AMOUNT") //phi token
    public int TOKEN_AMOUNT;
    @JsonProperty("SUBJECT")
    public String SUBJECT;
    @JsonProperty("PAST_SUBJECT")
    public String PAST_SUBJECT;
    @JsonProperty("PAST_PROPERTIES")
    public String PAST_PROPERTIES;
    @JsonProperty("PAST_CITY_PROVINCE_ID")
    public int PAST_CITY_PROVINCE_ID;
    @JsonProperty("ISSUER_SUBJECT")
    public String ISSUER_SUBJECT;
    @JsonProperty("PHONE_CONTRACT")
    public String PHONE_CONTRACT;
    @JsonProperty("PHONE_CONTRACT_REAL")
    public String PHONE_CONTRACT_REAL;
    @JsonProperty("EMAIL_CONTRACT")
    public String EMAIL_CONTRACT;
    @JsonProperty("EMAIL_CONTRACT_REAL")
    public String EMAIL_CONTRACT_REAL;
    @JsonProperty("PAST_CERTIFICATION_ID")
    public int PAST_CERTIFICATION_ID;
    @JsonProperty("BRANCH_ID")
    public int BRANCH_ID;
    @JsonProperty("BRANCH_NAME")
    public String BRANCH_NAME;
    @JsonProperty("BRANCH_DESC")
    public String BRANCH_DESC;
    @JsonProperty("BRANCH_TOKEN_ID")
    public int BRANCH_TOKEN_ID;
    @JsonProperty("BRANCH_TOKEN_DESC")
    public String BRANCH_TOKEN_DESC;
    @JsonProperty("TOKEN_STATE_DESC")
    public String TOKEN_STATE_DESC;
    @JsonProperty("RENEWAL_DT")
    public String RENEWAL_DT;
    @JsonProperty("RENEWAL_BY")
    public String RENEWAL_BY;
    @JsonProperty("CREATED_DT")
    public String CREATED_DT;
    @JsonProperty("CREATED_BY")
    public String CREATED_BY;
    @JsonProperty("MODIFIED_DT")
    public String MODIFIED_DT;
    @JsonProperty("MODIFIED_BY")
    public String MODIFIED_BY;
    @JsonProperty("PUBLIC_KEY")
    public String PUBLIC_KEY;
    @JsonProperty("PUBLIC_KEY_HASH")
    public String PUBLIC_KEY_HASH;
    @JsonProperty("CERTIFICATION_HASH")
    public String CERTIFICATION_HASH;
    @JsonProperty("CERTIFICATION_SN")
    public String CERTIFICATION_SN;
    @JsonProperty("KEY_SIZE")
    public String KEY_SIZE;
    @JsonProperty("PRINT_EFFECTIVE_DT")
    public String PRINT_EFFECTIVE_DT;
    @JsonProperty("PRINT_EXPIRATION_DT")
    public String PRINT_EXPIRATION_DT;
    @JsonProperty("PRINT_EXPIRATION_CONTRACT_DT")
    public String PRINT_EXPIRATION_CONTRACT_DT;
    @JsonProperty("CUSTOMER_CONFIRMATION")
    public String CUSTOMER_CONFIRMATION;
    @JsonProperty("ACTIVATION_CODE")
    public String ACTIVATION_CODE;
    @JsonProperty("ACTIVATION_EXPIRATION_DT")
    public String ACTIVATION_EXPIRATION_DT;
    @JsonProperty("VALUE")
    public String VALUE;
    @JsonProperty("CREATED_BY_ID")
    public int CREATED_BY_ID;
    @JsonProperty("COMMENT")
    public String COMMENT;
    @JsonProperty("PROPERTIES")
    public String PROPERTIES;
    @JsonProperty("PRIVATE_KEY_ENABLED")
    public boolean PRIVATE_KEY_ENABLED;
    @JsonProperty("PRIVATE_KEY")
    public String PRIVATE_KEY;
    @JsonProperty("RELEASE_DT")
    public String RELEASE_DT;
    @JsonProperty("PKI_FORMFACTOR_ID")
    public int PKI_FORMFACTOR_ID;
    @JsonProperty("PKI_FORMFACTOR_NAME")
    public String PKI_FORMFACTOR_NAME;
    @JsonProperty("PKI_FORMFACTOR_DESC")
    public String PKI_FORMFACTOR_DESC;
    @JsonProperty("CERTIFICATION_OWNER_ID")
    public int CERTIFICATION_OWNER_ID;
    @JsonProperty("DISCOUNT_RATE")
    public int DISCOUNT_RATE;
    @JsonProperty("CROSS_CHECK_ENABLED")//trang thai doi soat
    public boolean CROSS_CHECK_ENABLED;
    @JsonProperty("SHARED_MODE")
    public boolean SHARED_MODE;
    @JsonProperty("ISSUED_DT")
    public String ISSUED_DT;
    @JsonProperty("CROSS_CHECKED_DT") //ngay doi soat
    public String CROSS_CHECKED_DT;
    @JsonProperty("CROSS_CHECKED_MOUNTH")
    public String CROSS_CHECKED_MOUNTH;
//    @JsonProperty("COLLECTED_BRIEF_DT")
//    public String COLLECTED_BRIEF_DT;
    @JsonProperty("BRIEF_PROPERTIES")
    public String BRIEF_PROPERTIES;
    @JsonProperty("BRIEF_TYPE")
    public boolean BRIEF_TYPE;
    @JsonProperty("COLLECT_ENABLED")//trang thai doi soat
    public boolean COLLECT_ENABLED;
    @JsonProperty("REVOKED_DT")
    public String REVOKED_DT;
    @JsonProperty("FINE_FOR_LACK_OF_BRIEF")// tien phat ho so
    public int FINE_FOR_LACK_OF_BRIEF;
    @JsonProperty("TOKEN_NUMBER")// so luong token
    public int TOKEN_NUMBER;
    @JsonProperty("NUMBER_DECLINED_DAYS")// so ngay huy
    public int NUMBER_DECLINED_DAYS;
    @JsonProperty("DECLINED_DT")// ngay huy
    public String DECLINED_DT;
    @JsonProperty("APPROVAL_CA_DT")// ngay huy
    public String APPROVAL_CA_DT;
    // profile manager
    @JsonProperty("COLLECT_SOFTCOPY")
    public boolean COLLECT_SOFTCOPY;
    @JsonProperty("COMMIT_ENABLED")
    public boolean COMMIT_ENABLED;
    @JsonProperty("RECEIVED_BRIEF_DATE")
    public String RECEIVED_BRIEF_DATE;
    @JsonProperty("ADDRESS")
    public String ADDRESS;
    @JsonProperty("REPRESENTATIVE_EMAIL")
    public String REPRESENTATIVE_EMAIL;
    @JsonProperty("REPRESENTATIVE_PHONE")
    public String REPRESENTATIVE_PHONE;
    @JsonProperty("PROFILE_CONTACT_INFO")
    public String PROFILE_CONTACT_INFO;
    @JsonProperty("REPRESENTATIVE_NAME")
    public String REPRESENTATIVE_NAME;
    @JsonProperty("CONTACT_NAME")
    public String CONTACT_NAME;
    @JsonProperty("FILE_MANAGER_STATE_ID")
    public int FILE_MANAGER_STATE_ID;
    @JsonProperty("SERVICE_TYPE_DESC")
    public String SERVICE_TYPE_DESC;
    @JsonProperty("OPERATED_DT")
    public String OPERATED_DT;
    @JsonProperty("COLLECTED_FULL_BRIEF_DT")
    public String COLLECTED_FULL_BRIEF_DT;
    @JsonProperty("RECEIVED_BRIEF_DT")
    public String RECEIVED_BRIEF_DT;
    @JsonProperty("FILE_MANAGER_STATE_DESC")
    public String FILE_MANAGER_STATE_DESC;
    @JsonProperty("CREATED_LOCK_REQUEST_DT")
    public String CREATED_LOCK_REQUEST_DT;
    @JsonProperty("CERT_PROFILE_DURATION")
    public int CERT_PROFILE_DURATION;
    @JsonProperty("CERT_PROFILE_PROMOTION")
    public int CERT_PROFILE_PROMOTION;
    @JsonProperty("CORE_ERROR_DESCRIPTION")
    public String CORE_ERROR_DESCRIPTION;
    @JsonProperty("TOKEN_WAIT_TO_LOCK")
    public String TOKEN_WAIT_TO_LOCK;
    @JsonProperty("TOKEN_WAIT_TO_UNLOCK")
    public String TOKEN_WAIT_TO_UNLOCK;
    @JsonProperty("USER_CREATE_UNLOCK_LOCK")
    public String USER_CREATE_UNLOCK_LOCK;
    @JsonProperty("RESIGNING_CONFIRMATION_PAPER_ENABLED")
    public String RESIGNING_CONFIRMATION_PAPER_ENABLED;
    @JsonProperty("NO_CANCEL_COMMITMENT")
    public String NO_CANCEL_COMMITMENT;
    @JsonProperty("CONFIRMATION_PROPERTIES")
    public String CONFIRMATION_PROPERTIES;
    @JsonProperty("ACTIVATED_ENABLED")
    public int ACTIVATED_ENABLED;
    @JsonProperty("PERSONAL_ID")
    public String PERSONAL_ID;
    @JsonProperty("ENTERPRISE_ID")
    public String ENTERPRISE_ID;
    @JsonProperty("PERSONAL_ID_REMARK")
    public String PERSONAL_ID_REMARK;
    @JsonProperty("ENTERPRISE_ID_REMARK")
    public String ENTERPRISE_ID_REMARK;
    @JsonProperty("PROFILE_NOTE")
    public String PROFILE_NOTE;
    @JsonProperty("REVOCATION_REASON")
    public String REVOCATION_REASON;
    @JsonProperty("CERTIFICATE_THUMBPRINT")
    public String CERTIFICATE_THUMBPRINT;
    @JsonProperty("REMAINING_SIGNING_COUNTER")
    public String REMAINING_SIGNING_COUNTER;
    @JsonProperty("INFO_BRIEF")
    public String INFO_BRIEF;
    @JsonProperty("BUSINESS_LICENSE_TYPE_ID")
    public int BUSINESS_LICENSE_TYPE_ID;
    @JsonProperty("BRANCH_LEVEL_1_NAME")
    public String BRANCH_LEVEL_1_NAME;
}
