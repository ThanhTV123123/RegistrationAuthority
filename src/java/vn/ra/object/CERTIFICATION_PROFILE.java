/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.object;

/**
 *
 * @author THANH-PC
 */
public class CERTIFICATION_PROFILE {

    public int ID;
    public boolean ENABLED;
//    public boolean ISSUE_ENABLED;
    public String NAME;
    public String PROPERTIES;
    public int DURATION;
    public int CERTIFICATION_PURPOSE_ID;
    public String CERTIFICATION_PURPOSE_NAME;
    public String CERTIFICATION_PURPOSE_DESC;
    public int CERTIFICATION_ALGORITHM_ID;
    public int CERTIFICATION_AUTHORITY_ID;
    public String CERTIFICATION_AUTHORITY_DESC;
    public String CERTIFICATION_AUTHORITY_NAME;
    public int DURATION_FREE;
    public int AMOUNT;
    public int TOKEN_AMOUNT;
    public int RENEWAL_AMOUNT;
    public int CHANGE_AMOUNT;
    public int REISSUE_AMOUNT;
    public int GOVERNMENT_AMOUNT;
    public String ENTITY_EJBCA;
    public String REMARK_EN;
    public String REMARK;
    public String CREATED_DT;
    public String CREATED_BY;
    public String MODIFIED_DT;
    public String MODIFIED_BY;
    public String AUTO_ASYNC;
    public String ONLY_RENEWAL;
    public String ONLY_ISSUE;
    public boolean UNDISPLAY_RENEWAL_ENABLED;
}
