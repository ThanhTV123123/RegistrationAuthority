/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.rssp.restful;

import java.util.List;

/**
 *
 * @author USER
 */
public class RsspResponse {
    public String authorizationPhone;
    public String authorizationEmail;
    public Integer temporaryLockTime;
    public String version;
    public String name;
    public String description;
    public String logo;
    public String[] languages;
    public String[] authTypes;
    public String[] methods;
    public Integer error;
    public String errorDescription;
    public String billCode;
    public String accessToken;
    public String refreshToken;
    public Integer expiresIn;
    public String username;
    public List<Cert> certs;
    public Cert cert;
    public Integer[] authorizeMethod;
    public Integer numSignatures;
    public String authMode;
    public Integer SCAL;
    public String language;
    public String SAD;
    public Integer remainingCounter;
    public String[] signatures;
    public Owners[] owners;
    public String sharedMode;
    public String createdRP;
    public String[] authModeSupported;
    public String contractExpirationDt;
    public Integer remainingSigningCounter;
    public OwnerInfo ownerInfo;
    public String[] signaturePolicies;
    public String[] servicePolicies;
    public String[] operationModes;
    public SignatureFormats signatureFormats;
    public String rpRequestID;
    public String requestID;
    public Boolean rememberMe;
    public String profile;
    public Integer tempLockoutDuration;
    public String authorizeToken;
    public String twoFactorMethod;
    public Integer tempLockDuration;
    public Integer multisign;
    public String[] authModes;
    public String contractExpirationDate;
    public Boolean defaultPassphrase;
    public String[] documentWithSignature;
    public String[] signatureObject;
}
