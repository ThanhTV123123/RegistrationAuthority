/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.rssp.restful;

/**
 *
 * @author USER
 */
public class Cert {

    public String status;
    public String[] certificates;
    public String credentialID;
    public String issuerDN;
    public String serialNumber;
    public String thumbprint;
    public String subjectDN;
    public String validFrom;
    public String validTo;
    public String purpose;
    public String multisign;
    public Integer numSignatures;
    public Integer remainingSigningCounter;
    public Integer version;
    public String authorizationEmail;
    public String authorizationPhone;
    public CertificateProfile certificateProfile;
}
