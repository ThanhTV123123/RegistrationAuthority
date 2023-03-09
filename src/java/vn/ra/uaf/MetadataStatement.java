package vn.ra.uaf;

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;
import vn.ra.uaf.DisplayPNGCharacteristicsDescriptor;
import vn.ra.uaf.Extension;
import vn.ra.uaf.Version;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MetadataStatement {
	final static Logger logger = Logger.getLogger(MetadataStatement.class);
	
	
    private String aaid;
    private String assertionScheme;
    private int attachmentHint;
    private String[] attestationRootCertificates;
    private short[] attestationTypes;
    private short authenticationAlgorithm;
    private short authenticatorVersion;
    private String description;
    private String icon;
    private boolean isSecondFactorOnly;
    private short keyProtection;
    private boolean isKeyRestricted;
    private short matcherProtection;
    private short publicKeyAlgAndEncoding;
    private short tcDisplay;
    private String tcDisplayContentType;
    private DisplayPNGCharacteristicsDescriptor[] tcDisplayPNGCharacteristics;
    private Version[] upv;
    private VerificationMethodDescriptor[][] userVerificationDetails;

    private Extension[]  supportedExtensions;

    public static MetadataStatement fromJSON(String json){
        try {
        	ObjectMapper obj = new ObjectMapper();
            return obj.readValue(json, MetadataStatement.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }

    public String getAaid() {
        return this.aaid;
    }

    public String getAssertionScheme() {
        return this.assertionScheme;
    }

    public int getAttachmentHint() {
        return this.attachmentHint;
    }

    public String[] getAttestationRootCertificates() {
        return this.attestationRootCertificates;
    }

    public short[] getAttestationTypes() {
        return this.attestationTypes;
    }

    public short getAuthenticationAlgorithm() {
        return this.authenticationAlgorithm;
    }

    public short getAuthenticatorVersion() {
        return this.authenticatorVersion;
    }

    public String getDescription() {
        return this.description;
    }

    public String getIcon() {
        return this.icon;
    }

    public short getKeyProtection() {
        return this.keyProtection;
    }
    
    @JsonProperty(value="isKeyRestricted")     
    public boolean isKeyRestricted() {
		return this.isKeyRestricted;
	}

	public void setKeyRestricted(boolean isKeyRestricted) {
		this.isKeyRestricted = isKeyRestricted;
	}

	public short getMatcherProtection() {
        return this.matcherProtection;
    }

    public short getPublicKeyAlgAndEncoding() {
        return this.publicKeyAlgAndEncoding;
    }

    public short getTcDisplay() {
        return this.tcDisplay;
    }

    public String getTcDisplayContentType() {
        return this.tcDisplayContentType;
    }

    public DisplayPNGCharacteristicsDescriptor[] getTcDisplayPNGCharacteristics() {
        return this.tcDisplayPNGCharacteristics;
    }

    public Version[] getUpv() {
        return this.upv;
    }

    public VerificationMethodDescriptor[][] getUserVerificationDetails() {
        return this.userVerificationDetails;
    }
    
    @JsonProperty(value="isSecondFactorOnly")        
    public boolean isSecondFactorOnly() {
        return this.isSecondFactorOnly;
    }

    public void setAaid(String aaid) {
        this.aaid = aaid;
    }

    public void setAssertionScheme(String assertionScheme) {
        this.assertionScheme = assertionScheme;
    }

    public void setAttachmentHintTypes(int attachmentHint) {
        this.attachmentHint = attachmentHint;
    }

    public void setAttestationRootCertificates(String[] attestationRootCertificate) {
        this.attestationRootCertificates = attestationRootCertificate;
    }

    public void setAttestationType(short[] attestationTypes) {
        this.attestationTypes = attestationTypes;
    }

    public void setAuthenticationAlgorithm(short authenticationAlgorithm) {
        this.authenticationAlgorithm = authenticationAlgorithm;
    }

    public void setAuthenticatorVersion(short authenticatorVersion) {
        this.authenticatorVersion = authenticatorVersion;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setKeyProtection(short keyProtection) {
        this.keyProtection = keyProtection;
    }

    public void setMatcherProtection(short matcherProtection) {
        this.matcherProtection = matcherProtection;
    }

    public void setPublicKeyAlgAndEncoding(short publicKeyAlgAndEncoding) {
        this.publicKeyAlgAndEncoding = publicKeyAlgAndEncoding;
    }

    public void setSecondFactorOnly(boolean secondFactorOnly) {
        this.isSecondFactorOnly = secondFactorOnly;
    }

    public void setTcDisplay(short tcDisplay) {
        this.tcDisplay = tcDisplay;
    }

    public void setTcDisplayContentType(String tcDisplayContentType) {
        this.tcDisplayContentType = tcDisplayContentType;
    }

    public void setTcDisplayPNGCharacteristics(DisplayPNGCharacteristicsDescriptor[] tcDisplayPNGCharacteristics) {
        this.tcDisplayPNGCharacteristics = tcDisplayPNGCharacteristics;
    }

    public void setUpv(Version[] upv) {
        this.upv = upv;
    }

    public void setUserVerificationDetails(VerificationMethodDescriptor[][] userVerificationDetails) {
        this.userVerificationDetails = userVerificationDetails;
    }

    public Extension[] getSupportedExtensions() {
        return supportedExtensions;
    }

    public void setSupportedExtensions(Extension[] supportedExtensions) {
        this.supportedExtensions = supportedExtensions;
    }

    public String toJSON() {
        try {
			return (new ObjectMapper().writeValueAsString(this));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
			logger.error(e.getMessage());
		}
        return null;
    }

    public String toString() {
        return "MetadataStatement [aaid=" + this.aaid + ", description=" + this.description + ", authenticatorVersion=" + this.authenticatorVersion + ", upv=" + Arrays.toString(this.upv) + ", assertionScheme=" + this.assertionScheme + ", authenticationAlgorithm=" + this.authenticationAlgorithm + ", publicKeyAlgAndEncoding=" + this.publicKeyAlgAndEncoding + ", attestationTypes=" + Arrays.toString(this.attestationTypes) + ", userVerificationDetails=" + Arrays.toString(this.userVerificationDetails) + ", KeyProtection=" + this.keyProtection + ", matcherProtection=" + this.matcherProtection + ", attachmentHint=" + this.attachmentHint + ", isSecondFactorOnly=" + this.isSecondFactorOnly + ", tcDisplay=" + this.tcDisplay + ", tcDisplayContentType=" + this.tcDisplayContentType + ", tcDisplayPNGCharacteristics=" + Arrays.toString(this.tcDisplayPNGCharacteristics) + ", attestationRootCertificates=" + Arrays.toString(this.attestationRootCertificates) + ", icon=" + this.icon + ", supportedExtensions=" + Arrays.toString(this.supportedExtensions) + "]";
    }
}
