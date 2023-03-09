package vn.ra.uaf;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VerificationMethodDescriptor {
	final static Logger logger = Logger.getLogger(VerificationMethodDescriptor.class);
	
    private BiometricAccuracyDescriptor baDesc;
    private CodeAccuracyDescriptor caDesc;
    private PatternAccuracyDescriptor paDesc;
    private int userVerification;

    public static VerificationMethodDescriptor fromJSON(String json) {
        try {
            return new ObjectMapper().readValue(json, VerificationMethodDescriptor.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }
    
    public BiometricAccuracyDescriptor getBaDesc() {
        return this.baDesc;
    }

    public CodeAccuracyDescriptor getCaDesc() {
        return this.caDesc;
    }

    public PatternAccuracyDescriptor getPaDesc() {
        return this.paDesc;
    }

    public int getUserVerification() {
        return this.userVerification;
    }

    public void setBaDesc(BiometricAccuracyDescriptor baDesc) {
        this.baDesc = baDesc;
    }

    public void setCaDesc(CodeAccuracyDescriptor caDesc) {
        this.caDesc = caDesc;
    }

    public void setPaDesc(PatternAccuracyDescriptor paDesc) {
        this.paDesc = paDesc;
    }

    public void setUserVerification(int userVerification) {
        this.userVerification = userVerification;
    }

    public String toJSON() {
        try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}
        return null;
    }

    public String toString() {
        return "VerificationMethodDescriptor [userVerification=" + this.userVerification + ", caDesc=" + this.caDesc + ", baDesc=" + this.baDesc + ", paDesc=" + this.paDesc + "]";
    }
}
