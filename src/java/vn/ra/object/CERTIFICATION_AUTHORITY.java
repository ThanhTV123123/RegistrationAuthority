/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.object;

import java.sql.Blob;

/**
 *
 * @author THANH-PC
 */
public class CERTIFICATION_AUTHORITY {

    public int ID;
    public boolean ENABLED;
    public String NAME;
    public String PROPERTIES;
    public String CA_URI;
    public String OCSP_URI;
    public boolean OCSP_PRIORITY_ENABLED;
    public String SHORT_CODE;
    public String CERTIFICATE;
    public String CRL_URI;
    public Blob BLOB;
    public String REMARK_EN;
    public String REMARK;
    public byte[] CRL_BLOB;
    public String LAST_UPDATED_DT;
    public String NEXT_UPDATED_DT;
    public String ISSUER_SUBJECT;
    public String URI;
    public String CREATED_DT;
    public String CREATED_BY;
    public String MODIFIED_DT;
    public String MODIFIED_BY;
    public String CERTIFICATION_AUTHORITY_CORECA_SUBJECT;
    public String ENFORCE_UNIQUE_DN;
    // template
    public String TEMPLATE_PERSONAL_REGISTRATION_PAPER;
    public String TEMPLATE_ENTERPRISE_REGISTRATION_PAPER;
    public String TEMPLATE_CERTIFICATE_REVISION_PAPER;
    public String TEMPLATE_CERTIFICATE_REVOCATION_REISSUE_PAPER;
    public String TEMPLATE_DELIVERY_PAPER;
    public String TEMPLATE_LICENSE_CERTIFICATION;
    public String TEMPLATE_LICENSE_CERTIFICATION_DIGITAL;
    public String TEMPLATE_REPORT_DEBT_CONTROL;
    public String TEMPLATE_CONFIRMATION_PAPER;
    public String TEMPLATE_PERSONAL_ENTERPRISE_REGISTRATION_PAPER;
    public String TEMPLATE_PERSONAL_ENTERPRISE_RENEWAL_PAPER;
}
