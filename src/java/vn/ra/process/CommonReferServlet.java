/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import com.google.gson.Gson;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import vn.mobileid.esigncloud.management.AgreementDetails;
import vn.ra.object.BACKOFFICE_USER;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_PROFILE;
import vn.ra.object.CertificateBriefInfo;
import vn.ra.object.CertificateExpireSoonInfo;
import vn.ra.object.CertificateInfo;
import vn.ra.object.CertificateReportInfo;
import vn.ra.object.DISCOUNT_RATE_PROFILE;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.PREFIX_UUID;
import vn.ra.object.PROFILE_DISCOUNT_RATE_DATA;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.LoadParamSystem;
import vn.ra.utility.PropertiesContent;

/**
 *
 * @author USER
 */
public class CommonReferServlet {

    // approve for issue, buy_more renew cert
    public static void updateDiscountRate(String pStringCERTIFICATE_ID, String sBRANCH_ID, String sCertProfileID,
            String pTAX_CODE, String pBUDGET_CODE, String pP_ID, String pPASSPORT,
            String loginUID, String pCCCD, String pDECISION, String sEnterpriseCert, String sPersonalCert) {
        try {
//            System.out.println(pStringCERTIFICATE_ID);
            ConnectDatabase db = new ConnectDatabase();
            String sDiscountRate = "0";
            DISCOUNT_RATE_PROFILE[][] rsDisCount = new DISCOUNT_RATE_PROFILE[1][];
            db.S_BO_BRANCH_GET_DISCOUNT_RATE_PROFILE(sBRANCH_ID, rsDisCount);
            if (rsDisCount[0].length > 0 && !"".equals(rsDisCount[0][0].PROPERTIES)) {
                PROFILE_DISCOUNT_RATE_DATA[][] resIPData = new PROFILE_DISCOUNT_RATE_DATA[1][];
                CommonFunction.getAllProfileDiscountRate(rsDisCount[0][0].PROPERTIES, resIPData);
                if (resIPData[0] != null && resIPData[0].length > 0) {
                    String sProfileCode = "";
                    CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                    db.S_BO_CERTIFICATION_PROFILE_DETAIL(sCertProfileID, rsProfile);
                    if (rsProfile[0].length > 0) {
                        sProfileCode = EscapeUtils.CheckTextNull(rsProfile[0][0].NAME);
                    }
                    for (PROFILE_DISCOUNT_RATE_DATA resIPData1 : resIPData[0]) {
                        if (EscapeUtils.CheckTextNull(resIPData1.profileName).equals(sProfileCode)) {
                            sDiscountRate = resIPData1.rosePercent;
                            break;
                        }
                    }
                }
            }
            if (!"0".equals(sDiscountRate)) {
//                String[] sUIDResult = new String[2];
//                collectFieldToUID(pTAX_CODE, pBUDGET_CODE, pDECISION, pP_ID, pPASSPORT, pCCCD, sUIDResult);
//                String sEnterpriseCert = sUIDResult[0];
//                String sPersonalCert = sUIDResult[1];
                db.S_BO_CERTIFICATION_UPDATE(Integer.parseInt(pStringCERTIFICATE_ID), sCertProfileID, "", "", "",
                        pTAX_CODE.trim(), pBUDGET_CODE.trim(), pP_ID.trim(), pPASSPORT.trim(), "", "", "", "", "",
                        "", loginUID, "", "", "", sDiscountRate, "", pCCCD, pDECISION, sEnterpriseCert, sPersonalCert);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // approve for issue, buy_more renew import cert
    public static void updateDiscountRateImportCert(String pStringCERTIFICATE_ID, String sUsername, String sCertProfileCode,
            String pTAX_CODE, String pBUDGET_CODE, String pP_ID, String pPASSPORT, String loginUID, String pCCCD, String pDECISION,
            String sEnterpriseCert, String sPersonalCert) {
        try {
//            System.out.println(pStringCERTIFICATE_ID);
            ConnectDatabase db = new ConnectDatabase();
            String sDiscountRate = "0";
            String sProfileID = "0";
            String sBRANCH_ID = "0";
//            if ("1".equals(sDiscountRateOption)) {
            CERTIFICATION_PROFILE[][] resProfileDB = new CERTIFICATION_PROFILE[1][];
            db.S_BO_API_CERTIFICATION_PROFILE_GET_INFO(sCertProfileCode.trim(), resProfileDB);
            if (resProfileDB[0].length > 0) {
                sProfileID = String.valueOf(resProfileDB[0][0].ID);
            }
            BACKOFFICE_USER[][] rsUser = new BACKOFFICE_USER[1][];
            db.S_BO_USER_GET_BY_USERNAME(sUsername.trim(), rsUser);
            if (rsUser[0].length > 0) {
                sBRANCH_ID = String.valueOf(rsUser[0][0].BRANCH_ID);
            }
            DISCOUNT_RATE_PROFILE[][] rsDisCount = new DISCOUNT_RATE_PROFILE[1][];
            db.S_BO_BRANCH_GET_DISCOUNT_RATE_PROFILE(sBRANCH_ID, rsDisCount);
            if (rsDisCount[0].length > 0 && !"".equals(rsDisCount[0][0].PROPERTIES)) {
                PROFILE_DISCOUNT_RATE_DATA[][] resIPData = new PROFILE_DISCOUNT_RATE_DATA[1][];
                CommonFunction.getAllProfileDiscountRate(rsDisCount[0][0].PROPERTIES, resIPData);
                if (resIPData[0] != null && resIPData[0].length > 0) {
//                        String sProfileCode = "";
//                        CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
//                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(sCertProfileID, rsProfile);
//                        if (rsProfile[0].length > 0) {
//                            sProfileCode = EscapeUtils.CheckTextNull(rsProfile[0][0].NAME);
//                        }
                    for (PROFILE_DISCOUNT_RATE_DATA resIPData1 : resIPData[0]) {
                        if (EscapeUtils.CheckTextNull(resIPData1.profileName).equals(sCertProfileCode)) {
                            sDiscountRate = resIPData1.rosePercent;
                            break;
                        }
                    }
                }
            }
//            }
            if (!"0".equals(sDiscountRate)) {
                db.S_BO_CERTIFICATION_UPDATE(Integer.parseInt(pStringCERTIFICATE_ID), sProfileID, "", "", "",
                        pTAX_CODE.trim(), pBUDGET_CODE.trim(), pP_ID.trim(), pPASSPORT.trim(), "", "", "", "", "",
                        "", loginUID, "", "", "", sDiscountRate, "", pCCCD, pDECISION, sEnterpriseCert, sPersonalCert);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void separateUIDToField(String sENTERPRISE_ID, String sPERSONAL_ID, CERTIFICATION tempItem) {
        if (!"".equals(sENTERPRISE_ID)) {
            String prefixMST = Definitions.CONFIG_CERTIFICATION_PREFIX_TAXCODE;
            if (sENTERPRISE_ID.split(":")[0].equals(prefixMST)) {
                tempItem.TAX_CODE = sENTERPRISE_ID.replace(prefixMST + ":", "").trim();
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.TAX_CODE))) {
                String prefixMNS = Definitions.CONFIG_CERTIFICATION_PREFIX_BUDGET_CODE;
                if (sENTERPRISE_ID.split(":")[0].equals(prefixMNS)) {
                    tempItem.BUDGET_CODE = sENTERPRISE_ID.replace(prefixMNS + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.BUDGET_CODE))) {
                String prefixGD = Definitions.CONFIG_CERTIFICATION_PREFIX_DECISION;
                if (sENTERPRISE_ID.split(":")[0].equals(prefixGD)) {
                    tempItem.DECISION = sENTERPRISE_ID.replace(prefixGD + ":", "").trim();
                }
            }
        }
        if (!"".equals(sPERSONAL_ID)) {
            String prefixPID = Definitions.CONFIG_CERTIFICATION_PREFIX_PERSONAL_CODE;
            if (sPERSONAL_ID.split(":")[0].equals(prefixPID)) {
                tempItem.P_ID = sPERSONAL_ID.replace(prefixPID + ":", "").trim();
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.P_ID))) {
                String prefixHC = Definitions.CONFIG_CERTIFICATION_PREFIX_PASSPORT;
                if (sPERSONAL_ID.split(":")[0].equals(prefixHC)) {
                    tempItem.PASSPORT = sPERSONAL_ID.replace(prefixHC + ":", "").trim();
                }
                if ("".equals(EscapeUtils.CheckTextNull(tempItem.PASSPORT))) {
                    String prefixCCCD = Definitions.CONFIG_CERTIFICATION_PREFIX_CITIZEN_CODE;
                    if (sPERSONAL_ID.split(":")[0].equals(prefixCCCD)) {
                        tempItem.P_EID = sPERSONAL_ID.replace(prefixCCCD + ":", "").trim();
                    }
                }
            }
        }
    }

    public static void separateUIDToBriefField(String sENTERPRISE_ID, String sPERSONAL_ID, CertificateBriefInfo tempItem) {
        if (!"".equals(sENTERPRISE_ID)) {
            String prefixMST = Definitions.CONFIG_CERTIFICATION_PREFIX_TAXCODE;
            if (sENTERPRISE_ID.split(":")[0].equals(prefixMST)) {
                tempItem.taxCode = sENTERPRISE_ID.replace(prefixMST + ":", "").trim();
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.taxCode))) {
                String prefixMNS = Definitions.CONFIG_CERTIFICATION_PREFIX_BUDGET_CODE;
                if (sENTERPRISE_ID.split(":")[0].equals(prefixMNS)) {
                    tempItem.budgetCode = sENTERPRISE_ID.replace(prefixMNS + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.budgetCode))) {
                String prefixGD = Definitions.CONFIG_CERTIFICATION_PREFIX_DECISION;
                if (sENTERPRISE_ID.split(":")[0].equals(prefixGD)) {
                    tempItem.decision = sENTERPRISE_ID.replace(prefixGD + ":", "").trim();
                }
            }
        }
        if (!"".equals(sPERSONAL_ID)) {
            String prefixPID = Definitions.CONFIG_CERTIFICATION_PREFIX_PERSONAL_CODE;
            if (sPERSONAL_ID.split(":")[0].equals(prefixPID)) {
                tempItem.pid = sPERSONAL_ID.replace(prefixPID + ":", "").trim();
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.pid))) {
                String prefixHC = Definitions.CONFIG_CERTIFICATION_PREFIX_PASSPORT;
                if (sPERSONAL_ID.split(":")[0].equals(prefixHC)) {
                    tempItem.passport = sPERSONAL_ID.replace(prefixHC + ":", "").trim();
                }
                if ("".equals(EscapeUtils.CheckTextNull(tempItem.passport))) {
                    String prefixCCCD = Definitions.CONFIG_CERTIFICATION_PREFIX_CITIZEN_CODE;
                    if (sPERSONAL_ID.split(":")[0].equals(prefixCCCD)) {
                        tempItem.citizenId = sPERSONAL_ID.replace(prefixCCCD + ":", "").trim();
                    }
                }
            }
        }
    }

    public static void separateUIDGetInfoAPI(String sENTERPRISE_ID, String sPERSONAL_ID, CertificateInfo tempItem)
    {
        if (!"".equals(sENTERPRISE_ID)) {
            String prefixMST = Definitions.CONFIG_CERTIFICATION_PREFIX_TAXCODE;
            if (sENTERPRISE_ID.split(":")[0].equals(prefixMST)) {
                tempItem.taxCode = sENTERPRISE_ID.replace(prefixMST + ":", "").trim();
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.taxCode))) {
                String prefixMNS = Definitions.CONFIG_CERTIFICATION_PREFIX_BUDGET_CODE;
                if (sENTERPRISE_ID.split(":")[0].equals(prefixMNS)) {
                    tempItem.budgetCode = sENTERPRISE_ID.replace(prefixMNS + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.budgetCode))) {
                String prefixGD = Definitions.CONFIG_CERTIFICATION_PREFIX_DECISION;
                if (sENTERPRISE_ID.split(":")[0].equals(prefixGD)) {
                    tempItem.decision = sENTERPRISE_ID.replace(prefixGD + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.decision))) {
                String prefix = Definitions.CONFIG_CERTIFICATION_PREFIX_SOCIAL_INSURANCE_CODE;
                if (sENTERPRISE_ID.split(":")[0].equals(prefix)) {
                    tempItem.socialInsuranceCode = sENTERPRISE_ID.replace(prefix + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.socialInsuranceCode))) {
                String prefix = Definitions.CONFIG_CERTIFICATION_PREFIX_UNIT_CODE;
                if (sENTERPRISE_ID.split(":")[0].equals(prefix)) {
                    tempItem.unitCode = sENTERPRISE_ID.replace(prefix + ":", "").trim();
                }
            }
        }
        if (!"".equals(sPERSONAL_ID)) {
            String prefixPID = Definitions.CONFIG_CERTIFICATION_PREFIX_PERSONAL_CODE;
            if (sPERSONAL_ID.split(":")[0].equals(prefixPID)) {
                tempItem.pid = sPERSONAL_ID.replace(prefixPID + ":", "").trim();
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.pid))) {
                String prefixHC = Definitions.CONFIG_CERTIFICATION_PREFIX_PASSPORT;
                if (sPERSONAL_ID.split(":")[0].equals(prefixHC)) {
                    tempItem.passport = sPERSONAL_ID.replace(prefixHC + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.passport))) {
                String prefixCCCD = Definitions.CONFIG_CERTIFICATION_PREFIX_CITIZEN_CODE;
                if (sPERSONAL_ID.split(":")[0].equals(prefixCCCD)) {
                    tempItem.citizenId = sPERSONAL_ID.replace(prefixCCCD + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.citizenId))) {
                String prefix = Definitions.CONFIG_CERTIFICATION_PREFIX_TAXCODE;
                if (sPERSONAL_ID.split(":")[0].equals(prefix)) {
                    tempItem.personalTaxCode = sPERSONAL_ID.replace(prefix + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.personalTaxCode))) {
                String prefix = Definitions.CONFIG_CERTIFICATION_PREFIX_SOCIAL_INSURANCE_CODE;
                if (sPERSONAL_ID.split(":")[0].equals(prefix)) {
                    tempItem.personalSocialInsuranceCode = sPERSONAL_ID.replace(prefix + ":", "").trim();
                }
            }
        }
    }
    
    public static void separateUIDReportCertAPI(String sENTERPRISE_ID, String sPERSONAL_ID, CertificateReportInfo tempItem)
    {
        if (!"".equals(sENTERPRISE_ID)) {
            String prefixMST = Definitions.CONFIG_CERTIFICATION_PREFIX_TAXCODE;
            if (sENTERPRISE_ID.split(":")[0].equals(prefixMST)) {
                tempItem.taxCode = sENTERPRISE_ID.replace(prefixMST + ":", "").trim();
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.taxCode))) {
                String prefixMNS = Definitions.CONFIG_CERTIFICATION_PREFIX_BUDGET_CODE;
                if (sENTERPRISE_ID.split(":")[0].equals(prefixMNS)) {
                    tempItem.budgetCode = sENTERPRISE_ID.replace(prefixMNS + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.budgetCode))) {
                String prefixGD = Definitions.CONFIG_CERTIFICATION_PREFIX_DECISION;
                if (sENTERPRISE_ID.split(":")[0].equals(prefixGD)) {
                    tempItem.decision = sENTERPRISE_ID.replace(prefixGD + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.decision))) {
                String prefix = Definitions.CONFIG_CERTIFICATION_PREFIX_SOCIAL_INSURANCE_CODE;
                if (sENTERPRISE_ID.split(":")[0].equals(prefix)) {
                    tempItem.socialInsuranceCode = sENTERPRISE_ID.replace(prefix + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.socialInsuranceCode))) {
                String prefix = Definitions.CONFIG_CERTIFICATION_PREFIX_UNIT_CODE;
                if (sENTERPRISE_ID.split(":")[0].equals(prefix)) {
                    tempItem.unitCode = sENTERPRISE_ID.replace(prefix + ":", "").trim();
                }
            }
        }
        if (!"".equals(sPERSONAL_ID)) {
            String prefixPID = Definitions.CONFIG_CERTIFICATION_PREFIX_PERSONAL_CODE;
            if (sPERSONAL_ID.split(":")[0].equals(prefixPID)) {
                tempItem.pid = sPERSONAL_ID.replace(prefixPID + ":", "").trim();
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.pid))) {
                String prefixHC = Definitions.CONFIG_CERTIFICATION_PREFIX_PASSPORT;
                if (sPERSONAL_ID.split(":")[0].equals(prefixHC)) {
                    tempItem.passport = sPERSONAL_ID.replace(prefixHC + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.passport))) {
                String prefixCCCD = Definitions.CONFIG_CERTIFICATION_PREFIX_CITIZEN_CODE;
                if (sPERSONAL_ID.split(":")[0].equals(prefixCCCD)) {
                    tempItem.citizenId = sPERSONAL_ID.replace(prefixCCCD + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.citizenId))) {
                String prefix = Definitions.CONFIG_CERTIFICATION_PREFIX_TAXCODE;
                if (sPERSONAL_ID.split(":")[0].equals(prefix)) {
                    tempItem.personalTaxCode = sPERSONAL_ID.replace(prefix + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.personalTaxCode))) {
                String prefix = Definitions.CONFIG_CERTIFICATION_PREFIX_SOCIAL_INSURANCE_CODE;
                if (sPERSONAL_ID.split(":")[0].equals(prefix)) {
                    tempItem.personalSocialInsuranceCode = sPERSONAL_ID.replace(prefix + ":", "").trim();
                }
            }
        }
    }

    public static void separateUIDToAPIExpireField(String sENTERPRISE_ID, String sPERSONAL_ID, CertificateExpireSoonInfo tempItem) {
        if (!"".equals(sENTERPRISE_ID)) {
            String prefixMST = Definitions.CONFIG_CERTIFICATION_PREFIX_TAXCODE;
            if (sENTERPRISE_ID.split(":")[0].equals(prefixMST)) {
                tempItem.taxCode = sENTERPRISE_ID.replace(prefixMST + ":", "").trim();
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.taxCode))) {
                String prefixMNS = Definitions.CONFIG_CERTIFICATION_PREFIX_BUDGET_CODE;
                if (sENTERPRISE_ID.split(":")[0].equals(prefixMNS)) {
                    tempItem.budgetCode = sENTERPRISE_ID.replace(prefixMNS + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.budgetCode))) {
                String prefixGD = Definitions.CONFIG_CERTIFICATION_PREFIX_DECISION;
                if (sENTERPRISE_ID.split(":")[0].equals(prefixGD)) {
                    tempItem.decision = sENTERPRISE_ID.replace(prefixGD + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.decision))) {
                String prefixGD = Definitions.CONFIG_CERTIFICATION_PREFIX_SOCIAL_INSURANCE_CODE;
                if (sENTERPRISE_ID.split(":")[0].equals(prefixGD)) {
                    tempItem.socialInsuranceCode = sENTERPRISE_ID.replace(prefixGD + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.socialInsuranceCode))) {
                String prefixGD = Definitions.CONFIG_CERTIFICATION_PREFIX_UNIT_CODE;
                if (sENTERPRISE_ID.split(":")[0].equals(prefixGD)) {
                    tempItem.unitCode = sENTERPRISE_ID.replace(prefixGD + ":", "").trim();
                }
            }
        }
        if (!"".equals(sPERSONAL_ID)) {
            String prefixPID = Definitions.CONFIG_CERTIFICATION_PREFIX_PERSONAL_CODE;
            if (sPERSONAL_ID.split(":")[0].equals(prefixPID)) {
                tempItem.pid = sPERSONAL_ID.replace(prefixPID + ":", "").trim();
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.pid))) {
                String prefixHC = Definitions.CONFIG_CERTIFICATION_PREFIX_PASSPORT;
                if (sPERSONAL_ID.split(":")[0].equals(prefixHC)) {
                    tempItem.passport = sPERSONAL_ID.replace(prefixHC + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.passport))) {
                String prefixCCCD = Definitions.CONFIG_CERTIFICATION_PREFIX_CITIZEN_CODE;
                if (sPERSONAL_ID.split(":")[0].equals(prefixCCCD)) {
                    tempItem.citizenId = sPERSONAL_ID.replace(prefixCCCD + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.citizenId))) {
                String prefix = Definitions.CONFIG_CERTIFICATION_PREFIX_TAXCODE;
                if (sPERSONAL_ID.split(":")[0].equals(prefix)) {
                    tempItem.personalTaxCode = sPERSONAL_ID.replace(prefix + ":", "").trim();
                }
            }
            if ("".equals(EscapeUtils.CheckTextNull(tempItem.personalTaxCode))) {
                String prefix = Definitions.CONFIG_CERTIFICATION_PREFIX_SOCIAL_INSURANCE_CODE;
                if (sPERSONAL_ID.split(":")[0].equals(prefix)) {
                    tempItem.personalSocialInsuranceCode = sPERSONAL_ID.replace(prefix + ":", "").trim();
                }
            }
        }
    }

    public static void separateUIDToSumField(String sENTERPRISE_ID, String sPERSONAL_ID, String[] sResultSum) {
        sResultSum[0] = "";
        sResultSum[1] = "";
        if (!"".equals(sENTERPRISE_ID)) {
            String prefixMST = Definitions.CONFIG_CERTIFICATION_PREFIX_TAXCODE;
            if (sENTERPRISE_ID.split(":")[0].equals(prefixMST)) {
                sResultSum[0] = sENTERPRISE_ID.replace(prefixMST + ":", "").trim();
            }
            if ("".equals(sResultSum[0])) {
                String prefixMNS = Definitions.CONFIG_CERTIFICATION_PREFIX_BUDGET_CODE;
                if (sENTERPRISE_ID.split(":")[0].equals(prefixMNS)) {
                    sResultSum[0] = sENTERPRISE_ID.replace(prefixMNS + ":", "").trim();
                }
            }
            if ("".equals(sResultSum[0])) {
                String prefixGD = Definitions.CONFIG_CERTIFICATION_PREFIX_DECISION;
                if (sENTERPRISE_ID.split(":")[0].equals(prefixGD)) {
                    sResultSum[0] = sENTERPRISE_ID.replace(prefixGD + ":", "").trim();
                }
            }
        }
        if (!"".equals(sPERSONAL_ID)) {
            String prefixPID = Definitions.CONFIG_CERTIFICATION_PREFIX_PERSONAL_CODE;
            if (sPERSONAL_ID.split(":")[0].equals(prefixPID)) {
                sResultSum[1] = sPERSONAL_ID.replace(prefixPID + ":", "").trim();
            }
            if ("".equals(sResultSum[1])) {
                String prefixHC = Definitions.CONFIG_CERTIFICATION_PREFIX_PASSPORT;
                if (sPERSONAL_ID.split(":")[0].equals(prefixHC)) {
                    sResultSum[1] = sPERSONAL_ID.replace(prefixHC + ":", "").trim();
                }
                if ("".equals(sResultSum[1])) {
                    String prefixCCCD = Definitions.CONFIG_CERTIFICATION_PREFIX_CITIZEN_CODE;
                    if (sPERSONAL_ID.split(":")[0].equals(prefixCCCD)) {
                        sResultSum[1] = sPERSONAL_ID.replace(prefixCCCD + ":", "").trim();
                    }
                }
            }
        }
    }

    public static void collectFieldToUID(String pTAX_CODE, String pBUDGET_CODE, String pDECISION,
            String pP_ID, String pPASSPORT, String pCCCD, String[] sResult) {
        String sEnterpriseCert = "";
        String sPersonalCert = "";
        if (!"".equals(pTAX_CODE)) {
            sEnterpriseCert = Definitions.CONFIG_CERTIFICATION_PREFIX_TAXCODE + ":" + pTAX_CODE;
        }
        if (!"".equals(pBUDGET_CODE)) {
            sEnterpriseCert = Definitions.CONFIG_CERTIFICATION_PREFIX_BUDGET_CODE + ":" + pBUDGET_CODE;
        }
        if (!"".equals(pDECISION)) {
            if("2".equals(Definitions.CONFIG_LANGUAGE_TEMPLATE_UID_PREFIX)){
                sEnterpriseCert = Definitions.CONFIG_CERTIFICATION_PREFIX_UNIT_CODE + ":" + pDECISION;
            } else {
                sEnterpriseCert = Definitions.CONFIG_CERTIFICATION_PREFIX_DECISION + ":" + pDECISION;
            }
        }
        if (!"".equals(pP_ID)) {
            sPersonalCert = Definitions.CONFIG_CERTIFICATION_PREFIX_PERSONAL_CODE + ":" + pP_ID;
        }
        if (!"".equals(pPASSPORT)) {
            sPersonalCert = Definitions.CONFIG_CERTIFICATION_PREFIX_PASSPORT + ":" + pPASSPORT;
        }
        if (!"".equals(pCCCD)) {
            sPersonalCert = Definitions.CONFIG_CERTIFICATION_PREFIX_CITIZEN_CODE + ":" + pCCCD;
        }
        sResult[0] = sEnterpriseCert;
        sResult[1] = sPersonalCert;
    }
    
    public static void collectFieldToUIDAPI(String pTAX_CODE, String pBUDGET_CODE, String pDECISION, String sSOCIAL_INSURANCE, String sUNIT_CODE,
        String pP_ID, String pPASSPORT, String pCCCD, String pTAX_CODE_CN, String sSOCIAL_INSURANCE_CN, String[] sResult) {
        String sEnterpriseCert = "";
        String sPersonalCert = "";
        if (!"".equals(pTAX_CODE)) {
            sEnterpriseCert = Definitions.CONFIG_CERTIFICATION_PREFIX_TAXCODE + ":" + pTAX_CODE;
        }
        if (!"".equals(pBUDGET_CODE)) {
            sEnterpriseCert = Definitions.CONFIG_CERTIFICATION_PREFIX_BUDGET_CODE + ":" + pBUDGET_CODE;
        }
        if (!"".equals(pDECISION)) {
            sEnterpriseCert = Definitions.CONFIG_CERTIFICATION_PREFIX_DECISION + ":" + pDECISION;
        }
        if (!"".equals(sSOCIAL_INSURANCE)) {
            sEnterpriseCert = Definitions.CONFIG_CERTIFICATION_PREFIX_SOCIAL_INSURANCE_CODE + ":" + sSOCIAL_INSURANCE;
        }
        if (!"".equals(sUNIT_CODE)) {
            sEnterpriseCert = Definitions.CONFIG_CERTIFICATION_PREFIX_UNIT_CODE + ":" + sUNIT_CODE;
        }
        if (!"".equals(pP_ID)) {
            sPersonalCert = Definitions.CONFIG_CERTIFICATION_PREFIX_PERSONAL_CODE + ":" + pP_ID;
        }
        if (!"".equals(pPASSPORT)) {
            sPersonalCert = Definitions.CONFIG_CERTIFICATION_PREFIX_PASSPORT + ":" + pPASSPORT;
        }
        if (!"".equals(pCCCD)) {
            sPersonalCert = Definitions.CONFIG_CERTIFICATION_PREFIX_CITIZEN_CODE + ":" + pCCCD;
        }
        if (!"".equals(pTAX_CODE_CN)) {
            sPersonalCert = Definitions.CONFIG_CERTIFICATION_PREFIX_TAXCODE + ":" + pTAX_CODE_CN;
        }
        if (!"".equals(sSOCIAL_INSURANCE_CN)) {
            sPersonalCert = Definitions.CONFIG_CERTIFICATION_PREFIX_SOCIAL_INSURANCE_CODE + ":" + sSOCIAL_INSURANCE_CN;
        }
        sResult[0] = sEnterpriseCert;
        sResult[1] = sPersonalCert;
    }

    public static void actionSendMailHSM(GENERAL_POLICY[][] sessGeneralPolicy1, String sCERTIFICATION_ID, String DN,
        String strEmailCust, String sLanguage) {
        try {
            String templateActiveSignServer = "";
            String expireActiveSignServer = "";
            String urlActiveSignServer = "";
            if (sessGeneralPolicy1[0].length > 0) {
                for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy1[0]) {
                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_EMAIL_TEMPLATE_CERTIFICATION_SIGNSERVER_CONFIRM)) {
                        templateActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                    }
                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_EXPIRATION_DATE_SIGNSERVER_CONFIRM)) {
                        expireActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                    }
                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_URL_CERTIFICATION_SIGNSERVER_CONFIRM)) {
                        urlActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                    }
                }
            }
            String sSubject = PropertiesContent.getPropertiesUtf8ContentKey(templateActiveSignServer, Definitions.CONFIG_EMAIL_FO_EMAIL_TEMPLATE_AUTHENTICATION_CODE_SUBJECT);
            String sContent = PropertiesContent.getPropertiesUtf8ContentKey(templateActiveSignServer, Definitions.CONFIG_EMAIL_FO_EMAIL_TEMPLATE_AUTHENTICATION_CODE_CONTENT);
            if (!"".equals(urlActiveSignServer)) {
                String sKeyValue = CommonFunction.getKeyCertConfirm(sCERTIFICATION_ID, Integer.parseInt(expireActiveSignServer), sLanguage);
                urlActiveSignServer = urlActiveSignServer.replace(Definitions.CONFIG_EMAIL_URL_TAG_KEY_VALUE, URLEncoder.encode(sKeyValue));
                sContent = sContent.replace(Definitions.CONFIG_EMAIL_TEMPLATE_TAG_EXPIRATION_DURATION, expireActiveSignServer);
                sContent = sContent.replace(Definitions.CONFIG_EMAIL_TEMPLATE_TAG_SUBJECT_DN, DN);
                sContent = sContent.replace(Definitions.CONFIG_EMAIL_TEMPLATE_TAG_ACTIVE_URL, urlActiveSignServer);
                int[] intRes = new int[1];
                String[] sRes = new String[1];
                ConnectConnector.SendMailConfirmHSM(sSubject, sContent, strEmailCust, intRes, sRes);
            }
        } catch (Exception e) {
            CommonFunction.LogExceptionJSP(null, "actionSendMailHSM: " + e.getMessage(), e);
        }
    }
    
    public static String filterPrefixUIDAuto(String sUID, PREFIX_UUID[][] rsPrefix) {
        if(!"".equals(sUID)) {
            for (PREFIX_UUID rsPrefix1 : rsPrefix[0]) {
                if(sUID.contains(rsPrefix1.PREFIX)) {
                    sUID = sUID.replace(rsPrefix1.PREFIX, "");
                }
            }
        }
        return sUID;
    }
    
    public static String filterPrefixUIDLanguage(String sUID, String sLanguage) {
        if(!"".equals(sUID)) {
            String sListPrefixJson = LoadParamSystem.getParamStart(Definitions.CONFIG_KEY_PREFIX_ENTERPRISE_JSON);
            PREFIX_UUID[] prefixArray = new Gson().fromJson(sListPrefixJson, PREFIX_UUID[].class);
            List<PREFIX_UUID> prefixList = Arrays.asList(prefixArray);
            if("1".equals(sLanguage)) {
                for(int j=0; j<prefixList.size(); j++) {
                    if((sUID.split(":")[0] + ":").equals(prefixList.get(j).PREFIX)) {
                        sUID = sUID.replace(prefixList.get(j).PREFIX, "");
                        break;
                    }
                }
            } else {
                for(int j=0; j<prefixList.size(); j++) {
                    if((sUID.split(":")[0] + ":").equals(prefixList.get(j).PREFIX_DB)) {
                        sUID = sUID.replace(prefixList.get(j).PREFIX_DB, "");
                        break;
                    }
                }
            }
        }
        return sUID;
    }
    
    public static String filterPrefixCNUIDLanguage(String sUID, String sLanguage) {
        if(!"".equals(sUID)) {
            String sListPrefixJson = LoadParamSystem.getParamStart(Definitions.CONFIG_KEY_PREFIX_PERSONAL_JSON);
            PREFIX_UUID[] prefixArray = new Gson().fromJson(sListPrefixJson, PREFIX_UUID[].class);
            List<PREFIX_UUID> prefixList = Arrays.asList(prefixArray);
            if("1".equals(sLanguage)) {
                for(int j=0; j<prefixList.size(); j++) {
                    if((sUID.split(":")[0] + ":").equals(prefixList.get(j).PREFIX)) {
                        sUID = sUID.replace(prefixList.get(j).PREFIX, "");
                        break;
                    }
                }
            } else {
                for(int j=0; j<prefixList.size(); j++) {
                    if((sUID.split(":")[0] + ":").equals(prefixList.get(j).PREFIX_DB)) {
                        sUID = sUID.replace(prefixList.get(j).PREFIX_DB, "");
                        break;
                    }
                }
            }
        }
        return sUID;
    }
    
    public static String convertPrefixVNToEN(String sUID, boolean isEnterprise) {
        if(!"".equals(sUID)) {
            String sListPrefixJson = LoadParamSystem.getParamStart(Definitions.CONFIG_KEY_PREFIX_ENTERPRISE_JSON);
            if(isEnterprise == false){
                sListPrefixJson = LoadParamSystem.getParamStart(Definitions.CONFIG_KEY_PREFIX_PERSONAL_JSON);
            }
            PREFIX_UUID[] prefixArray = new Gson().fromJson(sListPrefixJson, PREFIX_UUID[].class);
            List<PREFIX_UUID> prefixList = Arrays.asList(prefixArray);
            for (PREFIX_UUID prefixList1 : prefixList) {
                if((sUID.split(":")[0] + ":").equals(prefixList1.PREFIX)) {
                    sUID = prefixList1.PREFIX_DB + sUID.replace(prefixList1.PREFIX, "");
                }
            }
        }
        return sUID;
    }
    
    public static boolean checkPrefixEnterpriseVN(String sPrefix) {
        boolean isCheck = false;
        if(!"".equals(sPrefix)) {
            String sListPrefixJson = LoadParamSystem.getParamStart(Definitions.CONFIG_KEY_PREFIX_ENTERPRISE_JSON);
            PREFIX_UUID[] prefixArray = new Gson().fromJson(sListPrefixJson, PREFIX_UUID[].class);
            List<PREFIX_UUID> prefixList = Arrays.asList(prefixArray);
            for (PREFIX_UUID prefixList1 : prefixList) {
                if((sPrefix + ":").equals(prefixList1.PREFIX)) {
                    isCheck = true;
                    break;
                }
            }
        }
        return isCheck;
    }
    
    public static boolean checkPrefixPersonalVN(String sPrefix) {
        boolean isCheck = false;
        if(!"".equals(sPrefix)) {
            String sListPrefixJson = LoadParamSystem.getParamStart(Definitions.CONFIG_KEY_PREFIX_PERSONAL_JSON);
            PREFIX_UUID[] prefixArray = new Gson().fromJson(sListPrefixJson, PREFIX_UUID[].class);
            List<PREFIX_UUID> prefixList = Arrays.asList(prefixArray);
            for (PREFIX_UUID prefixList1 : prefixList) {
                if((sPrefix + ":").equals(prefixList1.PREFIX)) {
                    isCheck = true;
                    break;
                }
            }
        }
        return isCheck;
    }
    
    public static String swapPrefixEnterpriseToVN(String sENTERPRISE_ID) {
        if (!"".equals(sENTERPRISE_ID)) {
            if("1".equals(Definitions.CONFIG_LANGUAGE_TEMPLATE_UID_PREFIX)) {
                String sListPrefixJson = LoadParamSystem.getParamStart(Definitions.CONFIG_KEY_PREFIX_ENTERPRISE_JSON);
                PREFIX_UUID[] prefixArray = new Gson().fromJson(sListPrefixJson, PREFIX_UUID[].class);
                List<PREFIX_UUID> prefixList = Arrays.asList(prefixArray);
                for(int j=0; j<prefixList.size(); j++) {
                    if((sENTERPRISE_ID.split(":")[0] + ":").equals(prefixList.get(j).PREFIX_DB)) {
                        sENTERPRISE_ID = sENTERPRISE_ID.replace(prefixList.get(j).PREFIX_DB, prefixList.get(j).PREFIX);
                    }
                }
            }
        }
        return sENTERPRISE_ID;
    }
    
    public static String swapPrefixPersonalToVN(String sPERSONAL_ID) {
        if (!"".equals(sPERSONAL_ID)) {
            if("1".equals(Definitions.CONFIG_LANGUAGE_TEMPLATE_UID_PREFIX)) {
                String sListPrefixJson = LoadParamSystem.getParamStart(Definitions.CONFIG_KEY_PREFIX_PERSONAL_JSON);
                PREFIX_UUID[] prefixArray = new Gson().fromJson(sListPrefixJson, PREFIX_UUID[].class);
                List<PREFIX_UUID> prefixList = Arrays.asList(prefixArray);
                for(int j=0; j<prefixList.size(); j++) {
                    if((sPERSONAL_ID.split(":")[0] + ":").equals(prefixList.get(j).PREFIX_DB)) {
                        sPERSONAL_ID = sPERSONAL_ID.replace(prefixList.get(j).PREFIX_DB, prefixList.get(j).PREFIX);
                    }
                }
            }
        }
        return sPERSONAL_ID;
    }
    
    public static String swapPrefixPersonalLanguage(String sUID, String sLanguage, String[] prefixRemark) {
        prefixRemark[0] = "";
        if (!"".equals(sUID)) {
            String sListPrefixJson = LoadParamSystem.getParamStart(Definitions.CONFIG_KEY_PREFIX_PERSONAL_JSON);
            PREFIX_UUID[] prefixArray = new Gson().fromJson(sListPrefixJson, PREFIX_UUID[].class);
            List<PREFIX_UUID> prefixList = Arrays.asList(prefixArray);
            if("1".equals(sLanguage)) {
                for(int j=0; j<prefixList.size(); j++) {
                    if((sUID.split(":")[0] + ":").equals(prefixList.get(j).PREFIX_DB)) {
                        sUID = sUID.replace(prefixList.get(j).PREFIX_DB, prefixList.get(j).PREFIX);
                        prefixRemark[0] = prefixList.get(j).REMARK_VN;
                        break;
                    }
                }
            } else {
                for(int j=0; j<prefixList.size(); j++) {
                    if((sUID.split(":")[0] + ":").equals(prefixList.get(j).PREFIX_DB)) {
                        prefixRemark[0] = prefixList.get(j).REMARK_EN;
                        break;
                    }
                }
            }
        }
        return sUID;
    }
    
    public static String swapPrefixEnterpriseLanguage(String sUID, String sLanguage, String[] prefixRemark) {
        prefixRemark[1] = "";
        if (!"".equals(sUID)) {
            String sListPrefixJson = LoadParamSystem.getParamStart(Definitions.CONFIG_KEY_PREFIX_ENTERPRISE_JSON);
            PREFIX_UUID[] prefixArray = new Gson().fromJson(sListPrefixJson, PREFIX_UUID[].class);
            List<PREFIX_UUID> prefixList = Arrays.asList(prefixArray);
            if("1".equals(sLanguage)) {
                for(int j=0; j<prefixList.size(); j++) {
                    if((sUID.split(":")[0] + ":").equals(prefixList.get(j).PREFIX_DB)) {
                        sUID = sUID.replace(prefixList.get(j).PREFIX_DB, prefixList.get(j).PREFIX);
                        prefixRemark[1] = prefixList.get(j).REMARK_VN;
                        break;
                    }
                }
            } else {
                for(int j=0; j<prefixList.size(); j++) {
                    if((sUID.split(":")[0] + ":").equals(prefixList.get(j).PREFIX_DB)) {
                        prefixRemark[1] = prefixList.get(j).REMARK_EN;
                        break;
                    }
                }
            }
        }
        return sUID;
    }
    
    public static String tooltipPrefixUIDLanguage(String sUID, String sLanguage) {
        if (!"".equals(sUID)) {
            if("1".equals(sLanguage)) {
                String sListPrefixJson = LoadParamSystem.getParamStart(Definitions.CONFIG_KEY_PREFIX_PERSONAL_JSON);
                PREFIX_UUID[] prefixArray = new Gson().fromJson(sListPrefixJson, PREFIX_UUID[].class);
                List<PREFIX_UUID> prefixList = Arrays.asList(prefixArray);
                for(int j=0; j<prefixList.size(); j++) {
                    if((sUID.split(":")[0] + ":").equals(prefixList.get(j).PREFIX_DB)) {
                        sUID = sUID.replace(prefixList.get(j).PREFIX_DB, prefixList.get(j).PREFIX);
                        break;
                    }
                }
            } else {
                
            }
        }
        return sUID;
    }
    
    public static boolean isSamedPrefixUIDOld(String sUID, String sPrefixUID, String sValeUIDOld) {
        boolean isSamed = true;
        if (!"".equals(sUID)) {
            String sPrefixNew = sUID.split(":")[0].trim();
            String sValueUIDNew = sUID.split(":")[1].trim();
            if(sPrefixNew.equals(sPrefixUID)) {
                if(!sValueUIDNew.equals(sValeUIDOld)){
                    isSamed = false;
                }
            }
        }
        return isSamed;
    }
    public static void addComponentOURSSP(AgreementDetails agreementDetails, int serverOUCount, String storeOU) {
        if(serverOUCount > 0) {
            if(!"".equals(storeOU)) {
                storeOU = storeOU.substring(0, storeOU.lastIndexOf("###"));
                String[] storeOUSplit = storeOU.split("###");
                if(storeOUSplit.length > 0) {
                    if(serverOUCount == 1){
                        agreementDetails.setOrganizationUnit(storeOUSplit[0]);
                    }
                    if(serverOUCount == 2) {
                        agreementDetails.setOrganizationUnit(storeOUSplit[0]);
                        if(storeOUSplit.length > 1){
                            agreementDetails.setOrganizationUnit2(storeOUSplit[1]);
                        }
                    }
                    if(serverOUCount == 3) {
                        agreementDetails.setOrganizationUnit(storeOUSplit[0]);
                        if(storeOUSplit.length > 1){
                            agreementDetails.setOrganizationUnit2(storeOUSplit[1]);
                        }
                        if(storeOUSplit.length > 2){
                            agreementDetails.setOrganizationUnit3(storeOUSplit[2]);
                        }
                    }
                    if(serverOUCount == 4) {
                        agreementDetails.setOrganizationUnit(storeOUSplit[0]);
                        if(storeOUSplit.length > 1){
                            agreementDetails.setOrganizationUnit2(storeOUSplit[1]);
                        }
                        if(storeOUSplit.length > 2){
                            agreementDetails.setOrganizationUnit3(storeOUSplit[2]);
                        }
                        if(storeOUSplit.length > 3){
                            agreementDetails.setOrganizationUnit4(storeOUSplit[3]);
                        }
                    }
                }
            }
        }
    }
    
}
