/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import org.apache.log4j.Logger;
import vn.ra.object.ATTRIBUTE_DATA;
import vn.ra.object.ATTRIBUTE_VALUES;
import vn.ra.object.FormFactorSNInfo;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.MENULINK_TOKEN;
import vn.ra.object.MenuLinkAPI;
import vn.ra.object.PUSH_TOKEN;
import vn.ra.object.PushNotificationAPI;
import vn.ra.object.TOKEN;
import vn.ra.object.UserInfo;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.ws.RAServiceReq;
import vn.ra.ws.RAServiceResp;

/**
 *
 * @author USER
 */
public class FormFactorTokenThread implements Runnable {

    private static final Logger log = Logger.getLogger(FormFactorTokenThread.class);
    private final RAServiceReq raServiceReq;
    private final RAServiceResp raServiceResp;
    private final String pApproveCAUser;
    private final String sTokenIdList;
    private final int System_Log_ID;
    private final String System_Log_BillCode;
    private final String sTOKEN_SN_LOG;
    ConnectDatabase db = new ConnectDatabase();
    ObjectMapper oMapperParse;

    public FormFactorTokenThread(RAServiceReq raServiceReq, RAServiceResp raServiceResp, String pApproveCAUser, String sTokenIdList,
            int System_Log_ID, String System_Log_BillCode, String sTOKEN_SN_LOG) {
        this.raServiceReq = raServiceReq;
        this.pApproveCAUser = pApproveCAUser;
        this.raServiceResp = raServiceResp;
        this.sTokenIdList = sTokenIdList;
        this.System_Log_ID = System_Log_ID;
        this.System_Log_BillCode = System_Log_BillCode;
        this.sTOKEN_SN_LOG = sTOKEN_SN_LOG;
    }

    public void run() {
        try {
//            Thread.sleep(60000);
            //<editor-fold defaultstate="collapsed" desc="### GET PROVINCE and FULLNAME">
            String raFullname = "";
            UserInfo[][] userDetail = new UserInfo[1][];
            db.S_BO_API_USER_DETAIL(pApproveCAUser, raServiceReq.language, userDetail);
            if (userDetail[0].length > 0) {
                raFullname = EscapeUtils.CheckTextNull(userDetail[0][0].fullName);
            }
            //</editor-fold>

            String sTokenSN = Definitions.CONFIG_TOKEN_SN_BULK_TOKEN;
            ATTRIBUTE_VALUES valueATTR;
            valueATTR = new ATTRIBUTE_VALUES();
            String sTokenAttrTypeCode = "";
            valueATTR.setTokenSn(sTokenSN);
            valueATTR.setTokenIdOfBundleList(sTokenIdList);
            int sTokenAttrTypeID = 0;
            if (EscapeUtils.CheckTextNull(raServiceReq.queueTypeCode).equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_LOCK)) {
                sTokenAttrTypeCode = Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_LOCK;
                sTokenAttrTypeID = Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_LOCK;
                valueATTR.setActionReason(raServiceReq.remark);
            } else if (EscapeUtils.CheckTextNull(raServiceReq.queueTypeCode).equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_UNLOCK)) {
                sTokenAttrTypeCode = Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_UNLOCK;
                sTokenAttrTypeID = Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_UNLOCK;
            } else if (EscapeUtils.CheckTextNull(raServiceReq.queueTypeCode).equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_RESET_ACTIVATION_REMAINING_COUNTER)) {
                sTokenAttrTypeCode = Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_RESET_ACTIVATION_REMAINING_COUNTER;
                sTokenAttrTypeID = Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_RESET_ACTIVATION_REMAINING_COUNTER;
            } else if (EscapeUtils.CheckTextNull(raServiceReq.queueTypeCode).equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_PUSH_NOTFICATION)) {
                oMapperParse = new ObjectMapper();
                sTokenAttrTypeCode = Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_PUSH_NOTFICATION;
                sTokenAttrTypeID = Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PUSH_NOTFICATION;
                PushNotificationAPI itemParse = oMapperParse.readValue(raServiceReq.pushNotification, PushNotificationAPI.class);
                PUSH_TOKEN itemParsePush = new PUSH_TOKEN();
                itemParsePush.PUSH_NOTICE_CONTENT = EscapeUtils.CheckTextNull(itemParse.notificationContent);
                itemParsePush.PUSH_NOTICE_URL = EscapeUtils.CheckTextNull(itemParse.popupUrl);
                itemParsePush.PUSH_NOTICE_TEXT_COLOR = EscapeUtils.CheckTextNull(itemParse.textColor);
                itemParsePush.PUSH_NOTICE_BGR_COLOR = EscapeUtils.CheckTextNull(itemParse.backgroundColor);
                ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                dataATTR.setSticker(itemParsePush);
                valueATTR.setAttributeData(dataATTR);
            } else if (EscapeUtils.CheckTextNull(raServiceReq.queueTypeCode).equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_MENU_LINK)) {
                oMapperParse = new ObjectMapper();
                sTokenAttrTypeCode = Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_MENU_LINK;
                sTokenAttrTypeID = Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_MENU_LINK;
                MenuLinkAPI itemParse = oMapperParse.readValue(raServiceReq.menuLink, MenuLinkAPI.class);
                MENULINK_TOKEN itemParseMenu = new MENULINK_TOKEN();
                itemParseMenu.MENU_LINK_NAME = EscapeUtils.CheckTextNull(itemParse.menuName);
                itemParseMenu.MENU_LINK_URL = EscapeUtils.CheckTextNull(itemParse.menuUrl);
                ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                dataATTR.setMenulink(itemParseMenu);
                valueATTR.setAttributeData(dataATTR);
            }
            valueATTR.setTypeName(sTokenAttrTypeCode);
            valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_PENDING);
            valueATTR.setCreateUser(raFullname + " (" + pApproveCAUser + ")");
            valueATTR.setCreateDt(new Date());
            int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_PENDING);
            if (EscapeUtils.CheckTextNull(raServiceReq.queueTypeCode).equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_LOCK)) {
                //<editor-fold defaultstate="collapsed" desc="### LOCK">
                String[] pRESPONSE_CODE_NAME = new String[1];
                if (raServiceReq.approveEnabled == true) {
                    valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                    valueATTR.setApproveUser(raFullname + " (" + pApproveCAUser + ")");
                    valueATTR.setApproveDt(new Date());
                    intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_COMMITED);
                }
                int[] pTOKEN_ATTR_ID = new int[1];
                db.S_BO_API_TOKEN_ATTR_INSERT(sTokenSN, sTokenAttrTypeID, intTOKEN_ATTR_STATE,
                        CommonFunction.GenJSONTokenATTR(valueATTR), pApproveCAUser, pRESPONSE_CODE_NAME, pTOKEN_ATTR_ID);
                if ("0".equals(pRESPONSE_CODE_NAME[0])) {
                    raServiceResp.formFactorTokenID = pTOKEN_ATTR_ID[0];
//                            String sBillCode = System_Log_BillCode[0];
//                            int sizeBill = sBillCode.split("-").length;
//                            System_Log_BillCode[0] = sBillCode.replace(sBillCode.split("-")[sizeBill-1], "") + pTOKEN_ATTR_ID[0];
                    if (raServiceReq.approveEnabled == true) {
                        int[] pTOKEN_ATTR_ID_INNER = new int[1];
                        String[] pRESPONSE_CODE_NAME_INNER = new String[1];
                        for (FormFactorSNInfo itemSN : raServiceReq.formFactorSNInfo) {
                            String tokenSNInner = EscapeUtils.CheckTextNull(itemSN.formFactorSN);
                            if (!"".equals(tokenSNInner)) {
                                valueATTR.setTokenIdOfBundleList(null);
                                valueATTR.setTokenSn(tokenSNInner);
                                db.S_BO_API_TOKEN_ATTR_INSERT(tokenSNInner, sTokenAttrTypeID, Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED),
                                        CommonFunction.GenJSONTokenATTR(valueATTR), pApproveCAUser, pRESPONSE_CODE_NAME_INNER, pTOKEN_ATTR_ID_INNER);
                            }
                        }
                    }
                    TOKEN[][] rsTokenResult = new TOKEN[1][];
                    db.S_BO_TOKEN_ATTR_DETAIL(String.valueOf(pTOKEN_ATTR_ID[0]), String.valueOf(raServiceReq.language), rsTokenResult);
                    if (rsTokenResult[0].length > 0) {
                        raServiceResp.queueStateCode = rsTokenResult[0][0].TOKEN_ATTR_STATE_NAME;
                        raServiceResp.queueStateName = rsTokenResult[0][0].TOKEN_ATTR_STATE_DESC;
                    }
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                } else {
                    raServiceResp.responseCode = Integer.parseInt(pRESPONSE_CODE_NAME[0]);
                }
                //</editor-fold>
            } else if (EscapeUtils.CheckTextNull(raServiceReq.queueTypeCode).equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_UNLOCK)
                || EscapeUtils.CheckTextNull(raServiceReq.queueTypeCode).equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_RESET_ACTIVATION_REMAINING_COUNTER)) {
                //<editor-fold defaultstate="collapsed" desc="### UNLOCK - RESET_ACTIVATION_REMAINING_COUNTER">
                String[] pRESPONSE_CODE_NAME = new String[1];
                int[] pTOKEN_ATTR_ID = new int[1];
                if (raServiceReq.approveEnabled == true) {
                    valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                    valueATTR.setApproveUser(raFullname + " (" + pApproveCAUser + ")");
                    valueATTR.setApproveDt(new Date());
                    if (EscapeUtils.CheckTextNull(raServiceReq.queueTypeCode).equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_UNLOCK)) {
                        intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_COMMITED);
                    } else {
                        intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_COMMITED);
                    }
                }
                db.S_BO_API_TOKEN_ATTR_INSERT(sTokenSN, sTokenAttrTypeID, intTOKEN_ATTR_STATE,
                        CommonFunction.GenJSONTokenATTR(valueATTR), pApproveCAUser, pRESPONSE_CODE_NAME, pTOKEN_ATTR_ID);
                if ("0".equals(pRESPONSE_CODE_NAME[0])) {
                    raServiceResp.formFactorTokenID = pTOKEN_ATTR_ID[0];
//                            String sBillCode = System_Log_BillCode[0];
//                            int sizeBill = sBillCode.split("-").length;
//                            System_Log_BillCode[0] = sBillCode.replace(sBillCode.split("-")[sizeBill-1], "") + pTOKEN_ATTR_ID[0];
                    if (raServiceReq.approveEnabled == true) {
                        if (EscapeUtils.CheckTextNull(raServiceReq.queueTypeCode).equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_RESET_ACTIVATION_REMAINING_COUNTER)) {
                            String sACTIVATION_MAX_COUNTER = "5";
                            GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
                            db.S_BO_GENERAL_POLICY_LIST(String.valueOf(raServiceReq.language), rsPolicy);
                            if (rsPolicy[0].length > 0) {
                                for (GENERAL_POLICY rsPolicy1 : rsPolicy[0]) {
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DEFAULT_ACTIVATION_MAX_COUNTER)) {
                                        sACTIVATION_MAX_COUNTER = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                        break;
                                    }
                                }
                            }
                            for (String split : sTokenIdList.split(",")) {
                                if (!"".equals(split)) {
                                    db.S_BO_TOKEN_UPDATE(Integer.parseInt(split), "", "", "", "", sACTIVATION_MAX_COUNTER, pApproveCAUser);
                                }
                            }
                        } else {
                            int[] pTOKEN_ATTR_ID_INNER = new int[1];
                            String[] pRESPONSE_CODE_NAME_INNER = new String[1];
                            for (FormFactorSNInfo itemSN : raServiceReq.formFactorSNInfo) {
                                String tokenSNInner = EscapeUtils.CheckTextNull(itemSN.formFactorSN);
                                if (!"".equals(tokenSNInner)) {
                                    valueATTR.setTokenIdOfBundleList(null);
                                    valueATTR.setTokenSn(tokenSNInner);
                                    db.S_BO_API_TOKEN_ATTR_INSERT(tokenSNInner, sTokenAttrTypeID, Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED),
                                            CommonFunction.GenJSONTokenATTR(valueATTR), pApproveCAUser, pRESPONSE_CODE_NAME_INNER, pTOKEN_ATTR_ID_INNER);
                                }
                            }
                        }
                    }
                    TOKEN[][] rsTokenResult = new TOKEN[1][];
                    db.S_BO_TOKEN_ATTR_DETAIL(String.valueOf(pTOKEN_ATTR_ID[0]), String.valueOf(raServiceReq.language), rsTokenResult);
                    if (rsTokenResult[0].length > 0) {
                        raServiceResp.queueStateCode = rsTokenResult[0][0].TOKEN_ATTR_STATE_NAME;
                        raServiceResp.queueStateName = rsTokenResult[0][0].TOKEN_ATTR_STATE_DESC;
                    }
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                } else {
                    raServiceResp.responseCode = Integer.parseInt(pRESPONSE_CODE_NAME[0]);
                }
                //</editor-fold>
            } else if (EscapeUtils.CheckTextNull(raServiceReq.queueTypeCode).equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_PUSH_NOTFICATION)) {
                //<editor-fold defaultstate="collapsed" desc="### PUSH_NOTFICATION">
                String[] pRESPONSE_CODE_NAME = new String[1];
                int[] pTOKEN_ATTR_ID = new int[1];
                if (raServiceReq.approveEnabled == true) {
                    valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                    valueATTR.setApproveUser(raFullname + " (" + pApproveCAUser + ")");
                    valueATTR.setApproveDt(new Date());
                    intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_COMMITED);
                }
                db.S_BO_API_TOKEN_ATTR_INSERT(sTokenSN, sTokenAttrTypeID, intTOKEN_ATTR_STATE,
                        CommonFunction.GenJSONTokenATTR(valueATTR), pApproveCAUser, pRESPONSE_CODE_NAME, pTOKEN_ATTR_ID);
                if ("0".equals(pRESPONSE_CODE_NAME[0])) {
                    raServiceResp.formFactorTokenID = pTOKEN_ATTR_ID[0];
//                            String sBillCode = System_Log_BillCode[0];
//                            int sizeBill = sBillCode.split("-").length;
//                            System_Log_BillCode[0] = sBillCode.replace(sBillCode.split("-")[sizeBill-1], "") + pTOKEN_ATTR_ID[0];
                    if (raServiceReq.approveEnabled == true) {
                        oMapperParse = new ObjectMapper();
                        PushNotificationAPI itemParse = oMapperParse.readValue(raServiceReq.pushNotification, PushNotificationAPI.class);
                        PUSH_TOKEN itemParsePush = new PUSH_TOKEN();
                        itemParsePush.PUSH_NOTICE_CONTENT = EscapeUtils.CheckTextNull(itemParse.notificationContent);
                        itemParsePush.PUSH_NOTICE_URL = EscapeUtils.CheckTextNull(itemParse.popupUrl);
                        itemParsePush.PUSH_NOTICE_TEXT_COLOR = EscapeUtils.CheckTextNull(itemParse.textColor);
                        itemParsePush.PUSH_NOTICE_BGR_COLOR = EscapeUtils.CheckTextNull(itemParse.backgroundColor);
                        for (String split : sTokenIdList.split(",")) {
                            if (!"".equals(split)) {
                                db.S_BO_TOKEN_UPDATE(Integer.parseInt(split), "", "", oMapperParse.writeValueAsString(itemParsePush), "", "", pApproveCAUser);
                            }
                        }
                    }
                    TOKEN[][] rsTokenResult = new TOKEN[1][];
                    db.S_BO_TOKEN_ATTR_DETAIL(String.valueOf(pTOKEN_ATTR_ID[0]), String.valueOf(raServiceReq.language), rsTokenResult);
                    if (rsTokenResult[0].length > 0) {
                        raServiceResp.queueStateCode = rsTokenResult[0][0].TOKEN_ATTR_STATE_NAME;
                        raServiceResp.queueStateName = rsTokenResult[0][0].TOKEN_ATTR_STATE_DESC;
                    }
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                } else {
                    raServiceResp.responseCode = Integer.parseInt(pRESPONSE_CODE_NAME[0]);
                }
                //</editor-fold>
            } else if (EscapeUtils.CheckTextNull(raServiceReq.queueTypeCode).equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_MENU_LINK)) {
                //<editor-fold defaultstate="collapsed" desc="### MENU_LINK">
                String[] pRESPONSE_CODE_NAME = new String[1];
                int[] pTOKEN_ATTR_ID = new int[1];
                if (raServiceReq.approveEnabled == true) {
                    valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                    valueATTR.setApproveUser(raFullname + " (" + pApproveCAUser + ")");
                    valueATTR.setApproveDt(new Date());
                    intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_COMMITED);
                }
                db.S_BO_API_TOKEN_ATTR_INSERT(sTokenSN, sTokenAttrTypeID, intTOKEN_ATTR_STATE,
                        CommonFunction.GenJSONTokenATTR(valueATTR), pApproveCAUser, pRESPONSE_CODE_NAME, pTOKEN_ATTR_ID);
                if ("0".equals(pRESPONSE_CODE_NAME[0])) {
                    raServiceResp.formFactorTokenID = pTOKEN_ATTR_ID[0];
//                            String sBillCode = System_Log_BillCode[0];
//                            int sizeBill = sBillCode.split("-").length;
//                            System_Log_BillCode[0] = sBillCode.replace(sBillCode.split("-")[sizeBill-1], "") + pTOKEN_ATTR_ID[0];
                    if (raServiceReq.approveEnabled == true) {
                        oMapperParse = new ObjectMapper();
                        MenuLinkAPI itemParse = oMapperParse.readValue(raServiceReq.menuLink, MenuLinkAPI.class);
                        MENULINK_TOKEN itemParseMenu = new MENULINK_TOKEN();
                        itemParseMenu.MENU_LINK_NAME = EscapeUtils.CheckTextNull(itemParse.menuName);
                        itemParseMenu.MENU_LINK_URL = EscapeUtils.CheckTextNull(itemParse.menuUrl);
                        for (String split : sTokenIdList.split(",")) {
                            if (!"".equals(split)) {
                                db.S_BO_TOKEN_UPDATE(Integer.parseInt(split), "", oMapperParse.writeValueAsString(itemParseMenu), "", "", "", pApproveCAUser);
                            }
                        }
                    }
                    TOKEN[][] rsTokenResult = new TOKEN[1][];
                    db.S_BO_TOKEN_ATTR_DETAIL(String.valueOf(pTOKEN_ATTR_ID[0]), String.valueOf(raServiceReq.language), rsTokenResult);
                    if (rsTokenResult[0].length > 0) {
                        raServiceResp.queueStateCode = rsTokenResult[0][0].TOKEN_ATTR_STATE_NAME;
                        raServiceResp.queueStateName = rsTokenResult[0][0].TOKEN_ATTR_STATE_DESC;
                    }
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                } else {
                    raServiceResp.responseCode = Integer.parseInt(pRESPONSE_CODE_NAME[0]);
                }
                //</editor-fold>
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_TOKEN_TYPE_INVALID;
            }
            System.out.println("raServiceResp: " + oMapperParse.writeValueAsString(raServiceResp));
        } catch (Exception ex) {
            CommonFunction.LogExceptionServlet(log, ex.getMessage(), ex);
        } finally {
            try {
                raServiceResp.requestExecutionProcess = Definitions.CONFIG_API_RESULT_PROCESS_COMPLETE;
                if (System_Log_ID != 0 && !"".equals(System_Log_BillCode)) {
                    oMapperParse = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID, String.valueOf(raServiceResp.responseCode),
                        oMapperParse.writeValueAsString(raServiceResp), sTOKEN_SN_LOG, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
    }
}
