package vn.ra.uaf;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BiometricAccuracyDescriptor {
	final static Logger logger = Logger.getLogger(BiometricAccuracyDescriptor.class);
	
    private Double EER;
    private Double FAAR;
    private Double FAR;
    private Double FRR;
    private Short blockSlowdown;
    private Short maxReferenceDataSets;
    private Short maxRetries;

    public static BiometricAccuracyDescriptor fromJSON(String json) {
        try {
        	ObjectMapper obj = new ObjectMapper();
            return obj.readValue(json, BiometricAccuracyDescriptor.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }

    public short getBlockSlowdown() {
        return this.blockSlowdown.shortValue();
    }

    public double getEER() {
        return this.EER.doubleValue();
    }

    public double getFAAR() {
        return this.FAAR.doubleValue();
    }

    public double getFAR() {
        return this.FAR.doubleValue();
    }

    public double getFRR() {
        return this.FRR.doubleValue();
    }

    public short getMaxReferenceDataSets() {
        return this.maxReferenceDataSets.shortValue();
    }

    public short getMaxRetries() {
        return this.maxRetries.shortValue();
    }

    public void setBlockSlowdown(short blockSlowdown) {
        this.blockSlowdown = Short.valueOf(blockSlowdown);
    }

    public void setEER(double eER) {
        this.EER = Double.valueOf(eER);
    }

    public void setFAAR(double fAAR) {
        this.FAAR = Double.valueOf(fAAR);
    }

    public void setFAR(double fAR) {
        this.FAR = Double.valueOf(fAR);
    }

    public void setFRR(double fRR) {
        this.FRR = Double.valueOf(fRR);
    }

    public void setMaxReferenceDataSets(short maxReferenceDataSets) {
        this.maxReferenceDataSets = Short.valueOf(maxReferenceDataSets);
    }

    public void setMaxRetries(short maxRetries) {
        this.maxRetries = Short.valueOf(maxRetries);
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
        return "BiometricAccuracyDescriptor [FAR=" + this.FAR + ", FRR=" + this.FRR + ", EER=" + this.EER + ", maxReferenceDataSets=" + this.maxReferenceDataSets + ", maxRetries=" + this.maxRetries + ", blockSlowdown=" + this.blockSlowdown + "]";
    }
}
